package javafx.scene.shape;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCubicCurve;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/CubicCurve.class */
public class CubicCurve extends Shape {
    private final CubicCurve2D shape = new CubicCurve2D();
    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty controlX1;
    private DoubleProperty controlY1;
    private DoubleProperty controlX2;
    private DoubleProperty controlY2;
    private DoubleProperty endX;
    private DoubleProperty endY;

    public CubicCurve() {
    }

    public CubicCurve(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY) {
        setStartX(startX);
        setStartY(startY);
        setControlX1(controlX1);
        setControlY1(controlY1);
        setControlX2(controlX2);
        setControlY2(controlY2);
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
            this.startX = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
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
            this.startY = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "startY";
                }
            };
        }
        return this.startY;
    }

    public final void setControlX1(double value) {
        if (this.controlX1 != null || value != 0.0d) {
            controlX1Property().set(value);
        }
    }

    public final double getControlX1() {
        if (this.controlX1 == null) {
            return 0.0d;
        }
        return this.controlX1.get();
    }

    public final DoubleProperty controlX1Property() {
        if (this.controlX1 == null) {
            this.controlX1 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "controlX1";
                }
            };
        }
        return this.controlX1;
    }

    public final void setControlY1(double value) {
        if (this.controlY1 != null || value != 0.0d) {
            controlY1Property().set(value);
        }
    }

    public final double getControlY1() {
        if (this.controlY1 == null) {
            return 0.0d;
        }
        return this.controlY1.get();
    }

    public final DoubleProperty controlY1Property() {
        if (this.controlY1 == null) {
            this.controlY1 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "controlY1";
                }
            };
        }
        return this.controlY1;
    }

    public final void setControlX2(double value) {
        if (this.controlX2 != null || value != 0.0d) {
            controlX2Property().set(value);
        }
    }

    public final double getControlX2() {
        if (this.controlX2 == null) {
            return 0.0d;
        }
        return this.controlX2.get();
    }

    public final DoubleProperty controlX2Property() {
        if (this.controlX2 == null) {
            this.controlX2 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "controlX2";
                }
            };
        }
        return this.controlX2;
    }

    public final void setControlY2(double value) {
        if (this.controlY2 != null || value != 0.0d) {
            controlY2Property().set(value);
        }
    }

    public final double getControlY2() {
        if (this.controlY2 == null) {
            return 0.0d;
        }
        return this.controlY2.get();
    }

    public final DoubleProperty controlY2Property() {
        if (this.controlY2 == null) {
            this.controlY2 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "controlY2";
                }
            };
        }
        return this.controlY2;
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
            this.endX = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
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
            this.endY = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurve.8
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    CubicCurve.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurve.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "endY";
                }
            };
        }
        return this.endY;
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public CubicCurve2D impl_configShape() {
        this.shape.x1 = (float) getStartX();
        this.shape.y1 = (float) getStartY();
        this.shape.ctrlx1 = (float) getControlX1();
        this.shape.ctrly1 = (float) getControlY1();
        this.shape.ctrlx2 = (float) getControlX2();
        this.shape.ctrly2 = (float) getControlY2();
        this.shape.x2 = (float) getEndX();
        this.shape.y2 = (float) getEndY();
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGCubicCurve();
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGCubicCurve peer = (NGCubicCurve) impl_getPeer();
            peer.updateCubicCurve((float) getStartX(), (float) getStartY(), (float) getEndX(), (float) getEndY(), (float) getControlX1(), (float) getControlY1(), (float) getControlX2(), (float) getControlY2());
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("CubicCurve[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("startX=").append(getStartX());
        sb.append(", startY=").append(getStartY());
        sb.append(", controlX1=").append(getControlX1());
        sb.append(", controlY1=").append(getControlY1());
        sb.append(", controlX2=").append(getControlX2());
        sb.append(", controlY2=").append(getControlY2());
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
