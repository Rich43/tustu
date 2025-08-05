package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.control.VideoRenderControl;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.AudioSpectrumListener;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/MediaPlayer.class */
public interface MediaPlayer {
    void addMediaErrorListener(MediaErrorListener mediaErrorListener);

    void removeMediaErrorListener(MediaErrorListener mediaErrorListener);

    void addMediaPlayerListener(PlayerStateListener playerStateListener);

    void removeMediaPlayerListener(PlayerStateListener playerStateListener);

    void addMediaTimeListener(PlayerTimeListener playerTimeListener);

    void removeMediaTimeListener(PlayerTimeListener playerTimeListener);

    void addVideoTrackSizeListener(VideoTrackSizeListener videoTrackSizeListener);

    void removeVideoTrackSizeListener(VideoTrackSizeListener videoTrackSizeListener);

    void addMarkerListener(MarkerListener markerListener);

    void removeMarkerListener(MarkerListener markerListener);

    void addBufferListener(BufferListener bufferListener);

    void removeBufferListener(BufferListener bufferListener);

    void addAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener);

    void removeAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener);

    VideoRenderControl getVideoRenderControl();

    MediaPlayerOverlay getMediaPlayerOverlay();

    Media getMedia();

    void setAudioSyncDelay(long j2);

    long getAudioSyncDelay();

    void play();

    void stop();

    void pause();

    float getRate();

    void setRate(float f2);

    double getPresentationTime();

    float getVolume();

    void setVolume(float f2);

    boolean getMute();

    void setMute(boolean z2);

    float getBalance();

    void setBalance(float f2);

    AudioEqualizer getEqualizer();

    AudioSpectrum getAudioSpectrum();

    double getDuration();

    double getStartTime();

    void setStartTime(double d2);

    double getStopTime();

    void setStopTime(double d2);

    void seek(double d2);

    PlayerStateEvent.PlayerState getState();

    void dispose();

    boolean isErrorEventCached();
}
