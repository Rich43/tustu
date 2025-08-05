package sun.awt.image;

import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import sun.net.util.URLUtil;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/awt/image/URLImageSource.class */
public class URLImageSource extends InputStreamImageSource {
    URL url;
    URLConnection conn;
    String actualHost;
    int actualPort;

    public URLImageSource(URL url) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                Permission connectPermission = URLUtil.getConnectPermission(url);
                if (connectPermission != null) {
                    try {
                        securityManager.checkPermission(connectPermission);
                    } catch (SecurityException e2) {
                        if ((connectPermission instanceof FilePermission) && connectPermission.getActions().indexOf("read") != -1) {
                            securityManager.checkRead(connectPermission.getName());
                        } else if ((connectPermission instanceof SocketPermission) && connectPermission.getActions().indexOf(SecurityConstants.SOCKET_CONNECT_ACTION) != -1) {
                            securityManager.checkConnect(url.getHost(), url.getPort());
                        } else {
                            throw e2;
                        }
                    }
                }
            } catch (IOException e3) {
                securityManager.checkConnect(url.getHost(), url.getPort());
            }
        }
        this.url = url;
    }

    public URLImageSource(String str) throws MalformedURLException {
        this(new URL(null, str));
    }

    public URLImageSource(URL url, URLConnection uRLConnection) {
        this(url);
        this.conn = uRLConnection;
    }

    public URLImageSource(URLConnection uRLConnection) {
        this(uRLConnection.getURL(), uRLConnection);
    }

    @Override // sun.awt.image.InputStreamImageSource
    final boolean checkSecurity(Object obj, boolean z2) {
        if (this.actualHost != null) {
            try {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkConnect(this.actualHost, this.actualPort, obj);
                }
                return true;
            } catch (SecurityException e2) {
                if (!z2) {
                    throw e2;
                }
                return false;
            }
        }
        return true;
    }

    private synchronized URLConnection getConnection() throws IOException {
        URLConnection uRLConnectionOpenConnection;
        if (this.conn != null) {
            uRLConnectionOpenConnection = this.conn;
            this.conn = null;
        } else {
            uRLConnectionOpenConnection = this.url.openConnection();
        }
        return uRLConnectionOpenConnection;
    }

    @Override // sun.awt.image.InputStreamImageSource
    protected ImageDecoder getDecoder() {
        AutoCloseable autoCloseable = null;
        Object obj = null;
        try {
            URLConnection connection = getConnection();
            InputStream inputStream = connection.getInputStream();
            String contentType = connection.getContentType();
            URL url = connection.getURL();
            if (url != this.url && (!url.getHost().equals(this.url.getHost()) || url.getPort() != this.url.getPort())) {
                if (this.actualHost != null && (!this.actualHost.equals(url.getHost()) || this.actualPort != url.getPort())) {
                    throw new SecurityException("image moved!");
                }
                this.actualHost = url.getHost();
                this.actualPort = url.getPort();
            }
            ImageDecoder imageDecoderDecoderForType = decoderForType(inputStream, contentType);
            if (imageDecoderDecoderForType == null) {
                imageDecoderDecoderForType = getDecoder(inputStream);
            }
            if (imageDecoderDecoderForType == null) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                    }
                } else if (connection instanceof HttpURLConnection) {
                    ((HttpURLConnection) connection).disconnect();
                }
            }
            return imageDecoderDecoderForType;
        } catch (IOException e3) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                    return null;
                } catch (IOException e4) {
                    return null;
                }
            }
            if (obj instanceof HttpURLConnection) {
                ((HttpURLConnection) null).disconnect();
                return null;
            }
            return null;
        }
    }
}
