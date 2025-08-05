package org.icepdf.core.util.content;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.OptionalContent;
import org.icepdf.core.pobjects.OptionalContentGroup;
import org.icepdf.core.pobjects.OptionalContents;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.fonts.Font;
import org.icepdf.core.pobjects.fonts.FontFactory;
import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.graphics.DeviceCMYK;
import org.icepdf.core.pobjects.graphics.DeviceGray;
import org.icepdf.core.pobjects.graphics.DeviceN;
import org.icepdf.core.pobjects.graphics.DeviceRGB;
import org.icepdf.core.pobjects.graphics.ExtGState;
import org.icepdf.core.pobjects.graphics.GlyphOutlineClip;
import org.icepdf.core.pobjects.graphics.GraphicsState;
import org.icepdf.core.pobjects.graphics.ImageReference;
import org.icepdf.core.pobjects.graphics.ImageReferenceFactory;
import org.icepdf.core.pobjects.graphics.PColorSpace;
import org.icepdf.core.pobjects.graphics.Pattern;
import org.icepdf.core.pobjects.graphics.PatternColor;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.TextSprite;
import org.icepdf.core.pobjects.graphics.TextState;
import org.icepdf.core.pobjects.graphics.TilingPattern;
import org.icepdf.core.pobjects.graphics.commands.AlphaDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ClipDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.FillDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.FormDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ImageDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.NoClipDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.OCGEndDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.OCGStartDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.PaintDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ShapesDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TextSpriteDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TilingPatternDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.pobjects.graphics.text.GlyphText;
import org.icepdf.core.pobjects.graphics.text.PageText;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/content/AbstractContentParser.class */
public abstract class AbstractContentParser implements ContentParser {
    public static final float OVERPAINT_ALPHA = 0.4f;
    protected GraphicsState graphicState;
    protected Library library;
    protected Resources resources;
    protected Shapes shapes;
    protected LinkedList<OptionalContents> oCGs;
    protected GeneralPath geometricPath;
    protected boolean inTextBlock;
    protected AffineTransform textBlockBase;
    protected float glyph2UserSpaceScale = 1.0f;
    protected AtomicInteger imageIndex = new AtomicInteger(1);
    protected Stack<Object> stack = new Stack<>();
    private static final Logger logger = Logger.getLogger(AbstractContentParser.class.toString());
    private static boolean disableTransparencyGroups = Defs.sysPropertyBoolean("org.icepdf.core.disableTransparencyGroup", false);
    private static boolean enabledOverPrint = Defs.sysPropertyBoolean("org.icepdf.core.enabledOverPrint", true);
    private static ClipDrawCmd clipDrawCmd = new ClipDrawCmd();
    private static NoClipDrawCmd noClipDrawCmd = new NoClipDrawCmd();

    @Override // org.icepdf.core.util.content.ContentParser
    public abstract ContentParser parse(byte[][] bArr, Page page) throws InterruptedException, IOException;

    @Override // org.icepdf.core.util.content.ContentParser
    public abstract Shapes parseTextBlocks(byte[][] bArr) throws UnsupportedEncodingException;

    public AbstractContentParser(Library l2, Resources r2) {
        this.library = l2;
        this.resources = r2;
    }

    @Override // org.icepdf.core.util.content.ContentParser
    public Shapes getShapes() {
        this.shapes.contract();
        return this.shapes;
    }

    @Override // org.icepdf.core.util.content.ContentParser
    public Stack<Object> getStack() {
        return this.stack;
    }

    @Override // org.icepdf.core.util.content.ContentParser
    public GraphicsState getGraphicsState() {
        return this.graphicState;
    }

    @Override // org.icepdf.core.util.content.ContentParser
    public void setGraphicsState(GraphicsState graphicState) {
        this.graphicState = graphicState;
    }

    protected static void consume_G(GraphicsState graphicState, Stack stack, Library library) {
        float gray = ((Number) stack.pop()).floatValue();
        graphicState.setStrokeColorSpace(PColorSpace.getColorSpace(library, DeviceGray.DEVICEGRAY_KEY));
        graphicState.setStrokeColor(new Color(gray, gray, gray));
    }

    protected static void consume_g(GraphicsState graphicState, Stack stack, Library library) {
        float gray = ((Number) stack.pop()).floatValue();
        graphicState.setFillColorSpace(PColorSpace.getColorSpace(library, DeviceGray.DEVICEGRAY_KEY));
        graphicState.setFillColor(new Color(gray, gray, gray));
    }

    protected static void consume_RG(GraphicsState graphicState, Stack stack, Library library) {
        float b2 = ((Number) stack.pop()).floatValue();
        float gg = ((Number) stack.pop()).floatValue();
        float r2 = ((Number) stack.pop()).floatValue();
        float b3 = Math.max(0.0f, Math.min(1.0f, b2));
        float gg2 = Math.max(0.0f, Math.min(1.0f, gg));
        float r3 = Math.max(0.0f, Math.min(1.0f, r2));
        graphicState.setStrokeColorSpace(PColorSpace.getColorSpace(library, DeviceRGB.DEVICERGB_KEY));
        graphicState.setStrokeColor(new Color(r3, gg2, b3));
    }

    protected static void consume_rg(GraphicsState graphicState, Stack stack, Library library) {
        float b2 = ((Number) stack.pop()).floatValue();
        float gg = ((Number) stack.pop()).floatValue();
        float r2 = ((Number) stack.pop()).floatValue();
        float b3 = Math.max(0.0f, Math.min(1.0f, b2));
        float gg2 = Math.max(0.0f, Math.min(1.0f, gg));
        float r3 = Math.max(0.0f, Math.min(1.0f, r2));
        graphicState.setFillColorSpace(PColorSpace.getColorSpace(library, DeviceRGB.DEVICERGB_KEY));
        graphicState.setFillColor(new Color(r3, gg2, b3));
    }

    protected static void consume_K(GraphicsState graphicState, Stack stack, Library library) {
        float k2 = ((Number) stack.pop()).floatValue();
        float y2 = ((Number) stack.pop()).floatValue();
        float m2 = ((Number) stack.pop()).floatValue();
        float c2 = ((Number) stack.pop()).floatValue();
        PColorSpace pColorSpace = PColorSpace.getColorSpace(library, DeviceCMYK.DEVICECMYK_KEY);
        graphicState.setStrokeColorSpace(pColorSpace);
        graphicState.setStrokeColor(pColorSpace.getColor(new float[]{k2, y2, m2, c2}, true));
    }

