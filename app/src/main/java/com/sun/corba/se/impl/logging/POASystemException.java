package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.TRANSIENT;
import org.omg.CORBA.UNKNOWN;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/POASystemException.class */
public class POASystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.POASystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new POASystemException(logger);
        }
    };
    public static final int SERVANT_MANAGER_ALREADY_SET = 1398080489;
    public static final int DESTROY_DEADLOCK = 1398080490;
    public static final int SERVANT_ORB = 1398080489;
    public static final int BAD_SERVANT = 1398080490;
    public static final int ILLEGAL_FORWARD_REQUEST = 1398080491;
    public static final int BAD_TRANSACTION_CONTEXT = 1398080489;
    public static final int BAD_REPOSITORY_ID = 1398080490;
    public static final int INVOKESETUP = 1398080489;
    public static final int BAD_LOCALREPLYSTATUS = 1398080490;
    public static final int PERSISTENT_SERVERPORT_ERROR = 1398080491;
    public static final int SERVANT_DISPATCH = 1398080492;
    public static final int WRONG_CLIENTSC = 1398080493;
    public static final int CANT_CLONE_TEMPLATE = 1398080494;
    public static final int POACURRENT_UNBALANCED_STACK = 1398080495;
    public static final int POACURRENT_NULL_FIELD = 1398080496;
    public static final int POA_INTERNAL_GET_SERVANT_ERROR = 1398080497;
    public static final int MAKE_FACTORY_NOT_POA = 1398080498;
    public static final int DUPLICATE_ORB_VERSION_SC = 1398080499;
    public static final int PREINVOKE_CLONE_ERROR = 1398080500;
    public static final int PREINVOKE_POA_DESTROYED = 1398080501;
    public static final int PMF_CREATE_RETAIN = 1398080502;
    public static final int PMF_CREATE_NON_RETAIN = 1398080503;
    public static final int POLICY_MEDIATOR_BAD_POLICY_IN_FACTORY = 1398080504;
    public static final int SERVANT_TO_ID_OAA = 1398080505;
    public static final int SERVANT_TO_ID_SAA = 1398080506;
    public static final int SERVANT_TO_ID_WP = 1398080507;
    public static final int CANT_RESOLVE_ROOT_POA = 1398080508;
    public static final int SERVANT_MUST_BE_LOCAL = 1398080509;
    public static final int NO_PROFILES_IN_IOR = 1398080510;
    public static final int AOM_ENTRY_DEC_ZERO = 1398080511;
    public static final int ADD_POA_INACTIVE = 1398080512;
    public static final int ILLEGAL_POA_STATE_TRANS = 1398080513;
    public static final int UNEXPECTED_EXCEPTION = 1398080514;
    public static final int SINGLE_THREAD_NOT_SUPPORTED = 1398080489;
    public static final int METHOD_NOT_IMPLEMENTED = 1398080490;
    public static final int POA_LOOKUP_ERROR = 1398080489;
    public static final int POA_INACTIVE = 1398080490;
    public static final int POA_NO_SERVANT_MANAGER = 1398080491;
    public static final int POA_NO_DEFAULT_SERVANT = 1398080492;
    public static final int POA_SERVANT_NOT_UNIQUE = 1398080493;
    public static final int POA_WRONG_POLICY = 1398080494;
    public static final int FINDPOA_ERROR = 1398080495;
    public static final int POA_SERVANT_ACTIVATOR_LOOKUP_FAILED = 1398080497;
    public static final int POA_BAD_SERVANT_MANAGER = 1398080498;
    public static final int POA_SERVANT_LOCATOR_LOOKUP_FAILED = 1398080499;
    public static final int POA_UNKNOWN_POLICY = 1398080500;
    public static final int POA_NOT_FOUND = 1398080501;
    public static final int SERVANT_LOOKUP = 1398080502;
    public static final int LOCAL_SERVANT_LOOKUP = 1398080503;
    public static final int SERVANT_MANAGER_BAD_TYPE = 1398080504;
    public static final int DEFAULT_POA_NOT_POAIMPL = 1398080505;
    public static final int WRONG_POLICIES_FOR_THIS_OBJECT = 1398080506;
    public static final int THIS_OBJECT_SERVANT_NOT_ACTIVE = 1398080507;
    public static final int THIS_OBJECT_WRONG_POLICY = 1398080508;
    public static final int NO_CONTEXT = 1398080509;
    public static final int INCARNATE_RETURNED_NULL = 1398080510;
    public static final int JTS_INIT_ERROR = 1398080489;
    public static final int PERSISTENT_SERVERID_NOT_SET = 1398080490;
    public static final int PERSISTENT_SERVERPORT_NOT_SET = 1398080491;
    public static final int ORBD_ERROR = 1398080492;
    public static final int BOOTSTRAP_ERROR = 1398080493;
    public static final int POA_DISCARDING = 1398080489;
    public static final int OTSHOOKEXCEPTION = 1398080489;
    public static final int UNKNOWN_SERVER_EXCEPTION = 1398080490;
    public static final int UNKNOWN_SERVERAPP_EXCEPTION = 1398080491;
    public static final int UNKNOWN_LOCALINVOCATION_ERROR = 1398080492;
    public static final int ADAPTER_ACTIVATOR_NONEXISTENT = 1398080489;
    public static final int ADAPTER_ACTIVATOR_FAILED = 1398080490;
    public static final int BAD_SKELETON = 1398080491;
    public static final int NULL_SERVANT = 1398080492;
    public static final int ADAPTER_DESTROYED = 1398080493;

    public POASystemException(Logger logger) {
        super(logger);
    }

    public static POASystemException get(ORB orb, String str) {
        return (POASystemException) orb.getLogWrapper(str, "POA", factory);
    }

    public static POASystemException get(String str) {
        return (POASystemException) ORB.staticGetLogWrapper(str, "POA", factory);
    }

    public BAD_INV_ORDER servantManagerAlreadySet(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398080489, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantManagerAlreadySet", null, POASystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER servantManagerAlreadySet(CompletionStatus completionStatus) {
        return servantManagerAlreadySet(completionStatus, null);
    }

    public BAD_INV_ORDER servantManagerAlreadySet(Throwable th) {
        return servantManagerAlreadySet(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER servantManagerAlreadySet() {
        return servantManagerAlreadySet(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER destroyDeadlock(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398080490, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.destroyDeadlock", null, POASystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER destroyDeadlock(CompletionStatus completionStatus) {
        return destroyDeadlock(completionStatus, null);
    }

    public BAD_INV_ORDER destroyDeadlock(Throwable th) {
        return destroyDeadlock(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER destroyDeadlock() {
        return destroyDeadlock(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION servantOrb(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080489, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantOrb", null, POASystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION servantOrb(CompletionStatus completionStatus) {
        return servantOrb(completionStatus, null);
    }

    public BAD_OPERATION servantOrb(Throwable th) {
        return servantOrb(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION servantOrb() {
        return servantOrb(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION badServant(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080490, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.badServant", null, POASystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION badServant(CompletionStatus completionStatus) {
        return badServant(completionStatus, null);
    }

    public BAD_OPERATION badServant(Throwable th) {
        return badServant(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION badServant() {
        return badServant(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION illegalForwardRequest(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080491, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.illegalForwardRequest", null, POASystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION illegalForwardRequest(CompletionStatus completionStatus) {
        return illegalForwardRequest(completionStatus, null);
    }

    public BAD_OPERATION illegalForwardRequest(Throwable th) {
        return illegalForwardRequest(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION illegalForwardRequest() {
        return illegalForwardRequest(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM badTransactionContext(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080489, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.badTransactionContext", null, POASystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badTransactionContext(CompletionStatus completionStatus) {
        return badTransactionContext(completionStatus, null);
    }

    public BAD_PARAM badTransactionContext(Throwable th) {
        return badTransactionContext(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM badTransactionContext() {
        return badTransactionContext(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM badRepositoryId(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080490, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.badRepositoryId", null, POASystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badRepositoryId(CompletionStatus completionStatus) {
        return badRepositoryId(completionStatus, null);
    }

    public BAD_PARAM badRepositoryId(Throwable th) {
        return badRepositoryId(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM badRepositoryId() {
        return badRepositoryId(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL invokesetup(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080489, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.invokesetup", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invokesetup(CompletionStatus completionStatus) {
        return invokesetup(completionStatus, null);
    }

    public INTERNAL invokesetup(Throwable th) {
        return invokesetup(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL invokesetup() {
        return invokesetup(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badLocalreplystatus(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080490, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.badLocalreplystatus", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badLocalreplystatus(CompletionStatus completionStatus) {
        return badLocalreplystatus(completionStatus, null);
    }

    public INTERNAL badLocalreplystatus(Throwable th) {
        return badLocalreplystatus(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badLocalreplystatus() {
        return badLocalreplystatus(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL persistentServerportError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080491, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.persistentServerportError", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL persistentServerportError(CompletionStatus completionStatus) {
        return persistentServerportError(completionStatus, null);
    }

    public INTERNAL persistentServerportError(Throwable th) {
        return persistentServerportError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL persistentServerportError() {
        return persistentServerportError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL servantDispatch(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080492, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantDispatch", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL servantDispatch(CompletionStatus completionStatus) {
        return servantDispatch(completionStatus, null);
    }

    public INTERNAL servantDispatch(Throwable th) {
        return servantDispatch(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL servantDispatch() {
        return servantDispatch(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL wrongClientsc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080493, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.wrongClientsc", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL wrongClientsc(CompletionStatus completionStatus) {
        return wrongClientsc(completionStatus, null);
    }

    public INTERNAL wrongClientsc(Throwable th) {
        return wrongClientsc(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL wrongClientsc() {
        return wrongClientsc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL cantCloneTemplate(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080494, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.cantCloneTemplate", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL cantCloneTemplate(CompletionStatus completionStatus) {
        return cantCloneTemplate(completionStatus, null);
    }

    public INTERNAL cantCloneTemplate(Throwable th) {
        return cantCloneTemplate(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL cantCloneTemplate() {
        return cantCloneTemplate(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL poacurrentUnbalancedStack(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080495, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poacurrentUnbalancedStack", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL poacurrentUnbalancedStack(CompletionStatus completionStatus) {
        return poacurrentUnbalancedStack(completionStatus, null);
    }

    public INTERNAL poacurrentUnbalancedStack(Throwable th) {
        return poacurrentUnbalancedStack(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL poacurrentUnbalancedStack() {
        return poacurrentUnbalancedStack(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL poacurrentNullField(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(POACURRENT_NULL_FIELD, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poacurrentNullField", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL poacurrentNullField(CompletionStatus completionStatus) {
        return poacurrentNullField(completionStatus, null);
    }

    public INTERNAL poacurrentNullField(Throwable th) {
        return poacurrentNullField(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL poacurrentNullField() {
        return poacurrentNullField(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL poaInternalGetServantError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080497, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaInternalGetServantError", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL poaInternalGetServantError(CompletionStatus completionStatus) {
        return poaInternalGetServantError(completionStatus, null);
    }

    public INTERNAL poaInternalGetServantError(Throwable th) {
        return poaInternalGetServantError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL poaInternalGetServantError() {
        return poaInternalGetServantError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL makeFactoryNotPoa(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398080498, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.makeFactoryNotPoa", new Object[]{obj}, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL makeFactoryNotPoa(CompletionStatus completionStatus, Object obj) {
        return makeFactoryNotPoa(completionStatus, null, obj);
    }

    public INTERNAL makeFactoryNotPoa(Throwable th, Object obj) {
        return makeFactoryNotPoa(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL makeFactoryNotPoa(Object obj) {
        return makeFactoryNotPoa(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL duplicateOrbVersionSc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080499, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.duplicateOrbVersionSc", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL duplicateOrbVersionSc(CompletionStatus completionStatus) {
        return duplicateOrbVersionSc(completionStatus, null);
    }

    public INTERNAL duplicateOrbVersionSc(Throwable th) {
        return duplicateOrbVersionSc(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL duplicateOrbVersionSc() {
        return duplicateOrbVersionSc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL preinvokeCloneError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080500, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.preinvokeCloneError", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL preinvokeCloneError(CompletionStatus completionStatus) {
        return preinvokeCloneError(completionStatus, null);
    }

    public INTERNAL preinvokeCloneError(Throwable th) {
        return preinvokeCloneError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL preinvokeCloneError() {
        return preinvokeCloneError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL preinvokePoaDestroyed(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080501, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.preinvokePoaDestroyed", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL preinvokePoaDestroyed(CompletionStatus completionStatus) {
        return preinvokePoaDestroyed(completionStatus, null);
    }

    public INTERNAL preinvokePoaDestroyed(Throwable th) {
        return preinvokePoaDestroyed(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL preinvokePoaDestroyed() {
        return preinvokePoaDestroyed(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL pmfCreateRetain(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080502, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.pmfCreateRetain", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL pmfCreateRetain(CompletionStatus completionStatus) {
        return pmfCreateRetain(completionStatus, null);
    }

    public INTERNAL pmfCreateRetain(Throwable th) {
        return pmfCreateRetain(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL pmfCreateRetain() {
        return pmfCreateRetain(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL pmfCreateNonRetain(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080503, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.pmfCreateNonRetain", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL pmfCreateNonRetain(CompletionStatus completionStatus) {
        return pmfCreateNonRetain(completionStatus, null);
    }

    public INTERNAL pmfCreateNonRetain(Throwable th) {
        return pmfCreateNonRetain(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL pmfCreateNonRetain() {
        return pmfCreateNonRetain(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL policyMediatorBadPolicyInFactory(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080504, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.policyMediatorBadPolicyInFactory", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL policyMediatorBadPolicyInFactory(CompletionStatus completionStatus) {
        return policyMediatorBadPolicyInFactory(completionStatus, null);
    }

    public INTERNAL policyMediatorBadPolicyInFactory(Throwable th) {
        return policyMediatorBadPolicyInFactory(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL policyMediatorBadPolicyInFactory() {
        return policyMediatorBadPolicyInFactory(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL servantToIdOaa(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080505, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantToIdOaa", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL servantToIdOaa(CompletionStatus completionStatus) {
        return servantToIdOaa(completionStatus, null);
    }

    public INTERNAL servantToIdOaa(Throwable th) {
        return servantToIdOaa(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL servantToIdOaa() {
        return servantToIdOaa(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL servantToIdSaa(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080506, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantToIdSaa", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL servantToIdSaa(CompletionStatus completionStatus) {
        return servantToIdSaa(completionStatus, null);
    }

    public INTERNAL servantToIdSaa(Throwable th) {
        return servantToIdSaa(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL servantToIdSaa() {
        return servantToIdSaa(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL servantToIdWp(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080507, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantToIdWp", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL servantToIdWp(CompletionStatus completionStatus) {
        return servantToIdWp(completionStatus, null);
    }

    public INTERNAL servantToIdWp(Throwable th) {
        return servantToIdWp(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL servantToIdWp() {
        return servantToIdWp(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL cantResolveRootPoa(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080508, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.cantResolveRootPoa", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL cantResolveRootPoa(CompletionStatus completionStatus) {
        return cantResolveRootPoa(completionStatus, null);
    }

    public INTERNAL cantResolveRootPoa(Throwable th) {
        return cantResolveRootPoa(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL cantResolveRootPoa() {
        return cantResolveRootPoa(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL servantMustBeLocal(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080509, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantMustBeLocal", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL servantMustBeLocal(CompletionStatus completionStatus) {
        return servantMustBeLocal(completionStatus, null);
    }

    public INTERNAL servantMustBeLocal(Throwable th) {
        return servantMustBeLocal(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL servantMustBeLocal() {
        return servantMustBeLocal(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL noProfilesInIor(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080510, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.noProfilesInIor", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL noProfilesInIor(CompletionStatus completionStatus) {
        return noProfilesInIor(completionStatus, null);
    }

    public INTERNAL noProfilesInIor(Throwable th) {
        return noProfilesInIor(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL noProfilesInIor() {
        return noProfilesInIor(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL aomEntryDecZero(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(AOM_ENTRY_DEC_ZERO, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.aomEntryDecZero", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL aomEntryDecZero(CompletionStatus completionStatus) {
        return aomEntryDecZero(completionStatus, null);
    }

    public INTERNAL aomEntryDecZero(Throwable th) {
        return aomEntryDecZero(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL aomEntryDecZero() {
        return aomEntryDecZero(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL addPoaInactive(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(ADD_POA_INACTIVE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.addPoaInactive", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL addPoaInactive(CompletionStatus completionStatus) {
        return addPoaInactive(completionStatus, null);
    }

    public INTERNAL addPoaInactive(Throwable th) {
        return addPoaInactive(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL addPoaInactive() {
        return addPoaInactive(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL illegalPoaStateTrans(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(ILLEGAL_POA_STATE_TRANS, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.illegalPoaStateTrans", null, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL illegalPoaStateTrans(CompletionStatus completionStatus) {
        return illegalPoaStateTrans(completionStatus, null);
    }

    public INTERNAL illegalPoaStateTrans(Throwable th) {
        return illegalPoaStateTrans(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL illegalPoaStateTrans() {
        return illegalPoaStateTrans(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unexpectedException(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(UNEXPECTED_EXCEPTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.unexpectedException", new Object[]{obj}, POASystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unexpectedException(CompletionStatus completionStatus, Object obj) {
        return unexpectedException(completionStatus, null, obj);
    }

    public INTERNAL unexpectedException(Throwable th, Object obj) {
        return unexpectedException(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL unexpectedException(Object obj) {
        return unexpectedException(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public NO_IMPLEMENT singleThreadNotSupported(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398080489, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.singleThreadNotSupported", null, POASystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT singleThreadNotSupported(CompletionStatus completionStatus) {
        return singleThreadNotSupported(completionStatus, null);
    }

    public NO_IMPLEMENT singleThreadNotSupported(Throwable th) {
        return singleThreadNotSupported(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT singleThreadNotSupported() {
        return singleThreadNotSupported(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT methodNotImplemented(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398080490, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.methodNotImplemented", null, POASystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT methodNotImplemented(CompletionStatus completionStatus) {
        return methodNotImplemented(completionStatus, null);
    }

    public NO_IMPLEMENT methodNotImplemented(Throwable th) {
        return methodNotImplemented(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT methodNotImplemented() {
        return methodNotImplemented(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaLookupError(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080489, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaLookupError", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaLookupError(CompletionStatus completionStatus) {
        return poaLookupError(completionStatus, null);
    }

    public OBJ_ADAPTER poaLookupError(Throwable th) {
        return poaLookupError(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaLookupError() {
        return poaLookupError(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaInactive(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080490, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "POA.poaInactive", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaInactive(CompletionStatus completionStatus) {
        return poaInactive(completionStatus, null);
    }

    public OBJ_ADAPTER poaInactive(Throwable th) {
        return poaInactive(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaInactive() {
        return poaInactive(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaNoServantManager(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080491, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaNoServantManager", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaNoServantManager(CompletionStatus completionStatus) {
        return poaNoServantManager(completionStatus, null);
    }

    public OBJ_ADAPTER poaNoServantManager(Throwable th) {
        return poaNoServantManager(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaNoServantManager() {
        return poaNoServantManager(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaNoDefaultServant(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080492, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaNoDefaultServant", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaNoDefaultServant(CompletionStatus completionStatus) {
        return poaNoDefaultServant(completionStatus, null);
    }

    public OBJ_ADAPTER poaNoDefaultServant(Throwable th) {
        return poaNoDefaultServant(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaNoDefaultServant() {
        return poaNoDefaultServant(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaServantNotUnique(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080493, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaServantNotUnique", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaServantNotUnique(CompletionStatus completionStatus) {
        return poaServantNotUnique(completionStatus, null);
    }

    public OBJ_ADAPTER poaServantNotUnique(Throwable th) {
        return poaServantNotUnique(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaServantNotUnique() {
        return poaServantNotUnique(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaWrongPolicy(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080494, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaWrongPolicy", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaWrongPolicy(CompletionStatus completionStatus) {
        return poaWrongPolicy(completionStatus, null);
    }

    public OBJ_ADAPTER poaWrongPolicy(Throwable th) {
        return poaWrongPolicy(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaWrongPolicy() {
        return poaWrongPolicy(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER findpoaError(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080495, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.findpoaError", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER findpoaError(CompletionStatus completionStatus) {
        return findpoaError(completionStatus, null);
    }

    public OBJ_ADAPTER findpoaError(Throwable th) {
        return findpoaError(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER findpoaError() {
        return findpoaError(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaServantActivatorLookupFailed(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080497, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaServantActivatorLookupFailed", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaServantActivatorLookupFailed(CompletionStatus completionStatus) {
        return poaServantActivatorLookupFailed(completionStatus, null);
    }

    public OBJ_ADAPTER poaServantActivatorLookupFailed(Throwable th) {
        return poaServantActivatorLookupFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaServantActivatorLookupFailed() {
        return poaServantActivatorLookupFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaBadServantManager(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080498, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaBadServantManager", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaBadServantManager(CompletionStatus completionStatus) {
        return poaBadServantManager(completionStatus, null);
    }

    public OBJ_ADAPTER poaBadServantManager(Throwable th) {
        return poaBadServantManager(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaBadServantManager() {
        return poaBadServantManager(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaServantLocatorLookupFailed(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080499, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaServantLocatorLookupFailed", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaServantLocatorLookupFailed(CompletionStatus completionStatus) {
        return poaServantLocatorLookupFailed(completionStatus, null);
    }

    public OBJ_ADAPTER poaServantLocatorLookupFailed(Throwable th) {
        return poaServantLocatorLookupFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaServantLocatorLookupFailed() {
        return poaServantLocatorLookupFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaUnknownPolicy(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080500, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaUnknownPolicy", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaUnknownPolicy(CompletionStatus completionStatus) {
        return poaUnknownPolicy(completionStatus, null);
    }

    public OBJ_ADAPTER poaUnknownPolicy(Throwable th) {
        return poaUnknownPolicy(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaUnknownPolicy() {
        return poaUnknownPolicy(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER poaNotFound(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080501, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.poaNotFound", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER poaNotFound(CompletionStatus completionStatus) {
        return poaNotFound(completionStatus, null);
    }

    public OBJ_ADAPTER poaNotFound(Throwable th) {
        return poaNotFound(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER poaNotFound() {
        return poaNotFound(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER servantLookup(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080502, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantLookup", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER servantLookup(CompletionStatus completionStatus) {
        return servantLookup(completionStatus, null);
    }

    public OBJ_ADAPTER servantLookup(Throwable th) {
        return servantLookup(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER servantLookup() {
        return servantLookup(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER localServantLookup(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080503, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.localServantLookup", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER localServantLookup(CompletionStatus completionStatus) {
        return localServantLookup(completionStatus, null);
    }

    public OBJ_ADAPTER localServantLookup(Throwable th) {
        return localServantLookup(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER localServantLookup() {
        return localServantLookup(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER servantManagerBadType(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080504, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.servantManagerBadType", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER servantManagerBadType(CompletionStatus completionStatus) {
        return servantManagerBadType(completionStatus, null);
    }

    public OBJ_ADAPTER servantManagerBadType(Throwable th) {
        return servantManagerBadType(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER servantManagerBadType() {
        return servantManagerBadType(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER defaultPoaNotPoaimpl(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080505, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.defaultPoaNotPoaimpl", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER defaultPoaNotPoaimpl(CompletionStatus completionStatus) {
        return defaultPoaNotPoaimpl(completionStatus, null);
    }

    public OBJ_ADAPTER defaultPoaNotPoaimpl(Throwable th) {
        return defaultPoaNotPoaimpl(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER defaultPoaNotPoaimpl() {
        return defaultPoaNotPoaimpl(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER wrongPoliciesForThisObject(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080506, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.wrongPoliciesForThisObject", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER wrongPoliciesForThisObject(CompletionStatus completionStatus) {
        return wrongPoliciesForThisObject(completionStatus, null);
    }

    public OBJ_ADAPTER wrongPoliciesForThisObject(Throwable th) {
        return wrongPoliciesForThisObject(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER wrongPoliciesForThisObject() {
        return wrongPoliciesForThisObject(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER thisObjectServantNotActive(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080507, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.thisObjectServantNotActive", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER thisObjectServantNotActive(CompletionStatus completionStatus) {
        return thisObjectServantNotActive(completionStatus, null);
    }

    public OBJ_ADAPTER thisObjectServantNotActive(Throwable th) {
        return thisObjectServantNotActive(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER thisObjectServantNotActive() {
        return thisObjectServantNotActive(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER thisObjectWrongPolicy(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080508, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.thisObjectWrongPolicy", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER thisObjectWrongPolicy(CompletionStatus completionStatus) {
        return thisObjectWrongPolicy(completionStatus, null);
    }

    public OBJ_ADAPTER thisObjectWrongPolicy(Throwable th) {
        return thisObjectWrongPolicy(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER thisObjectWrongPolicy() {
        return thisObjectWrongPolicy(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER noContext(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080509, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "POA.noContext", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER noContext(CompletionStatus completionStatus) {
        return noContext(completionStatus, null);
    }

    public OBJ_ADAPTER noContext(Throwable th) {
        return noContext(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER noContext() {
        return noContext(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER incarnateReturnedNull(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398080510, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.incarnateReturnedNull", null, POASystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER incarnateReturnedNull(CompletionStatus completionStatus) {
        return incarnateReturnedNull(completionStatus, null);
    }

    public OBJ_ADAPTER incarnateReturnedNull(Throwable th) {
        return incarnateReturnedNull(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER incarnateReturnedNull() {
        return incarnateReturnedNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE jtsInitError(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398080489, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.jtsInitError", null, POASystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE jtsInitError(CompletionStatus completionStatus) {
        return jtsInitError(completionStatus, null);
    }

    public INITIALIZE jtsInitError(Throwable th) {
        return jtsInitError(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE jtsInitError() {
        return jtsInitError(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE persistentServeridNotSet(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398080490, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.persistentServeridNotSet", null, POASystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE persistentServeridNotSet(CompletionStatus completionStatus) {
        return persistentServeridNotSet(completionStatus, null);
    }

    public INITIALIZE persistentServeridNotSet(Throwable th) {
        return persistentServeridNotSet(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE persistentServeridNotSet() {
        return persistentServeridNotSet(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE persistentServerportNotSet(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398080491, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.persistentServerportNotSet", null, POASystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE persistentServerportNotSet(CompletionStatus completionStatus) {
        return persistentServerportNotSet(completionStatus, null);
    }

    public INITIALIZE persistentServerportNotSet(Throwable th) {
        return persistentServerportNotSet(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE persistentServerportNotSet() {
        return persistentServerportNotSet(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE orbdError(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398080492, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.orbdError", null, POASystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE orbdError(CompletionStatus completionStatus) {
        return orbdError(completionStatus, null);
    }

    public INITIALIZE orbdError(Throwable th) {
        return orbdError(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE orbdError() {
        return orbdError(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE bootstrapError(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398080493, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.bootstrapError", null, POASystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE bootstrapError(CompletionStatus completionStatus) {
        return bootstrapError(completionStatus, null);
    }

    public INITIALIZE bootstrapError(Throwable th) {
        return bootstrapError(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE bootstrapError() {
        return bootstrapError(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSIENT poaDiscarding(CompletionStatus completionStatus, Throwable th) {
        TRANSIENT r0 = new TRANSIENT(1398080489, completionStatus);
        if (th != null) {
            r0.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "POA.poaDiscarding", null, POASystemException.class, r0);
        }
        return r0;
    }

    public TRANSIENT poaDiscarding(CompletionStatus completionStatus) {
        return poaDiscarding(completionStatus, null);
    }

    public TRANSIENT poaDiscarding(Throwable th) {
        return poaDiscarding(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSIENT poaDiscarding() {
        return poaDiscarding(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN otshookexception(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080489, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.otshookexception", null, POASystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN otshookexception(CompletionStatus completionStatus) {
        return otshookexception(completionStatus, null);
    }

    public UNKNOWN otshookexception(Throwable th) {
        return otshookexception(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN otshookexception() {
        return otshookexception(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownServerException(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080490, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.unknownServerException", null, POASystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownServerException(CompletionStatus completionStatus) {
        return unknownServerException(completionStatus, null);
    }

    public UNKNOWN unknownServerException(Throwable th) {
        return unknownServerException(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownServerException() {
        return unknownServerException(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownServerappException(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080491, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.unknownServerappException", null, POASystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownServerappException(CompletionStatus completionStatus) {
        return unknownServerappException(completionStatus, null);
    }

    public UNKNOWN unknownServerappException(Throwable th) {
        return unknownServerappException(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownServerappException() {
        return unknownServerappException(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownLocalinvocationError(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080492, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.unknownLocalinvocationError", null, POASystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownLocalinvocationError(CompletionStatus completionStatus) {
        return unknownLocalinvocationError(completionStatus, null);
    }

    public UNKNOWN unknownLocalinvocationError(Throwable th) {
        return unknownLocalinvocationError(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownLocalinvocationError() {
        return unknownLocalinvocationError(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST adapterActivatorNonexistent(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398080489, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.adapterActivatorNonexistent", null, POASystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST adapterActivatorNonexistent(CompletionStatus completionStatus) {
        return adapterActivatorNonexistent(completionStatus, null);
    }

    public OBJECT_NOT_EXIST adapterActivatorNonexistent(Throwable th) {
        return adapterActivatorNonexistent(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST adapterActivatorNonexistent() {
        return adapterActivatorNonexistent(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST adapterActivatorFailed(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398080490, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.adapterActivatorFailed", null, POASystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST adapterActivatorFailed(CompletionStatus completionStatus) {
        return adapterActivatorFailed(completionStatus, null);
    }

    public OBJECT_NOT_EXIST adapterActivatorFailed(Throwable th) {
        return adapterActivatorFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST adapterActivatorFailed() {
        return adapterActivatorFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST badSkeleton(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398080491, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.badSkeleton", null, POASystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST badSkeleton(CompletionStatus completionStatus) {
        return badSkeleton(completionStatus, null);
    }

    public OBJECT_NOT_EXIST badSkeleton(Throwable th) {
        return badSkeleton(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST badSkeleton() {
        return badSkeleton(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST nullServant(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398080492, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "POA.nullServant", null, POASystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST nullServant(CompletionStatus completionStatus) {
        return nullServant(completionStatus, null);
    }

    public OBJECT_NOT_EXIST nullServant(Throwable th) {
        return nullServant(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST nullServant() {
        return nullServant(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST adapterDestroyed(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398080493, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "POA.adapterDestroyed", null, POASystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST adapterDestroyed(CompletionStatus completionStatus) {
        return adapterDestroyed(completionStatus, null);
    }

    public OBJECT_NOT_EXIST adapterDestroyed(Throwable th) {
        return adapterDestroyed(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST adapterDestroyed() {
        return adapterDestroyed(CompletionStatus.COMPLETED_NO, null);
    }
}
