package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationState;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Vector;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDAbstractTraverser.class */
abstract class XSDAbstractTraverser {
    protected static final String NO_NAME = "(no name)";
    protected static final int NOT_ALL_CONTEXT = 0;
    protected static final int PROCESSING_ALL_EL = 1;
    protected static final int GROUP_REF_WITH_ALL = 2;
    protected static final int CHILD_OF_GROUP = 4;
    protected static final int PROCESSING_ALL_GP = 8;
    protected XSDHandler fSchemaHandler;
    protected XSAttributeChecker fAttrChecker;
    private static final XSSimpleType fQNameDV = (XSSimpleType) SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(SchemaSymbols.ATTVAL_QNAME);
    protected SymbolTable fSymbolTable = null;
    protected boolean fValidateAnnotations = false;
    ValidationState fValidationState = new ValidationState();
    private StringBuffer fPattern = new StringBuffer();
    private final XSFacets xsFacets = new XSFacets();

    XSDAbstractTraverser(XSDHandler handler, XSAttributeChecker attrChecker) {
        this.fSchemaHandler = null;
        this.fAttrChecker = null;
        this.fSchemaHandler = handler;
        this.fAttrChecker = attrChecker;
    }

    void reset(SymbolTable symbolTable, boolean validateAnnotations, Locale locale) {
        this.fSymbolTable = symbolTable;
        this.fValidateAnnotations = validateAnnotations;
        this.fValidationState.setExtraChecking(false);
        this.fValidationState.setSymbolTable(symbolTable);
        this.fValidationState.setLocale(locale);
    }

