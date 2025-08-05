package com.sun.media.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.sound.midi.Instrument;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/* loaded from: rt.jar:com/sun/media/sound/DLSSoundbank.class */
public final class DLSSoundbank implements Soundbank {
    private static final int DLS_CDL_AND = 1;
    private static final int DLS_CDL_OR = 2;
    private static final int DLS_CDL_XOR = 3;
    private static final int DLS_CDL_ADD = 4;
    private static final int DLS_CDL_SUBTRACT = 5;
    private static final int DLS_CDL_MULTIPLY = 6;
    private static final int DLS_CDL_DIVIDE = 7;
    private static final int DLS_CDL_LOGICAL_AND = 8;
    private static final int DLS_CDL_LOGICAL_OR = 9;
    private static final int DLS_CDL_LT = 10;
    private static final int DLS_CDL_LE = 11;
    private static final int DLS_CDL_GT = 12;
    private static final int DLS_CDL_GE = 13;
    private static final int DLS_CDL_EQ = 14;
    private static final int DLS_CDL_NOT = 15;
    private static final int DLS_CDL_CONST = 16;
    private static final int DLS_CDL_QUERY = 17;
    private static final int DLS_CDL_QUERYSUPPORTED = 18;
    private static final DLSID DLSID_GMInHardware = new DLSID(395259684, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
    private static final DLSID DLSID_GSInHardware = new DLSID(395259685, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
    private static final DLSID DLSID_XGInHardware = new DLSID(395259686, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
    private static final DLSID DLSID_SupportsDLS1 = new DLSID(395259687, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
    private static final DLSID DLSID_SupportsDLS2 = new DLSID(-247096859, 18057, 4562, 175, 166, 0, 170, 0, 36, 216, 182);
    private static final DLSID DLSID_SampleMemorySize = new DLSID(395259688, 50020, 4561, 167, 96, 0, 0, 248, 117, 172, 18);
    private static final DLSID DLSID_ManufacturersID = new DLSID(-1338109567, 32917, 4562, 161, 239, 0, 96, 8, 51, 219, 216);
    private static final DLSID DLSID_ProductID = new DLSID(-1338109566, 32917, 4562, 161, 239, 0, 96, 8, 51, 219, 216);
    private static final DLSID DLSID_SamplePlaybackRate = new DLSID(714209043, 42175, 4562, 187, 223, 0, 96, 8, 51, 219, 216);
    private long major;
    private long minor;
    private final DLSInfo info;
    private final List<DLSInstrument> instruments;
    private final List<DLSSample> samples;
    private boolean largeFormat;
    private File sampleFile;
    private Map<DLSRegion, Long> temp_rgnassign;

    /* loaded from: rt.jar:com/sun/media/sound/DLSSoundbank$DLSID.class */
    private static class DLSID {
        long i1;
        int s1;
        int s2;
        int x1;
        int x2;
        int x3;
        int x4;
        int x5;
        int x6;
        int x7;
        int x8;

        private DLSID() {
        }

        DLSID(long j2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
            this.i1 = j2;
            this.s1 = i2;
            this.s2 = i3;
            this.x1 = i4;
            this.x2 = i5;
            this.x3 = i6;
            this.x4 = i7;
            this.x5 = i8;
            this.x6 = i9;
            this.x7 = i10;
            this.x8 = i11;
        }

        public static DLSID read(RIFFReader rIFFReader) throws IOException {
            DLSID dlsid = new DLSID();
            dlsid.i1 = rIFFReader.readUnsignedInt();
            dlsid.s1 = rIFFReader.readUnsignedShort();
            dlsid.s2 = rIFFReader.readUnsignedShort();
            dlsid.x1 = rIFFReader.readUnsignedByte();
            dlsid.x2 = rIFFReader.readUnsignedByte();
            dlsid.x3 = rIFFReader.readUnsignedByte();
            dlsid.x4 = rIFFReader.readUnsignedByte();
            dlsid.x5 = rIFFReader.readUnsignedByte();
            dlsid.x6 = rIFFReader.readUnsignedByte();
            dlsid.x7 = rIFFReader.readUnsignedByte();
            dlsid.x8 = rIFFReader.readUnsignedByte();
            return dlsid;
        }

        public int hashCode() {
            return (int) this.i1;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof DLSID)) {
                return false;
            }
            DLSID dlsid = (DLSID) obj;
            return this.i1 == dlsid.i1 && this.s1 == dlsid.s1 && this.s2 == dlsid.s2 && this.x1 == dlsid.x1 && this.x2 == dlsid.x2 && this.x3 == dlsid.x3 && this.x4 == dlsid.x4 && this.x5 == dlsid.x5 && this.x6 == dlsid.x6 && this.x7 == dlsid.x7 && this.x8 == dlsid.x8;
        }
    }

    public DLSSoundbank() {
        this.major = -1L;
        this.minor = -1L;
        this.info = new DLSInfo();
        this.instruments = new ArrayList();
        this.samples = new ArrayList();
        this.largeFormat = false;
        this.temp_rgnassign = new HashMap();
    }

    public DLSSoundbank(URL url) throws IOException {
        this.major = -1L;
        this.minor = -1L;
        this.info = new DLSInfo();
        this.instruments = new ArrayList();
        this.samples = new ArrayList();
        this.largeFormat = false;
        this.temp_rgnassign = new HashMap();
        InputStream inputStreamOpenStream = url.openStream();
        try {
            readSoundbank(inputStreamOpenStream);
        } finally {
            inputStreamOpenStream.close();
        }
    }

    public DLSSoundbank(File file) throws IOException {
        this.major = -1L;
        this.minor = -1L;
        this.info = new DLSInfo();
        this.instruments = new ArrayList();
        this.samples = new ArrayList();
        this.largeFormat = false;
        this.temp_rgnassign = new HashMap();
        this.largeFormat = true;
        this.sampleFile = file;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            readSoundbank(fileInputStream);
        } finally {
            fileInputStream.close();
        }
    }

    public DLSSoundbank(InputStream inputStream) throws IOException {
        this.major = -1L;
        this.minor = -1L;
        this.info = new DLSInfo();
        this.instruments = new ArrayList();
        this.samples = new ArrayList();
        this.largeFormat = false;
        this.temp_rgnassign = new HashMap();
        readSoundbank(inputStream);
    }

    private void readSoundbank(InputStream inputStream) throws IOException {
        RIFFReader rIFFReader = new RIFFReader(inputStream);
        if (!rIFFReader.getFormat().equals("RIFF")) {
            throw new RIFFInvalidFormatException("Input stream is not a valid RIFF stream!");
        }
        if (!rIFFReader.getType().equals("DLS ")) {
            throw new RIFFInvalidFormatException("Input stream is not a valid DLS soundbank!");
        }
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("LIST")) {
                if (rIFFReaderNextChunk.getType().equals("INFO")) {
                    readInfoChunk(rIFFReaderNextChunk);
                }
                if (rIFFReaderNextChunk.getType().equals("lins")) {
                    readLinsChunk(rIFFReaderNextChunk);
                }
                if (rIFFReaderNextChunk.getType().equals("wvpl")) {
                    readWvplChunk(rIFFReaderNextChunk);
                }
            } else {
                if (rIFFReaderNextChunk.getFormat().equals("cdl ") && !readCdlChunk(rIFFReaderNextChunk)) {
                    throw new RIFFInvalidFormatException("DLS file isn't supported!");
                }
                if (rIFFReaderNextChunk.getFormat().equals("colh")) {
                }
                if (rIFFReaderNextChunk.getFormat().equals("ptbl")) {
                }
                if (rIFFReaderNextChunk.getFormat().equals("vers")) {
                    this.major = rIFFReaderNextChunk.readUnsignedInt();
                    this.minor = rIFFReaderNextChunk.readUnsignedInt();
                }
            }
        }
        for (Map.Entry<DLSRegion, Long> entry : this.temp_rgnassign.entrySet()) {
            entry.getKey().sample = this.samples.get((int) entry.getValue().longValue());
        }
        this.temp_rgnassign = null;
    }

    private boolean cdlIsQuerySupported(DLSID dlsid) {
        return dlsid.equals(DLSID_GMInHardware) || dlsid.equals(DLSID_GSInHardware) || dlsid.equals(DLSID_XGInHardware) || dlsid.equals(DLSID_SupportsDLS1) || dlsid.equals(DLSID_SupportsDLS2) || dlsid.equals(DLSID_SampleMemorySize) || dlsid.equals(DLSID_ManufacturersID) || dlsid.equals(DLSID_ProductID) || dlsid.equals(DLSID_SamplePlaybackRate);
    }

    private long cdlQuery(DLSID dlsid) {
        if (dlsid.equals(DLSID_GMInHardware)) {
            return 1L;
        }
        if (dlsid.equals(DLSID_GSInHardware) || dlsid.equals(DLSID_XGInHardware)) {
            return 0L;
        }
        if (dlsid.equals(DLSID_SupportsDLS1) || dlsid.equals(DLSID_SupportsDLS2)) {
            return 1L;
        }
        if (dlsid.equals(DLSID_SampleMemorySize)) {
            return Runtime.getRuntime().totalMemory();
        }
        if (!dlsid.equals(DLSID_ManufacturersID) && !dlsid.equals(DLSID_ProductID) && dlsid.equals(DLSID_SamplePlaybackRate)) {
            return 44100L;
        }
        return 0L;
    }

    private boolean readCdlChunk(RIFFReader rIFFReader) throws IOException {
        Stack stack = new Stack();
        while (rIFFReader.available() != 0) {
            switch (rIFFReader.readUnsignedShort()) {
                case 1:
                    stack.push(Long.valueOf((((Long) stack.pop()).longValue() == 0 || ((Long) stack.pop()).longValue() == 0) ? 0L : 1L));
                    break;
                case 2:
                    stack.push(Long.valueOf((((Long) stack.pop()).longValue() == 0 && ((Long) stack.pop()).longValue() == 0) ? 0L : 1L));
                    break;
                case 3:
                    stack.push(Long.valueOf(((((Long) stack.pop()).longValue() > 0L ? 1 : (((Long) stack.pop()).longValue() == 0L ? 0 : -1)) != 0) ^ ((((Long) stack.pop()).longValue() > 0L ? 1 : (((Long) stack.pop()).longValue() == 0L ? 0 : -1)) != 0) ? 1L : 0L));
                    break;
                case 4:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() + ((Long) stack.pop()).longValue()));
                    break;
                case 5:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() - ((Long) stack.pop()).longValue()));
                    break;
                case 6:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() * ((Long) stack.pop()).longValue()));
                    break;
                case 7:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() / ((Long) stack.pop()).longValue()));
                    break;
                case 8:
                    stack.push(Long.valueOf((((Long) stack.pop()).longValue() == 0 || ((Long) stack.pop()).longValue() == 0) ? 0L : 1L));
                    break;
                case 9:
                    stack.push(Long.valueOf((((Long) stack.pop()).longValue() == 0 && ((Long) stack.pop()).longValue() == 0) ? 0L : 1L));
                    break;
                case 10:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() < ((Long) stack.pop()).longValue() ? 1L : 0L));
                    break;
                case 11:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() <= ((Long) stack.pop()).longValue() ? 1L : 0L));
                    break;
                case 12:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() > ((Long) stack.pop()).longValue() ? 1L : 0L));
                    break;
                case 13:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() >= ((Long) stack.pop()).longValue() ? 1L : 0L));
                    break;
                case 14:
                    stack.push(Long.valueOf(((Long) stack.pop()).longValue() == ((Long) stack.pop()).longValue() ? 1L : 0L));
                    break;
                case 15:
                    long jLongValue = ((Long) stack.pop()).longValue();
                    ((Long) stack.pop()).longValue();
                    stack.push(Long.valueOf(jLongValue == 0 ? 1L : 0L));
                    break;
                case 16:
                    stack.push(Long.valueOf(rIFFReader.readUnsignedInt()));
                    break;
                case 17:
                    stack.push(Long.valueOf(cdlQuery(DLSID.read(rIFFReader))));
                    break;
                case 18:
                    stack.push(Long.valueOf(cdlIsQuerySupported(DLSID.read(rIFFReader)) ? 1L : 0L));
                    break;
            }
        }
        return !stack.isEmpty() && ((Long) stack.pop()).longValue() == 1;
    }

    private void readInfoChunk(RIFFReader rIFFReader) throws IOException {
        this.info.name = null;
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("INAM")) {
                this.info.name = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICRD")) {
                this.info.creationDate = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IENG")) {
                this.info.engineers = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IPRD")) {
                this.info.product = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICOP")) {
                this.info.copyright = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMT")) {
                this.info.comments = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISFT")) {
                this.info.tools = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IARL")) {
                this.info.archival_location = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IART")) {
                this.info.artist = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMS")) {
                this.info.commissioned = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IGNR")) {
                this.info.genre = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IKEY")) {
                this.info.keywords = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IMED")) {
                this.info.medium = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISBJ")) {
                this.info.subject = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISRC")) {
                this.info.source = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISRF")) {
                this.info.source_form = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ITCH")) {
                this.info.technician = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            }
        }
    }

    private void readLinsChunk(RIFFReader rIFFReader) throws IOException {
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("LIST") && rIFFReaderNextChunk.getType().equals("ins ")) {
                readInsChunk(rIFFReaderNextChunk);
            }
        }
    }

    private void readInsChunk(RIFFReader rIFFReader) throws IOException {
        DLSInstrument dLSInstrument = new DLSInstrument(this);
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("LIST")) {
                if (rIFFReaderNextChunk.getType().equals("INFO")) {
                    readInsInfoChunk(dLSInstrument, rIFFReaderNextChunk);
                }
                if (rIFFReaderNextChunk.getType().equals("lrgn")) {
                    while (rIFFReaderNextChunk.hasNextChunk()) {
                        RIFFReader rIFFReaderNextChunk2 = rIFFReaderNextChunk.nextChunk();
                        if (rIFFReaderNextChunk2.getFormat().equals("LIST")) {
                            if (rIFFReaderNextChunk2.getType().equals("rgn ")) {
                                DLSRegion dLSRegion = new DLSRegion();
                                if (readRgnChunk(dLSRegion, rIFFReaderNextChunk2)) {
                                    dLSInstrument.getRegions().add(dLSRegion);
                                }
                            }
                            if (rIFFReaderNextChunk2.getType().equals("rgn2")) {
                                DLSRegion dLSRegion2 = new DLSRegion();
                                if (readRgnChunk(dLSRegion2, rIFFReaderNextChunk2)) {
                                    dLSInstrument.getRegions().add(dLSRegion2);
                                }
                            }
                        }
                    }
                }
                if (rIFFReaderNextChunk.getType().equals("lart")) {
                    ArrayList arrayList = new ArrayList();
                    while (true) {
                        if (!rIFFReaderNextChunk.hasNextChunk()) {
                            break;
                        }
                        RIFFReader rIFFReaderNextChunk3 = rIFFReaderNextChunk.nextChunk();
                        if (rIFFReaderNextChunk.getFormat().equals("cdl ") && !readCdlChunk(rIFFReaderNextChunk)) {
                            arrayList.clear();
                            break;
                        } else if (rIFFReaderNextChunk3.getFormat().equals("art1")) {
                            readArt1Chunk(arrayList, rIFFReaderNextChunk3);
                        }
                    }
                    dLSInstrument.getModulators().addAll(arrayList);
                }
                if (rIFFReaderNextChunk.getType().equals("lar2")) {
                    ArrayList arrayList2 = new ArrayList();
                    while (true) {
                        if (!rIFFReaderNextChunk.hasNextChunk()) {
                            break;
                        }
                        RIFFReader rIFFReaderNextChunk4 = rIFFReaderNextChunk.nextChunk();
                        if (rIFFReaderNextChunk.getFormat().equals("cdl ") && !readCdlChunk(rIFFReaderNextChunk)) {
                            arrayList2.clear();
                            break;
                        } else if (rIFFReaderNextChunk4.getFormat().equals("art2")) {
                            readArt2Chunk(arrayList2, rIFFReaderNextChunk4);
                        }
                    }
                    dLSInstrument.getModulators().addAll(arrayList2);
                }
            } else {
                if (format.equals("dlid")) {
                    dLSInstrument.guid = new byte[16];
                    rIFFReaderNextChunk.readFully(dLSInstrument.guid);
                }
                if (format.equals("insh")) {
                    rIFFReaderNextChunk.readUnsignedInt();
                    int i2 = rIFFReaderNextChunk.read() + ((rIFFReaderNextChunk.read() & 127) << 7);
                    rIFFReaderNextChunk.read();
                    int i3 = rIFFReaderNextChunk.read();
                    int i4 = rIFFReaderNextChunk.read() & 127;
                    rIFFReaderNextChunk.read();
                    rIFFReaderNextChunk.read();
                    rIFFReaderNextChunk.read();
                    dLSInstrument.bank = i2;
                    dLSInstrument.preset = i4;
                    dLSInstrument.druminstrument = (i3 & 128) > 0;
                }
            }
        }
        this.instruments.add(dLSInstrument);
    }

    private void readArt1Chunk(List<DLSModulator> list, RIFFReader rIFFReader) throws IOException {
        long unsignedInt = rIFFReader.readUnsignedInt();
        long unsignedInt2 = rIFFReader.readUnsignedInt();
        if (unsignedInt - 8 != 0) {
            rIFFReader.skip(unsignedInt - 8);
        }
        for (int i2 = 0; i2 < unsignedInt2; i2++) {
            DLSModulator dLSModulator = new DLSModulator();
            dLSModulator.version = 1;
            dLSModulator.source = rIFFReader.readUnsignedShort();
            dLSModulator.control = rIFFReader.readUnsignedShort();
            dLSModulator.destination = rIFFReader.readUnsignedShort();
            dLSModulator.transform = rIFFReader.readUnsignedShort();
            dLSModulator.scale = rIFFReader.readInt();
            list.add(dLSModulator);
        }
    }

    private void readArt2Chunk(List<DLSModulator> list, RIFFReader rIFFReader) throws IOException {
        long unsignedInt = rIFFReader.readUnsignedInt();
        long unsignedInt2 = rIFFReader.readUnsignedInt();
        if (unsignedInt - 8 != 0) {
            rIFFReader.skip(unsignedInt - 8);
        }
        for (int i2 = 0; i2 < unsignedInt2; i2++) {
            DLSModulator dLSModulator = new DLSModulator();
            dLSModulator.version = 2;
            dLSModulator.source = rIFFReader.readUnsignedShort();
            dLSModulator.control = rIFFReader.readUnsignedShort();
            dLSModulator.destination = rIFFReader.readUnsignedShort();
            dLSModulator.transform = rIFFReader.readUnsignedShort();
            dLSModulator.scale = rIFFReader.readInt();
            list.add(dLSModulator);
        }
    }

    private boolean readRgnChunk(DLSRegion dLSRegion, RIFFReader rIFFReader) throws IOException {
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("LIST")) {
                if (rIFFReaderNextChunk.getType().equals("lart")) {
                    ArrayList arrayList = new ArrayList();
                    while (true) {
                        if (!rIFFReaderNextChunk.hasNextChunk()) {
                            break;
                        }
                        RIFFReader rIFFReaderNextChunk2 = rIFFReaderNextChunk.nextChunk();
                        if (rIFFReaderNextChunk.getFormat().equals("cdl ") && !readCdlChunk(rIFFReaderNextChunk)) {
                            arrayList.clear();
                            break;
                        }
                        if (rIFFReaderNextChunk2.getFormat().equals("art1")) {
                            readArt1Chunk(arrayList, rIFFReaderNextChunk2);
                        }
                    }
                    dLSRegion.getModulators().addAll(arrayList);
                }
                if (rIFFReaderNextChunk.getType().equals("lar2")) {
                    ArrayList arrayList2 = new ArrayList();
                    while (true) {
                        if (!rIFFReaderNextChunk.hasNextChunk()) {
                            break;
                        }
                        RIFFReader rIFFReaderNextChunk3 = rIFFReaderNextChunk.nextChunk();
                        if (rIFFReaderNextChunk.getFormat().equals("cdl ") && !readCdlChunk(rIFFReaderNextChunk)) {
                            arrayList2.clear();
                            break;
                        }
                        if (rIFFReaderNextChunk3.getFormat().equals("art2")) {
                            readArt2Chunk(arrayList2, rIFFReaderNextChunk3);
                        }
                    }
                    dLSRegion.getModulators().addAll(arrayList2);
                }
            } else {
                if (format.equals("cdl ") && !readCdlChunk(rIFFReaderNextChunk)) {
                    return false;
                }
                if (format.equals("rgnh")) {
                    dLSRegion.keyfrom = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.keyto = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.velfrom = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.velto = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.options = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.exclusiveClass = rIFFReaderNextChunk.readUnsignedShort();
                }
                if (format.equals("wlnk")) {
                    dLSRegion.fusoptions = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.phasegroup = rIFFReaderNextChunk.readUnsignedShort();
                    dLSRegion.channel = rIFFReaderNextChunk.readUnsignedInt();
                    this.temp_rgnassign.put(dLSRegion, Long.valueOf(rIFFReaderNextChunk.readUnsignedInt()));
                }
                if (format.equals("wsmp")) {
                    dLSRegion.sampleoptions = new DLSSampleOptions();
                    readWsmpChunk(dLSRegion.sampleoptions, rIFFReaderNextChunk);
                }
            }
        }
        return true;
    }

    private void readWsmpChunk(DLSSampleOptions dLSSampleOptions, RIFFReader rIFFReader) throws IOException {
        long unsignedInt = rIFFReader.readUnsignedInt();
        dLSSampleOptions.unitynote = rIFFReader.readUnsignedShort();
        dLSSampleOptions.finetune = rIFFReader.readShort();
        dLSSampleOptions.attenuation = rIFFReader.readInt();
        dLSSampleOptions.options = rIFFReader.readUnsignedInt();
        long j2 = rIFFReader.readInt();
        if (unsignedInt > 20) {
            rIFFReader.skip(unsignedInt - 20);
        }
        for (int i2 = 0; i2 < j2; i2++) {
            DLSSampleLoop dLSSampleLoop = new DLSSampleLoop();
            long unsignedInt2 = rIFFReader.readUnsignedInt();
            dLSSampleLoop.type = rIFFReader.readUnsignedInt();
            dLSSampleLoop.start = rIFFReader.readUnsignedInt();
            dLSSampleLoop.length = rIFFReader.readUnsignedInt();
            dLSSampleOptions.loops.add(dLSSampleLoop);
            if (unsignedInt2 > 16) {
                rIFFReader.skip(unsignedInt2 - 16);
            }
        }
    }

    private void readInsInfoChunk(DLSInstrument dLSInstrument, RIFFReader rIFFReader) throws IOException {
        dLSInstrument.info.name = null;
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("INAM")) {
                dLSInstrument.info.name = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICRD")) {
                dLSInstrument.info.creationDate = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IENG")) {
                dLSInstrument.info.engineers = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IPRD")) {
                dLSInstrument.info.product = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICOP")) {
                dLSInstrument.info.copyright = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMT")) {
                dLSInstrument.info.comments = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISFT")) {
                dLSInstrument.info.tools = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IARL")) {
                dLSInstrument.info.archival_location = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IART")) {
                dLSInstrument.info.artist = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMS")) {
                dLSInstrument.info.commissioned = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IGNR")) {
                dLSInstrument.info.genre = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IKEY")) {
                dLSInstrument.info.keywords = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IMED")) {
                dLSInstrument.info.medium = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISBJ")) {
                dLSInstrument.info.subject = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISRC")) {
                dLSInstrument.info.source = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISRF")) {
                dLSInstrument.info.source_form = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ITCH")) {
                dLSInstrument.info.technician = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            }
        }
    }

    private void readWvplChunk(RIFFReader rIFFReader) throws IOException {
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            if (rIFFReaderNextChunk.getFormat().equals("LIST") && rIFFReaderNextChunk.getType().equals("wave")) {
                readWaveChunk(rIFFReaderNextChunk);
            }
        }
    }

    private void readWaveChunk(RIFFReader rIFFReader) throws IOException {
        DLSSample dLSSample = new DLSSample(this);
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("LIST")) {
                if (rIFFReaderNextChunk.getType().equals("INFO")) {
                    readWaveInfoChunk(dLSSample, rIFFReaderNextChunk);
                }
            } else {
                if (format.equals("dlid")) {
                    dLSSample.guid = new byte[16];
                    rIFFReaderNextChunk.readFully(dLSSample.guid);
                }
                if (format.equals("fmt ")) {
                    int unsignedShort = rIFFReaderNextChunk.readUnsignedShort();
                    if (unsignedShort != 1 && unsignedShort != 3) {
                        throw new RIFFInvalidDataException("Only PCM samples are supported!");
                    }
                    int unsignedShort2 = rIFFReaderNextChunk.readUnsignedShort();
                    long unsignedInt = rIFFReaderNextChunk.readUnsignedInt();
                    rIFFReaderNextChunk.readUnsignedInt();
                    int unsignedShort3 = rIFFReaderNextChunk.readUnsignedShort();
                    int unsignedShort4 = rIFFReaderNextChunk.readUnsignedShort();
                    AudioFormat audioFormat = null;
                    if (unsignedShort == 1) {
                        if (unsignedShort4 == 8) {
                            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, unsignedInt, unsignedShort4, unsignedShort2, unsignedShort3, unsignedInt, false);
                        } else {
                            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, unsignedInt, unsignedShort4, unsignedShort2, unsignedShort3, unsignedInt, false);
                        }
                    }
                    if (unsignedShort == 3) {
                        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, unsignedInt, unsignedShort4, unsignedShort2, unsignedShort3, unsignedInt, false);
                    }
                    dLSSample.format = audioFormat;
                }
                if (format.equals("data")) {
                    if (this.largeFormat) {
                        dLSSample.setData(new ModelByteBuffer(this.sampleFile, rIFFReaderNextChunk.getFilePointer(), rIFFReaderNextChunk.available()));
                    } else {
                        byte[] bArr = new byte[rIFFReaderNextChunk.available()];
                        dLSSample.setData(bArr);
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
                    }
                }
                if (format.equals("wsmp")) {
                    dLSSample.sampleoptions = new DLSSampleOptions();
                    readWsmpChunk(dLSSample.sampleoptions, rIFFReaderNextChunk);
                }
            }
        }
        this.samples.add(dLSSample);
    }

    private void readWaveInfoChunk(DLSSample dLSSample, RIFFReader rIFFReader) throws IOException {
        dLSSample.info.name = null;
        while (rIFFReader.hasNextChunk()) {
            RIFFReader rIFFReaderNextChunk = rIFFReader.nextChunk();
            String format = rIFFReaderNextChunk.getFormat();
            if (format.equals("INAM")) {
                dLSSample.info.name = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICRD")) {
                dLSSample.info.creationDate = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IENG")) {
                dLSSample.info.engineers = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IPRD")) {
                dLSSample.info.product = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICOP")) {
                dLSSample.info.copyright = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMT")) {
                dLSSample.info.comments = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISFT")) {
                dLSSample.info.tools = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IARL")) {
                dLSSample.info.archival_location = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IART")) {
                dLSSample.info.artist = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ICMS")) {
                dLSSample.info.commissioned = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IGNR")) {
                dLSSample.info.genre = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IKEY")) {
                dLSSample.info.keywords = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("IMED")) {
                dLSSample.info.medium = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISBJ")) {
                dLSSample.info.subject = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISRC")) {
                dLSSample.info.source = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ISRF")) {
                dLSSample.info.source_form = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            } else if (format.equals("ITCH")) {
                dLSSample.info.technician = rIFFReaderNextChunk.readString(rIFFReaderNextChunk.available());
            }
        }
    }

    public void save(String str) throws IOException {
        writeSoundbank(new RIFFWriter(str, "DLS "));
    }

    public void save(File file) throws IOException {
        writeSoundbank(new RIFFWriter(file, "DLS "));
    }

    public void save(OutputStream outputStream) throws IOException {
        writeSoundbank(new RIFFWriter(outputStream, "DLS "));
    }

    private void writeSoundbank(RIFFWriter rIFFWriter) throws IOException {
        rIFFWriter.writeChunk("colh").writeUnsignedInt(this.instruments.size());
        if (this.major != -1 && this.minor != -1) {
            RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("vers");
            rIFFWriterWriteChunk.writeUnsignedInt(this.major);
            rIFFWriterWriteChunk.writeUnsignedInt(this.minor);
        }
        writeInstruments(rIFFWriter.writeList("lins"));
        RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeChunk("ptbl");
        rIFFWriterWriteChunk2.writeUnsignedInt(8L);
        rIFFWriterWriteChunk2.writeUnsignedInt(this.samples.size());
        long filePointer = rIFFWriter.getFilePointer();
        for (int i2 = 0; i2 < this.samples.size(); i2++) {
            rIFFWriterWriteChunk2.writeUnsignedInt(0L);
        }
        RIFFWriter rIFFWriterWriteList = rIFFWriter.writeList("wvpl");
        long filePointer2 = rIFFWriterWriteList.getFilePointer();
        ArrayList arrayList = new ArrayList();
        for (DLSSample dLSSample : this.samples) {
            arrayList.add(Long.valueOf(rIFFWriterWriteList.getFilePointer() - filePointer2));
            writeSample(rIFFWriterWriteList.writeList("wave"), dLSSample);
        }
        long filePointer3 = rIFFWriter.getFilePointer();
        rIFFWriter.seek(filePointer);
        rIFFWriter.setWriteOverride(true);
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            rIFFWriter.writeUnsignedInt(((Long) it.next()).longValue());
        }
        rIFFWriter.setWriteOverride(false);
        rIFFWriter.seek(filePointer3);
        writeInfo(rIFFWriter.writeList("INFO"), this.info);
        rIFFWriter.close();
    }

    private void writeSample(RIFFWriter rIFFWriter, DLSSample dLSSample) throws IOException {
        AudioFormat format = dLSSample.getFormat();
        AudioFormat.Encoding encoding = format.getEncoding();
        float sampleRate = format.getSampleRate();
        int sampleSizeInBits = format.getSampleSizeInBits();
        int channels = format.getChannels();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        boolean zIsBigEndian = format.isBigEndian();
        boolean z2 = false;
        if (format.getSampleSizeInBits() == 8) {
            if (!encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                encoding = AudioFormat.Encoding.PCM_UNSIGNED;
                z2 = true;
            }
        } else {
            if (!encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                encoding = AudioFormat.Encoding.PCM_SIGNED;
                z2 = true;
            }
            if (zIsBigEndian) {
                zIsBigEndian = false;
                z2 = true;
            }
        }
        if (z2) {
            format = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, zIsBigEndian);
        }
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("fmt ");
        int i2 = 0;
        if (format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED) || format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
            i2 = 1;
        } else if (format.getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT)) {
            i2 = 3;
        }
        rIFFWriterWriteChunk.writeUnsignedShort(i2);
        rIFFWriterWriteChunk.writeUnsignedShort(format.getChannels());
        rIFFWriterWriteChunk.writeUnsignedInt((long) format.getSampleRate());
        rIFFWriterWriteChunk.writeUnsignedInt(((long) format.getFrameRate()) * format.getFrameSize());
        rIFFWriterWriteChunk.writeUnsignedShort(format.getFrameSize());
        rIFFWriterWriteChunk.writeUnsignedShort(format.getSampleSizeInBits());
        rIFFWriterWriteChunk.write(0);
        rIFFWriterWriteChunk.write(0);
        writeSampleOptions(rIFFWriter.writeChunk("wsmp"), dLSSample.sampleoptions);
        if (z2) {
            RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeChunk("data");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(format, (AudioInputStream) dLSSample.getData());
            byte[] bArr = new byte[1024];
            while (true) {
                int i3 = audioInputStream.read(bArr);
                if (i3 == -1) {
                    break;
                } else {
                    rIFFWriterWriteChunk2.write(bArr, 0, i3);
                }
            }
        } else {
            dLSSample.getDataBuffer().writeTo(rIFFWriter.writeChunk("data"));
        }
        writeInfo(rIFFWriter.writeList("INFO"), dLSSample.info);
    }

    private void writeInstruments(RIFFWriter rIFFWriter) throws IOException {
        Iterator<DLSInstrument> it = this.instruments.iterator();
        while (it.hasNext()) {
            writeInstrument(rIFFWriter.writeList("ins "), it.next());
        }
    }

    private void writeInstrument(RIFFWriter rIFFWriter, DLSInstrument dLSInstrument) throws IOException {
        int i2 = 0;
        int i3 = 0;
        for (DLSModulator dLSModulator : dLSInstrument.getModulators()) {
            if (dLSModulator.version == 1) {
                i2++;
            }
            if (dLSModulator.version == 2) {
                i3++;
            }
        }
        Iterator<DLSRegion> it = dLSInstrument.regions.iterator();
        while (it.hasNext()) {
            for (DLSModulator dLSModulator2 : it.next().getModulators()) {
                if (dLSModulator2.version == 1) {
                    i2++;
                }
                if (dLSModulator2.version == 2) {
                    i3++;
                }
            }
        }
        int i4 = 1;
        if (i3 > 0) {
            i4 = 2;
        }
        RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeChunk("insh");
        rIFFWriterWriteChunk.writeUnsignedInt(dLSInstrument.getRegions().size());
        rIFFWriterWriteChunk.writeUnsignedInt(dLSInstrument.bank + (dLSInstrument.druminstrument ? 2147483648L : 0L));
        rIFFWriterWriteChunk.writeUnsignedInt(dLSInstrument.preset);
        RIFFWriter rIFFWriterWriteList = rIFFWriter.writeList("lrgn");
        Iterator<DLSRegion> it2 = dLSInstrument.regions.iterator();
        while (it2.hasNext()) {
            writeRegion(rIFFWriterWriteList, it2.next(), i4);
        }
        writeArticulators(rIFFWriter, dLSInstrument.getModulators());
        writeInfo(rIFFWriter.writeList("INFO"), dLSInstrument.info);
    }

    private void writeArticulators(RIFFWriter rIFFWriter, List<DLSModulator> list) throws IOException {
        int i2 = 0;
        int i3 = 0;
        for (DLSModulator dLSModulator : list) {
            if (dLSModulator.version == 1) {
                i2++;
            }
            if (dLSModulator.version == 2) {
                i3++;
            }
        }
        if (i2 > 0) {
            RIFFWriter rIFFWriterWriteChunk = rIFFWriter.writeList("lart").writeChunk("art1");
            rIFFWriterWriteChunk.writeUnsignedInt(8L);
            rIFFWriterWriteChunk.writeUnsignedInt(i2);
            for (DLSModulator dLSModulator2 : list) {
                if (dLSModulator2.version == 1) {
                    rIFFWriterWriteChunk.writeUnsignedShort(dLSModulator2.source);
                    rIFFWriterWriteChunk.writeUnsignedShort(dLSModulator2.control);
                    rIFFWriterWriteChunk.writeUnsignedShort(dLSModulator2.destination);
                    rIFFWriterWriteChunk.writeUnsignedShort(dLSModulator2.transform);
                    rIFFWriterWriteChunk.writeInt(dLSModulator2.scale);
                }
            }
        }
        if (i3 > 0) {
            RIFFWriter rIFFWriterWriteChunk2 = rIFFWriter.writeList("lar2").writeChunk("art2");
            rIFFWriterWriteChunk2.writeUnsignedInt(8L);
            rIFFWriterWriteChunk2.writeUnsignedInt(i3);
            for (DLSModulator dLSModulator3 : list) {
                if (dLSModulator3.version == 2) {
                    rIFFWriterWriteChunk2.writeUnsignedShort(dLSModulator3.source);
                    rIFFWriterWriteChunk2.writeUnsignedShort(dLSModulator3.control);
                    rIFFWriterWriteChunk2.writeUnsignedShort(dLSModulator3.destination);
                    rIFFWriterWriteChunk2.writeUnsignedShort(dLSModulator3.transform);
                    rIFFWriterWriteChunk2.writeInt(dLSModulator3.scale);
                }
            }
        }
    }

    private void writeRegion(RIFFWriter rIFFWriter, DLSRegion dLSRegion, int i2) throws IOException {
        RIFFWriter rIFFWriterWriteList = null;
        if (i2 == 1) {
            rIFFWriterWriteList = rIFFWriter.writeList("rgn ");
        }
        if (i2 == 2) {
            rIFFWriterWriteList = rIFFWriter.writeList("rgn2");
        }
        if (rIFFWriterWriteList == null) {
            return;
        }
        RIFFWriter rIFFWriterWriteChunk = rIFFWriterWriteList.writeChunk("rgnh");
        rIFFWriterWriteChunk.writeUnsignedShort(dLSRegion.keyfrom);
        rIFFWriterWriteChunk.writeUnsignedShort(dLSRegion.keyto);
        rIFFWriterWriteChunk.writeUnsignedShort(dLSRegion.velfrom);
        rIFFWriterWriteChunk.writeUnsignedShort(dLSRegion.velto);
        rIFFWriterWriteChunk.writeUnsignedShort(dLSRegion.options);
        rIFFWriterWriteChunk.writeUnsignedShort(dLSRegion.exclusiveClass);
        if (dLSRegion.sampleoptions != null) {
            writeSampleOptions(rIFFWriterWriteList.writeChunk("wsmp"), dLSRegion.sampleoptions);
        }
        if (dLSRegion.sample != null && this.samples.indexOf(dLSRegion.sample) != -1) {
            RIFFWriter rIFFWriterWriteChunk2 = rIFFWriterWriteList.writeChunk("wlnk");
            rIFFWriterWriteChunk2.writeUnsignedShort(dLSRegion.fusoptions);
            rIFFWriterWriteChunk2.writeUnsignedShort(dLSRegion.phasegroup);
            rIFFWriterWriteChunk2.writeUnsignedInt(dLSRegion.channel);
            rIFFWriterWriteChunk2.writeUnsignedInt(this.samples.indexOf(dLSRegion.sample));
        }
        writeArticulators(rIFFWriterWriteList, dLSRegion.getModulators());
        rIFFWriterWriteList.close();
    }

    private void writeSampleOptions(RIFFWriter rIFFWriter, DLSSampleOptions dLSSampleOptions) throws IOException {
        rIFFWriter.writeUnsignedInt(20L);
        rIFFWriter.writeUnsignedShort(dLSSampleOptions.unitynote);
        rIFFWriter.writeShort(dLSSampleOptions.finetune);
        rIFFWriter.writeInt(dLSSampleOptions.attenuation);
        rIFFWriter.writeUnsignedInt(dLSSampleOptions.options);
        rIFFWriter.writeInt(dLSSampleOptions.loops.size());
        for (DLSSampleLoop dLSSampleLoop : dLSSampleOptions.loops) {
            rIFFWriter.writeUnsignedInt(16L);
            rIFFWriter.writeUnsignedInt(dLSSampleLoop.type);
            rIFFWriter.writeUnsignedInt(dLSSampleLoop.start);
            rIFFWriter.writeUnsignedInt(dLSSampleLoop.length);
        }
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

    private void writeInfo(RIFFWriter rIFFWriter, DLSInfo dLSInfo) throws IOException {
        writeInfoStringChunk(rIFFWriter, "INAM", dLSInfo.name);
        writeInfoStringChunk(rIFFWriter, "ICRD", dLSInfo.creationDate);
        writeInfoStringChunk(rIFFWriter, "IENG", dLSInfo.engineers);
        writeInfoStringChunk(rIFFWriter, "IPRD", dLSInfo.product);
        writeInfoStringChunk(rIFFWriter, "ICOP", dLSInfo.copyright);
        writeInfoStringChunk(rIFFWriter, "ICMT", dLSInfo.comments);
        writeInfoStringChunk(rIFFWriter, "ISFT", dLSInfo.tools);
        writeInfoStringChunk(rIFFWriter, "IARL", dLSInfo.archival_location);
        writeInfoStringChunk(rIFFWriter, "IART", dLSInfo.artist);
        writeInfoStringChunk(rIFFWriter, "ICMS", dLSInfo.commissioned);
        writeInfoStringChunk(rIFFWriter, "IGNR", dLSInfo.genre);
        writeInfoStringChunk(rIFFWriter, "IKEY", dLSInfo.keywords);
        writeInfoStringChunk(rIFFWriter, "IMED", dLSInfo.medium);
        writeInfoStringChunk(rIFFWriter, "ISBJ", dLSInfo.subject);
        writeInfoStringChunk(rIFFWriter, "ISRC", dLSInfo.source);
        writeInfoStringChunk(rIFFWriter, "ISRF", dLSInfo.source_form);
        writeInfoStringChunk(rIFFWriter, "ITCH", dLSInfo.technician);
    }

    public DLSInfo getInfo() {
        return this.info;
    }

    @Override // javax.sound.midi.Soundbank
    public String getName() {
        return this.info.name;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVersion() {
        return this.major + "." + this.minor;
    }

    @Override // javax.sound.midi.Soundbank
    public String getVendor() {
        return this.info.engineers;
    }

    @Override // javax.sound.midi.Soundbank
    public String getDescription() {
        return this.info.comments;
    }

    public void setName(String str) {
        this.info.name = str;
    }

    public void setVendor(String str) {
        this.info.engineers = str;
    }

    public void setDescription(String str) {
        this.info.comments = str;
    }

    @Override // javax.sound.midi.Soundbank
    public SoundbankResource[] getResources() {
        SoundbankResource[] soundbankResourceArr = new SoundbankResource[this.samples.size()];
        int i2 = 0;
        for (int i3 = 0; i3 < this.samples.size(); i3++) {
            int i4 = i2;
            i2++;
            soundbankResourceArr[i4] = this.samples.get(i3);
        }
        return soundbankResourceArr;
    }

    @Override // javax.sound.midi.Soundbank
    public DLSInstrument[] getInstruments() {
        DLSInstrument[] dLSInstrumentArr = (DLSInstrument[]) this.instruments.toArray(new DLSInstrument[this.instruments.size()]);
        Arrays.sort(dLSInstrumentArr, new ModelInstrumentComparator());
        return dLSInstrumentArr;
    }

    public DLSSample[] getSamples() {
        return (DLSSample[]) this.samples.toArray(new DLSSample[this.samples.size()]);
    }

    @Override // javax.sound.midi.Soundbank
    public Instrument getInstrument(Patch patch) {
        int program = patch.getProgram();
        int bank = patch.getBank();
        boolean zIsPercussion = false;
        if (patch instanceof ModelPatch) {
            zIsPercussion = ((ModelPatch) patch).isPercussion();
        }
        for (DLSInstrument dLSInstrument : this.instruments) {
            Patch patch2 = dLSInstrument.getPatch();
            int program2 = patch2.getProgram();
            int bank2 = patch2.getBank();
            if (program == program2 && bank == bank2) {
                boolean zIsPercussion2 = false;
                if (patch2 instanceof ModelPatch) {
                    zIsPercussion2 = ((ModelPatch) patch2).isPercussion();
                }
                if (zIsPercussion == zIsPercussion2) {
                    return dLSInstrument;
                }
            }
        }
        return null;
    }

    public void addResource(SoundbankResource soundbankResource) {
        if (soundbankResource instanceof DLSInstrument) {
            this.instruments.add((DLSInstrument) soundbankResource);
        }
        if (soundbankResource instanceof DLSSample) {
            this.samples.add((DLSSample) soundbankResource);
        }
    }

    public void removeResource(SoundbankResource soundbankResource) {
        if (soundbankResource instanceof DLSInstrument) {
            this.instruments.remove((DLSInstrument) soundbankResource);
        }
        if (soundbankResource instanceof DLSSample) {
            this.samples.remove((DLSSample) soundbankResource);
        }
    }

    public void addInstrument(DLSInstrument dLSInstrument) {
        this.instruments.add(dLSInstrument);
    }

    public void removeInstrument(DLSInstrument dLSInstrument) {
        this.instruments.remove(dLSInstrument);
    }

    public long getMajor() {
        return this.major;
    }

    public void setMajor(long j2) {
        this.major = j2;
    }

    public long getMinor() {
        return this.minor;
    }

    public void setMinor(long j2) {
        this.minor = j2;
    }
}
