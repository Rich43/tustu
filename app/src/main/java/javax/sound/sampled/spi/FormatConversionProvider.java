package javax.sound.sampled.spi;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

/* loaded from: rt.jar:javax/sound/sampled/spi/FormatConversionProvider.class */
public abstract class FormatConversionProvider {
    public abstract AudioFormat.Encoding[] getSourceEncodings();

    public abstract AudioFormat.Encoding[] getTargetEncodings();

    public abstract AudioFormat.Encoding[] getTargetEncodings(AudioFormat audioFormat);

    public abstract AudioFormat[] getTargetFormats(AudioFormat.Encoding encoding, AudioFormat audioFormat);

    public abstract AudioInputStream getAudioInputStream(AudioFormat.Encoding encoding, AudioInputStream audioInputStream);

    public abstract AudioInputStream getAudioInputStream(AudioFormat audioFormat, AudioInputStream audioInputStream);

    public boolean isSourceEncodingSupported(AudioFormat.Encoding encoding) {
        for (AudioFormat.Encoding encoding2 : getSourceEncodings()) {
            if (encoding.equals(encoding2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTargetEncodingSupported(AudioFormat.Encoding encoding) {
        for (AudioFormat.Encoding encoding2 : getTargetEncodings()) {
            if (encoding.equals(encoding2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isConversionSupported(AudioFormat.Encoding encoding, AudioFormat audioFormat) {
        for (AudioFormat.Encoding encoding2 : getTargetEncodings(audioFormat)) {
            if (encoding.equals(encoding2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isConversionSupported(AudioFormat audioFormat, AudioFormat audioFormat2) {
        for (AudioFormat audioFormat3 : getTargetFormats(audioFormat.getEncoding(), audioFormat2)) {
            if (audioFormat.matches(audioFormat3)) {
                return true;
            }
        }
        return false;
    }
}
