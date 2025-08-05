package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.ws.addressing.policy.AddressingPolicyMapConfigurator;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.model.CheckedException;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
import com.sun.xml.internal.ws.api.policy.ModelGenerator;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
import com.sun.xml.internal.ws.encoding.policy.MtomPolicyMapConfigurator;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapExtender;
import com.sun.xml.internal.ws.policy.PolicyMapUtil;
import com.sun.xml.internal.ws.policy.PolicyMerger;
import com.sun.xml.internal.ws.policy.PolicySubject;
import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyMapConfigurator;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelMarshaller;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
import com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject;
import com.sun.xml.internal.ws.resources.PolicyMessages;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/PolicyWSDLGeneratorExtension.class */
public class PolicyWSDLGeneratorExtension extends WSDLGeneratorExtension {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyWSDLGeneratorExtension.class);
    private PolicyMap policyMap;
    private SEIModel seiModel;
    private final Collection<PolicySubject> subjects = new LinkedList();
    private final PolicyModelMarshaller marshaller = PolicyModelMarshaller.getXmlMarshaller(true);
    private final PolicyMerger merger = PolicyMerger.getMerger();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/PolicyWSDLGeneratorExtension$ScopeType.class */
    enum ScopeType {
        SERVICE,
        ENDPOINT,
        OPERATION,
        INPUT_MESSAGE,
        OUTPUT_MESSAGE,
        FAULT_MESSAGE
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void start(WSDLGenExtnContext context) {
        LOGGER.entering();
        try {
            this.seiModel = context.getModel();
            PolicyMapConfigurator[] policyMapConfigurators = loadConfigurators();
            PolicyMapExtender[] extenders = new PolicyMapExtender[policyMapConfigurators.length];
            for (int i2 = 0; i2 < policyMapConfigurators.length; i2++) {
                extenders[i2] = PolicyMapExtender.createPolicyMapExtender();
            }
            this.policyMap = PolicyResolverFactory.create().resolve(new PolicyResolver.ServerContext(this.policyMap, context.getContainer(), context.getEndpointClass(), false, extenders));
            if (this.policyMap == null) {
                LOGGER.fine(PolicyMessages.WSP_1019_CREATE_EMPTY_POLICY_MAP());
                this.policyMap = PolicyMap.createPolicyMap(Arrays.asList(extenders));
            }
            WSBinding binding = context.getBinding();
            try {
                Collection<PolicySubject> policySubjects = new LinkedList<>();
                for (int i3 = 0; i3 < policyMapConfigurators.length; i3++) {
                    policySubjects.addAll(policyMapConfigurators[i3].update(this.policyMap, this.seiModel, binding));
                    extenders[i3].disconnect();
                }
                PolicyMapUtil.insertPolicies(this.policyMap, policySubjects, this.seiModel.getServiceQName(), this.seiModel.getPortName());
                TypedXmlWriter root = context.getRoot();
                root._namespace(NamespaceVersion.v1_2.toString(), NamespaceVersion.v1_2.getDefaultNamespacePrefix());
                root._namespace(NamespaceVersion.v1_5.toString(), NamespaceVersion.v1_5.getDefaultNamespacePrefix());
                root._namespace(PolicyConstants.WSU_NAMESPACE_URI, PolicyConstants.WSU_NAMESPACE_PREFIX);
                LOGGER.exiting();
            } catch (PolicyException e2) {
                throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1017_MAP_UPDATE_FAILED(), e2)));
            }
        } catch (Throwable th) {
            LOGGER.exiting();
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addDefinitionsExtension(TypedXmlWriter definitions) {
        try {
            LOGGER.entering();
            if (this.policyMap == null) {
                LOGGER.fine(PolicyMessages.WSP_1009_NOT_MARSHALLING_ANY_POLICIES_POLICY_MAP_IS_NULL());
            } else {
                this.subjects.addAll(this.policyMap.getPolicySubjects());
                PolicyModelGenerator generator = ModelGenerator.getGenerator();
                Set<String> policyIDsOrNamesWritten = new HashSet<>();
                for (PolicySubject subject : this.subjects) {
                    if (subject.getSubject() == null) {
                        LOGGER.fine(PolicyMessages.WSP_1008_NOT_MARSHALLING_WSDL_SUBJ_NULL(subject));
                    } else {
                        try {
                            Policy policy = subject.getEffectivePolicy(this.merger);
                            if (null == policy.getIdOrName() || policyIDsOrNamesWritten.contains(policy.getIdOrName())) {
                                LOGGER.fine(PolicyMessages.WSP_1016_POLICY_ID_NULL_OR_DUPLICATE(policy));
                            } else {
                                try {
                                    PolicySourceModel policyInfoset = generator.translate(policy);
                                    this.marshaller.marshal(policyInfoset, definitions);
                                    policyIDsOrNamesWritten.add(policy.getIdOrName());
                                } catch (PolicyException e2) {
                                    throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1018_FAILED_TO_MARSHALL_POLICY(policy.getIdOrName()), e2)));
                                }
                            }
                        } catch (PolicyException e3) {
                            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1011_FAILED_TO_RETRIEVE_EFFECTIVE_POLICY_FOR_SUBJECT(subject.toString()), e3)));
                        }
                    }
                }
            }
            LOGGER.exiting();
        } catch (Throwable th) {
            LOGGER.exiting();
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addServiceExtension(TypedXmlWriter service) throws SecurityException {
        LOGGER.entering();
        String serviceName = null == this.seiModel ? null : this.seiModel.getServiceQName().getLocalPart();
        selectAndProcessSubject(service, WSDLService.class, ScopeType.SERVICE, serviceName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addPortExtension(TypedXmlWriter port) throws SecurityException {
        LOGGER.entering();
        String portName = null == this.seiModel ? null : this.seiModel.getPortName().getLocalPart();
        selectAndProcessSubject(port, WSDLPort.class, ScopeType.ENDPOINT, portName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addPortTypeExtension(TypedXmlWriter portType) throws SecurityException {
        LOGGER.entering();
        String portTypeName = null == this.seiModel ? null : this.seiModel.getPortTypeName().getLocalPart();
        selectAndProcessSubject(portType, WSDLPortType.class, ScopeType.ENDPOINT, portTypeName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingExtension(TypedXmlWriter binding) throws SecurityException {
        LOGGER.entering();
        QName bindingName = null == this.seiModel ? null : this.seiModel.getBoundPortTypeName();
        selectAndProcessBindingSubject(binding, WSDLBoundPortType.class, ScopeType.ENDPOINT, bindingName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationExtension(TypedXmlWriter operation, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        selectAndProcessSubject(operation, WSDLOperation.class, ScopeType.OPERATION, (String) null);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationExtension(TypedXmlWriter operation, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        QName operationName = method == null ? null : new QName(method.getOwner().getTargetNamespace(), method.getOperationName());
        selectAndProcessBindingSubject(operation, WSDLBoundOperation.class, ScopeType.OPERATION, operationName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addInputMessageExtension(TypedXmlWriter message, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        String messageName = null == method ? null : method.getRequestMessageName();
        selectAndProcessSubject(message, WSDLMessage.class, ScopeType.INPUT_MESSAGE, messageName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOutputMessageExtension(TypedXmlWriter message, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        String messageName = null == method ? null : method.getResponseMessageName();
        selectAndProcessSubject(message, WSDLMessage.class, ScopeType.OUTPUT_MESSAGE, messageName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addFaultMessageExtension(TypedXmlWriter message, JavaMethod method, CheckedException exception) throws SecurityException {
        LOGGER.entering();
        String messageName = null == exception ? null : exception.getMessageName();
        selectAndProcessSubject(message, WSDLMessage.class, ScopeType.FAULT_MESSAGE, messageName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        String messageName = null == method ? null : method.getRequestMessageName();
        selectAndProcessSubject(input, WSDLInput.class, ScopeType.INPUT_MESSAGE, messageName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        String messageName = null == method ? null : method.getResponseMessageName();
        selectAndProcessSubject(output, WSDLOutput.class, ScopeType.OUTPUT_MESSAGE, messageName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException exception) throws SecurityException {
        LOGGER.entering();
        String messageName = null == exception ? null : exception.getMessageName();
        selectAndProcessSubject(fault, WSDLFault.class, ScopeType.FAULT_MESSAGE, messageName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationInputExtension(TypedXmlWriter input, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        QName operationName = new QName(method.getOwner().getTargetNamespace(), method.getOperationName());
        selectAndProcessBindingSubject(input, WSDLBoundOperation.class, ScopeType.INPUT_MESSAGE, operationName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationOutputExtension(TypedXmlWriter output, JavaMethod method) throws SecurityException {
        LOGGER.entering();
        QName operationName = new QName(method.getOwner().getTargetNamespace(), method.getOperationName());
        selectAndProcessBindingSubject(output, WSDLBoundOperation.class, ScopeType.OUTPUT_MESSAGE, operationName);
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension
    public void addBindingOperationFaultExtension(TypedXmlWriter writer, JavaMethod method, CheckedException exception) {
        Object concreteSubject;
        LOGGER.entering(writer, method, exception);
        if (this.subjects != null) {
            for (PolicySubject subject : this.subjects) {
                if (this.policyMap.isFaultMessageSubject(subject) && (concreteSubject = subject.getSubject()) != null) {
                    String exceptionName = exception == null ? null : exception.getMessageName();
                    if (exceptionName == null) {
                        writePolicyOrReferenceIt(subject, writer);
                    }
                    if (WSDLBoundFaultContainer.class.isInstance(concreteSubject)) {
                        WSDLBoundFaultContainer faultContainer = (WSDLBoundFaultContainer) concreteSubject;
                        WSDLBoundFault fault = faultContainer.getBoundFault();
                        WSDLBoundOperation operation = faultContainer.getBoundOperation();
                        if (exceptionName.equals(fault.getName()) && operation.getName().getLocalPart().equals(method.getOperationName())) {
                            writePolicyOrReferenceIt(subject, writer);
                        }
                    } else if (WsdlBindingSubject.class.isInstance(concreteSubject)) {
                        WsdlBindingSubject wsdlSubject = (WsdlBindingSubject) concreteSubject;
                        if (wsdlSubject.getMessageType() == WsdlBindingSubject.WsdlMessageType.FAULT && exception.getOwner().getTargetNamespace().equals(wsdlSubject.getName().getNamespaceURI()) && exceptionName.equals(wsdlSubject.getName().getLocalPart())) {
                            writePolicyOrReferenceIt(subject, writer);
                        }
                    }
                }
            }
        }
        LOGGER.exiting();
    }

    private void selectAndProcessSubject(TypedXmlWriter xmlWriter, Class clazz, ScopeType scopeType, QName bindingName) throws SecurityException {
        LOGGER.entering(xmlWriter, clazz, scopeType, bindingName);
        if (bindingName == null) {
            selectAndProcessSubject(xmlWriter, clazz, scopeType, (String) null);
        } else {
            if (this.subjects != null) {
                for (PolicySubject subject : this.subjects) {
                    if (bindingName.equals(subject.getSubject())) {
                        writePolicyOrReferenceIt(subject, xmlWriter);
                    }
                }
            }
            selectAndProcessSubject(xmlWriter, clazz, scopeType, bindingName.getLocalPart());
        }
        LOGGER.exiting();
    }

    private void selectAndProcessBindingSubject(TypedXmlWriter xmlWriter, Class clazz, ScopeType scopeType, QName bindingName) throws SecurityException {
        LOGGER.entering(xmlWriter, clazz, scopeType, bindingName);
        if (this.subjects != null && bindingName != null) {
            for (PolicySubject subject : this.subjects) {
                if (subject.getSubject() instanceof WsdlBindingSubject) {
                    WsdlBindingSubject wsdlSubject = (WsdlBindingSubject) subject.getSubject();
                    if (bindingName.equals(wsdlSubject.getName())) {
                        writePolicyOrReferenceIt(subject, xmlWriter);
                    }
                }
            }
        }
        selectAndProcessSubject(xmlWriter, clazz, scopeType, bindingName);
        LOGGER.exiting();
    }

    private void selectAndProcessSubject(TypedXmlWriter xmlWriter, Class clazz, ScopeType scopeType, String wsdlName) throws SecurityException {
        Object concreteSubject;
        LOGGER.entering(xmlWriter, clazz, scopeType, wsdlName);
        if (this.subjects != null) {
            for (PolicySubject subject : this.subjects) {
                if (isCorrectType(this.policyMap, subject, scopeType) && (concreteSubject = subject.getSubject()) != null && clazz.isInstance(concreteSubject)) {
                    if (null == wsdlName) {
                        writePolicyOrReferenceIt(subject, xmlWriter);
                    } else {
                        try {
                            Method getNameMethod = clazz.getDeclaredMethod("getName", new Class[0]);
                            if (stringEqualsToStringOrQName(wsdlName, getNameMethod.invoke(concreteSubject, new Object[0]))) {
                                writePolicyOrReferenceIt(subject, xmlWriter);
                            }
                        } catch (IllegalAccessException e2) {
                            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1003_UNABLE_TO_CHECK_ELEMENT_NAME(clazz.getName(), wsdlName), e2)));
                        } catch (NoSuchMethodException e3) {
                            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1003_UNABLE_TO_CHECK_ELEMENT_NAME(clazz.getName(), wsdlName), e3)));
                        } catch (InvocationTargetException e4) {
                            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1003_UNABLE_TO_CHECK_ELEMENT_NAME(clazz.getName(), wsdlName), e4)));
                        }
                    }
                }
            }
        }
        LOGGER.exiting();
    }

    private static boolean isCorrectType(PolicyMap map, PolicySubject subject, ScopeType type) {
        switch (type) {
            case OPERATION:
                return (map.isInputMessageSubject(subject) || map.isOutputMessageSubject(subject) || map.isFaultMessageSubject(subject)) ? false : true;
            case INPUT_MESSAGE:
                return map.isInputMessageSubject(subject);
            case OUTPUT_MESSAGE:
                return map.isOutputMessageSubject(subject);
            case FAULT_MESSAGE:
                return map.isFaultMessageSubject(subject);
            default:
                return true;
        }
    }

    private boolean stringEqualsToStringOrQName(String first, Object second) {
        return second instanceof QName ? first.equals(((QName) second).getLocalPart()) : first.equals(second);
    }

    private void writePolicyOrReferenceIt(PolicySubject subject, TypedXmlWriter writer) {
        try {
            Policy policy = subject.getEffectivePolicy(this.merger);
            if (policy != null) {
                if (null == policy.getIdOrName()) {
                    PolicyModelGenerator generator = ModelGenerator.getGenerator();
                    try {
                        PolicySourceModel policyInfoset = generator.translate(policy);
                        this.marshaller.marshal(policyInfoset, writer);
                        return;
                    } catch (PolicyException pe) {
                        throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1002_UNABLE_TO_MARSHALL_POLICY_OR_POLICY_REFERENCE(), pe)));
                    }
                }
                TypedXmlWriter policyReference = writer._element(policy.getNamespaceVersion().asQName(XmlToken.PolicyReference), (Class<TypedXmlWriter>) TypedXmlWriter.class);
                policyReference._attribute(XmlToken.Uri.toString(), '#' + policy.getIdOrName());
            }
        } catch (PolicyException e2) {
            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1011_FAILED_TO_RETRIEVE_EFFECTIVE_POLICY_FOR_SUBJECT(subject.toString()), e2)));
        }
    }

    private PolicyMapConfigurator[] loadConfigurators() {
        Collection<PolicyMapConfigurator> configurators = new LinkedList<>();
        configurators.add(new AddressingPolicyMapConfigurator());
        configurators.add(new MtomPolicyMapConfigurator());
        PolicyUtil.addServiceProviders(configurators, PolicyMapConfigurator.class);
        return (PolicyMapConfigurator[]) configurators.toArray(new PolicyMapConfigurator[configurators.size()]);
    }
}
