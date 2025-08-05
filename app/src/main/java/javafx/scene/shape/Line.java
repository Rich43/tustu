package javafx.scene.shape;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGLine;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* loaded from: jfxrt.jar:javafx/scene/shape/Line.class */
public class Line extends Shape {
    private final Line2D shape = new Line2D();
    private final DoubleProperty startX;
    private final DoubleProperty startY;
    private final DoubleProperty endX;
    private final DoubleProperty endY;

    public Line() {
        ((StyleableProperty) fillProperty()).applyStyle(null, null);
        ((StyleableProperty) strokeProperty()).applyStyle(null, Color.BLACK);
        this.startX = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.1
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startX";
            }
        };
        this.startY = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.2
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startY";
            }
        };
        this.endX = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "endX";
            }
        };
        this.endY = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "endY";
            }
        };
    }

    public Line(double startX, double startY, double endX, double endY) {
        ((StyleableProperty) fillProperty()).applyStyle(null, null);
        ((StyleableProperty) strokeProperty()).applyStyle(null, Color.BLACK);
        this.startX = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.1
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startX";
            }
        };
        this.startY = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.2
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "startY";
            }
        };
        this.endX = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "endX";
            }
        };
        this.endY = new DoublePropertyBase() { // from class: javafx.scene.shape.Line.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Line.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Line.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Line.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "endY";
            }
        };
        setStartX(startX);
        setStartY(startY);
        setEndX(endX);
        setEndY(endY);
    }

    public final void setStartX(double value) {
        this.startX.set(value);
    }

    public final double getStartX() {
        return this.startX.get();
    }

    public final DoubleProperty startXProperty() {
        return this.startX;
    }

    public final void setStartY(double value) {
        this.startY.set(value);
    }

    public final double getStartY() {
        return this.startY.get();
    }

    public final DoubleProperty startYProperty() {
        return this.startY;
    }

    public final void setEndX(double value) {
        this.endX.set(value);
    }

    public final double getEndX() {
        return this.endX.get();
    }

    public final DoubleProperty endXProperty() {
        return this.endX;
    }

    public final void setEndY(double value) {
        this.endY.set(value);
    }

    public final double getEndY() {
        return this.endY.get();
    }

    public final DoubleProperty endYProperty() {
        return this.endY;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGLine();
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException
        */
    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v100 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v101 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v102 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v103 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v104 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v105 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v106 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v107 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v11 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v117 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v126 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v128 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v13 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v130 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v132 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v134 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v136 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v138 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v15 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v17 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v23 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v25 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v27 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v28 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v29 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v32 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v33 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v34 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v35 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v36 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v37 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v42 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v45 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v49 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v53 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v57 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v75 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v76 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v79 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v81 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v87 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v88 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v9 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v96 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r0v98 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r11v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r11v7 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v7 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v7 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r17v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r17v7 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r19v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r19v2 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r19v4 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r19v5 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v28 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v30 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v34 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v36 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v40 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v42 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v46 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v48 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v58 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v59 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v64 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v65 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v67 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v68 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v69 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v70 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v71 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v72 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v75 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r1v8 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v13 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v17 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v21 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v25 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v11 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v12 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v14 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v15 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v17 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v18 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v2 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v20 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v21 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v23 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v24 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v3 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v5 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v6 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v8 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r3v9 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v10 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v11 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v12 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v13 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v14 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v15 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v2 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v3 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v4 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v5 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v6 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v7 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v8 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r4v9 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v0 'this'  ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to set immutable type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to set immutable type for var: r8v0 'this'  ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 3, insn: MOVE (r1 I:??) = (r3 I:??), block:B:55:0x01a7 */
    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @java.lang.Deprecated
    public com.sun.javafx.geom.BaseBounds impl_computeGeomBounds(com.sun.javafx.geom.BaseBounds r9, com.sun.javafx.geom.transform.BaseTransform r10) {
        /*
            Method dump skipped, instructions count: 695
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.shape.Line.impl_computeGeomBounds(com.sun.javafx.geom.BaseBounds, com.sun.javafx.geom.transform.BaseTransform):com.sun.javafx.geom.BaseBounds");
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public Line2D impl_configShape() {
        this.shape.setLine((float) getStartX(), (float) getStartY(), (float) getEndX(), (float) getEndY());
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGLine peer = (NGLine) impl_getPeer();
            peer.updateLine((float) getStartX(), (float) getStartY(), (float) getEndX(), (float) getEndY());
        }
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    protected Paint impl_cssGetFillInitialValue() {
        return null;
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    protected Paint impl_cssGetStrokeInitialValue() {
        return Color.BLACK;
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Line[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("startX=").append(getStartX());
        sb.append(", startY=").append(getStartY());
        sb.append(", endX=").append(getEndX());
        sb.append(", endY=").append(getEndY());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
