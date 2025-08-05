package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/WSDLOperationFinder.class */
public abstract class WSDLOperationFinder {
    protected final WSDLPort wsdlModel;
    protected final WSBinding binding;
    protected final SEIModel seiModel;

    public WSDLOperationFinder(@NotNull WSDLPort wsdlModel, @NotNull WSBinding binding, @Nullable SEIModel seiModel) {
        this.wsdlModel = wsdlModel;
        this.binding = binding;
        this.seiModel = seiModel;
    }

    public QName getWSDLOperationQName(Packet request) throws DispatchException {
        WSDLOperationMapping m2 = getWSDLOperationMapping(request);
        if (m2 != null) {
            return m2.getOperationName();
        }
        return null;
    }

    public WSDLOperationMapping getWSDLOperationMapping(Packet request) throws DispatchException {
        return null;
    }

    protected WSDLOperationMapping wsdlOperationMapping(JavaMethodImpl j2) {
        return new WSDLOperationMappingImpl(j2.getOperation(), j2);
    }

    protected WSDLOperationMapping wsdlOperationMapping(WSDLBoundOperation o2) {
        return new WSDLOperationMappingImpl(o2, null);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/WSDLOperationFinder$WSDLOperationMappingImpl.class */
    static class WSDLOperationMappingImpl implements WSDLOperationMapping {
        private WSDLBoundOperation wsdlOperation;
        private JavaMethod javaMethod;
        private QName operationName;

        WSDLOperationMappingImpl(WSDLBoundOperation wsdlOperation, JavaMethodImpl javaMethod) {
            this.wsdlOperation = wsdlOperation;
            this.javaMethod = javaMethod;
            this.operationName = javaMethod != null ? javaMethod.getOperationQName() : wsdlOperation.getName();
        }

        @Override // com.sun.xml.internal.ws.api.model.WSDLOperationMapping
        public WSDLBoundOperation getWSDLBoundOperation() {
            return this.wsdlOperation;
        }

        @Override // com.sun.xml.internal.ws.api.model.WSDLOperationMapping
        public JavaMethod getJavaMethod() {
            return this.javaMethod;
        }

        @Override // com.sun.xml.internal.ws.api.model.WSDLOperationMapping
        public QName getOperationName() {
            return this.operationName;
        }
    }
}
