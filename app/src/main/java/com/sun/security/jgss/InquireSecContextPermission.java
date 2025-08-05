package com.sun.security.jgss;

import java.security.BasicPermission;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/security/jgss/InquireSecContextPermission.class */
public final class InquireSecContextPermission extends BasicPermission {
    private static final long serialVersionUID = -7131173349668647297L;

    public InquireSecContextPermission(String str) {
        super(str);
    }
}
