package com.sun.media.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.midi.Patch;

/* loaded from: rt.jar:com/sun/media/sound/SF2Instrument.class */
public final class SF2Instrument extends ModelInstrument {
    String name;
    int preset;
    int bank;
    long library;
    long genre;
    long morphology;
    SF2GlobalRegion globalregion;
    List<SF2InstrumentRegion> regions;

    public SF2Instrument() {
        super(null, null, null, null);
        this.name = "";
        this.preset = 0;
        this.bank = 0;
        this.library = 0L;
        this.genre = 0L;
        this.morphology = 0L;
        this.globalregion = null;
        this.regions = new ArrayList();
    }

    public SF2Instrument(SF2Soundbank sF2Soundbank) {
        super(sF2Soundbank, null, null, null);
        this.name = "";
        this.preset = 0;
        this.bank = 0;
        this.library = 0L;
        this.genre = 0L;
        this.morphology = 0L;
        this.globalregion = null;
        this.regions = new ArrayList();
    }

    @Override // javax.sound.midi.SoundbankResource
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    @Override // javax.sound.midi.Instrument
    public Patch getPatch() {
        if (this.bank == 128) {
            return new ModelPatch(0, this.preset, true);
        }
        return new ModelPatch(this.bank << 7, this.preset, false);
    }

    public void setPatch(Patch patch) {
        if ((patch instanceof ModelPatch) && ((ModelPatch) patch).isPercussion()) {
            this.bank = 128;
            this.preset = patch.getProgram();
        } else {
            this.bank = patch.getBank() >> 7;
            this.preset = patch.getProgram();
        }
    }

    @Override // javax.sound.midi.SoundbankResource
    public Object getData() {
        return null;
    }

    public long getGenre() {
        return this.genre;
    }

    public void setGenre(long j2) {
        this.genre = j2;
    }

    public long getLibrary() {
        return this.library;
    }

    public void setLibrary(long j2) {
        this.library = j2;
    }

    public long getMorphology() {
        return this.morphology;
    }

    public void setMorphology(long j2) {
        this.morphology = j2;
    }

    public List<SF2InstrumentRegion> getRegions() {
        return this.regions;
    }

    public SF2GlobalRegion getGlobalRegion() {
        return this.globalregion;
    }

    public void setGlobalZone(SF2GlobalRegion sF2GlobalRegion) {
        this.globalregion = sF2GlobalRegion;
    }

