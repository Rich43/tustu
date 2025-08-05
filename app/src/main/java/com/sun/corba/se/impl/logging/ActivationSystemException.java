package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.OBJECT_NOT_EXIST;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/ActivationSystemException.class */
public class ActivationSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.ActivationSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new ActivationSystemException(logger);
        }
    };
    public static final int CANNOT_READ_REPOSITORY_DB = 1398079889;
    public static final int CANNOT_ADD_INITIAL_NAMING = 1398079890;
    public static final int CANNOT_WRITE_REPOSITORY_DB = 1398079889;
    public static final int SERVER_NOT_EXPECTED_TO_REGISTER = 1398079891;
    public static final int UNABLE_TO_START_PROCESS = 1398079892;
    public static final int SERVER_NOT_RUNNING = 1398079894;
    public static final int ERROR_IN_BAD_SERVER_ID_HANDLER = 1398079889;

    public ActivationSystemException(Logger logger) {
        super(logger);
    }

    public static ActivationSystemException get(ORB orb, String str) {
        return (ActivationSystemException) orb.getLogWrapper(str, "ACTIVATION", factory);
    }

    public static ActivationSystemException get(String str) {
        return (ActivationSystemException) ORB.staticGetLogWrapper(str, "ACTIVATION", factory);
    }

    public INITIALIZE cannotReadRepositoryDb(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079889, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.cannotReadRepositoryDb", null, ActivationSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE cannotReadRepositoryDb(CompletionStatus completionStatus) {
        return cannotReadRepositoryDb(completionStatus, null);
    }

    public INITIALIZE cannotReadRepositoryDb(Throwable th) {
        return cannotReadRepositoryDb(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE cannotReadRepositoryDb() {
        return cannotReadRepositoryDb(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE cannotAddInitialNaming(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(CANNOT_ADD_INITIAL_NAMING, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.cannotAddInitialNaming", null, ActivationSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE cannotAddInitialNaming(CompletionStatus completionStatus) {
        return cannotAddInitialNaming(completionStatus, null);
    }

    public INITIALIZE cannotAddInitialNaming(Throwable th) {
        return cannotAddInitialNaming(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE cannotAddInitialNaming() {
        return cannotAddInitialNaming(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL cannotWriteRepositoryDb(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079889, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.cannotWriteRepositoryDb", null, ActivationSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL cannotWriteRepositoryDb(CompletionStatus completionStatus) {
        return cannotWriteRepositoryDb(completionStatus, null);
    }

    public INTERNAL cannotWriteRepositoryDb(Throwable th) {
        return cannotWriteRepositoryDb(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL cannotWriteRepositoryDb() {
        return cannotWriteRepositoryDb(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL serverNotExpectedToRegister(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(SERVER_NOT_EXPECTED_TO_REGISTER, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.serverNotExpectedToRegister", null, ActivationSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL serverNotExpectedToRegister(CompletionStatus completionStatus) {
        return serverNotExpectedToRegister(completionStatus, null);
    }

    public INTERNAL serverNotExpectedToRegister(Throwable th) {
        return serverNotExpectedToRegister(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL serverNotExpectedToRegister() {
        return serverNotExpectedToRegister(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unableToStartProcess(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(UNABLE_TO_START_PROCESS, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.unableToStartProcess", null, ActivationSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unableToStartProcess(CompletionStatus completionStatus) {
        return unableToStartProcess(completionStatus, null);
    }

    public INTERNAL unableToStartProcess(Throwable th) {
        return unableToStartProcess(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL unableToStartProcess() {
        return unableToStartProcess(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL serverNotRunning(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(SERVER_NOT_RUNNING, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.serverNotRunning", null, ActivationSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL serverNotRunning(CompletionStatus completionStatus) {
        return serverNotRunning(completionStatus, null);
    }

    public INTERNAL serverNotRunning(Throwable th) {
        return serverNotRunning(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL serverNotRunning() {
        return serverNotRunning(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST errorInBadServerIdHandler(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079889, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ACTIVATION.errorInBadServerIdHandler", null, ActivationSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST errorInBadServerIdHandler(CompletionStatus completionStatus) {
        return errorInBadServerIdHandler(completionStatus, null);
    }

    public OBJECT_NOT_EXIST errorInBadServerIdHandler(Throwable th) {
        return errorInBadServerIdHandler(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST errorInBadServerIdHandler() {
        return errorInBadServerIdHandler(CompletionStatus.COMPLETED_NO, null);
    }
}
