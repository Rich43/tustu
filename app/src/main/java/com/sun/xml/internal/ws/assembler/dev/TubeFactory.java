package com.sun.xml.internal.ws.assembler.dev;

import com.sun.xml.internal.ws.api.pipe.Tube;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/dev/TubeFactory.class */
public interface TubeFactory {
    Tube createTube(ClientTubelineAssemblyContext clientTubelineAssemblyContext) throws WebServiceException;

    Tube createTube(ServerTubelineAssemblyContext serverTubelineAssemblyContext) throws WebServiceException;
}
