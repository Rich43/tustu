package javax.swing;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Window;
import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

/* loaded from: rt.jar:javax/swing/LayoutComparator.class */
final class LayoutComparator implements Comparator<Component>, Serializable {
    private static final int ROW_TOLERANCE = 10;
    private boolean horizontal = true;
    private boolean leftToRight = true;

    LayoutComparator() {
    }

    void setComponentOrientation(ComponentOrientation componentOrientation) {
        this.horizontal = componentOrientation.isHorizontal();
        this.leftToRight = componentOrientation.isLeftToRight();
    }

    @Override // java.util.Comparator
    public int compare(Component component, Component component2) {
        if (component == component2) {
            return 0;
        }
        if (component.getParent() != component2.getParent()) {
            LinkedList linkedList = new LinkedList();
            while (component != null) {
                linkedList.add(component);
                if (component instanceof Window) {
                    break;
                }
                component = component.getParent();
            }
            if (component == null) {
                throw new ClassCastException();
            }
            LinkedList linkedList2 = new LinkedList();
            while (component2 != null) {
                linkedList2.add(component2);
                if (component2 instanceof Window) {
                    break;
                }
                component2 = component2.getParent();
            }
            if (component2 == null) {
                throw new ClassCastException();
            }
            ListIterator listIterator = linkedList.listIterator(linkedList.size());
            ListIterator listIterator2 = linkedList2.listIterator(linkedList2.size());
            while (listIterator.hasPrevious()) {
                component = (Component) listIterator.previous();
                if (listIterator2.hasPrevious()) {
                    component2 = (Component) listIterator2.previous();
                    if (component != component2) {
                    }
                } else {
                    return 1;
                }
            }
            return -1;
        }
        int x2 = component.getX();
        int y2 = component.getY();
        int x3 = component2.getX();
        int y3 = component2.getY();
        int componentZOrder = component.getParent().getComponentZOrder(component) - component2.getParent().getComponentZOrder(component2);
        if (this.horizontal) {
            if (this.leftToRight) {
                if (Math.abs(y2 - y3) >= 10) {
                    return y2 < y3 ? -1 : 1;
                }
                if (x2 < x3) {
                    return -1;
                }
                if (x2 > x3) {
                    return 1;
                }
                return componentZOrder;
            }
            if (Math.abs(y2 - y3) >= 10) {
                return y2 < y3 ? -1 : 1;
            }
            if (x2 > x3) {
                return -1;
            }
            if (x2 < x3) {
                return 1;
            }
            return componentZOrder;
        }
        if (this.leftToRight) {
            if (Math.abs(x2 - x3) >= 10) {
                return x2 < x3 ? -1 : 1;
            }
            if (y2 < y3) {
                return -1;
            }
            if (y2 > y3) {
                return 1;
            }
            return componentZOrder;
        }
        if (Math.abs(x2 - x3) >= 10) {
            return x2 > x3 ? -1 : 1;
        }
        if (y2 < y3) {
            return -1;
        }
        if (y2 > y3) {
            return 1;
        }
        return componentZOrder;
    }
}
