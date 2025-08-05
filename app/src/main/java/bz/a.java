package bz;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:bz/a.class */
public class a implements LayoutManager, Serializable {

    /* renamed from: a, reason: collision with root package name */
    int f9248a;

    /* renamed from: b, reason: collision with root package name */
    int f9249b;

    /* renamed from: c, reason: collision with root package name */
    int f9250c;

    /* renamed from: d, reason: collision with root package name */
    int f9251d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f9252e;

    public a() {
        this(1, 0, 0, 0);
    }

    public a(int i2, int i3, int i4, int i5) {
        this.f9252e = true;
        if (i2 == 0 && i3 == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.f9250c = i2;
        this.f9251d = i3;
        this.f9248a = i4;
        this.f9249b = i5;
    }

    public void a(int i2) {
        if (i2 == 0 && this.f9251d == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.f9250c = i2;
    }

    public void b(int i2) {
        if (i2 == 0 && this.f9250c == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.f9251d = i2;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.f9250c;
            int i3 = this.f9251d;
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < componentCount; i6++) {
                Dimension preferredSize = container.getComponent(i6).getPreferredSize();
                if (i4 < preferredSize.width) {
                    i4 = preferredSize.width;
                }
                if (i5 < preferredSize.height) {
                    i5 = preferredSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + (i3 * i4) + ((i3 - 1) * this.f9248a), insets.top + insets.bottom + (i2 * i5) + ((i2 - 1) * this.f9249b));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.f9250c;
            int i3 = this.f9251d;
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < componentCount; i6++) {
                Dimension minimumSize = container.getComponent(i6).getMinimumSize();
                if (i4 < minimumSize.width) {
                    i4 = minimumSize.width;
                }
                if (i5 < minimumSize.height) {
                    i5 = minimumSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + (i3 * i4) + ((i3 - 1) * this.f9248a), insets.top + insets.bottom + (i2 * i5) + ((i2 - 1) * this.f9249b));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.f9250c;
            int i3 = this.f9251d;
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            if (componentCount == 0) {
                return;
            }
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = (i3 - 1) * this.f9248a;
            int width = container.getWidth() - (insets.left + insets.right);
            int i5 = (width - i4) / i3;
            int i6 = (width - ((i5 * i3) + i4)) / 2;
            int i7 = (i2 - 1) * this.f9249b;
            int height = container.getHeight() - (insets.top + insets.bottom);
            int i8 = (height - i7) / i2;
            int i9 = (height - ((i8 * i2) + i7)) / 2;
            if (zIsLeftToRight) {
                int i10 = 0;
                int i11 = insets.top + i9;
                while (i10 < i2) {
                    int i12 = 0;
                    int i13 = insets.left + i6;
                    while (i12 < i3) {
                        int i14 = this.f9252e ? (i12 * i2) + i10 : (i10 * i3) + i12;
                        if (i14 < componentCount) {
                            container.getComponent(i14).setBounds(i13, i11, i5, i8);
                        }
                        i12++;
                        i13 += i5 + this.f9248a;
                    }
                    i10++;
                    i11 += i8 + this.f9249b;
                }
            } else {
                int i15 = 0;
                int i16 = insets.top + i9;
                while (i15 < i2) {
                    int i17 = 0;
                    int width2 = ((container.getWidth() - insets.right) - i5) - i6;
                    while (i17 < i3) {
                        int i18 = this.f9252e ? (i17 * i2) + i15 : (i15 * i3) + i17;
                        if (i18 < componentCount) {
                            container.getComponent(i18).setBounds(width2, i16, i5, i8);
                        }
                        i17++;
                        width2 -= i5 + this.f9248a;
                    }
                    i15++;
                    i16 += i8 + this.f9249b;
                }
            }
        }
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + this.f9248a + ",vgap=" + this.f9249b + ",rows=" + this.f9250c + ",cols=" + this.f9251d + "]";
    }

    public void a(boolean z2) {
        this.f9252e = z2;
    }
}
