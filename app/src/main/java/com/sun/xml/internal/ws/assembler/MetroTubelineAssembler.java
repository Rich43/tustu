package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
import com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator;
import com.sun.xml.internal.ws.dump.LoggingDumpTube;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.resources.TubelineassemblyMessages;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroTubelineAssembler.class */
public class MetroTubelineAssembler implements TubelineAssembler {
    private static final String COMMON_MESSAGE_DUMP_SYSTEM_PROPERTY_BASE = "com.sun.metro.soap.dump";
    public static final MetroConfigNameImpl JAXWS_TUBES_CONFIG_NAMES = new MetroConfigNameImpl("jaxws-tubes-default.xml", "jaxws-tubes.xml");
    private static final Logger LOGGER = Logger.getLogger(MetroTubelineAssembler.class);
    private final BindingID bindingId;
    private final TubelineAssemblyController tubelineAssemblyController;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroTubelineAssembler$Side.class */
    private enum Side {
        Client(PolicyConstants.CLIENT_CONFIGURATION_IDENTIFIER),
        Endpoint("endpoint");

        private final String name;

        Side(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/MetroTubelineAssembler$MessageDumpingInfo.class */
    private static class MessageDumpingInfo {
        final boolean dumpBefore;
        final boolean dumpAfter;
        final Level logLevel;

        MessageDumpingInfo(boolean dumpBefore, boolean dumpAfter, Level logLevel) {
            this.dumpBefore = dumpBefore;
            this.dumpAfter = dumpAfter;
            this.logLevel = logLevel;
        }
    }

    public MetroTubelineAssembler(BindingID bindingId, MetroConfigName metroConfigName) {
        this.bindingId = bindingId;
        this.tubelineAssemblyController = new TubelineAssemblyController(metroConfigName);
    }

    TubelineAssemblyController getTubelineAssemblyController() {
        return this.tubelineAssemblyController;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.TubelineAssembler
    @NotNull
    public Tube createClient(@NotNull ClientTubeAssemblerContext jaxwsContext) throws WebServiceException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Assembling client-side tubeline for WS endpoint: " + jaxwsContext.getAddress().getURI().toString());
        }
        DefaultClientTubelineAssemblyContext context = createClientContext(jaxwsContext);
        Collection<TubeCreator> tubeCreators = this.tubelineAssemblyController.getTubeCreators(context);
        Iterator<TubeCreator> it = tubeCreators.iterator();
        while (it.hasNext()) {
            it.next().updateContext(context);
        }
        TubelineAssemblyDecorator decorator = TubelineAssemblyDecorator.composite(ServiceFinder.find(TubelineAssemblyDecorator.class, context.getContainer()));
        boolean first = true;
        for (TubeCreator tubeCreator : tubeCreators) {
            MessageDumpingInfo msgDumpInfo = setupMessageDumping(tubeCreator.getMessageDumpPropertyBase(), Side.Client);
            Tube oldTubelineHead = context.getTubelineHead();
            LoggingDumpTube afterDumpTube = null;
            if (msgDumpInfo.dumpAfter) {
                afterDumpTube = new LoggingDumpTube(msgDumpInfo.logLevel, LoggingDumpTube.Position.After, context.getTubelineHead());
                context.setTubelineHead(afterDumpTube);
            }
            if (!context.setTubelineHead(decorator.decorateClient(tubeCreator.createTube(context), context))) {
                if (afterDumpTube != null) {
                    context.setTubelineHead(oldTubelineHead);
                }
            } else {
                String loggedTubeName = context.getTubelineHead().getClass().getName();
                if (afterDumpTube != null) {
                    afterDumpTube.setLoggedTubeName(loggedTubeName);
                }
                if (msgDumpInfo.dumpBefore) {
                    LoggingDumpTube beforeDumpTube = new LoggingDumpTube(msgDumpInfo.logLevel, LoggingDumpTube.Position.Before, context.getTubelineHead());
                    beforeDumpTube.setLoggedTubeName(loggedTubeName);
                    context.setTubelineHead(beforeDumpTube);
                }
            }
            if (first) {
                context.setTubelineHead(decorator.decorateClientTail(context.getTubelineHead(), context));
                first = false;
            }
        }
        return decorator.decorateClientHead(context.getTubelineHead(), context);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.TubelineAssembler
    @NotNull
    public Tube createServer(@NotNull ServerTubeAssemblerContext jaxwsContext) throws WebServiceException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Assembling endpoint tubeline for WS endpoint: " + ((Object) jaxwsContext.getEndpoint().getServiceName()) + "::" + ((Object) jaxwsContext.getEndpoint().getPortName()));
        }
        DefaultServerTubelineAssemblyContext context = createServerContext(jaxwsContext);
        Collection<TubeCreator> tubeCreators = this.tubelineAssemblyController.getTubeCreators(context);
        Iterator<TubeCreator> it = tubeCreators.iterator();
        while (it.hasNext()) {
            it.next().updateContext(context);
        }
        TubelineAssemblyDecorator decorator = TubelineAssemblyDecorator.composite(ServiceFinder.find(TubelineAssemblyDecorator.class, context.getEndpoint().getContainer()));
        boolean first = true;
        for (TubeCreator tubeCreator : tubeCreators) {
            MessageDumpingInfo msgDumpInfo = setupMessageDumping(tubeCreator.getMessageDumpPropertyBase(), Side.Endpoint);
            Tube oldTubelineHead = context.getTubelineHead();
            LoggingDumpTube afterDumpTube = null;
            if (msgDumpInfo.dumpAfter) {
                afterDumpTube = new LoggingDumpTube(msgDumpInfo.logLevel, LoggingDumpTube.Position.After, context.getTubelineHead());
                context.setTubelineHead(afterDumpTube);
            }
            if (!context.setTubelineHead(decorator.decorateServer(tubeCreator.createTube(context), context))) {
                if (afterDumpTube != null) {
                    context.setTubelineHead(oldTubelineHead);
                }
            } else {
                String loggedTubeName = context.getTubelineHead().getClass().getName();
                if (afterDumpTube != null) {
                    afterDumpTube.setLoggedTubeName(loggedTubeName);
                }
                if (msgDumpInfo.dumpBefore) {
                    LoggingDumpTube beforeDumpTube = new LoggingDumpTube(msgDumpInfo.logLevel, LoggingDumpTube.Position.Before, context.getTubelineHead());
                    beforeDumpTube.setLoggedTubeName(loggedTubeName);
                    context.setTubelineHead(beforeDumpTube);
                }
            }
            if (first) {
                context.setTubelineHead(decorator.decorateServerTail(context.getTubelineHead(), context));
                first = false;
            }
        }
        return decorator.decorateServerHead(context.getTubelineHead(), context);
    }

