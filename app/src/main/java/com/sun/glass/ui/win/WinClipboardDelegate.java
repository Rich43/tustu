package com.sun.glass.ui.win;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinClipboardDelegate.class */
final class WinClipboardDelegate implements ClipboardDelegate {
    WinClipboardDelegate() {
    }

    @Override // com.sun.glass.ui.delegate.ClipboardDelegate
    public Clipboard createClipboard(String clipboardName) {
        if (Clipboard.SYSTEM.equals(clipboardName)) {
            return new WinSystemClipboard(clipboardName);
        }
        if (Clipboard.DND.equals(clipboardName)) {
            return new WinDnDClipboard(clipboardName);
        }
        return null;
    }
}
