package javafx.scene.shape;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGArc;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Arc.class */
public class Arc extends Shape {
    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private DoubleProperty startAngle;
    private ObjectProperty<ArcType> type;
    private final Arc2D shape = new Arc2D();
    private final DoubleProperty radiusX = new DoublePropertyBase() { // from class: javafx.scene.shape.Arc.3
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Arc.this.impl_geomChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Arc.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "radiusX";
        }
    };
    private final DoubleProperty radiusY = new DoublePropertyBase() { // from class: javafx.scene.shape.Arc.4
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Arc.this.impl_geomChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Arc.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "radiusY";
        }
    };
    private final DoubleProperty length = new DoublePropertyBase() { // from class: javafx.scene.shape.Arc.6
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Arc.this.impl_geomChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Arc.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "length";
        }
    };

    public Arc() {
    }

    public Arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadiusX(radiusX);
        setRadiusY(radiusY);
        setStartAngle(startAngle);
        setLength(length);
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
            this.centerX = new DoublePropertyBase() { // from class: javafx.scene.shape.Arc.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Arc.this;
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
            this.centerY = new DoublePropertyBase() { // from class: javafx.scene.shape.Arc.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Arc.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "centerY";
                }
            };
        }
        return this.centerY;
    }

    public final void setRadiusX(double value) {
        this.radiusX.set(value);
    }

    public final double getRadiusX() {
        return this.radiusX.get();
    }

    public final DoubleProperty radiusXProperty() {
        return this.radiusX;
    }

    public final void setRadiusY(double value) {
        this.radiusY.set(value);
    }

    public final double getRadiusY() {
        return this.radiusY.get();
    }

    public final DoubleProperty radiusYProperty() {
        return this.radiusY;
    }

    public final void setStartAngle(double value) {
        if (this.startAngle != null || value != 0.0d) {
            startAngleProperty().set(value);
        }
    }

    public final double getStartAngle() {
        if (this.startAngle == null) {
            return 0.0d;
        }
        return this.startAngle.get();
    }

    public final DoubleProperty startAngleProperty() {
        if (this.startAngle == null) {
            this.startAngle = new DoublePropertyBase() { // from class: javafx.scene.shape.Arc.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Arc.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "startAngle";
                }
            };
        }
        return this.startAngle;
    }

    public final void setLength(double value) {
        this.length.set(value);
    }

    public final double getLength() {
        return this.length.get();
    }

    public final DoubleProperty lengthProperty() {
        return this.length;
    }

    public final void setType(ArcType value) {
        if (this.type != null || value != ArcType.OPEN) {
            typeProperty().set(value);
        }
    }

    public final ArcType getType() {
        return this.type == null ? ArcType.OPEN : this.type.get();
    }

    public final ObjectProperty<ArcType> typeProperty() {
        if (this.type == null) {
            this.type = new ObjectPropertyBase<ArcType>(ArcType.OPEN) { // from class: javafx.scene.shape.Arc.7
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Arc.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Arc.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "type";
                }
            };
        }
        return this.type;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGArc();
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Arc2D impl_configShape() {
        short tmpType;
        switch (getTypeInternal()) {
            case OPEN:
                tmpType = 0;
                break;
            case CHORD:
                tmpType = 1;
                break;
            default:
                tmpType = 2;
                break;
        }
        this.shape.setArc((float) (getCenterX() - getRadiusX()), (float) (getCenterY() - getRadiusY()), (float) (getRadiusX() * 2.0d), (float) (getRadiusY() * 2.0d), (float) getStartAngle(), (float) getLength(), tmpType);
        return this.shape;
    }

    private final ArcType getTypeInternal() {
        ArcType t2 = getType();
        return t2 == null ? ArcType.OPEN : t2;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGArc peer = (NGArc) impl_getPeer();
            peer.updateArc((float) getCenterX(), (float) getCenterY(), (float) getRadiusX(), (float) getRadiusY(), (float) getStartAngle(), (float) getLength(), getTypeInternal());
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Arc[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("centerX=").append(getCenterX());
        sb.append(", centerY=").append(getCenterY());
        sb.append(", radiusX=").append(getRadiusX());
        sb.append(", radiusY=").append(getRadiusY());
        sb.append(", startAngle=").append(getStartAngle());
        sb.append(", length=").append(getLength());
        sb.append(", type=").append((Object) getType());
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
