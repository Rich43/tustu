package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDComplexTypeTraverser;
import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xs.XSObject;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.util.Locale;
import java.util.MissingResourceException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDElementTraverser.class */
class XSDElementTraverser extends XSDAbstractTraverser {
    protected final XSElementDecl fTempElementDecl;
    boolean fDeferTraversingLocalElements;

    XSDElementTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
        this.fTempElementDecl = new XSElementDecl();
    }

    XSParticleDecl traverseLocal(Element elmDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent) {
        XSParticleDecl particle;
        if (this.fSchemaHandler.fDeclPool != null) {
            particle = this.fSchemaHandler.fDeclPool.getParticleDecl();
        } else {
            particle = new XSParticleDecl();
        }
        if (this.fDeferTraversingLocalElements) {
            particle.fType = (short) 1;
            Attr attr = elmDecl.getAttributeNode(SchemaSymbols.ATT_MINOCCURS);
            if (attr != null) {
                String min = attr.getValue();
                try {
                    int m2 = Integer.parseInt(XMLChar.trim(min));
                    if (m2 >= 0) {
                        particle.fMinOccurs = m2;
                    }
                } catch (NumberFormatException e2) {
                }
            }
            this.fSchemaHandler.fillInLocalElemInfo(elmDecl, schemaDoc, allContextFlags, parent, particle);
        } else {
            traverseLocal(particle, elmDecl, schemaDoc, grammar, allContextFlags, parent, null);
            if (particle.fType == 0) {
                particle = null;
            }
        }
        return particle;
    }

    protected void traverseLocal(XSParticleDecl particle, Element elmDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar, int allContextFlags, XSObject parent, String[] localNSDecls) {
        XSElementDecl element;
        XSObjectList annotations;
        if (localNSDecls != null) {
            schemaDoc.fNamespaceSupport.setEffectiveContext(localNSDecls);
        }
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmDecl, false, schemaDoc);
        QName refAtt = (QName) attrValues[XSAttributeChecker.ATTIDX_REF];
        XInt minAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_MINOCCURS];
        XInt maxAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS];
        XSAnnotationImpl annotation = null;
        if (elmDecl.getAttributeNode(SchemaSymbols.ATT_REF) != null) {
            if (refAtt != null) {
                element = (XSElementDecl) this.fSchemaHandler.getGlobalDecl(schemaDoc, 3, refAtt, elmDecl);
                Element child = DOMUtil.getFirstChildElement(elmDecl);
                if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
                    child = DOMUtil.getNextSiblingElement(child);
                } else {
                    String text = DOMUtil.getSyntheticAnnotation(elmDecl);
                    if (text != null) {
                        annotation = traverseSyntheticAnnotation(elmDecl, text, attrValues, false, schemaDoc);
                    }
                }
                if (child != null) {
                    reportSchemaError("src-element.2.2", new Object[]{refAtt.rawname, DOMUtil.getLocalName(child)}, child);
                }
            } else {
                element = null;
            }
        } else {
            element = traverseNamedElement(elmDecl, attrValues, schemaDoc, grammar, false, parent);
        }
        particle.fMinOccurs = minAtt.intValue();
        particle.fMaxOccurs = maxAtt.intValue();
        if (element != null) {
            particle.fType = (short) 1;
            particle.fValue = element;
        } else {
            particle.fType = (short) 0;
        }
        if (refAtt != null) {
            if (annotation != null) {
                annotations = new XSObjectListImpl();
                ((XSObjectListImpl) annotations).addXSObject(annotation);
            } else {
                annotations = XSObjectListImpl.EMPTY_LIST;
            }
            particle.fAnnotations = annotations;
        } else {
            particle.fAnnotations = element != null ? element.fAnnotations : XSObjectListImpl.EMPTY_LIST;
        }
        Long defaultVals = (Long) attrValues[XSAttributeChecker.ATTIDX_FROMDEFAULT];
        checkOccurrences(particle, SchemaSymbols.ELT_ELEMENT, (Element) elmDecl.getParentNode(), allContextFlags, defaultVals.longValue());
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
    }

    XSElementDecl traverseGlobal(Element elmDecl, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmDecl, true, schemaDoc);
        XSElementDecl element = traverseNamedElement(elmDecl, attrValues, schemaDoc, grammar, true, null);
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return element;
    }

    XSElementDecl traverseNamedElement(Element elmDecl, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar, boolean isGlobal, XSObject parent) throws DOMException, MissingResourceException, XSDComplexTypeTraverser.ComplexTypeRecoverableError, XNIException {
        XSElementDecl element;
        XSObjectList annotations;
        String str;
        Boolean abstractAtt = (Boolean) attrValues[XSAttributeChecker.ATTIDX_ABSTRACT];
        XInt blockAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_BLOCK];
        String defaultAtt = (String) attrValues[XSAttributeChecker.ATTIDX_DEFAULT];
        XInt finalAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_FINAL];
        String fixedAtt = (String) attrValues[XSAttributeChecker.ATTIDX_FIXED];
        XInt formAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_FORM];
        String nameAtt = (String) attrValues[XSAttributeChecker.ATTIDX_NAME];
        Boolean nillableAtt = (Boolean) attrValues[XSAttributeChecker.ATTIDX_NILLABLE];
        QName subGroupAtt = (QName) attrValues[XSAttributeChecker.ATTIDX_SUBSGROUP];
        QName typeAtt = (QName) attrValues[XSAttributeChecker.ATTIDX_TYPE];
        if (this.fSchemaHandler.fDeclPool != null) {
            element = this.fSchemaHandler.fDeclPool.getElementDecl();
        } else {
            element = new XSElementDecl();
        }
        if (nameAtt != null) {
            element.fName = this.fSymbolTable.addSymbol(nameAtt);
        }
        if (isGlobal) {
            element.fTargetNamespace = schemaDoc.fTargetNamespace;
            element.setIsGlobal();
        } else {
            if (parent instanceof XSComplexTypeDecl) {
                element.setIsLocal((XSComplexTypeDecl) parent);
            }
            if (formAtt != null) {
                if (formAtt.intValue() == 1) {
                    element.fTargetNamespace = schemaDoc.fTargetNamespace;
                } else {
                    element.fTargetNamespace = null;
                }
            } else if (schemaDoc.fAreLocalElementsQualified) {
                element.fTargetNamespace = schemaDoc.fTargetNamespace;
            } else {
                element.fTargetNamespace = null;
            }
        }
        if (blockAtt == null) {
            element.fBlock = schemaDoc.fBlockDefault;
            if (element.fBlock != 31) {
                XSElementDecl xSElementDecl = element;
                xSElementDecl.fBlock = (short) (xSElementDecl.fBlock & 7);
            }
        } else {
            element.fBlock = blockAtt.shortValue();
            if (element.fBlock != 31 && (element.fBlock | 7) != 7) {
                reportSchemaError("s4s-att-invalid-value", new Object[]{element.fName, "block", "must be (#all | List of (extension | restriction | substitution))"}, elmDecl);
            }
        }
        element.fFinal = finalAtt == null ? schemaDoc.fFinalDefault : finalAtt.shortValue();
        XSElementDecl xSElementDecl2 = element;
        xSElementDecl2.fFinal = (short) (xSElementDecl2.fFinal & 3);
        if (nillableAtt.booleanValue()) {
            element.setIsNillable();
        }
        if (abstractAtt != null && abstractAtt.booleanValue()) {
            element.setIsAbstract();
        }
        if (fixedAtt != null) {
            element.fDefault = new ValidatedInfo();
            element.fDefault.normalizedValue = fixedAtt;
            element.setConstraintType((short) 2);
        } else if (defaultAtt != null) {
            element.fDefault = new ValidatedInfo();
            element.fDefault.normalizedValue = defaultAtt;
            element.setConstraintType((short) 1);
        } else {
            element.setConstraintType((short) 0);
        }
        if (subGroupAtt != null) {
            element.fSubGroup = (XSElementDecl) this.fSchemaHandler.getGlobalDecl(schemaDoc, 3, subGroupAtt, elmDecl);
        }
        Element child = DOMUtil.getFirstChildElement(elmDecl);
        XSAnnotationImpl annotation = null;
        if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
            annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
            child = DOMUtil.getNextSiblingElement(child);
        } else {
            String text = DOMUtil.getSyntheticAnnotation(elmDecl);
            if (text != null) {
                annotation = traverseSyntheticAnnotation(elmDecl, text, attrValues, false, schemaDoc);
            }
        }
        if (annotation != null) {
            annotations = new XSObjectListImpl();
            ((XSObjectListImpl) annotations).addXSObject(annotation);
        } else {
            annotations = XSObjectListImpl.EMPTY_LIST;
        }
        element.fAnnotations = annotations;
        XSTypeDefinition elementType = null;
        boolean haveAnonType = false;
        if (child != null) {
            String childName = DOMUtil.getLocalName(child);
            if (childName.equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                elementType = this.fSchemaHandler.fComplexTypeTraverser.traverseLocal(child, schemaDoc, grammar);
                haveAnonType = true;
                child = DOMUtil.getNextSiblingElement(child);
            } else if (childName.equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                elementType = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(child, schemaDoc, grammar);
                haveAnonType = true;
                child = DOMUtil.getNextSiblingElement(child);
            }
        }
        if (elementType == null && typeAtt != null) {
            elementType = (XSTypeDefinition) this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, typeAtt, elmDecl);
            if (elementType == null) {
                element.fUnresolvedTypeName = typeAtt;
            }
        }
        if (elementType == null && element.fSubGroup != null) {
            elementType = element.fSubGroup.fType;
        }
        if (elementType == null) {
            elementType = SchemaGrammar.fAnyType;
        }
        element.fType = elementType;
        if (child != null) {
            String childName2 = DOMUtil.getLocalName(child);
            while (child != null && (childName2.equals(SchemaSymbols.ELT_KEY) || childName2.equals(SchemaSymbols.ELT_KEYREF) || childName2.equals(SchemaSymbols.ELT_UNIQUE))) {
                if (childName2.equals(SchemaSymbols.ELT_KEY) || childName2.equals(SchemaSymbols.ELT_UNIQUE)) {
                    DOMUtil.setHidden(child, this.fSchemaHandler.fHiddenNodes);
                    this.fSchemaHandler.fUniqueOrKeyTraverser.traverse(child, element, schemaDoc, grammar);
                    if (DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME).length() != 0) {
                        XSDHandler xSDHandler = this.fSchemaHandler;
                        if (schemaDoc.fTargetNamespace == null) {
                            str = "," + DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME);
                        } else {
                            str = schemaDoc.fTargetNamespace + "," + DOMUtil.getAttrValue(child, SchemaSymbols.ATT_NAME);
                        }
                        XSDHandler xSDHandler2 = this.fSchemaHandler;
                        xSDHandler.checkForDuplicateNames(str, 1, this.fSchemaHandler.getIDRegistry(), this.fSchemaHandler.getIDRegistry_sub(), child, schemaDoc);
                    }
                } else if (childName2.equals(SchemaSymbols.ELT_KEYREF)) {
                    this.fSchemaHandler.storeKeyRef(child, schemaDoc, element);
                }
                child = DOMUtil.getNextSiblingElement(child);
                if (child != null) {
                    childName2 = DOMUtil.getLocalName(child);
                }
            }
        }
        if (nameAtt == null) {
            if (isGlobal) {
                reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_ELEMENT, SchemaSymbols.ATT_NAME}, elmDecl);
            } else {
                reportSchemaError("src-element.2.1", null, elmDecl);
            }
            nameAtt = "(no name)";
        }
        if (child != null) {
            reportSchemaError("s4s-elt-must-match.1", new Object[]{nameAtt, "(annotation?, (simpleType | complexType)?, (unique | key | keyref)*))", DOMUtil.getLocalName(child)}, child);
        }
        if (defaultAtt != null && fixedAtt != null) {
            reportSchemaError("src-element.1", new Object[]{nameAtt}, elmDecl);
        }
        if (haveAnonType && typeAtt != null) {
            reportSchemaError("src-element.3", new Object[]{nameAtt}, elmDecl);
        }
        checkNotationType(nameAtt, elementType, elmDecl);
        if (element.fDefault != null) {
            this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
            if (XSConstraints.ElementDefaultValidImmediate(element.fType, element.fDefault.normalizedValue, this.fValidationState, element.fDefault) == null) {
                reportSchemaError("e-props-correct.2", new Object[]{nameAtt, element.fDefault.normalizedValue}, elmDecl);
                element.fDefault = null;
                element.setConstraintType((short) 0);
            }
        }
        if (element.fSubGroup != null && !XSConstraints.checkTypeDerivationOk(element.fType, element.fSubGroup.fType, element.fSubGroup.fFinal)) {
            reportSchemaError("e-props-correct.4", new Object[]{nameAtt, subGroupAtt.prefix + CallSiteDescriptor.TOKEN_DELIMITER + subGroupAtt.localpart}, elmDecl);
            element.fSubGroup = null;
        }
        if (element.fDefault != null && ((elementType.getTypeCategory() == 16 && ((XSSimpleType) elementType).isIDType()) || (elementType.getTypeCategory() == 15 && ((XSComplexTypeDecl) elementType).containsTypeID()))) {
            reportSchemaError("e-props-correct.5", new Object[]{element.fName}, elmDecl);
            element.fDefault = null;
            element.setConstraintType((short) 0);
        }
        if (element.fName == null) {
            return null;
        }
        if (isGlobal) {
            grammar.addGlobalElementDeclAll(element);
            if (grammar.getGlobalElementDecl(element.fName) == null) {
                grammar.addGlobalElementDecl(element);
            }
            String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
            XSElementDecl element2 = grammar.getGlobalElementDecl(element.fName, loc);
            if (element2 == null) {
                grammar.addGlobalElementDecl(element, loc);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (element2 != null) {
                    element = element2;
                }
                this.fSchemaHandler.addGlobalElementDecl(element);
            }
        }
        return element;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser
    void reset(SymbolTable symbolTable, boolean validateAnnotations, Locale locale) {
        super.reset(symbolTable, validateAnnotations, locale);
        this.fDeferTraversingLocalElements = true;
    }
}
