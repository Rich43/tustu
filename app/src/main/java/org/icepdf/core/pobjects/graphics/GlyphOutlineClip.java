package org.icepdf.core.pobjects.graphics;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/GlyphOutlineClip.class */
public class GlyphOutlineClip {
    private GeneralPath path;

    public void addTextSprite(TextSprite nextSprite) {
        Area area = nextSprite.getGlyphOutline();
        Area tmp = area.createTransformedArea(nextSprite.getGraphicStateTransform());
        if (this.path == null) {
            this.path = new GeneralPath(tmp);
        } else {
            this.path.append((Shape) tmp, false);
        }
    }

    public boolean isEmpty() {
        return this.path == null;
    }

    public Shape getGlyphOutlineClip() {
        return this.path;
    }
}
