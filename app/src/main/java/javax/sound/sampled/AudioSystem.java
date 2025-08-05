package javax.sound.sampled;

import com.sun.media.sound.JDK13Services;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.spi.AudioFileReader;
import javax.sound.sampled.spi.AudioFileWriter;
import javax.sound.sampled.spi.FormatConversionProvider;
import javax.sound.sampled.spi.MixerProvider;

/* loaded from: rt.jar:javax/sound/sampled/AudioSystem.class */
public class AudioSystem {
    public static final int NOT_SPECIFIED = -1;

    private AudioSystem() {
    }

    public static Mixer.Info[] getMixerInfo() {
        List mixerInfoList = getMixerInfoList();
        return (Mixer.Info[]) mixerInfoList.toArray(new Mixer.Info[mixerInfoList.size()]);
    }

    public static Mixer getMixer(Mixer.Info info) {
        List mixerProviders = getMixerProviders();
        for (int i2 = 0; i2 < mixerProviders.size(); i2++) {
            try {
                return ((MixerProvider) mixerProviders.get(i2)).getMixer(info);
            } catch (IllegalArgumentException | NullPointerException e2) {
            }
        }
        if (info == null) {
            for (int i3 = 0; i3 < mixerProviders.size(); i3++) {
                try {
                    MixerProvider mixerProvider = (MixerProvider) mixerProviders.get(i3);
                    for (Mixer.Info info2 : mixerProvider.getMixerInfo()) {
                        try {
                            return mixerProvider.getMixer(info2);
                        } catch (IllegalArgumentException e3) {
                        }
                    }
                } catch (IllegalArgumentException e4) {
                } catch (NullPointerException e5) {
                }
            }
        }
        throw new IllegalArgumentException("Mixer not supported: " + (info != null ? info.toString() : FXMLLoader.NULL_KEYWORD));
    }

    public static Line.Info[] getSourceLineInfo(Line.Info info) {
        Vector vector = new Vector();
        for (Mixer.Info info2 : getMixerInfo()) {
            for (Line.Info info3 : getMixer(info2).getSourceLineInfo(info)) {
                vector.addElement(info3);
            }
        }
        Line.Info[] infoArr = new Line.Info[vector.size()];
        for (int i2 = 0; i2 < infoArr.length; i2++) {
            infoArr[i2] = (Line.Info) vector.get(i2);
        }
        return infoArr;
    }

    public static Line.Info[] getTargetLineInfo(Line.Info info) {
        Vector vector = new Vector();
        for (Mixer.Info info2 : getMixerInfo()) {
            for (Line.Info info3 : getMixer(info2).getTargetLineInfo(info)) {
                vector.addElement(info3);
            }
        }
        Line.Info[] infoArr = new Line.Info[vector.size()];
        for (int i2 = 0; i2 < infoArr.length; i2++) {
            infoArr[i2] = (Line.Info) vector.get(i2);
        }
        return infoArr;
    }

    public static boolean isLineSupported(Line.Info info) {
        Mixer.Info[] mixerInfo = getMixerInfo();
        for (int i2 = 0; i2 < mixerInfo.length; i2++) {
            if (mixerInfo[i2] != null && getMixer(mixerInfo[i2]).isLineSupported(info)) {
                return true;
            }
        }
        return false;
    }

    public static Line getLine(Line.Info info) throws LineUnavailableException {
        Mixer mixer;
        Mixer mixer2;
        LineUnavailableException lineUnavailableException = null;
        List mixerProviders = getMixerProviders();
        try {
            Mixer defaultMixer = getDefaultMixer(mixerProviders, info);
            if (defaultMixer != null && defaultMixer.isLineSupported(info)) {
                return defaultMixer.getLine(info);
            }
        } catch (IllegalArgumentException e2) {
        } catch (LineUnavailableException e3) {
            lineUnavailableException = e3;
        }
        for (int i2 = 0; i2 < mixerProviders.size(); i2++) {
            MixerProvider mixerProvider = (MixerProvider) mixerProviders.get(i2);
            for (Mixer.Info info2 : mixerProvider.getMixerInfo()) {
                try {
                    mixer2 = mixerProvider.getMixer(info2);
                } catch (IllegalArgumentException e4) {
                } catch (LineUnavailableException e5) {
                    lineUnavailableException = e5;
                }
                if (!isAppropriateMixer(mixer2, info, true)) {
                    continue;
                } else {
                    return mixer2.getLine(info);
                }
            }
        }
        for (int i3 = 0; i3 < mixerProviders.size(); i3++) {
            MixerProvider mixerProvider2 = (MixerProvider) mixerProviders.get(i3);
            for (Mixer.Info info3 : mixerProvider2.getMixerInfo()) {
                try {
                    mixer = mixerProvider2.getMixer(info3);
                } catch (IllegalArgumentException e6) {
                } catch (LineUnavailableException e7) {
                    lineUnavailableException = e7;
                }
                if (!isAppropriateMixer(mixer, info, false)) {
                    continue;
                } else {
                    return mixer.getLine(info);
                }
            }
        }
        if (lineUnavailableException != null) {
            throw lineUnavailableException;
        }
        throw new IllegalArgumentException("No line matching " + info.toString() + " is supported.");
    }

