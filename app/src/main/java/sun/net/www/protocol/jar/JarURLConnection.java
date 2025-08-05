package sun.net.www.protocol.jar;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/* loaded from: rt.jar:sun/net/www/protocol/jar/JarURLConnection.class */
public class JarURLConnection extends java.net.JarURLConnection {
    private static final boolean debug = false;
    private static final JarFileFactory factory = JarFileFactory.getInstance();
    private URL jarFileURL;
    private Permission permission;
    private URLConnection jarFileURLConnection;
    private String entryName;
    private JarEntry jarEntry;
    private JarFile jarFile;
    private String contentType;

    public JarURLConnection(URL url, Handler handler) throws IOException {
        super(url);
        this.jarFileURL = getJarFileURL();
        this.jarFileURLConnection = this.jarFileURL.openConnection();
        this.entryName = getEntryName();
    }

    @Override // java.net.JarURLConnection
    public JarFile getJarFile() throws IOException {
        connect();
        return this.jarFile;
    }

    @Override // java.net.JarURLConnection
    public JarEntry getJarEntry() throws IOException {
        connect();
        return this.jarEntry;
    }

    @Override // java.net.URLConnection
    public Permission getPermission() throws IOException {
        return this.jarFileURLConnection.getPermission();
    }

    /* loaded from: rt.jar:sun/net/www/protocol/jar/JarURLConnection$JarURLInputStream.class */
    class JarURLInputStream extends FilterInputStream {
        JarURLInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                if (!JarURLConnection.this.getUseCaches()) {
                    JarURLConnection.this.jarFile.close();
                }
            }
        }
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
        if (!this.connected) {
            this.jarFile = factory.get(getJarFileURL(), getUseCaches());
            if (getUseCaches()) {
                boolean useCaches = this.jarFileURLConnection.getUseCaches();
                this.jarFileURLConnection = factory.getConnection(this.jarFile);
                this.jarFileURLConnection.setUseCaches(useCaches);
            }
            if (this.entryName != null) {
                this.jarEntry = (JarEntry) this.jarFile.getEntry(this.entryName);
                if (this.jarEntry == null) {
                    try {
                        if (!getUseCaches()) {
                            this.jarFile.close();
                        }
                    } catch (Exception e2) {
                    }
                    throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
                }
            }
            this.connected = true;
        }
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        connect();
        if (this.entryName == null) {
            throw new IOException("no entry name specified");
        }
        if (this.jarEntry == null) {
            throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
        }
        return new JarURLInputStream(this.jarFile.getInputStream(this.jarEntry));
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        long contentLengthLong = getContentLengthLong();
        if (contentLengthLong > 2147483647L) {
            return -1;
        }
        return (int) contentLengthLong;
    }

    @Override // java.net.URLConnection
    public long getContentLengthLong() {
        long size = -1;
        try {
            connect();
            if (this.jarEntry == null) {
                size = this.jarFileURLConnection.getContentLengthLong();
            } else {
                size = getJarEntry().getSize();
            }
        } catch (IOException e2) {
        }
        return size;
    }

    @Override // java.net.URLConnection
    public Object getContent() throws IOException {
        Object content;
        connect();
        if (this.entryName == null) {
            content = this.jarFile;
        } else {
            content = super.getContent();
        }
        return content;
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        if (this.contentType == null) {
            if (this.entryName == null) {
                this.contentType = "x-java/jar";
            } else {
                try {
                    connect();
                    InputStream inputStream = this.jarFile.getInputStream(this.jarEntry);
                    this.contentType = guessContentTypeFromStream(new BufferedInputStream(inputStream));
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
            if (this.contentType == null) {
                this.contentType = guessContentTypeFromName(this.entryName);
            }
            if (this.contentType == null) {
                this.contentType = "content/unknown";
            }
        }
        return this.contentType;
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String str) {
        return this.jarFileURLConnection.getHeaderField(str);
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String str, String str2) {
        this.jarFileURLConnection.setRequestProperty(str, str2);
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String str) {
        return this.jarFileURLConnection.getRequestProperty(str);
    }

    @Override // java.net.URLConnection
    public void addRequestProperty(String str, String str2) {
        this.jarFileURLConnection.addRequestProperty(str, str2);
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        return this.jarFileURLConnection.getRequestProperties();
    }

    @Override // java.net.URLConnection
    public void setAllowUserInteraction(boolean z2) {
        this.jarFileURLConnection.setAllowUserInteraction(z2);
    }

    @Override // java.net.URLConnection
    public boolean getAllowUserInteraction() {
        return this.jarFileURLConnection.getAllowUserInteraction();
    }

    @Override // java.net.URLConnection
    public void setUseCaches(boolean z2) {
        this.jarFileURLConnection.setUseCaches(z2);
    }

    @Override // java.net.URLConnection
    public boolean getUseCaches() {
        return this.jarFileURLConnection.getUseCaches();
    }

    @Override // java.net.URLConnection
    public void setIfModifiedSince(long j2) {
        this.jarFileURLConnection.setIfModifiedSince(j2);
    }

    @Override // java.net.URLConnection
    public void setDefaultUseCaches(boolean z2) {
        this.jarFileURLConnection.setDefaultUseCaches(z2);
    }

    @Override // java.net.URLConnection
    public boolean getDefaultUseCaches() {
        return this.jarFileURLConnection.getDefaultUseCaches();
    }
}
