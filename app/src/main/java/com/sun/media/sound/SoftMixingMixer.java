package com.sun.media.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/* loaded from: rt.jar:com/sun/media/sound/SoftMixingMixer.class */
public final class SoftMixingMixer implements Mixer {
    static final String INFO_NAME = "Gervill Sound Mixer";
    static final String INFO_VENDOR = "OpenJDK Proposal";
    static final String INFO_DESCRIPTION = "Software Sound Mixer";
    static final String INFO_VERSION = "1.0";
    static final Mixer.Info info = new Info();
    final Object control_mutex = this;
    boolean implicitOpen = false;
    private boolean open = false;
    private SoftMixingMainMixer mainmixer = null;
    private AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
    private SourceDataLine sourceDataLine = null;
    private SoftAudioPusher pusher = null;
    private AudioInputStream pusher_stream = null;
    private final float controlrate = 147.0f;
    private final long latency = 100000;
    private final boolean jitter_correction = false;
    private final List<LineListener> listeners = new ArrayList();
    private final Line.Info[] sourceLineInfo = new Line.Info[2];

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingMixer$Info.class */
    private static class Info extends Mixer.Info {
        Info() {
            super(SoftMixingMixer.INFO_NAME, SoftMixingMixer.INFO_VENDOR, SoftMixingMixer.INFO_DESCRIPTION, "1.0");
        }
    }

