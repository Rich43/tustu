package com.sun.media.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Patch;

/* loaded from: rt.jar:com/sun/media/sound/SoftChannel.class */
public final class SoftChannel implements MidiChannel, ModelDirectedPlayer {
    private static boolean[] dontResetControls = new boolean[128];
    private static final int RPN_NULL_VALUE = 16383;
    private final Object control_mutex;
    private int channel;
    private SoftVoice[] voices;
    private int bank;
    private int program;
    private SoftSynthesizer synthesizer;
    private SoftMainMixer mainmixer;
    private int pitchbend;
    private double[][] co_midi_cc_cc;
    private SoftControl co_midi_cc;
    Map<Integer, int[]> co_midi_rpn_rpn_i;
    Map<Integer, double[]> co_midi_rpn_rpn;
    private SoftControl co_midi_rpn;
    Map<Integer, int[]> co_midi_nrpn_nrpn_i;
    Map<Integer, double[]> co_midi_nrpn_nrpn;
    private SoftControl co_midi_nrpn;
    private int[] lastVelocity;
    private int prevVoiceID;
    private boolean firstVoice;
    private int voiceNo;
    private int play_noteNumber;
    private int play_velocity;
    private int play_delay;
    private boolean play_releasetriggered;
    private int rpn_control = RPN_NULL_VALUE;
    private int nrpn_control = RPN_NULL_VALUE;
    double portamento_time = 1.0d;
    int[] portamento_lastnote = new int[128];
    int portamento_lastnote_ix = 0;
    private boolean portamento = false;
    private boolean mono = false;
    private boolean mute = false;
    private boolean solo = false;
    private boolean solomute = false;
    private int[] polypressure = new int[128];
    private int channelpressure = 0;
    private int[] controller = new int[128];
    private double[] co_midi_pitch = new double[1];
    private double[] co_midi_channel_pressure = new double[1];
    SoftTuning tuning = new SoftTuning();
    int tuning_bank = 0;
    int tuning_program = 0;
    SoftInstrument current_instrument = null;
    ModelChannelMixer current_mixer = null;
    ModelDirector current_director = null;
    int cds_control_number = -1;
    ModelConnectionBlock[] cds_control_connections = null;
    ModelConnectionBlock[] cds_channelpressure_connections = null;
    ModelConnectionBlock[] cds_polypressure_connections = null;
    boolean sustain = false;
    boolean[][] keybasedcontroller_active = (boolean[][]) null;
    double[][] keybasedcontroller_value = (double[][]) null;
    private SoftControl[] co_midi = new SoftControl[128];

