package com.sun.xml.internal.ws.server.provider;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.server.AsyncProvider;
import com.sun.xml.internal.ws.resources.ServerMessages;
import com.sun.xml.internal.ws.spi.db.BindingHelper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.activation.DataSource;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/provider/ProviderEndpointModel.class */
final class ProviderEndpointModel<T> {
    final boolean isAsync;

    @NotNull
    final Service.Mode mode;

    @NotNull
    final Class datatype;

    @NotNull
    final Class implClass;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ProviderEndpointModel.class.desiredAssertionStatus();
    }

    ProviderEndpointModel(Class<T> implementorClass, WSBinding binding) {
        if (!$assertionsDisabled && implementorClass == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && binding == null) {
            throw new AssertionError();
        }
        this.implClass = implementorClass;
        this.mode = getServiceMode(implementorClass);
        Class otherClass = binding instanceof SOAPBinding ? SOAPMessage.class : DataSource.class;
        this.isAsync = AsyncProvider.class.isAssignableFrom(implementorClass);
        Class<? extends Object> baseType = this.isAsync ? AsyncProvider.class : Provider.class;
        Type baseParam = BindingHelper.getBaseType(implementorClass, baseType);
        if (baseParam == null) {
            throw new WebServiceException(ServerMessages.NOT_IMPLEMENT_PROVIDER(implementorClass.getName()));
        }
        if (!(baseParam instanceof ParameterizedType)) {
            throw new WebServiceException(ServerMessages.PROVIDER_NOT_PARAMETERIZED(implementorClass.getName()));
        }
        ParameterizedType pt = (ParameterizedType) baseParam;
        Type[] types = pt.getActualTypeArguments();
        if (!(types[0] instanceof Class)) {
            throw new WebServiceException(ServerMessages.PROVIDER_INVALID_PARAMETER_TYPE(implementorClass.getName(), types[0]));
        }
        this.datatype = (Class) types[0];
        if (this.mode == Service.Mode.PAYLOAD && this.datatype != Source.class) {
            throw new IllegalArgumentException("Illeagal combination - Mode.PAYLOAD and Provider<" + otherClass.getName() + ">");
        }
    }

    private static Service.Mode getServiceMode(Class<?> c2) {
        ServiceMode mode = (ServiceMode) c2.getAnnotation(ServiceMode.class);
        return mode == null ? Service.Mode.PAYLOAD : mode.value();
    }
}
