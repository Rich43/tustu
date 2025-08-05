package com.sun.media.sound;

import com.sun.javafx.scene.text.TextLayout;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Patch;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:com/sun/media/sound/SoftMainMixer.class */
public final class SoftMainMixer {
    public static final int CHANNEL_LEFT = 0;
    public static final int CHANNEL_RIGHT = 1;
    public static final int CHANNEL_MONO = 2;
    public static final int CHANNEL_DELAY_LEFT = 3;
    public static final int CHANNEL_DELAY_RIGHT = 4;
    public static final int CHANNEL_DELAY_MONO = 5;
    public static final int CHANNEL_EFFECT1 = 6;
    public static final int CHANNEL_EFFECT2 = 7;
    public static final int CHANNEL_DELAY_EFFECT1 = 8;
    public static final int CHANNEL_DELAY_EFFECT2 = 9;
    public static final int CHANNEL_LEFT_DRY = 10;
    public static final int CHANNEL_RIGHT_DRY = 11;
    public static final int CHANNEL_SCRATCH1 = 12;
    public static final int CHANNEL_SCRATCH2 = 13;
    private long sample_pos;
    private final Object control_mutex;
    private SoftSynthesizer synth;
    private float samplerate;
    private int nrofchannels;
    private SoftVoice[] voicestatus;
    private SoftAudioBuffer[] buffers;
    private SoftReverb reverb;
    private SoftAudioProcessor chorus;
    private SoftAudioProcessor agc;
    private long msec_buffer_len;
    private int buffer_len;
    private int max_delay_midievent;
    private AudioInputStream ais;
    boolean active_sensing_on = false;
    private long msec_last_activity = -1;
    private boolean pusher_silent = false;
    private int pusher_silent_count = 0;
    boolean readfully = true;
    TreeMap<Long, Object> midimessages = new TreeMap<>();
    private int delay_midievent = 0;
    double last_volume_left = 1.0d;
    double last_volume_right = 1.0d;
    private double[] co_master_balance = new double[1];
    private double[] co_master_volume = new double[1];
    private double[] co_master_coarse_tuning = new double[1];
    private double[] co_master_fine_tuning = new double[1];
    private Set<SoftChannelMixerContainer> registeredMixers = null;
    private Set<ModelChannelMixer> stoppedMixers = null;
    private SoftChannelMixerContainer[] cur_registeredMixers = null;
    SoftControl co_master = new SoftControl() { // from class: com.sun.media.sound.SoftMainMixer.1
        double[] balance;
        double[] volume;
        double[] coarse_tuning;
        double[] fine_tuning;

        {
            this.balance = SoftMainMixer.this.co_master_balance;
            this.volume = SoftMainMixer.this.co_master_volume;
            this.coarse_tuning = SoftMainMixer.this.co_master_coarse_tuning;
            this.fine_tuning = SoftMainMixer.this.co_master_fine_tuning;
        }

        @Override // com.sun.media.sound.SoftControl
        public double[] get(int i2, String str) {
            if (str == null) {
                return null;
            }
            if (str.equals("balance")) {
                return this.balance;
            }
            if (str.equals("volume")) {
                return this.volume;
            }
            if (str.equals("coarse_tuning")) {
                return this.coarse_tuning;
            }
            if (str.equals("fine_tuning")) {
                return this.fine_tuning;
            }
            return null;
        }
    };

    /* loaded from: rt.jar:com/sun/media/sound/SoftMainMixer$SoftChannelMixerContainer.class */
    private class SoftChannelMixerContainer {
        ModelChannelMixer mixer;
        SoftAudioBuffer[] buffers;

        private SoftChannelMixerContainer() {
        }
    }