    static {
        for (int i2 = 0; i2 < dontResetControls.length; i2++) {
            dontResetControls[i2] = false;
        }
        dontResetControls[0] = true;
        dontResetControls[32] = true;
        dontResetControls[7] = true;
        dontResetControls[8] = true;
        dontResetControls[10] = true;
        dontResetControls[11] = true;
        dontResetControls[91] = true;
        dontResetControls[92] = true;
        dontResetControls[93] = true;
        dontResetControls[94] = true;
        dontResetControls[95] = true;
        dontResetControls[70] = true;
        dontResetControls[71] = true;
        dontResetControls[72] = true;
        dontResetControls[73] = true;
        dontResetControls[74] = true;
        dontResetControls[75] = true;
        dontResetControls[76] = true;
        dontResetControls[77] = true;
        dontResetControls[78] = true;
        dontResetControls[79] = true;
        dontResetControls[120] = true;
        dontResetControls[121] = true;
        dontResetControls[122] = true;
        dontResetControls[123] = true;
        dontResetControls[124] = true;
        dontResetControls[125] = true;
        dontResetControls[126] = true;
        dontResetControls[127] = true;
        dontResetControls[6] = true;
        dontResetControls[38] = true;
        dontResetControls[96] = true;
        dontResetControls[97] = true;
        dontResetControls[98] = true;
        dontResetControls[99] = true;
        dontResetControls[100] = true;
        dontResetControls[101] = true;
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftChannel$MidiControlObject.class */
    private class MidiControlObject implements SoftControl {
        double[] pitch;
        double[] channel_pressure;
        double[] poly_pressure;

        private MidiControlObject() {
            this.pitch = SoftChannel.this.co_midi_pitch;
            this.channel_pressure = SoftChannel.this.co_midi_channel_pressure;
            this.poly_pressure = new double[1];
        }

        @Override // com.sun.media.sound.SoftControl
        public double[] get(int i2, String str) {
            if (str == null) {
                return null;
            }
            if (str.equals("pitch")) {
                return this.pitch;
            }
            if (str.equals("channel_pressure")) {
                return this.channel_pressure;
            }
            if (str.equals("poly_pressure")) {
                return this.poly_pressure;
            }
            return null;
        }
    }

    private static int restrict7Bit(int i2) {
        if (i2 < 0) {
            return 0;
        }
        if (i2 > 127) {
            return 127;
        }
        return i2;
    }

    private static int restrict14Bit(int i2) {
        if (i2 < 0) {
            return 0;
        }
        if (i2 > 16256) {
            return 16256;
        }
        return i2;
    }

    public SoftChannel(SoftSynthesizer softSynthesizer, int i2) {
        for (int i3 = 0; i3 < this.co_midi.length; i3++) {
            this.co_midi[i3] = new MidiControlObject();
        }
        this.co_midi_cc_cc = new double[128][1];
        this.co_midi_cc = new SoftControl() { // from class: com.sun.media.sound.SoftChannel.1
            double[][] cc;

            {
                this.cc = SoftChannel.this.co_midi_cc_cc;
            }

            @Override // com.sun.media.sound.SoftControl
            public double[] get(int i4, String str) {
                if (str == null) {
                    return null;
                }
                return this.cc[Integer.parseInt(str)];
            }
        };
        this.co_midi_rpn_rpn_i = new HashMap();
        this.co_midi_rpn_rpn = new HashMap();
        this.co_midi_rpn = new SoftControl() { // from class: com.sun.media.sound.SoftChannel.2
            Map<Integer, double[]> rpn;

            {
                this.rpn = SoftChannel.this.co_midi_rpn_rpn;
            }

            @Override // com.sun.media.sound.SoftControl
            public double[] get(int i4, String str) throws NumberFormatException {
                if (str == null) {
                    return null;
                }
                int i5 = Integer.parseInt(str);
                double[] dArr = this.rpn.get(Integer.valueOf(i5));
                if (dArr == null) {
                    dArr = new double[1];
                    this.rpn.put(Integer.valueOf(i5), dArr);
                }
                return dArr;
            }
        };
        this.co_midi_nrpn_nrpn_i = new HashMap();
        this.co_midi_nrpn_nrpn = new HashMap();
        this.co_midi_nrpn = new SoftControl() { // from class: com.sun.media.sound.SoftChannel.3
            Map<Integer, double[]> nrpn;

            {
                this.nrpn = SoftChannel.this.co_midi_nrpn_nrpn;
            }

            @Override // com.sun.media.sound.SoftControl
            public double[] get(int i4, String str) throws NumberFormatException {
                if (str == null) {
                    return null;
                }
                int i5 = Integer.parseInt(str);
                double[] dArr = this.nrpn.get(Integer.valueOf(i5));
                if (dArr == null) {
                    dArr = new double[1];
                    this.nrpn.put(Integer.valueOf(i5), dArr);
                }
                return dArr;
            }
        };
        this.lastVelocity = new int[128];
        this.firstVoice = true;
        this.voiceNo = 0;
        this.play_noteNumber = 0;
        this.play_velocity = 0;
        this.play_delay = 0;
        this.play_releasetriggered = false;
        this.channel = i2;
        this.voices = softSynthesizer.getVoices();
        this.synthesizer = softSynthesizer;
        this.mainmixer = softSynthesizer.getMainMixer();
        this.control_mutex = softSynthesizer.control_mutex;
        resetAllControllers(true);
    }

    private int findFreeVoice(int i2) {
        if (i2 == -1) {
            return -1;
        }
        for (int i3 = i2; i3 < this.voices.length; i3++) {
            if (!this.voices[i3].active) {
                return i3;
            }
        }
        if (this.synthesizer.getVoiceAllocationMode() == 1) {
            int i4 = this.channel;
            for (int i5 = 0; i5 < this.voices.length; i5++) {
                if (this.voices[i5].stealer_channel == null) {
                    if (i4 == 9) {
                        i4 = this.voices[i5].channel;
                    } else if (this.voices[i5].channel != 9 && this.voices[i5].channel > i4) {
                        i4 = this.voices[i5].channel;
                    }
                }
            }
            int i6 = -1;
            SoftVoice softVoice = null;
            for (int i7 = 0; i7 < this.voices.length; i7++) {
                if (this.voices[i7].channel == i4 && this.voices[i7].stealer_channel == null && !this.voices[i7].on) {
                    if (softVoice == null) {
                        softVoice = this.voices[i7];
                        i6 = i7;
                    }
                    if (this.voices[i7].voiceID < softVoice.voiceID) {
                        softVoice = this.voices[i7];
                        i6 = i7;
                    }
                }
            }
            if (i6 == -1) {
                for (int i8 = 0; i8 < this.voices.length; i8++) {
                    if (this.voices[i8].channel == i4 && this.voices[i8].stealer_channel == null) {
                        if (softVoice == null) {
                            softVoice = this.voices[i8];
                            i6 = i8;
                        }
                        if (this.voices[i8].voiceID < softVoice.voiceID) {
                            softVoice = this.voices[i8];
                            i6 = i8;
                        }
                    }
                }
            }
            return i6;
        }
        int i9 = -1;
        SoftVoice softVoice2 = null;
        for (int i10 = 0; i10 < this.voices.length; i10++) {
            if (this.voices[i10].stealer_channel == null && !this.voices[i10].on) {
                if (softVoice2 == null) {
                    softVoice2 = this.voices[i10];
                    i9 = i10;
                }
                if (this.voices[i10].voiceID < softVoice2.voiceID) {
                    softVoice2 = this.voices[i10];
                    i9 = i10;
                }
            }
        }
        if (i9 == -1) {
            for (int i11 = 0; i11 < this.voices.length; i11++) {
                if (this.voices[i11].stealer_channel == null) {
                    if (softVoice2 == null) {
                        softVoice2 = this.voices[i11];
                        i9 = i11;
                    }
                    if (this.voices[i11].voiceID < softVoice2.voiceID) {
                        softVoice2 = this.voices[i11];
                        i9 = i11;
                    }
                }
            }
        }
        return i9;
    }

    void initVoice(SoftVoice softVoice, SoftPerformer softPerformer, int i2, int i3, int i4, int i5, ModelConnectionBlock[] modelConnectionBlockArr, ModelChannelMixer modelChannelMixer, boolean z2) {
        if (softVoice.active) {
            softVoice.stealer_channel = this;
            softVoice.stealer_performer = softPerformer;
            softVoice.stealer_voiceID = i2;
            softVoice.stealer_noteNumber = i3;
            softVoice.stealer_velocity = i4;
            softVoice.stealer_extendedConnectionBlocks = modelConnectionBlockArr;
            softVoice.stealer_channelmixer = modelChannelMixer;
            softVoice.stealer_releaseTriggered = z2;
            for (int i6 = 0; i6 < this.voices.length; i6++) {
                if (this.voices[i6].active && this.voices[i6].voiceID == softVoice.voiceID) {
                    this.voices[i6].soundOff();
                }
            }
            return;
        }
        softVoice.extendedConnectionBlocks = modelConnectionBlockArr;
        softVoice.channelmixer = modelChannelMixer;
        softVoice.releaseTriggered = z2;
        softVoice.voiceID = i2;
        softVoice.tuning = this.tuning;
        softVoice.exclusiveClass = softPerformer.exclusiveClass;
        softVoice.softchannel = this;
        softVoice.channel = this.channel;
        softVoice.bank = this.bank;
        softVoice.program = this.program;
        softVoice.instrument = this.current_instrument;
        softVoice.performer = softPerformer;
        softVoice.objects.clear();
        softVoice.objects.put("midi", this.co_midi[i3]);
        softVoice.objects.put("midi_cc", this.co_midi_cc);
        softVoice.objects.put("midi_rpn", this.co_midi_rpn);
        softVoice.objects.put("midi_nrpn", this.co_midi_nrpn);
        softVoice.noteOn(i3, i4, i5);
        softVoice.setMute(this.mute);
        softVoice.setSoloMute(this.solomute);
        if (z2) {
            return;
        }
        if (this.controller[84] != 0) {
            softVoice.co_noteon_keynumber[0] = (this.tuning.getTuning(this.controller[84]) / 100.0d) * 0.0078125d;
            softVoice.portamento = true;
            controlChange(84, 0);
        } else if (this.portamento) {
            if (this.mono) {
                if (this.portamento_lastnote[0] != -1) {
                    softVoice.co_noteon_keynumber[0] = (this.tuning.getTuning(this.portamento_lastnote[0]) / 100.0d) * 0.0078125d;
                    softVoice.portamento = true;
                    controlChange(84, 0);
                }
                this.portamento_lastnote[0] = i3;
                return;
            }
            if (this.portamento_lastnote_ix != 0) {
                this.portamento_lastnote_ix--;
                softVoice.co_noteon_keynumber[0] = (this.tuning.getTuning(this.portamento_lastnote[this.portamento_lastnote_ix]) / 100.0d) * 0.0078125d;
                softVoice.portamento = true;
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOn(int i2, int i3) {
        noteOn(i2, i3, 0);
    }

    void noteOn(int i2, int i3, int i4) {
        int iRestrict7Bit = restrict7Bit(i2);
        int iRestrict7Bit2 = restrict7Bit(i3);
        noteOn_internal(iRestrict7Bit, iRestrict7Bit2, i4);
        if (this.current_mixer != null) {
            this.current_mixer.noteOn(iRestrict7Bit, iRestrict7Bit2);
        }
    }

    private void noteOn_internal(int i2, int i3, int i4) {
        if (i3 == 0) {
            noteOff_internal(i2, 64);
            return;
        }
        synchronized (this.control_mutex) {
            if (this.sustain) {
                this.sustain = false;
                for (int i5 = 0; i5 < this.voices.length; i5++) {
                    if ((this.voices[i5].sustain || this.voices[i5].on) && this.voices[i5].channel == this.channel && this.voices[i5].active && this.voices[i5].note == i2) {
                        this.voices[i5].sustain = false;
                        this.voices[i5].on = true;
                        this.voices[i5].noteOff(0);
                    }
                }
                this.sustain = true;
            }
            this.mainmixer.activity();
            if (this.mono) {
                if (this.portamento) {
                    boolean z2 = false;
                    for (int i6 = 0; i6 < this.voices.length; i6++) {
                        if (this.voices[i6].on && this.voices[i6].channel == this.channel && this.voices[i6].active && !this.voices[i6].releaseTriggered) {
                            this.voices[i6].portamento = true;
                            this.voices[i6].setNote(i2);
                            z2 = true;
                        }
                    }
                    if (z2) {
                        this.portamento_lastnote[0] = i2;
                        return;
                    }
                }
                if (this.controller[84] != 0) {
                    boolean z3 = false;
                    for (int i7 = 0; i7 < this.voices.length; i7++) {
                        if (this.voices[i7].on && this.voices[i7].channel == this.channel && this.voices[i7].active && this.voices[i7].note == this.controller[84] && !this.voices[i7].releaseTriggered) {
                            this.voices[i7].portamento = true;
                            this.voices[i7].setNote(i2);
                            z3 = true;
                        }
                    }
                    controlChange(84, 0);
                    if (z3) {
                        return;
                    }
                }
            }
            if (this.mono) {
                allNotesOff();
            }
            if (this.current_instrument == null) {
                this.current_instrument = this.synthesizer.findInstrument(this.program, this.bank, this.channel);
                if (this.current_instrument == null) {
                    return;
                }
                if (this.current_mixer != null) {
                    this.mainmixer.stopMixer(this.current_mixer);
                }
                this.current_mixer = this.current_instrument.getSourceInstrument().getChannelMixer(this, this.synthesizer.getFormat());
                if (this.current_mixer != null) {
                    this.mainmixer.registerMixer(this.current_mixer);
                }
                this.current_director = this.current_instrument.getDirector(this, this);
                applyInstrumentCustomization();
            }
            SoftSynthesizer softSynthesizer = this.synthesizer;
            int i8 = softSynthesizer.voiceIDCounter;
            softSynthesizer.voiceIDCounter = i8 + 1;
            this.prevVoiceID = i8;
            this.firstVoice = true;
            this.voiceNo = 0;
            int iRound = (int) Math.round(this.tuning.getTuning(i2) / 100.0d);
            this.play_noteNumber = i2;
            this.play_velocity = i3;
            this.play_delay = i4;
            this.play_releasetriggered = false;
            this.lastVelocity[i2] = i3;
            this.current_director.noteOn(iRound, i3);
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOff(int i2, int i3) {
        int iRestrict7Bit = restrict7Bit(i2);
        int iRestrict7Bit2 = restrict7Bit(i3);
        noteOff_internal(iRestrict7Bit, iRestrict7Bit2);
        if (this.current_mixer != null) {
            this.current_mixer.noteOff(iRestrict7Bit, iRestrict7Bit2);
        }
    }

    private void noteOff_internal(int i2, int i3) {
        synchronized (this.control_mutex) {
            if (!this.mono && this.portamento && this.portamento_lastnote_ix != 127) {
                this.portamento_lastnote[this.portamento_lastnote_ix] = i2;
                this.portamento_lastnote_ix++;
            }
            this.mainmixer.activity();
            for (int i4 = 0; i4 < this.voices.length; i4++) {
                if (this.voices[i4].on && this.voices[i4].channel == this.channel && this.voices[i4].note == i2 && !this.voices[i4].releaseTriggered) {
                    this.voices[i4].noteOff(i3);
                }
                if (this.voices[i4].stealer_channel == this && this.voices[i4].stealer_noteNumber == i2) {
                    SoftVoice softVoice = this.voices[i4];
                    softVoice.stealer_releaseTriggered = false;
                    softVoice.stealer_channel = null;
                    softVoice.stealer_performer = null;
                    softVoice.stealer_voiceID = -1;
                    softVoice.stealer_noteNumber = 0;
                    softVoice.stealer_velocity = 0;
                    softVoice.stealer_extendedConnectionBlocks = null;
                    softVoice.stealer_channelmixer = null;
                }
            }
            if (this.current_instrument == null) {
                this.current_instrument = this.synthesizer.findInstrument(this.program, this.bank, this.channel);
                if (this.current_instrument == null) {
                    return;
                }
                if (this.current_mixer != null) {
                    this.mainmixer.stopMixer(this.current_mixer);
                }
                this.current_mixer = this.current_instrument.getSourceInstrument().getChannelMixer(this, this.synthesizer.getFormat());
                if (this.current_mixer != null) {
                    this.mainmixer.registerMixer(this.current_mixer);
                }
                this.current_director = this.current_instrument.getDirector(this, this);
                applyInstrumentCustomization();
            }
            SoftSynthesizer softSynthesizer = this.synthesizer;
            int i5 = softSynthesizer.voiceIDCounter;
            softSynthesizer.voiceIDCounter = i5 + 1;
            this.prevVoiceID = i5;
            this.firstVoice = true;
            this.voiceNo = 0;
            int iRound = (int) Math.round(this.tuning.getTuning(i2) / 100.0d);
            this.play_noteNumber = i2;
            this.play_velocity = this.lastVelocity[i2];
            this.play_releasetriggered = true;
            this.play_delay = 0;
            this.current_director.noteOff(iRound, i3);
        }
    }

    @Override // com.sun.media.sound.ModelDirectedPlayer
    public void play(int i2, ModelConnectionBlock[] modelConnectionBlockArr) {
        int i3 = this.play_noteNumber;
        int i4 = this.play_velocity;
        int i5 = this.play_delay;
        boolean z2 = this.play_releasetriggered;
        SoftPerformer performer = this.current_instrument.getPerformer(i2);
        if (this.firstVoice) {
            this.firstVoice = false;
            if (performer.exclusiveClass != 0) {
                int i6 = performer.exclusiveClass;
                for (int i7 = 0; i7 < this.voices.length; i7++) {
                    if (this.voices[i7].active && this.voices[i7].channel == this.channel && this.voices[i7].exclusiveClass == i6 && (!performer.selfNonExclusive || this.voices[i7].note != i3)) {
                        this.voices[i7].shutdown();
                    }
                }
            }
        }
        this.voiceNo = findFreeVoice(this.voiceNo);
        if (this.voiceNo == -1) {
            return;
        }
        initVoice(this.voices[this.voiceNo], performer, this.prevVoiceID, i3, i4, i5, modelConnectionBlockArr, this.current_mixer, z2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void noteOff(int i2) {
        if (i2 < 0 || i2 > 127) {
            return;
        }
        noteOff_internal(i2, 64);
    }

    @Override // javax.sound.midi.MidiChannel
    public void setPolyPressure(int i2, int i3) {
        int iRestrict7Bit = restrict7Bit(i2);
        int iRestrict7Bit2 = restrict7Bit(i3);
        if (this.current_mixer != null) {
            this.current_mixer.setPolyPressure(iRestrict7Bit, iRestrict7Bit2);
        }
        synchronized (this.control_mutex) {
            this.mainmixer.activity();
            this.co_midi[iRestrict7Bit].get(0, "poly_pressure")[0] = iRestrict7Bit2 * 0.0078125d;
            this.polypressure[iRestrict7Bit] = iRestrict7Bit2;
            for (int i4 = 0; i4 < this.voices.length; i4++) {
                if (this.voices[i4].active && this.voices[i4].note == iRestrict7Bit) {
                    this.voices[i4].setPolyPressure(iRestrict7Bit2);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public int getPolyPressure(int i2) {
        int i3;
        synchronized (this.control_mutex) {
            i3 = this.polypressure[i2];
        }
        return i3;
    }

    @Override // javax.sound.midi.MidiChannel
    public void setChannelPressure(int i2) {
        int iRestrict7Bit = restrict7Bit(i2);
        if (this.current_mixer != null) {
            this.current_mixer.setChannelPressure(iRestrict7Bit);
        }
        synchronized (this.control_mutex) {
            this.mainmixer.activity();
            this.co_midi_channel_pressure[0] = iRestrict7Bit * 0.0078125d;
            this.channelpressure = iRestrict7Bit;
            for (int i3 = 0; i3 < this.voices.length; i3++) {
                if (this.voices[i3].active) {
                    this.voices[i3].setChannelPressure(iRestrict7Bit);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public int getChannelPressure() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = this.channelpressure;
        }
        return i2;
    }

    void applyInstrumentCustomization() {
        if (this.cds_control_connections == null && this.cds_channelpressure_connections == null && this.cds_polypressure_connections == null) {
            return;
        }
        ModelInstrument sourceInstrument = this.current_instrument.getSourceInstrument();
        ModelPerformer[] performers = sourceInstrument.getPerformers();
        ModelPerformer[] modelPerformerArr = new ModelPerformer[performers.length];
        for (int i2 = 0; i2 < modelPerformerArr.length; i2++) {
            ModelPerformer modelPerformer = performers[i2];
            ModelPerformer modelPerformer2 = new ModelPerformer();
            modelPerformer2.setName(modelPerformer.getName());
            modelPerformer2.setExclusiveClass(modelPerformer.getExclusiveClass());
            modelPerformer2.setKeyFrom(modelPerformer.getKeyFrom());
            modelPerformer2.setKeyTo(modelPerformer.getKeyTo());
            modelPerformer2.setVelFrom(modelPerformer.getVelFrom());
            modelPerformer2.setVelTo(modelPerformer.getVelTo());
            modelPerformer2.getOscillators().addAll(modelPerformer.getOscillators());
            modelPerformer2.getConnectionBlocks().addAll(modelPerformer.getConnectionBlocks());
            modelPerformerArr[i2] = modelPerformer2;
            List<ModelConnectionBlock> connectionBlocks = modelPerformer2.getConnectionBlocks();
            if (this.cds_control_connections != null) {
                String string = Integer.toString(this.cds_control_number);
                Iterator<ModelConnectionBlock> it = connectionBlocks.iterator();
                while (it.hasNext()) {
                    ModelSource[] sources = it.next().getSources();
                    boolean z2 = false;
                    if (sources != null) {
                        for (ModelSource modelSource : sources) {
                            if ("midi_cc".equals(modelSource.getIdentifier().getObject()) && string.equals(modelSource.getIdentifier().getVariable())) {
                                z2 = true;
                            }
                        }
                    }
                    if (z2) {
                        it.remove();
                    }
                }
                for (int i3 = 0; i3 < this.cds_control_connections.length; i3++) {
                    connectionBlocks.add(this.cds_control_connections[i3]);
                }
            }
            if (this.cds_polypressure_connections != null) {
                Iterator<ModelConnectionBlock> it2 = connectionBlocks.iterator();
                while (it2.hasNext()) {
                    ModelSource[] sources2 = it2.next().getSources();
                    boolean z3 = false;
                    if (sources2 != null) {
                        for (ModelSource modelSource2 : sources2) {
                            if ("midi".equals(modelSource2.getIdentifier().getObject()) && "poly_pressure".equals(modelSource2.getIdentifier().getVariable())) {
                                z3 = true;
                            }
                        }
                    }
                    if (z3) {
                        it2.remove();
                    }
                }
                for (int i4 = 0; i4 < this.cds_polypressure_connections.length; i4++) {
                    connectionBlocks.add(this.cds_polypressure_connections[i4]);
                }
            }
            if (this.cds_channelpressure_connections != null) {
                Iterator<ModelConnectionBlock> it3 = connectionBlocks.iterator();
                while (it3.hasNext()) {
                    ModelSource[] sources3 = it3.next().getSources();
                    boolean z4 = false;
                    if (sources3 != null) {
                        for (ModelSource modelSource3 : sources3) {
                            ModelIdentifier identifier = modelSource3.getIdentifier();
                            if ("midi".equals(identifier.getObject()) && "channel_pressure".equals(identifier.getVariable())) {
                                z4 = true;
                            }
                        }
                    }
                    if (z4) {
                        it3.remove();
                    }
                }
                for (int i5 = 0; i5 < this.cds_channelpressure_connections.length; i5++) {
                    connectionBlocks.add(this.cds_channelpressure_connections[i5]);
                }
            }
        }
        this.current_instrument = new SoftInstrument(sourceInstrument, modelPerformerArr);
    }

    private ModelConnectionBlock[] createModelConnections(ModelIdentifier modelIdentifier, int[] iArr, int[] iArr2) {
        ModelConnectionBlock modelConnectionBlock;
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            int i4 = iArr2[i2];
            if (i3 == 0) {
                arrayList.add(new ModelConnectionBlock(new ModelSource(modelIdentifier, false, false, 0), (i4 - 64) * 100, new ModelDestination(new ModelIdentifier("osc", "pitch"))));
            }
            if (i3 == 1) {
                double d2 = ((i4 / 64.0d) - 1.0d) * 9600.0d;
                if (d2 > 0.0d) {
                    modelConnectionBlock = new ModelConnectionBlock(new ModelSource(modelIdentifier, true, false, 0), -d2, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
                } else {
                    modelConnectionBlock = new ModelConnectionBlock(new ModelSource(modelIdentifier, false, false, 0), d2, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ));
                }
                arrayList.add(modelConnectionBlock);
            }
            if (i3 == 2) {
                final double d3 = i4 / 64.0d;
                arrayList.add(new ModelConnectionBlock(new ModelSource(modelIdentifier, new ModelTransform() { // from class: com.sun.media.sound.SoftChannel.4

                    /* renamed from: s, reason: collision with root package name */
                    double f11980s;

                    {
                        this.f11980s = d3;
                    }

                    @Override // com.sun.media.sound.ModelTransform
                    public double transform(double d4) {
                        double d5;
                        if (this.f11980s < 1.0d) {
                            d5 = this.f11980s + (d4 * (1.0d - this.f11980s));
                        } else if (this.f11980s > 1.0d) {
                            d5 = 1.0d + (d4 * (this.f11980s - 1.0d));
                        } else {
                            return 0.0d;
                        }
                        return (-(0.4166666666666667d / Math.log(10.0d))) * Math.log(d5);
                    }
                }), -960.0d, new ModelDestination(ModelDestination.DESTINATION_GAIN)));
            }
            if (i3 == 3) {
                arrayList.add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true, 0), new ModelSource(modelIdentifier, false, false, 0), ((i4 / 64.0d) - 1.0d) * 9600.0d, new ModelDestination(ModelDestination.DESTINATION_PITCH)));
            }
            if (i3 == 4) {
                arrayList.add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true, 0), new ModelSource(modelIdentifier, false, false, 0), (i4 / 128.0d) * 2400.0d, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ)));
            }
            if (i3 == 5) {
                final double d4 = i4 / 127.0d;
                arrayList.add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, false, 0), new ModelSource(modelIdentifier, new ModelTransform() { // from class: com.sun.media.sound.SoftChannel.5

                    /* renamed from: s, reason: collision with root package name */
                    double f11981s;

                    {
                        this.f11981s = d4;
                    }

                    @Override // com.sun.media.sound.ModelTransform
                    public double transform(double d5) {
                        return (-(0.4166666666666667d / Math.log(10.0d))) * Math.log(1.0d - (d5 * this.f11981s));
                    }
                }), -960.0d, new ModelDestination(ModelDestination.DESTINATION_GAIN)));
            }
        }
        return (ModelConnectionBlock[]) arrayList.toArray(new ModelConnectionBlock[arrayList.size()]);
    }

    public void mapPolyPressureToDestination(int[] iArr, int[] iArr2) {
        this.current_instrument = null;
        if (iArr.length == 0) {
            this.cds_polypressure_connections = null;
        } else {
            this.cds_polypressure_connections = createModelConnections(new ModelIdentifier("midi", "poly_pressure"), iArr, iArr2);
        }
    }

    public void mapChannelPressureToDestination(int[] iArr, int[] iArr2) {
        this.current_instrument = null;
        if (iArr.length == 0) {
            this.cds_channelpressure_connections = null;
        } else {
            this.cds_channelpressure_connections = createModelConnections(new ModelIdentifier("midi", "channel_pressure"), iArr, iArr2);
        }
    }

    public void mapControlToDestination(int i2, int[] iArr, int[] iArr2) {
        if ((i2 < 1 || i2 > 31) && (i2 < 64 || i2 > 95)) {
            this.cds_control_connections = null;
            return;
        }
        this.current_instrument = null;
        this.cds_control_number = i2;
        if (iArr.length == 0) {
            this.cds_control_connections = null;
        } else {
            this.cds_control_connections = createModelConnections(new ModelIdentifier("midi_cc", Integer.toString(i2)), iArr, iArr2);
        }
    }

    /* JADX WARN: Type inference failed for: r1v36, types: [boolean[], boolean[][]] */
    /* JADX WARN: Type inference failed for: r1v38, types: [double[], double[][]] */
    public void controlChangePerNote(int i2, int i3, int i4) {
        if (this.keybasedcontroller_active == null) {
            this.keybasedcontroller_active = new boolean[128];
            this.keybasedcontroller_value = new double[128];
        }
        if (this.keybasedcontroller_active[i2] == null) {
            this.keybasedcontroller_active[i2] = new boolean[128];
            Arrays.fill(this.keybasedcontroller_active[i2], false);
            this.keybasedcontroller_value[i2] = new double[128];
            Arrays.fill(this.keybasedcontroller_value[i2], 0.0d);
        }
        if (i4 == -1) {
            this.keybasedcontroller_active[i2][i3] = false;
        } else {
            this.keybasedcontroller_active[i2][i3] = true;
            this.keybasedcontroller_value[i2][i3] = i4 / 128.0d;
        }
        if (i3 < 120) {
            for (int i5 = 0; i5 < this.voices.length; i5++) {
                if (this.voices[i5].active) {
                    this.voices[i5].controlChange(i3, -1);
                }
            }
            return;
        }
        if (i3 == 120) {
            for (int i6 = 0; i6 < this.voices.length; i6++) {
                if (this.voices[i6].active) {
                    this.voices[i6].rpnChange(1, -1);
                }
            }
            return;
        }
        if (i3 == 121) {
            for (int i7 = 0; i7 < this.voices.length; i7++) {
                if (this.voices[i7].active) {
                    this.voices[i7].rpnChange(2, -1);
                }
            }
        }
    }

    public int getControlPerNote(int i2, int i3) {
        if (this.keybasedcontroller_active == null || this.keybasedcontroller_active[i2] == null || !this.keybasedcontroller_active[i2][i3]) {
            return -1;
        }
        return (int) (this.keybasedcontroller_value[i2][i3] * 128.0d);
    }

    @Override // javax.sound.midi.MidiChannel
    public void controlChange(int i2, int i3) {
        int[] iArr;
        int[] iArr2;
        int iRestrict7Bit = restrict7Bit(i2);
        int iRestrict7Bit2 = restrict7Bit(i3);
        if (this.current_mixer != null) {
            this.current_mixer.controlChange(iRestrict7Bit, iRestrict7Bit2);
        }
        synchronized (this.control_mutex) {
            switch (iRestrict7Bit) {
                case 5:
                    this.portamento_time = (((Math.pow(100000.0d, ((-Math.asin(((iRestrict7Bit2 / 128.0d) * 2.0d) - 1.0d)) / 3.141592653589793d) + 0.5d) / 100.0d) / 100.0d) * 1000.0d) / this.synthesizer.getControlRate();
                    break;
                case 6:
                case 38:
                case 96:
                case 97:
                    int i4 = 0;
                    if (this.nrpn_control != RPN_NULL_VALUE && (iArr2 = this.co_midi_nrpn_nrpn_i.get(Integer.valueOf(this.nrpn_control))) != null) {
                        i4 = iArr2[0];
                    }
                    if (this.rpn_control != RPN_NULL_VALUE && (iArr = this.co_midi_rpn_rpn_i.get(Integer.valueOf(this.rpn_control))) != null) {
                        i4 = iArr[0];
                    }
                    if (iRestrict7Bit == 6) {
                        i4 = (i4 & 127) + (iRestrict7Bit2 << 7);
                    } else if (iRestrict7Bit == 38) {
                        i4 = (i4 & 16256) + iRestrict7Bit2;
                    } else if (iRestrict7Bit == 96 || iRestrict7Bit == 97) {
                        int i5 = 1;
                        if (this.rpn_control == 2 || this.rpn_control == 3 || this.rpn_control == 4) {
                            i5 = 128;
                        }
                        if (iRestrict7Bit == 96) {
                            i4 += i5;
                        }
                        if (iRestrict7Bit == 97) {
                            i4 -= i5;
                        }
                    }
                    if (this.nrpn_control != RPN_NULL_VALUE) {
                        nrpnChange(this.nrpn_control, i4);
                    }
                    if (this.rpn_control != RPN_NULL_VALUE) {
                        rpnChange(this.rpn_control, i4);
                        break;
                    }
                    break;
                case 64:
                    boolean z2 = iRestrict7Bit2 >= 64;
                    if (this.sustain != z2) {
                        this.sustain = z2;
                        if (!z2) {
                            for (int i6 = 0; i6 < this.voices.length; i6++) {
                                if (this.voices[i6].active && this.voices[i6].sustain && this.voices[i6].channel == this.channel) {
                                    this.voices[i6].sustain = false;
                                    if (!this.voices[i6].on) {
                                        this.voices[i6].on = true;
                                        this.voices[i6].noteOff(0);
                                    }
                                }
                            }
                            break;
                        } else {
                            for (int i7 = 0; i7 < this.voices.length; i7++) {
                                if (this.voices[i7].active && this.voices[i7].channel == this.channel) {
                                    this.voices[i7].redamp();
                                }
                            }
                            break;
                        }
                    }
                    break;
                case 65:
                    this.portamento = iRestrict7Bit2 >= 64;
                    this.portamento_lastnote[0] = -1;
                    this.portamento_lastnote_ix = 0;
                    break;
                case 66:
                    boolean z3 = iRestrict7Bit2 >= 64;
                    if (z3) {
                        for (int i8 = 0; i8 < this.voices.length; i8++) {
                            if (this.voices[i8].active && this.voices[i8].on && this.voices[i8].channel == this.channel) {
                                this.voices[i8].sostenuto = true;
                            }
                        }
                    }
                    if (!z3) {
                        for (int i9 = 0; i9 < this.voices.length; i9++) {
                            if (this.voices[i9].active && this.voices[i9].sostenuto && this.voices[i9].channel == this.channel) {
                                this.voices[i9].sostenuto = false;
                                if (!this.voices[i9].on) {
                                    this.voices[i9].on = true;
                                    this.voices[i9].noteOff(0);
                                }
                            }
                        }
                        break;
                    }
                    break;
                case 98:
                    this.nrpn_control = (this.nrpn_control & 16256) + iRestrict7Bit2;
                    this.rpn_control = RPN_NULL_VALUE;
                    break;
                case 99:
                    this.nrpn_control = (this.nrpn_control & 127) + (iRestrict7Bit2 << 7);
                    this.rpn_control = RPN_NULL_VALUE;
                    break;
                case 100:
                    this.rpn_control = (this.rpn_control & 16256) + iRestrict7Bit2;
                    this.nrpn_control = RPN_NULL_VALUE;
                    break;
                case 101:
                    this.rpn_control = (this.rpn_control & 127) + (iRestrict7Bit2 << 7);
                    this.nrpn_control = RPN_NULL_VALUE;
                    break;
                case 120:
                    allSoundOff();
                    break;
                case 121:
                    resetAllControllers(iRestrict7Bit2 == 127);
                    break;
                case 122:
                    localControl(iRestrict7Bit2 >= 64);
                    break;
                case 123:
                    allNotesOff();
                    break;
                case 124:
                    setOmni(false);
                    break;
                case 125:
                    setOmni(true);
                    break;
                case 126:
                    if (iRestrict7Bit2 == 1) {
                        setMono(true);
                        break;
                    }
                    break;
                case 127:
                    setMono(false);
                    break;
            }
            this.co_midi_cc_cc[iRestrict7Bit][0] = iRestrict7Bit2 * 0.0078125d;
            if (iRestrict7Bit == 0) {
                this.bank = iRestrict7Bit2 << 7;
                return;
            }
            if (iRestrict7Bit == 32) {
                this.bank = (this.bank & 16256) + iRestrict7Bit2;
                return;
            }
            this.controller[iRestrict7Bit] = iRestrict7Bit2;
            if (iRestrict7Bit < 32) {
                this.controller[iRestrict7Bit + 32] = 0;
            }
            for (int i10 = 0; i10 < this.voices.length; i10++) {
                if (this.voices[i10].active) {
                    this.voices[i10].controlChange(iRestrict7Bit, iRestrict7Bit2);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public int getController(int i2) {
        int i3;
        synchronized (this.control_mutex) {
            i3 = this.controller[i2] & 127;
        }
        return i3;
    }

    public void tuningChange(int i2) {
        tuningChange(0, i2);
    }

    public void tuningChange(int i2, int i3) {
        synchronized (this.control_mutex) {
            this.tuning = this.synthesizer.getTuning(new Patch(i2, i3));
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public void programChange(int i2) {
        programChange(this.bank, i2);
    }

    @Override // javax.sound.midi.MidiChannel
    public void programChange(int i2, int i3) {
        int iRestrict14Bit = restrict14Bit(i2);
        int iRestrict7Bit = restrict7Bit(i3);
        synchronized (this.control_mutex) {
            this.mainmixer.activity();
            if (this.bank != iRestrict14Bit || this.program != iRestrict7Bit) {
                this.bank = iRestrict14Bit;
                this.program = iRestrict7Bit;
                this.current_instrument = null;
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public int getProgram() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = this.program;
        }
        return i2;
    }

    @Override // javax.sound.midi.MidiChannel
    public void setPitchBend(int i2) {
        int iRestrict14Bit = restrict14Bit(i2);
        if (this.current_mixer != null) {
            this.current_mixer.setPitchBend(iRestrict14Bit);
        }
        synchronized (this.control_mutex) {
            this.mainmixer.activity();
            this.co_midi_pitch[0] = iRestrict14Bit * 6.103515625E-5d;
            this.pitchbend = iRestrict14Bit;
            for (int i3 = 0; i3 < this.voices.length; i3++) {
                if (this.voices[i3].active) {
                    this.voices[i3].setPitchBend(iRestrict14Bit);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public int getPitchBend() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = this.pitchbend;
        }
        return i2;
    }

    public void nrpnChange(int i2, int i3) {
        if (this.synthesizer.getGeneralMidiMode() == 0) {
            if (i2 == 136) {
                controlChange(76, i3 >> 7);
            }
            if (i2 == 137) {
                controlChange(77, i3 >> 7);
            }
            if (i2 == 138) {
                controlChange(78, i3 >> 7);
            }
            if (i2 == 160) {
                controlChange(74, i3 >> 7);
            }
            if (i2 == 161) {
                controlChange(71, i3 >> 7);
            }
            if (i2 == 227) {
                controlChange(73, i3 >> 7);
            }
            if (i2 == 228) {
                controlChange(75, i3 >> 7);
            }
            if (i2 == 230) {
                controlChange(72, i3 >> 7);
            }
            if ((i2 >> 7) == 24) {
                controlChangePerNote(i2 % 128, 120, i3 >> 7);
            }
            if ((i2 >> 7) == 26) {
                controlChangePerNote(i2 % 128, 7, i3 >> 7);
            }
            if ((i2 >> 7) == 28) {
                controlChangePerNote(i2 % 128, 10, i3 >> 7);
            }
            if ((i2 >> 7) == 29) {
                controlChangePerNote(i2 % 128, 91, i3 >> 7);
            }
            if ((i2 >> 7) == 30) {
                controlChangePerNote(i2 % 128, 93, i3 >> 7);
            }
        }
        int[] iArr = this.co_midi_nrpn_nrpn_i.get(Integer.valueOf(i2));
        double[] dArr = this.co_midi_nrpn_nrpn.get(Integer.valueOf(i2));
        if (iArr == null) {
            iArr = new int[1];
            this.co_midi_nrpn_nrpn_i.put(Integer.valueOf(i2), iArr);
        }
        if (dArr == null) {
            dArr = new double[1];
            this.co_midi_nrpn_nrpn.put(Integer.valueOf(i2), dArr);
        }
        iArr[0] = i3;
        dArr[0] = iArr[0] * 6.103515625E-5d;
        for (int i4 = 0; i4 < this.voices.length; i4++) {
            if (this.voices[i4].active) {
                this.voices[i4].nrpnChange(i2, iArr[0]);
            }
        }
    }

    public void rpnChange(int i2, int i3) {
        if (i2 == 3) {
            this.tuning_program = (i3 >> 7) & 127;
            tuningChange(this.tuning_bank, this.tuning_program);
        }
        if (i2 == 4) {
            this.tuning_bank = (i3 >> 7) & 127;
        }
        int[] iArr = this.co_midi_rpn_rpn_i.get(Integer.valueOf(i2));
        double[] dArr = this.co_midi_rpn_rpn.get(Integer.valueOf(i2));
        if (iArr == null) {
            iArr = new int[1];
            this.co_midi_rpn_rpn_i.put(Integer.valueOf(i2), iArr);
        }
        if (dArr == null) {
            dArr = new double[1];
            this.co_midi_rpn_rpn.put(Integer.valueOf(i2), dArr);
        }
        iArr[0] = i3;
        dArr[0] = iArr[0] * 6.103515625E-5d;
        for (int i4 = 0; i4 < this.voices.length; i4++) {
            if (this.voices[i4].active) {
                this.voices[i4].rpnChange(i2, iArr[0]);
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public void resetAllControllers() {
        resetAllControllers(false);
    }

    public void resetAllControllers(boolean z2) {
        synchronized (this.control_mutex) {
            this.mainmixer.activity();
            for (int i2 = 0; i2 < 128; i2++) {
                setPolyPressure(i2, 0);
            }
            setChannelPressure(0);
            setPitchBend(8192);
            for (int i3 = 0; i3 < 128; i3++) {
                if (!dontResetControls[i3]) {
                    controlChange(i3, 0);
                }
            }
            controlChange(71, 64);
            controlChange(72, 64);
            controlChange(73, 64);
            controlChange(74, 64);
            controlChange(75, 64);
            controlChange(76, 64);
            controlChange(77, 64);
            controlChange(78, 64);
            controlChange(8, 64);
            controlChange(11, 127);
            controlChange(98, 127);
            controlChange(99, 127);
            controlChange(100, 127);
            controlChange(101, 127);
            if (z2) {
                this.keybasedcontroller_active = (boolean[][]) null;
                this.keybasedcontroller_value = (double[][]) null;
                controlChange(7, 100);
                controlChange(10, 64);
                controlChange(91, 40);
                Iterator<Integer> it = this.co_midi_rpn_rpn.keySet().iterator();
                while (it.hasNext()) {
                    int iIntValue = it.next().intValue();
                    if (iIntValue != 3 && iIntValue != 4) {
                        rpnChange(iIntValue, 0);
                    }
                }
                Iterator<Integer> it2 = this.co_midi_nrpn_nrpn.keySet().iterator();
                while (it2.hasNext()) {
                    nrpnChange(it2.next().intValue(), 0);
                }
                rpnChange(0, 256);
                rpnChange(1, 8192);
                rpnChange(2, 8192);
                rpnChange(5, 64);
                this.tuning_bank = 0;
                this.tuning_program = 0;
                this.tuning = new SoftTuning();
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public void allNotesOff() {
        if (this.current_mixer != null) {
            this.current_mixer.allNotesOff();
        }
        synchronized (this.control_mutex) {
            for (int i2 = 0; i2 < this.voices.length; i2++) {
                if (this.voices[i2].on && this.voices[i2].channel == this.channel && !this.voices[i2].releaseTriggered) {
                    this.voices[i2].noteOff(0);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public void allSoundOff() {
        if (this.current_mixer != null) {
            this.current_mixer.allSoundOff();
        }
        synchronized (this.control_mutex) {
            for (int i2 = 0; i2 < this.voices.length; i2++) {
                if (this.voices[i2].on && this.voices[i2].channel == this.channel) {
                    this.voices[i2].soundOff();
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean localControl(boolean z2) {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public void setMono(boolean z2) {
        if (this.current_mixer != null) {
            this.current_mixer.setMono(z2);
        }
        synchronized (this.control_mutex) {
            allNotesOff();
            this.mono = z2;
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getMono() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.mono;
        }
        return z2;
    }

    @Override // javax.sound.midi.MidiChannel
    public void setOmni(boolean z2) {
        if (this.current_mixer != null) {
            this.current_mixer.setOmni(z2);
        }
        allNotesOff();
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getOmni() {
        return false;
    }

    @Override // javax.sound.midi.MidiChannel
    public void setMute(boolean z2) {
        if (this.current_mixer != null) {
            this.current_mixer.setMute(z2);
        }
        synchronized (this.control_mutex) {
            this.mute = z2;
            for (int i2 = 0; i2 < this.voices.length; i2++) {
                if (this.voices[i2].active && this.voices[i2].channel == this.channel) {
                    this.voices[i2].setMute(z2);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getMute() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.mute;
        }
        return z2;
    }

    @Override // javax.sound.midi.MidiChannel
    public void setSolo(boolean z2) {
        if (this.current_mixer != null) {
            this.current_mixer.setSolo(z2);
        }
        synchronized (this.control_mutex) {
            this.solo = z2;
            boolean z3 = false;
            SoftChannel[] softChannelArr = this.synthesizer.channels;
            int length = softChannelArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                if (!softChannelArr[i2].solo) {
                    i2++;
                } else {
                    z3 = true;
                    break;
                }
            }
            if (!z3) {
                for (SoftChannel softChannel : this.synthesizer.channels) {
                    softChannel.setSoloMute(false);
                }
                return;
            }
            for (SoftChannel softChannel2 : this.synthesizer.channels) {
                softChannel2.setSoloMute(!softChannel2.solo);
            }
        }
    }

    private void setSoloMute(boolean z2) {
        synchronized (this.control_mutex) {
            if (this.solomute == z2) {
                return;
            }
            this.solomute = z2;
            for (int i2 = 0; i2 < this.voices.length; i2++) {
                if (this.voices[i2].active && this.voices[i2].channel == this.channel) {
                    this.voices[i2].setSoloMute(this.solomute);
                }
            }
        }
    }

    @Override // javax.sound.midi.MidiChannel
    public boolean getSolo() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.solo;
        }
        return z2;
    }
}
