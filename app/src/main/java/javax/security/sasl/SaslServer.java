package javax.security.sasl;

/* loaded from: rt.jar:javax/security/sasl/SaslServer.class */
public interface SaslServer {
    String getMechanismName();

    byte[] evaluateResponse(byte[] bArr) throws SaslException;

    boolean isComplete();

    String getAuthorizationID();

    byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException;

    byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException;

    Object getNegotiatedProperty(String str);

    void dispose() throws SaslException;
}
