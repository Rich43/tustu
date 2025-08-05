package javafx.scene.effect;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.scenario.effect.light.DistantLight;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/effect/Light.class */
public abstract class Light {
    private com.sun.scenario.effect.light.Light peer;
    private ObjectProperty<Color> color;
    private BooleanProperty effectDirty;

    abstract com.sun.scenario.effect.light.Light impl_createImpl();

    protected Light() {
        impl_markDirty();
    }

    com.sun.scenario.effect.light.Light impl_getImpl() {
        if (this.peer == null) {
            this.peer = impl_createImpl();
        }
        return this.peer;
    }

    public final void setColor(Color value) {
        colorProperty().set(value);
    }

    public final Color getColor() {
        return this.color == null ? Color.WHITE : this.color.get();
    }

    public final ObjectProperty<Color> colorProperty() {
        if (this.color == null) {
            this.color = new ObjectPropertyBase<Color>(Color.WHITE) { // from class: javafx.scene.effect.Light.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Light.this.impl_markDirty();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Light.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "color";
                }
            };
        }
        return this.color;
    }

    void impl_sync() {
        if (impl_isEffectDirty()) {
            impl_update();
            impl_clearDirty();
        }
    }

    private Color getColorInternal() {
        Color c2 = getColor();
        return c2 == null ? Color.WHITE : c2;
    }

    void impl_update() {
        impl_getImpl().setColor(Toolkit.getToolkit().toColor4f(getColorInternal()));
    }

    private void setEffectDirty(boolean value) {
        effectDirtyProperty().set(value);
    }

    final BooleanProperty effectDirtyProperty() {
        if (this.effectDirty == null) {
            this.effectDirty = new SimpleBooleanProperty(this, "effectDirty");
        }
        return this.effectDirty;
    }

    boolean impl_isEffectDirty() {
        if (this.effectDirty == null) {
            return false;
        }
        return this.effectDirty.get();
    }

    final void impl_markDirty() {
        setEffectDirty(true);
    }

    final void impl_clearDirty() {
        setEffectDirty(false);
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/Light$Distant.class */
    public static class Distant extends Light {
        private DoubleProperty azimuth;
        private DoubleProperty elevation;

        public Distant() {
        }

        public Distant(double azimuth, double elevation, Color color) {
            setAzimuth(azimuth);
            setElevation(elevation);
            setColor(color);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // javafx.scene.effect.Light
        public DistantLight impl_createImpl() {
            return new DistantLight();
        }

        public final void setAzimuth(double value) {
            azimuthProperty().set(value);
        }

        public final double getAzimuth() {
            if (this.azimuth == null) {
                return 45.0d;
            }
            return this.azimuth.get();
        }

        public final DoubleProperty azimuthProperty() {
            if (this.azimuth == null) {
                this.azimuth = new DoublePropertyBase(45.0d) { // from class: javafx.scene.effect.Light.Distant.1
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Distant.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Distant.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "azimuth";
                    }
                };
            }
            return this.azimuth;
        }

        public final void setElevation(double value) {
            elevationProperty().set(value);
        }

        public final double getElevation() {
            if (this.elevation == null) {
                return 45.0d;
            }
            return this.elevation.get();
        }

        public final DoubleProperty elevationProperty() {
            if (this.elevation == null) {
                this.elevation = new DoublePropertyBase(45.0d) { // from class: javafx.scene.effect.Light.Distant.2
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Distant.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Distant.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "elevation";
                    }
                };
            }
            return this.elevation;
        }

        @Override // javafx.scene.effect.Light
        void impl_update() {
            super.impl_update();
            DistantLight peer = (DistantLight) impl_getImpl();
            peer.setAzimuth((float) getAzimuth());
            peer.setElevation((float) getElevation());
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/Light$Point.class */
    public static class Point extends Light {

        /* renamed from: x, reason: collision with root package name */
        private DoubleProperty f12657x;

        /* renamed from: y, reason: collision with root package name */
        private DoubleProperty f12658y;

        /* renamed from: z, reason: collision with root package name */
        private DoubleProperty f12659z;

        public Point() {
        }

        public Point(double x2, double y2, double z2, Color color) {
            setX(x2);
            setY(y2);
            setZ(z2);
            setColor(color);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // javafx.scene.effect.Light
        @Deprecated
        public PointLight impl_createImpl() {
            return new PointLight();
        }

        public final void setX(double value) {
            xProperty().set(value);
        }

        public final double getX() {
            if (this.f12657x == null) {
                return 0.0d;
            }
            return this.f12657x.get();
        }

        public final DoubleProperty xProperty() {
            if (this.f12657x == null) {
                this.f12657x = new DoublePropertyBase() { // from class: javafx.scene.effect.Light.Point.1
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Point.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Point.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return LanguageTag.PRIVATEUSE;
                    }
                };
            }
            return this.f12657x;
        }

        public final void setY(double value) {
            yProperty().set(value);
        }

        public final double getY() {
            if (this.f12658y == null) {
                return 0.0d;
            }
            return this.f12658y.get();
        }

        public final DoubleProperty yProperty() {
            if (this.f12658y == null) {
                this.f12658y = new DoublePropertyBase() { // from class: javafx.scene.effect.Light.Point.2
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Point.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Point.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return PdfOps.y_TOKEN;
                    }
                };
            }
            return this.f12658y;
        }

        public final void setZ(double value) {
            zProperty().set(value);
        }

        public final double getZ() {
            if (this.f12659z == null) {
                return 0.0d;
            }
            return this.f12659z.get();
        }

        public final DoubleProperty zProperty() {
            if (this.f12659z == null) {
                this.f12659z = new DoublePropertyBase() { // from class: javafx.scene.effect.Light.Point.3
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Point.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Point.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "z";
                    }
                };
            }
            return this.f12659z;
        }

        @Override // javafx.scene.effect.Light
        void impl_update() {
            super.impl_update();
            PointLight peer = (PointLight) impl_getImpl();
            peer.setX((float) getX());
            peer.setY((float) getY());
            peer.setZ((float) getZ());
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/Light$Spot.class */
    public static class Spot extends Point {
        private DoubleProperty pointsAtX;
        private DoubleProperty pointsAtY;
        private DoubleProperty pointsAtZ;
        private DoubleProperty specularExponent;

        public Spot() {
        }

        public Spot(double x2, double y2, double z2, double specularExponent, Color color) {
            setX(x2);
            setY(y2);
            setZ(z2);
            setSpecularExponent(specularExponent);
            setColor(color);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // javafx.scene.effect.Light.Point, javafx.scene.effect.Light
        @Deprecated
        public SpotLight impl_createImpl() {
            return new SpotLight();
        }

        public final void setPointsAtX(double value) {
            pointsAtXProperty().set(value);
        }

        public final double getPointsAtX() {
            if (this.pointsAtX == null) {
                return 0.0d;
            }
            return this.pointsAtX.get();
        }

        public final DoubleProperty pointsAtXProperty() {
            if (this.pointsAtX == null) {
                this.pointsAtX = new DoublePropertyBase() { // from class: javafx.scene.effect.Light.Spot.1
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Spot.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Spot.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "pointsAtX";
                    }
                };
            }
            return this.pointsAtX;
        }

        public final void setPointsAtY(double value) {
            pointsAtYProperty().set(value);
        }

        public final double getPointsAtY() {
            if (this.pointsAtY == null) {
                return 0.0d;
            }
            return this.pointsAtY.get();
        }

        public final DoubleProperty pointsAtYProperty() {
            if (this.pointsAtY == null) {
                this.pointsAtY = new DoublePropertyBase() { // from class: javafx.scene.effect.Light.Spot.2
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Spot.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Spot.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "pointsAtY";
                    }
                };
            }
            return this.pointsAtY;
        }

        public final void setPointsAtZ(double value) {
            pointsAtZProperty().set(value);
        }

        public final double getPointsAtZ() {
            if (this.pointsAtZ == null) {
                return 0.0d;
            }
            return this.pointsAtZ.get();
        }

        public final DoubleProperty pointsAtZProperty() {
            if (this.pointsAtZ == null) {
                this.pointsAtZ = new DoublePropertyBase() { // from class: javafx.scene.effect.Light.Spot.3
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Spot.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Spot.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "pointsAtZ";
                    }
                };
            }
            return this.pointsAtZ;
        }

        public final void setSpecularExponent(double value) {
            specularExponentProperty().set(value);
        }

        public final double getSpecularExponent() {
            if (this.specularExponent == null) {
                return 1.0d;
            }
            return this.specularExponent.get();
        }

        public final DoubleProperty specularExponentProperty() {
            if (this.specularExponent == null) {
                this.specularExponent = new DoublePropertyBase(1.0d) { // from class: javafx.scene.effect.Light.Spot.4
                    @Override // javafx.beans.property.DoublePropertyBase
                    public void invalidated() {
                        Spot.this.impl_markDirty();
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public Object getBean() {
                        return Spot.this;
                    }

                    @Override // javafx.beans.property.ReadOnlyProperty
                    public String getName() {
                        return "specularExponent";
                    }
                };
            }
            return this.specularExponent;
        }

        @Override // javafx.scene.effect.Light.Point, javafx.scene.effect.Light
        void impl_update() {
            super.impl_update();
            SpotLight peer = (SpotLight) impl_getImpl();
            peer.setPointsAtX((float) getPointsAtX());
            peer.setPointsAtY((float) getPointsAtY());
            peer.setPointsAtZ((float) getPointsAtZ());
            peer.setSpecularExponent((float) Utils.clamp(0.0d, getSpecularExponent(), 4.0d));
        }
    }
}
