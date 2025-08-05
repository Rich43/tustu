package com.sun.java.accessibility.util;

import java.awt.Window;
import java.util.EventListener;
import jdk.Exported;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/TopLevelWindowListener.class */
public interface TopLevelWindowListener extends EventListener {
    void topLevelWindowCreated(Window window);

    void topLevelWindowDestroyed(Window window);
}
