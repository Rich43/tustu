package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SOAPHeaderElement.class */
public interface SOAPHeaderElement extends SOAPElement {
    void setActor(String str);

    void setRole(String str) throws SOAPException;

    String getActor();

    String getRole();

    void setMustUnderstand(boolean z2);

    boolean getMustUnderstand();

    void setRelay(boolean z2) throws SOAPException;

    boolean getRelay();
}
