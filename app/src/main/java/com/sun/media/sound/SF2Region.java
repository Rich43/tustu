package com.sun.media.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:com/sun/media/sound/SF2Region.class */
public class SF2Region {
    public static final int GENERATOR_STARTADDRSOFFSET = 0;
    public static final int GENERATOR_ENDADDRSOFFSET = 1;
    public static final int GENERATOR_STARTLOOPADDRSOFFSET = 2;
    public static final int GENERATOR_ENDLOOPADDRSOFFSET = 3;
    public static final int GENERATOR_STARTADDRSCOARSEOFFSET = 4;
    public static final int GENERATOR_MODLFOTOPITCH = 5;
    public static final int GENERATOR_VIBLFOTOPITCH = 6;
    public static final int GENERATOR_MODENVTOPITCH = 7;
    public static final int GENERATOR_INITIALFILTERFC = 8;
    public static final int GENERATOR_INITIALFILTERQ = 9;
    public static final int GENERATOR_MODLFOTOFILTERFC = 10;
    public static final int GENERATOR_MODENVTOFILTERFC = 11;
    public static final int GENERATOR_ENDADDRSCOARSEOFFSET = 12;
    public static final int GENERATOR_MODLFOTOVOLUME = 13;
    public static final int GENERATOR_UNUSED1 = 14;
    public static final int GENERATOR_CHORUSEFFECTSSEND = 15;
    public static final int GENERATOR_REVERBEFFECTSSEND = 16;
    public static final int GENERATOR_PAN = 17;
    public static final int GENERATOR_UNUSED2 = 18;
    public static final int GENERATOR_UNUSED3 = 19;
    public static final int GENERATOR_UNUSED4 = 20;
    public static final int GENERATOR_DELAYMODLFO = 21;
    public static final int GENERATOR_FREQMODLFO = 22;
    public static final int GENERATOR_DELAYVIBLFO = 23;
    public static final int GENERATOR_FREQVIBLFO = 24;
    public static final int GENERATOR_DELAYMODENV = 25;
    public static final int GENERATOR_ATTACKMODENV = 26;
    public static final int GENERATOR_HOLDMODENV = 27;
    public static final int GENERATOR_DECAYMODENV = 28;
    public static final int GENERATOR_SUSTAINMODENV = 29;
    public static final int GENERATOR_RELEASEMODENV = 30;
    public static final int GENERATOR_KEYNUMTOMODENVHOLD = 31;
    public static final int GENERATOR_KEYNUMTOMODENVDECAY = 32;
    public static final int GENERATOR_DELAYVOLENV = 33;
    public static final int GENERATOR_ATTACKVOLENV = 34;
    public static final int GENERATOR_HOLDVOLENV = 35;
    public static final int GENERATOR_DECAYVOLENV = 36;
    public static final int GENERATOR_SUSTAINVOLENV = 37;
    public static final int GENERATOR_RELEASEVOLENV = 38;
    public static final int GENERATOR_KEYNUMTOVOLENVHOLD = 39;
    public static final int GENERATOR_KEYNUMTOVOLENVDECAY = 40;
    public static final int GENERATOR_INSTRUMENT = 41;
    public static final int GENERATOR_RESERVED1 = 42;
    public static final int GENERATOR_KEYRANGE = 43;
    public static final int GENERATOR_VELRANGE = 44;
    public static final int GENERATOR_STARTLOOPADDRSCOARSEOFFSET = 45;
    public static final int GENERATOR_KEYNUM = 46;
    public static final int GENERATOR_VELOCITY = 47;
    public static final int GENERATOR_INITIALATTENUATION = 48;
    public static final int GENERATOR_RESERVED2 = 49;
    public static final int GENERATOR_ENDLOOPADDRSCOARSEOFFSET = 50;
    public static final int GENERATOR_COARSETUNE = 51;
    public static final int GENERATOR_FINETUNE = 52;
    public static final int GENERATOR_SAMPLEID = 53;
    public static final int GENERATOR_SAMPLEMODES = 54;
    public static final int GENERATOR_RESERVED3 = 55;
    public static final int GENERATOR_SCALETUNING = 56;
    public static final int GENERATOR_EXCLUSIVECLASS = 57;
    public static final int GENERATOR_OVERRIDINGROOTKEY = 58;
    public static final int GENERATOR_UNUSED5 = 59;
    public static final int GENERATOR_ENDOPR = 60;
    protected Map<Integer, Short> generators = new HashMap();
    protected List<SF2Modulator> modulators = new ArrayList();

    public Map<Integer, Short> getGenerators() {
        return this.generators;
    }

    public boolean contains(int i2) {
        return this.generators.containsKey(Integer.valueOf(i2));
    }

    public static short getDefaultValue(int i2) {
        if (i2 == 8) {
            return (short) 13500;
        }
        if (i2 == 21 || i2 == 23 || i2 == 25 || i2 == 26 || i2 == 27 || i2 == 28 || i2 == 30 || i2 == 33 || i2 == 34 || i2 == 35 || i2 == 36 || i2 == 38) {
            return (short) -12000;
        }
        if (i2 == 43 || i2 == 44) {
            return (short) 32512;
        }
        if (i2 == 46 || i2 == 47) {
            return (short) -1;
        }
        if (i2 == 56) {
            return (short) 100;
        }
        return i2 == 58 ? (short) -1 : (short) 0;
    }

    public short getShort(int i2) {
        if (!contains(i2)) {
            return getDefaultValue(i2);
        }
        return this.generators.get(Integer.valueOf(i2)).shortValue();
    }

    public void putShort(int i2, short s2) {
        this.generators.put(Integer.valueOf(i2), Short.valueOf(s2));
    }

    public byte[] getBytes(int i2) {
        int integer = getInteger(i2);
        return new byte[]{(byte) (255 & integer), (byte) ((65280 & integer) >> 8)};
    }

    public void putBytes(int i2, byte[] bArr) {
        this.generators.put(Integer.valueOf(i2), Short.valueOf((short) (bArr[0] + (bArr[1] << 8))));
    }

    public int getInteger(int i2) {
        return 65535 & getShort(i2);
    }

    public void putInteger(int i2, int i3) {
        this.generators.put(Integer.valueOf(i2), Short.valueOf((short) i3));
    }

    public List<SF2Modulator> getModulators() {
        return this.modulators;
    }
}
