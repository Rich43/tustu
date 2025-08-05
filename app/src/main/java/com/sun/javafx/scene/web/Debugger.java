package com.sun.javafx.scene.web;

import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/web/Debugger.class */
public interface Debugger {
    boolean isEnabled();

    void setEnabled(boolean z2);

    void sendMessage(String str);

    Callback<String, Void> getMessageCallback();

    void setMessageCallback(Callback<String, Void> callback);
}
