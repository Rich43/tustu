package com.sun.media.sound;

import java.util.Arrays;

/* loaded from: rt.jar:com/sun/media/sound/ModelStandardDirector.class */
public final class ModelStandardDirector implements ModelDirector {
    private final ModelPerformer[] performers;
    private final ModelDirectedPlayer player;
    private boolean noteOnUsed;
    private boolean noteOffUsed;

    public ModelStandardDirector(ModelPerformer[] modelPerformerArr, ModelDirectedPlayer modelDirectedPlayer) {
        this.noteOnUsed = false;
        this.noteOffUsed = false;
        this.performers = (ModelPerformer[]) Arrays.copyOf(modelPerformerArr, modelPerformerArr.length);
        this.player = modelDirectedPlayer;
        for (ModelPerformer modelPerformer : this.performers) {
            if (modelPerformer.isReleaseTriggered()) {
                this.noteOffUsed = true;
            } else {
                this.noteOnUsed = true;
            }
        }
    }

    @Override // com.sun.media.sound.ModelDirector
    public void close() {
    }

    @Override // com.sun.media.sound.ModelDirector
    public void noteOff(int i2, int i3) {
        if (!this.noteOffUsed) {
            return;
        }
        for (int i4 = 0; i4 < this.performers.length; i4++) {
            ModelPerformer modelPerformer = this.performers[i4];
            if (modelPerformer.getKeyFrom() <= i2 && modelPerformer.getKeyTo() >= i2 && modelPerformer.getVelFrom() <= i3 && modelPerformer.getVelTo() >= i3 && modelPerformer.isReleaseTriggered()) {
                this.player.play(i4, null);
            }
        }
    }

    @Override // com.sun.media.sound.ModelDirector
    public void noteOn(int i2, int i3) {
        if (!this.noteOnUsed) {
            return;
        }
        for (int i4 = 0; i4 < this.performers.length; i4++) {
            ModelPerformer modelPerformer = this.performers[i4];
            if (modelPerformer.getKeyFrom() <= i2 && modelPerformer.getKeyTo() >= i2 && modelPerformer.getVelFrom() <= i3 && modelPerformer.getVelTo() >= i3 && !modelPerformer.isReleaseTriggered()) {
                this.player.play(i4, null);
            }
        }
    }
}
