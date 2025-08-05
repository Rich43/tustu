package com.sun.corba.se.impl.corba;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import org.omg.CORBA.Any;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.Environment;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.WrongTransaction;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/RequestImpl.class */
public class RequestImpl extends Request {
    protected Object _target;
    protected String _opName;
    protected NVList _arguments;
    protected ExceptionList _exceptions;
    private NamedValue _result;
    protected Environment _env;
    private Context _ctx;
    private ContextList _ctxList;
    protected ORB _orb;
    private ORBUtilSystemException _wrapper;
    private int[] _paramCodes;
    private long[] _paramLongs;
    private Object[] _paramObjects;
    protected boolean _isOneWay = false;
    protected boolean gotResponse = false;

    public RequestImpl(ORB orb, Object object, Context context, String str, NVList nVList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList) {
        this._orb = orb;
        this._wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.OA_INVOCATION);
        this._target = object;
        this._ctx = context;
        this._opName = str;
        if (nVList == null) {
            this._arguments = new NVListImpl(this._orb);
        } else {
            this._arguments = nVList;
        }
        this._result = namedValue;
        if (exceptionList == null) {
            this._exceptions = new ExceptionListImpl();
        } else {
            this._exceptions = exceptionList;
        }
        if (contextList == null) {
            this._ctxList = new ContextListImpl(this._orb);
        } else {
            this._ctxList = contextList;
        }
        this._env = new EnvironmentImpl();
    }

    @Override // org.omg.CORBA.Request
    public Object target() {
        return this._target;
    }

    @Override // org.omg.CORBA.Request
    public String operation() {
        return this._opName;
    }

    @Override // org.omg.CORBA.Request
    public NVList arguments() {
        return this._arguments;
    }

    @Override // org.omg.CORBA.Request
    public NamedValue result() {
        return this._result;
    }

    @Override // org.omg.CORBA.Request
    public Environment env() {
        return this._env;
    }

    @Override // org.omg.CORBA.Request
    public ExceptionList exceptions() {
        return this._exceptions;
    }

    @Override // org.omg.CORBA.Request
    public ContextList contexts() {
        return this._ctxList;
    }

    @Override // org.omg.CORBA.Request
    public synchronized Context ctx() {
        if (this._ctx == null) {
            this._ctx = new ContextImpl(this._orb);
        }
        return this._ctx;
    }

    @Override // org.omg.CORBA.Request
    public synchronized void ctx(Context context) {
        this._ctx = context;
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any add_in_arg() {
        return this._arguments.add(1).value();
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any add_named_in_arg(String str) {
        return this._arguments.add_item(str, 1).value();
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any add_inout_arg() {
        return this._arguments.add(3).value();
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any add_named_inout_arg(String str) {
        return this._arguments.add_item(str, 3).value();
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any add_out_arg() {
        return this._arguments.add(2).value();
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any add_named_out_arg(String str) {
        return this._arguments.add_item(str, 2).value();
    }

    @Override // org.omg.CORBA.Request
    public synchronized void set_return_type(TypeCode typeCode) {
        if (this._result == null) {
            this._result = new NamedValueImpl(this._orb);
        }
        this._result.value().type(typeCode);
    }

    @Override // org.omg.CORBA.Request
    public synchronized Any return_value() {
        if (this._result == null) {
            this._result = new NamedValueImpl(this._orb);
        }
        return this._result.value();
    }

    public synchronized void add_exception(TypeCode typeCode) {
        this._exceptions.add(typeCode);
    }

    @Override // org.omg.CORBA.Request
    public synchronized void invoke() {
        doInvocation();
    }

    @Override // org.omg.CORBA.Request
    public synchronized void send_oneway() {
        this._isOneWay = true;
        doInvocation();
    }

    @Override // org.omg.CORBA.Request
    public synchronized void send_deferred() {
        new Thread(new AsynchInvoke(this._orb, this, false)).start();
    }

    @Override // org.omg.CORBA.Request
    public synchronized boolean poll_response() {
        return this.gotResponse;
    }

    @Override // org.omg.CORBA.Request
    public synchronized void get_response() throws WrongTransaction {
        while (!this.gotResponse) {
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
    }

    protected void doInvocation() {
        Delegate delegate = StubAdapter.getDelegate(this._target);
        this._orb.getPIHandler().initiateClientPIRequest(true);
        this._orb.getPIHandler().setClientPIInfo(this);
        try {
            try {
                try {
                    OutputStream outputStreamRequest = delegate.request(null, this._opName, !this._isOneWay);
                    for (int i2 = 0; i2 < this._arguments.count(); i2++) {
                        try {
                            NamedValue namedValueItem = this._arguments.item(i2);
                            switch (namedValueItem.flags()) {
                                case 1:
                                    namedValueItem.value().write_value(outputStreamRequest);
                                    break;
                                case 3:
                                    namedValueItem.value().write_value(outputStreamRequest);
                                    break;
                            }
                        } catch (Bounds e2) {
                            throw this._wrapper.boundsErrorInDiiRequest(e2);
                        }
                    }
                    delegate.releaseReply(null, delegate.invoke(null, outputStreamRequest));
                } catch (ApplicationException e3) {
                    delegate.releaseReply(null, null);
                } catch (RemarshalException e4) {
                    doInvocation();
                    delegate.releaseReply(null, null);
                }
            } catch (SystemException e5) {
                this._env.exception(e5);
                throw e5;
            }
        } catch (Throwable th) {
            delegate.releaseReply(null, null);
            throw th;
        }
    }

    public void unmarshalReply(InputStream inputStream) throws MARSHAL {
        if (this._result != null) {
            Any anyValue = this._result.value();
            TypeCode typeCodeType = anyValue.type();
            if (typeCodeType.kind().value() != 1) {
                anyValue.read_value(inputStream, typeCodeType);
            }
        }
        for (int i2 = 0; i2 < this._arguments.count(); i2++) {
            try {
                NamedValue namedValueItem = this._arguments.item(i2);
                switch (namedValueItem.flags()) {
                    case 2:
                    case 3:
                        Any anyValue2 = namedValueItem.value();
                        anyValue2.read_value(inputStream, anyValue2.type());
                        break;
                }
            } catch (Bounds e2) {
                return;
            }
        }
    }
}
