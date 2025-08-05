package com.sun.xml.internal.ws.assembler.jaxws;

import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.internal.ws.assembler.dev.TubeFactory;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/jaxws/AddressingTubeFactory.class */
public final class AddressingTubeFactory implements TubeFactory {
    @Override // com.sun.xml.internal.ws.assembler.dev.TubeFactory
    public Tube createTube(ClientTubelineAssemblyContext context) throws WebServiceException {
        return context.getWrappedContext().createWsaTube(context.getTubelineHead());
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.TubeFactory
    public Tube createTube(ServerTubelineAssemblyContext context) throws WebServiceException {
        return context.getWrappedContext().createWsaTube(context.getTubelineHead());
    }
}
