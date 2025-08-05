package sun.security.tools.policytool;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/URLPerm.class */
class URLPerm extends Perm {
    public URLPerm() {
        super("URLPermission", "java.net.URLPermission", new String[]{"<" + PolicyTool.getMessage("url") + ">"}, new String[]{"<" + PolicyTool.getMessage("method.list") + ">:<" + PolicyTool.getMessage("request.headers.list") + ">"});
    }
}
