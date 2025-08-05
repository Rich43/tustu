package javafx.scene.media;

import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.media.jfxmedia.logging.Logger;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdk.jfr.Enabled;

/* loaded from: jfxrt.jar:javafx/scene/media/AudioEqualizer.class */
public final class AudioEqualizer {
    public static final int MAX_NUM_BANDS = 64;
    private BooleanProperty enabled;
    private com.sun.media.jfxmedia.effects.AudioEqualizer jfxEqualizer = null;
    private final Object disposeLock = new Object();
    private final ObservableList<EqualizerBand> bands = new Bands();

    public final ObservableList<EqualizerBand> getBands() {
        return this.bands;
    }

    AudioEqualizer() {
        this.bands.addAll(new EqualizerBand(32.0d, 19.0d, 0.0d), new EqualizerBand(64.0d, 39.0d, 0.0d), new EqualizerBand(125.0d, 78.0d, 0.0d), new EqualizerBand(250.0d, 156.0d, 0.0d), new EqualizerBand(500.0d, 312.0d, 0.0d), new EqualizerBand(1000.0d, 625.0d, 0.0d), new EqualizerBand(2000.0d, 1250.0d, 0.0d), new EqualizerBand(4000.0d, 2500.0d, 0.0d), new EqualizerBand(8000.0d, 5000.0d, 0.0d), new EqualizerBand(16000.0d, 10000.0d, 0.0d));
    }

    void setAudioEqualizer(com.sun.media.jfxmedia.effects.AudioEqualizer jfxEqualizer) {
        synchronized (this.disposeLock) {
            if (this.jfxEqualizer == jfxEqualizer) {
                return;
            }
            if (this.jfxEqualizer != null && jfxEqualizer == null) {
                this.jfxEqualizer.setEnabled(false);
                Iterator<EqualizerBand> it = this.bands.iterator();
                while (it.hasNext()) {
                    it.next().setJfxBand(null);
                }
                this.jfxEqualizer = null;
                return;
            }
            this.jfxEqualizer = jfxEqualizer;
            jfxEqualizer.setEnabled(isEnabled());
            for (EqualizerBand band : this.bands) {
                if (band.getCenterFrequency() > 0.0d && band.getBandwidth() > 0.0d) {
                    com.sun.media.jfxmedia.effects.EqualizerBand jfxBand = jfxEqualizer.addBand(band.getCenterFrequency(), band.getBandwidth(), band.getGain());
                    band.setJfxBand(jfxBand);
                } else {
                    Logger.logMsg(4, "Center frequency [" + band.getCenterFrequency() + "] and bandwidth [" + band.getBandwidth() + "] must be greater than 0.");
                }
            }
        }
    }

    public final void setEnabled(boolean value) {
        enabledProperty().set(value);
    }

    public final boolean isEnabled() {
        if (this.enabled == null) {
            return false;
        }
        return this.enabled.get();
    }

    public BooleanProperty enabledProperty() {
        if (this.enabled == null) {
            this.enabled = new BooleanPropertyBase() { // from class: javafx.scene.media.AudioEqualizer.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    synchronized (AudioEqualizer.this.disposeLock) {
                        if (AudioEqualizer.this.jfxEqualizer != null) {
                            AudioEqualizer.this.jfxEqualizer.setEnabled(AudioEqualizer.this.enabled.get());
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return AudioEqualizer.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Enabled.NAME;
                }
            };
        }
        return this.enabled;
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/AudioEqualizer$Bands.class */
    private class Bands extends VetoableListDecorator<EqualizerBand> {
        public Bands() {
            super(FXCollections.observableArrayList());
        }

        @Override // com.sun.javafx.collections.VetoableListDecorator
        protected void onProposedChange(List<EqualizerBand> toBeAdded, int[] toBeRemoved) {
            synchronized (AudioEqualizer.this.disposeLock) {
                if (AudioEqualizer.this.jfxEqualizer != null) {
                    for (int i2 = 0; i2 < toBeRemoved.length; i2 += 2) {
                        Iterator<EqualizerBand> it = subList(toBeRemoved[i2], toBeRemoved[i2 + 1]).iterator();
                        while (it.hasNext()) {
                            AudioEqualizer.this.jfxEqualizer.removeBand(it.next().getCenterFrequency());
                        }
                    }
                    for (EqualizerBand band : toBeAdded) {
                        if (band.getCenterFrequency() > 0.0d && band.getBandwidth() > 0.0d) {
                            com.sun.media.jfxmedia.effects.EqualizerBand jfxBand = AudioEqualizer.this.jfxEqualizer.addBand(band.getCenterFrequency(), band.getBandwidth(), band.getGain());
                            band.setJfxBand(jfxBand);
                        } else {
                            Logger.logMsg(4, "Center frequency [" + band.getCenterFrequency() + "] and bandwidth [" + band.getBandwidth() + "] must be greater than 0.");
                        }
                    }
                }
            }
        }
    }
}
