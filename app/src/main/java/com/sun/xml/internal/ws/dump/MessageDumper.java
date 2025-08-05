package com.sun.xml.internal.ws.dump;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/dump/MessageDumper.class */
final class MessageDumper {
    private final String tubeName;
    private final Logger logger;
    private Level loggingLevel;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/dump/MessageDumper$MessageType.class */
    enum MessageType {
        Request("Request message"),
        Response("Response message"),
        Exception("Response exception");

        private final String name;

        MessageType(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/dump/MessageDumper$ProcessingState.class */
    enum ProcessingState {
        Received("received"),
        Processed("processed");

        private final String name;

        ProcessingState(String name) {
            this.name = name;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }

    public MessageDumper(String tubeName, Logger logger, Level loggingLevel) {
        this.tubeName = tubeName;
        this.logger = logger;
        this.loggingLevel = loggingLevel;
    }

    final boolean isLoggable() {
        return this.logger.isLoggable(this.loggingLevel);
    }

    final void setLoggingLevel(Level level) {
        this.loggingLevel = level;
    }

    final String createLogMessage(MessageType messageType, ProcessingState processingState, int tubeId, String engineId, String message) {
        return String.format("%s %s in Tube [ %s ] Instance [ %d ] Engine [ %s ] Thread [ %s ]:%n%s", messageType, processingState, this.tubeName, Integer.valueOf(tubeId), engineId, Thread.currentThread().getName(), message);
    }

    final String dump(MessageType messageType, ProcessingState processingState, String message, int tubeId, String engineId) {
        String logMessage = createLogMessage(messageType, processingState, tubeId, engineId, message);
        this.logger.log(this.loggingLevel, logMessage);
        return logMessage;
    }
}
