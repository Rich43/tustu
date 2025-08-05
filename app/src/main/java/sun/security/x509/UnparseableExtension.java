package sun.security.x509;

import sun.misc.HexDumpEncoder;

/* compiled from: CertificateExtensions.java */
/* loaded from: rt.jar:sun/security/x509/UnparseableExtension.class */
class UnparseableExtension extends Extension {
    private String name;
    private Throwable why;

    public UnparseableExtension(Extension extension, Throwable th) {
        super(extension);
        this.name = "";
        try {
            Class<?> cls = OIDMap.getClass(extension.getExtensionId());
            if (cls != null) {
                this.name = ((String) cls.getDeclaredField("NAME").get(null)) + " ";
            }
        } catch (Exception e2) {
        }
        this.why = th;
    }

    @Override // sun.security.x509.Extension
    public String toString() {
        return super.toString() + "Unparseable " + this.name + "extension due to\n" + ((Object) this.why) + "\n\n" + new HexDumpEncoder().encodeBuffer(getExtensionValue());
    }
}
