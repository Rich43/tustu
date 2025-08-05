package com.sun.xml.internal.txw2;

import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlCDATA;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import com.sun.xml.internal.txw2.annotation.XmlNamespace;
import com.sun.xml.internal.txw2.annotation.XmlValue;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/ContainerElement.class */
final class ContainerElement implements InvocationHandler, TypedXmlWriter {
    final Document document;
    StartTag startTag;
    final EndTag endTag = new EndTag();
    private final String nsUri;
    private Content tail;
    private ContainerElement prevOpen;
    private ContainerElement nextOpen;
    private final ContainerElement parent;
    private ContainerElement lastOpenChild;
    private boolean blocked;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ContainerElement.class.desiredAssertionStatus();
    }

    public ContainerElement(Document document, ContainerElement parent, String nsUri, String localName) {
        this.parent = parent;
        this.document = document;
        this.nsUri = nsUri;
        this.startTag = new StartTag(this, nsUri, localName);
        this.tail = this.startTag;
        if (isRoot()) {
            document.setFirstContent(this.startTag);
        }
    }

    private boolean isRoot() {
        return this.parent == null;
    }

    private boolean isCommitted() {
        return this.tail == null;
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public Document getDocument() {
        return this.document;
    }

    boolean isBlocked() {
        return this.blocked && !isCommitted();
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void block() {
        this.blocked = true;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == TypedXmlWriter.class || method.getDeclaringClass() == Object.class) {
            try {
                return method.invoke(this, args);
            } catch (InvocationTargetException e2) {
                throw e2.getTargetException();
            }
        }
        XmlAttribute xa = (XmlAttribute) method.getAnnotation(XmlAttribute.class);
        XmlValue xv = (XmlValue) method.getAnnotation(XmlValue.class);
        XmlElement xe = (XmlElement) method.getAnnotation(XmlElement.class);
        if (xa != null) {
            if (xv != null || xe != null) {
                throw new IllegalAnnotationException(method.toString());
            }
            addAttribute(xa, method, args);
            return proxy;
        }
        if (xv != null) {
            if (xe != null) {
                throw new IllegalAnnotationException(method.toString());
            }
            _pcdata(args);
            return proxy;
        }
        return addElement(xe, method, args);
    }

    private void addAttribute(XmlAttribute xa, Method method, Object[] args) {
        if (!$assertionsDisabled && xa == null) {
            throw new AssertionError();
        }
        checkStartTag();
        String localName = xa.value();
        if (xa.value().length() == 0) {
            localName = method.getName();
        }
        _attribute(xa.ns(), localName, args);
    }

    private void checkStartTag() {
        if (this.startTag == null) {
            throw new IllegalStateException("start tag has already been written");
        }
    }

    private Object addElement(XmlElement e2, Method method, Object[] args) {
        Class<?> rt = method.getReturnType();
        String nsUri = "##default";
        String localName = method.getName();
        if (e2 != null) {
            if (e2.value().length() != 0) {
                localName = e2.value();
            }
            nsUri = e2.ns();
        }
        if (nsUri.equals("##default")) {
            Class<?> c2 = method.getDeclaringClass();
            XmlElement ce = (XmlElement) c2.getAnnotation(XmlElement.class);
            if (ce != null) {
                nsUri = ce.ns();
            }
            if (nsUri.equals("##default")) {
                nsUri = getNamespace(c2.getPackage());
            }
        }
        if (rt == Void.TYPE) {
            boolean isCDATA = method.getAnnotation(XmlCDATA.class) != null;
            StartTag st = new StartTag(this.document, nsUri, localName);
            addChild(st);
            for (Object arg : args) {
                Text text = isCDATA ? new Cdata(this.document, st, arg) : new Pcdata(this.document, st, arg);
                addChild(text);
            }
            addChild(new EndTag());
            return null;
        }
        if (TypedXmlWriter.class.isAssignableFrom(rt)) {
            return _element(nsUri, localName, rt);
        }
        throw new IllegalSignatureException("Illegal return type: " + ((Object) rt));
    }

    private String getNamespace(Package pkg) {
        String nsUri;
        if (pkg == null) {
            return "";
        }
        XmlNamespace ns = (XmlNamespace) pkg.getAnnotation(XmlNamespace.class);
        if (ns != null) {
            nsUri = ns.value();
        } else {
            nsUri = "";
        }
        return nsUri;
    }

    private void addChild(Content child) {
        this.tail.setNext(this.document, child);
        this.tail = child;
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void commit() {
        commit(true);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void commit(boolean includingAllPredecessors) {
        _commit(includingAllPredecessors);
        this.document.flush();
    }

    private void _commit(boolean includingAllPredecessors) {
        if (isCommitted()) {
            return;
        }
        addChild(this.endTag);
        if (isRoot()) {
            addChild(new EndDocument());
        }
        this.tail = null;
        if (includingAllPredecessors) {
            ContainerElement containerElement = this;
            while (true) {
                ContainerElement e2 = containerElement;
                if (e2 == null) {
                    break;
                }
                while (e2.prevOpen != null) {
                    e2.prevOpen._commit(false);
                }
                containerElement = e2.parent;
            }
        }
        while (this.lastOpenChild != null) {
            this.lastOpenChild._commit(false);
        }
        if (this.parent != null) {
            if (this.parent.lastOpenChild == this) {
                if (!$assertionsDisabled && this.nextOpen != null) {
                    throw new AssertionError((Object) "this must be the last one");
                }
                this.parent.lastOpenChild = this.prevOpen;
            } else {
                if (!$assertionsDisabled && this.nextOpen.prevOpen != this) {
                    throw new AssertionError();
                }
                this.nextOpen.prevOpen = this.prevOpen;
            }
            if (this.prevOpen != null) {
                if (!$assertionsDisabled && this.prevOpen.nextOpen != this) {
                    throw new AssertionError();
                }
                this.prevOpen.nextOpen = this.nextOpen;
            }
        }
        this.nextOpen = null;
        this.prevOpen = null;
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _attribute(String localName, Object value) {
        _attribute("", localName, value);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _attribute(String nsUri, String localName, Object value) {
        checkStartTag();
        this.startTag.addAttribute(nsUri, localName, value);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _attribute(QName attributeName, Object value) {
        _attribute(attributeName.getNamespaceURI(), attributeName.getLocalPart(), value);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _namespace(String uri) {
        _namespace(uri, false);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _namespace(String uri, String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        checkStartTag();
        this.startTag.addNamespaceDecl(uri, prefix, false);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _namespace(String uri, boolean requirePrefix) {
        checkStartTag();
        this.startTag.addNamespaceDecl(uri, null, requirePrefix);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _pcdata(Object value) {
        addChild(new Pcdata(this.document, this.startTag, value));
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _cdata(Object value) {
        addChild(new Cdata(this.document, this.startTag, value));
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public void _comment(Object value) throws UnsupportedOperationException {
        addChild(new Comment(this.document, this.startTag, value));
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public <T extends TypedXmlWriter> T _element(String str, Class<T> cls) {
        return (T) _element(this.nsUri, str, cls);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public <T extends TypedXmlWriter> T _element(QName qName, Class<T> cls) {
        return (T) _element(qName.getNamespaceURI(), qName.getLocalPart(), cls);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public <T extends TypedXmlWriter> T _element(Class<T> cls) {
        return (T) _element(TXW.getTagName(cls), cls);
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public <T extends TypedXmlWriter> T _cast(Class<T> facadeType) {
        return facadeType.cast(Proxy.newProxyInstance(facadeType.getClassLoader(), new Class[]{facadeType}, this));
    }

    @Override // com.sun.xml.internal.txw2.TypedXmlWriter
    public <T extends TypedXmlWriter> T _element(String str, String str2, Class<T> cls) {
        ContainerElement containerElement = new ContainerElement(this.document, this, str, str2);
        addChild(containerElement.startTag);
        this.tail = containerElement.endTag;
        if (this.lastOpenChild != null) {
            if (!$assertionsDisabled && this.lastOpenChild.parent != this) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && containerElement.prevOpen != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && containerElement.nextOpen != null) {
                throw new AssertionError();
            }
            containerElement.prevOpen = this.lastOpenChild;
            if (!$assertionsDisabled && this.lastOpenChild.nextOpen != null) {
                throw new AssertionError();
            }
            this.lastOpenChild.nextOpen = containerElement;
        }
        this.lastOpenChild = containerElement;
        return (T) containerElement._cast(cls);
    }
}
