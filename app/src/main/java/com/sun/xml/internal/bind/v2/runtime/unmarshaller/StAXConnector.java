package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/StAXConnector.class */
abstract class StAXConnector {
    protected final XmlVisitor visitor;
    protected final UnmarshallingContext context;
    protected final XmlVisitor.TextPredictor predictor;
    protected final TagName tagName = new TagNameImpl();

    public abstract void bridge() throws XMLStreamException;

    protected abstract Location getCurrentLocation();

    protected abstract String getCurrentQName();

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/StAXConnector$TagNameImpl.class */
    private final class TagNameImpl extends TagName {
        private TagNameImpl() {
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName
        public String getQname() {
            return StAXConnector.this.getCurrentQName();
        }
    }

    protected StAXConnector(XmlVisitor visitor) {
        this.visitor = visitor;
        this.context = visitor.getContext();
        this.predictor = visitor.getPredictor();
    }

    protected final void handleStartDocument(NamespaceContext nsc) throws SAXException {
        this.visitor.startDocument(new LocatorEx() { // from class: com.sun.xml.internal.bind.v2.runtime.unmarshaller.StAXConnector.1
            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx
            public ValidationEventLocator getLocation() {
                return new ValidationEventLocatorImpl((Locator) this);
            }

            @Override // org.xml.sax.Locator
            public int getColumnNumber() {
                return StAXConnector.this.getCurrentLocation().getColumnNumber();
            }

            @Override // org.xml.sax.Locator
            public int getLineNumber() {
                return StAXConnector.this.getCurrentLocation().getLineNumber();
            }

            @Override // org.xml.sax.Locator
            public String getPublicId() {
                return StAXConnector.this.getCurrentLocation().getPublicId();
            }

            @Override // org.xml.sax.Locator
            public String getSystemId() {
                return StAXConnector.this.getCurrentLocation().getSystemId();
            }
        }, nsc);
    }

    protected final void handleEndDocument() throws SAXException {
        this.visitor.endDocument();
    }

    protected static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    protected final String getQName(String prefix, String localName) {
        if (prefix == null || prefix.length() == 0) {
            return localName;
        }
        return prefix + ':' + localName;
    }
}
