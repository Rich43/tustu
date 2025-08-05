package com.sun.media.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;

/* loaded from: rt.jar:com/sun/media/sound/SF2Soundbank.class */
public final class SF2Soundbank implements Soundbank {
    int major;
    int minor;
    String targetEngine;
    String name;
    String romName;
    int romVersionMajor;
    int romVersionMinor;
    String creationDate;
    String engineers;
    String product;
    String copyright;
    String comments;
    String tools;
    private ModelByteBuffer sampleData;
    private ModelByteBuffer sampleData24;
    private File sampleFile;
    private boolean largeFormat;
    private final List<SF2Instrument> instruments;
    private final List<SF2Layer> layers;
    private final List<SF2Sample> samples;

    public SF2Soundbank() {
        this.major = 2;
        this.minor = 1;
        this.targetEngine = "EMU8000";
        this.name = "untitled";
        this.romName = null;
        this.romVersionMajor = -1;
        this.romVersionMinor = -1;
        this.creationDate = null;
        this.engineers = null;
        this.product = null;
        this.copyright = null;
        this.comments = null;
        this.tools = null;
        this.sampleData = null;
        this.sampleData24 = null;
        this.sampleFile = null;
        this.largeFormat = false;
        this.instruments = new ArrayList();
        this.layers = new ArrayList();
        this.samples = new ArrayList();
    }

    public SF2Soundbank(URL url) throws IOException {
        this.major = 2;
        this.minor = 1;
        this.targetEngine = "EMU8000";
        this.name = "untitled";
        this.romName = null;
        this.romVersionMajor = -1;
        this.romVersionMinor = -1;
        this.creationDate = null;
        this.engineers = null;
        this.product = null;
        this.copyright = null;
        this.comments = null;
        this.tools = null;
        this.sampleData = null;
        this.sampleData24 = null;
        this.sampleFile = null;
        this.largeFormat = false;
        this.instruments = new ArrayList();
        this.layers = new ArrayList();
        this.samples = new ArrayList();
        InputStream inputStreamOpenStream = url.openStream();
        try {
            readSoundbank(inputStreamOpenStream);
        } finally {
            inputStreamOpenStream.close();
        }
    }

