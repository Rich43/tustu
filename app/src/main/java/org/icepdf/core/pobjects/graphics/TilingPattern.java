package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.content.ContentParser;
import org.icepdf.core.util.content.ContentParserFactory;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/TilingPattern.class */
public class TilingPattern extends Stream implements Pattern {
    private static final Logger logger = Logger.getLogger(TilingPattern.class.toString());
    public static final Name PATTERNTYPE_KEY = new Name("PatternType");
    public static final Name PAINTTYPE_KEY = new Name("PaintType");
    public static final Name TILINGTYPE_KEY = new Name("TilingType");
    public static final Name BBOX_KEY = new Name("BBox");
    public static final Name XSTEP_KEY = new Name("XStep");
    public static final Name YSTEP_KEY = new Name("YStep");
    public static final Name MATRIX_KEY = new Name("Matrix");
    public static final Name RESOURCES_KEY = new Name("Resources");
    private static RenderingHints renderingHints;
    private int patternType;
    private int paintType;
    private Color unColored;
    public static final int PAINTING_TYPE_COLORED_TILING_PATTERN = 1;
    public static final int PAINTING_TYPE_UNCOLORED_TILING_PATTERN = 2;
    private int tilingType;
    private Name type;
    public static final int TILING_TYPE_CONSTANT_SPACING = 1;
    public static final int TILING_TYPE_NO_DISTORTION = 2;
    public static final int TILING_TYPE_CONSTANT_SPACING_FASTER = 3;
    private Rectangle2D bBox;
    private Rectangle2D bBoxMod;
    private float xStep;
    private float yStep;
    private Resources resources;
    private AffineTransform matrix;
    private Shapes shapes;
    public Color fillColour;
    private boolean inited;
    private GraphicsState parentGraphicState;

