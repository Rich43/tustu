package com.sun.media.sound;

import com.sun.media.sound.AbstractMidiDevice;
import com.sun.media.sound.MidiUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:58)
    */
/* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer.class */
final class RealTimeSequencer extends AbstractMidiDevice implements Sequencer, AutoConnectSequencer {
    private static final boolean DEBUG_PUMP = false;
    private static final boolean DEBUG_PUMP_ALL = false;
    private static final Map<ThreadGroup, EventDispatcher> dispatchers = new WeakHashMap();
    static final RealTimeSequencerInfo info = new RealTimeSequencerInfo(null);
    private static final Sequencer.SyncMode[] masterSyncModes = {Sequencer.SyncMode.INTERNAL_CLOCK};
    private static final Sequencer.SyncMode[] slaveSyncModes = {Sequencer.SyncMode.NO_SYNC};
    private static final Sequencer.SyncMode masterSyncMode = Sequencer.SyncMode.INTERNAL_CLOCK;
    private static final Sequencer.SyncMode slaveSyncMode = Sequencer.SyncMode.NO_SYNC;
    private Sequence sequence;
    private double cacheTempoMPQ;
    private float cacheTempoFactor;
    private boolean[] trackMuted;
    private boolean[] trackSolo;
    private final MidiUtils.TempoCache tempoCache;
    private volatile boolean running;
    private PlayThread playThread;
    private volatile boolean recording;
    private final List recordingTracks;
    private long loopStart;
    private long loopEnd;
    private int loopCount;
    private final ArrayList metaEventListeners;
    private final ArrayList controllerEventListeners;
    private boolean autoConnect;
    private boolean doAutoConnectAtNextOpen;
    Receiver autoConnectedReceiver;

    static {
    }

