package com.sun.xml.internal.ws.model.wsdl;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/WSDLMessageImpl.class */
public final class WSDLMessageImpl extends AbstractExtensibleImpl implements EditableWSDLMessage {
    private final QName name;
    private final ArrayList<EditableWSDLPart> parts;

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

    public WSDLMessageImpl(XMLStreamReader xsr, QName name) {
        super(xsr);
        this.name = name;
        this.parts = new ArrayList<>();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage
    public QName getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage
    public void add(EditableWSDLPart part) {
        this.parts.add(part);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLMessage, com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage
    public Iterable<EditableWSDLPart> parts() {
        return this.parts;
    }
}
