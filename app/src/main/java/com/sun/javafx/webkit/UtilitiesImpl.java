package com.sun.javafx.webkit;

import com.sun.javafx.webkit.theme.ContextMenuImpl;
import com.sun.javafx.webkit.theme.PopupMenuImpl;
import com.sun.webkit.ContextMenu;
import com.sun.webkit.Pasteboard;
import com.sun.webkit.PopupMenu;
import com.sun.webkit.Utilities;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/UtilitiesImpl.class */
public final class UtilitiesImpl extends Utilities {
    @Override // com.sun.webkit.Utilities
    protected Pasteboard createPasteboard() {
        return new PasteboardImpl();
    }

    @Override // com.sun.webkit.Utilities
    protected PopupMenu createPopupMenu() {
        return new PopupMenuImpl();
    }

    @Override // com.sun.webkit.Utilities
    protected ContextMenu createContextMenu() {
        return new ContextMenuImpl();
    }
}
