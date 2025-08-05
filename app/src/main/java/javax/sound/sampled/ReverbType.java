package javax.sound.sampled;

/* loaded from: rt.jar:javax/sound/sampled/ReverbType.class */
public class ReverbType {
    private String name;
    private int earlyReflectionDelay;
    private float earlyReflectionIntensity;
    private int lateReflectionDelay;
    private float lateReflectionIntensity;
    private int decayTime;

    protected ReverbType(String str, int i2, float f2, int i3, float f3, int i4) {
        this.name = str;
        this.earlyReflectionDelay = i2;
        this.earlyReflectionIntensity = f2;
        this.lateReflectionDelay = i3;
        this.lateReflectionIntensity = f3;
        this.decayTime = i4;
    }

    public String getName() {
        return this.name;
    }

    public final int getEarlyReflectionDelay() {
        return this.earlyReflectionDelay;
    }

    public final float getEarlyReflectionIntensity() {
        return this.earlyReflectionIntensity;
    }

    public final int getLateReflectionDelay() {
        return this.lateReflectionDelay;
    }

    public final float getLateReflectionIntensity() {
        return this.lateReflectionIntensity;
    }

    public final int getDecayTime() {
        return this.decayTime;
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final String toString() {
        return this.name + ", early reflection delay " + this.earlyReflectionDelay + " ns, early reflection intensity " + this.earlyReflectionIntensity + " dB, late deflection delay " + this.lateReflectionDelay + " ns, late reflection intensity " + this.lateReflectionIntensity + " dB, decay time " + this.decayTime;
    }
}
