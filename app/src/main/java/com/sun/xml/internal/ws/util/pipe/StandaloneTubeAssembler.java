package com.sun.xml.internal.ws.util.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
import com.sun.xml.internal.ws.policy.PolicyConstants;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/pipe/StandaloneTubeAssembler.class */
public class StandaloneTubeAssembler implements TubelineAssembler {
    public static final boolean dump;

    @Override // com.sun.xml.internal.ws.api.pipe.TubelineAssembler
    @NotNull
    public Tube createClient(ClientTubeAssemblerContext context) {
        Tube head = context.createSecurityTube(context.createTransportTube());
        if (dump) {
            head = context.createDumpTube(PolicyConstants.CLIENT_CONFIGURATION_IDENTIFIER, System.out, head);
        }
        return context.createHandlerTube(context.createValidationTube(context.createClientMUTube(context.createWsaTube(head))));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.TubelineAssembler
    public Tube createServer(ServerTubeAssemblerContext context) {
        Tube head = context.createWsaTube(context.createServerMUTube(context.createMonitoringTube(context.createHandlerTube(context.createValidationTube(context.getTerminalTube())))));
        if (dump) {
            head = context.createDumpTube("server", System.out, head);
        }
        return context.createSecurityTube(head);
    }

    static {
        boolean b2 = false;
        try {
            b2 = Boolean.getBoolean(StandaloneTubeAssembler.class.getName() + ".dump");
        } catch (Throwable th) {
        }
        dump = b2;
    }
}
