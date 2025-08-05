package org.icepdf.core.pobjects.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.graphics.commands.ClipDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.NoClipDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/GraphicsState.class */
public class GraphicsState {
    private static final Logger logger = Logger.getLogger(GraphicsState.class.toString());
    public static final Name CA_STROKING_KEY = new Name("CA");
    public static final Name CA_NON_STROKING_KEY = new Name("ca");
    private static boolean enabledOverpaint = Defs.sysPropertyBoolean("org.icepdf.core.overpaint", true);
    private AffineTransform CTM;
    private ClipDrawCmd clipDrawCmd;
    private NoClipDrawCmd noClipDrawCmd;
    private int lineCap;
    private float lineWidth;
    private int lineJoin;
    private float miterLimit;
    private float[] dashArray;
    private float dashPhase;
    private Color fillColor;
    private Color strokeColor;
    private float strokeAlpha;
    private float fillAlpha;
    private SoftMask softMask;
    private int alphaRule;
    private boolean transparencyGroup;
    private boolean isolated;
    private boolean knockOut;
    private PColorSpace fillColorSpace;
    private PColorSpace strokeColorSpace;
    private TextState textState;
    private GraphicsState parentGraphicState;
    private Shapes shapes;
    private Area clip;
    private boolean clipChange;
    private int overprintMode;
    private boolean overprintStroking;
    private boolean overprintOther;

    public GraphicsState(Shapes shapes) {
        this.CTM = new AffineTransform();
        this.clipDrawCmd = new ClipDrawCmd();
        this.noClipDrawCmd = new NoClipDrawCmd();
        this.lineCap = 0;
        this.lineWidth = 1.0f;
        this.lineJoin = 0;
        this.miterLimit = 10.0f;
        this.dashArray = null;
        this.dashPhase = 0.0f;
        this.fillColor = Color.black;
        this.strokeColor = Color.black;
        this.strokeAlpha = 1.0f;
        this.fillAlpha = 1.0f;
        this.alphaRule = 3;
        this.fillColorSpace = new DeviceGray(null, null);
        this.strokeColorSpace = new DeviceGray(null, null);
        this.textState = new TextState();
        this.parentGraphicState = null;
        this.clipChange = false;
        this.shapes = shapes;
    }

    public GraphicsState(GraphicsState parentGraphicsState) {
        this.CTM = new AffineTransform();
        this.clipDrawCmd = new ClipDrawCmd();
        this.noClipDrawCmd = new NoClipDrawCmd();
        this.lineCap = 0;
        this.lineWidth = 1.0f;
        this.lineJoin = 0;
        this.miterLimit = 10.0f;
        this.dashArray = null;
        this.dashPhase = 0.0f;
        this.fillColor = Color.black;
        this.strokeColor = Color.black;
        this.strokeAlpha = 1.0f;
        this.fillAlpha = 1.0f;
        this.alphaRule = 3;
        this.fillColorSpace = new DeviceGray(null, null);
        this.strokeColorSpace = new DeviceGray(null, null);
        this.textState = new TextState();
        this.parentGraphicState = null;
        this.clipChange = false;
        this.CTM = new AffineTransform(parentGraphicsState.CTM);
        this.lineCap = parentGraphicsState.lineCap;
        this.lineWidth = parentGraphicsState.lineWidth;
        this.miterLimit = parentGraphicsState.miterLimit;
        this.lineJoin = parentGraphicsState.lineJoin;
        this.fillColor = new Color(parentGraphicsState.fillColor.getRed(), parentGraphicsState.fillColor.getGreen(), parentGraphicsState.fillColor.getBlue());
        this.strokeColor = new Color(parentGraphicsState.strokeColor.getRed(), parentGraphicsState.strokeColor.getGreen(), parentGraphicsState.strokeColor.getBlue());
        this.shapes = parentGraphicsState.shapes;
        if (parentGraphicsState.clip != null) {
            this.clip = (Area) parentGraphicsState.clip.clone();
        }
        this.fillColorSpace = parentGraphicsState.fillColorSpace;
        this.strokeColorSpace = parentGraphicsState.strokeColorSpace;
        this.textState = new TextState(parentGraphicsState.textState);
        this.dashPhase = parentGraphicsState.getDashPhase();
        this.dashArray = parentGraphicsState.getDashArray();
        this.overprintMode = parentGraphicsState.getOverprintMode();
        this.overprintOther = parentGraphicsState.isOverprintOther();
        this.overprintStroking = parentGraphicsState.isOverprintStroking();
        this.fillAlpha = parentGraphicsState.getFillAlpha();
        this.strokeAlpha = parentGraphicsState.getStrokeAlpha();
        this.alphaRule = parentGraphicsState.getAlphaRule();
        this.softMask = parentGraphicsState.getSoftMask();
    }