    public String toString() {
        if (this.bank == 128) {
            return "Drumkit: " + this.name + " preset #" + this.preset;
        }
        return "Instrument: " + this.name + " bank #" + this.bank + " preset #" + this.preset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v81, types: [int] */
    /* JADX WARN: Type inference failed for: r12v0, types: [com.sun.media.sound.SF2Instrument] */
    @Override // com.sun.media.sound.ModelInstrument
    public ModelPerformer[] getPerformers() {
        short sShortValue;
        int size = 0;
        Iterator<SF2InstrumentRegion> it = this.regions.iterator();
        while (it.hasNext()) {
            size += it.next().getLayer().getRegions().size();
        }
        ModelPerformer[] modelPerformerArr = new ModelPerformer[size];
        int i2 = 0;
        SF2GlobalRegion sF2GlobalRegion = this.globalregion;
        for (SF2InstrumentRegion sF2InstrumentRegion : this.regions) {
            HashMap map = new HashMap();
            map.putAll(sF2InstrumentRegion.getGenerators());
            if (sF2GlobalRegion != null) {
                map.putAll(sF2GlobalRegion.getGenerators());
            }
            SF2Layer layer = sF2InstrumentRegion.getLayer();
            SF2GlobalRegion globalRegion = layer.getGlobalRegion();
            for (SF2LayerRegion sF2LayerRegion : layer.getRegions()) {
                ModelPerformer modelPerformer = new ModelPerformer();
                if (sF2LayerRegion.getSample() != null) {
                    modelPerformer.setName(sF2LayerRegion.getSample().getName());
                } else {
                    modelPerformer.setName(layer.getName());
                }
                int i3 = i2;
                i2++;
                modelPerformerArr[i3] = modelPerformer;
                byte b2 = 0;
                byte b3 = Byte.MAX_VALUE;
                byte b4 = 0;
                byte b5 = Byte.MAX_VALUE;
                if (sF2LayerRegion.contains(57)) {
                    modelPerformer.setExclusiveClass(sF2LayerRegion.getInteger(57));
                }
                if (sF2LayerRegion.contains(43)) {
                    byte[] bytes = sF2LayerRegion.getBytes(43);
                    if (bytes[0] >= 0 && bytes[0] > 0) {
                        b2 = bytes[0];
                    }
                    if (bytes[1] >= 0 && bytes[1] < Byte.MAX_VALUE) {
                        b3 = bytes[1];
                    }
                }
                if (sF2LayerRegion.contains(44)) {
                    byte[] bytes2 = sF2LayerRegion.getBytes(44);
                    if (bytes2[0] >= 0 && bytes2[0] > 0) {
                        b4 = bytes2[0];
                    }
                    if (bytes2[1] >= 0 && bytes2[1] < Byte.MAX_VALUE) {
                        b5 = bytes2[1];
                    }
                }
                if (sF2InstrumentRegion.contains(43)) {
                    byte[] bytes3 = sF2InstrumentRegion.getBytes(43);
                    if (bytes3[0] > b2) {
                        b2 = bytes3[0];
                    }
                    if (bytes3[1] < b3) {
                        b3 = bytes3[1];
                    }
                }
                if (sF2InstrumentRegion.contains(44)) {
                    byte[] bytes4 = sF2InstrumentRegion.getBytes(44);
                    if (bytes4[0] > b4) {
                        b4 = bytes4[0];
                    }
                    if (bytes4[1] < b5) {
                        b5 = bytes4[1];
                    }
                }
                modelPerformer.setKeyFrom(b2);
                modelPerformer.setKeyTo(b3);
                modelPerformer.setVelFrom(b4);
                modelPerformer.setVelTo(b5);
                short s2 = sF2LayerRegion.getShort(0);
                short s3 = sF2LayerRegion.getShort(1);
                short s4 = sF2LayerRegion.getShort(2);
                short s5 = sF2LayerRegion.getShort(3);
                int i4 = s2 + (sF2LayerRegion.getShort(4) * 32768);
                int i5 = s3 + (sF2LayerRegion.getShort(12) * 32768);
                int i6 = s4 + (sF2LayerRegion.getShort(45) * 32768);
                int i7 = s5 + (sF2LayerRegion.getShort(50) * 32768);
                int i8 = i6 - i4;
                int i9 = i7 - i4;
                SF2Sample sample = sF2LayerRegion.getSample();
                short s6 = sample.originalPitch;
                if (sF2LayerRegion.getShort(58) != -1) {
                    s6 = sF2LayerRegion.getShort(58);
                }
                float f2 = ((-s6) * 100) + sample.pitchCorrection;
                ModelByteBuffer dataBuffer = sample.getDataBuffer();
                ModelByteBuffer data24Buffer = sample.getData24Buffer();
                if (i4 != 0 || i5 != 0) {
                    dataBuffer = dataBuffer.subbuffer(i4 * 2, dataBuffer.capacity() + (i5 * 2));
                    if (data24Buffer != null) {
                        data24Buffer = data24Buffer.subbuffer(i4, data24Buffer.capacity() + i5);
                    }
                }
                ModelByteBufferWavetable modelByteBufferWavetable = new ModelByteBufferWavetable(dataBuffer, sample.getFormat(), f2);
                if (data24Buffer != null) {
                    modelByteBufferWavetable.set8BitExtensionBuffer(data24Buffer);
                }
                HashMap map2 = new HashMap();
                if (globalRegion != null) {
                    map2.putAll(globalRegion.getGenerators());
                }
                map2.putAll(sF2LayerRegion.getGenerators());
                for (Map.Entry entry : map.entrySet()) {
                    if (!map2.containsKey(entry.getKey())) {
                        sShortValue = sF2LayerRegion.getShort(((Integer) entry.getKey()).intValue());
                    } else {
                        sShortValue = ((Short) map2.get(entry.getKey())).shortValue();
                    }
                    map2.put(entry.getKey(), Short.valueOf((short) (sShortValue + ((Short) entry.getValue()).shortValue())));
                }
                short generatorValue = getGeneratorValue(map2, 54);
                if ((generatorValue == 1 || generatorValue == 3) && sample.startLoop >= 0 && sample.endLoop > 0) {
                    modelByteBufferWavetable.setLoopStart((int) (sample.startLoop + i8));
                    modelByteBufferWavetable.setLoopLength((int) (((sample.endLoop - sample.startLoop) + i9) - i8));
                    if (generatorValue == 1) {
                        modelByteBufferWavetable.setLoopType(1);
                    }
                    if (generatorValue == 3) {
                        modelByteBufferWavetable.setLoopType(2);
                    }
                }
                modelPerformer.getOscillators().add(modelByteBufferWavetable);
                short generatorValue2 = getGeneratorValue(map2, 33);
                short generatorValue3 = getGeneratorValue(map2, 34);
                short generatorValue4 = getGeneratorValue(map2, 35);
                short generatorValue5 = getGeneratorValue(map2, 36);
                short generatorValue6 = getGeneratorValue(map2, 37);
                short generatorValue7 = getGeneratorValue(map2, 38);
                if (generatorValue4 != -12000) {
                    short generatorValue8 = getGeneratorValue(map2, 39);
                    generatorValue4 = (short) (generatorValue4 + (60 * generatorValue8));
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_KEYNUMBER), (-generatorValue8) * 128, new ModelDestination(ModelDestination.DESTINATION_EG1_HOLD)));
                }
                if (generatorValue5 != -12000) {
                    short generatorValue9 = getGeneratorValue(map2, 40);
                    generatorValue5 = (short) (generatorValue5 + (60 * generatorValue9));
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_KEYNUMBER), (-generatorValue9) * 128, new ModelDestination(ModelDestination.DESTINATION_EG1_DECAY)));
                }
                addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG1_DELAY, generatorValue2);
                addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG1_ATTACK, generatorValue3);
                addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG1_HOLD, generatorValue4);
                addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG1_DECAY, generatorValue5);
                short s7 = (short) (1000 - generatorValue6);
                if (s7 < 0) {
                    s7 = 0;
                }
                if (s7 > 1000) {
                    s7 = 1000;
                }
                addValue(modelPerformer, ModelDestination.DESTINATION_EG1_SUSTAIN, s7);
                addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG1_RELEASE, generatorValue7);
                if (getGeneratorValue(map2, 11) != 0 || getGeneratorValue(map2, 7) != 0) {
                    short generatorValue10 = getGeneratorValue(map2, 25);
                    short generatorValue11 = getGeneratorValue(map2, 26);
                    short generatorValue12 = getGeneratorValue(map2, 27);
                    short generatorValue13 = getGeneratorValue(map2, 28);
                    short generatorValue14 = getGeneratorValue(map2, 29);
                    short generatorValue15 = getGeneratorValue(map2, 30);
                    if (generatorValue12 != -12000) {
                        short generatorValue16 = getGeneratorValue(map2, 31);
                        generatorValue12 = (short) (generatorValue12 + (60 * generatorValue16));
                        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_KEYNUMBER), (-generatorValue16) * 128, new ModelDestination(ModelDestination.DESTINATION_EG2_HOLD)));
                    }
                    if (generatorValue13 != -12000) {
                        short generatorValue17 = getGeneratorValue(map2, 32);
                        generatorValue13 = (short) (generatorValue13 + (60 * generatorValue17));
                        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_KEYNUMBER), (-generatorValue17) * 128, new ModelDestination(ModelDestination.DESTINATION_EG2_DECAY)));
                    }
                    addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG2_DELAY, generatorValue10);
                    addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG2_ATTACK, generatorValue11);
                    addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG2_HOLD, generatorValue12);
                    addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG2_DECAY, generatorValue13);
                    if (generatorValue14 < 0) {
                        generatorValue14 = 0;
                    }
                    if (generatorValue14 > 1000) {
                        generatorValue14 = 1000;
                    }
                    addValue(modelPerformer, ModelDestination.DESTINATION_EG2_SUSTAIN, 1000 - generatorValue14);
                    addTimecentValue(modelPerformer, ModelDestination.DESTINATION_EG2_RELEASE, generatorValue15);
                    if (getGeneratorValue(map2, 11) != 0) {
                        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_EG2), getGeneratorValue(map2, 11), new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ)));
                    }
                    if (getGeneratorValue(map2, 7) != 0) {
                        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_EG2), getGeneratorValue(map2, 7), new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                    }
                }
                if (getGeneratorValue(map2, 10) != 0 || getGeneratorValue(map2, 5) != 0 || getGeneratorValue(map2, 13) != 0) {
                    short generatorValue18 = getGeneratorValue(map2, 22);
                    addTimecentValue(modelPerformer, ModelDestination.DESTINATION_LFO1_DELAY, getGeneratorValue(map2, 21));
                    addValue(modelPerformer, ModelDestination.DESTINATION_LFO1_FREQ, generatorValue18);
                }
                short generatorValue19 = getGeneratorValue(map2, 24);
                addTimecentValue(modelPerformer, ModelDestination.DESTINATION_LFO2_DELAY, getGeneratorValue(map2, 23));
                addValue(modelPerformer, ModelDestination.DESTINATION_LFO2_FREQ, generatorValue19);
                if (getGeneratorValue(map2, 6) != 0) {
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO2, false, true), getGeneratorValue(map2, 6), new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                }
                if (getGeneratorValue(map2, 10) != 0) {
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true), getGeneratorValue(map2, 10), new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ)));
                }
                if (getGeneratorValue(map2, 5) != 0) {
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true), getGeneratorValue(map2, 5), new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                }
                if (getGeneratorValue(map2, 13) != 0) {
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO1, false, true), getGeneratorValue(map2, 13), new ModelDestination(ModelDestination.DESTINATION_GAIN)));
                }
                if (sF2LayerRegion.getShort(46) != -1) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_KEYNUMBER, sF2LayerRegion.getShort(46) / 128.0d);
                }
                if (sF2LayerRegion.getShort(47) != -1) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_VELOCITY, sF2LayerRegion.getShort(47) / 128.0d);
                }
                if (getGeneratorValue(map2, 8) < 13500) {
                    short generatorValue20 = getGeneratorValue(map2, 8);
                    short generatorValue21 = getGeneratorValue(map2, 9);
                    addValue(modelPerformer, ModelDestination.DESTINATION_FILTER_FREQ, generatorValue20);
                    addValue(modelPerformer, ModelDestination.DESTINATION_FILTER_Q, generatorValue21);
                }
                int generatorValue22 = (100 * getGeneratorValue(map2, 51)) + getGeneratorValue(map2, 52);
                if (generatorValue22 != 0) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_PITCH, (short) generatorValue22);
                }
                if (getGeneratorValue(map2, 17) != 0) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_PAN, getGeneratorValue(map2, 17));
                }
                if (getGeneratorValue(map2, 48) != 0) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_GAIN, (-0.376287f) * getGeneratorValue(map2, 48));
                }
                if (getGeneratorValue(map2, 15) != 0) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_CHORUS, getGeneratorValue(map2, 15));
                }
                if (getGeneratorValue(map2, 16) != 0) {
                    addValue(modelPerformer, ModelDestination.DESTINATION_REVERB, getGeneratorValue(map2, 16));
                }
                if (getGeneratorValue(map2, 56) != 100) {
                    if (getGeneratorValue(map2, 56) == 0) {
                        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock((ModelSource) null, s6 * 100, new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                    } else {
                        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock((ModelSource) null, s6 * (100 - r0), new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                    }
                    modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_KEYNUMBER), 128 * r0, new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                }
                modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_NOTEON_VELOCITY, new ModelTransform() { // from class: com.sun.media.sound.SF2Instrument.1
                    @Override // com.sun.media.sound.ModelTransform
                    public double transform(double d2) {
                        if (d2 < 0.5d) {
                            return 1.0d - (d2 * 2.0d);
                        }
                        return 0.0d;
                    }
                }), -2400.0d, new ModelDestination(ModelDestination.DESTINATION_FILTER_FREQ)));
                modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(new ModelSource(ModelSource.SOURCE_LFO2, false, true, 0), new ModelSource(new ModelIdentifier("midi_cc", "1", 0), false, false, 0), 50.0d, new ModelDestination(ModelDestination.DESTINATION_PITCH)));
                if (layer.getGlobalRegion() != null) {
                    Iterator<SF2Modulator> it2 = layer.getGlobalRegion().getModulators().iterator();
                    while (it2.hasNext()) {
                        convertModulator(modelPerformer, it2.next());
                    }
                }
                Iterator<SF2Modulator> it3 = sF2LayerRegion.getModulators().iterator();
                while (it3.hasNext()) {
                    convertModulator(modelPerformer, it3.next());
                }
                if (sF2GlobalRegion != null) {
                    Iterator<SF2Modulator> it4 = sF2GlobalRegion.getModulators().iterator();
                    while (it4.hasNext()) {
                        convertModulator(modelPerformer, it4.next());
                    }
                }
                Iterator<SF2Modulator> it5 = sF2InstrumentRegion.getModulators().iterator();
                while (it5.hasNext()) {
                    convertModulator(modelPerformer, it5.next());
                }
            }
        }
        return modelPerformerArr;
    }

    private void convertModulator(ModelPerformer modelPerformer, SF2Modulator sF2Modulator) {
        ModelSource modelSourceConvertSource = convertSource(sF2Modulator.getSourceOperator());
        ModelSource modelSourceConvertSource2 = convertSource(sF2Modulator.getAmountSourceOperator());
        if (modelSourceConvertSource == null && sF2Modulator.getSourceOperator() != 0) {
            return;
        }
        if (modelSourceConvertSource2 == null && sF2Modulator.getAmountSourceOperator() != 0) {
            return;
        }
        double amount = sF2Modulator.getAmount();
        ModelSource[] modelSourceArr = new ModelSource[1];
        double[] dArr = {1.0d};
        ModelDestination modelDestinationConvertDestination = convertDestination(sF2Modulator.getDestinationOperator(), dArr, modelSourceArr);
        double d2 = amount * dArr[0];
        if (modelDestinationConvertDestination == null) {
            return;
        }
        if (sF2Modulator.getTransportOperator() == 2) {
            ((ModelStandardTransform) modelDestinationConvertDestination.getTransform()).setTransform(4);
        }
        ModelConnectionBlock modelConnectionBlock = new ModelConnectionBlock(modelSourceConvertSource, modelSourceConvertSource2, d2, modelDestinationConvertDestination);
        if (modelSourceArr[0] != null) {
            modelConnectionBlock.addSource(modelSourceArr[0]);
        }
        modelPerformer.getConnectionBlocks().add(modelConnectionBlock);
    }

    private static ModelSource convertSource(int i2) {
        if (i2 == 0) {
            return null;
        }
        ModelIdentifier modelIdentifier = null;
        int i3 = i2 & 127;
        if ((i2 & 128) != 0) {
            modelIdentifier = new ModelIdentifier("midi_cc", Integer.toString(i3));
        } else {
            if (i3 == 2) {
                modelIdentifier = ModelSource.SOURCE_NOTEON_VELOCITY;
            }
            if (i3 == 3) {
                modelIdentifier = ModelSource.SOURCE_NOTEON_KEYNUMBER;
            }
            if (i3 == 10) {
                modelIdentifier = ModelSource.SOURCE_MIDI_POLY_PRESSURE;
            }
            if (i3 == 13) {
                modelIdentifier = ModelSource.SOURCE_MIDI_CHANNEL_PRESSURE;
            }
            if (i3 == 14) {
                modelIdentifier = ModelSource.SOURCE_MIDI_PITCH;
            }
            if (i3 == 16) {
                modelIdentifier = new ModelIdentifier("midi_rpn", "0");
            }
        }
        if (modelIdentifier == null) {
            return null;
        }
        ModelSource modelSource = new ModelSource(modelIdentifier);
        ModelStandardTransform modelStandardTransform = (ModelStandardTransform) modelSource.getTransform();
        if ((256 & i2) != 0) {
            modelStandardTransform.setDirection(true);
        } else {
            modelStandardTransform.setDirection(false);
        }
        if ((512 & i2) != 0) {
            modelStandardTransform.setPolarity(true);
        } else {
            modelStandardTransform.setPolarity(false);
        }
        if ((1024 & i2) != 0) {
            modelStandardTransform.setTransform(1);
        }
        if ((2048 & i2) != 0) {
            modelStandardTransform.setTransform(2);
        }
        if ((3072 & i2) != 0) {
            modelStandardTransform.setTransform(3);
        }
        return modelSource;
    }

    static ModelDestination convertDestination(int i2, double[] dArr, ModelSource[] modelSourceArr) {
        ModelIdentifier modelIdentifier = null;
        switch (i2) {
            case 5:
                modelIdentifier = ModelDestination.DESTINATION_PITCH;
                modelSourceArr[0] = new ModelSource(ModelSource.SOURCE_LFO1, false, true);
                break;
            case 6:
                modelIdentifier = ModelDestination.DESTINATION_PITCH;
                modelSourceArr[0] = new ModelSource(ModelSource.SOURCE_LFO2, false, true);
                break;
            case 7:
                modelIdentifier = ModelDestination.DESTINATION_PITCH;
                modelSourceArr[0] = new ModelSource(ModelSource.SOURCE_EG2, false, true);
                break;
            case 8:
                modelIdentifier = ModelDestination.DESTINATION_FILTER_FREQ;
                break;
            case 9:
                modelIdentifier = ModelDestination.DESTINATION_FILTER_Q;
                break;
            case 10:
                modelIdentifier = ModelDestination.DESTINATION_FILTER_FREQ;
                modelSourceArr[0] = new ModelSource(ModelSource.SOURCE_LFO1, false, true);
                break;
            case 11:
                modelIdentifier = ModelDestination.DESTINATION_FILTER_FREQ;
                modelSourceArr[0] = new ModelSource(ModelSource.SOURCE_EG2, false, true);
                break;
            case 13:
                modelIdentifier = ModelDestination.DESTINATION_GAIN;
                dArr[0] = -0.3762870132923126d;
                modelSourceArr[0] = new ModelSource(ModelSource.SOURCE_LFO1, false, true);
                break;
            case 15:
                modelIdentifier = ModelDestination.DESTINATION_CHORUS;
                break;
            case 16:
                modelIdentifier = ModelDestination.DESTINATION_REVERB;
                break;
            case 17:
                modelIdentifier = ModelDestination.DESTINATION_PAN;
                break;
            case 21:
                modelIdentifier = ModelDestination.DESTINATION_LFO1_DELAY;
                break;
            case 22:
                modelIdentifier = ModelDestination.DESTINATION_LFO1_FREQ;
                break;
            case 23:
                modelIdentifier = ModelDestination.DESTINATION_LFO2_DELAY;
                break;
            case 24:
                modelIdentifier = ModelDestination.DESTINATION_LFO2_FREQ;
                break;
            case 25:
                modelIdentifier = ModelDestination.DESTINATION_EG2_DELAY;
                break;
            case 26:
                modelIdentifier = ModelDestination.DESTINATION_EG2_ATTACK;
                break;
            case 27:
                modelIdentifier = ModelDestination.DESTINATION_EG2_HOLD;
                break;
            case 28:
                modelIdentifier = ModelDestination.DESTINATION_EG2_DECAY;
                break;
            case 29:
                modelIdentifier = ModelDestination.DESTINATION_EG2_SUSTAIN;
                dArr[0] = -1.0d;
                break;
            case 30:
                modelIdentifier = ModelDestination.DESTINATION_EG2_RELEASE;
                break;
            case 33:
                modelIdentifier = ModelDestination.DESTINATION_EG1_DELAY;
                break;
            case 34:
                modelIdentifier = ModelDestination.DESTINATION_EG1_ATTACK;
                break;
            case 35:
                modelIdentifier = ModelDestination.DESTINATION_EG1_HOLD;
                break;
            case 36:
                modelIdentifier = ModelDestination.DESTINATION_EG1_DECAY;
                break;
            case 37:
                modelIdentifier = ModelDestination.DESTINATION_EG1_SUSTAIN;
                dArr[0] = -1.0d;
                break;
            case 38:
                modelIdentifier = ModelDestination.DESTINATION_EG1_RELEASE;
                break;
            case 46:
                modelIdentifier = ModelDestination.DESTINATION_KEYNUMBER;
                break;
            case 47:
                modelIdentifier = ModelDestination.DESTINATION_VELOCITY;
                break;
            case 48:
                modelIdentifier = ModelDestination.DESTINATION_GAIN;
                dArr[0] = -0.3762870132923126d;
                break;
            case 51:
                dArr[0] = 100.0d;
                modelIdentifier = ModelDestination.DESTINATION_PITCH;
                break;
            case 52:
                modelIdentifier = ModelDestination.DESTINATION_PITCH;
                break;
        }
        if (modelIdentifier != null) {
            return new ModelDestination(modelIdentifier);
        }
        return null;
    }

    private void addTimecentValue(ModelPerformer modelPerformer, ModelIdentifier modelIdentifier, short s2) {
        double d2;
        if (s2 == -12000) {
            d2 = Double.NEGATIVE_INFINITY;
        } else {
            d2 = s2;
        }
        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(d2, new ModelDestination(modelIdentifier)));
    }

    private void addValue(ModelPerformer modelPerformer, ModelIdentifier modelIdentifier, short s2) {
        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(s2, new ModelDestination(modelIdentifier)));
    }

    private void addValue(ModelPerformer modelPerformer, ModelIdentifier modelIdentifier, double d2) {
        modelPerformer.getConnectionBlocks().add(new ModelConnectionBlock(d2, new ModelDestination(modelIdentifier)));
    }

    private short getGeneratorValue(Map<Integer, Short> map, int i2) {
        if (map.containsKey(Integer.valueOf(i2))) {
            return map.get(Integer.valueOf(i2)).shortValue();
        }
        return SF2Region.getDefaultValue(i2);
    }
}
