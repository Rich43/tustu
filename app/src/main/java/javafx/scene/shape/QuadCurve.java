package javafx.scene.shape;

import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGQuadCurve;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/QuadCurve.class */
public class QuadCurve extends Shape {
    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty endX;
    private DoubleProperty endY;
    private final QuadCurve2D shape = new QuadCurve2D();
    private DoubleProperty controlX = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurve.3
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            QuadCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            QuadCurve.this.impl_geomChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return QuadCurve.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "controlX";
        }
    };
    private DoubleProperty controlY = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurve.4
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            QuadCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            QuadCurve.this.impl_geomChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return QuadCurve.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "controlY";
        }
    };

    public QuadCurve() {
    }

    public QuadCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
        setStartX(startX);
        setStartY(startY);
        setControlX(controlX);
        setControlY(controlY);
        setEndX(endX);
        setEndY(endY);
    }

    public final void setStartX(double value) {
        if (this.startX != null || value != 0.0d) {
            startXProperty().set(value);
        }
    }

    public final double getStartX() {
        if (this.startX == null) {
            return 0.0d;
        }
        return this.startX.get();
    }

    public final DoubleProperty startXProperty() {
        if (this.startX == null) {
            this.startX = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurve.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    QuadCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    QuadCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return QuadCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "startX";
                }
            };
        }
        return this.startX;
    }

    public final void setStartY(double value) {
        if (this.startY != null || value != 0.0d) {
            startYProperty().set(value);
        }
    }

    public final double getStartY() {
        if (this.startY == null) {
            return 0.0d;
        }
        return this.startY.get();
    }

    public final DoubleProperty startYProperty() {
        if (this.startY == null) {
            this.startY = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurve.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    QuadCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    QuadCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return QuadCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "startY";
                }
            };
        }
        return this.startY;
    }

    public final void setControlX(double value) {
        this.controlX.set(value);
    }

    public final double getControlX() {
        return this.controlX.get();
    }

    public final DoubleProperty controlXProperty() {
        return this.controlX;
    }

    public final void setControlY(double value) {
        this.controlY.set(value);
    }

    public final double getControlY() {
        return this.controlY.get();
    }

    public final DoubleProperty controlYProperty() {
        return this.controlY;
    }

    public final void setEndX(double value) {
        if (this.endX != null || value != 0.0d) {
            endXProperty().set(value);
        }
    }

    public final double getEndX() {
        if (this.endX == null) {
            return 0.0d;
        }
        return this.endX.get();
    }

    public final DoubleProperty endXProperty() {
        if (this.endX == null) {
            this.endX = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurve.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    QuadCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    QuadCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return QuadCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "endX";
                }
            };
        }
        return this.endX;
    }

    public final void setEndY(double value) {
        if (this.endY != null || value != 0.0d) {
            endYProperty().set(value);
        }
    }

    public final double getEndY() {
        if (this.endY == null) {
            return 0.0d;
        }
        return this.endY.get();
    }

    public final DoubleProperty endYProperty() {
        if (this.endY == null) {
            this.endY = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurve.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    QuadCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    QuadCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return QuadCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "endY";
                }
            };
        }
        return this.endY;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGQuadCurve();
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public QuadCurve2D impl_configShape() {
        this.shape.x1 = (float) getStartX();
        this.shape.y1 = (float) getStartY();
        this.shape.ctrlx = (float) getControlX();
        this.shape.ctrly = (float) getControlY();
        this.shape.x2 = (float) getEndX();
        this.shape.y2 = (float) getEndY();
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGQuadCurve peer = (NGQuadCurve) impl_getPeer();
            peer.updateQuadCurve((float) getStartX(), (float) getStartY(), (float) getEndX(), (float) getEndY(), (float) getControlX(), (float) getControlY());
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("QuadCurve[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("startX=").append(getStartX());
        sb.append(", startY=").append(getStartY());
        sb.append(", controlX=").append(getControlX());
        sb.append(", controlY=").append(getControlY());
        sb.append(", endX=").append(getEndX());
        sb.append(", endY=").append(getEndY());
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
