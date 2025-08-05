package com.sun.org.apache.xerces.internal.xni;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XMLDocumentFragmentHandler.class */
public interface XMLDocumentFragmentHandler {
    void startDocumentFragment(XMLLocator xMLLocator, NamespaceContext namespaceContext, Augmentations augmentations) throws XNIException;

    void startGeneralEntity(String str, XMLResourceIdentifier xMLResourceIdentifier, String str2, Augmentations augmentations) throws XNIException;

    void textDecl(String str, String str2, Augmentations augmentations) throws XNIException;

    void endGeneralEntity(String str, Augmentations augmentations) throws XNIException;

    void comment(XMLString xMLString, Augmentations augmentations) throws XNIException;

    void processingInstruction(String str, XMLString xMLString, Augmentations augmentations) throws XNIException;

    void startElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException;

    void emptyElement(QName qName, XMLAttributes xMLAttributes, Augmentations augmentations) throws XNIException;

    void characters(XMLString xMLString, Augmentations augmentations) throws XNIException;

    void ignorableWhitespace(XMLString xMLString, Augmentations augmentations) throws XNIException;

    void endElement(QName qName, Augmentations augmentations) throws XNIException;

    void startCDATA(Augmentations augmentations) throws XNIException;

    void endCDATA(Augmentations augmentations) throws XNIException;

    void endDocumentFragment(Augmentations augmentations) throws XNIException;
}
