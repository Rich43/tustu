package org.icepdf.core.pobjects.graphics;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ShadingPattern.class */
public abstract class ShadingPattern extends Dictionary implements Pattern {
    private static final Logger logger = Logger.getLogger(ShadingPattern.class.toString());
    public static final Name PATTERN_TYPE_KEY = new Name("PatternType");
    public static final Name EXTGSTATE_KEY = new Name("ExtGState");
    public static final Name MATRIX_KEY = new Name("Matrix");
    public static final Name SHADING_KEY = new Name("Shading");
    public static final Name SHADING_TYPE_KEY = new Name("ShadingType");
    public static final Name BBOX_KEY = new Name("BBox");
    public static final Name COLORSPACE_KEY = new Name(PdfOps.CS_NAME);
    public static final Name BACKGROUND_KEY = new Name("Background");
    public static final Name ANTIALIAS_KEY = new Name("AntiAlias");
    public static final Name DOMAIN_KEY = new Name("Domain");
    public static final Name COORDS_KEY = new Name("Coords");
    public static final Name EXTEND_KEY = new Name("Extend");
    public static final Name FUNCTION_KEY = new Name("Function");
    public static final int SHADING_PATTERN_TYPE_1 = 1;
    public static final int SHADING_PATTERN_TYPE_2 = 2;
    public static final int SHADING_PATTERN_TYPE_3 = 3;
    public static final int SHADING_PATTERN_TYPE_4 = 4;
    public static final int SHADING_PATTERN_TYPE_5 = 5;
    public static final int SHADING_PATTERN_TYPE_6 = 6;
    protected Name type;
    protected int patternType;
    protected HashMap shading;
    protected int shadingType;
    protected Rectangle2D bBox;
    protected PColorSpace colorSpace;
    protected List background;
    protected boolean antiAlias;
    protected AffineTransform matrix;
    protected ExtGState extGState;
    protected boolean inited;

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public abstract Paint getPaint();

    @Override // org.icepdf.core.pobjects.Dictionary
    public abstract void init();

    public ShadingPattern(Library library, HashMap entries) {
        super(library, entries);
        this.type = library.getName(entries, TYPE_KEY);
        this.patternType = library.getInt(entries, PATTERN_TYPE_KEY);
        Object attribute = library.getObject(entries, EXTGSTATE_KEY);
        if (attribute instanceof HashMap) {
            this.extGState = new ExtGState(library, (HashMap) attribute);
        } else if (attribute instanceof Reference) {
            this.extGState = new ExtGState(library, (HashMap) library.getObject((Reference) attribute));
        }
        List v2 = (List) library.getObject(entries, MATRIX_KEY);
        if (v2 != null) {
            this.matrix = getAffineTransform(v2);
        } else {
            this.matrix = new AffineTransform();
        }
    }

    public static ShadingPattern getShadingPattern(Library library, HashMap attribute) {
        HashMap shading = library.getDictionary(attribute, SHADING_KEY);
        if (shading != null) {
            return shadingFactory(library, attribute, shading);
        }
        return null;
    }

    public static ShadingPattern getShadingPattern(Library library, HashMap entries, HashMap shading) {
        if (entries != null) {
            ShadingPattern shadingPattern = shadingFactory(library, shading, shading);
            shadingPattern.setShading(shading);
            return shadingPattern;
        }
        return null;
    }

    private static ShadingPattern shadingFactory(Library library, HashMap attribute, HashMap patternDictionary) {
        int shadingType = library.getInt(patternDictionary, SHADING_TYPE_KEY);
        if (shadingType == 2) {
            return new ShadingType2Pattern(library, attribute);
        }
        if (shadingType == 3) {
            return new ShadingType3Pattern(library, attribute);
        }
        if (shadingType == 1) {
            return new ShadingType1Pattern(library, attribute);
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Shading pattern of Type " + shadingType + " are not currently supported");
            return null;
        }
        return null;
    }

    private static AffineTransform getAffineTransform(List v2) {
        float[] f2 = new float[6];
        for (int i2 = 0; i2 < 6; i2++) {
            f2[i2] = ((Number) v2.get(i2)).floatValue();
        }
        return new AffineTransform(f2);
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public void setParentGraphicState(GraphicsState graphicsState) {
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public void setMatrix(AffineTransform matrix) {
        this.matrix = matrix;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public int getPatternType() {
        return this.patternType;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public Rectangle2D getBBox() {
        return this.bBox;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public AffineTransform getMatrix() {
        return this.matrix;
    }

    public int getShadingType() {
        return this.shadingType;
    }

    public void setShading(HashMap shading) {
        this.shading = shading;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public Name getType() {
        return this.type;
    }

    public PColorSpace getColorSpace() {
        return this.colorSpace;
    }

    public List getBackground() {
        return this.background;
    }

    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    public ExtGState getExtGState() {
        return this.extGState;
    }

    public boolean isInited() {
        return this.inited;
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "Shading Pattern: \n           type: pattern \n    patternType: shading\n         matrix: " + ((Object) this.matrix) + "\n      extGState: " + ((Object) this.extGState) + "\n        shading dictionary: " + ((Object) this.shading) + "\n               shadingType: " + this.shadingType + "\n               colourSpace: " + ((Object) this.colorSpace) + "\n                background: " + ((Object) this.background) + "\n                      bbox: " + ((Object) this.bBox) + "\n                 antiAlias: " + this.antiAlias;
    }
}
