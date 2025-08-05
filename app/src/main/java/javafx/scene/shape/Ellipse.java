package javafx.scene.shape;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGEllipse;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Ellipse.class */
public class Ellipse extends Shape {
    private final Ellipse2D shape;
    private static final int NON_RECTILINEAR_TYPE_MASK = -80;
    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private final DoubleProperty radiusX;
    private final DoubleProperty radiusY;

    public Ellipse() {
        this.shape = new Ellipse2D();
        this.radiusX = new DoublePropertyBase() { // from class: javafx.scene.shape.Ellipse.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Ellipse.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Ellipse.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "radiusX";
            }
        };
        this.radiusY = new DoublePropertyBase() { // from class: javafx.scene.shape.Ellipse.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Ellipse.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Ellipse.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "radiusY";
            }
        };
    }

    public Ellipse(double radiusX, double radiusY) {
        this.shape = new Ellipse2D();
        this.radiusX = new DoublePropertyBase() { // from class: javafx.scene.shape.Ellipse.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Ellipse.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Ellipse.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "radiusX";
            }
        };
        this.radiusY = new DoublePropertyBase() { // from class: javafx.scene.shape.Ellipse.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Ellipse.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Ellipse.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "radiusY";
            }
        };
        setRadiusX(radiusX);
        setRadiusY(radiusY);
    }

    public Ellipse(double centerX, double centerY, double radiusX, double radiusY) {
        this(radiusX, radiusY);
        setCenterX(centerX);
        setCenterY(centerY);
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
            this.centerX = new DoublePropertyBase() { // from class: javafx.scene.shape.Ellipse.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Ellipse.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Ellipse.this;
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
            this.centerY = new DoublePropertyBase() { // from class: javafx.scene.shape.Ellipse.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Ellipse.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Ellipse.this;
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

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGEllipse();
    }

    @Override // javafx.scene.shape.Shape
    StrokeLineJoin convertLineJoin(StrokeLineJoin t2) {
        return StrokeLineJoin.BEVEL;
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException
        */
    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r16v0 'this'  ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to set immutable type for var: r16v0 'this'  ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 3, insn: MOVE (r1 I:??) = (r3 I:??), block:B:14:0x0061 */
    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @java.lang.Deprecated
    public com.sun.javafx.geom.BaseBounds impl_computeGeomBounds(com.sun.javafx.geom.BaseBounds r17, com.sun.javafx.geom.transform.BaseTransform r18) {
        /*
            r16 = this;
            r0 = r16
            com.sun.javafx.sg.prism.NGShape$Mode r0 = r0.impl_mode
            com.sun.javafx.sg.prism.NGShape$Mode r1 = com.sun.javafx.sg.prism.NGShape.Mode.EMPTY
            if (r0 != r1) goto Lf
            r0 = r17
            com.sun.javafx.geom.BaseBounds r0 = r0.makeEmpty()
            return r0
        Lf:
            r0 = r18
            int r0 = r0.getType()
            r1 = -80
            r0 = r0 & r1
            if (r0 == 0) goto L24
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r16
            com.sun.javafx.geom.Ellipse2D r3 = r3.impl_configShape()
            com.sun.javafx.geom.BaseBounds r0 = r0.computeShapeBounds(r1, r2, r3)
            return r0
        L24:
            r0 = r16
            double r0 = r0.getCenterX()
            r1 = r16
            double r1 = r1.getRadiusX()
            double r0 = r0 - r1
            r19 = r0
            r0 = r16
            double r0 = r0.getCenterY()
            r1 = r16
            double r1 = r1.getRadiusY()
            double r0 = r0 - r1
            r21 = r0
            r0 = 4611686018427387904(0x4000000000000000, double:2.0)
            r1 = r16
            double r1 = r1.getRadiusX()
            double r0 = r0 * r1
            r23 = r0
            r0 = 4611686018427387904(0x4000000000000000, double:2.0)
            r1 = r16
            double r1 = r1.getRadiusY()
            double r0 = r0 * r1
            r25 = r0
            r0 = r16
            com.sun.javafx.sg.prism.NGShape$Mode r0 = r0.impl_mode
            com.sun.javafx.sg.prism.NGShape$Mode r1 = com.sun.javafx.sg.prism.NGShape.Mode.FILL
            if (r0 == r1) goto L61
            r0 = r16
            javafx.scene.shape.StrokeType r0 = r0.getStrokeType()
            javafx.scene.shape.StrokeType r1 = javafx.scene.shape.StrokeType.INSIDE
            if (r0 != r1) goto L6a
        L61:
            r0 = 0
            r1 = r0; r1 = r3; 
            r29 = r1
            r27 = r0
            goto L85
        L6a:
            r0 = r16
            double r0 = r0.getStrokeWidth()
            r27 = r0
            r0 = r16
            javafx.scene.shape.StrokeType r0 = r0.getStrokeType()
            javafx.scene.shape.StrokeType r1 = javafx.scene.shape.StrokeType.CENTERED
            if (r0 != r1) goto L82
            r0 = r27
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r0 = r0 / r1
            r27 = r0
        L82:
            r0 = 0
            r29 = r0
        L85:
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r27
            r4 = r29
            r5 = r19
            r6 = r21
            r7 = r23
            r8 = r25
            com.sun.javafx.geom.BaseBounds r0 = r0.computeBounds(r1, r2, r3, r4, r5, r6, r7, r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.shape.Ellipse.impl_computeGeomBounds(com.sun.javafx.geom.BaseBounds, com.sun.javafx.geom.transform.BaseTransform):com.sun.javafx.geom.BaseBounds");
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Ellipse2D impl_configShape() {
        this.shape.setFrame((float) (getCenterX() - getRadiusX()), (float) (getCenterY() - getRadiusY()), (float) (getRadiusX() * 2.0d), (float) (getRadiusY() * 2.0d));
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGEllipse peer = (NGEllipse) impl_getPeer();
            peer.updateEllipse((float) getCenterX(), (float) getCenterY(), (float) getRadiusX(), (float) getRadiusY());
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Ellipse[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("centerX=").append(getCenterX());
        sb.append(", centerY=").append(getCenterY());
        sb.append(", radiusX=").append(getRadiusX());
        sb.append(", radiusY=").append(getRadiusY());
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