    private void processSystemExclusiveMessage(byte[] bArr) {
        int i2;
        int i3;
        synchronized (this.synth.control_mutex) {
            activity();
            if ((bArr[1] & 255) == 126 && ((i3 = bArr[2] & 255) == 127 || i3 == this.synth.getDeviceID())) {
                switch (bArr[3] & 255) {
                    case 8:
                        switch (bArr[4] & 255) {
                            case 1:
                                this.synth.getTuning(new Patch(0, bArr[5] & 255)).load(bArr);
                                break;
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                this.synth.getTuning(new Patch(bArr[5] & 255, bArr[6] & 255)).load(bArr);
                                break;
                            case 8:
                            case 9:
                                SoftTuning softTuning = new SoftTuning(bArr);
                                int i4 = ((bArr[5] & 255) * 16384) + ((bArr[6] & 255) * 128) + (bArr[7] & 255);
                                SoftChannel[] softChannelArr = this.synth.channels;
                                for (int i5 = 0; i5 < softChannelArr.length; i5++) {
                                    if ((i4 & (1 << i5)) != 0) {
                                        softChannelArr[i5].tuning = softTuning;
                                    }
                                }
                                break;
                        }
                    case 9:
                        switch (bArr[4] & 255) {
                            case 1:
                                this.synth.setGeneralMidiMode(1);
                                reset();
                                break;
                            case 2:
                                this.synth.setGeneralMidiMode(0);
                                reset();
                                break;
                            case 3:
                                this.synth.setGeneralMidiMode(2);
                                reset();
                                break;
                        }
                    case 10:
                        switch (bArr[4] & 255) {
                            case 1:
                                if (this.synth.getGeneralMidiMode() == 0) {
                                    this.synth.setGeneralMidiMode(1);
                                }
                                this.synth.voice_allocation_mode = 1;
                                reset();
                                break;
                            case 2:
                                this.synth.setGeneralMidiMode(0);
                                this.synth.voice_allocation_mode = 0;
                                reset();
                                break;
                            case 3:
                                this.synth.voice_allocation_mode = 0;
                                break;
                            case 4:
                                this.synth.voice_allocation_mode = 1;
                                break;
                        }
                }
            }
            if ((bArr[1] & 255) == 127 && ((i2 = bArr[2] & 255) == 127 || i2 == this.synth.getDeviceID())) {
                switch (bArr[3] & 255) {
                    case 4:
                        int i6 = bArr[4] & 255;
                        switch (i6) {
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                                int i7 = (bArr[5] & Byte.MAX_VALUE) + ((bArr[6] & Byte.MAX_VALUE) * 128);
                                if (i6 == 1) {
                                    setVolume(i7);
                                    break;
                                } else if (i6 == 2) {
                                    setBalance(i7);
                                    break;
                                } else if (i6 == 3) {
                                    setFineTuning(i7);
                                    break;
                                } else if (i6 == 4) {
                                    setCoarseTuning(i7);
                                    break;
                                }
                                break;
                            case 5:
                                int i8 = 5 + 1;
                                int i9 = bArr[5] & 255;
                                int i10 = i8 + 1;
                                int i11 = bArr[i8] & 255;
                                int i12 = i10 + 1;
                                int i13 = bArr[i10] & 255;
                                int[] iArr = new int[i9];
                                for (int i14 = 0; i14 < i9; i14++) {
                                    int i15 = i12;
                                    int i16 = i12 + 1;
                                    i12 = i16 + 1;
                                    iArr[i14] = ((bArr[i15] & 255) * 128) + (bArr[i16] & 255);
                                }
                                int length = ((bArr.length - 1) - i12) / (i11 + i13);
                                long[] jArr = new long[length];
                                long[] jArr2 = new long[length];
                                for (int i17 = 0; i17 < length; i17++) {
                                    jArr2[i17] = 0;
                                    for (int i18 = 0; i18 < i11; i18++) {
                                        int i19 = i12;
                                        i12++;
                                        jArr[i17] = (jArr[i17] * 128) + (bArr[i19] & 255);
                                    }
                                    for (int i20 = 0; i20 < i13; i20++) {
                                        int i21 = i12;
                                        i12++;
                                        jArr2[i17] = (jArr2[i17] * 128) + (bArr[i21] & 255);
                                    }
                                }
                                globalParameterControlChange(iArr, jArr, jArr2);
                                break;
                        }
                    case 8:
                        switch (bArr[4] & 255) {
                            case 2:
                                SoftTuning tuning = this.synth.getTuning(new Patch(0, bArr[5] & 255));
                                tuning.load(bArr);
                                SoftVoice[] voices = this.synth.getVoices();
                                for (int i22 = 0; i22 < voices.length; i22++) {
                                    if (voices[i22].active && voices[i22].tuning == tuning) {
                                        voices[i22].updateTuning(tuning);
                                    }
                                }
                                break;
                            case 7:
                                SoftTuning tuning2 = this.synth.getTuning(new Patch(bArr[5] & 255, bArr[6] & 255));
                                tuning2.load(bArr);
                                SoftVoice[] voices2 = this.synth.getVoices();
                                for (int i23 = 0; i23 < voices2.length; i23++) {
                                    if (voices2[i23].active && voices2[i23].tuning == tuning2) {
                                        voices2[i23].updateTuning(tuning2);
                                    }
                                }
                                break;
                            case 8:
                            case 9:
                                SoftTuning softTuning2 = new SoftTuning(bArr);
                                int i24 = ((bArr[5] & 255) * 16384) + ((bArr[6] & 255) * 128) + (bArr[7] & 255);
                                SoftChannel[] softChannelArr2 = this.synth.channels;
                                for (int i25 = 0; i25 < softChannelArr2.length; i25++) {
                                    if ((i24 & (1 << i25)) != 0) {
                                        softChannelArr2[i25].tuning = softTuning2;
                                    }
                                }
                                SoftVoice[] voices3 = this.synth.getVoices();
                                for (int i26 = 0; i26 < voices3.length; i26++) {
                                    if (voices3[i26].active && (i24 & (1 << voices3[i26].channel)) != 0) {
                                        voices3[i26].updateTuning(softTuning2);
                                    }
                                }
                                break;
                        }
                    case 9:
                        switch (bArr[4] & 255) {
                            case 1:
                                int[] iArr2 = new int[(bArr.length - 7) / 2];
                                int[] iArr3 = new int[(bArr.length - 7) / 2];
                                int i27 = 0;
                                for (int i28 = 6; i28 < bArr.length - 1; i28 += 2) {
                                    iArr2[i27] = bArr[i28] & 255;
                                    iArr3[i27] = bArr[i28 + 1] & 255;
                                    i27++;
                                }
                                this.synth.channels[bArr[5] & 255].mapChannelPressureToDestination(iArr2, iArr3);
                                break;
                            case 2:
                                int[] iArr4 = new int[(bArr.length - 7) / 2];
                                int[] iArr5 = new int[(bArr.length - 7) / 2];
                                int i29 = 0;
                                for (int i30 = 6; i30 < bArr.length - 1; i30 += 2) {
                                    iArr4[i29] = bArr[i30] & 255;
                                    iArr5[i29] = bArr[i30 + 1] & 255;
                                    i29++;
                                }
                                this.synth.channels[bArr[5] & 255].mapPolyPressureToDestination(iArr4, iArr5);
                                break;
                            case 3:
                                int[] iArr6 = new int[(bArr.length - 7) / 2];
                                int[] iArr7 = new int[(bArr.length - 7) / 2];
                                int i31 = 0;
                                for (int i32 = 7; i32 < bArr.length - 1; i32 += 2) {
                                    iArr6[i31] = bArr[i32] & 255;
                                    iArr7[i31] = bArr[i32 + 1] & 255;
                                    i31++;
                                }
                                this.synth.channels[bArr[5] & 255].mapControlToDestination(bArr[6] & 255, iArr6, iArr7);
                                break;
                        }
                    case 10:
                        switch (bArr[4] & 255) {
                            case 1:
                                int i33 = bArr[5] & 255;
                                int i34 = bArr[6] & 255;
                                SoftChannel softChannel = this.synth.channels[i33];
                                for (int i35 = 7; i35 < bArr.length - 1; i35 += 2) {
                                    softChannel.controlChangePerNote(i34, bArr[i35] & 255, bArr[i35 + 1] & 255);
                                }
                                break;
                        }
                }
            }
        }
    }

