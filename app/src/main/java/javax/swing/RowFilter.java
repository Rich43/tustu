package javax.swing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: rt.jar:javax/swing/RowFilter.class */
public abstract class RowFilter<M, I> {

    /* loaded from: rt.jar:javax/swing/RowFilter$ComparisonType.class */
    public enum ComparisonType {
        BEFORE,
        AFTER,
        EQUAL,
        NOT_EQUAL
    }

    public abstract boolean include(Entry<? extends M, ? extends I> entry);

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkIndices(int[] iArr) {
        for (int length = iArr.length - 1; length >= 0; length--) {
            if (iArr[length] < 0) {
                throw new IllegalArgumentException("Index must be >= 0");
            }
        }
    }

    public static <M, I> RowFilter<M, I> regexFilter(String str, int... iArr) {
        return new RegexFilter(Pattern.compile(str), iArr);
    }

    public static <M, I> RowFilter<M, I> dateFilter(ComparisonType comparisonType, Date date, int... iArr) {
        return new DateFilter(comparisonType, date.getTime(), iArr);
    }

    public static <M, I> RowFilter<M, I> numberFilter(ComparisonType comparisonType, Number number, int... iArr) {
        return new NumberFilter(comparisonType, number, iArr);
    }

    public static <M, I> RowFilter<M, I> orFilter(Iterable<? extends RowFilter<? super M, ? super I>> iterable) {
        return new OrFilter(iterable);
    }

    public static <M, I> RowFilter<M, I> andFilter(Iterable<? extends RowFilter<? super M, ? super I>> iterable) {
        return new AndFilter(iterable);
    }

