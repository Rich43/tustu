package com.sun.media.sound;

import java.util.ArrayList;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

/* loaded from: rt.jar:com/sun/media/sound/MidiUtils.class */
public final class MidiUtils {
    public static final int DEFAULT_TEMPO_MPQ = 500000;
    public static final int META_END_OF_TRACK_TYPE = 47;
    public static final int META_TEMPO_TYPE = 81;

    private MidiUtils() {
    }

    public static boolean isMetaEndOfTrack(MidiMessage midiMessage) {
        if (midiMessage.getLength() != 3 || midiMessage.getStatus() != 255) {
            return false;
        }
        byte[] message = midiMessage.getMessage();
        return (message[1] & 255) == 47 && message[2] == 0;
    }

    public static boolean isMetaTempo(MidiMessage midiMessage) {
        if (midiMessage.getLength() != 6 || midiMessage.getStatus() != 255) {
            return false;
        }
        byte[] message = midiMessage.getMessage();
        return (message[1] & 255) == 81 && message[2] == 3;
    }

    public static int getTempoMPQ(MidiMessage midiMessage) {
        if (midiMessage.getLength() != 6 || midiMessage.getStatus() != 255) {
            return -1;
        }
        byte[] message = midiMessage.getMessage();
        if ((message[1] & 255) != 81 || message[2] != 3) {
            return -1;
        }
        return (message[5] & 255) | ((message[4] & 255) << 8) | ((message[3] & 255) << 16);
    }

    public static double convertTempo(double d2) {
        if (d2 <= 0.0d) {
            d2 = 1.0d;
        }
        return 6.0E7d / d2;
    }

    public static long ticks2microsec(long j2, double d2, int i2) {
        return (long) ((j2 * d2) / i2);
    }

    public static long microsec2ticks(long j2, double d2, int i2) {
        return (long) ((j2 * i2) / d2);
    }

    public static long tick2microsecond(Sequence sequence, long j2, TempoCache tempoCache) {
        if (sequence.getDivisionType() != 0.0f) {
            return (long) (1000000.0d * (j2 / (sequence.getDivisionType() * sequence.getResolution())));
        }
        if (tempoCache == null) {
            tempoCache = new TempoCache(sequence);
        }
        int resolution = sequence.getResolution();
        long[] jArr = tempoCache.ticks;
        int length = tempoCache.tempos.length;
        int i2 = tempoCache.snapshotIndex;
        int iTicks2microsec = tempoCache.snapshotMicro;
        long jTicks2microsec = 0;
        if (i2 <= 0 || i2 >= length || jArr[i2] > j2) {
            iTicks2microsec = 0;
            i2 = 0;
        }
        if (length > 0) {
            for (int i3 = i2 + 1; i3 < length && jArr[i3] <= j2; i3++) {
                iTicks2microsec = (int) (iTicks2microsec + ticks2microsec(jArr[i3] - jArr[i3 - 1], r0[i3 - 1], resolution));
                i2 = i3;
            }
            jTicks2microsec = iTicks2microsec + ticks2microsec(j2 - jArr[i2], r0[i2], resolution);
        }
        tempoCache.snapshotIndex = i2;
        tempoCache.snapshotMicro = iTicks2microsec;
        return jTicks2microsec;
    }

    public static long microsecond2tick(Sequence sequence, long j2, TempoCache tempoCache) {
        if (sequence.getDivisionType() != 0.0f) {
            long divisionType = (long) (((j2 * sequence.getDivisionType()) * sequence.getResolution()) / 1000000.0d);
            if (tempoCache != null) {
                tempoCache.currTempo = (int) tempoCache.getTempoMPQAt(divisionType);
            }
            return divisionType;
        }
        if (tempoCache == null) {
            tempoCache = new TempoCache(sequence);
        }
        long[] jArr = tempoCache.ticks;
        int[] iArr = tempoCache.tempos;
        int length = iArr.length;
        int resolution = sequence.getResolution();
        long j3 = 0;
        long jMicrosec2ticks = 0;
        int i2 = 1;
        if (j2 > 0 && length > 0) {
            while (i2 < length) {
                long jTicks2microsec = j3 + ticks2microsec(jArr[i2] - jArr[i2 - 1], iArr[i2 - 1], resolution);
                if (jTicks2microsec > j2) {
                    break;
                }
                j3 = jTicks2microsec;
                i2++;
            }
            jMicrosec2ticks = jArr[i2 - 1] + microsec2ticks(j2 - j3, iArr[i2 - 1], resolution);
        }
        tempoCache.currTempo = iArr[i2 - 1];
        return jMicrosec2ticks;
    }

