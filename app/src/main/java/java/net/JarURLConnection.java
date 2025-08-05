package java.net;

import java.io.IOException;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.net.www.ParseUtil;

/* loaded from: rt.jar:java/net/JarURLConnection.class */
public abstract class JarURLConnection extends URLConnection {
    private URL jarFileURL;
    private String entryName;
    protected URLConnection jarFileURLConnection;

    public abstract JarFile getJarFile() throws IOException;

    protected JarURLConnection(URL url) throws MalformedURLException {
        super(url);
        parseSpecs(url);
    }

    private void parseSpecs(URL url) throws MalformedURLException {
        String file = url.getFile();
        int iIndexOf = file.indexOf("!/");
        if (iIndexOf == -1) {
            throw new MalformedURLException("no !/ found in url spec:" + file);
        }
        this.jarFileURL = new URL(file.substring(0, iIndexOf));
        this.entryName = null;
        int i2 = iIndexOf + 1 + 1;
        if (i2 != file.length()) {
            this.entryName = file.substring(i2, file.length());
            this.entryName = ParseUtil.decode(this.entryName);
        }
    }

    public URL getJarFileURL() {
        return this.jarFileURL;
    }

    public String getEntryName() {
        return this.entryName;
    }

    public Manifest getManifest() throws IOException {
        return getJarFile().getManifest();
    }

    public JarEntry getJarEntry() throws IOException {
        return getJarFile().getJarEntry(this.entryName);
    }

    public Attributes getAttributes() throws IOException {
        JarEntry jarEntry = getJarEntry();
        if (jarEntry != null) {
            return jarEntry.getAttributes();
        }
        return null;
    }

    public Attributes getMainAttributes() throws IOException {
        Manifest manifest = getManifest();
        if (manifest != null) {
            return manifest.getMainAttributes();
        }
        return null;
    }

    public Certificate[] getCertificates() throws IOException {
        JarEntry jarEntry = getJarEntry();
        if (jarEntry != null) {
            return jarEntry.getCertificates();
        }
        return null;
    }
}