    private MessageDumpingInfo setupMessageDumping(String msgDumpSystemPropertyBase, Side side) {
        boolean dumpBefore = false;
        boolean dumpAfter = false;
        Level logLevel = Level.INFO;
        Boolean value = getBooleanValue(COMMON_MESSAGE_DUMP_SYSTEM_PROPERTY_BASE);
        if (value != null) {
            dumpBefore = value.booleanValue();
            dumpAfter = value.booleanValue();
        }
        Boolean value2 = getBooleanValue("com.sun.metro.soap.dump.before");
        boolean dumpBefore2 = value2 != null ? value2.booleanValue() : dumpBefore;
        Boolean value3 = getBooleanValue("com.sun.metro.soap.dump.after");
        boolean dumpAfter2 = value3 != null ? value3.booleanValue() : dumpAfter;
        Level levelValue = getLevelValue("com.sun.metro.soap.dump.level");
        if (levelValue != null) {
            logLevel = levelValue;
        }
        Boolean value4 = getBooleanValue("com.sun.metro.soap.dump." + side.toString());
        if (value4 != null) {
            dumpBefore2 = value4.booleanValue();
            dumpAfter2 = value4.booleanValue();
        }
        Boolean value5 = getBooleanValue("com.sun.metro.soap.dump." + side.toString() + ".before");
        boolean dumpBefore3 = value5 != null ? value5.booleanValue() : dumpBefore2;
        Boolean value6 = getBooleanValue("com.sun.metro.soap.dump." + side.toString() + ".after");
        boolean dumpAfter3 = value6 != null ? value6.booleanValue() : dumpAfter2;
        Level levelValue2 = getLevelValue("com.sun.metro.soap.dump." + side.toString() + ".level");
        if (levelValue2 != null) {
            logLevel = levelValue2;
        }
        Boolean value7 = getBooleanValue(msgDumpSystemPropertyBase);
        if (value7 != null) {
            dumpBefore3 = value7.booleanValue();
            dumpAfter3 = value7.booleanValue();
        }
        Boolean value8 = getBooleanValue(msgDumpSystemPropertyBase + ".before");
        boolean dumpBefore4 = value8 != null ? value8.booleanValue() : dumpBefore3;
        Boolean value9 = getBooleanValue(msgDumpSystemPropertyBase + ".after");
        boolean dumpAfter4 = value9 != null ? value9.booleanValue() : dumpAfter3;
        Level levelValue3 = getLevelValue(msgDumpSystemPropertyBase + ".level");
        if (levelValue3 != null) {
            logLevel = levelValue3;
        }
        String msgDumpSystemPropertyBase2 = msgDumpSystemPropertyBase + "." + side.toString();
        Boolean value10 = getBooleanValue(msgDumpSystemPropertyBase2);
        if (value10 != null) {
            dumpBefore4 = value10.booleanValue();
            dumpAfter4 = value10.booleanValue();
        }
        Boolean value11 = getBooleanValue(msgDumpSystemPropertyBase2 + ".before");
        boolean dumpBefore5 = value11 != null ? value11.booleanValue() : dumpBefore4;
        Boolean value12 = getBooleanValue(msgDumpSystemPropertyBase2 + ".after");
        boolean dumpAfter5 = value12 != null ? value12.booleanValue() : dumpAfter4;
        Level levelValue4 = getLevelValue(msgDumpSystemPropertyBase2 + ".level");
        if (levelValue4 != null) {
            logLevel = levelValue4;
        }
        return new MessageDumpingInfo(dumpBefore5, dumpAfter5, logLevel);
    }

