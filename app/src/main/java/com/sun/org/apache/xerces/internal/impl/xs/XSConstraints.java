package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
import com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSConstraints.class */
public class XSConstraints {
    static final int OCCURRENCE_UNKNOWN = -2;
    static final XSSimpleType STRING_TYPE = (XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("string");
    private static XSParticleDecl fEmptyParticle = null;
    private static final Comparator ELEMENT_PARTICLE_COMPARATOR = new Comparator() { // from class: com.sun.org.apache.xerces.internal.impl.xs.XSConstraints.1
        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            XSParticleDecl pDecl1 = (XSParticleDecl) o1;
            XSParticleDecl pDecl2 = (XSParticleDecl) o2;
            XSElementDecl decl1 = (XSElementDecl) pDecl1.fValue;
            XSElementDecl decl2 = (XSElementDecl) pDecl2.fValue;
            String namespace1 = decl1.getNamespace();
            String namespace2 = decl2.getNamespace();
            String name1 = decl1.getName();
            String name2 = decl2.getName();
            boolean sameNamespace = namespace1 == namespace2;
            int namespaceComparison = 0;
            if (!sameNamespace) {
                if (namespace1 != null) {
                    if (namespace2 != null) {
                        namespaceComparison = namespace1.compareTo(namespace2);
                    } else {
                        namespaceComparison = 1;
                    }
                } else {
                    namespaceComparison = -1;
                }
            }
            return namespaceComparison != 0 ? namespaceComparison : name1.compareTo(name2);
        }
    };

    public static XSParticleDecl getEmptySequence() {
        if (fEmptyParticle == null) {
            XSModelGroupImpl group = new XSModelGroupImpl();
            group.fCompositor = (short) 102;
            group.fParticleCount = 0;
            group.fParticles = null;
            group.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            XSParticleDecl particle = new XSParticleDecl();
            particle.fType = (short) 3;
            particle.fValue = group;
            particle.fAnnotations = XSObjectListImpl.EMPTY_LIST;
            fEmptyParticle = particle;
        }
        return fEmptyParticle;
    }

    public static boolean checkTypeDerivationOk(XSTypeDefinition derived, XSTypeDefinition base, short block) {
        if (derived == SchemaGrammar.fAnyType) {
            return derived == base;
        }
        if (derived == SchemaGrammar.fAnySimpleType) {
            return base == SchemaGrammar.fAnyType || base == SchemaGrammar.fAnySimpleType;
        }
        if (derived.getTypeCategory() == 16) {
            if (base.getTypeCategory() == 15) {
                if (base == SchemaGrammar.fAnyType) {
                    base = SchemaGrammar.fAnySimpleType;
                } else {
                    return false;
                }
            }
            return checkSimpleDerivation((XSSimpleType) derived, (XSSimpleType) base, block);
        }
        return checkComplexDerivation((XSComplexTypeDecl) derived, base, block);
    }

    public static boolean checkSimpleDerivationOk(XSSimpleType derived, XSTypeDefinition base, short block) {
        if (derived == SchemaGrammar.fAnySimpleType) {
            return base == SchemaGrammar.fAnyType || base == SchemaGrammar.fAnySimpleType;
        }
        if (base.getTypeCategory() == 15) {
            if (base == SchemaGrammar.fAnyType) {
                base = SchemaGrammar.fAnySimpleType;
            } else {
                return false;
            }
        }
        return checkSimpleDerivation(derived, (XSSimpleType) base, block);
    }

    public static boolean checkComplexDerivationOk(XSComplexTypeDecl derived, XSTypeDefinition base, short block) {
        if (derived == SchemaGrammar.fAnyType) {
            return derived == base;
        }
        return checkComplexDerivation(derived, base, block);
    }

