package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.util.ArrayList;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLDTDDescription.class */
public class XMLDTDDescription extends XMLResourceIdentifierImpl implements com.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription {
    protected String fRootName;
    protected ArrayList fPossibleRoots;

    public XMLDTDDescription(XMLResourceIdentifier id, String rootName) {
        this.fRootName = null;
        this.fPossibleRoots = null;
        setValues(id.getPublicId(), id.getLiteralSystemId(), id.getBaseSystemId(), id.getExpandedSystemId());
        this.fRootName = rootName;
        this.fPossibleRoots = null;
    }

    public XMLDTDDescription(String publicId, String literalId, String baseId, String expandedId, String rootName) {
        this.fRootName = null;
        this.fPossibleRoots = null;
        setValues(publicId, literalId, baseId, expandedId);
        this.fRootName = rootName;
        this.fPossibleRoots = null;
    }

    public XMLDTDDescription(XMLInputSource source) {
        this.fRootName = null;
        this.fPossibleRoots = null;
        setValues(source.getPublicId(), null, source.getBaseSystemId(), source.getSystemId());
        this.fRootName = null;
        this.fPossibleRoots = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription
    public String getGrammarType() {
        return "http://www.w3.org/TR/REC-xml";
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription
    public String getRootName() {
        return this.fRootName;
    }

    public void setRootName(String rootName) {
        this.fRootName = rootName;
        this.fPossibleRoots = null;
    }

    public void setPossibleRoots(ArrayList possibleRoots) {
        this.fPossibleRoots = possibleRoots;
    }

    public void setPossibleRoots(Vector possibleRoots) {
        this.fPossibleRoots = possibleRoots != null ? new ArrayList(possibleRoots) : null;
    }

    public boolean equals(Object desc) {
        if (!(desc instanceof XMLGrammarDescription) || !getGrammarType().equals(((XMLGrammarDescription) desc).getGrammarType())) {
            return false;
        }
        XMLDTDDescription dtdDesc = (XMLDTDDescription) desc;
        if (this.fRootName != null) {
            if (dtdDesc.fRootName != null && !dtdDesc.fRootName.equals(this.fRootName)) {
                return false;
            }
            if (dtdDesc.fPossibleRoots != null && !dtdDesc.fPossibleRoots.contains(this.fRootName)) {
                return false;
            }
        } else if (this.fPossibleRoots != null) {
            if (dtdDesc.fRootName != null) {
                if (!this.fPossibleRoots.contains(dtdDesc.fRootName)) {
                    return false;
                }
            } else {
                if (dtdDesc.fPossibleRoots == null) {
                    return false;
                }
                boolean found = false;
                int size = this.fPossibleRoots.size();
                for (int i2 = 0; i2 < size; i2++) {
                    String root = (String) this.fPossibleRoots.get(i2);
                    found = dtdDesc.fPossibleRoots.contains(root);
                    if (found) {
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }
        if (this.fExpandedSystemId != null) {
            if (!this.fExpandedSystemId.equals(dtdDesc.fExpandedSystemId)) {
                return false;
            }
        } else if (dtdDesc.fExpandedSystemId != null) {
            return false;
        }
        if (this.fPublicId != null) {
            if (!this.fPublicId.equals(dtdDesc.fPublicId)) {
                return false;
            }
            return true;
        }
        if (dtdDesc.fPublicId != null) {
            return false;
        }
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl
    public int hashCode() {
        if (this.fExpandedSystemId != null) {
            return this.fExpandedSystemId.hashCode();
        }
        if (this.fPublicId != null) {
            return this.fPublicId.hashCode();
        }
        return 0;
    }
}
