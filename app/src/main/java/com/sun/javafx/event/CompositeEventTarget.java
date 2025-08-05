package com.sun.javafx.event;

import java.util.Set;
import javafx.event.EventTarget;

/* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventTarget.class */
public interface CompositeEventTarget extends EventTarget {
    Set<EventTarget> getTargets();

    boolean containsTarget(EventTarget eventTarget);
}
