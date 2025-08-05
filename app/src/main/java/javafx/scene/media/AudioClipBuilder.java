package javafx.scene.media;

import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/media/AudioClipBuilder.class */
public final class AudioClipBuilder implements Builder<AudioClip> {
    private int __set;
    private double balance;
    private int cycleCount;
    private double pan;
    private int priority;
    private double rate;
    private String source;
    private double volume;

    protected AudioClipBuilder() {
    }

    public static AudioClipBuilder create() {
        return new AudioClipBuilder();
    }

    public void applyTo(AudioClip x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setBalance(this.balance);
        }
        if ((set & 2) != 0) {
            x2.setCycleCount(this.cycleCount);
        }
        if ((set & 4) != 0) {
            x2.setPan(this.pan);
        }
        if ((set & 8) != 0) {
            x2.setPriority(this.priority);
        }
        if ((set & 16) != 0) {
            x2.setRate(this.rate);
        }
        if ((set & 32) != 0) {
            x2.setVolume(this.volume);
        }
    }

    public AudioClipBuilder balance(double x2) {
        this.balance = x2;
        this.__set |= 1;
        return this;
    }

    public AudioClipBuilder cycleCount(int x2) {
        this.cycleCount = x2;
        this.__set |= 2;
        return this;
    }

    public AudioClipBuilder pan(double x2) {
        this.pan = x2;
        this.__set |= 4;
        return this;
    }

    public AudioClipBuilder priority(int x2) {
        this.priority = x2;
        this.__set |= 8;
        return this;
    }

    public AudioClipBuilder rate(double x2) {
        this.rate = x2;
        this.__set |= 16;
        return this;
    }

    public AudioClipBuilder source(String x2) {
        this.source = x2;
        return this;
    }

    public AudioClipBuilder volume(double x2) {
        this.volume = x2;
        this.__set |= 32;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public AudioClip build2() {
        AudioClip x2 = new AudioClip(this.source);
        applyTo(x2);
        return x2;
    }
}
