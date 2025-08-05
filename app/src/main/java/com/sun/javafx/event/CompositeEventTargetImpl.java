package com.sun.javafx.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;

/* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventTargetImpl.class */
public class CompositeEventTargetImpl implements CompositeEventTarget {
    private final Set<EventTarget> eventTargets;

    public CompositeEventTargetImpl(EventTarget... eventTargets) {
        Set<EventTarget> mutableSet = new HashSet<>(eventTargets.length);
        mutableSet.addAll(Arrays.asList(eventTargets));
        this.eventTargets = Collections.unmodifiableSet(mutableSet);
    }

    @Override // com.sun.javafx.event.CompositeEventTarget
    public Set<EventTarget> getTargets() {
        return this.eventTargets;
    }

    @Override // com.sun.javafx.event.CompositeEventTarget
    public boolean containsTarget(EventTarget target) {
        return this.eventTargets.contains(target);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        EventDispatchTree eventDispatchTree = (EventDispatchTree) tail;
        for (EventTarget eventTarget : this.eventTargets) {
            EventDispatchTree targetDispatchTree = eventDispatchTree.createTree();
            eventDispatchTree = eventDispatchTree.mergeTree((EventDispatchTree) eventTarget.buildEventDispatchChain(targetDispatchTree));
        }
        return eventDispatchTree;
    }
}