    private static boolean checkSimpleDerivation(XSSimpleType derived, XSSimpleType base, short block) {
        if (derived == base) {
            return true;
        }
        if ((block & 2) != 0 || (derived.getBaseType().getFinal() & 2) != 0) {
            return false;
        }
        XSSimpleType directBase = (XSSimpleType) derived.getBaseType();
        if (directBase == base) {
            return true;
        }
        if (directBase != SchemaGrammar.fAnySimpleType && checkSimpleDerivation(directBase, base, block)) {
            return true;
        }
        if ((derived.getVariety() == 2 || derived.getVariety() == 3) && base == SchemaGrammar.fAnySimpleType) {
            return true;
        }
        if (base.getVariety() == 3) {
            XSObjectList subUnionMemberDV = base.getMemberTypes();
            int subUnionSize = subUnionMemberDV.getLength();
            for (int i2 = 0; i2 < subUnionSize; i2++) {
                if (checkSimpleDerivation(derived, (XSSimpleType) subUnionMemberDV.item(i2), block)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private static boolean checkComplexDerivation(XSComplexTypeDecl derived, XSTypeDefinition base, short block) {
        if (derived == base) {
            return true;
        }
        if ((derived.fDerivedBy & block) != 0) {
            return false;
        }
        XSTypeDefinition directBase = derived.fBaseType;
        if (directBase == base) {
            return true;
        }
        if (directBase == SchemaGrammar.fAnyType || directBase == SchemaGrammar.fAnySimpleType) {
            return false;
        }
        if (directBase.getTypeCategory() == 15) {
            return checkComplexDerivation((XSComplexTypeDecl) directBase, base, block);
        }
        if (directBase.getTypeCategory() == 16) {
            if (base.getTypeCategory() == 15) {
                if (base == SchemaGrammar.fAnyType) {
                    base = SchemaGrammar.fAnySimpleType;
                } else {
                    return false;
                }
            }
            return checkSimpleDerivation((XSSimpleType) directBase, (XSSimpleType) base, block);
        }
        return false;
    }

    public static Object ElementDefaultValidImmediate(XSTypeDefinition type, String value, ValidationContext context, ValidatedInfo vinfo) {
        XSSimpleType dv = null;
        if (type.getTypeCategory() == 16) {
            dv = (XSSimpleType) type;
        } else {
            XSComplexTypeDecl ctype = (XSComplexTypeDecl) type;
            if (ctype.fContentType == 1) {
                dv = ctype.fXSSimpleType;
            } else if (ctype.fContentType != 3 || !((XSParticleDecl) ctype.getParticle()).emptiable()) {
                return null;
            }
        }
        if (dv == null) {
            dv = STRING_TYPE;
        }
        try {
            Object actualValue = dv.validate(value, context, vinfo);
            if (vinfo != null) {
                actualValue = dv.validate(vinfo.stringValue(), context, vinfo);
            }
            return actualValue;
        } catch (InvalidDatatypeValueException e2) {
            return null;
        }
    }

    static void reportSchemaError(XMLErrorReporter errorReporter, SimpleLocator loc, String key, Object[] args) throws XNIException {
        if (loc != null) {
            errorReporter.reportError((XMLLocator) loc, XSMessageFormatter.SCHEMA_DOMAIN, key, args, (short) 1);
        } else {
            errorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, key, args, (short) 1);
        }
    }

    public static void fullSchemaChecking(XSGrammarBucket grammarBucket, SubstitutionGroupHandler SGHandler, CMBuilder cmBuilder, XMLErrorReporter errorReporter) {
        SchemaGrammar[] grammars = grammarBucket.getGrammars();
        for (int i2 = grammars.length - 1; i2 >= 0; i2--) {
            SGHandler.addSubstitutionGroup(grammars[i2].getSubstitutionGroups());
        }
        XSParticleDecl fakeDerived = new XSParticleDecl();
        XSParticleDecl fakeBase = new XSParticleDecl();
        fakeDerived.fType = (short) 3;
        fakeBase.fType = (short) 3;
        for (int g2 = grammars.length - 1; g2 >= 0; g2--) {
            XSGroupDecl[] redefinedGroups = grammars[g2].getRedefinedGroupDecls();
            SimpleLocator[] rgLocators = grammars[g2].getRGLocators();
            int i3 = 0;
            while (i3 < redefinedGroups.length) {
                int i4 = i3;
                int i5 = i3 + 1;
                XSGroupDecl derivedGrp = redefinedGroups[i4];
                XSModelGroupImpl derivedMG = derivedGrp.fModelGroup;
                i3 = i5 + 1;
                XSGroupDecl baseGrp = redefinedGroups[i5];
                XSModelGroupImpl baseMG = baseGrp.fModelGroup;
                fakeDerived.fValue = derivedMG;
                fakeBase.fValue = baseMG;
                if (baseMG == null) {
                    if (derivedMG != null) {
                        reportSchemaError(errorReporter, rgLocators[(i3 / 2) - 1], "src-redefine.6.2.2", new Object[]{derivedGrp.fName, "rcase-Recurse.2"});
                    }
                } else if (derivedMG == null) {
                    if (!fakeBase.emptiable()) {
                        reportSchemaError(errorReporter, rgLocators[(i3 / 2) - 1], "src-redefine.6.2.2", new Object[]{derivedGrp.fName, "rcase-Recurse.2"});
                    }
                } else {
                    try {
                        particleValidRestriction(fakeDerived, SGHandler, fakeBase, SGHandler);
                    } catch (XMLSchemaException e2) {
                        String key = e2.getKey();
                        reportSchemaError(errorReporter, rgLocators[(i3 / 2) - 1], key, e2.getArgs());
                        reportSchemaError(errorReporter, rgLocators[(i3 / 2) - 1], "src-redefine.6.2.2", new Object[]{derivedGrp.fName, key});
                    }
                }
            }
        }
        SymbolHash elemTable = new SymbolHash();
        for (int i6 = grammars.length - 1; i6 >= 0; i6--) {
            int keepType = 0;
            boolean fullChecked = grammars[i6].fFullChecked;
            XSComplexTypeDecl[] types = grammars[i6].getUncheckedComplexTypeDecls();
            SimpleLocator[] ctLocators = grammars[i6].getUncheckedCTLocators();
            for (int j2 = 0; j2 < types.length; j2++) {
                if (!fullChecked && types[j2].fParticle != null) {
                    elemTable.clear();
                    try {
                        checkElementDeclsConsistent(types[j2], types[j2].fParticle, elemTable, SGHandler);
                    } catch (XMLSchemaException e3) {
                        reportSchemaError(errorReporter, ctLocators[j2], e3.getKey(), e3.getArgs());
                    }
                }
                if (types[j2].fBaseType != null && types[j2].fBaseType != SchemaGrammar.fAnyType && types[j2].fDerivedBy == 2 && (types[j2].fBaseType instanceof XSComplexTypeDecl)) {
                    XSParticleDecl derivedParticle = types[j2].fParticle;
                    XSParticleDecl baseParticle = ((XSComplexTypeDecl) types[j2].fBaseType).fParticle;
                    if (derivedParticle == null) {
                        if (baseParticle != null && !baseParticle.emptiable()) {
                            reportSchemaError(errorReporter, ctLocators[j2], "derivation-ok-restriction.5.3.2", new Object[]{types[j2].fName, types[j2].fBaseType.getName()});
                        }
                    } else if (baseParticle != null) {
                        try {
                            particleValidRestriction(types[j2].fParticle, SGHandler, ((XSComplexTypeDecl) types[j2].fBaseType).fParticle, SGHandler);
                        } catch (XMLSchemaException e4) {
                            reportSchemaError(errorReporter, ctLocators[j2], e4.getKey(), e4.getArgs());
                            reportSchemaError(errorReporter, ctLocators[j2], "derivation-ok-restriction.5.4.2", new Object[]{types[j2].fName});
                        }
                    } else {
                        reportSchemaError(errorReporter, ctLocators[j2], "derivation-ok-restriction.5.4.2", new Object[]{types[j2].fName});
                    }
                }
                XSCMValidator cm = types[j2].getContentModel(cmBuilder);
                boolean further = false;
                if (cm != null) {
                    try {
                        further = cm.checkUniqueParticleAttribution(SGHandler);
                    } catch (XMLSchemaException e5) {
                        reportSchemaError(errorReporter, ctLocators[j2], e5.getKey(), e5.getArgs());
                    }
                }
                if (!fullChecked && further) {
                    int i7 = keepType;
                    keepType++;
                    types[i7] = types[j2];
                }
            }
            if (!fullChecked) {
                grammars[i6].setUncheckedTypeNum(keepType);
                grammars[i6].fFullChecked = true;
            }
        }
    }

    public static void checkElementDeclsConsistent(XSComplexTypeDecl type, XSParticleDecl particle, SymbolHash elemDeclHash, SubstitutionGroupHandler sgHandler) throws XMLSchemaException {
        int pType = particle.fType;
        if (pType == 2) {
            return;
        }
        if (pType == 1) {
            XSElementDecl elem = (XSElementDecl) particle.fValue;
            findElemInTable(type, elem, elemDeclHash);
            if (elem.fScope == 1) {
                XSElementDecl[] subGroup = sgHandler.getSubstitutionGroup(elem);
                for (XSElementDecl xSElementDecl : subGroup) {
                    findElemInTable(type, xSElementDecl, elemDeclHash);
                }
                return;
            }
            return;
        }
        XSModelGroupImpl group = (XSModelGroupImpl) particle.fValue;
        for (int i2 = 0; i2 < group.fParticleCount; i2++) {
            checkElementDeclsConsistent(type, group.fParticles[i2], elemDeclHash, sgHandler);
        }
    }

    public static void findElemInTable(XSComplexTypeDecl type, XSElementDecl elem, SymbolHash elemDeclHash) throws XMLSchemaException {
        String name = elem.fName + "," + elem.fTargetNamespace;
        XSElementDecl existingElem = (XSElementDecl) elemDeclHash.get(name);
        if (existingElem == null) {
            elemDeclHash.put(name, elem);
        } else if (elem != existingElem && elem.fType != existingElem.fType) {
            throw new XMLSchemaException("cos-element-consistent", new Object[]{type.fName, elem.fName});
        }
    }

    private static boolean particleValidRestriction(XSParticleDecl dParticle, SubstitutionGroupHandler dSGHandler, XSParticleDecl bParticle, SubstitutionGroupHandler bSGHandler) throws XMLSchemaException {
        return particleValidRestriction(dParticle, dSGHandler, bParticle, bSGHandler, true);
    }

    private static boolean particleValidRestriction(XSParticleDecl dParticle, SubstitutionGroupHandler dSGHandler, XSParticleDecl bParticle, SubstitutionGroupHandler bSGHandler, boolean checkWCOccurrence) throws XMLSchemaException {
        Vector dChildren = null;
        Vector bChildren = null;
        int dMinEffectiveTotalRange = -2;
        int dMaxEffectiveTotalRange = -2;
        boolean bExpansionHappened = false;
        if (dParticle.isEmpty() && !bParticle.emptiable()) {
            throw new XMLSchemaException("cos-particle-restrict.a", null);
        }
        if (!dParticle.isEmpty() && bParticle.isEmpty()) {
            throw new XMLSchemaException("cos-particle-restrict.b", null);
        }
        short dType = dParticle.fType;
        if (dType == 3) {
            dType = ((XSModelGroupImpl) dParticle.fValue).fCompositor;
            XSParticleDecl dtmp = getNonUnaryGroup(dParticle);
            if (dtmp != dParticle) {
                dParticle = dtmp;
                dType = dParticle.fType;
                if (dType == 3) {
                    dType = ((XSModelGroupImpl) dParticle.fValue).fCompositor;
                }
            }
            dChildren = removePointlessChildren(dParticle);
        }
        int dMinOccurs = dParticle.fMinOccurs;
        int dMaxOccurs = dParticle.fMaxOccurs;
        if (dSGHandler != null && dType == 1) {
            XSElementDecl dElement = (XSElementDecl) dParticle.fValue;
            if (dElement.fScope == 1) {
                XSElementDecl[] subGroup = dSGHandler.getSubstitutionGroup(dElement);
                if (subGroup.length > 0) {
                    dType = 101;
                    dMinEffectiveTotalRange = dMinOccurs;
                    dMaxEffectiveTotalRange = dMaxOccurs;
                    dChildren = new Vector(subGroup.length + 1);
                    for (XSElementDecl xSElementDecl : subGroup) {
                        addElementToParticleVector(dChildren, xSElementDecl);
                    }
                    addElementToParticleVector(dChildren, dElement);
                    Collections.sort(dChildren, ELEMENT_PARTICLE_COMPARATOR);
                    dSGHandler = null;
                }
            }
        }
        short bType = bParticle.fType;
        if (bType == 3) {
            bType = ((XSModelGroupImpl) bParticle.fValue).fCompositor;
            XSParticleDecl btmp = getNonUnaryGroup(bParticle);
            if (btmp != bParticle) {
                bParticle = btmp;
                bType = bParticle.fType;
                if (bType == 3) {
                    bType = ((XSModelGroupImpl) bParticle.fValue).fCompositor;
                }
            }
            bChildren = removePointlessChildren(bParticle);
        }
        int bMinOccurs = bParticle.fMinOccurs;
        int bMaxOccurs = bParticle.fMaxOccurs;
        if (bSGHandler != null && bType == 1) {
            XSElementDecl bElement = (XSElementDecl) bParticle.fValue;
            if (bElement.fScope == 1) {
                XSElementDecl[] bsubGroup = bSGHandler.getSubstitutionGroup(bElement);
                if (bsubGroup.length > 0) {
                    bType = 101;
                    bChildren = new Vector(bsubGroup.length + 1);
                    for (XSElementDecl xSElementDecl2 : bsubGroup) {
                        addElementToParticleVector(bChildren, xSElementDecl2);
                    }
                    addElementToParticleVector(bChildren, bElement);
                    Collections.sort(bChildren, ELEMENT_PARTICLE_COMPARATOR);
                    bSGHandler = null;
                    bExpansionHappened = true;
                }
            }
        }
        switch (dType) {
            case 1:
                switch (bType) {
                    case 1:
                        checkNameAndTypeOK((XSElementDecl) dParticle.fValue, dMinOccurs, dMaxOccurs, (XSElementDecl) bParticle.fValue, bMinOccurs, bMaxOccurs);
                        return bExpansionHappened;
                    case 2:
                        checkNSCompat((XSElementDecl) dParticle.fValue, dMinOccurs, dMaxOccurs, (XSWildcardDecl) bParticle.fValue, bMinOccurs, bMaxOccurs, checkWCOccurrence);
                        return bExpansionHappened;
                    case 101:
                        Vector dChildren2 = new Vector();
                        dChildren2.addElement(dParticle);
                        checkRecurseLax(dChildren2, 1, 1, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    case 102:
                    case 103:
                        Vector dChildren3 = new Vector();
                        dChildren3.addElement(dParticle);
                        checkRecurse(dChildren3, 1, 1, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    default:
                        throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
                }
            case 2:
                switch (bType) {
                    case 1:
                    case 101:
                    case 102:
                    case 103:
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"any:choice,sequence,all,elt"});
                    case 2:
                        checkNSSubset((XSWildcardDecl) dParticle.fValue, dMinOccurs, dMaxOccurs, (XSWildcardDecl) bParticle.fValue, bMinOccurs, bMaxOccurs);
                        return bExpansionHappened;
                    default:
                        throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
                }
            case 101:
                switch (bType) {
                    case 1:
                    case 102:
                    case 103:
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"choice:all,sequence,elt"});
                    case 2:
                        if (dMinEffectiveTotalRange == -2) {
                            dMinEffectiveTotalRange = dParticle.minEffectiveTotalRange();
                        }
                        if (dMaxEffectiveTotalRange == -2) {
                            dMaxEffectiveTotalRange = dParticle.maxEffectiveTotalRange();
                        }
                        checkNSRecurseCheckCardinality(dChildren, dMinEffectiveTotalRange, dMaxEffectiveTotalRange, dSGHandler, bParticle, bMinOccurs, bMaxOccurs, checkWCOccurrence);
                        return bExpansionHappened;
                    case 101:
                        checkRecurseLax(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    default:
                        throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
                }
            case 102:
                switch (bType) {
                    case 1:
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"seq:elt"});
                    case 2:
                        if (dMinEffectiveTotalRange == -2) {
                            dMinEffectiveTotalRange = dParticle.minEffectiveTotalRange();
                        }
                        if (dMaxEffectiveTotalRange == -2) {
                            dMaxEffectiveTotalRange = dParticle.maxEffectiveTotalRange();
                        }
                        checkNSRecurseCheckCardinality(dChildren, dMinEffectiveTotalRange, dMaxEffectiveTotalRange, dSGHandler, bParticle, bMinOccurs, bMaxOccurs, checkWCOccurrence);
                        return bExpansionHappened;
                    case 101:
                        int min1 = dMinOccurs * dChildren.size();
                        int max1 = dMaxOccurs == -1 ? dMaxOccurs : dMaxOccurs * dChildren.size();
                        checkMapAndSum(dChildren, min1, max1, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    case 102:
                        checkRecurse(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    case 103:
                        checkRecurseUnordered(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    default:
                        throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
                }
            case 103:
                switch (bType) {
                    case 1:
                    case 101:
                    case 102:
                        throw new XMLSchemaException("cos-particle-restrict.2", new Object[]{"all:choice,sequence,elt"});
                    case 2:
                        if (dMinEffectiveTotalRange == -2) {
                            dMinEffectiveTotalRange = dParticle.minEffectiveTotalRange();
                        }
                        if (dMaxEffectiveTotalRange == -2) {
                            dMaxEffectiveTotalRange = dParticle.maxEffectiveTotalRange();
                        }
                        checkNSRecurseCheckCardinality(dChildren, dMinEffectiveTotalRange, dMaxEffectiveTotalRange, dSGHandler, bParticle, bMinOccurs, bMaxOccurs, checkWCOccurrence);
                        return bExpansionHappened;
                    case 103:
                        checkRecurse(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
                        return bExpansionHappened;
                    default:
                        throw new XMLSchemaException("Internal-Error", new Object[]{"in particleValidRestriction"});
                }
            default:
                return bExpansionHappened;
        }
    }

    private static void addElementToParticleVector(Vector v2, XSElementDecl d2) {
        XSParticleDecl p2 = new XSParticleDecl();
        p2.fValue = d2;
        p2.fType = (short) 1;
        v2.addElement(p2);
    }

    private static XSParticleDecl getNonUnaryGroup(XSParticleDecl p2) {
        if (p2.fType == 1 || p2.fType == 2) {
            return p2;
        }
        if (p2.fMinOccurs == 1 && p2.fMaxOccurs == 1 && p2.fValue != null && ((XSModelGroupImpl) p2.fValue).fParticleCount == 1) {
            return getNonUnaryGroup(((XSModelGroupImpl) p2.fValue).fParticles[0]);
        }
        return p2;
    }

    private static Vector removePointlessChildren(XSParticleDecl p2) {
        if (p2.fType == 1 || p2.fType == 2) {
            return null;
        }
        Vector children = new Vector();
        XSModelGroupImpl group = (XSModelGroupImpl) p2.fValue;
        for (int i2 = 0; i2 < group.fParticleCount; i2++) {
            gatherChildren(group.fCompositor, group.fParticles[i2], children);
        }
        return children;
    }

    private static void gatherChildren(int parentType, XSParticleDecl p2, Vector children) {
        int min = p2.fMinOccurs;
        int max = p2.fMaxOccurs;
        int type = p2.fType;
        if (type == 3) {
            type = ((XSModelGroupImpl) p2.fValue).fCompositor;
        }
        if (type == 1 || type == 2) {
            children.addElement(p2);
            return;
        }
        if (min != 1 || max != 1) {
            children.addElement(p2);
            return;
        }
        if (parentType == type) {
            XSModelGroupImpl group = (XSModelGroupImpl) p2.fValue;
            for (int i2 = 0; i2 < group.fParticleCount; i2++) {
                gatherChildren(type, group.fParticles[i2], children);
            }
            return;
        }
        if (!p2.isEmpty()) {
            children.addElement(p2);
        }
    }

    private static void checkNameAndTypeOK(XSElementDecl dElement, int dMin, int dMax, XSElementDecl bElement, int bMin, int bMax) throws XMLSchemaException {
        if (dElement.fName != bElement.fName || dElement.fTargetNamespace != bElement.fTargetNamespace) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.1", new Object[]{dElement.fName, dElement.fTargetNamespace, bElement.fName, bElement.fTargetNamespace});
        }
        if (!bElement.getNillable() && dElement.getNillable()) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.2", new Object[]{dElement.fName});
        }
        if (!checkOccurrenceRange(dMin, dMax, bMin, bMax)) {
            Object[] objArr = new Object[5];
            objArr[0] = dElement.fName;
            objArr[1] = Integer.toString(dMin);
            objArr[2] = dMax == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(dMax);
            objArr[3] = Integer.toString(bMin);
            objArr[4] = bMax == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(bMax);
            throw new XMLSchemaException("rcase-NameAndTypeOK.3", objArr);
        }
        if (bElement.getConstraintType() == 2) {
            if (dElement.getConstraintType() != 2) {
                throw new XMLSchemaException("rcase-NameAndTypeOK.4.a", new Object[]{dElement.fName, bElement.fDefault.stringValue()});
            }
            boolean isSimple = false;
            if (dElement.fType.getTypeCategory() == 16 || ((XSComplexTypeDecl) dElement.fType).fContentType == 1) {
                isSimple = true;
            }
            if ((!isSimple && !bElement.fDefault.normalizedValue.equals(dElement.fDefault.normalizedValue)) || (isSimple && !bElement.fDefault.actualValue.equals(dElement.fDefault.actualValue))) {
                throw new XMLSchemaException("rcase-NameAndTypeOK.4.b", new Object[]{dElement.fName, dElement.fDefault.stringValue(), bElement.fDefault.stringValue()});
            }
        }
        checkIDConstraintRestriction(dElement, bElement);
        int blockSet1 = dElement.fBlock;
        int blockSet2 = bElement.fBlock;
        if ((blockSet1 & blockSet2) != blockSet2 || (blockSet1 == 0 && blockSet2 != 0)) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.6", new Object[]{dElement.fName});
        }
        if (!checkTypeDerivationOk(dElement.fType, bElement.fType, (short) 25)) {
            throw new XMLSchemaException("rcase-NameAndTypeOK.7", new Object[]{dElement.fName, dElement.fType.getName(), bElement.fType.getName()});
        }
    }

    private static void checkIDConstraintRestriction(XSElementDecl derivedElemDecl, XSElementDecl baseElemDecl) throws XMLSchemaException {
    }

    private static boolean checkOccurrenceRange(int min1, int max1, int min2, int max2) {
        if (min1 < min2) {
            return false;
        }
        if (max2 == -1) {
            return true;
        }
        if (max1 != -1 && max1 <= max2) {
            return true;
        }
        return false;
    }

    private static void checkNSCompat(XSElementDecl elem, int min1, int max1, XSWildcardDecl wildcard, int min2, int max2, boolean checkWCOccurrence) throws XMLSchemaException {
        if (checkWCOccurrence && !checkOccurrenceRange(min1, max1, min2, max2)) {
            Object[] objArr = new Object[5];
            objArr[0] = elem.fName;
            objArr[1] = Integer.toString(min1);
            objArr[2] = max1 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max1);
            objArr[3] = Integer.toString(min2);
            objArr[4] = max2 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max2);
            throw new XMLSchemaException("rcase-NSCompat.2", objArr);
        }
        if (!wildcard.allowNamespace(elem.fTargetNamespace)) {
            throw new XMLSchemaException("rcase-NSCompat.1", new Object[]{elem.fName, elem.fTargetNamespace});
        }
    }

    private static void checkNSSubset(XSWildcardDecl dWildcard, int min1, int max1, XSWildcardDecl bWildcard, int min2, int max2) throws XMLSchemaException {
        if (!checkOccurrenceRange(min1, max1, min2, max2)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(min1);
            objArr[1] = max1 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max1);
            objArr[2] = Integer.toString(min2);
            objArr[3] = max2 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max2);
            throw new XMLSchemaException("rcase-NSSubset.2", objArr);
        }
        if (!dWildcard.isSubsetOf(bWildcard)) {
            throw new XMLSchemaException("rcase-NSSubset.1", null);
        }
        if (dWildcard.weakerProcessContents(bWildcard)) {
            throw new XMLSchemaException("rcase-NSSubset.3", new Object[]{dWildcard.getProcessContentsAsString(), bWildcard.getProcessContentsAsString()});
        }
    }

    private static void checkNSRecurseCheckCardinality(Vector children, int min1, int max1, SubstitutionGroupHandler dSGHandler, XSParticleDecl wildcard, int min2, int max2, boolean checkWCOccurrence) throws XMLSchemaException {
        if (checkWCOccurrence && !checkOccurrenceRange(min1, max1, min2, max2)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(min1);
            objArr[1] = max1 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max1);
            objArr[2] = Integer.toString(min2);
            objArr[3] = max2 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max2);
            throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.2", objArr);
        }
        int count = children.size();
        for (int i2 = 0; i2 < count; i2++) {
            try {
                XSParticleDecl particle1 = (XSParticleDecl) children.elementAt(i2);
                particleValidRestriction(particle1, dSGHandler, wildcard, null, false);
            } catch (XMLSchemaException e2) {
                throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.1", null);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x00c0, code lost:
    
        r20 = r20 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void checkRecurse(java.util.Vector r9, int r10, int r11, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r12, java.util.Vector r13, int r14, int r15, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r16) throws com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException {
        /*
            Method dump skipped, instructions count: 247
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.XSConstraints.checkRecurse(java.util.Vector, int, int, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler, java.util.Vector, int, int, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler):void");
    }

    private static void checkRecurseUnordered(Vector dChildren, int min1, int max1, SubstitutionGroupHandler dSGHandler, Vector bChildren, int min2, int max2, SubstitutionGroupHandler bSGHandler) throws XMLSchemaException {
        if (!checkOccurrenceRange(min1, max1, min2, max2)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(min1);
            objArr[1] = max1 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max1);
            objArr[2] = Integer.toString(min2);
            objArr[3] = max2 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max2);
            throw new XMLSchemaException("rcase-RecurseUnordered.1", objArr);
        }
        int count1 = dChildren.size();
        int count2 = bChildren.size();
        boolean[] foundIt = new boolean[count2];
        for (int i2 = 0; i2 < count1; i2++) {
            XSParticleDecl particle1 = (XSParticleDecl) dChildren.elementAt(i2);
            for (int j2 = 0; j2 < count2; j2++) {
                XSParticleDecl particle2 = (XSParticleDecl) bChildren.elementAt(j2);
                try {
                    particleValidRestriction(particle1, dSGHandler, particle2, bSGHandler);
                    if (foundIt[j2]) {
                        throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
                    }
                    foundIt[j2] = true;
                } catch (XMLSchemaException e2) {
                }
            }
            throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
        }
        for (int j3 = 0; j3 < count2; j3++) {
            XSParticleDecl particle22 = (XSParticleDecl) bChildren.elementAt(j3);
            if (!foundIt[j3] && !particle22.emptiable()) {
                throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
            }
        }
    }

    private static void checkRecurseLax(Vector dChildren, int min1, int max1, SubstitutionGroupHandler dSGHandler, Vector bChildren, int min2, int max2, SubstitutionGroupHandler bSGHandler) throws XMLSchemaException {
        if (!checkOccurrenceRange(min1, max1, min2, max2)) {
            Object[] objArr = new Object[4];
            objArr[0] = Integer.toString(min1);
            objArr[1] = max1 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max1);
            objArr[2] = Integer.toString(min2);
            objArr[3] = max2 == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max2);
            throw new XMLSchemaException("rcase-RecurseLax.1", objArr);
        }
        int count1 = dChildren.size();
        int count2 = bChildren.size();
        int current = 0;
        for (int i2 = 0; i2 < count1; i2++) {
            XSParticleDecl particle1 = (XSParticleDecl) dChildren.elementAt(i2);
            for (int j2 = current; j2 < count2; j2++) {
                XSParticleDecl particle2 = (XSParticleDecl) bChildren.elementAt(j2);
                current++;
                try {
                    if (particleValidRestriction(particle1, dSGHandler, particle2, bSGHandler)) {
                        current--;
                    }
                } catch (XMLSchemaException e2) {
                }
            }
            throw new XMLSchemaException("rcase-RecurseLax.2", null);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x00a6, code lost:
    
        r19 = r19 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void checkMapAndSum(java.util.Vector r9, int r10, int r11, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r12, java.util.Vector r13, int r14, int r15, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler r16) throws com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException {
        /*
            r0 = r10
            r1 = r11
            r2 = r14
            r3 = r15
            boolean r0 = checkOccurrenceRange(r0, r1, r2, r3)
            if (r0 != 0) goto L4d
            com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException r0 = new com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException
            r1 = r0
            java.lang.String r2 = "rcase-MapAndSum.2"
            r3 = 4
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r4 = r3
            r5 = 0
            r6 = r10
            java.lang.String r6 = java.lang.Integer.toString(r6)
            r4[r5] = r6
            r4 = r3
            r5 = 1
            r6 = r11
            r7 = -1
            if (r6 != r7) goto L29
            java.lang.String r6 = "unbounded"
            goto L2d
        L29:
            r6 = r11
            java.lang.String r6 = java.lang.Integer.toString(r6)
        L2d:
            r4[r5] = r6
            r4 = r3
            r5 = 2
            r6 = r14
            java.lang.String r6 = java.lang.Integer.toString(r6)
            r4[r5] = r6
            r4 = r3
            r5 = 3
            r6 = r15
            r7 = -1
            if (r6 != r7) goto L43
            java.lang.String r6 = "unbounded"
            goto L48
        L43:
            r6 = r15
            java.lang.String r6 = java.lang.Integer.toString(r6)
        L48:
            r4[r5] = r6
            r1.<init>(r2, r3)
            throw r0
        L4d:
            r0 = r9
            int r0 = r0.size()
            r17 = r0
            r0 = r13
            int r0 = r0.size()
            r18 = r0
            r0 = 0
            r19 = r0
        L5d:
            r0 = r19
            r1 = r17
            if (r0 >= r1) goto Lac
            r0 = r9
            r1 = r19
            java.lang.Object r0 = r0.elementAt(r1)
            com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl r0 = (com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl) r0
            r20 = r0
            r0 = 0
            r21 = r0
        L72:
            r0 = r21
            r1 = r18
            if (r0 >= r1) goto L9b
            r0 = r13
            r1 = r21
            java.lang.Object r0 = r0.elementAt(r1)
            com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl r0 = (com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl) r0
            r22 = r0
            r0 = r20
            r1 = r12
            r2 = r22
            r3 = r16
            boolean r0 = particleValidRestriction(r0, r1, r2, r3)     // Catch: com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException -> L93
            goto La6
        L93:
            r23 = move-exception
            int r21 = r21 + 1
            goto L72
        L9b:
            com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException r0 = new com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException
            r1 = r0
            java.lang.String r2 = "rcase-MapAndSum.1"
            r3 = 0
            r1.<init>(r2, r3)
            throw r0
        La6:
            int r19 = r19 + 1
            goto L5d
        Lac:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.XSConstraints.checkMapAndSum(java.util.Vector, int, int, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler, java.util.Vector, int, int, com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler):void");
    }

    public static boolean overlapUPA(XSElementDecl element1, XSElementDecl element2, SubstitutionGroupHandler sgHandler) {
        if (element1.fName == element2.fName && element1.fTargetNamespace == element2.fTargetNamespace) {
            return true;
        }
        XSElementDecl[] subGroup = sgHandler.getSubstitutionGroup(element1);
        for (int i2 = subGroup.length - 1; i2 >= 0; i2--) {
            if (subGroup[i2].fName == element2.fName && subGroup[i2].fTargetNamespace == element2.fTargetNamespace) {
                return true;
            }
        }
        XSElementDecl[] subGroup2 = sgHandler.getSubstitutionGroup(element2);
        for (int i3 = subGroup2.length - 1; i3 >= 0; i3--) {
            if (subGroup2[i3].fName == element1.fName && subGroup2[i3].fTargetNamespace == element1.fTargetNamespace) {
                return true;
            }
        }
        return false;
    }

    public static boolean overlapUPA(XSElementDecl element, XSWildcardDecl wildcard, SubstitutionGroupHandler sgHandler) {
        if (wildcard.allowNamespace(element.fTargetNamespace)) {
            return true;
        }
        XSElementDecl[] subGroup = sgHandler.getSubstitutionGroup(element);
        for (int i2 = subGroup.length - 1; i2 >= 0; i2--) {
            if (wildcard.allowNamespace(subGroup[i2].fTargetNamespace)) {
                return true;
            }
        }
        return false;
    }

    public static boolean overlapUPA(XSWildcardDecl wildcard1, XSWildcardDecl wildcard2) {
        XSWildcardDecl intersect = wildcard1.performIntersectionWith(wildcard2, wildcard1.fProcessContents);
        if (intersect == null || intersect.fType != 3 || intersect.fNamespaceList.length != 0) {
            return true;
        }
        return false;
    }

    public static boolean overlapUPA(Object decl1, Object decl2, SubstitutionGroupHandler sgHandler) {
        if (decl1 instanceof XSElementDecl) {
            if (decl2 instanceof XSElementDecl) {
                return overlapUPA((XSElementDecl) decl1, (XSElementDecl) decl2, sgHandler);
            }
            return overlapUPA((XSElementDecl) decl1, (XSWildcardDecl) decl2, sgHandler);
        }
        if (decl2 instanceof XSElementDecl) {
            return overlapUPA((XSElementDecl) decl2, (XSWildcardDecl) decl1, sgHandler);
        }
        return overlapUPA((XSWildcardDecl) decl1, (XSWildcardDecl) decl2);
    }
}
