package javafx.scene.shape;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPolygon;
import com.sun.javafx.sg.prism.NGShape;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Polygon.class */
public class Polygon extends Shape {
    private final Path2D shape = new Path2D();
    private final ObservableList<Double> points = new TrackableObservableList<Double>() { // from class: javafx.scene.shape.Polygon.1
        @Override // com.sun.javafx.collections.TrackableObservableList
        protected void onChanged(ListChangeListener.Change<Double> c2) {
            Polygon.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Polygon.this.impl_geomChanged();
        }
    };

    public Polygon() {
    }

    public Polygon(double... points) {
        if (points != null) {
            for (double p2 : points) {
                getPoints().add(Double.valueOf(p2));
            }
        }
    }

    public final ObservableList<Double> getPoints() {
        return this.points;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGPolygon();
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        if (this.impl_mode == NGShape.Mode.EMPTY || getPoints().size() <= 1) {
            return bounds.makeEmpty();
        }
        if (getPoints().size() == 2) {
            if (this.impl_mode == NGShape.Mode.FILL || getStrokeType() == StrokeType.INSIDE) {
                return bounds.makeEmpty();
            }
            double upad = getStrokeWidth();
            if (getStrokeType() == StrokeType.CENTERED) {
                upad /= 2.0d;
            }
            return computeBounds(bounds, tx, upad, 0.5d, getPoints().get(0).doubleValue(), getPoints().get(1).doubleValue(), 0.0d, 0.0d);
        }
        return computeShapeBounds(bounds, tx, impl_configShape());
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Path2D impl_configShape() {
        double p1 = getPoints().get(0).doubleValue();
        double p2 = getPoints().get(1).doubleValue();
        this.shape.reset();
        this.shape.moveTo((float) p1, (float) p2);
        int numValidPoints = getPoints().size() & (-2);
        for (int i2 = 2; i2 < numValidPoints; i2 += 2) {
            double p12 = getPoints().get(i2).doubleValue();
            double p22 = getPoints().get(i2 + 1).doubleValue();
            this.shape.lineTo((float) p12, (float) p22);
        }
        this.shape.closePath();
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            int numValidPoints = getPoints().size() & (-2);
            float[] points_array = new float[numValidPoints];
            for (int i2 = 0; i2 < numValidPoints; i2++) {
                points_array[i2] = (float) getPoints().get(i2).doubleValue();
            }
            NGPolygon peer = (NGPolygon) impl_getPeer();
            peer.updatePolygon(points_array);
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Polygon[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("points=").append((Object) getPoints());
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
