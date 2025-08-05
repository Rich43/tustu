package com.sun.xml.internal.ws.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.handler.HandlerChainsModel;
import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
import com.sun.xml.internal.ws.util.HandlerAnnotationProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.soap.SOAPBinding;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/HandlerConfigurator.class */
abstract class HandlerConfigurator {
    abstract void configureHandlers(@NotNull WSPortInfo wSPortInfo, @NotNull BindingImpl bindingImpl);

    abstract HandlerResolver getResolver();

    HandlerConfigurator() {
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/HandlerConfigurator$HandlerResolverImpl.class */
    static final class HandlerResolverImpl extends HandlerConfigurator {

        @Nullable
        private final HandlerResolver resolver;

        public HandlerResolverImpl(HandlerResolver resolver) {
            this.resolver = resolver;
        }

        @Override // com.sun.xml.internal.ws.client.HandlerConfigurator
        void configureHandlers(@NotNull WSPortInfo port, @NotNull BindingImpl binding) {
            if (this.resolver != null) {
                binding.setHandlerChain(this.resolver.getHandlerChain(port));
            }
        }

        @Override // com.sun.xml.internal.ws.client.HandlerConfigurator
        HandlerResolver getResolver() {
            return this.resolver;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/HandlerConfigurator$AnnotationConfigurator.class */
    static final class AnnotationConfigurator extends HandlerConfigurator {
        private final HandlerChainsModel handlerModel;
        private final Map<WSPortInfo, HandlerAnnotationInfo> chainMap = new HashMap();
        private static final Logger logger;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !HandlerConfigurator.class.desiredAssertionStatus();
            logger = Logger.getLogger("com.sun.xml.internal.ws.handler");
        }

        AnnotationConfigurator(WSServiceDelegate delegate) {
            this.handlerModel = HandlerAnnotationProcessor.buildHandlerChainsModel(delegate.getServiceClass());
            if (!$assertionsDisabled && this.handlerModel == null) {
                throw new AssertionError();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.ws.client.HandlerConfigurator
        void configureHandlers(WSPortInfo port, BindingImpl bindingImpl) {
            HandlerAnnotationInfo chain = this.chainMap.get(port);
            if (chain == null) {
                logGetChain(port);
                chain = this.handlerModel.getHandlersForPortInfo(port);
                this.chainMap.put(port, chain);
            }
            if (bindingImpl instanceof SOAPBinding) {
                ((SOAPBinding) bindingImpl).setRoles(chain.getRoles());
            }
            logSetChain(port, chain);
            bindingImpl.setHandlerChain(chain.getHandlers());
        }

        @Override // com.sun.xml.internal.ws.client.HandlerConfigurator
        HandlerResolver getResolver() {
            return new HandlerResolver() { // from class: com.sun.xml.internal.ws.client.HandlerConfigurator.AnnotationConfigurator.1
                @Override // javax.xml.ws.handler.HandlerResolver
                public List<Handler> getHandlerChain(javax.xml.ws.handler.PortInfo portInfo) {
                    return new ArrayList(AnnotationConfigurator.this.handlerModel.getHandlersForPortInfo(portInfo).getHandlers());
                }
            };
        }

        private void logSetChain(WSPortInfo info, HandlerAnnotationInfo chain) {
            logger.finer("Setting chain of length " + chain.getHandlers().size() + " for port info");
            logPortInfo(info, Level.FINER);
        }

        private void logGetChain(WSPortInfo info) {
            logger.fine("No handler chain found for port info:");
            logPortInfo(info, Level.FINE);
            logger.fine("Existing handler chains:");
            if (this.chainMap.isEmpty()) {
                logger.fine(Separation.COLORANT_NONE);
                return;
            }
            for (WSPortInfo key : this.chainMap.keySet()) {
                logger.fine(this.chainMap.get(key).getHandlers().size() + " handlers for port info ");
                logPortInfo(key, Level.FINE);
            }
        }

        private void logPortInfo(WSPortInfo info, Level level) {
            logger.log(level, "binding: " + info.getBindingID() + "\nservice: " + ((Object) info.getServiceName()) + "\nport: " + ((Object) info.getPortName()));
        }
    }
}
