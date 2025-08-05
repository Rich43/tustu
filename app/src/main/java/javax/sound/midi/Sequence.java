package javax.sound.midi;

import com.sun.media.sound.MidiUtils;
import java.util.Vector;

/* loaded from: rt.jar:javax/sound/midi/Sequence.class */
public class Sequence {
    public static final float PPQ = 0.0f;
    public static final float SMPTE_24 = 24.0f;
    public static final float SMPTE_25 = 25.0f;
    public static final float SMPTE_30DROP = 29.97f;
    public static final float SMPTE_30 = 30.0f;
    protected float divisionType;
    protected int resolution;
    protected Vector<Track> tracks = new Vector<>();

    public Sequence(float f2, int i2) throws InvalidMidiDataException {
        if (f2 == 0.0f) {
            this.divisionType = 0.0f;
        } else if (f2 == 24.0f) {
            this.divisionType = 24.0f;
        } else if (f2 == 25.0f) {
            this.divisionType = 25.0f;
        } else if (f2 == 29.97f) {
            this.divisionType = 29.97f;
        } else if (f2 == 30.0f) {
            this.divisionType = 30.0f;
        } else {
            throw new InvalidMidiDataException("Unsupported division type: " + f2);
        }
        this.resolution = i2;
    }

    public Sequence(float f2, int i2, int i3) throws InvalidMidiDataException {
        if (f2 == 0.0f) {
            this.divisionType = 0.0f;
        } else if (f2 == 24.0f) {
            this.divisionType = 24.0f;
        } else if (f2 == 25.0f) {
            this.divisionType = 25.0f;
        } else if (f2 == 29.97f) {
            this.divisionType = 29.97f;
        } else if (f2 == 30.0f) {
            this.divisionType = 30.0f;
        } else {
            throw new InvalidMidiDataException("Unsupported division type: " + f2);
        }
        this.resolution = i2;
        for (int i4 = 0; i4 < i3; i4++) {
            this.tracks.addElement(new Track());
        }
    }

    public float getDivisionType() {
        return this.divisionType;
    }

    public int getResolution() {
        return this.resolution;
    }

    public Track createTrack() {
        Track track = new Track();
        this.tracks.addElement(track);
        return track;
    }

    public boolean deleteTrack(Track track) {
        return this.tracks.removeElement(track);
    }

    public Track[] getTracks() {
        return (Track[]) this.tracks.toArray(new Track[0]);
    }

    public long getMicrosecondLength() {
        return MidiUtils.tick2microsecond(this, getTickLength(), null);
    }

    public long getTickLength() {
        long j2;
        long j3 = 0;
        synchronized (this.tracks) {
            for (int i2 = 0; i2 < this.tracks.size(); i2++) {
                long jTicks = this.tracks.elementAt(i2).ticks();
                if (jTicks > j3) {
                    j3 = jTicks;
                }
            }
            j2 = j3;
        }
        return j2;
    }

    public Patch[] getPatchList() {
        return new Patch[0];
    }
}
