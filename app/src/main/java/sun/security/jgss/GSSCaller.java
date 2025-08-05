package sun.security.jgss;

/* loaded from: rt.jar:sun/security/jgss/GSSCaller.class */
public class GSSCaller {
    public static final GSSCaller CALLER_UNKNOWN = new GSSCaller("UNKNOWN");
    public static final GSSCaller CALLER_INITIATE = new GSSCaller("INITIATE");
    public static final GSSCaller CALLER_ACCEPT = new GSSCaller("ACCEPT");
    public static final GSSCaller CALLER_SSL_CLIENT = new GSSCaller("SSL_CLIENT");
    public static final GSSCaller CALLER_SSL_SERVER = new GSSCaller("SSL_SERVER");
    private String name;

    GSSCaller(String str) {
        this.name = str;
    }

    public String toString() {
        return "GSSCaller{" + this.name + '}';
    }
}
