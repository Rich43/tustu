package com.sun.media.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.sound.midi.Patch;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/media/sound/DLSInstrument.class */
public final class DLSInstrument extends ModelInstrument {
    int preset;
    int bank;
    boolean druminstrument;
    byte[] guid;
    DLSInfo info;
    List<DLSRegion> regions;
    List<DLSModulator> modulators;

    public DLSInstrument() {
        super(null, null, null, null);
        this.preset = 0;
        this.bank = 0;
        this.druminstrument = false;
        this.guid = null;
        this.info = new DLSInfo();
        this.regions = new ArrayList();
        this.modulators = new ArrayList();
    }

    public DLSInstrument(DLSSoundbank dLSSoundbank) {
        super(dLSSoundbank, null, null, null);
        this.preset = 0;
        this.bank = 0;
        this.druminstrument = false;
        this.guid = null;
        this.info = new DLSInfo();
        this.regions = new ArrayList();
        this.modulators = new ArrayList();
    }

    public DLSInfo getInfo() {
        return this.info;
    }

    @Override // javax.sound.midi.SoundbankResource
    public String getName() {
        return this.info.name;
    }

    public void setName(String str) {
        this.info.name = str;
    }

    @Override // javax.sound.midi.Instrument
    public ModelPatch getPatch() {
        return new ModelPatch(this.bank, this.preset, this.druminstrument);
    }

    public void setPatch(Patch patch) {
        if ((patch instanceof ModelPatch) && ((ModelPatch) patch).isPercussion()) {
            this.druminstrument = true;
            this.bank = patch.getBank();
            this.preset = patch.getProgram();
        } else {
            this.druminstrument = false;
            this.bank = patch.getBank();
            this.preset = patch.getProgram();
        }
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        return null;
    }

    public List<DLSRegion> getRegions() {
        return this.regions;
    }

    public List<DLSModulator> getModulators() {
        return this.modulators;
    }

    public String toString() {
        if (this.druminstrument) {
            return "Drumkit: " + this.info.name + " bank #" + this.bank + " preset #" + this.preset;
        }
        return "Instrument: " + this.info.name + " bank #" + this.bank + " preset #" + this.preset;
    }

    private ModelIdentifier convertToModelDest(int i2) {
        if (i2 == 0) {
            return null;
        }
        if (i2 == 1) {
            return ModelDestination.DESTINATION_GAIN;
        }
        if (i2 == 3) {
            return ModelDestination.DESTINATION_PITCH;
        }
        if (i2 == 4) {
            return ModelDestination.DESTINATION_PAN;
        }
        if (i2 == 260) {
            return ModelDestination.DESTINATION_LFO1_FREQ;
        }
        if (i2 == 261) {
            return ModelDestination.DESTINATION_LFO1_DELAY;
        }
        if (i2 == 518) {
            return ModelDestination.DESTINATION_EG1_ATTACK;
        }
        if (i2 == 519) {
            return ModelDestination.DESTINATION_EG1_DECAY;
        }
        if (i2 == 521) {
            return ModelDestination.DESTINATION_EG1_RELEASE;
        }
        if (i2 == 522) {
            return ModelDestination.DESTINATION_EG1_SUSTAIN;
        }
        if (i2 == 778) {
            return ModelDestination.DESTINATION_EG2_ATTACK;
        }
        if (i2 == 779) {
            return ModelDestination.DESTINATION_EG2_DECAY;
        }
        if (i2 == 781) {
            return ModelDestination.DESTINATION_EG2_RELEASE;
        }
        if (i2 == 782) {
            return ModelDestination.DESTINATION_EG2_SUSTAIN;
        }
        if (i2 == 5) {
            return ModelDestination.DESTINATION_KEYNUMBER;
        }
        if (i2 == 128) {
            return ModelDestination.DESTINATION_CHORUS;
        }
        if (i2 == 129) {
            return ModelDestination.DESTINATION_REVERB;
        }
        if (i2 == 276) {
            return ModelDestination.DESTINATION_LFO2_FREQ;
        }
        if (i2 == 277) {
            return ModelDestination.DESTINATION_LFO2_DELAY;
        }
        if (i2 == 523) {
            return ModelDestination.DESTINATION_EG1_DELAY;
        }
        if (i2 == 524) {
            return ModelDestination.DESTINATION_EG1_HOLD;
        }
        if (i2 == 525) {
            return ModelDestination.DESTINATION_EG1_SHUTDOWN;
        }
        if (i2 == 783) {
            return ModelDestination.DESTINATION_EG2_DELAY;
        }
        if (i2 == 784) {
            return ModelDestination.DESTINATION_EG2_HOLD;
        }
        if (i2 == 1280) {
            return ModelDestination.DESTINATION_FILTER_FREQ;
        }
        if (i2 == 1281) {
            return ModelDestination.DESTINATION_FILTER_Q;
        }
        return null;
    }

