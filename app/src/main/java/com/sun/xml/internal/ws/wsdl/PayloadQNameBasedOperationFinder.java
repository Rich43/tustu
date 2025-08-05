package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.resources.ServerMessages;
import com.sun.xml.internal.ws.util.QNameMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/PayloadQNameBasedOperationFinder.class */
final class PayloadQNameBasedOperationFinder extends WSDLOperationFinder {
    public static final String EMPTY_PAYLOAD_LOCAL = "";
    public static final String EMPTY_PAYLOAD_NSURI = "";
    private final QNameMap<WSDLOperationMapping> methodHandlers;
    private final QNameMap<List<String>> unique;
    private static final Logger LOGGER = Logger.getLogger(PayloadQNameBasedOperationFinder.class.getName());
    public static final QName EMPTY_PAYLOAD = new QName("", "");

    public PayloadQNameBasedOperationFinder(WSDLPort wsdlModel, WSBinding binding, @Nullable SEIModel seiModel) {
        super(wsdlModel, binding, seiModel);
        this.methodHandlers = new QNameMap<>();
        this.unique = new QNameMap<>();
        if (seiModel != null) {
            for (JavaMethodImpl m2 : ((AbstractSEIModelImpl) seiModel).getJavaMethods()) {
                if (!m2.getMEP().isAsync) {
                    QName name = m2.getRequestPayloadName();
                    name = name == null ? EMPTY_PAYLOAD : name;
                    List<String> methods = this.unique.get(name);
                    if (methods == null) {
                        methods = new ArrayList();
                        this.unique.put(name, methods);
                    }
                    methods.add(m2.getMethod().getName());
                }
            }
            for (QNameMap.Entry<List<String>> e2 : this.unique.entrySet()) {
                if (e2.getValue().size() > 1) {
                    LOGGER.warning(ServerMessages.NON_UNIQUE_DISPATCH_QNAME(e2.getValue(), e2.createQName()));
                }
            }
            for (JavaMethodImpl m3 : ((AbstractSEIModelImpl) seiModel).getJavaMethods()) {
                QName name2 = m3.getRequestPayloadName();
                name2 = name2 == null ? EMPTY_PAYLOAD : name2;
                if (this.unique.get(name2).size() == 1) {
                    this.methodHandlers.put(name2, wsdlOperationMapping(m3));
                }
            }
            return;
        }
        for (WSDLBoundOperation wsdlOp : wsdlModel.getBinding().getBindingOperations()) {
            QName name3 = wsdlOp.getRequestPayloadName();
            if (name3 == null) {
                name3 = EMPTY_PAYLOAD;
            }
            this.methodHandlers.put(name3, wsdlOperationMapping(wsdlOp));
        }
    }

    @Override // com.sun.xml.internal.ws.wsdl.WSDLOperationFinder
    public WSDLOperationMapping getWSDLOperationMapping(Packet request) throws DispatchException {
        String nsUri;
        Message message = request.getMessage();
        String localPart = message.getPayloadLocalPart();
        if (localPart == null) {
            localPart = "";
            nsUri = "";
        } else {
            nsUri = message.getPayloadNamespaceURI();
            if (nsUri == null) {
                nsUri = "";
            }
        }
        WSDLOperationMapping op = this.methodHandlers.get(nsUri, localPart);
        if (op == null && !this.unique.containsKey(nsUri, localPart)) {
            String dispatchKey = VectorFormat.DEFAULT_PREFIX + nsUri + "}" + localPart;
            String faultString = ServerMessages.DISPATCH_CANNOT_FIND_METHOD(dispatchKey);
            throw new DispatchException(SOAPFaultBuilder.createSOAPFaultMessage(this.binding.getSOAPVersion(), faultString, this.binding.getSOAPVersion().faultCodeClient));
        }
        return op;
    }
}
