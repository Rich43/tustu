package com.sun.media.sound;

import com.sun.corba.se.impl.util.Version;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Transmitter;
import javax.sound.midi.VoiceStatus;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;
import sun.security.x509.IssuingDistributionPointExtension;

/* loaded from: rt.jar:com/sun/media/sound/SoftSynthesizer.class */
public final class SoftSynthesizer implements AudioSynthesizer, ReferenceCountingDevice {
    static final String INFO_NAME = "Gervill";
    static final String INFO_VENDOR = "OpenJDK";
    static final String INFO_DESCRIPTION = "Software MIDI Synthesizer";
    static final String INFO_VERSION = "1.0";
    static final MidiDevice.Info info = new Info();
    private static SourceDataLine testline = null;
    private static Soundbank defaultSoundBank = null;
    SoftChannel[] channels;
    private SoftMainMixer mainmixer;
    private SoftVoice[] voices;
    WeakAudioStream weakstream = null;
    final Object control_mutex = this;
    int voiceIDCounter = 0;
    int voice_allocation_mode = 0;
    boolean load_default_soundbank = false;
    boolean reverb_light = true;
    boolean reverb_on = true;
    boolean chorus_on = true;
    boolean agc_on = true;
    SoftChannelProxy[] external_channels = null;
    private boolean largemode = false;
    private int gmmode = 0;
    private int deviceid = 0;
    private AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
    private SourceDataLine sourceDataLine = null;
    private SoftAudioPusher pusher = null;
    private AudioInputStream pusher_stream = null;
    private float controlrate = 147.0f;
    private boolean open = false;
    private boolean implicitOpen = false;
    private String resamplerType = "linear";
    private SoftResampler resampler = new SoftLinearResampler();
    private int number_of_midi_channels = 16;
    private int maxpoly = 64;
    private long latency = 200000;
    private boolean jitter_correction = false;
    private Map<String, SoftTuning> tunings = new HashMap();
    private Map<String, SoftInstrument> inslist = new HashMap();
    private Map<String, ModelInstrument> loadedlist = new HashMap();
    private ArrayList<Receiver> recvslist = new ArrayList<>();

    /* loaded from: rt.jar:com/sun/media/sound/SoftSynthesizer$WeakAudioStream.class */
    protected static final class WeakAudioStream extends InputStream {
        private volatile AudioInputStream stream;
        private int framesize;
        private WeakReference<AudioInputStream> weak_stream_link;
        private AudioFloatConverter converter;
        private int samplesize;
        public SoftAudioPusher pusher = null;
        public AudioInputStream jitter_stream = null;
        public SourceDataLine sourceDataLine = null;
        public volatile long silent_samples = 0;
        private float[] silentbuffer = null;

