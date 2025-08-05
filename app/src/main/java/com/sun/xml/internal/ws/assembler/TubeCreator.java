package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.internal.ws.assembler.dev.TubeFactory;
import com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyContextUpdater;
import com.sun.xml.internal.ws.resources.TubelineassemblyMessages;
import com.sun.xml.internal.ws.runtime.config.TubeFactoryConfig;
import com.sun.xml.internal.ws.util.Constants;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/TubeCreator.class */
final class TubeCreator {
    private static final Logger LOGGER = Logger.getLogger(TubeCreator.class);
    private final TubeFactory factory;
    private final String msgDumpPropertyBase;

    TubeCreator(TubeFactoryConfig config, ClassLoader tubeFactoryClassLoader) {
        Class<?> factoryClass;
        String className = config.getClassName();
        try {
            if (isJDKInternal(className)) {
                factoryClass = Class.forName(className, true, null);
            } else {
                factoryClass = Class.forName(className, true, tubeFactoryClassLoader);
            }
            if (TubeFactory.class.isAssignableFrom(factoryClass)) {
                this.factory = (TubeFactory) factoryClass.newInstance();
                this.msgDumpPropertyBase = this.factory.getClass().getName() + ".dump";
                return;
            }
            throw new RuntimeException(TubelineassemblyMessages.MASM_0015_CLASS_DOES_NOT_IMPLEMENT_INTERFACE(factoryClass.getName(), TubeFactory.class.getName()));
        } catch (ClassNotFoundException ex) {
            throw ((RuntimeException) LOGGER.logSevereException((Logger) new RuntimeException(TubelineassemblyMessages.MASM_0017_UNABLE_TO_LOAD_TUBE_FACTORY_CLASS(className), ex), true));
        } catch (IllegalAccessException ex2) {
            throw ((RuntimeException) LOGGER.logSevereException((Logger) new RuntimeException(TubelineassemblyMessages.MASM_0016_UNABLE_TO_INSTANTIATE_TUBE_FACTORY(className), ex2), true));
        } catch (InstantiationException ex3) {
            throw ((RuntimeException) LOGGER.logSevereException((Logger) new RuntimeException(TubelineassemblyMessages.MASM_0016_UNABLE_TO_INSTANTIATE_TUBE_FACTORY(className), ex3), true));
        }
    }

    Tube createTube(DefaultClientTubelineAssemblyContext context) {
        return this.factory.createTube(context);
    }

    Tube createTube(DefaultServerTubelineAssemblyContext context) {
        return this.factory.createTube(context);
    }

    void updateContext(ClientTubelineAssemblyContext context) throws WebServiceException {
        if (this.factory instanceof TubelineAssemblyContextUpdater) {
            ((TubelineAssemblyContextUpdater) this.factory).prepareContext(context);
        }
    }

    void updateContext(DefaultServerTubelineAssemblyContext context) throws WebServiceException {
        if (this.factory instanceof TubelineAssemblyContextUpdater) {
            ((TubelineAssemblyContextUpdater) this.factory).prepareContext(context);
        }
    }

    String getMessageDumpPropertyBase() {
        return this.msgDumpPropertyBase;
    }

    private boolean isJDKInternal(String className) {
        return className.startsWith(Constants.LoggingDomain);
    }
}
