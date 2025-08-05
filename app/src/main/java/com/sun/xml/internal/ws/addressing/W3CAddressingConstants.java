package com.sun.xml.internal.ws.addressing;

import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/W3CAddressingConstants.class */
public interface W3CAddressingConstants {
    public static final String WSAW_SERVICENAME_NAME = "ServiceName";
    public static final String WSAW_INTERFACENAME_NAME = "InterfaceName";
    public static final String WSAW_ENDPOINTNAME_NAME = "EndpointName";
    public static final String WSA_REFERENCEPROPERTIES_NAME = "ReferenceParameters";
    public static final String WSA_REFERENCEPARAMETERS_NAME = "ReferenceParameters";
    public static final String WSA_ADDRESS_NAME = "Address";
    public static final String WSA_ANONYMOUS_ADDRESS = "http://www.w3.org/2005/08/addressing/anonymous";
    public static final String WSA_NONE_ADDRESS = "http://www.w3.org/2005/08/addressing/none";
    public static final String WSA_DEFAULT_FAULT_ACTION = "http://www.w3.org/2005/08/addressing/fault";
    public static final String WSA_EPR_NAME = "EndpointReference";
    public static final String ACTION_NOT_SUPPORTED_TEXT = "The \"%s\" cannot be processed at the receiver";
    public static final String DESTINATION_UNREACHABLE_TEXT = "No route can be determined to reach %s";
    public static final String ENDPOINT_UNAVAILABLE_TEXT = "The endpoint is unable to process the message at this time";
    public static final String INVALID_MAP_TEXT = "A header representing a Message Addressing Property is not valid and the message cannot be processed";
    public static final String MAP_REQUIRED_TEXT = "A required header representing a Message Addressing Property is not present";
    public static final String ANONYMOUS_EPR = "<EndpointReference xmlns=\"http://www.w3.org/2005/08/addressing\">\n    <Address>http://www.w3.org/2005/08/addressing/anonymous</Address>\n</EndpointReference>";
    public static final String WSA_NAMESPACE_NAME = "http://www.w3.org/2005/08/addressing";
    public static final QName WSA_REFERENCEPROPERTIES_QNAME = new QName(WSA_NAMESPACE_NAME, "ReferenceParameters");
    public static final QName WSA_REFERENCEPARAMETERS_QNAME = new QName(WSA_NAMESPACE_NAME, "ReferenceParameters");
    public static final String WSA_METADATA_NAME = "Metadata";
    public static final QName WSA_METADATA_QNAME = new QName(WSA_NAMESPACE_NAME, WSA_METADATA_NAME);
    public static final QName WSA_ADDRESS_QNAME = new QName(WSA_NAMESPACE_NAME, "Address");
    public static final QName WSA_EPR_QNAME = new QName(WSA_NAMESPACE_NAME, "EndpointReference");
    public static final String WSA_NAMESPACE_WSDL_NAME = "http://www.w3.org/2006/05/addressing/wsdl";
    public static final String WSAW_USING_ADDRESSING_NAME = "UsingAddressing";
    public static final QName WSAW_USING_ADDRESSING_QNAME = new QName(WSA_NAMESPACE_WSDL_NAME, WSAW_USING_ADDRESSING_NAME);
    public static final QName INVALID_MAP_QNAME = new QName(WSA_NAMESPACE_NAME, "InvalidAddressingHeader");
    public static final QName MAP_REQUIRED_QNAME = new QName(WSA_NAMESPACE_NAME, "MessageAddressingHeaderRequired");
    public static final QName DESTINATION_UNREACHABLE_QNAME = new QName(WSA_NAMESPACE_NAME, "DestinationUnreachable");
    public static final QName ACTION_NOT_SUPPORTED_QNAME = new QName(WSA_NAMESPACE_NAME, "ActionNotSupported");
    public static final QName ENDPOINT_UNAVAILABLE_QNAME = new QName(WSA_NAMESPACE_NAME, "EndpointUnavailable");
    public static final QName PROBLEM_ACTION_QNAME = new QName(WSA_NAMESPACE_NAME, "ProblemAction");
    public static final QName PROBLEM_HEADER_QNAME_QNAME = new QName(WSA_NAMESPACE_NAME, "ProblemHeaderQName");
    public static final QName FAULT_DETAIL_QNAME = new QName(WSA_NAMESPACE_NAME, "FaultDetail");
    public static final QName INVALID_ADDRESS_SUBCODE = new QName(WSA_NAMESPACE_NAME, "InvalidAddress", AddressingVersion.W3C.getPrefix());
    public static final QName INVALID_EPR = new QName(WSA_NAMESPACE_NAME, "InvalidEPR", AddressingVersion.W3C.getPrefix());
    public static final QName INVALID_CARDINALITY = new QName(WSA_NAMESPACE_NAME, "InvalidCardinality", AddressingVersion.W3C.getPrefix());
    public static final QName MISSING_ADDRESS_IN_EPR = new QName(WSA_NAMESPACE_NAME, "MissingAddressInEPR", AddressingVersion.W3C.getPrefix());
    public static final QName DUPLICATE_MESSAGEID = new QName(WSA_NAMESPACE_NAME, "DuplicateMessageID", AddressingVersion.W3C.getPrefix());
    public static final QName ACTION_MISMATCH = new QName(WSA_NAMESPACE_NAME, "ActionMismatch", AddressingVersion.W3C.getPrefix());
    public static final QName ONLY_ANONYMOUS_ADDRESS_SUPPORTED = new QName(WSA_NAMESPACE_NAME, "OnlyAnonymousAddressSupported", AddressingVersion.W3C.getPrefix());
    public static final QName ONLY_NON_ANONYMOUS_ADDRESS_SUPPORTED = new QName(WSA_NAMESPACE_NAME, "OnlyNonAnonymousAddressSupported", AddressingVersion.W3C.getPrefix());
}
