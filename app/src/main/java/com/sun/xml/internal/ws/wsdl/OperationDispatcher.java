package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.resources.ServerMessages;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/OperationDispatcher.class */
public class OperationDispatcher {
    private List<WSDLOperationFinder> opFinders = new ArrayList();
    private WSBinding binding;

    public OperationDispatcher(@NotNull WSDLPort wsdlModel, @NotNull WSBinding binding, @Nullable SEIModel seiModel) {
        this.binding = binding;
        if (binding.getAddressingVersion() != null) {
            this.opFinders.add(new ActionBasedOperationFinder(wsdlModel, binding, seiModel));
        }
        this.opFinders.add(new PayloadQNameBasedOperationFinder(wsdlModel, binding, seiModel));
        this.opFinders.add(new SOAPActionBasedOperationFinder(wsdlModel, binding, seiModel));
    }

    @NotNull
    public QName getWSDLOperationQName(Packet request) throws DispatchException {
        WSDLOperationMapping m2 = getWSDLOperationMapping(request);
        if (m2 != null) {
            return m2.getOperationName();
        }
        return null;
    }

    @NotNull
    public WSDLOperationMapping getWSDLOperationMapping(Packet request) throws DispatchException {
        for (WSDLOperationFinder finder : this.opFinders) {
            WSDLOperationMapping opName = finder.getWSDLOperationMapping(request);
            if (opName != null) {
                return opName;
            }
        }
        String err = MessageFormat.format("Request=[SOAPAction={0},Payload='{'{1}'}'{2}]", request.soapAction, request.getMessage().getPayloadNamespaceURI(), request.getMessage().getPayloadLocalPart());
        String faultString = ServerMessages.DISPATCH_CANNOT_FIND_METHOD(err);
        Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(this.binding.getSOAPVersion(), faultString, this.binding.getSOAPVersion().faultCodeClient);
        throw new DispatchException(faultMsg);
    }
}
