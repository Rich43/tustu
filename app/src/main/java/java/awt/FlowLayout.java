package java.awt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/FlowLayout.class */
public class FlowLayout implements LayoutManager, Serializable {
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    public static final int LEADING = 3;
    public static final int TRAILING = 4;
    int align;
    int newAlign;
    int hgap;
    int vgap;
    private boolean alignOnBaseline;
    private static final long serialVersionUID = -7262534875583282631L;
    private static final int currentSerialVersion = 1;
    private int serialVersionOnStream;

    public FlowLayout() {
        this(1, 5, 5);
    }

    public FlowLayout(int i2) {
        this(i2, 5, 5);
    }

    public FlowLayout(int i2, int i3, int i4) {
        this.serialVersionOnStream = 1;
        this.hgap = i3;
        this.vgap = i4;
        setAlignment(i2);
    }

    public int getAlignment() {
        return this.newAlign;
    }

    public void setAlignment(int i2) {
        this.newAlign = i2;
        switch (i2) {
            case 3:
                this.align = 0;
                break;
            case 4:
                this.align = 2;
                break;
            default:
                this.align = i2;
                break;
        }
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int i2) {
        this.hgap = i2;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(int i2) {
        this.vgap = i2;
    }

    public void setAlignOnBaseline(boolean z2) {
        this.alignOnBaseline = z2;
    }

    public boolean getAlignOnBaseline() {
        return this.alignOnBaseline;
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
        int baseline;
        synchronized (container.getTreeLock()) {
            dimension = new Dimension(0, 0);
            int componentCount = container.getComponentCount();
            boolean z2 = true;
            boolean alignOnBaseline = getAlignOnBaseline();
            int iMax = 0;
            int iMax2 = 0;
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = container.getComponent(i2);
                if (component.isVisible()) {
                    Dimension preferredSize = component.getPreferredSize();
                    dimension.height = Math.max(dimension.height, preferredSize.height);
                    if (z2) {
                        z2 = false;
                    } else {
                        dimension.width += this.hgap;
                    }
                    dimension.width += preferredSize.width;
                    if (alignOnBaseline && (baseline = component.getBaseline(preferredSize.width, preferredSize.height)) >= 0) {
                        iMax = Math.max(iMax, baseline);
                        iMax2 = Math.max(iMax2, preferredSize.height - baseline);
                    }
                }
            }
            if (alignOnBaseline) {
                dimension.height = Math.max(iMax + iMax2, dimension.height);
            }
            Insets insets = container.getInsets();
            dimension.width += insets.left + insets.right + (this.hgap * 2);
            dimension.height += insets.top + insets.bottom + (this.vgap * 2);
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        int baseline;
        synchronized (container.getTreeLock()) {
            boolean alignOnBaseline = getAlignOnBaseline();
            dimension = new Dimension(0, 0);
            int componentCount = container.getComponentCount();
            int iMax = 0;
            int iMax2 = 0;
            boolean z2 = true;
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = container.getComponent(i2);
                if (component.visible) {
                    Dimension minimumSize = component.getMinimumSize();
                    dimension.height = Math.max(dimension.height, minimumSize.height);
                    if (z2) {
                        z2 = false;
                    } else {
                        dimension.width += this.hgap;
                    }
                    dimension.width += minimumSize.width;
                    if (alignOnBaseline && (baseline = component.getBaseline(minimumSize.width, minimumSize.height)) >= 0) {
                        iMax = Math.max(iMax, baseline);
                        iMax2 = Math.max(iMax2, dimension.height - baseline);
                    }
                }
            }
            if (alignOnBaseline) {
                dimension.height = Math.max(iMax + iMax2, dimension.height);
            }
            Insets insets = container.getInsets();
            dimension.width += insets.left + insets.right + (this.hgap * 2);
            dimension.height += insets.top + insets.bottom + (this.vgap * 2);
        }
        return dimension;
    }

