package sun.net.www.protocol.mailto;

import com.sun.glass.ui.Clipboard;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketPermission;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Permission;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.smtp.SmtpClient;
import sun.net.www.MessageHeader;
import sun.net.www.ParseUtil;
import sun.net.www.URLConnection;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/net/www/protocol/mailto/MailToURLConnection.class */
public class MailToURLConnection extends URLConnection {
    InputStream is;
    OutputStream os;
    SmtpClient client;
    Permission permission;
    private int connectTimeout;
    private int readTimeout;

    MailToURLConnection(URL url) {
        super(url);
        this.is = null;
        this.os = null;
        this.connectTimeout = -1;
        this.readTimeout = -1;
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.add("content-type", Clipboard.HTML_TYPE);
        setProperties(messageHeader);
    }

    String getFromAddress() {
        String property = System.getProperty("user.fromaddr");
        if (property == null) {
            String property2 = System.getProperty("user.name");
            if (property2 != null) {
                String property3 = System.getProperty("mail.host");
                if (property3 == null) {
                    try {
                        property3 = InetAddress.getLocalHost().getHostName();
                    } catch (UnknownHostException e2) {
                    }
                }
                property = property2 + "@" + property3;
            } else {
                property = "";
            }
        }
        return property;
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        this.client = new SmtpClient(this.connectTimeout);
        this.client.setReadTimeout(this.readTimeout);
    }

    @Override // java.net.URLConnection
    public synchronized OutputStream getOutputStream() throws IOException {
        if (this.os != null) {
            return this.os;
        }
        if (this.is != null) {
            throw new IOException("Cannot write output after reading input.");
        }
        connect();
        String strDecode = ParseUtil.decode(this.url.getPath());
        this.client.from(getFromAddress());
        this.client.to(strDecode);
        this.os = this.client.startMessage();
        return this.os;
    }

    @Override // java.net.URLConnection
    public Permission getPermission() throws IOException {
        if (this.permission == null) {
            connect();
            this.permission = new SocketPermission(this.client.getMailHost() + CallSiteDescriptor.TOKEN_DELIMITER + 25, SecurityConstants.SOCKET_CONNECT_ACTION);
        }
        return this.permission;
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeouts can't be negative");
        }
        this.connectTimeout = i2;
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        if (this.connectTimeout < 0) {
            return 0;
        }
        return this.connectTimeout;
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeouts can't be negative");
        }
        this.readTimeout = i2;
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        if (this.readTimeout < 0) {
            return 0;
        }
        return this.readTimeout;
    }
}