    public static <M, I> RowFilter<M, I> notFilter(RowFilter<M, I> rowFilter) {
        return new NotFilter(rowFilter);
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$Entry.class */
    public static abstract class Entry<M, I> {
        public abstract M getModel();

        public abstract int getValueCount();

        public abstract Object getValue(int i2);

        public abstract I getIdentifier();

        public String getStringValue(int i2) {
            Object value = getValue(i2);
            return value == null ? "" : value.toString();
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$GeneralFilter.class */
    private static abstract class GeneralFilter extends RowFilter<Object, Object> {
        private int[] columns;

        protected abstract boolean include(Entry<? extends Object, ? extends Object> entry, int i2);

        GeneralFilter(int[] iArr) {
            RowFilter.checkIndices(iArr);
            this.columns = iArr;
        }

        @Override // javax.swing.RowFilter
        public boolean include(Entry<? extends Object, ? extends Object> entry) {
            int valueCount = entry.getValueCount();
            if (this.columns.length > 0) {
                for (int length = this.columns.length - 1; length >= 0; length--) {
                    int i2 = this.columns[length];
                    if (i2 < valueCount && include(entry, i2)) {
                        return true;
                    }
                }
                return false;
            }
            do {
                valueCount--;
                if (valueCount < 0) {
                    return false;
                }
            } while (!include(entry, valueCount));
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$RegexFilter.class */
    private static class RegexFilter extends GeneralFilter {
        private Matcher matcher;

        RegexFilter(Pattern pattern, int[] iArr) {
            super(iArr);
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern must be non-null");
            }
            this.matcher = pattern.matcher("");
        }

        @Override // javax.swing.RowFilter.GeneralFilter
        protected boolean include(Entry<? extends Object, ? extends Object> entry, int i2) {
            this.matcher.reset(entry.getStringValue(i2));
            return this.matcher.find();
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$DateFilter.class */
    private static class DateFilter extends GeneralFilter {
        private long date;
        private ComparisonType type;

        DateFilter(ComparisonType comparisonType, long j2, int[] iArr) {
            super(iArr);
            if (comparisonType == null) {
                throw new IllegalArgumentException("type must be non-null");
            }
            this.type = comparisonType;
            this.date = j2;
        }

        @Override // javax.swing.RowFilter.GeneralFilter
        protected boolean include(Entry<? extends Object, ? extends Object> entry, int i2) {
            Object value = entry.getValue(i2);
            if (value instanceof Date) {
                long time = ((Date) value).getTime();
                switch (this.type) {
                    case BEFORE:
                        if (time < this.date) {
                        }
                        break;
                    case AFTER:
                        if (time > this.date) {
                        }
                        break;
                    case EQUAL:
                        if (time == this.date) {
                        }
                        break;
                    case NOT_EQUAL:
                        if (time != this.date) {
                        }
                        break;
                }
                return false;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$NumberFilter.class */
    private static class NumberFilter extends GeneralFilter {
        private boolean isComparable;
        private Number number;
        private ComparisonType type;

        NumberFilter(ComparisonType comparisonType, Number number, int[] iArr) {
            super(iArr);
            if (comparisonType == null || number == null) {
                throw new IllegalArgumentException("type and number must be non-null");
            }
            this.type = comparisonType;
            this.number = number;
            this.isComparable = number instanceof Comparable;
        }

        @Override // javax.swing.RowFilter.GeneralFilter
        protected boolean include(Entry<? extends Object, ? extends Object> entry, int i2) {
            int iLongCompare;
            Object value = entry.getValue(i2);
            if (value instanceof Number) {
                if (this.number.getClass() == value.getClass() && this.isComparable) {
                    iLongCompare = ((Comparable) this.number).compareTo(value);
                } else {
                    iLongCompare = longCompare((Number) value);
                }
                switch (this.type) {
                    case BEFORE:
                        if (iLongCompare > 0) {
                        }
                        break;
                    case AFTER:
                        if (iLongCompare < 0) {
                        }
                        break;
                    case EQUAL:
                        if (iLongCompare == 0) {
                        }
                        break;
                    case NOT_EQUAL:
                        if (iLongCompare != 0) {
                        }
                        break;
                }
                return false;
            }
            return false;
        }

        private int longCompare(Number number) {
            long jLongValue = this.number.longValue() - number.longValue();
            if (jLongValue < 0) {
                return -1;
            }
            if (jLongValue > 0) {
                return 1;
            }
            return 0;
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$OrFilter.class */
    private static class OrFilter<M, I> extends RowFilter<M, I> {
        List<RowFilter<? super M, ? super I>> filters = new ArrayList();

        OrFilter(Iterable<? extends RowFilter<? super M, ? super I>> iterable) {
            for (RowFilter<? super M, ? super I> rowFilter : iterable) {
                if (rowFilter == null) {
                    throw new IllegalArgumentException("Filter must be non-null");
                }
                this.filters.add(rowFilter);
            }
        }

        @Override // javax.swing.RowFilter
        public boolean include(Entry<? extends M, ? extends I> entry) {
            Iterator<RowFilter<? super M, ? super I>> it = this.filters.iterator();
            while (it.hasNext()) {
                if (it.next().include(entry)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$AndFilter.class */
    private static class AndFilter<M, I> extends OrFilter<M, I> {
        AndFilter(Iterable<? extends RowFilter<? super M, ? super I>> iterable) {
            super(iterable);
        }

        @Override // javax.swing.RowFilter.OrFilter, javax.swing.RowFilter
        public boolean include(Entry<? extends M, ? extends I> entry) {
            Iterator<RowFilter<? super M, ? super I>> it = this.filters.iterator();
            while (it.hasNext()) {
                if (!it.next().include(entry)) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/RowFilter$NotFilter.class */
    private static class NotFilter<M, I> extends RowFilter<M, I> {
        private RowFilter<M, I> filter;

        NotFilter(RowFilter<M, I> rowFilter) {
            if (rowFilter == null) {
                throw new IllegalArgumentException("filter must be non-null");
            }
            this.filter = rowFilter;
        }

        @Override // javax.swing.RowFilter
        public boolean include(Entry<? extends M, ? extends I> entry) {
            return !this.filter.include(entry);
        }
    }
}
