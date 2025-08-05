package sun.net.www.protocol.file;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import javafx.fxml.FXMLLoader;
import sun.net.www.ParseUtil;

/* loaded from: rt.jar:sun/net/www/protocol/file/Handler.class */
public class Handler extends URLStreamHandler {
    private String getHost(URL url) {
        String host = url.getHost();
        if (host == null) {
            host = "";
        }
        return host;
    }

    @Override // java.net.URLStreamHandler
    protected void parseURL(URL url, String str, int i2, int i3) {
        super.parseURL(url, str.replace(File.separatorChar, '/'), i2, i3);
    }

    @Override // java.net.URLStreamHandler
    public synchronized URLConnection openConnection(URL url) throws IOException {
        return openConnection(url, null);
    }

    @Override // java.net.URLStreamHandler
    public synchronized URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        URLConnection uRLConnectionOpenConnection;
        String file = url.getFile();
        String host = url.getHost();
        String strReplace = ParseUtil.decode(file).replace('/', '\\').replace('|', ':');
        if (host == null || host.equals("") || host.equalsIgnoreCase("localhost") || host.equals("~")) {
            return createFileURLConnection(url, new File(strReplace));
        }
        String str = "\\\\" + host + strReplace;
        File file2 = new File(str);
        if (file2.exists()) {
            return new UNCFileURLConnection(url, file2, str);
        }
        try {
            URL url2 = new URL("ftp", host, file + (url.getRef() == null ? "" : FXMLLoader.CONTROLLER_METHOD_PREFIX + url.getRef()));
            if (proxy != null) {
                uRLConnectionOpenConnection = url2.openConnection(proxy);
            } else {
                uRLConnectionOpenConnection = url2.openConnection();
            }
        } catch (IOException e2) {
            uRLConnectionOpenConnection = null;
        }
        if (uRLConnectionOpenConnection == null) {
            throw new IOException("Unable to connect to: " + url.toExternalForm());
        }
        return uRLConnectionOpenConnection;
    }

    protected URLConnection createFileURLConnection(URL url, File file) {
        return new FileURLConnection(url, file);
    }

    @Override // java.net.URLStreamHandler
    protected boolean hostsEqual(URL url, URL url2) {
        String host = url.getHost();
        String host2 = url2.getHost();
        if ("localhost".equalsIgnoreCase(host) && (host2 == null || "".equals(host2))) {
            return true;
        }
        if ("localhost".equalsIgnoreCase(host2) && (host == null || "".equals(host))) {
            return true;
        }
        return super.hostsEqual(url, url2);
    }
}