    public void setShapes(Shapes shapes) {
        this.shapes = shapes;
    }

    public void translate(double x2, double y2) {
        this.CTM.translate(x2, y2);
        this.shapes.add(new TransformDrawCmd(new AffineTransform(this.CTM)));
    }

    public void scale(double x2, double y2) {
        this.CTM.scale(x2, y2);
        this.shapes.add(new TransformDrawCmd(new AffineTransform(this.CTM)));
    }

    public void set(AffineTransform af2) {
        if (!this.CTM.equals(af2)) {
            this.CTM = new AffineTransform(af2);
        }
        this.shapes.add(new TransformDrawCmd(new AffineTransform(this.CTM)));
    }

    public GraphicsState save() {
        GraphicsState gs = new GraphicsState(this);
        gs.parentGraphicState = this;
        return gs;
    }

    public void concatenate(ExtGState extGState) {
        if (extGState.getLineWidth() != null) {
            setLineWidth(extGState.getLineWidth().floatValue());
        }
        if (extGState.getLineCapStyle() != null) {
            setLineCap(extGState.getLineCapStyle().intValue());
        }
        if (extGState.getLineJoinStyle() != null) {
            setLineJoin(extGState.getLineJoinStyle().intValue());
        }
        if (extGState.getMiterLimit() != null) {
            setMiterLimit(extGState.getMiterLimit().floatValue());
        }
        if (extGState.getLineDashPattern() != null) {
            List dasshPattern = extGState.getLineDashPattern();
            try {
                setDashArray((float[]) dasshPattern.get(0));
                setDashPhase(((Number) dasshPattern.get(1)).floatValue());
            } catch (ClassCastException e2) {
                logger.log(Level.FINE, "Dash cast error: ", (Throwable) e2);
            }
        }
        if (extGState.getNonStrokingAlphConstant() != null) {
            setFillAlpha(extGState.getNonStrokingAlphConstant().floatValue());
        }
        if (extGState.getStrokingAlphConstant() != null) {
            setStrokeAlpha(extGState.getStrokingAlphConstant().floatValue());
        }
        if (extGState.getBlendingMode() != null && extGState.getBlendingMode().equals("Multiply")) {
            setFillAlpha(0.7f);
            setStrokeAlpha(0.7f);
        }
        if (extGState.getOverprintMode() != null) {
            setOverprintMode(extGState.getOverprintMode().intValue());
        }
        if (extGState.getSMask() != null) {
            setSoftMask(extGState.getSMask());
        }
        if (extGState.getSMask() != null) {
            SoftMask sMask = extGState.getSMask();
            setSoftMask(sMask);
        }
        processOverPaint(extGState.getOverprint(), extGState.getOverprintFill());
    }

    private void processOverPaint(Boolean OP, Boolean op) {
        if (enabledOverpaint && OP != null && op == null && this.overprintMode == 1) {
            this.overprintStroking = OP.booleanValue();
            this.overprintOther = this.overprintStroking;
        }
    }

    public GraphicsState restore() {
        if (this.parentGraphicState != null) {
            this.parentGraphicState.set(this.parentGraphicState.CTM);
            if (this.parentGraphicState.clipChange || this.clipChange) {
                if (this.parentGraphicState.clip != null) {
                    if (!this.parentGraphicState.clip.equals(this.clip)) {
                        this.parentGraphicState.shapes.add(new ShapeDrawCmd(new Area(this.parentGraphicState.clip)));
                        this.parentGraphicState.shapes.add(this.clipDrawCmd);
                    }
                } else {
                    this.parentGraphicState.shapes.add(this.noClipDrawCmd);
                }
            }
            this.parentGraphicState.shapes.add(new StrokeDrawCmd(new BasicStroke(this.parentGraphicState.lineWidth, this.parentGraphicState.lineCap, this.parentGraphicState.lineJoin, this.parentGraphicState.miterLimit, this.parentGraphicState.dashArray, this.parentGraphicState.dashPhase)));
            this.parentGraphicState.shapes.add(new ColorDrawCmd(this.parentGraphicState.getFillColor()));
        }
        return this.parentGraphicState;
    }

