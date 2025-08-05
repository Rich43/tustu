package com.sun.java.accessibility.util;

import java.awt.AWTEventMulticaster;
import java.util.EventListener;
import jdk.Exported;

@Exported(false)
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/GUIInitializedMulticaster.class */
public class GUIInitializedMulticaster extends AWTEventMulticaster implements GUIInitializedListener {
    protected GUIInitializedMulticaster(EventListener eventListener, EventListener eventListener2) {
        super(eventListener, eventListener2);
    }

    @Override // com.sun.java.accessibility.util.GUIInitializedListener
    public void guiInitialized() {
        ((GUIInitializedListener) this.f12359a).guiInitialized();
        ((GUIInitializedListener) this.f12360b).guiInitialized();
    }

    public static GUIInitializedListener add(GUIInitializedListener gUIInitializedListener, GUIInitializedListener gUIInitializedListener2) {
        return (GUIInitializedListener) addInternal(gUIInitializedListener, gUIInitializedListener2);
    }

    public static GUIInitializedListener remove(GUIInitializedListener gUIInitializedListener, GUIInitializedListener gUIInitializedListener2) {
        return (GUIInitializedListener) removeInternal(gUIInitializedListener, gUIInitializedListener2);
    }

    protected static EventListener addInternal(EventListener eventListener, EventListener eventListener2) {
        return eventListener == null ? eventListener2 : eventListener2 == null ? eventListener : new GUIInitializedMulticaster(eventListener, eventListener2);
    }

    protected static EventListener removeInternal(EventListener eventListener, EventListener eventListener2) {
        if (eventListener == eventListener2 || eventListener == null) {
            return null;
        }
        if (eventListener instanceof GUIInitializedMulticaster) {
            return ((GUIInitializedMulticaster) eventListener).remove(eventListener2);
        }
        return eventListener;
    }
}
