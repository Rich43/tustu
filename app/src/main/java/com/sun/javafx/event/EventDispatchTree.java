package com.sun.javafx.event;

import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

/* loaded from: jfxrt.jar:com/sun/javafx/event/EventDispatchTree.class */
public interface EventDispatchTree extends EventDispatchChain {
    EventDispatchTree createTree();

    EventDispatchTree mergeTree(EventDispatchTree eventDispatchTree);

    @Override // javafx.event.EventDispatchChain
    EventDispatchTree append(EventDispatcher eventDispatcher);

    @Override // javafx.event.EventDispatchChain
    EventDispatchTree prepend(EventDispatcher eventDispatcher);
}
