package com.sun.media.sound;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.spi.MidiDeviceProvider;
import javax.sound.midi.spi.MidiFileReader;
import javax.sound.midi.spi.MidiFileWriter;
import javax.sound.midi.spi.SoundbankReader;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.spi.AudioFileReader;
import javax.sound.sampled.spi.AudioFileWriter;
import javax.sound.sampled.spi.FormatConversionProvider;
import javax.sound.sampled.spi.MixerProvider;

/* loaded from: rt.jar:com/sun/media/sound/JDK13Services.class */
public final class JDK13Services {
    private static final String PROPERTIES_FILENAME = "sound.properties";
    private static Properties properties;

    private JDK13Services() {
    }

    public static List<?> getProviders(Class<?> cls) {
        List providers;
        if (!MixerProvider.class.equals(cls) && !FormatConversionProvider.class.equals(cls) && !AudioFileReader.class.equals(cls) && !AudioFileWriter.class.equals(cls) && !MidiDeviceProvider.class.equals(cls) && !SoundbankReader.class.equals(cls) && !MidiFileWriter.class.equals(cls) && !MidiFileReader.class.equals(cls)) {
            providers = new ArrayList(0);
        } else {
            providers = JSSecurityManager.getProviders(cls);
        }
        return Collections.unmodifiableList(providers);
    }

    public static synchronized String getDefaultProviderClassName(Class cls) {
        int iIndexOf;
        String strSubstring = null;
        String defaultProvider = getDefaultProvider(cls);
        if (defaultProvider != null && (iIndexOf = defaultProvider.indexOf(35)) != 0) {
            strSubstring = iIndexOf > 0 ? defaultProvider.substring(0, iIndexOf) : defaultProvider;
        }
        return strSubstring;
    }

    public static synchronized String getDefaultInstanceName(Class cls) {
        int iIndexOf;
        String strSubstring = null;
        String defaultProvider = getDefaultProvider(cls);
        if (defaultProvider != null && (iIndexOf = defaultProvider.indexOf(35)) >= 0 && iIndexOf < defaultProvider.length() - 1) {
            strSubstring = defaultProvider.substring(iIndexOf + 1);
        }
        return strSubstring;
    }

    private static synchronized String getDefaultProvider(Class cls) {
        if (!SourceDataLine.class.equals(cls) && !TargetDataLine.class.equals(cls) && !Clip.class.equals(cls) && !Port.class.equals(cls) && !Receiver.class.equals(cls) && !Transmitter.class.equals(cls) && !Synthesizer.class.equals(cls) && !Sequencer.class.equals(cls)) {
            return null;
        }
        String name = cls.getName();
        String property = (String) AccessController.doPrivileged(() -> {
            return System.getProperty(name);
        });
        if (property == null) {
            property = getProperties().getProperty(name);
        }
        if ("".equals(property)) {
            property = null;
        }
        return property;
    }

    private static synchronized Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            JSSecurityManager.loadProperties(properties, PROPERTIES_FILENAME);
        }
        return properties;
    }
}
