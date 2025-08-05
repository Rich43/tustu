package sun.font;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/TextLineComponent.class */
public interface TextLineComponent {
    public static final int LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 1;
    public static final int UNCHANGED = 2;

    CoreMetrics getCoreMetrics();

    void draw(Graphics2D graphics2D, float f2, float f3);

    Rectangle2D getCharVisualBounds(int i2);

    Rectangle2D getVisualBounds();

    float getAdvance();

    Shape getOutline(float f2, float f3);

    int getNumCharacters();

    float getCharX(int i2);

    float getCharY(int i2);

    float getCharAdvance(int i2);

    boolean caretAtOffsetIsValid(int i2);

    int getLineBreakIndex(int i2, float f2);

    float getAdvanceBetween(int i2, int i3);

    Rectangle2D getLogicalBounds();

    Rectangle2D getItalicBounds();

    AffineTransform getBaselineTransform();

    boolean isSimple();

    Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3);

    TextLineComponent getSubset(int i2, int i3, int i4);

    int getNumJustificationInfos();

    void getJustificationInfos(GlyphJustificationInfo[] glyphJustificationInfoArr, int i2, int i3, int i4);

    TextLineComponent applyJustificationDeltas(float[] fArr, int i2, boolean[] zArr);
}