    static {
        Object antiAliasing = RenderingHints.VALUE_ANTIALIAS_OFF;
        Object interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        String property = Defs.sysProperty("org.icepdf.core.tiling.antiAliasing");
        if (property != null) {
            if (property.equalsIgnoreCase("VALUE_ANTIALIAS_DEFAULT")) {
                antiAliasing = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
            } else if (property.equalsIgnoreCase("VALUE_ANTIALIAS_ON")) {
                antiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
            } else if (property.equalsIgnoreCase("VALUE_ANTIALIAS_OFF")) {
                antiAliasing = RenderingHints.VALUE_ANTIALIAS_OFF;
            }
        }
        String property2 = Defs.sysProperty("org.icepdf.core.tiling.interpolation");
        if (property2 != null) {
            if (property2.equalsIgnoreCase("VALUE_INTERPOLATION_BICUBIC")) {
                interpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            } else if (property2.equalsIgnoreCase("VALUE_INTERPOLATION_BILINEAR")) {
                interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            } else if (property2.equalsIgnoreCase("VALUE_INTERPOLATION_NEAREST_NEIGHBOR")) {
                interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            }
        }
        renderingHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, interpolation);
        renderingHints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, antiAliasing));
    }

    public TilingPattern(Stream stream) {
        super(stream.getLibrary(), stream.getEntries(), stream.getRawBytes());
        this.fillColour = null;
        initiParams();
    }

    public TilingPattern(Library l2, HashMap h2, SeekableInputConstrainedWrapper streamInputWrapper) {
        super(l2, h2, streamInputWrapper);
        this.fillColour = null;
        initiParams();
    }

    private void initiParams() {
        this.type = this.library.getName(this.entries, TYPE_KEY);
        this.patternType = this.library.getInt(this.entries, PATTERNTYPE_KEY);
        this.paintType = this.library.getInt(this.entries, PAINTTYPE_KEY);
        this.tilingType = this.library.getInt(this.entries, TILINGTYPE_KEY);
        this.bBox = this.library.getRectangle(this.entries, BBOX_KEY);
        this.xStep = this.library.getFloat(this.entries, XSTEP_KEY);
        this.yStep = this.library.getFloat(this.entries, YSTEP_KEY);
        List v2 = (List) this.library.getObject(this.entries, MATRIX_KEY);
        if (v2 != null) {
            this.matrix = getAffineTransform(v2);
        } else {
            this.matrix = new AffineTransform();
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public Name getType() {
        return this.type;
    }

    private static AffineTransform getAffineTransform(List v2) {
        float[] f2 = new float[6];
        for (int i2 = 0; i2 < 6; i2++) {
            f2[i2] = ((Number) v2.get(i2)).floatValue();
        }
        return new AffineTransform(f2);
    }

    public Color getFirstColor() {
        if (this.shapes != null && this.unColored == null) {
            int max = this.shapes.shapes.size();
            for (int i2 = 0; i2 < max; i2++) {
                if (this.shapes.shapes.get(i2) instanceof ColorDrawCmd) {
                    ColorDrawCmd tmp = (ColorDrawCmd) this.shapes.shapes.get(i2);
                    this.unColored = tmp.getColor();
                    return this.unColored;
                }
            }
        }
        if (this.unColored == null) {
            this.unColored = Color.black;
            return this.unColored;
        }
        return this.unColored;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v6, types: [byte[], byte[][]] */
    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.inited) {
            return;
        }
        this.inited = true;
        Resources leafResources = this.library.getResources(this.entries, RESOURCES_KEY);
        if (leafResources != null) {
            this.resources = leafResources;
        }
        if (this.paintType == 2) {
            this.parentGraphicState.setFillColor(this.unColored);
            this.parentGraphicState.setStrokeColor(this.unColored);
        }
        ContentParser contentParser = ContentParserFactory.getInstance().getContentParser(this.library, this.resources);
        contentParser.setGraphicsState(this.parentGraphicState);
        try {
            this.shapes = contentParser.parse(new byte[]{getDecodedStreamBytes()}, null).getShapes();
        } catch (Throwable e2) {
            logger.log(Level.FINE, "Error processing tiling pattern.", e2);
        }
        if (this.xStep == 32767.0f) {
            this.xStep = (float) this.bBox.getWidth();
        }
        if (this.yStep == 32767.0f) {
            this.yStep = (float) this.bBox.getHeight();
        }
        this.bBoxMod = new Rectangle2D.Double(this.bBox.getX(), this.bBox.getY(), this.bBox.getWidth() == ((double) this.xStep) ? this.bBox.getWidth() : this.xStep, this.bBox.getHeight() == ((double) this.yStep) ? this.bBox.getHeight() : this.yStep);
    }

    public void paintPattern(Graphics2D g2, AffineTransform base) {
        AffineTransform originalPageSpace;
        AffineTransform context = new AffineTransform();
        try {
            originalPageSpace = new AffineTransform(base);
            context = g2.getTransform().createInverse();
            originalPageSpace.concatenate(context);
        } catch (NoninvertibleTransformException e2) {
            logger.warning("Error creating Tiling pattern transform. ");
            originalPageSpace = new AffineTransform();
        }
        Rectangle2D bBoxMod = originalPageSpace.createTransformedShape(this.matrix.createTransformedShape(this.bBoxMod).getBounds2D()).getBounds2D();
        double xOffset = ((base.getTranslateX() - g2.getTransform().getTranslateX()) * (1.0d / base.getScaleX())) + this.matrix.getTranslateX();
        double xOffset2 = xOffset * context.getScaleX() * base.getScaleX();
        double yOffset = ((base.getTranslateY() - g2.getTransform().getTranslateY()) * (1.0d / (-base.getScaleY()))) - this.matrix.getTranslateY();
        double yOffset2 = yOffset * context.getScaleY() * (-base.getScaleY());
        double scaledWidth = bBoxMod.getWidth();
        double scaledHeight = bBoxMod.getHeight();
        int width = (int) Math.round(scaledWidth);
        int height = (int) Math.round(scaledHeight);
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        BufferedImage bi2 = new BufferedImage(width, height, 2);
        Graphics2D canvas = bi2.createGraphics();
        TexturePaint patternPaint = new TexturePaint(bi2, new Rectangle2D.Double(xOffset2, yOffset2, width, height));
        g2.setPaint(patternPaint);
        canvas.setRenderingHints(renderingHints);
        Shapes tilingShapes = getShapes();
        canvas.setClip(0, 0, width, height);
        paintPattern(canvas, tilingShapes, this.matrix, originalPageSpace);
        canvas.dispose();
        bi2.flush();
    }

    private void paintPattern(Graphics2D g2d, Shapes tilingShapes, AffineTransform matrix, AffineTransform base) {
        AffineTransform preAf = g2d.getTransform();
        AffineTransform af2 = new AffineTransform(matrix);
        AffineTransform af22 = new AffineTransform(base);
        af22.concatenate(af2);
        g2d.setTransform(new AffineTransform(af22.getScaleX(), af22.getShearY(), af22.getShearX(), af22.getScaleY(), 0.0d, 0.0d));
        AffineTransform prePaint = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint);
        g2d.translate(this.xStep, 0.0d);
        AffineTransform prePaint2 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint2);
        g2d.translate(0.0d, -this.yStep);
        AffineTransform prePaint3 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint3);
        g2d.translate(-this.xStep, 0.0d);
        AffineTransform prePaint4 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint4);
        g2d.translate(-this.xStep, 0.0d);
        AffineTransform prePaint5 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint5);
        g2d.translate(0.0d, this.yStep);
        AffineTransform prePaint6 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint6);
        g2d.translate(0.0d, this.yStep);
        AffineTransform prePaint7 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint7);
        g2d.translate(this.xStep, 0.0d);
        AffineTransform prePaint8 = g2d.getTransform();
        tilingShapes.paint(g2d);
        g2d.setTransform(prePaint8);
        g2d.translate(this.xStep, 0.0d);
        tilingShapes.paint(g2d);
        g2d.setTransform(preAf);
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public Paint getPaint() {
        return null;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public int getPatternType() {
        return this.patternType;
    }

    public void setPatternType(int patternType) {
        this.patternType = patternType;
    }

    public int getPaintType() {
        return this.paintType;
    }

    public void setPaintType(int paintType) {
        this.paintType = paintType;
    }

    public int getTilingType() {
        return this.tilingType;
    }

    public void setTilingType(int tilingType) {
        this.tilingType = tilingType;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public Rectangle2D getBBox() {
        return this.bBox;
    }

    public Rectangle2D getbBoxMod() {
        return this.bBoxMod;
    }

    public float getXStep() {
        return this.xStep;
    }

    public float getYStep() {
        return this.yStep;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public AffineTransform getMatrix() {
        return this.matrix;
    }

    public AffineTransform getInvMatrix() {
        try {
            return this.matrix.createInverse();
        } catch (NoninvertibleTransformException e2) {
            return null;
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public void setMatrix(AffineTransform matrix) {
        this.matrix = matrix;
    }

    public Shapes getShapes() {
        return this.shapes;
    }

    public void setShapes(Shapes shapes) {
        this.shapes = shapes;
    }

    @Override // org.icepdf.core.pobjects.graphics.Pattern
    public void setParentGraphicState(GraphicsState graphicsState) {
        this.parentGraphicState = graphicsState;
    }

    public GraphicsState getParentGraphicState() {
        return this.parentGraphicState;
    }

    public Color getUnColored() {
        return this.unColored;
    }

    public void setUnColored(Color unColored) {
        this.unColored = unColored;
    }

    @Override // org.icepdf.core.pobjects.Stream, org.icepdf.core.pobjects.Dictionary
    public String toString() {
        return "Tiling Pattern: \n              obj:  " + ((Object) getPObjectReference()) + "\n    patternType: tilling\n      paintType: " + (this.paintType == 1 ? "colored" : "uncoloured") + "\n    tilingType: " + this.tilingType + "\n          bbox: " + ((Object) this.bBox) + "\n         xStep: " + this.xStep + "\n         yStep: " + this.yStep + "\n      resource: " + ((Object) this.resources) + "\n        matrix: " + ((Object) this.matrix);
    }
}
