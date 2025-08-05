package com.sun.media.sound;

import com.sun.media.sound.MidiOutDevice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDeviceReceiver;
import javax.sound.midi.MidiDeviceTransmitter;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/* loaded from: rt.jar:com/sun/media/sound/AbstractMidiDevice.class */
abstract class AbstractMidiDevice implements MidiDevice, ReferenceCountingDevice {
    private static final boolean TRACE_TRANSMITTER = false;
    private ArrayList<Receiver> receiverList;
    private TransmitterList transmitterList;
    private final MidiDevice.Info info;
    private volatile boolean open;
    private List openKeepingObjects;
    protected volatile long id;
    private final Object traRecLock = new Object();
    private int openRefCount = 0;

    protected abstract void implOpen() throws MidiUnavailableException;

    protected AbstractMidiDevice(MidiDevice.Info info) {
        this.info = info;
    }

    @Override // javax.sound.midi.MidiDevice
    public final MidiDevice.Info getDeviceInfo() {
        return this.info;
    }

    @Override // javax.sound.midi.MidiDevice
    public final void open() throws MidiUnavailableException {
        synchronized (this) {
            this.openRefCount = -1;
            doOpen();
        }
    }

    private void openInternal(Object obj) throws MidiUnavailableException {
        synchronized (this) {
            if (this.openRefCount != -1) {
                this.openRefCount++;
                getOpenKeepingObjects().add(obj);
            }
            doOpen();
        }
    }

    private void doOpen() throws MidiUnavailableException {
        synchronized (this) {
            if (!isOpen()) {
                implOpen();
                this.open = true;
            }
        }
    }

    @Override // javax.sound.midi.MidiDevice, java.lang.AutoCloseable
    public final void close() {
        synchronized (this) {
            doClose();
            this.openRefCount = 0;
        }
    }

    public final void closeInternal(Object obj) {
        synchronized (this) {
            if (getOpenKeepingObjects().remove(obj) && this.openRefCount > 0) {
                this.openRefCount--;
                if (this.openRefCount == 0) {
                    doClose();
                }
            }
        }
    }

    public final void doClose() {
        synchronized (this) {
            if (isOpen()) {
                implClose();
                this.open = false;
            }
        }
    }

    @Override // javax.sound.midi.MidiDevice
    public final boolean isOpen() {
        return this.open;
    }

    protected void implClose() {
        synchronized (this.traRecLock) {
            if (this.receiverList != null) {
                for (int i2 = 0; i2 < this.receiverList.size(); i2++) {
                    this.receiverList.get(i2).close();
                }
                this.receiverList.clear();
            }
            if (this.transmitterList != null) {
                this.transmitterList.close();
            }
        }
    }

    @Override // javax.sound.midi.MidiDevice
    public long getMicrosecondPosition() {
        return -1L;
    }

    @Override // javax.sound.midi.MidiDevice
    public final int getMaxReceivers() {
        if (hasReceivers()) {
            return -1;
        }
        return 0;
    }

    @Override // javax.sound.midi.MidiDevice
    public final int getMaxTransmitters() {
        if (hasTransmitters()) {
            return -1;
        }
        return 0;
    }

    @Override // javax.sound.midi.MidiDevice
    public final Receiver getReceiver() throws MidiUnavailableException {
        Receiver receiverCreateReceiver;
        synchronized (this.traRecLock) {
            receiverCreateReceiver = createReceiver();
            getReceiverList().add(receiverCreateReceiver);
        }
        return receiverCreateReceiver;
    }

    @Override // javax.sound.midi.MidiDevice
    public final List<Receiver> getReceivers() {
        List<Receiver> listUnmodifiableList;
        synchronized (this.traRecLock) {
            if (this.receiverList == null) {
                listUnmodifiableList = Collections.unmodifiableList(new ArrayList(0));
            } else {
                listUnmodifiableList = Collections.unmodifiableList((List) this.receiverList.clone());
            }
        }
        return listUnmodifiableList;
    }

    @Override // javax.sound.midi.MidiDevice
    public final Transmitter getTransmitter() throws MidiUnavailableException {
        Transmitter transmitterCreateTransmitter;
        synchronized (this.traRecLock) {
            transmitterCreateTransmitter = createTransmitter();
            getTransmitterList().add(transmitterCreateTransmitter);
        }
        return transmitterCreateTransmitter;
    }

