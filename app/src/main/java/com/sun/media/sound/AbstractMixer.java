package com.sun.media.sound;

import java.util.Vector;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

/* loaded from: rt.jar:com/sun/media/sound/AbstractMixer.class */
abstract class AbstractMixer extends AbstractLine implements Mixer {
    protected static final int PCM = 0;
    protected static final int ULAW = 1;
    protected static final int ALAW = 2;
    private final Mixer.Info mixerInfo;
    protected Line.Info[] sourceLineInfo;
    protected Line.Info[] targetLineInfo;
    private boolean started;
    private boolean manuallyOpened;
    private final Vector sourceLines;
    private final Vector targetLines;

    @Override // javax.sound.sampled.Mixer
    public abstract Line getLine(Line.Info info) throws LineUnavailableException;

    @Override // javax.sound.sampled.Mixer
    public abstract int getMaxLines(Line.Info info);

    protected abstract void implOpen() throws LineUnavailableException;

    protected abstract void implStart();

    protected abstract void implStop();

    protected abstract void implClose();

    protected AbstractMixer(Mixer.Info info, Control[] controlArr, Line.Info[] infoArr, Line.Info[] infoArr2) {
        super(new Line.Info(Mixer.class), null, controlArr);
        this.started = false;
        this.manuallyOpened = false;
        this.sourceLines = new Vector();
        this.targetLines = new Vector();
        this.mixer = this;
        if (controlArr == null) {
            Control[] controlArr2 = new Control[0];
        }
        this.mixerInfo = info;
        this.sourceLineInfo = infoArr;
        this.targetLineInfo = infoArr2;
    }

    @Override // javax.sound.sampled.Mixer
    public final Mixer.Info getMixerInfo() {
        return this.mixerInfo;
    }

    @Override // javax.sound.sampled.Mixer
    public final Line.Info[] getSourceLineInfo() {
        Line.Info[] infoArr = new Line.Info[this.sourceLineInfo.length];
        System.arraycopy(this.sourceLineInfo, 0, infoArr, 0, this.sourceLineInfo.length);
        return infoArr;
    }

    @Override // javax.sound.sampled.Mixer
    public final Line.Info[] getTargetLineInfo() {
        Line.Info[] infoArr = new Line.Info[this.targetLineInfo.length];
        System.arraycopy(this.targetLineInfo, 0, infoArr, 0, this.targetLineInfo.length);
        return infoArr;
    }

    @Override // javax.sound.sampled.Mixer
    public final Line.Info[] getSourceLineInfo(Line.Info info) {
        Vector vector = new Vector();
        for (int i2 = 0; i2 < this.sourceLineInfo.length; i2++) {
            if (info.matches(this.sourceLineInfo[i2])) {
                vector.addElement(this.sourceLineInfo[i2]);
            }
        }
        Line.Info[] infoArr = new Line.Info[vector.size()];
        for (int i3 = 0; i3 < infoArr.length; i3++) {
            infoArr[i3] = (Line.Info) vector.elementAt(i3);
        }
        return infoArr;
    }

    @Override // javax.sound.sampled.Mixer
    public final Line.Info[] getTargetLineInfo(Line.Info info) {
        Vector vector = new Vector();
        for (int i2 = 0; i2 < this.targetLineInfo.length; i2++) {
            if (info.matches(this.targetLineInfo[i2])) {
                vector.addElement(this.targetLineInfo[i2]);
            }
        }
        Line.Info[] infoArr = new Line.Info[vector.size()];
        for (int i3 = 0; i3 < infoArr.length; i3++) {
            infoArr[i3] = (Line.Info) vector.elementAt(i3);
        }
        return infoArr;
    }

