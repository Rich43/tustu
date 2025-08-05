package javafx.scene.media;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

/* loaded from: jfxrt.jar:javafx/scene/media/EqualizerBand.class */
public final class EqualizerBand {
    public static final double MIN_GAIN = -24.0d;
    public static final double MAX_GAIN = 12.0d;
    private final Object disposeLock = new Object();
    private com.sun.media.jfxmedia.effects.EqualizerBand jfxBand;
    private DoubleProperty centerFrequency;
    private DoubleProperty bandwidth;
    private DoubleProperty gain;

    public EqualizerBand() {
    }

    public EqualizerBand(double centerFrequency, double bandwidth, double gain) {
        setCenterFrequency(centerFrequency);
        setBandwidth(bandwidth);
        setGain(gain);
    }

    void setJfxBand(com.sun.media.jfxmedia.effects.EqualizerBand jfxBand) {
        synchronized (this.disposeLock) {
            this.jfxBand = jfxBand;
        }
    }

    public final void setCenterFrequency(double value) {
        centerFrequencyProperty().set(value);
    }

    public final double getCenterFrequency() {
        if (this.centerFrequency == null) {
            return 0.0d;
        }
        return this.centerFrequency.get();
    }

    public DoubleProperty centerFrequencyProperty() {
        if (this.centerFrequency == null) {
            this.centerFrequency = new DoublePropertyBase() { // from class: javafx.scene.media.EqualizerBand.1
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (EqualizerBand.this.disposeLock) {
                        double value = EqualizerBand.this.centerFrequency.get();
                        if (EqualizerBand.this.jfxBand != null && value > 0.0d) {
                            EqualizerBand.this.jfxBand.setCenterFrequency(value);
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return EqualizerBand.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "centerFrequency";
                }
            };
        }
        return this.centerFrequency;
    }

    public final void setBandwidth(double value) {
        bandwidthProperty().set(value);
    }

    public final double getBandwidth() {
        if (this.bandwidth == null) {
            return 0.0d;
        }
        return this.bandwidth.get();
    }

    public DoubleProperty bandwidthProperty() {
        if (this.bandwidth == null) {
            this.bandwidth = new DoublePropertyBase() { // from class: javafx.scene.media.EqualizerBand.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (EqualizerBand.this.disposeLock) {
                        double value = EqualizerBand.this.bandwidth.get();
                        if (EqualizerBand.this.jfxBand != null && value > 0.0d) {
                            EqualizerBand.this.jfxBand.setBandwidth(value);
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return EqualizerBand.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "bandwidth";
                }
            };
        }
        return this.bandwidth;
    }

    public final void setGain(double value) {
        gainProperty().set(value);
    }

    public final double getGain() {
        if (this.gain == null) {
            return 0.0d;
        }
        return this.gain.get();
    }

    public DoubleProperty gainProperty() {
        if (this.gain == null) {
            this.gain = new DoublePropertyBase() { // from class: javafx.scene.media.EqualizerBand.3
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    synchronized (EqualizerBand.this.disposeLock) {
                        if (EqualizerBand.this.jfxBand != null) {
                            EqualizerBand.this.jfxBand.setGain(EqualizerBand.this.gain.get());
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return EqualizerBand.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "gain";
                }
            };
        }
        return this.gain;
    }
}
