package com.sun.glass.ui.win;

import com.sun.glass.ui.Clipboard;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinDnDClipboard.class */
final class WinDnDClipboard extends WinSystemClipboard {
    private static int dragButton = 0;
    private int sourceSupportedActions;

    @Override // com.sun.glass.ui.win.WinSystemClipboard
    protected native void dispose();

    @Override // com.sun.glass.ui.win.WinSystemClipboard
    protected native void push(Object[] objArr, int i2);

    public WinDnDClipboard(String name) {
        super(name);
        this.sourceSupportedActions = 0;
    }

    @Override // com.sun.glass.ui.win.WinSystemClipboard
    protected void create() {
    }

    @Override // com.sun.glass.ui.win.WinSystemClipboard, com.sun.glass.ui.SystemClipboard
    protected boolean isOwner() {
        return getDragButton() != 0;
    }

    @Override // com.sun.glass.ui.win.WinSystemClipboard, com.sun.glass.ui.SystemClipboard
    protected void pushTargetActionToSystem(int actionDone) {
        throw new UnsupportedOperationException("[Target Action] not supported! Override View.handleDragDrop instead.");
    }

    @Override // com.sun.glass.ui.win.WinSystemClipboard
    protected boolean pop() {
        return getPtr() != 0;
    }

    private static WinDnDClipboard getInstance() {
        return (WinDnDClipboard) get(Clipboard.DND);
    }

    @Override // com.sun.glass.ui.win.WinSystemClipboard, com.sun.glass.ui.SystemClipboard, com.sun.glass.ui.Clipboard
    public String toString() {
        return "Windows DnD Clipboard";
    }

    public int getDragButton() {
        return dragButton;
    }

    private void setDragButton(int dragButton2) {
        dragButton = dragButton2;
    }

    @Override // com.sun.glass.ui.win.WinSystemClipboard, com.sun.glass.ui.SystemClipboard
    protected final int supportedSourceActionsFromSystem() {
        return this.sourceSupportedActions != 0 ? this.sourceSupportedActions : super.supportedSourceActionsFromSystem();
    }

    private void setSourceSupportedActions(int sourceSupportedActions) {
        this.sourceSupportedActions = sourceSupportedActions;
    }
}
