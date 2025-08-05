package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.FlightRecorder;
import com.oracle.jrockit.jfr.Producer;

/* loaded from: jfxrt.jar:com/sun/javafx/logging/JFRLogger.class */
class JFRLogger extends Logger {
    private static final String PRODUCER_URI = "http://www.oracle.com/technetwork/java/javafx/index.html";
    private static JFRLogger jfrLogger;
    private final Producer producer = new Producer("JavaFX producer", "JavaFX producer.", PRODUCER_URI);
    private final EventToken pulseEventToken = this.producer.addEvent(JFRPulseEvent.class);
    private final EventToken inputEventToken = this.producer.addEvent(JFRInputEvent.class);
    private final ThreadLocal<JFRPulseEvent> curPhaseEvent;
    private final ThreadLocal<JFRInputEvent> curInputEvent;
    private int pulseNumber;
    private int fxPulseNumber;
    private int renderPulseNumber;
    private Thread fxThread;

    private JFRLogger() throws Exception {
        this.producer.register();
        this.curPhaseEvent = new ThreadLocal() { // from class: com.sun.javafx.logging.JFRLogger.1
            @Override // java.lang.ThreadLocal
            public JFRPulseEvent initialValue() {
                return new JFRPulseEvent(JFRLogger.this.pulseEventToken);
            }
        };
        this.curInputEvent = new ThreadLocal() { // from class: com.sun.javafx.logging.JFRLogger.2
            @Override // java.lang.ThreadLocal
            public JFRInputEvent initialValue() {
                return new JFRInputEvent(JFRLogger.this.inputEventToken);
            }
        };
    }

    public static JFRLogger getInstance() {
        if (jfrLogger == null) {
            try {
                Class klass = Class.forName("com.oracle.jrockit.jfr.FlightRecorder");
                if (klass != null && FlightRecorder.isActive()) {
                    jfrLogger = new JFRLogger();
                }
            } catch (Exception e2) {
                jfrLogger = null;
            }
        }
        return jfrLogger;
    }

    @Override // com.sun.javafx.logging.Logger
    public void pulseStart() {
        this.pulseNumber++;
        this.fxPulseNumber = this.pulseNumber;
        if (this.fxThread == null) {
            this.fxThread = Thread.currentThread();
        }
        newPhase("Pulse start");
    }

    @Override // com.sun.javafx.logging.Logger
    public void pulseEnd() {
        newPhase(null);
        this.fxPulseNumber = 0;
    }

    @Override // com.sun.javafx.logging.Logger
    public void renderStart() {
        this.renderPulseNumber = this.fxPulseNumber;
    }

    @Override // com.sun.javafx.logging.Logger
    public void renderEnd() {
        newPhase(null);
        this.renderPulseNumber = 0;
    }

    @Override // com.sun.javafx.logging.Logger
    public void newPhase(String phaseName) {
        if (this.pulseEventToken == null) {
            return;
        }
        JFRPulseEvent event = this.curPhaseEvent.get();
        if (!this.pulseEventToken.isEnabled()) {
            event.setPhase(null);
            return;
        }
        if (event.getPhase() != null) {
            event.end();
            event.commit();
        }
        if (phaseName == null) {
            event.setPhase(null);
            return;
        }
        event.reset();
        event.begin();
        event.setPhase(phaseName);
        event.setPulseNumber(Thread.currentThread() == this.fxThread ? this.fxPulseNumber : this.renderPulseNumber);
    }

    @Override // com.sun.javafx.logging.Logger
    public void newInput(String input) {
        if (this.inputEventToken == null) {
            return;
        }
        JFRInputEvent event = this.curInputEvent.get();
        if (!this.inputEventToken.isEnabled()) {
            event.setInput(null);
            return;
        }
        if (event.getInput() != null) {
            event.end();
            event.commit();
        }
        if (input == null) {
            event.setInput(null);
            return;
        }
        event.reset();
        event.begin();
        event.setInput(input);
    }
}
