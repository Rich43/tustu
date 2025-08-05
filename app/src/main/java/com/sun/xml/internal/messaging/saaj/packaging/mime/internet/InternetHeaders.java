package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.List;
import java.util.NoSuchElementException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/InternetHeaders.class */
public final class InternetHeaders {
    private final FinalArrayList headers = new FinalArrayList();
    private List headerValueView;

    public InternetHeaders() {
    }

    public InternetHeaders(InputStream is) throws MessagingException {
        load(is);
    }

    public void load(InputStream is) throws MessagingException {
        String line;
        LineInputStream lis = new LineInputStream(is);
        String prevline = null;
        StringBuffer lineBuffer = new StringBuffer();
        do {
            try {
                line = lis.readLine();
                if (line != null && (line.startsWith(" ") || line.startsWith("\t"))) {
                    if (prevline != null) {
                        lineBuffer.append(prevline);
                        prevline = null;
                    }
                    lineBuffer.append("\r\n");
                    lineBuffer.append(line);
                } else {
                    if (prevline != null) {
                        addHeaderLine(prevline);
                    } else if (lineBuffer.length() > 0) {
                        addHeaderLine(lineBuffer.toString());
                        lineBuffer.setLength(0);
                    }
                    prevline = line;
                }
                if (line == null) {
                    break;
                }
            } catch (IOException ioex) {
                throw new MessagingException("Error in input stream", ioex);
            }
        } while (line.length() > 0);
    }

    public String[] getHeader(String name) {
        FinalArrayList v2 = new FinalArrayList();
        int len = this.headers.size();
        for (int i2 = 0; i2 < len; i2++) {
            hdr h2 = (hdr) this.headers.get(i2);
            if (name.equalsIgnoreCase(h2.name)) {
                v2.add(h2.getValue());
            }
        }
        if (v2.size() == 0) {
            return null;
        }
        return (String[]) v2.toArray(new String[v2.size()]);
    }

    public String getHeader(String name, String delimiter) {
        String[] s2 = getHeader(name);
        if (s2 == null) {
            return null;
        }
        if (s2.length == 1 || delimiter == null) {
            return s2[0];
        }
        StringBuffer r2 = new StringBuffer(s2[0]);
        for (int i2 = 1; i2 < s2.length; i2++) {
            r2.append(delimiter);
            r2.append(s2[i2]);
        }
        return r2.toString();
    }

    public void setHeader(String name, String value) {
        int j2;
        boolean found = false;
        int i2 = 0;
        while (i2 < this.headers.size()) {
            hdr h2 = (hdr) this.headers.get(i2);
            if (name.equalsIgnoreCase(h2.name)) {
                if (!found) {
                    if (h2.line != null && (j2 = h2.line.indexOf(58)) >= 0) {
                        h2.line = h2.line.substring(0, j2 + 1) + " " + value;
                    } else {
                        h2.line = name + ": " + value;
                    }
                    found = true;
                } else {
                    this.headers.remove(i2);
                    i2--;
                }
            }
            i2++;
        }
        if (!found) {
            addHeader(name, value);
        }
    }

    public void addHeader(String name, String value) {
        int pos = this.headers.size();
        for (int i2 = this.headers.size() - 1; i2 >= 0; i2--) {
            hdr h2 = (hdr) this.headers.get(i2);
            if (name.equalsIgnoreCase(h2.name)) {
                this.headers.add(i2 + 1, new hdr(name, value));
                return;
            } else {
                if (h2.name.equals(CallSiteDescriptor.TOKEN_DELIMITER)) {
                    pos = i2;
                }
            }
        }
        this.headers.add(pos, new hdr(name, value));
    }

    public void removeHeader(String name) {
        int i2 = 0;
        while (i2 < this.headers.size()) {
            hdr h2 = (hdr) this.headers.get(i2);
            if (name.equalsIgnoreCase(h2.name)) {
                this.headers.remove(i2);
                i2--;
            }
            i2++;
        }
    }

    public FinalArrayList getAllHeaders() {
        return this.headers;
    }

    public void addHeaderLine(String line) {
        try {
            char c2 = line.charAt(0);
            if (c2 == ' ' || c2 == '\t') {
                hdr h2 = (hdr) this.headers.get(this.headers.size() - 1);
                h2.line += "\r\n" + line;
            } else {
                this.headers.add(new hdr(line));
            }
        } catch (StringIndexOutOfBoundsException e2) {
        } catch (NoSuchElementException e3) {
        }
    }

    public List getAllHeaderLines() {
        if (this.headerValueView == null) {
            this.headerValueView = new AbstractList() { // from class: com.sun.xml.internal.messaging.saaj.packaging.mime.internet.InternetHeaders.1
                @Override // java.util.AbstractList, java.util.List
                public Object get(int index) {
                    return ((hdr) InternetHeaders.this.headers.get(index)).line;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return InternetHeaders.this.headers.size();
                }
            };
        }
        return this.headerValueView;
    }
}
