package javax.xml.bind.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import javax.xml.bind.ValidationEventLocator;
import org.w3c.dom.Node;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:javax/xml/bind/helpers/ValidationEventLocatorImpl.class */
public class ValidationEventLocatorImpl implements ValidationEventLocator {
    private URL url;
    private int offset;
    private int lineNumber;
    private int columnNumber;
    private Object object;
    private Node node;

    public ValidationEventLocatorImpl() {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
    }

    public ValidationEventLocatorImpl(Locator loc) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (loc == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "loc"));
        }
        this.url = toURL(loc.getSystemId());
        this.columnNumber = loc.getColumnNumber();
        this.lineNumber = loc.getLineNumber();
    }

    public ValidationEventLocatorImpl(SAXParseException e2) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (e2 == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "e"));
        }
        this.url = toURL(e2.getSystemId());
        this.columnNumber = e2.getColumnNumber();
        this.lineNumber = e2.getLineNumber();
    }

    public ValidationEventLocatorImpl(Node _node) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (_node == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_node"));
        }
        this.node = _node;
    }

    public ValidationEventLocatorImpl(Object _object) {
        this.url = null;
        this.offset = -1;
        this.lineNumber = -1;
        this.columnNumber = -1;
        this.object = null;
        this.node = null;
        if (_object == null) {
            throw new IllegalArgumentException(Messages.format("Shared.MustNotBeNull", "_object"));
        }
        this.object = _object;
    }

    private static URL toURL(String systemId) {
        try {
            return new URL(systemId);
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    @Override // javax.xml.bind.ValidationEventLocator
    public URL getURL() {
        return this.url;
    }

    public void setURL(URL _url) {
        this.url = _url;
    }

    @Override // javax.xml.bind.ValidationEventLocator
    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int _offset) {
        this.offset = _offset;
    }

    @Override // javax.xml.bind.ValidationEventLocator
    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int _lineNumber) {
        this.lineNumber = _lineNumber;
    }

    @Override // javax.xml.bind.ValidationEventLocator
    public int getColumnNumber() {
        return this.columnNumber;
    }

    public void setColumnNumber(int _columnNumber) {
        this.columnNumber = _columnNumber;
    }

    @Override // javax.xml.bind.ValidationEventLocator
    public Object getObject() {
        return this.object;
    }

    public void setObject(Object _object) {
        this.object = _object;
    }

    @Override // javax.xml.bind.ValidationEventLocator
    public Node getNode() {
        return this.node;
    }

    public void setNode(Node _node) {
        this.node = _node;
    }

    public String toString() {
        return MessageFormat.format("[node={0},object={1},url={2},line={3},col={4},offset={5}]", getNode(), getObject(), getURL(), String.valueOf(getLineNumber()), String.valueOf(getColumnNumber()), String.valueOf(getOffset()));
    }
}
