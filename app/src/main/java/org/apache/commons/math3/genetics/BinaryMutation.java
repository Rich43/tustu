package org.apache.commons.math3.genetics;

import java.util.ArrayList;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/genetics/BinaryMutation.class */
public class BinaryMutation implements MutationPolicy {
    @Override // org.apache.commons.math3.genetics.MutationPolicy
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof BinaryChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BINARY_CHROMOSOME, new Object[0]);
        }
        BinaryChromosome origChrom = (BinaryChromosome) original;
        ArrayList arrayList = new ArrayList(origChrom.getRepresentation());
        int geneIndex = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        arrayList.set(geneIndex, Integer.valueOf(origChrom.getRepresentation().get(geneIndex).intValue() == 0 ? 1 : 0));
        Chromosome newChrom = origChrom.newFixedLengthChromosome(arrayList);
        return newChrom;
    }
}
