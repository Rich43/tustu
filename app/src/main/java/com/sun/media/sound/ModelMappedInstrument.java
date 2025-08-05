package com.sun.media.sound;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Patch;
import javax.sound.sampled.AudioFormat;

/* loaded from: rt.jar:com/sun/media/sound/ModelMappedInstrument.class */
public final class ModelMappedInstrument extends ModelInstrument {
    private final ModelInstrument ins;

    public ModelMappedInstrument(ModelInstrument modelInstrument, Patch patch) {
        super(modelInstrument.getSoundbank(), patch, modelInstrument.getName(), modelInstrument.getDataClass());
        this.ins = modelInstrument;
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        return this.ins.getData();
    }

    @Override // com.sun.media.sound.ModelInstrument
    public ModelPerformer[] getPerformers() {
        return this.ins.getPerformers();
    }

    @Override // com.sun.media.sound.ModelInstrument
    public ModelDirector getDirector(ModelPerformer[] modelPerformerArr, MidiChannel midiChannel, ModelDirectedPlayer modelDirectedPlayer) {
        return this.ins.getDirector(modelPerformerArr, midiChannel, modelDirectedPlayer);
    }

    @Override // com.sun.media.sound.ModelInstrument
    public ModelChannelMixer getChannelMixer(MidiChannel midiChannel, AudioFormat audioFormat) {
        return this.ins.getChannelMixer(midiChannel, audioFormat);
    }
}
