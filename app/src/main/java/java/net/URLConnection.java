package java.net;

import com.sun.glass.ui.Clipboard;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Permission;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.net.www.MessageHeader;
import sun.net.www.MimeTable;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/URLConnection.class */
public abstract class URLConnection {
    protected URL url;
    protected boolean doInput = true;
    protected boolean doOutput = false;
    protected boolean allowUserInteraction = defaultAllowUserInteraction;
    protected boolean useCaches = defaultUseCaches;
    protected long ifModifiedSince = 0;
    protected boolean connected = false;
    private int connectTimeout;
    private int readTimeout;
    private MessageHeader requests;
    private static FileNameMap fileNameMap;
    static ContentHandlerFactory factory;
    private static final String contentClassPrefix = "sun.net.www.content";
    private static final String contentPathProp = "java.content.handler.pkgs";
    private static boolean defaultAllowUserInteraction = false;
    private static boolean defaultUseCaches = true;
    private static boolean fileNameMapLoaded = false;
    private static Hashtable<String, ContentHandler> handlers = new Hashtable<>();

    public abstract void connect() throws IOException;

    public static synchronized FileNameMap getFileNameMap() {
        if (fileNameMap == null && !fileNameMapLoaded) {
            fileNameMap = MimeTable.loadTable();
            fileNameMapLoaded = true;
        }
        return new FileNameMap() { // from class: java.net.URLConnection.1
            private FileNameMap map = URLConnection.fileNameMap;

            @Override // java.net.FileNameMap
            public String getContentTypeFor(String str) {
                return this.map.getContentTypeFor(str);
            }
        };
    }

    public static void setFileNameMap(FileNameMap fileNameMap2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        fileNameMap = fileNameMap2;
    }

    public void setConnectTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        this.connectTimeout = i2;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setReadTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        this.readTimeout = i2;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    protected URLConnection(URL url) {
        this.url = url;
    }

    public URL getURL() {
        return this.url;
    }

    public int getContentLength() {
        long contentLengthLong = getContentLengthLong();
        if (contentLengthLong > 2147483647L) {
            return -1;
        }
        return (int) contentLengthLong;
    }

    public long getContentLengthLong() {
        return getHeaderFieldLong("content-length", -1L);
    }

    public String getContentType() {
        return getHeaderField("content-type");
    }

    public String getContentEncoding() {
        return getHeaderField("content-encoding");
    }

    public long getExpiration() {
        return getHeaderFieldDate("expires", 0L);
    }

    public long getDate() {
        return getHeaderFieldDate("date", 0L);
    }

    public long getLastModified() {
        return getHeaderFieldDate("last-modified", 0L);
    }

    public String getHeaderField(String str) {
        return null;
    }

    public Map<String, List<String>> getHeaderFields() {
        return Collections.emptyMap();
    }

    public int getHeaderFieldInt(String str, int i2) {
        try {
            return Integer.parseInt(getHeaderField(str));
        } catch (Exception e2) {
            return i2;
        }
    }

    public long getHeaderFieldLong(String str, long j2) {
        try {
            return Long.parseLong(getHeaderField(str));
        } catch (Exception e2) {
            return j2;
        }
    }

    public long getHeaderFieldDate(String str, long j2) {
        try {
            return Date.parse(getHeaderField(str));
        } catch (Exception e2) {
            return j2;
        }
    }

    public String getHeaderFieldKey(int i2) {
        return null;
    }

    public String getHeaderField(int i2) {
        return null;
    }

    public Object getContent() throws IOException {
        getInputStream();
        return getContentHandler().getContent(this);
    }

    public Object getContent(Class[] clsArr) throws IOException {
        getInputStream();
        return getContentHandler().getContent(this, clsArr);
    }

    public Permission getPermission() throws IOException {
        return SecurityConstants.ALL_PERMISSION;
    }

    public InputStream getInputStream() throws IOException {
        throw new UnknownServiceException("protocol doesn't support input");
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException("protocol doesn't support output");
    }

