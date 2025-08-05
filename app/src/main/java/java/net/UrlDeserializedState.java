package java.net;

import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* compiled from: URL.java */
/* loaded from: rt.jar:java/net/UrlDeserializedState.class */
final class UrlDeserializedState {
    private final String protocol;
    private final String host;
    private final int port;
    private final String authority;
    private final String file;
    private final String ref;
    private final int hashCode;

    public UrlDeserializedState(String str, String str2, int i2, String str3, String str4, String str5, int i3) {
        this.protocol = str;
        this.host = str2;
        this.port = i2;
        this.authority = str3;
        this.file = str4;
        this.ref = str5;
        this.hashCode = i3;
    }

    String getProtocol() {
        return this.protocol;
    }

    String getHost() {
        return this.host;
    }

    String getAuthority() {
        return this.authority;
    }

    int getPort() {
        return this.port;
    }

    String getFile() {
        return this.file;
    }

    String getRef() {
        return this.ref;
    }

    int getHashCode() {
        return this.hashCode;
    }

    String reconstituteUrlString() {
        int length = this.protocol.length() + 1;
        if (this.authority != null && this.authority.length() > 0) {
            length += 2 + this.authority.length();
        }
        if (this.file != null) {
            length += this.file.length();
        }
        if (this.ref != null) {
            length += 1 + this.ref.length();
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(this.protocol);
        sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
        if (this.authority != null && this.authority.length() > 0) {
            sb.append("//");
            sb.append(this.authority);
        }
        if (this.file != null) {
            sb.append(this.file);
        }
        if (this.ref != null) {
            sb.append(FXMLLoader.CONTROLLER_METHOD_PREFIX);
            sb.append(this.ref);
        }
        return sb.toString();
    }
}
