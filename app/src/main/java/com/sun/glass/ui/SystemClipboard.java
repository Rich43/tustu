package com.sun.glass.ui;

import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/glass/ui/SystemClipboard.class */
public abstract class SystemClipboard extends Clipboard {
    protected abstract boolean isOwner();

    protected abstract void pushToSystem(HashMap<String, Object> map, int i2);

    protected abstract void pushTargetActionToSystem(int i2);

    protected abstract Object popFromSystem(String str);

    protected abstract int supportedSourceActionsFromSystem();

    protected abstract String[] mimesFromSystem();

    protected SystemClipboard(String name) {
        super(name);
        Application.checkEventThread();
    }

    @Override // com.sun.glass.ui.Clipboard
    public void flush(ClipboardAssistance dataSource, HashMap<String, Object> cacheData, int supportedActions) {
        Application.checkEventThread();
        setSharedData(dataSource, cacheData, supportedActions);
        pushToSystem(cacheData, supportedActions);
    }

    @Override // com.sun.glass.ui.Clipboard
    public int getSupportedSourceActions() {
        Application.checkEventThread();
        if (isOwner()) {
            return super.getSupportedSourceActions();
        }
        return supportedSourceActionsFromSystem();
    }

    @Override // com.sun.glass.ui.Clipboard
    public void setTargetAction(int actionDone) {
        Application.checkEventThread();
        pushTargetActionToSystem(actionDone);
    }

    public Object getLocalData(String mimeType) {
        return super.getData(mimeType);
    }

    @Override // com.sun.glass.ui.Clipboard
    public Object getData(String mimeType) {
        Application.checkEventThread();
        if (isOwner()) {
            return getLocalData(mimeType);
        }
        return popFromSystem(mimeType);
    }

    @Override // com.sun.glass.ui.Clipboard
    public String[] getMimeTypes() {
        Application.checkEventThread();
        if (isOwner()) {
            return super.getMimeTypes();
        }
        return mimesFromSystem();
    }

    @Override // com.sun.glass.ui.Clipboard
    public String toString() {
        Application.checkEventThread();
        return "System Clipboard";
    }
}
