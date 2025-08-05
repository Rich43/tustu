package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.UNKNOWN;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/UtilSystemException.class */
public class UtilSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.UtilSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new UtilSystemException(logger);
        }
    };
    public static final int STUB_FACTORY_COULD_NOT_MAKE_STUB = 1398080889;
    public static final int ERROR_IN_MAKE_STUB_FROM_REPOSITORY_ID = 1398080890;
    public static final int CLASS_CAST_EXCEPTION_IN_LOAD_STUB = 1398080891;
    public static final int EXCEPTION_IN_LOAD_STUB = 1398080892;
    public static final int NO_POA = 1398080890;
    public static final int CONNECT_WRONG_ORB = 1398080891;
    public static final int CONNECT_NO_TIE = 1398080892;
    public static final int CONNECT_TIE_WRONG_ORB = 1398080893;
    public static final int CONNECT_TIE_NO_SERVANT = 1398080894;
    public static final int LOAD_TIE_FAILED = 1398080895;
    public static final int BAD_HEX_DIGIT = 1398080889;
    public static final int UNABLE_LOCATE_VALUE_HELPER = 1398080890;
    public static final int INVALID_INDIRECTION = 1398080891;
    public static final int OBJECT_NOT_CONNECTED = 1398080889;
    public static final int COULD_NOT_LOAD_STUB = 1398080890;
    public static final int OBJECT_NOT_EXPORTED = 1398080891;
    public static final int ERROR_SET_OBJECT_FIELD = 1398080889;
    public static final int ERROR_SET_BOOLEAN_FIELD = 1398080890;
    public static final int ERROR_SET_BYTE_FIELD = 1398080891;
    public static final int ERROR_SET_CHAR_FIELD = 1398080892;
    public static final int ERROR_SET_SHORT_FIELD = 1398080893;
    public static final int ERROR_SET_INT_FIELD = 1398080894;
    public static final int ERROR_SET_LONG_FIELD = 1398080895;
    public static final int ERROR_SET_FLOAT_FIELD = 1398080896;
    public static final int ERROR_SET_DOUBLE_FIELD = 1398080897;
    public static final int ILLEGAL_FIELD_ACCESS = 1398080898;
    public static final int BAD_BEGIN_UNMARSHAL_CUSTOM_VALUE = 1398080899;
    public static final int CLASS_NOT_FOUND = 1398080900;
    public static final int UNKNOWN_SYSEX = 1398080889;

    public UtilSystemException(Logger logger) {
        super(logger);
    }

    public static UtilSystemException get(ORB orb, String str) {
        return (UtilSystemException) orb.getLogWrapper(str, "UTIL", factory);
    }

    public static UtilSystemException get(String str) {
        return (UtilSystemException) ORB.staticGetLogWrapper(str, "UTIL", factory);
    }

    public BAD_OPERATION stubFactoryCouldNotMakeStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080889, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "UTIL.stubFactoryCouldNotMakeStub", null, UtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION stubFactoryCouldNotMakeStub(CompletionStatus completionStatus) {
        return stubFactoryCouldNotMakeStub(completionStatus, null);
    }

    public BAD_OPERATION stubFactoryCouldNotMakeStub(Throwable th) {
        return stubFactoryCouldNotMakeStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION stubFactoryCouldNotMakeStub() {
        return stubFactoryCouldNotMakeStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION errorInMakeStubFromRepositoryId(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080890, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "UTIL.errorInMakeStubFromRepositoryId", null, UtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION errorInMakeStubFromRepositoryId(CompletionStatus completionStatus) {
        return errorInMakeStubFromRepositoryId(completionStatus, null);
    }

    public BAD_OPERATION errorInMakeStubFromRepositoryId(Throwable th) {
        return errorInMakeStubFromRepositoryId(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION errorInMakeStubFromRepositoryId() {
        return errorInMakeStubFromRepositoryId(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION classCastExceptionInLoadStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080891, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "UTIL.classCastExceptionInLoadStub", null, UtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION classCastExceptionInLoadStub(CompletionStatus completionStatus) {
        return classCastExceptionInLoadStub(completionStatus, null);
    }

    public BAD_OPERATION classCastExceptionInLoadStub(Throwable th) {
        return classCastExceptionInLoadStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION classCastExceptionInLoadStub() {
        return classCastExceptionInLoadStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION exceptionInLoadStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398080892, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "UTIL.exceptionInLoadStub", null, UtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION exceptionInLoadStub(CompletionStatus completionStatus) {
        return exceptionInLoadStub(completionStatus, null);
    }

    public BAD_OPERATION exceptionInLoadStub(Throwable th) {
        return exceptionInLoadStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION exceptionInLoadStub() {
        return exceptionInLoadStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM noPoa(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080890, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.noPoa", null, UtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM noPoa(CompletionStatus completionStatus) {
        return noPoa(completionStatus, null);
    }

    public BAD_PARAM noPoa(Throwable th) {
        return noPoa(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM noPoa() {
        return noPoa(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM connectWrongOrb(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080891, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "UTIL.connectWrongOrb", null, UtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM connectWrongOrb(CompletionStatus completionStatus) {
        return connectWrongOrb(completionStatus, null);
    }

    public BAD_PARAM connectWrongOrb(Throwable th) {
        return connectWrongOrb(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM connectWrongOrb() {
        return connectWrongOrb(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM connectNoTie(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080892, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.connectNoTie", null, UtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM connectNoTie(CompletionStatus completionStatus) {
        return connectNoTie(completionStatus, null);
    }

    public BAD_PARAM connectNoTie(Throwable th) {
        return connectNoTie(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM connectNoTie() {
        return connectNoTie(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM connectTieWrongOrb(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080893, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.connectTieWrongOrb", null, UtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM connectTieWrongOrb(CompletionStatus completionStatus) {
        return connectTieWrongOrb(completionStatus, null);
    }

    public BAD_PARAM connectTieWrongOrb(Throwable th) {
        return connectTieWrongOrb(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM connectTieWrongOrb() {
        return connectTieWrongOrb(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM connectTieNoServant(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080894, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.connectTieNoServant", null, UtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM connectTieNoServant(CompletionStatus completionStatus) {
        return connectTieNoServant(completionStatus, null);
    }

    public BAD_PARAM connectTieNoServant(Throwable th) {
        return connectTieNoServant(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM connectTieNoServant() {
        return connectTieNoServant(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM loadTieFailed(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398080895, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "UTIL.loadTieFailed", new Object[]{obj}, UtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM loadTieFailed(CompletionStatus completionStatus, Object obj) {
        return loadTieFailed(completionStatus, null, obj);
    }

    public BAD_PARAM loadTieFailed(Throwable th, Object obj) {
        return loadTieFailed(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM loadTieFailed(Object obj) {
        return loadTieFailed(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION badHexDigit(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398080889, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.badHexDigit", null, UtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badHexDigit(CompletionStatus completionStatus) {
        return badHexDigit(completionStatus, null);
    }

    public DATA_CONVERSION badHexDigit(Throwable th) {
        return badHexDigit(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badHexDigit() {
        return badHexDigit(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unableLocateValueHelper(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398080890, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.unableLocateValueHelper", null, UtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unableLocateValueHelper(CompletionStatus completionStatus) {
        return unableLocateValueHelper(completionStatus, null);
    }

    public MARSHAL unableLocateValueHelper(Throwable th) {
        return unableLocateValueHelper(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL unableLocateValueHelper() {
        return unableLocateValueHelper(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL invalidIndirection(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398080891, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.invalidIndirection", new Object[]{obj}, UtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL invalidIndirection(CompletionStatus completionStatus, Object obj) {
        return invalidIndirection(completionStatus, null, obj);
    }

    public MARSHAL invalidIndirection(Throwable th, Object obj) {
        return invalidIndirection(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL invalidIndirection(Object obj) {
        return invalidIndirection(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INV_OBJREF objectNotConnected(CompletionStatus completionStatus, Throwable th, Object obj) {
        INV_OBJREF inv_objref = new INV_OBJREF(1398080889, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.objectNotConnected", new Object[]{obj}, UtilSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF objectNotConnected(CompletionStatus completionStatus, Object obj) {
        return objectNotConnected(completionStatus, null, obj);
    }

    public INV_OBJREF objectNotConnected(Throwable th, Object obj) {
        return objectNotConnected(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INV_OBJREF objectNotConnected(Object obj) {
        return objectNotConnected(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INV_OBJREF couldNotLoadStub(CompletionStatus completionStatus, Throwable th, Object obj) {
        INV_OBJREF inv_objref = new INV_OBJREF(1398080890, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.couldNotLoadStub", new Object[]{obj}, UtilSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF couldNotLoadStub(CompletionStatus completionStatus, Object obj) {
        return couldNotLoadStub(completionStatus, null, obj);
    }

    public INV_OBJREF couldNotLoadStub(Throwable th, Object obj) {
        return couldNotLoadStub(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INV_OBJREF couldNotLoadStub(Object obj) {
        return couldNotLoadStub(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INV_OBJREF objectNotExported(CompletionStatus completionStatus, Throwable th, Object obj) {
        INV_OBJREF inv_objref = new INV_OBJREF(1398080891, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.objectNotExported", new Object[]{obj}, UtilSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF objectNotExported(CompletionStatus completionStatus, Object obj) {
        return objectNotExported(completionStatus, null, obj);
    }

    public INV_OBJREF objectNotExported(Throwable th, Object obj) {
        return objectNotExported(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INV_OBJREF objectNotExported(Object obj) {
        return objectNotExported(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL errorSetObjectField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080889, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetObjectField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetObjectField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetObjectField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetObjectField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetObjectField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetObjectField(Object obj, Object obj2, Object obj3) {
        return errorSetObjectField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetBooleanField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080890, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetBooleanField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetBooleanField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetBooleanField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetBooleanField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetBooleanField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetBooleanField(Object obj, Object obj2, Object obj3) {
        return errorSetBooleanField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetByteField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080891, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetByteField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetByteField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetByteField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetByteField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetByteField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetByteField(Object obj, Object obj2, Object obj3) {
        return errorSetByteField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetCharField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080892, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetCharField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetCharField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetCharField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetCharField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetCharField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetCharField(Object obj, Object obj2, Object obj3) {
        return errorSetCharField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetShortField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080893, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetShortField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetShortField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetShortField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetShortField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetShortField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetShortField(Object obj, Object obj2, Object obj3) {
        return errorSetShortField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetIntField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080894, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetIntField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetIntField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetIntField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetIntField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetIntField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetIntField(Object obj, Object obj2, Object obj3) {
        return errorSetIntField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetLongField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(1398080895, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetLongField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetLongField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetLongField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetLongField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetLongField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetLongField(Object obj, Object obj2, Object obj3) {
        return errorSetLongField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetFloatField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(ERROR_SET_FLOAT_FIELD, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetFloatField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetFloatField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetFloatField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetFloatField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetFloatField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetFloatField(Object obj, Object obj2, Object obj3) {
        return errorSetFloatField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetDoubleField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(ERROR_SET_DOUBLE_FIELD, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.errorSetDoubleField", new Object[]{obj, obj2, obj3}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorSetDoubleField(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return errorSetDoubleField(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL errorSetDoubleField(Throwable th, Object obj, Object obj2, Object obj3) {
        return errorSetDoubleField(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL errorSetDoubleField(Object obj, Object obj2, Object obj3) {
        return errorSetDoubleField(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL illegalFieldAccess(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(ILLEGAL_FIELD_ACCESS, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.illegalFieldAccess", new Object[]{obj}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL illegalFieldAccess(CompletionStatus completionStatus, Object obj) {
        return illegalFieldAccess(completionStatus, null, obj);
    }

    public INTERNAL illegalFieldAccess(Throwable th, Object obj) {
        return illegalFieldAccess(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL illegalFieldAccess(Object obj) {
        return illegalFieldAccess(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL badBeginUnmarshalCustomValue(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(BAD_BEGIN_UNMARSHAL_CUSTOM_VALUE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.badBeginUnmarshalCustomValue", null, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badBeginUnmarshalCustomValue(CompletionStatus completionStatus) {
        return badBeginUnmarshalCustomValue(completionStatus, null);
    }

    public INTERNAL badBeginUnmarshalCustomValue(Throwable th) {
        return badBeginUnmarshalCustomValue(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badBeginUnmarshalCustomValue() {
        return badBeginUnmarshalCustomValue(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL classNotFound(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(CLASS_NOT_FOUND, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.classNotFound", new Object[]{obj}, UtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL classNotFound(CompletionStatus completionStatus, Object obj) {
        return classNotFound(completionStatus, null, obj);
    }

    public INTERNAL classNotFound(Throwable th, Object obj) {
        return classNotFound(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL classNotFound(Object obj) {
        return classNotFound(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public UNKNOWN unknownSysex(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398080889, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "UTIL.unknownSysex", null, UtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownSysex(CompletionStatus completionStatus) {
        return unknownSysex(completionStatus, null);
    }

    public UNKNOWN unknownSysex(Throwable th) {
        return unknownSysex(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownSysex() {
        return unknownSysex(CompletionStatus.COMPLETED_NO, null);
    }
}
