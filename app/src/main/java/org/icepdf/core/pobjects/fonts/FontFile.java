package org.icepdf.core.pobjects.fonts;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/FontFile.class */
public interface FontFile {
    public static final long LAYOUT_NONE = 0;

    Point2D echarAdvance(char c2);

    FontFile deriveFont(AffineTransform affineTransform);

    FontFile deriveFont(Encoding encoding, CMap cMap);

    FontFile deriveFont(float[] fArr, int i2, float f2, float f3, float f4, char[] cArr);

    FontFile deriveFont(Map<Integer, Float> map, int i2, float f2, float f3, float f4, char[] cArr);

    boolean canDisplayEchar(char c2);

    FontFile deriveFont(float f2);

    CMap getToUnicode();

    String toUnicode(String str);

    String toUnicode(char c2);

    String getFamily();

    float getSize();

    double getAscent();

    double getDescent();

    Rectangle2D getMaxCharBounds();

    AffineTransform getTransform();

    int getRights();

    String getName();

    boolean isHinted();

    int getNumGlyphs();

    int getStyle();

    char getSpaceEchar();

    Rectangle2D getEstringBounds(String str, int i2, int i3);

    String getFormat();

    void drawEstring(Graphics2D graphics2D, String str, float f2, float f3, long j2, int i2, Color color);

    Shape getEstringOutline(String str, float f2, float f3);

    boolean isOneByteEncoding();
}
