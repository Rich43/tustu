package org.icepdf.core.pobjects.graphics.text;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/AbstractText.class */
public abstract class AbstractText implements Text {
    protected Rectangle2D.Float bounds;
    protected boolean selected;
    protected boolean highlight;
    protected boolean hasSelected;
    protected boolean hasHighlight;

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public abstract Rectangle2D.Float getBounds();

    public void clearBounds() {
        this.bounds = null;
    }

    public boolean intersects(Rectangle2D rect) {
        GeneralPath shapePath = new GeneralPath(getBounds());
        return shapePath.intersects(rect);
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public boolean isSelected() {
        return this.selected;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public boolean isHighlighted() {
        return this.highlight;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public void setHighlighted(boolean highlight) {
        this.highlight = highlight;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public boolean hasHighligh() {
        return this.hasHighlight;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public boolean hasSelected() {
        return this.hasSelected;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public void setHasHighlight(boolean hasHighlight) {
        this.hasHighlight = hasHighlight;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.Text
    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }
}
