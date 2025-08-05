package sun.security.tools.policytool;

import sun.security.util.SecurityConstants;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/FilePerm.class */
class FilePerm extends Perm {
    public FilePerm() {
        super("FilePermission", ToolDialog.FILE_PERM_CLASS, new String[]{"<<ALL FILES>>"}, new String[]{"read", "write", SecurityConstants.FILE_DELETE_ACTION, SecurityConstants.FILE_EXECUTE_ACTION});
    }
}
