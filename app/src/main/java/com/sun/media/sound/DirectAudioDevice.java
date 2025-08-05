package com.sun.media.sound;

import com.sun.media.sound.DirectAudioDeviceProvider;
import com.sun.media.sound.EventDispatcher;
import java.awt.BorderLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice.class */
final class DirectAudioDevice extends AbstractMixer {
    private static final int CLIP_BUFFER_TIME = 1000;
    private static final int DEFAULT_LINE_BUFFER_TIME = 500;
    private int deviceCountOpened;
    private int deviceCountStarted;

    private static native void nGetFormats(int i2, int i3, boolean z2, Vector vector);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nOpen(int i2, int i3, boolean z2, int i4, float f2, int i5, int i6, int i7, boolean z3, boolean z4, int i8) throws LineUnavailableException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nStart(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nStop(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nClose(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nWrite(long j2, byte[] bArr, int i2, int i3, int i4, float f2, float f3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nRead(long j2, byte[] bArr, int i2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nGetBufferSize(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nIsStillDraining(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nFlush(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nAvailable(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nGetBytePosition(long j2, boolean z2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetBytePosition(long j2, boolean z2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nRequiresServicing(long j2, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nService(long j2, boolean z2);

    DirectAudioDevice(DirectAudioDeviceProvider.DirectAudioDeviceInfo directAudioDeviceInfo) {
        super(directAudioDeviceInfo, null, null, null);
        this.deviceCountOpened = 0;
        this.deviceCountStarted = 0;
        DirectDLI directDLICreateDataLineInfo = createDataLineInfo(true);
        if (directDLICreateDataLineInfo != null) {
            this.sourceLineInfo = new Line.Info[2];
            this.sourceLineInfo[0] = directDLICreateDataLineInfo;
            this.sourceLineInfo[1] = new DirectDLI(Clip.class, directDLICreateDataLineInfo.getFormats(), directDLICreateDataLineInfo.getHardwareFormats(), 32, -1);
        } else {
            this.sourceLineInfo = new Line.Info[0];
        }
        DirectDLI directDLICreateDataLineInfo2 = createDataLineInfo(false);
        if (directDLICreateDataLineInfo2 != null) {
            this.targetLineInfo = new Line.Info[1];
            this.targetLineInfo[0] = directDLICreateDataLineInfo2;
        } else {
            this.targetLineInfo = new Line.Info[0];
        }
    }

    private DirectDLI createDataLineInfo(boolean z2) {
        Vector vector = new Vector();
        AudioFormat[] audioFormatArr = null;
        AudioFormat[] audioFormatArr2 = null;
        synchronized (vector) {
            nGetFormats(getMixerIndex(), getDeviceID(), z2, vector);
            if (vector.size() > 0) {
                int size = vector.size();
                int i2 = size;
                audioFormatArr = new AudioFormat[size];
                for (int i3 = 0; i3 < size; i3++) {
                    AudioFormat audioFormat = (AudioFormat) vector.elementAt(i3);
                    audioFormatArr[i3] = audioFormat;
                    audioFormat.getSampleSizeInBits();
                    boolean zEquals = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
                    boolean zEquals2 = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
                    if (zEquals || zEquals2) {
                        i2++;
                    }
                }
                audioFormatArr2 = new AudioFormat[i2];
                int i4 = 0;
                for (int i5 = 0; i5 < size; i5++) {
                    AudioFormat audioFormat2 = audioFormatArr[i5];
                    int i6 = i4;
                    i4++;
                    audioFormatArr2[i6] = audioFormat2;
                    int sampleSizeInBits = audioFormat2.getSampleSizeInBits();
                    boolean zEquals3 = audioFormat2.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
                    boolean zEquals4 = audioFormat2.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
                    if (sampleSizeInBits == 8) {
                        if (zEquals3) {
                            i4++;
                            audioFormatArr2[i4] = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, audioFormat2.getSampleRate(), sampleSizeInBits, audioFormat2.getChannels(), audioFormat2.getFrameSize(), audioFormat2.getSampleRate(), audioFormat2.isBigEndian());
                        } else if (zEquals4) {
                            i4++;
                            audioFormatArr2[i4] = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat2.getSampleRate(), sampleSizeInBits, audioFormat2.getChannels(), audioFormat2.getFrameSize(), audioFormat2.getSampleRate(), audioFormat2.isBigEndian());
                        }
                    } else if (sampleSizeInBits > 8 && (zEquals3 || zEquals4)) {
                        i4++;
                        audioFormatArr2[i4] = new AudioFormat(audioFormat2.getEncoding(), audioFormat2.getSampleRate(), sampleSizeInBits, audioFormat2.getChannels(), audioFormat2.getFrameSize(), audioFormat2.getSampleRate(), !audioFormat2.isBigEndian());
                    }
                }
            }
        }
        if (audioFormatArr2 != null) {
            return new DirectDLI(z2 ? SourceDataLine.class : TargetDataLine.class, audioFormatArr2, audioFormatArr, 32, -1);
        }
        return null;
    }

    @Override // com.sun.media.sound.AbstractMixer, javax.sound.sampled.Mixer
    public Line getLine(Line.Info info) throws LineUnavailableException {
        AudioFormat audioFormat;
        Line.Info lineInfo = getLineInfo(info);
        if (lineInfo == null) {
            throw new IllegalArgumentException("Line unsupported: " + ((Object) info));
        }
        if (lineInfo instanceof DataLine.Info) {
            DataLine.Info info2 = (DataLine.Info) lineInfo;
            int maxBufferSize = -1;
            AudioFormat[] formats = null;
            if (info instanceof DataLine.Info) {
                formats = ((DataLine.Info) info).getFormats();
                maxBufferSize = ((DataLine.Info) info).getMaxBufferSize();
            }
            if (formats == null || formats.length == 0) {
                audioFormat = null;
            } else {
                audioFormat = formats[formats.length - 1];
                if (!Toolkit.isFullySpecifiedPCMFormat(audioFormat)) {
                    audioFormat = null;
                }
            }
            if (info2.getLineClass().isAssignableFrom(DirectSDL.class)) {
                return new DirectSDL(info2, audioFormat, maxBufferSize, this);
            }
            if (info2.getLineClass().isAssignableFrom(DirectClip.class)) {
                return new DirectClip(info2, audioFormat, maxBufferSize, this);
            }
            if (info2.getLineClass().isAssignableFrom(DirectTDL.class)) {
                return new DirectTDL(info2, audioFormat, maxBufferSize, this);
            }
        }
        throw new IllegalArgumentException("Line unsupported: " + ((Object) info));
    }

    @Override // com.sun.media.sound.AbstractMixer, javax.sound.sampled.Mixer
    public int getMaxLines(Line.Info info) {
        Line.Info lineInfo = getLineInfo(info);
        if (lineInfo != null && (lineInfo instanceof DataLine.Info)) {
            return getMaxSimulLines();
        }
        return 0;
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implOpen() throws LineUnavailableException {
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implClose() {
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implStart() {
    }

    @Override // com.sun.media.sound.AbstractMixer
    protected void implStop() {
    }

    int getMixerIndex() {
        return ((DirectAudioDeviceProvider.DirectAudioDeviceInfo) getMixerInfo()).getIndex();
    }

    int getDeviceID() {
        return ((DirectAudioDeviceProvider.DirectAudioDeviceInfo) getMixerInfo()).getDeviceID();
    }

    int getMaxSimulLines() {
        return ((DirectAudioDeviceProvider.DirectAudioDeviceInfo) getMixerInfo()).getMaxSimulLines();
    }

    private static void addFormat(Vector vector, int i2, int i3, int i4, float f2, int i5, boolean z2, boolean z3) {
        AudioFormat.Encoding encoding = null;
        switch (i5) {
            case 0:
                encoding = z2 ? AudioFormat.Encoding.PCM_SIGNED : AudioFormat.Encoding.PCM_UNSIGNED;
                break;
            case 1:
                encoding = AudioFormat.Encoding.ULAW;
                if (i2 != 8) {
                    i2 = 8;
                    i3 = i4;
                    break;
                }
                break;
            case 2:
                encoding = AudioFormat.Encoding.ALAW;
                if (i2 != 8) {
                    i2 = 8;
                    i3 = i4;
                    break;
                }
                break;
        }
        if (encoding == null) {
            return;
        }
        if (i3 <= 0) {
            if (i4 > 0) {
                i3 = ((i2 + 7) / 8) * i4;
            } else {
                i3 = -1;
            }
        }
        vector.add(new AudioFormat(encoding, f2, i2, i4, i3, f2, z3));
    }

    protected static AudioFormat getSignOrEndianChangedFormat(AudioFormat audioFormat) {
        boolean zEquals = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
        boolean zEquals2 = audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
        if (audioFormat.getSampleSizeInBits() > 8 && zEquals) {
            return new AudioFormat(audioFormat.getEncoding(), audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), !audioFormat.isBigEndian());
        }
        if (audioFormat.getSampleSizeInBits() != 8) {
            return null;
        }
        if (zEquals || zEquals2) {
            return new AudioFormat(zEquals ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), audioFormat.isBigEndian());
        }
        return null;
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectDLI.class */
    private static final class DirectDLI extends DataLine.Info {
        final AudioFormat[] hardwareFormats;

        private DirectDLI(Class cls, AudioFormat[] audioFormatArr, AudioFormat[] audioFormatArr2, int i2, int i3) {
            super(cls, audioFormatArr, i2, i3);
            this.hardwareFormats = audioFormatArr2;
        }

        public boolean isFormatSupportedInHardware(AudioFormat audioFormat) {
            if (audioFormat == null) {
                return false;
            }
            for (int i2 = 0; i2 < this.hardwareFormats.length; i2++) {
                if (audioFormat.matches(this.hardwareFormats[i2])) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public AudioFormat[] getHardwareFormats() {
            return this.hardwareFormats;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectDL.class */
    private static class DirectDL extends AbstractDataLine implements EventDispatcher.LineMonitor {
        protected final int mixerIndex;
        protected final int deviceID;
        protected long id;
        protected int waitTime;
        protected volatile boolean flushing;
        protected final boolean isSource;
        protected volatile long bytePosition;
        protected volatile boolean doIO;
        protected volatile boolean stoppedWritten;
        protected volatile boolean drained;
        protected boolean monitoring;
        protected int softwareConversionSize;
        protected AudioFormat hardwareFormat;
        private final Gain gainControl;
        private final Mute muteControl;
        private final Balance balanceControl;
        private final Pan panControl;
        private float leftGain;
        private float rightGain;
        protected volatile boolean noService;
        protected final Object lockNative;

        protected DirectDL(DataLine.Info info, DirectAudioDevice directAudioDevice, AudioFormat audioFormat, int i2, int i3, int i4, boolean z2) {
            super(info, directAudioDevice, null, audioFormat, i2);
            this.flushing = false;
            this.doIO = false;
            this.stoppedWritten = false;
            this.drained = false;
            this.monitoring = false;
            this.softwareConversionSize = 0;
            this.gainControl = new Gain();
            this.muteControl = new Mute();
            this.balanceControl = new Balance();
            this.panControl = new Pan();
            this.noService = false;
            this.lockNative = new Object();
            this.mixerIndex = i3;
            this.deviceID = i4;
            this.waitTime = 10;
            this.isSource = z2;
        }

        @Override // com.sun.media.sound.AbstractDataLine
        void implOpen(AudioFormat audioFormat, int i2) throws LineUnavailableException, SecurityException {
            Toolkit.isFullySpecifiedAudioFormat(audioFormat);
            if (!this.isSource) {
                JSSecurityManager.checkRecordPermission();
            }
            int i3 = 0;
            if (audioFormat.getEncoding().equals(AudioFormat.Encoding.ULAW)) {
                i3 = 1;
            } else if (audioFormat.getEncoding().equals(AudioFormat.Encoding.ALAW)) {
                i3 = 2;
            }
            if (i2 <= -1) {
                i2 = (int) Toolkit.millis2bytes(audioFormat, 500L);
            }
            DirectDLI directDLI = null;
            if (this.info instanceof DirectDLI) {
                directDLI = (DirectDLI) this.info;
            }
            if (this.isSource) {
                if (!audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                    this.controls = new Control[0];
                } else if (audioFormat.getChannels() > 2 || audioFormat.getSampleSizeInBits() > 16) {
                    this.controls = new Control[0];
                } else {
                    if (audioFormat.getChannels() == 1) {
                        this.controls = new Control[2];
                    } else {
                        this.controls = new Control[4];
                        this.controls[2] = this.balanceControl;
                        this.controls[3] = this.panControl;
                    }
                    this.controls[0] = this.gainControl;
                    this.controls[1] = this.muteControl;
                }
            }
            this.hardwareFormat = audioFormat;
            this.softwareConversionSize = 0;
            if (directDLI != null && !directDLI.isFormatSupportedInHardware(audioFormat)) {
                AudioFormat signOrEndianChangedFormat = DirectAudioDevice.getSignOrEndianChangedFormat(audioFormat);
                if (directDLI.isFormatSupportedInHardware(signOrEndianChangedFormat)) {
                    this.hardwareFormat = signOrEndianChangedFormat;
                    this.softwareConversionSize = audioFormat.getFrameSize() / audioFormat.getChannels();
                }
            }
            int frameSize = (i2 / audioFormat.getFrameSize()) * audioFormat.getFrameSize();
            this.id = DirectAudioDevice.nOpen(this.mixerIndex, this.deviceID, this.isSource, i3, this.hardwareFormat.getSampleRate(), this.hardwareFormat.getSampleSizeInBits(), this.hardwareFormat.getFrameSize(), this.hardwareFormat.getChannels(), this.hardwareFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED), this.hardwareFormat.isBigEndian(), frameSize);
            if (this.id == 0) {
                throw new LineUnavailableException("line with format " + ((Object) audioFormat) + " not supported.");
            }
            this.bufferSize = DirectAudioDevice.nGetBufferSize(this.id, this.isSource);
            if (this.bufferSize < 1) {
                this.bufferSize = frameSize;
            }
            this.format = audioFormat;
            this.waitTime = ((int) Toolkit.bytes2millis(audioFormat, this.bufferSize)) / 4;
            if (this.waitTime < 10) {
                this.waitTime = 1;
            } else if (this.waitTime > 1000) {
                this.waitTime = 1000;
            }
            this.bytePosition = 0L;
            this.stoppedWritten = false;
            this.doIO = false;
            calcVolume();
        }

        @Override // com.sun.media.sound.AbstractDataLine
        void implStart() throws SecurityException {
            if (!this.isSource) {
                JSSecurityManager.checkRecordPermission();
            }
            synchronized (this.lockNative) {
                DirectAudioDevice.nStart(this.id, this.isSource);
            }
            this.monitoring = requiresServicing();
            if (this.monitoring) {
                getEventDispatcher().addLineMonitor(this);
            }
            synchronized (this.lock) {
                this.doIO = true;
                if (this.isSource && this.stoppedWritten) {
                    setStarted(true);
                    setActive(true);
                }
            }
        }

        @Override // com.sun.media.sound.AbstractDataLine
        void implStop() throws SecurityException {
            if (!this.isSource) {
                JSSecurityManager.checkRecordPermission();
            }
            if (this.monitoring) {
                getEventDispatcher().removeLineMonitor(this);
                this.monitoring = false;
            }
            synchronized (this.lockNative) {
                DirectAudioDevice.nStop(this.id, this.isSource);
            }
            synchronized (this.lock) {
                this.doIO = false;
                setActive(false);
                setStarted(false);
                this.lock.notifyAll();
            }
            this.stoppedWritten = false;
        }

        @Override // com.sun.media.sound.AbstractDataLine
        void implClose() throws SecurityException {
            if (!this.isSource) {
                JSSecurityManager.checkRecordPermission();
            }
            if (this.monitoring) {
                getEventDispatcher().removeLineMonitor(this);
                this.monitoring = false;
            }
            this.doIO = false;
            long j2 = this.id;
            this.id = 0L;
            synchronized (this.lockNative) {
                DirectAudioDevice.nClose(j2, this.isSource);
            }
            this.bytePosition = 0L;
            this.softwareConversionSize = 0;
        }

        @Override // com.sun.media.sound.AbstractDataLine, javax.sound.sampled.DataLine
        public int available() {
            int iNAvailable;
            if (this.id == 0) {
                return 0;
            }
            synchronized (this.lockNative) {
                iNAvailable = DirectAudioDevice.nAvailable(this.id, this.isSource);
            }
            return iNAvailable;
        }

        @Override // com.sun.media.sound.AbstractDataLine, javax.sound.sampled.DataLine
        public void drain() {
            this.noService = true;
            int i2 = 0;
            long longFramePosition = getLongFramePosition();
            boolean z2 = false;
            while (!this.drained) {
                synchronized (this.lockNative) {
                    if (this.id == 0 || !this.doIO || !DirectAudioDevice.nIsStillDraining(this.id, this.isSource)) {
                        break;
                    }
                    if (i2 % 5 == 4) {
                        long longFramePosition2 = getLongFramePosition();
                        z2 |= longFramePosition2 != longFramePosition;
                        if (i2 % 50 > 45) {
                            if (!z2) {
                                break;
                            }
                            z2 = false;
                            longFramePosition = longFramePosition2;
                        }
                    }
                    i2++;
                    synchronized (this.lock) {
                        try {
                            this.lock.wait(10L);
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            }
            if (this.doIO && this.id != 0) {
                this.drained = true;
            }
            this.noService = false;
        }

        @Override // com.sun.media.sound.AbstractDataLine, javax.sound.sampled.DataLine
        public void flush() {
            if (this.id != 0) {
                this.flushing = true;
                synchronized (this.lock) {
                    this.lock.notifyAll();
                }
                synchronized (this.lockNative) {
                    if (this.id != 0) {
                        DirectAudioDevice.nFlush(this.id, this.isSource);
                    }
                }
                this.drained = true;
            }
        }

        @Override // com.sun.media.sound.AbstractLine, javax.sound.sampled.DataLine
        public long getLongFramePosition() {
            long jNGetBytePosition;
            synchronized (this.lockNative) {
                jNGetBytePosition = DirectAudioDevice.nGetBytePosition(this.id, this.isSource, this.bytePosition);
            }
            if (jNGetBytePosition < 0) {
                jNGetBytePosition = 0;
            }
            return jNGetBytePosition / getFormat().getFrameSize();
        }

        public int write(byte[] bArr, int i2, int i3) {
            this.flushing = false;
            if (i3 == 0) {
                return 0;
            }
            if (i3 < 0) {
                throw new IllegalArgumentException("illegal len: " + i3);
            }
            if (i3 % getFormat().getFrameSize() != 0) {
                throw new IllegalArgumentException("illegal request to write non-integral number of frames (" + i3 + " bytes, frameSize = " + getFormat().getFrameSize() + " bytes)");
            }
            if (i2 < 0) {
                throw new ArrayIndexOutOfBoundsException(i2);
            }
            if (i2 + i3 > bArr.length) {
                throw new ArrayIndexOutOfBoundsException(bArr.length);
            }
            synchronized (this.lock) {
                if (!isActive() && this.doIO) {
                    setActive(true);
                    setStarted(true);
                }
            }
            int i4 = 0;
            while (true) {
                if (this.flushing) {
                    break;
                }
                synchronized (this.lockNative) {
                    int iNWrite = DirectAudioDevice.nWrite(this.id, bArr, i2, i3, this.softwareConversionSize, this.leftGain, this.rightGain);
                    if (iNWrite < 0) {
                        break;
                    }
                    this.bytePosition += iNWrite;
                    if (iNWrite > 0) {
                        this.drained = false;
                    }
                    i3 -= iNWrite;
                    i4 += iNWrite;
                    if (!this.doIO || i3 <= 0) {
                        break;
                    }
                    i2 += iNWrite;
                    synchronized (this.lock) {
                        try {
                            this.lock.wait(this.waitTime);
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            }
            if (i4 > 0 && !this.doIO) {
                this.stoppedWritten = true;
            }
            return i4;
        }

        protected boolean requiresServicing() {
            return DirectAudioDevice.nRequiresServicing(this.id, this.isSource);
        }

        @Override // com.sun.media.sound.EventDispatcher.LineMonitor
        public void checkLine() {
            synchronized (this.lockNative) {
                if (this.monitoring && this.doIO && this.id != 0 && !this.flushing && !this.noService) {
                    DirectAudioDevice.nService(this.id, this.isSource);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void calcVolume() {
            if (getFormat() == null) {
                return;
            }
            if (this.muteControl.getValue()) {
                this.leftGain = 0.0f;
                this.rightGain = 0.0f;
                return;
            }
            float linearGain = this.gainControl.getLinearGain();
            if (getFormat().getChannels() == 1) {
                this.leftGain = linearGain;
                this.rightGain = linearGain;
                return;
            }
            float value = this.balanceControl.getValue();
            if (value < 0.0f) {
                this.leftGain = linearGain;
                this.rightGain = linearGain * (value + 1.0f);
            } else {
                this.leftGain = linearGain * (1.0f - value);
                this.rightGain = linearGain;
            }
        }

        /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectDL$Gain.class */
        protected final class Gain extends FloatControl {
            private float linearGain;

            private Gain() {
                super(FloatControl.Type.MASTER_GAIN, Toolkit.linearToDB(0.0f), Toolkit.linearToDB(2.0f), Math.abs(Toolkit.linearToDB(1.0f) - Toolkit.linearToDB(0.0f)) / 128.0f, -1, 0.0f, "dB", "Minimum", "", "Maximum");
                this.linearGain = 1.0f;
            }

            @Override // javax.sound.sampled.FloatControl
            public void setValue(float f2) {
                float fDBToLinear = Toolkit.dBToLinear(f2);
                super.setValue(Toolkit.linearToDB(fDBToLinear));
                this.linearGain = fDBToLinear;
                DirectDL.this.calcVolume();
            }

            float getLinearGain() {
                return this.linearGain;
            }
        }

        /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectDL$Mute.class */
        private final class Mute extends BooleanControl {
            private Mute() {
                super(BooleanControl.Type.MUTE, false, "True", "False");
            }

            @Override // javax.sound.sampled.BooleanControl
            public void setValue(boolean z2) {
                super.setValue(z2);
                DirectDL.this.calcVolume();
            }
        }

        /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectDL$Balance.class */
        private final class Balance extends FloatControl {
            private Balance() {
                super(FloatControl.Type.BALANCE, -1.0f, 1.0f, 0.0078125f, -1, 0.0f, "", "Left", BorderLayout.CENTER, "Right");
            }

            @Override // javax.sound.sampled.FloatControl
            public void setValue(float f2) {
                setValueImpl(f2);
                DirectDL.this.panControl.setValueImpl(f2);
                DirectDL.this.calcVolume();
            }

            void setValueImpl(float f2) {
                super.setValue(f2);
            }
        }

        /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectDL$Pan.class */
        private final class Pan extends FloatControl {
            private Pan() {
                super(FloatControl.Type.PAN, -1.0f, 1.0f, 0.0078125f, -1, 0.0f, "", "Left", BorderLayout.CENTER, "Right");
            }

            @Override // javax.sound.sampled.FloatControl
            public void setValue(float f2) {
                setValueImpl(f2);
                DirectDL.this.balanceControl.setValueImpl(f2);
                DirectDL.this.calcVolume();
            }

            void setValueImpl(float f2) {
                super.setValue(f2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectSDL.class */
    private static final class DirectSDL extends DirectDL implements SourceDataLine {
        private DirectSDL(DataLine.Info info, AudioFormat audioFormat, int i2, DirectAudioDevice directAudioDevice) {
            super(info, directAudioDevice, audioFormat, i2, directAudioDevice.getMixerIndex(), directAudioDevice.getDeviceID(), true);
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectTDL.class */
    private static final class DirectTDL extends DirectDL implements TargetDataLine {
        private DirectTDL(DataLine.Info info, AudioFormat audioFormat, int i2, DirectAudioDevice directAudioDevice) {
            super(info, directAudioDevice, audioFormat, i2, directAudioDevice.getMixerIndex(), directAudioDevice.getDeviceID(), false);
        }

        @Override // javax.sound.sampled.TargetDataLine
        public int read(byte[] bArr, int i2, int i3) {
            this.flushing = false;
            if (i3 == 0) {
                return 0;
            }
            if (i3 < 0) {
                throw new IllegalArgumentException("illegal len: " + i3);
            }
            if (i3 % getFormat().getFrameSize() != 0) {
                throw new IllegalArgumentException("illegal request to read non-integral number of frames (" + i3 + " bytes, frameSize = " + getFormat().getFrameSize() + " bytes)");
            }
            if (i2 < 0) {
                throw new ArrayIndexOutOfBoundsException(i2);
            }
            if (i2 + i3 > bArr.length) {
                throw new ArrayIndexOutOfBoundsException(bArr.length);
            }
            synchronized (this.lock) {
                if (!isActive() && this.doIO) {
                    setActive(true);
                    setStarted(true);
                }
            }
            int i4 = 0;
            while (true) {
                if (this.doIO && !this.flushing) {
                    synchronized (this.lockNative) {
                        int iNRead = DirectAudioDevice.nRead(this.id, bArr, i2, i3, this.softwareConversionSize);
                        if (iNRead >= 0) {
                            this.bytePosition += iNRead;
                            if (iNRead > 0) {
                                this.drained = false;
                            }
                            i3 -= iNRead;
                            i4 += iNRead;
                            if (i3 <= 0) {
                                break;
                            }
                            i2 += iNRead;
                            synchronized (this.lock) {
                                try {
                                    this.lock.wait(this.waitTime);
                                } catch (InterruptedException e2) {
                                }
                            }
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
            if (this.flushing) {
                i4 = 0;
            }
            return i4;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectClip.class */
    private static final class DirectClip extends DirectDL implements Clip, Runnable, AutoClosingClip {
        private volatile Thread thread;
        private volatile byte[] audioData;
        private volatile int frameSize;
        private volatile int m_lengthInFrames;
        private volatile int loopCount;
        private volatile int clipBytePosition;
        private volatile int newFramePosition;
        private volatile int loopStartFrame;
        private volatile int loopEndFrame;
        private boolean autoclosing;

        private DirectClip(DataLine.Info info, AudioFormat audioFormat, int i2, DirectAudioDevice directAudioDevice) {
            super(info, directAudioDevice, audioFormat, i2, directAudioDevice.getMixerIndex(), directAudioDevice.getDeviceID(), true);
            this.audioData = null;
            this.autoclosing = false;
        }

        @Override // javax.sound.sampled.Clip
        public void open(AudioFormat audioFormat, byte[] bArr, int i2, int i3) throws LineUnavailableException {
            Toolkit.isFullySpecifiedAudioFormat(audioFormat);
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
            open(audioFormat, bArr2, i3 / audioFormat.getFrameSize());
        }

        private void open(AudioFormat audioFormat, byte[] bArr, int i2) throws LineUnavailableException {
            Toolkit.isFullySpecifiedAudioFormat(audioFormat);
            synchronized (this.mixer) {
                if (isOpen()) {
                    throw new IllegalStateException("Clip is already open with format " + ((Object) getFormat()) + " and frame lengh of " + getFrameLength());
                }
                this.audioData = bArr;
                this.frameSize = audioFormat.getFrameSize();
                this.m_lengthInFrames = i2;
                this.bytePosition = 0L;
                this.clipBytePosition = 0;
                this.newFramePosition = -1;
                this.loopStartFrame = 0;
                this.loopEndFrame = i2 - 1;
                this.loopCount = 0;
                try {
                    open(audioFormat, (int) Toolkit.millis2bytes(audioFormat, 1000L));
                    this.thread = JSSecurityManager.createThread(this, "Direct Clip", true, 6, false);
                    this.thread.start();
                } catch (IllegalArgumentException e2) {
                    this.audioData = null;
                    throw e2;
                } catch (LineUnavailableException e3) {
                    this.audioData = null;
                    throw e3;
                }
            }
            if (isAutoClosing()) {
                getEventDispatcher().autoClosingClipOpened(this);
            }
        }

        @Override // javax.sound.sampled.Clip
        public void open(AudioInputStream audioInputStream) throws LineUnavailableException, IOException {
            byte[] internalBuffer;
            Toolkit.isFullySpecifiedAudioFormat(audioInputStream.getFormat());
            synchronized (this.mixer) {
                if (isOpen()) {
                    throw new IllegalStateException("Clip is already open with format " + ((Object) getFormat()) + " and frame lengh of " + getFrameLength());
                }
                int frameLength = (int) audioInputStream.getFrameLength();
                int i2 = 0;
                int frameSize = audioInputStream.getFormat().getFrameSize();
                if (frameLength != -1) {
                    int i3 = frameLength * frameSize;
                    if (i3 < 0) {
                        throw new IllegalArgumentException("Audio data < 0");
                    }
                    try {
                        internalBuffer = new byte[i3];
                        int i4 = i3;
                        int i5 = 0;
                        while (i4 > 0 && i5 >= 0) {
                            i5 = audioInputStream.read(internalBuffer, i2, i4);
                            if (i5 > 0) {
                                i2 += i5;
                                i4 -= i5;
                            } else if (i5 == 0) {
                                Thread.yield();
                            }
                        }
                        open(audioInputStream.getFormat(), internalBuffer, i2 / frameSize);
                    } catch (OutOfMemoryError e2) {
                        throw new IOException("Audio data is too big");
                    }
                } else {
                    int iMax = Math.max(16384, frameSize);
                    DirectBAOS directBAOS = new DirectBAOS();
                    try {
                        byte[] bArr = new byte[iMax];
                        int i6 = 0;
                        while (i6 >= 0) {
                            i6 = audioInputStream.read(bArr, 0, bArr.length);
                            if (i6 > 0) {
                                directBAOS.write(bArr, 0, i6);
                                i2 += i6;
                            } else if (i6 == 0) {
                                Thread.yield();
                            }
                        }
                        internalBuffer = directBAOS.getInternalBuffer();
                        open(audioInputStream.getFormat(), internalBuffer, i2 / frameSize);
                    } catch (OutOfMemoryError e3) {
                        throw new IOException("Audio data is too big");
                    }
                }
            }
        }

        @Override // javax.sound.sampled.Clip
        public int getFrameLength() {
            return this.m_lengthInFrames;
        }

        @Override // javax.sound.sampled.Clip
        public long getMicrosecondLength() {
            return Toolkit.frames2micros(getFormat(), getFrameLength());
        }

        @Override // javax.sound.sampled.Clip
        public void setFramePosition(int i2) {
            if (i2 < 0) {
                i2 = 0;
            } else if (i2 >= getFrameLength()) {
                i2 = getFrameLength();
            }
            if (this.doIO) {
                this.newFramePosition = i2;
            } else {
                this.clipBytePosition = i2 * this.frameSize;
                this.newFramePosition = -1;
            }
            this.bytePosition = i2 * this.frameSize;
            flush();
            synchronized (this.lockNative) {
                DirectAudioDevice.nSetBytePosition(this.id, this.isSource, i2 * this.frameSize);
            }
        }

        @Override // com.sun.media.sound.DirectAudioDevice.DirectDL, com.sun.media.sound.AbstractLine, javax.sound.sampled.DataLine
        public long getLongFramePosition() {
            return super.getLongFramePosition();
        }

        @Override // javax.sound.sampled.Clip
        public synchronized void setMicrosecondPosition(long j2) {
            setFramePosition((int) Toolkit.micros2frames(getFormat(), j2));
        }

        @Override // javax.sound.sampled.Clip
        public void setLoopPoints(int i2, int i3) {
            if (i2 < 0 || i2 >= getFrameLength()) {
                throw new IllegalArgumentException("illegal value for start: " + i2);
            }
            if (i3 >= getFrameLength()) {
                throw new IllegalArgumentException("illegal value for end: " + i3);
            }
            if (i3 == -1) {
                i3 = getFrameLength() - 1;
                if (i3 < 0) {
                    i3 = 0;
                }
            }
            if (i3 < i2) {
                throw new IllegalArgumentException("End position " + i3 + "  preceeds start position " + i2);
            }
            this.loopStartFrame = i2;
            this.loopEndFrame = i3;
        }

        @Override // javax.sound.sampled.Clip
        public void loop(int i2) {
            this.loopCount = i2;
            start();
        }

        @Override // com.sun.media.sound.DirectAudioDevice.DirectDL, com.sun.media.sound.AbstractDataLine
        void implOpen(AudioFormat audioFormat, int i2) throws LineUnavailableException, SecurityException {
            if (this.audioData == null) {
                throw new IllegalArgumentException("illegal call to open() in interface Clip");
            }
            super.implOpen(audioFormat, i2);
        }

        @Override // com.sun.media.sound.DirectAudioDevice.DirectDL, com.sun.media.sound.AbstractDataLine
        void implClose() throws SecurityException {
            Thread thread = this.thread;
            this.thread = null;
            this.doIO = false;
            if (thread != null) {
                synchronized (this.lock) {
                    this.lock.notifyAll();
                }
                try {
                    thread.join(2000L);
                } catch (InterruptedException e2) {
                }
            }
            super.implClose();
            this.audioData = null;
            this.newFramePosition = -1;
            getEventDispatcher().autoClosingClipClosed(this);
        }

        @Override // com.sun.media.sound.DirectAudioDevice.DirectDL, com.sun.media.sound.AbstractDataLine
        void implStart() throws SecurityException {
            super.implStart();
        }

        @Override // com.sun.media.sound.DirectAudioDevice.DirectDL, com.sun.media.sound.AbstractDataLine
        void implStop() throws SecurityException {
            super.implStop();
            this.loopCount = 0;
        }

        @Override // java.lang.Runnable
        public void run() {
            Thread threadCurrentThread = Thread.currentThread();
            while (this.thread == threadCurrentThread) {
                synchronized (this.lock) {
                    while (!this.doIO && this.thread == threadCurrentThread) {
                        try {
                            this.lock.wait();
                        } catch (InterruptedException e2) {
                        }
                    }
                }
                while (this.doIO && this.thread == threadCurrentThread) {
                    if (this.newFramePosition >= 0) {
                        this.clipBytePosition = this.newFramePosition * this.frameSize;
                        this.newFramePosition = -1;
                    }
                    int frameLength = getFrameLength() - 1;
                    if (this.loopCount > 0 || this.loopCount == -1) {
                        frameLength = this.loopEndFrame;
                    }
                    int iAlign = ((int) ((frameLength - (this.clipBytePosition / this.frameSize)) + 1)) * this.frameSize;
                    if (iAlign > getBufferSize()) {
                        iAlign = Toolkit.align(getBufferSize(), this.frameSize);
                    }
                    int iWrite = write(this.audioData, this.clipBytePosition, iAlign);
                    this.clipBytePosition += iWrite;
                    if (this.doIO && this.newFramePosition < 0 && iWrite >= 0 && this.clipBytePosition / this.frameSize > frameLength) {
                        if (this.loopCount > 0 || this.loopCount == -1) {
                            if (this.loopCount != -1) {
                                this.loopCount--;
                            }
                            this.newFramePosition = this.loopStartFrame;
                        } else {
                            drain();
                            stop();
                        }
                    }
                }
            }
        }

        @Override // com.sun.media.sound.AutoClosingClip
        public boolean isAutoClosing() {
            return this.autoclosing;
        }

        @Override // com.sun.media.sound.AutoClosingClip
        public void setAutoClosing(boolean z2) {
            if (z2 != this.autoclosing) {
                if (isOpen()) {
                    if (z2) {
                        getEventDispatcher().autoClosingClipOpened(this);
                    } else {
                        getEventDispatcher().autoClosingClipClosed(this);
                    }
                }
                this.autoclosing = z2;
            }
        }

        @Override // com.sun.media.sound.DirectAudioDevice.DirectDL
        protected boolean requiresServicing() {
            return false;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/DirectAudioDevice$DirectBAOS.class */
    private static class DirectBAOS extends ByteArrayOutputStream {
        DirectBAOS() {
        }

        public byte[] getInternalBuffer() {
            return this.buf;
        }
    }
}
