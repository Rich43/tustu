package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.encoding.MarshalInputStream;
import com.sun.corba.se.impl.encoding.MarshalOutputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.Iterator;
import java.util.Set;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/BootstrapServerRequestDispatcher.class */
public class BootstrapServerRequestDispatcher implements CorbaServerRequestDispatcher {
    private ORB orb;
    ORBUtilSystemException wrapper;
    private static final boolean debug = false;

    public BootstrapServerRequestDispatcher(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    @Override // com.sun.corba.se.pept.protocol.ServerRequestDispatcher
    public void dispatch(MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        try {
            MarshalInputStream marshalInputStream = (MarshalInputStream) corbaMessageMediator.getInputObject();
            String operationName = corbaMessageMediator.getOperationName();
            MarshalOutputStream marshalOutputStream = (MarshalOutputStream) corbaMessageMediator.getProtocolHandler().createResponse(corbaMessageMediator, null).getOutputObject();
            if (operationName.equals("get")) {
                marshalOutputStream.write_Object(this.orb.getLocalResolver().resolve(marshalInputStream.read_string()));
            } else if (operationName.equals(SchemaSymbols.ATTVAL_LIST)) {
                Set list = this.orb.getLocalResolver().list();
                marshalOutputStream.write_long(list.size());
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    marshalOutputStream.write_string((String) it.next());
                }
            } else {
                throw this.wrapper.illegalBootstrapOperation(operationName);
            }
        } catch (SystemException e2) {
            corbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(corbaMessageMediator, e2, null);
        } catch (RuntimeException e3) {
            corbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(corbaMessageMediator, this.wrapper.bootstrapRuntimeException(e3), null);
        } catch (Exception e4) {
            corbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(corbaMessageMediator, this.wrapper.bootstrapException(e4), null);
        }
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher
    public IOR locate(ObjectKey objectKey) {
        return null;
    }

    public int getId() {
        throw this.wrapper.genericNoImpl();
    }
}
