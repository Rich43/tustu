package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl;
import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSWildcardDecl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSDWildcardTraverser.class */
class XSDWildcardTraverser extends XSDAbstractTraverser {
    XSDWildcardTraverser(XSDHandler handler, XSAttributeChecker gAttrCheck) {
        super(handler, gAttrCheck);
    }

    XSParticleDecl traverseAny(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
        XSWildcardDecl wildcard = traverseWildcardDecl(elmNode, attrValues, schemaDoc, grammar);
        XSParticleDecl particle = null;
        if (wildcard != null) {
            int min = ((XInt) attrValues[XSAttributeChecker.ATTIDX_MINOCCURS]).intValue();
            int max = ((XInt) attrValues[XSAttributeChecker.ATTIDX_MAXOCCURS]).intValue();
            if (max != 0) {
                if (this.fSchemaHandler.fDeclPool != null) {
                    particle = this.fSchemaHandler.fDeclPool.getParticleDecl();
                } else {
                    particle = new XSParticleDecl();
                }
                particle.fType = (short) 2;
                particle.fValue = wildcard;
                particle.fMinOccurs = min;
                particle.fMaxOccurs = max;
                particle.fAnnotations = wildcard.fAnnotations;
            }
        }
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return particle;
    }

    XSWildcardDecl traverseAnyAttribute(Element elmNode, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        Object[] attrValues = this.fAttrChecker.checkAttributes(elmNode, false, schemaDoc);
        XSWildcardDecl wildcard = traverseWildcardDecl(elmNode, attrValues, schemaDoc, grammar);
        this.fAttrChecker.returnAttrArray(attrValues, schemaDoc);
        return wildcard;
    }

    XSWildcardDecl traverseWildcardDecl(Element elmNode, Object[] attrValues, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
        XSObjectList annotations;
        XSWildcardDecl wildcard = new XSWildcardDecl();
        XInt namespaceTypeAttr = (XInt) attrValues[XSAttributeChecker.ATTIDX_NAMESPACE];
        wildcard.fType = namespaceTypeAttr.shortValue();
        wildcard.fNamespaceList = (String[]) attrValues[XSAttributeChecker.ATTIDX_NAMESPACE_LIST];
        XInt processContentsAttr = (XInt) attrValues[XSAttributeChecker.ATTIDX_PROCESSCONTENTS];
        wildcard.fProcessContents = processContentsAttr.shortValue();
        Element child = DOMUtil.getFirstChildElement(elmNode);
        XSAnnotationImpl annotation = null;
        if (child != null) {
            if (DOMUtil.getLocalName(child).equals(SchemaSymbols.ELT_ANNOTATION)) {
                annotation = traverseAnnotationDecl(child, attrValues, false, schemaDoc);
                child = DOMUtil.getNextSiblingElement(child);
            } else {
                String text = DOMUtil.getSyntheticAnnotation(elmNode);
                if (text != null) {
                    annotation = traverseSyntheticAnnotation(elmNode, text, attrValues, false, schemaDoc);
                }
            }
            if (child != null) {
                reportSchemaError("s4s-elt-must-match.1", new Object[]{"wildcard", "(annotation?)", DOMUtil.getLocalName(child)}, elmNode);
            }
        } else {
            String text2 = DOMUtil.getSyntheticAnnotation(elmNode);
            if (text2 != null) {
                annotation = traverseSyntheticAnnotation(elmNode, text2, attrValues, false, schemaDoc);
            }
        }
        if (annotation != null) {
            annotations = new XSObjectListImpl();
            ((XSObjectListImpl) annotations).addXSObject(annotation);
        } else {
            annotations = XSObjectListImpl.EMPTY_LIST;
        }
        wildcard.fAnnotations = annotations;
        return wildcard;
    }
}
