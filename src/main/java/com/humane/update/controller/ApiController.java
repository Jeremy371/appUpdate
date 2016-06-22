package com.humane.update.controller;

import com.humane.update.dto.AppUrlDto;
import com.humane.update.dto.AppVersionDto;
import com.humane.update.model.App;
import com.humane.update.model.AppVersion;
import com.humane.update.model.QApp;
import com.humane.update.model.QAppVersion;
import com.humane.update.repository.AppRepository;
import com.humane.update.repository.AppVersionRepository;
import com.humane.update.service.ApiService;
import com.mysema.query.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "api")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiController {

    @Value("${path.image.examinee:C:/api/apk}") String pathApk;
    private final ApiService apiService;
    private final AppRepository appRepository;
    private final AppVersionRepository appVersionRepository;

    @RequestMapping(value = "ver", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AppVersionDto ver(@RequestHeader(name = "packageName") String packageName) {
        return apiService.getLastVersion(packageName);
    }

    @RequestMapping(value = "url", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AppUrlDto> url(@RequestHeader(name = "packageName") String packageName) {
        return apiService.getUrlList(packageName);
    }

    @RequestMapping(value = "apk", method = RequestMethod.GET, produces = "application/apk")
    public ResponseEntity<InputStreamResource> downloadApk(@RequestHeader(name = "packageName") String packageName) {
        AppVersionDto appVersion = apiService.getLastVersion(packageName);

        File path = new File(pathApk);
        if (!path.exists()) path.mkdirs();
        File file = new File(path, packageName + "_" + appVersion.getVersionCode() + ".apk");
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        try {
            InputStreamResource isResource = new InputStreamResource(new FileInputStream(file));
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            String fileName = new String(FilenameUtils.getName(file.getAbsolutePath()).getBytes("UTF-8"), "iso-8859-1");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.setContentLength(fileSystemResource.contentLength());
            headers.setContentDispositionFormData("attachment", fileName);
            return new ResponseEntity<>(isResource, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "apk", method = RequestMethod.POST)
    public ResponseEntity uploadApk(@RequestParam("file") MultipartFile multipartFile) {

        File tempFile = null;
        try {
            // 1. 파일 임시 저장
            tempFile = File.createTempFile(multipartFile.getOriginalFilename(), "tmp");
            multipartFile.transferTo(tempFile);

            // 2. APK 분석
            ApkParser apkParser = new ApkParser(tempFile);
            ApkMeta apkMeta = apkParser.getApkMeta();

            String packageName = apkMeta.getPackageName();

            // 3. DB - App 조회
            App app = appRepository.findOne(new BooleanBuilder()
                    .and(QApp.app.packageName.eq(packageName))
            );
            if (app == null) {
                app = new App();
                app.setPackageName(packageName);
            }
            app = appRepository.save(app);

            // 4. DB - version 조회
            Long versionCode = apkMeta.getVersionCode();
            String versionName = apkMeta.getVersionName();

            AppVersion appVersion = appVersionRepository.findOne(new BooleanBuilder()
                    .and(QAppVersion.appVersion.app.packageName.eq(packageName))
                    .and(QAppVersion.appVersion.versionCode.eq(versionCode))
            );

            if (appVersion == null) {
                appVersion = new AppVersion();
                appVersion.setApp(app);
                appVersion.setVersionCode(versionCode);
                appVersion.setVersionName(versionName);
            }

            appVersionRepository.save(appVersion);

            // 5. 파일 저장
            File path = new File(pathApk);
            if (!path.exists()) path.mkdirs();

            File file = new File(path, packageName + "_" + appVersion.getVersionCode() + ".apk");
            multipartFile.transferTo(file);

            return ResponseEntity.ok(null);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        } finally {
            if (tempFile != null) tempFile.delete();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
