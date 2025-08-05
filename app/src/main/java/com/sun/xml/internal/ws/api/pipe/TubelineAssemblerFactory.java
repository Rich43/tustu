package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.assembler.MetroTubelineAssembler;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/TubelineAssemblerFactory.class */
public abstract class TubelineAssemblerFactory {
    private static final Logger logger = Logger.getLogger(TubelineAssemblerFactory.class.getName());

    public abstract TubelineAssembler doCreate(BindingID bindingID);

    public static TubelineAssembler create(ClassLoader classLoader, BindingID bindingId) {
        return create(classLoader, bindingId, null);
    }

    public static TubelineAssembler create(ClassLoader classLoader, BindingID bindingId, @Nullable Container container) {
        TubelineAssemblerFactory taf;
        TubelineAssembler a2;
        if (container != null && (taf = (TubelineAssemblerFactory) container.getSPI(TubelineAssemblerFactory.class)) != null && (a2 = taf.doCreate(bindingId)) != null) {
            return a2;
        }
        Iterator it = ServiceFinder.find(TubelineAssemblerFactory.class, classLoader).iterator();
        while (it.hasNext()) {
            TubelineAssemblerFactory factory = (TubelineAssemblerFactory) it.next();
            TubelineAssembler assembler = factory.doCreate(bindingId);
            if (assembler != null) {
                logger.log(Level.FINE, "{0} successfully created {1}", new Object[]{factory.getClass(), assembler});
                return assembler;
            }
        }
        Iterator it2 = ServiceFinder.find(PipelineAssemblerFactory.class, classLoader).iterator();
        while (it2.hasNext()) {
            PipelineAssemblerFactory factory2 = (PipelineAssemblerFactory) it2.next();
            PipelineAssembler assembler2 = factory2.doCreate(bindingId);
            if (assembler2 != null) {
                logger.log(Level.FINE, "{0} successfully created {1}", new Object[]{factory2.getClass(), assembler2});
                return new TubelineAssemblerAdapter(assembler2);
            }
        }
        return new MetroTubelineAssembler(bindingId, MetroTubelineAssembler.JAXWS_TUBES_CONFIG_NAMES);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/TubelineAssemblerFactory$TubelineAssemblerAdapter.class */
    private static class TubelineAssemblerAdapter implements TubelineAssembler {
        private PipelineAssembler assembler;

        TubelineAssemblerAdapter(PipelineAssembler assembler) {
            this.assembler = assembler;
        }

        @Override // com.sun.xml.internal.ws.api.pipe.TubelineAssembler
        @NotNull
        public Tube createClient(@NotNull ClientTubeAssemblerContext context) {
            ClientPipeAssemblerContext ctxt = new ClientPipeAssemblerContext(context.getAddress(), context.getWsdlModel(), context.getService(), context.getBinding(), context.getContainer());
            return PipeAdapter.adapt(this.assembler.createClient(ctxt));
        }

        @Override // com.sun.xml.internal.ws.api.pipe.TubelineAssembler
        @NotNull
        public Tube createServer(@NotNull ServerTubeAssemblerContext context) {
            if (!(context instanceof ServerPipeAssemblerContext)) {
                throw new IllegalArgumentException("{0} is not instance of ServerPipeAssemblerContext");
            }
            return PipeAdapter.adapt(this.assembler.createServer((ServerPipeAssemblerContext) context));
        }
    }
}
