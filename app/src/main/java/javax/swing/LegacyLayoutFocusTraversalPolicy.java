package javax.swing;

/* compiled from: DefaultFocusManager.java */
/* loaded from: rt.jar:javax/swing/LegacyLayoutFocusTraversalPolicy.class */
final class LegacyLayoutFocusTraversalPolicy extends LayoutFocusTraversalPolicy {
    LegacyLayoutFocusTraversalPolicy(DefaultFocusManager defaultFocusManager) {
        super(new CompareTabOrderComparator(defaultFocusManager));
    }
}
