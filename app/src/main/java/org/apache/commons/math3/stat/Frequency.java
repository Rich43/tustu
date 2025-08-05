package org.apache.commons.math3.stat;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/Frequency.class */
public class Frequency implements Serializable {
    private static final long serialVersionUID = -3845586908418844111L;
    private final SortedMap<Comparable<?>, Long> freqTable;

    public Frequency() {
        this.freqTable = new TreeMap();
    }

    public Frequency(Comparator<?> comparator) {
        this.freqTable = new TreeMap(comparator);
    }

    public String toString() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        StringBuilder outBuffer = new StringBuilder();
        outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
        for (Comparable<?> value : this.freqTable.keySet()) {
            outBuffer.append((Object) value);
            outBuffer.append('\t');
            outBuffer.append(getCount(value));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getPct(value)));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getCumPct(value)));
            outBuffer.append('\n');
        }
        return outBuffer.toString();
    }

    public void addValue(Comparable<?> v2) throws MathIllegalArgumentException {
        incrementValue(v2, 1L);
    }

    public void addValue(int v2) throws MathIllegalArgumentException {
        addValue(Long.valueOf(v2));
    }

    public void addValue(long v2) throws MathIllegalArgumentException {
        addValue(Long.valueOf(v2));
    }

    public void addValue(char v2) throws MathIllegalArgumentException {
        addValue(Character.valueOf(v2));
    }

    public void incrementValue(Comparable<?> v2, long increment) throws MathIllegalArgumentException {
        Comparable<?> obj = v2;
        if (v2 instanceof Integer) {
            obj = Long.valueOf(((Integer) v2).longValue());
        }
        try {
            Long count = this.freqTable.get(obj);
            if (count == null) {
                this.freqTable.put(obj, Long.valueOf(increment));
            } else {
                this.freqTable.put(obj, Long.valueOf(count.longValue() + increment));
            }
        } catch (ClassCastException e2) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSTANCES_NOT_COMPARABLE_TO_EXISTING_VALUES, v2.getClass().getName());
        }
    }

    public void incrementValue(int v2, long increment) throws MathIllegalArgumentException {
        incrementValue(Long.valueOf(v2), increment);
    }

    public void incrementValue(long v2, long increment) throws MathIllegalArgumentException {
        incrementValue(Long.valueOf(v2), increment);
    }

    public void incrementValue(char v2, long increment) throws MathIllegalArgumentException {
        incrementValue(Character.valueOf(v2), increment);
    }

    public void clear() {
        this.freqTable.clear();
    }

    public Iterator<Comparable<?>> valuesIterator() {
        return this.freqTable.keySet().iterator();
    }

    public Iterator<Map.Entry<Comparable<?>, Long>> entrySetIterator() {
        return this.freqTable.entrySet().iterator();
    }

    public long getSumFreq() {
        long result = 0;
        Iterator<Long> iterator = this.freqTable.values().iterator();
        while (iterator.hasNext()) {
            result += iterator.next().longValue();
        }
        return result;
    }

    public long getCount(Comparable<?> v2) {
        if (v2 instanceof Integer) {
            return getCount(((Integer) v2).longValue());
        }
        long result = 0;
        try {
            Long count = this.freqTable.get(v2);
            if (count != null) {
                result = count.longValue();
            }
        } catch (ClassCastException e2) {
        }
        return result;
    }

    public long getCount(int v2) {
        return getCount(Long.valueOf(v2));
    }

    public long getCount(long v2) {
        return getCount(Long.valueOf(v2));
    }

    public long getCount(char v2) {
        return getCount(Character.valueOf(v2));
    }

    public int getUniqueCount() {
        return this.freqTable.keySet().size();
    }

    public double getPct(Comparable<?> v2) {
        long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return getCount(v2) / sumFreq;
    }

    public double getPct(int v2) {
        return getPct(Long.valueOf(v2));
    }

    public double getPct(long v2) {
        return getPct(Long.valueOf(v2));
    }

    public double getPct(char v2) {
        return getPct(Character.valueOf(v2));
    }

    public long getCumFreq(Comparable<?> v2) {
        if (getSumFreq() == 0) {
            return 0L;
        }
        if (v2 instanceof Integer) {
            return getCumFreq(((Integer) v2).longValue());
        }
        Comparator<? super Comparable<?>> comparator = this.freqTable.comparator();
        if (comparator == null) {
            comparator = new NaturalComparator();
        }
        long result = 0;
        try {
            Long value = this.freqTable.get(v2);
            if (value != null) {
                result = value.longValue();
            }
            if (comparator.compare(v2, this.freqTable.firstKey()) < 0) {
                return 0L;
            }
            if (comparator.compare(v2, this.freqTable.lastKey()) >= 0) {
                return getSumFreq();
            }
            Iterator<Comparable<?>> values = valuesIterator();
            while (values.hasNext()) {
                Comparable<?> nextValue = values.next();
                if (comparator.compare(v2, nextValue) > 0) {
                    result += getCount(nextValue);
                } else {
                    return result;
                }
            }
            return result;
        } catch (ClassCastException e2) {
            return result;
        }
    }

    public long getCumFreq(int v2) {
        return getCumFreq(Long.valueOf(v2));
    }

    public long getCumFreq(long v2) {
        return getCumFreq(Long.valueOf(v2));
    }

    public long getCumFreq(char v2) {
        return getCumFreq(Character.valueOf(v2));
    }

    public double getCumPct(Comparable<?> v2) {
        long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return getCumFreq(v2) / sumFreq;
    }

    public double getCumPct(int v2) {
        return getCumPct(Long.valueOf(v2));
    }

    public double getCumPct(long v2) {
        return getCumPct(Long.valueOf(v2));
    }

    public double getCumPct(char v2) {
        return getCumPct(Character.valueOf(v2));
    }

    public List<Comparable<?>> getMode() {
        long mostPopular = 0;
        for (Long l2 : this.freqTable.values()) {
            long frequency = l2.longValue();
            if (frequency > mostPopular) {
                mostPopular = frequency;
            }
        }
        List<Comparable<?>> modeList = new ArrayList<>();
        for (Map.Entry<Comparable<?>, Long> ent : this.freqTable.entrySet()) {
            if (ent.getValue().longValue() == mostPopular) {
                modeList.add(ent.getKey());
            }
        }
        return modeList;
    }

    public void merge(Frequency other) throws MathIllegalArgumentException {
        MathUtils.checkNotNull(other, LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        Iterator<Map.Entry<Comparable<?>, Long>> iter = other.entrySetIterator();
        while (iter.hasNext()) {
            Map.Entry<Comparable<?>, Long> entry = iter.next();
            incrementValue(entry.getKey(), entry.getValue().longValue());
        }
    }

    public void merge(Collection<Frequency> others) throws MathIllegalArgumentException {
        MathUtils.checkNotNull(others, LocalizedFormats.NULL_NOT_ALLOWED, new Object[0]);
        for (Frequency freq : others) {
            merge(freq);
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/Frequency$NaturalComparator.class */
    private static class NaturalComparator<T extends Comparable<T>> implements Comparator<Comparable<T>>, Serializable {
        private static final long serialVersionUID = -3852193713161395148L;

        private NaturalComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Comparable<T> o1, Comparable<T> o2) {
            return o1.compareTo(o2);
        }
    }

    public int hashCode() {
        int result = (31 * 1) + (this.freqTable == null ? 0 : this.freqTable.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Frequency)) {
            return false;
        }
        Frequency other = (Frequency) obj;
        if (this.freqTable == null) {
            if (other.freqTable != null) {
                return false;
            }
            return true;
        }
        if (!this.freqTable.equals(other.freqTable)) {
            return false;
        }
        return true;
    }
}
