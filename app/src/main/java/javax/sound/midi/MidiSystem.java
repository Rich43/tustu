package javax.sound.midi;

import com.sun.media.sound.AutoConnectSequencer;
import com.sun.media.sound.JDK13Services;
import com.sun.media.sound.MidiDeviceReceiverEnvelope;
import com.sun.media.sound.MidiDeviceTransmitterEnvelope;
import com.sun.media.sound.ReferenceCountingDevice;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;
import javax.sound.midi.spi.MidiFileReader;
import javax.sound.midi.spi.MidiFileWriter;
import javax.sound.midi.spi.SoundbankReader;

/* loaded from: rt.jar:javax/sound/midi/MidiSystem.class */
public class MidiSystem {
    private MidiSystem() {
    }

    public static MidiDevice.Info[] getMidiDeviceInfo() {
        ArrayList arrayList = new ArrayList();
        List midiDeviceProviders = getMidiDeviceProviders();
        for (int i2 = 0; i2 < midiDeviceProviders.size(); i2++) {
            for (MidiDevice.Info info : ((MidiDeviceProvider) midiDeviceProviders.get(i2)).getDeviceInfo()) {
                arrayList.add(info);
            }
        }
        return (MidiDevice.Info[]) arrayList.toArray(new MidiDevice.Info[0]);
    }

    public static MidiDevice getMidiDevice(MidiDevice.Info info) throws MidiUnavailableException {
        List midiDeviceProviders = getMidiDeviceProviders();
        for (int i2 = 0; i2 < midiDeviceProviders.size(); i2++) {
            MidiDeviceProvider midiDeviceProvider = (MidiDeviceProvider) midiDeviceProviders.get(i2);
            if (midiDeviceProvider.isDeviceSupported(info)) {
                return midiDeviceProvider.getDevice(info);
            }
        }
        throw new IllegalArgumentException("Requested device not installed: " + ((Object) info));
    }

    public static Receiver getReceiver() throws MidiUnavailableException {
        Receiver receiver;
        MidiDevice defaultDeviceWrapper = getDefaultDeviceWrapper(Receiver.class);
        if (defaultDeviceWrapper instanceof ReferenceCountingDevice) {
            receiver = ((ReferenceCountingDevice) defaultDeviceWrapper).getReceiverReferenceCounting();
        } else {
            receiver = defaultDeviceWrapper.getReceiver();
        }
        if (!(receiver instanceof MidiDeviceReceiver)) {
            receiver = new MidiDeviceReceiverEnvelope(defaultDeviceWrapper, receiver);
        }
        return receiver;
    }

    public static Transmitter getTransmitter() throws MidiUnavailableException {
        Transmitter transmitter;
        MidiDevice defaultDeviceWrapper = getDefaultDeviceWrapper(Transmitter.class);
        if (defaultDeviceWrapper instanceof ReferenceCountingDevice) {
            transmitter = ((ReferenceCountingDevice) defaultDeviceWrapper).getTransmitterReferenceCounting();
        } else {
            transmitter = defaultDeviceWrapper.getTransmitter();
        }
        if (!(transmitter instanceof MidiDeviceTransmitter)) {
            transmitter = new MidiDeviceTransmitterEnvelope(defaultDeviceWrapper, transmitter);
        }
        return transmitter;
    }

    public static Synthesizer getSynthesizer() throws MidiUnavailableException {
        return (Synthesizer) getDefaultDeviceWrapper(Synthesizer.class);
    }

    public static Sequencer getSequencer() throws MidiUnavailableException {
        return getSequencer(true);
    }

