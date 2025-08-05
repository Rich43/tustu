package java.text;

import java.text.AttributedCharacterIterator;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/* loaded from: rt.jar:java/text/AttributedString.class */
public class AttributedString {
    private static final int ARRAY_SIZE_INCREMENT = 10;
    String text;
    int runArraySize;
    int runCount;
    int[] runStarts;
    Vector<AttributedCharacterIterator.Attribute>[] runAttributes;
    Vector<Object>[] runAttributeValues;

    AttributedString(AttributedCharacterIterator[] attributedCharacterIteratorArr) {
        if (attributedCharacterIteratorArr == null) {
            throw new NullPointerException("Iterators must not be null");
        }
        if (attributedCharacterIteratorArr.length == 0) {
            this.text = "";
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (AttributedCharacterIterator attributedCharacterIterator : attributedCharacterIteratorArr) {
            appendContents(stringBuffer, attributedCharacterIterator);
        }
        this.text = stringBuffer.toString();
        if (this.text.length() > 0) {
            int i2 = 0;
            Map<AttributedCharacterIterator.Attribute, Object> map = null;
            for (AttributedCharacterIterator attributedCharacterIterator2 : attributedCharacterIteratorArr) {
                int beginIndex = attributedCharacterIterator2.getBeginIndex();
                int endIndex = attributedCharacterIterator2.getEndIndex();
                int runLimit = beginIndex;
                while (true) {
                    int i3 = runLimit;
                    if (i3 < endIndex) {
                        attributedCharacterIterator2.setIndex(i3);
                        Map<AttributedCharacterIterator.Attribute, Object> attributes = attributedCharacterIterator2.getAttributes();
                        if (mapsDiffer(map, attributes)) {
                            setAttributes(attributes, (i3 - beginIndex) + i2);
                        }
                        map = attributes;
                        runLimit = attributedCharacterIterator2.getRunLimit();
                    }
                }
                i2 += endIndex - beginIndex;
            }
        }
    }

    public AttributedString(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.text = str;
    }

    public AttributedString(String str, Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        if (str == null || map == null) {
            throw new NullPointerException();
        }
        this.text = str;
        if (str.length() == 0) {
            if (map.isEmpty()) {
                return;
            } else {
                throw new IllegalArgumentException("Can't add attribute to 0-length text");
            }
        }
        int size = map.size();
        if (size > 0) {
            createRunAttributeDataVectors();
            Vector<AttributedCharacterIterator.Attribute> vector = new Vector<>(size);
            Vector<Object> vector2 = new Vector<>(size);
            this.runAttributes[0] = vector;
            this.runAttributeValues[0] = vector2;
            for (Map.Entry<? extends AttributedCharacterIterator.Attribute, ?> entry : map.entrySet()) {
                vector.addElement(entry.getKey());
                vector2.addElement(entry.getValue());
            }
        }
    }

    public AttributedString(AttributedCharacterIterator attributedCharacterIterator) {
        this(attributedCharacterIterator, attributedCharacterIterator.getBeginIndex(), attributedCharacterIterator.getEndIndex(), null);
    }

    public AttributedString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        this(attributedCharacterIterator, i2, i3, null);
    }

