package org.apache.commons.math3.genetics;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/ChromosomePair.class */
public class ChromosomePair {
    private final Chromosome first;
    private final Chromosome second;

    public ChromosomePair(Chromosome c1, Chromosome c2) {
        this.first = c1;
        this.second = c2;
    }

    public Chromosome getFirst() {
        return this.first;
    }

    public Chromosome getSecond() {
        return this.second;
    }

    public String toString() {
        return String.format("(%s,%s)", getFirst(), getSecond());
    }
}
