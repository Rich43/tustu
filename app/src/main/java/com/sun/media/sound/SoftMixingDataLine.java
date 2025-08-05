package com.sun.media.sound;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import sun.security.x509.IssuingDistributionPointExtension;

/* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine.class */
public abstract class SoftMixingDataLine implements DataLine {
    public static final FloatControl.Type CHORUS_SEND = new FloatControl.Type("Chorus Send") { // from class: com.sun.media.sound.SoftMixingDataLine.1
    };
    final Object control_mutex;
    SoftMixingMixer mixer;
    DataLine.Info info;
    private final Gain gain_control = new Gain();
    private final Mute mute_control = new Mute();
    private final Balance balance_control = new Balance();
    private final Pan pan_control = new Pan();
    private final ReverbSend reverbsend_control = new ReverbSend();
    private final ChorusSend chorussend_control = new ChorusSend();
    private final ApplyReverb apply_reverb = new ApplyReverb();
    float leftgain = 1.0f;
    float rightgain = 1.0f;
    float eff1gain = 0.0f;
    float eff2gain = 0.0f;
    List<LineListener> listeners = new ArrayList();
    private final Control[] controls = {this.gain_control, this.mute_control, this.balance_control, this.pan_control, this.reverbsend_control, this.chorussend_control, this.apply_reverb};

    protected abstract void processControlLogic();

    protected abstract void processAudioLogic(SoftAudioBuffer[] softAudioBufferArr);

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$AudioFloatInputStreamResampler.class */
    protected static final class AudioFloatInputStreamResampler extends AudioFloatInputStream {
        private final AudioFloatInputStream ais;
        private final AudioFormat targetFormat;
        private float[] skipbuffer;
        private SoftAbstractResampler resampler;
        private final float[] ibuffer2;
        private final float[][] ibuffer;
        private float ibuffer_index;
        private int ibuffer_len;
        private int nrofchannels;
        private float[][] cbuffer;
        private final int pad;
        private final int pad2;
        private final float[] pitch = new float[1];
        private final int buffer_len = 512;
        private final float[] ix = new float[1];
        private final int[] ox = new int[1];
        private float[][] mark_ibuffer = (float[][]) null;
        private float mark_ibuffer_index = 0.0f;
        private int mark_ibuffer_len = 0;

