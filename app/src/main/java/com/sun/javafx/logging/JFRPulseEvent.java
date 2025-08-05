package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.ContentType;
import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;
import com.oracle.jrockit.jfr.ValueDefinition;

@EventDefinition(path = "javafx/pulse", name = "JavaFX Pulse Phase", description = "Describes a phase in JavaFX pulse processing", stacktrace = false, thread = true)
/* loaded from: jfxrt.jar:com/sun/javafx/logging/JFRPulseEvent.class */
public class JFRPulseEvent extends TimedEvent {

    @ValueDefinition(name = "pulseID", description = "Pulse number", contentType = ContentType.None, relationKey = "http://www.oracle.com/javafx/pulse/id")
    private int pulseNumber;

    @ValueDefinition(name = "phaseName", description = "Pulse phase name", contentType = ContentType.None)
    private String phase;

    public JFRPulseEvent(EventToken eventToken) {
        super(eventToken);
    }

    public int getPulseNumber() {
        return this.pulseNumber;
    }

    public void setPulseNumber(int n2) {
        this.pulseNumber = n2;
    }

    public String getPhase() {
        return this.phase;
    }

    public void setPhase(String s2) {
        this.phase = s2;
    }
}
