package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/BinaryChromosome.class */
public abstract class BinaryChromosome extends AbstractListChromosome<Integer> {
    public BinaryChromosome(List<Integer> representation) throws InvalidRepresentationException {
        super(representation);
    }

    public BinaryChromosome(Integer[] representation) throws InvalidRepresentationException {
        super(representation);
    }

    @Override // org.apache.commons.math3.genetics.AbstractListChromosome
    protected void checkValidity(List<Integer> chromosomeRepresentation) throws InvalidRepresentationException {
        Iterator i$ = chromosomeRepresentation.iterator();
        while (i$.hasNext()) {
            int i2 = i$.next().intValue();
            if (i2 < 0 || i2 > 1) {
                throw new InvalidRepresentationException(LocalizedFormats.INVALID_BINARY_DIGIT, Integer.valueOf(i2));
            }
        }
    }

    public static List<Integer> randomBinaryRepresentation(int length) {
        List<Integer> rList = new ArrayList<>(length);
        for (int j2 = 0; j2 < length; j2++) {
            rList.add(Integer.valueOf(GeneticAlgorithm.getRandomGenerator().nextInt(2)));
        }
        return rList;
    }

    @Override // org.apache.commons.math3.genetics.Chromosome
    protected boolean isSame(Chromosome another) {
        if (!(another instanceof BinaryChromosome)) {
            return false;
        }
        BinaryChromosome anotherBc = (BinaryChromosome) another;
        if (getLength() != anotherBc.getLength()) {
            return false;
        }
        for (int i2 = 0; i2 < getRepresentation().size(); i2++) {
            if (!getRepresentation().get(i2).equals(anotherBc.getRepresentation().get(i2))) {
                return false;
            }
        }
        return true;
    }
}
