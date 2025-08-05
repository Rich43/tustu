package javax.swing;

import java.awt.Component;
import java.util.Comparator;

/* compiled from: DefaultFocusManager.java */
/* loaded from: rt.jar:javax/swing/CompareTabOrderComparator.class */
final class CompareTabOrderComparator implements Comparator<Component> {
    private final DefaultFocusManager defaultFocusManager;

    CompareTabOrderComparator(DefaultFocusManager defaultFocusManager) {
        this.defaultFocusManager = defaultFocusManager;
    }

    @Override // java.util.Comparator
    public int compare(Component component, Component component2) {
        if (component == component2) {
            return 0;
        }
        return this.defaultFocusManager.compareTabOrder(component, component2) ? -1 : 1;
    }
}
