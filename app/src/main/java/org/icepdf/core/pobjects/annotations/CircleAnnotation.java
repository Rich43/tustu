package org.icepdf.core.pobjects.annotations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.FillDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.PostScriptEncoder;
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/CircleAnnotation.class */
public class CircleAnnotation extends MarkupAnnotation {
    private static final Logger logger = Logger.getLogger(CircleAnnotation.class.toString());
    public static final Name IC_KEY = new Name("IC");
    private Color fillColor;
    private boolean isFillColor;
    private Rectangle rectangle;

    public CircleAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation, org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        this.fillColor = Color.WHITE;
        List C2 = (List) getObject(IC_KEY);
        if (C2 != null && C2.size() >= 3) {
            float red = ((Number) C2.get(0)).floatValue();
            float green = ((Number) C2.get(1)).floatValue();
            float blue = ((Number) C2.get(2)).floatValue();
            this.fillColor = new Color(Math.max(0.0f, Math.min(1.0f, red)), Math.max(0.0f, Math.min(1.0f, green)), Math.max(0.0f, Math.min(1.0f, blue)));
            this.isFillColor = true;
        }
    }

    public static CircleAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_CIRCLE);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        CircleAnnotation circleAnnotation = new CircleAnnotation(library, entries);
        circleAnnotation.init();
        circleAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        circleAnnotation.setNew(true);
        circleAnnotation.setFlag(64, false);
        circleAnnotation.setFlag(16, false);
        circleAnnotation.setFlag(8, false);
        circleAnnotation.setFlag(4, true);
        return circleAnnotation;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
        BasicStroke stroke;
        Form form;
        this.matrix = new AffineTransform();
        this.shapes = new Shapes();
        setModifiedDate(PDate.formatDateTime(new Date()));
        this.rectangle = getUserSpaceRectangle().getBounds();
        this.entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(this.rectangle));
        this.userSpaceRectangle = new Rectangle2D.Float((float) this.rectangle.getX(), (float) this.rectangle.getY(), (float) this.rectangle.getWidth(), (float) this.rectangle.getHeight());
        int strokeWidth = (int) this.borderStyle.getStrokeWidth();
        Rectangle rectangleToDraw = new Rectangle(((int) this.rectangle.getX()) + strokeWidth, ((int) this.rectangle.getY()) + strokeWidth, ((int) this.rectangle.getWidth()) - (strokeWidth * 2), ((int) this.rectangle.getHeight()) - (strokeWidth * 2));
        AffineTransform af2 = new AffineTransform();
        af2.scale(1.0d, -1.0d);
        af2.translate(-this.bbox.getMinX(), -this.bbox.getMaxY());
        if (this.borderStyle.isStyleDashed()) {
            stroke = new BasicStroke(this.borderStyle.getStrokeWidth(), 0, 0, this.borderStyle.getStrokeWidth() * 2.0f, this.borderStyle.getDashArray(), 0.0f);
        } else {
            stroke = new BasicStroke(this.borderStyle.getStrokeWidth());
        }
        Ellipse2D.Double circle = new Ellipse2D.Double(rectangleToDraw.getMinX(), rectangleToDraw.getMinY(), rectangleToDraw.getWidth(), rectangleToDraw.getHeight());
        this.shapes.add(new TransformDrawCmd(af2));
        this.shapes.add(new StrokeDrawCmd(stroke));
        this.shapes.add(new ShapeDrawCmd(circle));
        if (this.isFillColor) {
            this.shapes.add(new ColorDrawCmd(this.fillColor));
            this.shapes.add(new FillDrawCmd());
        }
        if (this.borderStyle.getStrokeWidth() > 0.0f) {
            this.shapes.add(new ColorDrawCmd(this.color));
            this.shapes.add(new DrawDrawCmd());
        }
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

    public Color getFillColor() {
        return this.fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        float[] compArray = new float[3];
        this.fillColor.getColorComponents(compArray);
        List<Float> colorValues = new ArrayList<>(compArray.length);
        for (float comp : compArray) {
            colorValues.add(Float.valueOf(comp));
        }
        this.entries.put(IC_KEY, colorValues);
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public boolean isFillColor() {
        return this.isFillColor;
    }

    public void setFillColor(boolean fillColor) {
        this.isFillColor = fillColor;
        if (!this.isFillColor) {
            this.entries.remove(IC_KEY);
        }
    }
}