    public String toString() {
        return getClass().getName() + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) this.url);
    }

    public void setDoInput(boolean z2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        this.doInput = z2;
    }

    public boolean getDoInput() {
        return this.doInput;
    }

    public void setDoOutput(boolean z2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        this.doOutput = z2;
    }

    public boolean getDoOutput() {
        return this.doOutput;
    }

    public void setAllowUserInteraction(boolean z2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        this.allowUserInteraction = z2;
    }

    public boolean getAllowUserInteraction() {
        return this.allowUserInteraction;
    }

    public static void setDefaultAllowUserInteraction(boolean z2) {
        defaultAllowUserInteraction = z2;
    }

    public static boolean getDefaultAllowUserInteraction() {
        return defaultAllowUserInteraction;
    }

    public void setUseCaches(boolean z2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        this.useCaches = z2;
    }

    public boolean getUseCaches() {
        return this.useCaches;
    }

    public void setIfModifiedSince(long j2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        this.ifModifiedSince = j2;
    }

    public long getIfModifiedSince() {
        return this.ifModifiedSince;
    }

    public boolean getDefaultUseCaches() {
        return defaultUseCaches;
    }

    public void setDefaultUseCaches(boolean z2) {
        defaultUseCaches = z2;
    }

    public void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (str == null) {
            throw new NullPointerException("key is null");
        }
        if (this.requests == null) {
            this.requests = new MessageHeader();
        }
        this.requests.set(str, str2);
    }

    public void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (str == null) {
            throw new NullPointerException("key is null");
        }
        if (this.requests == null) {
            this.requests = new MessageHeader();
        }
        this.requests.add(str, str2);
    }

    public String getRequestProperty(String str) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (this.requests == null) {
            return null;
        }
        return this.requests.findValue(str);
    }

    public Map<String, List<String>> getRequestProperties() {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (this.requests == null) {
            return Collections.emptyMap();
        }
        return this.requests.getHeaders(null);
    }

    @Deprecated
    public static void setDefaultRequestProperty(String str, String str2) {
    }

    @Deprecated
    public static String getDefaultRequestProperty(String str) {
        return null;
    }

    public static synchronized void setContentHandlerFactory(ContentHandlerFactory contentHandlerFactory) {
        if (factory != null) {
            throw new Error("factory already defined");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        factory = contentHandlerFactory;
    }

    synchronized ContentHandler getContentHandler() throws UnknownServiceException {
        String strStripOffParameters = stripOffParameters(getContentType());
        ContentHandler contentHandlerLookupContentHandlerClassFor = null;
        if (strStripOffParameters == null) {
            throw new UnknownServiceException("no content-type");
        }
        try {
            contentHandlerLookupContentHandlerClassFor = handlers.get(strStripOffParameters);
            if (contentHandlerLookupContentHandlerClassFor != null) {
                return contentHandlerLookupContentHandlerClassFor;
            }
        } catch (Exception e2) {
        }
        if (factory != null) {
            contentHandlerLookupContentHandlerClassFor = factory.createContentHandler(strStripOffParameters);
        }
        if (contentHandlerLookupContentHandlerClassFor == null) {
            try {
                contentHandlerLookupContentHandlerClassFor = lookupContentHandlerClassFor(strStripOffParameters);
            } catch (Exception e3) {
                e3.printStackTrace();
                contentHandlerLookupContentHandlerClassFor = UnknownContentHandler.INSTANCE;
            }
            handlers.put(strStripOffParameters, contentHandlerLookupContentHandlerClassFor);
        }
        return contentHandlerLookupContentHandlerClassFor;
    }

    private String stripOffParameters(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOf = str.indexOf(59);
        if (iIndexOf > 0) {
            return str.substring(0, iIndexOf);
        }
        return str;
    }

    private ContentHandler lookupContentHandlerClassFor(String str) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<?> clsLoadClass;
        String strTypeToPackageName = typeToPackageName(str);
        StringTokenizer stringTokenizer = new StringTokenizer(getContentHandlerPkgPrefixes(), CallSiteDescriptor.OPERATOR_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            try {
                String str2 = stringTokenizer.nextToken().trim() + "." + strTypeToPackageName;
                clsLoadClass = null;
                try {
                    clsLoadClass = Class.forName(str2);
                } catch (ClassNotFoundException e2) {
                    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                    if (systemClassLoader != null) {
                        clsLoadClass = systemClassLoader.loadClass(str2);
                    }
                }
            } catch (Exception e3) {
            }
            if (clsLoadClass != null) {
                return (ContentHandler) clsLoadClass.newInstance();
            }
            continue;
        }
        return UnknownContentHandler.INSTANCE;
    }

    private String typeToPackageName(String str) {
        String lowerCase = str.toLowerCase();
        int length = lowerCase.length();
        char[] cArr = new char[length];
        lowerCase.getChars(0, length, cArr, 0);
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = cArr[i2];
            if (c2 == '/') {
                cArr[i2] = '.';
            } else if (('A' > c2 || c2 > 'Z') && (('a' > c2 || c2 > 'z') && ('0' > c2 || c2 > '9'))) {
                cArr[i2] = '_';
            }
        }
        return new String(cArr);
    }

    private String getContentHandlerPkgPrefixes() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(contentPathProp, ""));
        if (str != "") {
            str = str + CallSiteDescriptor.OPERATOR_DELIMITER;
        }
        return str + contentClassPrefix;
    }

    public static String guessContentTypeFromName(String str) {
        return getFileNameMap().getContentTypeFor(str);
    }

    public static String guessContentTypeFromStream(InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            return null;
        }
        inputStream.mark(16);
        int i2 = inputStream.read();
        int i3 = inputStream.read();
        int i4 = inputStream.read();
        int i5 = inputStream.read();
        int i6 = inputStream.read();
        int i7 = inputStream.read();
        int i8 = inputStream.read();
        int i9 = inputStream.read();
        int i10 = inputStream.read();
        int i11 = inputStream.read();
        int i12 = inputStream.read();
        int i13 = inputStream.read();
        int i14 = inputStream.read();
        int i15 = inputStream.read();
        int i16 = inputStream.read();
        int i17 = inputStream.read();
        inputStream.reset();
        if (i2 == 202 && i3 == 254 && i4 == 186 && i5 == 190) {
            return "application/java-vm";
        }
        if (i2 == 172 && i3 == 237) {
            return DataFlavor.javaSerializedObjectMimeType;
        }
        if (i2 == 60) {
            if (i3 == 33) {
                return Clipboard.HTML_TYPE;
            }
            if (i3 == 104) {
                if (i4 == 116 && i5 == 109 && i6 == 108) {
                    return Clipboard.HTML_TYPE;
                }
                if (i4 == 101 && i5 == 97 && i6 == 100) {
                    return Clipboard.HTML_TYPE;
                }
            }
            if (i3 == 98 && i4 == 111 && i5 == 100 && i6 == 121) {
                return Clipboard.HTML_TYPE;
            }
            if (i3 == 72) {
                if (i4 == 84 && i5 == 77 && i6 == 76) {
                    return Clipboard.HTML_TYPE;
                }
                if (i4 == 69 && i5 == 65 && i6 == 68) {
                    return Clipboard.HTML_TYPE;
                }
            }
            if (i3 == 66 && i4 == 79 && i5 == 68 && i6 == 89) {
                return Clipboard.HTML_TYPE;
            }
            if (i3 == 63 && i4 == 120 && i5 == 109 && i6 == 108 && i7 == 32) {
                return XMLCodec.XML_APPLICATION_MIME_TYPE;
            }
        }
        if (i2 == 239 && i3 == 187 && i4 == 191 && i5 == 60 && i6 == 63 && i7 == 120) {
            return XMLCodec.XML_APPLICATION_MIME_TYPE;
        }
        if (i2 == 254 && i3 == 255 && i4 == 0 && i5 == 60 && i6 == 0 && i7 == 63 && i8 == 0 && i9 == 120) {
            return XMLCodec.XML_APPLICATION_MIME_TYPE;
        }
        if (i2 == 255 && i3 == 254 && i4 == 60 && i5 == 0 && i6 == 63 && i7 == 0 && i8 == 120 && i9 == 0) {
            return XMLCodec.XML_APPLICATION_MIME_TYPE;
        }
        if (i2 == 0 && i3 == 0 && i4 == 254 && i5 == 255 && i6 == 0 && i7 == 0 && i8 == 0 && i9 == 60 && i10 == 0 && i11 == 0 && i12 == 0 && i13 == 63 && i14 == 0 && i15 == 0 && i16 == 0 && i17 == 120) {
            return XMLCodec.XML_APPLICATION_MIME_TYPE;
        }
        if (i2 == 255 && i3 == 254 && i4 == 0 && i5 == 0 && i6 == 60 && i7 == 0 && i8 == 0 && i9 == 0 && i10 == 63 && i11 == 0 && i12 == 0 && i13 == 0 && i14 == 120 && i15 == 0 && i16 == 0 && i17 == 0) {
            return XMLCodec.XML_APPLICATION_MIME_TYPE;
        }
        if (i2 == 71 && i3 == 73 && i4 == 70 && i5 == 56) {
            return "image/gif";
        }
        if (i2 == 35 && i3 == 100 && i4 == 101 && i5 == 102) {
            return "image/x-bitmap";
        }
        if (i2 == 33 && i3 == 32 && i4 == 88 && i5 == 80 && i6 == 77 && i7 == 50) {
            return "image/x-pixmap";
        }
        if (i2 == 137 && i3 == 80 && i4 == 78 && i5 == 71 && i6 == 13 && i7 == 10 && i8 == 26 && i9 == 10) {
            return "image/png";
        }
        if (i2 == 255 && i3 == 216 && i4 == 255) {
            if (i5 == 224 || i5 == 238) {
                return "image/jpeg";
            }
            if (i5 == 225 && i8 == 69 && i9 == 120 && i10 == 105 && i11 == 102 && i12 == 0) {
                return "image/jpeg";
            }
        }
        if (i2 == 208 && i3 == 207 && i4 == 17 && i5 == 224 && i6 == 161 && i7 == 177 && i8 == 26 && i9 == 225 && checkfpx(inputStream)) {
            return "image/vnd.fpx";
        }
        if (i2 == 46 && i3 == 115 && i4 == 110 && i5 == 100) {
            return "audio/basic";
        }
        if (i2 == 100 && i3 == 110 && i4 == 115 && i5 == 46) {
            return "audio/basic";
        }
        if (i2 == 82 && i3 == 73 && i4 == 70 && i5 == 70) {
            return MediaUtils.CONTENT_TYPE_WAV;
        }
        return null;
    }

    private static boolean checkfpx(InputStream inputStream) throws IOException {
        int i2;
        int i3;
        inputStream.mark(256);
        long jSkipForward = skipForward(inputStream, 28L);
        if (jSkipForward < 28) {
            inputStream.reset();
            return false;
        }
        int[] iArr = new int[16];
        if (readBytes(iArr, 2, inputStream) < 0) {
            inputStream.reset();
            return false;
        }
        int i4 = iArr[0];
        long j2 = jSkipForward + 2;
        if (readBytes(iArr, 2, inputStream) < 0) {
            inputStream.reset();
            return false;
        }
        if (i4 == 254) {
            i2 = iArr[0] + (iArr[1] << 8);
        } else {
            i2 = (iArr[0] << 8) + iArr[1];
        }
        long j3 = j2 + 2;
        long j4 = 48 - j3;
        long jSkipForward2 = skipForward(inputStream, j4);
        if (jSkipForward2 < j4) {
            inputStream.reset();
            return false;
        }
        long j5 = j3 + jSkipForward2;
        if (readBytes(iArr, 4, inputStream) < 0) {
            inputStream.reset();
            return false;
        }
        if (i4 == 254) {
            i3 = iArr[0] + (iArr[1] << 8) + (iArr[2] << 16) + (iArr[3] << 24);
        } else {
            i3 = (iArr[0] << 24) + (iArr[1] << 16) + (iArr[2] << 8) + iArr[3];
        }
        long j6 = j5 + 4;
        inputStream.reset();
        long j7 = 512 + ((1 << i2) * i3) + 80;
        if (j7 < 0) {
            return false;
        }
        inputStream.mark(((int) j7) + 48);
        if (skipForward(inputStream, j7) < j7) {
            inputStream.reset();
            return false;
        }
        if (readBytes(iArr, 16, inputStream) < 0) {
            inputStream.reset();
            return false;
        }
        if (i4 == 254 && iArr[0] == 0 && iArr[2] == 97 && iArr[3] == 86 && iArr[4] == 84 && iArr[5] == 193 && iArr[6] == 206 && iArr[7] == 17 && iArr[8] == 133 && iArr[9] == 83 && iArr[10] == 0 && iArr[11] == 170 && iArr[12] == 0 && iArr[13] == 161 && iArr[14] == 249 && iArr[15] == 91) {
            inputStream.reset();
            return true;
        }
        if (iArr[3] == 0 && iArr[1] == 97 && iArr[0] == 86 && iArr[5] == 84 && iArr[4] == 193 && iArr[7] == 206 && iArr[6] == 17 && iArr[8] == 133 && iArr[9] == 83 && iArr[10] == 0 && iArr[11] == 170 && iArr[12] == 0 && iArr[13] == 161 && iArr[14] == 249 && iArr[15] == 91) {
            inputStream.reset();
            return true;
        }
        inputStream.reset();
        return false;
    }

    private static int readBytes(int[] iArr, int i2, InputStream inputStream) throws IOException {
        byte[] bArr = new byte[i2];
        if (inputStream.read(bArr, 0, i2) < i2) {
            return -1;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = bArr[i3] & 255;
        }
        return 0;
    }

    private static long skipForward(InputStream inputStream, long j2) throws IOException {
        long j3 = 0;
        while (true) {
            long j4 = j3;
            if (j4 != j2) {
                long jSkip = inputStream.skip(j2 - j4);
                if (jSkip <= 0) {
                    if (inputStream.read() == -1) {
                        return j4;
                    }
                    j4++;
                }
                j3 = j4 + jSkip;
            } else {
                return j4;
            }
        }
    }
}
