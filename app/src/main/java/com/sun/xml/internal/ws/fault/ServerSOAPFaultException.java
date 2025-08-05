package com.sun.xml.internal.ws.fault;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/fault/ServerSOAPFaultException.class */
public class ServerSOAPFaultException extends SOAPFaultException {
    public ServerSOAPFaultException(SOAPFault soapFault) {
        super(soapFault);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return "Client received SOAP Fault from server: " + super.getMessage() + " Please see the server log to find more detail regarding exact cause of the failure.";
    }
}
