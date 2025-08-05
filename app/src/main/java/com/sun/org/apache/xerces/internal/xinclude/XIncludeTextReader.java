package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import com.sun.org.apache.xerces.internal.util.EncodingMap;
import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XIncludeTextReader.class */
public class XIncludeTextReader {
    private Reader fReader;
    private XIncludeHandler fHandler;
    private XMLInputSource fSource;
    private XMLErrorReporter fErrorReporter;
    private XMLString fTempString;

    public XIncludeTextReader(XMLInputSource source, XIncludeHandler handler, int bufferSize) throws IOException {
        this.fTempString = new XMLString();
        this.fHandler = handler;
        this.fSource = source;
        this.fTempString = new XMLString(new char[bufferSize + 1], 0, 0);
    }

    public void setErrorReporter(XMLErrorReporter errorReporter) {
        this.fErrorReporter = errorReporter;
    }

    protected Reader getReader(XMLInputSource source) throws IOException {
        InputStream stream;
        String contentType;
        if (source.getCharacterStream() != null) {
            return source.getCharacterStream();
        }
        String encoding = source.getEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        if (source.getByteStream() != null) {
            stream = source.getByteStream();
            if (!(stream instanceof BufferedInputStream)) {
                stream = new BufferedInputStream(stream, this.fTempString.ch.length);
            }
        } else {
            String expandedSystemId = XMLEntityManager.expandSystemId(source.getSystemId(), source.getBaseSystemId(), false);
            URL url = new URL(expandedSystemId);
            URLConnection urlCon = url.openConnection();
            if ((urlCon instanceof HttpURLConnection) && (source instanceof HTTPInputSource)) {
                HttpURLConnection urlConnection = (HttpURLConnection) urlCon;
                HTTPInputSource httpInputSource = (HTTPInputSource) source;
                Iterator propIter = httpInputSource.getHTTPRequestProperties();
                while (propIter.hasNext()) {
                    Map.Entry<String, String> entry = propIter.next();
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
                boolean followRedirects = httpInputSource.getFollowHTTPRedirects();
                if (!followRedirects) {
                    XMLEntityManager.setInstanceFollowRedirects(urlConnection, followRedirects);
                }
            }
            stream = new BufferedInputStream(urlCon.getInputStream());
            String rawContentType = urlCon.getContentType();
            int index = rawContentType != null ? rawContentType.indexOf(59) : -1;
            String charset = null;
            if (index != -1) {
                contentType = rawContentType.substring(0, index).trim();
                String charset2 = rawContentType.substring(index + 1).trim();
                if (charset2.startsWith("charset=")) {
                    charset = charset2.substring(8).trim();
                    if ((charset.charAt(0) == '\"' && charset.charAt(charset.length() - 1) == '\"') || (charset.charAt(0) == '\'' && charset.charAt(charset.length() - 1) == '\'')) {
                        charset = charset.substring(1, charset.length() - 1);
                    }
                } else {
                    charset = null;
                }
            } else {
                contentType = rawContentType.trim();
            }
            String detectedEncoding = null;
            if (contentType.equals("text/xml")) {
                if (charset != null) {
                    detectedEncoding = charset;
                } else {
                    detectedEncoding = "US-ASCII";
                }
            } else if (contentType.equals(XMLCodec.XML_APPLICATION_MIME_TYPE)) {
                if (charset != null) {
                    detectedEncoding = charset;
                } else {
                    detectedEncoding = getEncodingName(stream);
                }
            } else if (contentType.endsWith("+xml")) {
                detectedEncoding = getEncodingName(stream);
            }
            if (detectedEncoding != null) {
                encoding = detectedEncoding;
            }
        }
        String encoding2 = consumeBOM(stream, encoding.toUpperCase(Locale.ENGLISH));
        if (encoding2.equals("UTF-8")) {
            return new UTF8Reader(stream, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        String javaEncoding = EncodingMap.getIANA2JavaMapping(encoding2);
        if (javaEncoding == null) {
            MessageFormatter aFormatter = this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210");
            Locale aLocale = this.fErrorReporter.getLocale();
            throw new IOException(aFormatter.formatMessage(aLocale, "EncodingDeclInvalid", new Object[]{encoding2}));
        }
        if (javaEncoding.equals("ASCII")) {
            return new ASCIIReader(stream, this.fTempString.ch.length, this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210"), this.fErrorReporter.getLocale());
        }
        return new InputStreamReader(stream, javaEncoding);
    }

    protected String getEncodingName(InputStream stream) throws IOException {
        byte[] b4 = new byte[4];
        String encoding = null;
        stream.mark(4);
        int count = stream.read(b4, 0, 4);
        stream.reset();
        if (count == 4) {
            encoding = getEncodingName(b4);
        }
        return encoding;
    }

    protected String consumeBOM(InputStream stream, String encoding) throws IOException {
        byte[] b2 = new byte[3];
        stream.mark(3);
        if (encoding.equals("UTF-8")) {
            int count = stream.read(b2, 0, 3);
            if (count == 3) {
                int b0 = b2[0] & 255;
                int b1 = b2[1] & 255;
                int b22 = b2[2] & 255;
                if (b0 != 239 || b1 != 187 || b22 != 191) {
                    stream.reset();
                }
            } else {
                stream.reset();
            }
        } else if (encoding.startsWith("UTF-16")) {
            int count2 = stream.read(b2, 0, 2);
            if (count2 == 2) {
                int b02 = b2[0] & 255;
                int b12 = b2[1] & 255;
                if (b02 == 254 && b12 == 255) {
                    return FastInfosetSerializer.UTF_16BE;
                }
                if (b02 == 255 && b12 == 254) {
                    return "UTF-16LE";
                }
            }
            stream.reset();
        }
        return encoding;
    }

    protected String getEncodingName(byte[] b4) {
        int b0 = b4[0] & 255;
        int b1 = b4[1] & 255;
        if (b0 == 254 && b1 == 255) {
            return FastInfosetSerializer.UTF_16BE;
        }
        if (b0 == 255 && b1 == 254) {
            return "UTF-16LE";
        }
        int b2 = b4[2] & 255;
        if (b0 == 239 && b1 == 187 && b2 == 191) {
            return "UTF-8";
        }
        int b3 = b4[3] & 255;
        if (b0 == 0 && b1 == 0 && b2 == 0 && b3 == 60) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 60 && b1 == 0 && b2 == 0 && b3 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0 && b1 == 0 && b2 == 60 && b3 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 0) {
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 63) {
            return FastInfosetSerializer.UTF_16BE;
        }
        if (b0 == 60 && b1 == 0 && b2 == 63 && b3 == 0) {
            return "UTF-16LE";
        }
        if (b0 == 76 && b1 == 111 && b2 == 167 && b3 == 148) {
            return "CP037";
        }
        return null;
    }

    public void parse() throws IOException, XNIException {
        int ch2;
        this.fReader = getReader(this.fSource);
        this.fSource = null;
        int i2 = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1);
        while (true) {
            int readSize = i2;
            if (readSize != -1) {
                int i3 = 0;
                while (i3 < readSize) {
                    char ch = this.fTempString.ch[i3];
                    if (!isValid(ch)) {
                        if (XMLChar.isHighSurrogate(ch)) {
                            i3++;
                            if (i3 < readSize) {
                                ch2 = this.fTempString.ch[i3];
                            } else {
                                ch2 = this.fReader.read();
                                if (ch2 != -1) {
                                    int i4 = readSize;
                                    readSize++;
                                    this.fTempString.ch[i4] = (char) ch2;
                                }
                            }
                            if (XMLChar.isLowSurrogate(ch2)) {
                                int sup = XMLChar.supplemental(ch, (char) ch2);
                                if (!isValid(sup)) {
                                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(sup, 16)}, (short) 2);
                                }
                            } else {
                                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(ch2, 16)}, (short) 2);
                            }
                        } else {
                            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInContent", new Object[]{Integer.toString(ch, 16)}, (short) 2);
                        }
                    }
                    i3++;
                }
                if (this.fHandler != null && readSize > 0) {
                    this.fTempString.offset = 0;
                    this.fTempString.length = readSize;
                    this.fHandler.characters(this.fTempString, this.fHandler.modifyAugmentations(null, true));
                }
                i2 = this.fReader.read(this.fTempString.ch, 0, this.fTempString.ch.length - 1);
            } else {
                return;
            }
        }
    }

    public void setInputSource(XMLInputSource source) {
        this.fSource = source;
    }

    public void close() throws IOException {
        if (this.fReader != null) {
            this.fReader.close();
            this.fReader = null;
        }
    }

    protected boolean isValid(int ch) {
        return XMLChar.isValid(ch);
    }

    protected void setBufferSize(int bufferSize) {
        int bufferSize2 = bufferSize + 1;
        if (this.fTempString.ch.length != bufferSize2) {
            this.fTempString.ch = new char[bufferSize2];
        }
    }
}
