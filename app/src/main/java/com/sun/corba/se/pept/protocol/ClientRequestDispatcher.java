package com.sun.corba.se.pept.protocol;

import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.transport.ContactInfo;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/pept/protocol/ClientRequestDispatcher.class */
public interface ClientRequestDispatcher {
    OutputObject beginRequest(Object obj, String str, boolean z2, ContactInfo contactInfo);

    InputObject marshalingComplete(Object obj, OutputObject outputObject) throws ApplicationException, RemarshalException;

    void endRequest(Broker broker, Object obj, InputObject inputObject);
}
