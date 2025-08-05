package org.icepdf.core.pobjects.graphics.text;

import java.awt.geom.Rectangle2D;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/Text.class */
public interface Text {
    Rectangle2D.Float getBounds();

    boolean isHighlighted();

    boolean isSelected();

    void setHighlighted(boolean z2);

    void setSelected(boolean z2);

    boolean hasHighligh();

    boolean hasSelected();

    void setHasHighlight(boolean z2);

    void setHasSelected(boolean z2);
}