    public void updateClipCM(AffineTransform af2) {
        if (this.clip != null) {
            AffineTransform afInverse = new AffineTransform();
            try {
                afInverse = af2.createInverse();
            } catch (Exception e2) {
                logger.log(Level.FINER, "Error generating clip inverse.", (Throwable) e2);
            }
            this.clip.transform(afInverse);
        }
    }

    public void setClip(Shape newClip) {
        if (newClip != null) {
            Area area = new Area(newClip);
            if (this.clip != null) {
                area.intersect(this.clip);
            }
            if (this.clip == null || !this.clip.equals(area)) {
                this.clip = new Area(area);
                this.shapes.add(new ShapeDrawCmd(new Area(area)));
                this.shapes.add(this.clipDrawCmd);
                this.clipChange = true;
                return;
            }
            this.clip = new Area(area);
            return;
        }
        this.clip = null;
        this.shapes.add(this.noClipDrawCmd);
    }

    public Area getClip() {
        return this.clip;
    }

    public AffineTransform getCTM() {
        return this.CTM;
    }

    public void setCTM(AffineTransform ctm) {
        this.CTM = ctm;
    }

    public int getLineCap() {
        return this.lineCap;
    }

    public void setLineCap(int lineCap) {
        this.lineCap = lineCap;
    }

    public float getLineWidth() {
        return this.lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        if (lineWidth <= Float.MIN_VALUE || lineWidth >= Float.MAX_VALUE || lineWidth == 0.0f) {
            this.lineWidth = 0.001f;
        } else {
            this.lineWidth = lineWidth;
        }
    }

    public int getLineJoin() {
        return this.lineJoin;
    }

    public void setLineJoin(int lineJoin) {
        this.lineJoin = lineJoin;
    }

    public float[] getDashArray() {
        return this.dashArray;
    }

    public void setDashArray(float[] dashArray) {
        this.dashArray = dashArray;
    }

    public float getDashPhase() {
        return this.dashPhase;
    }

    public void setDashPhase(float dashPhase) {
        this.dashPhase = dashPhase;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public void setMiterLimit(float miterLimit) {
        this.miterLimit = miterLimit;
    }

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeAlpha(float alpha) {
        this.strokeAlpha = alpha;
    }

    public float getStrokeAlpha() {
        return this.strokeAlpha;
    }

    public void setFillAlpha(float alpha) {
        this.fillAlpha = alpha;
    }

    public float getFillAlpha() {
        return this.fillAlpha;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public PColorSpace getFillColorSpace() {
        return this.fillColorSpace;
    }

    public void setFillColorSpace(PColorSpace fillColorSpace) {
        this.fillColorSpace = fillColorSpace;
    }

    public PColorSpace getStrokeColorSpace() {
        return this.strokeColorSpace;
    }

    public void setStrokeColorSpace(PColorSpace strokeColorSpace) {
        this.strokeColorSpace = strokeColorSpace;
    }

    public TextState getTextState() {
        return this.textState;
    }

    public void setTextState(TextState textState) {
        this.textState = textState;
    }

    public int getOverprintMode() {
        return this.overprintMode;
    }

    public boolean isOverprintStroking() {
        return this.overprintStroking;
    }

    public boolean isOverprintOther() {
        return this.overprintOther;
    }

    public void setOverprintMode(int overprintMode) {
        this.overprintMode = overprintMode;
    }

    public void setOverprintStroking(boolean overprintStroking) {
        this.overprintStroking = overprintStroking;
    }

    public void setOverprintOther(boolean overprintOther) {
        this.overprintOther = overprintOther;
    }

    public int getAlphaRule() {
        return this.alphaRule;
    }

    public void setAlphaRule(int alphaRule) {
        this.alphaRule = alphaRule;
    }

    public boolean isTransparencyGroup() {
        return this.transparencyGroup;
    }

    public void setTransparencyGroup(boolean transparencyGroup) {
        this.transparencyGroup = transparencyGroup;
    }

    public boolean isIsolated() {
        return this.isolated;
    }

    public void setIsolated(boolean isolated) {
        this.isolated = isolated;
    }

    public boolean isKnockOut() {
        return this.knockOut;
    }

    public void setKnockOut(boolean knockOut) {
        this.knockOut = knockOut;
    }

    public SoftMask getSoftMask() {
        return this.softMask;
    }

    public void setSoftMask(SoftMask softMask) {
        this.softMask = softMask;
    }
}