    /* JADX WARN: Finally extract failed */
    public static Sequencer getSequencer(boolean z2) throws MidiUnavailableException {
        Sequencer sequencer = (Sequencer) getDefaultDeviceWrapper(Sequencer.class);
        if (z2) {
            Receiver receiver = null;
            MidiUnavailableException midiUnavailableException = null;
            try {
                Synthesizer synthesizer = getSynthesizer();
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
            } catch (MidiUnavailableException e2) {
                if (e2 instanceof MidiUnavailableException) {
                    midiUnavailableException = e2;
                }
            }
            if (receiver == null) {
                try {
                    receiver = getReceiver();
                } catch (Exception e3) {
                    if (e3 instanceof MidiUnavailableException) {
                        midiUnavailableException = (MidiUnavailableException) e3;
                    }
                }
            }
            if (receiver != null) {
                sequencer.getTransmitter().setReceiver(receiver);
                if (sequencer instanceof AutoConnectSequencer) {
                    ((AutoConnectSequencer) sequencer).setAutoConnect(receiver);
                }
            } else {
                if (midiUnavailableException != null) {
                    throw midiUnavailableException;
                }
                throw new MidiUnavailableException("no receiver available");
            }
        }
        return sequencer;
    }

    public static Soundbank getSoundbank(InputStream inputStream) throws InvalidMidiDataException, IOException {
        List soundbankReaders = getSoundbankReaders();
        for (int i2 = 0; i2 < soundbankReaders.size(); i2++) {
            Soundbank soundbank = ((SoundbankReader) soundbankReaders.get(i2)).getSoundbank(inputStream);
            if (soundbank != null) {
                return soundbank;
            }
        }
        throw new InvalidMidiDataException("cannot get soundbank from stream");
    }

    public static Soundbank getSoundbank(URL url) throws InvalidMidiDataException, IOException {
        List soundbankReaders = getSoundbankReaders();
        for (int i2 = 0; i2 < soundbankReaders.size(); i2++) {
            Soundbank soundbank = ((SoundbankReader) soundbankReaders.get(i2)).getSoundbank(url);
            if (soundbank != null) {
                return soundbank;
            }
        }
        throw new InvalidMidiDataException("cannot get soundbank from stream");
    }

    public static Soundbank getSoundbank(File file) throws InvalidMidiDataException, IOException {
        List soundbankReaders = getSoundbankReaders();
        for (int i2 = 0; i2 < soundbankReaders.size(); i2++) {
            Soundbank soundbank = ((SoundbankReader) soundbankReaders.get(i2)).getSoundbank(file);
            if (soundbank != null) {
                return soundbank;
            }
        }
        throw new InvalidMidiDataException("cannot get soundbank from stream");
    }

