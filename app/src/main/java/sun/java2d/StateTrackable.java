package sun.java2d;

/* loaded from: rt.jar:sun/java2d/StateTrackable.class */
public interface StateTrackable {

    /* loaded from: rt.jar:sun/java2d/StateTrackable$State.class */
    public enum State {
        IMMUTABLE,
        STABLE,
        DYNAMIC,
        UNTRACKABLE
    }

    State getState();

    StateTracker getStateTracker();
}
