package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.v2.ClassFactory;
import java.util.HashMap;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.helpers.ValidationEventImpl;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/Coordinator.class */
public abstract class Coordinator implements ErrorHandler, ValidationEventHandler {
    private final HashMap<Class<? extends XmlAdapter>, XmlAdapter> adapters = new HashMap<>();
    private static final ThreadLocal<Coordinator> activeTable = new ThreadLocal<>();
    private Coordinator old;

    protected abstract ValidationEventLocator getLocation();

    public final XmlAdapter putAdapter(Class<? extends XmlAdapter> c2, XmlAdapter a2) {
        if (a2 == null) {
            return this.adapters.remove(c2);
        }
        return this.adapters.put(c2, a2);
    }

    public final <T extends XmlAdapter> T getAdapter(Class<T> cls) {
        XmlAdapter xmlAdapterCast = cls.cast(this.adapters.get(cls));
        if (xmlAdapterCast == null) {
            xmlAdapterCast = (XmlAdapter) ClassFactory.create(cls);
            putAdapter(cls, xmlAdapterCast);
        }
        return (T) xmlAdapterCast;
    }

    public <T extends XmlAdapter> boolean containsAdapter(Class<T> type) {
        return this.adapters.containsKey(type);
    }

    protected final void pushCoordinator() {
        this.old = activeTable.get();
        activeTable.set(this);
    }

    protected final void popCoordinator() {
        if (this.old != null) {
            activeTable.set(this.old);
        } else {
            activeTable.remove();
        }
        this.old = null;
    }

    public static Coordinator _getInstance() {
        return activeTable.get();
    }

    @Override // org.xml.sax.ErrorHandler
    public final void error(SAXParseException exception) throws SAXException {
        propagateEvent(1, exception);
    }

    @Override // org.xml.sax.ErrorHandler
    public final void warning(SAXParseException exception) throws SAXException {
        propagateEvent(0, exception);
    }

    @Override // org.xml.sax.ErrorHandler
    public final void fatalError(SAXParseException exception) throws SAXException {
        propagateEvent(2, exception);
    }

    private void propagateEvent(int severity, SAXParseException saxException) throws SAXException {
        ValidationEventImpl ve = new ValidationEventImpl(severity, saxException.getMessage(), getLocation());
        Exception e2 = saxException.getException();
        if (e2 != null) {
            ve.setLinkedException(e2);
        } else {
            ve.setLinkedException(saxException);
        }
        boolean result = handleEvent(ve);
        if (!result) {
            throw saxException;
        }
    }
}
