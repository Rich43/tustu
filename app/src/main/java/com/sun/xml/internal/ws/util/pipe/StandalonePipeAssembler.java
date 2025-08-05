package com.sun.xml.internal.ws.util.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.pipe.ClientPipeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Pipe;
import com.sun.xml.internal.ws.api.pipe.PipelineAssembler;
import com.sun.xml.internal.ws.api.pipe.ServerPipeAssemblerContext;
import com.sun.xml.internal.ws.policy.PolicyConstants;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/pipe/StandalonePipeAssembler.class */
public class StandalonePipeAssembler implements PipelineAssembler {
    private static final boolean dump;

    @Override // com.sun.xml.internal.ws.api.pipe.PipelineAssembler
    @NotNull
    public Pipe createClient(ClientPipeAssemblerContext context) {
        Pipe head = context.createSecurityPipe(context.createTransportPipe());
        if (dump) {
            head = context.createDumpPipe(PolicyConstants.CLIENT_CONFIGURATION_IDENTIFIER, System.out, head);
        }
        return context.createHandlerPipe(context.createClientMUPipe(context.createWsaPipe(head)));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.PipelineAssembler
    public Pipe createServer(ServerPipeAssemblerContext context) {
        Pipe head = context.getTerminalPipe();
        return context.createSecurityPipe(context.createWsaPipe(context.createServerMUPipe(context.createMonitoringPipe(context.createHandlerPipe(head)))));
    }

    static {
        boolean b2 = false;
        try {
            b2 = Boolean.getBoolean(StandalonePipeAssembler.class.getName() + ".dump");
        } catch (Throwable th) {
        }
        dump = b2;
    }
}
