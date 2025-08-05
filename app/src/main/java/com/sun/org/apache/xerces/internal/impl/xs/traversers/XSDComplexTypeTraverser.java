package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
import com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
import com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser;
import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDComplexTypeTraverser.class */
class XSDComplexTypeTraverser extends XSDAbstractParticleTraverser {
    private static final int GLOBAL_NUM = 11;
    private static XSParticleDecl fErrorContent = null;
    private static XSWildcardDecl fErrorWildcard = null;
    private String fName;
    private String fTargetNamespace;
    private short fDerivedBy;
    private short fFinal;
    private short fBlock;
    private short fContentType;
    private XSTypeDefinition fBaseType;
    private XSAttributeGroupDecl fAttrGrp;
    private XSSimpleType fXSSimpleType;
    private XSParticleDecl fParticle;
    private boolean fIsAbstract;
    private XSComplexTypeDecl fComplexTypeDecl;
    private XSAnnotationImpl[] fAnnotations;
    private Object[] fGlobalStore;
    private int fGlobalStorePos;
    private static final boolean DEBUG = false;

    private static XSParticleDecl getErrorContent() {
        if (fErrorContent == null) {
            XSParticleDecl particle = new XSParticleDecl();
            particle.fType = (short) 2;
            particle.fValue = getErrorWildcard();
            particle.fMinOccurs = 0;
            particle.fMaxOccurs = -1;
            XSModelGroupImpl group = new XSModelGroupImpl();
            group.fCompositor = (short) 102;
            group.fParticleCount = 1;
            group.fParticles = new XSParticleDecl[1];
            group.fParticles[0] = particle;
            XSParticleDecl errorContent = new XSParticleDecl();
            errorContent.fType = (short) 3;
            errorContent.fValue = group;
            fErrorContent = errorContent;
        }
        return fErrorContent;
    }

    private static XSWildcardDecl getErrorWildcard() {
        if (fErrorWildcard == null) {
            XSWildcardDecl wildcard = new XSWildcardDecl();
            wildcard.fProcessContents = (short) 2;
            fErrorWildcard = wildcard;
        }
        return fErrorWildcard;
    }

    XSDComplexTypeTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
        this.fName = null;
        this.fTargetNamespace = null;
        this.fDerivedBy = (short) 2;
        this.fFinal = (short) 0;
        this.fBlock = (short) 0;
        this.fContentType = (short) 0;
        this.fBaseType = null;
        this.fAttrGrp = null;
        this.fXSSimpleType = null;
        this.fParticle = null;
        this.fIsAbstract = false;
        this.fComplexTypeDecl = null;
        this.fAnnotations = null;
        this.fGlobalStore = null;
        this.fGlobalStorePos = 0;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDComplexTypeTraverser$ComplexTypeRecoverableError.class */
    private static final class ComplexTypeRecoverableError extends Exception {
        private static final long serialVersionUID = 6802729912091130335L;
        Object[] errorSubstText;
        Element errorElem;

        ComplexTypeRecoverableError() {
            this.errorSubstText = null;
            this.errorElem = null;
        }

        ComplexTypeRecoverableError(String msgKey, Object[] args, Element e2) {
            super(msgKey);
            this.errorSubstText = null;
            this.errorElem = null;
            this.errorSubstText = args;
            this.errorElem = e2;
        }
    }

    XSComplexTypeDecl traverseLocal(Element complexTypeNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) throws ComplexTypeRecoverableError {
        Object[] attrValues = this.fAttrChecker.checkAttributes(complexTypeNode, false, schemaDoc);
        String complexTypeName = genAnonTypeName(complexTypeNode);
        contentBackup();
        XSComplexTypeDecl type = traverseComplexTypeDecl(complexTypeNode, complexTypeName, attrValues, schemaDoc, grammar);
        contentRestore();
        grammar.addComplexTypeDecl(type, this.fSchemaHandler.element2Locator(complexTypeNode));
        type.setIsAnonymous();
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return type;
    }