    public SF2Soundbank(File file) throws IOException {
        this.major = 2;
        this.minor = 1;
        this.targetEngine = "EMU8000";
        this.name = "untitled";
        this.romName = null;
        this.romVersionMajor = -1;
        this.romVersionMinor = -1;
        this.creationDate = null;
        this.engineers = null;
        this.product = null;
        this.copyright = null;
        this.comments = null;
        this.tools = null;
        this.sampleData = null;
        this.sampleData24 = null;
        this.sampleFile = null;
        this.largeFormat = false;
        this.instruments = new ArrayList();
        this.layers = new ArrayList();
        this.samples = new ArrayList();
        this.largeFormat = true;
        this.sampleFile = file;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            readSoundbank(fileInputStream);
        } finally {
            fileInputStream.close();
        }
    }

    public SF2Soundbank(InputStream inputStream) throws IOException {
        this.major = 2;
        this.minor = 1;
        this.targetEngine = "EMU8000";
        this.name = "untitled";
        this.romName = null;
        this.romVersionMajor = -1;
        this.romVersionMinor = -1;
        this.creationDate = null;
        this.engineers = null;
        this.product = null;
        this.copyright = null;
        this.comments = null;
        this.tools = null;
        this.sampleData = null;
        this.sampleData24 = null;
        this.sampleFile = null;
        this.largeFormat = false;
        this.instruments = new ArrayList();
        this.layers = new ArrayList();
        this.samples = new ArrayList();
        readSoundbank(inputStream);
    }

    private void readSoundbank(InputStream inputStream) throws IOException {
        RIFFReader rIFFReader = new RIFFReader(inputStream);
        if (!rIFFReader.getFormat().equals("RIFF")) {
            throw new RIFFInvalidFormatException("Input stream is not a valid RIFF stream!");
        }
        if (!rIFFReader.getType().equals("sfbk")) {
            throw new RIFFInvalidFormatException("Input stream is not a valid SoundFont!");
        }
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("LIST")) {
                if (rIFFReaderNextChunk.getType().equals("INFO")) {
                    readInfoChunk(rIFFReaderNextChunk);
                }
                if (rIFFReaderNextChunk.getType().equals("sdta")) {
                    readSdtaChunk(rIFFReaderNextChunk);
                }
                if (rIFFReaderNextChunk.getType().equals("pdta")) {
                    readPdtaChunk(rIFFReaderNextChunk);
                }
            }
        }
    }

    private void readInfoChunk(RIFFReader rIFFReader) throws IOException {
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("ifil")) {
                this.major = rIFFReaderNextChunk.readUnsignedShort();
                this.minor = rIFFReaderNextChunk.readUnsignedShort();
            } else if (format.equals("isng")) {
                this.targetEngine = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("INAM")) {
                this.name = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("irom")) {
                this.romName = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("iver")) {
                this.romVersionMajor = rIFFReaderNextChunk.readUnsignedShort();
                this.romVersionMinor = rIFFReaderNextChunk.readUnsignedShort();
            } else if (format.equals("ICRD")) {
                this.creationDate = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IENG")) {
                this.engineers = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IPRD")) {
                this.product = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICOP")) {
                this.copyright = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMT")) {
                this.comments = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISFT")) {
                this.tools = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            }
        }
    }

    private void readSdtaChunk(RIFFReader rIFFReader) throws IOException {
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("smpl")) {
                if (!this.largeFormat) {
                    byte[] bArr = new byte[rIFFReaderNextChunk.available()];
                    int i2 = 0;
                    int iAvailable = rIFFReaderNextChunk.available();
                    while (i2 != iAvailable) {
                        if (iAvailable - i2 > 65536) {
                            rIFFReaderNextChunk.readFully(bArr, i2, 65536);
                            i2 += 65536;
                        } else {
                            rIFFReaderNextChunk.readFully(bArr, i2, iAvailable - i2);
                            i2 = iAvailable;
                        }
                    }
                    this.sampleData = new ModelByteBuffer(bArr);
                } else {
                    this.sampleData = new ModelByteBuffer(this.sampleFile, rIFFReaderNextChunk.getFilePointer(), rIFFReaderNextChunk.available());
                }
            }
            if (rIFFReaderNextChunk.getFormat().equals("sm24")) {
                if (!this.largeFormat) {
                    byte[] bArr2 = new byte[rIFFReaderNextChunk.available()];
                    int i3 = 0;
                    int iAvailable2 = rIFFReaderNextChunk.available();
                    while (i3 != iAvailable2) {
                        if (iAvailable2 - i3 > 65536) {
                            rIFFReaderNextChunk.readFully(bArr2, i3, 65536);
                            i3 += 65536;
                        } else {
                            rIFFReaderNextChunk.readFully(bArr2, i3, iAvailable2 - i3);
                            i3 = iAvailable2;
                        }
                    }
                    this.sampleData24 = new ModelByteBuffer(bArr2);
                } else {
                    this.sampleData24 = new ModelByteBuffer(this.sampleFile, rIFFReaderNextChunk.getFilePointer(), rIFFReaderNextChunk.available());
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readPdtaChunk(RIFFReader rIFFReader) throws IOException {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("phdr")) {
                if (rIFFReaderNextChunk.available() % 38 != 0) {
                    throw new RIFFInvalidDataException();
                }
                int iAvailable = rIFFReaderNextChunk.available() / 38;
                for (int i2 = 0; i2 < iAvailable; i2++) {
                    SF2Instrument sF2Instrument = new SF2Instrument(this);
                    sF2Instrument.name = rIFFReaderNextChunk.readString(20);
                    sF2Instrument.preset = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Instrument.bank = rIFFReaderNextChunk.readUnsignedShort();
                    arrayList2.add(Integer.valueOf(rIFFReaderNextChunk.readUnsignedShort()));
                    sF2Instrument.library = rIFFReaderNextChunk.readUnsignedInt();
                    sF2Instrument.genre = rIFFReaderNextChunk.readUnsignedInt();
                    sF2Instrument.morphology = rIFFReaderNextChunk.readUnsignedInt();
                    arrayList.add(sF2Instrument);
                    if (i2 != iAvailable - 1) {
                        this.instruments.add(sF2Instrument);
                    }
                }
            } else if (format.equals("pbag")) {
                if (rIFFReaderNextChunk.available() % 4 != 0) {
                    throw new RIFFInvalidDataException();
                }
                int iAvailable2 = rIFFReaderNextChunk.available() / 4;
                int unsignedShort = rIFFReaderNextChunk.readUnsignedShort();
                int unsignedShort2 = rIFFReaderNextChunk.readUnsignedShort();
                while (arrayList3.size() < unsignedShort) {
                    arrayList3.add(null);
                }
                while (arrayList4.size() < unsignedShort2) {
                    arrayList4.add(null);
                }
                int i3 = iAvailable2 - 1;
                if (arrayList2.isEmpty()) {
                    throw new RIFFInvalidDataException();
                }
                int iIntValue = ((Integer) arrayList2.get(0)).intValue();
                for (int i4 = 0; i4 < iIntValue; i4++) {
                    if (i3 == 0) {
                        throw new RIFFInvalidDataException();
                    }
                    int unsignedShort3 = rIFFReaderNextChunk.readUnsignedShort();
                    int unsignedShort4 = rIFFReaderNextChunk.readUnsignedShort();
                    while (arrayList3.size() < unsignedShort3) {
                        arrayList3.add(null);
                    }
                    while (arrayList4.size() < unsignedShort4) {
                        arrayList4.add(null);
                    }
                    i3--;
                }
                for (int i5 = 0; i5 < arrayList2.size() - 1; i5++) {
                    int iIntValue2 = ((Integer) arrayList2.get(i5 + 1)).intValue() - ((Integer) arrayList2.get(i5)).intValue();
                    SF2Instrument sF2Instrument2 = (SF2Instrument) arrayList.get(i5);
                    for (int i6 = 0; i6 < iIntValue2; i6++) {
                        if (i3 == 0) {
                            throw new RIFFInvalidDataException();
                        }
                        int unsignedShort5 = rIFFReaderNextChunk.readUnsignedShort();
                        int unsignedShort6 = rIFFReaderNextChunk.readUnsignedShort();
                        SF2InstrumentRegion sF2InstrumentRegion = new SF2InstrumentRegion();
                        sF2Instrument2.regions.add(sF2InstrumentRegion);
                        while (arrayList3.size() < unsignedShort5) {
                            arrayList3.add(sF2InstrumentRegion);
                        }
                        while (arrayList4.size() < unsignedShort6) {
                            arrayList4.add(sF2InstrumentRegion);
                        }
                        i3--;
                    }
                }
            } else if (format.equals("pmod")) {
                for (int i7 = 0; i7 < arrayList4.size(); i7++) {
                    SF2Modulator sF2Modulator = new SF2Modulator();
                    sF2Modulator.sourceOperator = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Modulator.destinationOperator = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Modulator.amount = rIFFReaderNextChunk.readShort();
                    sF2Modulator.amountSourceOperator = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Modulator.transportOperator = rIFFReaderNextChunk.readUnsignedShort();
                    SF2InstrumentRegion sF2InstrumentRegion2 = (SF2InstrumentRegion) arrayList4.get(i7);
                    if (sF2InstrumentRegion2 != null) {
                        sF2InstrumentRegion2.modulators.add(sF2Modulator);
                    }
                }
            } else if (format.equals("pgen")) {
                for (int i8 = 0; i8 < arrayList3.size(); i8++) {
                    int unsignedShort7 = rIFFReaderNextChunk.readUnsignedShort();
                    short s2 = rIFFReaderNextChunk.readShort();
                    SF2InstrumentRegion sF2InstrumentRegion3 = (SF2InstrumentRegion) arrayList3.get(i8);
                    if (sF2InstrumentRegion3 != null) {
                        sF2InstrumentRegion3.generators.put(Integer.valueOf(unsignedShort7), Short.valueOf(s2));
                    }
                }
            } else if (format.equals("inst")) {
                if (rIFFReaderNextChunk.available() % 22 != 0) {
                    throw new RIFFInvalidDataException();
                }
                int iAvailable3 = rIFFReaderNextChunk.available() / 22;
                for (int i9 = 0; i9 < iAvailable3; i9++) {
                    SF2Layer sF2Layer = new SF2Layer(this);
                    sF2Layer.name = rIFFReaderNextChunk.readString(20);
                    arrayList6.add(Integer.valueOf(rIFFReaderNextChunk.readUnsignedShort()));
                    arrayList5.add(sF2Layer);
                    if (i9 != iAvailable3 - 1) {
                        this.layers.add(sF2Layer);
                    }
                }
            } else if (format.equals("ibag")) {
                if (rIFFReaderNextChunk.available() % 4 != 0) {
                    throw new RIFFInvalidDataException();
                }
                int iAvailable4 = rIFFReaderNextChunk.available() / 4;
                int unsignedShort8 = rIFFReaderNextChunk.readUnsignedShort();
                int unsignedShort9 = rIFFReaderNextChunk.readUnsignedShort();
                while (arrayList7.size() < unsignedShort8) {
                    arrayList7.add(null);
                }
                while (arrayList8.size() < unsignedShort9) {
                    arrayList8.add(null);
                }
                int i10 = iAvailable4 - 1;
                if (arrayList6.isEmpty()) {
                    throw new RIFFInvalidDataException();
                }
                int iIntValue3 = ((Integer) arrayList6.get(0)).intValue();
                for (int i11 = 0; i11 < iIntValue3; i11++) {
                    if (i10 == 0) {
                        throw new RIFFInvalidDataException();
                    }
                    int unsignedShort10 = rIFFReaderNextChunk.readUnsignedShort();
                    int unsignedShort11 = rIFFReaderNextChunk.readUnsignedShort();
                    while (arrayList7.size() < unsignedShort10) {
                        arrayList7.add(null);
                    }
                    while (arrayList8.size() < unsignedShort11) {
                        arrayList8.add(null);
                    }
                    i10--;
                }
                for (int i12 = 0; i12 < arrayList6.size() - 1; i12++) {
                    int iIntValue4 = ((Integer) arrayList6.get(i12 + 1)).intValue() - ((Integer) arrayList6.get(i12)).intValue();
                    SF2Layer sF2Layer2 = this.layers.get(i12);
                    for (int i13 = 0; i13 < iIntValue4; i13++) {
                        if (i10 == 0) {
                            throw new RIFFInvalidDataException();
                        }
                        int unsignedShort12 = rIFFReaderNextChunk.readUnsignedShort();
                        int unsignedShort13 = rIFFReaderNextChunk.readUnsignedShort();
                        SF2LayerRegion sF2LayerRegion = new SF2LayerRegion();
                        sF2Layer2.regions.add(sF2LayerRegion);
                        while (arrayList7.size() < unsignedShort12) {
                            arrayList7.add(sF2LayerRegion);
                        }
                        while (arrayList8.size() < unsignedShort13) {
                            arrayList8.add(sF2LayerRegion);
                        }
                        i10--;
                    }
                }
            } else if (format.equals("imod")) {
                for (int i14 = 0; i14 < arrayList8.size(); i14++) {
                    SF2Modulator sF2Modulator2 = new SF2Modulator();
                    sF2Modulator2.sourceOperator = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Modulator2.destinationOperator = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Modulator2.amount = rIFFReaderNextChunk.readShort();
                    sF2Modulator2.amountSourceOperator = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Modulator2.transportOperator = rIFFReaderNextChunk.readUnsignedShort();
                    if (i14 < 0 || i14 >= arrayList7.size()) {
                        throw new RIFFInvalidDataException();
                    }
                    SF2LayerRegion sF2LayerRegion2 = (SF2LayerRegion) arrayList7.get(i14);
                    if (sF2LayerRegion2 != null) {
                        sF2LayerRegion2.modulators.add(sF2Modulator2);
                    }
                }
            } else if (format.equals("igen")) {
                for (int i15 = 0; i15 < arrayList7.size(); i15++) {
                    int unsignedShort14 = rIFFReaderNextChunk.readUnsignedShort();
                    short s3 = rIFFReaderNextChunk.readShort();
                    SF2LayerRegion sF2LayerRegion3 = (SF2LayerRegion) arrayList7.get(i15);
                    if (sF2LayerRegion3 != null) {
                        sF2LayerRegion3.generators.put(Integer.valueOf(unsignedShort14), Short.valueOf(s3));
                    }
                }
            } else if (!format.equals("shdr")) {
                continue;
            } else {
                if (rIFFReaderNextChunk.available() % 46 != 0) {
                    throw new RIFFInvalidDataException();
                }
                int iAvailable5 = rIFFReaderNextChunk.available() / 46;
                for (int i16 = 0; i16 < iAvailable5; i16++) {
                    SF2Sample sF2Sample = new SF2Sample(this);
                    sF2Sample.name = rIFFReaderNextChunk.readString(20);
                    long unsignedInt = rIFFReaderNextChunk.readUnsignedInt();
                    long unsignedInt2 = rIFFReaderNextChunk.readUnsignedInt();
                    if (this.sampleData != null) {
                        sF2Sample.data = this.sampleData.subbuffer(unsignedInt * 2, unsignedInt2 * 2, true);
                    }
                    if (this.sampleData24 != null) {
                        sF2Sample.data24 = this.sampleData24.subbuffer(unsignedInt, unsignedInt2, true);
                    }
                    sF2Sample.startLoop = rIFFReaderNextChunk.readUnsignedInt() - unsignedInt;
                    sF2Sample.endLoop = rIFFReaderNextChunk.readUnsignedInt() - unsignedInt;
                    if (sF2Sample.startLoop < 0) {
                        sF2Sample.startLoop = -1L;
                    }
                    if (sF2Sample.endLoop < 0) {
                        sF2Sample.endLoop = -1L;
                    }
                    sF2Sample.sampleRate = rIFFReaderNextChunk.readUnsignedInt();
                    sF2Sample.originalPitch = rIFFReaderNextChunk.readUnsignedByte();
                    sF2Sample.pitchCorrection = rIFFReaderNextChunk.readByte();
                    sF2Sample.sampleLink = rIFFReaderNextChunk.readUnsignedShort();
                    sF2Sample.sampleType = rIFFReaderNextChunk.readUnsignedShort();
                    if (i16 != iAvailable5 - 1) {
                        this.samples.add(sF2Sample);
                    }
                }
            }
        }
        for (SF2Layer sF2Layer3 : this.layers) {
            SF2LayerRegion sF2LayerRegion4 = null;
            for (SF2LayerRegion sF2LayerRegion5 : sF2Layer3.regions) {
                if (sF2LayerRegion5.generators.get(53) != null) {
                    short sShortValue = sF2LayerRegion5.generators.get(53).shortValue();
                    sF2LayerRegion5.generators.remove(53);
                    if (sShortValue < 0 || sShortValue >= this.samples.size()) {
                        throw new RIFFInvalidDataException();
                    }
                    sF2LayerRegion5.sample = this.samples.get(sShortValue);
                } else {
                    sF2LayerRegion4 = sF2LayerRegion5;
                }
            }
            if (sF2LayerRegion4 != null) {
                sF2Layer3.getRegions().remove(sF2LayerRegion4);
                SF2GlobalRegion sF2GlobalRegion = new SF2GlobalRegion();
                sF2GlobalRegion.generators = sF2LayerRegion4.generators;
                sF2GlobalRegion.modulators = sF2LayerRegion4.modulators;
                sF2Layer3.setGlobalZone(sF2GlobalRegion);
            }
        }
        for (SF2Instrument sF2Instrument3 : this.instruments) {
            SF2InstrumentRegion sF2InstrumentRegion4 = null;
            for (SF2InstrumentRegion sF2InstrumentRegion5 : sF2Instrument3.regions) {
                if (sF2InstrumentRegion5.generators.get(41) != null) {
                    short sShortValue2 = sF2InstrumentRegion5.generators.get(41).shortValue();
                    sF2InstrumentRegion5.generators.remove(41);
                    if (sShortValue2 < 0 || sShortValue2 >= this.layers.size()) {
                        throw new RIFFInvalidDataException();
                    }
                    sF2InstrumentRegion5.layer = this.layers.get(sShortValue2);
                } else {
                    sF2InstrumentRegion4 = sF2InstrumentRegion5;
                }
            }
            if (sF2InstrumentRegion4 != null) {
                sF2Instrument3.getRegions().remove(sF2InstrumentRegion4);
                SF2GlobalRegion sF2GlobalRegion2 = new SF2GlobalRegion();
                sF2GlobalRegion2.generators = sF2InstrumentRegion4.generators;
                sF2GlobalRegion2.modulators = sF2InstrumentRegion4.modulators;
                sF2Instrument3.setGlobalZone(sF2GlobalRegion2);
            }
        }
    }

    public void save(String str) throws IOException {
        writeSoundbank(new RIFFWriter(str, "sfbk"));
    }

    public void save(File file) throws IOException {
        writeSoundbank(new RIFFWriter(file, "sfbk"));
    }

    public void save(OutputStream outputStream) throws IOException {
        writeSoundbank(new RIFFWriter(outputStream, "sfbk"));
    }

    private void writeSoundbank(RIFFWriter rIFFWriter) throws IOException {
        writeInfo(rIFFWriter.writeList("INFO"));
        writeSdtaChunk(rIFFWriter.writeList("sdta"));
        writePdtaChunk(rIFFWriter.writeList("pdta"));
        rIFFWriter.close();
    }

    private void writeInfoStringChunk(RIFFWriter rIFFWriter, String str, String str2) throws IOException {
        if (str2 == null) {
            return;
        }
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk(str);
        rIFFWriterWriteChunk.writeString(str2);
        int length = str2.getBytes("ascii").length;
        rIFFWriterWriteChunk.write(0);
        if ((length + 1) % 2 != 0) {
            rIFFWriterWriteChunk.write(0);
        }
    }

    private void writeInfo(RIFFWriter rIFFWriter) throws IOException {
        if (this.targetEngine == null) {
            this.targetEngine = "EMU8000";
        }
        if (this.name == null) {
            this.name = "";
        }
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("ifil");
        rIFFWriterWriteChunk.writeUnsignedShort(this.major);
        rIFFWriterWriteChunk.writeUnsignedShort(this.minor);
        writeInfoStringChunk(rIFFWriter, "isng", this.targetEngine);
        writeInfoStringChunk(rIFFWriter, "INAM", this.name);
        writeInfoStringChunk(rIFFWriter, "irom", this.romName);
        if (this.romVersionMajor != -1) {
            RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeChunk("iver");
            rIFFWriterWriteChunk2.writeUnsignedShort(this.romVersionMajor);
            rIFFWriterWriteChunk2.writeUnsignedShort(this.romVersionMinor);
        }
        writeInfoStringChunk(rIFFWriter, "ICRD", this.creationDate);
        writeInfoStringChunk(rIFFWriter, "IENG", this.engineers);
        writeInfoStringChunk(rIFFWriter, "IPRD", this.product);
        writeInfoStringChunk(rIFFWriter, "ICOP", this.copyright);
        writeInfoStringChunk(rIFFWriter, "ICMT", this.comments);
        writeInfoStringChunk(rIFFWriter, "ISFT", this.tools);
        rIFFWriter.close();
    }

    private void writeSdtaChunk(RIFFWriter rIFFWriter) throws IOException {
        byte[] bArr = new byte[32];
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("smpl");
        Iterator<SF2Sample> it = this.samples.iterator();
        while (it.hasNext()) {
            it.next().getDataBuffer().writeTo(rIFFWriterWriteChunk);
            rIFFWriterWriteChunk.write(bArr);
            rIFFWriterWriteChunk.write(bArr);
        }
        if (this.major < 2) {
            return;
        }
        if (this.major == 2 && this.minor < 4) {
            return;
        }
        Iterator<SF2Sample> it2 = this.samples.iterator();
        while (it2.hasNext()) {
            if (it2.next().getData24Buffer() == null) {
                return;
            }
        }
        RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeChunk("sm24");
        Iterator<SF2Sample> it3 = this.samples.iterator();
        while (it3.hasNext()) {
            it3.next().getData24Buffer().writeTo(rIFFWriterWriteChunk2);
            rIFFWriterWriteChunk.write(bArr);
        }
    }

    private void writeModulators(RIFFWriter rIFFWriter, List<SF2Modulator> list) throws IOException {
        for (SF2Modulator sF2Modulator : list) {
            rIFFWriter.writeUnsignedShort(sF2Modulator.sourceOperator);
            rIFFWriter.writeUnsignedShort(sF2Modulator.destinationOperator);
            rIFFWriter.writeShort(sF2Modulator.amount);
            rIFFWriter.writeUnsignedShort(sF2Modulator.amountSourceOperator);
            rIFFWriter.writeUnsignedShort(sF2Modulator.transportOperator);
        }
    }

    private void writeGenerators(RIFFWriter rIFFWriter, Map<Integer, Short> map) throws IOException {
        Short sh = map.get(43);
        Short sh2 = map.get(44);
        if (sh != null) {
            rIFFWriter.writeUnsignedShort(43);
            rIFFWriter.writeShort(sh.shortValue());
        }
        if (sh2 != null) {
            rIFFWriter.writeUnsignedShort(44);
            rIFFWriter.writeShort(sh2.shortValue());
        }
        for (Map.Entry<Integer, Short> entry : map.entrySet()) {
            if (entry.getKey().intValue() != 43 && entry.getKey().intValue() != 44) {
                rIFFWriter.writeUnsignedShort(entry.getKey().intValue());
                rIFFWriter.writeShort(entry.getValue().shortValue());
            }
        }
    }

    private void writePdtaChunk(RIFFWriter rIFFWriter) throws IOException {
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("phdr");
        int size = 0;
        for (SF2Instrument sF2Instrument : this.instruments) {
            rIFFWriterWriteChunk.writeString(sF2Instrument.name, 20);
            rIFFWriterWriteChunk.writeUnsignedShort(sF2Instrument.preset);
            rIFFWriterWriteChunk.writeUnsignedShort(sF2Instrument.bank);
            rIFFWriterWriteChunk.writeUnsignedShort(size);
            if (sF2Instrument.getGlobalRegion() != null) {
                size++;
            }
            size += sF2Instrument.getRegions().size();
            rIFFWriterWriteChunk.writeUnsignedInt(sF2Instrument.library);
            rIFFWriterWriteChunk.writeUnsignedInt(sF2Instrument.genre);
            rIFFWriterWriteChunk.writeUnsignedInt(sF2Instrument.morphology);
        }
        rIFFWriterWriteChunk.writeString("EOP", 20);
        rIFFWriterWriteChunk.writeUnsignedShort(0);
        rIFFWriterWriteChunk.writeUnsignedShort(0);
        rIFFWriterWriteChunk.writeUnsignedShort(size);
        rIFFWriterWriteChunk.writeUnsignedInt(0L);
        rIFFWriterWriteChunk.writeUnsignedInt(0L);
        rIFFWriterWriteChunk.writeUnsignedInt(0L);
        RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeChunk("pbag");
        int size2 = 0;
        int size3 = 0;
        for (SF2Instrument sF2Instrument2 : this.instruments) {
            if (sF2Instrument2.getGlobalRegion() != null) {
                rIFFWriterWriteChunk2.writeUnsignedShort(size2);
                rIFFWriterWriteChunk2.writeUnsignedShort(size3);
                size2 += sF2Instrument2.getGlobalRegion().getGenerators().size();
                size3 += sF2Instrument2.getGlobalRegion().getModulators().size();
            }
            for (SF2InstrumentRegion sF2InstrumentRegion : sF2Instrument2.getRegions()) {
                rIFFWriterWriteChunk2.writeUnsignedShort(size2);
                rIFFWriterWriteChunk2.writeUnsignedShort(size3);
                if (this.layers.indexOf(sF2InstrumentRegion.layer) != -1) {
                    size2++;
                }
                size2 += sF2InstrumentRegion.getGenerators().size();
                size3 += sF2InstrumentRegion.getModulators().size();
            }
        }
        rIFFWriterWriteChunk2.writeUnsignedShort(size2);
        rIFFWriterWriteChunk2.writeUnsignedShort(size3);
        RIFFWriter rIFFWriterWriteChunk3 = rIFFWriter.writeChunk("pmod");
        for (SF2Instrument sF2Instrument3 : this.instruments) {
            if (sF2Instrument3.getGlobalRegion() != null) {
                writeModulators(rIFFWriterWriteChunk3, sF2Instrument3.getGlobalRegion().getModulators());
            }
            Iterator<SF2InstrumentRegion> it = sF2Instrument3.getRegions().iterator();
            while (it.hasNext()) {
                writeModulators(rIFFWriterWriteChunk3, it.next().getModulators());
            }
        }
        rIFFWriterWriteChunk3.write(new byte[10]);
        RIFFWriter rIFFWriterWriteChunk4 = rIFFWriter.writeChunk("pgen");
        for (SF2Instrument sF2Instrument4 : this.instruments) {
            if (sF2Instrument4.getGlobalRegion() != null) {
                writeGenerators(rIFFWriterWriteChunk4, sF2Instrument4.getGlobalRegion().getGenerators());
            }
            for (SF2InstrumentRegion sF2InstrumentRegion2 : sF2Instrument4.getRegions()) {
                writeGenerators(rIFFWriterWriteChunk4, sF2InstrumentRegion2.getGenerators());
                int iIndexOf = this.layers.indexOf(sF2InstrumentRegion2.layer);
                if (iIndexOf != -1) {
                    rIFFWriterWriteChunk4.writeUnsignedShort(41);
                    rIFFWriterWriteChunk4.writeShort((short) iIndexOf);
                }
            }
        }
        rIFFWriterWriteChunk4.write(new byte[4]);
        RIFFWriter rIFFWriterWriteChunk5 = rIFFWriter.writeChunk("inst");
        int size4 = 0;
        for (SF2Layer sF2Layer : this.layers) {
            rIFFWriterWriteChunk5.writeString(sF2Layer.name, 20);
            rIFFWriterWriteChunk5.writeUnsignedShort(size4);
            if (sF2Layer.getGlobalRegion() != null) {
                size4++;
            }
            size4 += sF2Layer.getRegions().size();
        }
        rIFFWriterWriteChunk5.writeString("EOI", 20);
        rIFFWriterWriteChunk5.writeUnsignedShort(size4);
        RIFFWriter rIFFWriterWriteChunk6 = rIFFWriter.writeChunk("ibag");
        int size5 = 0;
        int size6 = 0;
        for (SF2Layer sF2Layer2 : this.layers) {
            if (sF2Layer2.getGlobalRegion() != null) {
                rIFFWriterWriteChunk6.writeUnsignedShort(size5);
                rIFFWriterWriteChunk6.writeUnsignedShort(size6);
                size5 += sF2Layer2.getGlobalRegion().getGenerators().size();
                size6 += sF2Layer2.getGlobalRegion().getModulators().size();
            }
            for (SF2LayerRegion sF2LayerRegion : sF2Layer2.getRegions()) {
                rIFFWriterWriteChunk6.writeUnsignedShort(size5);
                rIFFWriterWriteChunk6.writeUnsignedShort(size6);
                if (this.samples.indexOf(sF2LayerRegion.sample) != -1) {
                    size5++;
                }
                size5 += sF2LayerRegion.getGenerators().size();
                size6 += sF2LayerRegion.getModulators().size();
            }
        }
        rIFFWriterWriteChunk6.writeUnsignedShort(size5);
        rIFFWriterWriteChunk6.writeUnsignedShort(size6);
        RIFFWriter rIFFWriterWriteChunk7 = rIFFWriter.writeChunk("imod");
        for (SF2Layer sF2Layer3 : this.layers) {
            if (sF2Layer3.getGlobalRegion() != null) {
                writeModulators(rIFFWriterWriteChunk7, sF2Layer3.getGlobalRegion().getModulators());
            }
            Iterator<SF2LayerRegion> it2 = sF2Layer3.getRegions().iterator();
            while (it2.hasNext()) {
                writeModulators(rIFFWriterWriteChunk7, it2.next().getModulators());
            }
        }
        rIFFWriterWriteChunk7.write(new byte[10]);
        RIFFWriter rIFFWriterWriteChunk8 = rIFFWriter.writeChunk("igen");
        for (SF2Layer sF2Layer4 : this.layers) {
            if (sF2Layer4.getGlobalRegion() != null) {
                writeGenerators(rIFFWriterWriteChunk8, sF2Layer4.getGlobalRegion().getGenerators());
            }
            for (SF2LayerRegion sF2LayerRegion2 : sF2Layer4.getRegions()) {
                writeGenerators(rIFFWriterWriteChunk8, sF2LayerRegion2.getGenerators());
                int iIndexOf2 = this.samples.indexOf(sF2LayerRegion2.sample);
                if (iIndexOf2 != -1) {
                    rIFFWriterWriteChunk8.writeUnsignedShort(53);
                    rIFFWriterWriteChunk8.writeShort((short) iIndexOf2);
                }
            }
        }
        rIFFWriterWriteChunk8.write(new byte[4]);
        RIFFWriter rIFFWriterWriteChunk9 = rIFFWriter.writeChunk("shdr");
        long j2 = 0;
        for (SF2Sample sF2Sample : this.samples) {
            rIFFWriterWriteChunk9.writeString(sF2Sample.name, 20);
            long j3 = j2;
            long jCapacity = j2 + (sF2Sample.data.capacity() / 2);
            long j4 = sF2Sample.startLoop + j3;
            long j5 = sF2Sample.endLoop + j3;
            if (j4 < j3) {
                j4 = j3;
            }
            if (j5 > jCapacity) {
                j5 = jCapacity;
            }
            rIFFWriterWriteChunk9.writeUnsignedInt(j3);
            rIFFWriterWriteChunk9.writeUnsignedInt(jCapacity);
            rIFFWriterWriteChunk9.writeUnsignedInt(j4);
            rIFFWriterWriteChunk9.writeUnsignedInt(j5);
            rIFFWriterWriteChunk9.writeUnsignedInt(sF2Sample.sampleRate);
            rIFFWriterWriteChunk9.writeUnsignedByte(sF2Sample.originalPitch);
            rIFFWriterWriteChunk9.writeByte(sF2Sample.pitchCorrection);
            rIFFWriterWriteChunk9.writeUnsignedShort(sF2Sample.sampleLink);
            rIFFWriterWriteChunk9.writeUnsignedShort(sF2Sample.sampleType);
            j2 = jCapacity + 32;
        }
        rIFFWriterWriteChunk9.writeString("EOS", 20);
        rIFFWriterWriteChunk9.write(new byte[26]);
    }

    @Override // javax.sound.midi.Soundbank
    public String getName() {
        return this.name;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVersion() {
        return this.major + "." + this.minor;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVendor() {
        return this.engineers;
    }

    @Override // javax.sound.midi.Soundbank
    public String getDescription() {
        return this.comments;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setVendor(String str) {
        this.engineers = str;
    }

    public void setDescription(String str) {
        this.comments = str;
    }

    @Override // javax.sound.midi.Soundbank
    public SoundbankResource[] getResources() {
        SoundbankResource[] soundbankResourceArr = new SoundbankResource[this.layers.size() + this.samples.size()];
        int i2 = 0;
        for (int i3 = 0; i3 < this.layers.size(); i3++) {
            int i4 = i2;
            i2++;
            soundbankResourceArr[i4] = this.layers.get(i3);
        }
        for (int i5 = 0; i5 < this.samples.size(); i5++) {
            int i6 = i2;
            i2++;
            soundbankResourceArr[i6] = this.samples.get(i5);
        }
        return soundbankResourceArr;
    }

    @Override // javax.sound.midi.Soundbank
    public SF2Instrument[] getInstruments() {
        SF2Instrument[] sF2InstrumentArr = (SF2Instrument[]) this.instruments.toArray(new SF2Instrument[this.instruments.size()]);
        Arrays.sort(sF2InstrumentArr, new ModelInstrumentComparator());
        return sF2InstrumentArr;
    }

    public SF2Layer[] getLayers() {
        return (SF2Layer[]) this.layers.toArray(new SF2Layer[this.layers.size()]);
    }

    public SF2Sample[] getSamples() {
        return (SF2Sample[]) this.samples.toArray(new SF2Sample[this.samples.size()]);
    }

    @Override // javax.sound.midi.Soundbank
    public Instrument getInstrument(Patch patch) {
        int program = patch.getProgram();
        int bank = patch.getBank();
        boolean zIsPercussion = false;
        if (patch instanceof ModelPatch) {
            zIsPercussion = ((ModelPatch) patch).isPercussion();
        }
        for (SF2Instrument sF2Instrument : this.instruments) {
            Patch patch2 = sF2Instrument.getPatch();
            int program2 = patch2.getProgram();
            int bank2 = patch2.getBank();
            if (program == program2 && bank == bank2) {
                boolean zIsPercussion2 = false;
                if (patch2 instanceof ModelPatch) {
                    zIsPercussion2 = ((ModelPatch) patch2).isPercussion();
                }
                if (zIsPercussion == zIsPercussion2) {
                    return sF2Instrument;
                }
            }
        }
        return null;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String str) {
        this.creationDate = str;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String str) {
        this.product = str;
    }

    public String getRomName() {
        return this.romName;
    }

    public void setRomName(String str) {
        this.romName = str;
    }

    public int getRomVersionMajor() {
        return this.romVersionMajor;
    }

    public void setRomVersionMajor(int i2) {
        this.romVersionMajor = i2;
    }

    public int getRomVersionMinor() {
        return this.romVersionMinor;
    }

    public void setRomVersionMinor(int i2) {
        this.romVersionMinor = i2;
    }

    public String getTargetEngine() {
        return this.targetEngine;
    }

    public void setTargetEngine(String str) {
        this.targetEngine = str;
    }

    public String getTools() {
        return this.tools;
    }

    public void setTools(String str) {
        this.tools = str;
    }

    public void addResource(SoundbankResource soundbankResource) {
        if (soundbankResource instanceof SF2Instrument) {
            this.instruments.add((SF2Instrument) soundbankResource);
        }
        if (soundbankResource instanceof SF2Layer) {
            this.layers.add((SF2Layer) soundbankResource);
        }
        if (soundbankResource instanceof SF2Sample) {
            this.samples.add((SF2Sample) soundbankResource);
        }
    }

    public void removeResource(SoundbankResource soundbankResource) {
        if (soundbankResource instanceof SF2Instrument) {
            this.instruments.remove((SF2Instrument) soundbankResource);
        }
        if (soundbankResource instanceof SF2Layer) {
            this.layers.remove((SF2Layer) soundbankResource);
        }
        if (soundbankResource instanceof SF2Sample) {
            this.samples.remove((SF2Sample) soundbankResource);
        }
    }

    public void addInstrument(SF2Instrument sF2Instrument) {
        this.instruments.add(sF2Instrument);
    }

    public void removeInstrument(SF2Instrument sF2Instrument) {
        this.instruments.remove(sF2Instrument);
    }
}
