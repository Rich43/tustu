package sun.java2d;

import sun.java2d.StateTrackable;

/* loaded from: rt.jar:sun/java2d/StateTrackableDelegate.class */
public final class StateTrackableDelegate implements StateTrackable {
    public static final StateTrackableDelegate UNTRACKABLE_DELEGATE = new StateTrackableDelegate(StateTrackable.State.UNTRACKABLE);
    public static final StateTrackableDelegate IMMUTABLE_DELEGATE = new StateTrackableDelegate(StateTrackable.State.IMMUTABLE);
    private StateTrackable.State theState;
    StateTracker theTracker;
    private int numDynamicAgents;

    public static StateTrackableDelegate createInstance(StateTrackable.State state) {
        switch (state) {
            case UNTRACKABLE:
                return UNTRACKABLE_DELEGATE;
            case STABLE:
                return new StateTrackableDelegate(StateTrackable.State.STABLE);
            case DYNAMIC:
                return new StateTrackableDelegate(StateTrackable.State.DYNAMIC);
            case IMMUTABLE:
                return IMMUTABLE_DELEGATE;
            default:
                throw new InternalError("unknown state");
        }
    }

    private StateTrackableDelegate(StateTrackable.State state) {
        this.theState = state;
    }

    @Override // sun.java2d.StateTrackable
    public StateTrackable.State getState() {
        return this.theState;
    }

    @Override // sun.java2d.StateTrackable
    public synchronized StateTracker getStateTracker() {
        StateTracker stateTracker = this.theTracker;
        if (stateTracker == null) {
            switch (this.theState) {
                case UNTRACKABLE:
                case DYNAMIC:
                    stateTracker = StateTracker.NEVER_CURRENT;
                    break;
                case STABLE:
                    stateTracker = new StateTracker() { // from class: sun.java2d.StateTrackableDelegate.1
                        @Override // sun.java2d.StateTracker
                        public boolean isCurrent() {
                            return StateTrackableDelegate.this.theTracker == this;
                        }
                    };
                    break;
                case IMMUTABLE:
                    stateTracker = StateTracker.ALWAYS_CURRENT;
                    break;
            }
            this.theTracker = stateTracker;
        }
        return stateTracker;
    }

    public synchronized void setImmutable() {
        if (this.theState == StateTrackable.State.UNTRACKABLE || this.theState == StateTrackable.State.DYNAMIC) {
            throw new IllegalStateException("UNTRACKABLE or DYNAMIC objects cannot become IMMUTABLE");
        }
        this.theState = StateTrackable.State.IMMUTABLE;
        this.theTracker = null;
    }

    public synchronized void setUntrackable() {
        if (this.theState == StateTrackable.State.IMMUTABLE) {
            throw new IllegalStateException("IMMUTABLE objects cannot become UNTRACKABLE");
        }
        this.theState = StateTrackable.State.UNTRACKABLE;
        this.theTracker = null;
    }

    public synchronized void addDynamicAgent() {
        if (this.theState == StateTrackable.State.IMMUTABLE) {
            throw new IllegalStateException("Cannot change state from IMMUTABLE");
        }
        this.numDynamicAgents++;
        if (this.theState == StateTrackable.State.STABLE) {
            this.theState = StateTrackable.State.DYNAMIC;
            this.theTracker = null;
        }
    }

    protected synchronized void removeDynamicAgent() {
        int i2 = this.numDynamicAgents - 1;
        this.numDynamicAgents = i2;
        if (i2 == 0 && this.theState == StateTrackable.State.DYNAMIC) {
            this.theState = StateTrackable.State.STABLE;
            this.theTracker = null;
        }
    }

    public final void markDirty() {
        this.theTracker = null;
    }
}
