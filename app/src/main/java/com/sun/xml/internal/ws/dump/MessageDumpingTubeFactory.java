package com.sun.xml.internal.ws.dump;

import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.internal.ws.assembler.dev.TubeFactory;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/dump/MessageDumpingTubeFactory.class */
public final class MessageDumpingTubeFactory implements TubeFactory {
    @Override // com.sun.xml.internal.ws.assembler.dev.TubeFactory
    public Tube createTube(ClientTubelineAssemblyContext context) throws WebServiceException {
        MessageDumpingFeature messageDumpingFeature = (MessageDumpingFeature) context.getBinding().getFeature(MessageDumpingFeature.class);
        if (messageDumpingFeature != null) {
            return new MessageDumpingTube(context.getTubelineHead(), messageDumpingFeature);
        }
        return context.getTubelineHead();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.TubeFactory
    public Tube createTube(ServerTubelineAssemblyContext context) throws WebServiceException {
        MessageDumpingFeature messageDumpingFeature = (MessageDumpingFeature) context.getEndpoint().getBinding().getFeature(MessageDumpingFeature.class);
        if (messageDumpingFeature != null) {
            return new MessageDumpingTube(context.getTubelineHead(), messageDumpingFeature);
        }
        return context.getTubelineHead();
    }
}
