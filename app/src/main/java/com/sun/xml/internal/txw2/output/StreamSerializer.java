package com.sun.xml.internal.txw2.output;

import com.sun.xml.internal.txw2.TxwException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/StreamSerializer.class */
public class StreamSerializer implements XmlSerializer {
    private final SaxSerializer serializer;
    private final XMLWriter writer;

    public StreamSerializer(OutputStream out) {
        this(createWriter(out));
    }

    public StreamSerializer(OutputStream out, String encoding) throws UnsupportedEncodingException {
        this(createWriter(out, encoding));
    }

    public StreamSerializer(Writer out) {
        this(new StreamResult(out));
    }

    public StreamSerializer(StreamResult streamResult) {
        final OutputStream[] autoClose = new OutputStream[1];
        if (streamResult.getWriter() != null) {
            this.writer = createWriter(streamResult.getWriter());
        } else if (streamResult.getOutputStream() != null) {
            this.writer = createWriter(streamResult.getOutputStream());
        } else if (streamResult.getSystemId() != null) {
            String fileURL = streamResult.getSystemId();
            try {
                FileOutputStream fos = new FileOutputStream(convertURL(fileURL));
                autoClose[0] = fos;
                this.writer = createWriter(fos);
            } catch (IOException e2) {
                throw new TxwException(e2);
            }
        } else {
            throw new IllegalArgumentException();
        }
        this.serializer = new SaxSerializer(this.writer, this.writer, false) { // from class: com.sun.xml.internal.txw2.output.StreamSerializer.1
            @Override // com.sun.xml.internal.txw2.output.SaxSerializer, com.sun.xml.internal.txw2.output.XmlSerializer
            public void endDocument() {
                super.endDocument();
                if (autoClose[0] != null) {
                    try {
                        autoClose[0].close();
                        autoClose[0] = null;
                    } catch (IOException e3) {
                        throw new TxwException(e3);
                    }
                }
            }
        };
    }

    private StreamSerializer(XMLWriter writer) {
        this.writer = writer;
        this.serializer = new SaxSerializer(writer, writer, false);
    }

    private String convertURL(String url) {
        String url2 = url.replace('\\', '/').replaceAll("//", "/").replaceAll("//", "/");
        if (url2.startsWith("file:/")) {
            if (url2.substring(6).indexOf(CallSiteDescriptor.TOKEN_DELIMITER) > 0) {
                url2 = url2.substring(6);
            } else {
                url2 = url2.substring(5);
            }
        }
        return url2;
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void startDocument() {
        this.serializer.startDocument();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void beginStartTag(String uri, String localName, String prefix) {
        this.serializer.beginStartTag(uri, localName, prefix);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
        this.serializer.writeAttribute(uri, localName, prefix, value);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeXmlns(String prefix, String uri) {
        this.serializer.writeXmlns(prefix, uri);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endStartTag(String uri, String localName, String prefix) {
        this.serializer.endStartTag(uri, localName, prefix);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endTag() {
        this.serializer.endTag();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void text(StringBuilder text) {
        this.serializer.text(text);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void cdata(StringBuilder text) {
        this.serializer.cdata(text);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void comment(StringBuilder comment) {
        this.serializer.comment(comment);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endDocument() {
        this.serializer.endDocument();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void flush() {
        this.serializer.flush();
        try {
            this.writer.flush();
        } catch (IOException e2) {
            throw new TxwException(e2);
        }
    }

    private static XMLWriter createWriter(Writer w2) {
        DataWriter dw = new DataWriter(new BufferedWriter(w2));
        dw.setIndentStep(Constants.INDENT);
        return dw;
    }

    private static XMLWriter createWriter(OutputStream os, String encoding) throws UnsupportedEncodingException {
        XMLWriter writer = createWriter(new OutputStreamWriter(os, encoding));
        writer.setEncoding(encoding);
        return writer;
    }

    private static XMLWriter createWriter(OutputStream os) {
        try {
            return createWriter(os, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            throw new Error(e2);
        }
    }
}
