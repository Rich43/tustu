package com.sun.media.sound;

import com.sun.javafx.scene.text.TextLayout;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.sampled.AudioFormat;

/* loaded from: rt.jar:com/sun/media/sound/ModelInstrument.class */
public abstract class ModelInstrument extends Instrument {
    protected ModelInstrument(Soundbank soundbank, Patch patch, String str, Class<?> cls) {
        super(soundbank, patch, str, cls);
    }

    public ModelDirector getDirector(ModelPerformer[] modelPerformerArr, MidiChannel midiChannel, ModelDirectedPlayer modelDirectedPlayer) {
        return new ModelStandardIndexedDirector(modelPerformerArr, modelDirectedPlayer);
    }

    public ModelPerformer[] getPerformers() {
        return new ModelPerformer[0];
    }

    public ModelChannelMixer getChannelMixer(MidiChannel midiChannel, AudioFormat audioFormat) {
        return null;
    }

    public final Patch getPatchAlias() {
        Patch patch = getPatch();
        int program = patch.getProgram();
        if (patch.getBank() != 0) {
            return patch;
        }
        boolean zIsPercussion = false;
        if (getPatch() instanceof ModelPatch) {
            zIsPercussion = ((ModelPatch) getPatch()).isPercussion();
        }
        if (zIsPercussion) {
            return new Patch(TextLayout.DIRECTION_MASK, program);
        }
        return new Patch(15488, program);
    }

    public final String[] getKeys() {
        String[] strArr = new String[128];
        for (ModelPerformer modelPerformer : getPerformers()) {
            for (int keyFrom = modelPerformer.getKeyFrom(); keyFrom <= modelPerformer.getKeyTo(); keyFrom++) {
                if (keyFrom >= 0 && keyFrom < 128 && strArr[keyFrom] == null) {
                    String name = modelPerformer.getName();
                    if (name == null) {
                        name = "untitled";
                    }
                    strArr[keyFrom] = name;
                }
            }
        }
        return strArr;
    }

    public final boolean[] getChannels() {
        boolean zIsPercussion = false;
        if (getPatch() instanceof ModelPatch) {
            zIsPercussion = ((ModelPatch) getPatch()).isPercussion();
        }
        if (zIsPercussion) {
            boolean[] zArr = new boolean[16];
            for (int i2 = 0; i2 < zArr.length; i2++) {
                zArr[i2] = false;
            }
            zArr[9] = true;
            return zArr;
        }
        int bank = getPatch().getBank();
        if ((bank >> 7) == 120 || (bank >> 7) == 121) {
            boolean[] zArr2 = new boolean[16];
            for (int i3 = 0; i3 < zArr2.length; i3++) {
                zArr2[i3] = true;
            }
            return zArr2;
        }
        boolean[] zArr3 = new boolean[16];
        for (int i4 = 0; i4 < zArr3.length; i4++) {
            zArr3[i4] = true;
        }
        zArr3[9] = false;
        return zArr3;
    }
}
