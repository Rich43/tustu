package com.sun.media.jfxmedia.events;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/PlayerStateListener.class */
public interface PlayerStateListener {
    void onReady(PlayerStateEvent playerStateEvent);

    void onPlaying(PlayerStateEvent playerStateEvent);

    void onPause(PlayerStateEvent playerStateEvent);

    void onStop(PlayerStateEvent playerStateEvent);

    void onStall(PlayerStateEvent playerStateEvent);

    void onFinish(PlayerStateEvent playerStateEvent);

    void onHalt(PlayerStateEvent playerStateEvent);
}