    public AttributedString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3, AttributedCharacterIterator.Attribute[] attributeArr) {
        if (attributedCharacterIterator == null) {
            throw new NullPointerException();
        }
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        int endIndex = attributedCharacterIterator.getEndIndex();
        if (i2 < beginIndex || i3 > endIndex || i2 > i3) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        StringBuffer stringBuffer = new StringBuffer();
        attributedCharacterIterator.setIndex(i2);
        char cCurrent = attributedCharacterIterator.current();
        while (true) {
            char c2 = cCurrent;
            if (attributedCharacterIterator.getIndex() >= i3) {
                break;
            }
            stringBuffer.append(c2);
            cCurrent = attributedCharacterIterator.next();
        }
        this.text = stringBuffer.toString();
        if (i2 == i3) {
            return;
        }
        HashSet hashSet = new HashSet();
        if (attributeArr == null) {
            hashSet.addAll(attributedCharacterIterator.getAllAttributeKeys());
        } else {
            for (AttributedCharacterIterator.Attribute attribute : attributeArr) {
                hashSet.add(attribute);
            }
            hashSet.retainAll(attributedCharacterIterator.getAllAttributeKeys());
        }
        if (hashSet.isEmpty()) {
            return;
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            AttributedCharacterIterator.Attribute attribute2 = (AttributedCharacterIterator.Attribute) it.next();
            attributedCharacterIterator.setIndex(beginIndex);
            while (attributedCharacterIterator.getIndex() < i3) {
                int runStart = attributedCharacterIterator.getRunStart(attribute2);
                int runLimit = attributedCharacterIterator.getRunLimit(attribute2);
                Object attribute3 = attributedCharacterIterator.getAttribute(attribute2);
                if (attribute3 != null) {
                    if (attribute3 instanceof Annotation) {
                        if (runStart >= i2 && runLimit <= i3) {
                            addAttribute(attribute2, attribute3, runStart - i2, runLimit - i2);
                        } else if (runLimit > i3) {
                            break;
                        }
                    } else {
                        if (runStart >= i3) {
                            break;
                        }
                        if (runLimit > i2) {
                            runStart = runStart < i2 ? i2 : runStart;
                            runLimit = runLimit > i3 ? i3 : runLimit;
                            if (runStart != runLimit) {
                                addAttribute(attribute2, attribute3, runStart - i2, runLimit - i2);
                            }
                        }
                    }
                }
                attributedCharacterIterator.setIndex(runLimit);
            }
        }
    }

    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj) {
        if (attribute == null) {
            throw new NullPointerException();
        }
        int length = length();
        if (length == 0) {
            throw new IllegalArgumentException("Can't add attribute to 0-length text");
        }
        addAttributeImpl(attribute, obj, 0, length);
    }

    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj, int i2, int i3) {
        if (attribute == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 > length() || i2 >= i3) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        addAttributeImpl(attribute, obj, i2, i3);
    }

    public void addAttributes(Map<? extends AttributedCharacterIterator.Attribute, ?> map, int i2, int i3) {
        if (map == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 > length() || i2 > i3) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        if (i2 == i3) {
            if (map.isEmpty()) {
                return;
            } else {
                throw new IllegalArgumentException("Can't add attribute to 0-length text");
            }
        }
        if (this.runCount == 0) {
            createRunAttributeDataVectors();
        }
        int iEnsureRunBreak = ensureRunBreak(i2);
        int iEnsureRunBreak2 = ensureRunBreak(i3);
        for (Map.Entry<? extends AttributedCharacterIterator.Attribute, ?> entry : map.entrySet()) {
            addAttributeRunData(entry.getKey(), entry.getValue(), iEnsureRunBreak, iEnsureRunBreak2);
        }
    }

    private synchronized void addAttributeImpl(AttributedCharacterIterator.Attribute attribute, Object obj, int i2, int i3) {
        if (this.runCount == 0) {
            createRunAttributeDataVectors();
        }
        addAttributeRunData(attribute, obj, ensureRunBreak(i2), ensureRunBreak(i3));
    }

    private final void createRunAttributeDataVectors() {
        this.runStarts = new int[10];
        this.runAttributes = new Vector[10];
        this.runAttributeValues = new Vector[10];
        this.runArraySize = 10;
        this.runCount = 1;
    }

    private final int ensureRunBreak(int i2) {
        return ensureRunBreak(i2, true);
    }

    private final int ensureRunBreak(int i2, boolean z2) {
        if (i2 == length()) {
            return this.runCount;
        }
        int i3 = 0;
        while (i3 < this.runCount && this.runStarts[i3] < i2) {
            i3++;
        }
        if (i3 < this.runCount && this.runStarts[i3] == i2) {
            return i3;
        }
        if (this.runCount == this.runArraySize) {
            int i4 = this.runArraySize + 10;
            int[] iArr = new int[i4];
            Vector<AttributedCharacterIterator.Attribute>[] vectorArr = new Vector[i4];
            Vector<Object>[] vectorArr2 = new Vector[i4];
            for (int i5 = 0; i5 < this.runArraySize; i5++) {
                iArr[i5] = this.runStarts[i5];
                vectorArr[i5] = this.runAttributes[i5];
                vectorArr2[i5] = this.runAttributeValues[i5];
            }
            this.runStarts = iArr;
            this.runAttributes = vectorArr;
            this.runAttributeValues = vectorArr2;
            this.runArraySize = i4;
        }
        Vector<AttributedCharacterIterator.Attribute> vector = null;
        Vector<Object> vector2 = null;
        if (z2) {
            Vector<AttributedCharacterIterator.Attribute> vector3 = this.runAttributes[i3 - 1];
            Vector<Object> vector4 = this.runAttributeValues[i3 - 1];
            if (vector3 != null) {
                vector = new Vector<>(vector3);
            }
            if (vector4 != null) {
                vector2 = new Vector<>(vector4);
            }
        }
        this.runCount++;
        for (int i6 = this.runCount - 1; i6 > i3; i6--) {
            this.runStarts[i6] = this.runStarts[i6 - 1];
            this.runAttributes[i6] = this.runAttributes[i6 - 1];
            this.runAttributeValues[i6] = this.runAttributeValues[i6 - 1];
        }
        this.runStarts[i3] = i2;
        this.runAttributes[i3] = vector;
        this.runAttributeValues[i3] = vector2;
        return i3;
    }

    private void addAttributeRunData(AttributedCharacterIterator.Attribute attribute, Object obj, int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            int iIndexOf = -1;
            if (this.runAttributes[i4] == null) {
                Vector<AttributedCharacterIterator.Attribute> vector = new Vector<>();
                Vector<Object> vector2 = new Vector<>();
                this.runAttributes[i4] = vector;
                this.runAttributeValues[i4] = vector2;
            } else {
                iIndexOf = this.runAttributes[i4].indexOf(attribute);
            }
            if (iIndexOf == -1) {
                int size = this.runAttributes[i4].size();
                this.runAttributes[i4].addElement(attribute);
                try {
                    this.runAttributeValues[i4].addElement(obj);
                } catch (Exception e2) {
                    this.runAttributes[i4].setSize(size);
                    this.runAttributeValues[i4].setSize(size);
                }
            } else {
                this.runAttributeValues[i4].set(iIndexOf, obj);
            }
        }
    }

    public AttributedCharacterIterator getIterator() {
        return getIterator(null, 0, length());
    }

    public AttributedCharacterIterator getIterator(AttributedCharacterIterator.Attribute[] attributeArr) {
        return getIterator(attributeArr, 0, length());
    }

    public AttributedCharacterIterator getIterator(AttributedCharacterIterator.Attribute[] attributeArr, int i2, int i3) {
        return new AttributedStringIterator(attributeArr, i2, i3);
    }

    int length() {
        return this.text.length();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public char charAt(int i2) {
        return this.text.charAt(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Object getAttribute(AttributedCharacterIterator.Attribute attribute, int i2) {
        int iIndexOf;
        Vector<AttributedCharacterIterator.Attribute> vector = this.runAttributes[i2];
        Vector<Object> vector2 = this.runAttributeValues[i2];
        if (vector != null && (iIndexOf = vector.indexOf(attribute)) != -1) {
            return vector2.elementAt(iIndexOf);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object getAttributeCheckRange(AttributedCharacterIterator.Attribute attribute, int i2, int i3, int i4) {
        int i5;
        int i6;
        Object attribute2 = getAttribute(attribute, i2);
        if (attribute2 instanceof Annotation) {
            if (i3 > 0) {
                int i7 = i2;
                int i8 = this.runStarts[i7];
                while (true) {
                    i6 = i8;
                    if (i6 < i3 || !valuesMatch(attribute2, getAttribute(attribute, i7 - 1))) {
                        break;
                    }
                    i7--;
                    i8 = this.runStarts[i7];
                }
                if (i6 < i3) {
                    return null;
                }
            }
            int length = length();
            if (i4 < length) {
                int i9 = i2;
                int i10 = i9 < this.runCount - 1 ? this.runStarts[i9 + 1] : length;
                while (true) {
                    i5 = i10;
                    if (i5 > i4 || !valuesMatch(attribute2, getAttribute(attribute, i9 + 1))) {
                        break;
                    }
                    i9++;
                    i10 = i9 < this.runCount - 1 ? this.runStarts[i9 + 1] : length;
                }
                if (i5 > i4) {
                    return null;
                }
            }
        }
        return attribute2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean attributeValuesMatch(Set<? extends AttributedCharacterIterator.Attribute> set, int i2, int i3) {
        for (AttributedCharacterIterator.Attribute attribute : set) {
            if (!valuesMatch(getAttribute(attribute, i2), getAttribute(attribute, i3))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean valuesMatch(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    private final void appendContents(StringBuffer stringBuffer, CharacterIterator characterIterator) {
        int beginIndex = characterIterator.getBeginIndex();
        int endIndex = characterIterator.getEndIndex();
        while (beginIndex < endIndex) {
            int i2 = beginIndex;
            beginIndex++;
            characterIterator.setIndex(i2);
            stringBuffer.append(characterIterator.current());
        }
    }

    private void setAttributes(Map<AttributedCharacterIterator.Attribute, Object> map, int i2) {
        int size;
        if (this.runCount == 0) {
            createRunAttributeDataVectors();
        }
        int iEnsureRunBreak = ensureRunBreak(i2, false);
        if (map != null && (size = map.size()) > 0) {
            Vector<AttributedCharacterIterator.Attribute> vector = new Vector<>(size);
            Vector<Object> vector2 = new Vector<>(size);
            for (Map.Entry<AttributedCharacterIterator.Attribute, Object> entry : map.entrySet()) {
                vector.add(entry.getKey());
                vector2.add(entry.getValue());
            }
            this.runAttributes[iEnsureRunBreak] = vector;
            this.runAttributeValues[iEnsureRunBreak] = vector2;
        }
    }

    private static <K, V> boolean mapsDiffer(Map<K, V> map, Map<K, V> map2) {
        return map == null ? map2 != null && map2.size() > 0 : !map.equals(map2);
    }

    /* loaded from: rt.jar:java/text/AttributedString$AttributedStringIterator.class */
    private final class AttributedStringIterator implements AttributedCharacterIterator {
        private int beginIndex;
        private int endIndex;
        private AttributedCharacterIterator.Attribute[] relevantAttributes;
        private int currentIndex;
        private int currentRunIndex;
        private int currentRunStart;
        private int currentRunLimit;

        AttributedStringIterator(AttributedCharacterIterator.Attribute[] attributeArr, int i2, int i3) {
            if (i2 < 0 || i2 > i3 || i3 > AttributedString.this.length()) {
                throw new IllegalArgumentException("Invalid substring range");
            }
            this.beginIndex = i2;
            this.endIndex = i3;
            this.currentIndex = i2;
            updateRunInfo();
            if (attributeArr != null) {
                this.relevantAttributes = (AttributedCharacterIterator.Attribute[]) attributeArr.clone();
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AttributedStringIterator)) {
                return false;
            }
            AttributedStringIterator attributedStringIterator = (AttributedStringIterator) obj;
            if (AttributedString.this != attributedStringIterator.getString() || this.currentIndex != attributedStringIterator.currentIndex || this.beginIndex != attributedStringIterator.beginIndex || this.endIndex != attributedStringIterator.endIndex) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return ((AttributedString.this.text.hashCode() ^ this.currentIndex) ^ this.beginIndex) ^ this.endIndex;
        }

        @Override // java.text.CharacterIterator
        public Object clone() {
            try {
                return (AttributedStringIterator) super.clone();
            } catch (CloneNotSupportedException e2) {
                throw new InternalError(e2);
            }
        }

        @Override // java.text.CharacterIterator
        public char first() {
            return internalSetIndex(this.beginIndex);
        }

        @Override // java.text.CharacterIterator
        public char last() {
            if (this.endIndex == this.beginIndex) {
                return internalSetIndex(this.endIndex);
            }
            return internalSetIndex(this.endIndex - 1);
        }

        @Override // java.text.CharacterIterator
        public char current() {
            if (this.currentIndex != this.endIndex) {
                return AttributedString.this.charAt(this.currentIndex);
            }
            return (char) 65535;
        }

        @Override // java.text.CharacterIterator
        public char next() {
            if (this.currentIndex < this.endIndex) {
                return internalSetIndex(this.currentIndex + 1);
            }
            return (char) 65535;
        }

        @Override // java.text.CharacterIterator
        public char previous() {
            if (this.currentIndex > this.beginIndex) {
                return internalSetIndex(this.currentIndex - 1);
            }
            return (char) 65535;
        }

        @Override // java.text.CharacterIterator
        public char setIndex(int i2) {
            if (i2 < this.beginIndex || i2 > this.endIndex) {
                throw new IllegalArgumentException("Invalid index");
            }
            return internalSetIndex(i2);
        }

        @Override // java.text.CharacterIterator
        public int getBeginIndex() {
            return this.beginIndex;
        }

        @Override // java.text.CharacterIterator
        public int getEndIndex() {
            return this.endIndex;
        }

        @Override // java.text.CharacterIterator
        public int getIndex() {
            return this.currentIndex;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunStart() {
            return this.currentRunStart;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunStart(AttributedCharacterIterator.Attribute attribute) {
            if (this.currentRunStart == this.beginIndex || this.currentRunIndex == -1) {
                return this.currentRunStart;
            }
            Object attribute2 = getAttribute(attribute);
            int i2 = this.currentRunStart;
            int i3 = this.currentRunIndex;
            while (i2 > this.beginIndex && AttributedString.valuesMatch(attribute2, AttributedString.this.getAttribute(attribute, i3 - 1))) {
                i3--;
                i2 = AttributedString.this.runStarts[i3];
            }
            if (i2 < this.beginIndex) {
                i2 = this.beginIndex;
            }
            return i2;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunStart(Set<? extends AttributedCharacterIterator.Attribute> set) {
            if (this.currentRunStart == this.beginIndex || this.currentRunIndex == -1) {
                return this.currentRunStart;
            }
            int i2 = this.currentRunStart;
            int i3 = this.currentRunIndex;
            while (i2 > this.beginIndex && AttributedString.this.attributeValuesMatch(set, this.currentRunIndex, i3 - 1)) {
                i3--;
                i2 = AttributedString.this.runStarts[i3];
            }
            if (i2 < this.beginIndex) {
                i2 = this.beginIndex;
            }
            return i2;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunLimit() {
            return this.currentRunLimit;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunLimit(AttributedCharacterIterator.Attribute attribute) {
            if (this.currentRunLimit == this.endIndex || this.currentRunIndex == -1) {
                return this.currentRunLimit;
            }
            Object attribute2 = getAttribute(attribute);
            int i2 = this.currentRunLimit;
            int i3 = this.currentRunIndex;
            while (i2 < this.endIndex && AttributedString.valuesMatch(attribute2, AttributedString.this.getAttribute(attribute, i3 + 1))) {
                i3++;
                i2 = i3 < AttributedString.this.runCount - 1 ? AttributedString.this.runStarts[i3 + 1] : this.endIndex;
            }
            if (i2 > this.endIndex) {
                i2 = this.endIndex;
            }
            return i2;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunLimit(Set<? extends AttributedCharacterIterator.Attribute> set) {
            if (this.currentRunLimit == this.endIndex || this.currentRunIndex == -1) {
                return this.currentRunLimit;
            }
            int i2 = this.currentRunLimit;
            int i3 = this.currentRunIndex;
            while (i2 < this.endIndex && AttributedString.this.attributeValuesMatch(set, this.currentRunIndex, i3 + 1)) {
                i3++;
                i2 = i3 < AttributedString.this.runCount - 1 ? AttributedString.this.runStarts[i3 + 1] : this.endIndex;
            }
            if (i2 > this.endIndex) {
                i2 = this.endIndex;
            }
            return i2;
        }

        @Override // java.text.AttributedCharacterIterator
        public Map<AttributedCharacterIterator.Attribute, Object> getAttributes() {
            if (AttributedString.this.runAttributes == null || this.currentRunIndex == -1 || AttributedString.this.runAttributes[this.currentRunIndex] == null) {
                return new Hashtable();
            }
            return AttributedString.this.new AttributeMap(this.currentRunIndex, this.beginIndex, this.endIndex);
        }

        @Override // java.text.AttributedCharacterIterator
        public Set<AttributedCharacterIterator.Attribute> getAllAttributeKeys() {
            HashSet hashSet;
            Vector<AttributedCharacterIterator.Attribute> vector;
            if (AttributedString.this.runAttributes == null) {
                return new HashSet();
            }
            synchronized (AttributedString.this) {
                hashSet = new HashSet();
                for (int i2 = 0; i2 < AttributedString.this.runCount; i2++) {
                    if (AttributedString.this.runStarts[i2] < this.endIndex && ((i2 == AttributedString.this.runCount - 1 || AttributedString.this.runStarts[i2 + 1] > this.beginIndex) && (vector = AttributedString.this.runAttributes[i2]) != null)) {
                        int size = vector.size();
                        while (true) {
                            int i3 = size;
                            size--;
                            if (i3 > 0) {
                                hashSet.add(vector.get(size));
                            }
                        }
                    }
                }
            }
            return hashSet;
        }

        @Override // java.text.AttributedCharacterIterator
        public Object getAttribute(AttributedCharacterIterator.Attribute attribute) {
            int i2 = this.currentRunIndex;
            if (i2 >= 0) {
                return AttributedString.this.getAttributeCheckRange(attribute, i2, this.beginIndex, this.endIndex);
            }
            return null;
        }

        private AttributedString getString() {
            return AttributedString.this;
        }

        private char internalSetIndex(int i2) {
            this.currentIndex = i2;
            if (i2 < this.currentRunStart || i2 >= this.currentRunLimit) {
                updateRunInfo();
            }
            if (this.currentIndex != this.endIndex) {
                return AttributedString.this.charAt(i2);
            }
            return (char) 65535;
        }

        private void updateRunInfo() {
            if (this.currentIndex == this.endIndex) {
                int i2 = this.endIndex;
                this.currentRunLimit = i2;
                this.currentRunStart = i2;
                this.currentRunIndex = -1;
                return;
            }
            synchronized (AttributedString.this) {
                int i3 = -1;
                while (i3 < AttributedString.this.runCount - 1 && AttributedString.this.runStarts[i3 + 1] <= this.currentIndex) {
                    i3++;
                }
                this.currentRunIndex = i3;
                if (i3 >= 0) {
                    this.currentRunStart = AttributedString.this.runStarts[i3];
                    if (this.currentRunStart < this.beginIndex) {
                        this.currentRunStart = this.beginIndex;
                    }
                } else {
                    this.currentRunStart = this.beginIndex;
                }
                if (i3 < AttributedString.this.runCount - 1) {
                    this.currentRunLimit = AttributedString.this.runStarts[i3 + 1];
                    if (this.currentRunLimit > this.endIndex) {
                        this.currentRunLimit = this.endIndex;
                    }
                } else {
                    this.currentRunLimit = this.endIndex;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/text/AttributedString$AttributeMap.class */
    private final class AttributeMap extends AbstractMap<AttributedCharacterIterator.Attribute, Object> {
        int runIndex;
        int beginIndex;
        int endIndex;

        AttributeMap(int i2, int i3, int i4) {
            this.runIndex = i2;
            this.beginIndex = i3;
            this.endIndex = i4;
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0078 A[Catch: all -> 0x0097, PHI: r12
  0x0078: PHI (r12v1 java.lang.Object) = (r12v0 java.lang.Object), (r12v2 java.lang.Object) binds: [B:8:0x0056, B:10:0x0072] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {, blocks: (B:4:0x000f, B:7:0x0028, B:9:0x0059, B:13:0x008c, B:12:0x0078, B:15:0x0093), top: B:24:0x000f }] */
        @Override // java.util.AbstractMap, java.util.Map
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.util.Set<java.util.Map.Entry<java.text.AttributedCharacterIterator.Attribute, java.lang.Object>> entrySet() {
            /*
                r6 = this;
                java.util.HashSet r0 = new java.util.HashSet
                r1 = r0
                r1.<init>()
                r7 = r0
                r0 = r6
                java.text.AttributedString r0 = java.text.AttributedString.this
                r1 = r0
                r8 = r1
                monitor-enter(r0)
                r0 = r6
                java.text.AttributedString r0 = java.text.AttributedString.this     // Catch: java.lang.Throwable -> L97
                java.util.Vector<java.text.AttributedCharacterIterator$Attribute>[] r0 = r0.runAttributes     // Catch: java.lang.Throwable -> L97
                r1 = r6
                int r1 = r1.runIndex     // Catch: java.lang.Throwable -> L97
                r0 = r0[r1]     // Catch: java.lang.Throwable -> L97
                int r0 = r0.size()     // Catch: java.lang.Throwable -> L97
                r9 = r0
                r0 = 0
                r10 = r0
            L22:
                r0 = r10
                r1 = r9
                if (r0 >= r1) goto L92
                r0 = r6
                java.text.AttributedString r0 = java.text.AttributedString.this     // Catch: java.lang.Throwable -> L97
                java.util.Vector<java.text.AttributedCharacterIterator$Attribute>[] r0 = r0.runAttributes     // Catch: java.lang.Throwable -> L97
                r1 = r6
                int r1 = r1.runIndex     // Catch: java.lang.Throwable -> L97
                r0 = r0[r1]     // Catch: java.lang.Throwable -> L97
                r1 = r10
                java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L97
                java.text.AttributedCharacterIterator$Attribute r0 = (java.text.AttributedCharacterIterator.Attribute) r0     // Catch: java.lang.Throwable -> L97
                r11 = r0
                r0 = r6
                java.text.AttributedString r0 = java.text.AttributedString.this     // Catch: java.lang.Throwable -> L97
                java.util.Vector<java.lang.Object>[] r0 = r0.runAttributeValues     // Catch: java.lang.Throwable -> L97
                r1 = r6
                int r1 = r1.runIndex     // Catch: java.lang.Throwable -> L97
                r0 = r0[r1]     // Catch: java.lang.Throwable -> L97
                r1 = r10
                java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L97
                r12 = r0
                r0 = r12
                boolean r0 = r0 instanceof java.text.Annotation     // Catch: java.lang.Throwable -> L97
                if (r0 == 0) goto L78
                r0 = r6
                java.text.AttributedString r0 = java.text.AttributedString.this     // Catch: java.lang.Throwable -> L97
                r1 = r11
                r2 = r6
                int r2 = r2.runIndex     // Catch: java.lang.Throwable -> L97
                r3 = r6
                int r3 = r3.beginIndex     // Catch: java.lang.Throwable -> L97
                r4 = r6
                int r4 = r4.endIndex     // Catch: java.lang.Throwable -> L97
                java.lang.Object r0 = java.text.AttributedString.access$400(r0, r1, r2, r3, r4)     // Catch: java.lang.Throwable -> L97
                r12 = r0
                r0 = r12
                if (r0 != 0) goto L78
                goto L8c
            L78:
                java.text.AttributeEntry r0 = new java.text.AttributeEntry     // Catch: java.lang.Throwable -> L97
                r1 = r0
                r2 = r11
                r3 = r12
                r1.<init>(r2, r3)     // Catch: java.lang.Throwable -> L97
                r13 = r0
                r0 = r7
                r1 = r13
                boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L97
            L8c:
                int r10 = r10 + 1
                goto L22
            L92:
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L97
                goto L9e
            L97:
                r14 = move-exception
                r0 = r8
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L97
                r0 = r14
                throw r0
            L9e:
                r0 = r7
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.text.AttributedString.AttributeMap.entrySet():java.util.Set");
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object get(Object obj) {
            return AttributedString.this.getAttributeCheckRange((AttributedCharacterIterator.Attribute) obj, this.runIndex, this.beginIndex, this.endIndex);
        }
    }
}
