package com.sun.xml.internal.ws.api.pipe;

import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.util.pipe.StandalonePipeAssembler;
import java.util.Iterator;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/PipelineAssemblerFactory.class */
public abstract class PipelineAssemblerFactory {
    private static final Logger logger = Logger.getLogger(PipelineAssemblerFactory.class.getName());

    public abstract PipelineAssembler doCreate(BindingID bindingID);

    public static PipelineAssembler create(ClassLoader classLoader, BindingID bindingId) {
        Iterator it = ServiceFinder.find(PipelineAssemblerFactory.class, classLoader).iterator();
        while (it.hasNext()) {
            PipelineAssemblerFactory factory = (PipelineAssemblerFactory) it.next();
            PipelineAssembler assembler = factory.doCreate(bindingId);
            if (assembler != null) {
                logger.fine(((Object) factory.getClass()) + " successfully created " + ((Object) assembler));
                return assembler;
            }
        }
        return new StandalonePipeAssembler();
    }
}