    @Override // javax.sound.midi.MidiDevice
    public final List<Transmitter> getTransmitters() {
        List<Transmitter> listUnmodifiableList;
        synchronized (this.traRecLock) {
            if (this.transmitterList == null || this.transmitterList.transmitters.size() == 0) {
                listUnmodifiableList = Collections.unmodifiableList(new ArrayList(0));
            } else {
                listUnmodifiableList = Collections.unmodifiableList((List) this.transmitterList.transmitters.clone());
            }
        }
        return listUnmodifiableList;
    }

    final long getId() {
        return this.id;
    }

    @Override // com.sun.media.sound.ReferenceCountingDevice
    public final Receiver getReceiverReferenceCounting() throws MidiUnavailableException {
        Receiver receiver;
        synchronized (this.traRecLock) {
            receiver = getReceiver();
            openInternal(receiver);
        }
        return receiver;
    }

    @Override // com.sun.media.sound.ReferenceCountingDevice
    public final Transmitter getTransmitterReferenceCounting() throws MidiUnavailableException {
        Transmitter transmitter;
        synchronized (this.traRecLock) {
            transmitter = getTransmitter();
            openInternal(transmitter);
        }
        return transmitter;
    }

    private synchronized List getOpenKeepingObjects() {
        if (this.openKeepingObjects == null) {
            this.openKeepingObjects = new ArrayList();
        }
        return this.openKeepingObjects;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<Receiver> getReceiverList() {
        synchronized (this.traRecLock) {
            if (this.receiverList == null) {
                this.receiverList = new ArrayList<>();
            }
        }
        return this.receiverList;
    }

    protected boolean hasReceivers() {
        return false;
    }

    protected Receiver createReceiver() throws MidiUnavailableException {
        throw new MidiUnavailableException("MIDI IN receiver not available");
    }

    final TransmitterList getTransmitterList() {
        synchronized (this.traRecLock) {
            if (this.transmitterList == null) {
                this.transmitterList = new TransmitterList();
            }
        }
        return this.transmitterList;
    }

    protected boolean hasTransmitters() {
        return false;
    }

    protected Transmitter createTransmitter() throws MidiUnavailableException {
        throw new MidiUnavailableException("MIDI OUT transmitter not available");
    }

    protected final void finalize() {
        close();
    }

    /* loaded from: rt.jar:com/sun/media/sound/AbstractMidiDevice$AbstractReceiver.class */
    abstract class AbstractReceiver implements MidiDeviceReceiver {
        private volatile boolean open = true;

        abstract void implSend(MidiMessage midiMessage, long j2);

        AbstractReceiver() {
        }

        @Override // javax.sound.midi.Receiver
        public final synchronized void send(MidiMessage midiMessage, long j2) {
            if (!this.open) {
                throw new IllegalStateException("Receiver is not open");
            }
            implSend(midiMessage, j2);
        }

        @Override // javax.sound.midi.Receiver, java.lang.AutoCloseable
        public final void close() {
            this.open = false;
            synchronized (AbstractMidiDevice.this.traRecLock) {
                AbstractMidiDevice.this.getReceiverList().remove(this);
            }
            AbstractMidiDevice.this.closeInternal(this);
        }

        @Override // javax.sound.midi.MidiDeviceReceiver
        public final MidiDevice getMidiDevice() {
            return AbstractMidiDevice.this;
        }

        final boolean isOpen() {
            return this.open;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AbstractMidiDevice$BasicTransmitter.class */
    class BasicTransmitter implements MidiDeviceTransmitter {
        private Receiver receiver = null;
        TransmitterList tlist = null;

        protected BasicTransmitter() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTransmitterList(TransmitterList transmitterList) {
            this.tlist = transmitterList;
        }

        @Override // javax.sound.midi.Transmitter
        public final void setReceiver(Receiver receiver) {
            if (this.tlist == null || this.receiver == receiver) {
                return;
            }
            this.tlist.receiverChanged(this, this.receiver, receiver);
            this.receiver = receiver;
        }

        @Override // javax.sound.midi.Transmitter
        public final Receiver getReceiver() {
            return this.receiver;
        }

        @Override // javax.sound.midi.Transmitter, java.lang.AutoCloseable
        public final void close() {
            AbstractMidiDevice.this.closeInternal(this);
            if (this.tlist == null) {
                return;
            }
            this.tlist.receiverChanged(this, this.receiver, null);
            this.tlist.remove(this);
            this.tlist = null;
        }

        @Override // javax.sound.midi.MidiDeviceTransmitter
        public final MidiDevice getMidiDevice() {
            return AbstractMidiDevice.this;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/AbstractMidiDevice$TransmitterList.class */
    final class TransmitterList {
        private MidiOutDevice.MidiOutReceiver midiOutReceiver;
        private final ArrayList<Transmitter> transmitters = new ArrayList<>();
        private int optimizedReceiverCount = 0;

        TransmitterList() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void add(Transmitter transmitter) {
            synchronized (this.transmitters) {
                this.transmitters.add(transmitter);
            }
            if (transmitter instanceof BasicTransmitter) {
                ((BasicTransmitter) transmitter).setTransmitterList(this);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void remove(Transmitter transmitter) {
            synchronized (this.transmitters) {
                int iIndexOf = this.transmitters.indexOf(transmitter);
                if (iIndexOf >= 0) {
                    this.transmitters.remove(iIndexOf);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void receiverChanged(BasicTransmitter basicTransmitter, Receiver receiver, Receiver receiver2) {
            synchronized (this.transmitters) {
                if (this.midiOutReceiver == receiver) {
                    this.midiOutReceiver = null;
                }
                if (receiver2 != null && (receiver2 instanceof MidiOutDevice.MidiOutReceiver) && this.midiOutReceiver == null) {
                    this.midiOutReceiver = (MidiOutDevice.MidiOutReceiver) receiver2;
                }
                this.optimizedReceiverCount = this.midiOutReceiver != null ? 1 : 0;
            }
        }

        void close() {
            synchronized (this.transmitters) {
                for (int i2 = 0; i2 < this.transmitters.size(); i2++) {
                    this.transmitters.get(i2).close();
                }
                this.transmitters.clear();
            }
        }

        void sendMessage(int i2, long j2) {
            try {
                synchronized (this.transmitters) {
                    int size = this.transmitters.size();
                    if (this.optimizedReceiverCount == size) {
                        if (this.midiOutReceiver != null) {
                            this.midiOutReceiver.sendPackedMidiMessage(i2, j2);
                        }
                    } else {
                        for (int i3 = 0; i3 < size; i3++) {
                            Receiver receiver = this.transmitters.get(i3).getReceiver();
                            if (receiver != null) {
                                if (this.optimizedReceiverCount > 0) {
                                    if (receiver instanceof MidiOutDevice.MidiOutReceiver) {
                                        ((MidiOutDevice.MidiOutReceiver) receiver).sendPackedMidiMessage(i2, j2);
                                    } else {
                                        receiver.send(new FastShortMessage(i2), j2);
                                    }
                                } else {
                                    receiver.send(new FastShortMessage(i2), j2);
                                }
                            }
                        }
                    }
                }
            } catch (InvalidMidiDataException e2) {
            }
        }

        void sendMessage(byte[] bArr, long j2) {
            try {
                synchronized (this.transmitters) {
                    int size = this.transmitters.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        Receiver receiver = this.transmitters.get(i2).getReceiver();
                        if (receiver != null) {
                            receiver.send(new FastSysexMessage(bArr), j2);
                        }
                    }
                }
            } catch (InvalidMidiDataException e2) {
            }
        }

        void sendMessage(MidiMessage midiMessage, long j2) {
            if (midiMessage instanceof FastShortMessage) {
                sendMessage(((FastShortMessage) midiMessage).getPackedMsg(), j2);
                return;
            }
            synchronized (this.transmitters) {
                int size = this.transmitters.size();
                if (this.optimizedReceiverCount == size) {
                    if (this.midiOutReceiver != null) {
                        this.midiOutReceiver.send(midiMessage, j2);
                    }
                } else {
                    for (int i2 = 0; i2 < size; i2++) {
                        Receiver receiver = this.transmitters.get(i2).getReceiver();
                        if (receiver != null) {
                            receiver.send(midiMessage, j2);
                        }
                    }
                }
            }
        }
    }
}
