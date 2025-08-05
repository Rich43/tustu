package sun.security.ssl;

/* loaded from: jsse.jar:sun/security/ssl/ContentType.class */
enum ContentType {
    INVALID((byte) 0, "invalid", ProtocolVersion.PROTOCOLS_OF_13),
    CHANGE_CIPHER_SPEC((byte) 20, "change_cipher_spec", ProtocolVersion.PROTOCOLS_TO_12),
    ALERT((byte) 21, "alert", ProtocolVersion.PROTOCOLS_TO_13),
    HANDSHAKE((byte) 22, "handshake", ProtocolVersion.PROTOCOLS_TO_13),
    APPLICATION_DATA((byte) 23, "application_data", ProtocolVersion.PROTOCOLS_TO_13);

    final byte id;
    final String name;
    final ProtocolVersion[] supportedProtocols;

    ContentType(byte b2, String str, ProtocolVersion[] protocolVersionArr) {
        this.id = b2;
        this.name = str;
        this.supportedProtocols = protocolVersionArr;
    }

    static ContentType valueOf(byte b2) {
        for (ContentType contentType : values()) {
            if (contentType.id == b2) {
                return contentType;
            }
        }
        return null;
    }

    static String nameOf(byte b2) {
        for (ContentType contentType : values()) {
            if (contentType.id == b2) {
                return contentType.name;
            }
        }
        return "<UNKNOWN CONTENT TYPE: " + (b2 & 255) + ">";
    }
}
