package org.icepdf.core.pobjects.annotations;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.LiteralStringObject;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.graphics.GraphicsState;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.commands.AlphaDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.FillDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.GraphicsStateCmd;
import org.icepdf.core.pobjects.graphics.commands.PostScriptEncoder;
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/TextMarkupAnnotation.class */
public class TextMarkupAnnotation extends MarkupAnnotation {
    private static final Logger logger = Logger.getLogger(TextMarkupAnnotation.class.toString());
    public static final Name SUBTYPE_HIGHLIGHT = new Name("Highlight");
    public static final Name SUBTYPE_UNDERLINE = new Name("Underline");
    public static final Name SUBTYPE_SQUIGGLY = new Name("Squiggly");
    public static final Name SUBTYPE_STRIKE_OUT = new Name("StrikeOut");
    private static Color highlightColor;
    private static Color strikeOutColor;
    private static Color underlineColor;
    public static final Name KEY_QUAD_POINTS;
    public static final Name EXTGSTATE_NAME;
    public static final float HIGHLIGHT_ALPHA = 0.3f;
    private Shape[] quadrilaterals;
    private Color textMarkupColor;
    private GeneralPath markupPath;
    private ArrayList<Shape> markupBounds;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.textmarkup.highlight.color", "#ffff00");
            int colorValue = ColorUtil.convertColor(color);
            highlightColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("ffff00", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading Text Markup Annotation highlight colour");
            }
        }
        try {
            String color2 = Defs.sysProperty("org.icepdf.core.views.page.annotation.textmarkup.strikeOut.color", "#ff0000");
            int colorValue2 = ColorUtil.convertColor(color2);
            strikeOutColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("ff0000", 16));
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading Text Markup Annotation strike out colour");
            }
        }
        try {
            String color3 = Defs.sysProperty("org.icepdf.core.views.page.annotation.textmarkup.underline.color", "#00ff00");
            int colorValue3 = ColorUtil.convertColor(color3);
            underlineColor = new Color(colorValue3 >= 0 ? colorValue3 : Integer.parseInt("00ff00", 16));
        } catch (NumberFormatException e4) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading Text Markup Annotation underline colour");
            }
        }
        KEY_QUAD_POINTS = new Name("QuadPoints");
        EXTGSTATE_NAME = new Name("ip1");
    }

    public TextMarkupAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation, org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        List<Number> quadPoints = this.library.getArray(this.entries, KEY_QUAD_POINTS);
        if (quadPoints != null) {
            int size = quadPoints.size() / 8;
            this.quadrilaterals = new Shape[size];
            int i2 = 0;
            int count = 0;
            while (i2 < size) {
                GeneralPath shape = new GeneralPath(0, 4);
                shape.moveTo(quadPoints.get(count + 6).floatValue(), quadPoints.get(count + 7).floatValue());
                shape.lineTo(quadPoints.get(count + 4).floatValue(), quadPoints.get(count + 5).floatValue());
                shape.lineTo(quadPoints.get(count).floatValue(), quadPoints.get(count + 1).floatValue());
                shape.lineTo(quadPoints.get(count + 2).floatValue(), quadPoints.get(count + 3).floatValue());
                shape.closePath();
                this.quadrilaterals[i2] = shape;
                i2++;
                count += 8;
            }
        }
        if (SUBTYPE_HIGHLIGHT.equals(this.subtype)) {
            this.textMarkupColor = highlightColor;
        } else if (SUBTYPE_STRIKE_OUT.equals(this.subtype)) {
            this.textMarkupColor = strikeOutColor;
        } else if (SUBTYPE_UNDERLINE.equals(this.subtype)) {
            this.textMarkupColor = underlineColor;
        } else if (SUBTYPE_SQUIGGLY.equals(this.subtype)) {
        }
        if (this.shapes != null) {
            this.markupBounds = new ArrayList<>();
            this.markupPath = new GeneralPath();
            Iterator i$ = this.shapes.getShapes().iterator();
            while (i$.hasNext()) {
                DrawCmd cmd = i$.next();
                if (cmd instanceof ShapeDrawCmd) {
                    ShapeDrawCmd shapeDrawCmd = (ShapeDrawCmd) cmd;
                    this.markupBounds.add(shapeDrawCmd.getShape());
                    this.markupPath.append(shapeDrawCmd.getShape(), false);
                }
            }
        }
    }

    public static TextMarkupAnnotation getInstance(Library library, Rectangle rect, Name subType) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, subType);
        entries.put(Annotation.FLAG_KEY, 4);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        TextMarkupAnnotation textMarkupAnnotation = new TextMarkupAnnotation(library, entries);
        textMarkupAnnotation.init();
        entries.put(NM_KEY, new LiteralStringObject(String.valueOf(textMarkupAnnotation.hashCode())));
        textMarkupAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        textMarkupAnnotation.setNew(true);
        textMarkupAnnotation.setModifiedDate(PDate.formatDateTime(new Date()));
        return textMarkupAnnotation;
    }

    public static boolean isTextMarkupAnnotation(Name subType) {
        return SUBTYPE_HIGHLIGHT.equals(subType) || SUBTYPE_UNDERLINE.equals(subType) || SUBTYPE_SQUIGGLY.equals(subType) || SUBTYPE_STRIKE_OUT.equals(subType);
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
        Form form;
        this.matrix = new AffineTransform();
        this.shapes = new Shapes();
        AffineTransform af2 = new AffineTransform();
        if (this.userSpaceRectangle == null) {
            this.userSpaceRectangle = getUserSpaceRectangle();
        }
        af2.translate(-this.userSpaceRectangle.getMinX(), -this.userSpaceRectangle.getMinY());
        BasicStroke stroke = new BasicStroke(1.0f);
        this.shapes.add(new TransformDrawCmd(af2));
        this.shapes.add(new StrokeDrawCmd(stroke));
        if (SUBTYPE_HIGHLIGHT.equals(this.subtype)) {
            this.shapes.add(new GraphicsStateCmd(EXTGSTATE_NAME));
            this.shapes.add(new AlphaDrawCmd(AlphaComposite.getInstance(3, 0.3f)));
            this.shapes.add(new ShapeDrawCmd(this.markupPath));
            this.shapes.add(new ColorDrawCmd(this.textMarkupColor));
            this.shapes.add(new FillDrawCmd());
            this.shapes.add(new AlphaDrawCmd(AlphaComposite.getInstance(3, 1.0f)));
        } else if (SUBTYPE_STRIKE_OUT.equals(this.subtype)) {
            Iterator i$ = this.markupBounds.iterator();
            while (i$.hasNext()) {
                Shape shape = i$.next();
                GeneralPath strikeOutPath = new GeneralPath();
                Rectangle2D bound = shape.getBounds2D();
                float y2 = (float) (bound.getMinY() + (bound.getHeight() / 2.0d));
                strikeOutPath.moveTo((float) bound.getMinX(), y2);
                strikeOutPath.lineTo((float) bound.getMaxX(), y2);
                strikeOutPath.closePath();
                this.shapes.add(new ShapeDrawCmd(strikeOutPath));
                this.shapes.add(new ColorDrawCmd(this.textMarkupColor));
                this.shapes.add(new DrawDrawCmd());
            }
        } else if (SUBTYPE_UNDERLINE.equals(this.subtype)) {
            Iterator i$2 = this.markupBounds.iterator();
            while (i$2.hasNext()) {
                Shape shape2 = i$2.next();
                GeneralPath underlinePath = new GeneralPath();
                Rectangle2D bound2 = shape2.getBounds2D();
                underlinePath.moveTo((float) bound2.getMinX(), (float) bound2.getMinY());
                underlinePath.lineTo((float) bound2.getMaxX(), (float) bound2.getMinY());
                underlinePath.closePath();
                this.shapes.add(new ShapeDrawCmd(underlinePath));
                this.shapes.add(new ColorDrawCmd(this.textMarkupColor));
                this.shapes.add(new DrawDrawCmd());
            }
        } else if (SUBTYPE_SQUIGGLY.equals(this.subtype)) {
        }
        List<Float> quadPoints = new ArrayList<>();
        if (this.markupBounds != null) {
            Iterator i$3 = this.markupBounds.iterator();
            while (i$3.hasNext()) {
                Shape shape3 = i$3.next();
                Rectangle2D bounds = shape3.getBounds2D();
                quadPoints.add(Float.valueOf((float) bounds.getX()));
                quadPoints.add(Float.valueOf((float) (bounds.getY() + bounds.getHeight())));
                quadPoints.add(Float.valueOf((float) (bounds.getX() + bounds.getWidth())));
                quadPoints.add(Float.valueOf((float) (bounds.getY() + bounds.getHeight())));
                quadPoints.add(Float.valueOf((float) bounds.getX()));
                quadPoints.add(Float.valueOf((float) bounds.getY()));
                quadPoints.add(Float.valueOf((float) (bounds.getX() + bounds.getWidth())));
                quadPoints.add(Float.valueOf((float) bounds.getY()));
            }
        }
        this.entries.put(KEY_QUAD_POINTS, quadPoints);
        setModifiedDate(PDate.formatDateTime(new Date()));
        StateManager stateManager = this.library.getStateManager();
        if (hasAppearanceStream()) {
            form = (Form) getAppearanceStream();
        } else {
            HashMap<Object, Object> formEntries = new HashMap<>();
            formEntries.put(Form.TYPE_KEY, Form.TYPE_VALUE);
            formEntries.put(Form.SUBTYPE_KEY, Form.SUB_TYPE_VALUE);
            form = new Form(this.library, formEntries, null);
            form.setPObjectReference(stateManager.getNewReferencNumber());
            this.library.addObject(form, form.getPObjectReference());
        }
        if (form != null) {
            Rectangle2D formBbox = new Rectangle2D.Float(0.0f, 0.0f, (float) this.bbox.getWidth(), (float) this.bbox.getHeight());
            form.setAppearance(this.shapes, this.matrix, formBbox);
            stateManager.addChange(new PObject(form, form.getPObjectReference()));
            if (SUBTYPE_HIGHLIGHT.equals(this.subtype)) {
                Resources resources = form.getResources();
                HashMap<Object, Object> graphicsProperties = new HashMap<>(2);
                HashMap<Object, Object> graphicsState = new HashMap<>(1);
                graphicsProperties.put(GraphicsState.CA_STROKING_KEY, Float.valueOf(0.3f));
                graphicsProperties.put(GraphicsState.CA_NON_STROKING_KEY, Float.valueOf(0.3f));
                graphicsState.put(EXTGSTATE_NAME, graphicsProperties);
                resources.getEntries().put(Resources.EXTGSTATE_KEY, graphicsState);
                form.setResources(resources);
            }
            form.setRawBytes(PostScriptEncoder.generatePostScript(this.shapes.getShapes()));
            HashMap<Object, Object> appearanceRefs = new HashMap<>();
            appearanceRefs.put(APPEARANCE_STREAM_NORMAL_KEY, form.getPObjectReference());
            this.entries.put(APPEARANCE_STREAM_KEY, appearanceRefs);
            if (compressAppearanceStream) {
                form.getEntries().put(Stream.FILTER_KEY, new Name("FlateDecode"));
            } else {
                form.getEntries().remove(Stream.FILTER_KEY);
            }
        }
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    protected void renderAppearanceStream(Graphics2D g2) {
        if (this.shapes != null) {
            super.renderAppearanceStream(g2);
            return;
        }
        if (this.quadrilaterals != null) {
            if (this.subtype != null && SUBTYPE_HIGHLIGHT.equals(this.subtype)) {
                g2.setComposite(AlphaComposite.getInstance(3, 0.3f));
                if (this.shapes != null) {
                    this.shapes.setPaintAlpha(false);
                }
            }
            Object tmp = getObject(RECTANGLE_KEY);
            Rectangle2D.Float rectangle = null;
            if (tmp instanceof List) {
                rectangle = this.library.getRectangle(this.entries, RECTANGLE_KEY);
            }
            Rectangle2D.Float origRect = getUserSpaceRectangle();
            AffineTransform af2 = g2.getTransform();
            double x2 = rectangle.getX() - origRect.getX();
            double y2 = rectangle.getY() - origRect.getY();
            af2.translate(-origRect.getX(), -origRect.getY());
            g2.setTransform(af2);
            g2.setColor(highlightColor);
            AffineTransform af22 = new AffineTransform();
            af22.translate(-x2, -y2);
            Shape[] arr$ = this.quadrilaterals;
            for (Shape shape : arr$) {
                g2.fill(af22.createTransformedShape(shape));
            }
            if (this.subtype != null && SUBTYPE_HIGHLIGHT.equals(this.subtype)) {
                g2.setComposite(AlphaComposite.getInstance(3, 1.0f));
                if (this.shapes != null) {
                    this.shapes.setPaintAlpha(true);
                }
            }
        }
    }

    public void setMarkupPath(GeneralPath markupPath) {
        this.markupPath = markupPath;
    }

    public void setMarkupBounds(ArrayList<Shape> markupBounds) {
        this.markupBounds = markupBounds;
    }

    public Color getTextMarkupColor() {
        return this.textMarkupColor;
    }

    public void setTextMarkupColor(Color textMarkupColor) {
        this.textMarkupColor = textMarkupColor;
    }
}
