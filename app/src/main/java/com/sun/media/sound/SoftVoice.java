package com.sun.media.sound;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javax.sound.midi.VoiceStatus;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/media/sound/SoftVoice.class */
public final class SoftVoice extends VoiceStatus {
    private final SoftFilter filter_left;
    private final SoftFilter filter_right;
    SoftSynthesizer synthesizer;
    SoftInstrument instrument;
    SoftPerformer performer;
    private ModelOscillatorStream osc_stream;
    private int osc_stream_nrofchannels;
    private ModelConnectionBlock[] connections;
    SoftResamplerStreamer resampler;
    private final int nrofchannels;
    public int exclusiveClass = 0;
    public boolean releaseTriggered = false;
    private int noteOn_noteNumber = 0;
    private int noteOn_velocity = 0;
    private int noteOff_velocity = 0;
    private int delay = 0;
    ModelChannelMixer channelmixer = null;
    double tunedKey = 0.0d;
    SoftTuning tuning = null;
    SoftChannel stealer_channel = null;
    ModelConnectionBlock[] stealer_extendedConnectionBlocks = null;
    SoftPerformer stealer_performer = null;
    ModelChannelMixer stealer_channelmixer = null;
    int stealer_voiceID = -1;
    int stealer_noteNumber = 0;
    int stealer_velocity = 0;
    boolean stealer_releaseTriggered = false;
    int voiceID = -1;
    boolean sustain = false;
    boolean sostenuto = false;
    boolean portamento = false;
    private final SoftProcess eg = new SoftEnvelopeGenerator();
    private final SoftProcess lfo = new SoftLowFrequencyOscillator();
    Map<String, SoftControl> objects = new HashMap();
    SoftChannel softchannel = null;
    boolean on = false;
    private boolean audiostarted = false;
    private boolean started = false;
    private boolean stopping = false;
    private float osc_attenuation = 0.0f;
    private float[][] osc_buff = new float[2];
    private boolean osc_stream_off_transmitted = false;
    private boolean out_mixer_end = false;
    private float out_mixer_left = 0.0f;
    private float out_mixer_right = 0.0f;
    private float out_mixer_effect1 = 0.0f;
    private float out_mixer_effect2 = 0.0f;
    private float last_out_mixer_left = 0.0f;
    private float last_out_mixer_right = 0.0f;
    private float last_out_mixer_effect1 = 0.0f;
    private float last_out_mixer_effect2 = 0.0f;
    ModelConnectionBlock[] extendedConnectionBlocks = null;
    private double[] connections_last = new double[50];
    private double[][][] connections_src = new double[50][3][];
    private int[][] connections_src_kc = new int[50][3];
    private double[][] connections_dst = new double[50];
    private boolean soundoff = false;
    private float lastMuteValue = 0.0f;
    private float lastSoloMuteValue = 0.0f;
    double[] co_noteon_keynumber = new double[1];
    double[] co_noteon_velocity = new double[1];
    double[] co_noteon_on = new double[1];
    private final SoftControl co_noteon = new SoftControl() { // from class: com.sun.media.sound.SoftVoice.1
        double[] keynumber;
        double[] velocity;
        double[] on;

        {
            this.keynumber = SoftVoice.this.co_noteon_keynumber;
            this.velocity = SoftVoice.this.co_noteon_velocity;
            this.on = SoftVoice.this.co_noteon_on;
        }

        @Override // com.sun.media.sound.SoftControl
        public double[] get(int i2, String str) {
            if (str == null) {
                return null;
            }
            if (str.equals("keynumber")) {
                return this.keynumber;
            }
            if (str.equals("velocity")) {
                return this.velocity;
            }
            if (str.equals(FXMLLoader.EVENT_HANDLER_PREFIX)) {
                return this.on;
            }
            return null;
        }
    };
    private final double[] co_mixer_active = new double[1];
    private final double[] co_mixer_gain = new double[1];
    private final double[] co_mixer_pan = new double[1];
    private final double[] co_mixer_balance = new double[1];
    private final double[] co_mixer_reverb = new double[1];
    private final double[] co_mixer_chorus = new double[1];
    private final SoftControl co_mixer = new SoftControl() { // from class: com.sun.media.sound.SoftVoice.2
        double[] active;
        double[] gain;
        double[] pan;
        double[] balance;
        double[] reverb;
        double[] chorus;

        {
            this.active = SoftVoice.this.co_mixer_active;
            this.gain = SoftVoice.this.co_mixer_gain;
            this.pan = SoftVoice.this.co_mixer_pan;
            this.balance = SoftVoice.this.co_mixer_balance;
            this.reverb = SoftVoice.this.co_mixer_reverb;
            this.chorus = SoftVoice.this.co_mixer_chorus;
        }

        @Override // com.sun.media.sound.SoftControl
        public double[] get(int i2, String str) {
            if (str == null) {
                return null;
            }
            if (str.equals("active")) {
                return this.active;
            }
            if (str.equals("gain")) {
                return this.gain;
            }
            if (str.equals("pan")) {
                return this.pan;
            }
            if (str.equals("balance")) {
                return this.balance;
            }
            if (str.equals("reverb")) {
                return this.reverb;
            }
            if (str.equals("chorus")) {
                return this.chorus;
            }
            return null;
        }
    };
    private final double[] co_osc_pitch = new double[1];
    private final SoftControl co_osc = new SoftControl() { // from class: com.sun.media.sound.SoftVoice.3
        double[] pitch;

        {
            this.pitch = SoftVoice.this.co_osc_pitch;
        }

        @Override // com.sun.media.sound.SoftControl
        public double[] get(int i2, String str) {
            if (str != null && str.equals("pitch")) {
                return this.pitch;
            }
            return null;
        }
    };
    private final double[] co_filter_freq = new double[1];
    private final double[] co_filter_type = new double[1];
    private final double[] co_filter_q = new double[1];
    private final SoftControl co_filter = new SoftControl() { // from class: com.sun.media.sound.SoftVoice.4
        double[] freq;
        double[] ftype;

        /* renamed from: q, reason: collision with root package name */
        double[] f11985q;

        {
            this.freq = SoftVoice.this.co_filter_freq;
            this.ftype = SoftVoice.this.co_filter_type;
            this.f11985q = SoftVoice.this.co_filter_q;
        }

        @Override // com.sun.media.sound.SoftControl
        public double[] get(int i2, String str) {
            if (str == null) {
                return null;
            }
            if (str.equals("freq")) {
                return this.freq;
            }
            if (str.equals("type")) {
                return this.ftype;
            }
            if (str.equals(PdfOps.q_TOKEN)) {
                return this.f11985q;
            }
            return null;
        }
    };

