package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import java.util.Map;
import javax.jws.WebParam;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLBoundOperation.class */
public interface WSDLBoundOperation extends WSDLObject, WSDLExtensible {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLBoundOperation$ANONYMOUS.class */
    public enum ANONYMOUS {
        optional,
        required,
        prohibited
    }

    @NotNull
    QName getName();

    @NotNull
    String getSOAPAction();

    @NotNull
    WSDLOperation getOperation();

    @NotNull
    WSDLBoundPortType getBoundPortType();

    ANONYMOUS getAnonymous();

    @Nullable
    WSDLPart getPart(@NotNull String str, @NotNull WebParam.Mode mode);

    ParameterBinding getInputBinding(String str);

    ParameterBinding getOutputBinding(String str);

    ParameterBinding getFaultBinding(String str);

    String getMimeTypeForInputPart(String str);

    String getMimeTypeForOutputPart(String str);

    String getMimeTypeForFaultPart(String str);

    @NotNull
    Map<String, ? extends WSDLPart> getInParts();

    @NotNull
    Map<String, ? extends WSDLPart> getOutParts();

    @NotNull
    Iterable<? extends WSDLBoundFault> getFaults();

    Map<String, ParameterBinding> getInputParts();

    Map<String, ParameterBinding> getOutputParts();

    Map<String, ParameterBinding> getFaultParts();

    @Nullable
    QName getRequestPayloadName();

    @Nullable
    QName getResponsePayloadName();

    String getRequestNamespace();

    String getResponseNamespace();
}
