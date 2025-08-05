package com.sun.scenario.effect;

import com.sun.scenario.effect.impl.Renderer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/FloatMap.class */
public class FloatMap {
    private final int width;
    private final int height;
    private final FloatBuffer buf;
    private boolean cacheValid;
    private Map<FilterContext, Entry> cache;

    public FloatMap(int width, int height) {
        if (width <= 0 || width > 4096 || height <= 0 || height > 4096) {
            throw new IllegalArgumentException("Width and height must be in the range [1, 4096]");
        }
        this.width = width;
        this.height = height;
        int size = width * height * 4;
        this.buf = FloatBuffer.wrap(new float[size]);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float[] getData() {
        return this.buf.array();
    }

    public FloatBuffer getBuffer() {
        return this.buf;
    }

    public float getSample(int x2, int y2, int band) {
        return this.buf.get(((x2 + (y2 * this.width)) * 4) + band);
    }

    public void setSample(int x2, int y2, int band, float sample) {
        this.buf.put(((x2 + (y2 * this.width)) * 4) + band, sample);
        this.cacheValid = false;
    }

    public void setSamples(int x2, int y2, float s0) {
        int index = (x2 + (y2 * this.width)) * 4;
        this.buf.put(index + 0, s0);
        this.cacheValid = false;
    }

    public void setSamples(int x2, int y2, float s0, float s1) {
        int index = (x2 + (y2 * this.width)) * 4;
        this.buf.put(index + 0, s0);
        this.buf.put(index + 1, s1);
        this.cacheValid = false;
    }

    public void setSamples(int x2, int y2, float s0, float s1, float s2) {
        int index = (x2 + (y2 * this.width)) * 4;
        this.buf.put(index + 0, s0);
        this.buf.put(index + 1, s1);
        this.buf.put(index + 2, s2);
        this.cacheValid = false;
    }

    public void setSamples(int x2, int y2, float s0, float s1, float s2, float s3) {
        int index = (x2 + (y2 * this.width)) * 4;
        this.buf.put(index + 0, s0);
        this.buf.put(index + 1, s1);
        this.buf.put(index + 2, s2);
        this.buf.put(index + 3, s3);
        this.cacheValid = false;
    }

    public void put(float[] floatBuf) {
        this.buf.rewind();
        this.buf.put(floatBuf);
        this.buf.rewind();
        this.cacheValid = false;
    }

    public LockableResource getAccelData(FilterContext fctx) {
        if (this.cache == null) {
            this.cache = new HashMap();
        } else if (!this.cacheValid) {
            Iterator<Entry> it = this.cache.values().iterator();
            while (it.hasNext()) {
                it.next().valid = false;
            }
            this.cacheValid = true;
        }
        Renderer renderer = Renderer.getRenderer(fctx);
        Entry entry = this.cache.get(fctx);
        if (entry != null) {
            entry.texture.lock();
            if (entry.texture.isLost()) {
                entry.texture.unlock();
                this.cache.remove(fctx);
                entry = null;
            }
        }
        if (entry == null) {
            LockableResource texture = renderer.createFloatTexture(this.width, this.height);
            entry = new Entry(texture);
            this.cache.put(fctx, entry);
        }
        if (!entry.valid) {
            renderer.updateFloatTexture(entry.texture, this);
            entry.valid = true;
        }
        return entry.texture;
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/FloatMap$Entry.class */
    private static class Entry {
        LockableResource texture;
        boolean valid;

        Entry(LockableResource texture) {
            this.texture = texture;
        }
    }
}
