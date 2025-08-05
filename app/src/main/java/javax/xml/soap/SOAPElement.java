package javax.xml.soap;

import java.util.Iterator;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/* loaded from: rt.jar:javax/xml/soap/SOAPElement.class */
public interface SOAPElement extends Node, Element {
    SOAPElement addChildElement(Name name) throws SOAPException;

    SOAPElement addChildElement(QName qName) throws SOAPException;

    SOAPElement addChildElement(String str) throws SOAPException;

    SOAPElement addChildElement(String str, String str2) throws SOAPException;

    SOAPElement addChildElement(String str, String str2, String str3) throws SOAPException;

    SOAPElement addChildElement(SOAPElement sOAPElement) throws SOAPException;

    void removeContents();

    SOAPElement addTextNode(String str) throws SOAPException;

    SOAPElement addAttribute(Name name, String str) throws SOAPException;

    SOAPElement addAttribute(QName qName, String str) throws SOAPException;

    SOAPElement addNamespaceDeclaration(String str, String str2) throws SOAPException;

    String getAttributeValue(Name name);

    String getAttributeValue(QName qName);

    Iterator getAllAttributes();

    Iterator getAllAttributesAsQNames();

    String getNamespaceURI(String str);

    Iterator getNamespacePrefixes();

    Iterator getVisibleNamespacePrefixes();

    QName createQName(String str, String str2) throws SOAPException;

    Name getElementName();

    QName getElementQName();

    SOAPElement setElementQName(QName qName) throws SOAPException;

    boolean removeAttribute(Name name);

    boolean removeAttribute(QName qName);

    boolean removeNamespaceDeclaration(String str);

    Iterator getChildElements();

    Iterator getChildElements(Name name);

    Iterator getChildElements(QName qName);

    void setEncodingStyle(String str) throws SOAPException;

    String getEncodingStyle();
}