    RealTimeSequencer() throws MidiUnavailableException {
        super(info);
        this.sequence = null;
        this.cacheTempoMPQ = -1.0d;
        this.cacheTempoFactor = -1.0f;
        this.trackMuted = null;
        this.trackSolo = null;
        this.tempoCache = new MidiUtils.TempoCache();
        this.recordingTracks = new ArrayList();
        this.loopStart = 0L;
        this.loopEnd = -1L;
        this.loopCount = 0;
        this.metaEventListeners = new ArrayList();
        this.controllerEventListeners = new ArrayList();
        this.autoConnect = false;
        this.doAutoConnectAtNextOpen = false;
        this.autoConnectedReceiver = null;
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void setSequence(Sequence sequence) throws InvalidMidiDataException, ArrayIndexOutOfBoundsException {
        if (sequence != this.sequence) {
            if (this.sequence != null && sequence == null) {
                setCaches();
                stop();
                this.trackMuted = null;
                this.trackSolo = null;
                this.loopStart = 0L;
                this.loopEnd = -1L;
                this.loopCount = 0;
                if (getDataPump() != null) {
                    getDataPump().setTickPos(0L);
                    getDataPump().resetLoopCount();
                }
            }
            if (this.playThread != null) {
                this.playThread.setSequence(sequence);
            }
            this.sequence = sequence;
            if (sequence != null) {
                this.tempoCache.refresh(sequence);
                setTickPosition(0L);
                propagateCaches();
                return;
            }
            return;
        }
        if (sequence != null) {
            this.tempoCache.refresh(sequence);
            if (this.playThread != null) {
                this.playThread.setSequence(sequence);
            }
        }
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void setSequence(InputStream inputStream) throws InvalidMidiDataException, IOException, ArrayIndexOutOfBoundsException {
        if (inputStream == null) {
            setSequence((Sequence) null);
        } else {
            setSequence(MidiSystem.getSequence(inputStream));
        }
    }

    @Override // javax.sound.midi.Sequencer
    public Sequence getSequence() {
        return this.sequence;
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void start() throws ArrayIndexOutOfBoundsException {
        if (!isOpen()) {
            throw new IllegalStateException("sequencer not open");
        }
        if (this.sequence == null) {
            throw new IllegalStateException("sequence not set");
        }
        if (this.running) {
            return;
        }
        implStart();
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void stop() {
        if (!isOpen()) {
            throw new IllegalStateException("sequencer not open");
        }
        stopRecording();
        if (!this.running) {
            return;
        }
        implStop();
    }

    @Override // javax.sound.midi.Sequencer
    public boolean isRunning() {
        return this.running;
    }

    @Override // javax.sound.midi.Sequencer
    public void startRecording() throws ArrayIndexOutOfBoundsException {
        if (!isOpen()) {
            throw new IllegalStateException("Sequencer not open");
        }
        start();
        this.recording = true;
    }

    @Override // javax.sound.midi.Sequencer
    public void stopRecording() {
        if (!isOpen()) {
            throw new IllegalStateException("Sequencer not open");
        }
        this.recording = false;
    }

    @Override // javax.sound.midi.Sequencer
    public boolean isRecording() {
        return this.recording;
    }

    @Override // javax.sound.midi.Sequencer
    public void recordEnable(Track track, int i2) {
        if (!findTrack(track)) {
            throw new IllegalArgumentException("Track does not exist in the current sequence");
        }
        synchronized (this.recordingTracks) {
            RecordingTrack recordingTrack = RecordingTrack.get(this.recordingTracks, track);
            if (recordingTrack == null) {
                this.recordingTracks.add(new RecordingTrack(track, i2));
            } else {
                recordingTrack.channel = i2;
            }
        }
    }

    @Override // javax.sound.midi.Sequencer
    public void recordDisable(Track track) {
        synchronized (this.recordingTracks) {
            RecordingTrack recordingTrack = RecordingTrack.get(this.recordingTracks, track);
            if (recordingTrack != null) {
                this.recordingTracks.remove(recordingTrack);
            }
        }
    }

    private boolean findTrack(Track track) {
        boolean z2 = false;
        if (this.sequence != null) {
            Track[] tracks = this.sequence.getTracks();
            int i2 = 0;
            while (true) {
                if (i2 >= tracks.length) {
                    break;
                }
                if (track != tracks[i2]) {
                    i2++;
                } else {
                    z2 = true;
                    break;
                }
            }
        }
        return z2;
    }

    @Override // javax.sound.midi.Sequencer
    public float getTempoInBPM() {
        return (float) MidiUtils.convertTempo(getTempoInMPQ());
    }

    @Override // javax.sound.midi.Sequencer
    public void setTempoInBPM(float f2) {
        if (f2 <= 0.0f) {
            f2 = 1.0f;
        }
        setTempoInMPQ((float) MidiUtils.convertTempo(f2));
    }

    @Override // javax.sound.midi.Sequencer
    public float getTempoInMPQ() {
        if (!needCaching()) {
            return getDataPump().getTempoMPQ();
        }
        if (this.cacheTempoMPQ != -1.0d) {
            return (float) this.cacheTempoMPQ;
        }
        if (this.sequence != null) {
            return this.tempoCache.getTempoMPQAt(getTickPosition());
        }
        return 500000.0f;
    }

    @Override // javax.sound.midi.Sequencer
    public void setTempoInMPQ(float f2) {
        if (f2 <= 0.0f) {
            f2 = 1.0f;
        }
        if (needCaching()) {
            this.cacheTempoMPQ = f2;
        } else {
            getDataPump().setTempoMPQ(f2);
            this.cacheTempoMPQ = -1.0d;
        }
    }

    @Override // javax.sound.midi.Sequencer
    public void setTempoFactor(float f2) {
        if (f2 <= 0.0f) {
            return;
        }
        if (needCaching()) {
            this.cacheTempoFactor = f2;
        } else {
            getDataPump().setTempoFactor(f2);
            this.cacheTempoFactor = -1.0f;
        }
    }

    @Override // javax.sound.midi.Sequencer
    public float getTempoFactor() {
        if (!needCaching()) {
            return getDataPump().getTempoFactor();
        }
        if (this.cacheTempoFactor != -1.0f) {
            return this.cacheTempoFactor;
        }
        return 1.0f;
    }

    @Override // javax.sound.midi.Sequencer
    public long getTickLength() {
        if (this.sequence == null) {
            return 0L;
        }
        return this.sequence.getTickLength();
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized long getTickPosition() {
        if (getDataPump() == null || this.sequence == null) {
            return 0L;
        }
        return getDataPump().getTickPos();
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void setTickPosition(long j2) {
        if (j2 < 0) {
            return;
        }
        if (getDataPump() != null) {
            if (this.sequence != null) {
                getDataPump().setTickPos(j2);
                return;
            } else {
                if (j2 != 0) {
                }
                return;
            }
        }
        if (j2 != 0) {
        }
    }

    @Override // javax.sound.midi.Sequencer
    public long getMicrosecondLength() {
        if (this.sequence == null) {
            return 0L;
        }
        return this.sequence.getMicrosecondLength();
    }

    @Override // com.sun.media.sound.AbstractMidiDevice, javax.sound.midi.MidiDevice
    public long getMicrosecondPosition() {
        long jTick2microsecond;
        if (getDataPump() == null || this.sequence == null) {
            return 0L;
        }
        synchronized (this.tempoCache) {
            jTick2microsecond = MidiUtils.tick2microsecond(this.sequence, getDataPump().getTickPos(), this.tempoCache);
        }
        return jTick2microsecond;
    }

    @Override // javax.sound.midi.Sequencer
    public void setMicrosecondPosition(long j2) {
        if (j2 < 0) {
            return;
        }
        if (getDataPump() != null) {
            if (this.sequence != null) {
                synchronized (this.tempoCache) {
                    setTickPosition(MidiUtils.microsecond2tick(this.sequence, j2, this.tempoCache));
                }
                return;
            }
            if (j2 != 0) {
            }
            return;
        }
        if (j2 != 0) {
        }
    }

    @Override // javax.sound.midi.Sequencer
    public void setMasterSyncMode(Sequencer.SyncMode syncMode) {
    }

    @Override // javax.sound.midi.Sequencer
    public Sequencer.SyncMode getMasterSyncMode() {
        return masterSyncMode;
    }

    @Override // javax.sound.midi.Sequencer
    public Sequencer.SyncMode[] getMasterSyncModes() {
        Sequencer.SyncMode[] syncModeArr = new Sequencer.SyncMode[masterSyncModes.length];
        System.arraycopy(masterSyncModes, 0, syncModeArr, 0, masterSyncModes.length);
        return syncModeArr;
    }

    @Override // javax.sound.midi.Sequencer
    public void setSlaveSyncMode(Sequencer.SyncMode syncMode) {
    }

    @Override // javax.sound.midi.Sequencer
    public Sequencer.SyncMode getSlaveSyncMode() {
        return slaveSyncMode;
    }

    @Override // javax.sound.midi.Sequencer
    public Sequencer.SyncMode[] getSlaveSyncModes() {
        Sequencer.SyncMode[] syncModeArr = new Sequencer.SyncMode[slaveSyncModes.length];
        System.arraycopy(slaveSyncModes, 0, syncModeArr, 0, slaveSyncModes.length);
        return syncModeArr;
    }

    int getTrackCount() {
        if (getSequence() != null) {
            return this.sequence.getTracks().length;
        }
        return 0;
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void setTrackMute(int i2, boolean z2) {
        int trackCount = getTrackCount();
        if (i2 < 0 || i2 >= getTrackCount()) {
            return;
        }
        this.trackMuted = ensureBoolArraySize(this.trackMuted, trackCount);
        this.trackMuted[i2] = z2;
        if (getDataPump() != null) {
            getDataPump().muteSoloChanged();
        }
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized boolean getTrackMute(int i2) {
        if (i2 < 0 || i2 >= getTrackCount() || this.trackMuted == null || this.trackMuted.length <= i2) {
            return false;
        }
        return this.trackMuted[i2];
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized void setTrackSolo(int i2, boolean z2) {
        int trackCount = getTrackCount();
        if (i2 < 0 || i2 >= getTrackCount()) {
            return;
        }
        this.trackSolo = ensureBoolArraySize(this.trackSolo, trackCount);
        this.trackSolo[i2] = z2;
        if (getDataPump() != null) {
            getDataPump().muteSoloChanged();
        }
    }

    @Override // javax.sound.midi.Sequencer
    public synchronized boolean getTrackSolo(int i2) {
        if (i2 < 0 || i2 >= getTrackCount() || this.trackSolo == null || this.trackSolo.length <= i2) {
            return false;
        }
        return this.trackSolo[i2];
    }

    @Override // javax.sound.midi.Sequencer
    public boolean addMetaEventListener(MetaEventListener metaEventListener) {
        synchronized (this.metaEventListeners) {
            if (!this.metaEventListeners.contains(metaEventListener)) {
                this.metaEventListeners.add(metaEventListener);
            }
        }
        return true;
    }

    @Override // javax.sound.midi.Sequencer
    public void removeMetaEventListener(MetaEventListener metaEventListener) {
        synchronized (this.metaEventListeners) {
            int iIndexOf = this.metaEventListeners.indexOf(metaEventListener);
            if (iIndexOf >= 0) {
                this.metaEventListeners.remove(iIndexOf);
            }
        }
    }

    @Override // javax.sound.midi.Sequencer
    public int[] addControllerEventListener(ControllerEventListener controllerEventListener, int[] iArr) {
        int[] controllers;
        synchronized (this.controllerEventListeners) {
            ControllerListElement controllerListElement = null;
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= this.controllerEventListeners.size()) {
                    break;
                }
                controllerListElement = (ControllerListElement) this.controllerEventListeners.get(i2);
                if (!controllerListElement.listener.equals(controllerEventListener)) {
                    i2++;
                } else {
                    controllerListElement.addControllers(iArr);
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                controllerListElement = new ControllerListElement(controllerEventListener, iArr);
                this.controllerEventListeners.add(controllerListElement);
            }
            controllers = controllerListElement.getControllers();
        }
        return controllers;
    }

    @Override // javax.sound.midi.Sequencer
    public int[] removeControllerEventListener(ControllerEventListener controllerEventListener, int[] iArr) {
        synchronized (this.controllerEventListeners) {
            ControllerListElement controllerListElement = null;
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= this.controllerEventListeners.size()) {
                    break;
                }
                controllerListElement = (ControllerListElement) this.controllerEventListeners.get(i2);
                if (!controllerListElement.listener.equals(controllerEventListener)) {
                    i2++;
                } else {
                    controllerListElement.removeControllers(iArr);
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                return new int[0];
            }
            if (iArr == null) {
                int iIndexOf = this.controllerEventListeners.indexOf(controllerListElement);
                if (iIndexOf >= 0) {
                    this.controllerEventListeners.remove(iIndexOf);
                }
                return new int[0];
            }
            return controllerListElement.getControllers();
        }
    }

    @Override // javax.sound.midi.Sequencer
    public void setLoopStartPoint(long j2) {
        if (j2 > getTickLength() || ((this.loopEnd != -1 && j2 > this.loopEnd) || j2 < 0)) {
            throw new IllegalArgumentException("invalid loop start point: " + j2);
        }
        this.loopStart = j2;
    }

    @Override // javax.sound.midi.Sequencer
    public long getLoopStartPoint() {
        return this.loopStart;
    }

    @Override // javax.sound.midi.Sequencer
    public void setLoopEndPoint(long j2) {
        if (j2 > getTickLength() || ((this.loopStart > j2 && j2 != -1) || j2 < -1)) {
            throw new IllegalArgumentException("invalid loop end point: " + j2);
        }
        this.loopEnd = j2;
    }

    @Override // javax.sound.midi.Sequencer
    public long getLoopEndPoint() {
        return this.loopEnd;
    }

    @Override // javax.sound.midi.Sequencer
    public void setLoopCount(int i2) {
        if (i2 != -1 && i2 < 0) {
            throw new IllegalArgumentException("illegal value for loop count: " + i2);
        }
        this.loopCount = i2;
        if (getDataPump() != null) {
            getDataPump().resetLoopCount();
        }
    }

    @Override // javax.sound.midi.Sequencer
    public int getLoopCount() {
        return this.loopCount;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected void implOpen() throws MidiUnavailableException {
        this.playThread = new PlayThread(this);
        if (this.sequence != null) {
            this.playThread.setSequence(this.sequence);
        }
        propagateCaches();
        if (this.doAutoConnectAtNextOpen) {
            doAutoConnect();
        }
    }

    /* JADX WARN: Finally extract failed */
    private void doAutoConnect() {
        Receiver receiver = null;
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            if (synthesizer instanceof ReferenceCountingDevice) {
                receiver = ((ReferenceCountingDevice) synthesizer).getReceiverReferenceCounting();
            } else {
                synthesizer.open();
                try {
                    receiver = synthesizer.getReceiver();
                    if (receiver == null) {
                        synthesizer.close();
                    }
                } catch (Throwable th) {
                    if (receiver == null) {
                        synthesizer.close();
                    }
                    throw th;
                }
            }
        } catch (Exception e2) {
        }
        if (receiver == null) {
            try {
                receiver = MidiSystem.getReceiver();
            } catch (Exception e3) {
            }
        }
        if (receiver != null) {
            this.autoConnectedReceiver = receiver;
            try {
                getTransmitter().setReceiver(receiver);
            } catch (Exception e4) {
            }
        }
    }

    private synchronized void propagateCaches() {
        if (this.sequence == null || !isOpen()) {
            return;
        }
        if (this.cacheTempoFactor != -1.0f) {
            setTempoFactor(this.cacheTempoFactor);
        }
        if (this.cacheTempoMPQ == -1.0d) {
            setTempoInMPQ(new MidiUtils.TempoCache(this.sequence).getTempoMPQAt(getTickPosition()));
        } else {
            setTempoInMPQ((float) this.cacheTempoMPQ);
        }
    }

    private synchronized void setCaches() {
        this.cacheTempoFactor = getTempoFactor();
        this.cacheTempoMPQ = getTempoInMPQ();
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected synchronized void implClose() {
        if (this.playThread != null) {
            this.playThread.close();
            this.playThread = null;
        }
        super.implClose();
        this.sequence = null;
        this.running = false;
        this.cacheTempoMPQ = -1.0d;
        this.cacheTempoFactor = -1.0f;
        this.trackMuted = null;
        this.trackSolo = null;
        this.loopStart = 0L;
        this.loopEnd = -1L;
        this.loopCount = 0;
        this.doAutoConnectAtNextOpen = this.autoConnect;
        if (this.autoConnectedReceiver != null) {
            try {
                this.autoConnectedReceiver.close();
            } catch (Exception e2) {
            }
            this.autoConnectedReceiver = null;
        }
    }

    void implStart() throws ArrayIndexOutOfBoundsException {
        if (this.playThread == null) {
            return;
        }
        this.tempoCache.refresh(this.sequence);
        if (!this.running) {
            this.running = true;
            this.playThread.start();
        }
    }

    void implStop() {
        if (this.playThread == null) {
            return;
        }
        this.recording = false;
        if (this.running) {
            this.running = false;
            this.playThread.stop();
        }
    }

    private static EventDispatcher getEventDispatcher() {
        EventDispatcher eventDispatcher;
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        synchronized (dispatchers) {
            EventDispatcher eventDispatcher2 = dispatchers.get(threadGroup);
            if (eventDispatcher2 == null) {
                eventDispatcher2 = new EventDispatcher();
                dispatchers.put(threadGroup, eventDispatcher2);
                eventDispatcher2.start();
            }
            eventDispatcher = eventDispatcher2;
        }
        return eventDispatcher;
    }

    void sendMetaEvents(MidiMessage midiMessage) {
        if (this.metaEventListeners.size() == 0) {
            return;
        }
        getEventDispatcher().sendAudioEvents(midiMessage, this.metaEventListeners);
    }

    void sendControllerEvents(MidiMessage midiMessage) {
        int size = this.controllerEventListeners.size();
        if (size == 0 || !(midiMessage instanceof ShortMessage)) {
            return;
        }
        int data1 = ((ShortMessage) midiMessage).getData1();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            ControllerListElement controllerListElement = (ControllerListElement) this.controllerEventListeners.get(i2);
            int i3 = 0;
            while (true) {
                if (i3 >= controllerListElement.controllers.length) {
                    break;
                }
                if (controllerListElement.controllers[i3] != data1) {
                    i3++;
                } else {
                    arrayList.add(controllerListElement.listener);
                    break;
                }
            }
        }
        getEventDispatcher().sendAudioEvents(midiMessage, arrayList);
    }

    private boolean needCaching() {
        return !isOpen() || this.sequence == null || this.playThread == null;
    }

    private DataPump getDataPump() {
        if (this.playThread != null) {
            return this.playThread.getDataPump();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MidiUtils.TempoCache getTempoCache() {
        return this.tempoCache;
    }

    private static boolean[] ensureBoolArraySize(boolean[] zArr, int i2) {
        if (zArr == null) {
            return new boolean[i2];
        }
        if (zArr.length < i2) {
            boolean[] zArr2 = new boolean[i2];
            System.arraycopy(zArr, 0, zArr2, 0, zArr.length);
            return zArr2;
        }
        return zArr;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected boolean hasReceivers() {
        return true;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected Receiver createReceiver() throws MidiUnavailableException {
        return new SequencerReceiver(this);
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected boolean hasTransmitters() {
        return true;
    }

    @Override // com.sun.media.sound.AbstractMidiDevice
    protected Transmitter createTransmitter() throws MidiUnavailableException {
        return new SequencerTransmitter(this, null);
    }

    @Override // com.sun.media.sound.AutoConnectSequencer
    public void setAutoConnect(Receiver receiver) {
        this.autoConnect = receiver != null;
        this.autoConnectedReceiver = receiver;
    }

    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$SequencerTransmitter.class */
    private class SequencerTransmitter extends AbstractMidiDevice.BasicTransmitter {
        final /* synthetic */ RealTimeSequencer this$0;

        /* synthetic */ SequencerTransmitter(RealTimeSequencer realTimeSequencer, AnonymousClass1 anonymousClass1) {
            this(realTimeSequencer);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private SequencerTransmitter(RealTimeSequencer realTimeSequencer) {
            super();
            this.this$0 = realTimeSequencer;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$SequencerReceiver.class */
    final class SequencerReceiver extends AbstractMidiDevice.AbstractReceiver {
        final /* synthetic */ RealTimeSequencer this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        SequencerReceiver(RealTimeSequencer realTimeSequencer) {
            super();
            this.this$0 = realTimeSequencer;
        }

        @Override // com.sun.media.sound.AbstractMidiDevice.AbstractReceiver
        void implSend(MidiMessage midiMessage, long j2) {
            long jMicrosecond2tick;
            MidiMessage fastShortMessage;
            if (this.this$0.recording) {
                if (j2 >= 0) {
                    synchronized (this.this$0.tempoCache) {
                        jMicrosecond2tick = MidiUtils.microsecond2tick(this.this$0.sequence, j2, this.this$0.tempoCache);
                    }
                } else {
                    jMicrosecond2tick = this.this$0.getTickPosition();
                }
                Track track = null;
                if (midiMessage.getLength() > 1) {
                    if (!(midiMessage instanceof ShortMessage)) {
                        track = RecordingTrack.get(this.this$0.recordingTracks, -1);
                    } else {
                        ShortMessage shortMessage = (ShortMessage) midiMessage;
                        if ((shortMessage.getStatus() & 240) != 240) {
                            track = RecordingTrack.get(this.this$0.recordingTracks, shortMessage.getChannel());
                        }
                    }
                    if (track != null) {
                        if (midiMessage instanceof ShortMessage) {
                            fastShortMessage = new FastShortMessage((ShortMessage) midiMessage);
                        } else {
                            fastShortMessage = (MidiMessage) midiMessage.clone();
                        }
                        track.add(new MidiEvent(fastShortMessage, jMicrosecond2tick));
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$RealTimeSequencerInfo.class */
    static class RealTimeSequencerInfo extends MidiDevice.Info {
        private static final String name = "Real Time Sequencer";
        private static final String vendor = "Oracle Corporation";
        private static final String description = "Software sequencer";
        private static final String version = "Version 1.0";

        /* synthetic */ RealTimeSequencerInfo(AnonymousClass1 anonymousClass1) {
            this();
        }

        private RealTimeSequencerInfo() {
            super(name, "Oracle Corporation", description, version);
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$ControllerListElement.class */
    private class ControllerListElement {
        int[] controllers;
        final ControllerEventListener listener;

        private ControllerListElement(ControllerEventListener controllerEventListener, int[] iArr) {
            this.listener = controllerEventListener;
            if (iArr == null) {
                iArr = new int[128];
                for (int i2 = 0; i2 < 128; i2++) {
                    iArr[i2] = i2;
                }
            }
            this.controllers = iArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addControllers(int[] iArr) {
            if (iArr == null) {
                this.controllers = new int[128];
                for (int i2 = 0; i2 < 128; i2++) {
                    this.controllers[i2] = i2;
                }
                return;
            }
            int[] iArr2 = new int[this.controllers.length + iArr.length];
            for (int i3 = 0; i3 < this.controllers.length; i3++) {
                iArr2[i3] = this.controllers[i3];
            }
            int length = this.controllers.length;
            for (int i4 = 0; i4 < iArr.length; i4++) {
                boolean z2 = false;
                int i5 = 0;
                while (true) {
                    if (i5 >= this.controllers.length) {
                        break;
                    }
                    if (iArr[i4] != this.controllers[i5]) {
                        i5++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    int i6 = length;
                    length++;
                    iArr2[i6] = iArr[i4];
                }
            }
            int[] iArr3 = new int[length];
            for (int i7 = 0; i7 < length; i7++) {
                iArr3[i7] = iArr2[i7];
            }
            this.controllers = iArr3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeControllers(int[] iArr) {
            if (iArr == null) {
                this.controllers = new int[0];
                return;
            }
            int[] iArr2 = new int[this.controllers.length];
            int i2 = 0;
            for (int i3 = 0; i3 < this.controllers.length; i3++) {
                boolean z2 = false;
                int i4 = 0;
                while (true) {
                    if (i4 >= iArr.length) {
                        break;
                    }
                    if (this.controllers[i3] != iArr[i4]) {
                        i4++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    int i5 = i2;
                    i2++;
                    iArr2[i5] = this.controllers[i3];
                }
            }
            int[] iArr3 = new int[i2];
            for (int i6 = 0; i6 < i2; i6++) {
                iArr3[i6] = iArr2[i6];
            }
            this.controllers = iArr3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int[] getControllers() {
            if (this.controllers == null) {
                return null;
            }
            int[] iArr = new int[this.controllers.length];
            for (int i2 = 0; i2 < this.controllers.length; i2++) {
                iArr[i2] = this.controllers[i2];
            }
            return iArr;
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$RecordingTrack.class */
    static class RecordingTrack {
        private final Track track;
        private int channel;

        RecordingTrack(Track track, int i2) {
            this.track = track;
            this.channel = i2;
        }

        static RecordingTrack get(List list, Track track) {
            synchronized (list) {
                int size = list.size();
                for (int i2 = 0; i2 < size; i2++) {
                    RecordingTrack recordingTrack = (RecordingTrack) list.get(i2);
                    if (recordingTrack.track == track) {
                        return recordingTrack;
                    }
                }
                return null;
            }
        }

        static Track get(List list, int i2) {
            synchronized (list) {
                int size = list.size();
                for (int i3 = 0; i3 < size; i3++) {
                    RecordingTrack recordingTrack = (RecordingTrack) list.get(i3);
                    if (recordingTrack.channel == i2 || recordingTrack.channel == -1) {
                        return recordingTrack.track;
                    }
                }
                return null;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$PlayThread.class */
    final class PlayThread implements Runnable {
        private final DataPump dataPump;
        final /* synthetic */ RealTimeSequencer this$0;
        private final Object lock = new Object();
        boolean interrupted = false;
        boolean isPumping = false;
        private Thread thread = JSSecurityManager.createThread(this, "Java Sound Sequencer", false, 8, true);

        PlayThread(RealTimeSequencer realTimeSequencer) {
            this.this$0 = realTimeSequencer;
            this.dataPump = this.this$0.new DataPump();
        }

        DataPump getDataPump() {
            return this.dataPump;
        }

        synchronized void setSequence(Sequence sequence) {
            this.dataPump.setSequence(sequence);
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.media.sound.RealTimeSequencer.DataPump.access$1202(com.sun.media.sound.RealTimeSequencer$DataPump, long):long */
        synchronized void start() {
            this.this$0.running = true;
            if (!this.dataPump.hasCachedTempo()) {
                this.dataPump.setTempoMPQ(this.this$0.tempoCache.getTempoMPQAt(this.this$0.getTickPosition()));
            }
            DataPump.access$1202(this.dataPump, 0L);
            this.dataPump.clearNoteOnCache();
            this.dataPump.needReindex = true;
            this.dataPump.resetLoopCount();
            synchronized (this.lock) {
                this.lock.notifyAll();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:5:0x0013  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        synchronized void stop() {
            /*
                r5 = this;
                r0 = r5
                r0.playThreadImplStop()
                long r0 = java.lang.System.nanoTime()
                r1 = 1000000(0xf4240, double:4.940656E-318)
                long r0 = r0 / r1
                r6 = r0
            Lc:
                r0 = r5
                boolean r0 = r0.isPumping
                if (r0 == 0) goto L48
                r0 = r5
                java.lang.Object r0 = r0.lock
                r1 = r0
                r8 = r1
                monitor-enter(r0)
                r0 = r5
                java.lang.Object r0 = r0.lock     // Catch: java.lang.InterruptedException -> L27 java.lang.Throwable -> L2e
                r1 = 2000(0x7d0, double:9.88E-321)
                r0.wait(r1)     // Catch: java.lang.InterruptedException -> L27 java.lang.Throwable -> L2e
                goto L29
            L27:
                r9 = move-exception
            L29:
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L2e
                goto L35
            L2e:
                r10 = move-exception
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L2e
                r0 = r10
                throw r0
            L35:
                long r0 = java.lang.System.nanoTime()
                r1 = 1000000(0xf4240, double:4.940656E-318)
                long r0 = r0 / r1
                r1 = r6
                long r0 = r0 - r1
                r1 = 1900(0x76c, double:9.387E-321)
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto Lc
                goto Lc
            L48:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.RealTimeSequencer.PlayThread.stop():void");
        }

        void playThreadImplStop() {
            this.this$0.running = false;
            synchronized (this.lock) {
                this.lock.notifyAll();
            }
        }

        void close() {
            Thread thread;
            synchronized (this) {
                this.interrupted = true;
                thread = this.thread;
                this.thread = null;
            }
            if (thread != null) {
                synchronized (this.lock) {
                    this.lock.notifyAll();
                }
            }
            if (thread != null) {
                try {
                    thread.join(2000L);
                } catch (InterruptedException e2) {
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!this.interrupted) {
                boolean zPump = false;
                boolean z2 = this.this$0.running;
                this.isPumping = !this.interrupted && this.this$0.running;
                while (!zPump && !this.interrupted && this.this$0.running) {
                    zPump = this.dataPump.pump();
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e2) {
                    }
                }
                playThreadImplStop();
                if (z2) {
                    this.dataPump.notesOff(true);
                }
                if (zPump) {
                    this.dataPump.setTickPos(this.this$0.sequence.getTickLength());
                    MetaMessage metaMessage = new MetaMessage();
                    try {
                        metaMessage.setMessage(47, new byte[0], 0);
                    } catch (InvalidMidiDataException e3) {
                    }
                    this.this$0.sendMetaEvents(metaMessage);
                }
                synchronized (this.lock) {
                    this.isPumping = false;
                    this.lock.notifyAll();
                    while (!this.this$0.running && !this.interrupted) {
                        try {
                            this.lock.wait();
                        } catch (Exception e4) {
                        }
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/RealTimeSequencer$DataPump.class */
    private class DataPump {
        private float currTempo;
        private float tempoFactor;
        private float inverseTempoFactor;
        private long ignoreTempoEventAt;
        private int resolution;
        private float divisionType;
        private long checkPointMillis;
        private long checkPointTick;
        private int[] noteOnCache;
        private Track[] tracks;
        private boolean[] trackDisabled;
        private int[] trackReadPos;
        private long lastTick;
        private boolean needReindex = false;
        private int currLoopCounter = 0;

        /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        static /* synthetic */ long access$1202(com.sun.media.sound.RealTimeSequencer.DataPump r6, long r7) {
            /*
                r0 = r6
                r1 = r7
                // decode failed: arraycopy: source index -1 out of bounds for object array[6]
                r0.checkPointMillis = r1
                return r-1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.RealTimeSequencer.DataPump.access$1202(com.sun.media.sound.RealTimeSequencer$DataPump, long):long");
        }

        DataPump() {
            init();
        }

        synchronized void init() {
            this.ignoreTempoEventAt = -1L;
            this.tempoFactor = 1.0f;
            this.inverseTempoFactor = 1.0f;
            this.noteOnCache = new int[128];
            this.tracks = null;
            this.trackDisabled = null;
        }

        synchronized void setTickPos(long j2) {
            this.lastTick = j2;
            if (RealTimeSequencer.this.running) {
                notesOff(false);
            }
            if (RealTimeSequencer.this.running || j2 > 0) {
                chaseEvents(j2, j2);
            } else {
                this.needReindex = true;
            }
            if (!hasCachedTempo()) {
                setTempoMPQ(RealTimeSequencer.this.getTempoCache().getTempoMPQAt(this.lastTick, this.currTempo));
                this.ignoreTempoEventAt = -1L;
            }
            this.checkPointMillis = 0L;
        }

        long getTickPos() {
            return this.lastTick;
        }

        boolean hasCachedTempo() {
            if (this.ignoreTempoEventAt != this.lastTick) {
                this.ignoreTempoEventAt = -1L;
            }
            return this.ignoreTempoEventAt >= 0;
        }

        synchronized void setTempoMPQ(float f2) {
            if (f2 > 0.0f && f2 != this.currTempo) {
                this.ignoreTempoEventAt = this.lastTick;
                this.currTempo = f2;
                this.checkPointMillis = 0L;
            }
        }

        float getTempoMPQ() {
            return this.currTempo;
        }

        synchronized void setTempoFactor(float f2) {
            if (f2 > 0.0f && f2 != this.tempoFactor) {
                this.tempoFactor = f2;
                this.inverseTempoFactor = 1.0f / f2;
                this.checkPointMillis = 0L;
            }
        }

        float getTempoFactor() {
            return this.tempoFactor;
        }

        synchronized void muteSoloChanged() {
            boolean[] zArrMakeDisabledArray = makeDisabledArray();
            if (RealTimeSequencer.this.running) {
                applyDisabledTracks(this.trackDisabled, zArrMakeDisabledArray);
            }
            this.trackDisabled = zArrMakeDisabledArray;
        }

        synchronized void setSequence(Sequence sequence) {
            if (sequence == null) {
                init();
                return;
            }
            this.tracks = sequence.getTracks();
            muteSoloChanged();
            this.resolution = sequence.getResolution();
            this.divisionType = sequence.getDivisionType();
            this.trackReadPos = new int[this.tracks.length];
            this.checkPointMillis = 0L;
            this.needReindex = true;
        }

        synchronized void resetLoopCount() {
            this.currLoopCounter = RealTimeSequencer.this.loopCount;
        }

        void clearNoteOnCache() {
            for (int i2 = 0; i2 < 128; i2++) {
                this.noteOnCache[i2] = 0;
            }
        }

        void notesOff(boolean z2) {
            int i2 = 0;
            for (int i3 = 0; i3 < 16; i3++) {
                int i4 = 1 << i3;
                for (int i5 = 0; i5 < 128; i5++) {
                    if ((this.noteOnCache[i5] & i4) != 0) {
                        int[] iArr = this.noteOnCache;
                        int i6 = i5;
                        iArr[i6] = iArr[i6] ^ i4;
                        RealTimeSequencer.this.getTransmitterList().sendMessage(144 | i3 | (i5 << 8), -1L);
                        i2++;
                    }
                }
                RealTimeSequencer.this.getTransmitterList().sendMessage(176 | i3 | 31488, -1L);
                RealTimeSequencer.this.getTransmitterList().sendMessage(176 | i3 | 16384, -1L);
                if (z2) {
                    RealTimeSequencer.this.getTransmitterList().sendMessage(176 | i3 | 30976, -1L);
                    i2++;
                }
            }
        }

        private boolean[] makeDisabledArray() {
            boolean[] zArr;
            boolean[] zArr2;
            if (this.tracks == null) {
                return null;
            }
            boolean[] zArr3 = new boolean[this.tracks.length];
            synchronized (RealTimeSequencer.this) {
                zArr = RealTimeSequencer.this.trackMuted;
                zArr2 = RealTimeSequencer.this.trackSolo;
            }
            boolean z2 = false;
            if (zArr2 != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= zArr2.length) {
                        break;
                    }
                    if (!zArr2[i2]) {
                        i2++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (z2) {
                int i3 = 0;
                while (i3 < zArr3.length) {
                    zArr3[i3] = i3 >= zArr2.length || !zArr2[i3];
                    i3++;
                }
            } else {
                int i4 = 0;
                while (i4 < zArr3.length) {
                    zArr3[i4] = zArr != null && i4 < zArr.length && zArr[i4];
                    i4++;
                }
            }
            return zArr3;
        }

        private void sendNoteOffIfOn(Track track, long j2) {
            int size = track.size();
            int i2 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                try {
                    MidiEvent midiEvent = track.get(i3);
                    if (midiEvent.getTick() > j2) {
                        break;
                    }
                    MidiMessage message = midiEvent.getMessage();
                    int status = message.getStatus();
                    if (message.getLength() == 3 && (status & 240) == 144) {
                        int data1 = -1;
                        if (message instanceof ShortMessage) {
                            ShortMessage shortMessage = (ShortMessage) message;
                            if (shortMessage.getData2() > 0) {
                                data1 = shortMessage.getData1();
                            }
                        } else {
                            byte[] message2 = message.getMessage();
                            if ((message2[2] & Byte.MAX_VALUE) > 0) {
                                data1 = message2[1] & 127;
                            }
                        }
                        if (data1 >= 0) {
                            int i4 = 1 << (status & 15);
                            if ((this.noteOnCache[data1] & i4) != 0) {
                                RealTimeSequencer.this.getTransmitterList().sendMessage(status | (data1 << 8), -1L);
                                int[] iArr = this.noteOnCache;
                                int i5 = data1;
                                iArr[i5] = iArr[i5] & (65535 ^ i4);
                                i2++;
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e2) {
                    return;
                }
            }
        }

        private void applyDisabledTracks(boolean[] zArr, boolean[] zArr2) {
            byte[][] bArr = (byte[][]) null;
            synchronized (RealTimeSequencer.this) {
                for (int i2 = 0; i2 < zArr2.length; i2++) {
                    if ((zArr == null || i2 >= zArr.length || !zArr[i2]) && zArr2[i2]) {
                        if (this.tracks.length > i2) {
                            sendNoteOffIfOn(this.tracks[i2], this.lastTick);
                        }
                    } else if (zArr != null && i2 < zArr.length && zArr[i2] && !zArr2[i2]) {
                        if (bArr == null) {
                            bArr = new byte[128][16];
                        }
                        chaseTrackEvents(i2, 0L, this.lastTick, true, bArr);
                    }
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x006e, code lost:
        
            if (r11 == false) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x0077, code lost:
        
            if (r6 >= r5.trackReadPos.length) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x007a, code lost:
        
            r0 = r5.trackReadPos;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0081, code lost:
        
            if (r16 <= 0) goto L26;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0084, code lost:
        
            r2 = r16 - 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x008b, code lost:
        
            r2 = 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x008c, code lost:
        
            r0[r6] = r2;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void chaseTrackEvents(int r6, long r7, long r9, boolean r11, byte[][] r12) {
            /*
                Method dump skipped, instructions count: 523
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.RealTimeSequencer.DataPump.chaseTrackEvents(int, long, long, boolean, byte[][]):void");
        }

        synchronized void chaseEvents(long j2, long j3) {
            byte[][] bArr = new byte[128][16];
            for (int i2 = 0; i2 < this.tracks.length; i2++) {
                if (this.trackDisabled == null || this.trackDisabled.length <= i2 || !this.trackDisabled[i2]) {
                    chaseTrackEvents(i2, j2, j3, true, bArr);
                }
            }
        }

        private long getCurrentTimeMillis() {
            return System.nanoTime() / 1000000;
        }

        private long millis2tick(long j2) {
            if (this.divisionType != 0.0f) {
                return (long) ((((j2 * this.tempoFactor) * this.divisionType) * this.resolution) / 1000.0d);
            }
            return MidiUtils.microsec2ticks(j2 * 1000, this.currTempo * this.inverseTempoFactor, this.resolution);
        }

        private long tick2millis(long j2) {
            if (this.divisionType != 0.0f) {
                return (long) ((j2 * 1000.0d) / ((this.tempoFactor * this.divisionType) * this.resolution));
            }
            return MidiUtils.ticks2microsec(j2, this.currTempo * this.inverseTempoFactor, this.resolution) / 1000;
        }

        private void ReindexTrack(int i2, long j2) {
            if (i2 < this.trackReadPos.length && i2 < this.tracks.length) {
                this.trackReadPos[i2] = MidiUtils.tick2index(this.tracks[i2], j2);
            }
        }

        private boolean dispatchMessage(int i2, MidiEvent midiEvent) {
            int tempoMPQ;
            boolean z2 = false;
            MidiMessage message = midiEvent.getMessage();
            int status = message.getStatus();
            int length = message.getLength();
            if (status == 255 && length >= 2) {
                if (i2 == 0 && (tempoMPQ = MidiUtils.getTempoMPQ(message)) > 0) {
                    if (midiEvent.getTick() != this.ignoreTempoEventAt) {
                        setTempoMPQ(tempoMPQ);
                        z2 = true;
                    }
                    this.ignoreTempoEventAt = -1L;
                }
                RealTimeSequencer.this.sendMetaEvents(message);
            } else {
                RealTimeSequencer.this.getTransmitterList().sendMessage(message, -1L);
                switch (status & 240) {
                    case 128:
                        int data1 = ((ShortMessage) message).getData1() & 127;
                        int[] iArr = this.noteOnCache;
                        iArr[data1] = iArr[data1] & (65535 ^ (1 << (status & 15)));
                        break;
                    case 144:
                        ShortMessage shortMessage = (ShortMessage) message;
                        int data12 = shortMessage.getData1() & 127;
                        if ((shortMessage.getData2() & 127) > 0) {
                            int[] iArr2 = this.noteOnCache;
                            iArr2[data12] = iArr2[data12] | (1 << (status & 15));
                            break;
                        } else {
                            int[] iArr3 = this.noteOnCache;
                            iArr3[data12] = iArr3[data12] & (65535 ^ (1 << (status & 15)));
                            break;
                        }
                    case 176:
                        RealTimeSequencer.this.sendControllerEvents(message);
                        break;
                }
            }
            return z2;
        }

        synchronized boolean pump() {
            boolean zDispatchMessage;
            boolean z2;
            long jMillis2tick = this.lastTick;
            boolean z3 = false;
            long currentTimeMillis = getCurrentTimeMillis();
            do {
                zDispatchMessage = false;
                if (this.needReindex) {
                    if (this.trackReadPos.length < this.tracks.length) {
                        this.trackReadPos = new int[this.tracks.length];
                    }
                    for (int i2 = 0; i2 < this.tracks.length; i2++) {
                        ReindexTrack(i2, jMillis2tick);
                    }
                    this.needReindex = false;
                    this.checkPointMillis = 0L;
                }
                if (this.checkPointMillis == 0) {
                    currentTimeMillis = getCurrentTimeMillis();
                    this.checkPointMillis = currentTimeMillis;
                    jMillis2tick = this.lastTick;
                    this.checkPointTick = jMillis2tick;
                } else {
                    jMillis2tick = this.checkPointTick + millis2tick(currentTimeMillis - this.checkPointMillis);
                    if (RealTimeSequencer.this.loopEnd != -1 && (((RealTimeSequencer.this.loopCount > 0 && this.currLoopCounter > 0) || RealTimeSequencer.this.loopCount == -1) && this.lastTick <= RealTimeSequencer.this.loopEnd && jMillis2tick >= RealTimeSequencer.this.loopEnd)) {
                        jMillis2tick = RealTimeSequencer.this.loopEnd - 1;
                        z3 = true;
                    }
                    this.lastTick = jMillis2tick;
                }
                int i3 = 0;
                for (int i4 = 0; i4 < this.tracks.length; i4++) {
                    try {
                        boolean z4 = this.trackDisabled[i4];
                        Track track = this.tracks[i4];
                        int i5 = this.trackReadPos[i4];
                        int size = track.size();
                        while (true) {
                            if (zDispatchMessage || i5 >= size) {
                                break;
                            }
                            MidiEvent midiEvent = track.get(i5);
                            if (midiEvent.getTick() > jMillis2tick) {
                                break;
                            }
                            if (i5 == size - 1 && MidiUtils.isMetaEndOfTrack(midiEvent.getMessage())) {
                                i5 = size;
                                break;
                            }
                            i5++;
                            if (!z4 || (i4 == 0 && MidiUtils.isMetaTempo(midiEvent.getMessage()))) {
                                zDispatchMessage = dispatchMessage(i4, midiEvent);
                            }
                        }
                        if (i5 >= size) {
                            i3++;
                        }
                        this.trackReadPos[i4] = i5;
                    } catch (Exception e2) {
                        if (e2 instanceof ArrayIndexOutOfBoundsException) {
                            this.needReindex = true;
                            zDispatchMessage = true;
                        }
                    }
                    if (zDispatchMessage) {
                        break;
                    }
                }
                z2 = i3 == this.tracks.length;
                if (z3 || (((RealTimeSequencer.this.loopCount > 0 && this.currLoopCounter > 0) || RealTimeSequencer.this.loopCount == -1) && !zDispatchMessage && RealTimeSequencer.this.loopEnd == -1 && z2)) {
                    long j2 = this.checkPointMillis;
                    long j3 = RealTimeSequencer.this.loopEnd;
                    if (j3 == -1) {
                        j3 = this.lastTick;
                    }
                    if (RealTimeSequencer.this.loopCount != -1) {
                        this.currLoopCounter--;
                    }
                    setTickPos(RealTimeSequencer.this.loopStart);
                    this.checkPointMillis = j2 + tick2millis(j3 - this.checkPointTick);
                    this.checkPointTick = RealTimeSequencer.this.loopStart;
                    this.needReindex = false;
                    zDispatchMessage = false;
                    z3 = false;
                    z2 = false;
                }
            } while (zDispatchMessage);
            return z2;
        }
    }
}
