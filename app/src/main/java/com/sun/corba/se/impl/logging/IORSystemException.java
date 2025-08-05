package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.INV_OBJREF;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/IORSystemException.class */
public class IORSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.IORSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new IORSystemException(logger);
        }
    };
    public static final int ORT_NOT_INITIALIZED = 1398080689;
    public static final int NULL_POA = 1398080690;
    public static final int BAD_MAGIC = 1398080691;
    public static final int STRINGIFY_WRITE_ERROR = 1398080692;
    public static final int TAGGED_PROFILE_TEMPLATE_FACTORY_NOT_FOUND = 1398080693;
    public static final int INVALID_JDK1_3_1_PATCH_LEVEL = 1398080694;
    public static final int GET_LOCAL_SERVANT_FAILURE = 1398080695;
    public static final int ADAPTER_ID_NOT_AVAILABLE = 1398080689;
    public static final int SERVER_ID_NOT_AVAILABLE = 1398080690;
    public static final int ORB_ID_NOT_AVAILABLE = 1398080691;
    public static final int OBJECT_ADAPTER_ID_NOT_AVAILABLE = 1398080692;
    public static final int BAD_OID_IN_IOR_TEMPLATE_LIST = 1398080689;
    public static final int INVALID_TAGGED_PROFILE = 1398080690;
    public static final int BAD_IIOP_ADDRESS_PORT = 1398080691;
    public static final int IOR_MUST_HAVE_IIOP_PROFILE = 1398080689;

    public IORSystemException(Logger logger) {
        super(logger);
    }

    public static IORSystemException get(ORB orb, String str) {
        return (IORSystemException) orb.getLogWrapper(str, "IOR", factory);
    }

    public static IORSystemException get(String str) {
        return (IORSystemException) ORB.staticGetLogWrapper(str, "IOR", factory);
    }

    public INTERNAL ortNotInitialized(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080689, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.ortNotInitialized", null, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL ortNotInitialized(CompletionStatus completionStatus) {
        return ortNotInitialized(completionStatus, null);
    }

    public INTERNAL ortNotInitialized(Throwable th) {
        return ortNotInitialized(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL ortNotInitialized() {
        return ortNotInitialized(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL nullPoa(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080690, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.nullPoa", null, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL nullPoa(CompletionStatus completionStatus) {
        return nullPoa(completionStatus, null);
    }

    public INTERNAL nullPoa(Throwable th) {
        return nullPoa(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL nullPoa() {
        return nullPoa(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badMagic(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398080691, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.badMagic", new Object[]{obj}, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badMagic(CompletionStatus completionStatus, Object obj) {
        return badMagic(completionStatus, null, obj);
    }

    public INTERNAL badMagic(Throwable th, Object obj) {
        return badMagic(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL badMagic(Object obj) {
        return badMagic(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL stringifyWriteError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080692, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.stringifyWriteError", null, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL stringifyWriteError(CompletionStatus completionStatus) {
        return stringifyWriteError(completionStatus, null);
    }

    public INTERNAL stringifyWriteError(Throwable th) {
        return stringifyWriteError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL stringifyWriteError() {
        return stringifyWriteError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL taggedProfileTemplateFactoryNotFound(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(TAGGED_PROFILE_TEMPLATE_FACTORY_NOT_FOUND, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.taggedProfileTemplateFactoryNotFound", new Object[]{obj}, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL taggedProfileTemplateFactoryNotFound(CompletionStatus completionStatus, Object obj) {
        return taggedProfileTemplateFactoryNotFound(completionStatus, null, obj);
    }

    public INTERNAL taggedProfileTemplateFactoryNotFound(Throwable th, Object obj) {
        return taggedProfileTemplateFactoryNotFound(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL taggedProfileTemplateFactoryNotFound(Object obj) {
        return taggedProfileTemplateFactoryNotFound(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL invalidJdk131PatchLevel(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(INVALID_JDK1_3_1_PATCH_LEVEL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.invalidJdk131PatchLevel", new Object[]{obj}, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidJdk131PatchLevel(CompletionStatus completionStatus, Object obj) {
        return invalidJdk131PatchLevel(completionStatus, null, obj);
    }

    public INTERNAL invalidJdk131PatchLevel(Throwable th, Object obj) {
        return invalidJdk131PatchLevel(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL invalidJdk131PatchLevel(Object obj) {
        return invalidJdk131PatchLevel(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL getLocalServantFailure(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(GET_LOCAL_SERVANT_FAILURE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "IOR.getLocalServantFailure", new Object[]{obj}, IORSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL getLocalServantFailure(CompletionStatus completionStatus, Object obj) {
        return getLocalServantFailure(completionStatus, null, obj);
    }

    public INTERNAL getLocalServantFailure(Throwable th, Object obj) {
        return getLocalServantFailure(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL getLocalServantFailure(Object obj) {
        return getLocalServantFailure(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_OPERATION adapterIdNotAvailable(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080689, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.adapterIdNotAvailable", null, IORSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION adapterIdNotAvailable(CompletionStatus completionStatus) {
        return adapterIdNotAvailable(completionStatus, null);
    }

    public BAD_OPERATION adapterIdNotAvailable(Throwable th) {
        return adapterIdNotAvailable(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION adapterIdNotAvailable() {
        return adapterIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION serverIdNotAvailable(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080690, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.serverIdNotAvailable", null, IORSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION serverIdNotAvailable(CompletionStatus completionStatus) {
        return serverIdNotAvailable(completionStatus, null);
    }

    public BAD_OPERATION serverIdNotAvailable(Throwable th) {
        return serverIdNotAvailable(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION serverIdNotAvailable() {
        return serverIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION orbIdNotAvailable(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080691, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.orbIdNotAvailable", null, IORSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION orbIdNotAvailable(CompletionStatus completionStatus) {
        return orbIdNotAvailable(completionStatus, null);
    }

    public BAD_OPERATION orbIdNotAvailable(Throwable th) {
        return orbIdNotAvailable(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION orbIdNotAvailable() {
        return orbIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION objectAdapterIdNotAvailable(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080692, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.objectAdapterIdNotAvailable", null, IORSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION objectAdapterIdNotAvailable(CompletionStatus completionStatus) {
        return objectAdapterIdNotAvailable(completionStatus, null);
    }

    public BAD_OPERATION objectAdapterIdNotAvailable(Throwable th) {
        return objectAdapterIdNotAvailable(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION objectAdapterIdNotAvailable() {
        return objectAdapterIdNotAvailable(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM badOidInIorTemplateList(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080689, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.badOidInIorTemplateList", null, IORSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badOidInIorTemplateList(CompletionStatus completionStatus) {
        return badOidInIorTemplateList(completionStatus, null);
    }

    public BAD_PARAM badOidInIorTemplateList(Throwable th) {
        return badOidInIorTemplateList(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM badOidInIorTemplateList() {
        return badOidInIorTemplateList(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidTaggedProfile(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080690, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.invalidTaggedProfile", null, IORSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidTaggedProfile(CompletionStatus completionStatus) {
        return invalidTaggedProfile(completionStatus, null);
    }

    public BAD_PARAM invalidTaggedProfile(Throwable th) {
        return invalidTaggedProfile(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM invalidTaggedProfile() {
        return invalidTaggedProfile(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM badIiopAddressPort(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080691, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.badIiopAddressPort", new Object[]{obj}, IORSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badIiopAddressPort(CompletionStatus completionStatus, Object obj) {
        return badIiopAddressPort(completionStatus, null, obj);
    }

    public BAD_PARAM badIiopAddressPort(Throwable th, Object obj) {
        return badIiopAddressPort(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM badIiopAddressPort(Object obj) {
        return badIiopAddressPort(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INV_OBJREF iorMustHaveIiopProfile(CompletionStatus completionStatus, Throwable th) {
        INV_OBJREF inv_objref = new INV_OBJREF(1398080689, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "IOR.iorMustHaveIiopProfile", null, IORSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF iorMustHaveIiopProfile(CompletionStatus completionStatus) {
        return iorMustHaveIiopProfile(completionStatus, null);
    }

    public INV_OBJREF iorMustHaveIiopProfile(Throwable th) {
        return iorMustHaveIiopProfile(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_OBJREF iorMustHaveIiopProfile() {
        return iorMustHaveIiopProfile(CompletionStatus.COMPLETED_NO, null);
    }
}
