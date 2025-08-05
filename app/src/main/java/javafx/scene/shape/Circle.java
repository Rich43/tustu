package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCircle;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Circle.class */
public class Circle extends Shape {
    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private final Ellipse2D shape = new Ellipse2D();
    private final DoubleProperty radius = new DoublePropertyBase() { // from class: javafx.scene.shape.Circle.3
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Circle.this.impl_geomChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Circle.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "radius";
        }
    };

    public Circle(double radius) {
        setRadius(radius);
    }

    public Circle(double radius, Paint fill) {
        setRadius(radius);
        setFill(fill);
    }

    public Circle() {
    }

    public Circle(double centerX, double centerY, double radius) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(radius);
    }

    public Circle(double centerX, double centerY, double radius, Paint fill) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(radius);
        setFill(fill);
    }

    public final void setCenterX(double value) {
        if (this.centerX != null || value != 0.0d) {
            centerXProperty().set(value);
        }
    }

    public final double getCenterX() {
        if (this.centerX == null) {
            return 0.0d;
        }
        return this.centerX.get();
    }

    public final DoubleProperty centerXProperty() {
        if (this.centerX == null) {
            this.centerX = new DoublePropertyBase(0.0d) { // from class: javafx.scene.shape.Circle.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Circle.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Circle.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "centerX";
                }
            };
        }
        return this.centerX;
    }

    public final void setCenterY(double value) {
        if (this.centerY != null || value != 0.0d) {
            centerYProperty().set(value);
        }
    }

    public final double getCenterY() {
        if (this.centerY == null) {
            return 0.0d;
        }
        return this.centerY.get();
    }

    public final DoubleProperty centerYProperty() {
        if (this.centerY == null) {
            this.centerY = new DoublePropertyBase(0.0d) { // from class: javafx.scene.shape.Circle.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Circle.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Circle.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "centerY";
                }
            };
        }
        return this.centerY;
    }

    public final void setRadius(double value) {
        this.radius.set(value);
    }

    public final double getRadius() {
        return this.radius.get();
    }

    public final DoubleProperty radiusProperty() {
        return this.radius;
    }

    @Override // javafx.scene.shape.Shape
    StrokeLineJoin convertLineJoin(StrokeLineJoin t2) {
        return StrokeLineJoin.BEVEL;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCircle();
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        double upad;
        if (this.impl_mode == NGShape.Mode.EMPTY) {
            return bounds.makeEmpty();
        }
        double cX = getCenterX();
        double cY = getCenterY();
        if ((tx.getType() & (-26)) == 0) {
            double tCX = (cX * tx.getMxx()) + (cY * tx.getMxy()) + tx.getMxt();
            double tCY = (cX * tx.getMyx()) + (cY * tx.getMyy()) + tx.getMyt();
            double r2 = getRadius();
            if (this.impl_mode != NGShape.Mode.FILL && getStrokeType() != StrokeType.INSIDE) {
                double upad2 = getStrokeWidth();
                if (getStrokeType() == StrokeType.CENTERED) {
                    upad2 /= 2.0d;
                }
                r2 += upad2;
            }
            return bounds.deriveWithNewBounds((float) (tCX - r2), (float) (tCY - r2), 0.0f, (float) (tCX + r2), (float) (tCY + r2), 0.0f);
        }
        if ((tx.getType() & (-72)) == 0) {
            double r3 = getRadius();
            double x2 = getCenterX() - r3;
            double y2 = getCenterY() - r3;
            double width = 2.0d * r3;
            if (this.impl_mode == NGShape.Mode.FILL || getStrokeType() == StrokeType.INSIDE) {
                upad = 0.0d;
            } else {
                upad = getStrokeWidth();
            }
            return computeBounds(bounds, tx, upad, 0.0d, x2, y2, width, width);
        }
        return computeShapeBounds(bounds, tx, impl_configShape());
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Ellipse2D impl_configShape() {
        double r2 = getRadius();
        this.shape.setFrame((float) (getCenterX() - r2), (float) (getCenterY() - r2), (float) (r2 * 2.0d), (float) (r2 * 2.0d));
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGCircle peer = (NGCircle) impl_getPeer();
            peer.updateCircle((float) getCenterX(), (float) getCenterY(), (float) getRadius());
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Circle[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("centerX=").append(getCenterX());
        sb.append(", centerY=").append(getCenterY());
        sb.append(", radius=").append(getRadius());
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
