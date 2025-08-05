package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.locator.LocatorCache;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/Locator.class */
public class Locator {
    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final int MAX_CONNECTION_ATTEMPTS = 5;
    private static final long CONNECTION_RETRY_INTERVAL = 1000;
    private static final int CONNECTION_TIMEOUT = 300000;
    protected URI uri;
    private Map<String, Object> connectionProperties;
    private String uriString;
    private String scheme;
    private String protocol;
    private boolean canBlock;
    private boolean isIpod;
    protected String contentType = DEFAULT_CONTENT_TYPE;
    protected long contentLength = -1;
    private final Object propertyLock = new Object();
    private LocatorCache.CacheReference cacheEntry = null;
    private CountDownLatch readySignal = new CountDownLatch(1);

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/locator/Locator$LocatorConnection.class */
    private static class LocatorConnection {
        public HttpURLConnection connection;
        public int responseCode;

        private LocatorConnection() {
            this.connection = null;
            this.responseCode = 200;
        }
    }

    private LocatorConnection getConnection(URI uri, String requestMethod) throws IOException {
        LocatorConnection locatorConnection = new LocatorConnection();
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod(requestMethod);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);
        synchronized (this.propertyLock) {
            if (this.connectionProperties != null) {
                for (String key : this.connectionProperties.keySet()) {
                    Object value = this.connectionProperties.get(key);
                    if (value instanceof String) {
                        connection.setRequestProperty(key, (String) value);
                    }
                }
            }
        }
        locatorConnection.responseCode = connection.getResponseCode();
        if (connection.getResponseCode() == 200) {
            locatorConnection.connection = connection;
        } else {
            closeConnection(connection);
            locatorConnection.connection = null;
        }
        return locatorConnection;
    }

    private static long getContentLengthLong(URLConnection connection) {
        Method method = (Method) AccessController.doPrivileged(() -> {
            try {
                return URLConnection.class.getMethod("getContentLengthLong", new Class[0]);
            } catch (NoSuchMethodException e2) {
                return null;
            }
        });
        try {
            if (method != null) {
                return ((Long) method.invoke(connection, new Object[0])).longValue();
            }
            return connection.getContentLength();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
            return -1L;
        }
    }

    public Locator(URI uri) throws URISyntaxException {
        this.uriString = null;
        this.scheme = null;
        this.protocol = null;
        this.canBlock = false;
        if (uri == null) {
            throw new NullPointerException("uri == null!");
        }
        this.uriString = uri.toASCIIString();
        this.scheme = uri.getScheme();
        if (this.scheme == null) {
            throw new IllegalArgumentException("uri.getScheme() == null! uri == '" + ((Object) uri) + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        this.scheme = this.scheme.toLowerCase();
        if (this.scheme.equals("jar")) {
            URI subURI = new URI(this.uriString.substring(4));
            this.protocol = subURI.getScheme();
            if (this.protocol == null) {
                throw new IllegalArgumentException("uri.getScheme() == null! subURI == '" + ((Object) subURI) + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            this.protocol = this.protocol.toLowerCase();
        } else {
            this.protocol = this.scheme;
        }
        if (HostUtils.isIOS() && this.protocol.equals("ipod-library")) {
            this.isIpod = true;
        }
        if (!this.isIpod && !MediaManager.canPlayProtocol(this.protocol)) {
            throw new UnsupportedOperationException("Unsupported protocol \"" + this.protocol + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (this.protocol.equals("http") || this.protocol.equals("https")) {
            this.canBlock = true;
        }
        this.uri = uri;
    }

    private InputStream getInputStream(URI uri) throws IOException {
        URL url = uri.toURL();
        URLConnection connection = url.openConnection();
        synchronized (this.propertyLock) {
            if (this.connectionProperties != null) {
                for (String key : this.connectionProperties.keySet()) {
                    Object value = this.connectionProperties.get(key);
                    if (value instanceof String) {
                        connection.setRequestProperty(key, (String) value);
                    }
                }
            }
        }
        InputStream inputStream = url.openStream();
        this.contentLength = getContentLengthLong(connection);
        return inputStream;
    }

    public void cacheMedia() {
        LocatorCache.CacheReference ref = LocatorCache.locatorCache().fetchURICache(this.uri);
        if (null == ref) {
            try {
                InputStream is = getInputStream(this.uri);
                ByteBuffer cacheBuffer = ByteBuffer.allocateDirect((int) this.contentLength);
                byte[] readBuf = new byte[8192];
                while (0 < this.contentLength) {
                    try {
                        int count = is.read(readBuf);
                        if (count != -1) {
                            cacheBuffer.put(readBuf, 0, count);
                        }
                    } catch (IOException ioe) {
                        try {
                            is.close();
                        } catch (Throwable th) {
                        }
                        if (Logger.canLog(1)) {
                            Logger.logMsg(1, "IOException trying to preload media: " + ((Object) ioe));
                            return;
                        }
                        return;
                    }
                }
                try {
                    is.close();
                } catch (Throwable th2) {
                }
                this.cacheEntry = LocatorCache.locatorCache().registerURICache(this.uri, cacheBuffer, this.contentType);
                this.canBlock = false;
            } catch (Throwable th3) {
            }
        }
    }

    public boolean canBlock() {
        return this.canBlock;
    }

    public void init() throws URISyntaxException, IOException {
        int index;
        try {
            try {
                int firstSlash = this.uriString.indexOf("/");
                if (firstSlash != -1 && this.uriString.charAt(firstSlash + 1) != '/') {
                    if (this.protocol.equals(DeploymentDescriptorParser.ATTR_FILE)) {
                        this.uriString = this.uriString.replaceFirst("/", "///");
                    } else if (this.protocol.equals("http") || this.protocol.equals("https")) {
                        this.uriString = this.uriString.replaceFirst("/", "//");
                    }
                }
                if (System.getProperty("os.name").toLowerCase().indexOf("win") == -1 && this.protocol.equals(DeploymentDescriptorParser.ATTR_FILE) && (index = this.uriString.indexOf("/~/")) != -1) {
                    this.uriString = this.uriString.substring(0, index) + System.getProperty("user.home") + this.uriString.substring(index + 2);
                }
                this.uri = new URI(this.uriString);
                this.cacheEntry = LocatorCache.locatorCache().fetchURICache(this.uri);
                if (null != this.cacheEntry) {
                    this.contentType = this.cacheEntry.getMIMEType();
                    this.contentLength = this.cacheEntry.getBuffer().capacity();
                    if (Logger.canLog(1)) {
                        Logger.logMsg(1, "Locator init cache hit:\n    uri " + ((Object) this.uri) + "\n    type " + this.contentType + "\n    length " + this.contentLength);
                    }
                    return;
                }
                boolean isConnected = false;
                boolean isMediaUnAvailable = false;
                boolean isMediaSupported = true;
                if (!this.isIpod) {
                    int numConnectionAttempts = 0;
                    while (true) {
                        if (numConnectionAttempts >= 5) {
                            break;
                        }
                        try {
                            if (this.scheme.equals("http") || this.scheme.equals("https")) {
                                LocatorConnection locatorConnection = getConnection(this.uri, "HEAD");
                                if (locatorConnection == null || locatorConnection.connection == null) {
                                    locatorConnection = getConnection(this.uri, "GET");
                                }
                                if (locatorConnection != null && locatorConnection.connection != null) {
                                    isConnected = true;
                                    this.contentType = locatorConnection.connection.getContentType();
                                    this.contentLength = getContentLengthLong(locatorConnection.connection);
                                    closeConnection(locatorConnection.connection);
                                    locatorConnection.connection = null;
                                } else if (locatorConnection != null && locatorConnection.responseCode == 404) {
                                    isMediaUnAvailable = true;
                                }
                            } else if (this.scheme.equals(DeploymentDescriptorParser.ATTR_FILE) || this.scheme.equals("jar")) {
                                InputStream stream = getInputStream(this.uri);
                                stream.close();
                                isConnected = true;
                                this.contentType = MediaUtils.filenameToContentType(this.uri);
                            }
                        } catch (IOException ioe) {
                            if (numConnectionAttempts + 1 >= 5) {
                                throw ioe;
                            }
                        }
                        if (isConnected) {
                            if (MediaUtils.CONTENT_TYPE_WAV.equals(this.contentType)) {
                                this.contentType = getContentTypeFromFileSignature(this.uri);
                                if (!MediaManager.canPlayContentType(this.contentType)) {
                                    isMediaSupported = false;
                                }
                            } else if (this.contentType == null || !MediaManager.canPlayContentType(this.contentType)) {
                                this.contentType = MediaUtils.filenameToContentType(this.uri);
                                if (DEFAULT_CONTENT_TYPE.equals(this.contentType)) {
                                    this.contentType = getContentTypeFromFileSignature(this.uri);
                                }
                                if (!MediaManager.canPlayContentType(this.contentType)) {
                                    isMediaSupported = false;
                                }
                            }
                        } else {
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException e2) {
                            }
                            numConnectionAttempts++;
                        }
                    }
                } else {
                    this.contentType = MediaUtils.filenameToContentType(this.uri);
                }
                if (!this.isIpod && !isConnected) {
                    if (isMediaUnAvailable) {
                        throw new FileNotFoundException("media is unavailable (" + this.uri.toString() + ")");
                    }
                    throw new IOException("could not connect to media (" + this.uri.toString() + ")");
                }
                if (!isMediaSupported) {
                    throw new MediaException("media type not supported (" + this.uri.toString() + ")");
                }
                this.readySignal.countDown();
            } catch (MediaException e3) {
                throw e3;
            } catch (FileNotFoundException e4) {
                throw e4;
            } catch (IOException e5) {
                throw e5;
            }
        } finally {
            this.readySignal.countDown();
        }
    }

    public String getContentType() {
        try {
            this.readySignal.await();
        } catch (Exception e2) {
        }
        return this.contentType;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public long getContentLength() {
        try {
            this.readySignal.await();
        } catch (Exception e2) {
        }
        return this.contentLength;
    }

    public void waitForReadySignal() {
        try {
            this.readySignal.await();
        } catch (Exception e2) {
        }
    }

    public URI getURI() {
        return this.uri;
    }

    public String toString() {
        if (LocatorCache.locatorCache().isCached(this.uri)) {
            return "{LocatorURI uri: " + this.uri.toString() + " (media cached)}";
        }
        return "{LocatorURI uri: " + this.uri.toString() + "}";
    }

    public String getStringLocation() {
        return this.uri.toString();
    }

    public void setConnectionProperty(String property, Object value) {
        synchronized (this.propertyLock) {
            if (this.connectionProperties == null) {
                this.connectionProperties = new TreeMap();
            }
            this.connectionProperties.put(property, value);
        }
    }

    public ConnectionHolder createConnectionHolder() throws IOException {
        ConnectionHolder connectionHolderCreateURIConnectionHolder;
        if (null != this.cacheEntry) {
            if (Logger.canLog(1)) {
                Logger.logMsg(1, "Locator.createConnectionHolder: media cached, creating memory connection holder");
            }
            return ConnectionHolder.createMemoryConnectionHolder(this.cacheEntry.getBuffer());
        }
        if (DeploymentDescriptorParser.ATTR_FILE.equals(this.scheme)) {
            return ConnectionHolder.createFileConnectionHolder(this.uri);
        }
        String uriPath = this.uri.getPath();
        if (uriPath != null && (uriPath.endsWith(".m3u8") || uriPath.endsWith(".m3u"))) {
            return ConnectionHolder.createHLSConnectionHolder(this.uri);
        }
        String type = getContentType();
        if (type != null && (type.equals(MediaUtils.CONTENT_TYPE_M3U8) || type.equals(MediaUtils.CONTENT_TYPE_M3U))) {
            return ConnectionHolder.createHLSConnectionHolder(this.uri);
        }
        synchronized (this.propertyLock) {
            connectionHolderCreateURIConnectionHolder = ConnectionHolder.createURIConnectionHolder(this.uri, this.connectionProperties);
        }
        return connectionHolderCreateURIConnectionHolder;
    }

    private String getContentTypeFromFileSignature(URI uri) throws IOException {
        InputStream stream = getInputStream(uri);
        byte[] signature = new byte[22];
        int size = stream.read(signature);
        stream.close();
        return MediaUtils.fileSignatureToContentType(signature, size);
    }

    static void closeConnection(URLConnection connection) {
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            try {
                if (httpConnection.getResponseCode() < 400 && httpConnection.getInputStream() != null) {
                    httpConnection.getInputStream().close();
                }
            } catch (IOException e2) {
                try {
                    if (httpConnection.getErrorStream() != null) {
                        httpConnection.getErrorStream().close();
                    }
                } catch (IOException e3) {
                }
            }
        }
    }
}
