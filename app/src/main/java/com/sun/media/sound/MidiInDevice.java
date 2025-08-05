package com.sun.media.sound;

import com.sun.media.sound.AbstractMidiDevice;
import com.sun.media.sound.AbstractMidiDeviceProvider;
import com.sun.media.sound.MidiInDeviceProvider;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

/* loaded from: rt.jar:com/sun/media/sound/MidiInDevice.class */
final class MidiInDevice extends AbstractMidiDevice implements Runnable {
    private volatile Thread midiInThread;

    private native long nOpen(int i2) throws MidiUnavailableException;

    private native void nClose(long j2);

    private native void nStart(long j2) throws MidiUnavailableException;

    private native void nStop(long j2);

    private native long nGetTimeStamp(long j2);

    private native void nGetMessages(long j2);

    MidiInDevice(AbstractMidiDeviceProvider.Info info) {
        super(info);
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected synchronized void implOpen() throws MidiUnavailableException {
        this.id = nOpen(((MidiInDeviceProvider.MidiInDeviceInfo) getDeviceInfo()).getIndex());
        if (this.id == 0) {
            throw new MidiUnavailableException("Unable to open native device");
        }
        if (this.midiInThread == null) {
            this.midiInThread = JSSecurityManager.createThread(this, "Java Sound MidiInDevice Thread", false, -1, true);
        }
        nStart(this.id);
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected synchronized void implClose() {
        long j2 = this.id;
        this.id = 0L;
        super.implClose();
        nStop(j2);
        if (this.midiInThread != null) {
            try {
                this.midiInThread.join(1000L);
            } catch (InterruptedException e2) {
            }
        }
        nClose(j2);
    }

    @Override // com.sun.media.sound.AbstractMidiDevice, javax.sound.midi.MidiDevice
    public long getMicrosecondPosition() {
        long jNGetTimeStamp = -1;
        if (isOpen()) {
            jNGetTimeStamp = nGetTimeStamp(this.id);
        }
        return jNGetTimeStamp;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected boolean hasTransmitters() {
        return true;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected Transmitter createTransmitter() {
        return new MidiInTransmitter();
    }

    /* loaded from: rt.jar:com/sun/media/sound/MidiInDevice$MidiInTransmitter.class */
    private final class MidiInTransmitter extends AbstractMidiDevice.BasicTransmitter {
        private MidiInTransmitter() {
            super();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        while (this.id != 0) {
            nGetMessages(this.id);
            if (this.id != 0) {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e2) {
                }
            }
        }
        this.midiInThread = null;
    }

    void callbackShortMessage(int i2, long j2) {
        if (i2 == 0 || this.id == 0) {
            return;
        }
        getTransmitterList().sendMessage(i2, j2);
    }

    void callbackLongMessage(byte[] bArr, long j2) {
        if (this.id == 0 || bArr == null) {
            return;
        }
        getTransmitterList().sendMessage(bArr, j2);
    }
}
