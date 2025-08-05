package com.sun.media.sound;

import com.sun.media.sound.AbstractMidiDevice;
import com.sun.media.sound.AbstractMidiDeviceProvider;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/* loaded from: rt.jar:com/sun/media/sound/MidiOutDevice.class */
final class MidiOutDevice extends AbstractMidiDevice {
    private native long nOpen(int i2) throws MidiUnavailableException;

    private native void nClose(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nSendShortMessage(long j2, int i2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public native void nSendLongMessage(long j2, byte[] bArr, int i2, long j3);

    private native long nGetTimeStamp(long j2);

    MidiOutDevice(AbstractMidiDeviceProvider.Info info) {
        super(info);
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected synchronized void implOpen() throws MidiUnavailableException {
        this.id = nOpen(((AbstractMidiDeviceProvider.Info) getDeviceInfo()).getIndex());
        if (this.id == 0) {
            throw new MidiUnavailableException("Unable to open native device");
        }
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected synchronized void implClose() {
        long j2 = this.id;
        this.id = 0L;
        super.implClose();
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
    protected boolean hasReceivers() {
        return true;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected Receiver createReceiver() {
        return new MidiOutReceiver();
    }

    /* loaded from: rt.jar:com/sun/media/sound/MidiOutDevice$MidiOutReceiver.class */
    final class MidiOutReceiver extends AbstractMidiDevice.AbstractReceiver {
        MidiOutReceiver() {
            super();
        }

        @Override // com.sun.media.sound.AbstractMidiDevice.AbstractReceiver
        void implSend(MidiMessage midiMessage, long j2) {
            byte[] message;
            int data1;
            int length = midiMessage.getLength();
            int status = midiMessage.getStatus();
            if (length <= 3 && status != 240 && status != 247) {
                if (midiMessage instanceof ShortMessage) {
                    if (midiMessage instanceof FastShortMessage) {
                        data1 = ((FastShortMessage) midiMessage).getPackedMsg();
                    } else {
                        ShortMessage shortMessage = (ShortMessage) midiMessage;
                        data1 = (status & 255) | ((shortMessage.getData1() & 255) << 8) | ((shortMessage.getData2() & 255) << 16);
                    }
                } else {
                    data1 = 0;
                    byte[] message2 = midiMessage.getMessage();
                    if (length > 0) {
                        data1 = message2[0] & 255;
                        if (length > 1) {
                            if (status == 255) {
                                return;
                            }
                            data1 |= (message2[1] & 255) << 8;
                            if (length > 2) {
                                data1 |= (message2[2] & 255) << 16;
                            }
                        }
                    }
                }
                MidiOutDevice.this.nSendShortMessage(MidiOutDevice.this.id, data1, j2);
                return;
            }
            if (midiMessage instanceof FastSysexMessage) {
                message = ((FastSysexMessage) midiMessage).getReadOnlyMessage();
            } else {
                message = midiMessage.getMessage();
            }
            int iMin = Math.min(length, message.length);
            if (iMin > 0) {
                MidiOutDevice.this.nSendLongMessage(MidiOutDevice.this.id, message, iMin, j2);
            }
        }

        synchronized void sendPackedMidiMessage(int i2, long j2) {
            if (isOpen() && MidiOutDevice.this.id != 0) {
                MidiOutDevice.this.nSendShortMessage(MidiOutDevice.this.id, i2, j2);
            }
        }
    }
}
