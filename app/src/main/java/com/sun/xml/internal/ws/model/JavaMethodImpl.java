package com.sun.xml.internal.ws.model;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.MEP;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.wsdl.ActionBasedOperationSignature;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.ws.Action;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/JavaMethodImpl.class */
public final class JavaMethodImpl implements JavaMethod {
    private final Method method;
    private SOAPBinding binding;
    private MEP mep;
    private QName operationName;
    private WSDLBoundOperation wsdlOperation;
    final AbstractSEIModelImpl owner;
    private final Method seiMethod;
    private QName requestPayloadName;
    private String soapAction;
    private static final Logger LOGGER;
    static final /* synthetic */ boolean $assertionsDisabled;
    private String inputAction = "";
    private String outputAction = "";
    private final List<CheckedExceptionImpl> exceptions = new ArrayList();
    final List<ParameterImpl> requestParams = new ArrayList();
    final List<ParameterImpl> responseParams = new ArrayList();
    private final List<ParameterImpl> unmReqParams = Collections.unmodifiableList(this.requestParams);
    private final List<ParameterImpl> unmResParams = Collections.unmodifiableList(this.responseParams);

    static {
        $assertionsDisabled = !JavaMethodImpl.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(JavaMethodImpl.class.getName());
    }

    public JavaMethodImpl(AbstractSEIModelImpl owner, Method method, Method seiMethod, MetadataReader metadataReader) {
        this.owner = owner;
        this.method = method;
        this.seiMethod = seiMethod;
        setWsaActions(metadataReader);
    }

    private void setWsaActions(MetadataReader metadataReader) {
        Action action = metadataReader != null ? (Action) metadataReader.getAnnotation(Action.class, this.seiMethod) : (Action) this.seiMethod.getAnnotation(Action.class);
        if (action != null) {
            this.inputAction = action.input();
            this.outputAction = action.output();
        }
        WebMethod webMethod = metadataReader != null ? (WebMethod) metadataReader.getAnnotation(WebMethod.class, this.seiMethod) : (WebMethod) this.seiMethod.getAnnotation(WebMethod.class);
        this.soapAction = "";
        if (webMethod != null) {
            this.soapAction = webMethod.action();
        }
        if (!this.soapAction.equals("")) {
            if (this.inputAction.equals("")) {
                this.inputAction = this.soapAction;
            } else {
                if (!this.inputAction.equals(this.soapAction)) {
                }
            }
        }
    }

