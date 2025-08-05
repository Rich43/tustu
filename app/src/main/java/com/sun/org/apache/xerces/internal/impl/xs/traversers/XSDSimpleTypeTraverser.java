package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser;
import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.util.ArrayList;
import java.util.Vector;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDSimpleTypeTraverser.class */
class XSDSimpleTypeTraverser extends XSDAbstractTraverser {
    private boolean fIsBuiltIn;

    XSDSimpleTypeTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
        this.fIsBuiltIn = false;
    }

    XSSimpleType traverseGlobal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
        String nameAtt = (String) attrValues[XSAttributeChecker.ATTIDX_NAME];
        if (nameAtt == null) {
            attrValues[XSAttributeChecker.ATTIDX_NAME] = "(no name)";
        }
        XSSimpleType type = traverseSimpleTypeDecl(elmNode, attrValues, schemaDoc, grammar);
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        if (nameAtt == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, SchemaSymbols.ATT_NAME}, elmNode);
            type = null;
        }
        if (type != null) {
            if (grammar.getGlobalTypeDecl(type.getName()) == null) {
                grammar.addGlobalSimpleTypeDecl(type);
            }
            String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
            XSTypeDefinition type2 = grammar.getGlobalTypeDecl(type.getName(), loc);
            if (type2 == null) {
                grammar.addGlobalSimpleTypeDecl(type, loc);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (type2 != null && (type2 instanceof XSSimpleType)) {
                    type = (XSSimpleType) type2;
                }
                this.fSchemaHandler.addGlobalTypeDecl(type);
            }
        }
        return type;
    }

    XSSimpleType traverseLocal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
        String name = genAnonTypeName(elmNode);
        XSSimpleType type = getSimpleType(name, elmNode, attrValues, schemaDoc, grammar);
        if (type instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl) type).setAnonymous(true);
        }
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return type;
    }

    private XSSimpleType traverseSimpleTypeDecl(Element simpleTypeDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        String name = (String) attrValues[XSAttributeChecker.ATTIDX_NAME];
        return getSimpleType(name, simpleTypeDecl, attrValues, schemaDoc, grammar);
    }

    private String genAnonTypeName(Element simpleTypeDecl) {
        StringBuffer typeName = new StringBuffer("#AnonType_");
        Element parent = DOMUtil.getParent(simpleTypeDecl);
        while (true) {
            Element node = parent;
            if (node == null || node == DOMUtil.getRoot(DOMUtil.getDocument(node))) {
                break;
            }
            typeName.append(node.getAttribute(SchemaSymbols.ATT_NAME));
            parent = DOMUtil.getParent(node);
        }
        return typeName.toString();
    }

    private XSSimpleType getSimpleType(String name, Element simpleTypeDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        short refType;
        XInt finalAttr = (XInt) attrValues[XSAttributeChecker.ATTIDX_FINAL];
        int finalProperty = finalAttr == null ? schemaDoc.fFinalDefault : finalAttr.intValue();
        Element child = DOMUtil.getFirstChildElement(simpleTypeDecl);
        XSAnnotationImpl[] annotations = null;
        if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
            XSAnnotationImpl annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
            if (annotation != null) {
                annotations = new XSAnnotationImpl[]{annotation};
            }
            child = DOMUtil.getNextSiblingElement(child);
        } else {
            String text = DOMUtil.getSyntheticAnnotation(simpleTypeDecl);
            if (text != null) {
                annotations = new XSAnnotationImpl[]{traverseSyntheticAnnotation(simpleTypeDecl, text, attrValues, false, schemaDoc)};
            }
        }
        if (child == null) {
            reportSchemaError("s4s-elt-must-match.2", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))"}, simpleTypeDecl);
            return errorType(name, schemaDoc.fTargetNamespace, (short) 2);
        }
        String varietyProperty = DOMUtil.getLocalName(child);
        boolean restriction = false;
        boolean list = false;
        boolean union = false;
        if (varietyProperty.equals(SchemaSymbols.ELT_RESTRICTION)) {
            refType = 2;
            restriction = true;
        } else if (varietyProperty.equals(SchemaSymbols.ELT_LIST)) {
            refType = 16;
            list = true;
        } else if (varietyProperty.equals(SchemaSymbols.ELT_UNION)) {
            refType = 8;
            union = true;
        } else {
            reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", varietyProperty}, simpleTypeDecl);
            return errorType(name, schemaDoc.fTargetNamespace, (short) 2);
        }
        Element nextChild = DOMUtil.getNextSiblingElement(child);
        if (nextChild != null) {
            reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", DOMUtil.getLocalName(nextChild)}, nextChild);
        }
        Object[] contentAttrs = this.fAttrChecker.checkAttributes(child, false, schemaDoc);
        QName baseTypeName = (QName) contentAttrs[restriction ? XSAttributeChecker.ATTIDX_BASE : XSAttributeChecker.ATTIDX_ITEMTYPE];
        Vector memberTypes = (Vector) contentAttrs[XSAttributeChecker.ATTIDX_MEMBERTYPES];
        Element content = DOMUtil.getFirstChildElement(child);
        if (content != null && DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_ANNOTATION)) {
            XSAnnotationImpl annotation2 = traverseAnnotationDecl(content, contentAttrs, false, schemaDoc);
            if (annotation2 != null) {
                if (annotations == null) {
                    annotations = new XSAnnotationImpl[]{annotation2};
                } else {
                    XSAnnotationImpl[] tempArray = new XSAnnotationImpl[2];
                    tempArray[0] = annotations[0];
                    annotations = tempArray;
                    annotations[1] = annotation2;
                }
            }
            content = DOMUtil.getNextSiblingElement(content);
        } else {
            String text2 = DOMUtil.getSyntheticAnnotation(child);
            if (text2 != null) {
                XSAnnotationImpl annotation3 = traverseSyntheticAnnotation(child, text2, contentAttrs, false, schemaDoc);
                if (annotations == null) {
                    annotations = new XSAnnotationImpl[]{annotation3};
                } else {
                    XSAnnotationImpl[] tempArray2 = new XSAnnotationImpl[2];
                    tempArray2[0] = annotations[0];
                    annotations = tempArray2;
                    annotations[1] = annotation3;
                }
            }
        }
        XSSimpleType baseValidator = null;
        if ((restriction || list) && baseTypeName != null) {
            baseValidator = findDTValidator(child, name, baseTypeName, refType, schemaDoc);
            if (baseValidator == null && this.fIsBuiltIn) {
                this.fIsBuiltIn = false;
                return null;
            }
        }
        ArrayList dTValidators = null;
        if (union && memberTypes != null && memberTypes.size() > 0) {
            int size = memberTypes.size();
            dTValidators = new ArrayList(size);
            for (int i2 = 0; i2 < size; i2++) {
                XSSimpleType dv = findDTValidator(child, name, (QName) memberTypes.elementAt(i2), (short) 8, schemaDoc);
                if (dv != null) {
                    if (dv.getVariety() == 3) {
                        XSObjectList dvs = dv.getMemberTypes();
                        for (int j2 = 0; j2 < dvs.getLength(); j2++) {
                            dTValidators.add(dvs.item(j2));
                        }
                    } else {
                        dTValidators.add(dv);
                    }
                }
            }
        }
        if (content != null && DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            if (restriction || list) {
                if (baseTypeName != null) {
                    reportSchemaError(list ? "src-simple-type.3.a" : "src-simple-type.2.a", null, content);
                }
                if (baseValidator == null) {
                    baseValidator = traverseLocal(content, schemaDoc, grammar);
                }
                content = DOMUtil.getNextSiblingElement(content);
            } else if (union) {
                if (dTValidators == null) {
                    dTValidators = new ArrayList(2);
                }
                do {
                    XSSimpleType dv2 = traverseLocal(content, schemaDoc, grammar);
                    if (dv2 != null) {
                        if (dv2.getVariety() == 3) {
                            XSObjectList dvs2 = dv2.getMemberTypes();
                            for (int j3 = 0; j3 < dvs2.getLength(); j3++) {
                                dTValidators.add(dvs2.item(j3));
                            }
                        } else {
                            dTValidators.add(dv2);
                        }
                    }
                    content = DOMUtil.getNextSiblingElement(content);
                    if (content == null) {
                        break;
                    }
                } while (DOMUtil.getLocalName(content).equals(SchemaSymbols.ELT_SIMPLETYPE));
            }
        } else if ((restriction || list) && baseTypeName == null) {
            reportSchemaError(list ? "src-simple-type.3.b" : "src-simple-type.2.b", null, child);
        } else if (union && (memberTypes == null || memberTypes.size() == 0)) {
            reportSchemaError("src-union-memberTypes-or-simpleTypes", null, child);
        }
        if ((restriction || list) && baseValidator == null) {
            this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
            return errorType(name, schemaDoc.fTargetNamespace, restriction ? (short) 2 : (short) 16);
        }
        if (union && (dTValidators == null || dTValidators.size() == 0)) {
            this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
            return errorType(name, schemaDoc.fTargetNamespace, (short) 8);
        }
        if (list && isListDatatype(baseValidator)) {
            reportSchemaError("cos-st-restricts.2.1", new Object[]{name, baseValidator.getName()}, child);
            this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
            return errorType(name, schemaDoc.fTargetNamespace, (short) 16);
        }
        XSSimpleType newDecl = null;
        if (restriction) {
            newDecl = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short) finalProperty, baseValidator, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
        } else if (list) {
            newDecl = this.fSchemaHandler.fDVFactory.createTypeList(name, schemaDoc.fTargetNamespace, (short) finalProperty, baseValidator, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
        } else if (union) {
            XSSimpleType[] memberDecls = (XSSimpleType[]) dTValidators.toArray(new XSSimpleType[dTValidators.size()]);
            newDecl = this.fSchemaHandler.fDVFactory.createTypeUnion(name, schemaDoc.fTargetNamespace, (short) finalProperty, memberDecls, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
        }
        if (restriction && content != null) {
            XSDAbstractTraverser.FacetInfo fi = traverseFacets(content, baseValidator, schemaDoc);
            content = fi.nodeAfterFacets;
            try {
                this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
                newDecl.applyFacets(fi.facetdata, fi.fPresentFacets, fi.fFixedFacets, this.fValidationState);
            } catch (InvalidDatatypeFacetException ex) {
                reportSchemaError(ex.getKey(), ex.getArgs(), child);
                newDecl = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short) finalProperty, baseValidator, annotations == null ? null : new XSObjectListImpl(annotations, annotations.length));
            }
        }
        if (content != null) {
            if (restriction) {
                reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_RESTRICTION, "(annotation?, (simpleType?, (minExclusive | minInclusive | maxExclusive | maxInclusive | totalDigits | fractionDigits | length | minLength | maxLength | enumeration | whiteSpace | pattern)*))", DOMUtil.getLocalName(content)}, content);
            } else if (list) {
                reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_LIST, "(annotation?, (simpleType?))", DOMUtil.getLocalName(content)}, content);
            } else if (union) {
                reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_UNION, "(annotation?, (simpleType*))", DOMUtil.getLocalName(content)}, content);
            }
        }
        this.fAttrChecker.returnAttrArray(contentAttrs, schemaDoc);
        return newDecl;
    }

    private XSSimpleType findDTValidator(Element elm, String refName, QName baseTypeStr, short baseRefContext, XSDocumentInfo schemaDoc) {
        XSTypeDefinition baseType;
        if (baseTypeStr == null || (baseType = (XSTypeDefinition) this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, baseTypeStr, elm)) == null) {
            return null;
        }
        if (baseType.getTypeCategory() != 16) {
            reportSchemaError("cos-st-restricts.1.1", new Object[]{baseTypeStr.rawname, refName}, elm);
            return null;
        }
        if (baseType == SchemaGrammar.fAnySimpleType && baseRefContext == 2) {
            if (checkBuiltIn(refName, schemaDoc.fTargetNamespace)) {
                return null;
            }
            reportSchemaError("cos-st-restricts.1.1", new Object[]{baseTypeStr.rawname, refName}, elm);
            return null;
        }
        if ((baseType.getFinal() & baseRefContext) != 0) {
            if (baseRefContext == 2) {
                reportSchemaError("st-props-correct.3", new Object[]{refName, baseTypeStr.rawname}, elm);
                return null;
            }
            if (baseRefContext == 16) {
                reportSchemaError("cos-st-restricts.2.3.1.1", new Object[]{baseTypeStr.rawname, refName}, elm);
                return null;
            }
            if (baseRefContext == 8) {
                reportSchemaError("cos-st-restricts.3.3.1.1", new Object[]{baseTypeStr.rawname, refName}, elm);
                return null;
            }
            return null;
        }
        return (XSSimpleType) baseType;
    }

    private final boolean checkBuiltIn(String name, String namespace) {
        if (namespace != SchemaSymbols.URI_SCHEMAFORSCHEMA) {
            return false;
        }
        if (SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(name) != null) {
            this.fIsBuiltIn = true;
        }
        return this.fIsBuiltIn;
    }

    private boolean isListDatatype(XSSimpleType validator) {
        if (validator.getVariety() == 2) {
            return true;
        }
        if (validator.getVariety() == 3) {
            XSObjectList temp = validator.getMemberTypes();
            for (int i2 = 0; i2 < temp.getLength(); i2++) {
                if (((XSSimpleType) temp.item(i2)).getVariety() == 2) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private XSSimpleType errorType(String name, String namespace, short refType) {
        XSSimpleType stringType = (XSSimpleType) SchemaGrammar.SG_SchemaNS.getTypeDefinition("string");
        switch (refType) {
            case 2:
                return this.fSchemaHandler.fDVFactory.createTypeRestriction(name, namespace, (short) 0, stringType, null);
            case 8:
                return this.fSchemaHandler.fDVFactory.createTypeUnion(name, namespace, (short) 0, new XSSimpleType[]{stringType}, null);
            case 16:
                return this.fSchemaHandler.fDVFactory.createTypeList(name, namespace, (short) 0, stringType, null);
            default:
                return null;
        }
    }
}
