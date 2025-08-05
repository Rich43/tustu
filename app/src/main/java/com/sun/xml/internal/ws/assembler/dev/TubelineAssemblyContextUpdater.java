package com.sun.xml.internal.ws.assembler.dev;

import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/dev/TubelineAssemblyContextUpdater.class */
public interface TubelineAssemblyContextUpdater {
    void prepareContext(ClientTubelineAssemblyContext clientTubelineAssemblyContext) throws WebServiceException;

    void prepareContext(ServerTubelineAssemblyContext serverTubelineAssemblyContext) throws WebServiceException;
}
