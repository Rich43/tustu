package com.sun.xml.internal.ws.model.wsdl;

import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPartDescriptor;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLPartImpl.class */
public final class WSDLPartImpl extends AbstractObjectImpl implements EditableWSDLPart {
    private final String name;
    private ParameterBinding binding;
    private int index;
    private final WSDLPartDescriptor descriptor;

    public WSDLPartImpl(XMLStreamReader xsr, String partName, int index, WSDLPartDescriptor descriptor) {
        super(xsr);
        this.name = partName;
        this.binding = ParameterBinding.UNBOUND;
        this.index = index;
        this.descriptor = descriptor;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPart
    public String getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPart
    public ParameterBinding getBinding() {
        return this.binding;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart
    public void setBinding(ParameterBinding binding) {
        this.binding = binding;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPart
    public int getIndex() {
        return this.index;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart
    public void setIndex(int index) {
        this.index = index;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLPart
    public WSDLPartDescriptor getDescriptor() {
        return this.descriptor;
    }
}
