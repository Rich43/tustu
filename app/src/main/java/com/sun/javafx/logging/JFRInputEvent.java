package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.ContentType;
import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;
import com.oracle.jrockit.jfr.ValueDefinition;

@EventDefinition(path = "javafx/input", name = "JavaFX Input", description = "JavaFX input event", stacktrace = false, thread = true)
/* loaded from: jfxrt.jar:com/sun/javafx/logging/JFRInputEvent.class */
public class JFRInputEvent extends TimedEvent {

    @ValueDefinition(name = "inputType", description = "Input event type", contentType = ContentType.None)
    private String input;

    public JFRInputEvent(EventToken eventToken) {
        super(eventToken);
    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String s2) {
        this.input = s2;
    }
}
