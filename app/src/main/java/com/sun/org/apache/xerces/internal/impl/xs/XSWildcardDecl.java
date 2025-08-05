package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSWildcard;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSWildcardDecl.class */
public class XSWildcardDecl implements XSWildcard {
    public static final String ABSENT = null;
    public String[] fNamespaceList;
    public short fType = 1;
    public short fProcessContents = 1;
    public XSObjectList fAnnotations = null;
    private String fDescription = null;

    public boolean allowNamespace(String namespace) {
        if (this.fType == 1) {
            return true;
        }
        if (this.fType == 2) {
            boolean found = false;
            int listNum = this.fNamespaceList.length;
            for (int i2 = 0; i2 < listNum && !found; i2++) {
                if (namespace == this.fNamespaceList[i2]) {
                    found = true;
                }
            }
            if (!found) {
                return true;
            }
        }
        if (this.fType == 3) {
            int listNum2 = this.fNamespaceList.length;
            for (int i3 = 0; i3 < listNum2; i3++) {
                if (namespace == this.fNamespaceList[i3]) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isSubsetOf(XSWildcardDecl superWildcard) {
        if (superWildcard == null) {
            return false;
        }
        if (superWildcard.fType == 1) {
            return true;
        }
        if (this.fType == 2 && superWildcard.fType == 2 && this.fNamespaceList[0] == superWildcard.fNamespaceList[0]) {
            return true;
        }
        if (this.fType == 3) {
            if (superWildcard.fType == 3 && subset2sets(this.fNamespaceList, superWildcard.fNamespaceList)) {
                return true;
            }
            if (superWildcard.fType == 2 && !elementInSet(superWildcard.fNamespaceList[0], this.fNamespaceList) && !elementInSet(ABSENT, this.fNamespaceList)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean weakerProcessContents(XSWildcardDecl superWildcard) {
        return (this.fProcessContents == 3 && superWildcard.fProcessContents == 1) || (this.fProcessContents == 2 && superWildcard.fProcessContents != 2);
    }

    public XSWildcardDecl performUnionWith(XSWildcardDecl wildcard, short processContents) {
        String[] other;
        String[] list;
        if (wildcard == null) {
            return null;
        }
        XSWildcardDecl unionWildcard = new XSWildcardDecl();
        unionWildcard.fProcessContents = processContents;
        if (areSame(wildcard)) {
            unionWildcard.fType = this.fType;
            unionWildcard.fNamespaceList = this.fNamespaceList;
        } else if (this.fType == 1 || wildcard.fType == 1) {
            unionWildcard.fType = (short) 1;
        } else if (this.fType == 3 && wildcard.fType == 3) {
            unionWildcard.fType = (short) 3;
            unionWildcard.fNamespaceList = union2sets(this.fNamespaceList, wildcard.fNamespaceList);
        } else if (this.fType == 2 && wildcard.fType == 2) {
            unionWildcard.fType = (short) 2;
            unionWildcard.fNamespaceList = new String[2];
            unionWildcard.fNamespaceList[0] = ABSENT;
            unionWildcard.fNamespaceList[1] = ABSENT;
        } else if ((this.fType == 2 && wildcard.fType == 3) || (this.fType == 3 && wildcard.fType == 2)) {
            if (this.fType == 2) {
                other = this.fNamespaceList;
                list = wildcard.fNamespaceList;
            } else {
                other = wildcard.fNamespaceList;
                list = this.fNamespaceList;
            }
            boolean foundAbsent = elementInSet(ABSENT, list);
            if (other[0] != ABSENT) {
                boolean foundNS = elementInSet(other[0], list);
                if (foundNS && foundAbsent) {
                    unionWildcard.fType = (short) 1;
                } else if (foundNS && !foundAbsent) {
                    unionWildcard.fType = (short) 2;
                    unionWildcard.fNamespaceList = new String[2];
                    unionWildcard.fNamespaceList[0] = ABSENT;
                    unionWildcard.fNamespaceList[1] = ABSENT;
                } else {
                    if (!foundNS && foundAbsent) {
                        return null;
                    }
                    unionWildcard.fType = (short) 2;
                    unionWildcard.fNamespaceList = other;
                }
            } else if (foundAbsent) {
                unionWildcard.fType = (short) 1;
            } else {
                unionWildcard.fType = (short) 2;
                unionWildcard.fNamespaceList = other;
            }
        }
        return unionWildcard;
    }

    public XSWildcardDecl performIntersectionWith(XSWildcardDecl wildcard, short processContents) {
        String[] other;
        String[] list;
        if (wildcard == null) {
            return null;
        }
        XSWildcardDecl intersectWildcard = new XSWildcardDecl();
        intersectWildcard.fProcessContents = processContents;
        if (areSame(wildcard)) {
            intersectWildcard.fType = this.fType;
            intersectWildcard.fNamespaceList = this.fNamespaceList;
        } else if (this.fType == 1 || wildcard.fType == 1) {
            XSWildcardDecl other2 = this;
            if (this.fType == 1) {
                other2 = wildcard;
            }
            intersectWildcard.fType = other2.fType;
            intersectWildcard.fNamespaceList = other2.fNamespaceList;
        } else if ((this.fType == 2 && wildcard.fType == 3) || (this.fType == 3 && wildcard.fType == 2)) {
            if (this.fType == 2) {
                other = this.fNamespaceList;
                list = wildcard.fNamespaceList;
            } else {
                other = wildcard.fNamespaceList;
                list = this.fNamespaceList;
            }
            int listSize = list.length;
            String[] intersect = new String[listSize];
            int newSize = 0;
            for (int i2 = 0; i2 < listSize; i2++) {
                if (list[i2] != other[0] && list[i2] != ABSENT) {
                    int i3 = newSize;
                    newSize++;
                    intersect[i3] = list[i2];
                }
            }
            intersectWildcard.fType = (short) 3;
            intersectWildcard.fNamespaceList = new String[newSize];
            System.arraycopy(intersect, 0, intersectWildcard.fNamespaceList, 0, newSize);
        } else if (this.fType == 3 && wildcard.fType == 3) {
            intersectWildcard.fType = (short) 3;
            intersectWildcard.fNamespaceList = intersect2sets(this.fNamespaceList, wildcard.fNamespaceList);
        } else if (this.fType == 2 && wildcard.fType == 2) {
            if (this.fNamespaceList[0] != ABSENT && wildcard.fNamespaceList[0] != ABSENT) {
                return null;
            }
            XSWildcardDecl other3 = this;
            if (this.fNamespaceList[0] == ABSENT) {
                other3 = wildcard;
            }
            intersectWildcard.fType = other3.fType;
            intersectWildcard.fNamespaceList = other3.fNamespaceList;
        }
        return intersectWildcard;
    }

    private boolean areSame(XSWildcardDecl wildcard) {
        if (this.fType == wildcard.fType) {
            if (this.fType == 1) {
                return true;
            }
            if (this.fType == 2) {
                return this.fNamespaceList[0] == wildcard.fNamespaceList[0];
            }
            if (this.fNamespaceList.length == wildcard.fNamespaceList.length) {
                for (int i2 = 0; i2 < this.fNamespaceList.length; i2++) {
                    if (!elementInSet(this.fNamespaceList[i2], wildcard.fNamespaceList)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    String[] intersect2sets(String[] one, String[] theOther) {
        String[] result = new String[Math.min(one.length, theOther.length)];
        int count = 0;
        for (int i2 = 0; i2 < one.length; i2++) {
            if (elementInSet(one[i2], theOther)) {
                int i3 = count;
                count++;
                result[i3] = one[i2];
            }
        }
        String[] result2 = new String[count];
        System.arraycopy(result, 0, result2, 0, count);
        return result2;
    }

    String[] union2sets(String[] one, String[] theOther) {
        String[] result1 = new String[one.length];
        int count = 0;
        for (int i2 = 0; i2 < one.length; i2++) {
            if (!elementInSet(one[i2], theOther)) {
                int i3 = count;
                count++;
                result1[i3] = one[i2];
            }
        }
        String[] result2 = new String[count + theOther.length];
        System.arraycopy(result1, 0, result2, 0, count);
        System.arraycopy(theOther, 0, result2, count, theOther.length);
        return result2;
    }

    boolean subset2sets(String[] subSet, String[] superSet) {
        for (String str : subSet) {
            if (!elementInSet(str, superSet)) {
                return false;
            }
        }
        return true;
    }

    boolean elementInSet(String ele, String[] set) {
        boolean found = false;
        for (int i2 = 0; i2 < set.length && !found; i2++) {
            if (ele == set[i2]) {
                found = true;
            }
        }
        return found;
    }

    public String toString() {
        if (this.fDescription == null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("WC[");
            switch (this.fType) {
                case 1:
                    buffer.append(SchemaSymbols.ATTVAL_TWOPOUNDANY);
                    break;
                case 2:
                    buffer.append(SchemaSymbols.ATTVAL_TWOPOUNDOTHER);
                    buffer.append(":\"");
                    if (this.fNamespaceList[0] != null) {
                        buffer.append(this.fNamespaceList[0]);
                    }
                    buffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                    break;
                case 3:
                    if (this.fNamespaceList.length != 0) {
                        buffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                        if (this.fNamespaceList[0] != null) {
                            buffer.append(this.fNamespaceList[0]);
                        }
                        buffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                        for (int i2 = 1; i2 < this.fNamespaceList.length; i2++) {
                            buffer.append(",\"");
                            if (this.fNamespaceList[i2] != null) {
                                buffer.append(this.fNamespaceList[i2]);
                            }
                            buffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                        }
                        break;
                    }
                    break;
            }
            buffer.append(']');
            this.fDescription = buffer.toString();
        }
        return this.fDescription;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 9;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSWildcard
    public short getConstraintType() {
        return this.fType;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSWildcard
    public StringList getNsConstraintList() {
        return new StringListImpl(this.fNamespaceList, this.fNamespaceList == null ? 0 : this.fNamespaceList.length);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSWildcard
    public short getProcessContents() {
        return this.fProcessContents;
    }

    public String getProcessContentsAsString() {
        switch (this.fProcessContents) {
            case 1:
                return SchemaSymbols.ATTVAL_STRICT;
            case 2:
                return SchemaSymbols.ATTVAL_SKIP;
            case 3:
                return SchemaSymbols.ATTVAL_LAX;
            default:
                return "invalid value";
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSWildcard
    public XSAnnotation getAnnotation() {
        if (this.fAnnotations != null) {
            return (XSAnnotation) this.fAnnotations.item(0);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSWildcard
    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }
}