    private ModelIdentifier convertToModelSrc(int i2) {
        if (i2 == 0) {
            return null;
        }
        if (i2 == 1) {
            return ModelSource.SOURCE_LFO1;
        }
        if (i2 == 2) {
            return ModelSource.SOURCE_NOTEON_VELOCITY;
        }
        if (i2 == 3) {
            return ModelSource.SOURCE_NOTEON_KEYNUMBER;
        }
        if (i2 == 4) {
            return ModelSource.SOURCE_EG1;
        }
        if (i2 == 5) {
            return ModelSource.SOURCE_EG2;
        }
        if (i2 == 6) {
            return ModelSource.SOURCE_MIDI_PITCH;
        }
        if (i2 == 129) {
            return new ModelIdentifier("midi_cc", "1", 0);
        }
        if (i2 == 135) {
            return new ModelIdentifier("midi_cc", "7", 0);
        }
        if (i2 == 138) {
            return new ModelIdentifier("midi_cc", "10", 0);
        }
        if (i2 == 139) {
            return new ModelIdentifier("midi_cc", "11", 0);
        }
        if (i2 == 256) {
            return new ModelIdentifier("midi_rpn", "0", 0);
        }
        if (i2 == 257) {
            return new ModelIdentifier("midi_rpn", "1", 0);
        }
        if (i2 == 7) {
            return ModelSource.SOURCE_MIDI_POLY_PRESSURE;
        }
        if (i2 == 8) {
            return ModelSource.SOURCE_MIDI_CHANNEL_PRESSURE;
        }
        if (i2 == 9) {
            return ModelSource.SOURCE_LFO2;
        }
        if (i2 == 10) {
            return ModelSource.SOURCE_MIDI_CHANNEL_PRESSURE;
        }
        if (i2 == 219) {
            return new ModelIdentifier("midi_cc", "91", 0);
        }
        if (i2 == 221) {
            return new ModelIdentifier("midi_cc", "93", 0);
        }
        return null;
    }

    private ModelConnectionBlock convertToModel(DLSModulator dLSModulator) {
        double d2;
        ModelIdentifier modelIdentifierConvertToModelSrc = convertToModelSrc(dLSModulator.getSource());
        ModelIdentifier modelIdentifierConvertToModelSrc2 = convertToModelSrc(dLSModulator.getControl());
        ModelIdentifier modelIdentifierConvertToModelDest = convertToModelDest(dLSModulator.getDestination());
        int scale = dLSModulator.getScale();
        if (scale == Integer.MIN_VALUE) {
            d2 = Double.NEGATIVE_INFINITY;
        } else {
            d2 = scale / 65536.0d;
        }
        if (modelIdentifierConvertToModelDest != null) {
            ModelSource modelSource = null;
            ModelSource modelSource2 = null;
            ModelConnectionBlock modelConnectionBlock = new ModelConnectionBlock();
            if (modelIdentifierConvertToModelSrc2 != null) {
                ModelSource modelSource3 = new ModelSource();
                if (modelIdentifierConvertToModelSrc2 == ModelSource.SOURCE_MIDI_PITCH || modelIdentifierConvertToModelSrc2 == ModelSource.SOURCE_LFO1 || modelIdentifierConvertToModelSrc2 == ModelSource.SOURCE_LFO2) {
                    ((ModelStandardTransform) modelSource3.getTransform()).setPolarity(true);
                }
                modelSource3.setIdentifier(modelIdentifierConvertToModelSrc2);
                modelConnectionBlock.addSource(modelSource3);
                modelSource2 = modelSource3;
            }
            if (modelIdentifierConvertToModelSrc != null) {
                ModelSource modelSource4 = new ModelSource();
                if (modelIdentifierConvertToModelSrc == ModelSource.SOURCE_MIDI_PITCH || modelIdentifierConvertToModelSrc == ModelSource.SOURCE_LFO1 || modelIdentifierConvertToModelSrc == ModelSource.SOURCE_LFO2) {
                    ((ModelStandardTransform) modelSource4.getTransform()).setPolarity(true);
                }
                modelSource4.setIdentifier(modelIdentifierConvertToModelSrc);
                modelConnectionBlock.addSource(modelSource4);
                modelSource = modelSource4;
            }
            ModelDestination modelDestination = new ModelDestination();
            modelDestination.setIdentifier(modelIdentifierConvertToModelDest);
            modelConnectionBlock.setDestination(modelDestination);
            if (dLSModulator.getVersion() == 1) {
                if (dLSModulator.getTransform() == 1) {
                    if (modelSource != null) {
                        ((ModelStandardTransform) modelSource.getTransform()).setTransform(1);
                        ((ModelStandardTransform) modelSource.getTransform()).setDirection(true);
                    }
                    if (modelSource2 != null) {
                        ((ModelStandardTransform) modelSource2.getTransform()).setTransform(1);
                        ((ModelStandardTransform) modelSource2.getTransform()).setDirection(true);
                    }
                }
            } else if (dLSModulator.getVersion() == 2) {
                int transform = dLSModulator.getTransform();
                int i2 = (transform >> 15) & 1;
                int i3 = (transform >> 14) & 1;
                int i4 = (transform >> 10) & 8;
                int i5 = (transform >> 9) & 1;
                int i6 = (transform >> 8) & 1;
                int i7 = (transform >> 4) & 8;
                if (modelSource != null) {
                    int i8 = 0;
                    if (i4 == 3) {
                        i8 = 3;
                    }
                    if (i4 == 1) {
                        i8 = 1;
                    }
                    if (i4 == 2) {
                        i8 = 2;
                    }
                    ((ModelStandardTransform) modelSource.getTransform()).setTransform(i8);
                    ((ModelStandardTransform) modelSource.getTransform()).setPolarity(i3 == 1);
                    ((ModelStandardTransform) modelSource.getTransform()).setDirection(i2 == 1);
                }
                if (modelSource2 != null) {
                    int i9 = 0;
                    if (i7 == 3) {
                        i9 = 3;
                    }
                    if (i7 == 1) {
                        i9 = 1;
                    }
                    if (i7 == 2) {
                        i9 = 2;
                    }
                    ((ModelStandardTransform) modelSource2.getTransform()).setTransform(i9);
                    ((ModelStandardTransform) modelSource2.getTransform()).setPolarity(i6 == 1);
                    ((ModelStandardTransform) modelSource2.getTransform()).setDirection(i5 == 1);
                }
            }
            modelConnectionBlock.setScale(d2);
            return modelConnectionBlock;
        }
        return null;
    }

