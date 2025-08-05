package javafx.scene.shape;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/shape/ArcTo.class */
public class ArcTo extends PathElement {
    private DoubleProperty radiusX = new DoublePropertyBase() { // from class: javafx.scene.shape.ArcTo.1
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            ArcTo.this.u();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ArcTo.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "radiusX";
        }
    };
    private DoubleProperty radiusY = new DoublePropertyBase() { // from class: javafx.scene.shape.ArcTo.2
        @Override // javafx.beans.property.DoublePropertyBase
        public void invalidated() {
            ArcTo.this.u();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ArcTo.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "radiusY";
        }
    };
    private DoubleProperty xAxisRotation;
    private BooleanProperty largeArcFlag;
    private BooleanProperty sweepFlag;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12715x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12716y;

    public ArcTo() {
    }

    public ArcTo(double radiusX, double radiusY, double xAxisRotation, double x2, double y2, boolean largeArcFlag, boolean sweepFlag) {
        setRadiusX(radiusX);
        setRadiusY(radiusY);
        setXAxisRotation(xAxisRotation);
        setX(x2);
        setY(y2);
        setLargeArcFlag(largeArcFlag);
        setSweepFlag(sweepFlag);
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

    public final void setXAxisRotation(double value) {
        if (this.xAxisRotation != null || value != 0.0d) {
            XAxisRotationProperty().set(value);
        }
    }

    public final double getXAxisRotation() {
        if (this.xAxisRotation == null) {
            return 0.0d;
        }
        return this.xAxisRotation.get();
    }

    public final DoubleProperty XAxisRotationProperty() {
        if (this.xAxisRotation == null) {
            this.xAxisRotation = new DoublePropertyBase() { // from class: javafx.scene.shape.ArcTo.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "XAxisRotation";
                }
            };
        }
        return this.xAxisRotation;
    }

    public final void setLargeArcFlag(boolean value) {
        if (this.largeArcFlag != null || value) {
            largeArcFlagProperty().set(value);
        }
    }

    public final boolean isLargeArcFlag() {
        if (this.largeArcFlag == null) {
            return false;
        }
        return this.largeArcFlag.get();
    }

    public final BooleanProperty largeArcFlagProperty() {
        if (this.largeArcFlag == null) {
            this.largeArcFlag = new BooleanPropertyBase() { // from class: javafx.scene.shape.ArcTo.4
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "largeArcFlag";
                }
            };
        }
        return this.largeArcFlag;
    }

    public final void setSweepFlag(boolean value) {
        if (this.sweepFlag != null || value) {
            sweepFlagProperty().set(value);
        }
    }

    public final boolean isSweepFlag() {
        if (this.sweepFlag == null) {
            return false;
        }
        return this.sweepFlag.get();
    }

    public final BooleanProperty sweepFlagProperty() {
        if (this.sweepFlag == null) {
            this.sweepFlag = new BooleanPropertyBase() { // from class: javafx.scene.shape.ArcTo.5
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "sweepFlag";
                }
            };
        }
        return this.sweepFlag;
    }

    public final void setX(double value) {
        if (this.f12715x != null || value != 0.0d) {
            xProperty().set(value);
        }
    }

    public final double getX() {
        if (this.f12715x == null) {
            return 0.0d;
        }
        return this.f12715x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12715x == null) {
            this.f12715x = new DoublePropertyBase() { // from class: javafx.scene.shape.ArcTo.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12715x;
    }

    public final void setY(double value) {
        if (this.f12716y != null || value != 0.0d) {
            yProperty().set(value);
        }
    }

    public final double getY() {
        if (this.f12716y == null) {
            return 0.0d;
        }
        return this.f12716y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12716y == null) {
            this.f12716y = new DoublePropertyBase() { // from class: javafx.scene.shape.ArcTo.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    ArcTo.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ArcTo.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12716y;
    }

    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        addArcTo(pgPath, null, pgPath.getCurrentX(), pgPath.getCurrentY());
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        addArcTo(null, path, path.getCurrentX(), path.getCurrentY());
    }

    private void addArcTo(NGPath pgPath, Path2D path, double x0, double y0) {
        double localX = getX();
        double localY = getY();
        boolean localSweepFlag = isSweepFlag();
        boolean localLargeArcFlag = isLargeArcFlag();
        double xto = isAbsolute() ? localX : localX + x0;
        double yto = isAbsolute() ? localY : localY + y0;
        double dx2 = (x0 - xto) / 2.0d;
        double dy2 = (y0 - yto) / 2.0d;
        double xAxisRotationR = Math.toRadians(getXAxisRotation());
        double cosAngle = Math.cos(xAxisRotationR);
        double sinAngle = Math.sin(xAxisRotationR);
        double x1 = (cosAngle * dx2) + (sinAngle * dy2);
        double y1 = ((-sinAngle) * dx2) + (cosAngle * dy2);
        double rx = Math.abs(getRadiusX());
        double ry = Math.abs(getRadiusY());
        double Prx = rx * rx;
        double Pry = ry * ry;
        double Px1 = x1 * x1;
        double Py1 = y1 * y1;
        double radiiCheck = (Px1 / Prx) + (Py1 / Pry);
        if (radiiCheck > 1.0d) {
            rx = Math.sqrt(radiiCheck) * rx;
            ry = Math.sqrt(radiiCheck) * ry;
            if (rx != rx || ry != ry) {
                if (pgPath == null) {
                    path.lineTo((float) xto, (float) yto);
                    return;
                } else {
                    pgPath.addLineTo((float) xto, (float) yto);
                    return;
                }
            }
            Prx = rx * rx;
            Pry = ry * ry;
        }
        double sign = localLargeArcFlag == localSweepFlag ? -1.0d : 1.0d;
        double sq = (((Prx * Pry) - (Prx * Py1)) - (Pry * Px1)) / ((Prx * Py1) + (Pry * Px1));
        double coef = sign * Math.sqrt(sq < 0.0d ? 0.0d : sq);
        double cx1 = coef * ((rx * y1) / ry);
        double cy1 = coef * (-((ry * x1) / rx));
        double sx2 = (x0 + xto) / 2.0d;
        double sy2 = (y0 + yto) / 2.0d;
        double cx = sx2 + ((cosAngle * cx1) - (sinAngle * cy1));
        double cy = sy2 + (sinAngle * cx1) + (cosAngle * cy1);
        double ux = (x1 - cx1) / rx;
        double uy = (y1 - cy1) / ry;
        double vx = ((-x1) - cx1) / rx;
        double vy = ((-y1) - cy1) / ry;
        double n2 = Math.sqrt((ux * ux) + (uy * uy));
        double sign2 = uy < 0.0d ? -1.0d : 1.0d;
        double angleStart = Math.toDegrees(sign2 * Math.acos(ux / n2));
        double n3 = Math.sqrt(((ux * ux) + (uy * uy)) * ((vx * vx) + (vy * vy)));
        double p2 = (ux * vx) + (uy * vy);
        double sign3 = (ux * vy) - (uy * vx) < 0.0d ? -1.0d : 1.0d;
        double angleExtent = Math.toDegrees(sign3 * Math.acos(p2 / n3));
        if (!localSweepFlag && angleExtent > 0.0d) {
            angleExtent -= 360.0d;
        } else if (localSweepFlag && angleExtent < 0.0d) {
            angleExtent += 360.0d;
        }
        double angleStart2 = angleStart % 360.0d;
        float arcX = (float) (cx - rx);
        float arcY = (float) (cy - ry);
        float arcW = (float) (rx * 2.0d);
        float arcH = (float) (ry * 2.0d);
        float arcStart = (float) (-angleStart2);
        float arcExtent = (float) (-(angleExtent % 360.0d));
        if (pgPath == null) {
            Arc2D arc = new Arc2D(arcX, arcY, arcW, arcH, arcStart, arcExtent, 0);
            BaseTransform xform = xAxisRotationR == 0.0d ? null : BaseTransform.getRotateInstance(xAxisRotationR, cx, cy);
            PathIterator pi = arc.getPathIterator(xform);
            pi.next();
            path.append(pi, true);
            return;
        }
        pgPath.addArcTo(arcX, arcY, arcW, arcH, arcStart, arcExtent, (float) xAxisRotationR);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ArcTo[");
        sb.append("x=").append(getX());
        sb.append(", y=").append(getY());
        sb.append(", radiusX=").append(getRadiusX());
        sb.append(", radiusY=").append(getRadiusY());
        sb.append(", xAxisRotation=").append(getXAxisRotation());
        if (isLargeArcFlag()) {
            sb.append(", lartArcFlag");
        }
        if (isSweepFlag()) {
            sb.append(", sweepFlag");
        }
        return sb.append("]").toString();
    }
}
