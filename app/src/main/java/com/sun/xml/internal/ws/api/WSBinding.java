package com.sun.xml.internal.ws.api;

import com.oracle.webservices.internal.api.message.MessageContextFactory;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.Handler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/WSBinding.class */
public interface WSBinding extends Binding {
    SOAPVersion getSOAPVersion();

    AddressingVersion getAddressingVersion();

    @NotNull
    BindingID getBindingId();

    @Override // javax.xml.ws.Binding
    @NotNull
    List<Handler> getHandlerChain();

    boolean isFeatureEnabled(@NotNull Class<? extends WebServiceFeature> cls);

    boolean isOperationFeatureEnabled(@NotNull Class<? extends WebServiceFeature> cls, @NotNull QName qName);

    @Nullable
    <F extends WebServiceFeature> F getFeature(@NotNull Class<F> cls);

    @Nullable
    <F extends WebServiceFeature> F getOperationFeature(@NotNull Class<F> cls, @NotNull QName qName);

    @NotNull
    WSFeatureList getFeatures();

    @NotNull
    WSFeatureList getOperationFeatures(@NotNull QName qName);

    @NotNull
    WSFeatureList getInputMessageFeatures(@NotNull QName qName);

    @NotNull
    WSFeatureList getOutputMessageFeatures(@NotNull QName qName);

    @NotNull
    WSFeatureList getFaultMessageFeatures(@NotNull QName qName, @NotNull QName qName2);

    @NotNull
    Set<QName> getKnownHeaders();

    boolean addKnownHeader(QName qName);

    @NotNull
    MessageContextFactory getMessageContextFactory();
}
