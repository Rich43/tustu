package java.awt;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.awt.SunHints;

/* loaded from: rt.jar:java/awt/RenderingHints.class */
public class RenderingHints implements Map<Object, Object>, Cloneable {
    HashMap<Object, Object> hintmap = new HashMap<>(7);
    public static final Key KEY_ANTIALIASING = SunHints.KEY_ANTIALIASING;
    public static final Object VALUE_ANTIALIAS_ON = SunHints.VALUE_ANTIALIAS_ON;
    public static final Object VALUE_ANTIALIAS_OFF = SunHints.VALUE_ANTIALIAS_OFF;
    public static final Object VALUE_ANTIALIAS_DEFAULT = SunHints.VALUE_ANTIALIAS_DEFAULT;
    public static final Key KEY_RENDERING = SunHints.KEY_RENDERING;
    public static final Object VALUE_RENDER_SPEED = SunHints.VALUE_RENDER_SPEED;
    public static final Object VALUE_RENDER_QUALITY = SunHints.VALUE_RENDER_QUALITY;
    public static final Object VALUE_RENDER_DEFAULT = SunHints.VALUE_RENDER_DEFAULT;
    public static final Key KEY_DITHERING = SunHints.KEY_DITHERING;
    public static final Object VALUE_DITHER_DISABLE = SunHints.VALUE_DITHER_DISABLE;
    public static final Object VALUE_DITHER_ENABLE = SunHints.VALUE_DITHER_ENABLE;
    public static final Object VALUE_DITHER_DEFAULT = SunHints.VALUE_DITHER_DEFAULT;
    public static final Key KEY_TEXT_ANTIALIASING = SunHints.KEY_TEXT_ANTIALIASING;
    public static final Object VALUE_TEXT_ANTIALIAS_ON = SunHints.VALUE_TEXT_ANTIALIAS_ON;
    public static final Object VALUE_TEXT_ANTIALIAS_OFF = SunHints.VALUE_TEXT_ANTIALIAS_OFF;
    public static final Object VALUE_TEXT_ANTIALIAS_DEFAULT = SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
    public static final Object VALUE_TEXT_ANTIALIAS_GASP = SunHints.VALUE_TEXT_ANTIALIAS_GASP;
    public static final Object VALUE_TEXT_ANTIALIAS_LCD_HRGB = SunHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
    public static final Object VALUE_TEXT_ANTIALIAS_LCD_HBGR = SunHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
    public static final Object VALUE_TEXT_ANTIALIAS_LCD_VRGB = SunHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
    public static final Object VALUE_TEXT_ANTIALIAS_LCD_VBGR = SunHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
    public static final Key KEY_TEXT_LCD_CONTRAST = SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST;
    public static final Key KEY_FRACTIONALMETRICS = SunHints.KEY_FRACTIONALMETRICS;
    public static final Object VALUE_FRACTIONALMETRICS_OFF = SunHints.VALUE_FRACTIONALMETRICS_OFF;
    public static final Object VALUE_FRACTIONALMETRICS_ON = SunHints.VALUE_FRACTIONALMETRICS_ON;
    public static final Object VALUE_FRACTIONALMETRICS_DEFAULT = SunHints.VALUE_FRACTIONALMETRICS_DEFAULT;
    public static final Key KEY_INTERPOLATION = SunHints.KEY_INTERPOLATION;
    public static final Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR = SunHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    public static final Object VALUE_INTERPOLATION_BILINEAR = SunHints.VALUE_INTERPOLATION_BILINEAR;
    public static final Object VALUE_INTERPOLATION_BICUBIC = SunHints.VALUE_INTERPOLATION_BICUBIC;
    public static final Key KEY_ALPHA_INTERPOLATION = SunHints.KEY_ALPHA_INTERPOLATION;
    public static final Object VALUE_ALPHA_INTERPOLATION_SPEED = SunHints.VALUE_ALPHA_INTERPOLATION_SPEED;
    public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = SunHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
    public static final Object VALUE_ALPHA_INTERPOLATION_DEFAULT = SunHints.VALUE_ALPHA_INTERPOLATION_DEFAULT;
    public static final Key KEY_COLOR_RENDERING = SunHints.KEY_COLOR_RENDERING;
    public static final Object VALUE_COLOR_RENDER_SPEED = SunHints.VALUE_COLOR_RENDER_SPEED;
    public static final Object VALUE_COLOR_RENDER_QUALITY = SunHints.VALUE_COLOR_RENDER_QUALITY;
    public static final Object VALUE_COLOR_RENDER_DEFAULT = SunHints.VALUE_COLOR_RENDER_DEFAULT;
    public static final Key KEY_STROKE_CONTROL = SunHints.KEY_STROKE_CONTROL;
    public static final Object VALUE_STROKE_DEFAULT = SunHints.VALUE_STROKE_DEFAULT;
    public static final Object VALUE_STROKE_NORMALIZE = SunHints.VALUE_STROKE_NORMALIZE;
    public static final Object VALUE_STROKE_PURE = SunHints.VALUE_STROKE_PURE;

