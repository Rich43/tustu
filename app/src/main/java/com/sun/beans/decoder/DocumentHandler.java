package com.sun.beans.decoder;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.sun.beans.finder.ClassFinder;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.beans.ExceptionListener;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:com/sun/beans/decoder/DocumentHandler.class */
public final class DocumentHandler extends DefaultHandler {
    private final AccessControlContext acc = AccessController.getContext();
    private final Map<String, Class<? extends ElementHandler>> handlers = new HashMap();
    private final Map<String, Object> environment = new HashMap();
    private final List<Object> objects = new ArrayList();
    private Reference<ClassLoader> loader;
    private ExceptionListener listener;
    private Object owner;
    private ElementHandler handler;

    public DocumentHandler() {
        setElementHandler("java", JavaElementHandler.class);
        setElementHandler(FXMLLoader.NULL_KEYWORD, NullElementHandler.class);
        setElementHandler(ControllerParameter.PARAM_CLASS_ARRAY, ArrayElementHandler.class);
        setElementHandler(Constants.ATTRNAME_CLASS, ClassElementHandler.class);
        setElementHandler("string", StringElementHandler.class);
        setElementHandler("object", ObjectElementHandler.class);
        setElementHandler("void", VoidElementHandler.class);
        setElementHandler("char", CharElementHandler.class);
        setElementHandler(SchemaSymbols.ATTVAL_BYTE, ByteElementHandler.class);
        setElementHandler(SchemaSymbols.ATTVAL_SHORT, ShortElementHandler.class);
        setElementHandler("int", IntElementHandler.class);
        setElementHandler(SchemaSymbols.ATTVAL_LONG, LongElementHandler.class);
        setElementHandler(SchemaSymbols.ATTVAL_FLOAT, FloatElementHandler.class);
        setElementHandler(SchemaSymbols.ATTVAL_DOUBLE, DoubleElementHandler.class);
        setElementHandler("boolean", BooleanElementHandler.class);
        setElementHandler("new", NewElementHandler.class);
        setElementHandler("var", VarElementHandler.class);
        setElementHandler("true", TrueElementHandler.class);
        setElementHandler("false", FalseElementHandler.class);
        setElementHandler("field", FieldElementHandler.class);
        setElementHandler("method", MethodElementHandler.class);
        setElementHandler("property", PropertyElementHandler.class);
    }

    public ClassLoader getClassLoader() {
        if (this.loader != null) {
            return this.loader.get();
        }
        return null;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.loader = new WeakReference(classLoader);
    }

    public ExceptionListener getExceptionListener() {
        return this.listener;
    }

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.listener = exceptionListener;
    }

    public Object getOwner() {
        return this.owner;
    }

    public void setOwner(Object obj) {
        this.owner = obj;
    }

    public Class<? extends ElementHandler> getElementHandler(String str) {
        Class<? extends ElementHandler> cls = this.handlers.get(str);
        if (cls == null) {
            throw new IllegalArgumentException("Unsupported element: " + str);
        }
        return cls;
    }

    public void setElementHandler(String str, Class<? extends ElementHandler> cls) {
        this.handlers.put(str, cls);
    }

    public boolean hasVariable(String str) {
        return this.environment.containsKey(str);
    }

    public Object getVariable(String str) {
        if (!this.environment.containsKey(str)) {
            throw new IllegalArgumentException("Unbound variable: " + str);
        }
        return this.environment.get(str);
    }

    public void setVariable(String str, Object obj) {
        this.environment.put(str, obj);
    }

    public Object[] getObjects() {
        return this.objects.toArray();
    }

    void addObject(Object obj) {
        this.objects.add(obj);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.EntityResolver
    public InputSource resolveEntity(String str, String str2) {
        return new InputSource(new StringReader(""));
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() {
        this.objects.clear();
        this.handler = null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        ElementHandler elementHandler = this.handler;
        try {
            this.handler = getElementHandler(str3).newInstance();
            this.handler.setOwner(this);
            this.handler.setParent(elementHandler);
            for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                try {
                    this.handler.addAttribute(attributes.getQName(i2), attributes.getValue(i2));
                } catch (RuntimeException e2) {
                    handleException(e2);
                }
            }
            this.handler.startElement();
        } catch (Exception e3) {
            throw new SAXException(e3);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) {
        try {
            try {
                this.handler.endElement();
                this.handler = this.handler.getParent();
            } catch (RuntimeException e2) {
                handleException(e2);
                this.handler = this.handler.getParent();
            }
        } catch (Throwable th) {
            this.handler = this.handler.getParent();
            throw th;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i2, int i3) {
        if (this.handler == null) {
            return;
        }
        while (true) {
            try {
                int i4 = i3;
                i3--;
                if (0 < i4) {
                    int i5 = i2;
                    i2++;
                    this.handler.addCharacter(cArr[i5]);
                } else {
                    return;
                }
            } catch (RuntimeException e2) {
                handleException(e2);
                return;
            }
        }
    }

    public void handleException(Exception exc) {
        if (this.listener == null) {
            throw new IllegalStateException(exc);
        }
        this.listener.exceptionThrown(exc);
    }

    public void parse(final InputSource inputSource) {
        if (this.acc == null && null != System.getSecurityManager()) {
            throw new SecurityException("AccessControlContext is not set");
        }
        SharedSecrets.getJavaSecurityAccess().doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: com.sun.beans.decoder.DocumentHandler.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                try {
                    SAXParserFactory.newInstance().newSAXParser().parse(inputSource, DocumentHandler.this);
                    return null;
                } catch (IOException e2) {
                    DocumentHandler.this.handleException(e2);
                    return null;
                } catch (ParserConfigurationException e3) {
                    DocumentHandler.this.handleException(e3);
                    return null;
                } catch (SAXException e4) {
                    Exception exception = e4.getException();
                    if (exception == null) {
                        exception = e4;
                    }
                    DocumentHandler.this.handleException(exception);
                    return null;
                }
            }
        }, AccessController.getContext(), this.acc);
    }

    public Class<?> findClass(String str) {
        try {
            return ClassFinder.resolveClass(str, getClassLoader());
        } catch (ClassNotFoundException e2) {
            handleException(e2);
            return null;
        }
    }
}
