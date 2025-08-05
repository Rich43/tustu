package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/shape/LineTo.class */
public class LineTo extends PathElement {

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12725x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12726y;

    public LineTo() {
    }

    public LineTo(double x2, double y2) {
        setX(x2);
        setY(y2);
    }

    public final void setX(double value) {
        if (this.f12725x != null || value != 0.0d) {
            xProperty().set(value);
        }
    }

    public final double getX() {
        if (this.f12725x == null) {
            return 0.0d;
        }
        return this.f12725x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12725x == null) {
            this.f12725x = new DoublePropertyBase() { // from class: javafx.scene.shape.LineTo.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    LineTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return LineTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12725x;
    }

    public final void setY(double value) {
        if (this.f12726y != null || value != 0.0d) {
            yProperty().set(value);
        }
    }

    public final double getY() {
        if (this.f12726y == null) {
            return 0.0d;
        }
        return this.f12726y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12726y == null) {
            this.f12726y = new DoublePropertyBase() { // from class: javafx.scene.shape.LineTo.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    LineTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return LineTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12726y;
    }

    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        if (isAbsolute()) {
            pgPath.addLineTo((float) getX(), (float) getY());
        } else {
            pgPath.addLineTo((float) (pgPath.getCurrentX() + getX()), (float) (pgPath.getCurrentY() + getY()));
        }
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        if (isAbsolute()) {
            path.lineTo((float) getX(), (float) getY());
        } else {
            path.lineTo((float) (path.getCurrentX() + getX()), (float) (path.getCurrentY() + getY()));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("LineTo[");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        return sb.append("]").toString();
    }
}