        public void setInputStream(AudioInputStream audioInputStream) {
            this.stream = audioInputStream;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            AudioInputStream audioInputStream = this.stream;
            if (audioInputStream != null) {
                return audioInputStream.available();
            }
            return 0;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] bArr = new byte[1];
            if (read(bArr) == -1) {
                return -1;
            }
            return bArr[0] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            AudioInputStream audioInputStream = this.stream;
            if (audioInputStream != null) {
                return audioInputStream.read(bArr, i2, i3);
            }
            int i4 = i3 / this.samplesize;
            if (this.silentbuffer == null || this.silentbuffer.length < i4) {
                this.silentbuffer = new float[i4];
            }
            this.converter.toByteArray(this.silentbuffer, i4, bArr, i2);
            this.silent_samples += i3 / this.framesize;
            if (this.pusher != null && this.weak_stream_link.get() == null) {
                Runnable runnable = new Runnable() { // from class: com.sun.media.sound.SoftSynthesizer.WeakAudioStream.1
                    SoftAudioPusher _pusher;
                    AudioInputStream _jitter_stream;
                    SourceDataLine _sourceDataLine;

                    {
                        this._pusher = WeakAudioStream.this.pusher;
                        this._jitter_stream = WeakAudioStream.this.jitter_stream;
                        this._sourceDataLine = WeakAudioStream.this.sourceDataLine;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        this._pusher.stop();
                        if (this._jitter_stream != null) {
                            try {
                                this._jitter_stream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (this._sourceDataLine != null) {
                            this._sourceDataLine.close();
                        }
                    }
                };
                this.pusher = null;
                this.jitter_stream = null;
                this.sourceDataLine = null;
                new Thread(runnable).start();
            }
            return i3;
        }

        public WeakAudioStream(AudioInputStream audioInputStream) {
            this.framesize = 0;
            this.stream = audioInputStream;
            this.weak_stream_link = new WeakReference<>(audioInputStream);
            this.converter = AudioFloatConverter.getConverter(audioInputStream.getFormat());
            this.samplesize = audioInputStream.getFormat().getFrameSize() / audioInputStream.getFormat().getChannels();
            this.framesize = audioInputStream.getFormat().getFrameSize();
        }

        public AudioInputStream getAudioInputStream() {
            return new AudioInputStream(this, this.stream.getFormat(), -1L);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            AudioInputStream audioInputStream = this.weak_stream_link.get();
            if (audioInputStream != null) {
                audioInputStream.close();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/SoftSynthesizer$Info.class */
    private static class Info extends MidiDevice.Info {
        Info() {
            super(SoftSynthesizer.INFO_NAME, SoftSynthesizer.INFO_VENDOR, SoftSynthesizer.INFO_DESCRIPTION, "1.0");
        }
    }

    private void getBuffers(ModelInstrument modelInstrument, List<ModelByteBuffer> list) {
        for (ModelPerformer modelPerformer : modelInstrument.getPerformers()) {
            if (modelPerformer.getOscillators() != null) {
                for (ModelOscillator modelOscillator : modelPerformer.getOscillators()) {
                    if (modelOscillator instanceof ModelByteBufferWavetable) {
                        ModelByteBufferWavetable modelByteBufferWavetable = (ModelByteBufferWavetable) modelOscillator;
                        ModelByteBuffer buffer = modelByteBufferWavetable.getBuffer();
                        if (buffer != null) {
                            list.add(buffer);
                        }
                        ModelByteBuffer modelByteBuffer = modelByteBufferWavetable.get8BitExtensionBuffer();
                        if (modelByteBuffer != null) {
                            list.add(modelByteBuffer);
                        }
                    }
                }
            }
        }
    }

    private boolean loadSamples(List<ModelInstrument> list) {
        if (this.largemode) {
            return true;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<ModelInstrument> it = list.iterator();
        while (it.hasNext()) {
            getBuffers(it.next(), arrayList);
        }
        try {
            ModelByteBuffer.loadAll(arrayList);
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    private boolean loadInstruments(List<ModelInstrument> list) {
        if (!isOpen() || !loadSamples(list)) {
            return false;
        }
        synchronized (this.control_mutex) {
            if (this.channels != null) {
                for (SoftChannel softChannel : this.channels) {
                    softChannel.current_instrument = null;
                    softChannel.current_director = null;
                }
            }
            for (ModelInstrument modelInstrument : list) {
                String strPatchToString = patchToString(modelInstrument.getPatch());
                this.inslist.put(strPatchToString, new SoftInstrument(modelInstrument));
                this.loadedlist.put(strPatchToString, modelInstrument);
            }
        }
        return true;
    }

    private void processPropertyInfo(Map<String, Object> map) {
        AudioSynthesizerPropertyInfo[] propertyInfo = getPropertyInfo(map);
        String str = (String) propertyInfo[0].value;
        if (str.equalsIgnoreCase(IssuingDistributionPointExtension.POINT)) {
            this.resampler = new SoftPointResampler();
            this.resamplerType = IssuingDistributionPointExtension.POINT;
        } else if (str.equalsIgnoreCase("linear")) {
            this.resampler = new SoftLinearResampler2();
            this.resamplerType = "linear";
        } else if (str.equalsIgnoreCase("linear1")) {
            this.resampler = new SoftLinearResampler();
            this.resamplerType = "linear1";
        } else if (str.equalsIgnoreCase("linear2")) {
            this.resampler = new SoftLinearResampler2();
            this.resamplerType = "linear2";
        } else if (str.equalsIgnoreCase("cubic")) {
            this.resampler = new SoftCubicResampler();
            this.resamplerType = "cubic";
        } else if (str.equalsIgnoreCase("lanczos")) {
            this.resampler = new SoftLanczosResampler();
            this.resamplerType = "lanczos";
        } else if (str.equalsIgnoreCase("sinc")) {
            this.resampler = new SoftSincResampler();
            this.resamplerType = "sinc";
        }
        setFormat((AudioFormat) propertyInfo[2].value);
        this.controlrate = ((Float) propertyInfo[1].value).floatValue();
        this.latency = ((Long) propertyInfo[3].value).longValue();
        this.deviceid = ((Integer) propertyInfo[4].value).intValue();
        this.maxpoly = ((Integer) propertyInfo[5].value).intValue();
        this.reverb_on = ((Boolean) propertyInfo[6].value).booleanValue();
        this.chorus_on = ((Boolean) propertyInfo[7].value).booleanValue();
        this.agc_on = ((Boolean) propertyInfo[8].value).booleanValue();
        this.largemode = ((Boolean) propertyInfo[9].value).booleanValue();
        this.number_of_midi_channels = ((Integer) propertyInfo[10].value).intValue();
        this.jitter_correction = ((Boolean) propertyInfo[11].value).booleanValue();
        this.reverb_light = ((Boolean) propertyInfo[12].value).booleanValue();
        this.load_default_soundbank = ((Boolean) propertyInfo[13].value).booleanValue();
    }

    private String patchToString(Patch patch) {
        if ((patch instanceof ModelPatch) && ((ModelPatch) patch).isPercussion()) {
            return "p." + patch.getProgram() + "." + patch.getBank();
        }
        return patch.getProgram() + "." + patch.getBank();
    }

    private void setFormat(AudioFormat audioFormat) {
        if (audioFormat.getChannels() > 2) {
            throw new IllegalArgumentException("Only mono and stereo audio supported.");
        }
        if (AudioFloatConverter.getConverter(audioFormat) == null) {
            throw new IllegalArgumentException("Audio format not supported.");
        }
        this.format = audioFormat;
    }

    void removeReceiver(Receiver receiver) {
        boolean z2 = false;
        synchronized (this.control_mutex) {
            if (this.recvslist.remove(receiver) && this.implicitOpen && this.recvslist.isEmpty()) {
                z2 = true;
            }
        }
        if (z2) {
            close();
        }
    }

    SoftMainMixer getMainMixer() {
        if (!isOpen()) {
            return null;
        }
        return this.mainmixer;
    }

    SoftInstrument findInstrument(int i2, int i3, int i4) {
        String str;
        String str2;
        if ((i3 >> 7) == 120 || (i3 >> 7) == 121) {
            SoftInstrument softInstrument = this.inslist.get(i2 + "." + i3);
            if (softInstrument != null) {
                return softInstrument;
            }
            if ((i3 >> 7) == 120) {
                str = "p.";
            } else {
                str = "";
            }
            SoftInstrument softInstrument2 = this.inslist.get(str + i2 + "." + ((i3 & 128) << 7));
            if (softInstrument2 != null) {
                return softInstrument2;
            }
            SoftInstrument softInstrument3 = this.inslist.get(str + i2 + "." + (i3 & 128));
            if (softInstrument3 != null) {
                return softInstrument3;
            }
            SoftInstrument softInstrument4 = this.inslist.get(str + i2 + ".0");
            if (softInstrument4 != null) {
                return softInstrument4;
            }
            SoftInstrument softInstrument5 = this.inslist.get(str + i2 + Version.BUILD);
            if (softInstrument5 != null) {
                return softInstrument5;
            }
            return null;
        }
        if (i4 == 9) {
            str2 = "p.";
        } else {
            str2 = "";
        }
        SoftInstrument softInstrument6 = this.inslist.get(str2 + i2 + "." + i3);
        if (softInstrument6 != null) {
            return softInstrument6;
        }
        SoftInstrument softInstrument7 = this.inslist.get(str2 + i2 + ".0");
        if (softInstrument7 != null) {
            return softInstrument7;
        }
        SoftInstrument softInstrument8 = this.inslist.get(str2 + Version.BUILD);
        if (softInstrument8 != null) {
            return softInstrument8;
        }
        return null;
    }

    int getVoiceAllocationMode() {
        return this.voice_allocation_mode;
    }

    int getGeneralMidiMode() {
        return this.gmmode;
    }

    void setGeneralMidiMode(int i2) {
        this.gmmode = i2;
    }

    int getDeviceID() {
        return this.deviceid;
    }

    float getControlRate() {
        return this.controlrate;
    }

    SoftVoice[] getVoices() {
        return this.voices;
    }

    SoftTuning getTuning(Patch patch) {
        String strPatchToString = patchToString(patch);
        SoftTuning softTuning = this.tunings.get(strPatchToString);
        if (softTuning == null) {
            softTuning = new SoftTuning(patch);
            this.tunings.put(strPatchToString, softTuning);
        }
        return softTuning;
    }

    @Override // javax.sound.midi.Synthesizer
    public long getLatency() {
        long j2;
        synchronized (this.control_mutex) {
            j2 = this.latency;
        }
        return j2;
    }

    @Override // com.sun.media.sound.AudioSynthesizer
    public AudioFormat getFormat() {
        AudioFormat audioFormat;
        synchronized (this.control_mutex) {
            audioFormat = this.format;
        }
        return audioFormat;
    }

    @Override // javax.sound.midi.Synthesizer
    public int getMaxPolyphony() {
        int i2;
        synchronized (this.control_mutex) {
            i2 = this.maxpoly;
        }
        return i2;
    }

    @Override // javax.sound.midi.Synthesizer
    public MidiChannel[] getChannels() {
        MidiChannel[] midiChannelArr;
        MidiChannel[] midiChannelArr2;
        synchronized (this.control_mutex) {
            if (this.external_channels == null) {
                this.external_channels = new SoftChannelProxy[16];
                for (int i2 = 0; i2 < this.external_channels.length; i2++) {
                    this.external_channels[i2] = new SoftChannelProxy();
                }
            }
            if (isOpen()) {
                midiChannelArr = new MidiChannel[this.channels.length];
            } else {
                midiChannelArr = new MidiChannel[16];
            }
            for (int i3 = 0; i3 < midiChannelArr.length; i3++) {
                midiChannelArr[i3] = this.external_channels[i3];
            }
            midiChannelArr2 = midiChannelArr;
        }
        return midiChannelArr2;
    }

    @Override // javax.sound.midi.Synthesizer
    public VoiceStatus[] getVoiceStatus() {
        VoiceStatus[] voiceStatusArr;
        if (!isOpen()) {
            VoiceStatus[] voiceStatusArr2 = new VoiceStatus[getMaxPolyphony()];
            for (int i2 = 0; i2 < voiceStatusArr2.length; i2++) {
                VoiceStatus voiceStatus = new VoiceStatus();
                voiceStatus.active = false;
                voiceStatus.bank = 0;
                voiceStatus.channel = 0;
                voiceStatus.note = 0;
                voiceStatus.program = 0;
                voiceStatus.volume = 0;
                voiceStatusArr2[i2] = voiceStatus;
            }
            return voiceStatusArr2;
        }
        synchronized (this.control_mutex) {
            voiceStatusArr = new VoiceStatus[this.voices.length];
            for (int i3 = 0; i3 < this.voices.length; i3++) {
                SoftVoice softVoice = this.voices[i3];
                VoiceStatus voiceStatus2 = new VoiceStatus();
                voiceStatus2.active = softVoice.active;
                voiceStatus2.bank = softVoice.bank;
                voiceStatus2.channel = softVoice.channel;
                voiceStatus2.note = softVoice.note;
                voiceStatus2.program = softVoice.program;
                voiceStatus2.volume = softVoice.volume;
                voiceStatusArr[i3] = voiceStatus2;
            }
        }
        return voiceStatusArr;
    }

    @Override // javax.sound.midi.Synthesizer
    public boolean isSoundbankSupported(Soundbank soundbank) {
        for (Instrument instrument : soundbank.getInstruments()) {
            if (!(instrument instanceof ModelInstrument)) {
                return false;
            }
        }
        return true;
    }

    @Override // javax.sound.midi.Synthesizer
    public boolean loadInstrument(Instrument instrument) {
        if (instrument == null || !(instrument instanceof ModelInstrument)) {
            throw new IllegalArgumentException("Unsupported instrument: " + ((Object) instrument));
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add((ModelInstrument) instrument);
        return loadInstruments(arrayList);
    }

    @Override // javax.sound.midi.Synthesizer
    public void unloadInstrument(Instrument instrument) {
        if (instrument == null || !(instrument instanceof ModelInstrument)) {
            throw new IllegalArgumentException("Unsupported instrument: " + ((Object) instrument));
        }
        if (!isOpen()) {
            return;
        }
        String strPatchToString = patchToString(instrument.getPatch());
        synchronized (this.control_mutex) {
            for (SoftChannel softChannel : this.channels) {
                softChannel.current_instrument = null;
            }
            this.inslist.remove(strPatchToString);
            this.loadedlist.remove(strPatchToString);
            for (int i2 = 0; i2 < this.channels.length; i2++) {
                this.channels[i2].allSoundOff();
            }
        }
    }

    @Override // javax.sound.midi.Synthesizer
    public boolean remapInstrument(Instrument instrument, Instrument instrument2) {
        boolean zLoadInstrument;
        if (instrument == null) {
            throw new NullPointerException();
        }
        if (instrument2 == null) {
            throw new NullPointerException();
        }
        if (!(instrument instanceof ModelInstrument)) {
            throw new IllegalArgumentException("Unsupported instrument: " + instrument.toString());
        }
        if (!(instrument2 instanceof ModelInstrument)) {
            throw new IllegalArgumentException("Unsupported instrument: " + instrument2.toString());
        }
        if (!isOpen()) {
            return false;
        }
        synchronized (this.control_mutex) {
            if (!this.loadedlist.containsValue(instrument2)) {
                throw new IllegalArgumentException("Instrument to is not loaded.");
            }
            unloadInstrument(instrument);
            zLoadInstrument = loadInstrument(new ModelMappedInstrument((ModelInstrument) instrument2, instrument.getPatch()));
        }
        return zLoadInstrument;
    }

    @Override // javax.sound.midi.Synthesizer
    public Soundbank getDefaultSoundbank() {
        OutputStream outputStream;
        InputStream inputStream;
        synchronized (SoftSynthesizer.class) {
            if (defaultSoundBank != null) {
                return defaultSoundBank;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(new PrivilegedAction<InputStream>() { // from class: com.sun.media.sound.SoftSynthesizer.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public InputStream run() {
                    File file = new File(new File(new File(System.getProperties().getProperty("java.home")), "lib"), "audio");
                    if (file.exists()) {
                        File file2 = null;
                        File[] fileArrListFiles = file.listFiles();
                        if (fileArrListFiles != null) {
                            for (File file3 : fileArrListFiles) {
                                if (file3.isFile()) {
                                    String lowerCase = file3.getName().toLowerCase();
                                    if ((lowerCase.endsWith(".sf2") || lowerCase.endsWith(".dls")) && (file2 == null || file3.length() > file2.length())) {
                                        file2 = file3;
                                    }
                                }
                            }
                        }
                        if (file2 != null) {
                            try {
                                return new FileInputStream(file2);
                            } catch (IOException e2) {
                                return null;
                            }
                        }
                        return null;
                    }
                    return null;
                }
            });
            arrayList.add(new PrivilegedAction<InputStream>() { // from class: com.sun.media.sound.SoftSynthesizer.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public InputStream run() {
                    if (System.getProperties().getProperty("os.name").startsWith("Linux")) {
                        for (File file : new File[]{new File("/usr/share/soundfonts/"), new File("/usr/local/share/soundfonts/"), new File("/usr/share/sounds/sf2/"), new File("/usr/local/share/sounds/sf2/")}) {
                            if (file.exists()) {
                                File file2 = new File(file, "default.sf2");
                                if (file2.exists()) {
                                    try {
                                        return new FileInputStream(file2);
                                    } catch (IOException e2) {
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        return null;
                    }
                    return null;
                }
            });
            arrayList.add(new PrivilegedAction<InputStream>() { // from class: com.sun.media.sound.SoftSynthesizer.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public InputStream run() {
                    if (System.getProperties().getProperty("os.name").startsWith("Windows")) {
                        File file = new File(System.getenv("SystemRoot") + "\\system32\\drivers\\gm.dls");
                        if (file.exists()) {
                            try {
                                return new FileInputStream(file);
                            } catch (IOException e2) {
                                return null;
                            }
                        }
                        return null;
                    }
                    return null;
                }
            });
            arrayList.add(new PrivilegedAction<InputStream>() { // from class: com.sun.media.sound.SoftSynthesizer.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public InputStream run() {
                    File file = new File(new File(System.getProperty("user.home"), ".gervill"), "soundbank-emg.sf2");
                    if (file.exists()) {
                        try {
                            return new FileInputStream(file);
                        } catch (IOException e2) {
                            return null;
                        }
                    }
                    return null;
                }
            });
            Iterator<E> it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    inputStream = (InputStream) AccessController.doPrivileged((PrivilegedAction) it.next());
                } catch (Exception e2) {
                }
                if (inputStream != null) {
                    try {
                        Soundbank soundbank = MidiSystem.getSoundbank(new BufferedInputStream(inputStream));
                        inputStream.close();
                        if (soundbank != null) {
                            defaultSoundBank = soundbank;
                            return defaultSoundBank;
                        }
                    } catch (Throwable th) {
                        inputStream.close();
                        throw th;
                    }
                }
            }
            try {
                defaultSoundBank = EmergencySoundbank.createSoundbank();
            } catch (Exception e3) {
            }
            if (defaultSoundBank != null && (outputStream = (OutputStream) AccessController.doPrivileged(() -> {
                try {
                    File file = new File(System.getProperty("user.home"), ".gervill");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File file2 = new File(file, "soundbank-emg.sf2");
                    if (file2.exists()) {
                        return null;
                    }
                    return new FileOutputStream(file2);
                } catch (FileNotFoundException e4) {
                    return null;
                }
            })) != null) {
                try {
                    ((SF2Soundbank) defaultSoundBank).save(outputStream);
                    outputStream.close();
                } catch (IOException e4) {
                }
            }
            return defaultSoundBank;
        }
    }

    @Override // javax.sound.midi.Synthesizer
    public Instrument[] getAvailableInstruments() {
        Soundbank defaultSoundbank = getDefaultSoundbank();
        if (defaultSoundbank == null) {
            return new Instrument[0];
        }
        Instrument[] instruments = defaultSoundbank.getInstruments();
        Arrays.sort(instruments, new ModelInstrumentComparator());
        return instruments;
    }

    @Override // javax.sound.midi.Synthesizer
    public Instrument[] getLoadedInstruments() {
        ModelInstrument[] modelInstrumentArr;
        if (!isOpen()) {
            return new Instrument[0];
        }
        synchronized (this.control_mutex) {
            modelInstrumentArr = new ModelInstrument[this.loadedlist.values().size()];
            this.loadedlist.values().toArray(modelInstrumentArr);
            Arrays.sort(modelInstrumentArr, new ModelInstrumentComparator());
        }
        return modelInstrumentArr;
    }

    @Override // javax.sound.midi.Synthesizer
    public boolean loadAllInstruments(Soundbank soundbank) {
        ArrayList arrayList = new ArrayList();
        for (Instrument instrument : soundbank.getInstruments()) {
            if (instrument == null || !(instrument instanceof ModelInstrument)) {
                throw new IllegalArgumentException("Unsupported instrument: " + ((Object) instrument));
            }
            arrayList.add((ModelInstrument) instrument);
        }
        return loadInstruments(arrayList);
    }

    @Override // javax.sound.midi.Synthesizer
    public void unloadAllInstruments(Soundbank soundbank) {
        if (soundbank == null || !isSoundbankSupported(soundbank)) {
            throw new IllegalArgumentException("Unsupported soundbank: " + ((Object) soundbank));
        }
        if (!isOpen()) {
            return;
        }
        for (Instrument instrument : soundbank.getInstruments()) {
            if (instrument instanceof ModelInstrument) {
                unloadInstrument(instrument);
            }
        }
    }

    @Override // javax.sound.midi.Synthesizer
    public boolean loadInstruments(Soundbank soundbank, Patch[] patchArr) {
        ArrayList arrayList = new ArrayList();
        for (Patch patch : patchArr) {
            Instrument instrument = soundbank.getInstrument(patch);
            if (instrument == null || !(instrument instanceof ModelInstrument)) {
                throw new IllegalArgumentException("Unsupported instrument: " + ((Object) instrument));
            }
            arrayList.add((ModelInstrument) instrument);
        }
        return loadInstruments(arrayList);
    }

    @Override // javax.sound.midi.Synthesizer
    public void unloadInstruments(Soundbank soundbank, Patch[] patchArr) {
        if (soundbank == null || !isSoundbankSupported(soundbank)) {
            throw new IllegalArgumentException("Unsupported soundbank: " + ((Object) soundbank));
        }
        if (!isOpen()) {
            return;
        }
        for (Patch patch : patchArr) {
            Instrument instrument = soundbank.getInstrument(patch);
            if (instrument instanceof ModelInstrument) {
                unloadInstrument(instrument);
            }
        }
    }

    @Override // javax.sound.midi.MidiDevice
    public MidiDevice.Info getDeviceInfo() {
        return info;
    }

    private Properties getStoredProperties() {
        return (Properties) AccessController.doPrivileged(() -> {
            Properties properties = new Properties();
            try {
                Preferences preferencesUserRoot = Preferences.userRoot();
                if (preferencesUserRoot.nodeExists("/com/sun/media/sound/softsynthesizer")) {
                    Preferences preferencesNode = preferencesUserRoot.node("/com/sun/media/sound/softsynthesizer");
                    for (String str : preferencesNode.keys()) {
                        String str2 = preferencesNode.get(str, null);
                        if (str2 != null) {
                            properties.setProperty(str, str2);
                        }
                    }
                }
            } catch (BackingStoreException e2) {
            }
            return properties;
        });
    }

    @Override // com.sun.media.sound.AudioSynthesizer
    public AudioSynthesizerPropertyInfo[] getPropertyInfo(Map<String, Object> map) {
        ArrayList arrayList = new ArrayList();
        boolean z2 = map == null && this.open;
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo = new AudioSynthesizerPropertyInfo("interpolation", z2 ? this.resamplerType : "linear");
        audioSynthesizerPropertyInfo.choices = new String[]{"linear", "linear1", "linear2", "cubic", "lanczos", "sinc", IssuingDistributionPointExtension.POINT};
        audioSynthesizerPropertyInfo.description = "Interpolation method";
        arrayList.add(audioSynthesizerPropertyInfo);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo2 = new AudioSynthesizerPropertyInfo("control rate", Float.valueOf(z2 ? this.controlrate : 147.0f));
        audioSynthesizerPropertyInfo2.description = "Control rate";
        arrayList.add(audioSynthesizerPropertyInfo2);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo3 = new AudioSynthesizerPropertyInfo(Constants.ATTRNAME_FORMAT, z2 ? this.format : new AudioFormat(44100.0f, 16, 2, true, false));
        audioSynthesizerPropertyInfo3.description = "Default audio format";
        arrayList.add(audioSynthesizerPropertyInfo3);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo4 = new AudioSynthesizerPropertyInfo("latency", Long.valueOf(z2 ? this.latency : 120000L));
        audioSynthesizerPropertyInfo4.description = "Default latency";
        arrayList.add(audioSynthesizerPropertyInfo4);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo5 = new AudioSynthesizerPropertyInfo("device id", Integer.valueOf(z2 ? this.deviceid : 0));
        audioSynthesizerPropertyInfo5.description = "Device ID for SysEx Messages";
        arrayList.add(audioSynthesizerPropertyInfo5);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo6 = new AudioSynthesizerPropertyInfo("max polyphony", Integer.valueOf(z2 ? this.maxpoly : 64));
        audioSynthesizerPropertyInfo6.description = "Maximum polyphony";
        arrayList.add(audioSynthesizerPropertyInfo6);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo7 = new AudioSynthesizerPropertyInfo("reverb", Boolean.valueOf(z2 ? this.reverb_on : true));
        audioSynthesizerPropertyInfo7.description = "Turn reverb effect on or off";
        arrayList.add(audioSynthesizerPropertyInfo7);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo8 = new AudioSynthesizerPropertyInfo("chorus", Boolean.valueOf(z2 ? this.chorus_on : true));
        audioSynthesizerPropertyInfo8.description = "Turn chorus effect on or off";
        arrayList.add(audioSynthesizerPropertyInfo8);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo9 = new AudioSynthesizerPropertyInfo("auto gain control", Boolean.valueOf(z2 ? this.agc_on : true));
        audioSynthesizerPropertyInfo9.description = "Turn auto gain control on or off";
        arrayList.add(audioSynthesizerPropertyInfo9);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo10 = new AudioSynthesizerPropertyInfo("large mode", Boolean.valueOf(z2 ? this.largemode : false));
        audioSynthesizerPropertyInfo10.description = "Turn large mode on or off.";
        arrayList.add(audioSynthesizerPropertyInfo10);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo11 = new AudioSynthesizerPropertyInfo("midi channels", Integer.valueOf(z2 ? this.channels.length : 16));
        audioSynthesizerPropertyInfo11.description = "Number of midi channels.";
        arrayList.add(audioSynthesizerPropertyInfo11);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo12 = new AudioSynthesizerPropertyInfo("jitter correction", Boolean.valueOf(z2 ? this.jitter_correction : true));
        audioSynthesizerPropertyInfo12.description = "Turn jitter correction on or off.";
        arrayList.add(audioSynthesizerPropertyInfo12);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo13 = new AudioSynthesizerPropertyInfo("light reverb", Boolean.valueOf(z2 ? this.reverb_light : true));
        audioSynthesizerPropertyInfo13.description = "Turn light reverb mode on or off";
        arrayList.add(audioSynthesizerPropertyInfo13);
        AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo14 = new AudioSynthesizerPropertyInfo("load default soundbank", Boolean.valueOf(z2 ? this.load_default_soundbank : true));
        audioSynthesizerPropertyInfo14.description = "Enabled/disable loading default soundbank";
        arrayList.add(audioSynthesizerPropertyInfo14);
        AudioSynthesizerPropertyInfo[] audioSynthesizerPropertyInfoArr = (AudioSynthesizerPropertyInfo[]) arrayList.toArray(new AudioSynthesizerPropertyInfo[arrayList.size()]);
        Properties storedProperties = getStoredProperties();
        for (AudioSynthesizerPropertyInfo audioSynthesizerPropertyInfo15 : audioSynthesizerPropertyInfoArr) {
            Object obj = map == null ? null : map.get(audioSynthesizerPropertyInfo15.name);
            Object property = obj != null ? obj : storedProperties.getProperty(audioSynthesizerPropertyInfo15.name);
            if (property != null) {
                Class cls = audioSynthesizerPropertyInfo15.valueClass;
                if (cls.isInstance(property)) {
                    audioSynthesizerPropertyInfo15.value = property;
                } else if (property instanceof String) {
                    String str = (String) property;
                    if (cls == Boolean.class) {
                        if (str.equalsIgnoreCase("true")) {
                            audioSynthesizerPropertyInfo15.value = Boolean.TRUE;
                        }
                        if (str.equalsIgnoreCase("false")) {
                            audioSynthesizerPropertyInfo15.value = Boolean.FALSE;
                        }
                    } else if (cls == AudioFormat.class) {
                        int i2 = 2;
                        boolean z3 = true;
                        boolean z4 = false;
                        int i3 = 16;
                        float f2 = 44100.0f;
                        try {
                            StringTokenizer stringTokenizer = new StringTokenizer(str, ", ");
                            String str2 = "";
                            while (stringTokenizer.hasMoreTokens()) {
                                String lowerCase = stringTokenizer.nextToken().toLowerCase();
                                if (lowerCase.equals("mono")) {
                                    i2 = 1;
                                }
                                if (lowerCase.startsWith("channel")) {
                                    i2 = Integer.parseInt(str2);
                                }
                                if (lowerCase.contains("unsigned")) {
                                    z3 = false;
                                }
                                if (lowerCase.equals("big-endian")) {
                                    z4 = true;
                                }
                                if (lowerCase.equals("bit")) {
                                    i3 = Integer.parseInt(str2);
                                }
                                if (lowerCase.equals("hz")) {
                                    f2 = Float.parseFloat(str2);
                                }
                                str2 = lowerCase;
                            }
                            audioSynthesizerPropertyInfo15.value = new AudioFormat(f2, i3, i2, z3, z4);
                        } catch (NumberFormatException e2) {
                        }
                    } else if (cls == Byte.class) {
                        try {
                            audioSynthesizerPropertyInfo15.value = Byte.valueOf(str);
                        } catch (NumberFormatException e3) {
                        }
                    } else if (cls == Short.class) {
                        audioSynthesizerPropertyInfo15.value = Short.valueOf(str);
                    } else if (cls == Integer.class) {
                        audioSynthesizerPropertyInfo15.value = Integer.valueOf(str);
                    } else if (cls == Long.class) {
                        audioSynthesizerPropertyInfo15.value = Long.valueOf(str);
                    } else if (cls == Float.class) {
                        audioSynthesizerPropertyInfo15.value = Float.valueOf(str);
                    } else if (cls == Double.class) {
                        audioSynthesizerPropertyInfo15.value = Double.valueOf(str);
                    }
                } else if (property instanceof Number) {
                    Number number = (Number) property;
                    if (cls == Byte.class) {
                        audioSynthesizerPropertyInfo15.value = Byte.valueOf(number.byteValue());
                    }
                    if (cls == Short.class) {
                        audioSynthesizerPropertyInfo15.value = Short.valueOf(number.shortValue());
                    }
                    if (cls == Integer.class) {
                        audioSynthesizerPropertyInfo15.value = Integer.valueOf(number.intValue());
                    }
                    if (cls == Long.class) {
                        audioSynthesizerPropertyInfo15.value = Long.valueOf(number.longValue());
                    }
                    if (cls == Float.class) {
                        audioSynthesizerPropertyInfo15.value = Float.valueOf(number.floatValue());
                    }
                    if (cls == Double.class) {
                        audioSynthesizerPropertyInfo15.value = Double.valueOf(number.doubleValue());
                    }
                }
            }
        }
        return audioSynthesizerPropertyInfoArr;
    }

    @Override // javax.sound.midi.MidiDevice
    public void open() throws MidiUnavailableException {
        if (isOpen()) {
            synchronized (this.control_mutex) {
                this.implicitOpen = false;
            }
            return;
        }
        open(null, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x005a A[Catch: IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, all -> 0x0177, TryCatch #3 {IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, blocks: (B:19:0x002b, B:20:0x0035, B:22:0x005a, B:24:0x0060, B:25:0x0067, B:26:0x006f, B:28:0x007f, B:29:0x00aa, B:31:0x00b3, B:33:0x00be, B:35:0x00ca, B:37:0x00e5, B:38:0x00eb, B:40:0x00f2, B:42:0x0108, B:43:0x0111, B:45:0x0135), top: B:67:0x002b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007f A[Catch: IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, all -> 0x0177, TryCatch #3 {IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, blocks: (B:19:0x002b, B:20:0x0035, B:22:0x005a, B:24:0x0060, B:25:0x0067, B:26:0x006f, B:28:0x007f, B:29:0x00aa, B:31:0x00b3, B:33:0x00be, B:35:0x00ca, B:37:0x00e5, B:38:0x00eb, B:40:0x00f2, B:42:0x0108, B:43:0x0111, B:45:0x0135), top: B:67:0x002b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b3 A[Catch: IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, all -> 0x0177, TryCatch #3 {IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, blocks: (B:19:0x002b, B:20:0x0035, B:22:0x005a, B:24:0x0060, B:25:0x0067, B:26:0x006f, B:28:0x007f, B:29:0x00aa, B:31:0x00b3, B:33:0x00be, B:35:0x00ca, B:37:0x00e5, B:38:0x00eb, B:40:0x00f2, B:42:0x0108, B:43:0x0111, B:45:0x0135), top: B:67:0x002b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e5 A[Catch: IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, all -> 0x0177, TryCatch #3 {IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, blocks: (B:19:0x002b, B:20:0x0035, B:22:0x005a, B:24:0x0060, B:25:0x0067, B:26:0x006f, B:28:0x007f, B:29:0x00aa, B:31:0x00b3, B:33:0x00be, B:35:0x00ca, B:37:0x00e5, B:38:0x00eb, B:40:0x00f2, B:42:0x0108, B:43:0x0111, B:45:0x0135), top: B:67:0x002b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f2 A[Catch: IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, all -> 0x0177, TryCatch #3 {IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, blocks: (B:19:0x002b, B:20:0x0035, B:22:0x005a, B:24:0x0060, B:25:0x0067, B:26:0x006f, B:28:0x007f, B:29:0x00aa, B:31:0x00b3, B:33:0x00be, B:35:0x00ca, B:37:0x00e5, B:38:0x00eb, B:40:0x00f2, B:42:0x0108, B:43:0x0111, B:45:0x0135), top: B:67:0x002b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0135 A[Catch: IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, all -> 0x0177, TryCatch #3 {IllegalArgumentException | SecurityException | LineUnavailableException -> 0x014e, blocks: (B:19:0x002b, B:20:0x0035, B:22:0x005a, B:24:0x0060, B:25:0x0067, B:26:0x006f, B:28:0x007f, B:29:0x00aa, B:31:0x00b3, B:33:0x00be, B:35:0x00ca, B:37:0x00e5, B:38:0x00eb, B:40:0x00f2, B:42:0x0108, B:43:0x0111, B:45:0x0135), top: B:67:0x002b, outer: #1 }] */
    @Override // com.sun.media.sound.AudioSynthesizer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void open(javax.sound.sampled.SourceDataLine r9, java.util.Map<java.lang.String, java.lang.Object> r10) throws javax.sound.midi.MidiUnavailableException {
        /*
            Method dump skipped, instructions count: 383
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.media.sound.SoftSynthesizer.open(javax.sound.sampled.SourceDataLine, java.util.Map):void");
    }

    @Override // com.sun.media.sound.AudioSynthesizer
    public AudioInputStream openStream(AudioFormat audioFormat, Map<String, Object> map) throws MidiUnavailableException {
        AudioInputStream inputStream;
        Soundbank defaultSoundbank;
        if (isOpen()) {
            throw new MidiUnavailableException("Synthesizer is already open");
        }
        synchronized (this.control_mutex) {
            this.gmmode = 0;
            this.voice_allocation_mode = 0;
            processPropertyInfo(map);
            this.open = true;
            this.implicitOpen = false;
            if (audioFormat != null) {
                setFormat(audioFormat);
            }
            if (this.load_default_soundbank && (defaultSoundbank = getDefaultSoundbank()) != null) {
                loadAllInstruments(defaultSoundbank);
            }
            this.voices = new SoftVoice[this.maxpoly];
            for (int i2 = 0; i2 < this.maxpoly; i2++) {
                this.voices[i2] = new SoftVoice(this);
            }
            this.mainmixer = new SoftMainMixer(this);
            this.channels = new SoftChannel[this.number_of_midi_channels];
            for (int i3 = 0; i3 < this.channels.length; i3++) {
                this.channels[i3] = new SoftChannel(this, i3);
            }
            if (this.external_channels == null) {
                if (this.channels.length < 16) {
                    this.external_channels = new SoftChannelProxy[16];
                } else {
                    this.external_channels = new SoftChannelProxy[this.channels.length];
                }
                for (int i4 = 0; i4 < this.external_channels.length; i4++) {
                    this.external_channels[i4] = new SoftChannelProxy();
                }
            } else if (this.channels.length > this.external_channels.length) {
                SoftChannelProxy[] softChannelProxyArr = new SoftChannelProxy[this.channels.length];
                for (int i5 = 0; i5 < this.external_channels.length; i5++) {
                    softChannelProxyArr[i5] = this.external_channels[i5];
                }
                for (int length = this.external_channels.length; length < softChannelProxyArr.length; length++) {
                    softChannelProxyArr[length] = new SoftChannelProxy();
                }
            }
            for (int i6 = 0; i6 < this.channels.length; i6++) {
                this.external_channels[i6].setChannel(this.channels[i6]);
            }
            for (SoftVoice softVoice : getVoices()) {
                softVoice.resampler = this.resampler.openStreamer();
            }
            Iterator<Receiver> it = getReceivers().iterator();
            while (it.hasNext()) {
                SoftReceiver softReceiver = (SoftReceiver) it.next();
                softReceiver.open = this.open;
                softReceiver.mainmixer = this.mainmixer;
                softReceiver.midimessages = this.mainmixer.midimessages;
            }
            inputStream = this.mainmixer.getInputStream();
        }
        return inputStream;
    }

    @Override // javax.sound.midi.MidiDevice, java.lang.AutoCloseable
    public void close() {
        if (!isOpen()) {
            return;
        }
        SoftAudioPusher softAudioPusher = null;
        AudioInputStream audioInputStream = null;
        synchronized (this.control_mutex) {
            if (this.pusher != null) {
                softAudioPusher = this.pusher;
                audioInputStream = this.pusher_stream;
                this.pusher = null;
                this.pusher_stream = null;
            }
        }
        if (softAudioPusher != null) {
            softAudioPusher.stop();
            try {
                audioInputStream.close();
            } catch (IOException e2) {
            }
        }
        synchronized (this.control_mutex) {
            if (this.mainmixer != null) {
                this.mainmixer.close();
            }
            this.open = false;
            this.implicitOpen = false;
            this.mainmixer = null;
            this.voices = null;
            this.channels = null;
            if (this.external_channels != null) {
                for (int i2 = 0; i2 < this.external_channels.length; i2++) {
                    this.external_channels[i2].setChannel(null);
                }
            }
            if (this.sourceDataLine != null) {
                this.sourceDataLine.close();
                this.sourceDataLine = null;
            }
            this.inslist.clear();
            this.loadedlist.clear();
            this.tunings.clear();
            while (this.recvslist.size() != 0) {
                this.recvslist.get(this.recvslist.size() - 1).close();
            }
        }
    }

    @Override // javax.sound.midi.MidiDevice
    public boolean isOpen() {
        boolean z2;
        synchronized (this.control_mutex) {
            z2 = this.open;
        }
        return z2;
    }

    @Override // javax.sound.midi.MidiDevice
    public long getMicrosecondPosition() {
        long microsecondPosition;
        if (!isOpen()) {
            return 0L;
        }
        synchronized (this.control_mutex) {
            microsecondPosition = this.mainmixer.getMicrosecondPosition();
        }
        return microsecondPosition;
    }

    @Override // javax.sound.midi.MidiDevice
    public int getMaxReceivers() {
        return -1;
    }

    @Override // javax.sound.midi.MidiDevice
    public int getMaxTransmitters() {
        return 0;
    }

    @Override // javax.sound.midi.MidiDevice
    public Receiver getReceiver() throws MidiUnavailableException {
        SoftReceiver softReceiver;
        synchronized (this.control_mutex) {
            softReceiver = new SoftReceiver(this);
            softReceiver.open = this.open;
            this.recvslist.add(softReceiver);
        }
        return softReceiver;
    }

    @Override // javax.sound.midi.MidiDevice
    public List<Receiver> getReceivers() {
        ArrayList arrayList;
        synchronized (this.control_mutex) {
            arrayList = new ArrayList();
            arrayList.addAll(this.recvslist);
        }
        return arrayList;
    }

    @Override // javax.sound.midi.MidiDevice
    public Transmitter getTransmitter() throws MidiUnavailableException {
        throw new MidiUnavailableException("No transmitter available");
    }

    @Override // javax.sound.midi.MidiDevice
    public List<Transmitter> getTransmitters() {
        return new ArrayList();
    }

    @Override // com.sun.media.sound.ReferenceCountingDevice
    public Receiver getReceiverReferenceCounting() throws MidiUnavailableException {
        if (!isOpen()) {
            open();
            synchronized (this.control_mutex) {
                this.implicitOpen = true;
            }
        }
        return getReceiver();
    }

    @Override // com.sun.media.sound.ReferenceCountingDevice
    public Transmitter getTransmitterReferenceCounting() throws MidiUnavailableException {
        throw new MidiUnavailableException("No transmitter available");
    }
}
