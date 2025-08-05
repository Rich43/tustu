package sun.audio;

import com.sun.media.sound.DataPusher;
import com.sun.media.sound.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/* loaded from: rt.jar:sun/audio/AudioDevice.class */
public final class AudioDevice {
    public static final AudioDevice device = new AudioDevice();
    private boolean DEBUG = false;
    private boolean playing = false;
    private Mixer mixer = null;
    private Hashtable clipStreams = new Hashtable();
    private Vector infos = new Vector();

    private AudioDevice() {
    }

    private synchronized void startSampled(AudioInputStream audioInputStream, InputStream inputStream) throws LineUnavailableException, UnsupportedAudioFileException {
        AudioInputStream pCMConvertedAudioInputStream = Toolkit.getPCMConvertedAudioInputStream(audioInputStream);
        if (pCMConvertedAudioInputStream == null) {
            return;
        }
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, pCMConvertedAudioInputStream.getFormat());
        if (!AudioSystem.isLineSupported(info)) {
            return;
        }
        DataPusher dataPusher = new DataPusher((SourceDataLine) AudioSystem.getLine(info), pCMConvertedAudioInputStream);
        this.infos.addElement(new Info(null, inputStream, dataPusher));
        dataPusher.start();
    }

    private synchronized void startMidi(InputStream inputStream, InputStream inputStream2) throws InvalidMidiDataException, MidiUnavailableException {
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        try {
            sequencer.setSequence(inputStream);
            Info info = new Info(sequencer, inputStream2, null);
            this.infos.addElement(info);
            sequencer.addMetaEventListener(info);
            sequencer.start();
        } catch (IOException e2) {
            throw new InvalidMidiDataException(e2.getMessage());
        }
    }

    public synchronized void openChannel(InputStream inputStream) {
        if (this.DEBUG) {
            System.out.println("AudioDevice: openChannel");
            System.out.println("input stream =" + ((Object) inputStream));
        }
        for (int i2 = 0; i2 < this.infos.size(); i2++) {
            if (((Info) this.infos.elementAt(i2)).in == inputStream) {
                return;
            }
        }
        if (inputStream instanceof AudioStream) {
            if (((AudioStream) inputStream).midiformat != null) {
                try {
                    startMidi(((AudioStream) inputStream).stream, inputStream);
                } catch (Exception e2) {
                    return;
                }
            } else if (((AudioStream) inputStream).ais != null) {
                try {
                    startSampled(((AudioStream) inputStream).ais, inputStream);
                } catch (Exception e3) {
                    return;
                }
            }
        } else if (inputStream instanceof AudioDataStream) {
            if (inputStream instanceof ContinuousAudioDataStream) {
                try {
                    startSampled(new AudioInputStream(inputStream, ((AudioDataStream) inputStream).getAudioData().format, -1L), inputStream);
                } catch (Exception e4) {
                    return;
                }
            } else {
                try {
                    startSampled(new AudioInputStream(inputStream, ((AudioDataStream) inputStream).getAudioData().format, ((AudioDataStream) inputStream).getAudioData().buffer.length), inputStream);
                } catch (Exception e5) {
                    return;
                }
            }
        } else {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
            try {
                try {
                    startSampled(AudioSystem.getAudioInputStream(bufferedInputStream), inputStream);
                } catch (LineUnavailableException e6) {
                    return;
                } catch (UnsupportedAudioFileException e7) {
                    try {
                        try {
                            MidiSystem.getMidiFileFormat(bufferedInputStream);
                            startMidi(bufferedInputStream, inputStream);
                        } catch (InvalidMidiDataException e8) {
                            try {
                                startSampled(new AudioInputStream(bufferedInputStream, new AudioFormat(AudioFormat.Encoding.ULAW, 8000.0f, 8, 1, 1, 8000.0f, true), -1L), inputStream);
                            } catch (LineUnavailableException e9) {
                                return;
                            } catch (UnsupportedAudioFileException e10) {
                                return;
                            }
                        } catch (MidiUnavailableException e11) {
                            return;
                        }
                    } catch (IOException e12) {
                        return;
                    }
                }
            } catch (IOException e13) {
                return;
            }
        }
        notify();
    }

    public synchronized void closeChannel(InputStream inputStream) {
        if (this.DEBUG) {
            System.out.println("AudioDevice.closeChannel");
        }
        if (inputStream == null) {
            return;
        }
        for (int i2 = 0; i2 < this.infos.size(); i2++) {
            Info info = (Info) this.infos.elementAt(i2);
            if (info.in == inputStream) {
                if (info.sequencer != null) {
                    info.sequencer.stop();
                    this.infos.removeElement(info);
                } else if (info.datapusher != null) {
                    info.datapusher.stop();
                    this.infos.removeElement(info);
                }
            }
        }
        notify();
    }

    public synchronized void open() {
    }

    public synchronized void close() {
    }

    public void play() {
        if (this.DEBUG) {
            System.out.println("exiting play()");
        }
    }

    public synchronized void closeStreams() {
        for (int i2 = 0; i2 < this.infos.size(); i2++) {
            Info info = (Info) this.infos.elementAt(i2);
            if (info.sequencer != null) {
                info.sequencer.stop();
                info.sequencer.close();
                this.infos.removeElement(info);
            } else if (info.datapusher != null) {
                info.datapusher.stop();
                this.infos.removeElement(info);
            }
        }
        if (this.DEBUG) {
            System.err.println("Audio Device: Streams all closed.");
        }
        this.clipStreams = new Hashtable();
        this.infos = new Vector();
    }

    public int openChannels() {
        return this.infos.size();
    }

    void setVerbose(boolean z2) {
        this.DEBUG = z2;
    }

    /* loaded from: rt.jar:sun/audio/AudioDevice$Info.class */
    final class Info implements MetaEventListener {
        final Sequencer sequencer;
        final InputStream in;
        final DataPusher datapusher;

        Info(Sequencer sequencer, InputStream inputStream, DataPusher dataPusher) {
            this.sequencer = sequencer;
            this.in = inputStream;
            this.datapusher = dataPusher;
        }

        @Override // javax.sound.midi.MetaEventListener
        public void meta(MetaMessage metaMessage) {
            if (metaMessage.getType() == 47 && this.sequencer != null) {
                this.sequencer.close();
            }
        }
    }
}
