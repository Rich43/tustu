package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SOAPEnvelope.class */
public interface SOAPEnvelope extends SOAPElement {
    Name createName(String str, String str2, String str3) throws SOAPException;

    Name createName(String str) throws SOAPException;

    SOAPHeader getHeader() throws SOAPException;

    SOAPBody getBody() throws SOAPException;

    SOAPHeader addHeader() throws SOAPException;

    SOAPBody addBody() throws SOAPException;
}
