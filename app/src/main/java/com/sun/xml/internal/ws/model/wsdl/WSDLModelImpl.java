package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPort;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;
import com.sun.xml.internal.ws.policy.PolicyMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebParam;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLModelImpl.class */
public final class WSDLModelImpl extends AbstractExtensibleImpl implements EditableWSDLModel {
    private final Map<QName, EditableWSDLMessage> messages;
    private final Map<QName, EditableWSDLPortType> portTypes;
    private final Map<QName, EditableWSDLBoundPortType> bindings;
    private final Map<QName, EditableWSDLService> services;
    private PolicyMap policyMap;
    private final Map<QName, EditableWSDLBoundPortType> unmBindings;
    static final /* synthetic */ boolean $assertionsDisabled;

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

    static {
        $assertionsDisabled = !WSDLModelImpl.class.desiredAssertionStatus();
    }

    public WSDLModelImpl(@NotNull String systemId) {
        super(systemId, -1);
        this.messages = new HashMap();
        this.portTypes = new HashMap();
        this.bindings = new HashMap();
        this.services = new LinkedHashMap();
        this.unmBindings = Collections.unmodifiableMap(this.bindings);
    }

    public WSDLModelImpl() {
        super(null, -1);
        this.messages = new HashMap();
        this.portTypes = new HashMap();
        this.bindings = new HashMap();
        this.services = new LinkedHashMap();
        this.unmBindings = Collections.unmodifiableMap(this.bindings);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void addMessage(EditableWSDLMessage msg) {
        this.messages.put(msg.getName(), msg);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public EditableWSDLMessage getMessage(QName name) {
        return this.messages.get(name);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void addPortType(EditableWSDLPortType pt) {
        this.portTypes.put(pt.getName(), pt);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public EditableWSDLPortType getPortType(QName name) {
        return this.portTypes.get(name);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void addBinding(EditableWSDLBoundPortType boundPortType) {
        if (!$assertionsDisabled && this.bindings.containsValue(boundPortType)) {
            throw new AssertionError();
        }
        this.bindings.put(boundPortType.getName(), boundPortType);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public EditableWSDLBoundPortType getBinding(QName name) {
        return this.bindings.get(name);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void addService(EditableWSDLService svc) {
        this.services.put(svc.getName(), svc);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public EditableWSDLService getService(QName name) {
        return this.services.get(name);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public Map<QName, EditableWSDLMessage> getMessages() {
        return this.messages;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    public Map<QName, EditableWSDLPortType> getPortTypes() {
        return this.portTypes;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    public Map<QName, ? extends EditableWSDLBoundPortType> getBindings() {
        return this.unmBindings;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    @NotNull
    public Map<QName, EditableWSDLService> getServices() {
        return this.services;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public QName getFirstServiceName() {
        if (this.services.isEmpty()) {
            return null;
        }
        return this.services.values().iterator().next().getName();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel, com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public EditableWSDLBoundPortType getBinding(QName serviceName, QName portName) {
        EditableWSDLPort port;
        EditableWSDLService service = this.services.get(serviceName);
        if (service != null && (port = service.get(portName)) != null) {
            return port.getBinding();
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void finalizeRpcLitBinding(EditableWSDLBoundPortType boundPortType) {
        WSDLPortType pt;
        WSDLMessage outMsgName;
        EditableWSDLMessage outMsg;
        if (!$assertionsDisabled && boundPortType == null) {
            throw new AssertionError();
        }
        QName portTypeName = boundPortType.getPortTypeName();
        if (portTypeName == null || (pt = this.portTypes.get(portTypeName)) == null) {
            return;
        }
        for (EditableWSDLBoundOperation bop : boundPortType.getBindingOperations()) {
            WSDLOperation pto = pt.get(bop.getName().getLocalPart());
            WSDLMessage inMsgName = pto.getInput().getMessage();
            if (inMsgName != null) {
                EditableWSDLMessage inMsg = this.messages.get(inMsgName.getName());
                int bodyindex = 0;
                if (inMsg != null) {
                    for (EditableWSDLPart part : inMsg.parts()) {
                        String name = part.getName();
                        ParameterBinding pb = bop.getInputBinding(name);
                        if (pb.isBody()) {
                            int i2 = bodyindex;
                            bodyindex++;
                            part.setIndex(i2);
                            part.setBinding(pb);
                            bop.addPart(part, WebParam.Mode.IN);
                        }
                    }
                }
                int bodyindex2 = 0;
                if (!pto.isOneWay() && (outMsgName = pto.getOutput().getMessage()) != null && (outMsg = this.messages.get(outMsgName.getName())) != null) {
                    for (EditableWSDLPart part2 : outMsg.parts()) {
                        String name2 = part2.getName();
                        ParameterBinding pb2 = bop.getOutputBinding(name2);
                        if (pb2.isBody()) {
                            int i3 = bodyindex2;
                            bodyindex2++;
                            part2.setIndex(i3);
                            part2.setBinding(pb2);
                            bop.addPart(part2, WebParam.Mode.OUT);
                        }
                    }
                }
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
    public PolicyMap getPolicyMap() {
        return this.policyMap;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void setPolicyMap(PolicyMap policyMap) {
        this.policyMap = policyMap;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel
    public void freeze() {
        for (EditableWSDLService service : this.services.values()) {
            service.freeze(this);
        }
        for (EditableWSDLBoundPortType bp2 : this.bindings.values()) {
            bp2.freeze();
        }
        for (EditableWSDLPortType pt : this.portTypes.values()) {
            pt.freeze();
        }
    }
}
