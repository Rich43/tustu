package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/shape/CubicCurveTo.class */
public class CubicCurveTo extends PathElement {
    private DoubleProperty controlX1;
    private DoubleProperty controlY1;
    private DoubleProperty controlX2;
    private DoubleProperty controlY2;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12719x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12720y;

    public CubicCurveTo() {
    }

    public CubicCurveTo(double controlX1, double controlY1, double controlX2, double controlY2, double x2, double y2) {
        setControlX1(controlX1);
        setControlY1(controlY1);
        setControlX2(controlX2);
        setControlY2(controlY2);
        setX(x2);
        setY(y2);
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
            this.controlX1 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurveTo.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurveTo.this;
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
            this.controlY1 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurveTo.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurveTo.this;
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
            this.controlX2 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurveTo.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurveTo.this;
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
            this.controlY2 = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurveTo.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurveTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "controlY2";
                }
            };
        }
        return this.controlY2;
    }

    public final void setX(double value) {
        if (this.f12719x != null || value != 0.0d) {
            xProperty().set(value);
        }
    }

    public final double getX() {
        if (this.f12719x == null) {
            return 0.0d;
        }
        return this.f12719x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12719x == null) {
            this.f12719x = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurveTo.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurveTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12719x;
    }

    public final void setY(double value) {
        if (this.f12720y != null || value != 0.0d) {
            yProperty().set(value);
        }
    }

    public final double getY() {
        if (this.f12720y == null) {
            return 0.0d;
        }
        return this.f12720y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12720y == null) {
            this.f12720y = new DoublePropertyBase() { // from class: javafx.scene.shape.CubicCurveTo.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    CubicCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CubicCurveTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12720y;
    }

    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        if (isAbsolute()) {
            pgPath.addCubicTo((float) getControlX1(), (float) getControlY1(), (float) getControlX2(), (float) getControlY2(), (float) getX(), (float) getY());
            return;
        }
        double dx = pgPath.getCurrentX();
        double dy = pgPath.getCurrentY();
        pgPath.addCubicTo((float) (getControlX1() + dx), (float) (getControlY1() + dy), (float) (getControlX2() + dx), (float) (getControlY2() + dy), (float) (getX() + dx), (float) (getY() + dy));
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        if (isAbsolute()) {
            path.curveTo((float) getControlX1(), (float) getControlY1(), (float) getControlX2(), (float) getControlY2(), (float) getX(), (float) getY());
            return;
        }
        double dx = path.getCurrentX();
        double dy = path.getCurrentY();
        path.curveTo((float) (getControlX1() + dx), (float) (getControlY1() + dy), (float) (getControlX2() + dx), (float) (getControlY2() + dy), (float) (getX() + dx), (float) (getY() + dy));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CubicCurveTo[");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", controlX1=").append(getControlX1());
        sb.append(", controlY1=").append(getControlY1());
        sb.append(", controlX2=").append(getControlX2());
        sb.append(", controlY2=").append(getControlY2());
        return sb.append("]").toString();
    }
}
