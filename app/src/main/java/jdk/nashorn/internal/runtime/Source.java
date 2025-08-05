package jdk.nashorn.internal.runtime;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.WeakHashMap;
import jdk.nashorn.api.scripting.URLReader;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.DebuggerSupport;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name = "source")
/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Source.class */
public final class Source implements Loggable {
    private static final int BUF_SIZE = 8192;
    private static final Cache CACHE = new Cache();
    private static final Base64.Encoder BASE64 = Base64.getUrlEncoder().withoutPadding();
    private final String name;
    private final String base;
    private final Data data;
    private int hash;
    private volatile byte[] digest;
    private String explicitURL;

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Source$Data.class */
    private interface Data {
        URL url();

        int length();

        long lastModified();

        char[] array();

        boolean isEvalCode();
    }

    private Source(String name, String base, Data data) {
        this.name = name;
        this.base = base;
        this.data = data;
    }

    private static synchronized Source sourceFor(String name, String base, URLData data) throws IOException {
        try {
            Source newSource = new Source(name, base, data);
            Source existingSource = CACHE.get(newSource);
            if (existingSource != null) {
                data.checkPermissionAndClose();
                return existingSource;
            }
            data.load();
            CACHE.put(newSource, newSource);
            return newSource;
        } catch (RuntimeException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            throw e2;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Source$Cache.class */
    private static class Cache extends WeakHashMap<Source, WeakReference<Source>> {
        static final /* synthetic */ boolean $assertionsDisabled;

        private Cache() {
        }

        static {
            $assertionsDisabled = !Source.class.desiredAssertionStatus();
        }

        public Source get(Source key) {
            WeakReference<Source> ref = (WeakReference) super.get((Object) key);
            if (ref == null) {
                return null;
            }
            return ref.get();
        }

        public void put(Source key, Source value) {
            if (!$assertionsDisabled && (value.data instanceof RawData)) {
                throw new AssertionError();
            }
            put((Cache) key, (Source) new WeakReference(value));
        }
    }

    DebuggerSupport.SourceInfo getSourceInfo() {
        return new DebuggerSupport.SourceInfo(getName(), this.data.hashCode(), this.data.url(), this.data.array());
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Source$RawData.class */
    private static class RawData implements Data {
        private final char[] array;
        private final boolean evalCode;
        private int hash;

        private RawData(char[] array, boolean evalCode) {
            this.array = (char[]) Objects.requireNonNull(array);
            this.evalCode = evalCode;
        }

        private RawData(String source, boolean evalCode) {
            this.array = ((String) Objects.requireNonNull(source)).toCharArray();
            this.evalCode = evalCode;
        }

        private RawData(Reader reader) throws IOException {
            this(Source.readFully(reader), false);
        }

        public int hashCode() {
            int h2 = this.hash;
            if (h2 == 0) {
                int iHashCode = Arrays.hashCode(this.array) ^ (this.evalCode ? 1 : 0);
                this.hash = iHashCode;
                h2 = iHashCode;
            }
            return h2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof RawData) {
                RawData other = (RawData) obj;
                return Arrays.equals(this.array, other.array) && this.evalCode == other.evalCode;
            }
            return false;
        }

        public String toString() {
            return new String(array());
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public URL url() {
            return null;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public int length() {
            return this.array.length;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public long lastModified() {
            return 0L;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public char[] array() {
            return this.array;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public boolean isEvalCode() {
            return this.evalCode;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Source$URLData.class */
    private static class URLData implements Data {
        private final URL url;
        protected final Charset cs;
        private int hash;
        protected char[] array;
        protected int length;
        protected long lastModified;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Source.class.desiredAssertionStatus();
        }

        private URLData(URL url, Charset cs) {
            this.url = (URL) Objects.requireNonNull(url);
            this.cs = cs;
        }

        public int hashCode() {
            int h2 = this.hash;
            if (h2 == 0) {
                int iHashCode = this.url.hashCode();
                this.hash = iHashCode;
                h2 = iHashCode;
            }
            return h2;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof URLData)) {
                return false;
            }
            URLData otherData = (URLData) other;
            if (this.url.equals(otherData.url)) {
                try {
                    if (isDeferred()) {
                        if (!$assertionsDisabled && otherData.isDeferred()) {
                            throw new AssertionError();
                        }
                        loadMeta();
                    } else if (otherData.isDeferred()) {
                        otherData.loadMeta();
                    }
                    return this.length == otherData.length && this.lastModified == otherData.lastModified;
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
            return false;
        }

        public String toString() {
            return new String(array());
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public URL url() {
            return this.url;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public int length() {
            return this.length;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public long lastModified() {
            return this.lastModified;
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public char[] array() {
            if ($assertionsDisabled || !isDeferred()) {
                return this.array;
            }
            throw new AssertionError();
        }

        @Override // jdk.nashorn.internal.runtime.Source.Data
        public boolean isEvalCode() {
            return false;
        }

        boolean isDeferred() {
            return this.array == null;
        }

        protected void checkPermissionAndClose() throws IOException {
            InputStream in = this.url.openStream();
            Throwable th = null;
            if (in != null) {
                if (0 != 0) {
                    try {
                        in.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    in.close();
                }
            }
            Source.debug("permission checked for ", this.url);
        }

        protected void load() throws IOException {
            if (this.array == null) {
                URLConnection c2 = this.url.openConnection();
                InputStream in = c2.getInputStream();
                Throwable th = null;
                try {
                    try {
                        this.array = this.cs == null ? Source.readFully(in) : Source.readFully(in, this.cs);
                        this.length = this.array.length;
                        this.lastModified = c2.getLastModified();
                        Source.debug("loaded content for ", this.url);
                        if (in != null) {
                            if (0 != 0) {
                                try {
                                    in.close();
                                    return;
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                    return;
                                }
                            }
                            in.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } catch (Throwable th4) {
                    if (in != null) {
                        if (th != null) {
                            try {
                                in.close();
                            } catch (Throwable th5) {
                                th.addSuppressed(th5);
                            }
                        } else {
                            in.close();
                        }
                    }
                    throw th4;
                }
            }
        }

        protected void loadMeta() throws IOException {
            if (this.length == 0 && this.lastModified == 0) {
                URLConnection c2 = this.url.openConnection();
                this.length = c2.getContentLength();
                this.lastModified = c2.getLastModified();
                Source.debug("loaded metadata for ", this.url);
            }
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Source$FileData.class */
    private static class FileData extends URLData {
        private final File file;

        private FileData(File file, Charset cs) {
            super(Source.getURLFromFile(file), cs);
            this.file = file;
        }

        @Override // jdk.nashorn.internal.runtime.Source.URLData
        protected void checkPermissionAndClose() throws IOException {
            if (this.file.canRead()) {
                Source.debug("permission checked for ", this.file);
                return;
            }
            throw new FileNotFoundException(((Object) this.file) + " (Permission Denied)");
        }

        @Override // jdk.nashorn.internal.runtime.Source.URLData
        protected void loadMeta() {
            if (this.length == 0 && this.lastModified == 0) {
                this.length = (int) this.file.length();
                this.lastModified = this.file.lastModified();
                Source.debug("loaded metadata for ", this.file);
            }
        }

        @Override // jdk.nashorn.internal.runtime.Source.URLData
        protected void load() throws IOException {
            if (this.array == null) {
                this.array = this.cs == null ? Source.readFully(this.file) : Source.readFully(this.file, this.cs);
                this.length = this.array.length;
                this.lastModified = this.file.lastModified();
                Source.debug("loaded content for ", this.file);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void debug(Object... msg) {
        DebugLogger logger = getLoggerStatic();
        if (logger != null) {
            logger.info(msg);
        }
    }

    private char[] data() {
        return this.data.array();
    }

    public static Source sourceFor(String name, char[] content, boolean isEval) {
        return new Source(name, baseName(name), new RawData(content, isEval));
    }

    public static Source sourceFor(String name, char[] content) {
        return sourceFor(name, content, false);
    }

    public static Source sourceFor(String name, String content, boolean isEval) {
        return new Source(name, baseName(name), new RawData(content, isEval));
    }

    public static Source sourceFor(String name, String content) {
        return sourceFor(name, content, false);
    }

    public static Source sourceFor(String name, URL url) throws IOException {
        return sourceFor(name, url, (Charset) null);
    }

    public static Source sourceFor(String name, URL url, Charset cs) throws IOException {
        return sourceFor(name, baseURL(url), new URLData(url, cs));
    }

    public static Source sourceFor(String name, File file) throws IOException {
        return sourceFor(name, file, (Charset) null);
    }

    public static Source sourceFor(String name, File file, Charset cs) throws IOException {
        File absFile = file.getAbsoluteFile();
        return sourceFor(name, dirName(absFile, null), new FileData(file, cs));
    }

    public static Source sourceFor(String name, Reader reader) throws IOException {
        if (reader instanceof URLReader) {
            URLReader urlReader = (URLReader) reader;
            return sourceFor(name, urlReader.getURL(), urlReader.getCharset());
        }
        return new Source(name, baseName(name), new RawData(reader));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        return Objects.equals(this.name, other.name) && this.data.equals(other.data);
    }

    public int hashCode() {
        int h2 = this.hash;
        if (h2 == 0) {
            int iHashCode = this.data.hashCode() ^ Objects.hashCode(this.name);
            this.hash = iHashCode;
            h2 = iHashCode;
        }
        return h2;
    }

    public String getString() {
        return this.data.toString();
    }

    public String getName() {
        return this.name;
    }

    public long getLastModified() {
        return this.data.lastModified();
    }

    public String getBase() {
        return this.base;
    }

    public String getString(int start, int len) {
        return new String(data(), start, len);
    }

    public String getString(long token) {
        int start = Token.descPosition(token);
        int len = Token.descLength(token);
        return new String(data(), start, len);
    }

    public URL getURL() {
        return this.data.url();
    }

    public String getExplicitURL() {
        return this.explicitURL;
    }

    public void setExplicitURL(String explicitURL) {
        this.explicitURL = explicitURL;
    }

    public boolean isEvalCode() {
        return this.data.isEvalCode();
    }

    private int findBOLN(int position) {
        char[] d2 = data();
        for (int i2 = position - 1; i2 > 0; i2--) {
            char ch = d2[i2];
            if (ch == '\n' || ch == '\r') {
                return i2 + 1;
            }
        }
        return 0;
    }

    private int findEOLN(int position) {
        char[] d2 = data();
        int length = d2.length;
        for (int i2 = position; i2 < length; i2++) {
            char ch = d2[i2];
            if (ch == '\n' || ch == '\r') {
                return i2 - 1;
            }
        }
        return length - 1;
    }

    public int getLine(int position) {
        char[] d2 = data();
        int line = 1;
        for (int i2 = 0; i2 < position; i2++) {
            char ch = d2[i2];
            if (ch == '\n') {
                line++;
            }
        }
        return line;
    }

    public int getColumn(int position) {
        return position - findBOLN(position);
    }

    public String getSourceLine(int position) {
        int first = findBOLN(position);
        int last = findEOLN(position);
        return new String(data(), first, (last - first) + 1);
    }

    public char[] getContent() {
        return data();
    }

    public int getLength() {
        return this.data.length();
    }

    public static char[] readFully(Reader reader) throws IOException {
        char[] arr = new char[8192];
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                int numChars = reader.read(arr, 0, arr.length);
                if (numChars > 0) {
                    sb.append(arr, 0, numChars);
                } else {
                    return sb.toString().toCharArray();
                }
            } finally {
                reader.close();
            }
        }
    }

    public static char[] readFully(File file) throws IOException {
        if (!file.isFile()) {
            throw new IOException(((Object) file) + " is not a file");
        }
        return byteToCharArray(Files.readAllBytes(file.toPath()));
    }

    public static char[] readFully(File file, Charset cs) throws IOException {
        if (!file.isFile()) {
            throw new IOException(((Object) file) + " is not a file");
        }
        byte[] buf = Files.readAllBytes(file.toPath());
        return cs != null ? new String(buf, cs).toCharArray() : byteToCharArray(buf);
    }

    public static char[] readFully(URL url) throws IOException {
        return readFully(url.openStream());
    }

    public static char[] readFully(URL url, Charset cs) throws IOException {
        return readFully(url.openStream(), cs);
    }

    public String getDigest() {
        return new String(getDigestBytes(), StandardCharsets.US_ASCII);
    }

    private byte[] getDigestBytes() {
        byte[] ldigest = this.digest;
        if (ldigest == null) {
            char[] content = data();
            byte[] bytes = new byte[content.length * 2];
            for (int i2 = 0; i2 < content.length; i2++) {
                bytes[i2 * 2] = (byte) (content[i2] & 255);
                bytes[(i2 * 2) + 1] = (byte) ((content[i2] & 65280) >> 8);
            }
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                if (this.name != null) {
                    md.update(this.name.getBytes(StandardCharsets.UTF_8));
                }
                if (this.base != null) {
                    md.update(this.base.getBytes(StandardCharsets.UTF_8));
                }
                if (getURL() != null) {
                    md.update(getURL().toString().getBytes(StandardCharsets.UTF_8));
                }
                byte[] bArrEncode = BASE64.encode(md.digest(bytes));
                ldigest = bArrEncode;
                this.digest = bArrEncode;
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException(e2);
            }
        }
        return ldigest;
    }

    public static String baseURL(URL url) {
        if (url.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            try {
                Path parent = Paths.get(url.toURI()).getParent();
                if (parent != null) {
                    return ((Object) parent) + File.separator;
                }
                return null;
            } catch (IOError | SecurityException | URISyntaxException e2) {
                return null;
            }
        }
        String path = url.getPath();
        if (path.isEmpty()) {
            return null;
        }
        String path2 = path.substring(0, path.lastIndexOf(47) + 1);
        int port = url.getPort();
        try {
            return new URL(url.getProtocol(), url.getHost(), port, path2).toString();
        } catch (MalformedURLException e3) {
            return null;
        }
    }

    private static String dirName(File file, String DEFAULT_BASE_NAME) {
        String res = file.getParent();
        return res != null ? res + File.separator : DEFAULT_BASE_NAME;
    }

    private static String baseName(String name) {
        int idx = name.lastIndexOf(47);
        if (idx == -1) {
            idx = name.lastIndexOf(92);
        }
        if (idx != -1) {
            return name.substring(0, idx + 1);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static char[] readFully(InputStream is, Charset cs) throws IOException {
        return cs != null ? new String(readBytes(is), cs).toCharArray() : readFully(is);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static char[] readFully(InputStream is) throws IOException {
        return byteToCharArray(readBytes(is));
    }

    private static char[] byteToCharArray(byte[] bytes) {
        Charset cs = StandardCharsets.UTF_8;
        int start = 0;
        if (bytes.length > 1 && bytes[0] == -2 && bytes[1] == -1) {
            start = 2;
            cs = StandardCharsets.UTF_16BE;
        } else if (bytes.length > 1 && bytes[0] == -1 && bytes[1] == -2) {
            if (bytes.length > 3 && bytes[2] == 0 && bytes[3] == 0) {
                start = 4;
                cs = Charset.forName("UTF-32LE");
            } else {
                start = 2;
                cs = StandardCharsets.UTF_16LE;
            }
        } else if (bytes.length > 2 && bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65) {
            start = 3;
            cs = StandardCharsets.UTF_8;
        } else if (bytes.length > 3 && bytes[0] == 0 && bytes[1] == 0 && bytes[2] == -2 && bytes[3] == -1) {
            start = 4;
            cs = Charset.forName("UTF-32BE");
        }
        return new String(bytes, start, bytes.length - start, cs).toCharArray();
    }

    static byte[] readBytes(InputStream is) throws IOException {
        byte[] arr = new byte[8192];
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            Throwable th = null;
            while (true) {
                try {
                    try {
                        int numBytes = is.read(arr, 0, arr.length);
                        if (numBytes <= 0) {
                            break;
                        }
                        buf.write(arr, 0, numBytes);
                    } finally {
                    }
                } finally {
                }
            }
            byte[] byteArray = buf.toByteArray();
            if (buf != null) {
                if (0 != 0) {
                    try {
                        buf.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    buf.close();
                }
            }
            return byteArray;
        } finally {
            is.close();
        }
    }

    public String toString() {
        return getName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static URL getURLFromFile(File file) {
        try {
            return file.toURI().toURL();
        } catch (SecurityException | MalformedURLException e2) {
            return null;
        }
    }

    private static DebugLogger getLoggerStatic() {
        Context context = Context.getContextTrustedOrNull();
        if (context == null) {
            return null;
        }
        return context.getLogger(Source.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger initLogger(Context context) {
        return context.getLogger(getClass());
    }

    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger getLogger() {
        return initLogger(Context.getContextTrusted());
    }

    private File dumpFile(File dirFile) {
        URL u2 = getURL();
        StringBuilder buf = new StringBuilder();
        buf.append(LocalDateTime.now().toString());
        buf.append('_');
        if (u2 != null) {
            buf.append(u2.toString().replace('/', '_').replace('\\', '_'));
        } else {
            buf.append(getName());
        }
        return new File(dirFile, buf.toString());
    }

    void dump(String dir) {
        File dirFile = new File(dir);
        File file = dumpFile(dirFile);
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            debug("Skipping source dump for " + this.name);
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Throwable th = null;
            try {
                try {
                    PrintWriter pw = new PrintWriter(fos);
                    pw.print(this.data.toString());
                    pw.flush();
                    if (fos != null) {
                        if (0 != 0) {
                            try {
                                fos.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            fos.close();
                        }
                    }
                } finally {
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (IOException ioExp) {
            debug("Skipping source dump for " + this.name + ": " + ECMAErrors.getMessage("io.error.cant.write", dir.toString() + " : " + ioExp.toString()));
        }
    }
}
