package com.sun.javafx.tk;

import java.security.AccessControlContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/LocalClipboard.class */
final class LocalClipboard implements TKClipboard {
    private final Map<DataFormat, Object> values = new HashMap();

    @Override // com.sun.javafx.tk.TKClipboard
    public void setSecurityContext(AccessControlContext ctx) {
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Set<DataFormat> getContentTypes() {
        return Collections.unmodifiableSet(new HashSet(this.values.keySet()));
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public boolean putContent(Pair<DataFormat, Object>... content) {
        for (Pair<DataFormat, Object> pair : content) {
            if (pair.getKey() == null) {
                throw new NullPointerException("Clipboard.putContent: null data format");
            }
            if (pair.getValue() == null) {
                throw new NullPointerException("Clipboard.putContent: null data");
            }
        }
        this.values.clear();
        for (Pair<DataFormat, Object> pair2 : content) {
            this.values.put(pair2.getKey(), pair2.getValue());
        }
        return true;
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Object getContent(DataFormat dataFormat) {
        return this.values.get(dataFormat);
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public boolean hasContent(DataFormat dataFormat) {
        return this.values.containsKey(dataFormat);
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Set<TransferMode> getTransferModes() {
        throw new IllegalStateException();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setDragView(Image image) {
        throw new IllegalStateException();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setDragViewOffsetX(double offsetX) {
        throw new IllegalStateException();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public void setDragViewOffsetY(double offsetY) {
        throw new IllegalStateException();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public Image getDragView() {
        throw new IllegalStateException();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public double getDragViewOffsetX() {
        throw new IllegalStateException();
    }

    @Override // com.sun.javafx.tk.TKClipboard
    public double getDragViewOffsetY() {
        throw new IllegalStateException();
    }
}
