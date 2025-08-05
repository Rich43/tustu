package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SubstitutionGroupHandler.class */
public class SubstitutionGroupHandler {
    XSGrammarBucket fGrammarBucket;
    Map<XSElementDecl, Object> fSubGroupsB = new HashMap();
    Map<XSElementDecl, XSElementDecl[]> fSubGroups = new HashMap();
    private static final XSElementDecl[] EMPTY_GROUP = new XSElementDecl[0];
    private static final OneSubGroup[] EMPTY_VECTOR = new OneSubGroup[0];

    public SubstitutionGroupHandler(XSGrammarBucket grammarBucket) {
        this.fGrammarBucket = grammarBucket;
    }

    public XSElementDecl getMatchingElemDecl(QName element, XSElementDecl exemplar) {
        SchemaGrammar sGrammar;
        XSElementDecl eDecl;
        if (element.localpart == exemplar.fName && element.uri == exemplar.fTargetNamespace) {
            return exemplar;
        }
        if (exemplar.fScope == 1 && (exemplar.fBlock & 4) == 0 && (sGrammar = this.fGrammarBucket.getGrammar(element.uri)) != null && (eDecl = sGrammar.getGlobalElementDecl(element.localpart)) != null && substitutionGroupOK(eDecl, exemplar, exemplar.fBlock)) {
            return eDecl;
        }
        return null;
    }

    protected boolean substitutionGroupOK(XSElementDecl element, XSElementDecl exemplar, short blockingConstraint) {
        XSElementDecl subGroup;
        if (element == exemplar) {
            return true;
        }
        if ((blockingConstraint & 4) != 0) {
            return false;
        }
        XSElementDecl xSElementDecl = element.fSubGroup;
        while (true) {
            subGroup = xSElementDecl;
            if (subGroup == null || subGroup == exemplar) {
                break;
            }
            xSElementDecl = subGroup.fSubGroup;
        }
        if (subGroup == null) {
            return false;
        }
        return typeDerivationOK(element.fType, exemplar.fType, blockingConstraint);
    }

