package sun.net.www.protocol.jar;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import sun.net.www.ParseUtil;

/* loaded from: rt.jar:sun/net/www/protocol/jar/URLJarFile.class */
public class URLJarFile extends JarFile {
    private URLJarFileCloseController closeController;
    private Manifest superMan;
    private Attributes superAttr;
    private Map<String, Attributes> superEntries;
    private static URLJarFileCallBack callback = null;
    private static int BUF_SIZE = 2048;

    /* loaded from: rt.jar:sun/net/www/protocol/jar/URLJarFile$URLJarFileCloseController.class */
    public interface URLJarFileCloseController {
        void close(JarFile jarFile);
    }

    static JarFile getJarFile(URL url) throws IOException {
        return getJarFile(url, null);
    }

    static JarFile getJarFile(URL url, URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        if (isFileURL(url)) {
            return new URLJarFile(url, uRLJarFileCloseController);
        }
        return retrieve(url, uRLJarFileCloseController);
    }

    public URLJarFile(File file) throws IOException {
        this(file, (URLJarFileCloseController) null);
    }

    public URLJarFile(File file, URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        super(file, true, 5);
        this.closeController = null;
        this.closeController = uRLJarFileCloseController;
    }

    private URLJarFile(URL url, URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        super(ParseUtil.decode(url.getFile()));
        this.closeController = null;
        this.closeController = uRLJarFileCloseController;
    }

    private static boolean isFileURL(URL url) {
        if (url.getProtocol().equalsIgnoreCase(DeploymentDescriptorParser.ATTR_FILE)) {
            String host = url.getHost();
            if (host == null || host.equals("") || host.equals("~") || host.equalsIgnoreCase("localhost")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // java.util.zip.ZipFile
    protected void finalize() throws IOException {
        close();
    }

    @Override // java.util.jar.JarFile, java.util.zip.ZipFile
    public ZipEntry getEntry(String str) {
        ZipEntry entry = super.getEntry(str);
        if (entry != null) {
            if (entry instanceof JarEntry) {
                return new URLJarFileEntry((JarEntry) entry);
            }
            throw new InternalError(((Object) super.getClass()) + " returned unexpected entry type " + ((Object) entry.getClass()));
        }
        return null;
    }

    @Override // java.util.jar.JarFile
    public Manifest getManifest() throws IOException {
        if (!isSuperMan()) {
            return null;
        }
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putAll((Map) this.superAttr.clone());
        if (this.superEntries != null) {
            Map<String, Attributes> entries = manifest.getEntries();
            for (String str : this.superEntries.keySet()) {
                entries.put(str, (Attributes) this.superEntries.get(str).clone());
            }
        }
        return manifest;
    }

    @Override // java.util.zip.ZipFile, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closeController != null) {
            this.closeController.close(this);
        }
        super.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isSuperMan() throws IOException {
        if (this.superMan == null) {
            this.superMan = super.getManifest();
        }
        if (this.superMan != null) {
            this.superAttr = this.superMan.getMainAttributes();
            this.superEntries = this.superMan.getEntries();
            return true;
        }
        return false;
    }

    private static JarFile retrieve(URL url) throws IOException {
        return retrieve(url, null);
    }

    private static JarFile retrieve(URL url, final URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        if (callback != null) {
            return callback.retrieve(url);
        }
        try {
            final InputStream inputStream = url.openConnection().getInputStream();
            Throwable th = null;
            try {
                try {
                    JarFile jarFile = (JarFile) AccessController.doPrivileged(new PrivilegedExceptionAction<JarFile>() { // from class: sun.net.www.protocol.jar.URLJarFile.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public JarFile run() throws IOException {
                            Path pathCreateTempFile = Files.createTempFile("jar_cache", null, new FileAttribute[0]);
                            try {
                                Files.copy(inputStream, pathCreateTempFile, StandardCopyOption.REPLACE_EXISTING);
                                URLJarFile uRLJarFile = new URLJarFile(pathCreateTempFile.toFile(), uRLJarFileCloseController);
                                pathCreateTempFile.toFile().deleteOnExit();
                                return uRLJarFile;
                            } catch (Throwable th2) {
                                try {
                                    Files.delete(pathCreateTempFile);
                                } catch (IOException e2) {
                                    th2.addSuppressed(e2);
                                }
                                throw th2;
                            }
                        }
                    });
                    if (inputStream != null) {
                        if (0 != 0) {
                            try {
                                inputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            inputStream.close();
                        }
                    }
                    return jarFile;
                } finally {
                }
            } finally {
            }
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    public static void setCallBack(URLJarFileCallBack uRLJarFileCallBack) {
        callback = uRLJarFileCallBack;
    }

    /* loaded from: rt.jar:sun/net/www/protocol/jar/URLJarFile$URLJarFileEntry.class */
    private class URLJarFileEntry extends JarEntry {
        private JarEntry je;

        URLJarFileEntry(JarEntry jarEntry) {
            super(jarEntry);
            this.je = jarEntry;
        }

        @Override // java.util.jar.JarEntry
        public Attributes getAttributes() throws IOException {
            Map map;
            Attributes attributes;
            if (URLJarFile.this.isSuperMan() && (map = URLJarFile.this.superEntries) != null && (attributes = (Attributes) map.get(getName())) != null) {
                return (Attributes) attributes.clone();
            }
            return null;
        }

        @Override // java.util.jar.JarEntry
        public Certificate[] getCertificates() {
            Certificate[] certificates = this.je.getCertificates();
            if (certificates == null) {
                return null;
            }
            return (Certificate[]) certificates.clone();
        }

        @Override // java.util.jar.JarEntry
        public CodeSigner[] getCodeSigners() {
            CodeSigner[] codeSigners = this.je.getCodeSigners();
            if (codeSigners == null) {
                return null;
            }
            return (CodeSigner[]) codeSigners.clone();
        }
    }
}