    private Boolean getBooleanValue(String propertyName) {
        Boolean retVal = null;
        String stringValue = System.getProperty(propertyName);
        if (stringValue != null) {
            retVal = Boolean.valueOf(stringValue);
            LOGGER.fine(TubelineassemblyMessages.MASM_0018_MSG_LOGGING_SYSTEM_PROPERTY_SET_TO_VALUE(propertyName, retVal));
        }
        return retVal;
    }

    private Level getLevelValue(String propertyName) {
        Level retVal = null;
        String stringValue = System.getProperty(propertyName);
        if (stringValue != null) {
            LOGGER.fine(TubelineassemblyMessages.MASM_0018_MSG_LOGGING_SYSTEM_PROPERTY_SET_TO_VALUE(propertyName, stringValue));
            try {
                retVal = Level.parse(stringValue);
            } catch (IllegalArgumentException ex) {
                LOGGER.warning(TubelineassemblyMessages.MASM_0019_MSG_LOGGING_SYSTEM_PROPERTY_ILLEGAL_VALUE(propertyName, stringValue), ex);
            }
        }
        return retVal;
    }

    protected DefaultServerTubelineAssemblyContext createServerContext(ServerTubeAssemblerContext jaxwsContext) {
        return new DefaultServerTubelineAssemblyContext(jaxwsContext);
    }

    protected DefaultClientTubelineAssemblyContext createClientContext(ClientTubeAssemblerContext jaxwsContext) {
        return new DefaultClientTubelineAssemblyContext(jaxwsContext);
    }
}