    XSComplexTypeDecl traverseGlobal(Element complexTypeNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(complexTypeNode, true, schemaDoc);
        String complexTypeName = (String) attrValues[XSAttributeChecker.ATTIDX_NAME];
        contentBackup();
        XSComplexTypeDecl type = traverseComplexTypeDecl(complexTypeNode, complexTypeName, attrValues, schemaDoc, grammar);
        contentRestore();
        grammar.addComplexTypeDecl(type, this.fSchemaHandler.element2Locator(complexTypeNode));
        if (complexTypeName == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_COMPLEXTYPE, SchemaSymbols.ATT_NAME}, complexTypeNode);
            type = null;
        } else {
            if (grammar.getGlobalTypeDecl(type.getName()) == null) {
                grammar.addGlobalComplexTypeDecl(type);
            }
            String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
            XSTypeDefinition type2 = grammar.getGlobalTypeDecl(type.getName(), loc);
            if (type2 == null) {
                grammar.addGlobalComplexTypeDecl(type, loc);
            }
            if (this.fSchemaHandler.fTolerateDuplicates) {
                if (type2 != null && (type2 instanceof XSComplexTypeDecl)) {
                    type = (XSComplexTypeDecl) type2;
                }
                this.fSchemaHandler.addGlobalTypeDecl(type);
            }
        }
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return type;
    }

    private XSComplexTypeDecl traverseComplexTypeDecl(Element complexTypeDecl, String complexTypeName, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar) throws ComplexTypeRecoverableError {
        this.fComplexTypeDecl = new XSComplexTypeDecl();
        this.fAttrGrp = new XSAttributeGroupDecl();
        Boolean abstractAtt = (Boolean) attrValues[XSAttributeChecker.ATTIDX_ABSTRACT];
        XInt blockAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_BLOCK];
        Boolean mixedAtt = (Boolean) attrValues[XSAttributeChecker.ATTIDX_MIXED];
        XInt finalAtt = (XInt) attrValues[XSAttributeChecker.ATTIDX_FINAL];
        this.fName = complexTypeName;
        this.fComplexTypeDecl.setName(this.fName);
        this.fTargetNamespace = schemaDoc.fTargetNamespace;
        this.fBlock = blockAtt == null ? schemaDoc.fBlockDefault : blockAtt.shortValue();
        this.fFinal = finalAtt == null ? schemaDoc.fFinalDefault : finalAtt.shortValue();
        this.fBlock = (short) (this.fBlock & 3);
        this.fFinal = (short) (this.fFinal & 3);
        this.fIsAbstract = abstractAtt != null && abstractAtt.booleanValue();
        this.fAnnotations = null;
        try {
            Element child = DOMUtil.getFirstChildElement(complexTypeDecl);
            if (child != null) {
                if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    addAnnotation(traverseAnnotationDecl(child, attrValues, false, schemaDoc));
                    child = DOMUtil.getNextSiblingElement(child);
                } else {
                    String text = DOMUtil.getSyntheticAnnotation(complexTypeDecl);
                    if (text != null) {
                        addAnnotation(traverseSyntheticAnnotation(complexTypeDecl, text, attrValues, false, schemaDoc));
                    }
                }
                if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, child);
                }
            } else {
                String text2 = DOMUtil.getSyntheticAnnotation(complexTypeDecl);
                if (text2 != null) {
                    addAnnotation(traverseSyntheticAnnotation(complexTypeDecl, text2, attrValues, false, schemaDoc));
                }
            }
            if (child == null) {
                this.fBaseType = SchemaGrammar.fAnyType;
                this.fDerivedBy = (short) 2;
                processComplexContent(child, mixedAtt.booleanValue(), false, schemaDoc, grammar);
            } else if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_SIMPLECONTENT)) {
                traverseSimpleContent(child, schemaDoc, grammar);
                Element elemTmp = DOMUtil.getNextSiblingElement(child);
                if (elemTmp != null) {
                    String siblingName = DOMUtil.getLocalName(elemTmp);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, siblingName}, elemTmp);
                }
            } else if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_COMPLEXCONTENT)) {
                traverseComplexContent(child, mixedAtt.booleanValue(), schemaDoc, grammar);
                Element elemTmp2 = DOMUtil.getNextSiblingElement(child);
                if (elemTmp2 != null) {
                    String siblingName2 = DOMUtil.getLocalName(elemTmp2);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, siblingName2}, elemTmp2);
                }
            } else {
                this.fBaseType = SchemaGrammar.fAnyType;
                this.fDerivedBy = (short) 2;
                processComplexContent(child, mixedAtt.booleanValue(), false, schemaDoc, grammar);
            }
        } catch (ComplexTypeRecoverableError e2) {
            handleComplexTypeError(e2.getMessage(), e2.errorSubstText, e2.errorElem);
        }
        this.fComplexTypeDecl.setValues(this.fName, this.fTargetNamespace, this.fBaseType, this.fDerivedBy, this.fFinal, this.fBlock, this.fContentType, this.fIsAbstract, this.fAttrGrp, this.fXSSimpleType, this.fParticle, new XSObjectListImpl(this.fAnnotations, this.fAnnotations == null ? 0 : this.fAnnotations.length));
        return this.fComplexTypeDecl;
    }

    private void traverseSimpleContent(Element simpleContentElement, XSDocumentInfo schemaDoc, SchemaGrammar grammar) throws ComplexTypeRecoverableError {
        int baseFinalSet;
        Object[] simpleContentAttrValues = this.fAttrChecker.checkAttributes(simpleContentElement, false, schemaDoc);
        this.fContentType = (short) 1;
        this.fParticle = null;
        Element simpleContent = DOMUtil.getFirstChildElement(simpleContentElement);
        if (simpleContent != null && DOMUtil.getLocalName(simpleContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
            addAnnotation(traverseAnnotationDecl(simpleContent, simpleContentAttrValues, false, schemaDoc));
            simpleContent = DOMUtil.getNextSiblingElement(simpleContent);
        } else {
            String text = DOMUtil.getSyntheticAnnotation(simpleContentElement);
            if (text != null) {
                addAnnotation(traverseSyntheticAnnotation(simpleContentElement, text, simpleContentAttrValues, false, schemaDoc));
            }
        }
        if (simpleContent == null) {
            this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[]{this.fName, SchemaSymbols.ELT_SIMPLECONTENT}, simpleContentElement);
        }
        String simpleContentName = DOMUtil.getLocalName(simpleContent);
        if (simpleContentName.equals(SchemaSymbols.ELT_RESTRICTION)) {
            this.fDerivedBy = (short) 2;
        } else if (simpleContentName.equals(SchemaSymbols.ELT_EXTENSION)) {
            this.fDerivedBy = (short) 1;
        } else {
            this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, simpleContentName}, simpleContent);
        }
        Element elemTmp = DOMUtil.getNextSiblingElement(simpleContent);
        if (elemTmp != null) {
            this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
            String siblingName = DOMUtil.getLocalName(elemTmp);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, siblingName}, elemTmp);
        }
        Object[] derivationTypeAttrValues = this.fAttrChecker.checkAttributes(simpleContent, false, schemaDoc);
        QName baseTypeName = (QName) derivationTypeAttrValues[XSAttributeChecker.ATTIDX_BASE];
        if (baseTypeName == null) {
            this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[]{simpleContentName, "base"}, simpleContent);
        }
        XSTypeDefinition type = (XSTypeDefinition) this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, baseTypeName, simpleContent);
        if (type == null) {
            this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError();
        }
        this.fBaseType = type;
        XSSimpleType baseValidator = null;
        XSComplexTypeDecl baseComplexType = null;
        if (type.getTypeCategory() == 15) {
            baseComplexType = (XSComplexTypeDecl) type;
            baseFinalSet = baseComplexType.getFinal();
            if (baseComplexType.getContentType() == 1) {
                baseValidator = (XSSimpleType) baseComplexType.getSimpleType();
            } else if (this.fDerivedBy != 2 || baseComplexType.getContentType() != 3 || !((XSParticleDecl) baseComplexType.getParticle()).emptiable()) {
                this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[]{this.fName, baseComplexType.getName()}, simpleContent);
            }
        } else {
            baseValidator = (XSSimpleType) type;
            if (this.fDerivedBy == 2) {
                this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[]{this.fName, baseValidator.getName()}, simpleContent);
            }
            baseFinalSet = baseValidator.getFinal();
        }
        if ((baseFinalSet & this.fDerivedBy) != 0) {
            this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            String errorKey = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
            throw new ComplexTypeRecoverableError(errorKey, new Object[]{this.fName, this.fBaseType.getName()}, simpleContent);
        }
        Element scElement = simpleContent;
        Element simpleContent2 = DOMUtil.getFirstChildElement(simpleContent);
        if (simpleContent2 != null) {
            if (DOMUtil.getLocalName(simpleContent2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                addAnnotation(traverseAnnotationDecl(simpleContent2, derivationTypeAttrValues, false, schemaDoc));
                simpleContent2 = DOMUtil.getNextSiblingElement(simpleContent2);
            } else {
                String text2 = DOMUtil.getSyntheticAnnotation(scElement);
                if (text2 != null) {
                    addAnnotation(traverseSyntheticAnnotation(scElement, text2, derivationTypeAttrValues, false, schemaDoc));
                }
            }
            if (simpleContent2 != null && DOMUtil.getLocalName(simpleContent2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, simpleContent2);
            }
        } else {
            String text3 = DOMUtil.getSyntheticAnnotation(scElement);
            if (text3 != null) {
                addAnnotation(traverseSyntheticAnnotation(scElement, text3, derivationTypeAttrValues, false, schemaDoc));
            }
        }
        if (this.fDerivedBy == 2) {
            if (simpleContent2 != null && DOMUtil.getLocalName(simpleContent2).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                XSSimpleType dv = this.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(simpleContent2, schemaDoc, grammar);
                if (dv == null) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError();
                }
                if (baseValidator != null && !XSConstraints.checkSimpleDerivationOk(dv, baseValidator, baseValidator.getFinal())) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.2.2.1", new Object[]{this.fName, dv.getName(), baseValidator.getName()}, simpleContent2);
                }
                baseValidator = dv;
                simpleContent2 = DOMUtil.getNextSiblingElement(simpleContent2);
            }
            if (baseValidator == null) {
                this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                throw new ComplexTypeRecoverableError("src-ct.2.2", new Object[]{this.fName}, simpleContent2);
            }
            Element attrNode = null;
            XSFacets facetData = null;
            short presentFacets = 0;
            short fixedFacets = 0;
            if (simpleContent2 != null) {
                XSDAbstractTraverser.FacetInfo fi = traverseFacets(simpleContent2, baseValidator, schemaDoc);
                attrNode = fi.nodeAfterFacets;
                facetData = fi.facetdata;
                presentFacets = fi.fPresentFacets;
                fixedFacets = fi.fFixedFacets;
            }
            String name = genAnonTypeName(simpleContentElement);
            this.fXSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short) 0, baseValidator, null);
            try {
                this.fValidationState.setNamespaceSupport(schemaDoc.fNamespaceSupport);
                this.fXSSimpleType.applyFacets(facetData, presentFacets, fixedFacets, this.fValidationState);
            } catch (InvalidDatatypeFacetException ex) {
                reportSchemaError(ex.getKey(), ex.getArgs(), simpleContent2);
                this.fXSSimpleType = this.fSchemaHandler.fDVFactory.createTypeRestriction(name, schemaDoc.fTargetNamespace, (short) 0, baseValidator, null);
            }
            if (this.fXSSimpleType instanceof XSSimpleTypeDecl) {
                ((XSSimpleTypeDecl) this.fXSSimpleType).setAnonymous(true);
            }
            if (attrNode != null) {
                if (!isAttrOrAttrGroup(attrNode)) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(attrNode)}, attrNode);
                }
                Element node = traverseAttrsAndAttrGrps(attrNode, this.fAttrGrp, schemaDoc, grammar, this.fComplexTypeDecl);
                if (node != null) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(node)}, node);
                }
            }
            try {
                mergeAttributes(baseComplexType.getAttrGrp(), this.fAttrGrp, this.fName, false, simpleContentElement);
                this.fAttrGrp.removeProhibitedAttrs();
                Object[] errArgs = this.fAttrGrp.validRestrictionOf(this.fName, baseComplexType.getAttrGrp());
                if (errArgs != null) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError((String) errArgs[errArgs.length - 1], errArgs, attrNode);
                }
            } catch (ComplexTypeRecoverableError e2) {
                this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                throw e2;
            }
        } else {
            this.fXSSimpleType = baseValidator;
            if (simpleContent2 != null) {
                Element attrNode2 = simpleContent2;
                if (!isAttrOrAttrGroup(attrNode2)) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(attrNode2)}, attrNode2);
                }
                Element node2 = traverseAttrsAndAttrGrps(attrNode2, this.fAttrGrp, schemaDoc, grammar, this.fComplexTypeDecl);
                if (node2 != null) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(node2)}, node2);
                }
                this.fAttrGrp.removeProhibitedAttrs();
            }
            if (baseComplexType != null) {
                try {
                    mergeAttributes(baseComplexType.getAttrGrp(), this.fAttrGrp, this.fName, true, simpleContentElement);
                } catch (ComplexTypeRecoverableError e3) {
                    this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw e3;
                }
            }
        }
        this.fAttrChecker.returnAttrArray(simpleContentAttrValues, schemaDoc);
        this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
    }

    private void traverseComplexContent(Element complexContentElement, boolean mixedOnType, XSDocumentInfo schemaDoc, SchemaGrammar grammar) throws ComplexTypeRecoverableError {
        Object[] errArgs;
        Object[] complexContentAttrValues = this.fAttrChecker.checkAttributes(complexContentElement, false, schemaDoc);
        boolean mixedContent = mixedOnType;
        Boolean mixedAtt = (Boolean) complexContentAttrValues[XSAttributeChecker.ATTIDX_MIXED];
        if (mixedAtt != null) {
            mixedContent = mixedAtt.booleanValue();
        }
        this.fXSSimpleType = null;
        Element complexContent = DOMUtil.getFirstChildElement(complexContentElement);
        if (complexContent != null && DOMUtil.getLocalName(complexContent).equals(SchemaSymbols.ELT_ANNOTATION)) {
            addAnnotation(traverseAnnotationDecl(complexContent, complexContentAttrValues, false, schemaDoc));
            complexContent = DOMUtil.getNextSiblingElement(complexContent);
        } else {
            String text = DOMUtil.getSyntheticAnnotation(complexContentElement);
            if (text != null) {
                addAnnotation(traverseSyntheticAnnotation(complexContentElement, text, complexContentAttrValues, false, schemaDoc));
            }
        }
        if (complexContent == null) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[]{this.fName, SchemaSymbols.ELT_COMPLEXCONTENT}, complexContentElement);
        }
        String complexContentName = DOMUtil.getLocalName(complexContent);
        if (complexContentName.equals(SchemaSymbols.ELT_RESTRICTION)) {
            this.fDerivedBy = (short) 2;
        } else if (complexContentName.equals(SchemaSymbols.ELT_EXTENSION)) {
            this.fDerivedBy = (short) 1;
        } else {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, complexContentName}, complexContent);
        }
        Element elemTmp = DOMUtil.getNextSiblingElement(complexContent);
        if (elemTmp != null) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            String siblingName = DOMUtil.getLocalName(elemTmp);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, siblingName}, elemTmp);
        }
        Object[] derivationTypeAttrValues = this.fAttrChecker.checkAttributes(complexContent, false, schemaDoc);
        QName baseTypeName = (QName) derivationTypeAttrValues[XSAttributeChecker.ATTIDX_BASE];
        if (baseTypeName == null) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[]{complexContentName, "base"}, complexContent);
        }
        XSTypeDefinition type = (XSTypeDefinition) this.fSchemaHandler.getGlobalDecl(schemaDoc, 7, baseTypeName, complexContent);
        if (type == null) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError();
        }
        if (!(type instanceof XSComplexTypeDecl)) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            throw new ComplexTypeRecoverableError("src-ct.1", new Object[]{this.fName, type.getName()}, complexContent);
        }
        XSComplexTypeDecl baseType = (XSComplexTypeDecl) type;
        this.fBaseType = baseType;
        if ((baseType.getFinal() & this.fDerivedBy) != 0) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            String errorKey = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
            throw new ComplexTypeRecoverableError(errorKey, new Object[]{this.fName, this.fBaseType.getName()}, complexContent);
        }
        Element complexContent2 = DOMUtil.getFirstChildElement(complexContent);
        if (complexContent2 != null) {
            if (DOMUtil.getLocalName(complexContent2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                addAnnotation(traverseAnnotationDecl(complexContent2, derivationTypeAttrValues, false, schemaDoc));
                complexContent2 = DOMUtil.getNextSiblingElement(complexContent2);
            } else {
                String text2 = DOMUtil.getSyntheticAnnotation(complexContent2);
                if (text2 != null) {
                    addAnnotation(traverseSyntheticAnnotation(complexContent2, text2, derivationTypeAttrValues, false, schemaDoc));
                }
            }
            if (complexContent2 != null && DOMUtil.getLocalName(complexContent2).equals(SchemaSymbols.ELT_ANNOTATION)) {
                this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, complexContent2);
            }
        } else {
            String text3 = DOMUtil.getSyntheticAnnotation(complexContent2);
            if (text3 != null) {
                addAnnotation(traverseSyntheticAnnotation(complexContent2, text3, derivationTypeAttrValues, false, schemaDoc));
            }
        }
        try {
            processComplexContent(complexContent2, mixedContent, true, schemaDoc, grammar);
            XSParticleDecl baseContent = (XSParticleDecl) baseType.getParticle();
            if (this.fDerivedBy == 2) {
                if (this.fContentType == 3 && baseType.getContentType() != 3) {
                    this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.4.1.2", new Object[]{this.fName, baseType.getName()}, complexContent2);
                }
                try {
                    mergeAttributes(baseType.getAttrGrp(), this.fAttrGrp, this.fName, false, complexContent2);
                    this.fAttrGrp.removeProhibitedAttrs();
                    if (baseType != SchemaGrammar.fAnyType && (errArgs = this.fAttrGrp.validRestrictionOf(this.fName, baseType.getAttrGrp())) != null) {
                        this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                        this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                        throw new ComplexTypeRecoverableError((String) errArgs[errArgs.length - 1], errArgs, complexContent2);
                    }
                } catch (ComplexTypeRecoverableError e2) {
                    this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw e2;
                }
            } else {
                if (this.fParticle == null) {
                    this.fContentType = baseType.getContentType();
                    this.fXSSimpleType = (XSSimpleType) baseType.getSimpleType();
                    this.fParticle = baseContent;
                } else if (baseType.getContentType() != 0) {
                    if (this.fContentType == 2 && baseType.getContentType() != 2) {
                        this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                        this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                        throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.a", new Object[]{this.fName}, complexContent2);
                    }
                    if (this.fContentType == 3 && baseType.getContentType() != 3) {
                        this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                        this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                        throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.b", new Object[]{this.fName}, complexContent2);
                    }
                    if ((this.fParticle.fType == 3 && ((XSModelGroupImpl) this.fParticle.fValue).fCompositor == 103) || (((XSParticleDecl) baseType.getParticle()).fType == 3 && ((XSModelGroupImpl) ((XSParticleDecl) baseType.getParticle()).fValue).fCompositor == 103)) {
                        this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                        this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                        throw new ComplexTypeRecoverableError("cos-all-limited.1.2", new Object[0], complexContent2);
                    }
                    XSModelGroupImpl group = new XSModelGroupImpl();
                    group.fCompositor = (short) 102;
                    group.fParticleCount = 2;
                    group.fParticles = new XSParticleDecl[2];
                    group.fParticles[0] = (XSParticleDecl) baseType.getParticle();
                    group.fParticles[1] = this.fParticle;
                    group.fAnnotations = XSObjectListImpl.EMPTY_LIST;
                    XSParticleDecl particle = new XSParticleDecl();
                    particle.fType = (short) 3;
                    particle.fValue = group;
                    particle.fAnnotations = XSObjectListImpl.EMPTY_LIST;
                    this.fParticle = particle;
                }
                this.fAttrGrp.removeProhibitedAttrs();
                try {
                    mergeAttributes(baseType.getAttrGrp(), this.fAttrGrp, this.fName, true, complexContent2);
                } catch (ComplexTypeRecoverableError e3) {
                    this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
                    this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
                    throw e3;
                }
            }
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
        } catch (ComplexTypeRecoverableError e4) {
            this.fAttrChecker.returnAttrArray(complexContentAttrValues, schemaDoc);
            this.fAttrChecker.returnAttrArray(derivationTypeAttrValues, schemaDoc);
            throw e4;
        }
    }

    private void mergeAttributes(XSAttributeGroupDecl fromAttrGrp, XSAttributeGroupDecl toAttrGrp, String typeName, boolean extension, Element elem) throws ComplexTypeRecoverableError {
        XSObjectList attrUseS = fromAttrGrp.getAttributeUses();
        int attrCount = attrUseS.getLength();
        for (int i2 = 0; i2 < attrCount; i2++) {
            XSAttributeUseImpl oneAttrUse = (XSAttributeUseImpl) attrUseS.item(i2);
            XSAttributeUse existingAttrUse = toAttrGrp.getAttributeUse(oneAttrUse.fAttrDecl.getNamespace(), oneAttrUse.fAttrDecl.getName());
            if (existingAttrUse == null) {
                String idName = toAttrGrp.addAttributeUse(oneAttrUse);
                if (idName != null) {
                    throw new ComplexTypeRecoverableError("ct-props-correct.5", new Object[]{typeName, idName, oneAttrUse.fAttrDecl.getName()}, elem);
                }
            } else if (existingAttrUse != oneAttrUse && extension) {
                reportSchemaError("ct-props-correct.4", new Object[]{typeName, oneAttrUse.fAttrDecl.getName()}, elem);
                toAttrGrp.replaceAttributeUse(existingAttrUse, oneAttrUse);
            }
        }
        if (extension) {
            if (toAttrGrp.fAttributeWC == null) {
                toAttrGrp.fAttributeWC = fromAttrGrp.fAttributeWC;
            } else if (fromAttrGrp.fAttributeWC != null) {
                toAttrGrp.fAttributeWC = toAttrGrp.fAttributeWC.performUnionWith(fromAttrGrp.fAttributeWC, toAttrGrp.fAttributeWC.fProcessContents);
                if (toAttrGrp.fAttributeWC == null) {
                    throw new ComplexTypeRecoverableError("src-ct.5", new Object[]{typeName}, elem);
                }
            }
        }
    }

    private void processComplexContent(Element complexContentChild, boolean isMixed, boolean isDerivation, XSDocumentInfo schemaDoc, SchemaGrammar grammar) throws ComplexTypeRecoverableError {
        Element attrNode = null;
        XSParticleDecl particle = null;
        boolean emptyParticle = false;
        if (complexContentChild != null) {
            String childName = DOMUtil.getLocalName(complexContentChild);
            if (childName.equals(SchemaSymbols.ELT_GROUP)) {
                particle = this.fSchemaHandler.fGroupTraverser.traverseLocal(complexContentChild, schemaDoc, grammar);
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            } else if (childName.equals(SchemaSymbols.ELT_SEQUENCE)) {
                particle = traverseSequence(complexContentChild, schemaDoc, grammar, 0, this.fComplexTypeDecl);
                if (particle != null) {
                    XSModelGroupImpl group = (XSModelGroupImpl) particle.fValue;
                    if (group.fParticleCount == 0) {
                        emptyParticle = true;
                    }
                }
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            } else if (childName.equals(SchemaSymbols.ELT_CHOICE)) {
                particle = traverseChoice(complexContentChild, schemaDoc, grammar, 0, this.fComplexTypeDecl);
                if (particle != null && particle.fMinOccurs == 0) {
                    XSModelGroupImpl group2 = (XSModelGroupImpl) particle.fValue;
                    if (group2.fParticleCount == 0) {
                        emptyParticle = true;
                    }
                }
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            } else if (childName.equals(SchemaSymbols.ELT_ALL)) {
                particle = traverseAll(complexContentChild, schemaDoc, grammar, 8, this.fComplexTypeDecl);
                if (particle != null) {
                    XSModelGroupImpl group3 = (XSModelGroupImpl) particle.fValue;
                    if (group3.fParticleCount == 0) {
                        emptyParticle = true;
                    }
                }
                attrNode = DOMUtil.getNextSiblingElement(complexContentChild);
            } else {
                attrNode = complexContentChild;
            }
        }
        if (emptyParticle) {
            Element child = DOMUtil.getFirstChildElement(complexContentChild);
            if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                child = DOMUtil.getNextSiblingElement(child);
            }
            if (child == null) {
                particle = null;
            }
        }
        if (particle == null && isMixed) {
            particle = XSConstraints.getEmptySequence();
        }
        this.fParticle = particle;
        if (this.fParticle == null) {
            this.fContentType = (short) 0;
        } else if (isMixed) {
            this.fContentType = (short) 3;
        } else {
            this.fContentType = (short) 2;
        }
        if (attrNode != null) {
            if (!isAttrOrAttrGroup(attrNode)) {
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(attrNode)}, attrNode);
            }
            Element node = traverseAttrsAndAttrGrps(attrNode, this.fAttrGrp, schemaDoc, grammar, this.fComplexTypeDecl);
            if (node != null) {
                throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(node)}, node);
            }
            if (!isDerivation) {
                this.fAttrGrp.removeProhibitedAttrs();
            }
        }
    }

    private boolean isAttrOrAttrGroup(Element e2) {
        String elementName = DOMUtil.getLocalName(e2);
        if (elementName.equals(SchemaSymbols.ELT_ATTRIBUTE) || elementName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) || elementName.equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
            return true;
        }
        return false;
    }

    private void traverseSimpleContentDecl(Element simpleContentDecl) {
    }

    private void traverseComplexContentDecl(Element complexContentDecl, boolean mixedOnComplexTypeDecl) {
    }

    private String genAnonTypeName(Element complexTypeDecl) {
        StringBuffer typeName = new StringBuffer("#AnonType_");
        Element parent = DOMUtil.getParent(complexTypeDecl);
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

    private void handleComplexTypeError(String messageId, Object[] args, Element e2) {
        if (messageId != null) {
            reportSchemaError(messageId, args, e2);
        }
        this.fBaseType = SchemaGrammar.fAnyType;
        this.fContentType = (short) 3;
        this.fXSSimpleType = null;
        this.fParticle = getErrorContent();
        this.fAttrGrp.fAttributeWC = getErrorWildcard();
    }

    private void contentBackup() {
        if (this.fGlobalStore == null) {
            this.fGlobalStore = new Object[11];
            this.fGlobalStorePos = 0;
        }
        if (this.fGlobalStorePos == this.fGlobalStore.length) {
            Object[] newArray = new Object[this.fGlobalStorePos + 11];
            System.arraycopy(this.fGlobalStore, 0, newArray, 0, this.fGlobalStorePos);
            this.fGlobalStore = newArray;
        }
        Object[] objArr = this.fGlobalStore;
        int i2 = this.fGlobalStorePos;
        this.fGlobalStorePos = i2 + 1;
        objArr[i2] = this.fComplexTypeDecl;
        Object[] objArr2 = this.fGlobalStore;
        int i3 = this.fGlobalStorePos;
        this.fGlobalStorePos = i3 + 1;
        objArr2[i3] = this.fIsAbstract ? Boolean.TRUE : Boolean.FALSE;
        Object[] objArr3 = this.fGlobalStore;
        int i4 = this.fGlobalStorePos;
        this.fGlobalStorePos = i4 + 1;
        objArr3[i4] = this.fName;
        Object[] objArr4 = this.fGlobalStore;
        int i5 = this.fGlobalStorePos;
        this.fGlobalStorePos = i5 + 1;
        objArr4[i5] = this.fTargetNamespace;
        Object[] objArr5 = this.fGlobalStore;
        int i6 = this.fGlobalStorePos;
        this.fGlobalStorePos = i6 + 1;
        objArr5[i6] = new Integer((this.fDerivedBy << 16) + this.fFinal);
        Object[] objArr6 = this.fGlobalStore;
        int i7 = this.fGlobalStorePos;
        this.fGlobalStorePos = i7 + 1;
        objArr6[i7] = new Integer((this.fBlock << 16) + this.fContentType);
        Object[] objArr7 = this.fGlobalStore;
        int i8 = this.fGlobalStorePos;
        this.fGlobalStorePos = i8 + 1;
        objArr7[i8] = this.fBaseType;
        Object[] objArr8 = this.fGlobalStore;
        int i9 = this.fGlobalStorePos;
        this.fGlobalStorePos = i9 + 1;
        objArr8[i9] = this.fAttrGrp;
        Object[] objArr9 = this.fGlobalStore;
        int i10 = this.fGlobalStorePos;
        this.fGlobalStorePos = i10 + 1;
        objArr9[i10] = this.fParticle;
        Object[] objArr10 = this.fGlobalStore;
        int i11 = this.fGlobalStorePos;
        this.fGlobalStorePos = i11 + 1;
        objArr10[i11] = this.fXSSimpleType;
        Object[] objArr11 = this.fGlobalStore;
        int i12 = this.fGlobalStorePos;
        this.fGlobalStorePos = i12 + 1;
        objArr11[i12] = this.fAnnotations;
    }

    private void contentRestore() {
        Object[] objArr = this.fGlobalStore;
        int i2 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i2;
        this.fAnnotations = (XSAnnotationImpl[]) objArr[i2];
        Object[] objArr2 = this.fGlobalStore;
        int i3 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i3;
        this.fXSSimpleType = (XSSimpleType) objArr2[i3];
        Object[] objArr3 = this.fGlobalStore;
        int i4 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i4;
        this.fParticle = (XSParticleDecl) objArr3[i4];
        Object[] objArr4 = this.fGlobalStore;
        int i5 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i5;
        this.fAttrGrp = (XSAttributeGroupDecl) objArr4[i5];
        Object[] objArr5 = this.fGlobalStore;
        int i6 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i6;
        this.fBaseType = (XSTypeDefinition) objArr5[i6];
        Object[] objArr6 = this.fGlobalStore;
        int i7 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i7;
        int i8 = ((Integer) objArr6[i7]).intValue();
        this.fBlock = (short) (i8 >> 16);
        this.fContentType = (short) i8;
        Object[] objArr7 = this.fGlobalStore;
        int i9 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i9;
        int i10 = ((Integer) objArr7[i9]).intValue();
        this.fDerivedBy = (short) (i10 >> 16);
        this.fFinal = (short) i10;
        Object[] objArr8 = this.fGlobalStore;
        int i11 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i11;
        this.fTargetNamespace = (String) objArr8[i11];
        Object[] objArr9 = this.fGlobalStore;
        int i12 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i12;
        this.fName = (String) objArr9[i12];
        Object[] objArr10 = this.fGlobalStore;
        int i13 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i13;
        this.fIsAbstract = ((Boolean) objArr10[i13]).booleanValue();
        Object[] objArr11 = this.fGlobalStore;
        int i14 = this.fGlobalStorePos - 1;
        this.fGlobalStorePos = i14;
        this.fComplexTypeDecl = (XSComplexTypeDecl) objArr11[i14];
    }

    private void addAnnotation(XSAnnotationImpl annotation) {
        if (annotation == null) {
            return;
        }
        if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[1];
        } else {
            XSAnnotationImpl[] tempArray = new XSAnnotationImpl[this.fAnnotations.length + 1];
            System.arraycopy(this.fAnnotations, 0, tempArray, 0, this.fAnnotations.length);
            this.fAnnotations = tempArray;
        }
        this.fAnnotations[this.fAnnotations.length - 1] = annotation;
    }
}
