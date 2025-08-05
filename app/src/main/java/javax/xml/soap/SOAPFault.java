package javax.xml.soap;

import java.util.Iterator;
import java.util.Locale;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:javax/xml/soap/SOAPFault.class */
public interface SOAPFault extends SOAPBodyElement {
    void setFaultCode(Name name) throws SOAPException;

    void setFaultCode(QName qName) throws SOAPException;

    void setFaultCode(String str) throws SOAPException;

    Name getFaultCodeAsName();

    QName getFaultCodeAsQName();

    Iterator getFaultSubcodes();

    void removeAllFaultSubcodes();

    void appendFaultSubcode(QName qName) throws SOAPException;

    String getFaultCode();

    void setFaultActor(String str) throws SOAPException;

    String getFaultActor();

    void setFaultString(String str) throws SOAPException;

    void setFaultString(String str, Locale locale) throws SOAPException;

    String getFaultString();

    Locale getFaultStringLocale();

    boolean hasDetail();

    Detail getDetail();

    Detail addDetail() throws SOAPException;

    Iterator getFaultReasonLocales() throws SOAPException;

    Iterator getFaultReasonTexts() throws SOAPException;

    String getFaultReasonText(Locale locale) throws SOAPException;

    void addFaultReasonText(String str, Locale locale) throws SOAPException;

    String getFaultNode();

    void setFaultNode(String str) throws SOAPException;

    String getFaultRole();

    void setFaultRole(String str) throws SOAPException;
}
