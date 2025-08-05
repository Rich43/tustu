package sun.swing;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.ListIterator;

/* loaded from: rt.jar:sun/swing/ImageCache.class */
public class ImageCache {
    private int maxCount;
    private final LinkedList<SoftReference<Entry>> entries = new LinkedList<>();

    public ImageCache(int i2) {
        this.maxCount = i2;
    }

    void setMaxCount(int i2) {
        this.maxCount = i2;
    }

    public void flush() {
        this.entries.clear();
    }

    private Entry getEntry(Object obj, GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object[] objArr) {
        ListIterator<SoftReference<Entry>> listIterator = this.entries.listIterator();
        while (listIterator.hasNext()) {
            SoftReference<Entry> next = listIterator.next();
            Entry entry = next.get();
            if (entry == null) {
                listIterator.remove();
            } else if (entry.equals(graphicsConfiguration, i2, i3, objArr)) {
                listIterator.remove();
                this.entries.addFirst(next);
                return entry;
            }
        }
        Entry entry2 = new Entry(graphicsConfiguration, i2, i3, objArr);
        if (this.entries.size() >= this.maxCount) {
            this.entries.removeLast();
        }
        this.entries.addFirst(new SoftReference<>(entry2));
        return entry2;
    }

    public Image getImage(Object obj, GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object[] objArr) {
        return getEntry(obj, graphicsConfiguration, i2, i3, objArr).getImage();
    }

    public void setImage(Object obj, GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object[] objArr, Image image) {
        getEntry(obj, graphicsConfiguration, i2, i3, objArr).setImage(image);
    }

    /* loaded from: rt.jar:sun/swing/ImageCache$Entry.class */
    private static class Entry {
        private final GraphicsConfiguration config;

        /* renamed from: w, reason: collision with root package name */
        private final int f13672w;

        /* renamed from: h, reason: collision with root package name */
        private final int f13673h;
        private final Object[] args;
        private Image image;

        Entry(GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object[] objArr) {
            this.config = graphicsConfiguration;
            this.args = objArr;
            this.f13672w = i2;
            this.f13673h = i3;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return this.image;
        }

        public String toString() {
            String str = super.toString() + "[ graphicsConfig=" + ((Object) this.config) + ", image=" + ((Object) this.image) + ", w=" + this.f13672w + ", h=" + this.f13673h;
            if (this.args != null) {
                for (int i2 = 0; i2 < this.args.length; i2++) {
                    str = str + ", " + this.args[i2];
                }
            }
            return str + "]";
        }

        public boolean equals(GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object[] objArr) {
            if (this.f13672w == i2 && this.f13673h == i3) {
                if ((this.config != null && this.config.equals(graphicsConfiguration)) || (this.config == null && graphicsConfiguration == null)) {
                    if (this.args == null && objArr == null) {
                        return true;
                    }
                    if (this.args != null && objArr != null && this.args.length == objArr.length) {
                        for (int length = objArr.length - 1; length >= 0; length--) {
                            Object obj = this.args[length];
                            Object obj2 = objArr[length];
                            if (obj != null || obj2 == null) {
                                if (obj != null && !obj.equals(obj2)) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }
}