    /* JADX WARN: Type inference failed for: r1v31, types: [float[], float[][]] */
    /* JADX WARN: Type inference failed for: r1v50, types: [double[], double[][]] */
    public SoftVoice(SoftSynthesizer softSynthesizer) {
        this.synthesizer = softSynthesizer;
        this.filter_left = new SoftFilter(softSynthesizer.getFormat().getSampleRate());
        this.filter_right = new SoftFilter(softSynthesizer.getFormat().getSampleRate());
        this.nrofchannels = softSynthesizer.getFormat().getChannels();
    }

    private int getValueKC(ModelIdentifier modelIdentifier) throws NumberFormatException {
        if (modelIdentifier.getObject().equals("midi_cc")) {
            int i2 = Integer.parseInt(modelIdentifier.getVariable());
            if (i2 != 0 && i2 != 32 && i2 < 120) {
                return i2;
            }
            return -1;
        }
        if (modelIdentifier.getObject().equals("midi_rpn")) {
            if (modelIdentifier.getVariable().equals("1")) {
                return 120;
            }
            if (modelIdentifier.getVariable().equals("2")) {
                return 121;
            }
            return -1;
        }
        return -1;
    }

    private double[] getValue(ModelIdentifier modelIdentifier) {
        SoftControl softControl = this.objects.get(modelIdentifier.getObject());
        if (softControl == null) {
            return null;
        }
        return softControl.get(modelIdentifier.getInstance(), modelIdentifier.getVariable());
    }

    private double transformValue(double d2, ModelSource modelSource) {
        if (modelSource.getTransform() != null) {
            return modelSource.getTransform().transform(d2);
        }
        return d2;
    }

    private double transformValue(double d2, ModelDestination modelDestination) {
        if (modelDestination.getTransform() != null) {
            return modelDestination.getTransform().transform(d2);
        }
        return d2;
    }