    @Override // javax.sound.sampled.Mixer
    public final boolean isLineSupported(Line.Info info) {
        for (int i2 = 0; i2 < this.sourceLineInfo.length; i2++) {
            if (info.matches(this.sourceLineInfo[i2])) {
                return true;
            }
        }
        for (int i3 = 0; i3 < this.targetLineInfo.length; i3++) {
            if (info.matches(this.targetLineInfo[i3])) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.sound.sampled.Mixer
    public final Line[] getSourceLines() {
        Line[] lineArr;
        synchronized (this.sourceLines) {
            lineArr = new Line[this.sourceLines.size()];
            for (int i2 = 0; i2 < lineArr.length; i2++) {
                lineArr[i2] = (Line) this.sourceLines.elementAt(i2);
            }
        }
        return lineArr;
    }

    @Override // javax.sound.sampled.Mixer
    public final Line[] getTargetLines() {
        Line[] lineArr;
        synchronized (this.targetLines) {
            lineArr = new Line[this.targetLines.size()];
            for (int i2 = 0; i2 < lineArr.length; i2++) {
                lineArr[i2] = (Line) this.targetLines.elementAt(i2);
            }
        }
        return lineArr;
    }

    @Override // javax.sound.sampled.Mixer
    public final void synchronize(Line[] lineArr, boolean z2) {
        throw new IllegalArgumentException("Synchronization not supported by this mixer.");
    }

    @Override // javax.sound.sampled.Mixer
    public final void unsynchronize(Line[] lineArr) {
        throw new IllegalArgumentException("Synchronization not supported by this mixer.");
    }

    @Override // javax.sound.sampled.Mixer
    public final boolean isSynchronizationSupported(Line[] lineArr, boolean z2) {
        return false;
    }

    @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.Line
    public final synchronized void open() throws LineUnavailableException {
        open(true);
    }

    final synchronized void open(boolean z2) throws LineUnavailableException {
        if (!isOpen()) {
            implOpen();
            setOpen(true);
            if (z2) {
                this.manuallyOpened = true;
            }
        }
    }

    final synchronized void open(Line line) throws LineUnavailableException {
        if (equals(line)) {
            return;
        }
        if (isSourceLine(line.getLineInfo())) {
            if (!this.sourceLines.contains(line)) {
                open(false);
                this.sourceLines.addElement(line);
                return;
            }
            return;
        }
        if (isTargetLine(line.getLineInfo()) && !this.targetLines.contains(line)) {
            open(false);
            this.targetLines.addElement(line);
        }
    }

    final synchronized void close(Line line) {
        if (equals(line)) {
            return;
        }
        this.sourceLines.removeElement(line);
        this.targetLines.removeElement(line);
        if (this.sourceLines.isEmpty() && this.targetLines.isEmpty() && !this.manuallyOpened) {
            close();
        }
    }

    @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.Line, java.lang.AutoCloseable
    public final synchronized void close() {
        if (isOpen()) {
            for (Line line : getSourceLines()) {
                line.close();
            }
            for (Line line2 : getTargetLines()) {
                line2.close();
            }
            implClose();
            setOpen(false);
        }
        this.manuallyOpened = false;
    }

    final synchronized void start(Line line) {
        if (!equals(line) && !this.started) {
            implStart();
            this.started = true;
        }
    }

    final synchronized void stop(Line line) {
        if (equals(line)) {
            return;
        }
        Vector vector = (Vector) this.sourceLines.clone();
        for (int i2 = 0; i2 < vector.size(); i2++) {
            if (vector.elementAt(i2) instanceof AbstractDataLine) {
                AbstractDataLine abstractDataLine = (AbstractDataLine) vector.elementAt(i2);
                if (abstractDataLine.isStartedRunning() && !abstractDataLine.equals(line)) {
                    return;
                }
            }
        }
        Vector vector2 = (Vector) this.targetLines.clone();
        for (int i3 = 0; i3 < vector2.size(); i3++) {
            if (vector2.elementAt(i3) instanceof AbstractDataLine) {
                AbstractDataLine abstractDataLine2 = (AbstractDataLine) vector2.elementAt(i3);
                if (abstractDataLine2.isStartedRunning() && !abstractDataLine2.equals(line)) {
                    return;
                }
            }
        }
        this.started = false;
        implStop();
    }

    final boolean isSourceLine(Line.Info info) {
        for (int i2 = 0; i2 < this.sourceLineInfo.length; i2++) {
            if (info.matches(this.sourceLineInfo[i2])) {
                return true;
            }
        }
        return false;
    }

    final boolean isTargetLine(Line.Info info) {
        for (int i2 = 0; i2 < this.targetLineInfo.length; i2++) {
            if (info.matches(this.targetLineInfo[i2])) {
                return true;
            }
        }
        return false;
    }

    final Line.Info getLineInfo(Line.Info info) {
        if (info == null) {
            return null;
        }
        for (int i2 = 0; i2 < this.sourceLineInfo.length; i2++) {
            if (info.matches(this.sourceLineInfo[i2])) {
                return this.sourceLineInfo[i2];
            }
        }
        for (int i3 = 0; i3 < this.targetLineInfo.length; i3++) {
            if (info.matches(this.targetLineInfo[i3])) {
                return this.targetLineInfo[i3];
            }
        }
        return null;
    }
}
