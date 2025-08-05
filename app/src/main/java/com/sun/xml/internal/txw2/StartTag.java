package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/StartTag.class */
class StartTag extends Content implements NamespaceResolver {
    private String uri;
    private final String localName;
    private Attribute firstAtt;
    private Attribute lastAtt;
    private ContainerElement owner;
    private NamespaceDecl firstNs;
    private NamespaceDecl lastNs;
    final Document document;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StartTag.class.desiredAssertionStatus();
    }

    public StartTag(ContainerElement owner, String uri, String localName) {
        this(owner.document, uri, localName);
        this.owner = owner;
    }

    public StartTag(Document document, String uri, String localName) {
        if (!$assertionsDisabled && uri == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && localName == null) {
            throw new AssertionError();
        }
        this.uri = uri;
        this.localName = localName;
        this.document = document;
        addNamespaceDecl(uri, null, false);
    }

    public void addAttribute(String nsUri, String localName, Object arg) {
        Attribute a2;
        checkWritable();
        Attribute attribute = this.firstAtt;
        while (true) {
            a2 = attribute;
            if (a2 == null || a2.hasName(nsUri, localName)) {
                break;
            } else {
                attribute = a2.next;
            }
        }
        if (a2 == null) {
            a2 = new Attribute(nsUri, localName);
            if (this.lastAtt == null) {
                if (!$assertionsDisabled && this.firstAtt != null) {
                    throw new AssertionError();
                }
                this.lastAtt = a2;
                this.firstAtt = a2;
            } else {
                if (!$assertionsDisabled && this.firstAtt == null) {
                    throw new AssertionError();
                }
                this.lastAtt.next = a2;
                this.lastAtt = a2;
            }
            if (nsUri.length() > 0) {
                addNamespaceDecl(nsUri, null, true);
            }
        }
        this.document.writeValue(arg, this, a2.value);
    }

    public NamespaceDecl addNamespaceDecl(String uri, String prefix, boolean requirePrefix) {
        checkWritable();
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        if (uri.length() == 0) {
            if (requirePrefix) {
                throw new IllegalArgumentException("The empty namespace cannot have a non-empty prefix");
            }
            if (prefix != null && prefix.length() > 0) {
                throw new IllegalArgumentException("The empty namespace can be only bound to the empty prefix");
            }
            prefix = "";
        }
        NamespaceDecl namespaceDecl = this.firstNs;
        while (true) {
            NamespaceDecl n2 = namespaceDecl;
            if (n2 != null) {
                if (uri.equals(n2.uri)) {
                    if (prefix == null) {
                        n2.requirePrefix |= requirePrefix;
                        return n2;
                    }
                    if (n2.prefix == null) {
                        n2.prefix = prefix;
                        n2.requirePrefix |= requirePrefix;
                        return n2;
                    }
                    if (prefix.equals(n2.prefix)) {
                        n2.requirePrefix |= requirePrefix;
                        return n2;
                    }
                }
                if (prefix == null || n2.prefix == null || !n2.prefix.equals(prefix)) {
                    namespaceDecl = n2.next;
                } else {
                    throw new IllegalArgumentException("Prefix '" + prefix + "' is already bound to '" + n2.uri + '\'');
                }
            } else {
                NamespaceDecl ns = new NamespaceDecl(this.document.assignNewId(), uri, prefix, requirePrefix);
                if (this.lastNs == null) {
                    if (!$assertionsDisabled && this.firstNs != null) {
                        throw new AssertionError();
                    }
                    this.lastNs = ns;
                    this.firstNs = ns;
                } else {
                    if (!$assertionsDisabled && this.firstNs == null) {
                        throw new AssertionError();
                    }
                    this.lastNs.next = ns;
                    this.lastNs = ns;
                }
                return ns;
            }
        }
    }

    private void checkWritable() {
        if (isWritten()) {
            throw new IllegalStateException("The start tag of " + this.localName + " has already been written. If you need out of order writing, see the TypedXmlWriter.block method");
        }
    }

    boolean isWritten() {
        return this.uri == null;
    }

    @Override // com.sun.xml.internal.txw2.Content
    boolean isReadyToCommit() {
        if (this.owner != null && this.owner.isBlocked()) {
            return false;
        }
        Content next = getNext();
        while (true) {
            Content c2 = next;
            if (c2 != null) {
                if (!c2.concludesPendingStartTag()) {
                    next = c2.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // com.sun.xml.internal.txw2.Content
    public void written() {
        this.lastAtt = null;
        this.firstAtt = null;
        this.uri = null;
        if (this.owner != null) {
            if (!$assertionsDisabled && this.owner.startTag != this) {
                throw new AssertionError();
            }
            this.owner.startTag = null;
        }
    }

    @Override // com.sun.xml.internal.txw2.Content
    boolean concludesPendingStartTag() {
        return true;
    }

    @Override // com.sun.xml.internal.txw2.Content
    void accept(ContentVisitor visitor) {
        visitor.onStartTag(this.uri, this.localName, this.firstAtt, this.firstNs);
    }

    @Override // com.sun.xml.internal.txw2.NamespaceResolver
    public String getPrefix(String nsUri) {
        NamespaceDecl ns = addNamespaceDecl(nsUri, null, false);
        if (ns.prefix != null) {
            return ns.prefix;
        }
        return ns.dummyPrefix;
    }
}