    private void processMessages(long j2) {
        Iterator<Map.Entry<Long, Object>> it = this.midimessages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, Object> next = it.next();
            if (next.getKey().longValue() >= j2 + this.msec_buffer_len) {
                return;
            }
            this.delay_midievent = (int) (((next.getKey().longValue() - j2) * (this.samplerate / 1000000.0d)) + 0.5d);
            if (this.delay_midievent > this.max_delay_midievent) {
                this.delay_midievent = this.max_delay_midievent;
            }
            if (this.delay_midievent < 0) {
                this.delay_midievent = 0;
            }
            processMessage(next.getValue());
            it.remove();
        }
        this.delay_midievent = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v267, types: [float[], float[][]] */
    void processAudioBuffers() {
        double d2;
        double d3;
        SoftChannelMixerContainer[] softChannelMixerContainerArr;
        int size;
        if (this.synth.weakstream != null && this.synth.weakstream.silent_samples != 0) {
            this.sample_pos += this.synth.weakstream.silent_samples;
            this.synth.weakstream.silent_samples = 0L;
        }
        for (int i2 = 0; i2 < this.buffers.length; i2++) {
            if (i2 != 3 && i2 != 4 && i2 != 5 && i2 != 8 && i2 != 9) {
                this.buffers[i2].clear();
            }
        }
        if (!this.buffers[3].isSilent()) {
            this.buffers[0].swap(this.buffers[3]);
        }
        if (!this.buffers[4].isSilent()) {
            this.buffers[1].swap(this.buffers[4]);
        }
        if (!this.buffers[5].isSilent()) {
            this.buffers[2].swap(this.buffers[5]);
        }
        if (!this.buffers[8].isSilent()) {
            this.buffers[6].swap(this.buffers[8]);
        }
        if (!this.buffers[9].isSilent()) {
            this.buffers[7].swap(this.buffers[9]);
        }
        synchronized (this.control_mutex) {
            long j2 = (long) (this.sample_pos * (1000000.0d / this.samplerate));
            processMessages(j2);
            if (this.active_sensing_on && j2 - this.msec_last_activity > 1000000) {
                this.active_sensing_on = false;
                for (SoftChannel softChannel : this.synth.channels) {
                    softChannel.allSoundOff();
                }
            }
            for (int i3 = 0; i3 < this.voicestatus.length; i3++) {
                if (this.voicestatus[i3].active) {
                    this.voicestatus[i3].processControlLogic();
                }
            }
            this.sample_pos += this.buffer_len;
            double d4 = this.co_master_volume[0];
            d2 = d4;
            d3 = d4;
            double d5 = this.co_master_balance[0];
            if (d5 > 0.5d) {
                d2 *= (1.0d - d5) * 2.0d;
            } else {
                d3 *= d5 * 2.0d;
            }
            this.chorus.processControlLogic();
            this.reverb.processControlLogic();
            this.agc.processControlLogic();
            if (this.cur_registeredMixers == null && this.registeredMixers != null) {
                this.cur_registeredMixers = new SoftChannelMixerContainer[this.registeredMixers.size()];
                this.registeredMixers.toArray(this.cur_registeredMixers);
            }
            softChannelMixerContainerArr = this.cur_registeredMixers;
            if (softChannelMixerContainerArr != null && softChannelMixerContainerArr.length == 0) {
                softChannelMixerContainerArr = null;
            }
        }
        if (softChannelMixerContainerArr != null) {
            SoftAudioBuffer softAudioBuffer = this.buffers[0];
            SoftAudioBuffer softAudioBuffer2 = this.buffers[1];
            SoftAudioBuffer softAudioBuffer3 = this.buffers[2];
            SoftAudioBuffer softAudioBuffer4 = this.buffers[3];
            SoftAudioBuffer softAudioBuffer5 = this.buffers[4];
            SoftAudioBuffer softAudioBuffer6 = this.buffers[5];
            int size2 = this.buffers[0].getSize();
            ?? r0 = new float[this.nrofchannels];
            float[] fArr = new float[this.nrofchannels];
            fArr[0] = softAudioBuffer.array();
            if (this.nrofchannels != 1) {
                fArr[1] = softAudioBuffer2.array();
            }
            for (SoftChannelMixerContainer softChannelMixerContainer : softChannelMixerContainerArr) {
                this.buffers[0] = softChannelMixerContainer.buffers[0];
                this.buffers[1] = softChannelMixerContainer.buffers[1];
                this.buffers[2] = softChannelMixerContainer.buffers[2];
                this.buffers[3] = softChannelMixerContainer.buffers[3];
                this.buffers[4] = softChannelMixerContainer.buffers[4];
                this.buffers[5] = softChannelMixerContainer.buffers[5];
                this.buffers[0].clear();
                this.buffers[1].clear();
                this.buffers[2].clear();
                if (!this.buffers[3].isSilent()) {
                    this.buffers[0].swap(this.buffers[3]);
                }
                if (!this.buffers[4].isSilent()) {
                    this.buffers[1].swap(this.buffers[4]);
                }
                if (!this.buffers[5].isSilent()) {
                    this.buffers[2].swap(this.buffers[5]);
                }
                r0[0] = this.buffers[0].array();
                if (this.nrofchannels != 1) {
                    r0[1] = this.buffers[1].array();
                }
                boolean z2 = false;
                for (int i4 = 0; i4 < this.voicestatus.length; i4++) {
                    if (this.voicestatus[i4].active && this.voicestatus[i4].channelmixer == softChannelMixerContainer.mixer) {
                        this.voicestatus[i4].processAudioLogic(this.buffers);
                        z2 = true;
                    }
                }
                if (!this.buffers[2].isSilent()) {
                    float[] fArrArray = this.buffers[2].array();
                    float[] fArrArray2 = this.buffers[0].array();
                    if (this.nrofchannels != 1) {
                        float[] fArrArray3 = this.buffers[1].array();
                        for (int i5 = 0; i5 < size2; i5++) {
                            float f2 = fArrArray[i5];
                            int i6 = i5;
                            fArrArray2[i6] = fArrArray2[i6] + f2;
                            int i7 = i5;
                            fArrArray3[i7] = fArrArray3[i7] + f2;
                        }
                    } else {
                        for (int i8 = 0; i8 < size2; i8++) {
                            int i9 = i8;
                            fArrArray2[i9] = fArrArray2[i9] + fArrArray[i8];
                        }
                    }
                }
                if (!softChannelMixerContainer.mixer.process(r0, 0, size2)) {
                    synchronized (this.control_mutex) {
                        this.registeredMixers.remove(softChannelMixerContainer);
                        this.cur_registeredMixers = null;
                    }
                }
                for (int i10 = 0; i10 < r0.length; i10++) {
                    Object[] objArr = r0[i10];
                    Object[] objArr2 = fArr[i10];
                    for (int i11 = 0; i11 < size2; i11++) {
                        int i12 = i11;
                        objArr2[i12] = objArr2[i12] + objArr[i11];
                    }
                }
                if (!z2) {
                    synchronized (this.control_mutex) {
                        if (this.stoppedMixers != null && this.stoppedMixers.contains(softChannelMixerContainer.mixer)) {
                            this.stoppedMixers.remove(softChannelMixerContainer.mixer);
                            softChannelMixerContainer.mixer.stop();
                        }
                    }
                }
            }
            this.buffers[0] = softAudioBuffer;
            this.buffers[1] = softAudioBuffer2;
            this.buffers[2] = softAudioBuffer3;
            this.buffers[3] = softAudioBuffer4;
            this.buffers[4] = softAudioBuffer5;
            this.buffers[5] = softAudioBuffer6;
        }
        for (int i13 = 0; i13 < this.voicestatus.length; i13++) {
            if (this.voicestatus[i13].active && this.voicestatus[i13].channelmixer == null) {
                this.voicestatus[i13].processAudioLogic(this.buffers);
            }
        }
        if (!this.buffers[2].isSilent()) {
            float[] fArrArray4 = this.buffers[2].array();
            float[] fArrArray5 = this.buffers[0].array();
            int size3 = this.buffers[0].getSize();
            if (this.nrofchannels != 1) {
                float[] fArrArray6 = this.buffers[1].array();
                for (int i14 = 0; i14 < size3; i14++) {
                    float f3 = fArrArray4[i14];
                    int i15 = i14;
                    fArrArray5[i15] = fArrArray5[i15] + f3;
                    int i16 = i14;
                    fArrArray6[i16] = fArrArray6[i16] + f3;
                }
            } else {
                for (int i17 = 0; i17 < size3; i17++) {
                    int i18 = i17;
                    fArrArray5[i18] = fArrArray5[i18] + fArrArray4[i17];
                }
            }
        }
        if (this.synth.chorus_on) {
            this.chorus.processAudio();
        }
        if (this.synth.reverb_on) {
            this.reverb.processAudio();
        }
        if (this.nrofchannels == 1) {
            d2 = (d2 + d3) / 2.0d;
        }
        if (this.last_volume_left != d2 || this.last_volume_right != d3) {
            float[] fArrArray7 = this.buffers[0].array();
            float[] fArrArray8 = this.buffers[1].array();
            int size4 = this.buffers[0].getSize();
            float f4 = (float) (this.last_volume_left * this.last_volume_left);
            float f5 = (float) (((d2 * d2) - f4) / size4);
            for (int i19 = 0; i19 < size4; i19++) {
                f4 += f5;
                int i20 = i19;
                fArrArray7[i20] = fArrArray7[i20] * f4;
            }
            if (this.nrofchannels != 1) {
                float f6 = (float) (this.last_volume_right * this.last_volume_right);
                float f7 = (float) (((d3 * d3) - f6) / size4);
                for (int i21 = 0; i21 < size4; i21++) {
                    f6 += f7;
                    fArrArray8[i21] = (float) (fArrArray8[r1] * d3);
                }
            }
            this.last_volume_left = d2;
            this.last_volume_right = d3;
        } else if (d2 != 1.0d || d3 != 1.0d) {
            float[] fArrArray9 = this.buffers[0].array();
            float[] fArrArray10 = this.buffers[1].array();
            int size5 = this.buffers[0].getSize();
            float f8 = (float) (d2 * d2);
            for (int i22 = 0; i22 < size5; i22++) {
                int i23 = i22;
                fArrArray9[i23] = fArrArray9[i23] * f8;
            }
            if (this.nrofchannels != 1) {
                float f9 = (float) (d3 * d3);
                for (int i24 = 0; i24 < size5; i24++) {
                    int i25 = i24;
                    fArrArray10[i25] = fArrArray10[i25] * f9;
                }
            }
        }
        if (this.buffers[0].isSilent() && this.buffers[1].isSilent()) {
            synchronized (this.control_mutex) {
                size = this.midimessages.size();
            }
            if (size == 0) {
                this.pusher_silent_count++;
                if (this.pusher_silent_count > 5) {
                    this.pusher_silent_count = 0;
                    synchronized (this.control_mutex) {
                        this.pusher_silent = true;
                        if (this.synth.weakstream != null) {
                            this.synth.weakstream.setInputStream(null);
                        }
                    }
                }
            }
        } else {
            this.pusher_silent_count = 0;
        }
        if (this.synth.agc_on) {
            this.agc.processAudio();
        }
    }