    public ActionBasedOperationSignature getOperationSignature() {
        QName qname = getRequestPayloadName();
        if (qname == null) {
            qname = new QName("", "");
        }
        return new ActionBasedOperationSignature(getInputAction(), qname);
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public SEIModel getOwner() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public Method getMethod() {
        return this.method;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public Method getSEIMethod() {
        return this.seiMethod;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public MEP getMEP() {
        return this.mep;
    }

    void setMEP(MEP mep) {
        this.mep = mep;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public SOAPBinding getBinding() {
        if (this.binding == null) {
            return new SOAPBindingImpl();
        }
        return this.binding;
    }

    void setBinding(SOAPBinding binding) {
        this.binding = binding;
    }

    public WSDLBoundOperation getOperation() {
        return this.wsdlOperation;
    }

    public void setOperationQName(QName name) {
        this.operationName = name;
    }

    public QName getOperationQName() {
        return this.wsdlOperation != null ? this.wsdlOperation.getName() : this.operationName;
    }

    public String getSOAPAction() {
        return this.wsdlOperation != null ? this.wsdlOperation.getSOAPAction() : this.soapAction;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public String getOperationName() {
        return this.operationName.getLocalPart();
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public String getRequestMessageName() {
        return getOperationName();
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    public String getResponseMessageName() {
        if (this.mep.isOneWay()) {
            return null;
        }
        return getOperationName() + RuntimeModeler.RESPONSE;
    }

    public void setRequestPayloadName(QName n2) {
        this.requestPayloadName = n2;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    @Nullable
    public QName getRequestPayloadName() {
        return this.wsdlOperation != null ? this.wsdlOperation.getRequestPayloadName() : this.requestPayloadName;
    }

    @Override // com.sun.xml.internal.ws.api.model.JavaMethod
    @Nullable
    public QName getResponsePayloadName() {
        if (this.mep == MEP.ONE_WAY) {
            return null;
        }
        return this.wsdlOperation.getResponsePayloadName();
    }

    public List<ParameterImpl> getRequestParameters() {
        return this.unmReqParams;
    }

    public List<ParameterImpl> getResponseParameters() {
        return this.unmResParams;
    }

    void addParameter(ParameterImpl p2) {
        if (p2.isIN() || p2.isINOUT()) {
            if (!$assertionsDisabled && this.requestParams.contains(p2)) {
                throw new AssertionError();
            }
            this.requestParams.add(p2);
        }
        if (p2.isOUT() || p2.isINOUT()) {
            if (!$assertionsDisabled && this.responseParams.contains(p2)) {
                throw new AssertionError();
            }
            this.responseParams.add(p2);
        }
    }

    void addRequestParameter(ParameterImpl p2) {
        if (p2.isIN() || p2.isINOUT()) {
            this.requestParams.add(p2);
        }
    }

    void addResponseParameter(ParameterImpl p2) {
        if (p2.isOUT() || p2.isINOUT()) {
            this.responseParams.add(p2);
        }
    }

    public int getInputParametersCount() {
        int count = 0;
        for (ParameterImpl param : this.requestParams) {
            if (param.isWrapperStyle()) {
                count += ((WrapperParameter) param).getWrapperChildren().size();
            } else {
                count++;
            }
        }
        for (ParameterImpl param2 : this.responseParams) {
            if (param2.isWrapperStyle()) {
                for (ParameterImpl wc : ((WrapperParameter) param2).getWrapperChildren()) {
                    if (!wc.isResponse() && wc.isOUT()) {
                        count++;
                    }
                }
            } else if (!param2.isResponse() && param2.isOUT()) {
                count++;
            }
        }
        return count;
    }

    void addException(CheckedExceptionImpl ce) {
        if (!this.exceptions.contains(ce)) {
            this.exceptions.add(ce);
        }
    }

    public CheckedExceptionImpl getCheckedException(Class exceptionClass) {
        for (CheckedExceptionImpl ce : this.exceptions) {
            if (ce.getExceptionClass() == exceptionClass) {
                return ce;
            }
        }
        return null;
    }

    public List<CheckedExceptionImpl> getCheckedExceptions() {
        return Collections.unmodifiableList(this.exceptions);
    }

    public String getInputAction() {
        return this.inputAction;
    }

    public String getOutputAction() {
        return this.outputAction;
    }

    public CheckedExceptionImpl getCheckedException(TypeReference detailType) {
        for (CheckedExceptionImpl ce : this.exceptions) {
            TypeInfo actual = ce.getDetailType();
            if (actual.tagName.equals(detailType.tagName) && actual.type == detailType.type) {
                return ce;
            }
        }
        return null;
    }

    public boolean isAsync() {
        return this.mep.isAsync;
    }

    void freeze(WSDLPort portType) {
        this.wsdlOperation = portType.getBinding().get(new QName(portType.getBinding().getPortType().getName().getNamespaceURI(), getOperationName()));
        if (this.wsdlOperation == null) {
            throw new WebServiceException("Method " + this.seiMethod.getName() + " is exposed as WebMethod, but there is no corresponding wsdl operation with name " + ((Object) this.operationName) + " in the wsdl:portType" + ((Object) portType.getBinding().getPortType().getName()));
        }
        if (this.inputAction.equals("")) {
            this.inputAction = this.wsdlOperation.getOperation().getInput().getAction();
        } else if (!this.inputAction.equals(this.wsdlOperation.getOperation().getInput().getAction())) {
            LOGGER.warning("Input Action on WSDL operation " + this.wsdlOperation.getName().getLocalPart() + " and @Action on its associated Web Method " + this.seiMethod.getName() + " did not match and will cause problems in dispatching the requests");
        }
        if (!this.mep.isOneWay()) {
            if (this.outputAction.equals("")) {
                this.outputAction = this.wsdlOperation.getOperation().getOutput().getAction();
            }
            for (CheckedExceptionImpl ce : this.exceptions) {
                if (ce.getFaultAction().equals("")) {
                    QName detailQName = ce.getDetailType().tagName;
                    WSDLFault wsdlfault = this.wsdlOperation.getOperation().getFault(detailQName);
                    if (wsdlfault == null) {
                        LOGGER.warning("Mismatch between Java model and WSDL model found, For wsdl operation " + ((Object) this.wsdlOperation.getName()) + ",There is no matching wsdl fault with detail QName " + ((Object) ce.getDetailType().tagName));
                        ce.setFaultAction(ce.getDefaultFaultAction());
                    } else {
                        ce.setFaultAction(wsdlfault.getAction());
                    }
                }
            }
        }
    }

    final void fillTypes(List<TypeInfo> types) {
        fillTypes(this.requestParams, types);
        fillTypes(this.responseParams, types);
        for (CheckedExceptionImpl ce : this.exceptions) {
            types.add(ce.getDetailType());
        }
    }

    private void fillTypes(List<ParameterImpl> params, List<TypeInfo> types) {
        for (ParameterImpl p2 : params) {
            p2.fillTypes(types);
        }
    }
}
