package com.sun.media.sound;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;

/* loaded from: rt.jar:com/sun/media/sound/SoftInstrument.class */
public final class SoftInstrument extends Instrument {
    private SoftPerformer[] performers;
    private ModelPerformer[] modelperformers;
    private final Object data;
    private final ModelInstrument ins;

    public SoftInstrument(ModelInstrument modelInstrument) {
        super(modelInstrument.getSoundbank(), modelInstrument.getPatch(), modelInstrument.getName(), modelInstrument.getDataClass());
        this.data = modelInstrument.getData();
        this.ins = modelInstrument;
        initPerformers(modelInstrument.getPerformers());
    }

    public SoftInstrument(ModelInstrument modelInstrument, ModelPerformer[] modelPerformerArr) {
        super(modelInstrument.getSoundbank(), modelInstrument.getPatch(), modelInstrument.getName(), modelInstrument.getDataClass());
        this.data = modelInstrument.getData();
        this.ins = modelInstrument;
        initPerformers(modelPerformerArr);
    }

    private void initPerformers(ModelPerformer[] modelPerformerArr) {
        this.modelperformers = modelPerformerArr;
        this.performers = new SoftPerformer[modelPerformerArr.length];
        for (int i2 = 0; i2 < modelPerformerArr.length; i2++) {
            this.performers[i2] = new SoftPerformer(modelPerformerArr[i2]);
        }
    }

    public ModelDirector getDirector(MidiChannel midiChannel, ModelDirectedPlayer modelDirectedPlayer) {
        return this.ins.getDirector(this.modelperformers, midiChannel, modelDirectedPlayer);
    }

    public ModelInstrument getSourceInstrument() {
        return this.ins;
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        return this.data;
    }

    public SoftPerformer getPerformer(int i2) {
        return this.performers[i2];
    }
}
