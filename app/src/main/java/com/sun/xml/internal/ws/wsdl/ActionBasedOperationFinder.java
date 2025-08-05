package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/ActionBasedOperationFinder.class */
final class ActionBasedOperationFinder extends WSDLOperationFinder {
    private static final Logger LOGGER;
    private final Map<ActionBasedOperationSignature, WSDLOperationMapping> uniqueOpSignatureMap;
    private final Map<String, WSDLOperationMapping> actionMap;

    /* renamed from: av, reason: collision with root package name */
    @NotNull
    private final AddressingVersion f12092av;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ActionBasedOperationFinder.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(ActionBasedOperationFinder.class.getName());
    }

    public ActionBasedOperationFinder(WSDLPort wsdlModel, WSBinding binding, @Nullable SEIModel seiModel) {
        super(wsdlModel, binding, seiModel);
        if (!$assertionsDisabled && binding.getAddressingVersion() == null) {
            throw new AssertionError();
        }
        this.f12092av = binding.getAddressingVersion();
        this.uniqueOpSignatureMap = new HashMap();
        this.actionMap = new HashMap();
        if (seiModel != null) {
            for (JavaMethodImpl m2 : ((AbstractSEIModelImpl) seiModel).getJavaMethods()) {
                if (!m2.getMEP().isAsync) {
                    String action = m2.getInputAction();
                    QName payloadName = m2.getRequestPayloadName();
                    payloadName = payloadName == null ? PayloadQNameBasedOperationFinder.EMPTY_PAYLOAD : payloadName;
                    if ((action == null || action.equals("")) && m2.getOperation() != null) {
                        action = m2.getOperation().getOperation().getInput().getAction();
                    }
                    if (action != null) {
                        ActionBasedOperationSignature opSignature = new ActionBasedOperationSignature(action, payloadName);
                        if (this.uniqueOpSignatureMap.get(opSignature) != null) {
                            LOGGER.warning(AddressingMessages.NON_UNIQUE_OPERATION_SIGNATURE(this.uniqueOpSignatureMap.get(opSignature), m2.getOperationQName(), action, payloadName));
                        }
                        this.uniqueOpSignatureMap.put(opSignature, wsdlOperationMapping(m2));
                        this.actionMap.put(action, wsdlOperationMapping(m2));
                    }
                }
            }
            return;
        }
        for (WSDLBoundOperation wsdlOp : wsdlModel.getBinding().getBindingOperations()) {
            QName payloadName2 = wsdlOp.getRequestPayloadName();
            payloadName2 = payloadName2 == null ? PayloadQNameBasedOperationFinder.EMPTY_PAYLOAD : payloadName2;
            String action2 = wsdlOp.getOperation().getInput().getAction();
            ActionBasedOperationSignature opSignature2 = new ActionBasedOperationSignature(action2, payloadName2);
            if (this.uniqueOpSignatureMap.get(opSignature2) != null) {
                LOGGER.warning(AddressingMessages.NON_UNIQUE_OPERATION_SIGNATURE(this.uniqueOpSignatureMap.get(opSignature2), wsdlOp.getName(), action2, payloadName2));
            }
            this.uniqueOpSignatureMap.put(opSignature2, wsdlOperationMapping(wsdlOp));
            this.actionMap.put(action2, wsdlOperationMapping(wsdlOp));
        }
    }

    @Override // com.sun.xml.internal.ws.wsdl.WSDLOperationFinder
    public WSDLOperationMapping getWSDLOperationMapping(Packet request) throws DispatchException {
        QName payloadName;
        MessageHeaders hl = request.getMessage().getHeaders();
        String action = AddressingUtils.getAction(hl, this.f12092av, this.binding.getSOAPVersion());
        if (action == null) {
            return null;
        }
        Message message = request.getMessage();
        String localPart = message.getPayloadLocalPart();
        if (localPart == null) {
            payloadName = PayloadQNameBasedOperationFinder.EMPTY_PAYLOAD;
        } else {
            String nsUri = message.getPayloadNamespaceURI();
            if (nsUri == null) {
                nsUri = "";
            }
            payloadName = new QName(nsUri, localPart);
        }
        WSDLOperationMapping opMapping = this.uniqueOpSignatureMap.get(new ActionBasedOperationSignature(action, payloadName));
        if (opMapping != null) {
            return opMapping;
        }
        WSDLOperationMapping opMapping2 = this.actionMap.get(action);
        if (opMapping2 != null) {
            return opMapping2;
        }
        Message result = Messages.create(action, this.f12092av, this.binding.getSOAPVersion());
        throw new DispatchException(result);
    }
}
