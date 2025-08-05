package javax.xml.soap;

import java.util.Locale;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;

/* loaded from: rt.jar:javax/xml/soap/SOAPBody.class */
public interface SOAPBody extends SOAPElement {
    SOAPFault addFault() throws SOAPException;

    SOAPFault addFault(Name name, String str, Locale locale) throws SOAPException;

    SOAPFault addFault(QName qName, String str, Locale locale) throws SOAPException;

    SOAPFault addFault(Name name, String str) throws SOAPException;

    SOAPFault addFault(QName qName, String str) throws SOAPException;

    boolean hasFault();

    SOAPFault getFault();

    SOAPBodyElement addBodyElement(Name name) throws SOAPException;

    SOAPBodyElement addBodyElement(QName qName) throws SOAPException;

    SOAPBodyElement addDocument(Document document) throws SOAPException;

    Document extractContentAsDocument() throws SOAPException;
}
