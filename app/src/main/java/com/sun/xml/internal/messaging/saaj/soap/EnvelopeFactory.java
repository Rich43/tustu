package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.util.JAXMStreamSource;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.messaging.saaj.util.ParserPool;
import com.sun.xml.internal.messaging.saaj.util.RejectDoctypeSaxFilter;
import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;
import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/EnvelopeFactory.class */
public class EnvelopeFactory {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
    private static ContextClassloaderLocal<ParserPool> parserPool = new ContextClassloaderLocal<ParserPool>() { // from class: com.sun.xml.internal.messaging.saaj.soap.EnvelopeFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.messaging.saaj.soap.ContextClassloaderLocal
        public ParserPool initialValue() throws Exception {
            return new ParserPool(5);
        }
    };

    /* JADX WARN: Finally extract failed */
    public static Envelope createEnvelope(Source src, SOAPPartImpl soapPart) throws SOAPException {
        SAXParser saxParser = null;
        if (src instanceof StreamSource) {
            if (src instanceof JAXMStreamSource) {
                try {
                    if (!SOAPPartImpl.lazyContentLength) {
                        ((JAXMStreamSource) src).reset();
                    }
                } catch (IOException ioe) {
                    log.severe("SAAJ0515.source.reset.exception");
                    throw new SOAPExceptionImpl(ioe);
                }
            }
            try {
                saxParser = parserPool.get().get();
                InputSource is = SAXSource.sourceToInputSource(src);
                if (is.getEncoding() == null && soapPart.getSourceCharsetEncoding() != null) {
                    is.setEncoding(soapPart.getSourceCharsetEncoding());
                }
                try {
                    XMLReader rejectFilter = new RejectDoctypeSaxFilter(saxParser);
                    src = new SAXSource(rejectFilter, is);
                } catch (Exception ex) {
                    log.severe("SAAJ0510.soap.cannot.create.envelope");
                    throw new SOAPExceptionImpl("Unable to create envelope from given source: ", ex);
                }
            } catch (Exception e2) {
                log.severe("SAAJ0601.util.newSAXParser.exception");
                throw new SOAPExceptionImpl("Couldn't get a SAX parser while constructing a envelope", e2);
            }
        }
        try {
            try {
                Transformer transformer = EfficientStreamingTransformer.newTransformer();
                DOMResult result = new DOMResult(soapPart);
                transformer.transform(src, result);
                Envelope env = (Envelope) soapPart.getEnvelope();
                if (saxParser != null) {
                    parserPool.get().returnParser(saxParser);
                }
                return env;
            } catch (Exception ex2) {
                if (ex2 instanceof SOAPVersionMismatchException) {
                    throw ((SOAPVersionMismatchException) ex2);
                }
                log.severe("SAAJ0511.soap.cannot.create.envelope");
                throw new SOAPExceptionImpl("Unable to create envelope from given source: ", ex2);
            }
        } catch (Throwable th) {
            if (saxParser != null) {
                parserPool.get().returnParser(saxParser);
            }
            throw th;
        }
    }
}
