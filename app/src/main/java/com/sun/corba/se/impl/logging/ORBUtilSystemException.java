package com.sun.corba.se.impl.logging;

import com.sun.corba.se.spi.logging.LogWrapperBase;
import com.sun.corba.se.spi.logging.LogWrapperFactory;
import com.sun.corba.se.spi.orb.ORB;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.BAD_TYPECODE;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.INV_OBJREF;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.TRANSIENT;
import org.omg.CORBA.UNKNOWN;

/* loaded from: rt.jar:com/sun/corba/se/impl/logging/ORBUtilSystemException.class */
public class ORBUtilSystemException extends LogWrapperBase {
    private static LogWrapperFactory factory = new LogWrapperFactory() { // from class: com.sun.corba.se.impl.logging.ORBUtilSystemException.1
        @Override // com.sun.corba.se.spi.logging.LogWrapperFactory
        public LogWrapperBase create(Logger logger) {
            return new ORBUtilSystemException(logger);
        }
    };
    public static final int ADAPTER_ID_NOT_AVAILABLE = 1398079689;
    public static final int SERVER_ID_NOT_AVAILABLE = 1398079690;
    public static final int ORB_ID_NOT_AVAILABLE = 1398079691;
    public static final int OBJECT_ADAPTER_ID_NOT_AVAILABLE = 1398079692;
    public static final int CONNECTING_SERVANT = 1398079693;
    public static final int EXTRACT_WRONG_TYPE = 1398079694;
    public static final int EXTRACT_WRONG_TYPE_LIST = 1398079695;
    public static final int BAD_STRING_BOUNDS = 1398079696;
    public static final int INSERT_OBJECT_INCOMPATIBLE = 1398079698;
    public static final int INSERT_OBJECT_FAILED = 1398079699;
    public static final int EXTRACT_OBJECT_INCOMPATIBLE = 1398079700;
    public static final int FIXED_NOT_MATCH = 1398079701;
    public static final int FIXED_BAD_TYPECODE = 1398079702;
    public static final int SET_EXCEPTION_CALLED_NULL_ARGS = 1398079711;
    public static final int SET_EXCEPTION_CALLED_BAD_TYPE = 1398079712;
    public static final int CONTEXT_CALLED_OUT_OF_ORDER = 1398079713;
    public static final int BAD_ORB_CONFIGURATOR = 1398079714;
    public static final int ORB_CONFIGURATOR_ERROR = 1398079715;
    public static final int ORB_DESTROYED = 1398079716;
    public static final int NEGATIVE_BOUNDS = 1398079717;
    public static final int EXTRACT_NOT_INITIALIZED = 1398079718;
    public static final int EXTRACT_OBJECT_FAILED = 1398079719;
    public static final int METHOD_NOT_FOUND_IN_TIE = 1398079720;
    public static final int CLASS_NOT_FOUND1 = 1398079721;
    public static final int CLASS_NOT_FOUND2 = 1398079722;
    public static final int CLASS_NOT_FOUND3 = 1398079723;
    public static final int GET_DELEGATE_SERVANT_NOT_ACTIVE = 1398079724;
    public static final int GET_DELEGATE_WRONG_POLICY = 1398079725;
    public static final int SET_DELEGATE_REQUIRES_STUB = 1398079726;
    public static final int GET_DELEGATE_REQUIRES_STUB = 1398079727;
    public static final int GET_TYPE_IDS_REQUIRES_STUB = 1398079728;
    public static final int GET_ORB_REQUIRES_STUB = 1398079729;
    public static final int CONNECT_REQUIRES_STUB = 1398079730;
    public static final int IS_LOCAL_REQUIRES_STUB = 1398079731;
    public static final int REQUEST_REQUIRES_STUB = 1398079732;
    public static final int BAD_ACTIVATE_TIE_CALL = 1398079733;
    public static final int IO_EXCEPTION_ON_CLOSE = 1398079734;
    public static final int NULL_PARAM = 1398079689;
    public static final int UNABLE_FIND_VALUE_FACTORY = 1398079690;
    public static final int ABSTRACT_FROM_NON_ABSTRACT = 1398079691;
    public static final int INVALID_TAGGED_PROFILE = 1398079692;
    public static final int OBJREF_FROM_FOREIGN_ORB = 1398079693;
    public static final int LOCAL_OBJECT_NOT_ALLOWED = 1398079694;
    public static final int NULL_OBJECT_REFERENCE = 1398079695;
    public static final int COULD_NOT_LOAD_CLASS = 1398079696;
    public static final int BAD_URL = 1398079697;
    public static final int FIELD_NOT_FOUND = 1398079698;
    public static final int ERROR_SETTING_FIELD = 1398079699;
    public static final int BOUNDS_ERROR_IN_DII_REQUEST = 1398079700;
    public static final int PERSISTENT_SERVER_INIT_ERROR = 1398079701;
    public static final int COULD_NOT_CREATE_ARRAY = 1398079702;
    public static final int COULD_NOT_SET_ARRAY = 1398079703;
    public static final int ILLEGAL_BOOTSTRAP_OPERATION = 1398079704;
    public static final int BOOTSTRAP_RUNTIME_EXCEPTION = 1398079705;
    public static final int BOOTSTRAP_EXCEPTION = 1398079706;
    public static final int STRING_EXPECTED = 1398079707;
    public static final int INVALID_TYPECODE_KIND = 1398079708;
    public static final int SOCKET_FACTORY_AND_CONTACT_INFO_LIST_AT_SAME_TIME = 1398079709;
    public static final int ACCEPTORS_AND_LEGACY_SOCKET_FACTORY_AT_SAME_TIME = 1398079710;
    public static final int BAD_ORB_FOR_SERVANT = 1398079711;
    public static final int INVALID_REQUEST_PARTITIONING_POLICY_VALUE = 1398079712;
    public static final int INVALID_REQUEST_PARTITIONING_COMPONENT_VALUE = 1398079713;
    public static final int INVALID_REQUEST_PARTITIONING_ID = 1398079714;
    public static final int ERROR_IN_SETTING_DYNAMIC_STUB_FACTORY_FACTORY = 1398079715;
    public static final int DSIMETHOD_NOTCALLED = 1398079689;
    public static final int ARGUMENTS_CALLED_MULTIPLE = 1398079690;
    public static final int ARGUMENTS_CALLED_AFTER_EXCEPTION = 1398079691;
    public static final int ARGUMENTS_CALLED_NULL_ARGS = 1398079692;
    public static final int ARGUMENTS_NOT_CALLED = 1398079693;
    public static final int SET_RESULT_CALLED_MULTIPLE = 1398079694;
    public static final int SET_RESULT_AFTER_EXCEPTION = 1398079695;
    public static final int SET_RESULT_CALLED_NULL_ARGS = 1398079696;
    public static final int BAD_REMOTE_TYPECODE = 1398079689;
    public static final int UNRESOLVED_RECURSIVE_TYPECODE = 1398079690;
    public static final int CONNECT_FAILURE = 1398079689;
    public static final int CONNECTION_CLOSE_REBIND = 1398079690;
    public static final int WRITE_ERROR_SEND = 1398079691;
    public static final int GET_PROPERTIES_ERROR = 1398079692;
    public static final int BOOTSTRAP_SERVER_NOT_AVAIL = 1398079693;
    public static final int INVOKE_ERROR = 1398079694;
    public static final int DEFAULT_CREATE_SERVER_SOCKET_GIVEN_NON_IIOP_CLEAR_TEXT = 1398079695;
    public static final int CONNECTION_ABORT = 1398079696;
    public static final int CONNECTION_REBIND = 1398079697;
    public static final int RECV_MSG_ERROR = 1398079698;
    public static final int IOEXCEPTION_WHEN_READING_CONNECTION = 1398079699;
    public static final int SELECTION_KEY_INVALID = 1398079700;
    public static final int EXCEPTION_IN_ACCEPT = 1398079701;
    public static final int SECURITY_EXCEPTION_IN_ACCEPT = 1398079702;
    public static final int TRANSPORT_READ_TIMEOUT_EXCEEDED = 1398079703;
    public static final int CREATE_LISTENER_FAILED = 1398079704;
    public static final int BUFFER_READ_MANAGER_TIMEOUT = 1398079705;
    public static final int BAD_STRINGIFIED_IOR_LEN = 1398079689;
    public static final int BAD_STRINGIFIED_IOR = 1398079690;
    public static final int BAD_MODIFIER = 1398079691;
    public static final int CODESET_INCOMPATIBLE = 1398079692;
    public static final int BAD_HEX_DIGIT = 1398079693;
    public static final int BAD_UNICODE_PAIR = 1398079694;
    public static final int BTC_RESULT_MORE_THAN_ONE_CHAR = 1398079695;
    public static final int BAD_CODESETS_FROM_CLIENT = 1398079696;
    public static final int INVALID_SINGLE_CHAR_CTB = 1398079697;
    public static final int BAD_GIOP_1_1_CTB = 1398079698;
    public static final int BAD_SEQUENCE_BOUNDS = 1398079700;
    public static final int ILLEGAL_SOCKET_FACTORY_TYPE = 1398079701;
    public static final int BAD_CUSTOM_SOCKET_FACTORY = 1398079702;
    public static final int FRAGMENT_SIZE_MINIMUM = 1398079703;
    public static final int FRAGMENT_SIZE_DIV = 1398079704;
    public static final int ORB_INITIALIZER_FAILURE = 1398079705;
    public static final int ORB_INITIALIZER_TYPE = 1398079706;
    public static final int ORB_INITIALREFERENCE_SYNTAX = 1398079707;
    public static final int ACCEPTOR_INSTANTIATION_FAILURE = 1398079708;
    public static final int ACCEPTOR_INSTANTIATION_TYPE_FAILURE = 1398079709;
    public static final int ILLEGAL_CONTACT_INFO_LIST_FACTORY_TYPE = 1398079710;
    public static final int BAD_CONTACT_INFO_LIST_FACTORY = 1398079711;
    public static final int ILLEGAL_IOR_TO_SOCKET_INFO_TYPE = 1398079712;
    public static final int BAD_CUSTOM_IOR_TO_SOCKET_INFO = 1398079713;
    public static final int ILLEGAL_IIOP_PRIMARY_TO_CONTACT_INFO_TYPE = 1398079714;
    public static final int BAD_CUSTOM_IIOP_PRIMARY_TO_CONTACT_INFO = 1398079715;
    public static final int BAD_CORBALOC_STRING = 1398079689;
    public static final int NO_PROFILE_PRESENT = 1398079690;
    public static final int CANNOT_CREATE_ORBID_DB = 1398079689;
    public static final int CANNOT_READ_ORBID_DB = 1398079690;
    public static final int CANNOT_WRITE_ORBID_DB = 1398079691;
    public static final int GET_SERVER_PORT_CALLED_BEFORE_ENDPOINTS_INITIALIZED = 1398079692;
    public static final int PERSISTENT_SERVERPORT_NOT_SET = 1398079693;
    public static final int PERSISTENT_SERVERID_NOT_SET = 1398079694;
    public static final int NON_EXISTENT_ORBID = 1398079689;
    public static final int NO_SERVER_SUBCONTRACT = 1398079690;
    public static final int SERVER_SC_TEMP_SIZE = 1398079691;
    public static final int NO_CLIENT_SC_CLASS = 1398079692;
    public static final int SERVER_SC_NO_IIOP_PROFILE = 1398079693;
    public static final int GET_SYSTEM_EX_RETURNED_NULL = 1398079694;
    public static final int PEEKSTRING_FAILED = 1398079695;
    public static final int GET_LOCAL_HOST_FAILED = 1398079696;
    public static final int BAD_LOCATE_REQUEST_STATUS = 1398079698;
    public static final int STRINGIFY_WRITE_ERROR = 1398079699;
    public static final int BAD_GIOP_REQUEST_TYPE = 1398079700;
    public static final int ERROR_UNMARSHALING_USEREXC = 1398079701;
    public static final int RequestDispatcherRegistry_ERROR = 1398079702;
    public static final int LOCATIONFORWARD_ERROR = 1398079703;
    public static final int WRONG_CLIENTSC = 1398079704;
    public static final int BAD_SERVANT_READ_OBJECT = 1398079705;
    public static final int MULT_IIOP_PROF_NOT_SUPPORTED = 1398079706;
    public static final int GIOP_MAGIC_ERROR = 1398079708;
    public static final int GIOP_VERSION_ERROR = 1398079709;
    public static final int ILLEGAL_REPLY_STATUS = 1398079710;
    public static final int ILLEGAL_GIOP_MSG_TYPE = 1398079711;
    public static final int FRAGMENTATION_DISALLOWED = 1398079712;
    public static final int BAD_REPLYSTATUS = 1398079713;
    public static final int CTB_CONVERTER_FAILURE = 1398079714;
    public static final int BTC_CONVERTER_FAILURE = 1398079715;
    public static final int WCHAR_ARRAY_UNSUPPORTED_ENCODING = 1398079716;
    public static final int ILLEGAL_TARGET_ADDRESS_DISPOSITION = 1398079717;
    public static final int NULL_REPLY_IN_GET_ADDR_DISPOSITION = 1398079718;
    public static final int ORB_TARGET_ADDR_PREFERENCE_IN_EXTRACT_OBJECTKEY_INVALID = 1398079719;
    public static final int INVALID_ISSTREAMED_TCKIND = 1398079720;
    public static final int INVALID_JDK1_3_1_PATCH_LEVEL = 1398079721;
    public static final int SVCCTX_UNMARSHAL_ERROR = 1398079722;
    public static final int NULL_IOR = 1398079723;
    public static final int UNSUPPORTED_GIOP_VERSION = 1398079724;
    public static final int APPLICATION_EXCEPTION_IN_SPECIAL_METHOD = 1398079725;
    public static final int STATEMENT_NOT_REACHABLE1 = 1398079726;
    public static final int STATEMENT_NOT_REACHABLE2 = 1398079727;
    public static final int STATEMENT_NOT_REACHABLE3 = 1398079728;
    public static final int STATEMENT_NOT_REACHABLE4 = 1398079729;
    public static final int STATEMENT_NOT_REACHABLE5 = 1398079730;
    public static final int STATEMENT_NOT_REACHABLE6 = 1398079731;
    public static final int UNEXPECTED_DII_EXCEPTION = 1398079732;
    public static final int METHOD_SHOULD_NOT_BE_CALLED = 1398079733;
    public static final int CANCEL_NOT_SUPPORTED = 1398079734;
    public static final int EMPTY_STACK_RUN_SERVANT_POST_INVOKE = 1398079735;
    public static final int PROBLEM_WITH_EXCEPTION_TYPECODE = 1398079736;
    public static final int ILLEGAL_SUBCONTRACT_ID = 1398079737;
    public static final int BAD_SYSTEM_EXCEPTION_IN_LOCATE_REPLY = 1398079738;
    public static final int BAD_SYSTEM_EXCEPTION_IN_REPLY = 1398079739;
    public static final int BAD_COMPLETION_STATUS_IN_LOCATE_REPLY = 1398079740;
    public static final int BAD_COMPLETION_STATUS_IN_REPLY = 1398079741;
    public static final int BADKIND_CANNOT_OCCUR = 1398079742;
    public static final int ERROR_RESOLVING_ALIAS = 1398079743;
    public static final int TK_LONG_DOUBLE_NOT_SUPPORTED = 1398079744;
    public static final int TYPECODE_NOT_SUPPORTED = 1398079745;
    public static final int BOUNDS_CANNOT_OCCUR = 1398079747;
    public static final int NUM_INVOCATIONS_ALREADY_ZERO = 1398079749;
    public static final int ERROR_INIT_BADSERVERIDHANDLER = 1398079750;
    public static final int NO_TOA = 1398079751;
    public static final int NO_POA = 1398079752;
    public static final int INVOCATION_INFO_STACK_EMPTY = 1398079753;
    public static final int BAD_CODE_SET_STRING = 1398079754;
    public static final int UNKNOWN_NATIVE_CODESET = 1398079755;
    public static final int UNKNOWN_CONVERSION_CODE_SET = 1398079756;
    public static final int INVALID_CODE_SET_NUMBER = 1398079757;
    public static final int INVALID_CODE_SET_STRING = 1398079758;
    public static final int INVALID_CTB_CONVERTER_NAME = 1398079759;
    public static final int INVALID_BTC_CONVERTER_NAME = 1398079760;
    public static final int COULD_NOT_DUPLICATE_CDR_INPUT_STREAM = 1398079761;
    public static final int BOOTSTRAP_APPLICATION_EXCEPTION = 1398079762;
    public static final int DUPLICATE_INDIRECTION_OFFSET = 1398079763;
    public static final int BAD_MESSAGE_TYPE_FOR_CANCEL = 1398079764;
    public static final int DUPLICATE_EXCEPTION_DETAIL_MESSAGE = 1398079765;
    public static final int BAD_EXCEPTION_DETAIL_MESSAGE_SERVICE_CONTEXT_TYPE = 1398079766;
    public static final int UNEXPECTED_DIRECT_BYTE_BUFFER_WITH_NON_CHANNEL_SOCKET = 1398079767;
    public static final int UNEXPECTED_NON_DIRECT_BYTE_BUFFER_WITH_CHANNEL_SOCKET = 1398079768;
    public static final int INVALID_CONTACT_INFO_LIST_ITERATOR_FAILURE_EXCEPTION = 1398079770;
    public static final int REMARSHAL_WITH_NOWHERE_TO_GO = 1398079771;
    public static final int EXCEPTION_WHEN_SENDING_CLOSE_CONNECTION = 1398079772;
    public static final int INVOCATION_ERROR_IN_REFLECTIVE_TIE = 1398079773;
    public static final int BAD_HELPER_WRITE_METHOD = 1398079774;
    public static final int BAD_HELPER_READ_METHOD = 1398079775;
    public static final int BAD_HELPER_ID_METHOD = 1398079776;
    public static final int WRITE_UNDECLARED_EXCEPTION = 1398079777;
    public static final int READ_UNDECLARED_EXCEPTION = 1398079778;
    public static final int UNABLE_TO_SET_SOCKET_FACTORY_ORB = 1398079779;
    public static final int UNEXPECTED_EXCEPTION = 1398079780;
    public static final int NO_INVOCATION_HANDLER = 1398079781;
    public static final int INVALID_BUFF_MGR_STRATEGY = 1398079782;
    public static final int JAVA_STREAM_INIT_FAILED = 1398079783;
    public static final int DUPLICATE_ORB_VERSION_SERVICE_CONTEXT = 1398079784;
    public static final int DUPLICATE_SENDING_CONTEXT_SERVICE_CONTEXT = 1398079785;
    public static final int WORK_QUEUE_THREAD_INTERRUPTED = 1398079786;
    public static final int WORKER_THREAD_CREATED = 1398079792;
    public static final int WORKER_THREAD_THROWABLE_FROM_REQUEST_WORK = 1398079797;
    public static final int WORKER_THREAD_NOT_NEEDED = 1398079798;
    public static final int WORKER_THREAD_DO_WORK_THROWABLE = 1398079799;
    public static final int WORKER_THREAD_CAUGHT_UNEXPECTED_THROWABLE = 1398079800;
    public static final int WORKER_THREAD_CREATION_FAILURE = 1398079801;
    public static final int WORKER_THREAD_SET_NAME_FAILURE = 1398079802;
    public static final int WORK_QUEUE_REQUEST_WORK_NO_WORK_FOUND = 1398079804;
    public static final int THREAD_POOL_CLOSE_ERROR = 1398079814;
    public static final int THREAD_GROUP_IS_DESTROYED = 1398079815;
    public static final int THREAD_GROUP_HAS_ACTIVE_THREADS_IN_CLOSE = 1398079816;
    public static final int THREAD_GROUP_HAS_SUB_GROUPS_IN_CLOSE = 1398079817;
    public static final int THREAD_GROUP_DESTROY_FAILED = 1398079818;
    public static final int INTERRUPTED_JOIN_CALL_WHILE_CLOSING_THREAD_POOL = 1398079819;
    public static final int CHUNK_OVERFLOW = 1398079689;
    public static final int UNEXPECTED_EOF = 1398079690;
    public static final int READ_OBJECT_EXCEPTION = 1398079691;
    public static final int CHARACTER_OUTOFRANGE = 1398079692;
    public static final int DSI_RESULT_EXCEPTION = 1398079693;
    public static final int IIOPINPUTSTREAM_GROW = 1398079694;
    public static final int END_OF_STREAM = 1398079695;
    public static final int INVALID_OBJECT_KEY = 1398079696;
    public static final int MALFORMED_URL = 1398079697;
    public static final int VALUEHANDLER_READ_ERROR = 1398079698;
    public static final int VALUEHANDLER_READ_EXCEPTION = 1398079699;
    public static final int BAD_KIND = 1398079700;
    public static final int CNFE_READ_CLASS = 1398079701;
    public static final int BAD_REP_ID_INDIRECTION = 1398079702;
    public static final int BAD_CODEBASE_INDIRECTION = 1398079703;
    public static final int UNKNOWN_CODESET = 1398079704;
    public static final int WCHAR_DATA_IN_GIOP_1_0 = 1398079705;
    public static final int NEGATIVE_STRING_LENGTH = 1398079706;
    public static final int EXPECTED_TYPE_NULL_AND_NO_REP_ID = 1398079707;
    public static final int READ_VALUE_AND_NO_REP_ID = 1398079708;
    public static final int UNEXPECTED_ENCLOSING_VALUETYPE = 1398079710;
    public static final int POSITIVE_END_TAG = 1398079711;
    public static final int NULL_OUT_CALL = 1398079712;
    public static final int WRITE_LOCAL_OBJECT = 1398079713;
    public static final int BAD_INSERTOBJ_PARAM = 1398079714;
    public static final int CUSTOM_WRAPPER_WITH_CODEBASE = 1398079715;
    public static final int CUSTOM_WRAPPER_INDIRECTION = 1398079716;
    public static final int CUSTOM_WRAPPER_NOT_SINGLE_REPID = 1398079717;
    public static final int BAD_VALUE_TAG = 1398079718;
    public static final int BAD_TYPECODE_FOR_CUSTOM_VALUE = 1398079719;
    public static final int ERROR_INVOKING_HELPER_WRITE = 1398079720;
    public static final int BAD_DIGIT_IN_FIXED = 1398079721;
    public static final int REF_TYPE_INDIR_TYPE = 1398079722;
    public static final int BAD_RESERVED_LENGTH = 1398079723;
    public static final int NULL_NOT_ALLOWED = 1398079724;
    public static final int UNION_DISCRIMINATOR_ERROR = 1398079726;
    public static final int CANNOT_MARSHAL_NATIVE = 1398079727;
    public static final int CANNOT_MARSHAL_BAD_TCKIND = 1398079728;
    public static final int INVALID_INDIRECTION = 1398079729;
    public static final int INDIRECTION_NOT_FOUND = 1398079730;
    public static final int RECURSIVE_TYPECODE_ERROR = 1398079731;
    public static final int INVALID_SIMPLE_TYPECODE = 1398079732;
    public static final int INVALID_COMPLEX_TYPECODE = 1398079733;
    public static final int INVALID_TYPECODE_KIND_MARSHAL = 1398079734;
    public static final int UNEXPECTED_UNION_DEFAULT = 1398079735;
    public static final int ILLEGAL_UNION_DISCRIMINATOR_TYPE = 1398079736;
    public static final int COULD_NOT_SKIP_BYTES = 1398079737;
    public static final int BAD_CHUNK_LENGTH = 1398079738;
    public static final int UNABLE_TO_LOCATE_REP_ID_ARRAY = 1398079739;
    public static final int BAD_FIXED = 1398079740;
    public static final int READ_OBJECT_LOAD_CLASS_FAILURE = 1398079741;
    public static final int COULD_NOT_INSTANTIATE_HELPER = 1398079742;
    public static final int BAD_TOA_OAID = 1398079743;
    public static final int COULD_NOT_INVOKE_HELPER_READ_METHOD = 1398079744;
    public static final int COULD_NOT_FIND_CLASS = 1398079745;
    public static final int BAD_ARGUMENTS_NVLIST = 1398079746;
    public static final int STUB_CREATE_ERROR = 1398079747;
    public static final int JAVA_SERIALIZATION_EXCEPTION = 1398079748;
    public static final int GENERIC_NO_IMPL = 1398079689;
    public static final int CONTEXT_NOT_IMPLEMENTED = 1398079690;
    public static final int GETINTERFACE_NOT_IMPLEMENTED = 1398079691;
    public static final int SEND_DEFERRED_NOTIMPLEMENTED = 1398079692;
    public static final int LONG_DOUBLE_NOT_IMPLEMENTED = 1398079693;
    public static final int NO_SERVER_SC_IN_DISPATCH = 1398079689;
    public static final int ORB_CONNECT_ERROR = 1398079690;
    public static final int ADAPTER_INACTIVE_IN_ACTIVATION = 1398079691;
    public static final int LOCATE_UNKNOWN_OBJECT = 1398079689;
    public static final int BAD_SERVER_ID = 1398079690;
    public static final int BAD_SKELETON = 1398079691;
    public static final int SERVANT_NOT_FOUND = 1398079692;
    public static final int NO_OBJECT_ADAPTER_FACTORY = 1398079693;
    public static final int BAD_ADAPTER_ID = 1398079694;
    public static final int DYN_ANY_DESTROYED = 1398079695;
    public static final int REQUEST_CANCELED = 1398079689;
    public static final int UNKNOWN_CORBA_EXC = 1398079689;
    public static final int RUNTIMEEXCEPTION = 1398079690;
    public static final int UNKNOWN_SERVER_ERROR = 1398079691;
    public static final int UNKNOWN_DSI_SYSEX = 1398079692;
    public static final int UNKNOWN_SYSEX = 1398079693;
    public static final int WRONG_INTERFACE_DEF = 1398079694;
    public static final int NO_INTERFACE_DEF_STUB = 1398079695;
    public static final int UNKNOWN_EXCEPTION_IN_DISPATCH = 1398079697;