    protected static void consume_k(GraphicsState graphicState, Stack stack, Library library) {
        float k2 = ((Number) stack.pop()).floatValue();
        float y2 = ((Number) stack.pop()).floatValue();
        float m2 = ((Number) stack.pop()).floatValue();
        float c2 = ((Number) stack.pop()).floatValue();
        PColorSpace pColorSpace = PColorSpace.getColorSpace(library, DeviceCMYK.DEVICECMYK_KEY);
        graphicState.setFillColorSpace(pColorSpace);
        graphicState.setFillColor(pColorSpace.getColor(new float[]{k2, y2, m2, c2}, true));
    }

    protected static void consume_CS(GraphicsState graphicState, Stack stack, Resources resources) {
        Name n2 = (Name) stack.pop();
        graphicState.setStrokeColorSpace(resources.getColorSpace(n2));
    }

    protected static void consume_cs(GraphicsState graphicState, Stack stack, Resources resources) {
        Name n2 = (Name) stack.pop();
        graphicState.setFillColorSpace(resources.getColorSpace(n2));
    }

    protected static void consume_ri(Stack stack) {
        stack.pop();
    }

    protected static void consume_SC(GraphicsState graphicState, Stack stack, Library library, Resources resources, boolean isTint) {
        Object o2 = stack.peek();
        if (!(o2 instanceof Name)) {
            if (o2 instanceof Number) {
                int colorSpaceN = graphicState.getStrokeColorSpace().getNumComponents();
                int nCount = 0;
                float[] colour = new float[4];
                while (!stack.isEmpty() && (stack.peek() instanceof Number) && nCount < 4) {
                    colour[nCount] = ((Number) stack.pop()).floatValue();
                    nCount++;
                }
                if (nCount != colorSpaceN) {
                    graphicState.setStrokeColorSpace(PColorSpace.getColorSpace(library, nCount));
                }
                float[] f2 = new float[nCount];
                System.arraycopy(colour, 0, f2, 0, nCount);
                graphicState.setStrokeColor(graphicState.getStrokeColorSpace().getColor(f2, isTint));
                return;
            }
            return;
        }
        Name patternName = (Name) stack.pop();
        Pattern pattern = resources.getPattern(patternName);
        if (graphicState.getStrokeColorSpace() instanceof PatternColor) {
            ((PatternColor) graphicState.getStrokeColorSpace()).setPattern(pattern);
        } else {
            PatternColor pc = new PatternColor(null, null);
            pc.setPattern(pattern);
            graphicState.setStrokeColorSpace(pc);
        }
        if (pattern instanceof TilingPattern) {
            TilingPattern tilingPattern = (TilingPattern) pattern;
            if (tilingPattern.getPaintType() == 2) {
                int compLength = graphicState.getStrokeColorSpace().getNumComponents();
                float[] colour2 = new float[compLength];
                for (int nCount2 = 0; !stack.isEmpty() && (stack.peek() instanceof Number) && nCount2 < compLength; nCount2++) {
                    colour2[nCount2] = ((Number) stack.pop()).floatValue();
                }
                Color color = graphicState.getStrokeColorSpace().getColor(colour2, isTint);
                graphicState.setStrokeColor(color);
                tilingPattern.setUnColored(color);
            }
        }
    }

    protected static void consume_sc(GraphicsState graphicState, Stack stack, Library library, Resources resources, boolean isTint) {
        Object o2 = null;
        if (!stack.isEmpty()) {
            o2 = stack.peek();
        }
        if (!(o2 instanceof Name)) {
            if (o2 instanceof Number) {
                int colorSpaceN = graphicState.getFillColorSpace().getNumComponents();
                int nCount = 0;
                float[] colour = new float[5];
                while (!stack.isEmpty() && (stack.peek() instanceof Number) && nCount < 5) {
                    colour[nCount] = ((Number) stack.pop()).floatValue();
                    nCount++;
                }
                if (nCount != colorSpaceN) {
                    graphicState.setFillColorSpace(PColorSpace.getColorSpace(library, nCount));
                }
                float[] f2 = new float[nCount];
                System.arraycopy(colour, 0, f2, 0, nCount);
                graphicState.setFillColor(graphicState.getFillColorSpace().getColor(f2, true));
                return;
            }
            return;
        }
        Name patternName = (Name) stack.pop();
        Pattern pattern = resources.getPattern(patternName);
        if (graphicState.getFillColorSpace() instanceof PatternColor) {
            ((PatternColor) graphicState.getFillColorSpace()).setPattern(pattern);
        } else {
            PatternColor pc = new PatternColor(library, null);
            pc.setPattern(pattern);
            graphicState.setFillColorSpace(pc);
        }
        if (pattern instanceof TilingPattern) {
            TilingPattern tilingPattern = (TilingPattern) pattern;
            if (tilingPattern.getPaintType() == 2) {
                int compLength = graphicState.getFillColorSpace().getNumComponents();
                float[] colour2 = new float[compLength];
                for (int nCount2 = 0; !stack.isEmpty() && (stack.peek() instanceof Number) && nCount2 < compLength; nCount2++) {
                    colour2[nCount2] = ((Number) stack.pop()).floatValue();
                }
                Color color = graphicState.getFillColorSpace().getColor(colour2, isTint);
                graphicState.setFillColor(color);
                tilingPattern.setUnColored(color);
            }
        }
    }

    protected static GraphicsState consume_q(GraphicsState graphicState) {
        return graphicState.save();
    }

    protected GraphicsState consume_Q(GraphicsState graphicState, Shapes shapes) {
        GraphicsState graphicState2;
        GraphicsState gs1 = graphicState.restore();
        if (gs1 != null) {
            graphicState2 = gs1;
        } else {
            graphicState2 = new GraphicsState(shapes);
            graphicState2.set(new AffineTransform());
            shapes.add(noClipDrawCmd);
        }
        return graphicState2;
    }

