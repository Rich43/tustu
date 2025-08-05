package com.sun.glass.ui;

import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/glass/ui/ClipboardAssistance.class */
public class ClipboardAssistance {
    private final Clipboard clipboard;
    private final HashMap<String, Object> cacheData = new HashMap<>();
    private int supportedActions = Clipboard.ACTION_ANY;

    public ClipboardAssistance(String cipboardName) {
        Application.checkEventThread();
        this.clipboard = Clipboard.get(cipboardName);
        this.clipboard.add(this);
    }

    public void close() {
        Application.checkEventThread();
        this.clipboard.remove(this);
    }

    public void flush() {
        Application.checkEventThread();
        this.clipboard.flush(this, this.cacheData, this.supportedActions);
    }

    public void emptyCache() {
        Application.checkEventThread();
        this.cacheData.clear();
    }

    public boolean isCacheEmpty() {
        Application.checkEventThread();
        return this.cacheData.isEmpty();
    }

    public void setData(String mimeType, Object data) {
        Application.checkEventThread();
        this.cacheData.put(mimeType, data);
    }

    public Object getData(String mimeType) {
        Application.checkEventThread();
        return this.clipboard.getData(mimeType);
    }

    public void setSupportedActions(int supportedActions) {
        Application.checkEventThread();
        this.supportedActions = supportedActions;
    }

    public int getSupportedSourceActions() {
        Application.checkEventThread();
        return this.clipboard.getSupportedSourceActions();
    }

    public void setTargetAction(int actionDone) {
        Application.checkEventThread();
        this.clipboard.setTargetAction(actionDone);
    }

    public void contentChanged() {
    }

    public void actionPerformed(int action) {
    }

    public String[] getMimeTypes() {
        Application.checkEventThread();
        return this.clipboard.getMimeTypes();
    }

    public String toString() {
        return "ClipboardAssistance[" + ((Object) this.clipboard) + "]";
    }
}