    private double processKeyBasedController(double d2, int i2) {
        if (i2 == -1) {
            return d2;
        }
        if (this.softchannel.keybasedcontroller_active != null && this.softchannel.keybasedcontroller_active[this.note] != null && this.softchannel.keybasedcontroller_active[this.note][i2]) {
            double d3 = this.softchannel.keybasedcontroller_value[this.note][i2];
            if (i2 == 10 || i2 == 91 || i2 == 93) {
                return d3;
            }
            d2 += (d3 * 2.0d) - 1.0d;
            if (d2 > 1.0d) {
                d2 = 1.0d;
            } else if (d2 < 0.0d) {
                d2 = 0.0d;
            }
        }
        return d2;
    }

    private void processConnection(int i2) {
        ModelConnectionBlock modelConnectionBlock = this.connections[i2];
        double[][] dArr = this.connections_src[i2];
        double[] dArr2 = this.connections_dst[i2];
        if (dArr2 == null || Double.isInfinite(dArr2[0])) {
            return;
        }
        double scale = modelConnectionBlock.getScale();
        if (this.softchannel.keybasedcontroller_active == null) {
            ModelSource[] sources = modelConnectionBlock.getSources();
            for (int i3 = 0; i3 < sources.length; i3++) {
                scale *= transformValue(dArr[i3][0], sources[i3]);
                if (scale == 0.0d) {
                    break;
                }
            }
        } else {
            ModelSource[] sources2 = modelConnectionBlock.getSources();
            int[] iArr = this.connections_src_kc[i2];
            for (int i4 = 0; i4 < sources2.length; i4++) {
                scale *= transformValue(processKeyBasedController(dArr[i4][0], iArr[i4]), sources2[i4]);
                if (scale == 0.0d) {
                    break;
                }
            }
        }
        double dTransformValue = transformValue(scale, modelConnectionBlock.getDestination());
        dArr2[0] = (dArr2[0] - this.connections_last[i2]) + dTransformValue;
        this.connections_last[i2] = dTransformValue;
    }

    void updateTuning(SoftTuning softTuning) {
        int[] iArr;
        this.tuning = softTuning;
        this.tunedKey = this.tuning.getTuning(this.note) / 100.0d;
        if (!this.portamento) {
            this.co_noteon_keynumber[0] = this.tunedKey * 0.0078125d;
            if (this.performer == null || (iArr = this.performer.midi_connections[4]) == null) {
                return;
            }
            for (int i2 : iArr) {
                processConnection(i2);
            }
        }
    }

