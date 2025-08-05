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
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Spring;

/* loaded from: rt.jar:javax/swing/SpringLayout.class */
public class SpringLayout implements LayoutManager2 {
    private Map<Component, Constraints> componentConstraints = new HashMap();
    private Spring cyclicReference = Spring.constant(Integer.MIN_VALUE);
    private Set<Spring> cyclicSprings;
    private Set<Spring> acyclicSprings;
    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String WIDTH = "Width";
    public static final String HEIGHT = "Height";
    public static final String HORIZONTAL_CENTER = "HorizontalCenter";
    private static String[] ALL_HORIZONTAL = {"West", "Width", "East", HORIZONTAL_CENTER};
    public static final String VERTICAL_CENTER = "VerticalCenter";
    public static final String BASELINE = "Baseline";
    private static String[] ALL_VERTICAL = {"North", "Height", "South", VERTICAL_CENTER, BASELINE};

    /* loaded from: rt.jar:javax/swing/SpringLayout$Constraints.class */
    public static class Constraints {

        /* renamed from: x, reason: collision with root package name */
        private Spring f12809x;

        /* renamed from: y, reason: collision with root package name */
        private Spring f12810y;
        private Spring width;
        private Spring height;
        private Spring east;
        private Spring south;
        private Spring horizontalCenter;
        private Spring verticalCenter;
        private Spring baseline;
        private List<String> horizontalHistory = new ArrayList(2);
        private List<String> verticalHistory = new ArrayList(2);

        /* renamed from: c, reason: collision with root package name */
        private Component f12811c;

        public Constraints() {
        }

        public Constraints(Spring spring, Spring spring2) {
            setX(spring);
            setY(spring2);
        }

        public Constraints(Spring spring, Spring spring2, Spring spring3, Spring spring4) {
            setX(spring);
            setY(spring2);
            setWidth(spring3);
            setHeight(spring4);
        }

        public Constraints(Component component) {
            this.f12811c = component;
            setX(Spring.constant(component.getX()));
            setY(Spring.constant(component.getY()));
            setWidth(Spring.width(component));
            setHeight(Spring.height(component));
        }

        private void pushConstraint(String str, Spring spring, boolean z2) {
            boolean z3 = true;
            List<String> list = z2 ? this.horizontalHistory : this.verticalHistory;
            if (list.contains(str)) {
                list.remove(str);
                z3 = false;
            } else if (list.size() == 2 && spring != null) {
                list.remove(0);
                z3 = false;
            }
            if (spring != null) {
                list.add(str);
            }
            if (!z3) {
                for (String str2 : z2 ? SpringLayout.ALL_HORIZONTAL : SpringLayout.ALL_VERTICAL) {
                    if (!list.contains(str2)) {
                        setConstraint(str2, null);
                    }
                }
            }
        }

        private Spring sum(Spring spring, Spring spring2) {
            if (spring == null || spring2 == null) {
                return null;
            }
            return Spring.sum(spring, spring2);
        }

        private Spring difference(Spring spring, Spring spring2) {
            if (spring == null || spring2 == null) {
                return null;
            }
            return Spring.difference(spring, spring2);
        }

