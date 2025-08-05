package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/DOMImplementation.class */
public interface DOMImplementation {
    boolean hasFeature(String str, String str2);

    DocumentType createDocumentType(String str, String str2, String str3) throws DOMException;

    Document createDocument(String str, String str2, DocumentType documentType) throws DOMException;

    Object getFeature(String str, String str2);
}
