package com.sun.media.sound;

import java.io.IOException;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;
import javax.sound.midi.VoiceStatus;

/* loaded from: rt.jar:com/sun/media/sound/ModelAbstractOscillator.class */
public abstract class ModelAbstractOscillator implements ModelOscillator, ModelOscillatorStream, Soundbank {
    protected float samplerate;
    protected MidiChannel channel;
    protected VoiceStatus voice;
    protected int noteNumber;
    protected int velocity;
    protected float pitch = 6000.0f;
    protected boolean on = false;

    public void init() {
    }

    @Override // com.sun.media.sound.ModelOscillatorStream
    public void close() throws IOException {
    }

    @Override // com.sun.media.sound.ModelOscillatorStream
    public void noteOff(int i2) {
        this.on = false;
    }

    @Override // com.sun.media.sound.ModelOscillatorStream
    public void noteOn(MidiChannel midiChannel, VoiceStatus voiceStatus, int i2, int i3) {
        this.channel = midiChannel;
        this.voice = voiceStatus;
        this.noteNumber = i2;
        this.velocity = i3;
        this.on = true;
    }

    @Override // com.sun.media.sound.ModelOscillatorStream
    public int read(float[][] fArr, int i2, int i3) throws IOException {
        return -1;
    }

    public MidiChannel getChannel() {
        return this.channel;
    }

    public VoiceStatus getVoice() {
        return this.voice;
    }

    public int getNoteNumber() {
        return this.noteNumber;
    }

    public int getVelocity() {
        return this.velocity;
    }

    public boolean isOn() {
        return this.on;
    }

    @Override // com.sun.media.sound.ModelOscillatorStream
    public void setPitch(float f2) {
        this.pitch = f2;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setSampleRate(float f2) {
        this.samplerate = f2;
    }

    public float getSampleRate() {
        return this.samplerate;
    }

    @Override // com.sun.media.sound.ModelOscillator
    public float getAttenuation() {
        return 0.0f;
    }

    @Override // com.sun.media.sound.ModelOscillator
    public int getChannels() {
        return 1;
    }

    @Override // javax.sound.midi.Soundbank
    public String getName() {
        return getClass().getName();
    }

    public Patch getPatch() {
        return new Patch(0, 0);
    }

    @Override // com.sun.media.sound.ModelOscillator
    public ModelOscillatorStream open(float f2) {
        try {
            ModelAbstractOscillator modelAbstractOscillator = (ModelAbstractOscillator) getClass().newInstance();
            modelAbstractOscillator.setSampleRate(f2);
            modelAbstractOscillator.init();
            return modelAbstractOscillator;
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException(e2);
        } catch (InstantiationException e3) {
            throw new IllegalArgumentException(e3);
        }
    }

    public ModelPerformer getPerformer() {
        ModelPerformer modelPerformer = new ModelPerformer();
        modelPerformer.getOscillators().add(this);
        return modelPerformer;
    }

    public ModelInstrument getInstrument() {
        SimpleInstrument simpleInstrument = new SimpleInstrument();
        simpleInstrument.setName(getName());
        simpleInstrument.add(getPerformer());
        simpleInstrument.setPatch(getPatch());
        return simpleInstrument;
    }

    public Soundbank getSoundBank() {
        SimpleSoundbank simpleSoundbank = new SimpleSoundbank();
        simpleSoundbank.addInstrument(getInstrument());
        return simpleSoundbank;
    }

    @Override // javax.sound.midi.Soundbank
    public String getDescription() {
        return getName();
    }

    @Override // javax.sound.midi.Soundbank
    public Instrument getInstrument(Patch patch) {
        ModelInstrument instrument = getInstrument();
        Patch patch2 = instrument.getPatch();
        if (patch2.getBank() != patch.getBank() || patch2.getProgram() != patch.getProgram()) {
            return null;
        }
        if ((patch2 instanceof ModelPatch) && (patch instanceof ModelPatch) && ((ModelPatch) patch2).isPercussion() != ((ModelPatch) patch).isPercussion()) {
            return null;
        }
        return instrument;
    }

    @Override // javax.sound.midi.Soundbank
    public Instrument[] getInstruments() {
        return new Instrument[]{getInstrument()};
    }

    @Override // javax.sound.midi.Soundbank
    public SoundbankResource[] getResources() {
        return new SoundbankResource[0];
    }

    @Override // javax.sound.midi.Soundbank
    public String getVendor() {
        return null;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVersion() {
        return null;
    }
}
