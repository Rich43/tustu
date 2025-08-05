package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLInput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLModel;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPortType;
import com.sun.xml.internal.ws.util.QNameMap;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLOperationImpl.class */
public final class WSDLOperationImpl extends AbstractExtensibleImpl implements EditableWSDLOperation {
    private final QName name;
    private String parameterOrder;
    private EditableWSDLInput input;
    private EditableWSDLOutput output;
    private final List<EditableWSDLFault> faults;
    private final QNameMap<EditableWSDLFault> faultMap;
    protected Iterable<EditableWSDLMessage> messages;
    private final EditableWSDLPortType owner;
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
        $assertionsDisabled = !WSDLOperationImpl.class.desiredAssertionStatus();
    }

    public WSDLOperationImpl(XMLStreamReader xsr, EditableWSDLPortType owner, QName name) {
        super(xsr);
        this.name = name;
        this.faults = new ArrayList();
        this.faultMap = new QNameMap<>();
        this.owner = owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public QName getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public String getParameterOrder() {
        return this.parameterOrder;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation
    public void setParameterOrder(String parameterOrder) {
        this.parameterOrder = parameterOrder;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public EditableWSDLInput getInput() {
        return this.input;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation
    public void setInput(EditableWSDLInput input) {
        this.input = input;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public EditableWSDLOutput getOutput() {
        return this.output;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public boolean isOneWay() {
        return this.output == null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation
    public void setOutput(EditableWSDLOutput output) {
        this.output = output;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public Iterable<EditableWSDLFault> getFaults() {
        return this.faults;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation, com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    public EditableWSDLFault getFault(QName faultDetailName) {
        EditableWSDLFault fault = this.faultMap.get(faultDetailName);
        if (fault != null) {
            return fault;
        }
        for (EditableWSDLFault fi : this.faults) {
            if (!$assertionsDisabled && !fi.getMessage().parts().iterator().hasNext()) {
                throw new AssertionError();
            }
            EditableWSDLPart part = fi.getMessage().parts().iterator().next();
            if (part.getDescriptor().name().equals(faultDetailName)) {
                this.faultMap.put(faultDetailName, fi);
                return fi;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation
    @NotNull
    public QName getPortTypeName() {
        return this.owner.getName();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation
    public void addFault(EditableWSDLFault fault) {
        this.faults.add(fault);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLOperation
    public void freeze(EditableWSDLModel root) {
        if (!$assertionsDisabled && this.input == null) {
            throw new AssertionError();
        }
        this.input.freeze(root);
        if (this.output != null) {
            this.output.freeze(root);
        }
        for (EditableWSDLFault fault : this.faults) {
            fault.freeze(root);
        }
    }
}
