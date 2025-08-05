package com.sun.xml.internal.ws.binding;

import com.oracle.webservices.internal.api.EnvelopeStyleFeature;
import com.oracle.webservices.internal.api.message.MessageContextFactory;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import com.sun.xml.internal.ws.developer.BindingTypeFeature;
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.CommandInfo;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.AddressingFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/binding/BindingImpl.class */
public abstract class BindingImpl implements WSBinding {
    protected static final WebServiceFeature[] EMPTY_FEATURES = new WebServiceFeature[0];
    private final BindingID bindingId;
    protected final WebServiceFeatureList features;
    protected MessageContextFactory messageContextFactory;
    private final Set<QName> addedHeaders = new HashSet();
    private final Set<QName> knownHeaders = new HashSet();
    private final Set<QName> unmodKnownHeaders = Collections.unmodifiableSet(this.knownHeaders);
    protected final Map<QName, WebServiceFeatureList> operationFeatures = new HashMap();
    protected final Map<QName, WebServiceFeatureList> inputMessageFeatures = new HashMap();
    protected final Map<QName, WebServiceFeatureList> outputMessageFeatures = new HashMap();
    protected final Map<MessageKey, WebServiceFeatureList> faultMessageFeatures = new HashMap();
    protected Service.Mode serviceMode = Service.Mode.PAYLOAD;
    private HandlerConfiguration handlerConfig = new HandlerConfiguration((Set<String>) Collections.emptySet(), (List<Handler>) Collections.emptyList());

