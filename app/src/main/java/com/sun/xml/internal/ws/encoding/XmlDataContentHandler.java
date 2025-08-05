package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/XmlDataContentHandler.class */
public class XmlDataContentHandler implements DataContentHandler {
    private final DataFlavor[] flavors = new DataFlavor[3];

    public XmlDataContentHandler() throws ClassNotFoundException {
        this.flavors[0] = new ActivationDataFlavor(StreamSource.class, "text/xml", "XML");
        this.flavors[1] = new ActivationDataFlavor(StreamSource.class, XMLCodec.XML_APPLICATION_MIME_TYPE, "XML");
        this.flavors[2] = new ActivationDataFlavor(String.class, "text/xml", "XML String");
    }

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) Arrays.copyOf(this.flavors, this.flavors.length);
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        for (DataFlavor aFlavor : this.flavors) {
            if (aFlavor.equals(df)) {
                return getContent(ds);
            }
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) throws IOException {
        String ctStr = ds.getContentType();
        String charset = null;
        if (ctStr != null) {
            ContentType ct = new ContentType(ctStr);
            if (!isXml(ct)) {
                throw new IOException("Cannot convert DataSource with content type \"" + ctStr + "\" to object in XmlDataContentHandler");
            }
            charset = ct.getParameter("charset");
        }
        if (charset != null) {
            return new StreamSource(new InputStreamReader(ds.getInputStream()), charset);
        }
        return new StreamSource(ds.getInputStream());
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (!(obj instanceof DataSource) && !(obj instanceof Source) && !(obj instanceof String)) {
            throw new IOException("Invalid Object type = " + ((Object) obj.getClass()) + ". XmlDataContentHandler can only convert DataSource|Source|String to XML.");
        }
        ContentType ct = new ContentType(mimeType);
        if (!isXml(ct)) {
            throw new IOException("Invalid content type \"" + mimeType + "\" for XmlDataContentHandler");
        }
        String charset = ct.getParameter("charset");
        if (obj instanceof String) {
            String s2 = (String) obj;
            if (charset == null) {
                charset = "utf-8";
            }
            OutputStreamWriter osw = new OutputStreamWriter(os, charset);
            osw.write(s2, 0, s2.length());
            osw.flush();
            return;
        }
        Source source = obj instanceof DataSource ? (Source) getContent((DataSource) obj) : (Source) obj;
        try {
            Transformer transformer = XmlUtil.newTransformer();
            if (charset != null) {
                transformer.setOutputProperty("encoding", charset);
            }
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
        } catch (Exception ex) {
            throw new IOException("Unable to run the JAXP transformer in XmlDataContentHandler " + ex.getMessage());
        }
    }

    private boolean isXml(ContentType ct) {
        return ct.getSubType().equals("xml") && (ct.getPrimaryType().equals("text") || ct.getPrimaryType().equals("application"));
    }
}
