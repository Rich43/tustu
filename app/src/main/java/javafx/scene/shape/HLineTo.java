package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/shape/HLineTo.class */
public class HLineTo extends PathElement {

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12723x = new DoublePropertyBase() { // from class: javafx.scene.shape.HLineTo.1
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            HLineTo.this.u();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return HLineTo.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return LanguageTag.PRIVATEUSE;
        }
    };

    public HLineTo() {
    }

    public HLineTo(double x2) {
        setX(x2);
    }

    public final void setX(double value) {
        this.f12723x.set(value);
    }

    public final double getX() {
        return this.f12723x.get();
    }

    public final DoubleProperty xProperty() {
        return this.f12723x;
    }

    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        if (isAbsolute()) {
            pgPath.addLineTo((float) getX(), pgPath.getCurrentY());
        } else {
            pgPath.addLineTo((float) (pgPath.getCurrentX() + getX()), pgPath.getCurrentY());
        }
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        if (isAbsolute()) {
            path.lineTo((float) getX(), path.getCurrentY());
        } else {
            path.lineTo((float) (path.getCurrentX() + getX()), path.getCurrentY());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("HLineTo[");
        sb.append("x=").append(getX());
        return sb.append("]").toString();
    }
}
