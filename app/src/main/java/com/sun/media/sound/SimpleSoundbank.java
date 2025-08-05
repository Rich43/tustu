package com.sun.media.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;

/* loaded from: rt.jar:com/sun/media/sound/SimpleSoundbank.class */
public class SimpleSoundbank implements Soundbank {
    String name = "";
    String version = "";
    String vendor = "";
    String description = "";
    List<SoundbankResource> resources = new ArrayList();
    List<Instrument> instruments = new ArrayList();

    @Override // javax.sound.midi.Soundbank
    public String getName() {
        return this.name;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVersion() {
        return this.version;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVendor() {
        return this.vendor;
    }

    @Override // javax.sound.midi.Soundbank
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setVendor(String str) {
        this.vendor = str;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    @Override // javax.sound.midi.Soundbank
    public SoundbankResource[] getResources() {
        return (SoundbankResource[]) this.resources.toArray(new SoundbankResource[this.resources.size()]);
    }

    @Override // javax.sound.midi.Soundbank
    public Instrument[] getInstruments() {
        Instrument[] instrumentArr = (Instrument[]) this.instruments.toArray(new Instrument[this.resources.size()]);
        Arrays.sort(instrumentArr, new ModelInstrumentComparator());
        return instrumentArr;
    }

    @Override // javax.sound.midi.Soundbank
    public Instrument getInstrument(Patch patch) {
        int program = patch.getProgram();
        int bank = patch.getBank();
        boolean zIsPercussion = false;
        if (patch instanceof ModelPatch) {
            zIsPercussion = ((ModelPatch) patch).isPercussion();
        }
        for (Instrument instrument : this.instruments) {
            Patch patch2 = instrument.getPatch();
            int program2 = patch2.getProgram();
            int bank2 = patch2.getBank();
            if (program == program2 && bank == bank2) {
                boolean zIsPercussion2 = false;
                if (patch2 instanceof ModelPatch) {
                    zIsPercussion2 = ((ModelPatch) patch2).isPercussion();
                }
                if (zIsPercussion == zIsPercussion2) {
                    return instrument;
                }
            }
        }
        return null;
    }

    public void addResource(SoundbankResource soundbankResource) {
        if (soundbankResource instanceof Instrument) {
            this.instruments.add((Instrument) soundbankResource);
        } else {
            this.resources.add(soundbankResource);
        }
    }

    public void removeResource(SoundbankResource soundbankResource) {
        if (soundbankResource instanceof Instrument) {
            this.instruments.remove((Instrument) soundbankResource);
        } else {
            this.resources.remove(soundbankResource);
        }
    }

    public void addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
    }

    public void removeInstrument(Instrument instrument) {
        this.instruments.remove(instrument);
    }

    public void addAllInstruments(Soundbank soundbank) {
        for (Instrument instrument : soundbank.getInstruments()) {
            addInstrument(instrument);
        }
    }

    public void removeAllInstruments(Soundbank soundbank) {
        for (Instrument instrument : soundbank.getInstruments()) {
            removeInstrument(instrument);
        }
    }
}
