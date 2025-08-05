package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLBoundFaultImpl.class */
public class WSDLBoundFaultImpl extends AbstractExtensibleImpl implements EditableWSDLBoundFault {
    private final String name;
    private EditableWSDLFault fault;
    private EditableWSDLBoundOperation owner;
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
        $assertionsDisabled = !WSDLBoundFaultImpl.class.desiredAssertionStatus();
    }

    public WSDLBoundFaultImpl(XMLStreamReader xsr, String name, EditableWSDLBoundOperation owner) {
        super(xsr);
        this.name = name;
        this.owner = owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
    public QName getQName() {
        if (this.owner.getOperation() != null) {
            return new QName(this.owner.getOperation().getName().getNamespaceURI(), this.name);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
    public EditableWSDLFault getFault() {
        return this.fault;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault, com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault
    @NotNull
    public EditableWSDLBoundOperation getBoundOperation() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLBoundFault
    public void freeze(EditableWSDLBoundOperation root) {
        if (!$assertionsDisabled && root == null) {
            throw new AssertionError();
        }
        EditableWSDLOperation op = root.getOperation();
        if (op != null) {
            for (EditableWSDLFault f2 : op.getFaults()) {
                if (f2.getName().equals(this.name)) {
                    this.fault = f2;
                    return;
                }
            }
        }
    }
}
