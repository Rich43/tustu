package com.sun.xml.internal.ws.dump;

import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/dump/MessageDumpingFeature.class */
public final class MessageDumpingFeature extends WebServiceFeature {
    public static final String ID = "com.sun.xml.internal.ws.messagedump.MessageDumpingFeature";
    private static final Level DEFAULT_MSG_LOG_LEVEL = Level.FINE;
    private final Queue<String> messageQueue;
    private final AtomicBoolean messageLoggingStatus;
    private final String messageLoggingRoot;
    private final Level messageLoggingLevel;

    public MessageDumpingFeature() {
        this(null, null, true);
    }

    public MessageDumpingFeature(String msgLogRoot, Level msgLogLevel, boolean storeMessages) {
        this.messageQueue = storeMessages ? new ConcurrentLinkedQueue() : null;
        this.messageLoggingStatus = new AtomicBoolean(true);
        this.messageLoggingRoot = (msgLogRoot == null || msgLogRoot.length() <= 0) ? "com.sun.xml.internal.ws.messagedump" : msgLogRoot;
        this.messageLoggingLevel = msgLogLevel != null ? msgLogLevel : DEFAULT_MSG_LOG_LEVEL;
        this.enabled = true;
    }

    public MessageDumpingFeature(boolean enabled) {
        this();
        this.enabled = enabled;
    }

    @FeatureConstructor({Enabled.NAME, "messageLoggingRoot", "messageLoggingLevel", "storeMessages"})
    public MessageDumpingFeature(boolean enabled, String msgLogRoot, String msgLogLevel, boolean storeMessages) {
        this(msgLogRoot, Level.parse(msgLogLevel), storeMessages);
        this.enabled = enabled;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }

    public String nextMessage() {
        if (this.messageQueue != null) {
            return this.messageQueue.poll();
        }
        return null;
    }

    public void enableMessageLogging() {
        this.messageLoggingStatus.set(true);
    }

    public void disableMessageLogging() {
        this.messageLoggingStatus.set(false);
    }

    @ManagedAttribute
    public boolean getMessageLoggingStatus() {
        return this.messageLoggingStatus.get();
    }

    @ManagedAttribute
    public String getMessageLoggingRoot() {
        return this.messageLoggingRoot;
    }

    @ManagedAttribute
    public Level getMessageLoggingLevel() {
        return this.messageLoggingLevel;
    }

    boolean offerMessage(String message) {
        if (this.messageQueue != null) {
            return this.messageQueue.offer(message);
        }
        return false;
    }
}
