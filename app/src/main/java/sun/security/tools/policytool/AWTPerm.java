package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/AWTPerm.class */
class AWTPerm extends Perm {
    public AWTPerm() {
        super("AWTPermission", "java.awt.AWTPermission", new String[]{"accessClipboard", "accessEventQueue", "accessSystemTray", "createRobot", "fullScreenExclusive", "listenToAllAWTEvents", "readDisplayPixels", "replaceKeyboardFocusManager", "setAppletStub", "setWindowAlwaysOnTop", "showWindowWithoutWarningBanner", "toolkitModality", "watchMousePointer"}, null);
    }
}
