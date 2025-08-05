package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.legacy.connection.Connection;
import com.sun.corba.se.spi.legacy.interceptor.RequestInfoExt;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.ParameterMode;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.InputStream;
import org.omg.Dynamic.Parameter;
import org.omg.IOP.ServiceContext;
import org.omg.IOP.ServiceContextHelper;
import org.omg.PortableInterceptor.ForwardRequest;
import org.omg.PortableInterceptor.InvalidSlot;
import org.omg.PortableInterceptor.RequestInfo;
import sun.corba.OutputStreamFactory;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/RequestInfoImpl.class */
public abstract class RequestInfoImpl extends LocalObject implements RequestInfo, RequestInfoExt {
    protected ORB myORB;
    protected InterceptorsSystemException wrapper;
    protected OMGSystemException stdWrapper;
    protected int startingPointCall;
    protected int intermediatePointCall;
    protected int endingPointCall;
    protected static final short UNINITIALIZED = -1;
    protected int currentExecutionPoint;
    protected static final int EXECUTION_POINT_STARTING = 0;
    protected static final int EXECUTION_POINT_INTERMEDIATE = 1;
    protected static final int EXECUTION_POINT_ENDING = 2;
    protected boolean alreadyExecuted;
    protected Connection connection;
    protected ServiceContexts serviceContexts;
    protected ForwardRequest forwardRequest;
    protected IOR forwardRequestIOR;
    protected SlotTable slotTable;
    protected Exception exception;
    protected static final int MID_REQUEST_ID = 0;
    protected static final int MID_OPERATION = 1;
    protected static final int MID_ARGUMENTS = 2;
    protected static final int MID_EXCEPTIONS = 3;
    protected static final int MID_CONTEXTS = 4;
    protected static final int MID_OPERATION_CONTEXT = 5;
    protected static final int MID_RESULT = 6;
    protected static final int MID_RESPONSE_EXPECTED = 7;
    protected static final int MID_SYNC_SCOPE = 8;
    protected static final int MID_REPLY_STATUS = 9;
    protected static final int MID_FORWARD_REFERENCE = 10;
    protected static final int MID_GET_SLOT = 11;
    protected static final int MID_GET_REQUEST_SERVICE_CONTEXT = 12;
    protected static final int MID_GET_REPLY_SERVICE_CONTEXT = 13;
    protected static final int MID_RI_LAST = 13;
    protected int flowStackIndex = 0;
    protected short replyStatus = -1;

    public abstract int request_id();

    public abstract String operation();

    public abstract Parameter[] arguments();

    public abstract TypeCode[] exceptions();

    public abstract String[] contexts();

    public abstract String[] operation_context();

    public abstract Any result();

    public abstract boolean response_expected();

    public abstract Object forward_reference();

    public abstract ServiceContext get_request_service_context(int i2);

    public abstract ServiceContext get_reply_service_context(int i2);

    protected abstract void checkAccess(int i2) throws BAD_INV_ORDER;

    void reset() {
        this.flowStackIndex = 0;
        this.startingPointCall = 0;
        this.intermediatePointCall = 0;
        this.endingPointCall = 0;
        setReplyStatus((short) -1);
        this.currentExecutionPoint = 0;
        this.alreadyExecuted = false;
        this.connection = null;
        this.serviceContexts = null;
        this.forwardRequest = null;
        this.forwardRequestIOR = null;
        this.exception = null;
    }

    public RequestInfoImpl(ORB orb) {
        this.myORB = orb;
        this.wrapper = InterceptorsSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.stdWrapper = OMGSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.slotTable = ((PICurrent) orb.getPIHandler().getPICurrent()).getSlotTable();
    }

    @Override // org.omg.PortableInterceptor.RequestInfoOperations
    public short sync_scope() throws BAD_INV_ORDER {
        checkAccess(8);
        return (short) 1;
    }

    @Override // org.omg.PortableInterceptor.RequestInfoOperations
    public short reply_status() throws BAD_INV_ORDER {
        checkAccess(9);
        return this.replyStatus;
    }