    public static int tick2index(Track track, long j2) {
        int i2 = 0;
        if (j2 > 0) {
            int i3 = 0;
            int size = track.size() - 1;
            while (true) {
                if (i3 >= size) {
                    break;
                }
                i2 = (i3 + size) >> 1;
                long tick = track.get(i2).getTick();
                if (tick == j2) {
                    break;
                }
                if (tick < j2) {
                    if (i3 == size - 1) {
                        i2++;
                        break;
                    }
                    i3 = i2;
                } else {
                    size = i2;
                }
            }
        }
        return i2;
    }

    /* loaded from: rt.jar:com/sun/media/sound/MidiUtils$TempoCache.class */
    public static final class TempoCache {
        long[] ticks;
        int[] tempos;
        int snapshotIndex;
        int snapshotMicro;
        int currTempo;
        private boolean firstTempoIsFake;

        public TempoCache() {
            this.snapshotIndex = 0;
            this.snapshotMicro = 0;
            this.firstTempoIsFake = false;
            this.ticks = new long[1];
            this.tempos = new int[1];
            this.tempos[0] = 500000;
            this.snapshotIndex = 0;
            this.snapshotMicro = 0;
        }

        public TempoCache(Sequence sequence) throws ArrayIndexOutOfBoundsException {
            this();
            refresh(sequence);
        }

        public synchronized void refresh(Sequence sequence) throws ArrayIndexOutOfBoundsException {
            ArrayList arrayList = new ArrayList();
            Track[] tracks = sequence.getTracks();
            if (tracks.length > 0) {
                Track track = tracks[0];
                int size = track.size();
                for (int i2 = 0; i2 < size; i2++) {
                    MidiEvent midiEvent = track.get(i2);
                    if (MidiUtils.isMetaTempo(midiEvent.getMessage())) {
                        arrayList.add(midiEvent);
                    }
                }
            }
            int size2 = arrayList.size() + 1;
            this.firstTempoIsFake = true;
            if (size2 > 1 && ((MidiEvent) arrayList.get(0)).getTick() == 0) {
                size2--;
                this.firstTempoIsFake = false;
            }
            this.ticks = new long[size2];
            this.tempos = new int[size2];
            int i3 = 0;
            if (this.firstTempoIsFake) {
                this.ticks[0] = 0;
                this.tempos[0] = 500000;
                i3 = 0 + 1;
            }
            int i4 = 0;
            while (i4 < arrayList.size()) {
                MidiEvent midiEvent2 = (MidiEvent) arrayList.get(i4);
                this.ticks[i3] = midiEvent2.getTick();
                this.tempos[i3] = MidiUtils.getTempoMPQ(midiEvent2.getMessage());
                i4++;
                i3++;
            }
            this.snapshotIndex = 0;
            this.snapshotMicro = 0;
        }

        public int getCurrTempoMPQ() {
            return this.currTempo;
        }

        float getTempoMPQAt(long j2) {
            return getTempoMPQAt(j2, -1.0f);
        }

        synchronized float getTempoMPQAt(long j2, float f2) {
            int i2 = 0;
            while (i2 < this.ticks.length) {
                if (this.ticks[i2] <= j2) {
                    i2++;
                } else {
                    if (i2 > 0) {
                        i2--;
                    }
                    if (f2 > 0.0f && i2 == 0 && this.firstTempoIsFake) {
                        return f2;
                    }
                    return this.tempos[i2];
                }
            }
            return this.tempos[this.tempos.length - 1];
        }
    }
}