    protected BindingImpl(BindingID bindingId, WebServiceFeature... features) {
        this.bindingId = bindingId;
        if (this.handlerConfig.getHandlerKnownHeaders() != null) {
            this.knownHeaders.addAll(this.handlerConfig.getHandlerKnownHeaders());
        }
        this.features = new WebServiceFeatureList(features);
        this.features.validate();
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding, javax.xml.ws.Binding
    @NotNull
    public List<Handler> getHandlerChain() {
        return this.handlerConfig.getHandlerChain();
    }

    public HandlerConfiguration getHandlerConfig() {
        return this.handlerConfig;
    }

    protected void setHandlerConfig(HandlerConfiguration handlerConfig) {
        this.handlerConfig = handlerConfig;
        this.knownHeaders.clear();
        this.knownHeaders.addAll(this.addedHeaders);
        if (handlerConfig != null && handlerConfig.getHandlerKnownHeaders() != null) {
            this.knownHeaders.addAll(handlerConfig.getHandlerKnownHeaders());
        }
    }

    public void setMode(@NotNull Service.Mode mode) {
        this.serviceMode = mode;
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    public Set<QName> getKnownHeaders() {
        return this.unmodKnownHeaders;
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    public boolean addKnownHeader(QName headerQName) {
        this.addedHeaders.add(headerQName);
        return this.knownHeaders.add(headerQName);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public BindingID getBindingId() {
        return this.bindingId;
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    public final SOAPVersion getSOAPVersion() {
        return this.bindingId.getSOAPVersion();
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    public AddressingVersion getAddressingVersion() {
        AddressingVersion addressingVersion;
        if (this.features.isEnabled(AddressingFeature.class)) {
            addressingVersion = AddressingVersion.W3C;
        } else if (this.features.isEnabled(MemberSubmissionAddressingFeature.class)) {
            addressingVersion = AddressingVersion.MEMBER;
        } else {
            addressingVersion = null;
        }
        return addressingVersion;
    }

    @NotNull
    public final Codec createCodec() {
        initializeJavaActivationHandlers();
        return this.bindingId.createEncoder(this);
    }

    public static void initializeJavaActivationHandlers() {
        try {
            CommandMap map = CommandMap.getDefaultCommandMap();
            if (map instanceof MailcapCommandMap) {
                MailcapCommandMap mailMap = (MailcapCommandMap) map;
                if (!cmdMapInitialized(mailMap)) {
                    mailMap.addMailcap("text/xml;;x-java-content-handler=com.sun.xml.internal.ws.encoding.XmlDataContentHandler");
                    mailMap.addMailcap("application/xml;;x-java-content-handler=com.sun.xml.internal.ws.encoding.XmlDataContentHandler");
                    mailMap.addMailcap("image/*;;x-java-content-handler=com.sun.xml.internal.ws.encoding.ImageDataContentHandler");
                    mailMap.addMailcap("text/plain;;x-java-content-handler=com.sun.xml.internal.ws.encoding.StringDataContentHandler");
                }
            }
        } catch (Throwable th) {
        }
    }

    private static boolean cmdMapInitialized(MailcapCommandMap mailMap) {
        CommandInfo[] commands = mailMap.getAllCommands("text/xml");
        if (commands == null || commands.length == 0) {
            return false;
        }
        for (CommandInfo command : commands) {
            String commandClass = command.getCommandClass();
            if ("com.sun.xml.internal.messaging.saaj.soap.XmlDataContentHandler".equals(commandClass) || "com.sun.xml.internal.ws.encoding.XmlDataContentHandler".equals(commandClass)) {
                return true;
            }
        }
        return false;
    }

    public static BindingImpl create(@NotNull BindingID bindingId) {
        if (bindingId.equals(BindingID.XML_HTTP)) {
            return new HTTPBindingImpl();
        }
        return new SOAPBindingImpl(bindingId);
    }

    public static BindingImpl create(@NotNull BindingID bindingId, WebServiceFeature[] features) throws WebServiceException {
        for (WebServiceFeature feature : features) {
            if (feature instanceof BindingTypeFeature) {
                BindingTypeFeature f2 = (BindingTypeFeature) feature;
                bindingId = BindingID.parse(f2.getBindingId());
            }
        }
        if (bindingId.equals(BindingID.XML_HTTP)) {
            return new HTTPBindingImpl(features);
        }
        return new SOAPBindingImpl(bindingId, features);
    }

    public static WSBinding getDefaultBinding() {
        return new SOAPBindingImpl(BindingID.SOAP11_HTTP);
    }

    @Override // javax.xml.ws.Binding
    public String getBindingID() {
        return this.bindingId.toString();
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @Nullable
    public <F extends WebServiceFeature> F getFeature(@NotNull Class<F> cls) {
        return (F) this.features.get((Class) cls);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @Nullable
    public <F extends WebServiceFeature> F getOperationFeature(@NotNull Class<F> cls, @NotNull QName qName) {
        return (F) FeatureListUtil.mergeFeature(cls, this.operationFeatures.get(qName), this.features);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    public boolean isFeatureEnabled(@NotNull Class<? extends WebServiceFeature> feature) {
        return this.features.isEnabled(feature);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    public boolean isOperationFeatureEnabled(@NotNull Class<? extends WebServiceFeature> featureType, @NotNull QName operationName) {
        WebServiceFeatureList operationFeatureList = this.operationFeatures.get(operationName);
        return FeatureListUtil.isFeatureEnabled(featureType, operationFeatureList, this.features);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public WebServiceFeatureList getFeatures() {
        if (!isFeatureEnabled(EnvelopeStyleFeature.class)) {
            WebServiceFeature[] f2 = {getSOAPVersion().toFeature()};
            this.features.mergeFeatures(f2, false);
        }
        return this.features;
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public WebServiceFeatureList getOperationFeatures(@NotNull QName operationName) {
        WebServiceFeatureList operationFeatureList = this.operationFeatures.get(operationName);
        return FeatureListUtil.mergeList(operationFeatureList, this.features);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public WebServiceFeatureList getInputMessageFeatures(@NotNull QName operationName) {
        WebServiceFeatureList operationFeatureList = this.operationFeatures.get(operationName);
        WebServiceFeatureList messageFeatureList = this.inputMessageFeatures.get(operationName);
        return FeatureListUtil.mergeList(operationFeatureList, messageFeatureList, this.features);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public WebServiceFeatureList getOutputMessageFeatures(@NotNull QName operationName) {
        WebServiceFeatureList operationFeatureList = this.operationFeatures.get(operationName);
        WebServiceFeatureList messageFeatureList = this.outputMessageFeatures.get(operationName);
        return FeatureListUtil.mergeList(operationFeatureList, messageFeatureList, this.features);
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public WebServiceFeatureList getFaultMessageFeatures(@NotNull QName operationName, @NotNull QName messageName) {
        WebServiceFeatureList operationFeatureList = this.operationFeatures.get(operationName);
        WebServiceFeatureList messageFeatureList = this.faultMessageFeatures.get(new MessageKey(operationName, messageName));
        return FeatureListUtil.mergeList(operationFeatureList, messageFeatureList, this.features);
    }

    public void setOperationFeatures(@NotNull QName operationName, WebServiceFeature... newFeatures) {
        if (newFeatures != null) {
            WebServiceFeatureList featureList = this.operationFeatures.get(operationName);
            if (featureList == null) {
                featureList = new WebServiceFeatureList();
            }
            for (WebServiceFeature f2 : newFeatures) {
                featureList.add(f2);
            }
            this.operationFeatures.put(operationName, featureList);
        }
    }

    public void setInputMessageFeatures(@NotNull QName operationName, WebServiceFeature... newFeatures) {
        if (newFeatures != null) {
            WebServiceFeatureList featureList = this.inputMessageFeatures.get(operationName);
            if (featureList == null) {
                featureList = new WebServiceFeatureList();
            }
            for (WebServiceFeature f2 : newFeatures) {
                featureList.add(f2);
            }
            this.inputMessageFeatures.put(operationName, featureList);
        }
    }

    public void setOutputMessageFeatures(@NotNull QName operationName, WebServiceFeature... newFeatures) {
        if (newFeatures != null) {
            WebServiceFeatureList featureList = this.outputMessageFeatures.get(operationName);
            if (featureList == null) {
                featureList = new WebServiceFeatureList();
            }
            for (WebServiceFeature f2 : newFeatures) {
                featureList.add(f2);
            }
            this.outputMessageFeatures.put(operationName, featureList);
        }
    }

    public void setFaultMessageFeatures(@NotNull QName operationName, @NotNull QName messageName, WebServiceFeature... newFeatures) {
        if (newFeatures != null) {
            MessageKey key = new MessageKey(operationName, messageName);
            WebServiceFeatureList featureList = this.faultMessageFeatures.get(key);
            if (featureList == null) {
                featureList = new WebServiceFeatureList();
            }
            for (WebServiceFeature f2 : newFeatures) {
                featureList.add(f2);
            }
            this.faultMessageFeatures.put(key, featureList);
        }
    }

    @Override // com.sun.xml.internal.ws.api.WSBinding
    @NotNull
    public synchronized MessageContextFactory getMessageContextFactory() {
        if (this.messageContextFactory == null) {
            this.messageContextFactory = MessageContextFactory.createFactory(getFeatures().toArray());
        }
        return this.messageContextFactory;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/binding/BindingImpl$MessageKey.class */
    protected static class MessageKey {
        private final QName operationName;
        private final QName messageName;

        public MessageKey(QName operationName, QName messageName) {
            this.operationName = operationName;
            this.messageName = messageName;
        }

        public int hashCode() {
            int hashFirst = this.operationName != null ? this.operationName.hashCode() : 0;
            int hashSecond = this.messageName != null ? this.messageName.hashCode() : 0;
            return ((hashFirst + hashSecond) * hashSecond) + hashFirst;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MessageKey other = (MessageKey) obj;
            if (this.operationName != other.operationName && (this.operationName == null || !this.operationName.equals(other.operationName))) {
                return false;
            }
            if (this.messageName == other.messageName) {
                return true;
            }
            if (this.messageName == null || !this.messageName.equals(other.messageName)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "(" + ((Object) this.operationName) + ", " + ((Object) this.messageName) + ")";
        }
    }
}
