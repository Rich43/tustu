package javax.xml.soap;

/* loaded from: rt.jar:javax/xml/soap/SOAPConnection.class */
public abstract class SOAPConnection {
    public abstract SOAPMessage call(SOAPMessage sOAPMessage, Object obj) throws SOAPException;

    public abstract void close() throws SOAPException;

    public SOAPMessage get(Object to) throws SOAPException {
        throw new UnsupportedOperationException("All subclasses of SOAPConnection must override get()");
    }
}
