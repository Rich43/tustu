package javax.swing;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.beans.ConstructorProperties;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/OverlayLayout.class */
public class OverlayLayout implements LayoutManager2, Serializable {
    private Container target;
    private SizeRequirements[] xChildren;
    private SizeRequirements[] yChildren;
    private SizeRequirements xTotal;
    private SizeRequirements yTotal;

    @ConstructorProperties({"target"})
    public OverlayLayout(Container container) {
        this.target = container;
    }

    public final Container getTarget() {
        return this.target;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
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
        checkContainer(container);
        checkRequests();
        Dimension dimension = new Dimension(this.xTotal.preferred, this.yTotal.preferred);
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        checkContainer(container);
        checkRequests();
        Dimension dimension = new Dimension(this.xTotal.minimum, this.yTotal.minimum);
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        checkContainer(container);
        checkRequests();
        Dimension dimension = new Dimension(this.xTotal.maximum, this.yTotal.maximum);
        Insets insets = container.getInsets();
        dimension.width += insets.left + insets.right;
        dimension.height += insets.top + insets.bottom;
        return dimension;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        checkContainer(container);
        checkRequests();
        return this.xTotal.alignment;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        checkContainer(container);
        checkRequests();
        return this.yTotal.alignment;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        checkContainer(container);
        checkRequests();
        int componentCount = container.getComponentCount();
        int[] iArr = new int[componentCount];
        int[] iArr2 = new int[componentCount];
        int[] iArr3 = new int[componentCount];
        int[] iArr4 = new int[componentCount];
        Dimension size = container.getSize();
        Insets insets = container.getInsets();
        size.width -= insets.left + insets.right;
        size.height -= insets.top + insets.bottom;
        SizeRequirements.calculateAlignedPositions(size.width, this.xTotal, this.xChildren, iArr, iArr2);
        SizeRequirements.calculateAlignedPositions(size.height, this.yTotal, this.yChildren, iArr3, iArr4);
        for (int i2 = 0; i2 < componentCount; i2++) {
            container.getComponent(i2).setBounds(insets.left + iArr[i2], insets.top + iArr3[i2], iArr2[i2], iArr4[i2]);
        }
    }

    void checkContainer(Container container) {
        if (this.target != container) {
            throw new AWTError("OverlayLayout can't be shared");
        }
    }

    void checkRequests() {
        if (this.xChildren == null || this.yChildren == null) {
            int componentCount = this.target.getComponentCount();
            this.xChildren = new SizeRequirements[componentCount];
            this.yChildren = new SizeRequirements[componentCount];
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = this.target.getComponent(i2);
                Dimension minimumSize = component.getMinimumSize();
                Dimension preferredSize = component.getPreferredSize();
                Dimension maximumSize = component.getMaximumSize();
                this.xChildren[i2] = new SizeRequirements(minimumSize.width, preferredSize.width, maximumSize.width, component.getAlignmentX());
                this.yChildren[i2] = new SizeRequirements(minimumSize.height, preferredSize.height, maximumSize.height, component.getAlignmentY());
            }
            this.xTotal = SizeRequirements.getAlignedSizeRequirements(this.xChildren);
            this.yTotal = SizeRequirements.getAlignedSizeRequirements(this.yChildren);
        }
    }
}
