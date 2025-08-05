package javafx.scene.shape;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.media.jfxmedia.MetadataParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.paint.Paint;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/shape/Rectangle.class */
public class Rectangle extends Shape {
    private final RoundRectangle2D shape;
    private static final int NON_RECTILINEAR_TYPE_MASK = -80;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12737x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12738y;
    private final DoubleProperty width;
    private final DoubleProperty height;
    private DoubleProperty arcWidth;
    private DoubleProperty arcHeight;

    public Rectangle() {
        this.shape = new RoundRectangle2D();
        this.width = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Rectangle.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Rectangle.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return MetadataParser.WIDTH_TAG_NAME;
            }
        };
        this.height = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Rectangle.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Rectangle.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return MetadataParser.HEIGHT_TAG_NAME;
            }
        };
    }

    public Rectangle(double width, double height) {
        this.shape = new RoundRectangle2D();
        this.width = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Rectangle.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Rectangle.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return MetadataParser.WIDTH_TAG_NAME;
            }
        };
        this.height = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Rectangle.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Rectangle.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return MetadataParser.HEIGHT_TAG_NAME;
            }
        };
        setWidth(width);
        setHeight(height);
    }

    public Rectangle(double width, double height, Paint fill) {
        this.shape = new RoundRectangle2D();
        this.width = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.3
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Rectangle.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Rectangle.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return MetadataParser.WIDTH_TAG_NAME;
            }
        };
        this.height = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.4
            @Override // javafx.beans.property.DoublePropertyBase
            public void invalidated() {
                Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                Rectangle.this.impl_geomChanged();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Rectangle.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return MetadataParser.HEIGHT_TAG_NAME;
            }
        };
        setWidth(width);
        setHeight(height);
        setFill(fill);
    }

    public Rectangle(double x2, double y2, double width, double height) {
        this(width, height);
        setX(x2);
        setY(y2);
    }

    public final void setX(double value) {
        if (this.f12737x != null || value != 0.0d) {
            xProperty().set(value);
        }
    }

    public final double getX() {
        if (this.f12737x == null) {
            return 0.0d;
        }
        return this.f12737x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12737x == null) {
            this.f12737x = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Rectangle.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12737x;
    }

    public final void setY(double value) {
        if (this.f12738y != null || value != 0.0d) {
            yProperty().set(value);
        }
    }

    public final double getY() {
        if (this.f12738y == null) {
            return 0.0d;
        }
        return this.f12738y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12738y == null) {
            this.f12738y = new DoublePropertyBase() { // from class: javafx.scene.shape.Rectangle.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                    Rectangle.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12738y;
    }

    public final void setWidth(double value) {
        this.width.set(value);
    }

    public final double getWidth() {
        return this.width.get();
    }

    public final DoubleProperty widthProperty() {
        return this.width;
    }

    public final void setHeight(double value) {
        this.height.set(value);
    }

    public final double getHeight() {
        return this.height.get();
    }

    public final DoubleProperty heightProperty() {
        return this.height;
    }

    public final void setArcWidth(double value) {
        if (this.arcWidth != null || value != 0.0d) {
            arcWidthProperty().set(value);
        }
    }

    public final double getArcWidth() {
        if (this.arcWidth == null) {
            return 0.0d;
        }
        return this.arcWidth.get();
    }

    public final DoubleProperty arcWidthProperty() {
        if (this.arcWidth == null) {
            this.arcWidth = new StyleableDoubleProperty() { // from class: javafx.scene.shape.Rectangle.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.ARC_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "arcWidth";
                }
            };
        }
        return this.arcWidth;
    }

    public final void setArcHeight(double value) {
        if (this.arcHeight != null || value != 0.0d) {
            arcHeightProperty().set(value);
        }
    }

    public final double getArcHeight() {
        if (this.arcHeight == null) {
            return 0.0d;
        }
        return this.arcHeight.get();
    }

    public final DoubleProperty arcHeightProperty() {
        if (this.arcHeight == null) {
            this.arcHeight = new StyleableDoubleProperty() { // from class: javafx.scene.shape.Rectangle.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Rectangle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.ARC_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Rectangle.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "arcHeight";
                }
            };
        }
        return this.arcHeight;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        return new NGRectangle();
    }

    /* loaded from: jfxrt.jar:javafx/scene/shape/Rectangle$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Rectangle, Number> ARC_HEIGHT = new CssMetaData<Rectangle, Number>("-fx-arc-height", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.shape.Rectangle.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Rectangle node) {
                return node.arcHeight == null || !node.arcHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Rectangle node) {
                return (StyleableProperty) node.arcHeightProperty();
            }
        };
        private static final CssMetaData<Rectangle, Number> ARC_WIDTH = new CssMetaData<Rectangle, Number>("-fx-arc-width", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.shape.Rectangle.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Rectangle node) {
                return node.arcWidth == null || !node.arcWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(Rectangle node) {
                return (StyleableProperty) node.arcWidthProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Shape.getClassCssMetaData());
            styleables.add(ARC_HEIGHT);
            styleables.add(ARC_WIDTH);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.shape.Shape
    StrokeLineJoin convertLineJoin(StrokeLineJoin t2) {
        if (getArcWidth() > 0.0d && getArcHeight() > 0.0d) {
            return StrokeLineJoin.BEVEL;
        }
        return t2;
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException
        */
    /* JADX WARN: Not initialized variable reg: 3, insn: MOVE (r2 I:??) = (r3 I:??), block:B:18:0x004a */
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
            r0 = r16
            double r0 = r0.getArcWidth()
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L36
            r0 = r16
            double r0 = r0.getArcHeight()
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L36
            r0 = r18
            int r0 = r0.getType()
            r1 = -80
            r0 = r0 & r1
            if (r0 == 0) goto L36
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r16
            com.sun.javafx.geom.RoundRectangle2D r3 = r3.impl_configShape()
            com.sun.javafx.geom.BaseBounds r0 = r0.computeShapeBounds(r1, r2, r3)
            return r0
        L36:
            r0 = r16
            com.sun.javafx.sg.prism.NGShape$Mode r0 = r0.impl_mode
            com.sun.javafx.sg.prism.NGShape$Mode r1 = com.sun.javafx.sg.prism.NGShape.Mode.FILL
            if (r0 == r1) goto L4a
            r0 = r16
            javafx.scene.shape.StrokeType r0 = r0.getStrokeType()
            javafx.scene.shape.StrokeType r1 = javafx.scene.shape.StrokeType.INSIDE
            if (r0 != r1) goto L52
        L4a:
            r0 = 0
            r1 = r0; r2 = r3; 
            r21 = r1
            r19 = r0
            goto L6a
        L52:
            r0 = r16
            double r0 = r0.getStrokeWidth()
            r19 = r0
            r0 = r16
            javafx.scene.shape.StrokeType r0 = r0.getStrokeType()
            javafx.scene.shape.StrokeType r1 = javafx.scene.shape.StrokeType.CENTERED
            if (r0 != r1) goto L67
            r0 = r19
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r0 = r0 / r1
            r19 = r0
        L67:
            r0 = 0
            r21 = r0
        L6a:
            r0 = r16
            r1 = r17
            r2 = r18
            r3 = r19
            r4 = r21
            r5 = r16
            double r5 = r5.getX()
            r6 = r16
            double r6 = r6.getY()
            r7 = r16
            double r7 = r7.getWidth()
            r8 = r16
            double r8 = r8.getHeight()
            com.sun.javafx.geom.BaseBounds r0 = r0.computeBounds(r1, r2, r3, r4, r5, r6, r7, r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.shape.Rectangle.impl_computeGeomBounds(com.sun.javafx.geom.BaseBounds, com.sun.javafx.geom.transform.BaseTransform):com.sun.javafx.geom.BaseBounds");
    }

    @Override // javafx.scene.shape.Shape
    @Deprecated
    public RoundRectangle2D impl_configShape() {
        if (getArcWidth() > 0.0d && getArcHeight() > 0.0d) {
            this.shape.setRoundRect((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight(), (float) getArcWidth(), (float) getArcHeight());
        } else {
            this.shape.setRoundRect((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight(), 0.0f, 0.0f);
        }
        return this.shape;
    }

    @Override // javafx.scene.shape.Shape, javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            NGRectangle peer = (NGRectangle) impl_getPeer();
            peer.updateRectangle((float) getX(), (float) getY(), (float) getWidth(), (float) getHeight(), (float) getArcWidth(), (float) getArcHeight());
        }
    }

    @Override // javafx.scene.Node
    public String toString() {
        StringBuilder sb = new StringBuilder("Rectangle[");
        String id = getId();
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", width=").append(getWidth());
        sb.append(", height=").append(getHeight());
        sb.append(", fill=").append((Object) getFill());
        Paint stroke = getStroke();
        if (stroke != null) {
            sb.append(", stroke=").append((Object) stroke);
            sb.append(", strokeWidth=").append(getStrokeWidth());
        }
        return sb.append("]").toString();
    }
}