    protected static void consume_cm(GraphicsState graphicState, Stack stack, boolean inTextBlock, AffineTransform textBlockBase) {
        float f2 = ((Number) stack.pop()).floatValue();
        float e2 = ((Number) stack.pop()).floatValue();
        float d2 = ((Number) stack.pop()).floatValue();
        float c2 = ((Number) stack.pop()).floatValue();
        float b2 = ((Number) stack.pop()).floatValue();
        float a2 = ((Number) stack.pop()).floatValue();
        AffineTransform af2 = new AffineTransform(graphicState.getCTM());
        af2.concatenate(new AffineTransform(a2, b2, c2, d2, e2, f2));
        graphicState.set(af2);
        graphicState.updateClipCM(new AffineTransform(a2, b2, c2, d2, e2, f2));
        if (inTextBlock) {
            AffineTransform af3 = new AffineTransform(textBlockBase);
            graphicState.getTextState().tmatrix = new AffineTransform(a2, b2, c2, d2, e2, f2);
            af3.concatenate(graphicState.getTextState().tmatrix);
            graphicState.set(af3);
            textBlockBase.setTransform(new AffineTransform(graphicState.getCTM()));
        }
    }

    protected static void consume_i(Stack stack) {
        stack.pop();
    }

    protected static void consume_J(GraphicsState graphicState, Stack stack, Shapes shapes) {
        graphicState.setLineCap((int) ((Number) stack.pop()).floatValue());
        if (graphicState.getLineCap() == 0) {
            graphicState.setLineCap(0);
        } else if (graphicState.getLineCap() == 1) {
            graphicState.setLineCap(1);
        } else if (graphicState.getLineCap() == 2) {
            graphicState.setLineCap(2);
        }
        setStroke(shapes, graphicState);
    }

    protected static GraphicsState consume_Do(GraphicsState graphicState, Stack stack, Shapes shapes, Resources resources, boolean viewParse, AtomicInteger imageIndex, Page page) {
        Name xobjectName = (Name) stack.pop();
        if (resources != null && resources.isForm(xobjectName)) {
            GraphicsState graphicState2 = graphicState.save();
            Form formXObject = resources.getForm(xobjectName);
            if (formXObject != null) {
                Object oc = formXObject.getObject(OptionalContent.OC_KEY);
                if (oc != null) {
                    OptionalContent optionalContent = resources.getLibrary().getCatalog().getOptionalContent();
                    optionalContent.init();
                    if (!optionalContent.isVisible(oc)) {
                        return graphicState2;
                    }
                }
                GraphicsState xformGraphicsState = new GraphicsState(graphicState2);
                formXObject.setGraphicsState(xformGraphicsState);
                if (formXObject.isTransparencyGroup()) {
                    xformGraphicsState.setTransparencyGroup(formXObject.isTransparencyGroup());
                    xformGraphicsState.setIsolated(formXObject.isIsolated());
                    xformGraphicsState.setKnockOut(formXObject.isKnockOut());
                }
                formXObject.setParentResources(resources);
                formXObject.init();
                AffineTransform af2 = new AffineTransform(graphicState2.getCTM());
                af2.concatenate(formXObject.getMatrix());
                shapes.add(new TransformDrawCmd(af2));
                if (graphicState2.getClip() != null) {
                    AffineTransform matrix = formXObject.getMatrix();
                    Area bbox = new Area(formXObject.getBBox());
                    Area clip = graphicState2.getClip();
                    try {
                        matrix = matrix.createInverse();
                    } catch (NoninvertibleTransformException e2) {
                        logger.warning("Error create xObject matrix inverse");
                    }
                    Shape shape = matrix.createTransformedShape(clip);
                    bbox.intersect(new Area(shape));
                    shapes.add(new ShapeDrawCmd(bbox));
                } else {
                    shapes.add(new ShapeDrawCmd(formXObject.getBBox()));
                }
                shapes.add(clipDrawCmd);
                setAlpha(shapes, graphicState2.getAlphaRule(), graphicState2.getFillAlpha());
                if ((!disableTransparencyGroups || (formXObject.getGraphicsState() != null && formXObject.getGraphicsState().getSoftMask() != null)) && formXObject.isTransparencyGroup() && formXObject.getBBox().getWidth() < 32767.0d && formXObject.getBBox().getHeight() < 32767.0d) {
                    shapes.add(new FormDrawCmd(formXObject));
                } else {
                    shapes.add(new ShapesDrawCmd(formXObject.getShapes()));
                }
                if (formXObject.getShapes() != null && formXObject.getShapes().getPageText() != null) {
                    formXObject.getShapes().getPageText().applyXObjectTransform(graphicState2.getCTM());
                    PageText pageText = formXObject.getShapes().getPageText();
                    if (pageText != null && pageText.getPageLines() != null) {
                        shapes.getPageText().addPageLines(pageText.getPageLines());
                    }
                }
                shapes.add(new NoClipDrawCmd());
            }
            graphicState = graphicState2.restore();
        } else if (viewParse) {
            setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
            ImageStream imageStream = resources.getImageStream(xobjectName);
            if (imageStream != null) {
                Object oc2 = imageStream.getObject(OptionalContent.OC_KEY);
                if (oc2 != null) {
                    OptionalContent optionalContent2 = resources.getLibrary().getCatalog().getOptionalContent();
                    optionalContent2.init();
                    if (!optionalContent2.isVisible(oc2)) {
                        return graphicState;
                    }
                }
                ImageReference imageReference = ImageReferenceFactory.getImageReference(imageStream, resources, graphicState.getFillColor(), Integer.valueOf(imageIndex.get()), page);
                imageIndex.incrementAndGet();
                if (imageReference != null) {
                    AffineTransform af3 = new AffineTransform(graphicState.getCTM());
                    graphicState.scale(1.0d, -1.0d);
                    graphicState.translate(0.0d, -1.0d);
                    shapes.add(new ImageDrawCmd(imageReference));
                    graphicState.set(af3);
                }
            }
        }
        return graphicState;
    }

    protected static void consume_d(GraphicsState graphicState, Stack stack, Shapes shapes) {
        float[] dashArray;
        try {
            float dashPhase = Math.abs(((Number) stack.pop()).floatValue());
            List dashVector = (List) stack.pop();
            if (dashVector.size() > 0) {
                int sz = dashVector.size();
                dashArray = new float[sz];
                for (int i2 = 0; i2 < sz; i2++) {
                    dashArray[i2] = Math.abs(((Number) dashVector.get(i2)).floatValue());
                }
                if (dashArray.length > 1 && dashArray[0] != 0.0f && dashArray[0] < dashArray[1] / 10000.0f) {
                    dashArray[0] = dashArray[1];
                }
            } else {
                dashPhase = 0.0f;
                dashArray = null;
            }
            graphicState.setDashArray(dashArray);
            graphicState.setDashPhase(dashPhase);
        } catch (ClassCastException e2) {
            logger.log(Level.FINE, "Dash pattern syntax error: ", (Throwable) e2);
        }
        setStroke(shapes, graphicState);
    }

