package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.LayoutStyle;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/GroupLayout.class */
public class GroupLayout implements LayoutManager2 {
    private static final int MIN_SIZE = 0;
    private static final int PREF_SIZE = 1;
    private static final int MAX_SIZE = 2;
    private static final int SPECIFIC_SIZE = 3;
    private static final int UNSET = Integer.MIN_VALUE;
    public static final int DEFAULT_SIZE = -1;
    public static final int PREFERRED_SIZE = -2;
    private boolean autocreatePadding;
    private boolean autocreateContainerPadding;
    private Group horizontalGroup;
    private Group verticalGroup;
    private Map<Component, ComponentInfo> componentInfos;
    private Container host;
    private Set<Spring> tmpParallelSet;
    private boolean springsChanged;
    private boolean isValid;
    private boolean hasPreferredPaddingSprings;
    private LayoutStyle layoutStyle;
    private boolean honorsVisibility;

    /* loaded from: rt.jar:javax/swing/GroupLayout$Alignment.class */
    public enum Alignment {
        LEADING,
        TRAILING,
        CENTER,
        BASELINE
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkSize(int i2, int i3, int i4, boolean z2) {
        checkResizeType(i2, z2);
        if (!z2 && i3 < 0) {
            throw new IllegalArgumentException("Pref must be >= 0");
        }
        if (z2) {
            checkResizeType(i3, true);
        }
        checkResizeType(i4, z2);
        checkLessThan(i2, i3);
        checkLessThan(i3, i4);
    }

    private static void checkResizeType(int i2, boolean z2) {
        if (i2 < 0) {
            if ((z2 && i2 != -1 && i2 != -2) || (!z2 && i2 != -2)) {
                throw new IllegalArgumentException("Invalid size");
            }
        }
    }

    private static void checkLessThan(int i2, int i3) {
        if (i2 >= 0 && i3 >= 0 && i2 > i3) {
            throw new IllegalArgumentException("Following is not met: min<=pref<=max");
        }
    }

    public GroupLayout(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("Container must be non-null");
        }
        this.honorsVisibility = true;
        this.host = container;
        setHorizontalGroup(createParallelGroup(Alignment.LEADING, true));
        setVerticalGroup(createParallelGroup(Alignment.LEADING, true));
        this.componentInfos = new HashMap();
        this.tmpParallelSet = new HashSet();
    }

    public void setHonorsVisibility(boolean z2) {
        if (this.honorsVisibility != z2) {
            this.honorsVisibility = z2;
            this.springsChanged = true;
            this.isValid = false;
            invalidateHost();
        }
    }

    public boolean getHonorsVisibility() {
        return this.honorsVisibility;
    }

    public void setHonorsVisibility(Component component, Boolean bool) {
        if (component == null) {
            throw new IllegalArgumentException("Component must be non-null");
        }
        getComponentInfo(component).setHonorsVisibility(bool);
        this.springsChanged = true;
        this.isValid = false;
        invalidateHost();
    }

    public void setAutoCreateGaps(boolean z2) {
        if (this.autocreatePadding != z2) {
            this.autocreatePadding = z2;
            invalidateHost();
        }
    }

    public boolean getAutoCreateGaps() {
        return this.autocreatePadding;
    }

    public void setAutoCreateContainerGaps(boolean z2) {
        if (this.autocreateContainerPadding != z2) {
            this.autocreateContainerPadding = z2;
            this.horizontalGroup = createTopLevelGroup(getHorizontalGroup());
            this.verticalGroup = createTopLevelGroup(getVerticalGroup());
            invalidateHost();
        }
    }

    public boolean getAutoCreateContainerGaps() {
        return this.autocreateContainerPadding;
    }

