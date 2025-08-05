package com.sun.media.sound;

import java.applet.AudioClip;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:com/sun/media/sound/JavaSoundAudioClip.class */
public final class JavaSoundAudioClip implements AudioClip, MetaEventListener, LineListener {
    private static final boolean DEBUG = false;
    private static final int BUFFER_SIZE = 16384;
    private static final int MINIMUM_PLAY_DELAY = 30;
    private static final long CLIP_THRESHOLD = 1048576;
    private static final int STREAM_BUFFER_SIZE = 1024;
    private long lastPlayCall = 0;
    private byte[] loadedAudio = null;
    private int loadedAudioByteLength = 0;
    private AudioFormat loadedAudioFormat = null;
    private AutoClosingClip clip = null;
    private boolean clipLooping = false;
    private DataPusher datapusher = null;
    private Sequencer sequencer = null;
    private Sequence sequence = null;
    private boolean sequencerloop = false;

    public JavaSoundAudioClip(InputStream inputStream) throws IOException {
        boolean zCreateSequencer;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
        bufferedInputStream.mark(1024);
        try {
            zCreateSequencer = loadAudioData(AudioSystem.getAudioInputStream(bufferedInputStream));
            if (zCreateSequencer) {
                zCreateSequencer = ((long) this.loadedAudioByteLength) < 1048576 ? createClip() : false;
                if (!zCreateSequencer) {
                    zCreateSequencer = createSourceDataLine();
                }
            }
        } catch (UnsupportedAudioFileException e2) {
            try {
                MidiSystem.getMidiFileFormat(bufferedInputStream);
                zCreateSequencer = createSequencer(bufferedInputStream);
            } catch (InvalidMidiDataException e3) {
                zCreateSequencer = false;
            }
        }
        if (!zCreateSequencer) {
            throw new IOException("Unable to create AudioClip from input stream");
        }
    }

    @Override // java.applet.AudioClip
    public synchronized void play() {
        startImpl(false);
    }

    @Override // java.applet.AudioClip
    public synchronized void loop() {
        startImpl(true);
    }

