package com.sun.media.sound;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Patch;

/* loaded from: rt.jar:com/sun/media/sound/SimpleInstrument.class */
public class SimpleInstrument extends ModelInstrument {
    protected int preset;
    protected int bank;
    protected boolean percussion;
    protected String name;
    protected List<SimpleInstrumentPart> parts;

    /* loaded from: rt.jar:com/sun/media/sound/SimpleInstrument$SimpleInstrumentPart.class */
    private static class SimpleInstrumentPart {
        ModelPerformer[] performers;
        int keyFrom;
        int keyTo;
        int velFrom;
        int velTo;
        int exclusiveClass;

        private SimpleInstrumentPart() {
        }
    }

    public SimpleInstrument() {
        super(null, null, null, null);
        this.preset = 0;
        this.bank = 0;
        this.percussion = false;
        this.name = "";
        this.parts = new ArrayList();
    }

    public void clear() {
        this.parts.clear();
    }

    public void add(ModelPerformer[] modelPerformerArr, int i2, int i3, int i4, int i5, int i6) {
        SimpleInstrumentPart simpleInstrumentPart = new SimpleInstrumentPart();
        simpleInstrumentPart.performers = modelPerformerArr;
        simpleInstrumentPart.keyFrom = i2;
        simpleInstrumentPart.keyTo = i3;
        simpleInstrumentPart.velFrom = i4;
        simpleInstrumentPart.velTo = i5;
        simpleInstrumentPart.exclusiveClass = i6;
        this.parts.add(simpleInstrumentPart);
    }

    public void add(ModelPerformer[] modelPerformerArr, int i2, int i3, int i4, int i5) {
        add(modelPerformerArr, i2, i3, i4, i5, -1);
    }

    public void add(ModelPerformer[] modelPerformerArr, int i2, int i3) {
        add(modelPerformerArr, i2, i3, 0, 127, -1);
    }

    public void add(ModelPerformer[] modelPerformerArr) {
        add(modelPerformerArr, 0, 127, 0, 127, -1);
    }

    public void add(ModelPerformer modelPerformer, int i2, int i3, int i4, int i5, int i6) {
        add(new ModelPerformer[]{modelPerformer}, i2, i3, i4, i5, i6);
    }

    public void add(ModelPerformer modelPerformer, int i2, int i3, int i4, int i5) {
        add(new ModelPerformer[]{modelPerformer}, i2, i3, i4, i5);
    }

    public void add(ModelPerformer modelPerformer, int i2, int i3) {
        add(new ModelPerformer[]{modelPerformer}, i2, i3);
    }

    public void add(ModelPerformer modelPerformer) {
        add(new ModelPerformer[]{modelPerformer});
    }

    public void add(ModelInstrument modelInstrument, int i2, int i3, int i4, int i5, int i6) {
        add(modelInstrument.getPerformers(), i2, i3, i4, i5, i6);
    }

    public void add(ModelInstrument modelInstrument, int i2, int i3, int i4, int i5) {
        add(modelInstrument.getPerformers(), i2, i3, i4, i5);
    }

    public void add(ModelInstrument modelInstrument, int i2, int i3) {
        add(modelInstrument.getPerformers(), i2, i3);
    }

    public void add(ModelInstrument modelInstrument) {
        add(modelInstrument.getPerformers());
    }

    @Override // com.sun.media.sound.ModelInstrument
    public ModelPerformer[] getPerformers() {
        int length = 0;
        for (SimpleInstrumentPart simpleInstrumentPart : this.parts) {
            if (simpleInstrumentPart.performers != null) {
                length += simpleInstrumentPart.performers.length;
            }
        }
        ModelPerformer[] modelPerformerArr = new ModelPerformer[length];
        int i2 = 0;
        for (SimpleInstrumentPart simpleInstrumentPart2 : this.parts) {
            if (simpleInstrumentPart2.performers != null) {
                for (ModelPerformer modelPerformer : simpleInstrumentPart2.performers) {
                    ModelPerformer modelPerformer2 = new ModelPerformer();
                    modelPerformer2.setName(getName());
                    int i3 = i2;
                    i2++;
                    modelPerformerArr[i3] = modelPerformer2;
                    modelPerformer2.setDefaultConnectionsEnabled(modelPerformer.isDefaultConnectionsEnabled());
                    modelPerformer2.setKeyFrom(modelPerformer.getKeyFrom());
                    modelPerformer2.setKeyTo(modelPerformer.getKeyTo());
                    modelPerformer2.setVelFrom(modelPerformer.getVelFrom());
                    modelPerformer2.setVelTo(modelPerformer.getVelTo());
                    modelPerformer2.setExclusiveClass(modelPerformer.getExclusiveClass());
                    modelPerformer2.setSelfNonExclusive(modelPerformer.isSelfNonExclusive());
                    modelPerformer2.setReleaseTriggered(modelPerformer.isReleaseTriggered());
                    if (simpleInstrumentPart2.exclusiveClass != -1) {
                        modelPerformer2.setExclusiveClass(simpleInstrumentPart2.exclusiveClass);
                    }
                    if (simpleInstrumentPart2.keyFrom > modelPerformer2.getKeyFrom()) {
                        modelPerformer2.setKeyFrom(simpleInstrumentPart2.keyFrom);
                    }
                    if (simpleInstrumentPart2.keyTo < modelPerformer2.getKeyTo()) {
                        modelPerformer2.setKeyTo(simpleInstrumentPart2.keyTo);
                    }
                    if (simpleInstrumentPart2.velFrom > modelPerformer2.getVelFrom()) {
                        modelPerformer2.setVelFrom(simpleInstrumentPart2.velFrom);
                    }
                    if (simpleInstrumentPart2.velTo < modelPerformer2.getVelTo()) {
                        modelPerformer2.setVelTo(simpleInstrumentPart2.velTo);
                    }
                    modelPerformer2.getOscillators().addAll(modelPerformer.getOscillators());
                    modelPerformer2.getConnectionBlocks().addAll(modelPerformer.getConnectionBlocks());
                }
            }
        }
        return modelPerformerArr;
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        return null;
    }

    @Override // javax.sound.midi.SoundbankResource
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    @Override // javax.sound.midi.Instrument
    public ModelPatch getPatch() {
        return new ModelPatch(this.bank, this.preset, this.percussion);
    }

    public void setPatch(Patch patch) {
        if ((patch instanceof ModelPatch) && ((ModelPatch) patch).isPercussion()) {
            this.percussion = true;
            this.bank = patch.getBank();
            this.preset = patch.getProgram();
        } else {
            this.percussion = false;
            this.bank = patch.getBank();
            this.preset = patch.getProgram();
        }
    }
}