    void setNote(int i2) {
        this.note = i2;
        this.tunedKey = this.tuning.getTuning(i2) / 100.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v43, types: [double[][], double[][][]] */
    /* JADX WARN: Type inference failed for: r1v47, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v51, types: [double[], double[][]] */
    void noteOn(int i2, int i3, int i4) {
        double d2;
        double dTransform;
        double d3;
        double dTransform2;
        this.sustain = false;
        this.sostenuto = false;
        this.portamento = false;
        this.soundoff = false;
        this.on = true;
        this.active = true;
        this.started = true;
        this.noteOn_noteNumber = i2;
        this.noteOn_velocity = i3;
        this.delay = i4;
        this.lastMuteValue = 0.0f;
        this.lastSoloMuteValue = 0.0f;
        setNote(i2);
        if (this.performer.forcedKeynumber) {
            this.co_noteon_keynumber[0] = 0.0d;
        } else {
            this.co_noteon_keynumber[0] = this.tunedKey * 0.0078125d;
        }
        if (this.performer.forcedVelocity) {
            this.co_noteon_velocity[0] = 0.0d;
        } else {
            this.co_noteon_velocity[0] = i3 * 0.0078125f;
        }
        this.co_mixer_active[0] = 0.0d;
        this.co_mixer_gain[0] = 0.0d;
        this.co_mixer_pan[0] = 0.0d;
        this.co_mixer_balance[0] = 0.0d;
        this.co_mixer_reverb[0] = 0.0d;
        this.co_mixer_chorus[0] = 0.0d;
        this.co_osc_pitch[0] = 0.0d;
        this.co_filter_freq[0] = 0.0d;
        this.co_filter_q[0] = 0.0d;
        this.co_filter_type[0] = 0.0d;
        this.co_noteon_on[0] = 1.0d;
        this.eg.reset();
        this.lfo.reset();
        this.filter_left.reset();
        this.filter_right.reset();
        this.objects.put("master", this.synthesizer.getMainMixer().co_master);
        this.objects.put("eg", this.eg);
        this.objects.put("lfo", this.lfo);
        this.objects.put("noteon", this.co_noteon);
        this.objects.put("osc", this.co_osc);
        this.objects.put("mixer", this.co_mixer);
        this.objects.put("filter", this.co_filter);
        this.connections = this.performer.connections;
        if (this.connections_last == null || this.connections_last.length < this.connections.length) {
            this.connections_last = new double[this.connections.length];
        }
        if (this.connections_src == null || this.connections_src.length < this.connections.length) {
            this.connections_src = new double[this.connections.length][];
            this.connections_src_kc = new int[this.connections.length];
        }
        if (this.connections_dst == null || this.connections_dst.length < this.connections.length) {
            this.connections_dst = new double[this.connections.length];
        }
        for (int i5 = 0; i5 < this.connections.length; i5++) {
            ModelConnectionBlock modelConnectionBlock = this.connections[i5];
            this.connections_last[i5] = 0.0d;
            if (modelConnectionBlock.getSources() != null) {
                ModelSource[] sources = modelConnectionBlock.getSources();
                if (this.connections_src[i5] == null || this.connections_src[i5].length < sources.length) {
                    this.connections_src[i5] = new double[sources.length];
                    this.connections_src_kc[i5] = new int[sources.length];
                }
                double[][] dArr = this.connections_src[i5];
                int[] iArr = this.connections_src_kc[i5];
                this.connections_src[i5] = dArr;
                for (int i6 = 0; i6 < sources.length; i6++) {
                    iArr[i6] = getValueKC(sources[i6].getIdentifier());
                    dArr[i6] = getValue(sources[i6].getIdentifier());
                }
            }
            if (modelConnectionBlock.getDestination() != null) {
                this.connections_dst[i5] = getValue(modelConnectionBlock.getDestination().getIdentifier());
            } else {
                this.connections_dst[i5] = null;
            }
        }
        for (int i7 = 0; i7 < this.connections.length; i7++) {
            processConnection(i7);
        }
        if (this.extendedConnectionBlocks != null) {
            for (ModelConnectionBlock modelConnectionBlock2 : this.extendedConnectionBlocks) {
                double dTransform3 = 0.0d;
                if (this.softchannel.keybasedcontroller_active == null) {
                    for (ModelSource modelSource : modelConnectionBlock2.getSources()) {
                        double d4 = getValue(modelSource.getIdentifier())[0];
                        ModelTransform transform = modelSource.getTransform();
                        if (transform == null) {
                            d3 = dTransform3;
                            dTransform2 = d4;
                        } else {
                            d3 = dTransform3;
                            dTransform2 = transform.transform(d4);
                        }
                        dTransform3 = d3 + dTransform2;
                    }
                } else {
                    for (ModelSource modelSource2 : modelConnectionBlock2.getSources()) {
                        double dProcessKeyBasedController = processKeyBasedController(getValue(modelSource2.getIdentifier())[0], getValueKC(modelSource2.getIdentifier()));
                        ModelTransform transform2 = modelSource2.getTransform();
                        if (transform2 == null) {
                            d2 = dTransform3;
                            dTransform = dProcessKeyBasedController;
                        } else {
                            d2 = dTransform3;
                            dTransform = transform2.transform(dProcessKeyBasedController);
                        }
                        dTransform3 = d2 + dTransform;
                    }
                }
                ModelDestination destination = modelConnectionBlock2.getDestination();
                ModelTransform transform3 = destination.getTransform();
                if (transform3 != null) {
                    dTransform3 = transform3.transform(dTransform3);
                }
                double[] value = getValue(destination.getIdentifier());
                value[0] = value[0] + dTransform3;
            }
        }
        this.eg.init(this.synthesizer);
        this.lfo.init(this.synthesizer);
    }

    void setPolyPressure(int i2) {
        int[] iArr;
        if (this.performer == null || (iArr = this.performer.midi_connections[2]) == null) {
            return;
        }
        for (int i3 : iArr) {
            processConnection(i3);
        }
    }

    void setChannelPressure(int i2) {
        int[] iArr;
        if (this.performer == null || (iArr = this.performer.midi_connections[1]) == null) {
            return;
        }
        for (int i3 : iArr) {
            processConnection(i3);
        }
    }

    void controlChange(int i2, int i3) {
        int[] iArr;
        if (this.performer == null || (iArr = this.performer.midi_ctrl_connections[i2]) == null) {
            return;
        }
        for (int i4 : iArr) {
            processConnection(i4);
        }
    }

    void nrpnChange(int i2, int i3) {
        int[] iArr;
        if (this.performer == null || (iArr = this.performer.midi_nrpn_connections.get(Integer.valueOf(i2))) == null) {
            return;
        }
        for (int i4 : iArr) {
            processConnection(i4);
        }
    }

    void rpnChange(int i2, int i3) {
        int[] iArr;
        if (this.performer == null || (iArr = this.performer.midi_rpn_connections.get(Integer.valueOf(i2))) == null) {
            return;
        }
        for (int i4 : iArr) {
            processConnection(i4);
        }
    }

    void setPitchBend(int i2) {
        int[] iArr;
        if (this.performer == null || (iArr = this.performer.midi_connections[0]) == null) {
            return;
        }
        for (int i3 : iArr) {
            processConnection(i3);
        }
    }

    void setMute(boolean z2) {
        double[] dArr = this.co_mixer_gain;
        dArr[0] = dArr[0] - this.lastMuteValue;
        this.lastMuteValue = z2 ? -960.0f : 0.0f;
        double[] dArr2 = this.co_mixer_gain;
        dArr2[0] = dArr2[0] + this.lastMuteValue;
    }

    void setSoloMute(boolean z2) {
        double[] dArr = this.co_mixer_gain;
        dArr[0] = dArr[0] - this.lastSoloMuteValue;
        this.lastSoloMuteValue = z2 ? -960.0f : 0.0f;
        double[] dArr2 = this.co_mixer_gain;
        dArr2[0] = dArr2[0] + this.lastSoloMuteValue;
    }

    void shutdown() {
        int[] iArr;
        if (this.co_noteon_on[0] < -0.5d) {
            return;
        }
        this.on = false;
        this.co_noteon_on[0] = -1.0d;
        if (this.performer == null || (iArr = this.performer.midi_connections[3]) == null) {
            return;
        }
        for (int i2 : iArr) {
            processConnection(i2);
        }
    }

    void soundOff() {
        this.on = false;
        this.soundoff = true;
    }

    void noteOff(int i2) {
        int[] iArr;
        if (!this.on) {
            return;
        }
        this.on = false;
        this.noteOff_velocity = i2;
        if (this.softchannel.sustain) {
            this.sustain = true;
            return;
        }
        if (this.sostenuto) {
            return;
        }
        this.co_noteon_on[0] = 0.0d;
        if (this.performer == null || (iArr = this.performer.midi_connections[3]) == null) {
            return;
        }
        for (int i3 : iArr) {
            processConnection(i3);
        }
    }

    void redamp() {
        int[] iArr;
        if (this.co_noteon_on[0] > 0.5d || this.co_noteon_on[0] < -0.5d) {
            return;
        }
        this.sustain = true;
        this.co_noteon_on[0] = 1.0d;
        if (this.performer == null || (iArr = this.performer.midi_connections[3]) == null) {
            return;
        }
        for (int i2 : iArr) {
            processConnection(i2);
        }
    }

    /* JADX WARN: Type inference failed for: r1v139, types: [float[], float[][]] */
    void processControlLogic() {
        double dExp;
        if (this.stopping) {
            this.active = false;
            this.stopping = false;
            this.audiostarted = false;
            this.instrument = null;
            this.performer = null;
            this.connections = null;
            this.extendedConnectionBlocks = null;
            this.channelmixer = null;
            if (this.osc_stream != null) {
                try {
                    this.osc_stream.close();
                } catch (IOException e2) {
                }
            }
            if (this.stealer_channel != null) {
                this.stealer_channel.initVoice(this, this.stealer_performer, this.stealer_voiceID, this.stealer_noteNumber, this.stealer_velocity, 0, this.stealer_extendedConnectionBlocks, this.stealer_channelmixer, this.stealer_releaseTriggered);
                this.stealer_releaseTriggered = false;
                this.stealer_channel = null;
                this.stealer_performer = null;
                this.stealer_voiceID = -1;
                this.stealer_noteNumber = 0;
                this.stealer_velocity = 0;
                this.stealer_extendedConnectionBlocks = null;
                this.stealer_channelmixer = null;
            }
        }
        if (this.started) {
            this.audiostarted = true;
            ModelOscillator modelOscillator = this.performer.oscillators[0];
            this.osc_stream_off_transmitted = false;
            if (modelOscillator instanceof ModelWavetable) {
                try {
                    this.resampler.open((ModelWavetable) modelOscillator, this.synthesizer.getFormat().getSampleRate());
                    this.osc_stream = this.resampler;
                } catch (IOException e3) {
                }
            } else {
                this.osc_stream = modelOscillator.open(this.synthesizer.getFormat().getSampleRate());
            }
            this.osc_attenuation = modelOscillator.getAttenuation();
            this.osc_stream_nrofchannels = modelOscillator.getChannels();
            if (this.osc_buff == null || this.osc_buff.length < this.osc_stream_nrofchannels) {
                this.osc_buff = new float[this.osc_stream_nrofchannels];
            }
            if (this.osc_stream != null) {
                this.osc_stream.noteOn(this.softchannel, this, this.noteOn_noteNumber, this.noteOn_velocity);
            }
        }
        if (this.audiostarted) {
            if (this.portamento) {
                double dSignum = this.tunedKey - (this.co_noteon_keynumber[0] * 128.0d);
                double dAbs = Math.abs(dSignum);
                if (dAbs < 1.0E-10d) {
                    this.co_noteon_keynumber[0] = this.tunedKey * 0.0078125d;
                    this.portamento = false;
                } else {
                    if (dAbs > this.softchannel.portamento_time) {
                        dSignum = Math.signum(dSignum) * this.softchannel.portamento_time;
                    }
                    double[] dArr = this.co_noteon_keynumber;
                    dArr[0] = dArr[0] + (dSignum * 0.0078125d);
                }
                int[] iArr = this.performer.midi_connections[4];
                if (iArr == null) {
                    return;
                }
                for (int i2 : iArr) {
                    processConnection(i2);
                }
            }
            this.eg.processControlLogic();
            this.lfo.processControlLogic();
            for (int i3 = 0; i3 < this.performer.ctrl_connections.length; i3++) {
                processConnection(this.performer.ctrl_connections[i3]);
            }
            this.osc_stream.setPitch((float) this.co_osc_pitch[0]);
            int i4 = (int) this.co_filter_type[0];
            if (this.co_filter_freq[0] == 13500.0d) {
                dExp = 19912.126958213175d;
            } else {
                dExp = 440.0d * Math.exp((this.co_filter_freq[0] - 6900.0d) * (Math.log(2.0d) / 1200.0d));
            }
            double d2 = this.co_filter_q[0] / 10.0d;
            this.filter_left.setFilterType(i4);
            this.filter_left.setFrequency(dExp);
            this.filter_left.setResonance(d2);
            this.filter_right.setFilterType(i4);
            this.filter_right.setFrequency(dExp);
            this.filter_right.setResonance(d2);
            float fExp = (float) Math.exp(((-this.osc_attenuation) + this.co_mixer_gain[0]) * (Math.log(10.0d) / 200.0d));
            if (this.co_mixer_gain[0] <= -960.0d) {
                fExp = 0.0f;
            }
            if (this.soundoff) {
                this.stopping = true;
                fExp = 0.0f;
            }
            this.volume = (int) (Math.sqrt(fExp) * 128.0d);
            double d3 = this.co_mixer_pan[0] * 0.001d;
            if (d3 < 0.0d) {
                d3 = 0.0d;
            } else if (d3 > 1.0d) {
                d3 = 1.0d;
            }
            if (d3 == 0.5d) {
                this.out_mixer_left = fExp * 0.70710677f;
                this.out_mixer_right = this.out_mixer_left;
            } else {
                this.out_mixer_left = fExp * ((float) Math.cos(d3 * 3.141592653589793d * 0.5d));
                this.out_mixer_right = fExp * ((float) Math.sin(d3 * 3.141592653589793d * 0.5d));
            }
            double d4 = this.co_mixer_balance[0] * 0.001d;
            if (d4 != 0.5d) {
                if (d4 > 0.5d) {
                    this.out_mixer_left = (float) (this.out_mixer_left * (1.0d - d4) * 2.0d);
                } else {
                    this.out_mixer_right = (float) (this.out_mixer_right * d4 * 2.0d);
                }
            }
            if (this.synthesizer.reverb_on) {
                this.out_mixer_effect1 = (float) (this.co_mixer_reverb[0] * 0.001d);
                this.out_mixer_effect1 *= fExp;
            } else {
                this.out_mixer_effect1 = 0.0f;
            }
            if (this.synthesizer.chorus_on) {
                this.out_mixer_effect2 = (float) (this.co_mixer_chorus[0] * 0.001d);
                this.out_mixer_effect2 *= fExp;
            } else {
                this.out_mixer_effect2 = 0.0f;
            }
            this.out_mixer_end = this.co_mixer_active[0] < 0.5d;
            if (!this.on && !this.osc_stream_off_transmitted) {
                this.osc_stream_off_transmitted = true;
                if (this.osc_stream != null) {
                    this.osc_stream.noteOff(this.noteOff_velocity);
                }
            }
        }
        if (this.started) {
            this.last_out_mixer_left = this.out_mixer_left;
            this.last_out_mixer_right = this.out_mixer_right;
            this.last_out_mixer_effect1 = this.out_mixer_effect1;
            this.last_out_mixer_effect2 = this.out_mixer_effect2;
            this.started = false;
        }
    }

    void mixAudioStream(SoftAudioBuffer softAudioBuffer, SoftAudioBuffer softAudioBuffer2, SoftAudioBuffer softAudioBuffer3, float f2, float f3) {
        int size = softAudioBuffer.getSize();
        if (f2 < 1.0E-9d && f3 < 1.0E-9d) {
            return;
        }
        if (softAudioBuffer3 == null || this.delay == 0) {
            if (f2 == f3) {
                float[] fArrArray = softAudioBuffer2.array();
                float[] fArrArray2 = softAudioBuffer.array();
                for (int i2 = 0; i2 < size; i2++) {
                    int i3 = i2;
                    fArrArray[i3] = fArrArray[i3] + (fArrArray2[i2] * f3);
                }
                return;
            }
            float f4 = f2;
            float f5 = (f3 - f2) / size;
            float[] fArrArray3 = softAudioBuffer2.array();
            float[] fArrArray4 = softAudioBuffer.array();
            for (int i4 = 0; i4 < size; i4++) {
                f4 += f5;
                int i5 = i4;
                fArrArray3[i5] = fArrArray3[i5] + (fArrArray4[i4] * f4);
            }
            return;
        }
        if (f2 == f3) {
            float[] fArrArray5 = softAudioBuffer2.array();
            float[] fArrArray6 = softAudioBuffer.array();
            int i6 = 0;
            for (int i7 = this.delay; i7 < size; i7++) {
                int i8 = i7;
                int i9 = i6;
                i6++;
                fArrArray5[i8] = fArrArray5[i8] + (fArrArray6[i9] * f3);
            }
            float[] fArrArray7 = softAudioBuffer3.array();
            for (int i10 = 0; i10 < this.delay; i10++) {
                int i11 = i10;
                int i12 = i6;
                i6++;
                fArrArray7[i11] = fArrArray7[i11] + (fArrArray6[i12] * f3);
            }
            return;
        }
        float f6 = f2;
        float f7 = (f3 - f2) / size;
        float[] fArrArray8 = softAudioBuffer2.array();
        float[] fArrArray9 = softAudioBuffer.array();
        int i13 = 0;
        for (int i14 = this.delay; i14 < size; i14++) {
            f6 += f7;
            int i15 = i14;
            int i16 = i13;
            i13++;
            fArrArray8[i15] = fArrArray8[i15] + (fArrArray9[i16] * f6);
        }
        float[] fArrArray10 = softAudioBuffer3.array();
        for (int i17 = 0; i17 < this.delay; i17++) {
            f6 += f7;
            int i18 = i17;
            int i19 = i13;
            i13++;
            fArrArray10[i18] = fArrArray10[i18] + (fArrArray9[i19] * f6);
        }
    }

    void processAudioLogic(SoftAudioBuffer[] softAudioBufferArr) {
        int i2;
        if (!this.audiostarted) {
            return;
        }
        int size = softAudioBufferArr[0].getSize();
        try {
            this.osc_buff[0] = softAudioBufferArr[10].array();
            if (this.nrofchannels != 1) {
                this.osc_buff[1] = softAudioBufferArr[11].array();
            }
            i2 = this.osc_stream.read(this.osc_buff, 0, size);
        } catch (IOException e2) {
        }
        if (i2 == -1) {
            this.stopping = true;
            return;
        }
        if (i2 != size) {
            Arrays.fill(this.osc_buff[0], i2, size, 0.0f);
            if (this.nrofchannels != 1) {
                Arrays.fill(this.osc_buff[1], i2, size, 0.0f);
            }
        }
        SoftAudioBuffer softAudioBuffer = softAudioBufferArr[0];
        SoftAudioBuffer softAudioBuffer2 = softAudioBufferArr[1];
        SoftAudioBuffer softAudioBuffer3 = softAudioBufferArr[2];
        SoftAudioBuffer softAudioBuffer4 = softAudioBufferArr[6];
        SoftAudioBuffer softAudioBuffer5 = softAudioBufferArr[7];
        SoftAudioBuffer softAudioBuffer6 = softAudioBufferArr[3];
        SoftAudioBuffer softAudioBuffer7 = softAudioBufferArr[4];
        SoftAudioBuffer softAudioBuffer8 = softAudioBufferArr[5];
        SoftAudioBuffer softAudioBuffer9 = softAudioBufferArr[8];
        SoftAudioBuffer softAudioBuffer10 = softAudioBufferArr[9];
        SoftAudioBuffer softAudioBuffer11 = softAudioBufferArr[10];
        SoftAudioBuffer softAudioBuffer12 = softAudioBufferArr[11];
        if (this.osc_stream_nrofchannels == 1) {
            softAudioBuffer12 = null;
        }
        if (!Double.isInfinite(this.co_filter_freq[0])) {
            this.filter_left.processAudio(softAudioBuffer11);
            if (softAudioBuffer12 != null) {
                this.filter_right.processAudio(softAudioBuffer12);
            }
        }
        if (this.nrofchannels == 1) {
            this.out_mixer_left = (this.out_mixer_left + this.out_mixer_right) / 2.0f;
            mixAudioStream(softAudioBuffer11, softAudioBuffer, softAudioBuffer6, this.last_out_mixer_left, this.out_mixer_left);
            if (softAudioBuffer12 != null) {
                mixAudioStream(softAudioBuffer12, softAudioBuffer, softAudioBuffer6, this.last_out_mixer_left, this.out_mixer_left);
            }
        } else if (softAudioBuffer12 == null && this.last_out_mixer_left == this.last_out_mixer_right && this.out_mixer_left == this.out_mixer_right) {
            mixAudioStream(softAudioBuffer11, softAudioBuffer3, softAudioBuffer8, this.last_out_mixer_left, this.out_mixer_left);
        } else {
            mixAudioStream(softAudioBuffer11, softAudioBuffer, softAudioBuffer6, this.last_out_mixer_left, this.out_mixer_left);
            if (softAudioBuffer12 != null) {
                mixAudioStream(softAudioBuffer12, softAudioBuffer2, softAudioBuffer7, this.last_out_mixer_right, this.out_mixer_right);
            } else {
                mixAudioStream(softAudioBuffer11, softAudioBuffer2, softAudioBuffer7, this.last_out_mixer_right, this.out_mixer_right);
            }
        }
        if (softAudioBuffer12 == null) {
            mixAudioStream(softAudioBuffer11, softAudioBuffer4, softAudioBuffer9, this.last_out_mixer_effect1, this.out_mixer_effect1);
            mixAudioStream(softAudioBuffer11, softAudioBuffer5, softAudioBuffer10, this.last_out_mixer_effect2, this.out_mixer_effect2);
        } else {
            mixAudioStream(softAudioBuffer11, softAudioBuffer4, softAudioBuffer9, this.last_out_mixer_effect1 * 0.5f, this.out_mixer_effect1 * 0.5f);
            mixAudioStream(softAudioBuffer11, softAudioBuffer5, softAudioBuffer10, this.last_out_mixer_effect2 * 0.5f, this.out_mixer_effect2 * 0.5f);
            mixAudioStream(softAudioBuffer12, softAudioBuffer4, softAudioBuffer9, this.last_out_mixer_effect1 * 0.5f, this.out_mixer_effect1 * 0.5f);
            mixAudioStream(softAudioBuffer12, softAudioBuffer5, softAudioBuffer10, this.last_out_mixer_effect2 * 0.5f, this.out_mixer_effect2 * 0.5f);
        }
        this.last_out_mixer_left = this.out_mixer_left;
        this.last_out_mixer_right = this.out_mixer_right;
        this.last_out_mixer_effect1 = this.out_mixer_effect1;
        this.last_out_mixer_effect2 = this.out_mixer_effect2;
        if (this.out_mixer_end) {
            this.stopping = true;
        }
    }
}
