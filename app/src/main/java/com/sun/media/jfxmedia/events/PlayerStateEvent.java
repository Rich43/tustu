package com.sun.media.jfxmedia.events;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/PlayerStateEvent.class */
public class PlayerStateEvent extends PlayerEvent {
    private PlayerState playerState;
    private double playerTime;
    private String message;

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/PlayerStateEvent$PlayerState.class */
    public enum PlayerState {
        UNKNOWN,
        READY,
        PLAYING,
        PAUSED,
        STOPPED,
        STALLED,
        FINISHED,
        HALTED
    }

    public PlayerStateEvent(PlayerState state, double time) {
        if (state == null) {
            throw new IllegalArgumentException("state == null!");
        }
        if (time < 0.0d) {
            throw new IllegalArgumentException("time < 0.0!");
        }
        this.playerState = state;
        this.playerTime = time;
    }

    public PlayerStateEvent(PlayerState state, double time, String message) {
        this(state, time);
        this.message = message;
    }

    public PlayerState getState() {
        return this.playerState;
    }

    public double getTime() {
        return this.playerTime;
    }

    public String getMessage() {
        return this.message;
    }
}
