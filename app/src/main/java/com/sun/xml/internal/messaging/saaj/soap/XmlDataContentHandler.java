package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/XmlDataContentHandler.class */
public class XmlDataContentHandler implements DataContentHandler {
    public static final String STR_SRC = "javax.xml.transform.stream.StreamSource";
    private static Class streamSourceClass = null;

    public XmlDataContentHandler() throws ClassNotFoundException {
        if (streamSourceClass == null) {
            streamSourceClass = Class.forName(STR_SRC);
        }
    }

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = {new ActivationDataFlavor(streamSourceClass, "text/xml", "XML"), new ActivationDataFlavor(streamSourceClass, XMLCodec.XML_APPLICATION_MIME_TYPE, "XML")};
        return flavors;
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor flavor, DataSource dataSource) throws IOException {
        if ((flavor.getMimeType().startsWith("text/xml") || flavor.getMimeType().startsWith(XMLCodec.XML_APPLICATION_MIME_TYPE)) && flavor.getRepresentationClass().getName().equals(STR_SRC)) {
            return new StreamSource(dataSource.getInputStream());
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource dataSource) throws IOException {
        return new StreamSource(dataSource.getInputStream());
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        Source src;
        if (!mimeType.startsWith("text/xml") && !mimeType.startsWith(XMLCodec.XML_APPLICATION_MIME_TYPE)) {
            throw new IOException("Invalid content type \"" + mimeType + "\" for XmlDCH");
        }
        try {
            Transformer transformer = EfficientStreamingTransformer.newTransformer();
            StreamResult result = new StreamResult(os);
            if (obj instanceof DataSource) {
                transformer.transform((Source) getContent((DataSource) obj), result);
            } else {
                if (obj instanceof String) {
                    src = new StreamSource(new StringReader((String) obj));
                } else {
                    src = (Source) obj;
                }
                transformer.transform(src, result);
            }
        } catch (Exception ex) {
            throw new IOException("Unable to run the JAXP transformer on a stream " + ex.getMessage());
        }
    }
}
