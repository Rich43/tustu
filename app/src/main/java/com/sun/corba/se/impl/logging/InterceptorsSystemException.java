package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.UNKNOWN;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/InterceptorsSystemException.class */
public class InterceptorsSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.InterceptorsSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new InterceptorsSystemException(logger);
        }
    };
    public static final int TYPE_OUT_OF_RANGE = 1398080289;
    public static final int NAME_NULL = 1398080290;
    public static final int RIR_INVALID_PRE_INIT = 1398080289;
    public static final int BAD_STATE1 = 1398080290;
    public static final int BAD_STATE2 = 1398080291;
    public static final int IOEXCEPTION_DURING_CANCEL_REQUEST = 1398080289;
    public static final int EXCEPTION_WAS_NULL = 1398080289;
    public static final int OBJECT_HAS_NO_DELEGATE = 1398080290;
    public static final int DELEGATE_NOT_CLIENTSUB = 1398080291;
    public static final int OBJECT_NOT_OBJECTIMPL = 1398080292;
    public static final int EXCEPTION_INVALID = 1398080293;
    public static final int REPLY_STATUS_NOT_INIT = 1398080294;
    public static final int EXCEPTION_IN_ARGUMENTS = 1398080295;
    public static final int EXCEPTION_IN_EXCEPTIONS = 1398080296;
    public static final int EXCEPTION_IN_CONTEXTS = 1398080297;
    public static final int EXCEPTION_WAS_NULL_2 = 1398080298;
    public static final int SERVANT_INVALID = 1398080299;
    public static final int CANT_POP_ONLY_PICURRENT = 1398080300;
    public static final int CANT_POP_ONLY_CURRENT_2 = 1398080301;
    public static final int PI_DSI_RESULT_IS_NULL = 1398080302;
    public static final int PI_DII_RESULT_IS_NULL = 1398080303;
    public static final int EXCEPTION_UNAVAILABLE = 1398080304;
    public static final int CLIENT_INFO_STACK_NULL = 1398080305;
    public static final int SERVER_INFO_STACK_NULL = 1398080306;
    public static final int MARK_AND_RESET_FAILED = 1398080307;
    public static final int SLOT_TABLE_INVARIANT = 1398080308;
    public static final int INTERCEPTOR_LIST_LOCKED = 1398080309;
    public static final int SORT_SIZE_MISMATCH = 1398080310;
    public static final int PI_ORB_NOT_POLICY_BASED = 1398080289;
    public static final int ORBINITINFO_INVALID = 1398080289;
    public static final int UNKNOWN_REQUEST_INVOKE = 1398080289;

    public InterceptorsSystemException(Logger logger) {
        super(logger);
    }

    public static InterceptorsSystemException get(ORB orb, String str) {
        return (InterceptorsSystemException) orb.getLogWrapper(str, "INTERCEPTORS", factory);
    }

    public static InterceptorsSystemException get(String str) {
        return (InterceptorsSystemException) ORB.staticGetLogWrapper(str, "INTERCEPTORS", factory);
    }

    public BAD_PARAM typeOutOfRange(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080289, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.typeOutOfRange", new Object[]{obj}, InterceptorsSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM typeOutOfRange(CompletionStatus completionStatus, Object obj) {
        return typeOutOfRange(completionStatus, null, obj);
    }

    public BAD_PARAM typeOutOfRange(Throwable th, Object obj) {
        return typeOutOfRange(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM typeOutOfRange(Object obj) {
        return typeOutOfRange(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM nameNull(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080290, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.nameNull", null, InterceptorsSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM nameNull(CompletionStatus completionStatus) {
        return nameNull(completionStatus, null);
    }

    public BAD_PARAM nameNull(Throwable th) {
        return nameNull(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM nameNull() {
        return nameNull(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER rirInvalidPreInit(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398080289, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.rirInvalidPreInit", null, InterceptorsSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER rirInvalidPreInit(CompletionStatus completionStatus) {
        return rirInvalidPreInit(completionStatus, null);
    }

    public BAD_INV_ORDER rirInvalidPreInit(Throwable th) {
        return rirInvalidPreInit(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER rirInvalidPreInit() {
        return rirInvalidPreInit(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badState1(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398080290, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.badState1", new Object[]{obj, obj2}, InterceptorsSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badState1(CompletionStatus completionStatus, Object obj, Object obj2) {
        return badState1(completionStatus, null, obj, obj2);
    }

    public BAD_INV_ORDER badState1(Throwable th, Object obj, Object obj2) {
        return badState1(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public BAD_INV_ORDER badState1(Object obj, Object obj2) {
        return badState1(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public BAD_INV_ORDER badState2(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398080291, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.badState2", new Object[]{obj, obj2, obj3}, InterceptorsSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badState2(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return badState2(completionStatus, null, obj, obj2, obj3);
    }

    public BAD_INV_ORDER badState2(Throwable th, Object obj, Object obj2, Object obj3) {
        return badState2(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public BAD_INV_ORDER badState2(Object obj, Object obj2, Object obj3) {
        return badState2(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public COMM_FAILURE ioexceptionDuringCancelRequest(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398080289, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.ioexceptionDuringCancelRequest", null, InterceptorsSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE ioexceptionDuringCancelRequest(CompletionStatus completionStatus) {
        return ioexceptionDuringCancelRequest(completionStatus, null);
    }

    public COMM_FAILURE ioexceptionDuringCancelRequest(Throwable th) {
        return ioexceptionDuringCancelRequest(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE ioexceptionDuringCancelRequest() {
        return ioexceptionDuringCancelRequest(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionWasNull(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080289, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionWasNull", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionWasNull(CompletionStatus completionStatus) {
        return exceptionWasNull(completionStatus, null);
    }

    public INTERNAL exceptionWasNull(Throwable th) {
        return exceptionWasNull(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionWasNull() {
        return exceptionWasNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL objectHasNoDelegate(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080290, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.objectHasNoDelegate", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL objectHasNoDelegate(CompletionStatus completionStatus) {
        return objectHasNoDelegate(completionStatus, null);
    }

    public INTERNAL objectHasNoDelegate(Throwable th) {
        return objectHasNoDelegate(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL objectHasNoDelegate() {
        return objectHasNoDelegate(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL delegateNotClientsub(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080291, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.delegateNotClientsub", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL delegateNotClientsub(CompletionStatus completionStatus) {
        return delegateNotClientsub(completionStatus, null);
    }

    public INTERNAL delegateNotClientsub(Throwable th) {
        return delegateNotClientsub(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL delegateNotClientsub() {
        return delegateNotClientsub(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL objectNotObjectimpl(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(OBJECT_NOT_OBJECTIMPL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.objectNotObjectimpl", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL objectNotObjectimpl(CompletionStatus completionStatus) {
        return objectNotObjectimpl(completionStatus, null);
    }

    public INTERNAL objectNotObjectimpl(Throwable th) {
        return objectNotObjectimpl(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL objectNotObjectimpl() {
        return objectNotObjectimpl(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionInvalid(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_INVALID, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionInvalid", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionInvalid(CompletionStatus completionStatus) {
        return exceptionInvalid(completionStatus, null);
    }

    public INTERNAL exceptionInvalid(Throwable th) {
        return exceptionInvalid(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionInvalid() {
        return exceptionInvalid(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL replyStatusNotInit(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(REPLY_STATUS_NOT_INIT, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.replyStatusNotInit", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL replyStatusNotInit(CompletionStatus completionStatus) {
        return replyStatusNotInit(completionStatus, null);
    }

    public INTERNAL replyStatusNotInit(Throwable th) {
        return replyStatusNotInit(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL replyStatusNotInit() {
        return replyStatusNotInit(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionInArguments(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_IN_ARGUMENTS, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionInArguments", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionInArguments(CompletionStatus completionStatus) {
        return exceptionInArguments(completionStatus, null);
    }

    public INTERNAL exceptionInArguments(Throwable th) {
        return exceptionInArguments(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionInArguments() {
        return exceptionInArguments(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionInExceptions(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_IN_EXCEPTIONS, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionInExceptions", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionInExceptions(CompletionStatus completionStatus) {
        return exceptionInExceptions(completionStatus, null);
    }

    public INTERNAL exceptionInExceptions(Throwable th) {
        return exceptionInExceptions(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionInExceptions() {
        return exceptionInExceptions(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionInContexts(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_IN_CONTEXTS, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionInContexts", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionInContexts(CompletionStatus completionStatus) {
        return exceptionInContexts(completionStatus, null);
    }

    public INTERNAL exceptionInContexts(Throwable th) {
        return exceptionInContexts(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionInContexts() {
        return exceptionInContexts(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionWasNull2(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_WAS_NULL_2, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionWasNull2", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionWasNull2(CompletionStatus completionStatus) {
        return exceptionWasNull2(completionStatus, null);
    }

    public INTERNAL exceptionWasNull2(Throwable th) {
        return exceptionWasNull2(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionWasNull2() {
        return exceptionWasNull2(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL servantInvalid(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(SERVANT_INVALID, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.servantInvalid", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL servantInvalid(CompletionStatus completionStatus) {
        return servantInvalid(completionStatus, null);
    }

    public INTERNAL servantInvalid(Throwable th) {
        return servantInvalid(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL servantInvalid() {
        return servantInvalid(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL cantPopOnlyPicurrent(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(CANT_POP_ONLY_PICURRENT, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.cantPopOnlyPicurrent", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL cantPopOnlyPicurrent(CompletionStatus completionStatus) {
        return cantPopOnlyPicurrent(completionStatus, null);
    }

    public INTERNAL cantPopOnlyPicurrent(Throwable th) {
        return cantPopOnlyPicurrent(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL cantPopOnlyPicurrent() {
        return cantPopOnlyPicurrent(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL cantPopOnlyCurrent2(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(CANT_POP_ONLY_CURRENT_2, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.cantPopOnlyCurrent2", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL cantPopOnlyCurrent2(CompletionStatus completionStatus) {
        return cantPopOnlyCurrent2(completionStatus, null);
    }

    public INTERNAL cantPopOnlyCurrent2(Throwable th) {
        return cantPopOnlyCurrent2(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL cantPopOnlyCurrent2() {
        return cantPopOnlyCurrent2(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL piDsiResultIsNull(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(PI_DSI_RESULT_IS_NULL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.piDsiResultIsNull", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL piDsiResultIsNull(CompletionStatus completionStatus) {
        return piDsiResultIsNull(completionStatus, null);
    }

    public INTERNAL piDsiResultIsNull(Throwable th) {
        return piDsiResultIsNull(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL piDsiResultIsNull() {
        return piDsiResultIsNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL piDiiResultIsNull(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(PI_DII_RESULT_IS_NULL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.piDiiResultIsNull", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL piDiiResultIsNull(CompletionStatus completionStatus) {
        return piDiiResultIsNull(completionStatus, null);
    }

    public INTERNAL piDiiResultIsNull(Throwable th) {
        return piDiiResultIsNull(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL piDiiResultIsNull() {
        return piDiiResultIsNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionUnavailable(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_UNAVAILABLE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.exceptionUnavailable", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionUnavailable(CompletionStatus completionStatus) {
        return exceptionUnavailable(completionStatus, null);
    }

    public INTERNAL exceptionUnavailable(Throwable th) {
        return exceptionUnavailable(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionUnavailable() {
        return exceptionUnavailable(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL clientInfoStackNull(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(CLIENT_INFO_STACK_NULL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.clientInfoStackNull", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL clientInfoStackNull(CompletionStatus completionStatus) {
        return clientInfoStackNull(completionStatus, null);
    }

    public INTERNAL clientInfoStackNull(Throwable th) {
        return clientInfoStackNull(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL clientInfoStackNull() {
        return clientInfoStackNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL serverInfoStackNull(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(SERVER_INFO_STACK_NULL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.serverInfoStackNull", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL serverInfoStackNull(CompletionStatus completionStatus) {
        return serverInfoStackNull(completionStatus, null);
    }

    public INTERNAL serverInfoStackNull(Throwable th) {
        return serverInfoStackNull(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL serverInfoStackNull() {
        return serverInfoStackNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL markAndResetFailed(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(MARK_AND_RESET_FAILED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.markAndResetFailed", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL markAndResetFailed(CompletionStatus completionStatus) {
        return markAndResetFailed(completionStatus, null);
    }

    public INTERNAL markAndResetFailed(Throwable th) {
        return markAndResetFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL markAndResetFailed() {
        return markAndResetFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL slotTableInvariant(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(SLOT_TABLE_INVARIANT, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.slotTableInvariant", new Object[]{obj, obj2}, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL slotTableInvariant(CompletionStatus completionStatus, Object obj, Object obj2) {
        return slotTableInvariant(completionStatus, null, obj, obj2);
    }

    public INTERNAL slotTableInvariant(Throwable th, Object obj, Object obj2) {
        return slotTableInvariant(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL slotTableInvariant(Object obj, Object obj2) {
        return slotTableInvariant(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL interceptorListLocked(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INTERCEPTOR_LIST_LOCKED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.interceptorListLocked", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL interceptorListLocked(CompletionStatus completionStatus) {
        return interceptorListLocked(completionStatus, null);
    }

    public INTERNAL interceptorListLocked(Throwable th) {
        return interceptorListLocked(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL interceptorListLocked() {
        return interceptorListLocked(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL sortSizeMismatch(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(SORT_SIZE_MISMATCH, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.sortSizeMismatch", null, InterceptorsSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL sortSizeMismatch(CompletionStatus completionStatus) {
        return sortSizeMismatch(completionStatus, null);
    }

    public INTERNAL sortSizeMismatch(Throwable th) {
        return sortSizeMismatch(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL sortSizeMismatch() {
        return sortSizeMismatch(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT piOrbNotPolicyBased(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398080289, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "INTERCEPTORS.piOrbNotPolicyBased", null, InterceptorsSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT piOrbNotPolicyBased(CompletionStatus completionStatus) {
        return piOrbNotPolicyBased(completionStatus, null);
    }

    public NO_IMPLEMENT piOrbNotPolicyBased(Throwable th) {
        return piOrbNotPolicyBased(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT piOrbNotPolicyBased() {
        return piOrbNotPolicyBased(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST orbinitinfoInvalid(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398080289, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "INTERCEPTORS.orbinitinfoInvalid", null, InterceptorsSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST orbinitinfoInvalid(CompletionStatus completionStatus) {
        return orbinitinfoInvalid(completionStatus, null);
    }

    public OBJECT_NOT_EXIST orbinitinfoInvalid(Throwable th) {
        return orbinitinfoInvalid(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST orbinitinfoInvalid() {
        return orbinitinfoInvalid(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownRequestInvoke(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080289, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "INTERCEPTORS.unknownRequestInvoke", null, InterceptorsSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownRequestInvoke(CompletionStatus completionStatus) {
        return unknownRequestInvoke(completionStatus, null);
    }

    public UNKNOWN unknownRequestInvoke(Throwable th) {
        return unknownRequestInvoke(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownRequestInvoke() {
        return unknownRequestInvoke(CompletionStatus.COMPLETED_NO, null);
    }
}