    @Override // org.omg.PortableInterceptor.RequestInfoOperations
    public Any get_slot(int i2) throws InvalidSlot {
        return this.slotTable.get_slot(i2);
    }

    public Connection connection() {
        return this.connection;
    }

    private void insertApplicationException(ApplicationException applicationException, Any any) throws UNKNOWN {
        try {
            Method method = SharedSecrets.getJavaCorbaAccess().loadClass(RepositoryId.cache.getId(applicationException.getId()).getClassName() + "Helper").getMethod("read", InputStream.class);
            InputStream inputStream = applicationException.getInputStream();
            inputStream.mark(0);
            try {
                UserException userException = (UserException) method.invoke(null, inputStream);
                try {
                    inputStream.reset();
                    insertUserException(userException, any);
                } catch (IOException e2) {
                    throw this.wrapper.markAndResetFailed(e2);
                }
            } catch (Throwable th) {
                try {
                    inputStream.reset();
                    throw th;
                } catch (IOException e3) {
                    throw this.wrapper.markAndResetFailed(e3);
                }
            }
        } catch (ClassNotFoundException e4) {
            throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e4);
        } catch (IllegalAccessException e5) {
            throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e5);
        } catch (IllegalArgumentException e6) {
            throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e6);
        } catch (NoSuchMethodException e7) {
            throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e7);
        } catch (SecurityException e8) {
            throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e8);
        } catch (InvocationTargetException e9) {
            throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e9);
        }
    }

    private void insertUserException(UserException userException, Any any) throws UNKNOWN {
        if (userException != null) {
            try {
                Class<?> cls = userException.getClass();
                SharedSecrets.getJavaCorbaAccess().loadClass(cls.getName() + "Helper").getMethod("insert", Any.class, cls).invoke(null, any, userException);
            } catch (ClassNotFoundException e2) {
                throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e2);
            } catch (IllegalAccessException e3) {
                throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e3);
            } catch (IllegalArgumentException e4) {
                throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e4);
            } catch (NoSuchMethodException e5) {
                throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e5);
            } catch (SecurityException e6) {
                throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e6);
            } catch (InvocationTargetException e7) {
                throw this.stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e7);
            }
        }
    }

    protected Parameter[] nvListToParameterArray(NVList nVList) {
        int iCount = nVList.count();
        Parameter[] parameterArr = new Parameter[iCount];
        for (int i2 = 0; i2 < iCount; i2++) {
            try {
                parameterArr[i2] = new Parameter();
                NamedValue namedValueItem = nVList.item(i2);
                parameterArr[i2].argument = namedValueItem.value();
                parameterArr[i2].mode = ParameterMode.from_int(namedValueItem.flags() - 1);
            } catch (Exception e2) {
                throw this.wrapper.exceptionInArguments(e2);
            }
        }
        return parameterArr;
    }

    protected Any exceptionToAny(Exception exc) throws MARSHAL {
        Any anyCreate_any = this.myORB.create_any();
        if (exc == null) {
            throw this.wrapper.exceptionWasNull2();
        }
        if (exc instanceof SystemException) {
            ORBUtility.insertSystemException((SystemException) exc, anyCreate_any);
        } else if (exc instanceof ApplicationException) {
            try {
                insertApplicationException((ApplicationException) exc, anyCreate_any);
            } catch (UNKNOWN e2) {
                ORBUtility.insertSystemException(e2, anyCreate_any);
            }
        } else if (exc instanceof UserException) {
            try {
                insertUserException((UserException) exc, anyCreate_any);
            } catch (UNKNOWN e3) {
                ORBUtility.insertSystemException(e3, anyCreate_any);
            }
        }
        return anyCreate_any;
    }

    protected ServiceContext getServiceContext(HashMap map, ServiceContexts serviceContexts, int i2) throws SystemException {
        Integer num = new Integer(i2);
        ServiceContext serviceContext = (ServiceContext) map.get(num);
        if (serviceContext == null) {
            com.sun.corba.se.spi.servicecontext.ServiceContext serviceContext2 = serviceContexts.get(i2);
            if (serviceContext2 == null) {
                throw this.stdWrapper.invalidServiceContextId();
            }
            EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.myORB);
            serviceContext2.write(encapsOutputStreamNewEncapsOutputStream, GIOPVersion.V1_2);
            serviceContext = ServiceContextHelper.read(encapsOutputStreamNewEncapsOutputStream.create_input_stream());
            map.put(num, serviceContext);
        }
        return serviceContext;
    }

    protected void addServiceContext(HashMap map, ServiceContexts serviceContexts, ServiceContext serviceContext, boolean z2) {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.myORB);
        ServiceContextHelper.write(encapsOutputStreamNewEncapsOutputStream, serviceContext);
        InputStream inputStreamCreate_input_stream = encapsOutputStreamNewEncapsOutputStream.create_input_stream();
        UnknownServiceContext unknownServiceContext = new UnknownServiceContext(inputStreamCreate_input_stream.read_long(), (org.omg.CORBA_2_3.portable.InputStream) inputStreamCreate_input_stream);
        int id = unknownServiceContext.getId();
        if (serviceContexts.get(id) != null) {
            if (z2) {
                serviceContexts.delete(id);
            } else {
                throw this.stdWrapper.serviceContextAddFailed(new Integer(id));
            }
        }
        serviceContexts.put(unknownServiceContext);
        map.put(new Integer(id), serviceContext);
    }

    protected void setFlowStackIndex(int i2) {
        this.flowStackIndex = i2;
    }

    protected int getFlowStackIndex() {
        return this.flowStackIndex;
    }

    protected void setEndingPointCall(int i2) {
        this.endingPointCall = i2;
    }

    protected int getEndingPointCall() {
        return this.endingPointCall;
    }

    protected void setIntermediatePointCall(int i2) {
        this.intermediatePointCall = i2;
    }

    protected int getIntermediatePointCall() {
        return this.intermediatePointCall;
    }

    protected void setStartingPointCall(int i2) {
        this.startingPointCall = i2;
    }

    protected int getStartingPointCall() {
        return this.startingPointCall;
    }

    protected boolean getAlreadyExecuted() {
        return this.alreadyExecuted;
    }

    protected void setAlreadyExecuted(boolean z2) {
        this.alreadyExecuted = z2;
    }

    protected void setReplyStatus(short s2) {
        this.replyStatus = s2;
    }

    protected short getReplyStatus() {
        return this.replyStatus;
    }

    protected void setForwardRequest(ForwardRequest forwardRequest) {
        this.forwardRequest = forwardRequest;
        this.forwardRequestIOR = null;
    }

    protected void setForwardRequest(IOR ior) {
        this.forwardRequestIOR = ior;
        this.forwardRequest = null;
    }

    protected ForwardRequest getForwardRequestException() {
        if (this.forwardRequest == null && this.forwardRequestIOR != null) {
            this.forwardRequest = new ForwardRequest(iorToObject(this.forwardRequestIOR));
        }
        return this.forwardRequest;
    }

    protected IOR getForwardRequestIOR() {
        if (this.forwardRequestIOR == null && this.forwardRequest != null) {
            this.forwardRequestIOR = ORBUtility.getIOR(this.forwardRequest.forward);
        }
        return this.forwardRequestIOR;
    }

    protected void setException(Exception exc) {
        this.exception = exc;
    }

    Exception getException() {
        return this.exception;
    }

    protected void setCurrentExecutionPoint(int i2) {
        this.currentExecutionPoint = i2;
    }

    void setSlotTable(SlotTable slotTable) {
        this.slotTable = slotTable;
    }

    protected Object iorToObject(IOR ior) {
        return ORBUtility.makeObjectReference(ior);
    }
}