    public void setHorizontalGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group must be non-null");
        }
        this.horizontalGroup = createTopLevelGroup(group);
        invalidateHost();
    }

    private Group getHorizontalGroup() {
        int i2 = 0;
        if (this.horizontalGroup.springs.size() > 1) {
            i2 = 1;
        }
        return (Group) this.horizontalGroup.springs.get(i2);
    }

    public void setVerticalGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group must be non-null");
        }
        this.verticalGroup = createTopLevelGroup(group);
        invalidateHost();
    }

    private Group getVerticalGroup() {
        int i2 = 0;
        if (this.verticalGroup.springs.size() > 1) {
            i2 = 1;
        }
        return (Group) this.verticalGroup.springs.get(i2);
    }

    private Group createTopLevelGroup(Group group) {
        SequentialGroup sequentialGroupCreateSequentialGroup = createSequentialGroup();
        if (getAutoCreateContainerGaps()) {
            sequentialGroupCreateSequentialGroup.addSpring(new ContainerAutoPreferredGapSpring());
            sequentialGroupCreateSequentialGroup.addGroup(group);
            sequentialGroupCreateSequentialGroup.addSpring(new ContainerAutoPreferredGapSpring());
        } else {
            sequentialGroupCreateSequentialGroup.addGroup(group);
        }
        return sequentialGroupCreateSequentialGroup;
    }

    public SequentialGroup createSequentialGroup() {
        return new SequentialGroup();
    }

    public ParallelGroup createParallelGroup() {
        return createParallelGroup(Alignment.LEADING);
    }

    public ParallelGroup createParallelGroup(Alignment alignment) {
        return createParallelGroup(alignment, true);
    }

    public ParallelGroup createParallelGroup(Alignment alignment, boolean z2) {
        if (alignment == null) {
            throw new IllegalArgumentException("alignment must be non null");
        }
        if (alignment == Alignment.BASELINE) {
            return new BaselineGroup(z2);
        }
        return new ParallelGroup(alignment, z2);
    }

    public ParallelGroup createBaselineGroup(boolean z2, boolean z3) {
        return new BaselineGroup(this, z2, z3);
    }

    public void linkSize(Component... componentArr) {
        linkSize(0, componentArr);
        linkSize(1, componentArr);
    }

    public void linkSize(int i2, Component... componentArr) {
        int i3;
        if (componentArr == null) {
            throw new IllegalArgumentException("Components must be non-null");
        }
        for (int length = componentArr.length - 1; length >= 0; length--) {
            Component component = componentArr[length];
            if (componentArr[length] == null) {
                throw new IllegalArgumentException("Components must be non-null");
            }
            getComponentInfo(component);
        }
        if (i2 == 0) {
            i3 = 0;
        } else if (i2 == 1) {
            i3 = 1;
        } else {
            throw new IllegalArgumentException("Axis must be one of SwingConstants.HORIZONTAL or SwingConstants.VERTICAL");
        }
        LinkInfo linkInfo = getComponentInfo(componentArr[componentArr.length - 1]).getLinkInfo(i3);
        for (int length2 = componentArr.length - 2; length2 >= 0; length2--) {
            linkInfo.add(getComponentInfo(componentArr[length2]));
        }
        invalidateHost();
    }

    public void replace(Component component, Component component2) {
        if (component == null || component2 == null) {
            throw new IllegalArgumentException("Components must be non-null");
        }
        if (this.springsChanged) {
            registerComponents(this.horizontalGroup, 0);
            registerComponents(this.verticalGroup, 1);
        }
        ComponentInfo componentInfoRemove = this.componentInfos.remove(component);
        if (componentInfoRemove == null) {
            throw new IllegalArgumentException("Component must already exist");
        }
        this.host.remove(component);
        if (component2.getParent() != this.host) {
            this.host.add(component2);
        }
        componentInfoRemove.setComponent(component2);
        this.componentInfos.put(component2, componentInfoRemove);
        invalidateHost();
    }

    public void setLayoutStyle(LayoutStyle layoutStyle) {
        this.layoutStyle = layoutStyle;
        invalidateHost();
    }

    public LayoutStyle getLayoutStyle() {
        return this.layoutStyle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LayoutStyle getLayoutStyle0() {
        LayoutStyle layoutStyle = getLayoutStyle();
        if (layoutStyle == null) {
            layoutStyle = LayoutStyle.getInstance();
        }
        return layoutStyle;
    }

    private void invalidateHost() {
        if (this.host instanceof JComponent) {
            ((JComponent) this.host).revalidate();
        } else {
            this.host.invalidate();
        }
        this.host.repaint();
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        ComponentInfo componentInfoRemove = this.componentInfos.remove(component);
        if (componentInfoRemove != null) {
            componentInfoRemove.dispose();
            this.springsChanged = true;
            this.isValid = false;
        }
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        checkParent(container);
        prepare(1);
        return adjustSize(this.horizontalGroup.getPreferredSize(0), this.verticalGroup.getPreferredSize(1));
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        checkParent(container);
        prepare(0);
        return adjustSize(this.horizontalGroup.getMinimumSize(0), this.verticalGroup.getMinimumSize(1));
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        prepare(3);
        Insets insets = container.getInsets();
        int width = (container.getWidth() - insets.left) - insets.right;
        int height = (container.getHeight() - insets.top) - insets.bottom;
        boolean zIsLeftToRight = isLeftToRight();
        if (getAutoCreateGaps() || getAutoCreateContainerGaps() || this.hasPreferredPaddingSprings) {
            calculateAutopadding(this.horizontalGroup, 0, 3, 0, width);
            calculateAutopadding(this.verticalGroup, 1, 3, 0, height);
        }
        this.horizontalGroup.setSize(0, 0, width);
        this.verticalGroup.setSize(1, 0, height);
        Iterator<ComponentInfo> it = this.componentInfos.values().iterator();
        while (it.hasNext()) {
            it.next().setBounds(insets, width, zIsLeftToRight);
        }
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        checkParent(container);
        prepare(2);
        return adjustSize(this.horizontalGroup.getMaximumSize(0), this.verticalGroup.getMaximumSize(1));
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        checkParent(container);
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        checkParent(container);
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
        checkParent(container);
        synchronized (container.getTreeLock()) {
            this.isValid = false;
        }
    }

    private void prepare(int i2) {
        boolean z2 = false;
        if (!this.isValid) {
            this.isValid = true;
            this.horizontalGroup.setSize(0, Integer.MIN_VALUE, Integer.MIN_VALUE);
            this.verticalGroup.setSize(1, Integer.MIN_VALUE, Integer.MIN_VALUE);
            for (ComponentInfo componentInfo : this.componentInfos.values()) {
                if (componentInfo.updateVisibility()) {
                    z2 = true;
                }
                componentInfo.clearCachedSize();
            }
        }
        if (this.springsChanged) {
            registerComponents(this.horizontalGroup, 0);
            registerComponents(this.verticalGroup, 1);
        }
        if (this.springsChanged || z2) {
            checkComponents();
            this.horizontalGroup.removeAutopadding();
            this.verticalGroup.removeAutopadding();
            if (getAutoCreateGaps()) {
                insertAutopadding(true);
            } else if (this.hasPreferredPaddingSprings || getAutoCreateContainerGaps()) {
                insertAutopadding(false);
            }
            this.springsChanged = false;
        }
        if (i2 != 3) {
            if (getAutoCreateGaps() || getAutoCreateContainerGaps() || this.hasPreferredPaddingSprings) {
                calculateAutopadding(this.horizontalGroup, 0, i2, 0, 0);
                calculateAutopadding(this.verticalGroup, 1, i2, 0, 0);
            }
        }
    }

    private void calculateAutopadding(Group group, int i2, int i3, int i4, int i5) {
        group.unsetAutopadding();
        switch (i3) {
            case 0:
                i5 = group.getMinimumSize(i2);
                break;
            case 1:
                i5 = group.getPreferredSize(i2);
                break;
            case 2:
                i5 = group.getMaximumSize(i2);
                break;
        }
        group.setSize(i2, i4, i5);
        group.calculateAutopadding(i2);
    }

    private void checkComponents() {
        for (ComponentInfo componentInfo : this.componentInfos.values()) {
            if (componentInfo.horizontalSpring == null) {
                throw new IllegalStateException(((Object) componentInfo.component) + " is not attached to a horizontal group");
            }
            if (componentInfo.verticalSpring == null) {
                throw new IllegalStateException(((Object) componentInfo.component) + " is not attached to a vertical group");
            }
        }
    }

    private void registerComponents(Group group, int i2) {
        List<Spring> list = group.springs;
        for (int size = list.size() - 1; size >= 0; size--) {
            Spring spring = list.get(size);
            if (spring instanceof ComponentSpring) {
                ((ComponentSpring) spring).installIfNecessary(i2);
            } else if (spring instanceof Group) {
                registerComponents((Group) spring, i2);
            }
        }
    }

    private Dimension adjustSize(int i2, int i3) {
        Insets insets = this.host.getInsets();
        return new Dimension(i2 + insets.left + insets.right, i3 + insets.top + insets.bottom);
    }

    private void checkParent(Container container) {
        if (container != this.host) {
            throw new IllegalArgumentException("GroupLayout can only be used with one Container at a time");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ComponentInfo getComponentInfo(Component component) {
        ComponentInfo componentInfo = this.componentInfos.get(component);
        if (componentInfo == null) {
            componentInfo = new ComponentInfo(component);
            this.componentInfos.put(component, componentInfo);
            if (component.getParent() != this.host) {
                this.host.add(component);
            }
        }
        return componentInfo;
    }

    private void insertAutopadding(boolean z2) {
        this.horizontalGroup.insertAutopadding(0, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), z2);
        this.verticalGroup.insertAutopadding(1, new ArrayList(1), new ArrayList(1), new ArrayList(1), new ArrayList(1), z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean areParallelSiblings(Component component, Component component2, int i2) {
        ComponentSpring componentSpring;
        ComponentSpring componentSpring2;
        ComponentInfo componentInfo = getComponentInfo(component);
        ComponentInfo componentInfo2 = getComponentInfo(component2);
        if (i2 == 0) {
            componentSpring = componentInfo.horizontalSpring;
            componentSpring2 = componentInfo2.horizontalSpring;
        } else {
            componentSpring = componentInfo.verticalSpring;
            componentSpring2 = componentInfo2.verticalSpring;
        }
        Set<Spring> set = this.tmpParallelSet;
        set.clear();
        Spring parent = componentSpring.getParent();
        while (true) {
            Spring spring = parent;
            if (spring == null) {
                break;
            }
            set.add(spring);
            parent = spring.getParent();
        }
        Spring parent2 = componentSpring2.getParent();
        while (true) {
            Spring parent3 = parent2;
            if (parent3 != null) {
                if (set.contains(parent3)) {
                    set.clear();
                    while (parent3 != null) {
                        if (parent3 instanceof ParallelGroup) {
                            return true;
                        }
                        parent3 = parent3.getParent();
                    }
                    return false;
                }
                parent2 = parent3.getParent();
            } else {
                set.clear();
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLeftToRight() {
        return this.host.getComponentOrientation().isLeftToRight();
    }

    public String toString() {
        if (this.springsChanged) {
            registerComponents(this.horizontalGroup, 0);
            registerComponents(this.verticalGroup, 1);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("HORIZONTAL\n");
        createSpringDescription(stringBuffer, this.horizontalGroup, Constants.INDENT, 0);
        stringBuffer.append("\nVERTICAL\n");
        createSpringDescription(stringBuffer, this.verticalGroup, Constants.INDENT, 1);
        return stringBuffer.toString();
    }

    private void createSpringDescription(StringBuffer stringBuffer, Spring spring, String str, int i2) {
        String str2 = "";
        String str3 = "";
        if (spring instanceof ComponentSpring) {
            ComponentSpring componentSpring = (ComponentSpring) spring;
            str2 = Integer.toString(componentSpring.getOrigin()) + " ";
            String name = componentSpring.getComponent().getName();
            if (name != null) {
                str2 = "name=" + name + ", ";
            }
        }
        if (spring instanceof AutoPreferredGapSpring) {
            AutoPreferredGapSpring autoPreferredGapSpring = (AutoPreferredGapSpring) spring;
            str3 = ", userCreated=" + autoPreferredGapSpring.getUserCreated() + ", matches=" + autoPreferredGapSpring.getMatchDescription();
        }
        stringBuffer.append(str + spring.getClass().getName() + " " + Integer.toHexString(spring.hashCode()) + " " + str2 + ", size=" + spring.getSize() + ", alignment=" + ((Object) spring.getAlignment()) + " prefs=[" + spring.getMinimumSize(i2) + " " + spring.getPreferredSize(i2) + " " + spring.getMaximumSize(i2) + str3 + "]\n");
        if (spring instanceof Group) {
            List<Spring> list = ((Group) spring).springs;
            String str4 = str + Constants.INDENT;
            for (int i3 = 0; i3 < list.size(); i3++) {
                createSpringDescription(stringBuffer, list.get(i3), str4, i2);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$Spring.class */
    private abstract class Spring {
        private int size;
        private Spring parent;
        private Alignment alignment;
        private int max = Integer.MIN_VALUE;
        private int pref = Integer.MIN_VALUE;
        private int min = Integer.MIN_VALUE;

        abstract int calculateMinimumSize(int i2);

        abstract int calculatePreferredSize(int i2);

        abstract int calculateMaximumSize(int i2);

        abstract boolean willHaveZeroSize(boolean z2);

        Spring() {
        }

        void setParent(Spring spring) {
            this.parent = spring;
        }

        Spring getParent() {
            return this.parent;
        }

        void setAlignment(Alignment alignment) {
            this.alignment = alignment;
        }

        Alignment getAlignment() {
            return this.alignment;
        }

        final int getMinimumSize(int i2) {
            if (this.min == Integer.MIN_VALUE) {
                this.min = constrain(calculateMinimumSize(i2));
            }
            return this.min;
        }

        final int getPreferredSize(int i2) {
            if (this.pref == Integer.MIN_VALUE) {
                this.pref = constrain(calculatePreferredSize(i2));
            }
            return this.pref;
        }

        final int getMaximumSize(int i2) {
            if (this.max == Integer.MIN_VALUE) {
                this.max = constrain(calculateMaximumSize(i2));
            }
            return this.max;
        }

        void setSize(int i2, int i3, int i4) {
            this.size = i4;
            if (i4 == Integer.MIN_VALUE) {
                unset();
            }
        }

        void unset() {
            this.max = Integer.MIN_VALUE;
            this.pref = Integer.MIN_VALUE;
            this.min = Integer.MIN_VALUE;
            this.size = Integer.MIN_VALUE;
        }

        int getSize() {
            return this.size;
        }

        int constrain(int i2) {
            return Math.min(i2, Short.MAX_VALUE);
        }

        int getBaseline() {
            return -1;
        }

        Component.BaselineResizeBehavior getBaselineResizeBehavior() {
            return Component.BaselineResizeBehavior.OTHER;
        }

        final boolean isResizable(int i2) {
            int minimumSize = getMinimumSize(i2);
            int preferredSize = getPreferredSize(i2);
            return (minimumSize == preferredSize && preferredSize == getMaximumSize(i2)) ? false : true;
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$Group.class */
    public abstract class Group extends Spring {
        List<Spring> springs;
        static final /* synthetic */ boolean $assertionsDisabled;

        abstract void setValidSize(int i2, int i3, int i4);

        abstract int operator(int i2, int i3);

        abstract void insertAutopadding(int i2, List<AutoPreferredGapSpring> list, List<AutoPreferredGapSpring> list2, List<ComponentSpring> list3, List<ComponentSpring> list4, boolean z2);

        static {
            $assertionsDisabled = !GroupLayout.class.desiredAssertionStatus();
        }

        Group() {
            super();
            this.springs = new ArrayList();
        }

        public Group addGroup(Group group) {
            return addSpring(group);
        }

        public Group addComponent(Component component) {
            return addComponent(component, -1, -1, -1);
        }

        public Group addComponent(Component component, int i2, int i3, int i4) {
            return addSpring(new ComponentSpring(component, i2, i3, i4));
        }

        public Group addGap(int i2) {
            return addGap(i2, i2, i2);
        }

        public Group addGap(int i2, int i3, int i4) {
            return addSpring(GroupLayout.this.new GapSpring(i2, i3, i4));
        }

        Spring getSpring(int i2) {
            return this.springs.get(i2);
        }

        int indexOf(Spring spring) {
            return this.springs.indexOf(spring);
        }

        Group addSpring(Spring spring) {
            this.springs.add(spring);
            spring.setParent(this);
            if (!(spring instanceof AutoPreferredGapSpring) || !((AutoPreferredGapSpring) spring).getUserCreated()) {
                GroupLayout.this.springsChanged = true;
            }
            return this;
        }

        @Override // javax.swing.GroupLayout.Spring
        void setSize(int i2, int i3, int i4) {
            super.setSize(i2, i3, i4);
            if (i4 == Integer.MIN_VALUE) {
                for (int size = this.springs.size() - 1; size >= 0; size--) {
                    getSpring(size).setSize(i2, i3, i4);
                }
                return;
            }
            setValidSize(i2, i3, i4);
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMinimumSize(int i2) {
            return calculateSize(i2, 0);
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculatePreferredSize(int i2) {
            return calculateSize(i2, 1);
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMaximumSize(int i2) {
            return calculateSize(i2, 2);
        }

        int calculateSize(int i2, int i3) {
            int size = this.springs.size();
            if (size == 0) {
                return 0;
            }
            if (size == 1) {
                return getSpringSize(getSpring(0), i2, i3);
            }
            int iConstrain = constrain(operator(getSpringSize(getSpring(0), i2, i3), getSpringSize(getSpring(1), i2, i3)));
            for (int i4 = 2; i4 < size; i4++) {
                iConstrain = constrain(operator(iConstrain, getSpringSize(getSpring(i4), i2, i3)));
            }
            return iConstrain;
        }

        int getSpringSize(Spring spring, int i2, int i3) {
            switch (i3) {
                case 0:
                    return spring.getMinimumSize(i2);
                case 1:
                    return spring.getPreferredSize(i2);
                case 2:
                    return spring.getMaximumSize(i2);
                default:
                    if ($assertionsDisabled) {
                        return 0;
                    }
                    throw new AssertionError();
            }
        }

        void removeAutopadding() {
            unset();
            for (int size = this.springs.size() - 1; size >= 0; size--) {
                Spring spring = this.springs.get(size);
                if (spring instanceof AutoPreferredGapSpring) {
                    if (((AutoPreferredGapSpring) spring).getUserCreated()) {
                        ((AutoPreferredGapSpring) spring).reset();
                    } else {
                        this.springs.remove(size);
                    }
                } else if (spring instanceof Group) {
                    ((Group) spring).removeAutopadding();
                }
            }
        }

        void unsetAutopadding() {
            unset();
            for (int size = this.springs.size() - 1; size >= 0; size--) {
                Spring spring = this.springs.get(size);
                if (spring instanceof AutoPreferredGapSpring) {
                    spring.unset();
                } else if (spring instanceof Group) {
                    ((Group) spring).unsetAutopadding();
                }
            }
        }

        void calculateAutopadding(int i2) {
            for (int size = this.springs.size() - 1; size >= 0; size--) {
                Spring spring = this.springs.get(size);
                if (spring instanceof AutoPreferredGapSpring) {
                    spring.unset();
                    ((AutoPreferredGapSpring) spring).calculatePadding(i2);
                } else if (spring instanceof Group) {
                    ((Group) spring).calculateAutopadding(i2);
                }
            }
            unset();
        }

        @Override // javax.swing.GroupLayout.Spring
        boolean willHaveZeroSize(boolean z2) {
            for (int size = this.springs.size() - 1; size >= 0; size--) {
                if (!this.springs.get(size).willHaveZeroSize(z2)) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$SequentialGroup.class */
    public class SequentialGroup extends Group {
        private Spring baselineSpring;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !GroupLayout.class.desiredAssertionStatus();
        }

        SequentialGroup() {
            super();
        }

        @Override // javax.swing.GroupLayout.Group
        public SequentialGroup addGroup(Group group) {
            return (SequentialGroup) super.addGroup(group);
        }

        public SequentialGroup addGroup(boolean z2, Group group) {
            super.addGroup(group);
            if (z2) {
                this.baselineSpring = group;
            }
            return this;
        }

        @Override // javax.swing.GroupLayout.Group
        public SequentialGroup addComponent(Component component) {
            return (SequentialGroup) super.addComponent(component);
        }

        public SequentialGroup addComponent(boolean z2, Component component) {
            super.addComponent(component);
            if (z2) {
                this.baselineSpring = this.springs.get(this.springs.size() - 1);
            }
            return this;
        }

        @Override // javax.swing.GroupLayout.Group
        public SequentialGroup addComponent(Component component, int i2, int i3, int i4) {
            return (SequentialGroup) super.addComponent(component, i2, i3, i4);
        }

        public SequentialGroup addComponent(boolean z2, Component component, int i2, int i3, int i4) {
            super.addComponent(component, i2, i3, i4);
            if (z2) {
                this.baselineSpring = this.springs.get(this.springs.size() - 1);
            }
            return this;
        }

        @Override // javax.swing.GroupLayout.Group
        public SequentialGroup addGap(int i2) {
            return (SequentialGroup) super.addGap(i2);
        }

        @Override // javax.swing.GroupLayout.Group
        public SequentialGroup addGap(int i2, int i3, int i4) {
            return (SequentialGroup) super.addGap(i2, i3, i4);
        }

        public SequentialGroup addPreferredGap(JComponent jComponent, JComponent jComponent2, LayoutStyle.ComponentPlacement componentPlacement) {
            return addPreferredGap(jComponent, jComponent2, componentPlacement, -1, -2);
        }

        public SequentialGroup addPreferredGap(JComponent jComponent, JComponent jComponent2, LayoutStyle.ComponentPlacement componentPlacement, int i2, int i3) {
            if (componentPlacement == null) {
                throw new IllegalArgumentException("Type must be non-null");
            }
            if (jComponent == null || jComponent2 == null) {
                throw new IllegalArgumentException("Components must be non-null");
            }
            checkPreferredGapValues(i2, i3);
            return (SequentialGroup) addSpring(GroupLayout.this.new PreferredGapSpring(jComponent, jComponent2, componentPlacement, i2, i3));
        }

        public SequentialGroup addPreferredGap(LayoutStyle.ComponentPlacement componentPlacement) {
            return addPreferredGap(componentPlacement, -1, -1);
        }

        public SequentialGroup addPreferredGap(LayoutStyle.ComponentPlacement componentPlacement, int i2, int i3) {
            if (componentPlacement != LayoutStyle.ComponentPlacement.RELATED && componentPlacement != LayoutStyle.ComponentPlacement.UNRELATED) {
                throw new IllegalArgumentException("Type must be one of LayoutStyle.ComponentPlacement.RELATED or LayoutStyle.ComponentPlacement.UNRELATED");
            }
            checkPreferredGapValues(i2, i3);
            GroupLayout.this.hasPreferredPaddingSprings = true;
            return (SequentialGroup) addSpring(GroupLayout.this.new AutoPreferredGapSpring(componentPlacement, i2, i3));
        }

        public SequentialGroup addContainerGap() {
            return addContainerGap(-1, -1);
        }

        public SequentialGroup addContainerGap(int i2, int i3) {
            if ((i2 >= 0 || i2 == -1) && ((i3 >= 0 || i3 == -1 || i3 == -2) && (i2 < 0 || i3 < 0 || i2 <= i3))) {
                GroupLayout.this.hasPreferredPaddingSprings = true;
                return (SequentialGroup) addSpring(GroupLayout.this.new ContainerAutoPreferredGapSpring(i2, i3));
            }
            throw new IllegalArgumentException("Pref and max must be either DEFAULT_VALUE or >= 0 and pref <= max");
        }

        @Override // javax.swing.GroupLayout.Group
        int operator(int i2, int i3) {
            return constrain(i2) + constrain(i3);
        }

        @Override // javax.swing.GroupLayout.Group
        void setValidSize(int i2, int i3, int i4) {
            if (i4 == getPreferredSize(i2)) {
                for (Spring spring : this.springs) {
                    int preferredSize = spring.getPreferredSize(i2);
                    spring.setSize(i2, i3, preferredSize);
                    i3 += preferredSize;
                }
                return;
            }
            if (this.springs.size() == 1) {
                Spring spring2 = getSpring(0);
                spring2.setSize(i2, i3, Math.min(Math.max(i4, spring2.getMinimumSize(i2)), spring2.getMaximumSize(i2)));
            } else if (this.springs.size() > 1) {
                setValidSizeNotPreferred(i2, i3, i4);
            }
        }

        private void setValidSizeNotPreferred(int i2, int i3, int i4) {
            int maximumSize;
            int preferredSize = i4 - getPreferredSize(i2);
            if (!$assertionsDisabled && preferredSize == 0) {
                throw new AssertionError();
            }
            boolean z2 = preferredSize < 0;
            int size = this.springs.size();
            if (z2) {
                preferredSize *= -1;
            }
            List<SpringDelta> listBuildResizableList = buildResizableList(i2, z2);
            int size2 = listBuildResizableList.size();
            if (size2 > 0) {
                int i5 = preferredSize / size2;
                int i6 = preferredSize - (i5 * size2);
                int[] iArr = new int[size];
                int i7 = z2 ? -1 : 1;
                for (int i8 = 0; i8 < size2; i8++) {
                    SpringDelta springDelta = listBuildResizableList.get(i8);
                    if (i8 + 1 == size2) {
                        i5 += i6;
                    }
                    springDelta.delta = Math.min(i5, springDelta.delta);
                    preferredSize -= springDelta.delta;
                    if (springDelta.delta != i5 && i8 + 1 < size2) {
                        i5 = preferredSize / ((size2 - i8) - 1);
                        i6 = preferredSize - (i5 * ((size2 - i8) - 1));
                    }
                    iArr[springDelta.index] = i7 * springDelta.delta;
                }
                for (int i9 = 0; i9 < size; i9++) {
                    Spring spring = getSpring(i9);
                    int preferredSize2 = spring.getPreferredSize(i2) + iArr[i9];
                    spring.setSize(i2, i3, preferredSize2);
                    i3 += preferredSize2;
                }
                return;
            }
            for (int i10 = 0; i10 < size; i10++) {
                Spring spring2 = getSpring(i10);
                if (z2) {
                    maximumSize = spring2.getMinimumSize(i2);
                } else {
                    maximumSize = spring2.getMaximumSize(i2);
                }
                int i11 = maximumSize;
                spring2.setSize(i2, i3, i11);
                i3 += i11;
            }
        }

        private List<SpringDelta> buildResizableList(int i2, boolean z2) {
            int maximumSize;
            int size = this.springs.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i3 = 0; i3 < size; i3++) {
                Spring spring = getSpring(i3);
                if (z2) {
                    maximumSize = spring.getPreferredSize(i2) - spring.getMinimumSize(i2);
                } else {
                    maximumSize = spring.getMaximumSize(i2) - spring.getPreferredSize(i2);
                }
                if (maximumSize > 0) {
                    arrayList.add(new SpringDelta(i3, maximumSize));
                }
            }
            Collections.sort(arrayList);
            return arrayList;
        }

        private int indexOfNextNonZeroSpring(int i2, boolean z2) {
            while (i2 < this.springs.size()) {
                if (!this.springs.get(i2).willHaveZeroSize(z2)) {
                    return i2;
                }
                i2++;
            }
            return i2;
        }

        @Override // javax.swing.GroupLayout.Group
        void insertAutopadding(int i2, List<AutoPreferredGapSpring> list, List<AutoPreferredGapSpring> list2, List<ComponentSpring> list3, List<ComponentSpring> list4, boolean z2) {
            ArrayList arrayList = new ArrayList(list);
            ArrayList arrayList2 = new ArrayList(1);
            List<ComponentSpring> arrayList3 = new ArrayList<>(list3);
            ArrayList arrayList4 = null;
            int iIndexOfNextNonZeroSpring = 0;
            while (iIndexOfNextNonZeroSpring < this.springs.size()) {
                Spring spring = getSpring(iIndexOfNextNonZeroSpring);
                if (spring instanceof AutoPreferredGapSpring) {
                    if (arrayList.size() == 0) {
                        AutoPreferredGapSpring autoPreferredGapSpring = (AutoPreferredGapSpring) spring;
                        autoPreferredGapSpring.setSources(arrayList3);
                        arrayList3.clear();
                        iIndexOfNextNonZeroSpring = indexOfNextNonZeroSpring(iIndexOfNextNonZeroSpring + 1, true);
                        if (iIndexOfNextNonZeroSpring == this.springs.size()) {
                            if (!(autoPreferredGapSpring instanceof ContainerAutoPreferredGapSpring)) {
                                list2.add(autoPreferredGapSpring);
                            }
                        } else {
                            arrayList.clear();
                            arrayList.add(autoPreferredGapSpring);
                        }
                    } else {
                        iIndexOfNextNonZeroSpring = indexOfNextNonZeroSpring(iIndexOfNextNonZeroSpring + 1, true);
                    }
                } else if (arrayList3.size() > 0 && z2) {
                    this.springs.add(iIndexOfNextNonZeroSpring, new AutoPreferredGapSpring());
                } else if (spring instanceof ComponentSpring) {
                    ComponentSpring componentSpring = (ComponentSpring) spring;
                    if (!componentSpring.isVisible()) {
                        iIndexOfNextNonZeroSpring++;
                    } else {
                        Iterator<E> it = arrayList.iterator();
                        while (it.hasNext()) {
                            ((AutoPreferredGapSpring) it.next()).addTarget(componentSpring, i2);
                        }
                        arrayList3.clear();
                        arrayList.clear();
                        iIndexOfNextNonZeroSpring = indexOfNextNonZeroSpring(iIndexOfNextNonZeroSpring + 1, false);
                        if (iIndexOfNextNonZeroSpring == this.springs.size()) {
                            list4.add(componentSpring);
                        } else {
                            arrayList3.add(componentSpring);
                        }
                    }
                } else if (spring instanceof Group) {
                    if (arrayList4 == null) {
                        arrayList4 = new ArrayList(1);
                    } else {
                        arrayList4.clear();
                    }
                    arrayList2.clear();
                    ((Group) spring).insertAutopadding(i2, arrayList, arrayList2, arrayList3, arrayList4, z2);
                    arrayList3.clear();
                    arrayList.clear();
                    iIndexOfNextNonZeroSpring = indexOfNextNonZeroSpring(iIndexOfNextNonZeroSpring + 1, arrayList4.size() == 0);
                    if (iIndexOfNextNonZeroSpring == this.springs.size()) {
                        list4.addAll(arrayList4);
                        list2.addAll(arrayList2);
                    } else {
                        arrayList3.addAll(arrayList4);
                        arrayList.addAll(arrayList2);
                    }
                } else {
                    arrayList.clear();
                    arrayList3.clear();
                    iIndexOfNextNonZeroSpring++;
                }
            }
        }

        @Override // javax.swing.GroupLayout.Spring
        int getBaseline() {
            int baseline;
            if (this.baselineSpring != null && (baseline = this.baselineSpring.getBaseline()) >= 0) {
                int preferredSize = 0;
                for (Spring spring : this.springs) {
                    if (spring == this.baselineSpring) {
                        return preferredSize + baseline;
                    }
                    preferredSize += spring.getPreferredSize(1);
                }
                return -1;
            }
            return -1;
        }

        @Override // javax.swing.GroupLayout.Spring
        Component.BaselineResizeBehavior getBaselineResizeBehavior() {
            Spring spring;
            Spring next;
            if (isResizable(1)) {
                if (!this.baselineSpring.isResizable(1)) {
                    boolean z2 = false;
                    Iterator<Spring> it = this.springs.iterator();
                    while (true) {
                        if (!it.hasNext() || (next = it.next()) == this.baselineSpring) {
                            break;
                        }
                        if (next.isResizable(1)) {
                            z2 = true;
                            break;
                        }
                    }
                    boolean z3 = false;
                    int size = this.springs.size() - 1;
                    while (true) {
                        if (size < 0 || (spring = this.springs.get(size)) == this.baselineSpring) {
                            break;
                        }
                        if (spring.isResizable(1)) {
                            z3 = true;
                            break;
                        }
                        size--;
                    }
                    if (z2 && !z3) {
                        return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
                    }
                    if (!z2 && z3) {
                        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
                    }
                } else {
                    Component.BaselineResizeBehavior baselineResizeBehavior = this.baselineSpring.getBaselineResizeBehavior();
                    if (baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_ASCENT) {
                        for (Spring spring2 : this.springs) {
                            if (spring2 == this.baselineSpring) {
                                return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
                            }
                            if (spring2.isResizable(1)) {
                                return Component.BaselineResizeBehavior.OTHER;
                            }
                        }
                    } else if (baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
                        for (int size2 = this.springs.size() - 1; size2 >= 0; size2--) {
                            Spring spring3 = this.springs.get(size2);
                            if (spring3 == this.baselineSpring) {
                                return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
                            }
                            if (spring3.isResizable(1)) {
                                return Component.BaselineResizeBehavior.OTHER;
                            }
                        }
                    }
                }
                return Component.BaselineResizeBehavior.OTHER;
            }
            return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
        }

        private void checkPreferredGapValues(int i2, int i3) {
            if ((i2 < 0 && i2 != -1 && i2 != -2) || ((i3 < 0 && i3 != -1 && i3 != -2) || (i2 >= 0 && i3 >= 0 && i2 > i3))) {
                throw new IllegalArgumentException("Pref and max must be either DEFAULT_SIZE, PREFERRED_SIZE, or >= 0 and pref <= max");
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$SpringDelta.class */
    private static final class SpringDelta implements Comparable<SpringDelta> {
        public final int index;
        public int delta;

        public SpringDelta(int i2, int i3) {
            this.index = i2;
            this.delta = i3;
        }

        @Override // java.lang.Comparable
        public int compareTo(SpringDelta springDelta) {
            return this.delta - springDelta.delta;
        }

        public String toString() {
            return super.toString() + "[index=" + this.index + ", delta=" + this.delta + "]";
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$ParallelGroup.class */
    public class ParallelGroup extends Group {
        private final Alignment childAlignment;
        private final boolean resizable;

        ParallelGroup(Alignment alignment, boolean z2) {
            super();
            this.childAlignment = alignment;
            this.resizable = z2;
        }

        @Override // javax.swing.GroupLayout.Group
        public ParallelGroup addGroup(Group group) {
            return (ParallelGroup) super.addGroup(group);
        }

        @Override // javax.swing.GroupLayout.Group
        public ParallelGroup addComponent(Component component) {
            return (ParallelGroup) super.addComponent(component);
        }

        @Override // javax.swing.GroupLayout.Group
        public ParallelGroup addComponent(Component component, int i2, int i3, int i4) {
            return (ParallelGroup) super.addComponent(component, i2, i3, i4);
        }

        @Override // javax.swing.GroupLayout.Group
        public ParallelGroup addGap(int i2) {
            return (ParallelGroup) super.addGap(i2);
        }

        @Override // javax.swing.GroupLayout.Group
        public ParallelGroup addGap(int i2, int i3, int i4) {
            return (ParallelGroup) super.addGap(i2, i3, i4);
        }

        public ParallelGroup addGroup(Alignment alignment, Group group) {
            checkChildAlignment(alignment);
            group.setAlignment(alignment);
            return (ParallelGroup) addSpring(group);
        }

        public ParallelGroup addComponent(Component component, Alignment alignment) {
            return addComponent(component, alignment, -1, -1, -1);
        }

        public ParallelGroup addComponent(Component component, Alignment alignment, int i2, int i3, int i4) {
            checkChildAlignment(alignment);
            ComponentSpring componentSpring = new ComponentSpring(component, i2, i3, i4);
            componentSpring.setAlignment(alignment);
            return (ParallelGroup) addSpring(componentSpring);
        }

        boolean isResizable() {
            return this.resizable;
        }

        @Override // javax.swing.GroupLayout.Group
        int operator(int i2, int i3) {
            return Math.max(i2, i3);
        }

        @Override // javax.swing.GroupLayout.Group, javax.swing.GroupLayout.Spring
        int calculateMinimumSize(int i2) {
            if (!isResizable()) {
                return getPreferredSize(i2);
            }
            return super.calculateMinimumSize(i2);
        }

        @Override // javax.swing.GroupLayout.Group, javax.swing.GroupLayout.Spring
        int calculateMaximumSize(int i2) {
            if (!isResizable()) {
                return getPreferredSize(i2);
            }
            return super.calculateMaximumSize(i2);
        }

        @Override // javax.swing.GroupLayout.Group
        void setValidSize(int i2, int i3, int i4) {
            Iterator<Spring> it = this.springs.iterator();
            while (it.hasNext()) {
                setChildSize(it.next(), i2, i3, i4);
            }
        }

        void setChildSize(Spring spring, int i2, int i3, int i4) {
            Alignment alignment = spring.getAlignment();
            int iMin = Math.min(Math.max(spring.getMinimumSize(i2), i4), spring.getMaximumSize(i2));
            if (alignment == null) {
                alignment = this.childAlignment;
            }
            switch (alignment) {
                case TRAILING:
                    spring.setSize(i2, (i3 + i4) - iMin, iMin);
                    break;
                case CENTER:
                    spring.setSize(i2, i3 + ((i4 - iMin) / 2), iMin);
                    break;
                default:
                    spring.setSize(i2, i3, iMin);
                    break;
            }
        }

        @Override // javax.swing.GroupLayout.Group
        void insertAutopadding(int i2, List<AutoPreferredGapSpring> list, List<AutoPreferredGapSpring> list2, List<ComponentSpring> list3, List<ComponentSpring> list4, boolean z2) {
            for (Spring spring : this.springs) {
                if (spring instanceof ComponentSpring) {
                    if (((ComponentSpring) spring).isVisible()) {
                        Iterator<AutoPreferredGapSpring> it = list.iterator();
                        while (it.hasNext()) {
                            it.next().addTarget((ComponentSpring) spring, i2);
                        }
                        list4.add((ComponentSpring) spring);
                    }
                } else if (spring instanceof Group) {
                    ((Group) spring).insertAutopadding(i2, list, list2, list3, list4, z2);
                } else if (spring instanceof AutoPreferredGapSpring) {
                    ((AutoPreferredGapSpring) spring).setSources(list3);
                    list2.add((AutoPreferredGapSpring) spring);
                }
            }
        }

        private void checkChildAlignment(Alignment alignment) {
            checkChildAlignment(alignment, this instanceof BaselineGroup);
        }

        private void checkChildAlignment(Alignment alignment, boolean z2) {
            if (alignment == null) {
                throw new IllegalArgumentException("Alignment must be non-null");
            }
            if (!z2 && alignment == Alignment.BASELINE) {
                throw new IllegalArgumentException("Alignment must be one of:LEADING, TRAILING or CENTER");
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$BaselineGroup.class */
    private class BaselineGroup extends ParallelGroup {
        private boolean allSpringsHaveBaseline;
        private int prefAscent;
        private int prefDescent;
        private boolean baselineAnchorSet;
        private boolean baselineAnchoredToTop;
        private boolean calcedBaseline;

        BaselineGroup(boolean z2) {
            super(Alignment.LEADING, z2);
            this.prefDescent = -1;
            this.prefAscent = -1;
            this.calcedBaseline = false;
        }

        BaselineGroup(GroupLayout groupLayout, boolean z2, boolean z3) {
            this(z2);
            this.baselineAnchoredToTop = z3;
            this.baselineAnchorSet = true;
        }

        @Override // javax.swing.GroupLayout.Spring
        void unset() {
            super.unset();
            this.prefDescent = -1;
            this.prefAscent = -1;
            this.calcedBaseline = false;
        }

        @Override // javax.swing.GroupLayout.ParallelGroup, javax.swing.GroupLayout.Group
        void setValidSize(int i2, int i3, int i4) {
            checkAxis(i2);
            if (this.prefAscent == -1) {
                super.setValidSize(i2, i3, i4);
            } else {
                baselineLayout(i3, i4);
            }
        }

        @Override // javax.swing.GroupLayout.Group
        int calculateSize(int i2, int i3) {
            checkAxis(i2);
            if (!this.calcedBaseline) {
                calculateBaselineAndResizeBehavior();
            }
            if (i3 == 0) {
                return calculateMinSize();
            }
            if (i3 == 2) {
                return calculateMaxSize();
            }
            if (this.allSpringsHaveBaseline) {
                return this.prefAscent + this.prefDescent;
            }
            return Math.max(this.prefAscent + this.prefDescent, super.calculateSize(i2, i3));
        }

        private void calculateBaselineAndResizeBehavior() {
            this.prefAscent = 0;
            this.prefDescent = 0;
            int i2 = 0;
            Component.BaselineResizeBehavior baselineResizeBehavior = null;
            for (Spring spring : this.springs) {
                if (spring.getAlignment() == null || spring.getAlignment() == Alignment.BASELINE) {
                    int baseline = spring.getBaseline();
                    if (baseline >= 0) {
                        if (spring.isResizable(1)) {
                            Component.BaselineResizeBehavior baselineResizeBehavior2 = spring.getBaselineResizeBehavior();
                            if (baselineResizeBehavior == null) {
                                baselineResizeBehavior = baselineResizeBehavior2;
                            } else if (baselineResizeBehavior2 != baselineResizeBehavior) {
                                baselineResizeBehavior = Component.BaselineResizeBehavior.CONSTANT_ASCENT;
                            }
                        }
                        this.prefAscent = Math.max(this.prefAscent, baseline);
                        this.prefDescent = Math.max(this.prefDescent, spring.getPreferredSize(1) - baseline);
                        i2++;
                    }
                }
            }
            if (!this.baselineAnchorSet) {
                if (baselineResizeBehavior == Component.BaselineResizeBehavior.CONSTANT_DESCENT) {
                    this.baselineAnchoredToTop = false;
                } else {
                    this.baselineAnchoredToTop = true;
                }
            }
            this.allSpringsHaveBaseline = i2 == this.springs.size();
            this.calcedBaseline = true;
        }

        private int calculateMaxSize() {
            int baseline;
            int iMax = this.prefAscent;
            int iMax2 = this.prefDescent;
            int iMax3 = 0;
            for (Spring spring : this.springs) {
                int maximumSize = spring.getMaximumSize(1);
                if ((spring.getAlignment() == null || spring.getAlignment() == Alignment.BASELINE) && (baseline = spring.getBaseline()) >= 0) {
                    int preferredSize = spring.getPreferredSize(1);
                    if (preferredSize != maximumSize) {
                        switch (spring.getBaselineResizeBehavior()) {
                            case CONSTANT_ASCENT:
                                if (this.baselineAnchoredToTop) {
                                    iMax2 = Math.max(iMax2, maximumSize - baseline);
                                    break;
                                } else {
                                    break;
                                }
                            case CONSTANT_DESCENT:
                                if (this.baselineAnchoredToTop) {
                                    break;
                                } else {
                                    iMax = Math.max(iMax, (maximumSize - preferredSize) + baseline);
                                    break;
                                }
                        }
                    }
                } else {
                    iMax3 = Math.max(iMax3, maximumSize);
                }
            }
            return Math.max(iMax3, iMax + iMax2);
        }

        private int calculateMinSize() {
            int baseline;
            int iMax = 0;
            int iMax2 = 0;
            int iMax3 = 0;
            if (this.baselineAnchoredToTop) {
                iMax = this.prefAscent;
            } else {
                iMax2 = this.prefDescent;
            }
            for (Spring spring : this.springs) {
                int minimumSize = spring.getMinimumSize(1);
                if ((spring.getAlignment() == null || spring.getAlignment() == Alignment.BASELINE) && (baseline = spring.getBaseline()) >= 0) {
                    int preferredSize = spring.getPreferredSize(1);
                    switch (spring.getBaselineResizeBehavior()) {
                        case CONSTANT_ASCENT:
                            if (this.baselineAnchoredToTop) {
                                iMax2 = Math.max(minimumSize - baseline, iMax2);
                                break;
                            } else {
                                iMax = Math.max(baseline, iMax);
                                break;
                            }
                        case CONSTANT_DESCENT:
                            if (!this.baselineAnchoredToTop) {
                                iMax = Math.max(baseline - (preferredSize - minimumSize), iMax);
                                break;
                            } else {
                                iMax2 = Math.max(preferredSize - baseline, iMax2);
                                break;
                            }
                        default:
                            iMax = Math.max(baseline, iMax);
                            iMax2 = Math.max(preferredSize - baseline, iMax2);
                            break;
                    }
                } else {
                    iMax3 = Math.max(iMax3, minimumSize);
                }
            }
            return Math.max(iMax3, iMax + iMax2);
        }

        private void baselineLayout(int i2, int i3) {
            int i4;
            int i5;
            int i6;
            if (this.baselineAnchoredToTop) {
                i4 = this.prefAscent;
                i5 = i3 - i4;
            } else {
                i4 = i3 - this.prefDescent;
                i5 = this.prefDescent;
            }
            for (Spring spring : this.springs) {
                Alignment alignment = spring.getAlignment();
                if (alignment == null || alignment == Alignment.BASELINE) {
                    int baseline = spring.getBaseline();
                    if (baseline >= 0) {
                        int maximumSize = spring.getMaximumSize(1);
                        int preferredSize = spring.getPreferredSize(1);
                        int iMin = preferredSize;
                        switch (spring.getBaselineResizeBehavior()) {
                            case CONSTANT_ASCENT:
                                i6 = (i2 + i4) - baseline;
                                iMin = Math.min(i5, maximumSize - baseline) + baseline;
                                break;
                            case CONSTANT_DESCENT:
                                iMin = Math.min(i4, (maximumSize - preferredSize) + baseline) + (preferredSize - baseline);
                                i6 = ((i2 + i4) + (preferredSize - baseline)) - iMin;
                                break;
                            default:
                                i6 = (i2 + i4) - baseline;
                                break;
                        }
                        spring.setSize(1, i6, iMin);
                    } else {
                        setChildSize(spring, 1, i2, i3);
                    }
                } else {
                    setChildSize(spring, 1, i2, i3);
                }
            }
        }

        @Override // javax.swing.GroupLayout.Spring
        int getBaseline() {
            if (this.springs.size() > 1) {
                getPreferredSize(1);
                return this.prefAscent;
            }
            if (this.springs.size() == 1) {
                return this.springs.get(0).getBaseline();
            }
            return -1;
        }

        @Override // javax.swing.GroupLayout.Spring
        Component.BaselineResizeBehavior getBaselineResizeBehavior() {
            if (this.springs.size() == 1) {
                return this.springs.get(0).getBaselineResizeBehavior();
            }
            if (this.baselineAnchoredToTop) {
                return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
            }
            return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
        }

        private void checkAxis(int i2) {
            if (i2 == 0) {
                throw new IllegalStateException("Baseline must be used along vertical axis");
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$ComponentSpring.class */
    private final class ComponentSpring extends Spring {
        private Component component;
        private int origin;
        private final int min;
        private final int pref;
        private final int max;
        private int baseline;
        private boolean installed;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !GroupLayout.class.desiredAssertionStatus();
        }

        private ComponentSpring(Component component, int i2, int i3, int i4) {
            super();
            this.baseline = -1;
            this.component = component;
            if (component != null) {
                GroupLayout.checkSize(i2, i3, i4, true);
                this.min = i2;
                this.max = i4;
                this.pref = i3;
                GroupLayout.this.getComponentInfo(component);
                return;
            }
            throw new IllegalArgumentException("Component must be non-null");
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMinimumSize(int i2) {
            if (isLinked(i2)) {
                return getLinkSize(i2, 0);
            }
            return calculateNonlinkedMinimumSize(i2);
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculatePreferredSize(int i2) {
            if (isLinked(i2)) {
                return getLinkSize(i2, 1);
            }
            return Math.min(getMaximumSize(i2), Math.max(getMinimumSize(i2), calculateNonlinkedPreferredSize(i2)));
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMaximumSize(int i2) {
            if (isLinked(i2)) {
                return getLinkSize(i2, 2);
            }
            return Math.max(getMinimumSize(i2), calculateNonlinkedMaximumSize(i2));
        }

        boolean isVisible() {
            return GroupLayout.this.getComponentInfo(getComponent()).isVisible();
        }

        int calculateNonlinkedMinimumSize(int i2) {
            if (!isVisible()) {
                return 0;
            }
            if (this.min >= 0) {
                return this.min;
            }
            if (this.min == -2) {
                return calculateNonlinkedPreferredSize(i2);
            }
            if ($assertionsDisabled || this.min == -1) {
                return getSizeAlongAxis(i2, this.component.getMinimumSize());
            }
            throw new AssertionError();
        }

        int calculateNonlinkedPreferredSize(int i2) {
            if (!isVisible()) {
                return 0;
            }
            if (this.pref >= 0) {
                return this.pref;
            }
            if ($assertionsDisabled || this.pref == -1 || this.pref == -2) {
                return getSizeAlongAxis(i2, this.component.getPreferredSize());
            }
            throw new AssertionError();
        }

        int calculateNonlinkedMaximumSize(int i2) {
            if (!isVisible()) {
                return 0;
            }
            if (this.max >= 0) {
                return this.max;
            }
            if (this.max == -2) {
                return calculateNonlinkedPreferredSize(i2);
            }
            if ($assertionsDisabled || this.max == -1) {
                return getSizeAlongAxis(i2, this.component.getMaximumSize());
            }
            throw new AssertionError();
        }

        private int getSizeAlongAxis(int i2, Dimension dimension) {
            return i2 == 0 ? dimension.width : dimension.height;
        }

        private int getLinkSize(int i2, int i3) {
            if (!isVisible()) {
                return 0;
            }
            return GroupLayout.this.getComponentInfo(this.component).getLinkSize(i2, i3);
        }

        @Override // javax.swing.GroupLayout.Spring
        void setSize(int i2, int i3, int i4) {
            super.setSize(i2, i3, i4);
            this.origin = i3;
            if (i4 == Integer.MIN_VALUE) {
                this.baseline = -1;
            }
        }

        int getOrigin() {
            return this.origin;
        }

        void setComponent(Component component) {
            this.component = component;
        }

        Component getComponent() {
            return this.component;
        }

        @Override // javax.swing.GroupLayout.Spring
        int getBaseline() {
            if (this.baseline == -1) {
                int preferredSize = GroupLayout.this.getComponentInfo(this.component).horizontalSpring.getPreferredSize(0);
                int preferredSize2 = getPreferredSize(1);
                if (preferredSize > 0 && preferredSize2 > 0) {
                    this.baseline = this.component.getBaseline(preferredSize, preferredSize2);
                }
            }
            return this.baseline;
        }

        @Override // javax.swing.GroupLayout.Spring
        Component.BaselineResizeBehavior getBaselineResizeBehavior() {
            return getComponent().getBaselineResizeBehavior();
        }

        private boolean isLinked(int i2) {
            return GroupLayout.this.getComponentInfo(this.component).isLinked(i2);
        }

        void installIfNecessary(int i2) {
            if (!this.installed) {
                this.installed = true;
                if (i2 == 0) {
                    GroupLayout.this.getComponentInfo(this.component).horizontalSpring = this;
                } else {
                    GroupLayout.this.getComponentInfo(this.component).verticalSpring = this;
                }
            }
        }

        @Override // javax.swing.GroupLayout.Spring
        boolean willHaveZeroSize(boolean z2) {
            return !isVisible();
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$PreferredGapSpring.class */
    private class PreferredGapSpring extends Spring {
        private final JComponent source;
        private final JComponent target;
        private final LayoutStyle.ComponentPlacement type;
        private final int pref;
        private final int max;

        PreferredGapSpring(JComponent jComponent, JComponent jComponent2, LayoutStyle.ComponentPlacement componentPlacement, int i2, int i3) {
            super();
            this.source = jComponent;
            this.target = jComponent2;
            this.type = componentPlacement;
            this.pref = i2;
            this.max = i3;
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMinimumSize(int i2) {
            return getPadding(i2);
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculatePreferredSize(int i2) {
            if (this.pref == -1 || this.pref == -2) {
                return getMinimumSize(i2);
            }
            return Math.min(getMaximumSize(i2), Math.max(getMinimumSize(i2), this.pref));
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMaximumSize(int i2) {
            if (this.max == -2 || this.max == -1) {
                return getPadding(i2);
            }
            return Math.max(getMinimumSize(i2), this.max);
        }

        private int getPadding(int i2) {
            int i3;
            if (i2 == 0) {
                i3 = 3;
            } else {
                i3 = 5;
            }
            return GroupLayout.this.getLayoutStyle0().getPreferredGap(this.source, this.target, this.type, i3, GroupLayout.this.host);
        }

        @Override // javax.swing.GroupLayout.Spring
        boolean willHaveZeroSize(boolean z2) {
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$GapSpring.class */
    private class GapSpring extends Spring {
        private final int min;
        private final int pref;
        private final int max;

        GapSpring(int i2, int i3, int i4) {
            super();
            GroupLayout.checkSize(i2, i3, i4, false);
            this.min = i2;
            this.pref = i3;
            this.max = i4;
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMinimumSize(int i2) {
            if (this.min == -2) {
                return getPreferredSize(i2);
            }
            return this.min;
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculatePreferredSize(int i2) {
            return this.pref;
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMaximumSize(int i2) {
            if (this.max == -2) {
                return getPreferredSize(i2);
            }
            return this.max;
        }

        @Override // javax.swing.GroupLayout.Spring
        boolean willHaveZeroSize(boolean z2) {
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$AutoPreferredGapSpring.class */
    private class AutoPreferredGapSpring extends Spring {
        List<ComponentSpring> sources;
        ComponentSpring source;
        private List<AutoPreferredGapMatch> matches;
        int size;
        int lastSize;
        private final int pref;
        private final int max;
        private LayoutStyle.ComponentPlacement type;
        private boolean userCreated;

        private AutoPreferredGapSpring() {
            super();
            this.pref = -2;
            this.max = -2;
            this.type = LayoutStyle.ComponentPlacement.RELATED;
        }

        AutoPreferredGapSpring(int i2, int i3) {
            super();
            this.pref = i2;
            this.max = i3;
        }

        AutoPreferredGapSpring(LayoutStyle.ComponentPlacement componentPlacement, int i2, int i3) {
            super();
            this.type = componentPlacement;
            this.pref = i2;
            this.max = i3;
            this.userCreated = true;
        }

        public void setSource(ComponentSpring componentSpring) {
            this.source = componentSpring;
        }

        public void setSources(List<ComponentSpring> list) {
            this.sources = new ArrayList(list);
        }

        public void setUserCreated(boolean z2) {
            this.userCreated = z2;
        }

        public boolean getUserCreated() {
            return this.userCreated;
        }

        @Override // javax.swing.GroupLayout.Spring
        void unset() {
            this.lastSize = getSize();
            super.unset();
            this.size = 0;
        }

        public void reset() {
            this.size = 0;
            this.sources = null;
            this.source = null;
            this.matches = null;
        }

        public void calculatePadding(int i2) {
            int i3;
            this.size = Integer.MIN_VALUE;
            int iMax = Integer.MIN_VALUE;
            if (this.matches != null) {
                LayoutStyle layoutStyle0 = GroupLayout.this.getLayoutStyle0();
                if (i2 == 0) {
                    if (GroupLayout.this.isLeftToRight()) {
                        i3 = 3;
                    } else {
                        i3 = 7;
                    }
                } else {
                    i3 = 5;
                }
                for (int size = this.matches.size() - 1; size >= 0; size--) {
                    AutoPreferredGapMatch autoPreferredGapMatch = this.matches.get(size);
                    iMax = Math.max(iMax, calculatePadding(layoutStyle0, i3, autoPreferredGapMatch.source, autoPreferredGapMatch.target));
                }
            }
            if (this.size == Integer.MIN_VALUE) {
                this.size = 0;
            }
            if (iMax == Integer.MIN_VALUE) {
                iMax = 0;
            }
            if (this.lastSize != Integer.MIN_VALUE) {
                this.size += Math.min(iMax, this.lastSize);
            }
        }

        private int calculatePadding(LayoutStyle layoutStyle, int i2, ComponentSpring componentSpring, ComponentSpring componentSpring2) {
            int preferredGap;
            int origin = componentSpring2.getOrigin() - (componentSpring.getOrigin() + componentSpring.getSize());
            if (origin >= 0) {
                if ((componentSpring.getComponent() instanceof JComponent) && (componentSpring2.getComponent() instanceof JComponent)) {
                    preferredGap = layoutStyle.getPreferredGap((JComponent) componentSpring.getComponent(), (JComponent) componentSpring2.getComponent(), this.type, i2, GroupLayout.this.host);
                } else {
                    preferredGap = 10;
                }
                if (preferredGap > origin) {
                    this.size = Math.max(this.size, preferredGap - origin);
                }
                return preferredGap;
            }
            return 0;
        }

        public void addTarget(ComponentSpring componentSpring, int i2) {
            int i3 = i2 == 0 ? 1 : 0;
            if (this.source != null) {
                if (GroupLayout.this.areParallelSiblings(this.source.getComponent(), componentSpring.getComponent(), i3)) {
                    addValidTarget(this.source, componentSpring);
                    return;
                }
                return;
            }
            Component component = componentSpring.getComponent();
            for (int size = this.sources.size() - 1; size >= 0; size--) {
                ComponentSpring componentSpring2 = this.sources.get(size);
                if (GroupLayout.this.areParallelSiblings(componentSpring2.getComponent(), component, i3)) {
                    addValidTarget(componentSpring2, componentSpring);
                }
            }
        }

        private void addValidTarget(ComponentSpring componentSpring, ComponentSpring componentSpring2) {
            if (this.matches == null) {
                this.matches = new ArrayList(1);
            }
            this.matches.add(new AutoPreferredGapMatch(componentSpring, componentSpring2));
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMinimumSize(int i2) {
            return this.size;
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculatePreferredSize(int i2) {
            if (this.pref == -2 || this.pref == -1) {
                return this.size;
            }
            return Math.max(this.size, this.pref);
        }

        @Override // javax.swing.GroupLayout.Spring
        int calculateMaximumSize(int i2) {
            if (this.max >= 0) {
                return Math.max(getPreferredSize(i2), this.max);
            }
            return this.size;
        }

        String getMatchDescription() {
            return this.matches == null ? "" : this.matches.toString();
        }

        public String toString() {
            return super.toString() + getMatchDescription();
        }

        @Override // javax.swing.GroupLayout.Spring
        boolean willHaveZeroSize(boolean z2) {
            return z2;
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$AutoPreferredGapMatch.class */
    private static final class AutoPreferredGapMatch {
        public final ComponentSpring source;
        public final ComponentSpring target;

        AutoPreferredGapMatch(ComponentSpring componentSpring, ComponentSpring componentSpring2) {
            this.source = componentSpring;
            this.target = componentSpring2;
        }

        private String toString(ComponentSpring componentSpring) {
            return componentSpring.getComponent().getName();
        }

        public String toString() {
            return "[" + toString(this.source) + LanguageTag.SEP + toString(this.target) + "]";
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$ContainerAutoPreferredGapSpring.class */
    private class ContainerAutoPreferredGapSpring extends AutoPreferredGapSpring {
        private List<ComponentSpring> targets;

        ContainerAutoPreferredGapSpring() {
            super();
            setUserCreated(true);
        }

        ContainerAutoPreferredGapSpring(int i2, int i3) {
            super(i2, i3);
            setUserCreated(true);
        }

        @Override // javax.swing.GroupLayout.AutoPreferredGapSpring
        public void addTarget(ComponentSpring componentSpring, int i2) {
            if (this.targets == null) {
                this.targets = new ArrayList(1);
            }
            this.targets.add(componentSpring);
        }

        @Override // javax.swing.GroupLayout.AutoPreferredGapSpring
        public void calculatePadding(int i2) {
            int i3;
            int i4;
            LayoutStyle layoutStyle0 = GroupLayout.this.getLayoutStyle0();
            int iUpdateSize = 0;
            this.size = 0;
            if (this.targets != null) {
                if (i2 == 0) {
                    if (GroupLayout.this.isLeftToRight()) {
                        i4 = 7;
                    } else {
                        i4 = 3;
                    }
                } else {
                    i4 = 5;
                }
                for (int size = this.targets.size() - 1; size >= 0; size--) {
                    ComponentSpring componentSpring = this.targets.get(size);
                    int origin = 10;
                    if (componentSpring.getComponent() instanceof JComponent) {
                        int containerGap = layoutStyle0.getContainerGap((JComponent) componentSpring.getComponent(), i4, GroupLayout.this.host);
                        iUpdateSize = Math.max(containerGap, iUpdateSize);
                        origin = containerGap - componentSpring.getOrigin();
                    } else {
                        iUpdateSize = Math.max(10, iUpdateSize);
                    }
                    this.size = Math.max(this.size, origin);
                }
            } else {
                if (i2 == 0) {
                    if (GroupLayout.this.isLeftToRight()) {
                        i3 = 3;
                    } else {
                        i3 = 7;
                    }
                } else {
                    i3 = 5;
                }
                if (this.sources != null) {
                    for (int size2 = this.sources.size() - 1; size2 >= 0; size2--) {
                        iUpdateSize = Math.max(iUpdateSize, updateSize(layoutStyle0, this.sources.get(size2), i3));
                    }
                } else if (this.source != null) {
                    iUpdateSize = updateSize(layoutStyle0, this.source, i3);
                }
            }
            if (this.lastSize != Integer.MIN_VALUE) {
                this.size += Math.min(iUpdateSize, this.lastSize);
            }
        }

        private int updateSize(LayoutStyle layoutStyle, ComponentSpring componentSpring, int i2) {
            int containerGap = 10;
            if (componentSpring.getComponent() instanceof JComponent) {
                containerGap = layoutStyle.getContainerGap((JComponent) componentSpring.getComponent(), i2, GroupLayout.this.host);
            }
            this.size = Math.max(this.size, containerGap - Math.max(0, (getParent().getSize() - componentSpring.getSize()) - componentSpring.getOrigin()));
            return containerGap;
        }

        @Override // javax.swing.GroupLayout.AutoPreferredGapSpring
        String getMatchDescription() {
            if (this.targets != null) {
                return "leading: " + this.targets.toString();
            }
            if (this.sources != null) {
                return "trailing: " + this.sources.toString();
            }
            return "--";
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$LinkInfo.class */
    private static class LinkInfo {
        private final int axis;
        private final List<ComponentInfo> linked = new ArrayList();
        private int size = Integer.MIN_VALUE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !GroupLayout.class.desiredAssertionStatus();
        }

        LinkInfo(int i2) {
            this.axis = i2;
        }

        public void add(ComponentInfo componentInfo) {
            LinkInfo linkInfo = componentInfo.getLinkInfo(this.axis, false);
            if (linkInfo == null) {
                this.linked.add(componentInfo);
                componentInfo.setLinkInfo(this.axis, this);
            } else if (linkInfo != this) {
                this.linked.addAll(linkInfo.linked);
                Iterator<ComponentInfo> it = linkInfo.linked.iterator();
                while (it.hasNext()) {
                    it.next().setLinkInfo(this.axis, this);
                }
            }
            clearCachedSize();
        }

        public void remove(ComponentInfo componentInfo) {
            this.linked.remove(componentInfo);
            componentInfo.setLinkInfo(this.axis, null);
            if (this.linked.size() == 1) {
                this.linked.get(0).setLinkInfo(this.axis, null);
            }
            clearCachedSize();
        }

        public void clearCachedSize() {
            this.size = Integer.MIN_VALUE;
        }

        public int getSize(int i2) {
            if (this.size == Integer.MIN_VALUE) {
                this.size = calculateLinkedSize(i2);
            }
            return this.size;
        }

        private int calculateLinkedSize(int i2) {
            ComponentSpring componentSpring;
            int iMax = 0;
            for (ComponentInfo componentInfo : this.linked) {
                if (i2 == 0) {
                    componentSpring = componentInfo.horizontalSpring;
                } else {
                    if (!$assertionsDisabled && i2 != 1) {
                        throw new AssertionError();
                    }
                    componentSpring = componentInfo.verticalSpring;
                }
                iMax = Math.max(iMax, componentSpring.calculateNonlinkedPreferredSize(i2));
            }
            return iMax;
        }
    }

    /* loaded from: rt.jar:javax/swing/GroupLayout$ComponentInfo.class */
    private class ComponentInfo {
        private Component component;
        ComponentSpring horizontalSpring;
        ComponentSpring verticalSpring;
        private LinkInfo horizontalMaster;
        private LinkInfo verticalMaster;
        private boolean visible;
        private Boolean honorsVisibility;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !GroupLayout.class.desiredAssertionStatus();
        }

        ComponentInfo(Component component) {
            this.component = component;
            updateVisibility();
        }

        public void dispose() {
            removeSpring(this.horizontalSpring);
            this.horizontalSpring = null;
            removeSpring(this.verticalSpring);
            this.verticalSpring = null;
            if (this.horizontalMaster != null) {
                this.horizontalMaster.remove(this);
            }
            if (this.verticalMaster != null) {
                this.verticalMaster.remove(this);
            }
        }

        void setHonorsVisibility(Boolean bool) {
            this.honorsVisibility = bool;
        }

        private void removeSpring(Spring spring) {
            if (spring != null) {
                ((Group) spring.getParent()).springs.remove(spring);
            }
        }

        public boolean isVisible() {
            return this.visible;
        }

        boolean updateVisibility() {
            boolean zBooleanValue;
            if (this.honorsVisibility == null) {
                zBooleanValue = GroupLayout.this.getHonorsVisibility();
            } else {
                zBooleanValue = this.honorsVisibility.booleanValue();
            }
            boolean zIsVisible = zBooleanValue ? this.component.isVisible() : true;
            if (this.visible != zIsVisible) {
                this.visible = zIsVisible;
                return true;
            }
            return false;
        }

        public void setBounds(Insets insets, int i2, boolean z2) {
            int origin = this.horizontalSpring.getOrigin();
            int size = this.horizontalSpring.getSize();
            int origin2 = this.verticalSpring.getOrigin();
            int size2 = this.verticalSpring.getSize();
            if (!z2) {
                origin = (i2 - origin) - size;
            }
            this.component.setBounds(origin + insets.left, origin2 + insets.top, size, size2);
        }

        public void setComponent(Component component) {
            this.component = component;
            if (this.horizontalSpring != null) {
                this.horizontalSpring.setComponent(component);
            }
            if (this.verticalSpring != null) {
                this.verticalSpring.setComponent(component);
            }
        }

        public Component getComponent() {
            return this.component;
        }

        public boolean isLinked(int i2) {
            if (i2 == 0) {
                return this.horizontalMaster != null;
            }
            if ($assertionsDisabled || i2 == 1) {
                return this.verticalMaster != null;
            }
            throw new AssertionError();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLinkInfo(int i2, LinkInfo linkInfo) {
            if (i2 == 0) {
                this.horizontalMaster = linkInfo;
            } else {
                if (!$assertionsDisabled && i2 != 1) {
                    throw new AssertionError();
                }
                this.verticalMaster = linkInfo;
            }
        }

        public LinkInfo getLinkInfo(int i2) {
            return getLinkInfo(i2, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public LinkInfo getLinkInfo(int i2, boolean z2) {
            if (i2 == 0) {
                if (this.horizontalMaster == null && z2) {
                    new LinkInfo(0).add(this);
                }
                return this.horizontalMaster;
            }
            if (!$assertionsDisabled && i2 != 1) {
                throw new AssertionError();
            }
            if (this.verticalMaster == null && z2) {
                new LinkInfo(1).add(this);
            }
            return this.verticalMaster;
        }

        public void clearCachedSize() {
            if (this.horizontalMaster != null) {
                this.horizontalMaster.clearCachedSize();
            }
            if (this.verticalMaster != null) {
                this.verticalMaster.clearCachedSize();
            }
        }

        int getLinkSize(int i2, int i3) {
            if (i2 == 0) {
                return this.horizontalMaster.getSize(i2);
            }
            if ($assertionsDisabled || i2 == 1) {
                return this.verticalMaster.getSize(i2);
            }
            throw new AssertionError();
        }
    }
}
