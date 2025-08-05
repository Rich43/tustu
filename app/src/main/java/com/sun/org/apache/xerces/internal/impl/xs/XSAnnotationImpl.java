package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import java.io.IOException;
import java.io.StringReader;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/XSAnnotationImpl.class */
public class XSAnnotationImpl implements XSAnnotation {
    private String fData;
    private SchemaGrammar fGrammar;

    public XSAnnotationImpl(String contents, SchemaGrammar grammar) {
        this.fData = null;
        this.fGrammar = null;
        this.fData = contents;
        this.fGrammar = grammar;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAnnotation
    public boolean writeAnnotation(Object target, short targetType) throws DOMException {
        if (targetType == 1 || targetType == 3) {
            writeToDOM((Node) target, targetType);
            return true;
        }
        if (targetType == 2) {
            writeToSAX((ContentHandler) target);
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSAnnotation
    public String getAnnotationString() {
        return this.fData;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 12;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return null;
    }

    private synchronized void writeToSAX(ContentHandler handler) {
        SAXParser parser = this.fGrammar.getSAXParser();
        StringReader aReader = new StringReader(this.fData);
        InputSource aSource = new InputSource(aReader);
        parser.setContentHandler(handler);
        try {
            parser.parse(aSource);
        } catch (IOException e2) {
        } catch (SAXException e3) {
        }
        parser.setContentHandler(null);
    }

    private synchronized void writeToDOM(Node target, short type) throws DOMException {
        Node newElem;
        Document futureOwner = type == 1 ? target.getOwnerDocument() : (Document) target;
        DOMParser parser = this.fGrammar.getDOMParser();
        StringReader aReader = new StringReader(this.fData);
        InputSource aSource = new InputSource(aReader);
        try {
            parser.parse(aSource);
        } catch (IOException e2) {
        } catch (SAXException e3) {
        }
        Document aDocument = parser.getDocument();
        parser.dropDocumentReferences();
        Element annotation = aDocument.getDocumentElement();
        if (futureOwner instanceof CoreDocumentImpl) {
            newElem = futureOwner.adoptNode(annotation);
            if (newElem == null) {
                newElem = futureOwner.importNode(annotation, true);
            }
        } else {
            newElem = futureOwner.importNode(annotation, true);
        }
        target.insertBefore(newElem, target.getFirstChild());
    }
}
