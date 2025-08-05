package com.sun.webkit.network;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.net.ssl.SSLHandshakeException;
import org.apache.commons.math3.optimization.direct.CMAESOptimizer;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/webkit/network/URLLoader.class */
final class URLLoader implements Runnable {
    public static final int ALLOW_UNASSIGNED = 1;
    private static final Logger logger = Logger.getLogger(URLLoader.class.getName());
    private static final int MAX_REDIRECTS = 10;
    private static final int MAX_BUF_COUNT = 3;
    private static final String GET = "GET";
    private static final String HEAD = "HEAD";
    private static final String DELETE = "DELETE";
    private final WebPage webPage;
    private final ByteBufferPool byteBufferPool;
    private final boolean asynchronous;
    private String url;
    private String method;
    private final String headers;
    private FormDataElement[] formDataElements;
    private final long data;
    private volatile boolean canceled = false;

    private static native void twkDidSendData(long j2, long j3, long j4);

    private static native boolean twkWillSendRequest(String str, String str2, int i2, String str3, String str4, long j2, String str5, String str6, long j3);

    private static native void twkDidReceiveResponse(int i2, String str, String str2, long j2, String str3, String str4, long j3);

    private static native void twkDidReceiveData(ByteBuffer byteBuffer, int i2, int i3, long j2);

    private static native void twkDidFinishLoading(long j2);

    private static native void twkDidFail(int i2, String str, String str2, long j2);

    URLLoader(WebPage webPage, ByteBufferPool byteBufferPool, boolean asynchronous, String url, String method, String headers, FormDataElement[] formDataElements, long data) {
        this.webPage = webPage;
        this.byteBufferPool = byteBufferPool;
        this.asynchronous = asynchronous;
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.formDataElements = formDataElements;
        this.data = data;
    }

