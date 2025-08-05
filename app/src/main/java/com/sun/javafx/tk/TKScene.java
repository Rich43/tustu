package com.sun.javafx.tk;

import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import java.security.AccessControlContext;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKScene.class */
public interface TKScene {
    void dispose();

    void waitForRenderingToComplete();

    void waitForSynchronization();

    void releaseSynchronization(boolean z2);

    void setTKSceneListener(TKSceneListener tKSceneListener);

    void setTKScenePaintListener(TKScenePaintListener tKScenePaintListener);

    void setRoot(NGNode nGNode);

    void markDirty();

    void setCamera(NGCamera nGCamera);

    NGLightBase[] getLights();

    void setLights(NGLightBase[] nGLightBaseArr);

    void setFillPaint(Object obj);

    void setCursor(Object obj);

    void enableInputMethodEvents(boolean z2);

    void finishInputMethodComposition();

    void entireSceneNeedsRepaint();

    TKClipboard createDragboard(boolean z2);

    AccessControlContext getAccessControlContext();
}
