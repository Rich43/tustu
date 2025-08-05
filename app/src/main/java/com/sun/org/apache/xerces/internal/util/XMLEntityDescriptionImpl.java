package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.impl.XMLEntityDescription;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLEntityDescriptionImpl.class */
public class XMLEntityDescriptionImpl extends XMLResourceIdentifierImpl implements XMLEntityDescription {
    protected String fEntityName;

    public XMLEntityDescriptionImpl() {
    }

    public XMLEntityDescriptionImpl(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId) {
        setDescription(entityName, publicId, literalSystemId, baseSystemId, expandedSystemId);
    }

    public XMLEntityDescriptionImpl(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace) {
        setDescription(entityName, publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityDescription
    public void setEntityName(String name) {
        this.fEntityName = name;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLEntityDescription
    public String getEntityName() {
        return this.fEntityName;
    }

    public void setDescription(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId) {
        setDescription(entityName, publicId, literalSystemId, baseSystemId, expandedSystemId, null);
    }

    public void setDescription(String entityName, String publicId, String literalSystemId, String baseSystemId, String expandedSystemId, String namespace) {
        this.fEntityName = entityName;
        setValues(publicId, literalSystemId, baseSystemId, expandedSystemId, namespace);
    }

    @Override // com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl
    public void clear() {
        super.clear();
        this.fEntityName = null;
    }

    @Override // com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl
    public int hashCode() {
        int code = super.hashCode();
        if (this.fEntityName != null) {
            code += this.fEntityName.hashCode();
        }
        return code;
    }

    @Override // com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (this.fEntityName != null) {
            str.append(this.fEntityName);
        }
        str.append(':');
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
