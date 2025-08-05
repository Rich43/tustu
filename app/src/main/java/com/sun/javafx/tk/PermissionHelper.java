package com.sun.javafx.tk;

import java.awt.AWTPermission;
import java.security.Permission;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/PermissionHelper.class */
public class PermissionHelper {
    private static final Permission accessClipboardPermission = new AWTPermission("accessClipboard");

    public static Permission getAccessClipboardPermission() {
        return accessClipboardPermission;
    }

    private PermissionHelper() {
    }
}
