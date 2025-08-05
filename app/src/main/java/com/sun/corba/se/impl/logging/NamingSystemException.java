package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.UNKNOWN;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/NamingSystemException.class */
public class NamingSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.NamingSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new NamingSystemException(logger);
        }
    };
    public static final int TRANSIENT_NAME_SERVER_BAD_PORT = 1398080088;
    public static final int TRANSIENT_NAME_SERVER_BAD_HOST = 1398080089;
    public static final int OBJECT_IS_NULL = 1398080090;
    public static final int INS_BAD_ADDRESS = 1398080091;
    public static final int BIND_UPDATE_CONTEXT_FAILED = 1398080088;
    public static final int BIND_FAILURE = 1398080089;
    public static final int RESOLVE_CONVERSION_FAILURE = 1398080090;
    public static final int RESOLVE_FAILURE = 1398080091;
    public static final int UNBIND_FAILURE = 1398080092;
    public static final int TRANS_NS_CANNOT_CREATE_INITIAL_NC_SYS = 1398080138;
    public static final int TRANS_NS_CANNOT_CREATE_INITIAL_NC = 1398080139;
    public static final int NAMING_CTX_REBIND_ALREADY_BOUND = 1398080088;
    public static final int NAMING_CTX_REBINDCTX_ALREADY_BOUND = 1398080089;
    public static final int NAMING_CTX_BAD_BINDINGTYPE = 1398080090;
    public static final int NAMING_CTX_RESOLVE_CANNOT_NARROW_TO_CTX = 1398080091;
    public static final int NAMING_CTX_BINDING_ITERATOR_CREATE = 1398080092;
    public static final int TRANS_NC_BIND_ALREADY_BOUND = 1398080188;
    public static final int TRANS_NC_LIST_GOT_EXC = 1398080189;
    public static final int TRANS_NC_NEWCTX_GOT_EXC = 1398080190;
    public static final int TRANS_NC_DESTROY_GOT_EXC = 1398080191;
    public static final int INS_BAD_SCHEME_NAME = 1398080193;
    public static final int INS_BAD_SCHEME_SPECIFIC_PART = 1398080195;
    public static final int INS_OTHER = 1398080196;

    public NamingSystemException(Logger logger) {
        super(logger);
    }

    public static NamingSystemException get(ORB orb, String str) {
        return (NamingSystemException) orb.getLogWrapper(str, "NAMING", factory);
    }

    public static NamingSystemException get(String str) {
        return (NamingSystemException) ORB.staticGetLogWrapper(str, "NAMING", factory);
    }

    public BAD_PARAM transientNameServerBadPort(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080088, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transientNameServerBadPort", null, NamingSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM transientNameServerBadPort(CompletionStatus completionStatus) {
        return transientNameServerBadPort(completionStatus, null);
    }

    public BAD_PARAM transientNameServerBadPort(Throwable th) {
        return transientNameServerBadPort(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM transientNameServerBadPort() {
        return transientNameServerBadPort(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM transientNameServerBadHost(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080089, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transientNameServerBadHost", null, NamingSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM transientNameServerBadHost(CompletionStatus completionStatus) {
        return transientNameServerBadHost(completionStatus, null);
    }

    public BAD_PARAM transientNameServerBadHost(Throwable th) {
        return transientNameServerBadHost(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM transientNameServerBadHost() {
        return transientNameServerBadHost(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM objectIsNull(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080090, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.objectIsNull", null, NamingSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM objectIsNull(CompletionStatus completionStatus) {
        return objectIsNull(completionStatus, null);
    }

    public BAD_PARAM objectIsNull(Throwable th) {
        return objectIsNull(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM objectIsNull() {
        return objectIsNull(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM insBadAddress(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080091, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.insBadAddress", null, NamingSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM insBadAddress(CompletionStatus completionStatus) {
        return insBadAddress(completionStatus, null);
    }

    public BAD_PARAM insBadAddress(Throwable th) {
        return insBadAddress(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM insBadAddress() {
        return insBadAddress(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN bindUpdateContextFailed(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080088, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.bindUpdateContextFailed", null, NamingSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN bindUpdateContextFailed(CompletionStatus completionStatus) {
        return bindUpdateContextFailed(completionStatus, null);
    }

    public UNKNOWN bindUpdateContextFailed(Throwable th) {
        return bindUpdateContextFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN bindUpdateContextFailed() {
        return bindUpdateContextFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN bindFailure(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080089, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.bindFailure", null, NamingSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN bindFailure(CompletionStatus completionStatus) {
        return bindFailure(completionStatus, null);
    }

    public UNKNOWN bindFailure(Throwable th) {
        return bindFailure(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN bindFailure() {
        return bindFailure(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN resolveConversionFailure(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080090, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.resolveConversionFailure", null, NamingSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN resolveConversionFailure(CompletionStatus completionStatus) {
        return resolveConversionFailure(completionStatus, null);
    }

    public UNKNOWN resolveConversionFailure(Throwable th) {
        return resolveConversionFailure(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN resolveConversionFailure() {
        return resolveConversionFailure(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN resolveFailure(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080091, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.resolveFailure", null, NamingSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN resolveFailure(CompletionStatus completionStatus) {
        return resolveFailure(completionStatus, null);
    }

    public UNKNOWN resolveFailure(Throwable th) {
        return resolveFailure(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN resolveFailure() {
        return resolveFailure(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unbindFailure(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080092, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.unbindFailure", null, NamingSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unbindFailure(CompletionStatus completionStatus) {
        return unbindFailure(completionStatus, null);
    }

    public UNKNOWN unbindFailure(Throwable th) {
        return unbindFailure(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unbindFailure() {
        return unbindFailure(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE transNsCannotCreateInitialNcSys(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(TRANS_NS_CANNOT_CREATE_INITIAL_NC_SYS, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transNsCannotCreateInitialNcSys", null, NamingSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE transNsCannotCreateInitialNcSys(CompletionStatus completionStatus) {
        return transNsCannotCreateInitialNcSys(completionStatus, null);
    }

    public INITIALIZE transNsCannotCreateInitialNcSys(Throwable th) {
        return transNsCannotCreateInitialNcSys(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE transNsCannotCreateInitialNcSys() {
        return transNsCannotCreateInitialNcSys(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE transNsCannotCreateInitialNc(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(TRANS_NS_CANNOT_CREATE_INITIAL_NC, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transNsCannotCreateInitialNc", null, NamingSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE transNsCannotCreateInitialNc(CompletionStatus completionStatus) {
        return transNsCannotCreateInitialNc(completionStatus, null);
    }

    public INITIALIZE transNsCannotCreateInitialNc(Throwable th) {
        return transNsCannotCreateInitialNc(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE transNsCannotCreateInitialNc() {
        return transNsCannotCreateInitialNc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL namingCtxRebindAlreadyBound(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080088, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.namingCtxRebindAlreadyBound", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL namingCtxRebindAlreadyBound(CompletionStatus completionStatus) {
        return namingCtxRebindAlreadyBound(completionStatus, null);
    }

    public INTERNAL namingCtxRebindAlreadyBound(Throwable th) {
        return namingCtxRebindAlreadyBound(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL namingCtxRebindAlreadyBound() {
        return namingCtxRebindAlreadyBound(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL namingCtxRebindctxAlreadyBound(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080089, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.namingCtxRebindctxAlreadyBound", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL namingCtxRebindctxAlreadyBound(CompletionStatus completionStatus) {
        return namingCtxRebindctxAlreadyBound(completionStatus, null);
    }

    public INTERNAL namingCtxRebindctxAlreadyBound(Throwable th) {
        return namingCtxRebindctxAlreadyBound(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL namingCtxRebindctxAlreadyBound() {
        return namingCtxRebindctxAlreadyBound(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL namingCtxBadBindingtype(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080090, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.namingCtxBadBindingtype", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL namingCtxBadBindingtype(CompletionStatus completionStatus) {
        return namingCtxBadBindingtype(completionStatus, null);
    }

    public INTERNAL namingCtxBadBindingtype(Throwable th) {
        return namingCtxBadBindingtype(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL namingCtxBadBindingtype() {
        return namingCtxBadBindingtype(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL namingCtxResolveCannotNarrowToCtx(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080091, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.namingCtxResolveCannotNarrowToCtx", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL namingCtxResolveCannotNarrowToCtx(CompletionStatus completionStatus) {
        return namingCtxResolveCannotNarrowToCtx(completionStatus, null);
    }

    public INTERNAL namingCtxResolveCannotNarrowToCtx(Throwable th) {
        return namingCtxResolveCannotNarrowToCtx(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL namingCtxResolveCannotNarrowToCtx() {
        return namingCtxResolveCannotNarrowToCtx(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL namingCtxBindingIteratorCreate(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398080092, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.namingCtxBindingIteratorCreate", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL namingCtxBindingIteratorCreate(CompletionStatus completionStatus) {
        return namingCtxBindingIteratorCreate(completionStatus, null);
    }

    public INTERNAL namingCtxBindingIteratorCreate(Throwable th) {
        return namingCtxBindingIteratorCreate(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL namingCtxBindingIteratorCreate() {
        return namingCtxBindingIteratorCreate(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL transNcBindAlreadyBound(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(TRANS_NC_BIND_ALREADY_BOUND, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transNcBindAlreadyBound", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL transNcBindAlreadyBound(CompletionStatus completionStatus) {
        return transNcBindAlreadyBound(completionStatus, null);
    }

    public INTERNAL transNcBindAlreadyBound(Throwable th) {
        return transNcBindAlreadyBound(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL transNcBindAlreadyBound() {
        return transNcBindAlreadyBound(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL transNcListGotExc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(TRANS_NC_LIST_GOT_EXC, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transNcListGotExc", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL transNcListGotExc(CompletionStatus completionStatus) {
        return transNcListGotExc(completionStatus, null);
    }

    public INTERNAL transNcListGotExc(Throwable th) {
        return transNcListGotExc(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL transNcListGotExc() {
        return transNcListGotExc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL transNcNewctxGotExc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(TRANS_NC_NEWCTX_GOT_EXC, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transNcNewctxGotExc", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL transNcNewctxGotExc(CompletionStatus completionStatus) {
        return transNcNewctxGotExc(completionStatus, null);
    }

    public INTERNAL transNcNewctxGotExc(Throwable th) {
        return transNcNewctxGotExc(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL transNcNewctxGotExc() {
        return transNcNewctxGotExc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL transNcDestroyGotExc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(TRANS_NC_DESTROY_GOT_EXC, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.transNcDestroyGotExc", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL transNcDestroyGotExc(CompletionStatus completionStatus) {
        return transNcDestroyGotExc(completionStatus, null);
    }

    public INTERNAL transNcDestroyGotExc(Throwable th) {
        return transNcDestroyGotExc(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL transNcDestroyGotExc() {
        return transNcDestroyGotExc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL insBadSchemeName(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INS_BAD_SCHEME_NAME, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.insBadSchemeName", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL insBadSchemeName(CompletionStatus completionStatus) {
        return insBadSchemeName(completionStatus, null);
    }

    public INTERNAL insBadSchemeName(Throwable th) {
        return insBadSchemeName(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL insBadSchemeName() {
        return insBadSchemeName(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL insBadSchemeSpecificPart(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INS_BAD_SCHEME_SPECIFIC_PART, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.insBadSchemeSpecificPart", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL insBadSchemeSpecificPart(CompletionStatus completionStatus) {
        return insBadSchemeSpecificPart(completionStatus, null);
    }

    public INTERNAL insBadSchemeSpecificPart(Throwable th) {
        return insBadSchemeSpecificPart(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL insBadSchemeSpecificPart() {
        return insBadSchemeSpecificPart(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL insOther(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INS_OTHER, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "NAMING.insOther", null, NamingSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL insOther(CompletionStatus completionStatus) {
        return insOther(completionStatus, null);
    }

    public INTERNAL insOther(Throwable th) {
        return insOther(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL insOther() {
        return insOther(CompletionStatus.COMPLETED_NO, null);
    }
}
