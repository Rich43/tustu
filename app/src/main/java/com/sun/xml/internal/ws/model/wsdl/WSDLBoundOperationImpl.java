package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLBoundOperationImpl.class */
public final class WSDLBoundOperationImpl extends AbstractExtensibleImpl implements EditableWSDLBoundOperation {
    private final QName name;
    private final Map<String, ParameterBinding> inputParts;
    private final Map<String, ParameterBinding> outputParts;
    private final Map<String, ParameterBinding> faultParts;
    private final Map<String, String> inputMimeTypes;
    private final Map<String, String> outputMimeTypes;
    private final Map<String, String> faultMimeTypes;
    private boolean explicitInputSOAPBodyParts;
    private boolean explicitOutputSOAPBodyParts;
    private boolean explicitFaultSOAPBodyParts;
    private Boolean emptyInputBody;
    private Boolean emptyOutputBody;
    private Boolean emptyFaultBody;
    private final Map<String, EditableWSDLPart> inParts;
    private final Map<String, EditableWSDLPart> outParts;
    private final List<EditableWSDLBoundFault> wsdlBoundFaults;
    private EditableWSDLOperation operation;
    private String soapAction;
    private WSDLBoundOperation.ANONYMOUS anonymous;
    private final EditableWSDLBoundPortType owner;
    private SOAPBinding.Style style;
    private String reqNamespace;
    private String respNamespace;
    private QName requestPayloadName;
    private QName responsePayloadName;
    private boolean emptyRequestPayload;
    private boolean emptyResponsePayload;
    private Map<QName, ? extends EditableWSDLMessage> messages;

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ boolean areRequiredExtensionsUnderstood() {
        return super.areRequiredExtensionsUnderstood();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ void addNotUnderstoodExtension(QName qName, Locator locator) {
        super.addNotUnderstoodExtension(qName, locator);
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ List getNotUnderstoodExtensions() {
        return super.getNotUnderstoodExtensions();
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ void addExtension(WSDLExtension wSDLExtension) {
        super.addExtension(wSDLExtension);
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractExtensibleImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public /* bridge */ /* synthetic */ WSDLExtension getExtension(Class cls) {
        return super.getExtension(cls);
    }

    public WSDLBoundOperationImpl(XMLStreamReader xsr, EditableWSDLBoundPortType owner, QName name) {
        super(xsr);
        this.explicitInputSOAPBodyParts = false;
        this.explicitOutputSOAPBodyParts = false;
        this.explicitFaultSOAPBodyParts = false;
        this.style = SOAPBinding.Style.DOCUMENT;
        this.name = name;
        this.inputParts = new HashMap();
        this.outputParts = new HashMap();
        this.faultParts = new HashMap();
        this.inputMimeTypes = new HashMap();
        this.outputMimeTypes = new HashMap();
        this.faultMimeTypes = new HashMap();
        this.inParts = new HashMap();
        this.outParts = new HashMap();
        this.wsdlBoundFaults = new ArrayList();
        this.owner = owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public QName getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public String getSOAPAction() {
        return this.soapAction;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction != null ? soapAction : "";
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public EditableWSDLPart getPart(String partName, WebParam.Mode mode) {
        if (mode == WebParam.Mode.IN) {
            return this.inParts.get(partName);
        }
        if (mode == WebParam.Mode.OUT) {
            return this.outParts.get(partName);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void addPart(EditableWSDLPart part, WebParam.Mode mode) {
        if (mode == WebParam.Mode.IN) {
            this.inParts.put(part.getName(), part);
        } else if (mode == WebParam.Mode.OUT) {
            this.outParts.put(part.getName(), part);
        }
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public Map<String, ParameterBinding> getInputParts() {
        return this.inputParts;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public Map<String, ParameterBinding> getOutputParts() {
        return this.outputParts;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public Map<String, ParameterBinding> getFaultParts() {
        return this.faultParts;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public Map<String, ? extends EditableWSDLPart> getInParts() {
        return Collections.unmodifiableMap(this.inParts);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public Map<String, ? extends EditableWSDLPart> getOutParts() {
        return Collections.unmodifiableMap(this.outParts);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @NotNull
    public List<? extends EditableWSDLBoundFault> getFaults() {
        return this.wsdlBoundFaults;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void addFault(@NotNull EditableWSDLBoundFault fault) {
        this.wsdlBoundFaults.add(fault);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public ParameterBinding getInputBinding(String part) {
        if (this.emptyInputBody == null) {
            if (this.inputParts.get(" ") != null) {
                this.emptyInputBody = true;
            } else {
                this.emptyInputBody = false;
            }
        }
        ParameterBinding block = this.inputParts.get(part);
        if (block == null) {
            if (this.explicitInputSOAPBodyParts || this.emptyInputBody.booleanValue()) {
                return ParameterBinding.UNBOUND;
            }
            return ParameterBinding.BODY;
        }
        return block;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public ParameterBinding getOutputBinding(String part) {
        if (this.emptyOutputBody == null) {
            if (this.outputParts.get(" ") != null) {
                this.emptyOutputBody = true;
            } else {
                this.emptyOutputBody = false;
            }
        }
        ParameterBinding block = this.outputParts.get(part);
        if (block == null) {
            if (this.explicitOutputSOAPBodyParts || this.emptyOutputBody.booleanValue()) {
                return ParameterBinding.UNBOUND;
            }
            return ParameterBinding.BODY;
        }
        return block;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public ParameterBinding getFaultBinding(String part) {
        if (this.emptyFaultBody == null) {
            if (this.faultParts.get(" ") != null) {
                this.emptyFaultBody = true;
            } else {
                this.emptyFaultBody = false;
            }
        }
        ParameterBinding block = this.faultParts.get(part);
        if (block == null) {
            if (this.explicitFaultSOAPBodyParts || this.emptyFaultBody.booleanValue()) {
                return ParameterBinding.UNBOUND;
            }
            return ParameterBinding.BODY;
        }
        return block;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public String getMimeTypeForInputPart(String part) {
        return this.inputMimeTypes.get(part);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public String getMimeTypeForOutputPart(String part) {
        return this.outputMimeTypes.get(part);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public String getMimeTypeForFaultPart(String part) {
        return this.faultMimeTypes.get(part);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public EditableWSDLOperation getOperation() {
        return this.operation;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public EditableWSDLBoundPortType getBoundPortType() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setInputExplicitBodyParts(boolean b2) {
        this.explicitInputSOAPBodyParts = b2;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setOutputExplicitBodyParts(boolean b2) {
        this.explicitOutputSOAPBodyParts = b2;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setFaultExplicitBodyParts(boolean b2) {
        this.explicitFaultSOAPBodyParts = b2;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setStyle(SOAPBinding.Style style) {
        this.style = style;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @Nullable
    public QName getRequestPayloadName() {
        if (this.emptyRequestPayload) {
            return null;
        }
        if (this.requestPayloadName != null) {
            return this.requestPayloadName;
        }
        if (this.style.equals(SOAPBinding.Style.RPC)) {
            String ns = getRequestNamespace() != null ? getRequestNamespace() : this.name.getNamespaceURI();
            this.requestPayloadName = new QName(ns, this.name.getLocalPart());
            return this.requestPayloadName;
        }
        QName inMsgName = this.operation.getInput().getMessage().getName();
        EditableWSDLMessage message = this.messages.get(inMsgName);
        for (EditableWSDLPart part : message.parts()) {
            ParameterBinding binding = getInputBinding(part.getName());
            if (binding.isBody()) {
                this.requestPayloadName = part.getDescriptor().name();
                return this.requestPayloadName;
            }
        }
        this.emptyRequestPayload = true;
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    @Nullable
    public QName getResponsePayloadName() {
        if (this.emptyResponsePayload) {
            return null;
        }
        if (this.responsePayloadName != null) {
            return this.responsePayloadName;
        }
        if (this.style.equals(SOAPBinding.Style.RPC)) {
            String ns = getResponseNamespace() != null ? getResponseNamespace() : this.name.getNamespaceURI();
            this.responsePayloadName = new QName(ns, this.name.getLocalPart() + RuntimeModeler.RESPONSE);
            return this.responsePayloadName;
        }
        QName outMsgName = this.operation.getOutput().getMessage().getName();
        EditableWSDLMessage message = this.messages.get(outMsgName);
        for (EditableWSDLPart part : message.parts()) {
            ParameterBinding binding = getOutputBinding(part.getName());
            if (binding.isBody()) {
                this.responsePayloadName = part.getDescriptor().name();
                return this.responsePayloadName;
            }
        }
        this.emptyResponsePayload = true;
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public String getRequestNamespace() {
        return this.reqNamespace != null ? this.reqNamespace : this.name.getNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setRequestNamespace(String ns) {
        this.reqNamespace = ns;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public String getResponseNamespace() {
        return this.respNamespace != null ? this.respNamespace : this.name.getNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setResponseNamespace(String ns) {
        this.respNamespace = ns;
    }

    EditableWSDLBoundPortType getOwner() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void freeze(EditableWSDLModel parent) {
        this.messages = parent.getMessages();
        this.operation = this.owner.getPortType().get(this.name.getLocalPart());
        for (EditableWSDLBoundFault bf2 : this.wsdlBoundFaults) {
            bf2.freeze(this);
        }
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation
    public void setAnonymous(WSDLBoundOperation.ANONYMOUS anonymous) {
        this.anonymous = anonymous;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
    public WSDLBoundOperation.ANONYMOUS getAnonymous() {
        return this.anonymous;
    }
}
