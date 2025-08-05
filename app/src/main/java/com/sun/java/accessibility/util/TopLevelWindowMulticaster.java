package com.sun.java.accessibility.util;

import java.awt.AWTEventMulticaster;
import java.awt.Window;
import java.util.EventListener;
import jdk.Exported;

@Exported(false)
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/TopLevelWindowMulticaster.class */
public class TopLevelWindowMulticaster extends AWTEventMulticaster implements TopLevelWindowListener {
    protected TopLevelWindowMulticaster(EventListener eventListener, EventListener eventListener2) {
        super(eventListener, eventListener2);
    }

    @Override // com.sun.java.accessibility.util.TopLevelWindowListener
    public void topLevelWindowCreated(Window window) {
        ((TopLevelWindowListener) this.f12359a).topLevelWindowCreated(window);
        ((TopLevelWindowListener) this.f12360b).topLevelWindowCreated(window);
    }

    @Override // com.sun.java.accessibility.util.TopLevelWindowListener
    public void topLevelWindowDestroyed(Window window) {
        ((TopLevelWindowListener) this.f12359a).topLevelWindowDestroyed(window);
        ((TopLevelWindowListener) this.f12360b).topLevelWindowDestroyed(window);
    }

    public static TopLevelWindowListener add(TopLevelWindowListener topLevelWindowListener, TopLevelWindowListener topLevelWindowListener2) {
        return (TopLevelWindowListener) addInternal(topLevelWindowListener, topLevelWindowListener2);
    }

    public static TopLevelWindowListener remove(TopLevelWindowListener topLevelWindowListener, TopLevelWindowListener topLevelWindowListener2) {
        return (TopLevelWindowListener) removeInternal(topLevelWindowListener, topLevelWindowListener2);
    }

    protected static EventListener addInternal(EventListener eventListener, EventListener eventListener2) {
        return eventListener == null ? eventListener2 : eventListener2 == null ? eventListener : new TopLevelWindowMulticaster(eventListener, eventListener2);
    }

    protected static EventListener removeInternal(EventListener eventListener, EventListener eventListener2) {
        if (eventListener == eventListener2 || eventListener == null) {
            return null;
        }
        if (eventListener instanceof TopLevelWindowMulticaster) {
            return ((TopLevelWindowMulticaster) eventListener).remove(eventListener2);
        }
        return eventListener;
    }
}
