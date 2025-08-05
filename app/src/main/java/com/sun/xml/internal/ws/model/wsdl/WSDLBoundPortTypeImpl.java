package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.util.QNameMap;
import com.sun.xml.internal.ws.util.exception.LocatableWebServiceException;
import java.util.List;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceFeature;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLBoundPortTypeImpl.class */
public final class WSDLBoundPortTypeImpl extends AbstractFeaturedObjectImpl implements EditableWSDLBoundPortType {
    private final QName name;
    private final QName portTypeName;
    private EditableWSDLPortType portType;
    private BindingID bindingId;

    @NotNull
    private final EditableWSDLModel owner;
    private final QNameMap<EditableWSDLBoundOperation> bindingOperations;
    private QNameMap<EditableWSDLBoundOperation> payloadMap;
    private EditableWSDLBoundOperation emptyPayloadOperation;
    private SOAPBinding.Style style;

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractFeaturedObjectImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    @Nullable
    public /* bridge */ /* synthetic */ WebServiceFeature getFeature(@NotNull Class cls) {
        return super.getFeature(cls);
    }

    @Override // com.sun.xml.internal.ws.model.wsdl.AbstractFeaturedObjectImpl, com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject
    @NotNull
    public /* bridge */ /* synthetic */ WebServiceFeatureList getFeatures() {
        return super.getFeatures();
    }

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

    public WSDLBoundPortTypeImpl(XMLStreamReader xsr, @NotNull EditableWSDLModel owner, QName name, QName portTypeName) {
        super(xsr);
        this.bindingOperations = new QNameMap<>();
        this.style = SOAPBinding.Style.DOCUMENT;
        this.owner = owner;
        this.name = name;
        this.portTypeName = portTypeName;
        owner.addBinding(this);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public QName getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    @NotNull
    public EditableWSDLModel getOwner() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public EditableWSDLBoundOperation get(QName operationName) {
        return this.bindingOperations.get(operationName);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType
    public void put(QName opName, EditableWSDLBoundOperation ptOp) {
        this.bindingOperations.put(opName, ptOp);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public QName getPortTypeName() {
        return this.portTypeName;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public EditableWSDLPortType getPortType() {
        return this.portType;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public Iterable<EditableWSDLBoundOperation> getBindingOperations() {
        return this.bindingOperations.values();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public BindingID getBindingId() {
        return this.bindingId == null ? BindingID.SOAP11_HTTP : this.bindingId;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType
    public void setBindingId(BindingID bindingId) {
        this.bindingId = bindingId;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType
    public void setStyle(SOAPBinding.Style style) {
        this.style = style;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public SOAPBinding.Style getStyle() {
        return this.style;
    }

    public boolean isRpcLit() {
        return SOAPBinding.Style.RPC == this.style;
    }

    public boolean isDoclit() {
        return SOAPBinding.Style.DOCUMENT == this.style;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public ParameterBinding getBinding(QName operation, String part, WebParam.Mode mode) {
        EditableWSDLBoundOperation op = get(operation);
        if (op == null) {
            return null;
        }
        if (WebParam.Mode.IN == mode || WebParam.Mode.INOUT == mode) {
            return op.getInputBinding(part);
        }
        return op.getOutputBinding(part);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType
    public EditableWSDLBoundOperation getOperation(String namespaceUri, String localName) {
        if (namespaceUri == null && localName == null) {
            return this.emptyPayloadOperation;
        }
        return this.payloadMap.get(namespaceUri == null ? "" : namespaceUri, localName);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundPortType
    public void freeze() {
        this.portType = this.owner.getPortType(this.portTypeName);
        if (this.portType == null) {
            throw new LocatableWebServiceException(ClientMessages.UNDEFINED_PORT_TYPE(this.portTypeName), getLocation());
        }
        this.portType.freeze();
        for (EditableWSDLBoundOperation op : this.bindingOperations.values()) {
            op.freeze(this.owner);
        }
        freezePayloadMap();
        this.owner.finalizeRpcLitBinding(this);
    }

    private void freezePayloadMap() {
        if (this.style == SOAPBinding.Style.RPC) {
            this.payloadMap = new QNameMap<>();
            for (EditableWSDLBoundOperation op : this.bindingOperations.values()) {
                this.payloadMap.put(op.getRequestPayloadName(), op);
            }
            return;
        }
        this.payloadMap = new QNameMap<>();
        for (EditableWSDLBoundOperation op2 : this.bindingOperations.values()) {
            QName name = op2.getRequestPayloadName();
            if (name == null) {
                this.emptyPayloadOperation = op2;
            } else {
                this.payloadMap.put(name, op2);
            }
        }
    }
}
