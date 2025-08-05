package sun.net.www;

import com.sun.glass.ui.Clipboard;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:sun/net/www/URLConnection.class */
public abstract class URLConnection extends java.net.URLConnection {
    private String contentType;
    private int contentLength;
    protected MessageHeader properties;
    private static HashMap<String, Void> proxiedHosts = new HashMap<>();

    public URLConnection(URL url) {
        super(url);
        this.contentLength = -1;
        this.properties = new MessageHeader();
    }

    public MessageHeader getProperties() {
        return this.properties;
    }

    public void setProperties(MessageHeader messageHeader) {
        this.properties = messageHeader;
    }

    @Override // java.net.URLConnection
    public void setRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalAccessError("Already connected");
        }
        if (str == null) {
            throw new NullPointerException("key cannot be null");
        }
        this.properties.set(str, str2);
    }

    @Override // java.net.URLConnection
    public void addRequestProperty(String str, String str2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (str == null) {
            throw new NullPointerException("key is null");
        }
    }

    @Override // java.net.URLConnection
    public String getRequestProperty(String str) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        return null;
    }

    @Override // java.net.URLConnection
    public Map<String, List<String>> getRequestProperties() {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        return Collections.emptyMap();
    }

    @Override // java.net.URLConnection
    public String getHeaderField(String str) {
        try {
            getInputStream();
            if (this.properties == null) {
                return null;
            }
            return this.properties.findValue(str);
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public String getHeaderFieldKey(int i2) {
        try {
            getInputStream();
            MessageHeader messageHeader = this.properties;
            if (messageHeader == null) {
                return null;
            }
            return messageHeader.getKey(i2);
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public String getHeaderField(int i2) {
        try {
            getInputStream();
            MessageHeader messageHeader = this.properties;
            if (messageHeader == null) {
                return null;
            }
            return messageHeader.getValue(i2);
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // java.net.URLConnection
    public String getContentType() {
        if (this.contentType == null) {
            this.contentType = getHeaderField("content-type");
        }
        if (this.contentType == null) {
            String strFindValue = null;
            try {
                strFindValue = guessContentTypeFromStream(getInputStream());
            } catch (IOException e2) {
            }
            String strFindValue2 = this.properties.findValue("content-encoding");
            if (strFindValue == null) {
                strFindValue = this.properties.findValue("content-type");
                if (strFindValue == null) {
                    if (this.url.getFile().endsWith("/")) {
                        strFindValue = Clipboard.HTML_TYPE;
                    } else {
                        strFindValue = guessContentTypeFromName(this.url.getFile());
                    }
                }
            }
            if (strFindValue == null || (strFindValue2 != null && !strFindValue2.equalsIgnoreCase("7bit") && !strFindValue2.equalsIgnoreCase("8bit") && !strFindValue2.equalsIgnoreCase("binary"))) {
                strFindValue = "content/unknown";
            }
            setContentType(strFindValue);
        }
        return this.contentType;
    }

    public void setContentType(String str) {
        this.contentType = str;
        this.properties.set("content-type", str);
    }

    @Override // java.net.URLConnection
    public int getContentLength() {
        try {
            getInputStream();
            int i2 = this.contentLength;
            if (i2 < 0) {
                try {
                    i2 = Integer.parseInt(this.properties.findValue("content-length"));
                    setContentLength(i2);
                } catch (Exception e2) {
                }
            }
            return i2;
        } catch (Exception e3) {
            return -1;
        }
    }

    protected void setContentLength(int i2) {
        this.contentLength = i2;
        this.properties.set("content-length", String.valueOf(i2));
    }

    public boolean canCache() {
        return this.url.getFile().indexOf(63) < 0;
    }

    public void close() {
        this.url = null;
    }

    public static synchronized void setProxiedHost(String str) {
        proxiedHosts.put(str.toLowerCase(), null);
    }

    public static synchronized boolean isProxiedHost(String str) {
        return proxiedHosts.containsKey(str.toLowerCase());
    }
}