    public static MidiFileFormat getMidiFileFormat(InputStream inputStream) throws InvalidMidiDataException, IOException {
        List midiFileReaders = getMidiFileReaders();
        MidiFileFormat midiFileFormat = null;
        for (int i2 = 0; i2 < midiFileReaders.size(); i2++) {
            try {
                midiFileFormat = ((MidiFileReader) midiFileReaders.get(i2)).getMidiFileFormat(inputStream);
                break;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (midiFileFormat == null) {
            throw new InvalidMidiDataException("input stream is not a supported file type");
        }
        return midiFileFormat;
    }

    public static MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException {
        List midiFileReaders = getMidiFileReaders();
        MidiFileFormat midiFileFormat = null;
        for (int i2 = 0; i2 < midiFileReaders.size(); i2++) {
            try {
                midiFileFormat = ((MidiFileReader) midiFileReaders.get(i2)).getMidiFileFormat(url);
                break;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (midiFileFormat == null) {
            throw new InvalidMidiDataException("url is not a supported file type");
        }
        return midiFileFormat;
    }

    public static MidiFileFormat getMidiFileFormat(File file) throws InvalidMidiDataException, IOException {
        List midiFileReaders = getMidiFileReaders();
        MidiFileFormat midiFileFormat = null;
        for (int i2 = 0; i2 < midiFileReaders.size(); i2++) {
            try {
                midiFileFormat = ((MidiFileReader) midiFileReaders.get(i2)).getMidiFileFormat(file);
                break;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (midiFileFormat == null) {
            throw new InvalidMidiDataException("file is not a supported file type");
        }
        return midiFileFormat;
    }

    public static Sequence getSequence(InputStream inputStream) throws InvalidMidiDataException, IOException {
        List midiFileReaders = getMidiFileReaders();
        Sequence sequence = null;
        for (int i2 = 0; i2 < midiFileReaders.size(); i2++) {
            try {
                sequence = ((MidiFileReader) midiFileReaders.get(i2)).getSequence(inputStream);
                break;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (sequence == null) {
            throw new InvalidMidiDataException("could not get sequence from input stream");
        }
        return sequence;
    }

    public static Sequence getSequence(URL url) throws InvalidMidiDataException, IOException {
        List midiFileReaders = getMidiFileReaders();
        Sequence sequence = null;
        for (int i2 = 0; i2 < midiFileReaders.size(); i2++) {
            try {
                sequence = ((MidiFileReader) midiFileReaders.get(i2)).getSequence(url);
                break;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (sequence == null) {
            throw new InvalidMidiDataException("could not get sequence from URL");
        }
        return sequence;
    }

    public static Sequence getSequence(File file) throws InvalidMidiDataException, IOException {
        List midiFileReaders = getMidiFileReaders();
        Sequence sequence = null;
        for (int i2 = 0; i2 < midiFileReaders.size(); i2++) {
            try {
                sequence = ((MidiFileReader) midiFileReaders.get(i2)).getSequence(file);
                break;
            } catch (InvalidMidiDataException e2) {
            }
        }
        if (sequence == null) {
            throw new InvalidMidiDataException("could not get sequence from file");
        }
        return sequence;
    }

    public static int[] getMidiFileTypes() {
        List midiFileWriters = getMidiFileWriters();
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < midiFileWriters.size(); i2++) {
            for (int i3 : ((MidiFileWriter) midiFileWriters.get(i2)).getMidiFileTypes()) {
                hashSet.add(new Integer(i3));
            }
        }
        int[] iArr = new int[hashSet.size()];
        int i4 = 0;
        Iterator<E> it = hashSet.iterator();
        while (it.hasNext()) {
            int i5 = i4;
            i4++;
            iArr[i5] = ((Integer) it.next()).intValue();
        }
        return iArr;
    }

    public static boolean isFileTypeSupported(int i2) {
        List midiFileWriters = getMidiFileWriters();
        for (int i3 = 0; i3 < midiFileWriters.size(); i3++) {
            if (((MidiFileWriter) midiFileWriters.get(i3)).isFileTypeSupported(i2)) {
                return true;
            }
        }
        return false;
    }

    public static int[] getMidiFileTypes(Sequence sequence) {
        List midiFileWriters = getMidiFileWriters();
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < midiFileWriters.size(); i2++) {
            for (int i3 : ((MidiFileWriter) midiFileWriters.get(i2)).getMidiFileTypes(sequence)) {
                hashSet.add(new Integer(i3));
            }
        }
        int[] iArr = new int[hashSet.size()];
        int i4 = 0;
        Iterator<E> it = hashSet.iterator();
        while (it.hasNext()) {
            int i5 = i4;
            i4++;
            iArr[i5] = ((Integer) it.next()).intValue();
        }
        return iArr;
    }

    public static boolean isFileTypeSupported(int i2, Sequence sequence) {
        List midiFileWriters = getMidiFileWriters();
        for (int i3 = 0; i3 < midiFileWriters.size(); i3++) {
            if (((MidiFileWriter) midiFileWriters.get(i3)).isFileTypeSupported(i2, sequence)) {
                return true;
            }
        }
        return false;
    }

    public static int write(Sequence sequence, int i2, OutputStream outputStream) throws IOException {
        List midiFileWriters = getMidiFileWriters();
        int iWrite = -2;
        int i3 = 0;
        while (true) {
            if (i3 >= midiFileWriters.size()) {
                break;
            }
            MidiFileWriter midiFileWriter = (MidiFileWriter) midiFileWriters.get(i3);
            if (!midiFileWriter.isFileTypeSupported(i2, sequence)) {
                i3++;
            } else {
                iWrite = midiFileWriter.write(sequence, i2, outputStream);
                break;
            }
        }
        if (iWrite == -2) {
            throw new IllegalArgumentException("MIDI file type is not supported");
        }
        return iWrite;
    }

    public static int write(Sequence sequence, int i2, File file) throws IOException {
        List midiFileWriters = getMidiFileWriters();
        int iWrite = -2;
        int i3 = 0;
        while (true) {
            if (i3 >= midiFileWriters.size()) {
                break;
            }
            MidiFileWriter midiFileWriter = (MidiFileWriter) midiFileWriters.get(i3);
            if (!midiFileWriter.isFileTypeSupported(i2, sequence)) {
                i3++;
            } else {
                iWrite = midiFileWriter.write(sequence, i2, file);
                break;
            }
        }
        if (iWrite == -2) {
            throw new IllegalArgumentException("MIDI file type is not supported");
        }
        return iWrite;
    }

    private static List getMidiDeviceProviders() {
        return getProviders(MidiDeviceProvider.class);
    }

    private static List getSoundbankReaders() {
        return getProviders(SoundbankReader.class);
    }

    private static List getMidiFileWriters() {
        return getProviders(MidiFileWriter.class);
    }

    private static List getMidiFileReaders() {
        return getProviders(MidiFileReader.class);
    }

    private static MidiDevice getDefaultDeviceWrapper(Class cls) throws MidiUnavailableException {
        try {
            return getDefaultDevice(cls);
        } catch (IllegalArgumentException e2) {
            MidiUnavailableException midiUnavailableException = new MidiUnavailableException();
            midiUnavailableException.initCause(e2);
            throw midiUnavailableException;
        }
    }

    private static MidiDevice getDefaultDevice(Class cls) {
        MidiDevice namedDevice;
        MidiDeviceProvider namedProvider;
        MidiDevice namedDevice2;
        List midiDeviceProviders = getMidiDeviceProviders();
        String defaultProviderClassName = JDK13Services.getDefaultProviderClassName(cls);
        String defaultInstanceName = JDK13Services.getDefaultInstanceName(cls);
        if (defaultProviderClassName != null && (namedProvider = getNamedProvider(defaultProviderClassName, midiDeviceProviders)) != null) {
            if (defaultInstanceName != null && (namedDevice2 = getNamedDevice(defaultInstanceName, namedProvider, cls)) != null) {
                return namedDevice2;
            }
            MidiDevice firstDevice = getFirstDevice(namedProvider, cls);
            if (firstDevice != null) {
                return firstDevice;
            }
        }
        if (defaultInstanceName != null && (namedDevice = getNamedDevice(defaultInstanceName, midiDeviceProviders, cls)) != null) {
            return namedDevice;
        }
        MidiDevice firstDevice2 = getFirstDevice(midiDeviceProviders, cls);
        if (firstDevice2 != null) {
            return firstDevice2;
        }
        throw new IllegalArgumentException("Requested device not installed");
    }

    private static MidiDeviceProvider getNamedProvider(String str, List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            MidiDeviceProvider midiDeviceProvider = (MidiDeviceProvider) list.get(i2);
            if (midiDeviceProvider.getClass().getName().equals(str)) {
                return midiDeviceProvider;
            }
        }
        return null;
    }

    private static MidiDevice getNamedDevice(String str, MidiDeviceProvider midiDeviceProvider, Class cls) {
        MidiDevice namedDevice;
        MidiDevice namedDevice2 = getNamedDevice(str, midiDeviceProvider, cls, false, false);
        if (namedDevice2 != null) {
            return namedDevice2;
        }
        if (cls == Receiver.class && (namedDevice = getNamedDevice(str, midiDeviceProvider, cls, true, false)) != null) {
            return namedDevice;
        }
        return null;
    }

    private static MidiDevice getNamedDevice(String str, MidiDeviceProvider midiDeviceProvider, Class cls, boolean z2, boolean z3) {
        MidiDevice.Info[] deviceInfo = midiDeviceProvider.getDeviceInfo();
        for (int i2 = 0; i2 < deviceInfo.length; i2++) {
            if (deviceInfo[i2].getName().equals(str)) {
                MidiDevice device = midiDeviceProvider.getDevice(deviceInfo[i2]);
                if (isAppropriateDevice(device, cls, z2, z3)) {
                    return device;
                }
            }
        }
        return null;
    }

    private static MidiDevice getNamedDevice(String str, List list, Class cls) {
        MidiDevice namedDevice;
        MidiDevice namedDevice2 = getNamedDevice(str, list, cls, false, false);
        if (namedDevice2 != null) {
            return namedDevice2;
        }
        if (cls == Receiver.class && (namedDevice = getNamedDevice(str, list, cls, true, false)) != null) {
            return namedDevice;
        }
        return null;
    }

    private static MidiDevice getNamedDevice(String str, List list, Class cls, boolean z2, boolean z3) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            MidiDevice namedDevice = getNamedDevice(str, (MidiDeviceProvider) list.get(i2), cls, z2, z3);
            if (namedDevice != null) {
                return namedDevice;
            }
        }
        return null;
    }

    private static MidiDevice getFirstDevice(MidiDeviceProvider midiDeviceProvider, Class cls) {
        MidiDevice firstDevice;
        MidiDevice firstDevice2 = getFirstDevice(midiDeviceProvider, cls, false, false);
        if (firstDevice2 != null) {
            return firstDevice2;
        }
        if (cls == Receiver.class && (firstDevice = getFirstDevice(midiDeviceProvider, cls, true, false)) != null) {
            return firstDevice;
        }
        return null;
    }

    private static MidiDevice getFirstDevice(MidiDeviceProvider midiDeviceProvider, Class cls, boolean z2, boolean z3) {
        for (MidiDevice.Info info : midiDeviceProvider.getDeviceInfo()) {
            MidiDevice device = midiDeviceProvider.getDevice(info);
            if (isAppropriateDevice(device, cls, z2, z3)) {
                return device;
            }
        }
        return null;
    }

    private static MidiDevice getFirstDevice(List list, Class cls) {
        MidiDevice firstDevice;
        MidiDevice firstDevice2 = getFirstDevice(list, cls, false, false);
        if (firstDevice2 != null) {
            return firstDevice2;
        }
        if (cls == Receiver.class && (firstDevice = getFirstDevice(list, cls, true, false)) != null) {
            return firstDevice;
        }
        return null;
    }

    private static MidiDevice getFirstDevice(List list, Class cls, boolean z2, boolean z3) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            MidiDevice firstDevice = getFirstDevice((MidiDeviceProvider) list.get(i2), cls, z2, z3);
            if (firstDevice != null) {
                return firstDevice;
            }
        }
        return null;
    }

    private static boolean isAppropriateDevice(MidiDevice midiDevice, Class cls, boolean z2, boolean z3) {
        if (cls.isInstance(midiDevice)) {
            return true;
        }
        if ((!(midiDevice instanceof Sequencer) && !(midiDevice instanceof Synthesizer)) || (((midiDevice instanceof Sequencer) && z3) || ((midiDevice instanceof Synthesizer) && z2))) {
            if (cls != Receiver.class || midiDevice.getMaxReceivers() == 0) {
                if (cls == Transmitter.class && midiDevice.getMaxTransmitters() != 0) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    private static List getProviders(Class cls) {
        return JDK13Services.getProviders(cls);
    }
}