        private Spring scale(Spring spring, float f2) {
            if (spring == null) {
                return null;
            }
            return Spring.scale(spring, f2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getBaselineFromHeight(int i2) {
            if (i2 < 0) {
                return -this.f12811c.getBaseline(this.f12811c.getPreferredSize().width, -i2);
            }
            return this.f12811c.getBaseline(this.f12811c.getPreferredSize().width, i2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getHeightFromBaseLine(int i2) {
            Dimension preferredSize = this.f12811c.getPreferredSize();
            int i3 = preferredSize.height;
            int baseline = this.f12811c.getBaseline(preferredSize.width, i3);
            if (baseline == i2) {
                return i3;
            }
            switch (this.f12811c.getBaselineResizeBehavior()) {
                case CONSTANT_DESCENT:
                    return i3 + (i2 - baseline);
                case CENTER_OFFSET:
                    return i3 + (2 * (i2 - baseline));
                case CONSTANT_ASCENT:
                default:
                    return Integer.MIN_VALUE;
            }
        }

        private Spring heightToRelativeBaseline(Spring spring) {
            return new Spring.SpringMap(spring) { // from class: javax.swing.SpringLayout.Constraints.1
                @Override // javax.swing.Spring.SpringMap
                protected int map(int i2) {
                    return Constraints.this.getBaselineFromHeight(i2);
                }

                @Override // javax.swing.Spring.SpringMap
                protected int inv(int i2) {
                    return Constraints.this.getHeightFromBaseLine(i2);
                }
            };
        }

        private Spring relativeBaselineToHeight(Spring spring) {
            return new Spring.SpringMap(spring) { // from class: javax.swing.SpringLayout.Constraints.2
                @Override // javax.swing.Spring.SpringMap
                protected int map(int i2) {
                    return Constraints.this.getHeightFromBaseLine(i2);
                }

                @Override // javax.swing.Spring.SpringMap
                protected int inv(int i2) {
                    return Constraints.this.getBaselineFromHeight(i2);
                }
            };
        }

        private boolean defined(List list, String str, String str2) {
            return list.contains(str) && list.contains(str2);
        }

        public void setX(Spring spring) {
            this.f12809x = spring;
            pushConstraint("West", spring, true);
        }

        public Spring getX() {
            if (this.f12809x == null) {
                if (defined(this.horizontalHistory, "East", "Width")) {
                    this.f12809x = difference(this.east, this.width);
                } else if (defined(this.horizontalHistory, SpringLayout.HORIZONTAL_CENTER, "Width")) {
                    this.f12809x = difference(this.horizontalCenter, scale(this.width, 0.5f));
                } else if (defined(this.horizontalHistory, SpringLayout.HORIZONTAL_CENTER, "East")) {
                    this.f12809x = difference(scale(this.horizontalCenter, 2.0f), this.east);
                }
            }
            return this.f12809x;
        }

        public void setY(Spring spring) {
            this.f12810y = spring;
            pushConstraint("North", spring, false);
        }

        public Spring getY() {
            if (this.f12810y == null) {
                if (defined(this.verticalHistory, "South", "Height")) {
                    this.f12810y = difference(this.south, this.height);
                } else if (defined(this.verticalHistory, SpringLayout.VERTICAL_CENTER, "Height")) {
                    this.f12810y = difference(this.verticalCenter, scale(this.height, 0.5f));
                } else if (defined(this.verticalHistory, SpringLayout.VERTICAL_CENTER, "South")) {
                    this.f12810y = difference(scale(this.verticalCenter, 2.0f), this.south);
                } else if (defined(this.verticalHistory, SpringLayout.BASELINE, "Height")) {
                    this.f12810y = difference(this.baseline, heightToRelativeBaseline(this.height));
                } else if (defined(this.verticalHistory, SpringLayout.BASELINE, "South")) {
                    this.f12810y = scale(difference(this.baseline, heightToRelativeBaseline(this.south)), 2.0f);
                }
            }
            return this.f12810y;
        }

        public void setWidth(Spring spring) {
            this.width = spring;
            pushConstraint("Width", spring, true);
        }

        public Spring getWidth() {
            if (this.width == null) {
                if (this.horizontalHistory.contains("East")) {
                    this.width = difference(this.east, getX());
                } else if (this.horizontalHistory.contains(SpringLayout.HORIZONTAL_CENTER)) {
                    this.width = scale(difference(this.horizontalCenter, getX()), 2.0f);
                }
            }
            return this.width;
        }

        public void setHeight(Spring spring) {
            this.height = spring;
            pushConstraint("Height", spring, false);
        }

        public Spring getHeight() {
            if (this.height == null) {
                if (this.verticalHistory.contains("South")) {
                    this.height = difference(this.south, getY());
                } else if (this.verticalHistory.contains(SpringLayout.VERTICAL_CENTER)) {
                    this.height = scale(difference(this.verticalCenter, getY()), 2.0f);
                } else if (this.verticalHistory.contains(SpringLayout.BASELINE)) {
                    this.height = relativeBaselineToHeight(difference(this.baseline, getY()));
                }
            }
            return this.height;
        }

        private void setEast(Spring spring) {
            this.east = spring;
            pushConstraint("East", spring, true);
        }

        private Spring getEast() {
            if (this.east == null) {
                this.east = sum(getX(), getWidth());
            }
            return this.east;
        }

        private void setSouth(Spring spring) {
            this.south = spring;
            pushConstraint("South", spring, false);
        }

        private Spring getSouth() {
            if (this.south == null) {
                this.south = sum(getY(), getHeight());
            }
            return this.south;
        }

        private Spring getHorizontalCenter() {
            if (this.horizontalCenter == null) {
                this.horizontalCenter = sum(getX(), scale(getWidth(), 0.5f));
            }
            return this.horizontalCenter;
        }

        private void setHorizontalCenter(Spring spring) {
            this.horizontalCenter = spring;
            pushConstraint(SpringLayout.HORIZONTAL_CENTER, spring, true);
        }

        private Spring getVerticalCenter() {
            if (this.verticalCenter == null) {
                this.verticalCenter = sum(getY(), scale(getHeight(), 0.5f));
            }
            return this.verticalCenter;
        }

        private void setVerticalCenter(Spring spring) {
            this.verticalCenter = spring;
            pushConstraint(SpringLayout.VERTICAL_CENTER, spring, false);
        }

        private Spring getBaseline() {
            if (this.baseline == null) {
                this.baseline = sum(getY(), heightToRelativeBaseline(getHeight()));
            }
            return this.baseline;
        }

        private void setBaseline(Spring spring) {
            this.baseline = spring;
            pushConstraint(SpringLayout.BASELINE, spring, false);
        }

        public void setConstraint(String str, Spring spring) {
            String strIntern = str.intern();
            if (strIntern == "West") {
                setX(spring);
                return;
            }
            if (strIntern == "North") {
                setY(spring);
                return;
            }
            if (strIntern == "East") {
                setEast(spring);
                return;
            }
            if (strIntern == "South") {
                setSouth(spring);
                return;
            }
            if (strIntern == SpringLayout.HORIZONTAL_CENTER) {
                setHorizontalCenter(spring);
                return;
            }
            if (strIntern == "Width") {
                setWidth(spring);
                return;
            }
            if (strIntern == "Height") {
                setHeight(spring);
            } else if (strIntern == SpringLayout.VERTICAL_CENTER) {
                setVerticalCenter(spring);
            } else if (strIntern == SpringLayout.BASELINE) {
                setBaseline(spring);
            }
        }

        public Spring getConstraint(String str) {
            String strIntern = str.intern();
            if (strIntern == "West") {
                return getX();
            }
            if (strIntern == "North") {
                return getY();
            }
            if (strIntern == "East") {
                return getEast();
            }
            if (strIntern == "South") {
                return getSouth();
            }
            if (strIntern == "Width") {
                return getWidth();
            }
            if (strIntern == "Height") {
                return getHeight();
            }
            if (strIntern == SpringLayout.HORIZONTAL_CENTER) {
                return getHorizontalCenter();
            }
            if (strIntern == SpringLayout.VERTICAL_CENTER) {
                return getVerticalCenter();
            }
            if (strIntern == SpringLayout.BASELINE) {
                return getBaseline();
            }
            return null;
        }

        void reset() {
            for (Spring spring : new Spring[]{this.f12809x, this.f12810y, this.width, this.height, this.east, this.south, this.horizontalCenter, this.verticalCenter, this.baseline}) {
                if (spring != null) {
                    spring.setValue(Integer.MIN_VALUE);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/SpringLayout$SpringProxy.class */
    private static class SpringProxy extends Spring {
        private String edgeName;

        /* renamed from: c, reason: collision with root package name */
        private Component f12812c;

        /* renamed from: l, reason: collision with root package name */
        private SpringLayout f12813l;

        public SpringProxy(String str, Component component, SpringLayout springLayout) {
            this.edgeName = str;
            this.f12812c = component;
            this.f12813l = springLayout;
        }

        private Spring getConstraint() {
            return this.f12813l.getConstraints(this.f12812c).getConstraint(this.edgeName);
        }

        @Override // javax.swing.Spring
        public int getMinimumValue() {
            return getConstraint().getMinimumValue();
        }

        @Override // javax.swing.Spring
        public int getPreferredValue() {
            return getConstraint().getPreferredValue();
        }

        @Override // javax.swing.Spring
        public int getMaximumValue() {
            return getConstraint().getMaximumValue();
        }

        @Override // javax.swing.Spring
        public int getValue() {
            return getConstraint().getValue();
        }

        @Override // javax.swing.Spring
        public void setValue(int i2) {
            getConstraint().setValue(i2);
        }

        @Override // javax.swing.Spring
        boolean isCyclic(SpringLayout springLayout) {
            return springLayout.isCyclic(getConstraint());
        }

        public String toString() {
            return "SpringProxy for " + this.edgeName + " edge of " + this.f12812c.getName() + ".";
        }
    }

    private void resetCyclicStatuses() {
        this.cyclicSprings = new HashSet();
        this.acyclicSprings = new HashSet();
    }

    private void setParent(Container container) {
        resetCyclicStatuses();
        Constraints constraints = getConstraints(container);
        constraints.setX(Spring.constant(0));
        constraints.setY(Spring.constant(0));
        Spring width = constraints.getWidth();
        if ((width instanceof Spring.WidthSpring) && ((Spring.WidthSpring) width).f12808c == container) {
            constraints.setWidth(Spring.constant(0, 0, Integer.MAX_VALUE));
        }
        Spring height = constraints.getHeight();
        if ((height instanceof Spring.HeightSpring) && ((Spring.HeightSpring) height).f12804c == container) {
            constraints.setHeight(Spring.constant(0, 0, Integer.MAX_VALUE));
        }
    }

    boolean isCyclic(Spring spring) {
        if (spring == null) {
            return false;
        }
        if (this.cyclicSprings.contains(spring)) {
            return true;
        }
        if (this.acyclicSprings.contains(spring)) {
            return false;
        }
        this.cyclicSprings.add(spring);
        boolean zIsCyclic = spring.isCyclic(this);
        if (!zIsCyclic) {
            this.acyclicSprings.add(spring);
            this.cyclicSprings.remove(spring);
        } else {
            System.err.println(((Object) spring) + " is cyclic. ");
        }
        return zIsCyclic;
    }

    private Spring abandonCycles(Spring spring) {
        return isCyclic(spring) ? this.cyclicReference : spring;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        this.componentConstraints.remove(component);
    }

    private static Dimension addInsets(int i2, int i3, Container container) {
        Insets insets = container.getInsets();
        return new Dimension(i2 + insets.left + insets.right, i3 + insets.top + insets.bottom);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        setParent(container);
        Constraints constraints = getConstraints(container);
        return addInsets(abandonCycles(constraints.getWidth()).getMinimumValue(), abandonCycles(constraints.getHeight()).getMinimumValue(), container);
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        setParent(container);
        Constraints constraints = getConstraints(container);
        return addInsets(abandonCycles(constraints.getWidth()).getPreferredValue(), abandonCycles(constraints.getHeight()).getPreferredValue(), container);
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        setParent(container);
        Constraints constraints = getConstraints(container);
        return addInsets(abandonCycles(constraints.getWidth()).getMaximumValue(), abandonCycles(constraints.getHeight()).getMaximumValue(), container);
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        if (obj instanceof Constraints) {
            putConstraints(component, (Constraints) obj);
        }
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
    }

    public void putConstraint(String str, Component component, int i2, String str2, Component component2) {
        putConstraint(str, component, Spring.constant(i2), str2, component2);
    }

    public void putConstraint(String str, Component component, Spring spring, String str2, Component component2) {
        putConstraint(str, component, Spring.sum(spring, getConstraint(str2, component2)));
    }

    private void putConstraint(String str, Component component, Spring spring) {
        if (spring != null) {
            getConstraints(component).setConstraint(str, spring);
        }
    }

    private Constraints applyDefaults(Component component, Constraints constraints) {
        if (constraints == null) {
            constraints = new Constraints();
        }
        if (constraints.f12811c == null) {
            constraints.f12811c = component;
        }
        if (constraints.horizontalHistory.size() < 2) {
            applyDefaults(constraints, "West", Spring.constant(0), "Width", Spring.width(component), constraints.horizontalHistory);
        }
        if (constraints.verticalHistory.size() < 2) {
            applyDefaults(constraints, "North", Spring.constant(0), "Height", Spring.height(component), constraints.verticalHistory);
        }
        return constraints;
    }

    private void applyDefaults(Constraints constraints, String str, Spring spring, String str2, Spring spring2, List<String> list) {
        if (list.size() == 0) {
            constraints.setConstraint(str, spring);
            constraints.setConstraint(str2, spring2);
        } else {
            if (constraints.getConstraint(str2) == null) {
                constraints.setConstraint(str2, spring2);
            } else {
                constraints.setConstraint(str, spring);
            }
            Collections.rotate(list, 1);
        }
    }

    private void putConstraints(Component component, Constraints constraints) {
        this.componentConstraints.put(component, applyDefaults(component, constraints));
    }

    public Constraints getConstraints(Component component) {
        Constraints constraints = this.componentConstraints.get(component);
        if (constraints == null) {
            if (component instanceof JComponent) {
                Object clientProperty = ((JComponent) component).getClientProperty(SpringLayout.class);
                if (clientProperty instanceof Constraints) {
                    return applyDefaults(component, (Constraints) clientProperty);
                }
            }
            constraints = new Constraints();
            putConstraints(component, constraints);
        }
        return constraints;
    }

    public Spring getConstraint(String str, Component component) {
        return new SpringProxy(str.intern(), component, this);
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        setParent(container);
        int componentCount = container.getComponentCount();
        getConstraints(container).reset();
        for (int i2 = 0; i2 < componentCount; i2++) {
            getConstraints(container.getComponent(i2)).reset();
        }
        Insets insets = container.getInsets();
        Constraints constraints = getConstraints(container);
        abandonCycles(constraints.getX()).setValue(0);
        abandonCycles(constraints.getY()).setValue(0);
        abandonCycles(constraints.getWidth()).setValue((container.getWidth() - insets.left) - insets.right);
        abandonCycles(constraints.getHeight()).setValue((container.getHeight() - insets.top) - insets.bottom);
        for (int i3 = 0; i3 < componentCount; i3++) {
            Component component = container.getComponent(i3);
            Constraints constraints2 = getConstraints(component);
            component.setBounds(insets.left + abandonCycles(constraints2.getX()).getValue(), insets.top + abandonCycles(constraints2.getY()).getValue(), abandonCycles(constraints2.getWidth()).getValue(), abandonCycles(constraints2.getHeight()).getValue());
        }
    }
}
