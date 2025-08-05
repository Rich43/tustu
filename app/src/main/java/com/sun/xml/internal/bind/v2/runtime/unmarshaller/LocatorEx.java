package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import java.net.URL;
import javax.xml.bind.ValidationEventLocator;
import org.w3c.dom.Node;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/LocatorEx.class */
public interface LocatorEx extends Locator {
    ValidationEventLocator getLocation();

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/LocatorEx$Snapshot.class */
    public static final class Snapshot implements LocatorEx, ValidationEventLocator {
        private final int columnNumber;
        private final int lineNumber;
        private final int offset;
        private final String systemId;
        private final String publicId;
        private final URL url;
        private final Object object;
        private final Node node;

        public Snapshot(LocatorEx loc) {
            this.columnNumber = loc.getColumnNumber();
            this.lineNumber = loc.getLineNumber();
            this.systemId = loc.getSystemId();
            this.publicId = loc.getPublicId();
            ValidationEventLocator vel = loc.getLocation();
            this.offset = vel.getOffset();
            this.url = vel.getURL();
            this.object = vel.getObject();
            this.node = vel.getNode();
        }

        @Override // javax.xml.bind.ValidationEventLocator
        public Object getObject() {
            return this.object;
        }

        @Override // javax.xml.bind.ValidationEventLocator
        public Node getNode() {
            return this.node;
        }

        @Override // javax.xml.bind.ValidationEventLocator
        public int getOffset() {
            return this.offset;
        }

        @Override // javax.xml.bind.ValidationEventLocator
        public URL getURL() {
            return this.url;
        }

        @Override // org.xml.sax.Locator
        public int getColumnNumber() {
            return this.columnNumber;
        }

        @Override // org.xml.sax.Locator
        public int getLineNumber() {
            return this.lineNumber;
        }

        @Override // org.xml.sax.Locator
        public String getSystemId() {
            return this.systemId;
        }

        @Override // org.xml.sax.Locator
        public String getPublicId() {
            return this.publicId;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx
        public ValidationEventLocator getLocation() {
            return this;
        }
    }
}