    /* loaded from: rt.jar:java/awt/RenderingHints$Key.class */
    public static abstract class Key {
        private static HashMap<Object, Object> identitymap = new HashMap<>(17);
        private int privatekey;

        public abstract boolean isCompatibleValue(Object obj);

        private String getIdentity() {
            return getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(getClass())) + CallSiteDescriptor.TOKEN_DELIMITER + Integer.toHexString(this.privatekey);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static synchronized void recordIdentity(Key key) {
            Key key2;
            String identity = key.getIdentity();
            Object obj = identitymap.get(identity);
            if (obj != null && (key2 = (Key) ((WeakReference) obj).get()) != null && key2.getClass() == key.getClass()) {
                throw new IllegalArgumentException(((Object) identity) + " already registered");
            }
            identitymap.put(identity, new WeakReference(key));
        }

        protected Key(int i2) {
            this.privatekey = i2;
            recordIdentity(this);
        }

        protected final int intKey() {
            return this.privatekey;
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final boolean equals(Object obj) {
            return this == obj;
        }
    }

    public RenderingHints(Map<Key, ?> map) {
        if (map != null) {
            this.hintmap.putAll(map);
        }
    }

    public RenderingHints(Key key, Object obj) {
        this.hintmap.put(key, obj);
    }

    @Override // java.util.Map
    public int size() {
        return this.hintmap.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.hintmap.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.hintmap.containsKey((Key) obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.hintmap.containsValue(obj);
    }

    @Override // java.util.Map
    public Object get(Object obj) {
        return this.hintmap.get((Key) obj);
    }

    @Override // java.util.Map
    public Object put(Object obj, Object obj2) {
        if (!((Key) obj).isCompatibleValue(obj2)) {
            throw new IllegalArgumentException(obj2 + " incompatible with " + obj);
        }
        return this.hintmap.put((Key) obj, obj2);
    }

    public void add(RenderingHints renderingHints) {
        this.hintmap.putAll(renderingHints.hintmap);
    }

    @Override // java.util.Map
    public void clear() {
        this.hintmap.clear();
    }

    @Override // java.util.Map
    public Object remove(Object obj) {
        return this.hintmap.remove((Key) obj);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Object, ? extends Object> map) {
        if (RenderingHints.class.isInstance(map)) {
            for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
                this.hintmap.put(entry.getKey(), entry.getValue());
            }
            return;
        }
        for (Map.Entry<? extends Object, ? extends Object> entry2 : map.entrySet()) {
            put(entry2.getKey(), entry2.getValue());
        }
    }

    @Override // java.util.Map
    public Set<Object> keySet() {
        return this.hintmap.keySet();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.hintmap.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<Object, Object>> entrySet() {
        return Collections.unmodifiableMap(this.hintmap).entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (obj instanceof RenderingHints) {
            return this.hintmap.equals(((RenderingHints) obj).hintmap);
        }
        if (obj instanceof Map) {
            return this.hintmap.equals(obj);
        }
        return false;
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.hintmap.hashCode();
    }

    public Object clone() {
        try {
            RenderingHints renderingHints = (RenderingHints) super.clone();
            if (this.hintmap != null) {
                renderingHints.hintmap = (HashMap) this.hintmap.clone();
            }
            return renderingHints;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public String toString() {
        if (this.hintmap == null) {
            return getClass().getName() + "@" + Integer.toHexString(hashCode()) + " (0 hints)";
        }
        return this.hintmap.toString();
    }
}
