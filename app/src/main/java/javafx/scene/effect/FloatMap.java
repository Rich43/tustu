package javafx.scene.effect;

import com.sun.javafx.util.Utils;
import com.sun.media.jfxmedia.MetadataParser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;

/* loaded from: jfxrt.jar:javafx/scene/effect/FloatMap.class */
public class FloatMap {
    private com.sun.scenario.effect.FloatMap map;
    private float[] buf;
    private boolean mapBufferDirty = true;
    private BooleanProperty effectDirty;
    private IntegerProperty width;
    private IntegerProperty height;

    com.sun.scenario.effect.FloatMap getImpl() {
        return this.map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBuffer() {
        if (getWidth() > 0 && getHeight() > 0) {
            int w2 = Utils.clampMax(getWidth(), 4096);
            int h2 = Utils.clampMax(getHeight(), 4096);
            int size = w2 * h2 * 4;
            this.buf = new float[size];
            this.mapBufferDirty = true;
        }
    }

    private void impl_update() {
        if (this.mapBufferDirty) {
            this.map = new com.sun.scenario.effect.FloatMap(Utils.clamp(1, getWidth(), 4096), Utils.clamp(1, getHeight(), 4096));
            this.mapBufferDirty = false;
        }
        this.map.put(this.buf);
    }

    void impl_sync() {
        if (impl_isEffectDirty()) {
            impl_update();
            impl_clearDirty();
        }
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

    @Deprecated
    boolean impl_isEffectDirty() {
        if (this.effectDirty == null) {
            return false;
        }
        return this.effectDirty.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void impl_markDirty() {
        setEffectDirty(true);
    }

    private void impl_clearDirty() {
        setEffectDirty(false);
    }

    public FloatMap() {
        updateBuffer();
        impl_markDirty();
    }

    public FloatMap(int width, int height) {
        setWidth(width);
        setHeight(height);
        updateBuffer();
        impl_markDirty();
    }

    public final void setWidth(int value) {
        widthProperty().set(value);
    }

    public final int getWidth() {
        if (this.width == null) {
            return 1;
        }
        return this.width.get();
    }

    public final IntegerProperty widthProperty() {
        if (this.width == null) {
            this.width = new IntegerPropertyBase(1) { // from class: javafx.scene.effect.FloatMap.1
                @Override // javafx.beans.property.IntegerPropertyBase
                public void invalidated() {
                    FloatMap.this.updateBuffer();
                    FloatMap.this.impl_markDirty();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FloatMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.WIDTH_TAG_NAME;
                }
            };
        }
        return this.width;
    }

    public final void setHeight(int value) {
        heightProperty().set(value);
    }

    public final int getHeight() {
        if (this.height == null) {
            return 1;
        }
        return this.height.get();
    }

    public final IntegerProperty heightProperty() {
        if (this.height == null) {
            this.height = new IntegerPropertyBase(1) { // from class: javafx.scene.effect.FloatMap.2
                @Override // javafx.beans.property.IntegerPropertyBase
                public void invalidated() {
                    FloatMap.this.updateBuffer();
                    FloatMap.this.impl_markDirty();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FloatMap.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return MetadataParser.HEIGHT_TAG_NAME;
                }
            };
        }
        return this.height;
    }

    public void setSample(int x2, int y2, int band, float s2) {
        this.buf[((x2 + (y2 * getWidth())) * 4) + band] = s2;
        impl_markDirty();
    }

    public void setSamples(int x2, int y2, float s0) {
        int index = (x2 + (y2 * getWidth())) * 4;
        this.buf[index + 0] = s0;
        impl_markDirty();
    }

    public void setSamples(int x2, int y2, float s0, float s1) {
        int index = (x2 + (y2 * getWidth())) * 4;
        this.buf[index + 0] = s0;
        this.buf[index + 1] = s1;
        impl_markDirty();
    }

    public void setSamples(int x2, int y2, float s0, float s1, float s2) {
        int index = (x2 + (y2 * getWidth())) * 4;
        this.buf[index + 0] = s0;
        this.buf[index + 1] = s1;
        this.buf[index + 2] = s2;
        impl_markDirty();
    }

    public void setSamples(int x2, int y2, float s0, float s1, float s2, float s3) {
        int index = (x2 + (y2 * getWidth())) * 4;
        this.buf[index + 0] = s0;
        this.buf[index + 1] = s1;
        this.buf[index + 2] = s2;
        this.buf[index + 3] = s3;
        impl_markDirty();
    }

    @Deprecated
    public FloatMap impl_copy() {
        FloatMap dest = new FloatMap(getWidth(), getHeight());
        System.arraycopy(this.buf, 0, dest.buf, 0, this.buf.length);
        return dest;
    }
}
