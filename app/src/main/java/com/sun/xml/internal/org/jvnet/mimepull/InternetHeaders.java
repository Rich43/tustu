package com.sun.xml.internal.org.jvnet.mimepull;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEParser;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/InternetHeaders.class */
final class InternetHeaders {
    private final FinalArrayList<Hdr> headers = new FinalArrayList<>();

    InternetHeaders(MIMEParser.LineInputStream lis) {
        String line;
        String prevline = null;
        StringBuilder lineBuffer = new StringBuilder();
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
                throw new MIMEParsingException("Error in input stream", ioex);
            }
        } while (line.length() > 0);
    }

    List<String> getHeader(String name) {
        FinalArrayList<String> v2 = new FinalArrayList<>();
        int len = this.headers.size();
        for (int i2 = 0; i2 < len; i2++) {
            Hdr h2 = this.headers.get(i2);
            if (name.equalsIgnoreCase(h2.name)) {
                v2.add(h2.getValue());
            }
        }
        if (v2.size() == 0) {
            return null;
        }
        return v2;
    }

    FinalArrayList<? extends Header> getAllHeaders() {
        return this.headers;
    }

    void addHeaderLine(String line) {
        try {
            char c2 = line.charAt(0);
            if (c2 == ' ' || c2 == '\t') {
                Hdr h2 = this.headers.get(this.headers.size() - 1);
                h2.line += "\r\n" + line;
            } else {
                this.headers.add(new Hdr(line));
            }
        } catch (StringIndexOutOfBoundsException e2) {
        } catch (NoSuchElementException e3) {
        }
    }
}
