package com.sun.media.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

/* loaded from: rt.jar:com/sun/media/sound/AbstractDataLine.class */
abstract class AbstractDataLine extends AbstractLine implements DataLine {
    private final AudioFormat defaultFormat;
    private final int defaultBufferSize;
    protected final Object lock;
    protected AudioFormat format;
    protected int bufferSize;
    private volatile boolean running;
    private volatile boolean started;
    private volatile boolean active;

    abstract void implOpen(AudioFormat audioFormat, int i2) throws LineUnavailableException;

    abstract void implClose();

    abstract void implStart();

    abstract void implStop();

    protected AbstractDataLine(DataLine.Info info, AbstractMixer abstractMixer, Control[] controlArr) {
        this(info, abstractMixer, controlArr, null, -1);
    }

    protected AbstractDataLine(DataLine.Info info, AbstractMixer abstractMixer, Control[] controlArr, AudioFormat audioFormat, int i2) {
        super(info, abstractMixer, controlArr);
        this.lock = new Object();
        if (audioFormat != null) {
            this.defaultFormat = audioFormat;
        } else {
            this.defaultFormat = new AudioFormat(44100.0f, 16, 2, true, Platform.isBigEndian());
        }
        if (i2 > 0) {
            this.defaultBufferSize = i2;
        } else {
            this.defaultBufferSize = ((int) (this.defaultFormat.getFrameRate() / 2.0f)) * this.defaultFormat.getFrameSize();
        }
        this.format = this.defaultFormat;
        this.bufferSize = this.defaultBufferSize;
    }

    public final void open(AudioFormat audioFormat, int i2) throws LineUnavailableException {
        synchronized (this.mixer) {
            if (!isOpen()) {
                Toolkit.isFullySpecifiedAudioFormat(audioFormat);
                this.mixer.open(this);
                try {
                    implOpen(audioFormat, i2);
                    setOpen(true);
                } catch (LineUnavailableException e2) {
                    this.mixer.close(this);
                    throw e2;
                }
            } else {
                if (!audioFormat.matches(getFormat())) {
                    throw new IllegalStateException("Line is already open with format " + ((Object) getFormat()) + " and bufferSize " + getBufferSize());
                }
                if (i2 > 0) {
                    setBufferSize(i2);
                }
            }
        }
    }

    public final void open(AudioFormat audioFormat) throws LineUnavailableException {
        open(audioFormat, -1);
    }

    @Override // javax.sound.sampled.DataLine
    public int available() {
        return 0;
    }

    @Override // javax.sound.sampled.DataLine
    public void drain() {
    }

    @Override // javax.sound.sampled.DataLine
    public void flush() {
    }

    @Override // javax.sound.sampled.DataLine
    public final void start() {
        synchronized (this.mixer) {
            if (isOpen() && !isStartedRunning()) {
                this.mixer.start(this);
                implStart();
                this.running = true;
            }
        }
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }

    @Override // javax.sound.sampled.DataLine
    public final void stop() {
        synchronized (this.mixer) {
            if (isOpen() && isStartedRunning()) {
                implStop();
                this.mixer.stop(this);
                this.running = false;
                if (this.started && !isActive()) {
                    setStarted(false);
                }
            }
        }
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }

    @Override // javax.sound.sampled.DataLine
    public final boolean isRunning() {
        return this.started;
    }

    @Override // javax.sound.sampled.DataLine
    public final boolean isActive() {
        return this.active;
    }

    @Override // javax.sound.sampled.DataLine
    public final long getMicrosecondPosition() {
        long longFramePosition = getLongFramePosition();
        if (longFramePosition != -1) {
            longFramePosition = Toolkit.frames2micros(getFormat(), longFramePosition);
        }
        return longFramePosition;
    }

    @Override // javax.sound.sampled.DataLine
    public final AudioFormat getFormat() {
        return this.format;
    }

    @Override // javax.sound.sampled.DataLine
    public final int getBufferSize() {
        return this.bufferSize;
    }

    public final int setBufferSize(int i2) {
        return getBufferSize();
    }

    @Override // javax.sound.sampled.DataLine
    public final float getLevel() {
        return -1.0f;
    }

    final boolean isStartedRunning() {
        return this.running;
    }

    final void setActive(boolean z2) {
        synchronized (this) {
            if (this.active != z2) {
                this.active = z2;
            }
        }
    }

    final void setStarted(boolean z2) {
        boolean z3 = false;
        long longFramePosition = getLongFramePosition();
        synchronized (this) {
            if (this.started != z2) {
                this.started = z2;
                z3 = true;
            }
        }
        if (z3) {
            if (z2) {
                sendEvents(new LineEvent(this, LineEvent.Type.START, longFramePosition));
            } else {
                sendEvents(new LineEvent(this, LineEvent.Type.STOP, longFramePosition));
            }
        }
    }

    final void setEOM() {
        setStarted(false);
    }

    @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.Line
    public final void open() throws LineUnavailableException {
        open(this.format, this.bufferSize);
    }

    @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.Line, java.lang.AutoCloseable
    public final void close() {
        synchronized (this.mixer) {
            if (isOpen()) {
                stop();
                setOpen(false);
                implClose();
                this.mixer.close(this);
                this.format = this.defaultFormat;
                this.bufferSize = this.defaultBufferSize;
            }
        }
    }
}