    public ORBUtilSystemException(Logger logger) {
        super(logger);
    }

    public static ORBUtilSystemException get(ORB orb, String str) {
        return (ORBUtilSystemException) orb.getLogWrapper(str, "ORBUTIL", factory);
    }

    public static ORBUtilSystemException get(String str) {
        return (ORBUtilSystemException) ORB.staticGetLogWrapper(str, "ORBUTIL", factory);
    }

    public BAD_OPERATION adapterIdNotAvailable(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079689, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.adapterIdNotAvailable", null, ORBUtilSystemException.class, bad_operation);
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
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079690, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.serverIdNotAvailable", null, ORBUtilSystemException.class, bad_operation);
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
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079691, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbIdNotAvailable", null, ORBUtilSystemException.class, bad_operation);
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
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079692, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.objectAdapterIdNotAvailable", null, ORBUtilSystemException.class, bad_operation);
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

    public BAD_OPERATION connectingServant(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079693, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.connectingServant", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION connectingServant(CompletionStatus completionStatus) {
        return connectingServant(completionStatus, null);
    }

    public BAD_OPERATION connectingServant(Throwable th) {
        return connectingServant(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION connectingServant() {
        return connectingServant(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION extractWrongType(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079694, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.extractWrongType", new Object[]{obj, obj2}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION extractWrongType(CompletionStatus completionStatus, Object obj, Object obj2) {
        return extractWrongType(completionStatus, null, obj, obj2);
    }

    public BAD_OPERATION extractWrongType(Throwable th, Object obj, Object obj2) {
        return extractWrongType(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public BAD_OPERATION extractWrongType(Object obj, Object obj2) {
        return extractWrongType(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public BAD_OPERATION extractWrongTypeList(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079695, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.extractWrongTypeList", new Object[]{obj, obj2}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION extractWrongTypeList(CompletionStatus completionStatus, Object obj, Object obj2) {
        return extractWrongTypeList(completionStatus, null, obj, obj2);
    }

    public BAD_OPERATION extractWrongTypeList(Throwable th, Object obj, Object obj2) {
        return extractWrongTypeList(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public BAD_OPERATION extractWrongTypeList(Object obj, Object obj2) {
        return extractWrongTypeList(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public BAD_OPERATION badStringBounds(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079696, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badStringBounds", new Object[]{obj, obj2}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION badStringBounds(CompletionStatus completionStatus, Object obj, Object obj2) {
        return badStringBounds(completionStatus, null, obj, obj2);
    }

    public BAD_OPERATION badStringBounds(Throwable th, Object obj, Object obj2) {
        return badStringBounds(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public BAD_OPERATION badStringBounds(Object obj, Object obj2) {
        return badStringBounds(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public BAD_OPERATION insertObjectIncompatible(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079698, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.insertObjectIncompatible", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION insertObjectIncompatible(CompletionStatus completionStatus) {
        return insertObjectIncompatible(completionStatus, null);
    }

    public BAD_OPERATION insertObjectIncompatible(Throwable th) {
        return insertObjectIncompatible(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION insertObjectIncompatible() {
        return insertObjectIncompatible(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION insertObjectFailed(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079699, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.insertObjectFailed", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION insertObjectFailed(CompletionStatus completionStatus) {
        return insertObjectFailed(completionStatus, null);
    }

    public BAD_OPERATION insertObjectFailed(Throwable th) {
        return insertObjectFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION insertObjectFailed() {
        return insertObjectFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION extractObjectIncompatible(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079700, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.extractObjectIncompatible", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION extractObjectIncompatible(CompletionStatus completionStatus) {
        return extractObjectIncompatible(completionStatus, null);
    }

    public BAD_OPERATION extractObjectIncompatible(Throwable th) {
        return extractObjectIncompatible(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION extractObjectIncompatible() {
        return extractObjectIncompatible(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION fixedNotMatch(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079701, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.fixedNotMatch", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION fixedNotMatch(CompletionStatus completionStatus) {
        return fixedNotMatch(completionStatus, null);
    }

    public BAD_OPERATION fixedNotMatch(Throwable th) {
        return fixedNotMatch(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION fixedNotMatch() {
        return fixedNotMatch(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION fixedBadTypecode(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079702, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.fixedBadTypecode", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION fixedBadTypecode(CompletionStatus completionStatus) {
        return fixedBadTypecode(completionStatus, null);
    }

    public BAD_OPERATION fixedBadTypecode(Throwable th) {
        return fixedBadTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION fixedBadTypecode() {
        return fixedBadTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION setExceptionCalledNullArgs(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079711, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.setExceptionCalledNullArgs", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION setExceptionCalledNullArgs(CompletionStatus completionStatus) {
        return setExceptionCalledNullArgs(completionStatus, null);
    }

    public BAD_OPERATION setExceptionCalledNullArgs(Throwable th) {
        return setExceptionCalledNullArgs(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION setExceptionCalledNullArgs() {
        return setExceptionCalledNullArgs(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION setExceptionCalledBadType(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079712, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.setExceptionCalledBadType", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION setExceptionCalledBadType(CompletionStatus completionStatus) {
        return setExceptionCalledBadType(completionStatus, null);
    }

    public BAD_OPERATION setExceptionCalledBadType(Throwable th) {
        return setExceptionCalledBadType(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION setExceptionCalledBadType() {
        return setExceptionCalledBadType(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION contextCalledOutOfOrder(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079713, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.contextCalledOutOfOrder", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION contextCalledOutOfOrder(CompletionStatus completionStatus) {
        return contextCalledOutOfOrder(completionStatus, null);
    }

    public BAD_OPERATION contextCalledOutOfOrder(Throwable th) {
        return contextCalledOutOfOrder(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION contextCalledOutOfOrder() {
        return contextCalledOutOfOrder(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION badOrbConfigurator(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079714, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badOrbConfigurator", new Object[]{obj}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION badOrbConfigurator(CompletionStatus completionStatus, Object obj) {
        return badOrbConfigurator(completionStatus, null, obj);
    }

    public BAD_OPERATION badOrbConfigurator(Throwable th, Object obj) {
        return badOrbConfigurator(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_OPERATION badOrbConfigurator(Object obj) {
        return badOrbConfigurator(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_OPERATION orbConfiguratorError(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079715, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbConfiguratorError", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION orbConfiguratorError(CompletionStatus completionStatus) {
        return orbConfiguratorError(completionStatus, null);
    }

    public BAD_OPERATION orbConfiguratorError(Throwable th) {
        return orbConfiguratorError(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION orbConfiguratorError() {
        return orbConfiguratorError(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION orbDestroyed(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079716, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbDestroyed", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION orbDestroyed(CompletionStatus completionStatus) {
        return orbDestroyed(completionStatus, null);
    }

    public BAD_OPERATION orbDestroyed(Throwable th) {
        return orbDestroyed(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION orbDestroyed() {
        return orbDestroyed(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION negativeBounds(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079717, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.negativeBounds", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION negativeBounds(CompletionStatus completionStatus) {
        return negativeBounds(completionStatus, null);
    }

    public BAD_OPERATION negativeBounds(Throwable th) {
        return negativeBounds(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION negativeBounds() {
        return negativeBounds(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION extractNotInitialized(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079718, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.extractNotInitialized", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION extractNotInitialized(CompletionStatus completionStatus) {
        return extractNotInitialized(completionStatus, null);
    }

    public BAD_OPERATION extractNotInitialized(Throwable th) {
        return extractNotInitialized(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION extractNotInitialized() {
        return extractNotInitialized(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION extractObjectFailed(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079719, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.extractObjectFailed", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION extractObjectFailed(CompletionStatus completionStatus) {
        return extractObjectFailed(completionStatus, null);
    }

    public BAD_OPERATION extractObjectFailed(Throwable th) {
        return extractObjectFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION extractObjectFailed() {
        return extractObjectFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION methodNotFoundInTie(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079720, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.methodNotFoundInTie", new Object[]{obj, obj2}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION methodNotFoundInTie(CompletionStatus completionStatus, Object obj, Object obj2) {
        return methodNotFoundInTie(completionStatus, null, obj, obj2);
    }

    public BAD_OPERATION methodNotFoundInTie(Throwable th, Object obj, Object obj2) {
        return methodNotFoundInTie(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public BAD_OPERATION methodNotFoundInTie(Object obj, Object obj2) {
        return methodNotFoundInTie(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public BAD_OPERATION classNotFound1(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079721, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.classNotFound1", new Object[]{obj}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION classNotFound1(CompletionStatus completionStatus, Object obj) {
        return classNotFound1(completionStatus, null, obj);
    }

    public BAD_OPERATION classNotFound1(Throwable th, Object obj) {
        return classNotFound1(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_OPERATION classNotFound1(Object obj) {
        return classNotFound1(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_OPERATION classNotFound2(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079722, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.classNotFound2", new Object[]{obj}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION classNotFound2(CompletionStatus completionStatus, Object obj) {
        return classNotFound2(completionStatus, null, obj);
    }

    public BAD_OPERATION classNotFound2(Throwable th, Object obj) {
        return classNotFound2(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_OPERATION classNotFound2(Object obj) {
        return classNotFound2(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_OPERATION classNotFound3(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079723, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.classNotFound3", new Object[]{obj}, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION classNotFound3(CompletionStatus completionStatus, Object obj) {
        return classNotFound3(completionStatus, null, obj);
    }

    public BAD_OPERATION classNotFound3(Throwable th, Object obj) {
        return classNotFound3(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_OPERATION classNotFound3(Object obj) {
        return classNotFound3(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_OPERATION getDelegateServantNotActive(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079724, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getDelegateServantNotActive", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION getDelegateServantNotActive(CompletionStatus completionStatus) {
        return getDelegateServantNotActive(completionStatus, null);
    }

    public BAD_OPERATION getDelegateServantNotActive(Throwable th) {
        return getDelegateServantNotActive(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION getDelegateServantNotActive() {
        return getDelegateServantNotActive(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION getDelegateWrongPolicy(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079725, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getDelegateWrongPolicy", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION getDelegateWrongPolicy(CompletionStatus completionStatus) {
        return getDelegateWrongPolicy(completionStatus, null);
    }

    public BAD_OPERATION getDelegateWrongPolicy(Throwable th) {
        return getDelegateWrongPolicy(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION getDelegateWrongPolicy() {
        return getDelegateWrongPolicy(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION setDelegateRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079726, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.setDelegateRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION setDelegateRequiresStub(CompletionStatus completionStatus) {
        return setDelegateRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION setDelegateRequiresStub(Throwable th) {
        return setDelegateRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION setDelegateRequiresStub() {
        return setDelegateRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION getDelegateRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079727, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getDelegateRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION getDelegateRequiresStub(CompletionStatus completionStatus) {
        return getDelegateRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION getDelegateRequiresStub(Throwable th) {
        return getDelegateRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION getDelegateRequiresStub() {
        return getDelegateRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION getTypeIdsRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079728, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getTypeIdsRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION getTypeIdsRequiresStub(CompletionStatus completionStatus) {
        return getTypeIdsRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION getTypeIdsRequiresStub(Throwable th) {
        return getTypeIdsRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION getTypeIdsRequiresStub() {
        return getTypeIdsRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION getOrbRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079729, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getOrbRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION getOrbRequiresStub(CompletionStatus completionStatus) {
        return getOrbRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION getOrbRequiresStub(Throwable th) {
        return getOrbRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION getOrbRequiresStub() {
        return getOrbRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION connectRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079730, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.connectRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION connectRequiresStub(CompletionStatus completionStatus) {
        return connectRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION connectRequiresStub(Throwable th) {
        return connectRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION connectRequiresStub() {
        return connectRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION isLocalRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079731, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.isLocalRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION isLocalRequiresStub(CompletionStatus completionStatus) {
        return isLocalRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION isLocalRequiresStub(Throwable th) {
        return isLocalRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION isLocalRequiresStub() {
        return isLocalRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION requestRequiresStub(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079732, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.requestRequiresStub", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION requestRequiresStub(CompletionStatus completionStatus) {
        return requestRequiresStub(completionStatus, null);
    }

    public BAD_OPERATION requestRequiresStub(Throwable th) {
        return requestRequiresStub(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION requestRequiresStub() {
        return requestRequiresStub(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION badActivateTieCall(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079733, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badActivateTieCall", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION badActivateTieCall(CompletionStatus completionStatus) {
        return badActivateTieCall(completionStatus, null);
    }

    public BAD_OPERATION badActivateTieCall(Throwable th) {
        return badActivateTieCall(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION badActivateTieCall() {
        return badActivateTieCall(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_OPERATION ioExceptionOnClose(CompletionStatus completionStatus, Throwable th) {
        BAD_OPERATION bad_operation = new BAD_OPERATION(1398079734, completionStatus);
        if (th != null) {
            bad_operation.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.ioExceptionOnClose", null, ORBUtilSystemException.class, bad_operation);
        }
        return bad_operation;
    }

    public BAD_OPERATION ioExceptionOnClose(CompletionStatus completionStatus) {
        return ioExceptionOnClose(completionStatus, null);
    }

    public BAD_OPERATION ioExceptionOnClose(Throwable th) {
        return ioExceptionOnClose(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_OPERATION ioExceptionOnClose() {
        return ioExceptionOnClose(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM nullParam(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079689, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nullParam", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM nullParam(CompletionStatus completionStatus) {
        return nullParam(completionStatus, null);
    }

    public BAD_PARAM nullParam(Throwable th) {
        return nullParam(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM nullParam() {
        return nullParam(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM unableFindValueFactory(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079690, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.unableFindValueFactory", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM unableFindValueFactory(CompletionStatus completionStatus) {
        return unableFindValueFactory(completionStatus, null);
    }

    public BAD_PARAM unableFindValueFactory(Throwable th) {
        return unableFindValueFactory(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM unableFindValueFactory() {
        return unableFindValueFactory(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM abstractFromNonAbstract(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079691, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.abstractFromNonAbstract", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM abstractFromNonAbstract(CompletionStatus completionStatus) {
        return abstractFromNonAbstract(completionStatus, null);
    }

    public BAD_PARAM abstractFromNonAbstract(Throwable th) {
        return abstractFromNonAbstract(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM abstractFromNonAbstract() {
        return abstractFromNonAbstract(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidTaggedProfile(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079692, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidTaggedProfile", null, ORBUtilSystemException.class, bad_param);
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

    public BAD_PARAM objrefFromForeignOrb(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079693, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.objrefFromForeignOrb", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM objrefFromForeignOrb(CompletionStatus completionStatus) {
        return objrefFromForeignOrb(completionStatus, null);
    }

    public BAD_PARAM objrefFromForeignOrb(Throwable th) {
        return objrefFromForeignOrb(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM objrefFromForeignOrb() {
        return objrefFromForeignOrb(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM localObjectNotAllowed(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079694, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.localObjectNotAllowed", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM localObjectNotAllowed(CompletionStatus completionStatus) {
        return localObjectNotAllowed(completionStatus, null);
    }

    public BAD_PARAM localObjectNotAllowed(Throwable th) {
        return localObjectNotAllowed(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM localObjectNotAllowed() {
        return localObjectNotAllowed(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM nullObjectReference(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079695, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nullObjectReference", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM nullObjectReference(CompletionStatus completionStatus) {
        return nullObjectReference(completionStatus, null);
    }

    public BAD_PARAM nullObjectReference(Throwable th) {
        return nullObjectReference(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM nullObjectReference() {
        return nullObjectReference(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM couldNotLoadClass(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079696, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotLoadClass", new Object[]{obj}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM couldNotLoadClass(CompletionStatus completionStatus, Object obj) {
        return couldNotLoadClass(completionStatus, null, obj);
    }

    public BAD_PARAM couldNotLoadClass(Throwable th, Object obj) {
        return couldNotLoadClass(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM couldNotLoadClass(Object obj) {
        return couldNotLoadClass(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM badUrl(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079697, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badUrl", new Object[]{obj}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badUrl(CompletionStatus completionStatus, Object obj) {
        return badUrl(completionStatus, null, obj);
    }

    public BAD_PARAM badUrl(Throwable th, Object obj) {
        return badUrl(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM badUrl(Object obj) {
        return badUrl(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM fieldNotFound(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079698, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.fieldNotFound", new Object[]{obj}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM fieldNotFound(CompletionStatus completionStatus, Object obj) {
        return fieldNotFound(completionStatus, null, obj);
    }

    public BAD_PARAM fieldNotFound(Throwable th, Object obj) {
        return fieldNotFound(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM fieldNotFound(Object obj) {
        return fieldNotFound(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM errorSettingField(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079699, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.errorSettingField", new Object[]{obj, obj2}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM errorSettingField(CompletionStatus completionStatus, Object obj, Object obj2) {
        return errorSettingField(completionStatus, null, obj, obj2);
    }

    public BAD_PARAM errorSettingField(Throwable th, Object obj, Object obj2) {
        return errorSettingField(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public BAD_PARAM errorSettingField(Object obj, Object obj2) {
        return errorSettingField(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public BAD_PARAM boundsErrorInDiiRequest(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079700, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.boundsErrorInDiiRequest", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM boundsErrorInDiiRequest(CompletionStatus completionStatus) {
        return boundsErrorInDiiRequest(completionStatus, null);
    }

    public BAD_PARAM boundsErrorInDiiRequest(Throwable th) {
        return boundsErrorInDiiRequest(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM boundsErrorInDiiRequest() {
        return boundsErrorInDiiRequest(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM persistentServerInitError(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079701, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.persistentServerInitError", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM persistentServerInitError(CompletionStatus completionStatus) {
        return persistentServerInitError(completionStatus, null);
    }

    public BAD_PARAM persistentServerInitError(Throwable th) {
        return persistentServerInitError(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM persistentServerInitError() {
        return persistentServerInitError(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM couldNotCreateArray(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079702, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotCreateArray", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM couldNotCreateArray(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return couldNotCreateArray(completionStatus, null, obj, obj2, obj3);
    }

    public BAD_PARAM couldNotCreateArray(Throwable th, Object obj, Object obj2, Object obj3) {
        return couldNotCreateArray(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public BAD_PARAM couldNotCreateArray(Object obj, Object obj2, Object obj3) {
        return couldNotCreateArray(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public BAD_PARAM couldNotSetArray(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079703, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotSetArray", new Object[]{obj, obj2, obj3, obj4, obj5}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM couldNotSetArray(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        return couldNotSetArray(completionStatus, null, obj, obj2, obj3, obj4, obj5);
    }

    public BAD_PARAM couldNotSetArray(Throwable th, Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        return couldNotSetArray(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3, obj4, obj5);
    }

    public BAD_PARAM couldNotSetArray(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        return couldNotSetArray(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3, obj4, obj5);
    }

    public BAD_PARAM illegalBootstrapOperation(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079704, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalBootstrapOperation", new Object[]{obj}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM illegalBootstrapOperation(CompletionStatus completionStatus, Object obj) {
        return illegalBootstrapOperation(completionStatus, null, obj);
    }

    public BAD_PARAM illegalBootstrapOperation(Throwable th, Object obj) {
        return illegalBootstrapOperation(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM illegalBootstrapOperation(Object obj) {
        return illegalBootstrapOperation(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM bootstrapRuntimeException(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079705, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.bootstrapRuntimeException", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM bootstrapRuntimeException(CompletionStatus completionStatus) {
        return bootstrapRuntimeException(completionStatus, null);
    }

    public BAD_PARAM bootstrapRuntimeException(Throwable th) {
        return bootstrapRuntimeException(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM bootstrapRuntimeException() {
        return bootstrapRuntimeException(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM bootstrapException(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079706, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.bootstrapException", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM bootstrapException(CompletionStatus completionStatus) {
        return bootstrapException(completionStatus, null);
    }

    public BAD_PARAM bootstrapException(Throwable th) {
        return bootstrapException(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM bootstrapException() {
        return bootstrapException(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM stringExpected(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079707, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.stringExpected", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM stringExpected(CompletionStatus completionStatus) {
        return stringExpected(completionStatus, null);
    }

    public BAD_PARAM stringExpected(Throwable th) {
        return stringExpected(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM stringExpected() {
        return stringExpected(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidTypecodeKind(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079708, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidTypecodeKind", new Object[]{obj}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidTypecodeKind(CompletionStatus completionStatus, Object obj) {
        return invalidTypecodeKind(completionStatus, null, obj);
    }

    public BAD_PARAM invalidTypecodeKind(Throwable th, Object obj) {
        return invalidTypecodeKind(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM invalidTypecodeKind(Object obj) {
        return invalidTypecodeKind(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_PARAM socketFactoryAndContactInfoListAtSameTime(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079709, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.socketFactoryAndContactInfoListAtSameTime", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM socketFactoryAndContactInfoListAtSameTime(CompletionStatus completionStatus) {
        return socketFactoryAndContactInfoListAtSameTime(completionStatus, null);
    }

    public BAD_PARAM socketFactoryAndContactInfoListAtSameTime(Throwable th) {
        return socketFactoryAndContactInfoListAtSameTime(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM socketFactoryAndContactInfoListAtSameTime() {
        return socketFactoryAndContactInfoListAtSameTime(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079710, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.acceptorsAndLegacySocketFactoryAtSameTime", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus completionStatus) {
        return acceptorsAndLegacySocketFactoryAtSameTime(completionStatus, null);
    }

    public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime(Throwable th) {
        return acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM acceptorsAndLegacySocketFactoryAtSameTime() {
        return acceptorsAndLegacySocketFactoryAtSameTime(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM badOrbForServant(CompletionStatus completionStatus, Throwable th) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079711, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badOrbForServant", null, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM badOrbForServant(CompletionStatus completionStatus) {
        return badOrbForServant(completionStatus, null);
    }

    public BAD_PARAM badOrbForServant(Throwable th) {
        return badOrbForServant(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_PARAM badOrbForServant() {
        return badOrbForServant(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_PARAM invalidRequestPartitioningPolicyValue(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079712, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidRequestPartitioningPolicyValue", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidRequestPartitioningPolicyValue(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningPolicyValue(completionStatus, null, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningPolicyValue(Throwable th, Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningPolicyValue(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningPolicyValue(Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningPolicyValue(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningComponentValue(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079713, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidRequestPartitioningComponentValue", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidRequestPartitioningComponentValue(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningComponentValue(completionStatus, null, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningComponentValue(Throwable th, Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningComponentValue(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningComponentValue(Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningComponentValue(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningId(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079714, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidRequestPartitioningId", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM invalidRequestPartitioningId(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningId(completionStatus, null, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningId(Throwable th, Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningId(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public BAD_PARAM invalidRequestPartitioningId(Object obj, Object obj2, Object obj3) {
        return invalidRequestPartitioningId(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public BAD_PARAM errorInSettingDynamicStubFactoryFactory(CompletionStatus completionStatus, Throwable th, Object obj) {
        BAD_PARAM bad_param = new BAD_PARAM(1398079715, completionStatus);
        if (th != null) {
            bad_param.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.errorInSettingDynamicStubFactoryFactory", new Object[]{obj}, ORBUtilSystemException.class, bad_param);
        }
        return bad_param;
    }

    public BAD_PARAM errorInSettingDynamicStubFactoryFactory(CompletionStatus completionStatus, Object obj) {
        return errorInSettingDynamicStubFactoryFactory(completionStatus, null, obj);
    }

    public BAD_PARAM errorInSettingDynamicStubFactoryFactory(Throwable th, Object obj) {
        return errorInSettingDynamicStubFactoryFactory(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public BAD_PARAM errorInSettingDynamicStubFactoryFactory(Object obj) {
        return errorInSettingDynamicStubFactoryFactory(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public BAD_INV_ORDER dsimethodNotcalled(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079689, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.dsimethodNotcalled", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER dsimethodNotcalled(CompletionStatus completionStatus) {
        return dsimethodNotcalled(completionStatus, null);
    }

    public BAD_INV_ORDER dsimethodNotcalled(Throwable th) {
        return dsimethodNotcalled(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER dsimethodNotcalled() {
        return dsimethodNotcalled(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER argumentsCalledMultiple(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079690, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.argumentsCalledMultiple", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER argumentsCalledMultiple(CompletionStatus completionStatus) {
        return argumentsCalledMultiple(completionStatus, null);
    }

    public BAD_INV_ORDER argumentsCalledMultiple(Throwable th) {
        return argumentsCalledMultiple(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER argumentsCalledMultiple() {
        return argumentsCalledMultiple(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER argumentsCalledAfterException(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079691, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.argumentsCalledAfterException", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER argumentsCalledAfterException(CompletionStatus completionStatus) {
        return argumentsCalledAfterException(completionStatus, null);
    }

    public BAD_INV_ORDER argumentsCalledAfterException(Throwable th) {
        return argumentsCalledAfterException(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER argumentsCalledAfterException() {
        return argumentsCalledAfterException(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER argumentsCalledNullArgs(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079692, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.argumentsCalledNullArgs", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER argumentsCalledNullArgs(CompletionStatus completionStatus) {
        return argumentsCalledNullArgs(completionStatus, null);
    }

    public BAD_INV_ORDER argumentsCalledNullArgs(Throwable th) {
        return argumentsCalledNullArgs(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER argumentsCalledNullArgs() {
        return argumentsCalledNullArgs(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER argumentsNotCalled(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079693, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.argumentsNotCalled", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER argumentsNotCalled(CompletionStatus completionStatus) {
        return argumentsNotCalled(completionStatus, null);
    }

    public BAD_INV_ORDER argumentsNotCalled(Throwable th) {
        return argumentsNotCalled(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER argumentsNotCalled() {
        return argumentsNotCalled(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER setResultCalledMultiple(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079694, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.setResultCalledMultiple", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER setResultCalledMultiple(CompletionStatus completionStatus) {
        return setResultCalledMultiple(completionStatus, null);
    }

    public BAD_INV_ORDER setResultCalledMultiple(Throwable th) {
        return setResultCalledMultiple(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER setResultCalledMultiple() {
        return setResultCalledMultiple(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER setResultAfterException(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079695, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.setResultAfterException", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER setResultAfterException(CompletionStatus completionStatus) {
        return setResultAfterException(completionStatus, null);
    }

    public BAD_INV_ORDER setResultAfterException(Throwable th) {
        return setResultAfterException(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER setResultAfterException() {
        return setResultAfterException(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_INV_ORDER setResultCalledNullArgs(CompletionStatus completionStatus, Throwable th) {
        BAD_INV_ORDER bad_inv_order = new BAD_INV_ORDER(1398079696, completionStatus);
        if (th != null) {
            bad_inv_order.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.setResultCalledNullArgs", null, ORBUtilSystemException.class, bad_inv_order);
        }
        return bad_inv_order;
    }

    public BAD_INV_ORDER setResultCalledNullArgs(CompletionStatus completionStatus) {
        return setResultCalledNullArgs(completionStatus, null);
    }

    public BAD_INV_ORDER setResultCalledNullArgs(Throwable th) {
        return setResultCalledNullArgs(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_INV_ORDER setResultCalledNullArgs() {
        return setResultCalledNullArgs(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_TYPECODE badRemoteTypecode(CompletionStatus completionStatus, Throwable th) {
        BAD_TYPECODE bad_typecode = new BAD_TYPECODE(1398079689, completionStatus);
        if (th != null) {
            bad_typecode.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badRemoteTypecode", null, ORBUtilSystemException.class, bad_typecode);
        }
        return bad_typecode;
    }

    public BAD_TYPECODE badRemoteTypecode(CompletionStatus completionStatus) {
        return badRemoteTypecode(completionStatus, null);
    }

    public BAD_TYPECODE badRemoteTypecode(Throwable th) {
        return badRemoteTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_TYPECODE badRemoteTypecode() {
        return badRemoteTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public BAD_TYPECODE unresolvedRecursiveTypecode(CompletionStatus completionStatus, Throwable th) {
        BAD_TYPECODE bad_typecode = new BAD_TYPECODE(1398079690, completionStatus);
        if (th != null) {
            bad_typecode.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unresolvedRecursiveTypecode", null, ORBUtilSystemException.class, bad_typecode);
        }
        return bad_typecode;
    }

    public BAD_TYPECODE unresolvedRecursiveTypecode(CompletionStatus completionStatus) {
        return unresolvedRecursiveTypecode(completionStatus, null);
    }

    public BAD_TYPECODE unresolvedRecursiveTypecode(Throwable th) {
        return unresolvedRecursiveTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public BAD_TYPECODE unresolvedRecursiveTypecode() {
        return unresolvedRecursiveTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE connectFailure(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079689, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.connectFailure", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE connectFailure(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return connectFailure(completionStatus, null, obj, obj2, obj3);
    }

    public COMM_FAILURE connectFailure(Throwable th, Object obj, Object obj2, Object obj3) {
        return connectFailure(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public COMM_FAILURE connectFailure(Object obj, Object obj2, Object obj3) {
        return connectFailure(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public COMM_FAILURE connectionCloseRebind(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079690, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.connectionCloseRebind", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE connectionCloseRebind(CompletionStatus completionStatus) {
        return connectionCloseRebind(completionStatus, null);
    }

    public COMM_FAILURE connectionCloseRebind(Throwable th) {
        return connectionCloseRebind(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE connectionCloseRebind() {
        return connectionCloseRebind(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE writeErrorSend(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079691, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.writeErrorSend", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE writeErrorSend(CompletionStatus completionStatus) {
        return writeErrorSend(completionStatus, null);
    }

    public COMM_FAILURE writeErrorSend(Throwable th) {
        return writeErrorSend(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE writeErrorSend() {
        return writeErrorSend(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE getPropertiesError(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079692, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getPropertiesError", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE getPropertiesError(CompletionStatus completionStatus) {
        return getPropertiesError(completionStatus, null);
    }

    public COMM_FAILURE getPropertiesError(Throwable th) {
        return getPropertiesError(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE getPropertiesError() {
        return getPropertiesError(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE bootstrapServerNotAvail(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079693, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.bootstrapServerNotAvail", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE bootstrapServerNotAvail(CompletionStatus completionStatus) {
        return bootstrapServerNotAvail(completionStatus, null);
    }

    public COMM_FAILURE bootstrapServerNotAvail(Throwable th) {
        return bootstrapServerNotAvail(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE bootstrapServerNotAvail() {
        return bootstrapServerNotAvail(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE invokeError(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079694, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invokeError", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE invokeError(CompletionStatus completionStatus) {
        return invokeError(completionStatus, null);
    }

    public COMM_FAILURE invokeError(Throwable th) {
        return invokeError(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE invokeError() {
        return invokeError(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus completionStatus, Throwable th, Object obj) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079695, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.defaultCreateServerSocketGivenNonIiopClearText", new Object[]{obj}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus completionStatus, Object obj) {
        return defaultCreateServerSocketGivenNonIiopClearText(completionStatus, null, obj);
    }

    public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(Throwable th, Object obj) {
        return defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public COMM_FAILURE defaultCreateServerSocketGivenNonIiopClearText(Object obj) {
        return defaultCreateServerSocketGivenNonIiopClearText(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public COMM_FAILURE connectionAbort(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079696, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.connectionAbort", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE connectionAbort(CompletionStatus completionStatus) {
        return connectionAbort(completionStatus, null);
    }

    public COMM_FAILURE connectionAbort(Throwable th) {
        return connectionAbort(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE connectionAbort() {
        return connectionAbort(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE connectionRebind(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079697, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.connectionRebind", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE connectionRebind(CompletionStatus completionStatus) {
        return connectionRebind(completionStatus, null);
    }

    public COMM_FAILURE connectionRebind(Throwable th) {
        return connectionRebind(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE connectionRebind() {
        return connectionRebind(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE recvMsgError(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079698, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.recvMsgError", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE recvMsgError(CompletionStatus completionStatus) {
        return recvMsgError(completionStatus, null);
    }

    public COMM_FAILURE recvMsgError(Throwable th) {
        return recvMsgError(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE recvMsgError() {
        return recvMsgError(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE ioexceptionWhenReadingConnection(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079699, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.ioexceptionWhenReadingConnection", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE ioexceptionWhenReadingConnection(CompletionStatus completionStatus) {
        return ioexceptionWhenReadingConnection(completionStatus, null);
    }

    public COMM_FAILURE ioexceptionWhenReadingConnection(Throwable th) {
        return ioexceptionWhenReadingConnection(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE ioexceptionWhenReadingConnection() {
        return ioexceptionWhenReadingConnection(CompletionStatus.COMPLETED_NO, null);
    }

    public COMM_FAILURE selectionKeyInvalid(CompletionStatus completionStatus, Throwable th, Object obj) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079700, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.selectionKeyInvalid", new Object[]{obj}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE selectionKeyInvalid(CompletionStatus completionStatus, Object obj) {
        return selectionKeyInvalid(completionStatus, null, obj);
    }

    public COMM_FAILURE selectionKeyInvalid(Throwable th, Object obj) {
        return selectionKeyInvalid(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public COMM_FAILURE selectionKeyInvalid(Object obj) {
        return selectionKeyInvalid(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public COMM_FAILURE exceptionInAccept(CompletionStatus completionStatus, Throwable th, Object obj) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079701, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.exceptionInAccept", new Object[]{obj}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE exceptionInAccept(CompletionStatus completionStatus, Object obj) {
        return exceptionInAccept(completionStatus, null, obj);
    }

    public COMM_FAILURE exceptionInAccept(Throwable th, Object obj) {
        return exceptionInAccept(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public COMM_FAILURE exceptionInAccept(Object obj) {
        return exceptionInAccept(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public COMM_FAILURE securityExceptionInAccept(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079702, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.securityExceptionInAccept", new Object[]{obj, obj2}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE securityExceptionInAccept(CompletionStatus completionStatus, Object obj, Object obj2) {
        return securityExceptionInAccept(completionStatus, null, obj, obj2);
    }

    public COMM_FAILURE securityExceptionInAccept(Throwable th, Object obj, Object obj2) {
        return securityExceptionInAccept(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public COMM_FAILURE securityExceptionInAccept(Object obj, Object obj2) {
        return securityExceptionInAccept(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public COMM_FAILURE transportReadTimeoutExceeded(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3, Object obj4) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079703, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.transportReadTimeoutExceeded", new Object[]{obj, obj2, obj3, obj4}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE transportReadTimeoutExceeded(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3, Object obj4) {
        return transportReadTimeoutExceeded(completionStatus, null, obj, obj2, obj3, obj4);
    }

    public COMM_FAILURE transportReadTimeoutExceeded(Throwable th, Object obj, Object obj2, Object obj3, Object obj4) {
        return transportReadTimeoutExceeded(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3, obj4);
    }

    public COMM_FAILURE transportReadTimeoutExceeded(Object obj, Object obj2, Object obj3, Object obj4) {
        return transportReadTimeoutExceeded(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3, obj4);
    }

    public COMM_FAILURE createListenerFailed(CompletionStatus completionStatus, Throwable th, Object obj) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079704, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.SEVERE)) {
            doLog(Level.SEVERE, "ORBUTIL.createListenerFailed", new Object[]{obj}, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE createListenerFailed(CompletionStatus completionStatus, Object obj) {
        return createListenerFailed(completionStatus, null, obj);
    }

    public COMM_FAILURE createListenerFailed(Throwable th, Object obj) {
        return createListenerFailed(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public COMM_FAILURE createListenerFailed(Object obj) {
        return createListenerFailed(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public COMM_FAILURE bufferReadManagerTimeout(CompletionStatus completionStatus, Throwable th) {
        COMM_FAILURE comm_failure = new COMM_FAILURE(1398079705, completionStatus);
        if (th != null) {
            comm_failure.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.bufferReadManagerTimeout", null, ORBUtilSystemException.class, comm_failure);
        }
        return comm_failure;
    }

    public COMM_FAILURE bufferReadManagerTimeout(CompletionStatus completionStatus) {
        return bufferReadManagerTimeout(completionStatus, null);
    }

    public COMM_FAILURE bufferReadManagerTimeout(Throwable th) {
        return bufferReadManagerTimeout(CompletionStatus.COMPLETED_NO, th);
    }

    public COMM_FAILURE bufferReadManagerTimeout() {
        return bufferReadManagerTimeout(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badStringifiedIorLen(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079689, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badStringifiedIorLen", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badStringifiedIorLen(CompletionStatus completionStatus) {
        return badStringifiedIorLen(completionStatus, null);
    }

    public DATA_CONVERSION badStringifiedIorLen(Throwable th) {
        return badStringifiedIorLen(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badStringifiedIorLen() {
        return badStringifiedIorLen(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badStringifiedIor(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079690, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badStringifiedIor", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badStringifiedIor(CompletionStatus completionStatus) {
        return badStringifiedIor(completionStatus, null);
    }

    public DATA_CONVERSION badStringifiedIor(Throwable th) {
        return badStringifiedIor(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badStringifiedIor() {
        return badStringifiedIor(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badModifier(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079691, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badModifier", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badModifier(CompletionStatus completionStatus) {
        return badModifier(completionStatus, null);
    }

    public DATA_CONVERSION badModifier(Throwable th) {
        return badModifier(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badModifier() {
        return badModifier(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION codesetIncompatible(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079692, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.codesetIncompatible", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION codesetIncompatible(CompletionStatus completionStatus) {
        return codesetIncompatible(completionStatus, null);
    }

    public DATA_CONVERSION codesetIncompatible(Throwable th) {
        return codesetIncompatible(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION codesetIncompatible() {
        return codesetIncompatible(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badHexDigit(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079693, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badHexDigit", null, ORBUtilSystemException.class, data_conversion);
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

    public DATA_CONVERSION badUnicodePair(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079694, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badUnicodePair", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badUnicodePair(CompletionStatus completionStatus) {
        return badUnicodePair(completionStatus, null);
    }

    public DATA_CONVERSION badUnicodePair(Throwable th) {
        return badUnicodePair(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badUnicodePair() {
        return badUnicodePair(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION btcResultMoreThanOneChar(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079695, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.btcResultMoreThanOneChar", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION btcResultMoreThanOneChar(CompletionStatus completionStatus) {
        return btcResultMoreThanOneChar(completionStatus, null);
    }

    public DATA_CONVERSION btcResultMoreThanOneChar(Throwable th) {
        return btcResultMoreThanOneChar(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION btcResultMoreThanOneChar() {
        return btcResultMoreThanOneChar(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badCodesetsFromClient(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079696, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCodesetsFromClient", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badCodesetsFromClient(CompletionStatus completionStatus) {
        return badCodesetsFromClient(completionStatus, null);
    }

    public DATA_CONVERSION badCodesetsFromClient(Throwable th) {
        return badCodesetsFromClient(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badCodesetsFromClient() {
        return badCodesetsFromClient(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION invalidSingleCharCtb(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079697, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidSingleCharCtb", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION invalidSingleCharCtb(CompletionStatus completionStatus) {
        return invalidSingleCharCtb(completionStatus, null);
    }

    public DATA_CONVERSION invalidSingleCharCtb(Throwable th) {
        return invalidSingleCharCtb(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION invalidSingleCharCtb() {
        return invalidSingleCharCtb(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badGiop11Ctb(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079698, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badGiop11Ctb", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badGiop11Ctb(CompletionStatus completionStatus) {
        return badGiop11Ctb(completionStatus, null);
    }

    public DATA_CONVERSION badGiop11Ctb(Throwable th) {
        return badGiop11Ctb(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION badGiop11Ctb() {
        return badGiop11Ctb(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION badSequenceBounds(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079700, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badSequenceBounds", new Object[]{obj, obj2}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badSequenceBounds(CompletionStatus completionStatus, Object obj, Object obj2) {
        return badSequenceBounds(completionStatus, null, obj, obj2);
    }

    public DATA_CONVERSION badSequenceBounds(Throwable th, Object obj, Object obj2) {
        return badSequenceBounds(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public DATA_CONVERSION badSequenceBounds(Object obj, Object obj2) {
        return badSequenceBounds(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public DATA_CONVERSION illegalSocketFactoryType(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079701, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalSocketFactoryType", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION illegalSocketFactoryType(CompletionStatus completionStatus, Object obj) {
        return illegalSocketFactoryType(completionStatus, null, obj);
    }

    public DATA_CONVERSION illegalSocketFactoryType(Throwable th, Object obj) {
        return illegalSocketFactoryType(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION illegalSocketFactoryType(Object obj) {
        return illegalSocketFactoryType(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION badCustomSocketFactory(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079702, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCustomSocketFactory", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badCustomSocketFactory(CompletionStatus completionStatus, Object obj) {
        return badCustomSocketFactory(completionStatus, null, obj);
    }

    public DATA_CONVERSION badCustomSocketFactory(Throwable th, Object obj) {
        return badCustomSocketFactory(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION badCustomSocketFactory(Object obj) {
        return badCustomSocketFactory(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION fragmentSizeMinimum(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079703, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.fragmentSizeMinimum", new Object[]{obj, obj2}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION fragmentSizeMinimum(CompletionStatus completionStatus, Object obj, Object obj2) {
        return fragmentSizeMinimum(completionStatus, null, obj, obj2);
    }

    public DATA_CONVERSION fragmentSizeMinimum(Throwable th, Object obj, Object obj2) {
        return fragmentSizeMinimum(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public DATA_CONVERSION fragmentSizeMinimum(Object obj, Object obj2) {
        return fragmentSizeMinimum(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public DATA_CONVERSION fragmentSizeDiv(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079704, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.fragmentSizeDiv", new Object[]{obj, obj2}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION fragmentSizeDiv(CompletionStatus completionStatus, Object obj, Object obj2) {
        return fragmentSizeDiv(completionStatus, null, obj, obj2);
    }

    public DATA_CONVERSION fragmentSizeDiv(Throwable th, Object obj, Object obj2) {
        return fragmentSizeDiv(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public DATA_CONVERSION fragmentSizeDiv(Object obj, Object obj2) {
        return fragmentSizeDiv(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public DATA_CONVERSION orbInitializerFailure(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079705, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbInitializerFailure", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION orbInitializerFailure(CompletionStatus completionStatus, Object obj) {
        return orbInitializerFailure(completionStatus, null, obj);
    }

    public DATA_CONVERSION orbInitializerFailure(Throwable th, Object obj) {
        return orbInitializerFailure(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION orbInitializerFailure(Object obj) {
        return orbInitializerFailure(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION orbInitializerType(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079706, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbInitializerType", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION orbInitializerType(CompletionStatus completionStatus, Object obj) {
        return orbInitializerType(completionStatus, null, obj);
    }

    public DATA_CONVERSION orbInitializerType(Throwable th, Object obj) {
        return orbInitializerType(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION orbInitializerType(Object obj) {
        return orbInitializerType(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION orbInitialreferenceSyntax(CompletionStatus completionStatus, Throwable th) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079707, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbInitialreferenceSyntax", null, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION orbInitialreferenceSyntax(CompletionStatus completionStatus) {
        return orbInitialreferenceSyntax(completionStatus, null);
    }

    public DATA_CONVERSION orbInitialreferenceSyntax(Throwable th) {
        return orbInitialreferenceSyntax(CompletionStatus.COMPLETED_NO, th);
    }

    public DATA_CONVERSION orbInitialreferenceSyntax() {
        return orbInitialreferenceSyntax(CompletionStatus.COMPLETED_NO, null);
    }

    public DATA_CONVERSION acceptorInstantiationFailure(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079708, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.acceptorInstantiationFailure", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION acceptorInstantiationFailure(CompletionStatus completionStatus, Object obj) {
        return acceptorInstantiationFailure(completionStatus, null, obj);
    }

    public DATA_CONVERSION acceptorInstantiationFailure(Throwable th, Object obj) {
        return acceptorInstantiationFailure(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION acceptorInstantiationFailure(Object obj) {
        return acceptorInstantiationFailure(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION acceptorInstantiationTypeFailure(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079709, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.acceptorInstantiationTypeFailure", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION acceptorInstantiationTypeFailure(CompletionStatus completionStatus, Object obj) {
        return acceptorInstantiationTypeFailure(completionStatus, null, obj);
    }

    public DATA_CONVERSION acceptorInstantiationTypeFailure(Throwable th, Object obj) {
        return acceptorInstantiationTypeFailure(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION acceptorInstantiationTypeFailure(Object obj) {
        return acceptorInstantiationTypeFailure(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION illegalContactInfoListFactoryType(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079710, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalContactInfoListFactoryType", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION illegalContactInfoListFactoryType(CompletionStatus completionStatus, Object obj) {
        return illegalContactInfoListFactoryType(completionStatus, null, obj);
    }

    public DATA_CONVERSION illegalContactInfoListFactoryType(Throwable th, Object obj) {
        return illegalContactInfoListFactoryType(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION illegalContactInfoListFactoryType(Object obj) {
        return illegalContactInfoListFactoryType(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION badContactInfoListFactory(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079711, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badContactInfoListFactory", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badContactInfoListFactory(CompletionStatus completionStatus, Object obj) {
        return badContactInfoListFactory(completionStatus, null, obj);
    }

    public DATA_CONVERSION badContactInfoListFactory(Throwable th, Object obj) {
        return badContactInfoListFactory(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION badContactInfoListFactory(Object obj) {
        return badContactInfoListFactory(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION illegalIorToSocketInfoType(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079712, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalIorToSocketInfoType", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION illegalIorToSocketInfoType(CompletionStatus completionStatus, Object obj) {
        return illegalIorToSocketInfoType(completionStatus, null, obj);
    }

    public DATA_CONVERSION illegalIorToSocketInfoType(Throwable th, Object obj) {
        return illegalIorToSocketInfoType(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION illegalIorToSocketInfoType(Object obj) {
        return illegalIorToSocketInfoType(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION badCustomIorToSocketInfo(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079713, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCustomIorToSocketInfo", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badCustomIorToSocketInfo(CompletionStatus completionStatus, Object obj) {
        return badCustomIorToSocketInfo(completionStatus, null, obj);
    }

    public DATA_CONVERSION badCustomIorToSocketInfo(Throwable th, Object obj) {
        return badCustomIorToSocketInfo(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION badCustomIorToSocketInfo(Object obj) {
        return badCustomIorToSocketInfo(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079714, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalIiopPrimaryToContactInfoType", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(CompletionStatus completionStatus, Object obj) {
        return illegalIiopPrimaryToContactInfoType(completionStatus, null, obj);
    }

    public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(Throwable th, Object obj) {
        return illegalIiopPrimaryToContactInfoType(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION illegalIiopPrimaryToContactInfoType(Object obj) {
        return illegalIiopPrimaryToContactInfoType(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(CompletionStatus completionStatus, Throwable th, Object obj) {
        DATA_CONVERSION data_conversion = new DATA_CONVERSION(1398079715, completionStatus);
        if (th != null) {
            data_conversion.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCustomIiopPrimaryToContactInfo", new Object[]{obj}, ORBUtilSystemException.class, data_conversion);
        }
        return data_conversion;
    }

    public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(CompletionStatus completionStatus, Object obj) {
        return badCustomIiopPrimaryToContactInfo(completionStatus, null, obj);
    }

    public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(Throwable th, Object obj) {
        return badCustomIiopPrimaryToContactInfo(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public DATA_CONVERSION badCustomIiopPrimaryToContactInfo(Object obj) {
        return badCustomIiopPrimaryToContactInfo(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INV_OBJREF badCorbalocString(CompletionStatus completionStatus, Throwable th) {
        INV_OBJREF inv_objref = new INV_OBJREF(1398079689, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCorbalocString", null, ORBUtilSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF badCorbalocString(CompletionStatus completionStatus) {
        return badCorbalocString(completionStatus, null);
    }

    public INV_OBJREF badCorbalocString(Throwable th) {
        return badCorbalocString(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_OBJREF badCorbalocString() {
        return badCorbalocString(CompletionStatus.COMPLETED_NO, null);
    }

    public INV_OBJREF noProfilePresent(CompletionStatus completionStatus, Throwable th) {
        INV_OBJREF inv_objref = new INV_OBJREF(1398079690, completionStatus);
        if (th != null) {
            inv_objref.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noProfilePresent", null, ORBUtilSystemException.class, inv_objref);
        }
        return inv_objref;
    }

    public INV_OBJREF noProfilePresent(CompletionStatus completionStatus) {
        return noProfilePresent(completionStatus, null);
    }

    public INV_OBJREF noProfilePresent(Throwable th) {
        return noProfilePresent(CompletionStatus.COMPLETED_NO, th);
    }

    public INV_OBJREF noProfilePresent() {
        return noProfilePresent(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE cannotCreateOrbidDb(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079689, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cannotCreateOrbidDb", null, ORBUtilSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE cannotCreateOrbidDb(CompletionStatus completionStatus) {
        return cannotCreateOrbidDb(completionStatus, null);
    }

    public INITIALIZE cannotCreateOrbidDb(Throwable th) {
        return cannotCreateOrbidDb(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE cannotCreateOrbidDb() {
        return cannotCreateOrbidDb(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE cannotReadOrbidDb(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079690, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cannotReadOrbidDb", null, ORBUtilSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE cannotReadOrbidDb(CompletionStatus completionStatus) {
        return cannotReadOrbidDb(completionStatus, null);
    }

    public INITIALIZE cannotReadOrbidDb(Throwable th) {
        return cannotReadOrbidDb(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE cannotReadOrbidDb() {
        return cannotReadOrbidDb(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE cannotWriteOrbidDb(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079691, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cannotWriteOrbidDb", null, ORBUtilSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE cannotWriteOrbidDb(CompletionStatus completionStatus) {
        return cannotWriteOrbidDb(completionStatus, null);
    }

    public INITIALIZE cannotWriteOrbidDb(Throwable th) {
        return cannotWriteOrbidDb(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE cannotWriteOrbidDb() {
        return cannotWriteOrbidDb(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE getServerPortCalledBeforeEndpointsInitialized(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079692, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getServerPortCalledBeforeEndpointsInitialized", null, ORBUtilSystemException.class, initialize);
        }
        return initialize;
    }

    public INITIALIZE getServerPortCalledBeforeEndpointsInitialized(CompletionStatus completionStatus) {
        return getServerPortCalledBeforeEndpointsInitialized(completionStatus, null);
    }

    public INITIALIZE getServerPortCalledBeforeEndpointsInitialized(Throwable th) {
        return getServerPortCalledBeforeEndpointsInitialized(CompletionStatus.COMPLETED_NO, th);
    }

    public INITIALIZE getServerPortCalledBeforeEndpointsInitialized() {
        return getServerPortCalledBeforeEndpointsInitialized(CompletionStatus.COMPLETED_NO, null);
    }

    public INITIALIZE persistentServerportNotSet(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079693, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.persistentServerportNotSet", null, ORBUtilSystemException.class, initialize);
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

    public INITIALIZE persistentServeridNotSet(CompletionStatus completionStatus, Throwable th) {
        INITIALIZE initialize = new INITIALIZE(1398079694, completionStatus);
        if (th != null) {
            initialize.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.persistentServeridNotSet", null, ORBUtilSystemException.class, initialize);
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

    public INTERNAL nonExistentOrbid(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079689, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nonExistentOrbid", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL nonExistentOrbid(CompletionStatus completionStatus) {
        return nonExistentOrbid(completionStatus, null);
    }

    public INTERNAL nonExistentOrbid(Throwable th) {
        return nonExistentOrbid(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL nonExistentOrbid() {
        return nonExistentOrbid(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL noServerSubcontract(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079690, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noServerSubcontract", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL noServerSubcontract(CompletionStatus completionStatus) {
        return noServerSubcontract(completionStatus, null);
    }

    public INTERNAL noServerSubcontract(Throwable th) {
        return noServerSubcontract(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL noServerSubcontract() {
        return noServerSubcontract(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL serverScTempSize(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079691, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.serverScTempSize", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL serverScTempSize(CompletionStatus completionStatus) {
        return serverScTempSize(completionStatus, null);
    }

    public INTERNAL serverScTempSize(Throwable th) {
        return serverScTempSize(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL serverScTempSize() {
        return serverScTempSize(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL noClientScClass(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079692, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noClientScClass", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL noClientScClass(CompletionStatus completionStatus) {
        return noClientScClass(completionStatus, null);
    }

    public INTERNAL noClientScClass(Throwable th) {
        return noClientScClass(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL noClientScClass() {
        return noClientScClass(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL serverScNoIiopProfile(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079693, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.serverScNoIiopProfile", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL serverScNoIiopProfile(CompletionStatus completionStatus) {
        return serverScNoIiopProfile(completionStatus, null);
    }

    public INTERNAL serverScNoIiopProfile(Throwable th) {
        return serverScNoIiopProfile(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL serverScNoIiopProfile() {
        return serverScNoIiopProfile(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL getSystemExReturnedNull(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079694, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getSystemExReturnedNull", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL getSystemExReturnedNull(CompletionStatus completionStatus) {
        return getSystemExReturnedNull(completionStatus, null);
    }

    public INTERNAL getSystemExReturnedNull(Throwable th) {
        return getSystemExReturnedNull(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL getSystemExReturnedNull() {
        return getSystemExReturnedNull(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL peekstringFailed(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079695, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.peekstringFailed", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL peekstringFailed(CompletionStatus completionStatus) {
        return peekstringFailed(completionStatus, null);
    }

    public INTERNAL peekstringFailed(Throwable th) {
        return peekstringFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL peekstringFailed() {
        return peekstringFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL getLocalHostFailed(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079696, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.getLocalHostFailed", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL getLocalHostFailed(CompletionStatus completionStatus) {
        return getLocalHostFailed(completionStatus, null);
    }

    public INTERNAL getLocalHostFailed(Throwable th) {
        return getLocalHostFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL getLocalHostFailed() {
        return getLocalHostFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badLocateRequestStatus(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079698, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badLocateRequestStatus", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badLocateRequestStatus(CompletionStatus completionStatus) {
        return badLocateRequestStatus(completionStatus, null);
    }

    public INTERNAL badLocateRequestStatus(Throwable th) {
        return badLocateRequestStatus(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badLocateRequestStatus() {
        return badLocateRequestStatus(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL stringifyWriteError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079699, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.stringifyWriteError", null, ORBUtilSystemException.class, internal);
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

    public INTERNAL badGiopRequestType(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079700, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badGiopRequestType", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badGiopRequestType(CompletionStatus completionStatus) {
        return badGiopRequestType(completionStatus, null);
    }

    public INTERNAL badGiopRequestType(Throwable th) {
        return badGiopRequestType(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badGiopRequestType() {
        return badGiopRequestType(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL errorUnmarshalingUserexc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079701, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.errorUnmarshalingUserexc", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorUnmarshalingUserexc(CompletionStatus completionStatus) {
        return errorUnmarshalingUserexc(completionStatus, null);
    }

    public INTERNAL errorUnmarshalingUserexc(Throwable th) {
        return errorUnmarshalingUserexc(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL errorUnmarshalingUserexc() {
        return errorUnmarshalingUserexc(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL requestdispatcherregistryError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079702, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.requestdispatcherregistryError", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL requestdispatcherregistryError(CompletionStatus completionStatus) {
        return requestdispatcherregistryError(completionStatus, null);
    }

    public INTERNAL requestdispatcherregistryError(Throwable th) {
        return requestdispatcherregistryError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL requestdispatcherregistryError() {
        return requestdispatcherregistryError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL locationforwardError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079703, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.locationforwardError", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL locationforwardError(CompletionStatus completionStatus) {
        return locationforwardError(completionStatus, null);
    }

    public INTERNAL locationforwardError(Throwable th) {
        return locationforwardError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL locationforwardError() {
        return locationforwardError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL wrongClientsc(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079704, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.wrongClientsc", null, ORBUtilSystemException.class, internal);
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

    public INTERNAL badServantReadObject(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079705, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badServantReadObject", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badServantReadObject(CompletionStatus completionStatus) {
        return badServantReadObject(completionStatus, null);
    }

    public INTERNAL badServantReadObject(Throwable th) {
        return badServantReadObject(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badServantReadObject() {
        return badServantReadObject(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL multIiopProfNotSupported(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079706, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.multIiopProfNotSupported", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL multIiopProfNotSupported(CompletionStatus completionStatus) {
        return multIiopProfNotSupported(completionStatus, null);
    }

    public INTERNAL multIiopProfNotSupported(Throwable th) {
        return multIiopProfNotSupported(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL multIiopProfNotSupported() {
        return multIiopProfNotSupported(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL giopMagicError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079708, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.giopMagicError", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL giopMagicError(CompletionStatus completionStatus) {
        return giopMagicError(completionStatus, null);
    }

    public INTERNAL giopMagicError(Throwable th) {
        return giopMagicError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL giopMagicError() {
        return giopMagicError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL giopVersionError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079709, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.giopVersionError", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL giopVersionError(CompletionStatus completionStatus) {
        return giopVersionError(completionStatus, null);
    }

    public INTERNAL giopVersionError(Throwable th) {
        return giopVersionError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL giopVersionError() {
        return giopVersionError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL illegalReplyStatus(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079710, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalReplyStatus", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL illegalReplyStatus(CompletionStatus completionStatus) {
        return illegalReplyStatus(completionStatus, null);
    }

    public INTERNAL illegalReplyStatus(Throwable th) {
        return illegalReplyStatus(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL illegalReplyStatus() {
        return illegalReplyStatus(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL illegalGiopMsgType(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079711, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalGiopMsgType", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL illegalGiopMsgType(CompletionStatus completionStatus) {
        return illegalGiopMsgType(completionStatus, null);
    }

    public INTERNAL illegalGiopMsgType(Throwable th) {
        return illegalGiopMsgType(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL illegalGiopMsgType() {
        return illegalGiopMsgType(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL fragmentationDisallowed(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079712, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.fragmentationDisallowed", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL fragmentationDisallowed(CompletionStatus completionStatus) {
        return fragmentationDisallowed(completionStatus, null);
    }

    public INTERNAL fragmentationDisallowed(Throwable th) {
        return fragmentationDisallowed(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL fragmentationDisallowed() {
        return fragmentationDisallowed(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badReplystatus(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079713, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badReplystatus", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badReplystatus(CompletionStatus completionStatus) {
        return badReplystatus(completionStatus, null);
    }

    public INTERNAL badReplystatus(Throwable th) {
        return badReplystatus(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badReplystatus() {
        return badReplystatus(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL ctbConverterFailure(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079714, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.ctbConverterFailure", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL ctbConverterFailure(CompletionStatus completionStatus) {
        return ctbConverterFailure(completionStatus, null);
    }

    public INTERNAL ctbConverterFailure(Throwable th) {
        return ctbConverterFailure(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL ctbConverterFailure() {
        return ctbConverterFailure(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL btcConverterFailure(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079715, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.btcConverterFailure", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL btcConverterFailure(CompletionStatus completionStatus) {
        return btcConverterFailure(completionStatus, null);
    }

    public INTERNAL btcConverterFailure(Throwable th) {
        return btcConverterFailure(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL btcConverterFailure() {
        return btcConverterFailure(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL wcharArrayUnsupportedEncoding(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079716, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.wcharArrayUnsupportedEncoding", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL wcharArrayUnsupportedEncoding(CompletionStatus completionStatus) {
        return wcharArrayUnsupportedEncoding(completionStatus, null);
    }

    public INTERNAL wcharArrayUnsupportedEncoding(Throwable th) {
        return wcharArrayUnsupportedEncoding(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL wcharArrayUnsupportedEncoding() {
        return wcharArrayUnsupportedEncoding(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL illegalTargetAddressDisposition(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079717, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalTargetAddressDisposition", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL illegalTargetAddressDisposition(CompletionStatus completionStatus) {
        return illegalTargetAddressDisposition(completionStatus, null);
    }

    public INTERNAL illegalTargetAddressDisposition(Throwable th) {
        return illegalTargetAddressDisposition(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL illegalTargetAddressDisposition() {
        return illegalTargetAddressDisposition(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL nullReplyInGetAddrDisposition(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079718, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nullReplyInGetAddrDisposition", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL nullReplyInGetAddrDisposition(CompletionStatus completionStatus) {
        return nullReplyInGetAddrDisposition(completionStatus, null);
    }

    public INTERNAL nullReplyInGetAddrDisposition(Throwable th) {
        return nullReplyInGetAddrDisposition(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL nullReplyInGetAddrDisposition() {
        return nullReplyInGetAddrDisposition(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079719, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbTargetAddrPreferenceInExtractObjectkeyInvalid", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus completionStatus) {
        return orbTargetAddrPreferenceInExtractObjectkeyInvalid(completionStatus, null);
    }

    public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid(Throwable th) {
        return orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL orbTargetAddrPreferenceInExtractObjectkeyInvalid() {
        return orbTargetAddrPreferenceInExtractObjectkeyInvalid(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL invalidIsstreamedTckind(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398079720, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidIsstreamedTckind", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidIsstreamedTckind(CompletionStatus completionStatus, Object obj) {
        return invalidIsstreamedTckind(completionStatus, null, obj);
    }

    public INTERNAL invalidIsstreamedTckind(Throwable th, Object obj) {
        return invalidIsstreamedTckind(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL invalidIsstreamedTckind(Object obj) {
        return invalidIsstreamedTckind(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL invalidJdk131PatchLevel(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079721, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidJdk131PatchLevel", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidJdk131PatchLevel(CompletionStatus completionStatus) {
        return invalidJdk131PatchLevel(completionStatus, null);
    }

    public INTERNAL invalidJdk131PatchLevel(Throwable th) {
        return invalidJdk131PatchLevel(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL invalidJdk131PatchLevel() {
        return invalidJdk131PatchLevel(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL svcctxUnmarshalError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079722, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.svcctxUnmarshalError", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL svcctxUnmarshalError(CompletionStatus completionStatus) {
        return svcctxUnmarshalError(completionStatus, null);
    }

    public INTERNAL svcctxUnmarshalError(Throwable th) {
        return svcctxUnmarshalError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL svcctxUnmarshalError() {
        return svcctxUnmarshalError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL nullIor(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079723, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nullIor", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL nullIor(CompletionStatus completionStatus) {
        return nullIor(completionStatus, null);
    }

    public INTERNAL nullIor(Throwable th) {
        return nullIor(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL nullIor() {
        return nullIor(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unsupportedGiopVersion(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398079724, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unsupportedGiopVersion", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unsupportedGiopVersion(CompletionStatus completionStatus, Object obj) {
        return unsupportedGiopVersion(completionStatus, null, obj);
    }

    public INTERNAL unsupportedGiopVersion(Throwable th, Object obj) {
        return unsupportedGiopVersion(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL unsupportedGiopVersion(Object obj) {
        return unsupportedGiopVersion(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL applicationExceptionInSpecialMethod(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079725, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.applicationExceptionInSpecialMethod", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL applicationExceptionInSpecialMethod(CompletionStatus completionStatus) {
        return applicationExceptionInSpecialMethod(completionStatus, null);
    }

    public INTERNAL applicationExceptionInSpecialMethod(Throwable th) {
        return applicationExceptionInSpecialMethod(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL applicationExceptionInSpecialMethod() {
        return applicationExceptionInSpecialMethod(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL statementNotReachable1(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079726, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.statementNotReachable1", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL statementNotReachable1(CompletionStatus completionStatus) {
        return statementNotReachable1(completionStatus, null);
    }

    public INTERNAL statementNotReachable1(Throwable th) {
        return statementNotReachable1(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL statementNotReachable1() {
        return statementNotReachable1(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL statementNotReachable2(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079727, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.statementNotReachable2", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL statementNotReachable2(CompletionStatus completionStatus) {
        return statementNotReachable2(completionStatus, null);
    }

    public INTERNAL statementNotReachable2(Throwable th) {
        return statementNotReachable2(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL statementNotReachable2() {
        return statementNotReachable2(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL statementNotReachable3(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079728, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.statementNotReachable3", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL statementNotReachable3(CompletionStatus completionStatus) {
        return statementNotReachable3(completionStatus, null);
    }

    public INTERNAL statementNotReachable3(Throwable th) {
        return statementNotReachable3(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL statementNotReachable3() {
        return statementNotReachable3(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL statementNotReachable4(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079729, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.statementNotReachable4", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL statementNotReachable4(CompletionStatus completionStatus) {
        return statementNotReachable4(completionStatus, null);
    }

    public INTERNAL statementNotReachable4(Throwable th) {
        return statementNotReachable4(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL statementNotReachable4() {
        return statementNotReachable4(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL statementNotReachable5(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079730, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.statementNotReachable5", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL statementNotReachable5(CompletionStatus completionStatus) {
        return statementNotReachable5(completionStatus, null);
    }

    public INTERNAL statementNotReachable5(Throwable th) {
        return statementNotReachable5(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL statementNotReachable5() {
        return statementNotReachable5(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL statementNotReachable6(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079731, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.statementNotReachable6", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL statementNotReachable6(CompletionStatus completionStatus) {
        return statementNotReachable6(completionStatus, null);
    }

    public INTERNAL statementNotReachable6(Throwable th) {
        return statementNotReachable6(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL statementNotReachable6() {
        return statementNotReachable6(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unexpectedDiiException(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079732, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedDiiException", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unexpectedDiiException(CompletionStatus completionStatus) {
        return unexpectedDiiException(completionStatus, null);
    }

    public INTERNAL unexpectedDiiException(Throwable th) {
        return unexpectedDiiException(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL unexpectedDiiException() {
        return unexpectedDiiException(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL methodShouldNotBeCalled(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079733, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.methodShouldNotBeCalled", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL methodShouldNotBeCalled(CompletionStatus completionStatus) {
        return methodShouldNotBeCalled(completionStatus, null);
    }

    public INTERNAL methodShouldNotBeCalled(Throwable th) {
        return methodShouldNotBeCalled(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL methodShouldNotBeCalled() {
        return methodShouldNotBeCalled(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL cancelNotSupported(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079734, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cancelNotSupported", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL cancelNotSupported(CompletionStatus completionStatus) {
        return cancelNotSupported(completionStatus, null);
    }

    public INTERNAL cancelNotSupported(Throwable th) {
        return cancelNotSupported(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL cancelNotSupported() {
        return cancelNotSupported(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL emptyStackRunServantPostInvoke(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079735, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.emptyStackRunServantPostInvoke", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL emptyStackRunServantPostInvoke(CompletionStatus completionStatus) {
        return emptyStackRunServantPostInvoke(completionStatus, null);
    }

    public INTERNAL emptyStackRunServantPostInvoke(Throwable th) {
        return emptyStackRunServantPostInvoke(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL emptyStackRunServantPostInvoke() {
        return emptyStackRunServantPostInvoke(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL problemWithExceptionTypecode(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079736, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.problemWithExceptionTypecode", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL problemWithExceptionTypecode(CompletionStatus completionStatus) {
        return problemWithExceptionTypecode(completionStatus, null);
    }

    public INTERNAL problemWithExceptionTypecode(Throwable th) {
        return problemWithExceptionTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL problemWithExceptionTypecode() {
        return problemWithExceptionTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL illegalSubcontractId(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398079737, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalSubcontractId", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL illegalSubcontractId(CompletionStatus completionStatus, Object obj) {
        return illegalSubcontractId(completionStatus, null, obj);
    }

    public INTERNAL illegalSubcontractId(Throwable th, Object obj) {
        return illegalSubcontractId(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL illegalSubcontractId(Object obj) {
        return illegalSubcontractId(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL badSystemExceptionInLocateReply(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079738, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badSystemExceptionInLocateReply", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badSystemExceptionInLocateReply(CompletionStatus completionStatus) {
        return badSystemExceptionInLocateReply(completionStatus, null);
    }

    public INTERNAL badSystemExceptionInLocateReply(Throwable th) {
        return badSystemExceptionInLocateReply(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badSystemExceptionInLocateReply() {
        return badSystemExceptionInLocateReply(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badSystemExceptionInReply(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079739, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badSystemExceptionInReply", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badSystemExceptionInReply(CompletionStatus completionStatus) {
        return badSystemExceptionInReply(completionStatus, null);
    }

    public INTERNAL badSystemExceptionInReply(Throwable th) {
        return badSystemExceptionInReply(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badSystemExceptionInReply() {
        return badSystemExceptionInReply(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badCompletionStatusInLocateReply(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398079740, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCompletionStatusInLocateReply", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badCompletionStatusInLocateReply(CompletionStatus completionStatus, Object obj) {
        return badCompletionStatusInLocateReply(completionStatus, null, obj);
    }

    public INTERNAL badCompletionStatusInLocateReply(Throwable th, Object obj) {
        return badCompletionStatusInLocateReply(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL badCompletionStatusInLocateReply(Object obj) {
        return badCompletionStatusInLocateReply(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL badCompletionStatusInReply(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(1398079741, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCompletionStatusInReply", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badCompletionStatusInReply(CompletionStatus completionStatus, Object obj) {
        return badCompletionStatusInReply(completionStatus, null, obj);
    }

    public INTERNAL badCompletionStatusInReply(Throwable th, Object obj) {
        return badCompletionStatusInReply(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL badCompletionStatusInReply(Object obj) {
        return badCompletionStatusInReply(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL badkindCannotOccur(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079742, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badkindCannotOccur", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badkindCannotOccur(CompletionStatus completionStatus) {
        return badkindCannotOccur(completionStatus, null);
    }

    public INTERNAL badkindCannotOccur(Throwable th) {
        return badkindCannotOccur(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badkindCannotOccur() {
        return badkindCannotOccur(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL errorResolvingAlias(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079743, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.errorResolvingAlias", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorResolvingAlias(CompletionStatus completionStatus) {
        return errorResolvingAlias(completionStatus, null);
    }

    public INTERNAL errorResolvingAlias(Throwable th) {
        return errorResolvingAlias(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL errorResolvingAlias() {
        return errorResolvingAlias(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL tkLongDoubleNotSupported(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079744, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.tkLongDoubleNotSupported", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL tkLongDoubleNotSupported(CompletionStatus completionStatus) {
        return tkLongDoubleNotSupported(completionStatus, null);
    }

    public INTERNAL tkLongDoubleNotSupported(Throwable th) {
        return tkLongDoubleNotSupported(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL tkLongDoubleNotSupported() {
        return tkLongDoubleNotSupported(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL typecodeNotSupported(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079745, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.typecodeNotSupported", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL typecodeNotSupported(CompletionStatus completionStatus) {
        return typecodeNotSupported(completionStatus, null);
    }

    public INTERNAL typecodeNotSupported(Throwable th) {
        return typecodeNotSupported(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL typecodeNotSupported() {
        return typecodeNotSupported(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL boundsCannotOccur(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(1398079747, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.boundsCannotOccur", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL boundsCannotOccur(CompletionStatus completionStatus) {
        return boundsCannotOccur(completionStatus, null);
    }

    public INTERNAL boundsCannotOccur(Throwable th) {
        return boundsCannotOccur(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL boundsCannotOccur() {
        return boundsCannotOccur(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL numInvocationsAlreadyZero(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(NUM_INVOCATIONS_ALREADY_ZERO, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.numInvocationsAlreadyZero", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL numInvocationsAlreadyZero(CompletionStatus completionStatus) {
        return numInvocationsAlreadyZero(completionStatus, null);
    }

    public INTERNAL numInvocationsAlreadyZero(Throwable th) {
        return numInvocationsAlreadyZero(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL numInvocationsAlreadyZero() {
        return numInvocationsAlreadyZero(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL errorInitBadserveridhandler(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(ERROR_INIT_BADSERVERIDHANDLER, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.errorInitBadserveridhandler", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL errorInitBadserveridhandler(CompletionStatus completionStatus) {
        return errorInitBadserveridhandler(completionStatus, null);
    }

    public INTERNAL errorInitBadserveridhandler(Throwable th) {
        return errorInitBadserveridhandler(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL errorInitBadserveridhandler() {
        return errorInitBadserveridhandler(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL noToa(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(NO_TOA, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noToa", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL noToa(CompletionStatus completionStatus) {
        return noToa(completionStatus, null);
    }

    public INTERNAL noToa(Throwable th) {
        return noToa(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL noToa() {
        return noToa(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL noPoa(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(NO_POA, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noPoa", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL noPoa(CompletionStatus completionStatus) {
        return noPoa(completionStatus, null);
    }

    public INTERNAL noPoa(Throwable th) {
        return noPoa(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL noPoa() {
        return noPoa(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL invocationInfoStackEmpty(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INVOCATION_INFO_STACK_EMPTY, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invocationInfoStackEmpty", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invocationInfoStackEmpty(CompletionStatus completionStatus) {
        return invocationInfoStackEmpty(completionStatus, null);
    }

    public INTERNAL invocationInfoStackEmpty(Throwable th) {
        return invocationInfoStackEmpty(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL invocationInfoStackEmpty() {
        return invocationInfoStackEmpty(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badCodeSetString(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(BAD_CODE_SET_STRING, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCodeSetString", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badCodeSetString(CompletionStatus completionStatus) {
        return badCodeSetString(completionStatus, null);
    }

    public INTERNAL badCodeSetString(Throwable th) {
        return badCodeSetString(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badCodeSetString() {
        return badCodeSetString(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unknownNativeCodeset(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(UNKNOWN_NATIVE_CODESET, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownNativeCodeset", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unknownNativeCodeset(CompletionStatus completionStatus, Object obj) {
        return unknownNativeCodeset(completionStatus, null, obj);
    }

    public INTERNAL unknownNativeCodeset(Throwable th, Object obj) {
        return unknownNativeCodeset(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL unknownNativeCodeset(Object obj) {
        return unknownNativeCodeset(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL unknownConversionCodeSet(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(UNKNOWN_CONVERSION_CODE_SET, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownConversionCodeSet", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unknownConversionCodeSet(CompletionStatus completionStatus, Object obj) {
        return unknownConversionCodeSet(completionStatus, null, obj);
    }

    public INTERNAL unknownConversionCodeSet(Throwable th, Object obj) {
        return unknownConversionCodeSet(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL unknownConversionCodeSet(Object obj) {
        return unknownConversionCodeSet(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL invalidCodeSetNumber(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INVALID_CODE_SET_NUMBER, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidCodeSetNumber", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidCodeSetNumber(CompletionStatus completionStatus) {
        return invalidCodeSetNumber(completionStatus, null);
    }

    public INTERNAL invalidCodeSetNumber(Throwable th) {
        return invalidCodeSetNumber(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL invalidCodeSetNumber() {
        return invalidCodeSetNumber(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL invalidCodeSetString(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(INVALID_CODE_SET_STRING, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidCodeSetString", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidCodeSetString(CompletionStatus completionStatus, Object obj) {
        return invalidCodeSetString(completionStatus, null, obj);
    }

    public INTERNAL invalidCodeSetString(Throwable th, Object obj) {
        return invalidCodeSetString(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL invalidCodeSetString(Object obj) {
        return invalidCodeSetString(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL invalidCtbConverterName(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(INVALID_CTB_CONVERTER_NAME, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidCtbConverterName", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidCtbConverterName(CompletionStatus completionStatus, Object obj) {
        return invalidCtbConverterName(completionStatus, null, obj);
    }

    public INTERNAL invalidCtbConverterName(Throwable th, Object obj) {
        return invalidCtbConverterName(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL invalidCtbConverterName(Object obj) {
        return invalidCtbConverterName(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL invalidBtcConverterName(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(INVALID_BTC_CONVERTER_NAME, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidBtcConverterName", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidBtcConverterName(CompletionStatus completionStatus, Object obj) {
        return invalidBtcConverterName(completionStatus, null, obj);
    }

    public INTERNAL invalidBtcConverterName(Throwable th, Object obj) {
        return invalidBtcConverterName(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL invalidBtcConverterName(Object obj) {
        return invalidBtcConverterName(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL couldNotDuplicateCdrInputStream(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(COULD_NOT_DUPLICATE_CDR_INPUT_STREAM, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotDuplicateCdrInputStream", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL couldNotDuplicateCdrInputStream(CompletionStatus completionStatus) {
        return couldNotDuplicateCdrInputStream(completionStatus, null);
    }

    public INTERNAL couldNotDuplicateCdrInputStream(Throwable th) {
        return couldNotDuplicateCdrInputStream(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL couldNotDuplicateCdrInputStream() {
        return couldNotDuplicateCdrInputStream(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL bootstrapApplicationException(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(BOOTSTRAP_APPLICATION_EXCEPTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.bootstrapApplicationException", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL bootstrapApplicationException(CompletionStatus completionStatus) {
        return bootstrapApplicationException(completionStatus, null);
    }

    public INTERNAL bootstrapApplicationException(Throwable th) {
        return bootstrapApplicationException(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL bootstrapApplicationException() {
        return bootstrapApplicationException(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL duplicateIndirectionOffset(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(DUPLICATE_INDIRECTION_OFFSET, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.duplicateIndirectionOffset", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL duplicateIndirectionOffset(CompletionStatus completionStatus) {
        return duplicateIndirectionOffset(completionStatus, null);
    }

    public INTERNAL duplicateIndirectionOffset(Throwable th) {
        return duplicateIndirectionOffset(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL duplicateIndirectionOffset() {
        return duplicateIndirectionOffset(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badMessageTypeForCancel(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(BAD_MESSAGE_TYPE_FOR_CANCEL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badMessageTypeForCancel", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badMessageTypeForCancel(CompletionStatus completionStatus) {
        return badMessageTypeForCancel(completionStatus, null);
    }

    public INTERNAL badMessageTypeForCancel(Throwable th) {
        return badMessageTypeForCancel(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badMessageTypeForCancel() {
        return badMessageTypeForCancel(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL duplicateExceptionDetailMessage(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(DUPLICATE_EXCEPTION_DETAIL_MESSAGE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.duplicateExceptionDetailMessage", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL duplicateExceptionDetailMessage(CompletionStatus completionStatus) {
        return duplicateExceptionDetailMessage(completionStatus, null);
    }

    public INTERNAL duplicateExceptionDetailMessage(Throwable th) {
        return duplicateExceptionDetailMessage(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL duplicateExceptionDetailMessage() {
        return duplicateExceptionDetailMessage(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL badExceptionDetailMessageServiceContextType(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(BAD_EXCEPTION_DETAIL_MESSAGE_SERVICE_CONTEXT_TYPE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badExceptionDetailMessageServiceContextType", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badExceptionDetailMessageServiceContextType(CompletionStatus completionStatus) {
        return badExceptionDetailMessageServiceContextType(completionStatus, null);
    }

    public INTERNAL badExceptionDetailMessageServiceContextType(Throwable th) {
        return badExceptionDetailMessageServiceContextType(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL badExceptionDetailMessageServiceContextType() {
        return badExceptionDetailMessageServiceContextType(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(UNEXPECTED_DIRECT_BYTE_BUFFER_WITH_NON_CHANNEL_SOCKET, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedDirectByteBufferWithNonChannelSocket", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus completionStatus) {
        return unexpectedDirectByteBufferWithNonChannelSocket(completionStatus, null);
    }

    public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket(Throwable th) {
        return unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL unexpectedDirectByteBufferWithNonChannelSocket() {
        return unexpectedDirectByteBufferWithNonChannelSocket(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(UNEXPECTED_NON_DIRECT_BYTE_BUFFER_WITH_CHANNEL_SOCKET, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedNonDirectByteBufferWithChannelSocket", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus completionStatus) {
        return unexpectedNonDirectByteBufferWithChannelSocket(completionStatus, null);
    }

    public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket(Throwable th) {
        return unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL unexpectedNonDirectByteBufferWithChannelSocket() {
        return unexpectedNonDirectByteBufferWithChannelSocket(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL invalidContactInfoListIteratorFailureException(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(INVALID_CONTACT_INFO_LIST_ITERATOR_FAILURE_EXCEPTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidContactInfoListIteratorFailureException", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidContactInfoListIteratorFailureException(CompletionStatus completionStatus) {
        return invalidContactInfoListIteratorFailureException(completionStatus, null);
    }

    public INTERNAL invalidContactInfoListIteratorFailureException(Throwable th) {
        return invalidContactInfoListIteratorFailureException(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL invalidContactInfoListIteratorFailureException() {
        return invalidContactInfoListIteratorFailureException(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL remarshalWithNowhereToGo(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(REMARSHAL_WITH_NOWHERE_TO_GO, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.remarshalWithNowhereToGo", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL remarshalWithNowhereToGo(CompletionStatus completionStatus) {
        return remarshalWithNowhereToGo(completionStatus, null);
    }

    public INTERNAL remarshalWithNowhereToGo(Throwable th) {
        return remarshalWithNowhereToGo(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL remarshalWithNowhereToGo() {
        return remarshalWithNowhereToGo(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL exceptionWhenSendingCloseConnection(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(EXCEPTION_WHEN_SENDING_CLOSE_CONNECTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.exceptionWhenSendingCloseConnection", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL exceptionWhenSendingCloseConnection(CompletionStatus completionStatus) {
        return exceptionWhenSendingCloseConnection(completionStatus, null);
    }

    public INTERNAL exceptionWhenSendingCloseConnection(Throwable th) {
        return exceptionWhenSendingCloseConnection(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL exceptionWhenSendingCloseConnection() {
        return exceptionWhenSendingCloseConnection(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL invocationErrorInReflectiveTie(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(INVOCATION_ERROR_IN_REFLECTIVE_TIE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invocationErrorInReflectiveTie", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invocationErrorInReflectiveTie(CompletionStatus completionStatus, Object obj, Object obj2) {
        return invocationErrorInReflectiveTie(completionStatus, null, obj, obj2);
    }

    public INTERNAL invocationErrorInReflectiveTie(Throwable th, Object obj, Object obj2) {
        return invocationErrorInReflectiveTie(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL invocationErrorInReflectiveTie(Object obj, Object obj2) {
        return invocationErrorInReflectiveTie(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL badHelperWriteMethod(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(BAD_HELPER_WRITE_METHOD, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badHelperWriteMethod", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badHelperWriteMethod(CompletionStatus completionStatus, Object obj) {
        return badHelperWriteMethod(completionStatus, null, obj);
    }

    public INTERNAL badHelperWriteMethod(Throwable th, Object obj) {
        return badHelperWriteMethod(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL badHelperWriteMethod(Object obj) {
        return badHelperWriteMethod(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL badHelperReadMethod(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(BAD_HELPER_READ_METHOD, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badHelperReadMethod", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badHelperReadMethod(CompletionStatus completionStatus, Object obj) {
        return badHelperReadMethod(completionStatus, null, obj);
    }

    public INTERNAL badHelperReadMethod(Throwable th, Object obj) {
        return badHelperReadMethod(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL badHelperReadMethod(Object obj) {
        return badHelperReadMethod(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL badHelperIdMethod(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(BAD_HELPER_ID_METHOD, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badHelperIdMethod", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL badHelperIdMethod(CompletionStatus completionStatus, Object obj) {
        return badHelperIdMethod(completionStatus, null, obj);
    }

    public INTERNAL badHelperIdMethod(Throwable th, Object obj) {
        return badHelperIdMethod(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL badHelperIdMethod(Object obj) {
        return badHelperIdMethod(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL writeUndeclaredException(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(WRITE_UNDECLARED_EXCEPTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.writeUndeclaredException", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL writeUndeclaredException(CompletionStatus completionStatus, Object obj) {
        return writeUndeclaredException(completionStatus, null, obj);
    }

    public INTERNAL writeUndeclaredException(Throwable th, Object obj) {
        return writeUndeclaredException(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL writeUndeclaredException(Object obj) {
        return writeUndeclaredException(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL readUndeclaredException(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(READ_UNDECLARED_EXCEPTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.readUndeclaredException", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL readUndeclaredException(CompletionStatus completionStatus, Object obj) {
        return readUndeclaredException(completionStatus, null, obj);
    }

    public INTERNAL readUndeclaredException(Throwable th, Object obj) {
        return readUndeclaredException(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL readUndeclaredException(Object obj) {
        return readUndeclaredException(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL unableToSetSocketFactoryOrb(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(UNABLE_TO_SET_SOCKET_FACTORY_ORB, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unableToSetSocketFactoryOrb", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unableToSetSocketFactoryOrb(CompletionStatus completionStatus) {
        return unableToSetSocketFactoryOrb(completionStatus, null);
    }

    public INTERNAL unableToSetSocketFactoryOrb(Throwable th) {
        return unableToSetSocketFactoryOrb(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL unableToSetSocketFactoryOrb() {
        return unableToSetSocketFactoryOrb(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL unexpectedException(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(UNEXPECTED_EXCEPTION, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedException", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL unexpectedException(CompletionStatus completionStatus) {
        return unexpectedException(completionStatus, null);
    }

    public INTERNAL unexpectedException(Throwable th) {
        return unexpectedException(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL unexpectedException() {
        return unexpectedException(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL noInvocationHandler(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(NO_INVOCATION_HANDLER, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noInvocationHandler", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL noInvocationHandler(CompletionStatus completionStatus, Object obj) {
        return noInvocationHandler(completionStatus, null, obj);
    }

    public INTERNAL noInvocationHandler(Throwable th, Object obj) {
        return noInvocationHandler(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL noInvocationHandler(Object obj) {
        return noInvocationHandler(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL invalidBuffMgrStrategy(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(INVALID_BUFF_MGR_STRATEGY, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidBuffMgrStrategy", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL invalidBuffMgrStrategy(CompletionStatus completionStatus, Object obj) {
        return invalidBuffMgrStrategy(completionStatus, null, obj);
    }

    public INTERNAL invalidBuffMgrStrategy(Throwable th, Object obj) {
        return invalidBuffMgrStrategy(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL invalidBuffMgrStrategy(Object obj) {
        return invalidBuffMgrStrategy(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL javaStreamInitFailed(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(JAVA_STREAM_INIT_FAILED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.javaStreamInitFailed", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL javaStreamInitFailed(CompletionStatus completionStatus) {
        return javaStreamInitFailed(completionStatus, null);
    }

    public INTERNAL javaStreamInitFailed(Throwable th) {
        return javaStreamInitFailed(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL javaStreamInitFailed() {
        return javaStreamInitFailed(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL duplicateOrbVersionServiceContext(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(DUPLICATE_ORB_VERSION_SERVICE_CONTEXT, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.duplicateOrbVersionServiceContext", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL duplicateOrbVersionServiceContext(CompletionStatus completionStatus) {
        return duplicateOrbVersionServiceContext(completionStatus, null);
    }

    public INTERNAL duplicateOrbVersionServiceContext(Throwable th) {
        return duplicateOrbVersionServiceContext(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL duplicateOrbVersionServiceContext() {
        return duplicateOrbVersionServiceContext(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL duplicateSendingContextServiceContext(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(DUPLICATE_SENDING_CONTEXT_SERVICE_CONTEXT, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.duplicateSendingContextServiceContext", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL duplicateSendingContextServiceContext(CompletionStatus completionStatus) {
        return duplicateSendingContextServiceContext(completionStatus, null);
    }

    public INTERNAL duplicateSendingContextServiceContext(Throwable th) {
        return duplicateSendingContextServiceContext(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL duplicateSendingContextServiceContext() {
        return duplicateSendingContextServiceContext(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL workQueueThreadInterrupted(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(WORK_QUEUE_THREAD_INTERRUPTED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.workQueueThreadInterrupted", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workQueueThreadInterrupted(CompletionStatus completionStatus, Object obj, Object obj2) {
        return workQueueThreadInterrupted(completionStatus, null, obj, obj2);
    }

    public INTERNAL workQueueThreadInterrupted(Throwable th, Object obj, Object obj2) {
        return workQueueThreadInterrupted(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL workQueueThreadInterrupted(Object obj, Object obj2) {
        return workQueueThreadInterrupted(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL workerThreadCreated(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_CREATED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.workerThreadCreated", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadCreated(CompletionStatus completionStatus, Object obj, Object obj2) {
        return workerThreadCreated(completionStatus, null, obj, obj2);
    }

    public INTERNAL workerThreadCreated(Throwable th, Object obj, Object obj2) {
        return workerThreadCreated(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL workerThreadCreated(Object obj, Object obj2) {
        return workerThreadCreated(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL workerThreadThrowableFromRequestWork(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_THROWABLE_FROM_REQUEST_WORK, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.workerThreadThrowableFromRequestWork", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadThrowableFromRequestWork(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return workerThreadThrowableFromRequestWork(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL workerThreadThrowableFromRequestWork(Throwable th, Object obj, Object obj2, Object obj3) {
        return workerThreadThrowableFromRequestWork(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL workerThreadThrowableFromRequestWork(Object obj, Object obj2, Object obj3) {
        return workerThreadThrowableFromRequestWork(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL workerThreadNotNeeded(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_NOT_NEEDED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.workerThreadNotNeeded", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadNotNeeded(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return workerThreadNotNeeded(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL workerThreadNotNeeded(Throwable th, Object obj, Object obj2, Object obj3) {
        return workerThreadNotNeeded(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL workerThreadNotNeeded(Object obj, Object obj2, Object obj3) {
        return workerThreadNotNeeded(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL workerThreadDoWorkThrowable(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_DO_WORK_THROWABLE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.workerThreadDoWorkThrowable", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadDoWorkThrowable(CompletionStatus completionStatus, Object obj, Object obj2) {
        return workerThreadDoWorkThrowable(completionStatus, null, obj, obj2);
    }

    public INTERNAL workerThreadDoWorkThrowable(Throwable th, Object obj, Object obj2) {
        return workerThreadDoWorkThrowable(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL workerThreadDoWorkThrowable(Object obj, Object obj2) {
        return workerThreadDoWorkThrowable(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL workerThreadCaughtUnexpectedThrowable(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_CAUGHT_UNEXPECTED_THROWABLE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.workerThreadCaughtUnexpectedThrowable", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadCaughtUnexpectedThrowable(CompletionStatus completionStatus, Object obj, Object obj2) {
        return workerThreadCaughtUnexpectedThrowable(completionStatus, null, obj, obj2);
    }

    public INTERNAL workerThreadCaughtUnexpectedThrowable(Throwable th, Object obj, Object obj2) {
        return workerThreadCaughtUnexpectedThrowable(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL workerThreadCaughtUnexpectedThrowable(Object obj, Object obj2) {
        return workerThreadCaughtUnexpectedThrowable(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL workerThreadCreationFailure(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_CREATION_FAILURE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.SEVERE)) {
            doLog(Level.SEVERE, "ORBUTIL.workerThreadCreationFailure", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadCreationFailure(CompletionStatus completionStatus, Object obj) {
        return workerThreadCreationFailure(completionStatus, null, obj);
    }

    public INTERNAL workerThreadCreationFailure(Throwable th, Object obj) {
        return workerThreadCreationFailure(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL workerThreadCreationFailure(Object obj) {
        return workerThreadCreationFailure(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL workerThreadSetNameFailure(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2, Object obj3) {
        INTERNAL internal = new INTERNAL(WORKER_THREAD_SET_NAME_FAILURE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.workerThreadSetNameFailure", new Object[]{obj, obj2, obj3}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workerThreadSetNameFailure(CompletionStatus completionStatus, Object obj, Object obj2, Object obj3) {
        return workerThreadSetNameFailure(completionStatus, null, obj, obj2, obj3);
    }

    public INTERNAL workerThreadSetNameFailure(Throwable th, Object obj, Object obj2, Object obj3) {
        return workerThreadSetNameFailure(CompletionStatus.COMPLETED_NO, th, obj, obj2, obj3);
    }

    public INTERNAL workerThreadSetNameFailure(Object obj, Object obj2, Object obj3) {
        return workerThreadSetNameFailure(CompletionStatus.COMPLETED_NO, null, obj, obj2, obj3);
    }

    public INTERNAL workQueueRequestWorkNoWorkFound(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(WORK_QUEUE_REQUEST_WORK_NO_WORK_FOUND, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.workQueueRequestWorkNoWorkFound", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL workQueueRequestWorkNoWorkFound(CompletionStatus completionStatus, Object obj, Object obj2) {
        return workQueueRequestWorkNoWorkFound(completionStatus, null, obj, obj2);
    }

    public INTERNAL workQueueRequestWorkNoWorkFound(Throwable th, Object obj, Object obj2) {
        return workQueueRequestWorkNoWorkFound(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL workQueueRequestWorkNoWorkFound(Object obj, Object obj2) {
        return workQueueRequestWorkNoWorkFound(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL threadPoolCloseError(CompletionStatus completionStatus, Throwable th) {
        INTERNAL internal = new INTERNAL(THREAD_POOL_CLOSE_ERROR, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.threadPoolCloseError", null, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL threadPoolCloseError(CompletionStatus completionStatus) {
        return threadPoolCloseError(completionStatus, null);
    }

    public INTERNAL threadPoolCloseError(Throwable th) {
        return threadPoolCloseError(CompletionStatus.COMPLETED_NO, th);
    }

    public INTERNAL threadPoolCloseError() {
        return threadPoolCloseError(CompletionStatus.COMPLETED_NO, null);
    }

    public INTERNAL threadGroupIsDestroyed(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(THREAD_GROUP_IS_DESTROYED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.threadGroupIsDestroyed", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL threadGroupIsDestroyed(CompletionStatus completionStatus, Object obj) {
        return threadGroupIsDestroyed(completionStatus, null, obj);
    }

    public INTERNAL threadGroupIsDestroyed(Throwable th, Object obj) {
        return threadGroupIsDestroyed(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL threadGroupIsDestroyed(Object obj) {
        return threadGroupIsDestroyed(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL threadGroupHasActiveThreadsInClose(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(THREAD_GROUP_HAS_ACTIVE_THREADS_IN_CLOSE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.threadGroupHasActiveThreadsInClose", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL threadGroupHasActiveThreadsInClose(CompletionStatus completionStatus, Object obj, Object obj2) {
        return threadGroupHasActiveThreadsInClose(completionStatus, null, obj, obj2);
    }

    public INTERNAL threadGroupHasActiveThreadsInClose(Throwable th, Object obj, Object obj2) {
        return threadGroupHasActiveThreadsInClose(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL threadGroupHasActiveThreadsInClose(Object obj, Object obj2) {
        return threadGroupHasActiveThreadsInClose(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL threadGroupHasSubGroupsInClose(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(THREAD_GROUP_HAS_SUB_GROUPS_IN_CLOSE, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.threadGroupHasSubGroupsInClose", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL threadGroupHasSubGroupsInClose(CompletionStatus completionStatus, Object obj, Object obj2) {
        return threadGroupHasSubGroupsInClose(completionStatus, null, obj, obj2);
    }

    public INTERNAL threadGroupHasSubGroupsInClose(Throwable th, Object obj, Object obj2) {
        return threadGroupHasSubGroupsInClose(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL threadGroupHasSubGroupsInClose(Object obj, Object obj2) {
        return threadGroupHasSubGroupsInClose(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public INTERNAL threadGroupDestroyFailed(CompletionStatus completionStatus, Throwable th, Object obj) {
        INTERNAL internal = new INTERNAL(THREAD_GROUP_DESTROY_FAILED, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.threadGroupDestroyFailed", new Object[]{obj}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL threadGroupDestroyFailed(CompletionStatus completionStatus, Object obj) {
        return threadGroupDestroyFailed(completionStatus, null, obj);
    }

    public INTERNAL threadGroupDestroyFailed(Throwable th, Object obj) {
        return threadGroupDestroyFailed(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public INTERNAL threadGroupDestroyFailed(Object obj) {
        return threadGroupDestroyFailed(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public INTERNAL interruptedJoinCallWhileClosingThreadPool(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        INTERNAL internal = new INTERNAL(INTERRUPTED_JOIN_CALL_WHILE_CLOSING_THREAD_POOL, completionStatus);
        if (th != null) {
            internal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.interruptedJoinCallWhileClosingThreadPool", new Object[]{obj, obj2}, ORBUtilSystemException.class, internal);
        }
        return internal;
    }

    public INTERNAL interruptedJoinCallWhileClosingThreadPool(CompletionStatus completionStatus, Object obj, Object obj2) {
        return interruptedJoinCallWhileClosingThreadPool(completionStatus, null, obj, obj2);
    }

    public INTERNAL interruptedJoinCallWhileClosingThreadPool(Throwable th, Object obj, Object obj2) {
        return interruptedJoinCallWhileClosingThreadPool(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public INTERNAL interruptedJoinCallWhileClosingThreadPool(Object obj, Object obj2) {
        return interruptedJoinCallWhileClosingThreadPool(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL chunkOverflow(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079689, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.chunkOverflow", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL chunkOverflow(CompletionStatus completionStatus) {
        return chunkOverflow(completionStatus, null);
    }

    public MARSHAL chunkOverflow(Throwable th) {
        return chunkOverflow(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL chunkOverflow() {
        return chunkOverflow(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unexpectedEof(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079690, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedEof", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unexpectedEof(CompletionStatus completionStatus) {
        return unexpectedEof(completionStatus, null);
    }

    public MARSHAL unexpectedEof(Throwable th) {
        return unexpectedEof(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL unexpectedEof() {
        return unexpectedEof(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL readObjectException(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079691, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.readObjectException", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL readObjectException(CompletionStatus completionStatus) {
        return readObjectException(completionStatus, null);
    }

    public MARSHAL readObjectException(Throwable th) {
        return readObjectException(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL readObjectException() {
        return readObjectException(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL characterOutofrange(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079692, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.characterOutofrange", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL characterOutofrange(CompletionStatus completionStatus) {
        return characterOutofrange(completionStatus, null);
    }

    public MARSHAL characterOutofrange(Throwable th) {
        return characterOutofrange(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL characterOutofrange() {
        return characterOutofrange(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL dsiResultException(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079693, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.dsiResultException", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL dsiResultException(CompletionStatus completionStatus) {
        return dsiResultException(completionStatus, null);
    }

    public MARSHAL dsiResultException(Throwable th) {
        return dsiResultException(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL dsiResultException() {
        return dsiResultException(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL iiopinputstreamGrow(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079694, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.iiopinputstreamGrow", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL iiopinputstreamGrow(CompletionStatus completionStatus) {
        return iiopinputstreamGrow(completionStatus, null);
    }

    public MARSHAL iiopinputstreamGrow(Throwable th) {
        return iiopinputstreamGrow(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL iiopinputstreamGrow() {
        return iiopinputstreamGrow(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL endOfStream(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079695, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.endOfStream", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL endOfStream(CompletionStatus completionStatus) {
        return endOfStream(completionStatus, null);
    }

    public MARSHAL endOfStream(Throwable th) {
        return endOfStream(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL endOfStream() {
        return endOfStream(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL invalidObjectKey(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079696, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidObjectKey", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL invalidObjectKey(CompletionStatus completionStatus) {
        return invalidObjectKey(completionStatus, null);
    }

    public MARSHAL invalidObjectKey(Throwable th) {
        return invalidObjectKey(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL invalidObjectKey() {
        return invalidObjectKey(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL malformedUrl(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079697, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.malformedUrl", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL malformedUrl(CompletionStatus completionStatus, Object obj, Object obj2) {
        return malformedUrl(completionStatus, null, obj, obj2);
    }

    public MARSHAL malformedUrl(Throwable th, Object obj, Object obj2) {
        return malformedUrl(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL malformedUrl(Object obj, Object obj2) {
        return malformedUrl(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL valuehandlerReadError(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079698, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.valuehandlerReadError", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL valuehandlerReadError(CompletionStatus completionStatus) {
        return valuehandlerReadError(completionStatus, null);
    }

    public MARSHAL valuehandlerReadError(Throwable th) {
        return valuehandlerReadError(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL valuehandlerReadError() {
        return valuehandlerReadError(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL valuehandlerReadException(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079699, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.valuehandlerReadException", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL valuehandlerReadException(CompletionStatus completionStatus) {
        return valuehandlerReadException(completionStatus, null);
    }

    public MARSHAL valuehandlerReadException(Throwable th) {
        return valuehandlerReadException(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL valuehandlerReadException() {
        return valuehandlerReadException(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badKind(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079700, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badKind", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badKind(CompletionStatus completionStatus) {
        return badKind(completionStatus, null);
    }

    public MARSHAL badKind(Throwable th) {
        return badKind(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badKind() {
        return badKind(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL cnfeReadClass(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079701, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cnfeReadClass", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL cnfeReadClass(CompletionStatus completionStatus, Object obj) {
        return cnfeReadClass(completionStatus, null, obj);
    }

    public MARSHAL cnfeReadClass(Throwable th, Object obj) {
        return cnfeReadClass(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL cnfeReadClass(Object obj) {
        return cnfeReadClass(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL badRepIdIndirection(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079702, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badRepIdIndirection", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badRepIdIndirection(CompletionStatus completionStatus, Object obj) {
        return badRepIdIndirection(completionStatus, null, obj);
    }

    public MARSHAL badRepIdIndirection(Throwable th, Object obj) {
        return badRepIdIndirection(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL badRepIdIndirection(Object obj) {
        return badRepIdIndirection(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL badCodebaseIndirection(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079703, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badCodebaseIndirection", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badCodebaseIndirection(CompletionStatus completionStatus, Object obj) {
        return badCodebaseIndirection(completionStatus, null, obj);
    }

    public MARSHAL badCodebaseIndirection(Throwable th, Object obj) {
        return badCodebaseIndirection(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL badCodebaseIndirection(Object obj) {
        return badCodebaseIndirection(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL unknownCodeset(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079704, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownCodeset", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unknownCodeset(CompletionStatus completionStatus, Object obj) {
        return unknownCodeset(completionStatus, null, obj);
    }

    public MARSHAL unknownCodeset(Throwable th, Object obj) {
        return unknownCodeset(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL unknownCodeset(Object obj) {
        return unknownCodeset(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL wcharDataInGiop10(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079705, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.wcharDataInGiop10", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL wcharDataInGiop10(CompletionStatus completionStatus) {
        return wcharDataInGiop10(completionStatus, null);
    }

    public MARSHAL wcharDataInGiop10(Throwable th) {
        return wcharDataInGiop10(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL wcharDataInGiop10() {
        return wcharDataInGiop10(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL negativeStringLength(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079706, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.negativeStringLength", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL negativeStringLength(CompletionStatus completionStatus, Object obj) {
        return negativeStringLength(completionStatus, null, obj);
    }

    public MARSHAL negativeStringLength(Throwable th, Object obj) {
        return negativeStringLength(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL negativeStringLength(Object obj) {
        return negativeStringLength(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL expectedTypeNullAndNoRepId(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079707, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.expectedTypeNullAndNoRepId", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL expectedTypeNullAndNoRepId(CompletionStatus completionStatus) {
        return expectedTypeNullAndNoRepId(completionStatus, null);
    }

    public MARSHAL expectedTypeNullAndNoRepId(Throwable th) {
        return expectedTypeNullAndNoRepId(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL expectedTypeNullAndNoRepId() {
        return expectedTypeNullAndNoRepId(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL readValueAndNoRepId(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079708, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.readValueAndNoRepId", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL readValueAndNoRepId(CompletionStatus completionStatus) {
        return readValueAndNoRepId(completionStatus, null);
    }

    public MARSHAL readValueAndNoRepId(Throwable th) {
        return readValueAndNoRepId(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL readValueAndNoRepId() {
        return readValueAndNoRepId(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unexpectedEnclosingValuetype(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079710, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedEnclosingValuetype", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unexpectedEnclosingValuetype(CompletionStatus completionStatus, Object obj, Object obj2) {
        return unexpectedEnclosingValuetype(completionStatus, null, obj, obj2);
    }

    public MARSHAL unexpectedEnclosingValuetype(Throwable th, Object obj, Object obj2) {
        return unexpectedEnclosingValuetype(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL unexpectedEnclosingValuetype(Object obj, Object obj2) {
        return unexpectedEnclosingValuetype(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL positiveEndTag(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079711, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.positiveEndTag", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL positiveEndTag(CompletionStatus completionStatus, Object obj, Object obj2) {
        return positiveEndTag(completionStatus, null, obj, obj2);
    }

    public MARSHAL positiveEndTag(Throwable th, Object obj, Object obj2) {
        return positiveEndTag(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL positiveEndTag(Object obj, Object obj2) {
        return positiveEndTag(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL nullOutCall(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079712, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nullOutCall", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL nullOutCall(CompletionStatus completionStatus) {
        return nullOutCall(completionStatus, null);
    }

    public MARSHAL nullOutCall(Throwable th) {
        return nullOutCall(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL nullOutCall() {
        return nullOutCall(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL writeLocalObject(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079713, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.writeLocalObject", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL writeLocalObject(CompletionStatus completionStatus) {
        return writeLocalObject(completionStatus, null);
    }

    public MARSHAL writeLocalObject(Throwable th) {
        return writeLocalObject(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL writeLocalObject() {
        return writeLocalObject(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badInsertobjParam(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079714, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badInsertobjParam", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badInsertobjParam(CompletionStatus completionStatus, Object obj) {
        return badInsertobjParam(completionStatus, null, obj);
    }

    public MARSHAL badInsertobjParam(Throwable th, Object obj) {
        return badInsertobjParam(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL badInsertobjParam(Object obj) {
        return badInsertobjParam(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL customWrapperWithCodebase(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079715, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.customWrapperWithCodebase", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL customWrapperWithCodebase(CompletionStatus completionStatus) {
        return customWrapperWithCodebase(completionStatus, null);
    }

    public MARSHAL customWrapperWithCodebase(Throwable th) {
        return customWrapperWithCodebase(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL customWrapperWithCodebase() {
        return customWrapperWithCodebase(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL customWrapperIndirection(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079716, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.customWrapperIndirection", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL customWrapperIndirection(CompletionStatus completionStatus) {
        return customWrapperIndirection(completionStatus, null);
    }

    public MARSHAL customWrapperIndirection(Throwable th) {
        return customWrapperIndirection(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL customWrapperIndirection() {
        return customWrapperIndirection(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL customWrapperNotSingleRepid(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079717, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.customWrapperNotSingleRepid", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL customWrapperNotSingleRepid(CompletionStatus completionStatus) {
        return customWrapperNotSingleRepid(completionStatus, null);
    }

    public MARSHAL customWrapperNotSingleRepid(Throwable th) {
        return customWrapperNotSingleRepid(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL customWrapperNotSingleRepid() {
        return customWrapperNotSingleRepid(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badValueTag(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079718, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badValueTag", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badValueTag(CompletionStatus completionStatus, Object obj) {
        return badValueTag(completionStatus, null, obj);
    }

    public MARSHAL badValueTag(Throwable th, Object obj) {
        return badValueTag(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL badValueTag(Object obj) {
        return badValueTag(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL badTypecodeForCustomValue(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079719, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badTypecodeForCustomValue", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badTypecodeForCustomValue(CompletionStatus completionStatus) {
        return badTypecodeForCustomValue(completionStatus, null);
    }

    public MARSHAL badTypecodeForCustomValue(Throwable th) {
        return badTypecodeForCustomValue(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badTypecodeForCustomValue() {
        return badTypecodeForCustomValue(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL errorInvokingHelperWrite(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079720, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.errorInvokingHelperWrite", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL errorInvokingHelperWrite(CompletionStatus completionStatus) {
        return errorInvokingHelperWrite(completionStatus, null);
    }

    public MARSHAL errorInvokingHelperWrite(Throwable th) {
        return errorInvokingHelperWrite(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL errorInvokingHelperWrite() {
        return errorInvokingHelperWrite(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badDigitInFixed(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079721, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badDigitInFixed", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badDigitInFixed(CompletionStatus completionStatus) {
        return badDigitInFixed(completionStatus, null);
    }

    public MARSHAL badDigitInFixed(Throwable th) {
        return badDigitInFixed(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badDigitInFixed() {
        return badDigitInFixed(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL refTypeIndirType(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079722, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.refTypeIndirType", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL refTypeIndirType(CompletionStatus completionStatus) {
        return refTypeIndirType(completionStatus, null);
    }

    public MARSHAL refTypeIndirType(Throwable th) {
        return refTypeIndirType(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL refTypeIndirType() {
        return refTypeIndirType(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badReservedLength(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079723, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badReservedLength", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badReservedLength(CompletionStatus completionStatus) {
        return badReservedLength(completionStatus, null);
    }

    public MARSHAL badReservedLength(Throwable th) {
        return badReservedLength(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badReservedLength() {
        return badReservedLength(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL nullNotAllowed(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079724, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.nullNotAllowed", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL nullNotAllowed(CompletionStatus completionStatus) {
        return nullNotAllowed(completionStatus, null);
    }

    public MARSHAL nullNotAllowed(Throwable th) {
        return nullNotAllowed(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL nullNotAllowed() {
        return nullNotAllowed(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unionDiscriminatorError(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079726, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unionDiscriminatorError", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unionDiscriminatorError(CompletionStatus completionStatus) {
        return unionDiscriminatorError(completionStatus, null);
    }

    public MARSHAL unionDiscriminatorError(Throwable th) {
        return unionDiscriminatorError(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL unionDiscriminatorError() {
        return unionDiscriminatorError(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL cannotMarshalNative(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079727, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cannotMarshalNative", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL cannotMarshalNative(CompletionStatus completionStatus) {
        return cannotMarshalNative(completionStatus, null);
    }

    public MARSHAL cannotMarshalNative(Throwable th) {
        return cannotMarshalNative(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL cannotMarshalNative() {
        return cannotMarshalNative(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL cannotMarshalBadTckind(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079728, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.cannotMarshalBadTckind", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL cannotMarshalBadTckind(CompletionStatus completionStatus) {
        return cannotMarshalBadTckind(completionStatus, null);
    }

    public MARSHAL cannotMarshalBadTckind(Throwable th) {
        return cannotMarshalBadTckind(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL cannotMarshalBadTckind() {
        return cannotMarshalBadTckind(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL invalidIndirection(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079729, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidIndirection", new Object[]{obj}, ORBUtilSystemException.class, marshal);
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

    public MARSHAL indirectionNotFound(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079730, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.indirectionNotFound", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL indirectionNotFound(CompletionStatus completionStatus, Object obj) {
        return indirectionNotFound(completionStatus, null, obj);
    }

    public MARSHAL indirectionNotFound(Throwable th, Object obj) {
        return indirectionNotFound(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL indirectionNotFound(Object obj) {
        return indirectionNotFound(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL recursiveTypecodeError(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079731, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.recursiveTypecodeError", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL recursiveTypecodeError(CompletionStatus completionStatus) {
        return recursiveTypecodeError(completionStatus, null);
    }

    public MARSHAL recursiveTypecodeError(Throwable th) {
        return recursiveTypecodeError(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL recursiveTypecodeError() {
        return recursiveTypecodeError(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL invalidSimpleTypecode(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079732, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidSimpleTypecode", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL invalidSimpleTypecode(CompletionStatus completionStatus) {
        return invalidSimpleTypecode(completionStatus, null);
    }

    public MARSHAL invalidSimpleTypecode(Throwable th) {
        return invalidSimpleTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL invalidSimpleTypecode() {
        return invalidSimpleTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL invalidComplexTypecode(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079733, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidComplexTypecode", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL invalidComplexTypecode(CompletionStatus completionStatus) {
        return invalidComplexTypecode(completionStatus, null);
    }

    public MARSHAL invalidComplexTypecode(Throwable th) {
        return invalidComplexTypecode(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL invalidComplexTypecode() {
        return invalidComplexTypecode(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL invalidTypecodeKindMarshal(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079734, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.invalidTypecodeKindMarshal", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL invalidTypecodeKindMarshal(CompletionStatus completionStatus) {
        return invalidTypecodeKindMarshal(completionStatus, null);
    }

    public MARSHAL invalidTypecodeKindMarshal(Throwable th) {
        return invalidTypecodeKindMarshal(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL invalidTypecodeKindMarshal() {
        return invalidTypecodeKindMarshal(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL unexpectedUnionDefault(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079735, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unexpectedUnionDefault", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unexpectedUnionDefault(CompletionStatus completionStatus) {
        return unexpectedUnionDefault(completionStatus, null);
    }

    public MARSHAL unexpectedUnionDefault(Throwable th) {
        return unexpectedUnionDefault(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL unexpectedUnionDefault() {
        return unexpectedUnionDefault(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL illegalUnionDiscriminatorType(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079736, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.illegalUnionDiscriminatorType", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL illegalUnionDiscriminatorType(CompletionStatus completionStatus) {
        return illegalUnionDiscriminatorType(completionStatus, null);
    }

    public MARSHAL illegalUnionDiscriminatorType(Throwable th) {
        return illegalUnionDiscriminatorType(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL illegalUnionDiscriminatorType() {
        return illegalUnionDiscriminatorType(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL couldNotSkipBytes(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079737, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotSkipBytes", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL couldNotSkipBytes(CompletionStatus completionStatus, Object obj, Object obj2) {
        return couldNotSkipBytes(completionStatus, null, obj, obj2);
    }

    public MARSHAL couldNotSkipBytes(Throwable th, Object obj, Object obj2) {
        return couldNotSkipBytes(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL couldNotSkipBytes(Object obj, Object obj2) {
        return couldNotSkipBytes(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL badChunkLength(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079738, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badChunkLength", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badChunkLength(CompletionStatus completionStatus, Object obj, Object obj2) {
        return badChunkLength(completionStatus, null, obj, obj2);
    }

    public MARSHAL badChunkLength(Throwable th, Object obj, Object obj2) {
        return badChunkLength(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL badChunkLength(Object obj, Object obj2) {
        return badChunkLength(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL unableToLocateRepIdArray(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079739, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unableToLocateRepIdArray", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL unableToLocateRepIdArray(CompletionStatus completionStatus, Object obj) {
        return unableToLocateRepIdArray(completionStatus, null, obj);
    }

    public MARSHAL unableToLocateRepIdArray(Throwable th, Object obj) {
        return unableToLocateRepIdArray(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL unableToLocateRepIdArray(Object obj) {
        return unableToLocateRepIdArray(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL badFixed(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079740, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badFixed", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badFixed(CompletionStatus completionStatus, Object obj, Object obj2) {
        return badFixed(completionStatus, null, obj, obj2);
    }

    public MARSHAL badFixed(Throwable th, Object obj, Object obj2) {
        return badFixed(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL badFixed(Object obj, Object obj2) {
        return badFixed(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL readObjectLoadClassFailure(CompletionStatus completionStatus, Throwable th, Object obj, Object obj2) {
        MARSHAL marshal = new MARSHAL(1398079741, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.readObjectLoadClassFailure", new Object[]{obj, obj2}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL readObjectLoadClassFailure(CompletionStatus completionStatus, Object obj, Object obj2) {
        return readObjectLoadClassFailure(completionStatus, null, obj, obj2);
    }

    public MARSHAL readObjectLoadClassFailure(Throwable th, Object obj, Object obj2) {
        return readObjectLoadClassFailure(CompletionStatus.COMPLETED_NO, th, obj, obj2);
    }

    public MARSHAL readObjectLoadClassFailure(Object obj, Object obj2) {
        return readObjectLoadClassFailure(CompletionStatus.COMPLETED_NO, null, obj, obj2);
    }

    public MARSHAL couldNotInstantiateHelper(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079742, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotInstantiateHelper", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL couldNotInstantiateHelper(CompletionStatus completionStatus, Object obj) {
        return couldNotInstantiateHelper(completionStatus, null, obj);
    }

    public MARSHAL couldNotInstantiateHelper(Throwable th, Object obj) {
        return couldNotInstantiateHelper(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL couldNotInstantiateHelper(Object obj) {
        return couldNotInstantiateHelper(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL badToaOaid(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079743, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badToaOaid", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badToaOaid(CompletionStatus completionStatus) {
        return badToaOaid(completionStatus, null);
    }

    public MARSHAL badToaOaid(Throwable th) {
        return badToaOaid(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badToaOaid() {
        return badToaOaid(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL couldNotInvokeHelperReadMethod(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(1398079744, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotInvokeHelperReadMethod", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL couldNotInvokeHelperReadMethod(CompletionStatus completionStatus, Object obj) {
        return couldNotInvokeHelperReadMethod(completionStatus, null, obj);
    }

    public MARSHAL couldNotInvokeHelperReadMethod(Throwable th, Object obj) {
        return couldNotInvokeHelperReadMethod(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL couldNotInvokeHelperReadMethod(Object obj) {
        return couldNotInvokeHelperReadMethod(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public MARSHAL couldNotFindClass(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079745, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.couldNotFindClass", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL couldNotFindClass(CompletionStatus completionStatus) {
        return couldNotFindClass(completionStatus, null);
    }

    public MARSHAL couldNotFindClass(Throwable th) {
        return couldNotFindClass(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL couldNotFindClass() {
        return couldNotFindClass(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL badArgumentsNvlist(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(BAD_ARGUMENTS_NVLIST, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.badArgumentsNvlist", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL badArgumentsNvlist(CompletionStatus completionStatus) {
        return badArgumentsNvlist(completionStatus, null);
    }

    public MARSHAL badArgumentsNvlist(Throwable th) {
        return badArgumentsNvlist(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL badArgumentsNvlist() {
        return badArgumentsNvlist(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL stubCreateError(CompletionStatus completionStatus, Throwable th) {
        MARSHAL marshal = new MARSHAL(1398079747, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.stubCreateError", null, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL stubCreateError(CompletionStatus completionStatus) {
        return stubCreateError(completionStatus, null);
    }

    public MARSHAL stubCreateError(Throwable th) {
        return stubCreateError(CompletionStatus.COMPLETED_NO, th);
    }

    public MARSHAL stubCreateError() {
        return stubCreateError(CompletionStatus.COMPLETED_NO, null);
    }

    public MARSHAL javaSerializationException(CompletionStatus completionStatus, Throwable th, Object obj) {
        MARSHAL marshal = new MARSHAL(JAVA_SERIALIZATION_EXCEPTION, completionStatus);
        if (th != null) {
            marshal.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.javaSerializationException", new Object[]{obj}, ORBUtilSystemException.class, marshal);
        }
        return marshal;
    }

    public MARSHAL javaSerializationException(CompletionStatus completionStatus, Object obj) {
        return javaSerializationException(completionStatus, null, obj);
    }

    public MARSHAL javaSerializationException(Throwable th, Object obj) {
        return javaSerializationException(CompletionStatus.COMPLETED_NO, th, obj);
    }

    public MARSHAL javaSerializationException(Object obj) {
        return javaSerializationException(CompletionStatus.COMPLETED_NO, null, obj);
    }

    public NO_IMPLEMENT genericNoImpl(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398079689, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.genericNoImpl", null, ORBUtilSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT genericNoImpl(CompletionStatus completionStatus) {
        return genericNoImpl(completionStatus, null);
    }

    public NO_IMPLEMENT genericNoImpl(Throwable th) {
        return genericNoImpl(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT genericNoImpl() {
        return genericNoImpl(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT contextNotImplemented(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398079690, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.contextNotImplemented", null, ORBUtilSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT contextNotImplemented(CompletionStatus completionStatus) {
        return contextNotImplemented(completionStatus, null);
    }

    public NO_IMPLEMENT contextNotImplemented(Throwable th) {
        return contextNotImplemented(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT contextNotImplemented() {
        return contextNotImplemented(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT getinterfaceNotImplemented(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398079691, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.getinterfaceNotImplemented", null, ORBUtilSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT getinterfaceNotImplemented(CompletionStatus completionStatus) {
        return getinterfaceNotImplemented(completionStatus, null);
    }

    public NO_IMPLEMENT getinterfaceNotImplemented(Throwable th) {
        return getinterfaceNotImplemented(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT getinterfaceNotImplemented() {
        return getinterfaceNotImplemented(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT sendDeferredNotimplemented(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398079692, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.sendDeferredNotimplemented", null, ORBUtilSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT sendDeferredNotimplemented(CompletionStatus completionStatus) {
        return sendDeferredNotimplemented(completionStatus, null);
    }

    public NO_IMPLEMENT sendDeferredNotimplemented(Throwable th) {
        return sendDeferredNotimplemented(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT sendDeferredNotimplemented() {
        return sendDeferredNotimplemented(CompletionStatus.COMPLETED_NO, null);
    }

    public NO_IMPLEMENT longDoubleNotImplemented(CompletionStatus completionStatus, Throwable th) {
        NO_IMPLEMENT no_implement = new NO_IMPLEMENT(1398079693, completionStatus);
        if (th != null) {
            no_implement.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.longDoubleNotImplemented", null, ORBUtilSystemException.class, no_implement);
        }
        return no_implement;
    }

    public NO_IMPLEMENT longDoubleNotImplemented(CompletionStatus completionStatus) {
        return longDoubleNotImplemented(completionStatus, null);
    }

    public NO_IMPLEMENT longDoubleNotImplemented(Throwable th) {
        return longDoubleNotImplemented(CompletionStatus.COMPLETED_NO, th);
    }

    public NO_IMPLEMENT longDoubleNotImplemented() {
        return longDoubleNotImplemented(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER noServerScInDispatch(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398079689, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noServerScInDispatch", null, ORBUtilSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER noServerScInDispatch(CompletionStatus completionStatus) {
        return noServerScInDispatch(completionStatus, null);
    }

    public OBJ_ADAPTER noServerScInDispatch(Throwable th) {
        return noServerScInDispatch(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER noServerScInDispatch() {
        return noServerScInDispatch(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER orbConnectError(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398079690, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.orbConnectError", null, ORBUtilSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER orbConnectError(CompletionStatus completionStatus) {
        return orbConnectError(completionStatus, null);
    }

    public OBJ_ADAPTER orbConnectError(Throwable th) {
        return orbConnectError(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER orbConnectError() {
        return orbConnectError(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJ_ADAPTER adapterInactiveInActivation(CompletionStatus completionStatus, Throwable th) {
        OBJ_ADAPTER obj_adapter = new OBJ_ADAPTER(1398079691, completionStatus);
        if (th != null) {
            obj_adapter.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.adapterInactiveInActivation", null, ORBUtilSystemException.class, obj_adapter);
        }
        return obj_adapter;
    }

    public OBJ_ADAPTER adapterInactiveInActivation(CompletionStatus completionStatus) {
        return adapterInactiveInActivation(completionStatus, null);
    }

    public OBJ_ADAPTER adapterInactiveInActivation(Throwable th) {
        return adapterInactiveInActivation(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJ_ADAPTER adapterInactiveInActivation() {
        return adapterInactiveInActivation(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST locateUnknownObject(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079689, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.locateUnknownObject", null, ORBUtilSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST locateUnknownObject(CompletionStatus completionStatus) {
        return locateUnknownObject(completionStatus, null);
    }

    public OBJECT_NOT_EXIST locateUnknownObject(Throwable th) {
        return locateUnknownObject(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST locateUnknownObject() {
        return locateUnknownObject(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST badServerId(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079690, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.badServerId", null, ORBUtilSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST badServerId(CompletionStatus completionStatus) {
        return badServerId(completionStatus, null);
    }

    public OBJECT_NOT_EXIST badServerId(Throwable th) {
        return badServerId(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST badServerId() {
        return badServerId(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST badSkeleton(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079691, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badSkeleton", null, ORBUtilSystemException.class, object_not_exist);
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

    public OBJECT_NOT_EXIST servantNotFound(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079692, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.servantNotFound", null, ORBUtilSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST servantNotFound(CompletionStatus completionStatus) {
        return servantNotFound(completionStatus, null);
    }

    public OBJECT_NOT_EXIST servantNotFound(Throwable th) {
        return servantNotFound(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST servantNotFound() {
        return servantNotFound(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST noObjectAdapterFactory(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079693, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noObjectAdapterFactory", null, ORBUtilSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST noObjectAdapterFactory(CompletionStatus completionStatus) {
        return noObjectAdapterFactory(completionStatus, null);
    }

    public OBJECT_NOT_EXIST noObjectAdapterFactory(Throwable th) {
        return noObjectAdapterFactory(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST noObjectAdapterFactory() {
        return noObjectAdapterFactory(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST badAdapterId(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079694, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.badAdapterId", null, ORBUtilSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST badAdapterId(CompletionStatus completionStatus) {
        return badAdapterId(completionStatus, null);
    }

    public OBJECT_NOT_EXIST badAdapterId(Throwable th) {
        return badAdapterId(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST badAdapterId() {
        return badAdapterId(CompletionStatus.COMPLETED_NO, null);
    }

    public OBJECT_NOT_EXIST dynAnyDestroyed(CompletionStatus completionStatus, Throwable th) {
        OBJECT_NOT_EXIST object_not_exist = new OBJECT_NOT_EXIST(1398079695, completionStatus);
        if (th != null) {
            object_not_exist.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.dynAnyDestroyed", null, ORBUtilSystemException.class, object_not_exist);
        }
        return object_not_exist;
    }

    public OBJECT_NOT_EXIST dynAnyDestroyed(CompletionStatus completionStatus) {
        return dynAnyDestroyed(completionStatus, null);
    }

    public OBJECT_NOT_EXIST dynAnyDestroyed(Throwable th) {
        return dynAnyDestroyed(CompletionStatus.COMPLETED_NO, th);
    }

    public OBJECT_NOT_EXIST dynAnyDestroyed() {
        return dynAnyDestroyed(CompletionStatus.COMPLETED_NO, null);
    }

    public TRANSIENT requestCanceled(CompletionStatus completionStatus, Throwable th) {
        TRANSIENT r0 = new TRANSIENT(1398079689, completionStatus);
        if (th != null) {
            r0.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.requestCanceled", null, ORBUtilSystemException.class, r0);
        }
        return r0;
    }

    public TRANSIENT requestCanceled(CompletionStatus completionStatus) {
        return requestCanceled(completionStatus, null);
    }

    public TRANSIENT requestCanceled(Throwable th) {
        return requestCanceled(CompletionStatus.COMPLETED_NO, th);
    }

    public TRANSIENT requestCanceled() {
        return requestCanceled(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownCorbaExc(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079689, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownCorbaExc", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownCorbaExc(CompletionStatus completionStatus) {
        return unknownCorbaExc(completionStatus, null);
    }

    public UNKNOWN unknownCorbaExc(Throwable th) {
        return unknownCorbaExc(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownCorbaExc() {
        return unknownCorbaExc(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN runtimeexception(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079690, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.runtimeexception", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN runtimeexception(CompletionStatus completionStatus) {
        return runtimeexception(completionStatus, null);
    }

    public UNKNOWN runtimeexception(Throwable th) {
        return runtimeexception(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN runtimeexception() {
        return runtimeexception(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownServerError(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079691, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownServerError", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownServerError(CompletionStatus completionStatus) {
        return unknownServerError(completionStatus, null);
    }

    public UNKNOWN unknownServerError(Throwable th) {
        return unknownServerError(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownServerError() {
        return unknownServerError(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownDsiSysex(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079692, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownDsiSysex", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownDsiSysex(CompletionStatus completionStatus) {
        return unknownDsiSysex(completionStatus, null);
    }

    public UNKNOWN unknownDsiSysex(Throwable th) {
        return unknownDsiSysex(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownDsiSysex() {
        return unknownDsiSysex(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownSysex(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079693, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.unknownSysex", null, ORBUtilSystemException.class, unknown);
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

    public UNKNOWN wrongInterfaceDef(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079694, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.wrongInterfaceDef", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN wrongInterfaceDef(CompletionStatus completionStatus) {
        return wrongInterfaceDef(completionStatus, null);
    }

    public UNKNOWN wrongInterfaceDef(Throwable th) {
        return wrongInterfaceDef(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN wrongInterfaceDef() {
        return wrongInterfaceDef(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN noInterfaceDefStub(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079695, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.WARNING)) {
            doLog(Level.WARNING, "ORBUTIL.noInterfaceDefStub", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN noInterfaceDefStub(CompletionStatus completionStatus) {
        return noInterfaceDefStub(completionStatus, null);
    }

    public UNKNOWN noInterfaceDefStub(Throwable th) {
        return noInterfaceDefStub(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN noInterfaceDefStub() {
        return noInterfaceDefStub(CompletionStatus.COMPLETED_NO, null);
    }

    public UNKNOWN unknownExceptionInDispatch(CompletionStatus completionStatus, Throwable th) {
        UNKNOWN unknown = new UNKNOWN(1398079697, completionStatus);
        if (th != null) {
            unknown.initCause(th);
        }
        if (this.logger.isLoggable(Level.FINE)) {
            doLog(Level.FINE, "ORBUTIL.unknownExceptionInDispatch", null, ORBUtilSystemException.class, unknown);
        }
        return unknown;
    }

    public UNKNOWN unknownExceptionInDispatch(CompletionStatus completionStatus) {
        return unknownExceptionInDispatch(completionStatus, null);
    }

    public UNKNOWN unknownExceptionInDispatch(Throwable th) {
        return unknownExceptionInDispatch(CompletionStatus.COMPLETED_NO, th);
    }

    public UNKNOWN unknownExceptionInDispatch() {
        return unknownExceptionInDispatch(CompletionStatus.COMPLETED_NO, null);
    }
}
