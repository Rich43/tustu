package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:javafx/scene/shape/VLineTo.class */
public class VLineTo extends PathElement {

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12741y = new DoublePropertyBase() { // from class: javafx.scene.shape.VLineTo.1
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            VLineTo.this.u();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return VLineTo.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return PdfOps.y_TOKEN;
        }
    };

    public VLineTo() {
    }

    public VLineTo(double y2) {
        setY(y2);
    }

    public final void setY(double value) {
        this.f12741y.set(value);
    }

    public final double getY() {
        return this.f12741y.get();
    }

    public final DoubleProperty yProperty() {
        return this.f12741y;
    }

    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        if (isAbsolute()) {
            pgPath.addLineTo(pgPath.getCurrentX(), (float) getY());
        } else {
            pgPath.addLineTo(pgPath.getCurrentX(), (float) (pgPath.getCurrentY() + getY()));
        }
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        if (isAbsolute()) {
            path.lineTo(path.getCurrentX(), (float) getY());
        } else {
            path.lineTo(path.getCurrentX(), (float) (path.getCurrentY() + getY()));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("VLineTo[");
        sb.append("y=").append(getY());
        return sb.append("]").toString();
    }
}
