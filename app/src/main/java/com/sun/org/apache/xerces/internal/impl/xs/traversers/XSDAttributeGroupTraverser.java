package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeGroupDecl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDAttributeGroupTraverser.class */
class XSDAttributeGroupTraverser extends XSDAbstractTraverser {
    XSDAttributeGroupTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
    }

    XSAttributeGroupDecl traverseLocal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
        QName refAttr = (QName) attrValues[XSAttributeChecker.ATTIDX_REF];
        if (refAttr == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{"attributeGroup (local)", "ref"}, elmNode);
            this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
            return null;
        }
        XSAttributeGroupDecl attrGrp = (XSAttributeGroupDecl) this.fSchemaHandler.getGlobalDecl(schemaDoc, 2, refAttr, elmNode);
        Element child = DOMUtil.getFirstChildElement(elmNode);
        if (child != null) {
            String childName = DOMUtil.getLocalName(child);
            if (childName.equals(SchemaSymbols.ELT_ANNOTATION)) {
                traverseAnnotationDecl(child, attrValues, false, schemaDoc);
                child = DOMUtil.getNextSiblingElement(child);
            } else {
                String text = DOMUtil.getSyntheticAnnotation(child);
                if (text != null) {
                    traverseSyntheticAnnotation(child, text, attrValues, false, schemaDoc);
                }
            }
            if (child != null) {
                Object[] args = {refAttr.rawname, "(annotation?)", DOMUtil.getLocalName(child)};
                reportSchemaError("s4s-elt-must-match.1", args, child);
            }
        }
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return attrGrp;
    }

    XSAttributeGroupDecl traverseGlobal(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        XSObjectList annotations;
        Object[] errArgs;
        XSAttributeGroupDecl attrGrp = new XSAttributeGroupDecl();
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, true, schemaDoc);
        String nameAttr = (String) attrValues[XSAttributeChecker.ATTIDX_NAME];
        if (nameAttr == null) {
            reportSchemaError("s4s-att-must-appear", new Object[]{"attributeGroup (global)", "name"}, elmNode);
            nameAttr = "(no name)";
        }
        attrGrp.fName = nameAttr;
        attrGrp.fTargetNamespace = schemaDoc.fTargetNamespace;
        Element child = DOMUtil.getFirstChildElement(elmNode);
        XSAnnotationImpl annotation = null;
        if (child != null && DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
            annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
            child = DOMUtil.getNextSiblingElement(child);
        } else {
            String text = DOMUtil.getSyntheticAnnotation(elmNode);
            if (text != null) {
                annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
            }
        }
        Element nextNode = traverseAttrsAndAttrGrps(child, attrGrp, schemaDoc, grammar, null);
        if (nextNode != null) {
            Object[] args = {nameAttr, "(annotation?, ((attribute | attributeGroup)*, anyAttribute?))", DOMUtil.getLocalName(nextNode)};
            reportSchemaError("s4s-elt-must-match.1", args, nextNode);
        }
        if (nameAttr.equals("(no name)")) {
            this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
            return null;
        }
        attrGrp.removeProhibitedAttrs();
        XSAttributeGroupDecl redefinedAttrGrp = (XSAttributeGroupDecl) this.fSchemaHandler.getGrpOrAttrGrpRedefinedByRestriction(2, new QName(XMLSymbols.EMPTY_STRING, nameAttr, nameAttr, schemaDoc.fTargetNamespace), schemaDoc, elmNode);
        if (redefinedAttrGrp != null && (errArgs = attrGrp.validRestrictionOf(nameAttr, redefinedAttrGrp)) != null) {
            reportSchemaError((String) errArgs[errArgs.length - 1], errArgs, child);
            reportSchemaError("src-redefine.7.2.2", new Object[]{nameAttr, errArgs[errArgs.length - 1]}, child);
        }
        if (annotation != null) {
            annotations = new XSObjectListImpl();
            ((XSObjectListImpl) annotations).addXSObject(annotation);
        } else {
            annotations = XSObjectListImpl.EMPTY_LIST;
        }
        attrGrp.fAnnotations = annotations;
        if (grammar.getGlobalAttributeGroupDecl(attrGrp.fName) == null) {
            grammar.addGlobalAttributeGroupDecl(attrGrp);
        }
        String loc = this.fSchemaHandler.schemaDocument2SystemId(schemaDoc);
        XSAttributeGroupDecl attrGrp2 = grammar.getGlobalAttributeGroupDecl(attrGrp.fName, loc);
        if (attrGrp2 == null) {
            grammar.addGlobalAttributeGroupDecl(attrGrp, loc);
        }
        if (this.fSchemaHandler.fTolerateDuplicates) {
            if (attrGrp2 != null) {
                attrGrp = attrGrp2;
            }
            this.fSchemaHandler.addGlobalAttributeGroupDecl(attrGrp);
        }
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return attrGrp;
    }
}
