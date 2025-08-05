package com.sun.media.sound;

import java.util.Comparator;
import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;

/* loaded from: rt.jar:com/sun/media/sound/ModelInstrumentComparator.class */
public final class ModelInstrumentComparator implements Comparator<Instrument> {
    @Override // java.util.Comparator
    public int compare(Instrument instrument, Instrument instrument2) {
        Patch patch = instrument.getPatch();
        Patch patch2 = instrument2.getPatch();
        int bank = (patch.getBank() * 128) + patch.getProgram();
        int bank2 = (patch2.getBank() * 128) + patch2.getProgram();
        if (patch instanceof ModelPatch) {
            bank += ((ModelPatch) patch).isPercussion() ? 2097152 : 0;
        }
        if (patch2 instanceof ModelPatch) {
            bank2 += ((ModelPatch) patch2).isPercussion() ? 2097152 : 0;
        }
        return bank - bank2;
    }
}
