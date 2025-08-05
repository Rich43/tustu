package javax.sound.midi;

import com.sun.media.sound.MidiUtils;
import java.util.ArrayList;
import java.util.HashSet;

/* loaded from: rt.jar:javax/sound/midi/Track.class */
public class Track {
    private ArrayList eventsList = new ArrayList();
    private HashSet set = new HashSet();
    private MidiEvent eotEvent = new MidiEvent(new ImmutableEndOfTrack(), 0);

    Track() {
        this.eventsList.add(this.eotEvent);
        this.set.add(this.eotEvent);
    }

    public boolean add(MidiEvent midiEvent) {
        if (midiEvent == null) {
            return false;
        }
        synchronized (this.eventsList) {
            if (!this.set.contains(midiEvent)) {
                int size = this.eventsList.size();
                MidiEvent midiEvent2 = null;
                if (size > 0) {
                    midiEvent2 = (MidiEvent) this.eventsList.get(size - 1);
                }
                if (midiEvent2 != this.eotEvent) {
                    if (midiEvent2 != null) {
                        this.eotEvent.setTick(midiEvent2.getTick());
                    } else {
                        this.eotEvent.setTick(0L);
                    }
                    this.eventsList.add(this.eotEvent);
                    this.set.add(this.eotEvent);
                    size = this.eventsList.size();
                }
                if (MidiUtils.isMetaEndOfTrack(midiEvent.getMessage())) {
                    if (midiEvent.getTick() > this.eotEvent.getTick()) {
                        this.eotEvent.setTick(midiEvent.getTick());
                    }
                    return true;
                }
                this.set.add(midiEvent);
                int i2 = size;
                while (i2 > 0 && midiEvent.getTick() < ((MidiEvent) this.eventsList.get(i2 - 1)).getTick()) {
                    i2--;
                }
                if (i2 == size) {
                    this.eventsList.set(size - 1, midiEvent);
                    if (this.eotEvent.getTick() < midiEvent.getTick()) {
                        this.eotEvent.setTick(midiEvent.getTick());
                    }
                    this.eventsList.add(this.eotEvent);
                } else {
                    this.eventsList.add(i2, midiEvent);
                }
                return true;
            }
            return false;
        }
    }

    public boolean remove(MidiEvent midiEvent) {
        int iIndexOf;
        synchronized (this.eventsList) {
            if (this.set.remove(midiEvent) && (iIndexOf = this.eventsList.indexOf(midiEvent)) >= 0) {
                this.eventsList.remove(iIndexOf);
                return true;
            }
            return false;
        }
    }

    public MidiEvent get(int i2) throws ArrayIndexOutOfBoundsException {
        MidiEvent midiEvent;
        try {
            synchronized (this.eventsList) {
                midiEvent = (MidiEvent) this.eventsList.get(i2);
            }
            return midiEvent;
        } catch (IndexOutOfBoundsException e2) {
            throw new ArrayIndexOutOfBoundsException(e2.getMessage());
        }
    }

    public int size() {
        int size;
        synchronized (this.eventsList) {
            size = this.eventsList.size();
        }
        return size;
    }

    public long ticks() {
        long tick = 0;
        synchronized (this.eventsList) {
            if (this.eventsList.size() > 0) {
                tick = ((MidiEvent) this.eventsList.get(this.eventsList.size() - 1)).getTick();
            }
        }
        return tick;
    }

    /* loaded from: rt.jar:javax/sound/midi/Track$ImmutableEndOfTrack.class */
    private static class ImmutableEndOfTrack extends MetaMessage {
        private ImmutableEndOfTrack() {
            super(new byte[3]);
            this.data[0] = -1;
            this.data[1] = 47;
            this.data[2] = 0;
        }

        @Override // javax.sound.midi.MetaMessage
        public void setMessage(int i2, byte[] bArr, int i3) throws InvalidMidiDataException {
            throw new InvalidMidiDataException("cannot modify end of track message");
        }
    }
}
