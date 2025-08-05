package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/SOAPActionBasedOperationFinder.class */
final class SOAPActionBasedOperationFinder extends WSDLOperationFinder {
    private final Map<String, WSDLOperationMapping> methodHandlers;

    public SOAPActionBasedOperationFinder(WSDLPort wsdlModel, WSBinding binding, @Nullable SEIModel seiModel) {
        super(wsdlModel, binding, seiModel);
        this.methodHandlers = new HashMap();
        Map<String, Integer> unique = new HashMap<>();
        if (seiModel != null) {
            Iterator<JavaMethodImpl> it = ((AbstractSEIModelImpl) seiModel).getJavaMethods().iterator();
            while (it.hasNext()) {
                String soapAction = it.next().getSOAPAction();
                Integer count = unique.get(soapAction);
                if (count == null) {
                    unique.put(soapAction, 1);
                } else {
                    unique.put(soapAction, Integer.valueOf(count.intValue() + 1));
                }
            }
            for (JavaMethodImpl m2 : ((AbstractSEIModelImpl) seiModel).getJavaMethods()) {
                String soapAction2 = m2.getSOAPAction();
                if (unique.get(soapAction2).intValue() == 1) {
                    this.methodHandlers.put('\"' + soapAction2 + '\"', wsdlOperationMapping(m2));
                }
            }
            return;
        }
        for (WSDLBoundOperation wsdlOp : wsdlModel.getBinding().getBindingOperations()) {
            this.methodHandlers.put(wsdlOp.getSOAPAction(), wsdlOperationMapping(wsdlOp));
        }
    }

    @Override // com.sun.xml.internal.ws.wsdl.WSDLOperationFinder
    public WSDLOperationMapping getWSDLOperationMapping(Packet request) throws DispatchException {
        if (request.soapAction == null) {
            return null;
        }
        return this.methodHandlers.get(request.soapAction);
    }
}
