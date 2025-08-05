package com.sun.xml.internal.ws.api.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
import com.sun.xml.internal.ws.addressing.WsaTubeHelperImpl;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import com.sun.xml.internal.ws.message.stream.OutboundStreamHeader;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/AddressingVersion.class */
public enum AddressingVersion {
    W3C(W3CAddressingConstants.WSA_NAMESPACE_NAME, "wsa", W3CAddressingConstants.ANONYMOUS_EPR, W3CAddressingConstants.WSA_NAMESPACE_WSDL_NAME, W3CAddressingConstants.WSA_NAMESPACE_WSDL_NAME, W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS, W3CAddressingConstants.WSA_NONE_ADDRESS, new EPR(W3CEndpointReference.class, "Address", "ServiceName", W3CAddressingConstants.WSAW_ENDPOINTNAME_NAME, W3CAddressingConstants.WSAW_INTERFACENAME_NAME, new QName(W3CAddressingConstants.WSA_NAMESPACE_NAME, W3CAddressingConstants.WSA_METADATA_NAME, "wsa"), "ReferenceParameters", null)) { // from class: com.sun.xml.internal.ws.api.addressing.AddressingVersion.1
        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getActionMismatchLocalName() {
            return "ActionMismatch";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public boolean isReferenceParameter(String localName) {
            return localName.equals("ReferenceParameters");
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public WsaTubeHelper getWsaHelper(WSDLPort wsdlPort, SEIModel seiModel, WSBinding binding) {
            return new WsaTubeHelperImpl(wsdlPort, seiModel, binding);
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getMapRequiredLocalName() {
            return "MessageAddressingHeaderRequired";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getMapRequiredText() {
            return W3CAddressingConstants.MAP_REQUIRED_TEXT;
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getInvalidAddressLocalName() {
            return "InvalidAddress";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getInvalidMapLocalName() {
            return "InvalidAddressingHeader";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getInvalidMapText() {
            return W3CAddressingConstants.INVALID_MAP_TEXT;
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getInvalidCardinalityLocalName() {
            return "InvalidCardinality";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        Header createReferenceParameterHeader(XMLStreamBuffer mark, String nsUri, String localName) {
            return new OutboundReferenceParameterHeader(mark, nsUri, localName);
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getIsReferenceParameterLocalName() {
            return "IsReferenceParameter";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getWsdlAnonymousLocalName() {
            return "Anonymous";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getPrefix() {
            return "wsa";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getWsdlPrefix() {
            return "wsaw";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public Class<? extends WebServiceFeature> getFeatureClass() {
            return AddressingFeature.class;
        }
    },
    MEMBER("http://schemas.xmlsoap.org/ws/2004/08/addressing", "wsa", MemberSubmissionAddressingConstants.ANONYMOUS_EPR, "http://schemas.xmlsoap.org/ws/2004/08/addressing", MemberSubmissionAddressingConstants.WSA_NAMESPACE_POLICY_NAME, MemberSubmissionAddressingConstants.WSA_ANONYMOUS_ADDRESS, "", new EPR(MemberSubmissionEndpointReference.class, "Address", "ServiceName", MemberSubmissionAddressingConstants.WSA_PORTNAME_NAME, MemberSubmissionAddressingConstants.WSA_PORTTYPE_NAME, MemberSubmissionAddressingConstants.MEX_METADATA, "ReferenceParameters", "ReferenceProperties")) { // from class: com.sun.xml.internal.ws.api.addressing.AddressingVersion.2
        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getActionMismatchLocalName() {
            return "InvalidMessageInformationHeader";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public boolean isReferenceParameter(String localName) {
            return localName.equals("ReferenceParameters") || localName.equals("ReferenceProperties");
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public WsaTubeHelper getWsaHelper(WSDLPort wsdlPort, SEIModel seiModel, WSBinding binding) {
            return new com.sun.xml.internal.ws.addressing.v200408.WsaTubeHelperImpl(wsdlPort, seiModel, binding);
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getMapRequiredLocalName() {
            return "MessageInformationHeaderRequired";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getMapRequiredText() {
            return MemberSubmissionAddressingConstants.MAP_REQUIRED_TEXT;
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getInvalidAddressLocalName() {
            return getInvalidMapLocalName();
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getInvalidMapLocalName() {
            return "InvalidMessageInformationHeader";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getInvalidMapText() {
            return MemberSubmissionAddressingConstants.INVALID_MAP_TEXT;
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getInvalidCardinalityLocalName() {
            return getInvalidMapLocalName();
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        Header createReferenceParameterHeader(XMLStreamBuffer mark, String nsUri, String localName) {
            return new OutboundStreamHeader(mark, nsUri, localName);
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getIsReferenceParameterLocalName() {
            return "";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        String getWsdlAnonymousLocalName() {
            return "";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getPrefix() {
            return "wsa";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public String getWsdlPrefix() {
            return "wsaw";
        }

        @Override // com.sun.xml.internal.ws.api.addressing.AddressingVersion
        public Class<? extends WebServiceFeature> getFeatureClass() {
            return MemberSubmissionAddressingFeature.class;
        }
    };

    public final String nsUri;
    public final String wsdlNsUri;
    public final EPR eprType;
    public final String policyNsUri;

    @NotNull
    public final String anonymousUri;

    @NotNull
    public final String noneUri;
    public final WSEndpointReference anonymousEpr;
    public final QName toTag;
    public final QName fromTag;
    public final QName replyToTag;
    public final QName faultToTag;
    public final QName actionTag;
    public final QName messageIDTag;
    public final QName relatesToTag;
    public final QName mapRequiredTag;
    public final QName actionMismatchTag;
    public final QName actionNotSupportedTag;
    public final String actionNotSupportedText;
    public final QName invalidMapTag;
    public final QName invalidCardinalityTag;
    public final QName invalidAddressTag;
    public final QName problemHeaderQNameTag;
    public final QName problemActionTag;
    public final QName faultDetailTag;
    public final QName fault_missingAddressInEpr;
    public final QName wsdlActionTag;
    public final QName wsdlExtensionTag;
    public final QName wsdlAnonymousTag;
    public final QName isReferenceParameterTag;
    public static final String UNSET_OUTPUT_ACTION = "http://jax-ws.dev.java.net/addressing/output-action-not-set";
    public static final String UNSET_INPUT_ACTION = "http://jax-ws.dev.java.net/addressing/input-action-not-set";
    private static final String EXTENDED_FAULT_NAMESPACE = "http://jax-ws.dev.java.net/addressing/fault";
    public static final QName fault_duplicateAddressInEpr = new QName(EXTENDED_FAULT_NAMESPACE, "DuplicateAddressInEpr", "wsa");

    abstract String getActionMismatchLocalName();

    public abstract boolean isReferenceParameter(String str);

    public abstract WsaTubeHelper getWsaHelper(WSDLPort wSDLPort, SEIModel sEIModel, WSBinding wSBinding);

    abstract String getMapRequiredLocalName();

    public abstract String getMapRequiredText();

    abstract String getInvalidAddressLocalName();

    abstract String getInvalidMapLocalName();

    public abstract String getInvalidMapText();

    abstract String getInvalidCardinalityLocalName();

    abstract String getWsdlAnonymousLocalName();

    public abstract String getPrefix();

    public abstract String getWsdlPrefix();

    public abstract Class<? extends WebServiceFeature> getFeatureClass();

    abstract Header createReferenceParameterHeader(XMLStreamBuffer xMLStreamBuffer, String str, String str2);

    abstract String getIsReferenceParameterLocalName();

    AddressingVersion(String nsUri, String prefix, String anonymousEprString, String wsdlNsUri, String policyNsUri, String anonymousUri, String noneUri, EPR eprType) {
        this.nsUri = nsUri;
        this.wsdlNsUri = wsdlNsUri;
        this.policyNsUri = policyNsUri;
        this.anonymousUri = anonymousUri;
        this.noneUri = noneUri;
        this.toTag = new QName(nsUri, "To", prefix);
        this.fromTag = new QName(nsUri, "From", prefix);
        this.replyToTag = new QName(nsUri, "ReplyTo", prefix);
        this.faultToTag = new QName(nsUri, "FaultTo", prefix);
        this.actionTag = new QName(nsUri, "Action", prefix);
        this.messageIDTag = new QName(nsUri, "MessageID", prefix);
        this.relatesToTag = new QName(nsUri, "RelatesTo", prefix);
        this.mapRequiredTag = new QName(nsUri, getMapRequiredLocalName(), prefix);
        this.actionMismatchTag = new QName(nsUri, getActionMismatchLocalName(), prefix);
        this.actionNotSupportedTag = new QName(nsUri, "ActionNotSupported", prefix);
        this.actionNotSupportedText = W3CAddressingConstants.ACTION_NOT_SUPPORTED_TEXT;
        this.invalidMapTag = new QName(nsUri, getInvalidMapLocalName(), prefix);
        this.invalidAddressTag = new QName(nsUri, getInvalidAddressLocalName(), prefix);
        this.invalidCardinalityTag = new QName(nsUri, getInvalidCardinalityLocalName(), prefix);
        this.faultDetailTag = new QName(nsUri, "FaultDetail", prefix);
        this.problemHeaderQNameTag = new QName(nsUri, "ProblemHeaderQName", prefix);
        this.problemActionTag = new QName(nsUri, "ProblemAction", prefix);
        this.fault_missingAddressInEpr = new QName(nsUri, "MissingAddressInEPR", prefix);
        this.isReferenceParameterTag = new QName(nsUri, getIsReferenceParameterLocalName(), prefix);
        this.wsdlActionTag = new QName(wsdlNsUri, "Action", prefix);
        this.wsdlExtensionTag = new QName(wsdlNsUri, W3CAddressingConstants.WSAW_USING_ADDRESSING_NAME, prefix);
        this.wsdlAnonymousTag = new QName(wsdlNsUri, getWsdlAnonymousLocalName(), prefix);
        try {
            this.anonymousEpr = new WSEndpointReference(new ByteArrayInputStream(anonymousEprString.getBytes("UTF-8")), this);
            this.eprType = eprType;
        } catch (UnsupportedEncodingException e2) {
            throw new Error(e2);
        } catch (XMLStreamException e3) {
            throw new Error(e3);
        }
    }

    public static AddressingVersion fromNsUri(String nsUri) {
        if (nsUri.equals(W3C.nsUri)) {
            return W3C;
        }
        if (nsUri.equals(MEMBER.nsUri)) {
            return MEMBER;
        }
        return null;
    }

    @Nullable
    public static AddressingVersion fromBinding(WSBinding binding) {
        if (binding.isFeatureEnabled(AddressingFeature.class)) {
            return W3C;
        }
        if (binding.isFeatureEnabled(MemberSubmissionAddressingFeature.class)) {
            return MEMBER;
        }
        return null;
    }

    public static AddressingVersion fromPort(WSDLPort port) {
        if (port == null) {
            return null;
        }
        WebServiceFeature wsf = port.getFeature(AddressingFeature.class);
        if (wsf == null) {
            wsf = port.getFeature(MemberSubmissionAddressingFeature.class);
        }
        if (wsf == null) {
            return null;
        }
        return fromFeature(wsf);
    }

    public String getNsUri() {
        return this.nsUri;
    }

    public final String getNoneUri() {
        return this.noneUri;
    }

    public final String getAnonymousUri() {
        return this.anonymousUri;
    }

    public String getDefaultFaultAction() {
        return this.nsUri + "/fault";
    }

    public static AddressingVersion fromFeature(WebServiceFeature af2) {
        if (af2.getID().equals(AddressingFeature.ID)) {
            return W3C;
        }
        if (af2.getID().equals(MemberSubmissionAddressingFeature.ID)) {
            return MEMBER;
        }
        return null;
    }

    @NotNull
    public static WebServiceFeature getFeature(String nsUri, boolean enabled, boolean required) {
        if (nsUri.equals(W3C.policyNsUri)) {
            return new AddressingFeature(enabled, required);
        }
        if (nsUri.equals(MEMBER.policyNsUri)) {
            return new MemberSubmissionAddressingFeature(enabled, required);
        }
        throw new WebServiceException("Unsupported namespace URI: " + nsUri);
    }

    @NotNull
    public static AddressingVersion fromSpecClass(Class<? extends EndpointReference> eprClass) {
        if (eprClass == W3CEndpointReference.class) {
            return W3C;
        }
        if (eprClass == MemberSubmissionEndpointReference.class) {
            return MEMBER;
        }
        throw new WebServiceException("Unsupported EPR type: " + ((Object) eprClass));
    }

    public static boolean isRequired(WebServiceFeature wsf) {
        if (wsf.getID().equals(AddressingFeature.ID)) {
            return ((AddressingFeature) wsf).isRequired();
        }
        if (wsf.getID().equals(MemberSubmissionAddressingFeature.ID)) {
            return ((MemberSubmissionAddressingFeature) wsf).isRequired();
        }
        throw new WebServiceException("WebServiceFeature not an Addressing feature: " + wsf.getID());
    }

    public static boolean isRequired(WSBinding binding) {
        AddressingFeature af2 = (AddressingFeature) binding.getFeature(AddressingFeature.class);
        if (af2 != null) {
            return af2.isRequired();
        }
        MemberSubmissionAddressingFeature msaf = (MemberSubmissionAddressingFeature) binding.getFeature(MemberSubmissionAddressingFeature.class);
        if (msaf != null) {
            return msaf.isRequired();
        }
        return false;
    }

    public static boolean isEnabled(WSBinding binding) {
        return binding.isFeatureEnabled(MemberSubmissionAddressingFeature.class) || binding.isFeatureEnabled(AddressingFeature.class);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/AddressingVersion$EPR.class */
    public static final class EPR {
        public final Class<? extends EndpointReference> eprClass;
        public final String address;
        public final String serviceName;
        public final String portName;
        public final String portTypeName;
        public final String referenceParameters;
        public final QName wsdlMetadata;
        public final String referenceProperties;

        public EPR(Class<? extends EndpointReference> eprClass, String address, String serviceName, String portName, String portTypeName, QName wsdlMetadata, String referenceParameters, String referenceProperties) {
            this.eprClass = eprClass;
            this.address = address;
            this.serviceName = serviceName;
            this.portName = portName;
            this.portTypeName = portTypeName;
            this.referenceParameters = referenceParameters;
            this.referenceProperties = referenceProperties;
            this.wsdlMetadata = wsdlMetadata;
        }
    }
}