    protected static void consume_j(GraphicsState graphicState, Stack stack, Shapes shapes) {
        graphicState.setLineJoin((int) ((Number) stack.pop()).floatValue());
        if (graphicState.getLineJoin() == 0) {
            graphicState.setLineJoin(0);
        } else if (graphicState.getLineJoin() == 1) {
            graphicState.setLineJoin(1);
        } else if (graphicState.getLineJoin() == 2) {
            graphicState.setLineJoin(2);
        }
        setStroke(shapes, graphicState);
    }

    protected static void consume_w(GraphicsState graphicState, Stack stack, Shapes shapes, float glyph2UserSpaceScale) {
        if (!stack.isEmpty()) {
            float scale = ((Number) stack.pop()).floatValue() * glyph2UserSpaceScale;
            graphicState.setLineWidth(scale);
            setStroke(shapes, graphicState);
        }
    }

    protected static void consume_M(GraphicsState graphicState, Stack stack, Shapes shapes) {
        graphicState.setMiterLimit(((Number) stack.pop()).floatValue());
        setStroke(shapes, graphicState);
    }

    protected static void consume_gs(GraphicsState graphicState, Stack stack, Resources resources) {
        ExtGState extGState;
        Object gs = stack.pop();
        if ((gs instanceof Name) && resources != null && (extGState = resources.getExtGState((Name) gs)) != null) {
            graphicState.concatenate(extGState);
        }
    }

    protected static void consume_Tf(GraphicsState graphicState, Stack stack, Resources resources) {
        Reference fontRef;
        float size = ((Number) stack.pop()).floatValue();
        Name name2 = (Name) stack.pop();
        graphicState.getTextState().font = resources.getFont(name2);
        if (graphicState.getTextState().font == null || graphicState.getTextState().font.getFont() == null) {
            FontFactory fontFactory = FontFactory.getInstance();
            boolean awtState = fontFactory.isAwtFontSubstitution();
            fontFactory.setAwtFontSubstitution(true);
            try {
                Page page = resources.getLibrary().getCatalog().getPageTree().getPage(0);
                page.init();
                Resources res = page.getResources();
                Object pageFonts = res.getEntries().get(Resources.FONT_KEY);
                if ((pageFonts instanceof HashMap) && (fontRef = (Reference) ((HashMap) pageFonts).get(name2)) != null) {
                    graphicState.getTextState().font = (Font) resources.getLibrary().getObject(fontRef);
                    graphicState.getTextState().font.init();
                }
            } catch (Throwable th) {
                logger.warning("Warning could not find font by named resource " + ((Object) name2));
            }
            fontFactory.setAwtFontSubstitution(awtState);
        }
        if (graphicState.getTextState().font != null) {
            graphicState.getTextState().currentfont = graphicState.getTextState().font.getFont().deriveFont(size);
        }
    }

    protected static void consume_Tc(GraphicsState graphicState, Stack stack) {
        graphicState.getTextState().cspace = ((Number) stack.pop()).floatValue();
    }

    protected static void consume_tm(GraphicsState graphicState, Stack stack, TextMetrics textMetrics, PageText pageText, double previousBTStart, AffineTransform textBlockBase, LinkedList<OptionalContents> oCGs) {
        textMetrics.setShift(0.0f);
        textMetrics.setPreviousAdvance(0.0f);
        textMetrics.getAdvance().setLocation(0.0f, 0.0f);
        float[] tm = new float[6];
        tm[0] = 1.0f;
        tm[1] = 0.0f;
        tm[2] = 0.0f;
        tm[3] = 1.0f;
        tm[4] = 0.0f;
        tm[5] = 0.0f;
        int hits = 5;
        int max = stack.size();
        for (int i2 = 0; hits != -1 && i2 < max; i2++) {
            Object next = stack.pop();
            if (next instanceof Number) {
                tm[hits] = ((Number) next).floatValue();
                hits--;
            }
        }
        AffineTransform af2 = new AffineTransform(textBlockBase);
        graphicState.getCTM().getTranslateY();
        graphicState.getCTM().getScaleY();
        graphicState.getTextState().tmatrix = new AffineTransform(tm);
        af2.concatenate(graphicState.getTextState().tmatrix);
        graphicState.set(af2);
        graphicState.scale(1.0d, -1.0d);
        if (textMetrics.isYstart()) {
            textMetrics.setyBTStart(tm[5]);
            textMetrics.setYstart(false);
        }
    }

    protected static void consume_T_star(GraphicsState graphicState, TextMetrics textMetrics, PageText pageText, LinkedList<OptionalContents> oCGs) {
        graphicState.translate(-textMetrics.getShift(), 0.0d);
        textMetrics.setShift(0.0f);
        textMetrics.setPreviousAdvance(0.0f);
        textMetrics.getAdvance().setLocation(0.0f, 0.0f);
        graphicState.translate(0.0d, graphicState.getTextState().leading);
        pageText.newLine(oCGs);
    }

    protected static void consume_TD(GraphicsState graphicState, Stack stack, TextMetrics textMetrics, PageText pageText, LinkedList<OptionalContents> oCGs) {
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        graphicState.translate(-textMetrics.getShift(), 0.0d);
        textMetrics.setShift(0.0f);
        textMetrics.setPreviousAdvance(0.0f);
        textMetrics.getAdvance().setLocation(0.0f, 0.0f);
        graphicState.translate(x2, -y2);
        graphicState.getTextState().leading = -y2;
        if (textMetrics.isYstart()) {
            textMetrics.setyBTStart(y2);
            textMetrics.setYstart(false);
        }
    }

    protected static void consume_double_quote(GraphicsState graphicState, Stack stack, Shapes shapes, TextMetrics textMetrics, GlyphOutlineClip glyphOutlineClip, LinkedList<OptionalContents> oCGs) {
        StringObject stringObject = (StringObject) stack.pop();
        graphicState.getTextState().cspace = ((Number) stack.pop()).floatValue();
        graphicState.getTextState().wspace = ((Number) stack.pop()).floatValue();
        graphicState.translate(-textMetrics.getShift(), graphicState.getTextState().leading);
        setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
        textMetrics.setShift(0.0f);
        textMetrics.setPreviousAdvance(0.0f);
        textMetrics.getAdvance().setLocation(0.0f, 0.0f);
        TextState textState = graphicState.getTextState();
        AffineTransform tmp = applyTextScaling(graphicState);
        drawString(stringObject.getLiteralStringBuffer(textState.font.getSubTypeFormat(), textState.font.getFont()), textMetrics, graphicState.getTextState(), shapes, glyphOutlineClip, graphicState, oCGs);
        graphicState.set(tmp);
        graphicState.translate(textMetrics.getAdvance().f12396x, 0.0d);
        float shift = textMetrics.getShift();
        textMetrics.setShift(shift + textMetrics.getAdvance().f12396x);
    }