    private int moveComponents(Container container, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2, boolean z3, int[] iArr, int[] iArr2) {
        int i8;
        switch (this.newAlign) {
            case 0:
                i2 += z2 ? 0 : i4;
                break;
            case 1:
                i2 += i4 / 2;
                break;
            case 2:
                i2 += z2 ? i4 : 0;
                break;
            case 4:
                i2 += i4;
                break;
        }
        int iMax = 0;
        int iMax2 = 0;
        int i9 = 0;
        if (z3) {
            int iMax3 = 0;
            for (int i10 = i6; i10 < i7; i10++) {
                Component component = container.getComponent(i10);
                if (component.visible) {
                    if (iArr[i10] >= 0) {
                        iMax = Math.max(iMax, iArr[i10]);
                        iMax3 = Math.max(iMax3, iArr2[i10]);
                    } else {
                        iMax2 = Math.max(component.getHeight(), iMax2);
                    }
                }
            }
            i5 = Math.max(iMax + iMax3, iMax2);
            i9 = ((i5 - iMax) - iMax3) / 2;
        }
        for (int i11 = i6; i11 < i7; i11++) {
            Component component2 = container.getComponent(i11);
            if (component2.isVisible()) {
                if (z3 && iArr[i11] >= 0) {
                    i8 = ((i3 + i9) + iMax) - iArr[i11];
                } else {
                    i8 = i3 + ((i5 - component2.height) / 2);
                }
                if (z2) {
                    component2.setLocation(i2, i8);
                } else {
                    component2.setLocation((container.width - i2) - component2.width, i8);
                }
                i2 += component2.width + this.hgap;
            }
        }
        return i5;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int i2 = container.width - ((insets.left + insets.right) + (this.hgap * 2));
            int componentCount = container.getComponentCount();
            int i3 = 0;
            int i4 = insets.top + this.vgap;
            int iMax = 0;
            int i5 = 0;
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            boolean alignOnBaseline = getAlignOnBaseline();
            int[] iArr = null;
            int[] iArr2 = null;
            if (alignOnBaseline) {
                iArr = new int[componentCount];
                iArr2 = new int[componentCount];
            }
            for (int i6 = 0; i6 < componentCount; i6++) {
                Component component = container.getComponent(i6);
                if (component.isVisible()) {
                    Dimension preferredSize = component.getPreferredSize();
                    component.setSize(preferredSize.width, preferredSize.height);
                    if (alignOnBaseline) {
                        int baseline = component.getBaseline(preferredSize.width, preferredSize.height);
                        if (baseline >= 0) {
                            iArr[i6] = baseline;
                            iArr2[i6] = preferredSize.height - baseline;
                        } else {
                            iArr[i6] = -1;
                        }
                    }
                    if (i3 == 0 || i3 + preferredSize.width <= i2) {
                        if (i3 > 0) {
                            i3 += this.hgap;
                        }
                        i3 += preferredSize.width;
                        iMax = Math.max(iMax, preferredSize.height);
                    } else {
                        int iMoveComponents = moveComponents(container, insets.left + this.hgap, i4, i2 - i3, iMax, i5, i6, zIsLeftToRight, alignOnBaseline, iArr, iArr2);
                        i3 = preferredSize.width;
                        i4 += this.vgap + iMoveComponents;
                        iMax = preferredSize.height;
                        i5 = i6;
                    }
                }
            }
            moveComponents(container, insets.left + this.hgap, i4, i2 - i3, iMax, i5, componentCount, zIsLeftToRight, alignOnBaseline, iArr, iArr2);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.serialVersionOnStream < 1) {
            setAlignment(this.align);
        }
        this.serialVersionOnStream = 1;
    }

    public String toString() {
        String str = "";
        switch (this.align) {
            case 0:
                str = ",align=left";
                break;
            case 1:
                str = ",align=center";
                break;
            case 2:
                str = ",align=right";
                break;
            case 3:
                str = ",align=leading";
                break;
            case 4:
                str = ",align=trailing";
                break;
        }
        return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + str + "]";
    }
}
