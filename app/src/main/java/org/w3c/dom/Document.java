package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/Document.class */
public interface Document extends Node {
    DocumentType getDoctype();

    DOMImplementation getImplementation();

    Element getDocumentElement();

    Element createElement(String str) throws DOMException;

    DocumentFragment createDocumentFragment();

    Text createTextNode(String str);

    Comment createComment(String str);

    CDATASection createCDATASection(String str) throws DOMException;

    ProcessingInstruction createProcessingInstruction(String str, String str2) throws DOMException;

    Attr createAttribute(String str) throws DOMException;

    EntityReference createEntityReference(String str) throws DOMException;

    NodeList getElementsByTagName(String str);

    Node importNode(Node node, boolean z2) throws DOMException;

    Element createElementNS(String str, String str2) throws DOMException;

    Attr createAttributeNS(String str, String str2) throws DOMException;

    NodeList getElementsByTagNameNS(String str, String str2);

    Element getElementById(String str);

    String getInputEncoding();

    String getXmlEncoding();

    boolean getXmlStandalone();

    void setXmlStandalone(boolean z2) throws DOMException;

    String getXmlVersion();

    void setXmlVersion(String str) throws DOMException;

    boolean getStrictErrorChecking();

    void setStrictErrorChecking(boolean z2);

    String getDocumentURI();

    void setDocumentURI(String str);

    Node adoptNode(Node node) throws DOMException;

    DOMConfiguration getDomConfig();

    void normalizeDocument();

    Node renameNode(Node node, String str, String str2) throws DOMException;
}
