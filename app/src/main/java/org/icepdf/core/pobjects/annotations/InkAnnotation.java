package org.icepdf.core.pobjects.annotations;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
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
import org.icepdf.core.pobjects.graphics.commands.ShapeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.StrokeDrawCmd;
import org.icepdf.core.pobjects.graphics.commands.TransformDrawCmd;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/annotations/InkAnnotation.class */
public class InkAnnotation extends MarkupAnnotation {
    private static final Logger logger = Logger.getLogger(InkAnnotation.class.toString());
    public static final Name INK_LIST_KEY = new Name("InkList");
    protected Shape inkPath;

    public InkAnnotation(Library l2, HashMap h2) {
        super(l2, h2);
    }

    @Override // org.icepdf.core.pobjects.annotations.MarkupAnnotation, org.icepdf.core.pobjects.annotations.Annotation, org.icepdf.core.pobjects.Dictionary
    public void init() {
        super.init();
        List<List<Number>> inkLists = this.library.getArray(this.entries, INK_LIST_KEY);
        GeneralPath inkPaths = new GeneralPath();
        if (inkLists != null) {
            this.inkPath = new GeneralPath();
            for (List<Number> inkList : inkLists) {
                GeneralPath inkPath = null;
                int max = inkList.size() - 1;
                for (int i2 = 0; i2 < max; i2 += 2) {
                    if (inkPath == null) {
                        inkPath = new GeneralPath();
                        inkPath.moveTo(inkList.get(i2).floatValue(), inkList.get(i2 + 1).floatValue());
                    } else {
                        inkPath.lineTo(inkList.get(i2).floatValue(), inkList.get(i2 + 1).floatValue());
                    }
                }
                inkPaths.append((Shape) inkPath, false);
            }
        }
        this.inkPath = inkPaths;
        if (!hasAppearanceStream() && this.inkPath != null) {
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

    private List<List<Float>> convertPathToArray(Shape inkPath) {
        List<List<Float>> inkLists = new ArrayList<>();
        List<Float> segment = null;
        if (inkPath != null) {
            PathIterator pathIterator = inkPath.getPathIterator(null);
            float[] inkSegment = new float[6];
            while (!pathIterator.isDone()) {
                int segmentType = pathIterator.currentSegment(inkSegment);
                if (segmentType == 0) {
                    segment = new ArrayList<>();
                    segment.add(Float.valueOf(inkSegment[0]));
                    segment.add(Float.valueOf(inkSegment[1]));
                    inkLists.add(segment);
                } else if (segmentType == 1) {
                    segment.add(Float.valueOf(inkSegment[0]));
                    segment.add(Float.valueOf(inkSegment[1]));
                }
                pathIterator.next();
            }
        }
        return inkLists;
    }

    public static InkAnnotation getInstance(Library library, Rectangle rect) {
        StateManager stateManager = library.getStateManager();
        HashMap<Name, Object> entries = new HashMap<>();
        entries.put(Dictionary.TYPE_KEY, Annotation.TYPE_VALUE);
        entries.put(Dictionary.SUBTYPE_KEY, Annotation.SUBTYPE_INK);
        if (rect != null) {
            entries.put(Annotation.RECTANGLE_KEY, PRectangle.getPRectangleVector(rect));
        } else {
            entries.put(Annotation.RECTANGLE_KEY, new Rectangle(10, 10, 50, 100));
        }
        InkAnnotation inkAnnotation = new InkAnnotation(library, entries);
        inkAnnotation.init();
        inkAnnotation.setPObjectReference(stateManager.getNewReferencNumber());
        inkAnnotation.setNew(true);
        inkAnnotation.setFlag(64, false);
        inkAnnotation.setFlag(16, false);
        inkAnnotation.setFlag(8, false);
        inkAnnotation.setFlag(4, true);
        return inkAnnotation;
    }

    @Override // org.icepdf.core.pobjects.annotations.Annotation
    public void resetAppearanceStream(double dx, double dy, AffineTransform pageSpace) {
        this.matrix = new AffineTransform();
        this.shapes = new Shapes();
        setModifiedDate(PDate.formatDateTime(new Date()));
        AffineTransform af2 = new AffineTransform();
        af2.setToTranslation(dx * pageSpace.getScaleX(), (-dy) * pageSpace.getScaleY());
        this.inkPath = af2.createTransformedShape(this.inkPath);
        this.entries.put(INK_LIST_KEY, convertPathToArray(this.inkPath));
        this.entries.remove(APPEARANCE_STREAM_KEY);
        Stroke stroke = getBorderStyleStroke();
        AffineTransform af3 = new AffineTransform();
        af3.translate(-this.bbox.getMinX(), -this.bbox.getMinY());
        this.shapes.add(new TransformDrawCmd(af3));
        this.shapes.add(new StrokeDrawCmd(stroke));
        this.shapes.add(new ColorDrawCmd(this.color));
        this.shapes.add(new ShapeDrawCmd(this.inkPath));
        this.shapes.add(new DrawDrawCmd());
    }

    public Shape getInkPath() {
        return this.inkPath;
    }

    public void setInkPath(Shape inkPath) {
        this.inkPath = inkPath;
    }
}
