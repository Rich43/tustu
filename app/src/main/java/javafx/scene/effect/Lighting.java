package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.util.Utils;
import com.sun.scenario.effect.PhongLighting;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light;

/* loaded from: jfxrt.jar:javafx/scene/effect/Lighting.class */
public class Lighting extends Effect {
    private final Light defaultLight = new Light.Distant();
    private ObjectProperty<Light> light = new ObjectPropertyBase<Light>(new Light.Distant()) { // from class: javafx.scene.effect.Lighting.1
        @Override // javafx.beans.property.ObjectPropertyBase
        public void invalidated() {
            Lighting.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
            Lighting.this.effectBoundsChanged();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Lighting.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "light";
        }
    };
    private final LightChangeListener lightChangeListener = new LightChangeListener();
    private ObjectProperty<Effect> bumpInput;
    private ObjectProperty<Effect> contentInput;
    private DoubleProperty diffuseConstant;
    private DoubleProperty specularConstant;
    private DoubleProperty specularExponent;
    private DoubleProperty surfaceScale;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public PhongLighting impl_createImpl() {
        return new PhongLighting(getLightInternal().impl_getImpl());
    }

    public Lighting() {
        Shadow shadow = new Shadow();
        shadow.setRadius(10.0d);
        setBumpInput(shadow);
    }

    public Lighting(Light light) {
        Shadow shadow = new Shadow();
        shadow.setRadius(10.0d);
        setBumpInput(shadow);
        setLight(light);
    }

    public final void setLight(Light value) {
        lightProperty().set(value);
    }

    public final Light getLight() {
        return this.light.get();
    }

    public final ObjectProperty<Light> lightProperty() {
        return this.light;
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        Lighting lighting = new Lighting(getLight());
        lighting.setBumpInput(getBumpInput());
        lighting.setContentInput(getContentInput());
        lighting.setDiffuseConstant(getDiffuseConstant());
        lighting.setSpecularConstant(getSpecularConstant());
        lighting.setSpecularExponent(getSpecularExponent());
        lighting.setSurfaceScale(getSurfaceScale());
        return lighting;
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/Lighting$LightChangeListener.class */
    private class LightChangeListener extends EffectChangeListener {
        Light light;

        private LightChangeListener() {
        }

        public void register(Light value) {
            this.light = value;
            super.register(this.light == null ? null : this.light.effectDirtyProperty());
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            if (this.light.impl_isEffectDirty()) {
                Lighting.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                Lighting.this.effectBoundsChanged();
            }
        }
    }

    public final void setBumpInput(Effect value) {
        bumpInputProperty().set(value);
    }

    public final Effect getBumpInput() {
        if (this.bumpInput == null) {
            return null;
        }
        return this.bumpInput.get();
    }

    public final ObjectProperty<Effect> bumpInputProperty() {
        if (this.bumpInput == null) {
            this.bumpInput = new Effect.EffectInputProperty("bumpInput");
        }
        return this.bumpInput;
    }

    public final void setContentInput(Effect value) {
        contentInputProperty().set(value);
    }

    public final Effect getContentInput() {
        if (this.contentInput == null) {
            return null;
        }
        return this.contentInput.get();
    }

    public final ObjectProperty<Effect> contentInputProperty() {
        if (this.contentInput == null) {
            this.contentInput = new Effect.EffectInputProperty("contentInput");
        }
        return this.contentInput;
    }

    @Override // javafx.scene.effect.Effect
    boolean impl_checkChainContains(Effect e2) {
        Effect localBumpInput = getBumpInput();
        Effect localContentInput = getContentInput();
        if (localContentInput == e2 || localBumpInput == e2) {
            return true;
        }
        if (localContentInput != null && localContentInput.impl_checkChainContains(e2)) {
            return true;
        }
        if (localBumpInput != null && localBumpInput.impl_checkChainContains(e2)) {
            return true;
        }
        return false;
    }

    public final void setDiffuseConstant(double value) {
        diffuseConstantProperty().set(value);
    }

    public final double getDiffuseConstant() {
        if (this.diffuseConstant == null) {
            return 1.0d;
        }
        return this.diffuseConstant.get();
    }

    public final DoubleProperty diffuseConstantProperty() {
        if (this.diffuseConstant == null) {
            this.diffuseConstant = new DoublePropertyBase(1.0d) { // from class: javafx.scene.effect.Lighting.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Lighting.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Lighting.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "diffuseConstant";
                }
            };
        }
        return this.diffuseConstant;
    }

    public final void setSpecularConstant(double value) {
        specularConstantProperty().set(value);
    }

    public final double getSpecularConstant() {
        if (this.specularConstant == null) {
            return 0.3d;
        }
        return this.specularConstant.get();
    }

    public final DoubleProperty specularConstantProperty() {
        if (this.specularConstant == null) {
            this.specularConstant = new DoublePropertyBase(0.3d) { // from class: javafx.scene.effect.Lighting.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Lighting.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Lighting.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "specularConstant";
                }
            };
        }
        return this.specularConstant;
    }

    public final void setSpecularExponent(double value) {
        specularExponentProperty().set(value);
    }

    public final double getSpecularExponent() {
        if (this.specularExponent == null) {
            return 20.0d;
        }
        return this.specularExponent.get();
    }

    public final DoubleProperty specularExponentProperty() {
        if (this.specularExponent == null) {
            this.specularExponent = new DoublePropertyBase(20.0d) { // from class: javafx.scene.effect.Lighting.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Lighting.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Lighting.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "specularExponent";
                }
            };
        }
        return this.specularExponent;
    }

    public final void setSurfaceScale(double value) {
        surfaceScaleProperty().set(value);
    }

    public final double getSurfaceScale() {
        if (this.surfaceScale == null) {
            return 1.5d;
        }
        return this.surfaceScale.get();
    }

    public final DoubleProperty surfaceScaleProperty() {
        if (this.surfaceScale == null) {
            this.surfaceScale = new DoublePropertyBase(1.5d) { // from class: javafx.scene.effect.Lighting.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    Lighting.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Lighting.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "surfaceScale";
                }
            };
        }
        return this.surfaceScale;
    }

    private Light getLightInternal() {
        Light localLight = getLight();
        return localLight == null ? this.defaultLight : localLight;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localBumpInput = getBumpInput();
        if (localBumpInput != null) {
            localBumpInput.impl_sync();
        }
        Effect localContentInput = getContentInput();
        if (localContentInput != null) {
            localContentInput.impl_sync();
        }
        PhongLighting peer = (PhongLighting) impl_getImpl();
        peer.setBumpInput(localBumpInput == null ? null : localBumpInput.impl_getImpl());
        peer.setContentInput(localContentInput == null ? null : localContentInput.impl_getImpl());
        peer.setDiffuseConstant((float) Utils.clamp(0.0d, getDiffuseConstant(), 2.0d));
        peer.setSpecularConstant((float) Utils.clamp(0.0d, getSpecularConstant(), 2.0d));
        peer.setSpecularExponent((float) Utils.clamp(0.0d, getSpecularExponent(), 40.0d));
        peer.setSurfaceScale((float) Utils.clamp(0.0d, getSurfaceScale(), 10.0d));
        this.lightChangeListener.register(getLight());
        getLightInternal().impl_sync();
        peer.setLight(getLightInternal().impl_getImpl());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        return getInputBounds(bounds, tx, node, boundsAccessor, getContentInput());
    }
}
