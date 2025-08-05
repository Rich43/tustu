package org.icepdf.core.pobjects.graphics;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.fonts.Font;
import org.icepdf.core.pobjects.fonts.FontFile;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/TextState.class */
public class TextState {
    public static final int MODE_FILL = 0;
    public static final int MODE_STROKE = 1;
    public static final int MODE_FILL_STROKE = 2;
    public static final int MODE_INVISIBLE = 3;
    public static final int MODE_FILL_ADD = 4;
    public static final int MODE_STROKE_ADD = 5;
    public static final int MODE_FILL_STROKE_ADD = 6;
    public static final int MODE_ADD = 7;
    protected PRectangle type3BBox;
    protected Point2D.Float type3HorizontalDisplacement;
    public float cspace;
    public float wspace;
    public float hScalling;
    public float leading;
    public float tsize;
    public int rmode;
    public float trise;
    public AffineTransform tmatrix;
    public AffineTransform tlmatrix;
    public Font font;
    public FontFile currentfont;

    public TextState() {
        this.cspace = 0.0f;
        this.wspace = 0.0f;
        this.hScalling = 1.0f;
        this.leading = 0.0f;
        this.tsize = 0.0f;
        this.rmode = 0;
        this.trise = 0.0f;
        this.tmatrix = new AffineTransform();
        this.tlmatrix = new AffineTransform();
    }

    public TextState(TextState ts) {
        this.cspace = 0.0f;
        this.wspace = 0.0f;
        this.hScalling = 1.0f;
        this.leading = 0.0f;
        this.tsize = 0.0f;
        this.rmode = 0;
        this.trise = 0.0f;
        this.tmatrix = new AffineTransform();
        this.tlmatrix = new AffineTransform();
        this.cspace = ts.cspace;
        this.wspace = ts.wspace;
        this.hScalling = ts.hScalling;
        this.leading = ts.leading;
        this.font = ts.font;
        this.currentfont = ts.currentfont != null ? ts.currentfont.deriveFont(new AffineTransform()) : null;
        this.tsize = ts.tsize;
        this.tmatrix = new AffineTransform(ts.tmatrix);
        this.tlmatrix = new AffineTransform(ts.tlmatrix);
        this.rmode = ts.rmode;
        this.trise = ts.trise;
    }

    public PRectangle getType3BBox() {
        return this.type3BBox;
    }

    public void setType3BBox(PRectangle type3BBox) {
        this.type3BBox = type3BBox;
    }

    public Point2D.Float getType3HorizontalDisplacement() {
        return this.type3HorizontalDisplacement;
    }

    public void setType3HorizontalDisplacement(Point2D.Float type3HorizontalDisplacement) {
        this.type3HorizontalDisplacement = type3HorizontalDisplacement;
    }
}
