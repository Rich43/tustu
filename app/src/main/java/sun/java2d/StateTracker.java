package sun.java2d;

/* loaded from: rt.jar:sun/java2d/StateTracker.class */
public interface StateTracker {
    public static final StateTracker ALWAYS_CURRENT = new StateTracker() { // from class: sun.java2d.StateTracker.1
        @Override // sun.java2d.StateTracker
        public boolean isCurrent() {
            return true;
        }
    };
    public static final StateTracker NEVER_CURRENT = new StateTracker() { // from class: sun.java2d.StateTracker.2
        @Override // sun.java2d.StateTracker
        public boolean isCurrent() {
            return false;
        }
    };

    boolean isCurrent();
}
