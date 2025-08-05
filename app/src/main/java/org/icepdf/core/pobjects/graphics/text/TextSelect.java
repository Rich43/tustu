package org.icepdf.core.pobjects.graphics.text;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/TextSelect.class */
public interface TextSelect {
    void clearSelected();

    StringBuilder getSelected();

    void clearHighlighted();

    void selectAll();
}