    private boolean typeDerivationOK(XSTypeDefinition derived, XSTypeDefinition base, short blockingConstraint) {
        short devMethod = 0;
        short blockConstraint = blockingConstraint;
        XSTypeDefinition type = derived;
        while (type != base && type != SchemaGrammar.fAnyType) {
            if (type.getTypeCategory() == 15) {
                devMethod = (short) (devMethod | ((XSComplexTypeDecl) type).fDerivedBy);
            } else {
                devMethod = (short) (devMethod | 2);
            }
            type = type.getBaseType();
            if (type == null) {
                type = SchemaGrammar.fAnyType;
            }
            if (type.getTypeCategory() == 15) {
                blockConstraint = (short) (blockConstraint | ((XSComplexTypeDecl) type).fBlock);
            }
        }
        if (type != base) {
            if (base.getTypeCategory() == 16) {
                XSSimpleTypeDefinition st = (XSSimpleTypeDefinition) base;
                if (st.getVariety() == 3) {
                    XSObjectList memberTypes = st.getMemberTypes();
                    int length = memberTypes.getLength();
                    for (int i2 = 0; i2 < length; i2++) {
                        if (typeDerivationOK(derived, (XSTypeDefinition) memberTypes.item(i2), blockingConstraint)) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        if ((devMethod & blockConstraint) != 0) {
            return false;
        }
        return true;
    }

    public boolean inSubstitutionGroup(XSElementDecl element, XSElementDecl exemplar) {
        return substitutionGroupOK(element, exemplar, exemplar.fBlock);
    }

    public void reset() {
        this.fSubGroupsB.clear();
        this.fSubGroups.clear();
    }

    public void addSubstitutionGroup(XSElementDecl[] elements) {
        for (int i2 = elements.length - 1; i2 >= 0; i2--) {
            XSElementDecl element = elements[i2];
            XSElementDecl subHead = element.fSubGroup;
            Vector subGroup = (Vector) this.fSubGroupsB.get(subHead);
            if (subGroup == null) {
                subGroup = new Vector();
                this.fSubGroupsB.put(subHead, subGroup);
            }
            subGroup.addElement(element);
        }
    }

    public XSElementDecl[] getSubstitutionGroup(XSElementDecl element) {
        XSElementDecl[] subGroup = this.fSubGroups.get(element);
        if (subGroup != null) {
            return subGroup;
        }
        if ((element.fBlock & 4) != 0) {
            this.fSubGroups.put(element, EMPTY_GROUP);
            return EMPTY_GROUP;
        }
        OneSubGroup[] groupB = getSubGroupB(element, new OneSubGroup());
        int len = groupB.length;
        int rlen = 0;
        XSElementDecl[] ret = new XSElementDecl[len];
        for (int i2 = 0; i2 < len; i2++) {
            if ((element.fBlock & groupB[i2].dMethod) == 0) {
                int i3 = rlen;
                rlen++;
                ret[i3] = groupB[i2].sub;
            }
        }
        if (rlen < len) {
            XSElementDecl[] ret1 = new XSElementDecl[rlen];
            System.arraycopy(ret, 0, ret1, 0, rlen);
            ret = ret1;
        }
        this.fSubGroups.put(element, ret);
        return ret;
    }

    private OneSubGroup[] getSubGroupB(XSElementDecl element, OneSubGroup methods) {
        Object subGroup = this.fSubGroupsB.get(element);
        if (subGroup == null) {
            this.fSubGroupsB.put(element, EMPTY_VECTOR);
            return EMPTY_VECTOR;
        }
        if (subGroup instanceof OneSubGroup[]) {
            return (OneSubGroup[]) subGroup;
        }
        Vector group = (Vector) subGroup;
        Vector newGroup = new Vector();
        for (int i2 = group.size() - 1; i2 >= 0; i2--) {
            XSElementDecl sub = (XSElementDecl) group.elementAt(i2);
            if (getDBMethods(sub.fType, element.fType, methods)) {
                short dMethod = methods.dMethod;
                short bMethod = methods.bMethod;
                newGroup.addElement(new OneSubGroup(sub, methods.dMethod, methods.bMethod));
                OneSubGroup[] group1 = getSubGroupB(sub, methods);
                for (int j2 = group1.length - 1; j2 >= 0; j2--) {
                    short dSubMethod = (short) (dMethod | group1[j2].dMethod);
                    short bSubMethod = (short) (bMethod | group1[j2].bMethod);
                    if ((dSubMethod & bSubMethod) == 0) {
                        newGroup.addElement(new OneSubGroup(group1[j2].sub, dSubMethod, bSubMethod));
                    }
                }
            }
        }
        OneSubGroup[] ret = new OneSubGroup[newGroup.size()];
        for (int i3 = newGroup.size() - 1; i3 >= 0; i3--) {
            ret[i3] = (OneSubGroup) newGroup.elementAt(i3);
        }
        this.fSubGroupsB.put(element, ret);
        return ret;
    }

    private boolean getDBMethods(XSTypeDefinition typed, XSTypeDefinition typeb, OneSubGroup methods) {
        short dMethod = 0;
        short bMethod = 0;
        while (typed != typeb && typed != SchemaGrammar.fAnyType) {
            if (typed.getTypeCategory() == 15) {
                dMethod = (short) (dMethod | ((XSComplexTypeDecl) typed).fDerivedBy);
            } else {
                dMethod = (short) (dMethod | 2);
            }
            typed = typed.getBaseType();
            if (typed == null) {
                typed = SchemaGrammar.fAnyType;
            }
            if (typed.getTypeCategory() == 15) {
                bMethod = (short) (bMethod | ((XSComplexTypeDecl) typed).fBlock);
            }
        }
        if (typed != typeb || (dMethod & bMethod) != 0) {
            return false;
        }
        methods.dMethod = dMethod;
        methods.bMethod = bMethod;
        return true;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SubstitutionGroupHandler$OneSubGroup.class */
    private static final class OneSubGroup {
        XSElementDecl sub;
        short dMethod;
        short bMethod;

        OneSubGroup() {
        }

        OneSubGroup(XSElementDecl sub, short dMethod, short bMethod) {
            this.sub = sub;
            this.dMethod = dMethod;
            this.bMethod = bMethod;
        }
    }
}
