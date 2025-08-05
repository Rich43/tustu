package javax.net.ssl;

/* loaded from: rt.jar:javax/net/ssl/HostnameVerifier.class */
public interface HostnameVerifier {
    boolean verify(String str, SSLSession sSLSession);
}
