package javax.activation;

import com.sun.media.jfxmedia.locator.Locator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: rt.jar:javax/activation/URLDataSource.class */
public class URLDataSource implements DataSource {
    private URL url;
    private URLConnection url_conn = null;

    public URLDataSource(URL url) {
        this.url = null;
        this.url = url;
    }

    @Override // javax.activation.DataSource
    public String getContentType() {
        String type = null;
        try {
            if (this.url_conn == null) {
                this.url_conn = this.url.openConnection();
            }
        } catch (IOException e2) {
        }
        if (this.url_conn != null) {
            type = this.url_conn.getContentType();
        }
        if (type == null) {
            type = Locator.DEFAULT_CONTENT_TYPE;
        }
        return type;
    }

    @Override // javax.activation.DataSource
    public String getName() {
        return this.url.getFile();
    }

    @Override // javax.activation.DataSource
    public InputStream getInputStream() throws IOException {
        return this.url.openStream();
    }

    @Override // javax.activation.DataSource
    public OutputStream getOutputStream() throws IOException {
        this.url_conn = this.url.openConnection();
        if (this.url_conn != null) {
            this.url_conn.setDoOutput(true);
            return this.url_conn.getOutputStream();
        }
        return null;
    }

    public URL getURL() {
        return this.url;
    }
}
