package com.sun.corba.se.impl.corba;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import org.omg.CORBA.Any;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ServerRequest;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/ServerRequestImpl.class */
public class ServerRequestImpl extends ServerRequest {
    private ORB _orb;
    private ORBUtilSystemException _wrapper;
    private String _opName;
    private Context _ctx;
    private InputStream _ins;
    private NVList _arguments = null;
    private boolean _paramsCalled = false;
    private boolean _resultSet = false;
    private boolean _exceptionSet = false;
    private Any _resultAny = null;
    private Any _exception = null;

    public ServerRequestImpl(CorbaMessageMediator corbaMessageMediator, ORB orb) {
        this._orb = null;
        this._wrapper = null;
        this._opName = null;
        this._ctx = null;
        this._ins = null;
        this._opName = corbaMessageMediator.getOperationName();
        this._ins = (InputStream) corbaMessageMediator.getInputObject();
        this._ctx = null;
        this._orb = orb;
        this._wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.OA_INVOCATION);
    }

    @Override // org.omg.CORBA.ServerRequest
    public String operation() {
        return this._opName;
    }

    @Override // org.omg.CORBA.ServerRequest
    public void arguments(NVList nVList) {
        if (this._paramsCalled) {
            throw this._wrapper.argumentsCalledMultiple();
        }
        if (this._exceptionSet) {
            throw this._wrapper.argumentsCalledAfterException();
        }
        if (nVList == null) {
            throw this._wrapper.argumentsCalledNullArgs();
        }
        this._paramsCalled = true;
        for (int i2 = 0; i2 < nVList.count(); i2++) {
            try {
                NamedValue namedValueItem = nVList.item(i2);
                try {
                    if (namedValueItem.flags() == 1 || namedValueItem.flags() == 3) {
                        namedValueItem.value().read_value(this._ins, namedValueItem.value().type());
                    }
                } catch (Exception e2) {
                    throw this._wrapper.badArgumentsNvlist(e2);
                }
            } catch (Bounds e3) {
                throw this._wrapper.boundsCannotOccur(e3);
            }
        }
        this._arguments = nVList;
        this._orb.getPIHandler().setServerPIInfo(this._arguments);
        this._orb.getPIHandler().invokeServerPIIntermediatePoint();
    }

    @Override // org.omg.CORBA.ServerRequest
    public void set_result(Any any) {
        if (!this._paramsCalled) {
            throw this._wrapper.argumentsNotCalled();
        }
        if (this._resultSet) {
            throw this._wrapper.setResultCalledMultiple();
        }
        if (this._exceptionSet) {
            throw this._wrapper.setResultAfterException();
        }
        if (any == null) {
            throw this._wrapper.setResultCalledNullArgs();
        }
        this._resultAny = any;
        this._resultSet = true;
        this._orb.getPIHandler().setServerPIInfo(this._resultAny);
    }

    @Override // org.omg.CORBA.ServerRequest
    public void set_exception(Any any) {
        if (any == null) {
            throw this._wrapper.setExceptionCalledNullArgs();
        }
        if (any.type().kind() != TCKind.tk_except) {
            throw this._wrapper.setExceptionCalledBadType();
        }
        this._exception = any;
        this._orb.getPIHandler().setServerPIExceptionInfo(this._exception);
        if (!this._exceptionSet && !this._paramsCalled) {
            this._orb.getPIHandler().invokeServerPIIntermediatePoint();
        }
        this._exceptionSet = true;
    }

    public Any checkResultCalled() {
        if (this._paramsCalled && this._resultSet) {
            return null;
        }
        if (this._paramsCalled && !this._resultSet && !this._exceptionSet) {
            try {
                TypeCode typeCode = this._orb.get_primitive_tc(TCKind.tk_void);
                this._resultAny = this._orb.create_any();
                this._resultAny.type(typeCode);
                this._resultSet = true;
                return null;
            } catch (Exception e2) {
                throw this._wrapper.dsiResultException(CompletionStatus.COMPLETED_MAYBE, e2);
            }
        }
        if (this._exceptionSet) {
            return this._exception;
        }
        throw this._wrapper.dsimethodNotcalled(CompletionStatus.COMPLETED_MAYBE);
    }

    public void marshalReplyParams(OutputStream outputStream) {
        this._resultAny.write_value(outputStream);
        NamedValue namedValueItem = null;
        for (int i2 = 0; i2 < this._arguments.count(); i2++) {
            try {
                namedValueItem = this._arguments.item(i2);
            } catch (Bounds e2) {
            }
            if (namedValueItem.flags() == 2 || namedValueItem.flags() == 3) {
                namedValueItem.value().write_value(outputStream);
            }
        }
    }

    @Override // org.omg.CORBA.ServerRequest
    public Context ctx() {
        if (!this._paramsCalled || this._resultSet || this._exceptionSet) {
            throw this._wrapper.contextCalledOutOfOrder();
        }
        throw this._wrapper.contextNotImplemented();
    }
}
