package sun.management.counter;

/* loaded from: rt.jar:sun/management/counter/AbstractCounter.class */
public abstract class AbstractCounter implements Counter {
    String name;
    Units units;
    Variability variability;
    int flags;
    int vectorLength;
    private static final long serialVersionUID = 6992337162326171013L;

    @Override // sun.management.counter.Counter
    public abstract Object getValue();

    /* loaded from: rt.jar:sun/management/counter/AbstractCounter$Flags.class */
    class Flags {
        static final int SUPPORTED = 1;

        Flags() {
        }
    }

    protected AbstractCounter(String str, Units units, Variability variability, int i2, int i3) {
        this.name = str;
        this.units = units;
        this.variability = variability;
        this.flags = i2;
        this.vectorLength = i3;
    }

    protected AbstractCounter(String str, Units units, Variability variability, int i2) {
        this(str, units, variability, i2, 0);
    }

    @Override // sun.management.counter.Counter
    public String getName() {
        return this.name;
    }

    @Override // sun.management.counter.Counter
    public Units getUnits() {
        return this.units;
    }

    @Override // sun.management.counter.Counter
    public Variability getVariability() {
        return this.variability;
    }

    @Override // sun.management.counter.Counter
    public boolean isVector() {
        return this.vectorLength > 0;
    }

    @Override // sun.management.counter.Counter
    public int getVectorLength() {
        return this.vectorLength;
    }

    @Override // sun.management.counter.Counter
    public boolean isInternal() {
        return (this.flags & 1) == 0;
    }

    @Override // sun.management.counter.Counter
    public int getFlags() {
        return this.flags;
    }

    public String toString() {
        String str = getName() + ": " + getValue() + " " + ((Object) getUnits());
        if (isInternal()) {
            return str + " [INTERNAL]";
        }
        return str;
    }
}
