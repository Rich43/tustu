package javafx.scene.media;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.scene.media.MediaException;

/* loaded from: jfxrt.jar:javafx/scene/media/AudioClip.class */
public final class AudioClip {
    private String sourceURL;
    private com.sun.media.jfxmedia.AudioClip audioClip;
    private DoubleProperty volume;
    private DoubleProperty balance;
    private DoubleProperty rate;
    private DoubleProperty pan;
    private IntegerProperty priority;
    public static final int INDEFINITE = -1;
    private IntegerProperty cycleCount;

    public AudioClip(@NamedArg("source") String source) {
        URI srcURI = URI.create(source);
        this.sourceURL = source;
        try {
            this.audioClip = com.sun.media.jfxmedia.AudioClip.load(srcURI);
        } catch (com.sun.media.jfxmedia.MediaException me) {
            throw new MediaException(MediaException.Type.MEDIA_UNSUPPORTED, me.getMessage());
        } catch (FileNotFoundException fnfe) {
            throw new MediaException(MediaException.Type.MEDIA_UNAVAILABLE, fnfe.getMessage());
        } catch (IOException ioe) {
            throw new MediaException(MediaException.Type.MEDIA_INACCESSIBLE, ioe.getMessage());
        } catch (URISyntaxException use) {
            throw new IllegalArgumentException(use);
        }
    }

    public String getSource() {
        return this.sourceURL;
    }

    public final void setVolume(double value) {
        volumeProperty().set(value);
    }

    public final double getVolume() {
        if (null == this.volume) {
            return 1.0d;
        }
        return this.volume.get();
    }

    public DoubleProperty volumeProperty() {
        if (this.volume == null) {
            this.volume = new DoublePropertyBase(1.0d) { // from class: javafx.scene.media.AudioClip.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (null != AudioClip.this.audioClip) {
                        AudioClip.this.audioClip.setVolume(AudioClip.this.volume.get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioClip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "volume";
                }
            };
        }
        return this.volume;
    }

    public void setBalance(double balance) {
        balanceProperty().set(balance);
    }

    public double getBalance() {
        if (null != this.balance) {
            return this.balance.get();
        }
        return 0.0d;
    }

    public DoubleProperty balanceProperty() {
        if (null == this.balance) {
            this.balance = new DoublePropertyBase(0.0d) { // from class: javafx.scene.media.AudioClip.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (null != AudioClip.this.audioClip) {
                        AudioClip.this.audioClip.setBalance(AudioClip.this.balance.get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioClip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "balance";
                }
            };
        }
        return this.balance;
    }

    public void setRate(double rate) {
        rateProperty().set(rate);
    }

    public double getRate() {
        if (null != this.rate) {
            return this.rate.get();
        }
        return 1.0d;
    }

    public DoubleProperty rateProperty() {
        if (null == this.rate) {
            this.rate = new DoublePropertyBase(1.0d) { // from class: javafx.scene.media.AudioClip.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (null != AudioClip.this.audioClip) {
                        AudioClip.this.audioClip.setPlaybackRate(AudioClip.this.rate.get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioClip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "rate";
                }
            };
        }
        return this.rate;
    }

    public void setPan(double pan) {
        panProperty().set(pan);
    }

    public double getPan() {
        if (null != this.pan) {
            return this.pan.get();
        }
        return 0.0d;
    }

    public DoubleProperty panProperty() {
        if (null == this.pan) {
            this.pan = new DoublePropertyBase(0.0d) { // from class: javafx.scene.media.AudioClip.4
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (null != AudioClip.this.audioClip) {
                        AudioClip.this.audioClip.setPan(AudioClip.this.pan.get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioClip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pan";
                }
            };
        }
        return this.pan;
    }

    public void setPriority(int priority) {
        priorityProperty().set(priority);
    }

    public int getPriority() {
        if (null != this.priority) {
            return this.priority.get();
        }
        return 0;
    }

    public IntegerProperty priorityProperty() {
        if (null == this.priority) {
            this.priority = new IntegerPropertyBase(0) { // from class: javafx.scene.media.AudioClip.5
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    if (null != AudioClip.this.audioClip) {
                        AudioClip.this.audioClip.setPriority(AudioClip.this.priority.get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioClip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Constants.ATTRNAME_PRIORITY;
                }
            };
        }
        return this.priority;
    }

    public void setCycleCount(int count) {
        cycleCountProperty().set(count);
    }

    public int getCycleCount() {
        if (null != this.cycleCount) {
            return this.cycleCount.get();
        }
        return 1;
    }

    public IntegerProperty cycleCountProperty() {
        if (null == this.cycleCount) {
            this.cycleCount = new IntegerPropertyBase(1) { // from class: javafx.scene.media.AudioClip.6
                @Override // javafx.beans.property.IntegerPropertyBase
                protected void invalidated() {
                    if (null != AudioClip.this.audioClip) {
                        int value = AudioClip.this.cycleCount.get();
                        if (-1 == value) {
                            AudioClip.this.audioClip.setLoopCount(value);
                        } else {
                            AudioClip.this.audioClip.setLoopCount(Math.max(1, value) - 1);
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioClip.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cycleCount";
                }
            };
        }
        return this.cycleCount;
    }

    public void play() {
        if (null != this.audioClip) {
            this.audioClip.play();
        }
    }

    public void play(double volume) {
        if (null != this.audioClip) {
            this.audioClip.play(volume);
        }
    }

    public void play(double volume, double balance, double rate, double pan, int priority) {
        if (null != this.audioClip) {
            this.audioClip.play(volume, balance, rate, pan, this.audioClip.loopCount(), priority);
        }
    }

    public boolean isPlaying() {
        return null != this.audioClip && this.audioClip.isPlaying();
    }

    public void stop() {
        if (null != this.audioClip) {
            this.audioClip.stop();
        }
    }
}