    private synchronized void startImpl(boolean z2) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.lastPlayCall < 30) {
            return;
        }
        this.lastPlayCall = jCurrentTimeMillis;
        try {
            if (this.clip != null) {
                this.clip.setAutoClosing(false);
                try {
                    if (!this.clip.isOpen()) {
                        this.clip.open(this.loadedAudioFormat, this.loadedAudio, 0, this.loadedAudioByteLength);
                    } else {
                        this.clip.flush();
                        if (z2 != this.clipLooping) {
                            this.clip.stop();
                        }
                    }
                    this.clip.setFramePosition(0);
                    if (z2) {
                        this.clip.loop(-1);
                    } else {
                        this.clip.start();
                    }
                    this.clipLooping = z2;
                    this.clip.setAutoClosing(true);
                } catch (Throwable th) {
                    this.clip.setAutoClosing(true);
                    throw th;
                }
            } else if (this.datapusher != null) {
                this.datapusher.start(z2);
            } else if (this.sequencer != null) {
                this.sequencerloop = z2;
                if (this.sequencer.isRunning()) {
                    this.sequencer.setMicrosecondPosition(0L);
                }
                if (!this.sequencer.isOpen()) {
                    try {
                        this.sequencer.open();
                        this.sequencer.setSequence(this.sequence);
                    } catch (InvalidMidiDataException e2) {
                    } catch (MidiUnavailableException e3) {
                    }
                }
                this.sequencer.addMetaEventListener(this);
                try {
                    this.sequencer.start();
                } catch (Exception e4) {
                }
            }
        } catch (Exception e5) {
        }
    }

    @Override // java.applet.AudioClip
    public synchronized void stop() {
        this.lastPlayCall = 0L;
        if (this.clip != null) {
            try {
                this.clip.flush();
            } catch (Exception e2) {
            }
            try {
                this.clip.stop();
            } catch (Exception e3) {
            }
        } else {
            if (this.datapusher != null) {
                this.datapusher.stop();
                return;
            }
            if (this.sequencer != null) {
                try {
                    this.sequencerloop = false;
                    this.sequencer.addMetaEventListener(this);
                    this.sequencer.stop();
                } catch (Exception e4) {
                }
                try {
                    this.sequencer.close();
                } catch (Exception e5) {
                }
            }
        }
    }

    @Override // javax.sound.sampled.LineListener
    public synchronized void update(LineEvent lineEvent) {
    }

    @Override // javax.sound.midi.MetaEventListener
    public synchronized void meta(MetaMessage metaMessage) {
        if (metaMessage.getType() == 47) {
            if (this.sequencerloop) {
                this.sequencer.setMicrosecondPosition(0L);
                loop();
            } else {
                stop();
            }
        }
    }

    public String toString() {
        return getClass().toString();
    }

    protected void finalize() {
        if (this.clip != null) {
            this.clip.close();
        }
        if (this.datapusher != null) {
            this.datapusher.close();
        }
        if (this.sequencer != null) {
            this.sequencer.close();
        }
    }

    private boolean loadAudioData(AudioInputStream audioInputStream) throws UnsupportedAudioFileException, IOException {
        AudioInputStream pCMConvertedAudioInputStream = Toolkit.getPCMConvertedAudioInputStream(audioInputStream);
        if (pCMConvertedAudioInputStream == null) {
            return false;
        }
        this.loadedAudioFormat = pCMConvertedAudioInputStream.getFormat();
        long frameLength = pCMConvertedAudioInputStream.getFrameLength();
        int frameSize = this.loadedAudioFormat.getFrameSize();
        long j2 = -1;
        if (frameLength != -1 && frameLength > 0 && frameSize != -1 && frameSize > 0) {
            j2 = frameLength * frameSize;
        }
        if (j2 != -1) {
            readStream(pCMConvertedAudioInputStream, j2);
            return true;
        }
        readStream(pCMConvertedAudioInputStream);
        return true;
    }

    private void readStream(AudioInputStream audioInputStream, long j2) throws IOException {
        int i2;
        if (j2 > 2147483647L) {
            i2 = Integer.MAX_VALUE;
        } else {
            i2 = (int) j2;
        }
        this.loadedAudio = new byte[i2];
        this.loadedAudioByteLength = 0;
        while (true) {
            int i3 = audioInputStream.read(this.loadedAudio, this.loadedAudioByteLength, i2 - this.loadedAudioByteLength);
            if (i3 <= 0) {
                audioInputStream.close();
                return;
            }
            this.loadedAudioByteLength += i3;
        }
    }

    private void readStream(AudioInputStream audioInputStream) throws IOException {
        DirectBAOS directBAOS = new DirectBAOS();
        byte[] bArr = new byte[16384];
        int i2 = 0;
        while (true) {
            int i3 = audioInputStream.read(bArr, 0, bArr.length);
            if (i3 <= 0) {
                audioInputStream.close();
                this.loadedAudio = directBAOS.getInternalBuffer();
                this.loadedAudioByteLength = i2;
                return;
            }
            i2 += i3;
            directBAOS.write(bArr, 0, i3);
        }
    }

    private boolean createClip() {
        try {
            DataLine.Info info = new DataLine.Info(Clip.class, this.loadedAudioFormat);
            if (!AudioSystem.isLineSupported(info)) {
                return false;
            }
            Line line = AudioSystem.getLine(info);
            if (!(line instanceof AutoClosingClip)) {
                return false;
            }
            this.clip = (AutoClosingClip) line;
            this.clip.setAutoClosing(true);
            if (this.clip == null) {
                return false;
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private boolean createSourceDataLine() {
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.loadedAudioFormat);
            if (!AudioSystem.isLineSupported(info)) {
                return false;
            }
            this.datapusher = new DataPusher((SourceDataLine) AudioSystem.getLine(info), this.loadedAudioFormat, this.loadedAudio, this.loadedAudioByteLength);
            if (this.datapusher == null) {
                return false;
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private boolean createSequencer(BufferedInputStream bufferedInputStream) throws IOException {
        try {
            this.sequencer = MidiSystem.getSequencer();
            if (this.sequencer == null) {
                return false;
            }
            try {
                this.sequence = MidiSystem.getSequence(bufferedInputStream);
                if (this.sequence == null) {
                    return false;
                }
                return true;
            } catch (InvalidMidiDataException e2) {
                return false;
            }
        } catch (MidiUnavailableException e3) {
            return false;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/JavaSoundAudioClip$DirectBAOS.class */
    private static class DirectBAOS extends ByteArrayOutputStream {
        DirectBAOS() {
        }

        public byte[] getInternalBuffer() {
            return this.buf;
        }
    }
}
