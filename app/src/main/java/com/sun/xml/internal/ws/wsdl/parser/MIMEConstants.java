package com.sun.xml.internal.ws.wsdl.parser;

import javax.swing.text.AbstractDocument;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/MIMEConstants.class */
interface MIMEConstants {
    public static final String NS_WSDL_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";
    public static final QName QNAME_CONTENT = new QName(NS_WSDL_MIME, AbstractDocument.ContentElementName);
    public static final QName QNAME_MULTIPART_RELATED = new QName(NS_WSDL_MIME, "multipartRelated");
    public static final QName QNAME_PART = new QName(NS_WSDL_MIME, "part");
    public static final QName QNAME_MIME_XML = new QName(NS_WSDL_MIME, "mimeXml");
}
