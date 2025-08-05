package org.icepdf.core.pobjects.annotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Dictionary;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.PRectangle;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.commands.ColorDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.DrawDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.FillDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/LineAnnotation.class */
public class LineAnnotation extends MarkupAnnotation {
    private static final Logger logger = Logger.getLogger(LineAnnotation.class.toString());
    public static final Name L_KEY = new Name("L");
    public static final Name LE_KEY = new Name("LE");
    public static final Name LL_KEY = new Name("LL");
    public static final Name LLE_KEY = new Name("LLE");
    public static final Name IC_KEY = new Name("IC");
    public static final Name CAP_KEY = new Name("Cap");
    public static final Name LLO_KEY = new Name("LLO");
    public static final Name CP_KEY = new Name("CP");
    public static final Name MEASURE_KEY = new Name("Measure");
    public static final Name CO_KEY = new Name("CO");
    public static final Name LINE_END_NONE = new Name("None");
    public static final Name LINE_END_SQUARE = new Name("Square");
    public static final Name LINE_END_CIRCLE = new Name("Circle");
    public static final Name LINE_END_DIAMOND = new Name("Diamond");
    public static final Name LINE_END_OPEN_ARROW = new Name("OpenArrow");
    public static final Name LINE_END_CLOSED_ARROW = new Name("ClosedArrow");
    protected Point2D startOfLine;
    protected Point2D endOfLine;
    protected Color interiorColor;
    protected Name startArrow;
    protected Name endArrow;