    protected static void consume_single_quote(GraphicsState graphicState, Stack stack, Shapes shapes, TextMetrics textMetrics, GlyphOutlineClip glyphOutlineClip, LinkedList<OptionalContents> oCGs) {
        consume_T_star(graphicState, textMetrics, shapes.getPageText(), oCGs);
        consume_Tj(graphicState, stack, shapes, textMetrics, glyphOutlineClip, oCGs);
    }

    protected static void consume_Td(GraphicsState graphicState, Stack stack, TextMetrics textMetrics, PageText pageText, double previousBTStart, LinkedList<OptionalContents> oCGs) {
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        graphicState.getCTM().getTranslateY();
        graphicState.translate(-textMetrics.getShift(), 0.0d);
        textMetrics.setShift(0.0f);
        textMetrics.setPreviousAdvance(0.0f);
        textMetrics.getAdvance().setLocation(0.0f, 0.0f);
        graphicState.translate(x2, -y2);
        float newY = (float) graphicState.getCTM().getTranslateY();
        if (textMetrics.isYstart()) {
            textMetrics.setyBTStart(newY);
            textMetrics.setYstart(false);
        }
    }

    protected static void consume_Tz(GraphicsState graphicState, Stack stack) {
        Object ob = stack.pop();
        if (ob instanceof Number) {
            float hScaling = ((Number) ob).floatValue();
            graphicState.getTextState().hScalling = hScaling / 100.0f;
        }
    }

    protected static void consume_Tw(GraphicsState graphicState, Stack stack) {
        graphicState.getTextState().wspace = ((Number) stack.pop()).floatValue();
    }

    protected static void consume_Tr(GraphicsState graphicState, Stack stack) {
        graphicState.getTextState().rmode = (int) ((Number) stack.pop()).floatValue();
    }

    protected static void consume_TL(GraphicsState graphicState, Stack stack) {
        graphicState.getTextState().leading = ((Number) stack.pop()).floatValue();
    }

    protected static void consume_Ts(GraphicsState graphicState, Stack stack) {
        graphicState.getTextState().trise = ((Number) stack.pop()).floatValue();
    }

    protected static GeneralPath consume_L(Stack stack, GeneralPath geometricPath) {
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        if (geometricPath == null) {
            geometricPath = new GeneralPath();
        }
        geometricPath.lineTo(x2, y2);
        return geometricPath;
    }

    protected static GeneralPath consume_m(Stack stack, GeneralPath geometricPath) {
        if (geometricPath == null) {
            geometricPath = new GeneralPath();
        }
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        geometricPath.moveTo(x2, y2);
        return geometricPath;
    }

    protected static GeneralPath consume_c(Stack stack, GeneralPath geometricPath) {
        if (!stack.isEmpty()) {
            float y3 = ((Number) stack.pop()).floatValue();
            float x3 = ((Number) stack.pop()).floatValue();
            float y2 = ((Number) stack.pop()).floatValue();
            float x2 = ((Number) stack.pop()).floatValue();
            float y1 = ((Number) stack.pop()).floatValue();
            float x1 = ((Number) stack.pop()).floatValue();
            if (geometricPath == null) {
                geometricPath = new GeneralPath();
            }
            geometricPath.curveTo(x1, y1, x2, y2, x3, y3);
        }
        return geometricPath;
    }

    protected static GeneralPath consume_S(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) {
        if (geometricPath != null) {
            commonStroke(graphicState, shapes, geometricPath);
            geometricPath = null;
        }
        return geometricPath;
    }