    public void activity() {
        long j2 = 0;
        if (this.pusher_silent) {
            this.pusher_silent = false;
            if (this.synth.weakstream != null) {
                this.synth.weakstream.setInputStream(this.ais);
                j2 = this.synth.weakstream.silent_samples;
            }
        }
        this.msec_last_activity = (long) ((this.sample_pos + j2) * (1000000.0d / this.samplerate));
    }

    public void stopMixer(ModelChannelMixer modelChannelMixer) {
        if (this.stoppedMixers == null) {
            this.stoppedMixers = new HashSet();
        }
        this.stoppedMixers.add(modelChannelMixer);
    }

    public void registerMixer(ModelChannelMixer modelChannelMixer) {
        if (this.registeredMixers == null) {
            this.registeredMixers = new HashSet();
        }
        SoftChannelMixerContainer softChannelMixerContainer = new SoftChannelMixerContainer();
        softChannelMixerContainer.buffers = new SoftAudioBuffer[6];
        for (int i2 = 0; i2 < softChannelMixerContainer.buffers.length; i2++) {
            softChannelMixerContainer.buffers[i2] = new SoftAudioBuffer(this.buffer_len, this.synth.getFormat());
        }
        softChannelMixerContainer.mixer = modelChannelMixer;
        this.registeredMixers.add(softChannelMixerContainer);
        this.cur_registeredMixers = null;
    }

