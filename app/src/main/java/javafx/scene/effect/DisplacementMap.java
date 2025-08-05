package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/* loaded from: jfxrt.jar:javafx/scene/effect/DisplacementMap.class */
public class DisplacementMap extends Effect {
    private ObjectProperty<Effect> input;
    private ObjectProperty<FloatMap> mapData;
    private DoubleProperty scaleX;
    private DoubleProperty scaleY;
    private DoubleProperty offsetX;
    private DoubleProperty offsetY;
    private BooleanProperty wrap;
    private final FloatMap defaultMap = new FloatMap(1, 1);
    private final MapDataChangeListener mapDataChangeListener = new MapDataChangeListener();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.DisplacementMap impl_createImpl() {
        return new com.sun.scenario.effect.DisplacementMap(new com.sun.scenario.effect.FloatMap(1, 1), com.sun.scenario.effect.Effect.DefaultInput);
    }

    public DisplacementMap() {
        setMapData(new FloatMap(1, 1));
    }

    public DisplacementMap(FloatMap mapData) {
        setMapData(mapData);
    }

    public DisplacementMap(FloatMap mapData, double offsetX, double offsetY, double scaleX, double scaleY) {
        setMapData(mapData);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    public final void setInput(Effect value) {
        inputProperty().set(value);
    }

    public final Effect getInput() {
        if (this.input == null) {
            return null;
        }
        return this.input.get();
    }

    public final ObjectProperty<Effect> inputProperty() {
        if (this.input == null) {
            this.input = new Effect.EffectInputProperty("input");
        }
        return this.input;
    }

    @Override // javafx.scene.effect.Effect
    boolean impl_checkChainContains(Effect e2) {
        Effect localInput = getInput();
        if (localInput == null) {
            return false;
        }
        if (localInput == e2) {
            return true;
        }
        return localInput.impl_checkChainContains(e2);
    }

    public final void setMapData(FloatMap value) {
        mapDataProperty().set(value);
    }

    public final FloatMap getMapData() {
        if (this.mapData == null) {
            return null;
        }
        return this.mapData.get();
    }

    public final ObjectProperty<FloatMap> mapDataProperty() {
        if (this.mapData == null) {
            this.mapData = new ObjectPropertyBase<FloatMap>() { // from class: javafx.scene.effect.DisplacementMap.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    DisplacementMap.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mapData";
                }
            };
        }
        return this.mapData;
    }

    /* loaded from: jfxrt.jar:javafx/scene/effect/DisplacementMap$MapDataChangeListener.class */
    private class MapDataChangeListener extends EffectChangeListener {
        FloatMap mapData;

        private MapDataChangeListener() {
        }

        public void register(FloatMap value) {
            this.mapData = value;
            super.register(this.mapData == null ? null : this.mapData.effectDirtyProperty());
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable valueModel) {
            if (this.mapData.impl_isEffectDirty()) {
                DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                DisplacementMap.this.effectBoundsChanged();
            }
        }
    }

    public final void setScaleX(double value) {
        scaleXProperty().set(value);
    }

    public final double getScaleX() {
        if (this.scaleX == null) {
            return 1.0d;
        }
        return this.scaleX.get();
    }

    public final DoubleProperty scaleXProperty() {
        if (this.scaleX == null) {
            this.scaleX = new DoublePropertyBase(1.0d) { // from class: javafx.scene.effect.DisplacementMap.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "scaleX";
                }
            };
        }
        return this.scaleX;
    }

    public final void setScaleY(double value) {
        scaleYProperty().set(value);
    }

    public final double getScaleY() {
        if (this.scaleY == null) {
            return 1.0d;
        }
        return this.scaleY.get();
    }

    public final DoubleProperty scaleYProperty() {
        if (this.scaleY == null) {
            this.scaleY = new DoublePropertyBase(1.0d) { // from class: javafx.scene.effect.DisplacementMap.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "scaleY";
                }
            };
        }
        return this.scaleY;
    }

    public final void setOffsetX(double value) {
        offsetXProperty().set(value);
    }

    public final double getOffsetX() {
        if (this.offsetX == null) {
            return 0.0d;
        }
        return this.offsetX.get();
    }

    public final DoubleProperty offsetXProperty() {
        if (this.offsetX == null) {
            this.offsetX = new DoublePropertyBase() { // from class: javafx.scene.effect.DisplacementMap.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "offsetX";
                }
            };
        }
        return this.offsetX;
    }

    public final void setOffsetY(double value) {
        offsetYProperty().set(value);
    }

    public final double getOffsetY() {
        if (this.offsetY == null) {
            return 0.0d;
        }
        return this.offsetY.get();
    }

    public final DoubleProperty offsetYProperty() {
        if (this.offsetY == null) {
            this.offsetY = new DoublePropertyBase() { // from class: javafx.scene.effect.DisplacementMap.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "offsetY";
                }
            };
        }
        return this.offsetY;
    }

    public final void setWrap(boolean value) {
        wrapProperty().set(value);
    }

    public final boolean isWrap() {
        if (this.wrap == null) {
            return false;
        }
        return this.wrap.get();
    }

    public final BooleanProperty wrapProperty() {
        if (this.wrap == null) {
            this.wrap = new BooleanPropertyBase() { // from class: javafx.scene.effect.DisplacementMap.6
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    DisplacementMap.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return DisplacementMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "wrap";
                }
            };
        }
        return this.wrap;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        com.sun.scenario.effect.DisplacementMap peer = (com.sun.scenario.effect.DisplacementMap) impl_getImpl();
        peer.setContentInput(localInput == null ? null : localInput.impl_getImpl());
        FloatMap localMapData = getMapData();
        this.mapDataChangeListener.register(localMapData);
        if (localMapData != null) {
            localMapData.impl_sync();
            peer.setMapData(localMapData.getImpl());
        } else {
            this.defaultMap.impl_sync();
            peer.setMapData(this.defaultMap.getImpl());
        }
        peer.setScaleX((float) getScaleX());
        peer.setScaleY((float) getScaleY());
        peer.setOffsetX((float) getOffsetX());
        peer.setOffsetY((float) getOffsetY());
        peer.setWrap(isWrap());
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        return transformBounds(tx, getInputBounds(bounds, BaseTransform.IDENTITY_TRANSFORM, node, boundsAccessor, getInput()));
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        DisplacementMap dm = new DisplacementMap(getMapData().impl_copy(), getOffsetX(), getOffsetY(), getScaleX(), getScaleY());
        dm.setInput(getInput());
        return dm;
    }
}
