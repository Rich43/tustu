package javax.obex;

/* loaded from: bluecove-2.1.1.jar:javax/obex/Authenticator.class */
public interface Authenticator {
    PasswordAuthentication onAuthenticationChallenge(String str, boolean z2, boolean z3);

    byte[] onAuthenticationResponse(byte[] bArr);
}
