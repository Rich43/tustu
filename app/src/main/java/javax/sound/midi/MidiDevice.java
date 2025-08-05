package javax.sound.midi;

import java.util.List;

/* loaded from: rt.jar:javax/sound/midi/MidiDevice.class */
public interface MidiDevice extends AutoCloseable {
    Info getDeviceInfo();

    void open() throws MidiUnavailableException;

    @Override // java.lang.AutoCloseable
    void close();

    boolean isOpen();

    long getMicrosecondPosition();

    int getMaxReceivers();

    int getMaxTransmitters();

    Receiver getReceiver() throws MidiUnavailableException;

    List<Receiver> getReceivers();

    Transmitter getTransmitter() throws MidiUnavailableException;

    List<Transmitter> getTransmitters();

    /* loaded from: rt.jar:javax/sound/midi/MidiDevice$Info.class */
    public static class Info {
        private String name;
        private String vendor;
        private String description;
        private String version;

        protected Info(String str, String str2, String str3, String str4) {
            this.name = str;
            this.vendor = str2;
            this.description = str3;
            this.version = str4;
        }

        public final boolean equals(Object obj) {
            return super.equals(obj);
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public final String getName() {
            return this.name;
        }

        public final String getVendor() {
            return this.vendor;
        }

        public final String getDescription() {
            return this.description;
        }

        public final String getVersion() {
            return this.version;
        }

        public final String toString() {
            return this.name;
        }
    }
}
