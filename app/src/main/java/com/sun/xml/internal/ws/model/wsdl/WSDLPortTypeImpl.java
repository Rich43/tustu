package com.sun.xml.internal.ws.model.wsdl;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLPortTypeImpl.class */
public final class WSDLPortTypeImpl extends AbstractExtensibleImpl implements EditableWSDLPortType {
    private QName name;
    private final Map<String, EditableWSDLOperation> portTypeOperations;
    private EditableWSDLModel owner;

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

    public WSDLPortTypeImpl(XMLStreamReader xsr, EditableWSDLModel owner, QName name) {
        super(xsr);
        this.name = name;
        this.owner = owner;
        this.portTypeOperations = new Hashtable();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType
    public QName getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType
    public EditableWSDLOperation get(String operationName) {
        return this.portTypeOperations.get(operationName);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType, com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType
    public Iterable<EditableWSDLOperation> getOperations() {
        return this.portTypeOperations.values();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType
    public void put(String opName, EditableWSDLOperation ptOp) {
        this.portTypeOperations.put(opName, ptOp);
    }

    EditableWSDLModel getOwner() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType
    public void freeze() {
        for (EditableWSDLOperation op : this.portTypeOperations.values()) {
            op.freeze(this.owner);
        }
    }
}