    private void fwkCancel() {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("data: [0x%016X]", Long.valueOf(this.data)));
        }
        this.canceled = true;
    }

    @Override // java.lang.Runnable
    public void run() {
        AccessController.doPrivileged(() -> {
            doRun();
            return null;
        }, this.webPage.getAccessControlContext());
    }

    /* JADX WARN: Finally extract failed */
    private void doRun() {
        Redirect redirect;
        int questionMarkPosition;
        Throwable error = null;
        int errorCode = 0;
        int redirectCount = 0;
        boolean streaming = true;
        boolean connectionResetRetry = true;
        while (true) {
            try {
                String actualUrl = this.url;
                if (this.url.startsWith("file:") && (questionMarkPosition = this.url.indexOf(63)) != -1) {
                    actualUrl = this.url.substring(0, questionMarkPosition);
                }
                URL urlObject = URLs.newURL(actualUrl);
                workaround7177996(urlObject);
                URLConnection c2 = urlObject.openConnection();
                prepareConnection(c2);
                try {
                    try {
                        sendRequest(c2, streaming);
                        redirect = receiveResponse(c2);
                        close(c2);
                    } catch (HttpRetryException ex) {
                        if (!streaming) {
                            throw ex;
                        }
                        streaming = false;
                        close(c2);
                    } catch (SocketException ex2) {
                        if (!"Connection reset".equals(ex2.getMessage()) || !connectionResetRetry) {
                            throw ex2;
                        }
                        connectionResetRetry = false;
                        close(c2);
                    }
                    if (redirect == null) {
                        break;
                    }
                    int i2 = redirectCount;
                    redirectCount++;
                    if (i2 >= 10) {
                        throw new TooManyRedirectsException();
                    }
                    boolean resetRequest = (redirect.preserveRequest || this.method.equals(GET) || this.method.equals(HEAD)) ? false : true;
                    String newMethod = resetRequest ? GET : this.method;
                    willSendRequest(redirect.url, newMethod, c2);
                    if (this.canceled) {
                        break;
                    }
                    this.url = redirect.url;
                    this.method = newMethod;
                    this.formDataElements = resetRequest ? null : this.formDataElements;
                } catch (Throwable th) {
                    close(c2);
                    throw th;
                }
            } catch (InvalidResponseException ex3) {
                error = ex3;
                errorCode = 9;
            } catch (TooManyRedirectsException ex4) {
                error = ex4;
                errorCode = 10;
            } catch (FileNotFoundException ex5) {
                error = ex5;
                errorCode = 11;
            } catch (ConnectException ex6) {
                error = ex6;
                errorCode = 4;
            } catch (MalformedURLException ex7) {
                error = ex7;
                errorCode = 2;
            } catch (NoRouteToHostException ex8) {
                error = ex8;
                errorCode = 6;
            } catch (SocketException ex9) {
                error = ex9;
                errorCode = 5;
            } catch (SocketTimeoutException ex10) {
                error = ex10;
                errorCode = 7;
            } catch (UnknownHostException ex11) {
                error = ex11;
                errorCode = 1;
            } catch (AccessControlException ex12) {
                error = ex12;
                errorCode = 8;
            } catch (SSLHandshakeException ex13) {
                error = ex13;
                errorCode = 3;
            } catch (Throwable th2) {
                error = th2;
                errorCode = 99;
            }
        }
        if (error != null) {
            if (errorCode == 99) {
                logger.log(Level.WARNING, "Unexpected error", error);
            } else {
                logger.log(Level.FINEST, "Load error", error);
            }
            didFail(errorCode, error.getMessage());
        }
    }

    private static void workaround7177996(URL url) throws FileNotFoundException {
        String host;
        if (!url.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE) || (host = url.getHost()) == null || host.equals("") || host.equals("~") || host.equalsIgnoreCase("localhost")) {
            return;
        }
        if (System.getProperty("os.name").startsWith("Windows")) {
            String path = null;
            try {
                path = URLDecoder.decode(url.getPath(), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
            }
            File file = new File("\\\\" + host + path.replace('/', '\\').replace('|', ':'));
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + ((Object) url));
            }
            return;
        }
        throw new FileNotFoundException("File not found: " + ((Object) url));
    }

    private void prepareConnection(URLConnection c2) throws IOException {
        String str;
        c2.setConnectTimeout(CMAESOptimizer.DEFAULT_MAXITERATIONS);
        c2.setReadTimeout(3600000);
        c2.setUseCaches(false);
        Locale loc = Locale.getDefault();
        String lang = "";
        if (!loc.equals(Locale.US) && !loc.equals(Locale.ENGLISH)) {
            if (loc.getCountry().isEmpty()) {
                str = loc.getLanguage() + ",";
            } else {
                str = loc.getLanguage() + LanguageTag.SEP + loc.getCountry() + ",";
            }
            lang = str;
        }
        c2.setRequestProperty(XIncludeHandler.HTTP_ACCEPT_LANGUAGE, lang.toLowerCase() + "en-us;q=0.8,en;q=0.7");
        c2.setRequestProperty("Accept-Encoding", "gzip");
        c2.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        if (this.headers != null && this.headers.length() > 0) {
            for (String h2 : this.headers.split("\n")) {
                int i2 = h2.indexOf(58);
                if (i2 > 0) {
                    c2.addRequestProperty(h2.substring(0, i2), h2.substring(i2 + 2));
                }
            }
        }
        if (c2 instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) c2;
            httpConnection.setRequestMethod(this.method);
            httpConnection.setInstanceFollowRedirects(false);
        }
    }

    private void sendRequest(URLConnection c2, boolean streaming) throws IOException {
        OutputStream out = null;
        try {
            long bytesToBeSent = 0;
            boolean sendFormData = (this.formDataElements == null || !(c2 instanceof HttpURLConnection) || this.method.equals(DELETE)) ? false : true;
            boolean isGetOrHead = this.method.equals(GET) || this.method.equals(HEAD);
            if (sendFormData) {
                c2.setDoOutput(true);
                for (FormDataElement formDataElement : this.formDataElements) {
                    formDataElement.open();
                    bytesToBeSent += formDataElement.getSize();
                }
                if (streaming) {
                    HttpURLConnection http = (HttpURLConnection) c2;
                    if (bytesToBeSent <= 2147483647L) {
                        http.setFixedLengthStreamingMode((int) bytesToBeSent);
                    } else {
                        http.setChunkedStreamingMode(0);
                    }
                }
            } else if (!isGetOrHead && (c2 instanceof HttpURLConnection)) {
                c2.setRequestProperty("Content-Length", "0");
            }
            int maxTryCount = isGetOrHead ? 3 : 1;
            c2.setConnectTimeout(c2.getConnectTimeout() / maxTryCount);
            int tryCount = 0;
            while (!this.canceled) {
                try {
                    try {
                        c2.connect();
                        break;
                    } catch (SocketTimeoutException ex) {
                        tryCount++;
                        if (tryCount >= maxTryCount) {
                            throw ex;
                        }
                    }
                } catch (IllegalArgumentException e2) {
                    throw new MalformedURLException(this.url);
                }
            }
            if (sendFormData) {
                OutputStream out2 = c2.getOutputStream();
                byte[] buffer = new byte[4096];
                long bytesSent = 0;
                for (FormDataElement formDataElement2 : this.formDataElements) {
                    InputStream in = formDataElement2.getInputStream();
                    while (true) {
                        int count = in.read(buffer);
                        if (count > 0) {
                            out2.write(buffer, 0, count);
                            bytesSent += count;
                            didSendData(bytesSent, bytesToBeSent);
                        }
                    }
                    formDataElement2.close();
                }
                out2.flush();
                out2.close();
                out = null;
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e3) {
                }
            }
            if (this.formDataElements == null || !(c2 instanceof HttpURLConnection)) {
                return;
            }
            for (FormDataElement formDataElement3 : this.formDataElements) {
                try {
                    formDataElement3.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    out.close();
                } catch (IOException e5) {
                }
            }
            if (this.formDataElements != null && (c2 instanceof HttpURLConnection)) {
                for (FormDataElement formDataElement4 : this.formDataElements) {
                    try {
                        formDataElement4.close();
                    } catch (IOException e6) {
                    }
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Finally extract failed */
    private Redirect receiveResponse(URLConnection c2) throws InterruptedException, IOException {
        InputStream inputStream;
        int count;
        URL newUrl;
        if (this.canceled) {
            return null;
        }
        InputStream errorStream = null;
        if (c2 instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) c2;
            int code = http.getResponseCode();
            if (code == -1) {
                throw new InvalidResponseException();
            }
            if (this.canceled) {
                return null;
            }
            switch (code) {
                case 301:
                case 302:
                case 303:
                case 307:
                    String newLoc = http.getHeaderField("Location");
                    if (newLoc != null) {
                        try {
                            newUrl = URLs.newURL(newLoc);
                        } catch (MalformedURLException e2) {
                            newUrl = URLs.newURL(c2.getURL(), newLoc);
                        }
                        return new Redirect(newUrl.toExternalForm(), code == 307);
                    }
                    if (code >= 400 && !this.method.equals(HEAD)) {
                        errorStream = http.getErrorStream();
                        break;
                    }
                    break;
                case 304:
                    didReceiveResponse(c2);
                    didFinishLoading();
                    return null;
                case 305:
                case 306:
                default:
                    if (code >= 400) {
                        errorStream = http.getErrorStream();
                        break;
                    }
                    break;
            }
        }
        if (this.url.startsWith("ftp:") || this.url.startsWith("ftps:")) {
            boolean dir = false;
            boolean notsure = false;
            String path = c2.getURL().getPath();
            if (path == null || path.isEmpty() || path.endsWith("/") || path.contains(";type=d")) {
                dir = true;
            } else {
                String type = c2.getContentType();
                if (Clipboard.TEXT_TYPE.equalsIgnoreCase(type) || Clipboard.HTML_TYPE.equalsIgnoreCase(type)) {
                    dir = true;
                    notsure = true;
                }
            }
            if (dir) {
                c2 = new DirectoryURLConnection(c2, notsure);
            }
        }
        if (this.url.startsWith("file:") && Clipboard.TEXT_TYPE.equals(c2.getContentType()) && c2.getHeaderField("content-length") == null) {
            c2 = new DirectoryURLConnection(c2);
        }
        didReceiveResponse(c2);
        if (this.method.equals(HEAD)) {
            didFinishLoading();
            return null;
        }
        InputStream inputStream2 = null;
        if (errorStream == null) {
            try {
                inputStream = c2.getInputStream();
            } catch (HttpRetryException ex) {
                throw ex;
            } catch (IOException e3) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, String.format("Exception caught: [%s], %s", e3.getClass().getSimpleName(), e3.getMessage()));
                }
            }
        } else {
            inputStream = errorStream;
        }
        inputStream2 = inputStream;
        String encoding = c2.getContentEncoding();
        if (inputStream2 != null) {
            try {
                if ("gzip".equalsIgnoreCase(encoding)) {
                    inputStream2 = new GZIPInputStream(inputStream2);
                } else if ("deflate".equalsIgnoreCase(encoding)) {
                    inputStream2 = new InflaterInputStream(inputStream2);
                }
            } catch (IOException e4) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, String.format("Exception caught: [%s], %s", e4.getClass().getSimpleName(), e4.getMessage()));
                }
            }
        }
        ByteBufferAllocator allocator = this.byteBufferPool.newAllocator(3);
        ByteBuffer byteBuffer = null;
        if (inputStream2 != null) {
            try {
                byte[] buffer = new byte[8192];
                while (!this.canceled) {
                    try {
                        count = inputStream2.read(buffer);
                    } catch (EOFException e5) {
                        count = -1;
                    }
                    if (count != -1) {
                        if (byteBuffer == null) {
                            byteBuffer = allocator.allocate();
                        }
                        int remaining = byteBuffer.remaining();
                        if (count < remaining) {
                            byteBuffer.put(buffer, 0, count);
                        } else {
                            byteBuffer.put(buffer, 0, remaining);
                            byteBuffer.flip();
                            didReceiveData(byteBuffer, allocator);
                            byteBuffer = null;
                            int outstanding = count - remaining;
                            if (outstanding > 0) {
                                byteBuffer = allocator.allocate();
                                byteBuffer.put(buffer, remaining, outstanding);
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                if (byteBuffer != null) {
                    byteBuffer.clear();
                    allocator.release(byteBuffer);
                }
                throw th;
            }
        }
        if (!this.canceled) {
            if (byteBuffer != null && byteBuffer.position() > 0) {
                byteBuffer.flip();
                didReceiveData(byteBuffer, allocator);
                byteBuffer = null;
            }
            didFinishLoading();
        }
        if (byteBuffer != null) {
            byteBuffer.clear();
            allocator.release(byteBuffer);
            return null;
        }
        return null;
    }

    private static void close(URLConnection c2) {
        InputStream errorStream;
        if ((c2 instanceof HttpURLConnection) && (errorStream = ((HttpURLConnection) c2).getErrorStream()) != null) {
            try {
                errorStream.close();
            } catch (IOException e2) {
            }
        }
        try {
            c2.getInputStream().close();
        } catch (IOException e3) {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/URLLoader$Redirect.class */
    private static final class Redirect {
        private final String url;
        private final boolean preserveRequest;

        private Redirect(String url, boolean preserveRequest) {
            this.url = url;
            this.preserveRequest = preserveRequest;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/URLLoader$InvalidResponseException.class */
    private static final class InvalidResponseException extends IOException {
        private InvalidResponseException() {
            super("Invalid server response");
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/network/URLLoader$TooManyRedirectsException.class */
    private static final class TooManyRedirectsException extends IOException {
        private TooManyRedirectsException() {
            super("Too many redirects");
        }
    }

    private void didSendData(long totalBytesSent, long totalBytesToBeSent) {
        callBack(() -> {
            if (!this.canceled) {
                notifyDidSendData(totalBytesSent, totalBytesToBeSent);
            }
        });
    }

    private void notifyDidSendData(long totalBytesSent, long totalBytesToBeSent) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("totalBytesSent: [%d], totalBytesToBeSent: [%d], data: [0x%016X]", Long.valueOf(totalBytesSent), Long.valueOf(totalBytesToBeSent), Long.valueOf(this.data)));
        }
        twkDidSendData(totalBytesSent, totalBytesToBeSent, this.data);
    }

    private void willSendRequest(String newUrl, String newMethod, URLConnection c2) throws InterruptedException {
        String adjustedNewUrl = adjustUrlForWebKit(newUrl);
        int status = extractStatus(c2);
        String contentType = c2.getContentType();
        String contentEncoding = extractContentEncoding(c2);
        long contentLength = extractContentLength(c2);
        String responseHeaders = extractHeaders(c2);
        String adjustedUrl = adjustUrlForWebKit(this.url);
        CountDownLatch latch = this.asynchronous ? new CountDownLatch(1) : null;
        callBack(() -> {
            try {
                if (!this.canceled) {
                    boolean keepGoing = notifyWillSendRequest(adjustedNewUrl, newMethod, status, contentType, contentEncoding, contentLength, responseHeaders, adjustedUrl);
                    if (!keepGoing) {
                        fwkCancel();
                    }
                }
            } finally {
                if (latch != null) {
                    latch.countDown();
                }
            }
        });
        if (latch != null) {
            latch.await();
        }
    }

    private boolean notifyWillSendRequest(String newUrl, String newMethod, int status, String contentType, String contentEncoding, long contentLength, String headers, String url) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("newUrl: [%s], newMethod: [%s], status: [%d], contentType: [%s], contentEncoding: [%s], contentLength: [%d], url: [%s], data: [0x%016X], headers:%n%s", newUrl, newMethod, Integer.valueOf(status), contentType, contentEncoding, Long.valueOf(contentLength), url, Long.valueOf(this.data), Util.formatHeaders(headers)));
        }
        boolean result = twkWillSendRequest(newUrl, newMethod, status, contentType, contentEncoding, contentLength, headers, url, this.data);
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("result: [%s]", Boolean.valueOf(result)));
        }
        return result;
    }

    private void didReceiveResponse(URLConnection c2) {
        int status = extractStatus(c2);
        String contentType = c2.getContentType();
        String contentEncoding = extractContentEncoding(c2);
        long contentLength = extractContentLength(c2);
        String responseHeaders = extractHeaders(c2);
        String adjustedUrl = adjustUrlForWebKit(this.url);
        callBack(() -> {
            if (!this.canceled) {
                notifyDidReceiveResponse(status, contentType, contentEncoding, contentLength, responseHeaders, adjustedUrl);
            }
        });
    }

    private void notifyDidReceiveResponse(int status, String contentType, String contentEncoding, long contentLength, String headers, String url) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("status: [%d], contentType: [%s], contentEncoding: [%s], contentLength: [%d], url: [%s], data: [0x%016X], headers:%n%s", Integer.valueOf(status), contentType, contentEncoding, Long.valueOf(contentLength), url, Long.valueOf(this.data), Util.formatHeaders(headers)));
        }
        twkDidReceiveResponse(status, contentType, contentEncoding, contentLength, headers, url, this.data);
    }

    private void didReceiveData(ByteBuffer byteBuffer, ByteBufferAllocator allocator) {
        callBack(() -> {
            if (!this.canceled) {
                notifyDidReceiveData(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
            }
            byteBuffer.clear();
            allocator.release(byteBuffer);
        });
    }

    private void notifyDidReceiveData(ByteBuffer byteBuffer, int position, int remaining) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("byteBuffer: [%s], position: [%s], remaining: [%s], data: [0x%016X]", byteBuffer, Integer.valueOf(position), Integer.valueOf(remaining), Long.valueOf(this.data)));
        }
        twkDidReceiveData(byteBuffer, position, remaining, this.data);
    }

    private void didFinishLoading() {
        callBack(() -> {
            if (!this.canceled) {
                notifyDidFinishLoading();
            }
        });
    }

    private void notifyDidFinishLoading() {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("data: [0x%016X]", Long.valueOf(this.data)));
        }
        twkDidFinishLoading(this.data);
    }

    private void didFail(int errorCode, String message) {
        String adjustedUrl = adjustUrlForWebKit(this.url);
        callBack(() -> {
            if (!this.canceled) {
                notifyDidFail(errorCode, adjustedUrl, message);
            }
        });
    }

    private void notifyDidFail(int errorCode, String url, String message) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, String.format("errorCode: [%d], url: [%s], message: [%s], data: [0x%016X]", Integer.valueOf(errorCode), url, message, Long.valueOf(this.data)));
        }
        twkDidFail(errorCode, url, message, this.data);
    }

    private void callBack(Runnable runnable) {
        if (this.asynchronous) {
            Invoker.getInvoker().invokeOnEventThread(runnable);
        } else {
            runnable.run();
        }
    }

    private static int extractStatus(URLConnection c2) {
        int status = 0;
        if (c2 instanceof HttpURLConnection) {
            try {
                status = ((HttpURLConnection) c2).getResponseCode();
            } catch (IOException e2) {
            }
        }
        return status;
    }

    private static String extractContentEncoding(URLConnection c2) {
        int i2;
        String contentEncoding = c2.getContentEncoding();
        if ("gzip".equalsIgnoreCase(contentEncoding) || "deflate".equalsIgnoreCase(contentEncoding)) {
            contentEncoding = null;
            String contentType = c2.getContentType();
            if (contentType != null && (i2 = contentType.indexOf("charset=")) >= 0) {
                contentEncoding = contentType.substring(i2 + 8);
                int i3 = contentEncoding.indexOf(";");
                if (i3 > 0) {
                    contentEncoding = contentEncoding.substring(0, i3);
                }
            }
        }
        return contentEncoding;
    }

    private static long extractContentLength(URLConnection c2) {
        try {
            return Long.parseLong(c2.getHeaderField("content-length"));
        } catch (Exception e2) {
            return -1L;
        }
    }

    private static String extractHeaders(URLConnection c2) {
        StringBuilder sb = new StringBuilder();
        Map<String, List<String>> headers = c2.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            for (String value : values) {
                sb.append(key != null ? key : "");
                sb.append(':').append(value).append('\n');
            }
        }
        return sb.toString();
    }

    private static String adjustUrlForWebKit(String url) {
        try {
            url = Util.adjustUrlForWebKit(url);
        } catch (Exception e2) {
        }
        return url;
    }
}