    public SoftMainMixer(SoftSynthesizer softSynthesizer) {
        this.sample_pos = 0L;
        this.samplerate = 44100.0f;
        this.nrofchannels = 2;
        this.voicestatus = null;
        this.msec_buffer_len = 0L;
        this.buffer_len = 0;
        this.max_delay_midievent = 0;
        this.synth = softSynthesizer;
        this.sample_pos = 0L;
        this.co_master_balance[0] = 0.5d;
        this.co_master_volume[0] = 1.0d;
        this.co_master_coarse_tuning[0] = 0.5d;
        this.co_master_fine_tuning[0] = 0.5d;
        this.msec_buffer_len = (long) (1000000.0d / softSynthesizer.getControlRate());
        this.samplerate = softSynthesizer.getFormat().getSampleRate();
        this.nrofchannels = softSynthesizer.getFormat().getChannels();
        int sampleRate = (int) (softSynthesizer.getFormat().getSampleRate() / softSynthesizer.getControlRate());
        this.buffer_len = sampleRate;
        this.max_delay_midievent = sampleRate;
        this.control_mutex = softSynthesizer.control_mutex;
        this.buffers = new SoftAudioBuffer[14];
        for (int i2 = 0; i2 < this.buffers.length; i2++) {
            this.buffers[i2] = new SoftAudioBuffer(sampleRate, softSynthesizer.getFormat());
        }
        this.voicestatus = softSynthesizer.getVoices();
        this.reverb = new SoftReverb();
        this.chorus = new SoftChorus();
        this.agc = new SoftLimiter();
        float sampleRate2 = softSynthesizer.getFormat().getSampleRate();
        float controlRate = softSynthesizer.getControlRate();
        this.reverb.init(sampleRate2, controlRate);
        this.chorus.init(sampleRate2, controlRate);
        this.agc.init(sampleRate2, controlRate);
        this.reverb.setLightMode(softSynthesizer.reverb_light);
        this.reverb.setMixMode(true);
        this.chorus.setMixMode(true);
        this.agc.setMixMode(false);
        this.chorus.setInput(0, this.buffers[7]);
        this.chorus.setOutput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.chorus.setOutput(1, this.buffers[1]);
        }
        this.chorus.setOutput(2, this.buffers[6]);
        this.reverb.setInput(0, this.buffers[6]);
        this.reverb.setOutput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.reverb.setOutput(1, this.buffers[1]);
        }
        this.agc.setInput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.agc.setInput(1, this.buffers[1]);
        }
        this.agc.setOutput(0, this.buffers[0]);
        if (this.nrofchannels != 1) {
            this.agc.setOutput(1, this.buffers[1]);
        }
        this.ais = new AudioInputStream(new InputStream() { // from class: com.sun.media.sound.SoftMainMixer.2
            private final SoftAudioBuffer[] buffers;
            private final int nrofchannels;
            private final int buffersize;
            private final byte[] bbuffer;
            private int bbuffer_pos = 0;
            private final byte[] single = new byte[1];

            {
                this.buffers = SoftMainMixer.this.buffers;
                this.nrofchannels = SoftMainMixer.this.synth.getFormat().getChannels();
                this.buffersize = this.buffers[0].getSize();
                this.bbuffer = new byte[this.buffersize * (SoftMainMixer.this.synth.getFormat().getSampleSizeInBits() / 8) * this.nrofchannels];
            }

            public void fillBuffer() {
                SoftMainMixer.this.processAudioBuffers();
                for (int i3 = 0; i3 < this.nrofchannels; i3++) {
                    this.buffers[i3].get(this.bbuffer, i3);
                }
                this.bbuffer_pos = 0;
            }

            @Override // java.io.InputStream
            public int read(byte[] bArr, int i3, int i4) {
                int length = this.bbuffer.length;
                int i5 = i3 + i4;
                byte[] bArr2 = this.bbuffer;
                while (i3 < i5) {
                    if (available() == 0) {
                        fillBuffer();
                    } else {
                        int i6 = this.bbuffer_pos;
                        while (i3 < i5 && i6 < length) {
                            int i7 = i3;
                            i3++;
                            int i8 = i6;
                            i6++;
                            bArr[i7] = bArr2[i8];
                        }
                        this.bbuffer_pos = i6;
                        if (!SoftMainMixer.this.readfully) {
                            return i3 - i3;
                        }
                    }
                }
                return i4;
            }

            @Override // java.io.InputStream
            public int read() throws IOException {
                if (read(this.single) == -1) {
                    return -1;
                }
                return this.single[0] & 255;
            }

            @Override // java.io.InputStream
            public int available() {
                return this.bbuffer.length - this.bbuffer_pos;
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
                SoftMainMixer.this.synth.close();
            }
        }, softSynthesizer.getFormat(), -1L);
    }

    public AudioInputStream getInputStream() {
        return this.ais;
    }

    public void reset() {
        SoftChannel[] softChannelArr = this.synth.channels;
        for (int i2 = 0; i2 < softChannelArr.length; i2++) {
            softChannelArr[i2].allSoundOff();
            softChannelArr[i2].resetAllControllers(true);
            if (this.synth.getGeneralMidiMode() == 2) {
                if (i2 == 9) {
                    softChannelArr[i2].programChange(0, TextLayout.DIRECTION_MASK);
                } else {
                    softChannelArr[i2].programChange(0, 15488);
                }
            } else {
                softChannelArr[i2].programChange(0, 0);
            }
        }
        setVolume(16383);
        setBalance(8192);
        setCoarseTuning(8192);
        setFineTuning(8192);
        globalParameterControlChange(new int[]{129}, new long[]{0}, new long[]{4});
        globalParameterControlChange(new int[]{130}, new long[]{0}, new long[]{2});
    }

    public void setVolume(int i2) {
        synchronized (this.control_mutex) {
            this.co_master_volume[0] = i2 / 16384.0d;
        }
    }

    public void setBalance(int i2) {
        synchronized (this.control_mutex) {
            this.co_master_balance[0] = i2 / 16384.0d;
        }
    }

    public void setFineTuning(int i2) {
        synchronized (this.control_mutex) {
            this.co_master_fine_tuning[0] = i2 / 16384.0d;
        }
    }

    public void setCoarseTuning(int i2) {
        synchronized (this.control_mutex) {
            this.co_master_coarse_tuning[0] = i2 / 16384.0d;
        }
    }

    public int getVolume() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = (int) (this.co_master_volume[0] * 16384.0d);
        }
        return i2;
    }

    public int getBalance() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = (int) (this.co_master_balance[0] * 16384.0d);
        }
        return i2;
    }

    public int getFineTuning() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = (int) (this.co_master_fine_tuning[0] * 16384.0d);
        }
        return i2;
    }

    public int getCoarseTuning() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = (int) (this.co_master_coarse_tuning[0] * 16384.0d);
        }
        return i2;
    }

    public void globalParameterControlChange(int[] iArr, long[] jArr, long[] jArr2) {
        if (iArr.length == 0) {
            return;
        }
        synchronized (this.control_mutex) {
            if (iArr[0] == 129) {
                for (int i2 = 0; i2 < jArr2.length; i2++) {
                    this.reverb.globalParameterControlChange(iArr, jArr[i2], jArr2[i2]);
                }
            }
            if (iArr[0] == 130) {
                for (int i3 = 0; i3 < jArr2.length; i3++) {
                    this.chorus.globalParameterControlChange(iArr, jArr[i3], jArr2[i3]);
                }
            }
        }
    }

    public void processMessage(Object obj) {
        if (obj instanceof byte[]) {
            processMessage((byte[]) obj);
        }
        if (obj instanceof MidiMessage) {
            processMessage((MidiMessage) obj);
        }
    }

    public void processMessage(MidiMessage midiMessage) {
        if (midiMessage instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) midiMessage;
            processMessage(shortMessage.getChannel(), shortMessage.getCommand(), shortMessage.getData1(), shortMessage.getData2());
        } else {
            processMessage(midiMessage.getMessage());
        }
    }

    public void processMessage(byte[] bArr) {
        int i2;
        int i3;
        int i4 = 0;
        if (bArr.length > 0) {
            i4 = bArr[0] & 255;
        }
        if (i4 == 240) {
            processSystemExclusiveMessage(bArr);
            return;
        }
        int i5 = i4 & 240;
        int i6 = i4 & 15;
        if (bArr.length > 1) {
            i2 = bArr[1] & 255;
        } else {
            i2 = 0;
        }
        if (bArr.length > 2) {
            i3 = bArr[2] & 255;
        } else {
            i3 = 0;
        }
        processMessage(i6, i5, i2, i3);
    }

    public void processMessage(int i2, int i3, int i4, int i5) {
        synchronized (this.synth.control_mutex) {
            activity();
        }
        if (i3 == 240) {
            switch (i3 | i2) {
                case 254:
                    synchronized (this.synth.control_mutex) {
                        this.active_sensing_on = true;
                    }
                    return;
                default:
                    return;
            }
        }
        SoftChannel[] softChannelArr = this.synth.channels;
        if (i2 >= softChannelArr.length) {
            return;
        }
        SoftChannel softChannel = softChannelArr[i2];
        switch (i3) {
            case 128:
                softChannel.noteOff(i4, i5);
                return;
            case 144:
                if (this.delay_midievent != 0) {
                    softChannel.noteOn(i4, i5, this.delay_midievent);
                    return;
                } else {
                    softChannel.noteOn(i4, i5);
                    return;
                }
            case 160:
                softChannel.setPolyPressure(i4, i5);
                return;
            case 176:
                softChannel.controlChange(i4, i5);
                return;
            case 192:
                softChannel.programChange(i4);
                return;
            case 208:
                softChannel.setChannelPressure(i4);
                return;
            case 224:
                softChannel.setPitchBend(i4 + (i5 * 128));
                return;
            default:
                return;
        }
    }

    public long getMicrosecondPosition() {
        if (this.pusher_silent && this.synth.weakstream != null) {
            return (long) ((this.sample_pos + this.synth.weakstream.silent_samples) * (1000000.0d / this.samplerate));
        }
        return (long) (this.sample_pos * (1000000.0d / this.samplerate));
    }

    public void close() {
    }
}
