package sun.net.www.protocol.netdoc;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.AccessController;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/www/protocol/netdoc/Handler.class */
public class Handler extends URLStreamHandler {
    static URL base;

    @Override // java.net.URLStreamHandler
    public synchronized URLConnection openConnection(URL url) throws IOException {
        URL url2;
        URLConnection uRLConnectionOpenConnection = null;
        boolean zBooleanValue = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("newdoc.localonly"))).booleanValue();
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("doc.url"));
        String file = url.getFile();
        if (!zBooleanValue) {
            try {
                if (base == null) {
                    base = new URL(str);
                }
                url2 = new URL(base, file);
            } catch (MalformedURLException e2) {
                url2 = null;
            }
            if (url2 != null) {
                uRLConnectionOpenConnection = url2.openConnection();
            }
        }
        if (uRLConnectionOpenConnection == null) {
            try {
                uRLConnectionOpenConnection = new URL(DeploymentDescriptorParser.ATTR_FILE, "~", file).openConnection();
                uRLConnectionOpenConnection.getInputStream();
            } catch (MalformedURLException e3) {
                uRLConnectionOpenConnection = null;
            } catch (IOException e4) {
                uRLConnectionOpenConnection = null;
            }
        }
        if (uRLConnectionOpenConnection == null) {
            throw new IOException("Can't find file for URL: " + url.toExternalForm());
        }
        return uRLConnectionOpenConnection;
    }
}
