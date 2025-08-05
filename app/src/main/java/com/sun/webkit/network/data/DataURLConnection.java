package com.sun.webkit.network.data;

import com.sun.glass.ui.Clipboard;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: jfxrt.jar:com/sun/webkit/network/data/DataURLConnection.class */
final class DataURLConnection extends URLConnection {
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private final String mediaType;
    private final byte[] data;
    private final InputStream inputStream;

    DataURLConnection(URL url) throws IOException {
        super(url);
        String content = url.toString();
        String content2 = content.substring(content.indexOf(58) + 1);
        int commaPosition = content2.indexOf(44);
        if (commaPosition < 0) {
            throw new ProtocolException("Invalid URL, ',' not found in: " + ((Object) getURL()));
        }
        String metadata = content2.substring(0, commaPosition);
        String dataString = content2.substring(commaPosition + 1);
        String mimeType = null;
        LinkedList<String> parameters = new LinkedList<>();
        Charset charset = null;
        boolean base64 = false;
        String[] components = metadata.split(";", -1);
        for (int i2 = 0; i2 < components.length; i2++) {
            String component = components[i2];
            if (component.equalsIgnoreCase("base64")) {
                base64 = true;
            } else if (i2 == 0 && !component.contains("=")) {
                mimeType = component;
            } else {
                parameters.add(component);
                if (component.toLowerCase().startsWith("charset=")) {
                    try {
                        charset = Charset.forName(component.substring(8));
                    } catch (IllegalArgumentException ex) {
                        UnsupportedEncodingException ex2 = new UnsupportedEncodingException();
                        ex2.initCause(ex);
                        throw ex2;
                    }
                } else {
                    continue;
                }
            }
        }
        mimeType = (mimeType == null || mimeType.isEmpty()) ? Clipboard.TEXT_TYPE : mimeType;
        if (charset == null) {
            charset = US_ASCII;
            if (mimeType.toLowerCase().startsWith("text/")) {
                parameters.addFirst("charset=" + charset.name());
            }
        }
        StringBuilder mediaTypeBuilder = new StringBuilder();
        mediaTypeBuilder.append(mimeType);
        Iterator<String> it = parameters.iterator();
        while (it.hasNext()) {
            String parameter = it.next();
            mediaTypeBuilder.append(';').append(parameter);
        }
        this.mediaType = mediaTypeBuilder.toString();
        if (base64) {
            String s2 = urlDecode(dataString, US_ASCII);
            this.data = Base64.getMimeDecoder().decode(s2.replaceAll("\\s+", ""));
        } else {
            String s3 = urlDecode(dataString, charset);
            this.data = s3.getBytes(charset);
        }
        this.inputStream = new ByteArrayInputStream(this.data);
    }

    @Override // java.net.URLConnection
    public void connect() {
        this.connected = true;
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        return this.mediaType;
    }

    @Override // java.net.URLConnection
    public String getContentEncoding() {
        return null;
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        if (this.data != null) {
            return this.data.length;
        }
        return -1;
    }

    private static String urlDecode(String str, Charset charset) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        byte[] bytes = null;
        int i2 = 0;
        while (i2 < length) {
            char c2 = str.charAt(i2);
            if (c2 == '%') {
                if (bytes == null) {
                    bytes = new byte[(length - i2) / 3];
                }
                int count = 0;
                int proceedTo = i2;
                while (true) {
                    if (i2 >= length || str.charAt(i2) != '%') {
                        break;
                    }
                    if (i2 + 2 >= length) {
                        proceedTo = length;
                        break;
                    }
                    try {
                        byte b2 = (byte) Integer.parseInt(str.substring(i2 + 1, i2 + 3), 16);
                        int i3 = count;
                        count++;
                        bytes[i3] = b2;
                        i2 += 3;
                    } catch (NumberFormatException e2) {
                        proceedTo = i2 + 3;
                    }
                }
                if (count > 0) {
                    sb.append(new String(bytes, 0, count, charset));
                }
                while (i2 < proceedTo) {
                    int i4 = i2;
                    i2++;
                    sb.append(str.charAt(i4));
                }
            } else {
                sb.append(c2);
                i2++;
            }
        }
        return sb.toString();
    }
}
