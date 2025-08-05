package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.xpath.XPathException;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Field;
import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDAbstractIDConstraintTraverser.class */
class XSDAbstractIDConstraintTraverser extends XSDAbstractTraverser {
    public XSDAbstractIDConstraintTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
    }

    boolean traverseIdentityConstraint(IdentityConstraint ic, Element icElem, XSDocumentInfo schemaDoc, Object[] icElemAttrs) {
        Element sElem = DOMUtil.getFirstChildElement(icElem);
        if (sElem == null) {
            reportSchemaError("s4s-elt-must-match.2", new Object[]{"identity constraint", "(annotation?, selector, field+)"}, icElem);
            return false;
        }
        if (DOMUtil.getLocalName(sElem).equals(SchemaSymbols.ELT_ANNOTATION)) {
            ic.addAnnotation(traverseAnnotationDecl(sElem, icElemAttrs, false, schemaDoc));
            sElem = DOMUtil.getNextSiblingElement(sElem);
            if (sElem == null) {
                reportSchemaError("s4s-elt-must-match.2", new Object[]{"identity constraint", "(annotation?, selector, field+)"}, icElem);
                return false;
            }
        } else {
            String text = DOMUtil.getSyntheticAnnotation(icElem);
            if (text != null) {
                ic.addAnnotation(traverseSyntheticAnnotation(icElem, text, icElemAttrs, false, schemaDoc));
            }
        }
        if (!DOMUtil.getLocalName(sElem).equals(SchemaSymbols.ELT_SELECTOR)) {
            reportSchemaError("s4s-elt-must-match.1", new Object[]{"identity constraint", "(annotation?, selector, field+)", SchemaSymbols.ELT_SELECTOR}, sElem);
            return false;
        }
        Object[] attrValues = this.fAttrChecker.checkAttributes(sElem, false, schemaDoc);
        Element selChild = DOMUtil.getFirstChildElement(sElem);
        if (selChild != null) {
            if (DOMUtil.getLocalName(selChild).equals(SchemaSymbols.ELT_ANNOTATION)) {
                ic.addAnnotation(traverseAnnotationDecl(selChild, attrValues, false, schemaDoc));
                selChild = DOMUtil.getNextSiblingElement(selChild);
            } else {
                reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SELECTOR, "(annotation?)", DOMUtil.getLocalName(selChild)}, selChild);
            }
            if (selChild != null) {
                reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SELECTOR, "(annotation?)", DOMUtil.getLocalName(selChild)}, selChild);
            }
        } else {
            String text2 = DOMUtil.getSyntheticAnnotation(sElem);
            if (text2 != null) {
                ic.addAnnotation(traverseSyntheticAnnotation(icElem, text2, attrValues, false, schemaDoc));
            }
        }
        String sText = (String) attrValues[XSAttributeChecker.ATTIDX_XPATH];
        if (sText == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_SELECTOR, SchemaSymbols.ATT_XPATH}, sElem);
            return false;
        }
        String sText2 = XMLChar.trim(sText);
        try {
            Selector.XPath sXpath = new Selector.XPath(sText2, this.fSymbolTable, schemaDoc.fNamespaceSupport);
            Selector selector = new Selector(sXpath, ic);
            ic.setSelector(selector);
            this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
            Element fElem = DOMUtil.getNextSiblingElement(sElem);
            if (fElem == null) {
                reportSchemaError("s4s-elt-must-match.2", new Object[]{"identity constraint", "(annotation?, selector, field+)"}, sElem);
                return false;
            }
            while (fElem != null) {
                if (!DOMUtil.getLocalName(fElem).equals(SchemaSymbols.ELT_FIELD)) {
                    reportSchemaError("s4s-elt-must-match.1", new Object[]{"identity constraint", "(annotation?, selector, field+)", SchemaSymbols.ELT_FIELD}, fElem);
                    fElem = DOMUtil.getNextSiblingElement(fElem);
                } else {
                    Object[] attrValues2 = this.fAttrChecker.checkAttributes(fElem, false, schemaDoc);
                    Element fieldChild = DOMUtil.getFirstChildElement(fElem);
                    if (fieldChild != null && DOMUtil.getLocalName(fieldChild).equals(SchemaSymbols.ELT_ANNOTATION)) {
                        ic.addAnnotation(traverseAnnotationDecl(fieldChild, attrValues2, false, schemaDoc));
                        fieldChild = DOMUtil.getNextSiblingElement(fieldChild);
                    }
                    if (fieldChild != null) {
                        reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_FIELD, "(annotation?)", DOMUtil.getLocalName(fieldChild)}, fieldChild);
                    } else {
                        String text3 = DOMUtil.getSyntheticAnnotation(fElem);
                        if (text3 != null) {
                            ic.addAnnotation(traverseSyntheticAnnotation(icElem, text3, attrValues2, false, schemaDoc));
                        }
                    }
                    String fText = (String) attrValues2[XSAttributeChecker.ATTIDX_XPATH];
                    if (fText == null) {
                        reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_FIELD, SchemaSymbols.ATT_XPATH}, fElem);
                        this.fAttrChecker.returnAttrArray(attrValues2, schemaDoc);
                        return false;
                    }
                    String fText2 = XMLChar.trim(fText);
                    try {
                        Field.XPath fXpath = new Field.XPath(fText2, this.fSymbolTable, schemaDoc.fNamespaceSupport);
                        Field field = new Field(fXpath, ic);
                        ic.addField(field);
                        fElem = DOMUtil.getNextSiblingElement(fElem);
                        this.fAttrChecker.returnAttrArray(attrValues2, schemaDoc);
                    } catch (XPathException e2) {
                        reportSchemaError(e2.getKey(), new Object[]{fText2}, fElem);
                        this.fAttrChecker.returnAttrArray(attrValues2, schemaDoc);
                        return false;
                    }
                }
            }
            return ic.getFieldCount() > 0;
        } catch (XPathException e3) {
            reportSchemaError(e3.getKey(), new Object[]{sText2}, sElem);
            this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
            return false;
        }
    }
}
