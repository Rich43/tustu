package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.internal.ws.resources.TubelineassemblyMessages;
import com.sun.xml.internal.ws.runtime.config.TubeFactoryConfig;
import com.sun.xml.internal.ws.runtime.config.TubeFactoryList;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/TubelineAssemblyController.class */
final class TubelineAssemblyController {
    private final MetroConfigName metroConfigName;

    TubelineAssemblyController(MetroConfigName metroConfigName) {
        this.metroConfigName = metroConfigName;
    }

    Collection<TubeCreator> getTubeCreators(ClientTubelineAssemblyContext context) {
        URI endpointUri;
        if (context.getPortInfo() != null) {
            endpointUri = createEndpointComponentUri(context.getPortInfo().getServiceName(), context.getPortInfo().getPortName());
        } else {
            endpointUri = null;
        }
        MetroConfigLoader configLoader = new MetroConfigLoader(context.getContainer(), this.metroConfigName);
        return initializeTubeCreators(configLoader.getClientSideTubeFactories(endpointUri));
    }

    Collection<TubeCreator> getTubeCreators(DefaultServerTubelineAssemblyContext context) {
        URI endpointUri;
        if (context.getEndpoint() != null) {
            endpointUri = createEndpointComponentUri(context.getEndpoint().getServiceName(), context.getEndpoint().getPortName());
        } else {
            endpointUri = null;
        }
        MetroConfigLoader configLoader = new MetroConfigLoader(context.getEndpoint().getContainer(), this.metroConfigName);
        return initializeTubeCreators(configLoader.getEndpointSideTubeFactories(endpointUri));
    }

    private Collection<TubeCreator> initializeTubeCreators(TubeFactoryList tfl) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        LinkedList<TubeCreator> tubeCreators = new LinkedList<>();
        for (TubeFactoryConfig tubeFactoryConfig : tfl.getTubeFactoryConfigs()) {
            tubeCreators.addFirst(new TubeCreator(tubeFactoryConfig, contextClassLoader));
        }
        return tubeCreators;
    }

    private URI createEndpointComponentUri(@NotNull QName serviceName, @NotNull QName portName) {
        StringBuilder sb = new StringBuilder(serviceName.getNamespaceURI()).append("#wsdl11.port(").append(serviceName.getLocalPart()).append('/').append(portName.getLocalPart()).append(')');
        try {
            return new URI(sb.toString());
        } catch (URISyntaxException ex) {
            Logger.getLogger(TubelineAssemblyController.class).warning(TubelineassemblyMessages.MASM_0020_ERROR_CREATING_URI_FROM_GENERATED_STRING(sb.toString()), ex);
            return null;
        }
    }
}
