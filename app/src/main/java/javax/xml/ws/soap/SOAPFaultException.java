package javax.xml.ws.soap;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.ProtocolException;

/* loaded from: rt.jar:javax/xml/ws/soap/SOAPFaultException.class */
public class SOAPFaultException extends ProtocolException {
    private SOAPFault fault;

    public SOAPFaultException(SOAPFault fault) {
        super(fault.getFaultString());
        this.fault = fault;
    }

    public SOAPFault getFault() {
        return this.fault;
    }
}
