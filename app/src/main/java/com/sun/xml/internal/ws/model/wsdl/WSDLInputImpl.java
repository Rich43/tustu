package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLInputImpl.class */
public final class WSDLInputImpl extends AbstractExtensibleImpl implements EditableWSDLInput {
    private String name;
    private QName messageName;
    private EditableWSDLOperation operation;
    private EditableWSDLMessage message;
    private String action;
    private boolean defaultAction;

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

    public WSDLInputImpl(XMLStreamReader xsr, String name, QName messageName, EditableWSDLOperation operation) {
        super(xsr);
        this.defaultAction = true;
        this.name = name;
        this.messageName = messageName;
        this.operation = operation;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    public String getName() {
        if (this.name != null) {
            return this.name;
        }
        return this.operation.isOneWay() ? this.operation.getName().getLocalPart() : this.operation.getName().getLocalPart() + "Request";
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput, com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    public EditableWSDLMessage getMessage() {
        return this.message;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    public String getAction() {
        return this.action;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput, com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    @NotNull
    public EditableWSDLOperation getOperation() {
        return this.operation;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    public QName getQName() {
        return new QName(this.operation.getName().getNamespaceURI(), getName());
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput
    public void setAction(String action) {
        this.action = action;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLInput
    public boolean isDefaultAction() {
        return this.defaultAction;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput
    public void setDefaultAction(boolean defaultAction) {
        this.defaultAction = defaultAction;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput
    public void freeze(EditableWSDLModel parent) {
        this.message = parent.getMessage(this.messageName);
    }
}
