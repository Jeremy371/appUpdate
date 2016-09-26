package com.humane.util.spring.data;

import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;

public class JoinDescriptor {
    public final EntityPath path;
    public final JoinType type;

    private JoinDescriptor(EntityPath path, JoinType type) {
        this.path = path;
        this.type = type;
    }

    public static JoinDescriptor innerJoin(EntityPath path) {
        return new JoinDescriptor(path, JoinType.INNERJOIN);
    }

    public static JoinDescriptor join(EntityPath path) {
        return new JoinDescriptor(path, JoinType.JOIN);
    }

    public static JoinDescriptor leftJoin(EntityPath path) {
        return new JoinDescriptor(path, JoinType.LEFTJOIN);
    }

    public static JoinDescriptor rightJoin(EntityPath path) {
        return new JoinDescriptor(path, JoinType.RIGHTJOIN);
    }

    public static JoinDescriptor fullJoin(EntityPath path) {
        return new JoinDescriptor(path, JoinType.FULLJOIN);
    }
}