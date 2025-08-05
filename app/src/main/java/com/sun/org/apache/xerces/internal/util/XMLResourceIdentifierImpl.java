package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLResourceIdentifierImpl.class */
public class XMLResourceIdentifierImpl implements XMLResourceIdentifier {
    protected String fPublicId;
    protected String fLiteralSystemId;
    protected String fBaseSystemId;
    protected String fExpandedSystemId;
    protected String fNamespace;

    public XMLResourceIdentifierImpl() {
    }

    public XMLResourceIdentifierImpl(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId) {
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, null);
    }

    public XMLResourceIdentifierImpl(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace) {
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
    }

    public void setValues(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId) {
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, null);
    }

    public void setValues(String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace) {
        this.fPublicId = publicId;
        this.fLiteralSystemId = literalSystemId;
        this.fBaseSystemId = baseSystemId;
        this.fExpandedSystemId = expandedSystemId;
        this.fNamespace = namespace;
    }

    public void clear() {
        this.fPublicId = null;
        this.fLiteralSystemId = null;
        this.fBaseSystemId = null;
        this.fExpandedSystemId = null;
        this.fNamespace = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public void setPublicId(String publicId) {
        this.fPublicId = publicId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public void setLiteralSystemId(String literalSystemId) {
        this.fLiteralSystemId = literalSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public void setBaseSystemId(String baseSystemId) {
        this.fBaseSystemId = baseSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public void setExpandedSystemId(String expandedSystemId) {
        this.fExpandedSystemId = expandedSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public void setNamespace(String namespace) {
        this.fNamespace = namespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public String getPublicId() {
        return this.fPublicId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public String getLiteralSystemId() {
        return this.fLiteralSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public String getBaseSystemId() {
        return this.fBaseSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public String getExpandedSystemId() {
        return this.fExpandedSystemId;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier
    public String getNamespace() {
        return this.fNamespace;
    }

    public int hashCode() {
        int code = 0;
        if (this.fPublicId != null) {
            code = 0 + this.fPublicId.hashCode();
        }
        if (this.fLiteralSystemId != null) {
            code += this.fLiteralSystemId.hashCode();
        }
        if (this.fBaseSystemId != null) {
            code += this.fBaseSystemId.hashCode();
        }
        if (this.fExpandedSystemId != null) {
            code += this.fExpandedSystemId.hashCode();
        }
        if (this.fNamespace != null) {
            code += this.fNamespace.hashCode();
        }
        return code;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        if (this.fPublicId != null) {
            str.append(this.fPublicId);
        }
        str.append(':');
        if (this.fLiteralSystemId != null) {
            str.append(this.fLiteralSystemId);
        }
        str.append(':');
        if (this.fBaseSystemId != null) {
            str.append(this.fBaseSystemId);
        }
        str.append(':');
        if (this.fExpandedSystemId != null) {
            str.append(this.fExpandedSystemId);
        }
        str.append(':');
        if (this.fNamespace != null) {
            str.append(this.fNamespace);
        }
        return str.toString();
    }
}
