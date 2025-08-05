package sun.security.timestamp;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sun.misc.IOUtils;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/timestamp/HttpTimestamper.class */
public class HttpTimestamper implements Timestamper {
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String TS_QUERY_MIME_TYPE = "application/timestamp-query";
    private static final String TS_REPLY_MIME_TYPE = "application/timestamp-reply";
    private static final Debug debug = Debug.getInstance("ts");
    private URI tsaURI;

    public HttpTimestamper(URI uri) {
        this.tsaURI = null;
        if (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https")) {
            throw new IllegalArgumentException("TSA must be an HTTP or HTTPS URI");
        }
        this.tsaURI = uri;
    }

    @Override // sun.security.timestamp.Timestamper
    public TSResponse generateTimestamp(TSRequest tSRequest) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) this.tsaURI.toURL().openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestProperty("Content-Type", TS_QUERY_MIME_TYPE);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
        if (debug != null) {
            Set<Map.Entry<String, List<String>>> setEntrySet = httpURLConnection.getRequestProperties().entrySet();
            debug.println(httpURLConnection.getRequestMethod() + " " + ((Object) this.tsaURI) + " HTTP/1.1");
            Iterator<Map.Entry<String, List<String>>> it = setEntrySet.iterator();
            while (it.hasNext()) {
                debug.println(Constants.INDENT + ((Object) it.next()));
            }
            debug.println();
        }
        httpURLConnection.connect();
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            byte[] bArrEncode = tSRequest.encode();
            dataOutputStream.write(bArrEncode, 0, bArrEncode.length);
            dataOutputStream.flush();
            if (debug != null) {
                debug.println("sent timestamp query (length=" + bArrEncode.length + ")");
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            AutoCloseable autoCloseable = null;
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                if (debug != null) {
                    debug.println(httpURLConnection.getHeaderField(0));
                    int i2 = 1;
                    while (true) {
                        String headerField = httpURLConnection.getHeaderField(i2);
                        if (headerField == null) {
                            break;
                        }
                        String headerFieldKey = httpURLConnection.getHeaderFieldKey(i2);
                        debug.println(Constants.INDENT + (headerFieldKey == null ? "" : headerFieldKey + ": ") + headerField);
                        i2++;
                    }
                    debug.println();
                }
                verifyMimeType(httpURLConnection.getContentType());
                int contentLength = httpURLConnection.getContentLength();
                byte[] allBytes = IOUtils.readAllBytes(bufferedInputStream);
                if (contentLength != -1 && allBytes.length != contentLength) {
                    throw new EOFException("Expected:" + contentLength + ", read:" + allBytes.length);
                }
                if (debug != null) {
                    debug.println("received timestamp response (length=" + allBytes.length + ")");
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                return new TSResponse(allBytes);
            } catch (Throwable th) {
                if (0 != 0) {
                    autoCloseable.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            throw th2;
        }
    }

    private static void verifyMimeType(String str) throws IOException {
        if (!TS_REPLY_MIME_TYPE.equalsIgnoreCase(str)) {
            throw new IOException("MIME Content-Type is not application/timestamp-reply");
        }
    }
}