    public static Clip getClip() throws LineUnavailableException {
        return (Clip) getLine(new DataLine.Info(Clip.class, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, 16, 2, 4, -1.0f, true)));
    }

    public static Clip getClip(Mixer.Info info) throws LineUnavailableException {
        return (Clip) getMixer(info).getLine(new DataLine.Info(Clip.class, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0f, 16, 2, 4, -1.0f, true)));
    }

    public static SourceDataLine getSourceDataLine(AudioFormat audioFormat) throws LineUnavailableException {
        return (SourceDataLine) getLine(new DataLine.Info(SourceDataLine.class, audioFormat));
    }

    public static SourceDataLine getSourceDataLine(AudioFormat audioFormat, Mixer.Info info) throws LineUnavailableException {
        return (SourceDataLine) getMixer(info).getLine(new DataLine.Info(SourceDataLine.class, audioFormat));
    }

    public static TargetDataLine getTargetDataLine(AudioFormat audioFormat) throws LineUnavailableException {
        return (TargetDataLine) getLine(new DataLine.Info(TargetDataLine.class, audioFormat));
    }

    public static TargetDataLine getTargetDataLine(AudioFormat audioFormat, Mixer.Info info) throws LineUnavailableException {
        return (TargetDataLine) getMixer(info).getLine(new DataLine.Info(TargetDataLine.class, audioFormat));
    }

    public static AudioFormat.Encoding[] getTargetEncodings(AudioFormat.Encoding encoding) {
        List formatConversionProviders = getFormatConversionProviders();
        Vector vector = new Vector();
        for (int i2 = 0; i2 < formatConversionProviders.size(); i2++) {
            FormatConversionProvider formatConversionProvider = (FormatConversionProvider) formatConversionProviders.get(i2);
            if (formatConversionProvider.isSourceEncodingSupported(encoding)) {
                for (AudioFormat.Encoding encoding2 : formatConversionProvider.getTargetEncodings()) {
                    vector.addElement(encoding2);
                }
            }
        }
        return (AudioFormat.Encoding[]) vector.toArray(new AudioFormat.Encoding[0]);
    }

    public static AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat) {
        List formatConversionProviders = getFormatConversionProviders();
        Vector vector = new Vector();
        int length = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < formatConversionProviders.size(); i3++) {
            AudioFormat.Encoding[] targetEncodings = ((FormatConversionProvider) formatConversionProviders.get(i3)).getTargetEncodings(audioFormat);
            length += targetEncodings.length;
            vector.addElement(targetEncodings);
        }
        AudioFormat.Encoding[] encodingArr = new AudioFormat.Encoding[length];
        for (int i4 = 0; i4 < vector.size(); i4++) {
            for (AudioFormat.Encoding encoding : (AudioFormat.Encoding[]) vector.get(i4)) {
                int i5 = i2;
                i2++;
                encodingArr[i5] = encoding;
            }
        }
        return encodingArr;
    }

    public static boolean isConversionSupported(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        List formatConversionProviders = getFormatConversionProviders();
        for (int i2 = 0; i2 < formatConversionProviders.size(); i2++) {
            if (((FormatConversionProvider) formatConversionProviders.get(i2)).isConversionSupported(encoding, audioFormat)) {
                return true;
            }
        }
        return false;
    }

    public static AudioInputStream getAudioInputStream(AudioFormat.Encoding encoding, AudioInputStream audioInputStream) {
        List formatConversionProviders = getFormatConversionProviders();
        for (int i2 = 0; i2 < formatConversionProviders.size(); i2++) {
            FormatConversionProvider formatConversionProvider = (FormatConversionProvider) formatConversionProviders.get(i2);
            if (formatConversionProvider.isConversionSupported(encoding, audioInputStream.getFormat())) {
                return formatConversionProvider.getAudioInputStream(encoding, audioInputStream);
            }
        }
        throw new IllegalArgumentException("Unsupported conversion: " + ((Object) encoding) + " from " + ((Object) audioInputStream.getFormat()));
    }

    public static AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        List formatConversionProviders = getFormatConversionProviders();
        Vector vector = new Vector();
        int length = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < formatConversionProviders.size(); i3++) {
            AudioFormat[] targetFormats = ((FormatConversionProvider) formatConversionProviders.get(i3)).getTargetFormats(encoding, audioFormat);
            length += targetFormats.length;
            vector.addElement(targetFormats);
        }
        AudioFormat[] audioFormatArr = new AudioFormat[length];
        for (int i4 = 0; i4 < vector.size(); i4++) {
            for (AudioFormat audioFormat2 : (AudioFormat[]) vector.get(i4)) {
                int i5 = i2;
                i2++;
                audioFormatArr[i5] = audioFormat2;
            }
        }
        return audioFormatArr;
    }

    public static boolean isConversionSupported(AudioFormat audioFormat, AudioFormat audioFormat2) {
        List formatConversionProviders = getFormatConversionProviders();
        for (int i2 = 0; i2 < formatConversionProviders.size(); i2++) {
            if (((FormatConversionProvider) formatConversionProviders.get(i2)).isConversionSupported(audioFormat, audioFormat2)) {
                return true;
            }
        }
        return false;
    }

    public static AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioInputStream audioInputStream) {
        if (audioInputStream.getFormat().matches(audioFormat)) {
            return audioInputStream;
        }
        List formatConversionProviders = getFormatConversionProviders();
        for (int i2 = 0; i2 < formatConversionProviders.size(); i2++) {
            FormatConversionProvider formatConversionProvider = (FormatConversionProvider) formatConversionProviders.get(i2);
            if (formatConversionProvider.isConversionSupported(audioFormat, audioInputStream.getFormat())) {
                return formatConversionProvider.getAudioInputStream(audioFormat, audioInputStream);
            }
        }
        throw new IllegalArgumentException("Unsupported conversion: " + ((Object) audioFormat) + " from " + ((Object) audioInputStream.getFormat()));
    }

    public static AudioFileFormat getAudioFileFormat(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        List audioFileReaders = getAudioFileReaders();
        AudioFileFormat audioFileFormat = null;
        for (int i2 = 0; i2 < audioFileReaders.size(); i2++) {
            try {
                audioFileFormat = ((AudioFileReader) audioFileReaders.get(i2)).getAudioFileFormat(inputStream);
                break;
            } catch (UnsupportedAudioFileException e2) {
            }
        }
        if (audioFileFormat == null) {
            throw new UnsupportedAudioFileException("file is not a supported file type");
        }
        return audioFileFormat;
    }

    public static AudioFileFormat getAudioFileFormat(URL url) throws UnsupportedAudioFileException, IOException {
        List audioFileReaders = getAudioFileReaders();
        AudioFileFormat audioFileFormat = null;
        for (int i2 = 0; i2 < audioFileReaders.size(); i2++) {
            try {
                audioFileFormat = ((AudioFileReader) audioFileReaders.get(i2)).getAudioFileFormat(url);
                break;
            } catch (UnsupportedAudioFileException e2) {
            }
        }
        if (audioFileFormat == null) {
            throw new UnsupportedAudioFileException("file is not a supported file type");
        }
        return audioFileFormat;
    }

    public static AudioFileFormat getAudioFileFormat(File file) throws UnsupportedAudioFileException, IOException {
        List audioFileReaders = getAudioFileReaders();
        AudioFileFormat audioFileFormat = null;
        for (int i2 = 0; i2 < audioFileReaders.size(); i2++) {
            try {
                audioFileFormat = ((AudioFileReader) audioFileReaders.get(i2)).getAudioFileFormat(file);
                break;
            } catch (UnsupportedAudioFileException e2) {
            }
        }
        if (audioFileFormat == null) {
            throw new UnsupportedAudioFileException("file is not a supported file type");
        }
        return audioFileFormat;
    }

    public static AudioInputStream getAudioInputStream(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        List audioFileReaders = getAudioFileReaders();
        AudioInputStream audioInputStream = null;
        for (int i2 = 0; i2 < audioFileReaders.size(); i2++) {
            try {
                audioInputStream = ((AudioFileReader) audioFileReaders.get(i2)).getAudioInputStream(inputStream);
                break;
            } catch (UnsupportedAudioFileException e2) {
            }
        }
        if (audioInputStream == null) {
            throw new UnsupportedAudioFileException("could not get audio input stream from input stream");
        }
        return audioInputStream;
    }

    public static AudioInputStream getAudioInputStream(URL url) throws UnsupportedAudioFileException, IOException {
        List audioFileReaders = getAudioFileReaders();
        AudioInputStream audioInputStream = null;
        for (int i2 = 0; i2 < audioFileReaders.size(); i2++) {
            try {
                audioInputStream = ((AudioFileReader) audioFileReaders.get(i2)).getAudioInputStream(url);
                break;
            } catch (UnsupportedAudioFileException e2) {
            }
        }
        if (audioInputStream == null) {
            throw new UnsupportedAudioFileException("could not get audio input stream from input URL");
        }
        return audioInputStream;
    }

    public static AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        List audioFileReaders = getAudioFileReaders();
        AudioInputStream audioInputStream = null;
        for (int i2 = 0; i2 < audioFileReaders.size(); i2++) {
            try {
                audioInputStream = ((AudioFileReader) audioFileReaders.get(i2)).getAudioInputStream(file);
                break;
            } catch (UnsupportedAudioFileException e2) {
            }
        }
        if (audioInputStream == null) {
            throw new UnsupportedAudioFileException("could not get audio input stream from input file");
        }
        return audioInputStream;
    }

    public static AudioFileFormat.Type[] getAudioFileTypes() {
        List audioFileWriters = getAudioFileWriters();
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < audioFileWriters.size(); i2++) {
            for (AudioFileFormat.Type type : ((AudioFileWriter) audioFileWriters.get(i2)).getAudioFileTypes()) {
                hashSet.add(type);
            }
        }
        return (AudioFileFormat.Type[]) hashSet.toArray(new AudioFileFormat.Type[0]);
    }

    public static boolean isFileTypeSupported(AudioFileFormat.Type type) {
        List audioFileWriters = getAudioFileWriters();
        for (int i2 = 0; i2 < audioFileWriters.size(); i2++) {
            if (((AudioFileWriter) audioFileWriters.get(i2)).isFileTypeSupported(type)) {
                return true;
            }
        }
        return false;
    }

    public static AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream audioInputStream) {
        List audioFileWriters = getAudioFileWriters();
        HashSet hashSet = new HashSet();
        for (int i2 = 0; i2 < audioFileWriters.size(); i2++) {
            for (AudioFileFormat.Type type : ((AudioFileWriter) audioFileWriters.get(i2)).getAudioFileTypes(audioInputStream)) {
                hashSet.add(type);
            }
        }
        return (AudioFileFormat.Type[]) hashSet.toArray(new AudioFileFormat.Type[0]);
    }

    public static boolean isFileTypeSupported(AudioFileFormat.Type type, AudioInputStream audioInputStream) {
        List audioFileWriters = getAudioFileWriters();
        for (int i2 = 0; i2 < audioFileWriters.size(); i2++) {
            if (((AudioFileWriter) audioFileWriters.get(i2)).isFileTypeSupported(type, audioInputStream)) {
                return true;
            }
        }
        return false;
    }

    public static int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, OutputStream outputStream) throws IOException {
        List audioFileWriters = getAudioFileWriters();
        int iWrite = 0;
        boolean z2 = false;
        for (int i2 = 0; i2 < audioFileWriters.size(); i2++) {
            try {
                iWrite = ((AudioFileWriter) audioFileWriters.get(i2)).write(audioInputStream, type, outputStream);
                z2 = true;
                break;
            } catch (IllegalArgumentException e2) {
            }
        }
        if (!z2) {
            throw new IllegalArgumentException("could not write audio file: file type not supported: " + ((Object) type));
        }
        return iWrite;
    }

    public static int write(AudioInputStream audioInputStream, AudioFileFormat.Type type, File file) throws IOException {
        List audioFileWriters = getAudioFileWriters();
        int iWrite = 0;
        boolean z2 = false;
        for (int i2 = 0; i2 < audioFileWriters.size(); i2++) {
            try {
                iWrite = ((AudioFileWriter) audioFileWriters.get(i2)).write(audioInputStream, type, file);
                z2 = true;
                break;
            } catch (IllegalArgumentException e2) {
            }
        }
        if (!z2) {
            throw new IllegalArgumentException("could not write audio file: file type not supported: " + ((Object) type));
        }
        return iWrite;
    }

    private static List getMixerProviders() {
        return getProviders(MixerProvider.class);
    }

    private static List getFormatConversionProviders() {
        return getProviders(FormatConversionProvider.class);
    }

    private static List getAudioFileReaders() {
        return getProviders(AudioFileReader.class);
    }

    private static List getAudioFileWriters() {
        return getProviders(AudioFileWriter.class);
    }

    private static Mixer getDefaultMixer(List list, Line.Info info) {
        Mixer namedMixer;
        MixerProvider namedProvider;
        Class<?> lineClass = info.getLineClass();
        String defaultProviderClassName = JDK13Services.getDefaultProviderClassName(lineClass);
        String defaultInstanceName = JDK13Services.getDefaultInstanceName(lineClass);
        if (defaultProviderClassName != null && (namedProvider = getNamedProvider(defaultProviderClassName, list)) != null) {
            if (defaultInstanceName != null) {
                Mixer namedMixer2 = getNamedMixer(defaultInstanceName, namedProvider, info);
                if (namedMixer2 != null) {
                    return namedMixer2;
                }
            } else {
                Mixer firstMixer = getFirstMixer(namedProvider, info, false);
                if (firstMixer != null) {
                    return firstMixer;
                }
            }
        }
        if (defaultInstanceName != null && (namedMixer = getNamedMixer(defaultInstanceName, list, info)) != null) {
            return namedMixer;
        }
        return null;
    }

    private static MixerProvider getNamedProvider(String str, List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            MixerProvider mixerProvider = (MixerProvider) list.get(i2);
            if (mixerProvider.getClass().getName().equals(str)) {
                return mixerProvider;
            }
        }
        return null;
    }

    private static Mixer getNamedMixer(String str, MixerProvider mixerProvider, Line.Info info) {
        Mixer.Info[] mixerInfo = mixerProvider.getMixerInfo();
        for (int i2 = 0; i2 < mixerInfo.length; i2++) {
            if (mixerInfo[i2].getName().equals(str)) {
                Mixer mixer = mixerProvider.getMixer(mixerInfo[i2]);
                if (isAppropriateMixer(mixer, info, false)) {
                    return mixer;
                }
            }
        }
        return null;
    }

    private static Mixer getNamedMixer(String str, List list, Line.Info info) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            Mixer namedMixer = getNamedMixer(str, (MixerProvider) list.get(i2), info);
            if (namedMixer != null) {
                return namedMixer;
            }
        }
        return null;
    }

    private static Mixer getFirstMixer(MixerProvider mixerProvider, Line.Info info, boolean z2) {
        for (Mixer.Info info2 : mixerProvider.getMixerInfo()) {
            Mixer mixer = mixerProvider.getMixer(info2);
            if (isAppropriateMixer(mixer, info, z2)) {
                return mixer;
            }
        }
        return null;
    }

    private static boolean isAppropriateMixer(Mixer mixer, Line.Info info, boolean z2) {
        int maxLines;
        if (!mixer.isLineSupported(info)) {
            return false;
        }
        Class<?> lineClass = info.getLineClass();
        if (z2) {
            return !(SourceDataLine.class.isAssignableFrom(lineClass) || Clip.class.isAssignableFrom(lineClass)) || (maxLines = mixer.getMaxLines(info)) == -1 || maxLines > 1;
        }
        return true;
    }

    private static List getMixerInfoList() {
        return getMixerInfoList(getMixerProviders());
    }

    private static List getMixerInfoList(List list) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (Mixer.Info info : ((MixerProvider) list.get(i2)).getMixerInfo()) {
                arrayList.add(info);
            }
        }
        return arrayList;
    }

    private static List getProviders(Class cls) {
        return JDK13Services.getProviders(cls);
    }
}
