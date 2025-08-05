package javafx.scene.effect;

import com.sun.javafx.effect.EffectDirtyBits;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.effect.Effect;

/* loaded from: jfxrt.jar:javafx/scene/effect/PerspectiveTransform.class */
public class PerspectiveTransform extends Effect {
    private ObjectProperty<Effect> input;
    private DoubleProperty ulx;
    private DoubleProperty uly;
    private DoubleProperty urx;
    private DoubleProperty ury;
    private DoubleProperty lrx;
    private DoubleProperty lry;
    private DoubleProperty llx;
    private DoubleProperty lly;
    private float[] devcoords;

    public PerspectiveTransform() {
        this.devcoords = new float[8];
    }

    public PerspectiveTransform(double ulx, double uly, double urx, double ury, double lrx, double lry, double llx, double lly) {
        this.devcoords = new float[8];
        setUlx(ulx);
        setUly(uly);
        setUrx(urx);
        setUry(ury);
        setLlx(llx);
        setLly(lly);
        setLrx(lrx);
        setLry(lry);
    }

    private void updateXform() {
        ((com.sun.scenario.effect.PerspectiveTransform) impl_getImpl()).setQuadMapping((float) getUlx(), (float) getUly(), (float) getUrx(), (float) getUry(), (float) getLrx(), (float) getLry(), (float) getLlx(), (float) getLly());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javafx.scene.effect.Effect
    public com.sun.scenario.effect.PerspectiveTransform impl_createImpl() {
        return new com.sun.scenario.effect.PerspectiveTransform();
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

    public final void setUlx(double value) {
        ulxProperty().set(value);
    }

    public final double getUlx() {
        if (this.ulx == null) {
            return 0.0d;
        }
        return this.ulx.get();
    }

    public final DoubleProperty ulxProperty() {
        if (this.ulx == null) {
            this.ulx = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "ulx";
                }
            };
        }
        return this.ulx;
    }

    public final void setUly(double value) {
        ulyProperty().set(value);
    }

    public final double getUly() {
        if (this.uly == null) {
            return 0.0d;
        }
        return this.uly.get();
    }

    public final DoubleProperty ulyProperty() {
        if (this.uly == null) {
            this.uly = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "uly";
                }
            };
        }
        return this.uly;
    }

    public final void setUrx(double value) {
        urxProperty().set(value);
    }

    public final double getUrx() {
        if (this.urx == null) {
            return 0.0d;
        }
        return this.urx.get();
    }

    public final DoubleProperty urxProperty() {
        if (this.urx == null) {
            this.urx = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "urx";
                }
            };
        }
        return this.urx;
    }

    public final void setUry(double value) {
        uryProperty().set(value);
    }

    public final double getUry() {
        if (this.ury == null) {
            return 0.0d;
        }
        return this.ury.get();
    }

    public final DoubleProperty uryProperty() {
        if (this.ury == null) {
            this.ury = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "ury";
                }
            };
        }
        return this.ury;
    }

    public final void setLrx(double value) {
        lrxProperty().set(value);
    }

    public final double getLrx() {
        if (this.lrx == null) {
            return 0.0d;
        }
        return this.lrx.get();
    }

    public final DoubleProperty lrxProperty() {
        if (this.lrx == null) {
            this.lrx = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "lrx";
                }
            };
        }
        return this.lrx;
    }

    public final void setLry(double value) {
        lryProperty().set(value);
    }

    public final double getLry() {
        if (this.lry == null) {
            return 0.0d;
        }
        return this.lry.get();
    }

    public final DoubleProperty lryProperty() {
        if (this.lry == null) {
            this.lry = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.6
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "lry";
                }
            };
        }
        return this.lry;
    }

    public final void setLlx(double value) {
        llxProperty().set(value);
    }

    public final double getLlx() {
        if (this.llx == null) {
            return 0.0d;
        }
        return this.llx.get();
    }

    public final DoubleProperty llxProperty() {
        if (this.llx == null) {
            this.llx = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.7
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "llx";
                }
            };
        }
        return this.llx;
    }

    public final void setLly(double value) {
        llyProperty().set(value);
    }

    public final double getLly() {
        if (this.lly == null) {
            return 0.0d;
        }
        return this.lly.get();
    }

    public final DoubleProperty llyProperty() {
        if (this.lly == null) {
            this.lly = new DoublePropertyBase() { // from class: javafx.scene.effect.PerspectiveTransform.8
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    PerspectiveTransform.this.markDirty(EffectDirtyBits.EFFECT_DIRTY);
                    PerspectiveTransform.this.effectBoundsChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PerspectiveTransform.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "lly";
                }
            };
        }
        return this.lly;
    }

    @Override // javafx.scene.effect.Effect
    void impl_update() {
        Effect localInput = getInput();
        if (localInput != null) {
            localInput.impl_sync();
        }
        ((com.sun.scenario.effect.PerspectiveTransform) impl_getImpl()).setInput(localInput == null ? null : localInput.impl_getImpl());
        updateXform();
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public BaseBounds impl_getBounds(BaseBounds bounds, BaseTransform tx, Node node, BoundsAccessor boundsAccessor) {
        setupDevCoords(tx);
        float f2 = this.devcoords[0];
        float maxx = f2;
        float minx = f2;
        float f3 = this.devcoords[1];
        float maxy = f3;
        float miny = f3;
        for (int i2 = 2; i2 < this.devcoords.length; i2 += 2) {
            if (minx > this.devcoords[i2]) {
                minx = this.devcoords[i2];
            } else if (maxx < this.devcoords[i2]) {
                maxx = this.devcoords[i2];
            }
            if (miny > this.devcoords[i2 + 1]) {
                miny = this.devcoords[i2 + 1];
            } else if (maxy < this.devcoords[i2 + 1]) {
                maxy = this.devcoords[i2 + 1];
            }
        }
        return new RectBounds(minx, miny, maxx, maxy);
    }

    private void setupDevCoords(BaseTransform transform) {
        this.devcoords[0] = (float) getUlx();
        this.devcoords[1] = (float) getUly();
        this.devcoords[2] = (float) getUrx();
        this.devcoords[3] = (float) getUry();
        this.devcoords[4] = (float) getLrx();
        this.devcoords[5] = (float) getLry();
        this.devcoords[6] = (float) getLlx();
        this.devcoords[7] = (float) getLly();
        transform.transform(this.devcoords, 0, this.devcoords, 0, 4);
    }

    @Override // javafx.scene.effect.Effect
    @Deprecated
    public Effect impl_copy() {
        return new PerspectiveTransform(getUlx(), getUly(), getUrx(), getUry(), getLrx(), getLry(), getLlx(), getLly());
    }
}
