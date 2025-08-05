package javax.swing;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.beans.ConstructorProperties;
import java.io.PrintStream;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/BoxLayout.class */
public class BoxLayout implements LayoutManager2, Serializable {
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int LINE_AXIS = 2;
    public static final int PAGE_AXIS = 3;
    private int axis;
    private Container target;
    private transient SizeRequirements[] xChildren;
    private transient SizeRequirements[] yChildren;
    private transient SizeRequirements xTotal;
    private transient SizeRequirements yTotal;
    private transient PrintStream dbg;

    @ConstructorProperties({"target", "axis"})
    public BoxLayout(Container container, int i2) {
        if (i2 != 0 && i2 != 1 && i2 != 2 && i2 != 3) {
            throw new AWTError("Invalid axis");
        }
        this.axis = i2;
        this.target = container;
    }

    BoxLayout(Container container, int i2, PrintStream printStream) {
        this(container, i2);
        this.dbg = printStream;
    }

    public final Container getTarget() {
        return this.target;
    }

    public final int getAxis() {
        return this.axis;
    }

    @Override // java.awt.LayoutManager2
    public synchronized void invalidateLayout(Container container) {
        checkContainer(container);
        this.xChildren = null;
        this.yChildren = null;
        this.xTotal = null;
        this.yTotal = null;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
        invalidateLayout(component.getParent());
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        invalidateLayout(component.getParent());
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        invalidateLayout(component.getParent());
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (this) {
            checkContainer(container);
            checkRequests();
            dimension = new Dimension(this.xTotal.preferred, this.yTotal.preferred);
        }
        Insets insets = container.getInsets();
        dimension.width = (int) Math.min(dimension.width + insets.left + insets.right, 2147483647L);
        dimension.height = (int) Math.min(dimension.height + insets.top + insets.bottom, 2147483647L);
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (this) {
            checkContainer(container);
            checkRequests();
            dimension = new Dimension(this.xTotal.minimum, this.yTotal.minimum);
        }
        Insets insets = container.getInsets();
        dimension.width = (int) Math.min(dimension.width + insets.left + insets.right, 2147483647L);
        dimension.height = (int) Math.min(dimension.height + insets.top + insets.bottom, 2147483647L);
        return dimension;
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (this) {
            checkContainer(container);
            checkRequests();
            dimension = new Dimension(this.xTotal.maximum, this.yTotal.maximum);
        }
        Insets insets = container.getInsets();
        dimension.width = (int) Math.min(dimension.width + insets.left + insets.right, 2147483647L);
        dimension.height = (int) Math.min(dimension.height + insets.top + insets.bottom, 2147483647L);
        return dimension;
    }

    @Override // java.awt.LayoutManager2
    public synchronized float getLayoutAlignmentX(Container container) {
        checkContainer(container);
        checkRequests();
        return this.xTotal.alignment;
    }

    @Override // java.awt.LayoutManager2
    public synchronized float getLayoutAlignmentY(Container container) {
        checkContainer(container);
        checkRequests();
        return this.yTotal.alignment;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        checkContainer(container);
        int componentCount = container.getComponentCount();
        int[] iArr = new int[componentCount];
        int[] iArr2 = new int[componentCount];
        int[] iArr3 = new int[componentCount];
        int[] iArr4 = new int[componentCount];
        Dimension size = container.getSize();
        Insets insets = container.getInsets();
        size.width -= insets.left + insets.right;
        size.height -= insets.top + insets.bottom;
        ComponentOrientation componentOrientation = container.getComponentOrientation();
        int iResolveAxis = resolveAxis(this.axis, componentOrientation);
        boolean zIsLeftToRight = iResolveAxis != this.axis ? componentOrientation.isLeftToRight() : true;
        synchronized (this) {
            checkRequests();
            if (iResolveAxis == 0) {
                SizeRequirements.calculateTiledPositions(size.width, this.xTotal, this.xChildren, iArr, iArr2, zIsLeftToRight);
                SizeRequirements.calculateAlignedPositions(size.height, this.yTotal, this.yChildren, iArr3, iArr4);
            } else {
                SizeRequirements.calculateAlignedPositions(size.width, this.xTotal, this.xChildren, iArr, iArr2, zIsLeftToRight);
                SizeRequirements.calculateTiledPositions(size.height, this.yTotal, this.yChildren, iArr3, iArr4);
            }
        }
        for (int i2 = 0; i2 < componentCount; i2++) {
            container.getComponent(i2).setBounds((int) Math.min(insets.left + iArr[i2], 2147483647L), (int) Math.min(insets.top + iArr3[i2], 2147483647L), iArr2[i2], iArr4[i2]);
        }
        if (this.dbg != null) {
            for (int i3 = 0; i3 < componentCount; i3++) {
                this.dbg.println(container.getComponent(i3).toString());
                this.dbg.println("X: " + ((Object) this.xChildren[i3]));
                this.dbg.println("Y: " + ((Object) this.yChildren[i3]));
            }
        }
    }

    void checkContainer(Container container) {
        if (this.target != container) {
            throw new AWTError("BoxLayout can't be shared");
        }
    }

    void checkRequests() {
        if (this.xChildren == null || this.yChildren == null) {
            int componentCount = this.target.getComponentCount();
            this.xChildren = new SizeRequirements[componentCount];
            this.yChildren = new SizeRequirements[componentCount];
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = this.target.getComponent(i2);
                if (!component.isVisible()) {
                    this.xChildren[i2] = new SizeRequirements(0, 0, 0, component.getAlignmentX());
                    this.yChildren[i2] = new SizeRequirements(0, 0, 0, component.getAlignmentY());
                } else {
                    Dimension minimumSize = component.getMinimumSize();
                    Dimension preferredSize = component.getPreferredSize();
                    Dimension maximumSize = component.getMaximumSize();
                    this.xChildren[i2] = new SizeRequirements(minimumSize.width, preferredSize.width, maximumSize.width, component.getAlignmentX());
                    this.yChildren[i2] = new SizeRequirements(minimumSize.height, preferredSize.height, maximumSize.height, component.getAlignmentY());
                }
            }
            if (resolveAxis(this.axis, this.target.getComponentOrientation()) == 0) {
                this.xTotal = SizeRequirements.getTiledSizeRequirements(this.xChildren);
                this.yTotal = SizeRequirements.getAlignedSizeRequirements(this.yChildren);
            } else {
                this.xTotal = SizeRequirements.getAlignedSizeRequirements(this.xChildren);
                this.yTotal = SizeRequirements.getTiledSizeRequirements(this.yChildren);
            }
        }
    }

    private int resolveAxis(int i2, ComponentOrientation componentOrientation) {
        int i3;
        if (i2 == 2) {
            i3 = componentOrientation.isHorizontal() ? 0 : 1;
        } else if (i2 == 3) {
            i3 = componentOrientation.isHorizontal() ? 1 : 0;
        } else {
            i3 = i2;
        }
        return i3;
    }
}