    @Override // com.sun.media.sound.ModelInstrument
    public ModelPerformer[] getPerformers() {
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        for (DLSModulator dLSModulator : getModulators()) {
            map.put(dLSModulator.getSource() + LanguageTag.PRIVATEUSE + dLSModulator.getControl() + "=" + dLSModulator.getDestination(), dLSModulator);
        }
        HashMap map2 = new HashMap();
        for (DLSRegion dLSRegion : this.regions) {
            ModelPerformer modelPerformer = new ModelPerformer();
            modelPerformer.setName(dLSRegion.getSample().getName());
            modelPerformer.setSelfNonExclusive((dLSRegion.getFusoptions() & 1) != 0);
            modelPerformer.setExclusiveClass(dLSRegion.getExclusiveClass());
            modelPerformer.setKeyFrom(dLSRegion.getKeyfrom());
            modelPerformer.setKeyTo(dLSRegion.getKeyto());
            modelPerformer.setVelFrom(dLSRegion.getVelfrom());
            modelPerformer.setVelTo(dLSRegion.getVelto());
            map2.clear();
            map2.putAll(map);
            for (DLSModulator dLSModulator2 : dLSRegion.getModulators()) {
                map2.put(dLSModulator2.getSource() + LanguageTag.PRIVATEUSE + dLSModulator2.getControl() + "=" + dLSModulator2.getDestination(), dLSModulator2);
            }
            List<ModelConnectionBlock> connectionBlocks = modelPerformer.getConnectionBlocks();
            Iterator it = map2.values().iterator();
            while (it.hasNext()) {
                ModelConnectionBlock modelConnectionBlockConvertToModel = convertToModel((DLSModulator) it.next());
                if (modelConnectionBlockConvertToModel != null) {
                    connectionBlocks.add(modelConnectionBlockConvertToModel);
                }
            }
            DLSSample sample = dLSRegion.getSample();
            DLSSampleOptions sampleoptions = dLSRegion.getSampleoptions();
            if (sampleoptions == null) {
                sampleoptions = sample.getSampleoptions();
            }
            ModelByteBufferWavetable modelByteBufferWavetable = new ModelByteBufferWavetable(sample.getDataBuffer(), sample.getFormat(), ((-sampleoptions.unitynote) * 100) + sampleoptions.finetune);
            modelByteBufferWavetable.setAttenuation(modelByteBufferWavetable.getAttenuation() / 65536.0f);
            if (sampleoptions.getLoops().size() != 0) {
                DLSSampleLoop dLSSampleLoop = sampleoptions.getLoops().get(0);
                modelByteBufferWavetable.setLoopStart((int) dLSSampleLoop.getStart());
                modelByteBufferWavetable.setLoopLength((int) dLSSampleLoop.getLength());
                if (dLSSampleLoop.getType() == 0) {
                    modelByteBufferWavetable.setLoopType(1);
                }
                if (dLSSampleLoop.getType() == 1) {
                    modelByteBufferWavetable.setLoopType(2);
                } else {
                    modelByteBufferWavetable.setLoopType(1);
                }
            }
            modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(1.0d, new ModelDestination(new ModelIdentifier("filter", "type", 1))));
            modelPerformer.getOscillators().add(modelByteBufferWavetable);
            arrayList.add(modelPerformer);
        }
        return (ModelPerformer[]) arrayList.toArray(new ModelPerformer[arrayList.size()]);
    }

    public byte[] getGuid() {
        if (this.guid == null) {
            return null;
        }
        return Arrays.copyOf(this.guid, this.guid.length);
    }

    public void setGuid(byte[] bArr) {
        this.guid = bArr == null ? null : Arrays.copyOf(bArr, bArr.length);
    }
}