    protected static GeneralPath consume_F(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(1);
            commonFill(shapes, graphicState, geometricPath);
        }
        return null;
    }

    protected static GeneralPath consume_f(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(1);
            commonFill(shapes, graphicState, geometricPath);
        }
        return null;
    }

    protected static GeneralPath consume_re(Stack stack, GeneralPath geometricPath) {
        if (geometricPath == null) {
            geometricPath = new GeneralPath();
        }
        float h2 = ((Number) stack.pop()).floatValue();
        float w2 = ((Number) stack.pop()).floatValue();
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        geometricPath.moveTo(x2, y2);
        geometricPath.lineTo(x2 + w2, y2);
        geometricPath.lineTo(x2 + w2, y2 + h2);
        geometricPath.lineTo(x2, y2 + h2);
        geometricPath.lineTo(x2, y2);
        return geometricPath;
    }

    protected static void consume_h(GeneralPath geometricPath) {
        if (geometricPath != null) {
            geometricPath.closePath();
        }
    }

    protected static void consume_BDC(Stack stack, Shapes shapes, LinkedList<OptionalContents> oCGs, Resources resources) {
        Object properties = stack.pop();
        Name tag = (Name) stack.pop();
        OptionalContents optionalContents = null;
        if (tag.equals(OptionalContent.OC_KEY) && (properties instanceof Name)) {
            optionalContents = resources.getPropertyEntry((Name) properties);
            if (optionalContents != null) {
                optionalContents.init();
                shapes.add(new OCGStartDrawCmd(optionalContents));
            }
        }
        if (optionalContents == null) {
            Name tmp = OptionalContent.NONE_OC_FLAG;
            if (properties instanceof Name) {
                tmp = (Name) properties;
            }
            optionalContents = new OptionalContentGroup(tmp.getName(), true);
        }
        if (oCGs != null) {
            oCGs.add(optionalContents);
        }
    }

    protected static void consume_EMC(Shapes shapes, LinkedList<OptionalContents> oCGs) {
        if (oCGs != null && !oCGs.isEmpty()) {
            OptionalContents optionalContents = oCGs.removeLast();
            if (optionalContents.isOCG()) {
                shapes.add(new OCGEndDrawCmd());
            }
        }
    }

    protected static void consume_BMC(Stack stack, Shapes shapes, LinkedList<OptionalContents> oCGs, Resources resources) {
        Object properties = stack.pop();
        if (properties instanceof Name) {
            OptionalContents optionalContents = resources.getPropertyEntry((Name) properties);
            if (optionalContents != null) {
                optionalContents.init();
                shapes.add(new OCGStartDrawCmd(optionalContents));
            } else {
                Name tmp = (Name) properties;
                optionalContents = new OptionalContentGroup(tmp.getName(), true);
            }
            if (oCGs != null) {
                oCGs.add(optionalContents);
            }
        }
    }

    protected static GeneralPath consume_f_star(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(0);
            commonFill(shapes, graphicState, geometricPath);
        }
        return null;
    }

    protected static GeneralPath consume_b(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(1);
            geometricPath.closePath();
            commonFill(shapes, graphicState, geometricPath);
            commonStroke(graphicState, shapes, geometricPath);
        }
        return null;
    }

    protected static GeneralPath consume_n(GeneralPath geometricPath) throws NoninvertibleTransformException {
        return null;
    }

    protected static void consume_W(GraphicsState graphicState, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(1);
            geometricPath.closePath();
            graphicState.setClip(geometricPath);
        }
    }

    protected static void consume_v(Stack stack, GeneralPath geometricPath) {
        float y3 = ((Number) stack.pop()).floatValue();
        float x3 = ((Number) stack.pop()).floatValue();
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        geometricPath.curveTo((float) geometricPath.getCurrentPoint().getX(), (float) geometricPath.getCurrentPoint().getY(), x2, y2, x3, y3);
    }

    protected static void consume_y(Stack stack, GeneralPath geometricPath) {
        float y3 = ((Number) stack.pop()).floatValue();
        float x3 = ((Number) stack.pop()).floatValue();
        float y1 = ((Number) stack.pop()).floatValue();
        float x1 = ((Number) stack.pop()).floatValue();
        geometricPath.curveTo(x1, y1, x3, y3, x3, y3);
    }

    protected static GeneralPath consume_B(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(1);
            commonFill(shapes, graphicState, geometricPath);
            commonStroke(graphicState, shapes, geometricPath);
        }
        return null;
    }

    protected static GraphicsState consume_d0(GraphicsState graphicState, Stack stack) {
        GraphicsState graphicState2 = graphicState.save();
        float y2 = ((Number) stack.pop()).floatValue();
        float x2 = ((Number) stack.pop()).floatValue();
        TextState textState = graphicState2.getTextState();
        textState.setType3HorizontalDisplacement(new Point2D.Float(x2, y2));
        return graphicState2;
    }

    protected static GeneralPath consume_s(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) {
        if (geometricPath != null) {
            geometricPath.closePath();
            commonStroke(graphicState, shapes, geometricPath);
            geometricPath = null;
        }
        return geometricPath;
    }

    protected static GeneralPath consume_b_star(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(0);
            geometricPath.closePath();
            commonStroke(graphicState, shapes, geometricPath);
            commonFill(shapes, graphicState, geometricPath);
        }
        return null;
    }

    protected static GraphicsState consume_d1(GraphicsState graphicState, Stack stack) {
        GraphicsState graphicState2 = graphicState.save();
        float x2 = ((Number) stack.pop()).floatValue();
        float y2 = ((Number) stack.pop()).floatValue();
        float x1 = ((Number) stack.pop()).floatValue();
        float y1 = ((Number) stack.pop()).floatValue();
        float y3 = ((Number) stack.pop()).floatValue();
        float x3 = ((Number) stack.pop()).floatValue();
        TextState textState = graphicState2.getTextState();
        textState.setType3HorizontalDisplacement(new Point2D.Float(x3, y3));
        textState.setType3BBox(new PRectangle(new Point2D.Float(x1, y1), new Point2D.Float(x2, y2)));
        return graphicState2;
    }

    protected static GeneralPath consume_B_star(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (geometricPath != null) {
            geometricPath.setWindingRule(0);
            commonStroke(graphicState, shapes, geometricPath);
            commonFill(shapes, graphicState, geometricPath);
        }
        return null;
    }

    public static void consume_W_star(GraphicsState graphicState, GeneralPath geometricPath) {
        if (geometricPath != null) {
            geometricPath.setWindingRule(0);
            geometricPath.closePath();
            graphicState.setClip(geometricPath);
        }
    }

    public static void consume_DP(Stack stack) {
        stack.pop();
        stack.pop();
    }

    public static void consume_MP(Stack stack) {
        stack.pop();
    }

    public static void consume_sh(GraphicsState graphicState, Stack stack, Shapes shapes, Resources resources) {
        Object o2 = stack.peek();
        if (o2 instanceof Name) {
            Name patternName = (Name) stack.pop();
            Pattern pattern = resources.getShading(patternName);
            if (pattern != null) {
                pattern.init();
                if (graphicState.getSoftMask() != null) {
                    setAlpha(shapes, graphicState.getAlphaRule(), 0.5f);
                } else {
                    setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
                }
                shapes.add(new PaintDrawCmd(pattern.getPaint()));
                shapes.add(new ShapeDrawCmd(graphicState.getClip()));
                shapes.add(new FillDrawCmd());
                return;
            }
            setAlpha(shapes, graphicState.getAlphaRule(), 0.5f);
            shapes.add(new PaintDrawCmd(graphicState.getFillColor()));
            shapes.add(new ShapeDrawCmd(graphicState.getClip()));
            shapes.add(new FillDrawCmd());
        }
    }

    protected static void consume_TJ(GraphicsState graphicState, Stack stack, Shapes shapes, TextMetrics textMetrics, GlyphOutlineClip glyphOutlineClip, LinkedList<OptionalContents> oCGs) {
        AffineTransform tmp = applyTextScaling(graphicState);
        setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
        List v2 = (List) stack.pop();
        for (Object currentObject : v2) {
            if (currentObject instanceof StringObject) {
                StringObject stringObject = (StringObject) currentObject;
                TextState textState = graphicState.getTextState();
                drawString(stringObject.getLiteralStringBuffer(textState.font.getSubTypeFormat(), textState.font.getFont()), textMetrics, graphicState.getTextState(), shapes, glyphOutlineClip, graphicState, oCGs);
            } else if (currentObject instanceof Number) {
                Number f2 = (Number) currentObject;
                textMetrics.getAdvance().f12396x -= (f2.floatValue() / 1000.0f) * graphicState.getTextState().currentfont.getSize();
            }
            textMetrics.setPreviousAdvance(textMetrics.getAdvance().f12396x);
        }
        graphicState.set(tmp);
    }

    protected static void consume_Tj(GraphicsState graphicState, Stack stack, Shapes shapes, TextMetrics textMetrics, GlyphOutlineClip glyphOutlineClip, LinkedList<OptionalContents> oCGs) {
        Object tjValue = stack.pop();
        if (tjValue instanceof StringObject) {
            StringObject stringObject = (StringObject) tjValue;
            TextState textState = graphicState.getTextState();
            AffineTransform tmp = applyTextScaling(graphicState);
            setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
            drawString(stringObject.getLiteralStringBuffer(textState.font.getSubTypeFormat(), textState.font.getFont()), textMetrics, graphicState.getTextState(), shapes, glyphOutlineClip, graphicState, oCGs);
            graphicState.set(tmp);
            graphicState.translate(textMetrics.getAdvance().f12396x, 0.0d);
            float shift = textMetrics.getShift();
            textMetrics.setShift(shift + textMetrics.getAdvance().f12396x);
            textMetrics.setPreviousAdvance(0.0f);
            textMetrics.getAdvance().setLocation(0.0f, 0.0f);
        }
    }

    protected static void drawString(StringBuilder displayText, TextMetrics textMetrics, TextState textState, Shapes shapes, GlyphOutlineClip glyphOutlineClip, GraphicsState graphicState, LinkedList<OptionalContents> oCGs) {
        float currentX;
        float currentY;
        float advanceX = textMetrics.getAdvance().f12396x;
        float advanceY = textMetrics.getAdvance().f12397y;
        if (displayText.length() == 0) {
            textMetrics.getAdvance().setLocation(textMetrics.getPreviousAdvance(), 0.0f);
            return;
        }
        float lastx = 0.0f;
        float lasty = 0.0f;
        float firstCharWidth = (float) textState.currentfont.echarAdvance(displayText.charAt(0)).getX();
        if (advanceX + firstCharWidth < textMetrics.getPreviousAdvance()) {
            advanceX = textMetrics.getPreviousAdvance();
        }
        FontFile currentFont = textState.currentfont;
        boolean isVerticalWriting = textState.font.isVerticalWriting();
        float textRise = textState.trise;
        float characterSpace = textState.cspace * textState.hScalling;
        float whiteSpace = textState.wspace * textState.hScalling;
        int textLength = displayText.length();
        TextSprite textSprites = new TextSprite(currentFont, textLength, new AffineTransform(graphicState.getCTM()));
        for (int i2 = 0; i2 < textLength; i2++) {
            char currentChar = displayText.charAt(i2);
            float newAdvanceX = (float) currentFont.echarAdvance(currentChar).getX();
            if (isVerticalWriting) {
                lasty += newAdvanceX - textRise;
                currentX = advanceX - (newAdvanceX / 2.0f);
                currentY = advanceY + lasty;
            } else {
                currentX = advanceX + lastx;
                currentY = lasty - textRise;
                float lastx2 = lastx + newAdvanceX;
                textMetrics.setPreviousAdvance(lastx2);
                lastx = lastx2 + characterSpace;
                if (displayText.charAt(i2) == ' ') {
                    lastx += whiteSpace;
                }
            }
            GlyphText glyphText = textSprites.addText(String.valueOf(currentChar), textState.currentfont.toUnicode(currentChar), currentX, currentY, newAdvanceX);
            shapes.getPageText().addGlyph(glyphText, oCGs);
        }
        float advanceX2 = advanceX + lastx;
        float advanceY2 = advanceY + lasty;
        int rmode = textState.rmode;
        switch (rmode) {
            case 0:
                drawModeFill(graphicState, textSprites, shapes, rmode);
                break;
            case 1:
                drawModeStroke(graphicState, textSprites, textState, shapes, rmode);
                break;
            case 2:
                drawModeFillStroke(graphicState, textSprites, textState, shapes, rmode);
                break;
            case 4:
                drawModeFill(graphicState, textSprites, shapes, rmode);
                glyphOutlineClip.addTextSprite(textSprites);
                break;
            case 5:
                drawModeStroke(graphicState, textSprites, textState, shapes, rmode);
                glyphOutlineClip.addTextSprite(textSprites);
                break;
            case 6:
                drawModeFillStroke(graphicState, textSprites, textState, shapes, rmode);
                glyphOutlineClip.addTextSprite(textSprites);
                break;
            case 7:
                glyphOutlineClip.addTextSprite(textSprites);
                break;
        }
        textMetrics.getAdvance().setLocation(advanceX2, advanceY2);
    }

    protected static void drawModeFill(GraphicsState graphicState, TextSprite textSprites, Shapes shapes, int rmode) {
        textSprites.setRMode(rmode);
        textSprites.setStrokeColor(graphicState.getFillColor());
        shapes.add(new ColorDrawCmd(graphicState.getFillColor()));
        shapes.add(new TextSpriteDrawCmd(textSprites));
    }

    protected static void drawModeStroke(GraphicsState graphicState, TextSprite textSprites, TextState textState, Shapes shapes, int rmode) {
        textSprites.setRMode(rmode);
        textSprites.setStrokeColor(graphicState.getStrokeColor());
        float old = graphicState.getLineWidth();
        float lineWidth = graphicState.getLineWidth();
        graphicState.setLineWidth((float) (lineWidth / textState.tmatrix.getScaleX()));
        setStroke(shapes, graphicState);
        shapes.add(new ColorDrawCmd(graphicState.getStrokeColor()));
        shapes.add(new TextSpriteDrawCmd(textSprites));
        graphicState.setLineWidth(old);
        setStroke(shapes, graphicState);
    }

    protected static void drawModeFillStroke(GraphicsState graphicState, TextSprite textSprites, TextState textState, Shapes shapes, int rmode) {
        textSprites.setRMode(rmode);
        textSprites.setStrokeColor(graphicState.getStrokeColor());
        float old = graphicState.getLineWidth();
        float lineWidth = graphicState.getLineWidth();
        graphicState.setLineWidth((float) (lineWidth / textState.tmatrix.getScaleX()));
        setStroke(shapes, graphicState);
        shapes.add(new ColorDrawCmd(graphicState.getFillColor()));
        shapes.add(new TextSpriteDrawCmd(textSprites));
        graphicState.setLineWidth(old);
        setStroke(shapes, graphicState);
    }

    protected static void commonStroke(GraphicsState graphicState, Shapes shapes, GeneralPath geometricPath) {
        if (graphicState.isOverprintStroking()) {
            setAlpha(shapes, graphicState.getAlphaRule(), commonOverPrintAlpha(graphicState.getStrokeAlpha(), graphicState.getStrokeColorSpace()));
        } else if (graphicState.isKnockOut()) {
            setAlpha(shapes, 2, graphicState.getStrokeAlpha());
        }
        if (graphicState.getStrokeColorSpace() instanceof PatternColor) {
            PatternColor patternColor = (PatternColor) graphicState.getStrokeColorSpace();
            Pattern pattern = patternColor.getPattern();
            if (pattern != null && pattern.getPatternType() == 1) {
                TilingPattern tilingPattern = (TilingPattern) pattern;
                GraphicsState graphicState2 = graphicState.save();
                tilingPattern.setParentGraphicState(graphicState2);
                tilingPattern.init();
                graphicState = graphicState2.restore();
                if (tilingPattern.getbBoxMod() != null && (tilingPattern.getbBoxMod().getWidth() > 1.0d || tilingPattern.getbBoxMod().getHeight() > 1.0d)) {
                    shapes.add(new TilingPatternDrawCmd(tilingPattern));
                } else if (tilingPattern.getPaintType() == 2) {
                    shapes.add(new ColorDrawCmd(tilingPattern.getUnColored()));
                } else {
                    shapes.add(new ColorDrawCmd(tilingPattern.getFirstColor()));
                }
                shapes.add(new ShapeDrawCmd(geometricPath));
                shapes.add(new DrawDrawCmd());
            } else if (pattern != null && pattern.getPatternType() == 2) {
                pattern.init();
                shapes.add(new PaintDrawCmd(pattern.getPaint()));
                shapes.add(new ShapeDrawCmd(geometricPath));
                shapes.add(new DrawDrawCmd());
            }
        } else {
            setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getStrokeAlpha());
            shapes.add(new ColorDrawCmd(graphicState.getStrokeColor()));
            shapes.add(new ShapeDrawCmd(geometricPath));
            shapes.add(new DrawDrawCmd());
        }
        if (graphicState.isOverprintStroking()) {
            setAlpha(shapes, 3, graphicState.getFillAlpha());
        }
    }

    protected static float commonOverPrintAlpha(float alpha, PColorSpace colorSpace) {
        if (!enabledOverPrint) {
            return alpha;
        }
        if (colorSpace instanceof DeviceN) {
            if (alpha != 1.0f && alpha > 0.4f) {
                alpha -= 0.4f;
            } else if (alpha >= 0.4f) {
                alpha = 0.4f;
            }
            return alpha;
        }
        return alpha;
    }

    protected static void commonFill(Shapes shapes, GraphicsState graphicState, GeneralPath geometricPath) throws NoninvertibleTransformException {
        if (graphicState.isOverprintOther()) {
            setAlpha(shapes, graphicState.getAlphaRule(), commonOverPrintAlpha(graphicState.getFillAlpha(), graphicState.getFillColorSpace()));
        } else if (graphicState.isKnockOut()) {
            setAlpha(shapes, 2, graphicState.getFillAlpha());
        } else {
            setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
        }
        if (graphicState.getFillColorSpace() instanceof PatternColor) {
            PatternColor patternColor = (PatternColor) graphicState.getFillColorSpace();
            Pattern pattern = patternColor.getPattern();
            if (pattern != null && pattern.getPatternType() == 1) {
                TilingPattern tilingPattern = (TilingPattern) pattern;
                GraphicsState graphicState2 = graphicState.save();
                tilingPattern.setParentGraphicState(graphicState2);
                tilingPattern.init();
                graphicState = graphicState2.restore();
                if (tilingPattern.getbBoxMod() != null && (tilingPattern.getbBoxMod().getWidth() >= 1.0d || tilingPattern.getbBoxMod().getHeight() >= 1.0d)) {
                    shapes.add(new TilingPatternDrawCmd(tilingPattern));
                } else if (tilingPattern.getPaintType() == 2) {
                    shapes.add(new ColorDrawCmd(tilingPattern.getUnColored()));
                } else {
                    shapes.add(new ColorDrawCmd(tilingPattern.getFirstColor()));
                }
                shapes.add(new ShapeDrawCmd(geometricPath));
                shapes.add(new FillDrawCmd());
            } else if (pattern != null && pattern.getPatternType() == 2) {
                pattern.init();
                shapes.add(new PaintDrawCmd(pattern.getPaint()));
                shapes.add(new ShapeDrawCmd(geometricPath));
                shapes.add(new FillDrawCmd());
            }
        } else {
            shapes.add(new ColorDrawCmd(graphicState.getFillColor()));
            shapes.add(new ShapeDrawCmd(geometricPath));
            shapes.add(new FillDrawCmd());
        }
        if (graphicState.isOverprintOther()) {
            setAlpha(shapes, graphicState.getAlphaRule(), graphicState.getFillAlpha());
        }
    }

    protected static void setStroke(Shapes shapes, GraphicsState graphicState) {
        shapes.add(new StrokeDrawCmd(new BasicStroke(graphicState.getLineWidth(), graphicState.getLineCap(), graphicState.getLineJoin(), graphicState.getMiterLimit(), graphicState.getDashArray(), graphicState.getDashPhase())));
    }

    protected static AffineTransform applyTextScaling(GraphicsState graphicState) {
        AffineTransform af2 = new AffineTransform(graphicState.getCTM());
        AffineTransform oldHScaling = new AffineTransform(graphicState.getCTM());
        float hScalling = graphicState.getTextState().hScalling;
        AffineTransform horizontalScalingTransform = new AffineTransform(af2.getScaleX() * hScalling, af2.getShearY(), af2.getShearX(), af2.getScaleY(), af2.getTranslateX(), af2.getTranslateY());
        graphicState.set(horizontalScalingTransform);
        return oldHScaling;
    }

    protected static void setAlpha(Shapes shapes, int rule, float alpha) {
        if (shapes.getAlpha() != alpha || shapes.getRule() != rule) {
            AlphaComposite alphaComposite = AlphaComposite.getInstance(rule, alpha);
            shapes.add(new AlphaDrawCmd(alphaComposite));
            shapes.setAlpha(alpha);
            shapes.setRule(rule);
        }
    }

    @Override // org.icepdf.core.util.content.ContentParser
    public void setGlyph2UserSpaceScale(float scale) {
        this.glyph2UserSpaceScale = scale;
    }
}