    public LineAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
        this.startArrow = LINE_END_NONE;
        this.endArrow = LINE_END_NONE;
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation, org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        List<Number> value = this.library.getArray(this.entries, L_KEY);
        if (value != null) {
            this.startOfLine = new Point2D.Float(value.get(0).floatValue(), value.get(1).floatValue());
            this.endOfLine = new Point2D.Float(value.get(2).floatValue(), value.get(3).floatValue());
        }
        List value2 = this.library.getArray(this.entries, LE_KEY);
        if (value2 != null) {
            this.startArrow = (Name) value2.get(0);
            this.endArrow = (Name) value2.get(1);
        }
        this.interiorColor = null;
        List C2 = (List) getObject(IC_KEY);
        if (C2 != null && C2.size() >= 3) {
            float red = ((Number) C2.get(0)).floatValue();
            float green = ((Number) C2.get(1)).floatValue();
            float blue = ((Number) C2.get(2)).floatValue();
            this.interiorColor = new Color(Math.max(0.0f, Math.min(1.0f, red)), Math.max(0.0f, Math.min(1.0f, green)), Math.max(0.0f, Math.min(1.0f, blue)));
        }
        if (!hasAppearanceStream() && this.startOfLine != null && this.endOfLine != null) {
            Object tmp = getObject(RECTANGLE_KEY);
            Rectangle2D.Float rectangle = null;
            if (tmp instanceof List) {
                rectangle = this.library.getRectangle(this.entries, RECTANGLE_KEY);
            }
            if (rectangle != null) {
                setBBox(rectangle.getBounds());
            }
            resetAppearanceStream(new AffineTransform());
        }
    }

    public static LineAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_LINE);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        LineAnnotation lineAnnotation = new LineAnnotation(library, entries);
        lineAnnotation.init();
        lineAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        lineAnnotation.setNew(true);
        return lineAnnotation;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageTransform) {
        AffineTransform af2 = new AffineTransform();
        af2.setToTranslation(dx * pageTransform.getScaleX(), (-dy) * pageTransform.getScaleY());
        af2.transform(this.startOfLine, this.startOfLine);
        af2.transform(this.endOfLine, this.endOfLine);
        setStartOfLine(this.startOfLine);
        setEndOfLine(this.endOfLine);
        setModifiedDate(PDate.formatDateTime(new Date()));
        this.matrix = new AffineTransform();
        this.shapes = new Shapes();
        AffineTransform af3 = new AffineTransform();
        if (this.userSpaceRectangle == null) {
            this.userSpaceRectangle = getUserSpaceRectangle();
        }
        af3.translate(-this.userSpaceRectangle.getMinX(), -this.userSpaceRectangle.getMinY());
        Stroke stroke = getBorderStyleStroke();
        GeneralPath line = new GeneralPath();
        line.moveTo((float) this.startOfLine.getX(), (float) this.startOfLine.getY());
        line.lineTo((float) this.endOfLine.getX(), (float) this.endOfLine.getY());
        line.closePath();
        this.shapes.add(new TransformDrawCmd(af3));
        this.shapes.add(new ShapeDrawCmd(line));
        this.shapes.add(new StrokeDrawCmd(stroke));
        this.shapes.add(new ColorDrawCmd(this.color));
        this.shapes.add(new DrawDrawCmd());
        if (this.startArrow.equals(LINE_END_OPEN_ARROW)) {
            openArrowStartDrawOps(this.shapes, af3, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.startArrow.equals(LINE_END_CLOSED_ARROW)) {
            closedArrowStartDrawOps(this.shapes, af3, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.startArrow.equals(LINE_END_CIRCLE)) {
            circleDrawOps(this.shapes, af3, this.startOfLine, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.startArrow.equals(LINE_END_DIAMOND)) {
            diamondDrawOps(this.shapes, af3, this.startOfLine, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.startArrow.equals(LINE_END_SQUARE)) {
            squareDrawOps(this.shapes, af3, this.startOfLine, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        }
        if (this.endArrow.equals(LINE_END_OPEN_ARROW)) {
            openArrowEndDrawOps(this.shapes, af3, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.endArrow.equals(LINE_END_CLOSED_ARROW)) {
            closedArrowEndDrawOps(this.shapes, af3, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.endArrow.equals(LINE_END_CIRCLE)) {
            circleDrawOps(this.shapes, af3, this.endOfLine, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.endArrow.equals(LINE_END_DIAMOND)) {
            diamondDrawOps(this.shapes, af3, this.endOfLine, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        } else if (this.endArrow.equals(LINE_END_SQUARE)) {
            squareDrawOps(this.shapes, af3, this.endOfLine, this.startOfLine, this.endOfLine, this.color, this.interiorColor);
        }
        this.entries.remove(APPEARANCE_STREAM_KEY);
    }

    public static Logger getLogger() {
        return logger;
    }

    public Point2D getStartOfLine() {
        return this.startOfLine;
    }

    public Point2D getEndOfLine() {
        return this.endOfLine;
    }

    public Color getInteriorColor() {
        return this.interiorColor;
    }

    public Name getStartArrow() {
        return this.startArrow;
    }

    public Name getEndArrow() {
        return this.endArrow;
    }

    public void setStartOfLine(Point2D startOfLine) {
        this.startOfLine = startOfLine;
    }

    public void setEndArrow(Name endArrow) {
        this.endArrow = endArrow;
        List<Name> endNameArray = new ArrayList<>(2);
        endNameArray.add(this.startArrow);
        endNameArray.add(endArrow);
        this.entries.put(LE_KEY, endNameArray);
    }

    public void setStartArrow(Name startArrow) {
        this.startArrow = startArrow;
        List<Name> endNameArray = new ArrayList<>(2);
        endNameArray.add(startArrow);
        endNameArray.add(this.endArrow);
        this.entries.put(LE_KEY, endNameArray);
    }

    public void setInteriorColor(Color interiorColor) {
        this.interiorColor = interiorColor;
        float[] compArray = new float[3];
        this.interiorColor.getColorComponents(compArray);
        List<Float> colorValues = new ArrayList<>(compArray.length);
        for (float comp : compArray) {
            colorValues.add(Float.valueOf(comp));
        }
        this.entries.put(IC_KEY, colorValues);
    }

    public void setEndOfLine(Point2D endOfLine) {
        this.endOfLine = endOfLine;
        List<Number> pointArray = new ArrayList<>(4);
        pointArray.add(Float.valueOf((float) this.startOfLine.getX()));
        pointArray.add(Float.valueOf((float) this.startOfLine.getY()));
        pointArray.add(Float.valueOf((float) endOfLine.getX()));
        pointArray.add(Float.valueOf((float) endOfLine.getY()));
        this.entries.put(L_KEY, pointArray);
    }

    public static void drawLineStart(Graphics2D g2, Name lineEnding, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        if (lineEnding.equals(LINE_END_OPEN_ARROW)) {
            drawOpenArrowStart(g2, startOfLine, endOfLine, lineColor, interiorColor);
            return;
        }
        if (lineEnding.equals(LINE_END_CLOSED_ARROW)) {
            drawClosedArrowStart(g2, startOfLine, endOfLine, lineColor, interiorColor);
            return;
        }
        if (lineEnding.equals(LINE_END_CIRCLE)) {
            drawCircle(g2, startOfLine, startOfLine, endOfLine, lineColor, interiorColor);
        } else if (lineEnding.equals(LINE_END_DIAMOND)) {
            drawDiamond(g2, startOfLine, startOfLine, endOfLine, lineColor, interiorColor);
        } else if (lineEnding.equals(LINE_END_SQUARE)) {
            drawSquare(g2, startOfLine, startOfLine, endOfLine, lineColor, interiorColor);
        }
    }

    public static void drawLineEnd(Graphics2D g2, Name lineEnding, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        if (lineEnding.equals(LINE_END_OPEN_ARROW)) {
            drawOpenArrowEnd(g2, startOfLine, endOfLine, lineColor, interiorColor);
            return;
        }
        if (lineEnding.equals(LINE_END_CLOSED_ARROW)) {
            drawClosedArrowEnd(g2, startOfLine, endOfLine, lineColor, interiorColor);
            return;
        }
        if (lineEnding.equals(LINE_END_CIRCLE)) {
            drawCircle(g2, endOfLine, startOfLine, endOfLine, lineColor, interiorColor);
        } else if (lineEnding.equals(LINE_END_DIAMOND)) {
            drawDiamond(g2, endOfLine, startOfLine, endOfLine, lineColor, interiorColor);
        } else if (lineEnding.equals(LINE_END_SQUARE)) {
            drawSquare(g2, endOfLine, startOfLine, endOfLine, lineColor, interiorColor);
        }
    }

    public static void circleDrawOps(Shapes shapes, AffineTransform at2, Point2D point, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform af2 = createRotation(point, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(af2);
        shapes.add(new TransformDrawCmd(at3));
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createCircleEnd()));
        shapes.add(new FillDrawCmd());
    }

    private static Shape createCircleEnd() {
        return new Ellipse2D.Double(-4.0d, -4.0d, 8.0d, 8.0d);
    }

    private static void drawCircle(Graphics2D g2, Point2D point, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        AffineTransform oldAf = g2.getTransform();
        AffineTransform af2 = createRotation(point, startOfLine, endOfLine);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(af2);
        g2.setTransform(gAf);
        g2.setColor(lineColor);
        g2.fill(createCircleEnd());
        g2.setTransform(oldAf);
    }

    public static void diamondDrawOps(Shapes shapes, AffineTransform at2, Point2D point, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform tx = new AffineTransform();
        Line2D.Double line = new Line2D.Double(start, end);
        tx.setToIdentity();
        double angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
        tx.translate(point.getX(), point.getY());
        tx.rotate(angle - 0.7853981633974483d);
        createRotation(point, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(tx);
        shapes.add(new TransformDrawCmd(at3));
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createSquareEnd()));
        shapes.add(new FillDrawCmd());
    }

    private static void drawDiamond(Graphics2D g2, Point2D point, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        AffineTransform oldAf = g2.getTransform();
        AffineTransform tx = new AffineTransform();
        Line2D.Double line = new Line2D.Double(startOfLine, endOfLine);
        tx.setToIdentity();
        double angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
        tx.translate(point.getX(), point.getY());
        tx.rotate(angle - 0.7853981633974483d);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(tx);
        g2.setTransform(gAf);
        g2.setColor(lineColor);
        g2.fill(createSquareEnd());
        g2.setTransform(oldAf);
    }

    public static void squareDrawOps(Shapes shapes, AffineTransform at2, Point2D point, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform af2 = createRotation(point, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(af2);
        shapes.add(new TransformDrawCmd(at3));
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createSquareEnd()));
        shapes.add(new FillDrawCmd());
    }

    private static Shape createSquareEnd() {
        return new Rectangle2D.Double(-4.0d, -4.0d, 8.0d, 8.0d);
    }

    private static void drawSquare(Graphics2D g2, Point2D point, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        AffineTransform oldAf = g2.getTransform();
        AffineTransform af2 = createRotation(point, startOfLine, endOfLine);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(af2);
        g2.setTransform(gAf);
        g2.setColor(lineColor);
        g2.fill(createSquareEnd());
        g2.setTransform(oldAf);
    }

    public static void openArrowEndDrawOps(Shapes shapes, AffineTransform at2, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform af2 = createRotation(end, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(af2);
        shapes.add(new TransformDrawCmd(at3));
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createOpenArrowEnd()));
        shapes.add(new DrawDrawCmd());
    }

    private static Shape createOpenArrowEnd() {
        GeneralPath arrowHead = new GeneralPath();
        arrowHead.moveTo(0.0f, 0.0f);
        arrowHead.lineTo(5.0f, -10.0f);
        arrowHead.moveTo(0.0f, 0.0f);
        arrowHead.lineTo(-5.0f, -10.0f);
        arrowHead.closePath();
        return arrowHead;
    }

    private static void drawOpenArrowEnd(Graphics2D g2, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        Shape arrowHead = createOpenArrowEnd();
        AffineTransform oldAf = g2.getTransform();
        AffineTransform af2 = createRotation(endOfLine, startOfLine, endOfLine);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(af2);
        g2.setTransform(gAf);
        g2.setColor(lineColor);
        g2.draw(arrowHead);
        g2.setTransform(oldAf);
    }

    public static void openArrowStartDrawOps(Shapes shapes, AffineTransform at2, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform af2 = createRotation(start, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(af2);
        shapes.add(new TransformDrawCmd(at3));
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createOpenArrowStart()));
        shapes.add(new DrawDrawCmd());
    }

    private static Shape createOpenArrowStart() {
        GeneralPath arrowHead = new GeneralPath();
        arrowHead.moveTo(0.0f, 0.0f);
        arrowHead.lineTo(5.0f, 10.0f);
        arrowHead.moveTo(0.0f, 0.0f);
        arrowHead.lineTo(-5.0f, 10.0f);
        arrowHead.closePath();
        return arrowHead;
    }

    private static void drawOpenArrowStart(Graphics2D g2, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        Shape arrowHead = createOpenArrowStart();
        AffineTransform oldAf = g2.getTransform();
        AffineTransform af2 = createRotation(startOfLine, startOfLine, endOfLine);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(af2);
        g2.setTransform(gAf);
        g2.setColor(lineColor);
        g2.draw(arrowHead);
        g2.setTransform(oldAf);
    }

    public static void closedArrowStartDrawOps(Shapes shapes, AffineTransform at2, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform af2 = createRotation(start, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(af2);
        shapes.add(new TransformDrawCmd(at3));
        if (internalColor != null) {
            shapes.add(new ColorDrawCmd(internalColor));
            shapes.add(new ShapeDrawCmd(createClosedArrowStart()));
            shapes.add(new FillDrawCmd());
        }
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createClosedArrowStart()));
        shapes.add(new DrawDrawCmd());
    }

    private static Shape createClosedArrowStart() {
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, -5);
        arrowHead.addPoint(-5, 5);
        arrowHead.addPoint(5, 5);
        return arrowHead;
    }

    private static void drawClosedArrowStart(Graphics2D g2, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        Shape arrowHead = createClosedArrowStart();
        AffineTransform oldAf = g2.getTransform();
        AffineTransform af2 = createRotation(startOfLine, startOfLine, endOfLine);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(af2);
        g2.setTransform(gAf);
        if (interiorColor != null) {
            g2.setColor(interiorColor);
            g2.fill(arrowHead);
        }
        g2.setColor(lineColor);
        g2.draw(arrowHead);
        g2.setTransform(oldAf);
    }

    public static void closedArrowEndDrawOps(Shapes shapes, AffineTransform at2, Point2D start, Point2D end, Color lineColor, Color internalColor) {
        AffineTransform af2 = createRotation(end, start, end);
        AffineTransform at3 = new AffineTransform(at2);
        at3.concatenate(af2);
        shapes.add(new TransformDrawCmd(at3));
        if (internalColor != null) {
            shapes.add(new ColorDrawCmd(internalColor));
            shapes.add(new ShapeDrawCmd(createClosedArrowEnd()));
            shapes.add(new FillDrawCmd());
        }
        shapes.add(new ColorDrawCmd(lineColor));
        shapes.add(new ShapeDrawCmd(createClosedArrowEnd()));
        shapes.add(new DrawDrawCmd());
    }

    private static Shape createClosedArrowEnd() {
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, 5);
        arrowHead.addPoint(-5, -5);
        arrowHead.addPoint(5, -5);
        return arrowHead;
    }

    private static void drawClosedArrowEnd(Graphics2D g2, Point2D startOfLine, Point2D endOfLine, Color lineColor, Color interiorColor) {
        Shape arrowHead = createClosedArrowEnd();
        AffineTransform oldAf = g2.getTransform();
        AffineTransform af2 = createRotation(endOfLine, startOfLine, endOfLine);
        AffineTransform gAf = g2.getTransform();
        gAf.concatenate(af2);
        g2.setTransform(gAf);
        if (interiorColor != null) {
            g2.setColor(interiorColor);
            g2.fill(arrowHead);
        }
        g2.setColor(lineColor);
        g2.draw(arrowHead);
        g2.setTransform(oldAf);
    }

    private static AffineTransform createRotation(Point2D point, Point2D startOfLine, Point2D endOfLine) {
        AffineTransform tx = new AffineTransform();
        Line2D.Double line = new Line2D.Double(startOfLine, endOfLine);
        tx.setToIdentity();
        double angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
        tx.translate(point.getX(), point.getY());
        tx.rotate(angle - 1.5707963267948966d);
        return tx;
    }
}