    public SoftMixingMixer() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 1; i2 <= 2; i2++) {
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, 8, i2, i2, -1.0f, false));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0f, 8, i2, i2, -1.0f, false));
            for (int i3 = 16; i3 < 32; i3 += 8) {
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, i3, i2, (i2 * i3) / 8, -1.0f, false));
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0f, i3, i2, (i2 * i3) / 8, -1.0f, false));
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, i3, i2, (i2 * i3) / 8, -1.0f, true));
                arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, -1.0f, i3, i2, (i2 * i3) / 8, -1.0f, true));
            }
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 32, i2, i2 * 4, -1.0f, false));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 32, i2, i2 * 4, -1.0f, true));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 64, i2, i2 * 8, -1.0f, false));
            arrayList.add(new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, -1.0f, 64, i2, i2 * 8, -1.0f, true));
        }
        AudioFormat[] audioFormatArr = (AudioFormat[]) arrayList.toArray(new AudioFormat[arrayList.size()]);
        this.sourceLineInfo[0] = new DataLine.Info(SourceDataLine.class, audioFormatArr, -1, -1);
        this.sourceLineInfo[1] = new DataLine.Info(Clip.class, audioFormatArr, -1, -1);
    }

    @Override // javax.sound.sampled.Mixer
    public Line getLine(Line.Info info2) throws LineUnavailableException {
        if (!isLineSupported(info2)) {
            throw new IllegalArgumentException("Line unsupported: " + ((Object) info2));
        }
        if (info2.getLineClass() == SourceDataLine.class) {
            return new SoftMixingSourceDataLine(this, (DataLine.Info) info2);
        }
        if (info2.getLineClass() == Clip.class) {
            return new SoftMixingClip(this, (DataLine.Info) info2);
        }
        throw new IllegalArgumentException("Line unsupported: " + ((Object) info2));
    }

    @Override // javax.sound.sampled.Mixer
    public int getMaxLines(Line.Info info2) {
        if (info2.getLineClass() == SourceDataLine.class || info2.getLineClass() == Clip.class) {
            return -1;
        }
        return 0;
    }

    @Override // javax.sound.sampled.Mixer
    public Mixer.Info getMixerInfo() {
        return info;
    }

    @Override // javax.sound.sampled.Mixer
    public Line.Info[] getSourceLineInfo() {
        Line.Info[] infoArr = new Line.Info[this.sourceLineInfo.length];
        System.arraycopy(this.sourceLineInfo, 0, infoArr, 0, this.sourceLineInfo.length);
        return infoArr;
    }

    @Override // javax.sound.sampled.Mixer
    public Line.Info[] getSourceLineInfo(Line.Info info2) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.sourceLineInfo.length; i2++) {
            if (info2.matches(this.sourceLineInfo[i2])) {
                arrayList.add(this.sourceLineInfo[i2]);
            }
        }
        return (Line.Info[]) arrayList.toArray(new Line.Info[arrayList.size()]);
    }

    @Override // javax.sound.sampled.Mixer
    public Line[] getSourceLines() {
        synchronized (this.control_mutex) {
            if (this.mainmixer == null) {
                return new Line[0];
            }
            SoftMixingDataLine[] openLines = this.mainmixer.getOpenLines();
            Line[] lineArr = new Line[openLines.length];
            for (int i2 = 0; i2 < lineArr.length; i2++) {
                lineArr[i2] = openLines[i2];
            }
            return lineArr;
        }
    }

    @Override // javax.sound.sampled.Mixer
    public Line.Info[] getTargetLineInfo() {
        return new Line.Info[0];
    }

    @Override // javax.sound.sampled.Mixer
    public Line.Info[] getTargetLineInfo(Line.Info info2) {
        return new Line.Info[0];
    }

    @Override // javax.sound.sampled.Mixer
    public Line[] getTargetLines() {
        return new Line[0];
    }

    @Override // javax.sound.sampled.Mixer
    public boolean isLineSupported(Line.Info info2) {
        if (info2 != null) {
            for (int i2 = 0; i2 < this.sourceLineInfo.length; i2++) {
                if (info2.matches(this.sourceLineInfo[i2])) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // javax.sound.sampled.Mixer
    public boolean isSynchronizationSupported(Line[] lineArr, boolean z2) {
        return false;
    }

    @Override // javax.sound.sampled.Mixer
    public void synchronize(Line[] lineArr, boolean z2) {
        throw new IllegalArgumentException("Synchronization not supported by this mixer.");
    }

    @Override // javax.sound.sampled.Mixer
    public void unsynchronize(Line[] lineArr) {
        throw new IllegalArgumentException("Synchronization not supported by this mixer.");
    }

    @Override // javax.sound.sampled.Line
    public void addLineListener(LineListener lineListener) {
        synchronized (this.control_mutex) {
            this.listeners.add(lineListener);
        }
    }

    private void sendEvent(LineEvent lineEvent) {
        if (this.listeners.size() == 0) {
            return;
        }
        for (LineListener lineListener : (LineListener[]) this.listeners.toArray(new LineListener[this.listeners.size()])) {
            lineListener.update(lineEvent);
        }
    }

    @Override // javax.sound.sampled.Line, java.lang.AutoCloseable
    public void close() {
        if (!isOpen()) {
            return;
        }
        sendEvent(new LineEvent(this, LineEvent.Type.CLOSE, -1L));
        SoftAudioPusher softAudioPusher = null;
        AudioInputStream audioInputStream = null;
        synchronized (this.control_mutex) {
            if (this.pusher != null) {
                softAudioPusher = this.pusher;
                audioInputStream = this.pusher_stream;
                this.pusher = null;
                this.pusher_stream = null;
            }
        }
        if (softAudioPusher != null) {
            softAudioPusher.stop();
            try {
                audioInputStream.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        synchronized (this.control_mutex) {
            if (this.mainmixer != null) {
                this.mainmixer.close();
            }
            this.open = false;
            if (this.sourceDataLine != null) {
                this.sourceDataLine.drain();
                this.sourceDataLine.close();
                this.sourceDataLine = null;
            }
        }
    }

    @Override // javax.sound.sampled.Line
    public Control getControl(Control.Type type) {
        throw new IllegalArgumentException("Unsupported control type : " + ((Object) type));
    }

    @Override // javax.sound.sampled.Line
    public Control[] getControls() {
        return new Control[0];
    }

    @Override // javax.sound.sampled.Line
    public Line.Info getLineInfo() {
        return new Line.Info(Mixer.class);
    }

    @Override // javax.sound.sampled.Line
    public boolean isControlSupported(Control.Type type) {
        return false;
    }

    @Override // javax.sound.sampled.Line
    public boolean isOpen() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.open;
        }
        return z2;
    }

    @Override // javax.sound.sampled.Line
    public void open() throws LineUnavailableException {
        if (isOpen()) {
            this.implicitOpen = false;
        } else {
            open(null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:63:0x016b A[Catch: all -> 0x01a6, LineUnavailableException -> 0x0289, all -> 0x02a6, TryCatch #2 {all -> 0x01a6, blocks: (B:23:0x004a, B:25:0x0055, B:26:0x0067, B:28:0x006f, B:30:0x007c, B:31:0x0090, B:33:0x0098, B:35:0x00a8, B:59:0x015a, B:37:0x00b1, B:39:0x00bf, B:41:0x00cd, B:43:0x00d8, B:45:0x00e5, B:47:0x00ee, B:49:0x00f8, B:58:0x0144, B:60:0x0160, B:63:0x016b, B:66:0x0182, B:67:0x018a), top: B:129:0x004a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void open(javax.sound.sampled.SourceDataLine r9) throws javax.sound.sampled.LineUnavailableException {
        /*
            Method dump skipped, instructions count: 686
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.SoftMixingMixer.open(javax.sound.sampled.SourceDataLine):void");
    }

    public AudioInputStream openStream(AudioFormat audioFormat) throws LineUnavailableException {
        AudioInputStream inputStream;
        if (isOpen()) {
            throw new LineUnavailableException("Mixer is already open");
        }
        synchronized (this.control_mutex) {
            this.open = true;
            this.implicitOpen = false;
            if (audioFormat != null) {
                this.format = audioFormat;
            }
            this.mainmixer = new SoftMixingMainMixer(this);
            sendEvent(new LineEvent(this, LineEvent.Type.OPEN, -1L));
            inputStream = this.mainmixer.getInputStream();
        }
        return inputStream;
    }

    @Override // javax.sound.sampled.Line
    public void removeLineListener(LineListener lineListener) {
        synchronized (this.control_mutex) {
            this.listeners.remove(lineListener);
        }
    }

    public long getLatency() {
        synchronized (this.control_mutex) {
        }
        return 100000L;
    }

    public AudioFormat getFormat() {
        AudioFormat audioFormat;
        synchronized (this.control_mutex) {
            audioFormat = this.format;
        }
        return audioFormat;
    }

    float getControlRate() {
        return 147.0f;
    }

    SoftMixingMainMixer getMainMixer() {
        if (!isOpen()) {
            return null;
        }
        return this.mainmixer;
    }
}