    XSAnnotationImpl traverseAnnotationDecl(Element annotationDecl, Object[] parentAttrs, boolean isGlobal, XSDocumentInfo schemaDoc) throws MissingResourceException, XNIException {
        String prefix;
        String localpart;
        Object[] attrValues = this.fAttrChecker.checkAttributes(annotationDecl, isGlobal, schemaDoc);
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        String contents = DOMUtil.getAnnotation(annotationDecl);
        Element child = DOMUtil.getFirstChildElement(annotationDecl);
        if (child != null) {
            do {
                String name = DOMUtil.getLocalName(child);
                if (!name.equals(SchemaSymbols.ELT_APPINFO) && !name.equals(SchemaSymbols.ELT_DOCUMENTATION)) {
                    reportSchemaError("src-annotation", new Object[]{name}, child);
                } else {
                    Object[] attrValues2 = this.fAttrChecker.checkAttributes(child, true, schemaDoc);
                    this.fAttrChecker.returnAttrArray(attrValues2, schemaDoc);
                }
                child = DOMUtil.getNextSiblingElement(child);
            } while (child != null);
        }
        if (contents == null) {
            return null;
        }
        SchemaGrammar grammar = this.fSchemaHandler.getGrammar(schemaDoc.fTargetNamespace);
        Vector annotationLocalAttrs = (Vector) parentAttrs[XSAttributeChecker.ATTIDX_NONSCHEMA];
        if (annotationLocalAttrs != null && !annotationLocalAttrs.isEmpty()) {
            StringBuffer localStrBuffer = new StringBuffer(64);
            localStrBuffer.append(" ");
            int i2 = 0;
            while (i2 < annotationLocalAttrs.size()) {
                int i3 = i2;
                int i4 = i2 + 1;
                String rawname = (String) annotationLocalAttrs.elementAt(i3);
                int colonIndex = rawname.indexOf(58);
                if (colonIndex == -1) {
                    prefix = "";
                    localpart = rawname;
                } else {
                    prefix = rawname.substring(0, colonIndex);
                    localpart = rawname.substring(colonIndex + 1);
                }
                String uri = schemaDoc.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(prefix));
                if (annotationDecl.getAttributeNS(uri, localpart).length() != 0) {
                    i2 = i4 + 1;
                } else {
                    localStrBuffer.append(rawname).append("=\"");
                    i2 = i4 + 1;
                    String value = (String) annotationLocalAttrs.elementAt(i4);
                    localStrBuffer.append(processAttValue(value)).append("\" ");
                }
            }
            StringBuffer contentBuffer = new StringBuffer(contents.length() + localStrBuffer.length());
            int annotationTokenEnd = contents.indexOf(SchemaSymbols.ELT_ANNOTATION);
            if (annotationTokenEnd == -1) {
                return null;
            }
            int annotationTokenEnd2 = annotationTokenEnd + SchemaSymbols.ELT_ANNOTATION.length();
            contentBuffer.append(contents.substring(0, annotationTokenEnd2));
            contentBuffer.append(localStrBuffer.toString());
            contentBuffer.append(contents.substring(annotationTokenEnd2, contents.length()));
            String annotation = contentBuffer.toString();
            if (this.fValidateAnnotations) {
                schemaDoc.addAnnotation(new XSAnnotationInfo(annotation, annotationDecl));
            }
            return new XSAnnotationImpl(annotation, grammar);
        }
        if (this.fValidateAnnotations) {
            schemaDoc.addAnnotation(new XSAnnotationInfo(contents, annotationDecl));
        }
        return new XSAnnotationImpl(contents, grammar);
    }

    XSAnnotationImpl traverseSyntheticAnnotation(Element annotationParent, String initialContent, Object[] parentAttrs, boolean isGlobal, XSDocumentInfo schemaDoc) {
        String prefix;
        String strSubstring;
        SchemaGrammar grammar = this.fSchemaHandler.getGrammar(schemaDoc.fTargetNamespace);
        Vector annotationLocalAttrs = (Vector) parentAttrs[XSAttributeChecker.ATTIDX_NONSCHEMA];
        if (annotationLocalAttrs != null && !annotationLocalAttrs.isEmpty()) {
            StringBuffer localStrBuffer = new StringBuffer(64);
            localStrBuffer.append(" ");
            int i2 = 0;
            while (i2 < annotationLocalAttrs.size()) {
                int i3 = i2;
                int i4 = i2 + 1;
                String rawname = (String) annotationLocalAttrs.elementAt(i3);
                int colonIndex = rawname.indexOf(58);
                if (colonIndex == -1) {
                    prefix = "";
                    strSubstring = rawname;
                } else {
                    prefix = rawname.substring(0, colonIndex);
                    strSubstring = rawname.substring(colonIndex + 1);
                }
                schemaDoc.fNamespaceSupport.getURI(this.fSymbolTable.addSymbol(prefix));
                localStrBuffer.append(rawname).append("=\"");
                i2 = i4 + 1;
                String value = (String) annotationLocalAttrs.elementAt(i4);
                localStrBuffer.append(processAttValue(value)).append("\" ");
            }
            StringBuffer contentBuffer = new StringBuffer(initialContent.length() + localStrBuffer.length());
            int annotationTokenEnd = initialContent.indexOf(SchemaSymbols.ELT_ANNOTATION);
            if (annotationTokenEnd == -1) {
                return null;
            }
            int annotationTokenEnd2 = annotationTokenEnd + SchemaSymbols.ELT_ANNOTATION.length();
            contentBuffer.append(initialContent.substring(0, annotationTokenEnd2));
            contentBuffer.append(localStrBuffer.toString());
            contentBuffer.append(initialContent.substring(annotationTokenEnd2, initialContent.length()));
            String annotation = contentBuffer.toString();
            if (this.fValidateAnnotations) {
                schemaDoc.addAnnotation(new XSAnnotationInfo(annotation, annotationParent));
            }
            return new XSAnnotationImpl(annotation, grammar);
        }
        if (this.fValidateAnnotations) {
            schemaDoc.addAnnotation(new XSAnnotationInfo(initialContent, annotationParent));
        }
        return new XSAnnotationImpl(initialContent, grammar);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDAbstractTraverser$FacetInfo.class */
    static final class FacetInfo {
        final XSFacets facetdata;
        final Element nodeAfterFacets;
        final short fPresentFacets;
        final short fFixedFacets;

        FacetInfo(XSFacets facets, Element nodeAfterFacets, short presentFacets, short fixedFacets) {
            this.facetdata = facets;
            this.nodeAfterFacets = nodeAfterFacets;
            this.fPresentFacets = presentFacets;
            this.fFixedFacets = fixedFacets;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:151:0x069f  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x06cb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser.FacetInfo traverseFacets(org.w3c.dom.Element r10, com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType r11, com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r12) throws java.util.MissingResourceException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 1787
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser.traverseFacets(org.w3c.dom.Element, com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType, com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo):com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDAbstractTraverser$FacetInfo");
    }

    private boolean containsQName(XSSimpleType type) {
        if (type.getVariety() == 1) {
            short primitive = type.getPrimitiveKind();
            return primitive == 18 || primitive == 20;
        }
        if (type.getVariety() == 2) {
            return containsQName((XSSimpleType) type.getItemType());
        }
        if (type.getVariety() == 3) {
            XSObjectList members = type.getMemberTypes();
            for (int i2 = 0; i2 < members.getLength(); i2++) {
                if (containsQName((XSSimpleType) members.item(i2))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    Element traverseAttrsAndAttrGrps(Element firstAttr, XSAttributeGroupDecl attrGrp, XSDocumentInfo schemaDoc, SchemaGrammar grammar, XSComplexTypeDecl enclosingCT) throws MissingResourceException, XNIException {
        Element child;
        Element nextSiblingElement = firstAttr;
        while (true) {
            child = nextSiblingElement;
            if (child == null) {
                break;
            }
            String childName = DOMUtil.getLocalName(child);
            if (childName.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
                XSAttributeUseImpl tempAttrUse = this.fSchemaHandler.fAttributeTraverser.traverseLocal(child, schemaDoc, grammar, enclosingCT);
                if (tempAttrUse != null) {
                    if (tempAttrUse.fUse == 2) {
                        attrGrp.addAttributeUse(tempAttrUse);
                    } else {
                        XSAttributeUse otherUse = attrGrp.getAttributeUseNoProhibited(tempAttrUse.fAttrDecl.getNamespace(), tempAttrUse.fAttrDecl.getName());
                        if (otherUse == null) {
                            String idName = attrGrp.addAttributeUse(tempAttrUse);
                            if (idName != null) {
                                String code = enclosingCT == null ? "ag-props-correct.3" : "ct-props-correct.5";
                                String name = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
                                reportSchemaError(code, new Object[]{name, tempAttrUse.fAttrDecl.getName(), idName}, child);
                            }
                        } else if (otherUse != tempAttrUse) {
                            String code2 = enclosingCT == null ? "ag-props-correct.2" : "ct-props-correct.4";
                            String name2 = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
                            reportSchemaError(code2, new Object[]{name2, tempAttrUse.fAttrDecl.getName()}, child);
                        }
                    }
                }
            } else {
                if (!childName.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
                    break;
                }
                XSAttributeGroupDecl tempAttrGrp = this.fSchemaHandler.fAttributeGroupTraverser.traverseLocal(child, schemaDoc, grammar);
                if (tempAttrGrp != null) {
                    XSObjectList attrUseS = tempAttrGrp.getAttributeUses();
                    int attrCount = attrUseS.getLength();
                    for (int i2 = 0; i2 < attrCount; i2++) {
                        XSAttributeUseImpl oneAttrUse = (XSAttributeUseImpl) attrUseS.item(i2);
                        if (oneAttrUse.fUse == 2) {
                            attrGrp.addAttributeUse(oneAttrUse);
                        } else {
                            XSAttributeUse otherUse2 = attrGrp.getAttributeUseNoProhibited(oneAttrUse.fAttrDecl.getNamespace(), oneAttrUse.fAttrDecl.getName());
                            if (otherUse2 == null) {
                                String idName2 = attrGrp.addAttributeUse(oneAttrUse);
                                if (idName2 != null) {
                                    String code3 = enclosingCT == null ? "ag-props-correct.3" : "ct-props-correct.5";
                                    String name3 = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
                                    reportSchemaError(code3, new Object[]{name3, oneAttrUse.fAttrDecl.getName(), idName2}, child);
                                }
                            } else if (oneAttrUse != otherUse2) {
                                String code4 = enclosingCT == null ? "ag-props-correct.2" : "ct-props-correct.4";
                                String name4 = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
                                reportSchemaError(code4, new Object[]{name4, oneAttrUse.fAttrDecl.getName()}, child);
                            }
                        }
                    }
                    if (tempAttrGrp.fAttributeWC != null) {
                        if (attrGrp.fAttributeWC == null) {
                            attrGrp.fAttributeWC = tempAttrGrp.fAttributeWC;
                        } else {
                            attrGrp.fAttributeWC = attrGrp.fAttributeWC.performIntersectionWith(tempAttrGrp.fAttributeWC, attrGrp.fAttributeWC.fProcessContents);
                            if (attrGrp.fAttributeWC == null) {
                                String code5 = enclosingCT == null ? "src-attribute_group.2" : "src-ct.4";
                                String name5 = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
                                reportSchemaError(code5, new Object[]{name5}, child);
                            }
                        }
                    }
                }
            }
            nextSiblingElement = DOMUtil.getNextSiblingElement(child);
        }
        if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
            XSWildcardDecl tempAttrWC = this.fSchemaHandler.fWildCardTraverser.traverseAnyAttribute(child, schemaDoc, grammar);
            if (attrGrp.fAttributeWC == null) {
                attrGrp.fAttributeWC = tempAttrWC;
            } else {
                attrGrp.fAttributeWC = tempAttrWC.performIntersectionWith(attrGrp.fAttributeWC, tempAttrWC.fProcessContents);
                if (attrGrp.fAttributeWC == null) {
                    String code6 = enclosingCT == null ? "src-attribute_group.2" : "src-ct.4";
                    String name6 = enclosingCT == null ? attrGrp.fName : enclosingCT.getName();
                    reportSchemaError(code6, new Object[]{name6}, child);
                }
            }
            child = DOMUtil.getNextSiblingElement(child);
        }
        return child;
    }

    void reportSchemaError(String key, Object[] args, Element ele) throws MissingResourceException, XNIException {
        this.fSchemaHandler.reportSchemaError(key, args, ele);
    }

    void checkNotationType(String refName, XSTypeDefinition typeDecl, Element elem) throws MissingResourceException, XNIException {
        if (typeDecl.getTypeCategory() == 16 && ((XSSimpleType) typeDecl).getVariety() == 1 && ((XSSimpleType) typeDecl).getPrimitiveKind() == 20 && (((XSSimpleType) typeDecl).getDefinedFacets() & 2048) == 0) {
            reportSchemaError("enumeration-required-notation", new Object[]{typeDecl.getName(), refName, DOMUtil.getLocalName(elem)}, elem);
        }
    }

    protected XSParticleDecl checkOccurrences(XSParticleDecl particle, String particleName, Element parent, int allContextFlags, long defaultVals) throws MissingResourceException, XNIException {
        int min = particle.fMinOccurs;
        int max = particle.fMaxOccurs;
        boolean defaultMin = (defaultVals & ((long) (1 << XSAttributeChecker.ATTIDX_MINOCCURS))) != 0;
        boolean defaultMax = (defaultVals & ((long) (1 << XSAttributeChecker.ATTIDX_MAXOCCURS))) != 0;
        boolean processingAllEl = (allContextFlags & 1) != 0;
        boolean processingAllGP = (allContextFlags & 8) != 0;
        boolean groupRefWithAll = (allContextFlags & 2) != 0;
        boolean isGroupChild = (allContextFlags & 4) != 0;
        if (isGroupChild) {
            if (!defaultMin) {
                Object[] args = {particleName, "minOccurs"};
                reportSchemaError("s4s-att-not-allowed", args, parent);
                min = 1;
            }
            if (!defaultMax) {
                Object[] args2 = {particleName, "maxOccurs"};
                reportSchemaError("s4s-att-not-allowed", args2, parent);
                max = 1;
            }
        }
        if (min == 0 && max == 0) {
            particle.fType = (short) 0;
            return null;
        }
        if (processingAllEl) {
            if (max != 1) {
                Object[] objArr = new Object[2];
                objArr[0] = max == -1 ? SchemaSymbols.ATTVAL_UNBOUNDED : Integer.toString(max);
                objArr[1] = ((XSElementDecl) particle.fValue).getName();
                reportSchemaError("cos-all-limited.2", objArr, parent);
                max = 1;
                if (min > 1) {
                    min = 1;
                }
            }
        } else if ((processingAllGP || groupRefWithAll) && max != 1) {
            reportSchemaError("cos-all-limited.1.2", null, parent);
            if (min > 1) {
                min = 1;
            }
            max = 1;
        }
        particle.fMinOccurs = min;
        particle.fMaxOccurs = max;
        return particle;
    }

    private static String processAttValue(String original) {
        int length = original.length();
        for (int i2 = 0; i2 < length; i2++) {
            char currChar = original.charAt(i2);
            if (currChar == '\"' || currChar == '<' || currChar == '&' || currChar == '\t' || currChar == '\n' || currChar == '\r') {
                return escapeAttValue(original, i2);
            }
        }
        return original;
    }

    private static String escapeAttValue(String original, int from) {
        int length = original.length();
        StringBuffer newVal = new StringBuffer(length);
        newVal.append(original.substring(0, from));
        for (int i2 = from; i2 < length; i2++) {
            char currChar = original.charAt(i2);
            if (currChar == '\"') {
                newVal.append(SerializerConstants.ENTITY_QUOT);
            } else if (currChar == '<') {
                newVal.append(SerializerConstants.ENTITY_LT);
            } else if (currChar == '&') {
                newVal.append(SerializerConstants.ENTITY_AMP);
            } else if (currChar == '\t') {
                newVal.append("&#x9;");
            } else if (currChar == '\n') {
                newVal.append(SerializerConstants.ENTITY_CRLF);
            } else if (currChar == '\r') {
                newVal.append("&#xD;");
            } else {
                newVal.append(currChar);
            }
        }
        return newVal.toString();
    }
}
