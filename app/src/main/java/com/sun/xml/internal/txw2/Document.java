package com.sun.xml.internal.txw2;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.txw2.output.XmlSerializer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/Document.class */
public final class Document {
    private final XmlSerializer out;
    private NamespaceDecl activeNamespaces;
    static final char MAGIC = 0;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean started = false;
    private Content current = null;
    private final Map<Class, DatatypeWriter> datatypeWriters = new HashMap();
    private int iota = 1;
    private final NamespaceSupport inscopeNamespace = new NamespaceSupport();
    private final ContentVisitor visitor = new ContentVisitor() { // from class: com.sun.xml.internal.txw2.Document.1
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Document.class.desiredAssertionStatus();
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onStartDocument() {
            throw new IllegalStateException();
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onEndDocument() {
            Document.this.out.endDocument();
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onEndTag() {
            Document.this.out.endTag();
            Document.this.inscopeNamespace.popContext();
            Document.this.activeNamespaces = null;
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onPcdata(StringBuilder buffer) {
            if (Document.this.activeNamespaces != null) {
                buffer = Document.this.fixPrefix(buffer);
            }
            Document.this.out.text(buffer);
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onCdata(StringBuilder buffer) {
            if (Document.this.activeNamespaces != null) {
                buffer = Document.this.fixPrefix(buffer);
            }
            Document.this.out.cdata(buffer);
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onComment(StringBuilder buffer) {
            if (Document.this.activeNamespaces != null) {
                buffer = Document.this.fixPrefix(buffer);
            }
            Document.this.out.comment(buffer);
        }

        @Override // com.sun.xml.internal.txw2.ContentVisitor
        public void onStartTag(String nsUri, String localName, Attribute attributes, NamespaceDecl namespaces) {
            NamespaceSupport namespaceSupport;
            String strNewPrefix;
            String uri;
            if (!$assertionsDisabled && nsUri == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && localName == null) {
                throw new AssertionError();
            }
            Document.this.activeNamespaces = namespaces;
            if (!Document.this.started) {
                Document.this.started = true;
                Document.this.out.startDocument();
            }
            Document.this.inscopeNamespace.pushContext();
            NamespaceDecl namespaceDecl = namespaces;
            while (true) {
                NamespaceDecl ns = namespaceDecl;
                if (ns == null) {
                    break;
                }
                ns.declared = false;
                if (ns.prefix != null && ((uri = Document.this.inscopeNamespace.getURI(ns.prefix)) == null || !uri.equals(ns.uri))) {
                    Document.this.inscopeNamespace.declarePrefix(ns.prefix, ns.uri);
                    ns.declared = true;
                }
                namespaceDecl = ns.next;
            }
            NamespaceDecl namespaceDecl2 = namespaces;
            while (true) {
                NamespaceDecl ns2 = namespaceDecl2;
                if (ns2 == null) {
                    break;
                }
                if (ns2.prefix == null) {
                    if (!Document.this.inscopeNamespace.getURI("").equals(ns2.uri)) {
                        String p2 = Document.this.inscopeNamespace.getPrefix(ns2.uri);
                        if (p2 == null) {
                            do {
                                namespaceSupport = Document.this.inscopeNamespace;
                                strNewPrefix = Document.this.newPrefix();
                                p2 = strNewPrefix;
                            } while (namespaceSupport.getURI(strNewPrefix) != null);
                            ns2.declared = true;
                            Document.this.inscopeNamespace.declarePrefix(p2, ns2.uri);
                        }
                        ns2.prefix = p2;
                    } else {
                        ns2.prefix = "";
                    }
                }
                namespaceDecl2 = ns2.next;
            }
            if (!$assertionsDisabled && !namespaces.uri.equals(nsUri)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && namespaces.prefix == null) {
                throw new AssertionError((Object) "a prefix must have been all allocated");
            }
            Document.this.out.beginStartTag(nsUri, localName, namespaces.prefix);
            NamespaceDecl namespaceDecl3 = namespaces;
            while (true) {
                NamespaceDecl ns3 = namespaceDecl3;
                if (ns3 == null) {
                    break;
                }
                if (ns3.declared) {
                    Document.this.out.writeXmlns(ns3.prefix, ns3.uri);
                }
                namespaceDecl3 = ns3.next;
            }
            Attribute attribute = attributes;
            while (true) {
                Attribute a2 = attribute;
                if (a2 == null) {
                    Document.this.out.endStartTag(nsUri, localName, namespaces.prefix);
                    return;
                } else {
                    String prefix = a2.nsUri.length() == 0 ? "" : Document.this.inscopeNamespace.getPrefix(a2.nsUri);
                    Document.this.out.writeAttribute(a2.nsUri, a2.localName, prefix, Document.this.fixPrefix(a2.value));
                    attribute = a2.next;
                }
            }
        }
    };
    private final StringBuilder prefixSeed = new StringBuilder(Constants.ATTRNAME_NS);
    private int prefixIota = 0;

    static {
        $assertionsDisabled = !Document.class.desiredAssertionStatus();
    }

    Document(XmlSerializer out) {
        this.out = out;
        for (DatatypeWriter dw : DatatypeWriter.BUILTIN) {
            this.datatypeWriters.put(dw.getType(), dw);
        }
    }

    void flush() {
        this.out.flush();
    }

    void setFirstContent(Content c2) {
        if (!$assertionsDisabled && this.current != null) {
            throw new AssertionError();
        }
        this.current = new StartDocument();
        this.current.setNext(this, c2);
    }

    public void addDatatypeWriter(DatatypeWriter<?> dw) {
        this.datatypeWriters.put(dw.getType(), dw);
    }

    void run() {
        while (true) {
            Content next = this.current.getNext();
            if (next == null || !next.isReadyToCommit()) {
                return;
            }
            next.accept(this.visitor);
            next.written();
            this.current = next;
        }
    }

    void writeValue(Object obj, NamespaceResolver nsResolver, StringBuilder buf) {
        if (obj == null) {
            throw new IllegalArgumentException("argument contains null");
        }
        if (obj instanceof Object[]) {
            for (Object o2 : (Object[]) obj) {
                writeValue(o2, nsResolver, buf);
            }
            return;
        }
        if (obj instanceof Iterable) {
            for (Object o3 : (Iterable) obj) {
                writeValue(o3, nsResolver, buf);
            }
            return;
        }
        if (buf.length() > 0) {
            buf.append(' ');
        }
        Class superclass = obj.getClass();
        while (true) {
            Class c2 = superclass;
            if (c2 != null) {
                DatatypeWriter dw = this.datatypeWriters.get(c2);
                if (dw != null) {
                    dw.print(obj, nsResolver, buf);
                    return;
                }
                superclass = c2.getSuperclass();
            } else {
                buf.append(obj);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String newPrefix() {
        this.prefixSeed.setLength(2);
        StringBuilder sb = this.prefixSeed;
        int i2 = this.prefixIota + 1;
        this.prefixIota = i2;
        sb.append(i2);
        return this.prefixSeed.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StringBuilder fixPrefix(StringBuilder buf) {
        NamespaceDecl ns;
        if (!$assertionsDisabled && this.activeNamespaces == null) {
            throw new AssertionError();
        }
        int len = buf.length();
        int i2 = 0;
        while (i2 < len && buf.charAt(i2) != 0) {
            i2++;
        }
        if (i2 == len) {
            return buf;
        }
        while (i2 < len) {
            char uriIdx = buf.charAt(i2 + 1);
            NamespaceDecl namespaceDecl = this.activeNamespaces;
            while (true) {
                ns = namespaceDecl;
                if (ns == null || ns.uniqueId == uriIdx) {
                    break;
                }
                namespaceDecl = ns.next;
            }
            if (ns == null) {
                throw new IllegalStateException("Unexpected use of prefixes " + ((Object) buf));
            }
            int length = 2;
            String prefix = ns.prefix;
            if (prefix.length() == 0) {
                if (buf.length() <= i2 + 2 || buf.charAt(i2 + 2) != ':') {
                    throw new IllegalStateException("Unexpected use of prefixes " + ((Object) buf));
                }
                length = 3;
            }
            buf.replace(i2, i2 + length, prefix);
            len += prefix.length() - length;
            while (i2 < len && buf.charAt(i2) != 0) {
                i2++;
            }
        }
        return buf;
    }

    char assignNewId() {
        int i2 = this.iota;
        this.iota = i2 + 1;
        return (char) i2;
    }
}
