package com.sun.xml.internal.ws.streaming;

import com.sun.xml.internal.messaging.saaj.soap.FastInfosetDataContentHandler;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.util.FastInfosetUtil;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/streaming/SourceReaderFactory.class */
public class SourceReaderFactory {
    static Class fastInfosetSourceClass;
    static Method fastInfosetSource_getInputStream;

    static {
        try {
            fastInfosetSourceClass = Class.forName(FastInfosetDataContentHandler.STR_SRC);
            fastInfosetSource_getInputStream = fastInfosetSourceClass.getMethod("getInputStream", new Class[0]);
        } catch (Exception e2) {
            fastInfosetSourceClass = null;
        }
    }

    public static XMLStreamReader createSourceReader(Source source, boolean rejectDTDs) {
        return createSourceReader(source, rejectDTDs, null);
    }

    public static XMLStreamReader createSourceReader(Source source, boolean rejectDTDs, String charsetName) {
        try {
            if (source instanceof StreamSource) {
                StreamSource streamSource = (StreamSource) source;
                InputStream is = streamSource.getInputStream();
                if (is != null) {
                    if (charsetName != null) {
                        return XMLStreamReaderFactory.create(source.getSystemId(), new InputStreamReader(is, charsetName), rejectDTDs);
                    }
                    return XMLStreamReaderFactory.create(source.getSystemId(), is, rejectDTDs);
                }
                Reader reader = streamSource.getReader();
                if (reader != null) {
                    return XMLStreamReaderFactory.create(source.getSystemId(), reader, rejectDTDs);
                }
                return XMLStreamReaderFactory.create(source.getSystemId(), new URL(source.getSystemId()).openStream(), rejectDTDs);
            }
            if (source.getClass() == fastInfosetSourceClass) {
                return FastInfosetUtil.createFIStreamReader((InputStream) fastInfosetSource_getInputStream.invoke(source, new Object[0]));
            }
            if (source instanceof DOMSource) {
                DOMStreamReader dsr = new DOMStreamReader();
                dsr.setCurrentNode(((DOMSource) source).getNode());
                return dsr;
            }
            if (source instanceof SAXSource) {
                Transformer tx = XmlUtil.newTransformer();
                DOMResult domResult = new DOMResult();
                tx.transform(source, domResult);
                return createSourceReader(new DOMSource(domResult.getNode()), rejectDTDs);
            }
            throw new XMLReaderException("sourceReader.invalidSource", source.getClass().getName());
        } catch (Exception e2) {
            throw new XMLReaderException(e2);
        }
    }
}
