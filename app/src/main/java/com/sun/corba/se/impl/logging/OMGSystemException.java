package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_CONTEXT;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.BAD_TYPECODE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.IMP_LIMIT;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.INTF_REPOS;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.INV_POLICY;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NO_RESOURCES;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CORBA.TRANSIENT;
import org.omg.CORBA.UNKNOWN;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/OMGSystemException.class */
public class OMGSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.OMGSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new OMGSystemException(logger);
        }
    };
    public static final int IDL_CONTEXT_NOT_FOUND = 1330446337;
    public static final int NO_MATCHING_IDL_CONTEXT = 1330446338;
    public static final int DEP_PREVENT_DESTRUCTION = 1330446337;
    public static final int DESTROY_INDESTRUCTIBLE = 1330446338;
    public static final int SHUTDOWN_WAIT_FOR_COMPLETION_DEADLOCK = 1330446339;
    public static final int BAD_OPERATION_AFTER_SHUTDOWN = 1330446340;
    public static final int BAD_INVOKE = 1330446341;
    public static final int BAD_SET_SERVANT_MANAGER = 1330446342;
    public static final int BAD_ARGUMENTS_CALL = 1330446343;
    public static final int BAD_CTX_CALL = 1330446344;
    public static final int BAD_RESULT_CALL = 1330446345;
    public static final int BAD_SEND = 1330446346;
    public static final int BAD_POLL_BEFORE = 1330446347;
    public static final int BAD_POLL_AFTER = 1330446348;
    public static final int BAD_POLL_SYNC = 1330446349;
    public static final int INVALID_PI_CALL1 = 1330446350;
    public static final int INVALID_PI_CALL2 = 1330446350;
    public static final int INVALID_PI_CALL3 = 1330446350;
    public static final int INVALID_PI_CALL4 = 1330446350;
    public static final int SERVICE_CONTEXT_ADD_FAILED = 1330446351;
    public static final int POLICY_FACTORY_REG_FAILED = 1330446352;
    public static final int CREATE_POA_DESTROY = 1330446353;
    public static final int PRIORITY_REASSIGN = 1330446354;
    public static final int XA_START_OUTSIZE = 1330446355;
    public static final int XA_START_PROTO = 1330446356;
    public static final int BAD_SERVANT_MANAGER_TYPE = 1330446337;
    public static final int OPERATION_UNKNOWN_TO_TARGET = 1330446338;
    public static final int UNABLE_REGISTER_VALUE_FACTORY = 1330446337;
    public static final int RID_ALREADY_DEFINED = 1330446338;
    public static final int NAME_USED_IFR = 1330446339;
    public static final int TARGET_NOT_CONTAINER = 1330446340;
    public static final int NAME_CLASH = 1330446341;
    public static final int NOT_SERIALIZABLE = 1330446342;
    public static final int SO_BAD_SCHEME_NAME = 1330446343;
    public static final int SO_BAD_ADDRESS = 1330446344;
    public static final int SO_BAD_SCHEMA_SPECIFIC = 1330446345;
    public static final int SO_NON_SPECIFIC = 1330446346;
    public static final int IR_DERIVE_ABS_INT_BASE = 1330446347;
    public static final int IR_VALUE_SUPPORT = 1330446348;
    public static final int INCOMPLETE_TYPECODE = 1330446349;
    public static final int INVALID_OBJECT_ID = 1330446350;
    public static final int TYPECODE_BAD_NAME = 1330446351;
    public static final int TYPECODE_BAD_REPID = 1330446352;
    public static final int TYPECODE_INV_MEMBER = 1330446353;
    public static final int TC_UNION_DUP_LABEL = 1330446354;
    public static final int TC_UNION_INCOMPATIBLE = 1330446355;
    public static final int TC_UNION_BAD_DISC = 1330446356;
    public static final int SET_EXCEPTION_BAD_ANY = 1330446357;
    public static final int SET_EXCEPTION_UNLISTED = 1330446358;
    public static final int NO_CLIENT_WCHAR_CODESET_CTX = 1330446359;
    public static final int ILLEGAL_SERVICE_CONTEXT = 1330446360;
    public static final int ENUM_OUT_OF_RANGE = 1330446361;
    public static final int INVALID_SERVICE_CONTEXT_ID = 1330446362;
    public static final int RIR_WITH_NULL_OBJECT = 1330446363;
    public static final int INVALID_COMPONENT_ID = 1330446364;
    public static final int INVALID_PROFILE_ID = 1330446365;
    public static final int POLICY_TYPE_DUPLICATE = 1330446366;
    public static final int BAD_ONEWAY_DEFINITION = 1330446367;
    public static final int DII_FOR_IMPLICIT_OPERATION = 1330446368;
    public static final int XA_CALL_INVAL = 1330446369;
    public static final int UNION_BAD_DISCRIMINATOR = 1330446370;
    public static final int CTX_ILLEGAL_PROPERTY_NAME = 1330446371;
    public static final int CTX_ILLEGAL_SEARCH_STRING = 1330446372;
    public static final int CTX_ILLEGAL_NAME = 1330446373;
    public static final int CTX_NON_EMPTY = 1330446374;
    public static final int INVALID_STREAM_FORMAT_VERSION = 1330446375;
    public static final int NOT_A_VALUEOUTPUTSTREAM = 1330446376;
    public static final int NOT_A_VALUEINPUTSTREAM = 1330446377;
    public static final int MARSHALL_INCOMPLETE_TYPECODE = 1330446337;
    public static final int BAD_MEMBER_TYPECODE = 1330446338;
    public static final int ILLEGAL_PARAMETER = 1330446339;
    public static final int CHAR_NOT_IN_CODESET = 1330446337;
    public static final int PRIORITY_MAP_FAILRE = 1330446338;
    public static final int NO_USABLE_PROFILE = 1330446337;
    public static final int PRIORITY_RANGE_RESTRICT = 1330446337;
    public static final int NO_SERVER_WCHAR_CODESET_CMP = 1330446337;
    public static final int CODESET_COMPONENT_REQUIRED = 1330446338;
    public static final int IOR_POLICY_RECONCILE_ERROR = 1330446337;
    public static final int POLICY_UNKNOWN = 1330446338;
    public static final int NO_POLICY_FACTORY = 1330446339;
    public static final int XA_RMERR = 1330446337;
    public static final int XA_RMFAIL = 1330446338;
    public static final int NO_IR = 1330446337;
    public static final int NO_INTERFACE_IN_IR = 1330446338;
    public static final int UNABLE_LOCATE_VALUE_FACTORY = 1330446337;
    public static final int SET_RESULT_BEFORE_CTX = 1330446338;
    public static final int BAD_NVLIST = 1330446339;
    public static final int NOT_AN_OBJECT_IMPL = 1330446340;
    public static final int WCHAR_BAD_GIOP_VERSION_SENT = 1330446341;
    public static final int WCHAR_BAD_GIOP_VERSION_RETURNED = 1330446342;
    public static final int UNSUPPORTED_FORMAT_VERSION = 1330446343;
    public static final int RMIIIOP_OPTIONAL_DATA_INCOMPATIBLE1 = 1330446344;
    public static final int RMIIIOP_OPTIONAL_DATA_INCOMPATIBLE2 = 1330446344;
    public static final int RMIIIOP_OPTIONAL_DATA_INCOMPATIBLE3 = 1330446344;
    public static final int MISSING_LOCAL_VALUE_IMPL = 1330446337;
    public static final int INCOMPATIBLE_VALUE_IMPL = 1330446338;
    public static final int NO_USABLE_PROFILE_2 = 1330446339;
    public static final int DII_LOCAL_OBJECT = 1330446340;
    public static final int BIO_RESET = 1330446341;
    public static final int BIO_META_NOT_AVAILABLE = 1330446342;
    public static final int BIO_GENOMIC_NO_ITERATOR = 1330446343;
    public static final int PI_OPERATION_NOT_SUPPORTED1 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED2 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED3 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED4 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED5 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED6 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED7 = 1330446337;
    public static final int PI_OPERATION_NOT_SUPPORTED8 = 1330446337;
    public static final int NO_CONNECTION_PRIORITY = 1330446338;
    public static final int XA_RB = 1330446337;
    public static final int XA_NOTA = 1330446338;
    public static final int XA_END_TRUE_ROLLBACK_DEFERRED = 1330446339;
    public static final int POA_REQUEST_DISCARD = 1330446337;
    public static final int NO_USABLE_PROFILE_3 = 1330446338;
    public static final int REQUEST_CANCELLED = 1330446339;
    public static final int POA_DESTROYED = 1330446340;
    public static final int UNREGISTERED_VALUE_AS_OBJREF = 1330446337;
    public static final int NO_OBJECT_ADAPTOR = 1330446338;
    public static final int BIO_NOT_AVAILABLE = 1330446339;
    public static final int OBJECT_ADAPTER_INACTIVE = 1330446340;
    public static final int ADAPTER_ACTIVATOR_EXCEPTION = 1330446337;
    public static final int BAD_SERVANT_TYPE = 1330446338;
    public static final int NO_DEFAULT_SERVANT = 1330446339;
    public static final int NO_SERVANT_MANAGER = 1330446340;
    public static final int BAD_POLICY_INCARNATE = 1330446341;
    public static final int PI_EXC_COMP_ESTABLISHED = 1330446342;
    public static final int NULL_SERVANT_RETURNED = 1330446343;
    public static final int UNKNOWN_USER_EXCEPTION = 1330446337;
    public static final int UNSUPPORTED_SYSTEM_EXCEPTION = 1330446338;
    public static final int PI_UNKNOWN_USER_EXCEPTION = 1330446339;

    public OMGSystemException(Logger logger) {
        super(logger);
    }

    public static OMGSystemException get(ORB orb, String str) {
        return (OMGSystemException) orb.getLogWrapper(str, "OMG", factory);
    }

    public static OMGSystemException get(String str) {
        return (OMGSystemException) ORB.staticGetLogWrapper(str, "OMG", factory);
    }

    public BAD_CONTEXT idlContextNotFound(CompletionStatus completionStatus, Throwable th) {
        BAD_CONTEXT bad_context = new BAD_CONTEXT(1330446337, completionStatus);
        if (th != null) {
            bad_context.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.idlContextNotFound", null, OMGSystemException.class, bad_context);
        }
        return bad_context;
    }

    public BAD_CONTEXT idlContextNotFound(CompletionStatus completionStatus) {
        return idlContextNotFound(completionStatus, null);
    }

    public BAD_CONTEXT idlContextNotFound(Throwable th) {
        return idlContextNotFound(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_CONTEXT idlContextNotFound() {
        return idlContextNotFound(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_CONTEXT noMatchingIdlContext(CompletionStatus completionStatus, Throwable th) {
        BAD_CONTEXT bad_context = new BAD_CONTEXT(1330446338, completionStatus);
        if (th != null) {
            bad_context.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noMatchingIdlContext", null, OMGSystemException.class, bad_context);
        }
        return bad_context;
    }

    public BAD_CONTEXT noMatchingIdlContext(CompletionStatus completionStatus) {
        return noMatchingIdlContext(completionStatus, null);
    }

    public BAD_CONTEXT noMatchingIdlContext(Throwable th) {
        return noMatchingIdlContext(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_CONTEXT noMatchingIdlContext() {
        return noMatchingIdlContext(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER depPreventDestruction(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446337, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.depPreventDestruction", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER depPreventDestruction(CompletionStatus completionStatus) {
        return depPreventDestruction(completionStatus, null);
    }

    public BAD_INV_ORDER depPreventDestruction(Throwable th) {
        return depPreventDestruction(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER depPreventDestruction() {
        return depPreventDestruction(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER destroyIndestructible(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446338, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.destroyIndestructible", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER destroyIndestructible(CompletionStatus completionStatus) {
        return destroyIndestructible(completionStatus, null);
    }

    public BAD_INV_ORDER destroyIndestructible(Throwable th) {
        return destroyIndestructible(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER destroyIndestructible() {
        return destroyIndestructible(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER shutdownWaitForCompletionDeadlock(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446339, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.shutdownWaitForCompletionDeadlock", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER shutdownWaitForCompletionDeadlock(CompletionStatus completionStatus) {
        return shutdownWaitForCompletionDeadlock(completionStatus, null);
    }

    public BAD_INV_ORDER shutdownWaitForCompletionDeadlock(Throwable th) {
        return shutdownWaitForCompletionDeadlock(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER shutdownWaitForCompletionDeadlock() {
        return shutdownWaitForCompletionDeadlock(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badOperationAfterShutdown(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446340, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badOperationAfterShutdown", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badOperationAfterShutdown(CompletionStatus completionStatus) {
        return badOperationAfterShutdown(completionStatus, null);
    }

    public BAD_INV_ORDER badOperationAfterShutdown(Throwable th) {
        return badOperationAfterShutdown(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badOperationAfterShutdown() {
        return badOperationAfterShutdown(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badInvoke(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446341, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badInvoke", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badInvoke(CompletionStatus completionStatus) {
        return badInvoke(completionStatus, null);
    }

    public BAD_INV_ORDER badInvoke(Throwable th) {
        return badInvoke(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badInvoke() {
        return badInvoke(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badSetServantManager(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446342, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badSetServantManager", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badSetServantManager(CompletionStatus completionStatus) {
        return badSetServantManager(completionStatus, null);
    }

    public BAD_INV_ORDER badSetServantManager(Throwable th) {
        return badSetServantManager(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badSetServantManager() {
        return badSetServantManager(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badArgumentsCall(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446343, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badArgumentsCall", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badArgumentsCall(CompletionStatus completionStatus) {
        return badArgumentsCall(completionStatus, null);
    }

    public BAD_INV_ORDER badArgumentsCall(Throwable th) {
        return badArgumentsCall(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badArgumentsCall() {
        return badArgumentsCall(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badCtxCall(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446344, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badCtxCall", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badCtxCall(CompletionStatus completionStatus) {
        return badCtxCall(completionStatus, null);
    }

    public BAD_INV_ORDER badCtxCall(Throwable th) {
        return badCtxCall(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badCtxCall() {
        return badCtxCall(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badResultCall(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446345, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badResultCall", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badResultCall(CompletionStatus completionStatus) {
        return badResultCall(completionStatus, null);
    }

    public BAD_INV_ORDER badResultCall(Throwable th) {
        return badResultCall(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badResultCall() {
        return badResultCall(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badSend(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446346, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badSend", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badSend(CompletionStatus completionStatus) {
        return badSend(completionStatus, null);
    }

    public BAD_INV_ORDER badSend(Throwable th) {
        return badSend(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badSend() {
        return badSend(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badPollBefore(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446347, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badPollBefore", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badPollBefore(CompletionStatus completionStatus) {
        return badPollBefore(completionStatus, null);
    }

    public BAD_INV_ORDER badPollBefore(Throwable th) {
        return badPollBefore(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badPollBefore() {
        return badPollBefore(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badPollAfter(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446348, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badPollAfter", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badPollAfter(CompletionStatus completionStatus) {
        return badPollAfter(completionStatus, null);
    }

    public BAD_INV_ORDER badPollAfter(Throwable th) {
        return badPollAfter(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badPollAfter() {
        return badPollAfter(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER badPollSync(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446349, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badPollSync", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER badPollSync(CompletionStatus completionStatus) {
        return badPollSync(completionStatus, null);
    }

    public BAD_INV_ORDER badPollSync(Throwable th) {
        return badPollSync(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER badPollSync() {
        return badPollSync(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER invalidPiCall1(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446350, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.invalidPiCall1", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER invalidPiCall1(CompletionStatus completionStatus) {
        return invalidPiCall1(completionStatus, null);
    }

    public BAD_INV_ORDER invalidPiCall1(Throwable th) {
        return invalidPiCall1(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER invalidPiCall1() {
        return invalidPiCall1(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER invalidPiCall2(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446350, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.invalidPiCall2", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER invalidPiCall2(CompletionStatus completionStatus) {
        return invalidPiCall2(completionStatus, null);
    }

    public BAD_INV_ORDER invalidPiCall2(Throwable th) {
        return invalidPiCall2(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER invalidPiCall2() {
        return invalidPiCall2(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER invalidPiCall3(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446350, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.invalidPiCall3", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER invalidPiCall3(CompletionStatus completionStatus) {
        return invalidPiCall3(completionStatus, null);
    }

    public BAD_INV_ORDER invalidPiCall3(Throwable th) {
        return invalidPiCall3(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER invalidPiCall3() {
        return invalidPiCall3(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER invalidPiCall4(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446350, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.invalidPiCall4", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER invalidPiCall4(CompletionStatus completionStatus) {
        return invalidPiCall4(completionStatus, null);
    }

    public BAD_INV_ORDER invalidPiCall4(Throwable th) {
        return invalidPiCall4(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER invalidPiCall4() {
        return invalidPiCall4(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER serviceContextAddFailed(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446351, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.serviceContextAddFailed", new Object[]{obj}, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER serviceContextAddFailed(CompletionStatus completionStatus, Object obj) {
        return serviceContextAddFailed(completionStatus, null, obj);
    }

    public BAD_INV_ORDER serviceContextAddFailed(Throwable th, Object obj) {
        return serviceContextAddFailed(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_INV_ORDER serviceContextAddFailed(Object obj) {
        return serviceContextAddFailed(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_INV_ORDER policyFactoryRegFailed(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446352, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.policyFactoryRegFailed", new Object[]{obj}, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER policyFactoryRegFailed(CompletionStatus completionStatus, Object obj) {
        return policyFactoryRegFailed(completionStatus, null, obj);
    }

    public BAD_INV_ORDER policyFactoryRegFailed(Throwable th, Object obj) {
        return policyFactoryRegFailed(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_INV_ORDER policyFactoryRegFailed(Object obj) {
        return policyFactoryRegFailed(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_INV_ORDER createPoaDestroy(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446353, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.createPoaDestroy", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER createPoaDestroy(CompletionStatus completionStatus) {
        return createPoaDestroy(completionStatus, null);
    }

    public BAD_INV_ORDER createPoaDestroy(Throwable th) {
        return createPoaDestroy(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER createPoaDestroy() {
        return createPoaDestroy(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER priorityReassign(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446354, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.priorityReassign", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER priorityReassign(CompletionStatus completionStatus) {
        return priorityReassign(completionStatus, null);
    }

    public BAD_INV_ORDER priorityReassign(Throwable th) {
        return priorityReassign(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER priorityReassign() {
        return priorityReassign(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER xaStartOutsize(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446355, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaStartOutsize", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER xaStartOutsize(CompletionStatus completionStatus) {
        return xaStartOutsize(completionStatus, null);
    }

    public BAD_INV_ORDER xaStartOutsize(Throwable th) {
        return xaStartOutsize(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER xaStartOutsize() {
        return xaStartOutsize(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER xaStartProto(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1330446356, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaStartProto", null, OMGSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER xaStartProto(CompletionStatus completionStatus) {
        return xaStartProto(completionStatus, null);
    }

    public BAD_INV_ORDER xaStartProto(Throwable th) {
        return xaStartProto(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER xaStartProto() {
        return xaStartProto(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION badServantManagerType(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1330446337, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badServantManagerType", null, OMGSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION badServantManagerType(CompletionStatus completionStatus) {
        return badServantManagerType(completionStatus, null);
    }

    public BAD_OPERATION badServantManagerType(Throwable th) {
        return badServantManagerType(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION badServantManagerType() {
        return badServantManagerType(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION operationUnknownToTarget(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1330446338, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.operationUnknownToTarget", null, OMGSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION operationUnknownToTarget(CompletionStatus completionStatus) {
        return operationUnknownToTarget(completionStatus, null);
    }

    public BAD_OPERATION operationUnknownToTarget(Throwable th) {
        return operationUnknownToTarget(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION operationUnknownToTarget() {
        return operationUnknownToTarget(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM unableRegisterValueFactory(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446337, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.unableRegisterValueFactory", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM unableRegisterValueFactory(CompletionStatus completionStatus) {
        return unableRegisterValueFactory(completionStatus, null);
    }

    public BAD_PARAM unableRegisterValueFactory(Throwable th) {
        return unableRegisterValueFactory(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM unableRegisterValueFactory() {
        return unableRegisterValueFactory(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM ridAlreadyDefined(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446338, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.ridAlreadyDefined", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM ridAlreadyDefined(CompletionStatus completionStatus) {
        return ridAlreadyDefined(completionStatus, null);
    }

    public BAD_PARAM ridAlreadyDefined(Throwable th) {
        return ridAlreadyDefined(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM ridAlreadyDefined() {
        return ridAlreadyDefined(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM nameUsedIfr(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446339, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.nameUsedIfr", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM nameUsedIfr(CompletionStatus completionStatus) {
        return nameUsedIfr(completionStatus, null);
    }

    public BAD_PARAM nameUsedIfr(Throwable th) {
        return nameUsedIfr(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM nameUsedIfr() {
        return nameUsedIfr(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM targetNotContainer(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446340, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.targetNotContainer", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM targetNotContainer(CompletionStatus completionStatus) {
        return targetNotContainer(completionStatus, null);
    }

    public BAD_PARAM targetNotContainer(Throwable th) {
        return targetNotContainer(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM targetNotContainer() {
        return targetNotContainer(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM nameClash(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446341, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.nameClash", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM nameClash(CompletionStatus completionStatus) {
        return nameClash(completionStatus, null);
    }

    public BAD_PARAM nameClash(Throwable th) {
        return nameClash(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM nameClash() {
        return nameClash(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM notSerializable(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446342, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.notSerializable", new Object[]{obj}, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM notSerializable(CompletionStatus completionStatus, Object obj) {
        return notSerializable(completionStatus, null, obj);
    }

    public BAD_PARAM notSerializable(Throwable th, Object obj) {
        return notSerializable(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM notSerializable(Object obj) {
        return notSerializable(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM soBadSchemeName(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446343, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.soBadSchemeName", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM soBadSchemeName(CompletionStatus completionStatus) {
        return soBadSchemeName(completionStatus, null);
    }

    public BAD_PARAM soBadSchemeName(Throwable th) {
        return soBadSchemeName(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM soBadSchemeName() {
        return soBadSchemeName(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM soBadAddress(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446344, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.soBadAddress", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM soBadAddress(CompletionStatus completionStatus) {
        return soBadAddress(completionStatus, null);
    }

    public BAD_PARAM soBadAddress(Throwable th) {
        return soBadAddress(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM soBadAddress() {
        return soBadAddress(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM soBadSchemaSpecific(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446345, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.soBadSchemaSpecific", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM soBadSchemaSpecific(CompletionStatus completionStatus) {
        return soBadSchemaSpecific(completionStatus, null);
    }

    public BAD_PARAM soBadSchemaSpecific(Throwable th) {
        return soBadSchemaSpecific(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM soBadSchemaSpecific() {
        return soBadSchemaSpecific(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM soNonSpecific(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446346, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.soNonSpecific", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM soNonSpecific(CompletionStatus completionStatus) {
        return soNonSpecific(completionStatus, null);
    }

    public BAD_PARAM soNonSpecific(Throwable th) {
        return soNonSpecific(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM soNonSpecific() {
        return soNonSpecific(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM irDeriveAbsIntBase(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446347, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.irDeriveAbsIntBase", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM irDeriveAbsIntBase(CompletionStatus completionStatus) {
        return irDeriveAbsIntBase(completionStatus, null);
    }

    public BAD_PARAM irDeriveAbsIntBase(Throwable th) {
        return irDeriveAbsIntBase(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM irDeriveAbsIntBase() {
        return irDeriveAbsIntBase(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM irValueSupport(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446348, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.irValueSupport", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM irValueSupport(CompletionStatus completionStatus) {
        return irValueSupport(completionStatus, null);
    }

    public BAD_PARAM irValueSupport(Throwable th) {
        return irValueSupport(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM irValueSupport() {
        return irValueSupport(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM incompleteTypecode(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446349, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.incompleteTypecode", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM incompleteTypecode(CompletionStatus completionStatus) {
        return incompleteTypecode(completionStatus, null);
    }

    public BAD_PARAM incompleteTypecode(Throwable th) {
        return incompleteTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM incompleteTypecode() {
        return incompleteTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidObjectId(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446350, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.invalidObjectId", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidObjectId(CompletionStatus completionStatus) {
        return invalidObjectId(completionStatus, null);
    }

    public BAD_PARAM invalidObjectId(Throwable th) {
        return invalidObjectId(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM invalidObjectId() {
        return invalidObjectId(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM typecodeBadName(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446351, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.typecodeBadName", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM typecodeBadName(CompletionStatus completionStatus) {
        return typecodeBadName(completionStatus, null);
    }

    public BAD_PARAM typecodeBadName(Throwable th) {
        return typecodeBadName(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM typecodeBadName() {
        return typecodeBadName(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM typecodeBadRepid(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446352, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.typecodeBadRepid", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM typecodeBadRepid(CompletionStatus completionStatus) {
        return typecodeBadRepid(completionStatus, null);
    }

    public BAD_PARAM typecodeBadRepid(Throwable th) {
        return typecodeBadRepid(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM typecodeBadRepid() {
        return typecodeBadRepid(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM typecodeInvMember(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446353, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.typecodeInvMember", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM typecodeInvMember(CompletionStatus completionStatus) {
        return typecodeInvMember(completionStatus, null);
    }

    public BAD_PARAM typecodeInvMember(Throwable th) {
        return typecodeInvMember(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM typecodeInvMember() {
        return typecodeInvMember(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM tcUnionDupLabel(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446354, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.tcUnionDupLabel", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM tcUnionDupLabel(CompletionStatus completionStatus) {
        return tcUnionDupLabel(completionStatus, null);
    }

    public BAD_PARAM tcUnionDupLabel(Throwable th) {
        return tcUnionDupLabel(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM tcUnionDupLabel() {
        return tcUnionDupLabel(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM tcUnionIncompatible(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446355, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.tcUnionIncompatible", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM tcUnionIncompatible(CompletionStatus completionStatus) {
        return tcUnionIncompatible(completionStatus, null);
    }

    public BAD_PARAM tcUnionIncompatible(Throwable th) {
        return tcUnionIncompatible(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM tcUnionIncompatible() {
        return tcUnionIncompatible(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM tcUnionBadDisc(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1330446356, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.tcUnionBadDisc", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM tcUnionBadDisc(CompletionStatus completionStatus) {
        return tcUnionBadDisc(completionStatus, null);
    }

    public BAD_PARAM tcUnionBadDisc(Throwable th) {
        return tcUnionBadDisc(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM tcUnionBadDisc() {
        return tcUnionBadDisc(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM setExceptionBadAny(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(SET_EXCEPTION_BAD_ANY, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.setExceptionBadAny", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM setExceptionBadAny(CompletionStatus completionStatus) {
        return setExceptionBadAny(completionStatus, null);
    }

    public BAD_PARAM setExceptionBadAny(Throwable th) {
        return setExceptionBadAny(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM setExceptionBadAny() {
        return setExceptionBadAny(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM setExceptionUnlisted(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(SET_EXCEPTION_UNLISTED, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.setExceptionUnlisted", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM setExceptionUnlisted(CompletionStatus completionStatus) {
        return setExceptionUnlisted(completionStatus, null);
    }

    public BAD_PARAM setExceptionUnlisted(Throwable th) {
        return setExceptionUnlisted(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM setExceptionUnlisted() {
        return setExceptionUnlisted(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM noClientWcharCodesetCtx(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(NO_CLIENT_WCHAR_CODESET_CTX, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noClientWcharCodesetCtx", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM noClientWcharCodesetCtx(CompletionStatus completionStatus) {
        return noClientWcharCodesetCtx(completionStatus, null);
    }

    public BAD_PARAM noClientWcharCodesetCtx(Throwable th) {
        return noClientWcharCodesetCtx(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM noClientWcharCodesetCtx() {
        return noClientWcharCodesetCtx(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM illegalServiceContext(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(ILLEGAL_SERVICE_CONTEXT, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.illegalServiceContext", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM illegalServiceContext(CompletionStatus completionStatus) {
        return illegalServiceContext(completionStatus, null);
    }

    public BAD_PARAM illegalServiceContext(Throwable th) {
        return illegalServiceContext(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM illegalServiceContext() {
        return illegalServiceContext(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM enumOutOfRange(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(ENUM_OUT_OF_RANGE, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.enumOutOfRange", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM enumOutOfRange(CompletionStatus completionStatus) {
        return enumOutOfRange(completionStatus, null);
    }

    public BAD_PARAM enumOutOfRange(Throwable th) {
        return enumOutOfRange(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM enumOutOfRange() {
        return enumOutOfRange(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidServiceContextId(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(INVALID_SERVICE_CONTEXT_ID, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.invalidServiceContextId", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidServiceContextId(CompletionStatus completionStatus) {
        return invalidServiceContextId(completionStatus, null);
    }

    public BAD_PARAM invalidServiceContextId(Throwable th) {
        return invalidServiceContextId(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM invalidServiceContextId() {
        return invalidServiceContextId(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM rirWithNullObject(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(RIR_WITH_NULL_OBJECT, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.rirWithNullObject", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM rirWithNullObject(CompletionStatus completionStatus) {
        return rirWithNullObject(completionStatus, null);
    }

    public BAD_PARAM rirWithNullObject(Throwable th) {
        return rirWithNullObject(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM rirWithNullObject() {
        return rirWithNullObject(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidComponentId(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(INVALID_COMPONENT_ID, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.invalidComponentId", new Object[]{obj}, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidComponentId(CompletionStatus completionStatus, Object obj) {
        return invalidComponentId(completionStatus, null, obj);
    }

    public BAD_PARAM invalidComponentId(Throwable th, Object obj) {
        return invalidComponentId(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM invalidComponentId(Object obj) {
        return invalidComponentId(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM invalidProfileId(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(INVALID_PROFILE_ID, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.invalidProfileId", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidProfileId(CompletionStatus completionStatus) {
        return invalidProfileId(completionStatus, null);
    }

    public BAD_PARAM invalidProfileId(Throwable th) {
        return invalidProfileId(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM invalidProfileId() {
        return invalidProfileId(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM policyTypeDuplicate(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(POLICY_TYPE_DUPLICATE, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.policyTypeDuplicate", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM policyTypeDuplicate(CompletionStatus completionStatus) {
        return policyTypeDuplicate(completionStatus, null);
    }

    public BAD_PARAM policyTypeDuplicate(Throwable th) {
        return policyTypeDuplicate(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM policyTypeDuplicate() {
        return policyTypeDuplicate(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM badOnewayDefinition(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(BAD_ONEWAY_DEFINITION, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badOnewayDefinition", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badOnewayDefinition(CompletionStatus completionStatus) {
        return badOnewayDefinition(completionStatus, null);
    }

    public BAD_PARAM badOnewayDefinition(Throwable th) {
        return badOnewayDefinition(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM badOnewayDefinition() {
        return badOnewayDefinition(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM diiForImplicitOperation(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(DII_FOR_IMPLICIT_OPERATION, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.diiForImplicitOperation", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM diiForImplicitOperation(CompletionStatus completionStatus) {
        return diiForImplicitOperation(completionStatus, null);
    }

    public BAD_PARAM diiForImplicitOperation(Throwable th) {
        return diiForImplicitOperation(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM diiForImplicitOperation() {
        return diiForImplicitOperation(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM xaCallInval(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(XA_CALL_INVAL, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaCallInval", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM xaCallInval(CompletionStatus completionStatus) {
        return xaCallInval(completionStatus, null);
    }

    public BAD_PARAM xaCallInval(Throwable th) {
        return xaCallInval(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM xaCallInval() {
        return xaCallInval(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM unionBadDiscriminator(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(UNION_BAD_DISCRIMINATOR, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.unionBadDiscriminator", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM unionBadDiscriminator(CompletionStatus completionStatus) {
        return unionBadDiscriminator(completionStatus, null);
    }

    public BAD_PARAM unionBadDiscriminator(Throwable th) {
        return unionBadDiscriminator(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM unionBadDiscriminator() {
        return unionBadDiscriminator(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM ctxIllegalPropertyName(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(CTX_ILLEGAL_PROPERTY_NAME, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.ctxIllegalPropertyName", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM ctxIllegalPropertyName(CompletionStatus completionStatus) {
        return ctxIllegalPropertyName(completionStatus, null);
    }

    public BAD_PARAM ctxIllegalPropertyName(Throwable th) {
        return ctxIllegalPropertyName(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM ctxIllegalPropertyName() {
        return ctxIllegalPropertyName(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM ctxIllegalSearchString(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(CTX_ILLEGAL_SEARCH_STRING, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.ctxIllegalSearchString", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM ctxIllegalSearchString(CompletionStatus completionStatus) {
        return ctxIllegalSearchString(completionStatus, null);
    }

    public BAD_PARAM ctxIllegalSearchString(Throwable th) {
        return ctxIllegalSearchString(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM ctxIllegalSearchString() {
        return ctxIllegalSearchString(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM ctxIllegalName(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(CTX_ILLEGAL_NAME, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.ctxIllegalName", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM ctxIllegalName(CompletionStatus completionStatus) {
        return ctxIllegalName(completionStatus, null);
    }

    public BAD_PARAM ctxIllegalName(Throwable th) {
        return ctxIllegalName(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM ctxIllegalName() {
        return ctxIllegalName(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM ctxNonEmpty(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(CTX_NON_EMPTY, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.ctxNonEmpty", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM ctxNonEmpty(CompletionStatus completionStatus) {
        return ctxNonEmpty(completionStatus, null);
    }

    public BAD_PARAM ctxNonEmpty(Throwable th) {
        return ctxNonEmpty(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM ctxNonEmpty() {
        return ctxNonEmpty(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidStreamFormatVersion(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(INVALID_STREAM_FORMAT_VERSION, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.invalidStreamFormatVersion", new Object[]{obj}, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidStreamFormatVersion(CompletionStatus completionStatus, Object obj) {
        return invalidStreamFormatVersion(completionStatus, null, obj);
    }

    public BAD_PARAM invalidStreamFormatVersion(Throwable th, Object obj) {
        return invalidStreamFormatVersion(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM invalidStreamFormatVersion(Object obj) {
        return invalidStreamFormatVersion(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM notAValueoutputstream(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(NOT_A_VALUEOUTPUTSTREAM, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.notAValueoutputstream", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM notAValueoutputstream(CompletionStatus completionStatus) {
        return notAValueoutputstream(completionStatus, null);
    }

    public BAD_PARAM notAValueoutputstream(Throwable th) {
        return notAValueoutputstream(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM notAValueoutputstream() {
        return notAValueoutputstream(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM notAValueinputstream(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(NOT_A_VALUEINPUTSTREAM, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.notAValueinputstream", null, OMGSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM notAValueinputstream(CompletionStatus completionStatus) {
        return notAValueinputstream(completionStatus, null);
    }

    public BAD_PARAM notAValueinputstream(Throwable th) {
        return notAValueinputstream(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM notAValueinputstream() {
        return notAValueinputstream(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_TYPECODE marshallIncompleteTypecode(CompletionStatus completionStatus, Throwable th) {
        BAD_TYPECODE bad_typecode = new BAD_TYPECODE(1330446337, completionStatus);
        if (th != null) {
            bad_typecode.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.marshallIncompleteTypecode", null, OMGSystemException.class, bad_typecode);
        }
        return bad_typecode;
    }

    public BAD_TYPECODE marshallIncompleteTypecode(CompletionStatus completionStatus) {
        return marshallIncompleteTypecode(completionStatus, null);
    }

    public BAD_TYPECODE marshallIncompleteTypecode(Throwable th) {
        return marshallIncompleteTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_TYPECODE marshallIncompleteTypecode() {
        return marshallIncompleteTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_TYPECODE badMemberTypecode(CompletionStatus completionStatus, Throwable th) {
        BAD_TYPECODE bad_typecode = new BAD_TYPECODE(1330446338, completionStatus);
        if (th != null) {
            bad_typecode.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badMemberTypecode", null, OMGSystemException.class, bad_typecode);
        }
        return bad_typecode;
    }

    public BAD_TYPECODE badMemberTypecode(CompletionStatus completionStatus) {
        return badMemberTypecode(completionStatus, null);
    }

    public BAD_TYPECODE badMemberTypecode(Throwable th) {
        return badMemberTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_TYPECODE badMemberTypecode() {
        return badMemberTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_TYPECODE illegalParameter(CompletionStatus completionStatus, Throwable th) {
        BAD_TYPECODE bad_typecode = new BAD_TYPECODE(1330446339, completionStatus);
        if (th != null) {
            bad_typecode.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.illegalParameter", null, OMGSystemException.class, bad_typecode);
        }
        return bad_typecode;
    }

    public BAD_TYPECODE illegalParameter(CompletionStatus completionStatus) {
        return illegalParameter(completionStatus, null);
    }

    public BAD_TYPECODE illegalParameter(Throwable th) {
        return illegalParameter(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_TYPECODE illegalParameter() {
        return illegalParameter(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION charNotInCodeset(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1330446337, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.charNotInCodeset", null, OMGSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION charNotInCodeset(CompletionStatus completionStatus) {
        return charNotInCodeset(completionStatus, null);
    }

    public DATA_CONVERSION charNotInCodeset(Throwable th) {
        return charNotInCodeset(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION charNotInCodeset() {
        return charNotInCodeset(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION priorityMapFailre(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1330446338, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.priorityMapFailre", null, OMGSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION priorityMapFailre(CompletionStatus completionStatus) {
        return priorityMapFailre(completionStatus, null);
    }

    public DATA_CONVERSION priorityMapFailre(Throwable th) {
        return priorityMapFailre(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION priorityMapFailre() {
        return priorityMapFailre(CompletionStatus.COMPLETED_NO, null);
    }

    public IMP_LIMIT noUsableProfile(CompletionStatus completionStatus, Throwable th) {
        IMP_LIMIT imp_limit = new IMP_LIMIT(1330446337, completionStatus);
        if (th != null) {
            imp_limit.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noUsableProfile", null, OMGSystemException.class, imp_limit);
        }
        return imp_limit;
    }

    public IMP_LIMIT noUsableProfile(CompletionStatus completionStatus) {
        return noUsableProfile(completionStatus, null);
    }

    public IMP_LIMIT noUsableProfile(Throwable th) {
        return noUsableProfile(CompletionStatus.COMPLETED_NO, th);
    }

    public IMP_LIMIT noUsableProfile() {
        return noUsableProfile(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE priorityRangeRestrict(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1330446337, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.priorityRangeRestrict", null, OMGSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE priorityRangeRestrict(CompletionStatus completionStatus) {
        return priorityRangeRestrict(completionStatus, null);
    }

    public INITIALIZE priorityRangeRestrict(Throwable th) {
        return priorityRangeRestrict(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE priorityRangeRestrict() {
        return priorityRangeRestrict(CompletionStatus.COMPLETED_NO, null);
    }

    public INV_OBJREF noServerWcharCodesetCmp(CompletionStatus completionStatus, Throwable th) {
        INV_OBJREF inv_objref = new INV_OBJREF(1330446337, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noServerWcharCodesetCmp", null, OMGSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF noServerWcharCodesetCmp(CompletionStatus completionStatus) {
        return noServerWcharCodesetCmp(completionStatus, null);
    }

    public INV_OBJREF noServerWcharCodesetCmp(Throwable th) {
        return noServerWcharCodesetCmp(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_OBJREF noServerWcharCodesetCmp() {
        return noServerWcharCodesetCmp(CompletionStatus.COMPLETED_NO, null);
    }

    public INV_OBJREF codesetComponentRequired(CompletionStatus completionStatus, Throwable th) {
        INV_OBJREF inv_objref = new INV_OBJREF(1330446338, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.codesetComponentRequired", null, OMGSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF codesetComponentRequired(CompletionStatus completionStatus) {
        return codesetComponentRequired(completionStatus, null);
    }

    public INV_OBJREF codesetComponentRequired(Throwable th) {
        return codesetComponentRequired(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_OBJREF codesetComponentRequired() {
        return codesetComponentRequired(CompletionStatus.COMPLETED_NO, null);
    }

    public INV_POLICY iorPolicyReconcileError(CompletionStatus completionStatus, Throwable th) {
        INV_POLICY inv_policy = new INV_POLICY(1330446337, completionStatus);
        if (th != null) {
            inv_policy.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.iorPolicyReconcileError", null, OMGSystemException.class, inv_policy);
        }
        return inv_policy;
    }

    public INV_POLICY iorPolicyReconcileError(CompletionStatus completionStatus) {
        return iorPolicyReconcileError(completionStatus, null);
    }

    public INV_POLICY iorPolicyReconcileError(Throwable th) {
        return iorPolicyReconcileError(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_POLICY iorPolicyReconcileError() {
        return iorPolicyReconcileError(CompletionStatus.COMPLETED_NO, null);
    }

    public INV_POLICY policyUnknown(CompletionStatus completionStatus, Throwable th) {
        INV_POLICY inv_policy = new INV_POLICY(1330446338, completionStatus);
        if (th != null) {
            inv_policy.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.policyUnknown", null, OMGSystemException.class, inv_policy);
        }
        return inv_policy;
    }

    public INV_POLICY policyUnknown(CompletionStatus completionStatus) {
        return policyUnknown(completionStatus, null);
    }

    public INV_POLICY policyUnknown(Throwable th) {
        return policyUnknown(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_POLICY policyUnknown() {
        return policyUnknown(CompletionStatus.COMPLETED_NO, null);
    }

    public INV_POLICY noPolicyFactory(CompletionStatus completionStatus, Throwable th) {
        INV_POLICY inv_policy = new INV_POLICY(1330446339, completionStatus);
        if (th != null) {
            inv_policy.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noPolicyFactory", null, OMGSystemException.class, inv_policy);
        }
        return inv_policy;
    }

    public INV_POLICY noPolicyFactory(CompletionStatus completionStatus) {
        return noPolicyFactory(completionStatus, null);
    }

    public INV_POLICY noPolicyFactory(Throwable th) {
        return noPolicyFactory(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_POLICY noPolicyFactory() {
        return noPolicyFactory(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL xaRmerr(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1330446337, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaRmerr", null, OMGSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL xaRmerr(CompletionStatus completionStatus) {
        return xaRmerr(completionStatus, null);
    }

    public INTERNAL xaRmerr(Throwable th) {
        return xaRmerr(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL xaRmerr() {
        return xaRmerr(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL xaRmfail(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1330446338, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaRmfail", null, OMGSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL xaRmfail(CompletionStatus completionStatus) {
        return xaRmfail(completionStatus, null);
    }

    public INTERNAL xaRmfail(Throwable th) {
        return xaRmfail(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL xaRmfail() {
        return xaRmfail(CompletionStatus.COMPLETED_NO, null);
    }

    public INTF_REPOS noIr(CompletionStatus completionStatus, Throwable th) {
        INTF_REPOS intf_repos = new INTF_REPOS(1330446337, completionStatus);
        if (th != null) {
            intf_repos.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noIr", null, OMGSystemException.class, intf_repos);
        }
        return intf_repos;
    }

    public INTF_REPOS noIr(CompletionStatus completionStatus) {
        return noIr(completionStatus, null);
    }

    public INTF_REPOS noIr(Throwable th) {
        return noIr(CompletionStatus.COMPLETED_NO, th);
    }

    public INTF_REPOS noIr() {
        return noIr(CompletionStatus.COMPLETED_NO, null);
    }

    public INTF_REPOS noInterfaceInIr(CompletionStatus completionStatus, Throwable th) {
        INTF_REPOS intf_repos = new INTF_REPOS(1330446338, completionStatus);
        if (th != null) {
            intf_repos.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noInterfaceInIr", null, OMGSystemException.class, intf_repos);
        }
        return intf_repos;
    }

    public INTF_REPOS noInterfaceInIr(CompletionStatus completionStatus) {
        return noInterfaceInIr(completionStatus, null);
    }

    public INTF_REPOS noInterfaceInIr(Throwable th) {
        return noInterfaceInIr(CompletionStatus.COMPLETED_NO, th);
    }

    public INTF_REPOS noInterfaceInIr() {
        return noInterfaceInIr(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unableLocateValueFactory(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446337, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.unableLocateValueFactory", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unableLocateValueFactory(CompletionStatus completionStatus) {
        return unableLocateValueFactory(completionStatus, null);
    }

    public MARSHAL unableLocateValueFactory(Throwable th) {
        return unableLocateValueFactory(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL unableLocateValueFactory() {
        return unableLocateValueFactory(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL setResultBeforeCtx(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446338, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.setResultBeforeCtx", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL setResultBeforeCtx(CompletionStatus completionStatus) {
        return setResultBeforeCtx(completionStatus, null);
    }

    public MARSHAL setResultBeforeCtx(Throwable th) {
        return setResultBeforeCtx(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL setResultBeforeCtx() {
        return setResultBeforeCtx(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badNvlist(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446339, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badNvlist", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badNvlist(CompletionStatus completionStatus) {
        return badNvlist(completionStatus, null);
    }

    public MARSHAL badNvlist(Throwable th) {
        return badNvlist(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badNvlist() {
        return badNvlist(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL notAnObjectImpl(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446340, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.notAnObjectImpl", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL notAnObjectImpl(CompletionStatus completionStatus) {
        return notAnObjectImpl(completionStatus, null);
    }

    public MARSHAL notAnObjectImpl(Throwable th) {
        return notAnObjectImpl(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL notAnObjectImpl() {
        return notAnObjectImpl(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL wcharBadGiopVersionSent(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446341, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.wcharBadGiopVersionSent", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL wcharBadGiopVersionSent(CompletionStatus completionStatus) {
        return wcharBadGiopVersionSent(completionStatus, null);
    }

    public MARSHAL wcharBadGiopVersionSent(Throwable th) {
        return wcharBadGiopVersionSent(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL wcharBadGiopVersionSent() {
        return wcharBadGiopVersionSent(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL wcharBadGiopVersionReturned(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446342, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.wcharBadGiopVersionReturned", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL wcharBadGiopVersionReturned(CompletionStatus completionStatus) {
        return wcharBadGiopVersionReturned(completionStatus, null);
    }

    public MARSHAL wcharBadGiopVersionReturned(Throwable th) {
        return wcharBadGiopVersionReturned(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL wcharBadGiopVersionReturned() {
        return wcharBadGiopVersionReturned(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unsupportedFormatVersion(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446343, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.unsupportedFormatVersion", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unsupportedFormatVersion(CompletionStatus completionStatus) {
        return unsupportedFormatVersion(completionStatus, null);
    }

    public MARSHAL unsupportedFormatVersion(Throwable th) {
        return unsupportedFormatVersion(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL unsupportedFormatVersion() {
        return unsupportedFormatVersion(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible1(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446344, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.rmiiiopOptionalDataIncompatible1", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL rmiiiopOptionalDataIncompatible1(CompletionStatus completionStatus) {
        return rmiiiopOptionalDataIncompatible1(completionStatus, null);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible1(Throwable th) {
        return rmiiiopOptionalDataIncompatible1(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible1() {
        return rmiiiopOptionalDataIncompatible1(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible2(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446344, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.rmiiiopOptionalDataIncompatible2", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL rmiiiopOptionalDataIncompatible2(CompletionStatus completionStatus) {
        return rmiiiopOptionalDataIncompatible2(completionStatus, null);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible2(Throwable th) {
        return rmiiiopOptionalDataIncompatible2(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible2() {
        return rmiiiopOptionalDataIncompatible2(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible3(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1330446344, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.rmiiiopOptionalDataIncompatible3", null, OMGSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL rmiiiopOptionalDataIncompatible3(CompletionStatus completionStatus) {
        return rmiiiopOptionalDataIncompatible3(completionStatus, null);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible3(Throwable th) {
        return rmiiiopOptionalDataIncompatible3(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL rmiiiopOptionalDataIncompatible3() {
        return rmiiiopOptionalDataIncompatible3(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT missingLocalValueImpl(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446337, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.missingLocalValueImpl", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT missingLocalValueImpl(CompletionStatus completionStatus) {
        return missingLocalValueImpl(completionStatus, null);
    }

    public NO_IMPLEMENT missingLocalValueImpl(Throwable th) {
        return missingLocalValueImpl(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT missingLocalValueImpl() {
        return missingLocalValueImpl(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT incompatibleValueImpl(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446338, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.incompatibleValueImpl", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT incompatibleValueImpl(CompletionStatus completionStatus) {
        return incompatibleValueImpl(completionStatus, null);
    }

    public NO_IMPLEMENT incompatibleValueImpl(Throwable th) {
        return incompatibleValueImpl(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT incompatibleValueImpl() {
        return incompatibleValueImpl(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT noUsableProfile2(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446339, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noUsableProfile2", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT noUsableProfile2(CompletionStatus completionStatus) {
        return noUsableProfile2(completionStatus, null);
    }

    public NO_IMPLEMENT noUsableProfile2(Throwable th) {
        return noUsableProfile2(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT noUsableProfile2() {
        return noUsableProfile2(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT diiLocalObject(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446340, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.diiLocalObject", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT diiLocalObject(CompletionStatus completionStatus) {
        return diiLocalObject(completionStatus, null);
    }

    public NO_IMPLEMENT diiLocalObject(Throwable th) {
        return diiLocalObject(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT diiLocalObject() {
        return diiLocalObject(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT bioReset(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446341, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.bioReset", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT bioReset(CompletionStatus completionStatus) {
        return bioReset(completionStatus, null);
    }

    public NO_IMPLEMENT bioReset(Throwable th) {
        return bioReset(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT bioReset() {
        return bioReset(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT bioMetaNotAvailable(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446342, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.bioMetaNotAvailable", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT bioMetaNotAvailable(CompletionStatus completionStatus) {
        return bioMetaNotAvailable(completionStatus, null);
    }

    public NO_IMPLEMENT bioMetaNotAvailable(Throwable th) {
        return bioMetaNotAvailable(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT bioMetaNotAvailable() {
        return bioMetaNotAvailable(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT bioGenomicNoIterator(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1330446343, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.bioGenomicNoIterator", null, OMGSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT bioGenomicNoIterator(CompletionStatus completionStatus) {
        return bioGenomicNoIterator(completionStatus, null);
    }

    public NO_IMPLEMENT bioGenomicNoIterator(Throwable th) {
        return bioGenomicNoIterator(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT bioGenomicNoIterator() {
        return bioGenomicNoIterator(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported1(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported1", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported1(CompletionStatus completionStatus) {
        return piOperationNotSupported1(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported1(Throwable th) {
        return piOperationNotSupported1(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported1() {
        return piOperationNotSupported1(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported2(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported2", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported2(CompletionStatus completionStatus) {
        return piOperationNotSupported2(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported2(Throwable th) {
        return piOperationNotSupported2(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported2() {
        return piOperationNotSupported2(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported3(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported3", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported3(CompletionStatus completionStatus) {
        return piOperationNotSupported3(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported3(Throwable th) {
        return piOperationNotSupported3(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported3() {
        return piOperationNotSupported3(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported4(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported4", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported4(CompletionStatus completionStatus) {
        return piOperationNotSupported4(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported4(Throwable th) {
        return piOperationNotSupported4(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported4() {
        return piOperationNotSupported4(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported5(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported5", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported5(CompletionStatus completionStatus) {
        return piOperationNotSupported5(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported5(Throwable th) {
        return piOperationNotSupported5(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported5() {
        return piOperationNotSupported5(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported6(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported6", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported6(CompletionStatus completionStatus) {
        return piOperationNotSupported6(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported6(Throwable th) {
        return piOperationNotSupported6(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported6() {
        return piOperationNotSupported6(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported7(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported7", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported7(CompletionStatus completionStatus) {
        return piOperationNotSupported7(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported7(Throwable th) {
        return piOperationNotSupported7(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported7() {
        return piOperationNotSupported7(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES piOperationNotSupported8(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446337, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.piOperationNotSupported8", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES piOperationNotSupported8(CompletionStatus completionStatus) {
        return piOperationNotSupported8(completionStatus, null);
    }

    public NO_RESOURCES piOperationNotSupported8(Throwable th) {
        return piOperationNotSupported8(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES piOperationNotSupported8() {
        return piOperationNotSupported8(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_RESOURCES noConnectionPriority(CompletionStatus completionStatus, Throwable th) {
        NO_RESOURCES no_resources = new NO_RESOURCES(1330446338, completionStatus);
        if (th != null) {
            no_resources.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noConnectionPriority", null, OMGSystemException.class, no_resources);
        }
        return no_resources;
    }

    public NO_RESOURCES noConnectionPriority(CompletionStatus completionStatus) {
        return noConnectionPriority(completionStatus, null);
    }

    public NO_RESOURCES noConnectionPriority(Throwable th) {
        return noConnectionPriority(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_RESOURCES noConnectionPriority() {
        return noConnectionPriority(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSACTION_ROLLEDBACK xaRb(CompletionStatus completionStatus, Throwable th) {
        TRANSACTION_ROLLEDBACK transaction_rolledback = new TRANSACTION_ROLLEDBACK(1330446337, completionStatus);
        if (th != null) {
            transaction_rolledback.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaRb", null, OMGSystemException.class, transaction_rolledback);
        }
        return transaction_rolledback;
    }

    public TRANSACTION_ROLLEDBACK xaRb(CompletionStatus completionStatus) {
        return xaRb(completionStatus, null);
    }

    public TRANSACTION_ROLLEDBACK xaRb(Throwable th) {
        return xaRb(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSACTION_ROLLEDBACK xaRb() {
        return xaRb(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSACTION_ROLLEDBACK xaNota(CompletionStatus completionStatus, Throwable th) {
        TRANSACTION_ROLLEDBACK transaction_rolledback = new TRANSACTION_ROLLEDBACK(1330446338, completionStatus);
        if (th != null) {
            transaction_rolledback.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaNota", null, OMGSystemException.class, transaction_rolledback);
        }
        return transaction_rolledback;
    }

    public TRANSACTION_ROLLEDBACK xaNota(CompletionStatus completionStatus) {
        return xaNota(completionStatus, null);
    }

    public TRANSACTION_ROLLEDBACK xaNota(Throwable th) {
        return xaNota(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSACTION_ROLLEDBACK xaNota() {
        return xaNota(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred(CompletionStatus completionStatus, Throwable th) {
        TRANSACTION_ROLLEDBACK transaction_rolledback = new TRANSACTION_ROLLEDBACK(1330446339, completionStatus);
        if (th != null) {
            transaction_rolledback.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.xaEndTrueRollbackDeferred", null, OMGSystemException.class, transaction_rolledback);
        }
        return transaction_rolledback;
    }

    public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred(CompletionStatus completionStatus) {
        return xaEndTrueRollbackDeferred(completionStatus, null);
    }

    public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred(Throwable th) {
        return xaEndTrueRollbackDeferred(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSACTION_ROLLEDBACK xaEndTrueRollbackDeferred() {
        return xaEndTrueRollbackDeferred(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSIENT poaRequestDiscard(CompletionStatus completionStatus, Throwable th) {
        TRANSIENT r0 = new TRANSIENT(1330446337, completionStatus);
        if (th != null) {
            r0.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.poaRequestDiscard", null, OMGSystemException.class, r0);
        }
        return r0;
    }

    public TRANSIENT poaRequestDiscard(CompletionStatus completionStatus) {
        return poaRequestDiscard(completionStatus, null);
    }

    public TRANSIENT poaRequestDiscard(Throwable th) {
        return poaRequestDiscard(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSIENT poaRequestDiscard() {
        return poaRequestDiscard(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSIENT noUsableProfile3(CompletionStatus completionStatus, Throwable th) {
        TRANSIENT r0 = new TRANSIENT(1330446338, completionStatus);
        if (th != null) {
            r0.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noUsableProfile3", null, OMGSystemException.class, r0);
        }
        return r0;
    }

    public TRANSIENT noUsableProfile3(CompletionStatus completionStatus) {
        return noUsableProfile3(completionStatus, null);
    }

    public TRANSIENT noUsableProfile3(Throwable th) {
        return noUsableProfile3(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSIENT noUsableProfile3() {
        return noUsableProfile3(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSIENT requestCancelled(CompletionStatus completionStatus, Throwable th) {
        TRANSIENT r0 = new TRANSIENT(1330446339, completionStatus);
        if (th != null) {
            r0.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.requestCancelled", null, OMGSystemException.class, r0);
        }
        return r0;
    }

    public TRANSIENT requestCancelled(CompletionStatus completionStatus) {
        return requestCancelled(completionStatus, null);
    }

    public TRANSIENT requestCancelled(Throwable th) {
        return requestCancelled(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSIENT requestCancelled() {
        return requestCancelled(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSIENT poaDestroyed(CompletionStatus completionStatus, Throwable th) {
        TRANSIENT r0 = new TRANSIENT(1330446340, completionStatus);
        if (th != null) {
            r0.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.poaDestroyed", null, OMGSystemException.class, r0);
        }
        return r0;
    }

    public TRANSIENT poaDestroyed(CompletionStatus completionStatus) {
        return poaDestroyed(completionStatus, null);
    }

    public TRANSIENT poaDestroyed(Throwable th) {
        return poaDestroyed(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSIENT poaDestroyed() {
        return poaDestroyed(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST unregisteredValueAsObjref(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1330446337, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.unregisteredValueAsObjref", null, OMGSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST unregisteredValueAsObjref(CompletionStatus completionStatus) {
        return unregisteredValueAsObjref(completionStatus, null);
    }

    public OBJECT_NOT_EXIST unregisteredValueAsObjref(Throwable th) {
        return unregisteredValueAsObjref(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST unregisteredValueAsObjref() {
        return unregisteredValueAsObjref(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST noObjectAdaptor(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1330446338, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.noObjectAdaptor", null, OMGSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST noObjectAdaptor(CompletionStatus completionStatus) {
        return noObjectAdaptor(completionStatus, null);
    }

    public OBJECT_NOT_EXIST noObjectAdaptor(Throwable th) {
        return noObjectAdaptor(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST noObjectAdaptor() {
        return noObjectAdaptor(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST bioNotAvailable(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1330446339, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.bioNotAvailable", null, OMGSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST bioNotAvailable(CompletionStatus completionStatus) {
        return bioNotAvailable(completionStatus, null);
    }

    public OBJECT_NOT_EXIST bioNotAvailable(Throwable th) {
        return bioNotAvailable(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST bioNotAvailable() {
        return bioNotAvailable(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST objectAdapterInactive(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1330446340, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.objectAdapterInactive", null, OMGSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST objectAdapterInactive(CompletionStatus completionStatus) {
        return objectAdapterInactive(completionStatus, null);
    }

    public OBJECT_NOT_EXIST objectAdapterInactive(Throwable th) {
        return objectAdapterInactive(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST objectAdapterInactive() {
        return objectAdapterInactive(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER adapterActivatorException(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446337, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.adapterActivatorException", new Object[]{obj, obj2}, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER adapterActivatorException(CompletionStatus completionStatus, Object obj, Object obj2) {
        return adapterActivatorException(completionStatus, null, obj, obj2);
    }

    public OBJ_ADAPTER adapterActivatorException(Throwable th, Object obj, Object obj2) {
        return adapterActivatorException(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public OBJ_ADAPTER adapterActivatorException(Object obj, Object obj2) {
        return adapterActivatorException(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public OBJ_ADAPTER badServantType(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446338, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badServantType", null, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER badServantType(CompletionStatus completionStatus) {
        return badServantType(completionStatus, null);
    }

    public OBJ_ADAPTER badServantType(Throwable th) {
        return badServantType(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER badServantType() {
        return badServantType(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER noDefaultServant(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446339, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noDefaultServant", null, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER noDefaultServant(CompletionStatus completionStatus) {
        return noDefaultServant(completionStatus, null);
    }

    public OBJ_ADAPTER noDefaultServant(Throwable th) {
        return noDefaultServant(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER noDefaultServant() {
        return noDefaultServant(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER noServantManager(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446340, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.noServantManager", null, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER noServantManager(CompletionStatus completionStatus) {
        return noServantManager(completionStatus, null);
    }

    public OBJ_ADAPTER noServantManager(Throwable th) {
        return noServantManager(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER noServantManager() {
        return noServantManager(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER badPolicyIncarnate(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446341, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.badPolicyIncarnate", null, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER badPolicyIncarnate(CompletionStatus completionStatus) {
        return badPolicyIncarnate(completionStatus, null);
    }

    public OBJ_ADAPTER badPolicyIncarnate(Throwable th) {
        return badPolicyIncarnate(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER badPolicyIncarnate() {
        return badPolicyIncarnate(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER piExcCompEstablished(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446342, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.piExcCompEstablished", null, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER piExcCompEstablished(CompletionStatus completionStatus) {
        return piExcCompEstablished(completionStatus, null);
    }

    public OBJ_ADAPTER piExcCompEstablished(Throwable th) {
        return piExcCompEstablished(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER piExcCompEstablished() {
        return piExcCompEstablished(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER nullServantReturned(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1330446343, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.nullServantReturned", null, OMGSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER nullServantReturned(CompletionStatus completionStatus) {
        return nullServantReturned(completionStatus, null);
    }

    public OBJ_ADAPTER nullServantReturned(Throwable th) {
        return nullServantReturned(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER nullServantReturned() {
        return nullServantReturned(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownUserException(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1330446337, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "OMG.unknownUserException", null, OMGSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownUserException(CompletionStatus completionStatus) {
        return unknownUserException(completionStatus, null);
    }

    public UNKNOWN unknownUserException(Throwable th) {
        return unknownUserException(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownUserException() {
        return unknownUserException(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unsupportedSystemException(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1330446338, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.unsupportedSystemException", null, OMGSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unsupportedSystemException(CompletionStatus completionStatus) {
        return unsupportedSystemException(completionStatus, null);
    }

    public UNKNOWN unsupportedSystemException(Throwable th) {
        return unsupportedSystemException(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unsupportedSystemException() {
        return unsupportedSystemException(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN piUnknownUserException(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1330446339, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "OMG.piUnknownUserException", null, OMGSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN piUnknownUserException(CompletionStatus completionStatus) {
        return piUnknownUserException(completionStatus, null);
    }

    public UNKNOWN piUnknownUserException(Throwable th) {
        return piUnknownUserException(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN piUnknownUserException() {
        return piUnknownUserException(CompletionStatus.COMPLETED_NO, null);
    }
}
