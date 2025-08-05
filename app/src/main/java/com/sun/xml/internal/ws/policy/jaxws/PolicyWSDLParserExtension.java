package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLObject;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapMutator;
import com.sun.xml.internal.ws.policy.jaxws.BuilderHandlerMessageScope;
import com.sun.xml.internal.ws.policy.jaxws.SafePolicyReader;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModelContext;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
import com.sun.xml.internal.ws.resources.PolicyMessages;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/PolicyWSDLParserExtension.class */
public final class PolicyWSDLParserExtension extends WSDLParserExtension {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyWSDLParserExtension.class);
    private static final StringBuffer AnonymnousPolicyIdPrefix = new StringBuffer("#__anonymousPolicy__ID");
    private int anonymousPoliciesCount;
    private final SafePolicyReader policyReader = new SafePolicyReader();
    private SafePolicyReader.PolicyRecord expandQueueHead = null;
    private Map<String, SafePolicyReader.PolicyRecord> policyRecordsPassedBy = null;
    private Map<String, PolicySourceModel> anonymousPolicyModels = null;
    private List<String> unresolvedUris = null;
    private final LinkedList<String> urisNeeded = new LinkedList<>();
    private final Map<String, PolicySourceModel> modelsNeeded = new HashMap();
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4ServiceMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4PortMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4PortTypeMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BoundOperationMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4OperationMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4MessageMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4InputMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4OutputMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4FaultMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingInputOpMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingOutputOpMap = null;
    private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingFaultOpMap = null;
    private PolicyMapBuilder policyBuilder = new PolicyMapBuilder();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/PolicyWSDLParserExtension$HandlerType.class */
    enum HandlerType {
        PolicyUri,
        AnonymousPolicyId
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/PolicyWSDLParserExtension$PolicyRecordHandler.class */
    static final class PolicyRecordHandler {
        String handler;
        HandlerType type;

        PolicyRecordHandler(HandlerType type, String handler) {
            this.type = type;
            this.handler = handler;
        }

        HandlerType getType() {
            return this.type;
        }

        String getHandler() {
            return this.handler;
        }
    }

    private boolean isPolicyProcessed(String policyUri) {
        return this.modelsNeeded.containsKey(policyUri);
    }

    private void addNewPolicyNeeded(String policyUri, PolicySourceModel policyModel) {
        if (!this.modelsNeeded.containsKey(policyUri)) {
            this.modelsNeeded.put(policyUri, policyModel);
            this.urisNeeded.addFirst(policyUri);
        }
    }

    private Map<String, PolicySourceModel> getPolicyModels() {
        return this.modelsNeeded;
    }

    private Map<String, SafePolicyReader.PolicyRecord> getPolicyRecordsPassedBy() {
        if (null == this.policyRecordsPassedBy) {
            this.policyRecordsPassedBy = new HashMap();
        }
        return this.policyRecordsPassedBy;
    }

    private Map<String, PolicySourceModel> getAnonymousPolicyModels() {
        if (null == this.anonymousPolicyModels) {
            this.anonymousPolicyModels = new HashMap();
        }
        return this.anonymousPolicyModels;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4ServiceMap() {
        if (null == this.handlers4ServiceMap) {
            this.handlers4ServiceMap = new HashMap();
        }
        return this.handlers4ServiceMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4PortMap() {
        if (null == this.handlers4PortMap) {
            this.handlers4PortMap = new HashMap();
        }
        return this.handlers4PortMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4PortTypeMap() {
        if (null == this.handlers4PortTypeMap) {
            this.handlers4PortTypeMap = new HashMap();
        }
        return this.handlers4PortTypeMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingMap() {
        if (null == this.handlers4BindingMap) {
            this.handlers4BindingMap = new HashMap();
        }
        return this.handlers4BindingMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4OperationMap() {
        if (null == this.handlers4OperationMap) {
            this.handlers4OperationMap = new HashMap();
        }
        return this.handlers4OperationMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BoundOperationMap() {
        if (null == this.handlers4BoundOperationMap) {
            this.handlers4BoundOperationMap = new HashMap();
        }
        return this.handlers4BoundOperationMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4MessageMap() {
        if (null == this.handlers4MessageMap) {
            this.handlers4MessageMap = new HashMap();
        }
        return this.handlers4MessageMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4InputMap() {
        if (null == this.handlers4InputMap) {
            this.handlers4InputMap = new HashMap();
        }
        return this.handlers4InputMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4OutputMap() {
        if (null == this.handlers4OutputMap) {
            this.handlers4OutputMap = new HashMap();
        }
        return this.handlers4OutputMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4FaultMap() {
        if (null == this.handlers4FaultMap) {
            this.handlers4FaultMap = new HashMap();
        }
        return this.handlers4FaultMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingInputOpMap() {
        if (null == this.handlers4BindingInputOpMap) {
            this.handlers4BindingInputOpMap = new HashMap();
        }
        return this.handlers4BindingInputOpMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingOutputOpMap() {
        if (null == this.handlers4BindingOutputOpMap) {
            this.handlers4BindingOutputOpMap = new HashMap();
        }
        return this.handlers4BindingOutputOpMap;
    }

    private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingFaultOpMap() {
        if (null == this.handlers4BindingFaultOpMap) {
            this.handlers4BindingFaultOpMap = new HashMap();
        }
        return this.handlers4BindingFaultOpMap;
    }

    private List<String> getUnresolvedUris(boolean emptyListNeeded) {
        if (null == this.unresolvedUris || emptyListNeeded) {
            this.unresolvedUris = new LinkedList();
        }
        return this.unresolvedUris;
    }

    private void policyRecToExpandQueue(SafePolicyReader.PolicyRecord policyRec) {
        if (null == this.expandQueueHead) {
            this.expandQueueHead = policyRec;
        } else {
            this.expandQueueHead = this.expandQueueHead.insert(policyRec);
        }
    }

    private PolicyRecordHandler readSinglePolicy(SafePolicyReader.PolicyRecord policyRec, boolean inner) {
        PolicyRecordHandler handler = null;
        String policyId = policyRec.policyModel.getPolicyId();
        if (policyId == null) {
            policyId = policyRec.policyModel.getPolicyName();
        }
        if (policyId != null) {
            handler = new PolicyRecordHandler(HandlerType.PolicyUri, policyRec.getUri());
            getPolicyRecordsPassedBy().put(policyRec.getUri(), policyRec);
            policyRecToExpandQueue(policyRec);
        } else if (inner) {
            StringBuffer stringBuffer = AnonymnousPolicyIdPrefix;
            int i2 = this.anonymousPoliciesCount;
            this.anonymousPoliciesCount = i2 + 1;
            String anonymousId = stringBuffer.append(i2).toString();
            handler = new PolicyRecordHandler(HandlerType.AnonymousPolicyId, anonymousId);
            getAnonymousPolicyModels().put(anonymousId, policyRec.policyModel);
            if (null != policyRec.unresolvedURIs) {
                getUnresolvedUris(false).addAll(policyRec.unresolvedURIs);
            }
        }
        return handler;
    }

    private void addHandlerToMap(Map<WSDLObject, Collection<PolicyRecordHandler>> map, WSDLObject key, PolicyRecordHandler handler) {
        if (map.containsKey(key)) {
            map.get(key).add(handler);
            return;
        }
        Collection<PolicyRecordHandler> newSet = new LinkedList<>();
        newSet.add(handler);
        map.put(key, newSet);
    }

    private String getBaseUrl(String policyUri) {
        if (null == policyUri) {
            return null;
        }
        int fragmentIdx = policyUri.indexOf(35);
        return fragmentIdx == -1 ? policyUri : policyUri.substring(0, fragmentIdx);
    }

    private void processReferenceUri(String policyUri, WSDLObject element, XMLStreamReader reader, Map<WSDLObject, Collection<PolicyRecordHandler>> map) {
        if (null == policyUri || policyUri.length() == 0) {
            return;
        }
        if ('#' != policyUri.charAt(0)) {
            getUnresolvedUris(false).add(policyUri);
        }
        addHandlerToMap(map, element, new PolicyRecordHandler(HandlerType.PolicyUri, SafePolicyReader.relativeToAbsoluteUrl(policyUri, reader.getLocation().getSystemId())));
    }

    private boolean processSubelement(WSDLObject element, XMLStreamReader reader, Map<WSDLObject, Collection<PolicyRecordHandler>> map) {
        if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.PolicyReference) {
            processReferenceUri(this.policyReader.readPolicyReferenceElement(reader), element, reader, map);
            return true;
        }
        if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.Policy) {
            PolicyRecordHandler handler = readSinglePolicy(this.policyReader.readPolicyElement(reader, null == reader.getLocation().getSystemId() ? "" : reader.getLocation().getSystemId()), true);
            if (null != handler) {
                addHandlerToMap(map, element, handler);
                return true;
            }
            return true;
        }
        return false;
    }

    private void processAttributes(WSDLObject element, XMLStreamReader reader, Map<WSDLObject, Collection<PolicyRecordHandler>> map) {
        String[] uriArray = getPolicyURIsFromAttr(reader);
        if (null != uriArray) {
            for (String policyUri : uriArray) {
                processReferenceUri(policyUri, element, reader, map);
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portElements(EditableWSDLPort port, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(port, reader, getHandlers4PortMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portAttributes(EditableWSDLPort port, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(port, reader, getHandlers4PortMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean serviceElements(EditableWSDLService service, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(service, reader, getHandlers4ServiceMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void serviceAttributes(EditableWSDLService service, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(service, reader, getHandlers4ServiceMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean definitionsElements(XMLStreamReader reader) {
        LOGGER.entering();
        if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.Policy) {
            readSinglePolicy(this.policyReader.readPolicyElement(reader, null == reader.getLocation().getSystemId() ? "" : reader.getLocation().getSystemId()), false);
            LOGGER.exiting();
            return true;
        }
        LOGGER.exiting();
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingElements(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(binding, reader, getHandlers4BindingMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingAttributes(EditableWSDLBoundPortType binding, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(binding, reader, getHandlers4BindingMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeElements(EditableWSDLPortType portType, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(portType, reader, getHandlers4PortTypeMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeAttributes(EditableWSDLPortType portType, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(portType, reader, getHandlers4PortTypeMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationElements(EditableWSDLOperation operation, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(operation, reader, getHandlers4OperationMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationAttributes(EditableWSDLOperation operation, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(operation, reader, getHandlers4OperationMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationElements(EditableWSDLBoundOperation boundOperation, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(boundOperation, reader, getHandlers4BoundOperationMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationAttributes(EditableWSDLBoundOperation boundOperation, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(boundOperation, reader, getHandlers4BoundOperationMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean messageElements(EditableWSDLMessage msg, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(msg, reader, getHandlers4MessageMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void messageAttributes(EditableWSDLMessage msg, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(msg, reader, getHandlers4MessageMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationInputElements(EditableWSDLInput input, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(input, reader, getHandlers4InputMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationInputAttributes(EditableWSDLInput input, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(input, reader, getHandlers4InputMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationOutputElements(EditableWSDLOutput output, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(output, reader, getHandlers4OutputMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationOutputAttributes(EditableWSDLOutput output, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(output, reader, getHandlers4OutputMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean portTypeOperationFaultElements(EditableWSDLFault fault, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(fault, reader, getHandlers4FaultMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void portTypeOperationFaultAttributes(EditableWSDLFault fault, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(fault, reader, getHandlers4FaultMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationInputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(operation, reader, getHandlers4BindingInputOpMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationInputAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(operation, reader, getHandlers4BindingInputOpMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationOutputElements(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(operation, reader, getHandlers4BindingOutputOpMap());
        LOGGER.exiting();
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationOutputAttributes(EditableWSDLBoundOperation operation, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(operation, reader, getHandlers4BindingOutputOpMap());
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public boolean bindingOperationFaultElements(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        LOGGER.entering();
        boolean result = processSubelement(fault, reader, getHandlers4BindingFaultOpMap());
        LOGGER.exiting(Boolean.valueOf(result));
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void bindingOperationFaultAttributes(EditableWSDLBoundFault fault, XMLStreamReader reader) {
        LOGGER.entering();
        processAttributes(fault, reader, getHandlers4BindingFaultOpMap());
        LOGGER.exiting();
    }

    private PolicyMapBuilder getPolicyMapBuilder() {
        if (null == this.policyBuilder) {
            this.policyBuilder = new PolicyMapBuilder();
        }
        return this.policyBuilder;
    }

    private Collection<String> getPolicyURIs(Collection<PolicyRecordHandler> handlers, PolicySourceModelContext modelContext) throws PolicyException {
        Collection<String> result = new ArrayList<>(handlers.size());
        for (PolicyRecordHandler handler : handlers) {
            String policyUri = handler.handler;
            if (HandlerType.AnonymousPolicyId == handler.type) {
                PolicySourceModel policyModel = getAnonymousPolicyModels().get(policyUri);
                policyModel.expand(modelContext);
                while (getPolicyModels().containsKey(policyUri)) {
                    StringBuffer stringBuffer = AnonymnousPolicyIdPrefix;
                    int i2 = this.anonymousPoliciesCount;
                    this.anonymousPoliciesCount = i2 + 1;
                    policyUri = stringBuffer.append(i2).toString();
                }
                getPolicyModels().put(policyUri, policyModel);
            }
            result.add(policyUri);
        }
        return result;
    }

    private boolean readExternalFile(String fileUrl) {
        InputStream ios = null;
        XMLStreamReader reader = null;
        try {
            URL xmlURL = new URL(fileUrl);
            ios = xmlURL.openStream();
            reader = XmlUtil.newXMLInputFactory(true).createXMLStreamReader(ios);
            while (reader.hasNext()) {
                if (reader.isStartElement() && NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.Policy) {
                    readSinglePolicy(this.policyReader.readPolicyElement(reader, fileUrl), false);
                }
                reader.next();
            }
            PolicyUtils.IO.closeResource(reader);
            PolicyUtils.IO.closeResource(ios);
            return true;
        } catch (IOException e2) {
            PolicyUtils.IO.closeResource(reader);
            PolicyUtils.IO.closeResource(ios);
            return false;
        } catch (XMLStreamException e3) {
            PolicyUtils.IO.closeResource(reader);
            PolicyUtils.IO.closeResource(ios);
            return false;
        } catch (Throwable th) {
            PolicyUtils.IO.closeResource(reader);
            PolicyUtils.IO.closeResource(ios);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void finished(WSDLParserExtensionContext context) {
        EditableWSDLMessage outputMsg;
        EditableWSDLMessage inputMsg;
        LOGGER.entering(context);
        if (null != this.expandQueueHead) {
            List<String> externalUris = getUnresolvedUris(false);
            getUnresolvedUris(true);
            LinkedList<String> baseUnresolvedUris = new LinkedList<>();
            SafePolicyReader.PolicyRecord policyRecord = this.expandQueueHead;
            while (true) {
                SafePolicyReader.PolicyRecord currentRec = policyRecord;
                if (null == currentRec) {
                    break;
                }
                baseUnresolvedUris.addFirst(currentRec.getUri());
                policyRecord = currentRec.next;
            }
            getUnresolvedUris(false).addAll(baseUnresolvedUris);
            this.expandQueueHead = null;
            getUnresolvedUris(false).addAll(externalUris);
        }
        while (!getUnresolvedUris(false).isEmpty()) {
            List<String> urisToBeSolvedList = getUnresolvedUris(false);
            getUnresolvedUris(true);
            for (String currentUri : urisToBeSolvedList) {
                if (!isPolicyProcessed(currentUri)) {
                    SafePolicyReader.PolicyRecord prefetchedRecord = getPolicyRecordsPassedBy().get(currentUri);
                    if (null == prefetchedRecord) {
                        if (this.policyReader.getUrlsRead().contains(getBaseUrl(currentUri))) {
                            LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1014_CAN_NOT_FIND_POLICY(currentUri)));
                        } else if (readExternalFile(getBaseUrl(currentUri))) {
                            getUnresolvedUris(false).add(currentUri);
                        }
                    } else {
                        if (null != prefetchedRecord.unresolvedURIs) {
                            getUnresolvedUris(false).addAll(prefetchedRecord.unresolvedURIs);
                        }
                        addNewPolicyNeeded(currentUri, prefetchedRecord.policyModel);
                    }
                }
            }
        }
        PolicySourceModelContext modelContext = PolicySourceModelContext.createContext();
        Iterator<String> it = this.urisNeeded.iterator();
        while (it.hasNext()) {
            String policyUri = it.next();
            PolicySourceModel sourceModel = this.modelsNeeded.get(policyUri);
            try {
                sourceModel.expand(modelContext);
                modelContext.addModel(new URI(policyUri), sourceModel);
            } catch (PolicyException e2) {
                LOGGER.logSevereException(e2);
            } catch (URISyntaxException e3) {
                LOGGER.logSevereException(e3);
            }
        }
        try {
            HashSet<BuilderHandlerMessageScope> messageSet = new HashSet<>();
            for (EditableWSDLService service : context.getWSDLModel().getServices().values()) {
                if (getHandlers4ServiceMap().containsKey(service)) {
                    getPolicyMapBuilder().registerHandler(new BuilderHandlerServiceScope(getPolicyURIs(getHandlers4ServiceMap().get(service), modelContext), getPolicyModels(), service, service.getName()));
                }
                for (EditableWSDLPort port : service.getPorts()) {
                    if (getHandlers4PortMap().containsKey(port)) {
                        getPolicyMapBuilder().registerHandler(new BuilderHandlerEndpointScope(getPolicyURIs(getHandlers4PortMap().get(port), modelContext), getPolicyModels(), port, port.getOwner().getName(), port.getName()));
                    }
                    if (null != port.getBinding()) {
                        if (getHandlers4BindingMap().containsKey(port.getBinding())) {
                            getPolicyMapBuilder().registerHandler(new BuilderHandlerEndpointScope(getPolicyURIs(getHandlers4BindingMap().get(port.getBinding()), modelContext), getPolicyModels(), port.getBinding(), service.getName(), port.getName()));
                        }
                        if (getHandlers4PortTypeMap().containsKey(port.getBinding().getPortType())) {
                            getPolicyMapBuilder().registerHandler(new BuilderHandlerEndpointScope(getPolicyURIs(getHandlers4PortTypeMap().get(port.getBinding().getPortType()), modelContext), getPolicyModels(), port.getBinding().getPortType(), service.getName(), port.getName()));
                        }
                        for (EditableWSDLBoundOperation boundOperation : port.getBinding().getBindingOperations()) {
                            EditableWSDLOperation operation = boundOperation.getOperation();
                            QName operationName = new QName(boundOperation.getBoundPortType().getName().getNamespaceURI(), boundOperation.getName().getLocalPart());
                            if (getHandlers4BoundOperationMap().containsKey(boundOperation)) {
                                getPolicyMapBuilder().registerHandler(new BuilderHandlerOperationScope(getPolicyURIs(getHandlers4BoundOperationMap().get(boundOperation), modelContext), getPolicyModels(), boundOperation, service.getName(), port.getName(), operationName));
                            }
                            if (getHandlers4OperationMap().containsKey(operation)) {
                                getPolicyMapBuilder().registerHandler(new BuilderHandlerOperationScope(getPolicyURIs(getHandlers4OperationMap().get(operation), modelContext), getPolicyModels(), operation, service.getName(), port.getName(), operationName));
                            }
                            EditableWSDLInput input = operation.getInput();
                            if (null != input && (inputMsg = input.getMessage()) != null && getHandlers4MessageMap().containsKey(inputMsg)) {
                                messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4MessageMap().get(inputMsg), modelContext), getPolicyModels(), inputMsg, BuilderHandlerMessageScope.Scope.InputMessageScope, service.getName(), port.getName(), operationName, null));
                            }
                            if (getHandlers4BindingInputOpMap().containsKey(boundOperation)) {
                                getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4BindingInputOpMap().get(boundOperation), modelContext), getPolicyModels(), boundOperation, BuilderHandlerMessageScope.Scope.InputMessageScope, service.getName(), port.getName(), operationName, null));
                            }
                            if (null != input && getHandlers4InputMap().containsKey(input)) {
                                getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4InputMap().get(input), modelContext), getPolicyModels(), input, BuilderHandlerMessageScope.Scope.InputMessageScope, service.getName(), port.getName(), operationName, null));
                            }
                            EditableWSDLOutput output = operation.getOutput();
                            if (null != output && (outputMsg = output.getMessage()) != null && getHandlers4MessageMap().containsKey(outputMsg)) {
                                messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4MessageMap().get(outputMsg), modelContext), getPolicyModels(), outputMsg, BuilderHandlerMessageScope.Scope.OutputMessageScope, service.getName(), port.getName(), operationName, null));
                            }
                            if (getHandlers4BindingOutputOpMap().containsKey(boundOperation)) {
                                getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4BindingOutputOpMap().get(boundOperation), modelContext), getPolicyModels(), boundOperation, BuilderHandlerMessageScope.Scope.OutputMessageScope, service.getName(), port.getName(), operationName, null));
                            }
                            if (null != output && getHandlers4OutputMap().containsKey(output)) {
                                getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4OutputMap().get(output), modelContext), getPolicyModels(), output, BuilderHandlerMessageScope.Scope.OutputMessageScope, service.getName(), port.getName(), operationName, null));
                            }
                            for (EditableWSDLBoundFault boundFault : boundOperation.getFaults()) {
                                EditableWSDLFault fault = boundFault.getFault();
                                if (fault == null) {
                                    LOGGER.warning(PolicyMessages.WSP_1021_FAULT_NOT_BOUND(boundFault.getName()));
                                } else {
                                    EditableWSDLMessage faultMessage = fault.getMessage();
                                    QName faultName = new QName(boundOperation.getBoundPortType().getName().getNamespaceURI(), boundFault.getName());
                                    if (faultMessage != null && getHandlers4MessageMap().containsKey(faultMessage)) {
                                        messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4MessageMap().get(faultMessage), modelContext), getPolicyModels(), new WSDLBoundFaultContainer(boundFault, boundOperation), BuilderHandlerMessageScope.Scope.FaultMessageScope, service.getName(), port.getName(), operationName, faultName));
                                    }
                                    if (getHandlers4FaultMap().containsKey(fault)) {
                                        messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4FaultMap().get(fault), modelContext), getPolicyModels(), new WSDLBoundFaultContainer(boundFault, boundOperation), BuilderHandlerMessageScope.Scope.FaultMessageScope, service.getName(), port.getName(), operationName, faultName));
                                    }
                                    if (getHandlers4BindingFaultOpMap().containsKey(boundFault)) {
                                        messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs(getHandlers4BindingFaultOpMap().get(boundFault), modelContext), getPolicyModels(), new WSDLBoundFaultContainer(boundFault, boundOperation), BuilderHandlerMessageScope.Scope.FaultMessageScope, service.getName(), port.getName(), operationName, faultName));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Iterator<BuilderHandlerMessageScope> it2 = messageSet.iterator();
            while (it2.hasNext()) {
                BuilderHandlerMessageScope scopeHandler = it2.next();
                getPolicyMapBuilder().registerHandler(scopeHandler);
            }
        } catch (PolicyException e4) {
            LOGGER.logSevereException(e4);
        }
        LOGGER.exiting();
    }

    @Override // com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension
    public void postFinished(WSDLParserExtensionContext context) throws WebServiceException {
        PolicyMap effectiveMap;
        EditableWSDLModel wsdlModel = context.getWSDLModel();
        try {
            if (context.isClientSide()) {
                effectiveMap = context.getPolicyResolver().resolve(new PolicyResolver.ClientContext(this.policyBuilder.getPolicyMap(new PolicyMapMutator[0]), context.getContainer()));
            } else {
                effectiveMap = context.getPolicyResolver().resolve(new PolicyResolver.ServerContext(this.policyBuilder.getPolicyMap(new PolicyMapMutator[0]), context.getContainer(), null, new PolicyMapMutator[0]));
            }
            wsdlModel.setPolicyMap(effectiveMap);
            try {
                PolicyUtil.configureModel(wsdlModel, effectiveMap);
                LOGGER.exiting();
            } catch (PolicyException e2) {
                LOGGER.logSevereException(e2);
                throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1012_FAILED_CONFIGURE_WSDL_MODEL(), e2)));
            }
        } catch (PolicyException e3) {
            LOGGER.logSevereException(e3);
            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1007_POLICY_EXCEPTION_WHILE_FINISHING_PARSING_WSDL(), e3)));
        }
    }

    private String[] getPolicyURIsFromAttr(XMLStreamReader reader) {
        StringBuilder policyUriBuffer = new StringBuilder();
        for (NamespaceVersion version : NamespaceVersion.values()) {
            String value = reader.getAttributeValue(version.toString(), XmlToken.PolicyUris.toString());
            if (value != null) {
                policyUriBuffer.append(value).append(" ");
            }
        }
        if (policyUriBuffer.length() > 0) {
            return policyUriBuffer.toString().split("[\\n ]+");
        }
        return null;
    }
}
