package javax.security.sasl;

/* loaded from: rt.jar:javax/security/sasl/SaslClient.class */
public interface SaslClient {
    String getMechanismName();

    boolean hasInitialResponse();

    byte[] evaluateChallenge(byte[] bArr) throws SaslException;

    boolean isComplete();

    byte[] unwrap(byte[] bArr, int i2, int i3) throws SaslException;

    byte[] wrap(byte[] bArr, int i2, int i3) throws SaslException;

    Object getNegotiatedProperty(String str);

    void dispose() throws SaslException;
}