        public AudioFloatInputStreamResampler(AudioFloatInputStream audioFloatInputStream, AudioFormat audioFormat) {
            this.ibuffer_index = 0.0f;
            this.ibuffer_len = 0;
            this.nrofchannels = 0;
            this.ais = audioFloatInputStream;
            AudioFormat format = audioFloatInputStream.getFormat();
            this.targetFormat = new AudioFormat(format.getEncoding(), audioFormat.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), audioFormat.getSampleRate(), format.isBigEndian());
            this.nrofchannels = this.targetFormat.getChannels();
            Object property = audioFormat.getProperty("interpolation");
            if (property != null && (property instanceof String)) {
                String str = (String) property;
                if (str.equalsIgnoreCase(IssuingDistributionPointExtension.POINT)) {
                    this.resampler = new SoftPointResampler();
                }
                if (str.equalsIgnoreCase("linear")) {
                    this.resampler = new SoftLinearResampler2();
                }
                if (str.equalsIgnoreCase("linear1")) {
                    this.resampler = new SoftLinearResampler();
                }
                if (str.equalsIgnoreCase("linear2")) {
                    this.resampler = new SoftLinearResampler2();
                }
                if (str.equalsIgnoreCase("cubic")) {
                    this.resampler = new SoftCubicResampler();
                }
                if (str.equalsIgnoreCase("lanczos")) {
                    this.resampler = new SoftLanczosResampler();
                }
                if (str.equalsIgnoreCase("sinc")) {
                    this.resampler = new SoftSincResampler();
                }
            }
            if (this.resampler == null) {
                this.resampler = new SoftLinearResampler2();
            }
            this.pitch[0] = format.getSampleRate() / audioFormat.getSampleRate();
            this.pad = this.resampler.getPadding();
            this.pad2 = this.pad * 2;
            this.ibuffer = new float[this.nrofchannels][512 + this.pad2];
            this.ibuffer2 = new float[this.nrofchannels * 512];
            this.ibuffer_index = 512 + this.pad;
            this.ibuffer_len = 512;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public int available() throws IOException {
            return 0;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void close() throws IOException {
            this.ais.close();
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public AudioFormat getFormat() {
            return this.targetFormat;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long getFrameLength() {
            return -1L;
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void mark(int i2) {
            this.ais.mark((int) (i2 * this.pitch[0]));
            this.mark_ibuffer_index = this.ibuffer_index;
            this.mark_ibuffer_len = this.ibuffer_len;
            if (this.mark_ibuffer == null) {
                this.mark_ibuffer = new float[this.ibuffer.length][this.ibuffer[0].length];
            }
            for (int i3 = 0; i3 < this.ibuffer.length; i3++) {
                float[] fArr = this.ibuffer[i3];
                float[] fArr2 = this.mark_ibuffer[i3];
                for (int i4 = 0; i4 < fArr2.length; i4++) {
                    fArr2[i4] = fArr[i4];
                }
            }
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public boolean markSupported() {
            return this.ais.markSupported();
        }

        private void readNextBuffer() throws IOException {
            int i2;
            if (this.ibuffer_len == -1) {
                return;
            }
            for (int i3 = 0; i3 < this.nrofchannels; i3++) {
                float[] fArr = this.ibuffer[i3];
                int i4 = this.ibuffer_len + this.pad2;
                int i5 = this.ibuffer_len;
                int i6 = 0;
                while (i5 < i4) {
                    fArr[i6] = fArr[i5];
                    i5++;
                    i6++;
                }
            }
            this.ibuffer_index -= this.ibuffer_len;
            this.ibuffer_len = this.ais.read(this.ibuffer2);
            if (this.ibuffer_len >= 0) {
                while (this.ibuffer_len < this.ibuffer2.length && (i2 = this.ais.read(this.ibuffer2, this.ibuffer_len, this.ibuffer2.length - this.ibuffer_len)) != -1) {
                    this.ibuffer_len += i2;
                }
                Arrays.fill(this.ibuffer2, this.ibuffer_len, this.ibuffer2.length, 0.0f);
                this.ibuffer_len /= this.nrofchannels;
            } else {
                Arrays.fill(this.ibuffer2, 0, this.ibuffer2.length, 0.0f);
            }
            int length = this.ibuffer2.length;
            for (int i7 = 0; i7 < this.nrofchannels; i7++) {
                float[] fArr2 = this.ibuffer[i7];
                int i8 = i7;
                int i9 = this.pad2;
                while (i8 < length) {
                    fArr2[i9] = this.ibuffer2[i8];
                    i8 += this.nrofchannels;
                    i9++;
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0092 A[PHI: r17
  0x0092: PHI (r17v3 int) = (r17v2 int), (r17v4 int) binds: [B:25:0x007b, B:27:0x008c] A[DONT_GENERATE, DONT_INLINE]] */
        @Override // com.sun.media.sound.AudioFloatInputStream
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int read(float[] r12, int r13, int r14) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 351
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.SoftMixingDataLine.AudioFloatInputStreamResampler.read(float[], int, int):int");
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public void reset() throws IOException {
            this.ais.reset();
            if (this.mark_ibuffer == null) {
                return;
            }
            this.ibuffer_index = this.mark_ibuffer_index;
            this.ibuffer_len = this.mark_ibuffer_len;
            for (int i2 = 0; i2 < this.ibuffer.length; i2++) {
                float[] fArr = this.mark_ibuffer[i2];
                float[] fArr2 = this.ibuffer[i2];
                for (int i3 = 0; i3 < fArr2.length; i3++) {
                    fArr2[i3] = fArr[i3];
                }
            }
        }

        @Override // com.sun.media.sound.AudioFloatInputStream
        public long skip(long j2) throws IOException {
            long j3;
            if (j2 > 0) {
                return 0L;
            }
            if (this.skipbuffer == null) {
                this.skipbuffer = new float[1024 * this.targetFormat.getFrameSize()];
            }
            float[] fArr = this.skipbuffer;
            long j4 = j2;
            while (true) {
                j3 = j4;
                if (j3 <= 0) {
                    break;
                }
                int i2 = read(fArr, 0, (int) Math.min(j3, this.skipbuffer.length));
                if (i2 >= 0) {
                    j4 = j3 - i2;
                } else if (j3 == j2) {
                    return i2;
                }
            }
            return j2 - j3;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$Gain.class */
    private final class Gain extends FloatControl {
        private Gain() {
            super(FloatControl.Type.MASTER_GAIN, -80.0f, 6.0206f, 0.625f, -1, 0.0f, "dB", "Minimum", "", "Maximum");
        }

        @Override // javax.sound.sampled.FloatControl
        public void setValue(float f2) {
            super.setValue(f2);
            SoftMixingDataLine.this.calcVolume();
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$Mute.class */
    private final class Mute extends BooleanControl {
        private Mute() {
            super(BooleanControl.Type.MUTE, false, "True", "False");
        }

        @Override // javax.sound.sampled.BooleanControl
        public void setValue(boolean z2) {
            super.setValue(z2);
            SoftMixingDataLine.this.calcVolume();
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$ApplyReverb.class */
    private final class ApplyReverb extends BooleanControl {
        private ApplyReverb() {
            super(BooleanControl.Type.APPLY_REVERB, false, "True", "False");
        }

        @Override // javax.sound.sampled.BooleanControl
        public void setValue(boolean z2) {
            super.setValue(z2);
            SoftMixingDataLine.this.calcVolume();
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$Balance.class */
    private final class Balance extends FloatControl {
        private Balance() {
            super(FloatControl.Type.BALANCE, -1.0f, 1.0f, 0.0078125f, -1, 0.0f, "", "Left", BorderLayout.CENTER, "Right");
        }

        @Override // javax.sound.sampled.FloatControl
        public void setValue(float f2) {
            super.setValue(f2);
            SoftMixingDataLine.this.calcVolume();
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$Pan.class */
    private final class Pan extends FloatControl {
        private Pan() {
            super(FloatControl.Type.PAN, -1.0f, 1.0f, 0.0078125f, -1, 0.0f, "", "Left", BorderLayout.CENTER, "Right");
        }

        @Override // javax.sound.sampled.FloatControl
        public void setValue(float f2) {
            super.setValue(f2);
            SoftMixingDataLine.this.balance_control.setValue(f2);
        }

        @Override // javax.sound.sampled.FloatControl
        public float getValue() {
            return SoftMixingDataLine.this.balance_control.getValue();
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$ReverbSend.class */
    private final class ReverbSend extends FloatControl {
        private ReverbSend() {
            super(FloatControl.Type.REVERB_SEND, -80.0f, 6.0206f, 0.625f, -1, -80.0f, "dB", "Minimum", "", "Maximum");
        }

        @Override // javax.sound.sampled.FloatControl
        public void setValue(float f2) {
            super.setValue(f2);
            SoftMixingDataLine.this.balance_control.setValue(f2);
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftMixingDataLine$ChorusSend.class */
    private final class ChorusSend extends FloatControl {
        private ChorusSend() {
            super(SoftMixingDataLine.CHORUS_SEND, -80.0f, 6.0206f, 0.625f, -1, -80.0f, "dB", "Minimum", "", "Maximum");
        }

        @Override // javax.sound.sampled.FloatControl
        public void setValue(float f2) {
            super.setValue(f2);
            SoftMixingDataLine.this.balance_control.setValue(f2);
        }
    }

    SoftMixingDataLine(SoftMixingMixer softMixingMixer, DataLine.Info info) {
        this.mixer = softMixingMixer;
        this.info = info;
        this.control_mutex = softMixingMixer.control_mutex;
        calcVolume();
    }

    final void calcVolume() {
        synchronized (this.control_mutex) {
            double dPow = Math.pow(10.0d, this.gain_control.getValue() / 20.0d);
            if (this.mute_control.getValue()) {
                dPow = 0.0d;
            }
            this.leftgain = (float) dPow;
            this.rightgain = (float) dPow;
            if (this.mixer.getFormat().getChannels() > 1) {
                double value = this.balance_control.getValue();
                if (value > 0.0d) {
                    this.leftgain = (float) (this.leftgain * (1.0d - value));
                } else {
                    this.rightgain = (float) (this.rightgain * (1.0d + value));
                }
            }
        }
        this.eff1gain = (float) Math.pow(10.0d, this.reverbsend_control.getValue() / 20.0d);
        this.eff2gain = (float) Math.pow(10.0d, this.chorussend_control.getValue() / 20.0d);
        if (!this.apply_reverb.getValue()) {
            this.eff1gain = 0.0f;
        }
    }

    final void sendEvent(LineEvent lineEvent) {
        if (this.listeners.size() == 0) {
            return;
        }
        for (LineListener lineListener : (LineListener[]) this.listeners.toArray(new LineListener[this.listeners.size()])) {
            lineListener.update(lineEvent);
        }
    }

    @Override // javax.sound.sampled.Line
    public final void addLineListener(LineListener lineListener) {
        synchronized (this.control_mutex) {
            this.listeners.add(lineListener);
        }
    }

    @Override // javax.sound.sampled.Line
    public final void removeLineListener(LineListener lineListener) {
        synchronized (this.control_mutex) {
            this.listeners.add(lineListener);
        }
    }

    @Override // javax.sound.sampled.Line
    public final Line.Info getLineInfo() {
        return this.info;
    }

    @Override // javax.sound.sampled.Line
    public final Control getControl(Control.Type type) {
        if (type != null) {
            for (int i2 = 0; i2 < this.controls.length; i2++) {
                if (this.controls[i2].getType() == type) {
                    return this.controls[i2];
                }
            }
        }
        throw new IllegalArgumentException("Unsupported control type : " + ((Object) type));
    }

    @Override // javax.sound.sampled.Line
    public final Control[] getControls() {
        return (Control[]) Arrays.copyOf(this.controls, this.controls.length);
    }

    @Override // javax.sound.sampled.Line
    public final boolean isControlSupported(Control.Type type) {
        if (type != null) {
            for (int i2 = 0; i2 < this.controls.length; i2++) {
                if (this.controls[i2].getType() == type) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
