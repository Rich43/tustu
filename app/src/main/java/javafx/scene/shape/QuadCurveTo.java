package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/shape/QuadCurveTo.class */
public class QuadCurveTo extends PathElement {
    private DoubleProperty controlX = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurveTo.1
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            QuadCurveTo.this.u();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return QuadCurveTo.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "controlX";
        }
    };
    private DoubleProperty controlY = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurveTo.2
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            QuadCurveTo.this.u();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return QuadCurveTo.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "controlY";
        }
    };

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12733x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12734y;

    public QuadCurveTo() {
    }

    public QuadCurveTo(double controlX, double controlY, double x2, double y2) {
        setControlX(controlX);
        setControlY(controlY);
        setX(x2);
        setY(y2);
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

    public final void setX(double value) {
        if (this.f12733x != null || value != 0.0d) {
            xProperty().set(value);
        }
    }

    public final double getX() {
        if (this.f12733x == null) {
            return 0.0d;
        }
        return this.f12733x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12733x == null) {
            this.f12733x = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurveTo.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    QuadCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return QuadCurveTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12733x;
    }

    public final void setY(double value) {
        if (this.f12734y != null || value != 0.0d) {
            yProperty().set(value);
        }
    }

    public final double getY() {
        if (this.f12734y == null) {
            return 0.0d;
        }
        return this.f12734y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12734y == null) {
            this.f12734y = new DoublePropertyBase() { // from class: javafx.scene.shape.QuadCurveTo.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    QuadCurveTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return QuadCurveTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12734y;
    }

    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        if (isAbsolute()) {
            pgPath.addQuadTo((float) getControlX(), (float) getControlY(), (float) getX(), (float) getY());
            return;
        }
        double dx = pgPath.getCurrentX();
        double dy = pgPath.getCurrentY();
        pgPath.addQuadTo((float) (getControlX() + dx), (float) (getControlY() + dy), (float) (getX() + dx), (float) (getY() + dy));
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        if (isAbsolute()) {
            path.quadTo((float) getControlX(), (float) getControlY(), (float) getX(), (float) getY());
            return;
        }
        double dx = path.getCurrentX();
        double dy = path.getCurrentY();
        path.quadTo((float) (getControlX() + dx), (float) (getControlY() + dy), (float) (getX() + dx), (float) (getY() + dy));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CubicCurveTo[");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", controlX=").append(getControlX());
        sb.append(", controlY=").append(getControlY());
        return sb.append("]").toString();
    }
}
