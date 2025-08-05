package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLOutputImpl.class */
public final class WSDLOutputImpl extends AbstractExtensibleImpl implements EditableWSDLOutput {
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

    public WSDLOutputImpl(XMLStreamReader xsr, String name, QName messageName, EditableWSDLOperation operation) {
        super(xsr);
        this.defaultAction = true;
        this.name = name;
        this.messageName = messageName;
        this.operation = operation;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    public String getName() {
        return this.name == null ? this.operation.getName().getLocalPart() + RuntimeModeler.RESPONSE : this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput, com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    public EditableWSDLMessage getMessage() {
        return this.message;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    public String getAction() {
        return this.action;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    public boolean isDefaultAction() {
        return this.defaultAction;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput
    public void setDefaultAction(boolean defaultAction) {
        this.defaultAction = defaultAction;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput, com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    @NotNull
    public EditableWSDLOperation getOperation() {
        return this.operation;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput
    @NotNull
    public QName getQName() {
        return new QName(this.operation.getName().getNamespaceURI(), getName());
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput
    public void setAction(String action) {
        this.action = action;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput
    public void freeze(EditableWSDLModel root) {
        this.message = root.getMessage(this.messageName);
    }
}
