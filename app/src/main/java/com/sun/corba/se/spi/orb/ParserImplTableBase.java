package com.sun.corba.se.spi.orb;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/ParserImplTableBase.class */
public abstract class ParserImplTableBase extends ParserImplBase {
    private final ParserData[] entries;

    public ParserImplTableBase(ParserData[] parserDataArr) {
        this.entries = parserDataArr;
        setDefaultValues();
    }

    @Override // com.sun.corba.se.spi.orb.ParserImplBase
    protected PropertyParser makeParser() {
        PropertyParser propertyParser = new PropertyParser();
        for (int i2 = 0; i2 < this.entries.length; i2++) {
            this.entries[i2].addToParser(propertyParser);
        }
        return propertyParser;
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/ParserImplTableBase$MapEntry.class */
    private static final class MapEntry implements Map.Entry {
        private Object key;
        private Object value;

        public MapEntry(Object obj) {
            this.key = obj;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            Object obj2 = this.value;
            this.value = obj;
            return obj2;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof MapEntry)) {
                return false;
            }
            MapEntry mapEntry = (MapEntry) obj;
            return this.key.equals(mapEntry.key) && this.value.equals(mapEntry.value);
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orb/ParserImplTableBase$FieldMap.class */
    private static class FieldMap extends AbstractMap {
        private final ParserData[] entries;
        private final boolean useDefault;

        public FieldMap(ParserData[] parserDataArr, boolean z2) {
            this.entries = parserDataArr;
            this.useDefault = z2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set entrySet() {
            return new AbstractSet() { // from class: com.sun.corba.se.spi.orb.ParserImplTableBase.FieldMap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator iterator() {
                    return new Iterator() { // from class: com.sun.corba.se.spi.orb.ParserImplTableBase.FieldMap.1.1
                        int ctr = 0;

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.ctr < FieldMap.this.entries.length;
                        }

                        @Override // java.util.Iterator
                        public Object next() {
                            ParserData[] parserDataArr = FieldMap.this.entries;
                            int i2 = this.ctr;
                            this.ctr = i2 + 1;
                            ParserData parserData = parserDataArr[i2];
                            MapEntry mapEntry = new MapEntry(parserData.getFieldName());
                            if (FieldMap.this.useDefault) {
                                mapEntry.setValue(parserData.getDefaultValue());
                            } else {
                                mapEntry.setValue(parserData.getTestValue());
                            }
                            return mapEntry;
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return FieldMap.this.entries.length;
                }
            };
        }
    }

    protected void setDefaultValues() {
        setFields(new FieldMap(this.entries, true));
    }

    public void setTestValues() {
        setFields(new FieldMap(this.entries, false));
    }
}
