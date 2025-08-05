package com.sun.java.util.jar.pack;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool.class */
abstract class ConstantPool {
    protected static final Entry[] noRefs;
    protected static final ClassEntry[] noClassRefs;
    static final byte[] TAGS_IN_ORDER;
    static final byte[] TAG_ORDER;
    static final byte[] NUMBER_TAGS;
    static final byte[] EXTRA_TAGS;
    static final byte[] LOADABLE_VALUE_TAGS;
    static final byte[] ANY_MEMBER_TAGS;
    static final byte[] FIELD_SPECIFIC_TAGS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        noRefs = new Entry[0];
        noClassRefs = new ClassEntry[0];
        TAGS_IN_ORDER = new byte[]{1, 3, 4, 5, 6, 8, 7, 13, 12, 9, 10, 11, 15, 16, 17, 18};
        TAG_ORDER = new byte[19];
        for (int i2 = 0; i2 < TAGS_IN_ORDER.length; i2++) {
            TAG_ORDER[TAGS_IN_ORDER[i2]] = (byte) (i2 + 1);
        }
        NUMBER_TAGS = new byte[]{3, 4, 5, 6};
        EXTRA_TAGS = new byte[]{15, 16, 17, 18};
        LOADABLE_VALUE_TAGS = new byte[]{3, 4, 5, 6, 8, 7, 15, 16};
        ANY_MEMBER_TAGS = new byte[]{9, 10, 11};
        FIELD_SPECIFIC_TAGS = new byte[]{3, 4, 5, 6, 8};
        if (!$assertionsDisabled) {
            if (!verifyTagOrder(TAGS_IN_ORDER) || !verifyTagOrder(NUMBER_TAGS) || !verifyTagOrder(EXTRA_TAGS) || !verifyTagOrder(LOADABLE_VALUE_TAGS) || !verifyTagOrder(ANY_MEMBER_TAGS) || !verifyTagOrder(FIELD_SPECIFIC_TAGS)) {
                throw new AssertionError();
            }
        }
    }

    private ConstantPool() {
    }

    static int verbose() {
        return Utils.currentPropMap().getInteger("com.sun.java.util.jar.pack.verbose");
    }

    public static synchronized Utf8Entry getUtf8Entry(String str) {
        Map<String, Utf8Entry> utf8Entries = Utils.getTLGlobals().getUtf8Entries();
        Utf8Entry utf8Entry = utf8Entries.get(str);
        if (utf8Entry == null) {
            utf8Entry = new Utf8Entry(str);
            utf8Entries.put(utf8Entry.stringValue(), utf8Entry);
        }
        return utf8Entry;
    }

    public static ClassEntry getClassEntry(String str) {
        Map<String, ClassEntry> classEntries = Utils.getTLGlobals().getClassEntries();
        ClassEntry classEntry = classEntries.get(str);
        if (classEntry == null) {
            classEntry = new ClassEntry(getUtf8Entry(str));
            if (!$assertionsDisabled && !str.equals(classEntry.stringValue())) {
                throw new AssertionError();
            }
            classEntries.put(classEntry.stringValue(), classEntry);
        }
        return classEntry;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static LiteralEntry getLiteralEntry(Comparable<?> comparable) {
        Map<Object, LiteralEntry> literalEntries = Utils.getTLGlobals().getLiteralEntries();
        LiteralEntry numberEntry = literalEntries.get(comparable);
        if (numberEntry == null) {
            if (comparable instanceof String) {
                numberEntry = new StringEntry(getUtf8Entry((String) comparable));
            } else {
                numberEntry = new NumberEntry((Number) comparable);
            }
            literalEntries.put(comparable, numberEntry);
        }
        return numberEntry;
    }

    public static StringEntry getStringEntry(String str) {
        return (StringEntry) getLiteralEntry(str);
    }

    public static SignatureEntry getSignatureEntry(String str) {
        Map<String, SignatureEntry> signatureEntries = Utils.getTLGlobals().getSignatureEntries();
        SignatureEntry signatureEntry = signatureEntries.get(str);
        if (signatureEntry == null) {
            signatureEntry = new SignatureEntry(str);
            if (!$assertionsDisabled && !signatureEntry.stringValue().equals(str)) {
                throw new AssertionError();
            }
            signatureEntries.put(str, signatureEntry);
        }
        return signatureEntry;
    }

    public static SignatureEntry getSignatureEntry(Utf8Entry utf8Entry, ClassEntry[] classEntryArr) {
        return getSignatureEntry(SignatureEntry.stringValueOf(utf8Entry, classEntryArr));
    }

    public static DescriptorEntry getDescriptorEntry(Utf8Entry utf8Entry, SignatureEntry signatureEntry) {
        Map<String, DescriptorEntry> descriptorEntries = Utils.getTLGlobals().getDescriptorEntries();
        String strStringValueOf = DescriptorEntry.stringValueOf(utf8Entry, signatureEntry);
        DescriptorEntry descriptorEntry = descriptorEntries.get(strStringValueOf);
        if (descriptorEntry == null) {
            descriptorEntry = new DescriptorEntry(utf8Entry, signatureEntry);
            if (!$assertionsDisabled && !descriptorEntry.stringValue().equals(strStringValueOf)) {
                throw new AssertionError((Object) (descriptorEntry.stringValue() + " != " + strStringValueOf));
            }
            descriptorEntries.put(strStringValueOf, descriptorEntry);
        }
        return descriptorEntry;
    }

    public static DescriptorEntry getDescriptorEntry(Utf8Entry utf8Entry, Utf8Entry utf8Entry2) {
        return getDescriptorEntry(utf8Entry, getSignatureEntry(utf8Entry2.stringValue()));
    }

    public static MemberEntry getMemberEntry(byte b2, ClassEntry classEntry, DescriptorEntry descriptorEntry) {
        Map<String, MemberEntry> memberEntries = Utils.getTLGlobals().getMemberEntries();
        String strStringValueOf = MemberEntry.stringValueOf(b2, classEntry, descriptorEntry);
        MemberEntry memberEntry = memberEntries.get(strStringValueOf);
        if (memberEntry == null) {
            memberEntry = new MemberEntry(b2, classEntry, descriptorEntry);
            if (!$assertionsDisabled && !memberEntry.stringValue().equals(strStringValueOf)) {
                throw new AssertionError((Object) (memberEntry.stringValue() + " != " + strStringValueOf));
            }
            memberEntries.put(strStringValueOf, memberEntry);
        }
        return memberEntry;
    }

    public static MethodHandleEntry getMethodHandleEntry(byte b2, MemberEntry memberEntry) {
        Map<String, MethodHandleEntry> methodHandleEntries = Utils.getTLGlobals().getMethodHandleEntries();
        String strStringValueOf = MethodHandleEntry.stringValueOf(b2, memberEntry);
        MethodHandleEntry methodHandleEntry = methodHandleEntries.get(strStringValueOf);
        if (methodHandleEntry == null) {
            methodHandleEntry = new MethodHandleEntry(b2, memberEntry);
            if (!$assertionsDisabled && !methodHandleEntry.stringValue().equals(strStringValueOf)) {
                throw new AssertionError();
            }
            methodHandleEntries.put(strStringValueOf, methodHandleEntry);
        }
        return methodHandleEntry;
    }

    public static MethodTypeEntry getMethodTypeEntry(SignatureEntry signatureEntry) {
        Map<String, MethodTypeEntry> methodTypeEntries = Utils.getTLGlobals().getMethodTypeEntries();
        String strStringValue = signatureEntry.stringValue();
        MethodTypeEntry methodTypeEntry = methodTypeEntries.get(strStringValue);
        if (methodTypeEntry == null) {
            methodTypeEntry = new MethodTypeEntry(signatureEntry);
            if (!$assertionsDisabled && !methodTypeEntry.stringValue().equals(strStringValue)) {
                throw new AssertionError();
            }
            methodTypeEntries.put(strStringValue, methodTypeEntry);
        }
        return methodTypeEntry;
    }

    public static MethodTypeEntry getMethodTypeEntry(Utf8Entry utf8Entry) {
        return getMethodTypeEntry(getSignatureEntry(utf8Entry.stringValue()));
    }

    public static InvokeDynamicEntry getInvokeDynamicEntry(BootstrapMethodEntry bootstrapMethodEntry, DescriptorEntry descriptorEntry) {
        Map<String, InvokeDynamicEntry> invokeDynamicEntries = Utils.getTLGlobals().getInvokeDynamicEntries();
        String strStringValueOf = InvokeDynamicEntry.stringValueOf(bootstrapMethodEntry, descriptorEntry);
        InvokeDynamicEntry invokeDynamicEntry = invokeDynamicEntries.get(strStringValueOf);
        if (invokeDynamicEntry == null) {
            invokeDynamicEntry = new InvokeDynamicEntry(bootstrapMethodEntry, descriptorEntry);
            if (!$assertionsDisabled && !invokeDynamicEntry.stringValue().equals(strStringValueOf)) {
                throw new AssertionError();
            }
            invokeDynamicEntries.put(strStringValueOf, invokeDynamicEntry);
        }
        return invokeDynamicEntry;
    }

    public static BootstrapMethodEntry getBootstrapMethodEntry(MethodHandleEntry methodHandleEntry, Entry[] entryArr) {
        Map<String, BootstrapMethodEntry> bootstrapMethodEntries = Utils.getTLGlobals().getBootstrapMethodEntries();
        String strStringValueOf = BootstrapMethodEntry.stringValueOf(methodHandleEntry, entryArr);
        BootstrapMethodEntry bootstrapMethodEntry = bootstrapMethodEntries.get(strStringValueOf);
        if (bootstrapMethodEntry == null) {
            bootstrapMethodEntry = new BootstrapMethodEntry(methodHandleEntry, entryArr);
            if (!$assertionsDisabled && !bootstrapMethodEntry.stringValue().equals(strStringValueOf)) {
                throw new AssertionError();
            }
            bootstrapMethodEntries.put(strStringValueOf, bootstrapMethodEntry);
        }
        return bootstrapMethodEntry;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$Entry.class */
    public static abstract class Entry implements Comparable<Object> {
        protected final byte tag;
        protected int valueHash;
        static final /* synthetic */ boolean $assertionsDisabled;

        public abstract boolean equals(Object obj);

        protected abstract int computeValueHash();

        @Override // java.lang.Comparable
        public abstract int compareTo(Object obj);

        public abstract String stringValue();

        static {
            $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        }

        protected Entry(byte b2) {
            this.tag = b2;
        }

        public final byte getTag() {
            return this.tag;
        }

        public final boolean tagEquals(int i2) {
            return getTag() == i2;
        }

        public Entry getRef(int i2) {
            return null;
        }

        public boolean eq(Entry entry) {
            if ($assertionsDisabled || entry != null) {
                return this == entry || equals(entry);
            }
            throw new AssertionError();
        }

        public final int hashCode() {
            if (this.valueHash == 0) {
                this.valueHash = computeValueHash();
                if (this.valueHash == 0) {
                    this.valueHash = 1;
                }
            }
            return this.valueHash;
        }

        protected int superCompareTo(Object obj) {
            Entry entry = (Entry) obj;
            if (this.tag != entry.tag) {
                return ConstantPool.TAG_ORDER[this.tag] - ConstantPool.TAG_ORDER[entry.tag];
            }
            return 0;
        }

        public final boolean isDoubleWord() {
            return this.tag == 6 || this.tag == 5;
        }

        public final boolean tagMatches(int i2) {
            byte[] bArr;
            if (this.tag == i2) {
                return true;
            }
            switch (i2) {
                case 13:
                    return this.tag == 1;
                case 50:
                    return true;
                case 51:
                    bArr = ConstantPool.LOADABLE_VALUE_TAGS;
                    break;
                case 52:
                    bArr = ConstantPool.ANY_MEMBER_TAGS;
                    break;
                case 53:
                    bArr = ConstantPool.FIELD_SPECIFIC_TAGS;
                    break;
                default:
                    return false;
            }
            for (byte b2 : bArr) {
                if (b2 == this.tag) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            String strStringValue = stringValue();
            if (ConstantPool.verbose() > 4) {
                if (this.valueHash != 0) {
                    strStringValue = strStringValue + " hash=" + this.valueHash;
                }
                strStringValue = strStringValue + " id=" + System.identityHashCode(this);
            }
            return ConstantPool.tagName(this.tag) + "=" + strStringValue;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$Utf8Entry.class */
    public static class Utf8Entry extends Entry {
        final String value;

        Utf8Entry(String str) {
            super((byte) 1);
            this.value = str.intern();
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            return this.value.hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == Utf8Entry.class && ((Utf8Entry) obj).value.equals(this.value);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                iSuperCompareTo = this.value.compareTo(((Utf8Entry) obj).value);
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return this.value;
        }
    }

    static boolean isMemberTag(byte b2) {
        switch (b2) {
            case 9:
            case 10:
            case 11:
                return true;
            default:
                return false;
        }
    }

    static byte numberTagOf(Number number) {
        if (number instanceof Integer) {
            return (byte) 3;
        }
        if (number instanceof Float) {
            return (byte) 4;
        }
        if (number instanceof Long) {
            return (byte) 5;
        }
        if (number instanceof Double) {
            return (byte) 6;
        }
        throw new RuntimeException("bad literal value " + ((Object) number));
    }

    static boolean isRefKind(byte b2) {
        return 1 <= b2 && b2 <= 9;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$LiteralEntry.class */
    public static abstract class LiteralEntry extends Entry {
        public abstract Comparable<?> literalValue();

        protected LiteralEntry(byte b2) {
            super(b2);
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$NumberEntry.class */
    public static class NumberEntry extends LiteralEntry {
        final Number value;

        NumberEntry(Number number) {
            super(ConstantPool.numberTagOf(number));
            this.value = number;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            return this.value.hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == NumberEntry.class && ((NumberEntry) obj).value.equals(this.value);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                iSuperCompareTo = ((Comparable) this.value).compareTo(((NumberEntry) obj).value);
            }
            return iSuperCompareTo;
        }

        public Number numberValue() {
            return this.value;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.LiteralEntry
        public Comparable<?> literalValue() {
            return (Comparable) this.value;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return this.value.toString();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$StringEntry.class */
    public static class StringEntry extends LiteralEntry {
        final Utf8Entry ref;

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.ref;
            }
            return null;
        }

        StringEntry(Entry entry) {
            super((byte) 8);
            this.ref = (Utf8Entry) entry;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            return this.ref.hashCode() + this.tag;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == StringEntry.class && ((StringEntry) obj).ref.eq(this.ref);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                iSuperCompareTo = this.ref.compareTo(((StringEntry) obj).ref);
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.LiteralEntry
        public Comparable<?> literalValue() {
            return this.ref.stringValue();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return this.ref.stringValue();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$ClassEntry.class */
    public static class ClassEntry extends Entry {
        final Utf8Entry ref;

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.ref;
            }
            return null;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            return this.ref.hashCode() + this.tag;
        }

        ClassEntry(Entry entry) {
            super((byte) 7);
            this.ref = (Utf8Entry) entry;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == ClassEntry.class && ((ClassEntry) obj).ref.eq(this.ref);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                iSuperCompareTo = this.ref.compareTo(((ClassEntry) obj).ref);
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return this.ref.stringValue();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$DescriptorEntry.class */
    public static class DescriptorEntry extends Entry {
        final Utf8Entry nameRef;
        final SignatureEntry typeRef;

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.nameRef;
            }
            if (i2 == 1) {
                return this.typeRef;
            }
            return null;
        }

        DescriptorEntry(Entry entry, Entry entry2) {
            super((byte) 12);
            entry2 = entry2 instanceof Utf8Entry ? ConstantPool.getSignatureEntry(entry2.stringValue()) : entry2;
            this.nameRef = (Utf8Entry) entry;
            this.typeRef = (SignatureEntry) entry2;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            int iHashCode = this.typeRef.hashCode();
            return (this.nameRef.hashCode() + (iHashCode << 8)) ^ iHashCode;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != DescriptorEntry.class) {
                return false;
            }
            DescriptorEntry descriptorEntry = (DescriptorEntry) obj;
            return this.nameRef.eq(descriptorEntry.nameRef) && this.typeRef.eq(descriptorEntry.typeRef);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                DescriptorEntry descriptorEntry = (DescriptorEntry) obj;
                iSuperCompareTo = this.typeRef.compareTo(descriptorEntry.typeRef);
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.nameRef.compareTo(descriptorEntry.nameRef);
                }
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return stringValueOf(this.nameRef, this.typeRef);
        }

        static String stringValueOf(Entry entry, Entry entry2) {
            return ConstantPool.qualifiedStringValue(entry2, entry);
        }

        public String prettyString() {
            return this.nameRef.stringValue() + this.typeRef.prettyString();
        }

        public boolean isMethod() {
            return this.typeRef.isMethod();
        }

        public byte getLiteralTag() {
            return this.typeRef.getLiteralTag();
        }
    }

    static String qualifiedStringValue(Entry entry, Entry entry2) {
        return qualifiedStringValue(entry.stringValue(), entry2.stringValue());
    }

    static String qualifiedStringValue(String str, String str2) {
        if ($assertionsDisabled || str.indexOf(".") < 0) {
            return str + "." + str2;
        }
        throw new AssertionError();
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$MemberEntry.class */
    public static class MemberEntry extends Entry {
        final ClassEntry classRef;
        final DescriptorEntry descRef;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.classRef;
            }
            if (i2 == 1) {
                return this.descRef;
            }
            return null;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            int iHashCode = this.descRef.hashCode();
            return (this.classRef.hashCode() + (iHashCode << 8)) ^ iHashCode;
        }

        MemberEntry(byte b2, ClassEntry classEntry, DescriptorEntry descriptorEntry) {
            super(b2);
            if (!$assertionsDisabled && !ConstantPool.isMemberTag(b2)) {
                throw new AssertionError();
            }
            this.classRef = classEntry;
            this.descRef = descriptorEntry;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != MemberEntry.class) {
                return false;
            }
            MemberEntry memberEntry = (MemberEntry) obj;
            return this.classRef.eq(memberEntry.classRef) && this.descRef.eq(memberEntry.descRef);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                MemberEntry memberEntry = (MemberEntry) obj;
                if (Utils.SORT_MEMBERS_DESCR_MAJOR) {
                    iSuperCompareTo = this.descRef.compareTo(memberEntry.descRef);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.classRef.compareTo(memberEntry.classRef);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.descRef.compareTo(memberEntry.descRef);
                }
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return stringValueOf(this.tag, this.classRef, this.descRef);
        }

        static String stringValueOf(byte b2, ClassEntry classEntry, DescriptorEntry descriptorEntry) {
            String str;
            if (!$assertionsDisabled && !ConstantPool.isMemberTag(b2)) {
                throw new AssertionError();
            }
            switch (b2) {
                case 9:
                    str = "Field:";
                    break;
                case 10:
                    str = "Method:";
                    break;
                case 11:
                    str = "IMethod:";
                    break;
                default:
                    str = ((int) b2) + "???";
                    break;
            }
            return str + ConstantPool.qualifiedStringValue(classEntry, descriptorEntry);
        }

        public boolean isMethod() {
            return this.descRef.isMethod();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$SignatureEntry.class */
    public static class SignatureEntry extends Entry {
        final Utf8Entry formRef;
        final ClassEntry[] classRefs;
        String value;
        Utf8Entry asUtf8Entry;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.formRef;
            }
            if (i2 - 1 < this.classRefs.length) {
                return this.classRefs[i2 - 1];
            }
            return null;
        }

        SignatureEntry(String str) {
            super((byte) 13);
            String strIntern = str.intern();
            this.value = strIntern;
            String[] strArrStructureSignature = ConstantPool.structureSignature(strIntern);
            this.formRef = ConstantPool.getUtf8Entry(strArrStructureSignature[0]);
            this.classRefs = new ClassEntry[strArrStructureSignature.length - 1];
            for (int i2 = 1; i2 < strArrStructureSignature.length; i2++) {
                this.classRefs[i2 - 1] = ConstantPool.getClassEntry(strArrStructureSignature[i2]);
            }
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            stringValue();
            return this.value.hashCode() + this.tag;
        }

        public Utf8Entry asUtf8Entry() {
            if (this.asUtf8Entry == null) {
                this.asUtf8Entry = ConstantPool.getUtf8Entry(stringValue());
            }
            return this.asUtf8Entry;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            return obj != null && obj.getClass() == SignatureEntry.class && ((SignatureEntry) obj).value.equals(this.value);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                iSuperCompareTo = ConstantPool.compareSignatures(this.value, ((SignatureEntry) obj).value);
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            if (this.value == null) {
                this.value = stringValueOf(this.formRef, this.classRefs);
            }
            return this.value;
        }

        static String stringValueOf(Utf8Entry utf8Entry, ClassEntry[] classEntryArr) {
            String[] strArr = new String[1 + classEntryArr.length];
            strArr[0] = utf8Entry.stringValue();
            for (int i2 = 1; i2 < strArr.length; i2++) {
                strArr[i2] = classEntryArr[i2 - 1].stringValue();
            }
            return ConstantPool.flattenSignature(strArr).intern();
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public int computeSize(boolean z2) {
            String strStringValue = this.formRef.stringValue();
            int i2 = 0;
            int iIndexOf = 1;
            if (isMethod()) {
                i2 = 1;
                iIndexOf = strStringValue.indexOf(41);
            }
            int i3 = 0;
            int i4 = i2;
            while (i4 < iIndexOf) {
                switch (strStringValue.charAt(i4)) {
                    case ';':
                        i4++;
                    case 'D':
                    case 'J':
                        if (z2) {
                            i3++;
                        }
                        i3++;
                        i4++;
                    case '[':
                        while (strStringValue.charAt(i4) == '[') {
                            i4++;
                        }
                        i3++;
                        i4++;
                    default:
                        if (!$assertionsDisabled && 0 > Constants.JAVA_SIGNATURE_CHARS.indexOf(strStringValue.charAt(i4))) {
                            throw new AssertionError();
                        }
                        i3++;
                        i4++;
                        break;
                }
            }
            return i3;
        }

        public boolean isMethod() {
            return this.formRef.stringValue().charAt(0) == '(';
        }

        public byte getLiteralTag() {
            switch (this.formRef.stringValue().charAt(0)) {
                case 'B':
                case 'C':
                case 'S':
                case 'Z':
                    return (byte) 3;
                case 'D':
                    return (byte) 6;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    if ($assertionsDisabled) {
                        return (byte) 0;
                    }
                    throw new AssertionError();
                case 'F':
                    return (byte) 4;
                case 'I':
                    return (byte) 3;
                case 'J':
                    return (byte) 5;
                case 'L':
                    return (byte) 8;
            }
        }

        public String prettyString() {
            String strSubstring;
            if (isMethod()) {
                String strStringValue = this.formRef.stringValue();
                strSubstring = strStringValue.substring(0, 1 + strStringValue.indexOf(41));
            } else {
                strSubstring = "/" + this.formRef.stringValue();
            }
            while (true) {
                String str = strSubstring;
                int iIndexOf = str.indexOf(59);
                if (iIndexOf >= 0) {
                    strSubstring = str.substring(0, iIndexOf) + str.substring(iIndexOf + 1);
                } else {
                    return str;
                }
            }
        }
    }

    static int compareSignatures(String str, String str2) {
        return compareSignatures(str, str2, null, null);
    }

    static int compareSignatures(String str, String str2, String[] strArr, String[] strArr2) {
        int iCompareTo;
        char cCharAt = str.charAt(0);
        char cCharAt2 = str2.charAt(0);
        if (cCharAt != '(' && cCharAt2 == '(') {
            return -1;
        }
        if (cCharAt2 != '(' && cCharAt == '(') {
            return 1;
        }
        if (strArr == null) {
            strArr = structureSignature(str);
        }
        if (strArr2 == null) {
            strArr2 = structureSignature(str2);
        }
        if (strArr.length != strArr2.length) {
            return strArr.length - strArr2.length;
        }
        int length = strArr.length;
        do {
            length--;
            if (length >= 0) {
                iCompareTo = strArr[length].compareTo(strArr2[length]);
            } else {
                if ($assertionsDisabled || str.equals(str2)) {
                    return 0;
                }
                throw new AssertionError();
            }
        } while (iCompareTo == 0);
        return iCompareTo;
    }

    static int countClassParts(Utf8Entry utf8Entry) {
        int i2 = 0;
        String strStringValue = utf8Entry.stringValue();
        for (int i3 = 0; i3 < strStringValue.length(); i3++) {
            if (strStringValue.charAt(i3) == 'L') {
                i2++;
            }
        }
        return i2;
    }

    static String flattenSignature(String[] strArr) {
        String str = strArr[0];
        if (strArr.length == 1) {
            return str;
        }
        int length = str.length();
        for (int i2 = 1; i2 < strArr.length; i2++) {
            length += strArr[i2].length();
        }
        char[] cArr = new char[length];
        int length2 = 0;
        int i3 = 1;
        for (int i4 = 0; i4 < str.length(); i4++) {
            char cCharAt = str.charAt(i4);
            int i5 = length2;
            length2++;
            cArr[i5] = cCharAt;
            if (cCharAt == 'L') {
                int i6 = i3;
                i3++;
                String str2 = strArr[i6];
                str2.getChars(0, str2.length(), cArr, length2);
                length2 += str2.length();
            }
        }
        if (!$assertionsDisabled && length2 != length) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || i3 == strArr.length) {
            return new String(cArr);
        }
        throw new AssertionError();
    }

    private static int skipTo(char c2, String str, int i2) {
        int iIndexOf = str.indexOf(c2, i2);
        return iIndexOf >= 0 ? iIndexOf : str.length();
    }

    static String[] structureSignature(String str) {
        int iIndexOf = str.indexOf(76);
        if (iIndexOf < 0) {
            return new String[]{str};
        }
        char[] cArr = null;
        String[] strArr = null;
        int i2 = 0;
        while (true) {
            if (i2 > 1) {
                break;
            }
            int i3 = 0;
            int i4 = 1;
            int iSkipTo = 0;
            int iSkipTo2 = 0;
            int i5 = 0;
            int iIndexOf2 = iIndexOf;
            while (true) {
                int i6 = iIndexOf2 + 1;
                if (i6 <= 0) {
                    break;
                }
                if (iSkipTo < i6) {
                    iSkipTo = skipTo(';', str, i6);
                }
                if (iSkipTo2 < i6) {
                    iSkipTo2 = skipTo('<', str, i6);
                }
                int i7 = iSkipTo < iSkipTo2 ? iSkipTo : iSkipTo2;
                if (i2 != 0) {
                    str.getChars(i5, i6, cArr, i3);
                    strArr[i4] = str.substring(i6, i7);
                }
                i3 += i6 - i5;
                i4++;
                i5 = i7;
                iIndexOf2 = str.indexOf(76, i7);
            }
            if (i2 != 0) {
                str.getChars(i5, str.length(), cArr, i3);
                break;
            }
            cArr = new char[i3 + (str.length() - i5)];
            strArr = new String[i4];
            i2++;
        }
        strArr[0] = new String(cArr);
        return strArr;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$MethodHandleEntry.class */
    public static class MethodHandleEntry extends Entry {
        final int refKind;
        final MemberEntry memRef;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.memRef;
            }
            return null;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            int i2 = this.refKind;
            return (this.memRef.hashCode() + (i2 << 8)) ^ i2;
        }

        MethodHandleEntry(byte b2, MemberEntry memberEntry) {
            super((byte) 15);
            if (!$assertionsDisabled && !ConstantPool.isRefKind(b2)) {
                throw new AssertionError();
            }
            this.refKind = b2;
            this.memRef = memberEntry;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != MethodHandleEntry.class) {
                return false;
            }
            MethodHandleEntry methodHandleEntry = (MethodHandleEntry) obj;
            return this.refKind == methodHandleEntry.refKind && this.memRef.eq(methodHandleEntry.memRef);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                MethodHandleEntry methodHandleEntry = (MethodHandleEntry) obj;
                if (Utils.SORT_HANDLES_KIND_MAJOR) {
                    iSuperCompareTo = this.refKind - methodHandleEntry.refKind;
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.memRef.compareTo(methodHandleEntry.memRef);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.refKind - methodHandleEntry.refKind;
                }
            }
            return iSuperCompareTo;
        }

        public static String stringValueOf(int i2, MemberEntry memberEntry) {
            return ConstantPool.refKindName(i2) + CallSiteDescriptor.TOKEN_DELIMITER + memberEntry.stringValue();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return stringValueOf(this.refKind, this.memRef);
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$MethodTypeEntry.class */
    public static class MethodTypeEntry extends Entry {
        final SignatureEntry typeRef;

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.typeRef;
            }
            return null;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            return this.typeRef.hashCode() + this.tag;
        }

        MethodTypeEntry(SignatureEntry signatureEntry) {
            super((byte) 16);
            this.typeRef = signatureEntry;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != MethodTypeEntry.class) {
                return false;
            }
            return this.typeRef.eq(((MethodTypeEntry) obj).typeRef);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                iSuperCompareTo = this.typeRef.compareTo(((MethodTypeEntry) obj).typeRef);
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return this.typeRef.stringValue();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$InvokeDynamicEntry.class */
    public static class InvokeDynamicEntry extends Entry {
        final BootstrapMethodEntry bssRef;
        final DescriptorEntry descRef;

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.bssRef;
            }
            if (i2 == 1) {
                return this.descRef;
            }
            return null;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            int iHashCode = this.descRef.hashCode();
            return (this.bssRef.hashCode() + (iHashCode << 8)) ^ iHashCode;
        }

        InvokeDynamicEntry(BootstrapMethodEntry bootstrapMethodEntry, DescriptorEntry descriptorEntry) {
            super((byte) 18);
            this.bssRef = bootstrapMethodEntry;
            this.descRef = descriptorEntry;
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != InvokeDynamicEntry.class) {
                return false;
            }
            InvokeDynamicEntry invokeDynamicEntry = (InvokeDynamicEntry) obj;
            return this.bssRef.eq(invokeDynamicEntry.bssRef) && this.descRef.eq(invokeDynamicEntry.descRef);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                InvokeDynamicEntry invokeDynamicEntry = (InvokeDynamicEntry) obj;
                if (Utils.SORT_INDY_BSS_MAJOR) {
                    iSuperCompareTo = this.bssRef.compareTo(invokeDynamicEntry.bssRef);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.descRef.compareTo(invokeDynamicEntry.descRef);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.bssRef.compareTo(invokeDynamicEntry.bssRef);
                }
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return stringValueOf(this.bssRef, this.descRef);
        }

        static String stringValueOf(BootstrapMethodEntry bootstrapMethodEntry, DescriptorEntry descriptorEntry) {
            return "Indy:" + bootstrapMethodEntry.stringValue() + "." + descriptorEntry.stringValue();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$BootstrapMethodEntry.class */
    public static class BootstrapMethodEntry extends Entry {
        final MethodHandleEntry bsmRef;
        final Entry[] argRefs;

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public Entry getRef(int i2) {
            if (i2 == 0) {
                return this.bsmRef;
            }
            if (i2 - 1 < this.argRefs.length) {
                return this.argRefs[i2 - 1];
            }
            return null;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            int iHashCode = this.bsmRef.hashCode();
            return (Arrays.hashCode(this.argRefs) + (iHashCode << 8)) ^ iHashCode;
        }

        BootstrapMethodEntry(MethodHandleEntry methodHandleEntry, Entry[] entryArr) {
            super((byte) 17);
            this.bsmRef = methodHandleEntry;
            this.argRefs = (Entry[]) entryArr.clone();
            hashCode();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != BootstrapMethodEntry.class) {
                return false;
            }
            BootstrapMethodEntry bootstrapMethodEntry = (BootstrapMethodEntry) obj;
            return this.bsmRef.eq(bootstrapMethodEntry.bsmRef) && Arrays.equals(this.argRefs, bootstrapMethodEntry.argRefs);
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            int iSuperCompareTo = superCompareTo(obj);
            if (iSuperCompareTo == 0) {
                BootstrapMethodEntry bootstrapMethodEntry = (BootstrapMethodEntry) obj;
                if (Utils.SORT_BSS_BSM_MAJOR) {
                    iSuperCompareTo = this.bsmRef.compareTo(bootstrapMethodEntry.bsmRef);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = compareArgArrays(this.argRefs, bootstrapMethodEntry.argRefs);
                }
                if (iSuperCompareTo == 0) {
                    iSuperCompareTo = this.bsmRef.compareTo(bootstrapMethodEntry.bsmRef);
                }
            }
            return iSuperCompareTo;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            return stringValueOf(this.bsmRef, this.argRefs);
        }

        static String stringValueOf(MethodHandleEntry methodHandleEntry, Entry[] entryArr) {
            StringBuffer stringBuffer = new StringBuffer(methodHandleEntry.stringValue());
            char c2 = '<';
            for (Entry entry : entryArr) {
                stringBuffer.append(c2).append(entry.stringValue());
                c2 = ';';
            }
            if (c2 == '<') {
                stringBuffer.append(c2);
            }
            stringBuffer.append('>');
            return stringBuffer.toString();
        }

        static int compareArgArrays(Entry[] entryArr, Entry[] entryArr2) {
            int length = entryArr.length - entryArr2.length;
            if (length != 0) {
                return length;
            }
            for (int i2 = 0; i2 < entryArr.length; i2++) {
                length = entryArr[i2].compareTo(entryArr2[i2]);
                if (length != 0) {
                    break;
                }
            }
            return length;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$Index.class */
    public static final class Index extends AbstractList<Entry> {
        protected String debugName;
        protected Entry[] cpMap;
        protected boolean flattenSigs;
        protected Entry[] indexKey;
        protected int[] indexValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        }

        protected Entry[] getMap() {
            return this.cpMap;
        }

        protected Index(String str) {
            this.debugName = str;
        }

        protected Index(String str, Entry[] entryArr) {
            this(str);
            setMap(entryArr);
        }

        protected void setMap(Entry[] entryArr) {
            clearIndex();
            this.cpMap = entryArr;
        }

        protected Index(String str, Collection<Entry> collection) {
            this(str);
            setMap(collection);
        }

        protected void setMap(Collection<Entry> collection) {
            this.cpMap = new Entry[collection.size()];
            collection.toArray(this.cpMap);
            setMap(this.cpMap);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.cpMap.length;
        }

        @Override // java.util.AbstractList, java.util.List
        public Entry get(int i2) {
            return this.cpMap[i2];
        }

        public Entry getEntry(int i2) {
            return this.cpMap[i2];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int findIndexOf(Entry entry) {
            if (this.indexKey == null) {
                initializeIndex();
            }
            int iFindIndexLocation = findIndexLocation(entry);
            if (this.indexKey[iFindIndexLocation] != entry) {
                if (this.flattenSigs && entry.tag == 13) {
                    return findIndexOf(((SignatureEntry) entry).asUtf8Entry());
                }
                return -1;
            }
            int i2 = this.indexValue[iFindIndexLocation];
            if ($assertionsDisabled || entry.equals(this.cpMap[i2])) {
                return i2;
            }
            throw new AssertionError();
        }

        public boolean contains(Entry entry) {
            return findIndexOf(entry) >= 0;
        }

        public int indexOf(Entry entry) {
            int iFindIndexOf = findIndexOf(entry);
            if (iFindIndexOf < 0 && ConstantPool.verbose() > 0) {
                System.out.println("not found: " + ((Object) entry));
                System.out.println("       in: " + dumpString());
                Thread.dumpStack();
            }
            if ($assertionsDisabled || iFindIndexOf >= 0) {
                return iFindIndexOf;
            }
            throw new AssertionError();
        }

        public int lastIndexOf(Entry entry) {
            return indexOf(entry);
        }

        public boolean assertIsSorted() {
            for (int i2 = 1; i2 < this.cpMap.length; i2++) {
                if (this.cpMap[i2 - 1].compareTo(this.cpMap[i2]) > 0) {
                    System.out.println("Not sorted at " + (i2 - 1) + "/" + i2 + ": " + dumpString());
                    return false;
                }
            }
            return true;
        }

        protected void clearIndex() {
            this.indexKey = null;
            this.indexValue = null;
        }

        private int findIndexLocation(Entry entry) {
            int length = this.indexKey.length;
            int iHashCode = entry.hashCode();
            int i2 = iHashCode & (length - 1);
            int i3 = ((iHashCode >>> 8) | 1) & (length - 1);
            while (true) {
                Entry entry2 = this.indexKey[i2];
                if (entry2 == entry || entry2 == null) {
                    break;
                }
                i2 += i3;
                if (i2 >= length) {
                    i2 -= length;
                }
            }
            return i2;
        }

        private void initializeIndex() {
            int i2;
            if (ConstantPool.verbose() > 2) {
                System.out.println("initialize Index " + this.debugName + " [" + size() + "]");
            }
            int i3 = 1;
            while (true) {
                i2 = i3;
                if (i2 >= ((int) ((this.cpMap.length + 10) * 1.5d))) {
                    break;
                } else {
                    i3 = i2 << 1;
                }
            }
            this.indexKey = new Entry[i2];
            this.indexValue = new int[i2];
            for (int i4 = 0; i4 < this.cpMap.length; i4++) {
                Entry entry = this.cpMap[i4];
                if (entry != null) {
                    int iFindIndexLocation = findIndexLocation(entry);
                    if (!$assertionsDisabled && this.indexKey[iFindIndexLocation] != null) {
                        throw new AssertionError();
                    }
                    this.indexKey[iFindIndexLocation] = entry;
                    this.indexValue[iFindIndexLocation] = i4;
                }
            }
        }

        public Entry[] toArray(Entry[] entryArr) {
            int size = size();
            if (entryArr.length < size) {
                return (Entry[]) super.toArray((Object[]) entryArr);
            }
            System.arraycopy(this.cpMap, 0, entryArr, 0, size);
            if (entryArr.length > size) {
                entryArr[size] = null;
            }
            return entryArr;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Entry[] toArray() {
            return toArray(new Entry[size()]);
        }

        public Object clone() {
            return new Index(this.debugName, (Entry[]) this.cpMap.clone());
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return "Index " + this.debugName + " [" + size() + "]";
        }

        public String dumpString() {
            String str = toString() + " {\n";
            for (int i2 = 0; i2 < this.cpMap.length; i2++) {
                str = str + "    " + i2 + ": " + ((Object) this.cpMap[i2]) + "\n";
            }
            return str + "}";
        }
    }

    public static Index makeIndex(String str, Entry[] entryArr) {
        return new Index(str, entryArr);
    }

    public static Index makeIndex(String str, Collection<Entry> collection) {
        return new Index(str, collection);
    }

    public static void sort(Index index) {
        index.clearIndex();
        Arrays.sort(index.cpMap);
        if (verbose() > 2) {
            System.out.println("sorted " + index.dumpString());
        }
    }

    public static Index[] partition(Index index, int[] iArr) {
        ArrayList arrayList = new ArrayList();
        Entry[] entryArr = index.cpMap;
        if (!$assertionsDisabled && iArr.length != entryArr.length) {
            throw new AssertionError();
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            if (i3 >= 0) {
                while (i3 >= arrayList.size()) {
                    arrayList.add(null);
                }
                List list = (List) arrayList.get(i3);
                if (list == null) {
                    ArrayList arrayList2 = new ArrayList();
                    list = arrayList2;
                    arrayList.set(i3, arrayList2);
                }
                list.add(entryArr[i2]);
            }
        }
        Index[] indexArr = new Index[arrayList.size()];
        for (int i4 = 0; i4 < indexArr.length; i4++) {
            List list2 = (List) arrayList.get(i4);
            if (list2 != null) {
                indexArr[i4] = new Index(index.debugName + "/part#" + i4, list2);
                if (!$assertionsDisabled && indexArr[i4].indexOf((Entry) list2.get(0)) != 0) {
                    throw new AssertionError();
                }
            }
        }
        return indexArr;
    }

    public static Index[] partitionByTag(Index index) {
        Entry[] entryArr = index.cpMap;
        int[] iArr = new int[entryArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            Entry entry = entryArr[i2];
            iArr[i2] = entry == null ? (byte) -1 : entry.tag;
        }
        Index[] indexArrPartition = partition(index, iArr);
        for (int i3 = 0; i3 < indexArrPartition.length; i3++) {
            if (indexArrPartition[i3] != null) {
                indexArrPartition[i3].debugName = tagName(i3);
            }
        }
        if (indexArrPartition.length < 19) {
            Index[] indexArr = new Index[19];
            System.arraycopy(indexArrPartition, 0, indexArr, 0, indexArrPartition.length);
            indexArrPartition = indexArr;
        }
        return indexArrPartition;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ConstantPool$IndexGroup.class */
    public static class IndexGroup {
        private Index[] indexByTag = new Index[19];
        private Index[] indexByTagGroup;
        private int[] untypedFirstIndexByTag;
        private int totalSizeQQ;
        private Index[][] indexByTagAndClass;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ConstantPool.class.desiredAssertionStatus();
        }

        private Index makeTagGroupIndex(byte b2, byte[] bArr) {
            int length;
            if (this.indexByTagGroup == null) {
                this.indexByTagGroup = new Index[4];
            }
            int i2 = b2 - 50;
            if (!$assertionsDisabled && this.indexByTagGroup[i2] != null) {
                throw new AssertionError();
            }
            int i3 = 0;
            Entry[] entryArr = null;
            for (int i4 = 1; i4 <= 2; i4++) {
                untypedIndexOf(null);
                for (byte b3 : bArr) {
                    Index index = this.indexByTag[b3];
                    if (index != null && (length = index.cpMap.length) != 0) {
                        if (!$assertionsDisabled) {
                            if (b2 == 50) {
                                if (i3 != this.untypedFirstIndexByTag[b3]) {
                                    throw new AssertionError();
                                }
                            } else if (i3 >= this.untypedFirstIndexByTag[b3]) {
                                throw new AssertionError();
                            }
                        }
                        if (entryArr != null) {
                            if (!$assertionsDisabled && entryArr[i3] != null) {
                                throw new AssertionError();
                            }
                            if (!$assertionsDisabled && entryArr[(i3 + length) - 1] != null) {
                                throw new AssertionError();
                            }
                            System.arraycopy(index.cpMap, 0, entryArr, i3, length);
                        }
                        i3 += length;
                    }
                }
                if (entryArr == null) {
                    if (!$assertionsDisabled && i4 != 1) {
                        throw new AssertionError();
                    }
                    entryArr = new Entry[i3];
                    i3 = 0;
                }
            }
            this.indexByTagGroup[i2] = new Index(ConstantPool.tagName(b2), entryArr);
            return this.indexByTagGroup[i2];
        }

        public int untypedIndexOf(Entry entry) {
            byte b2;
            Index index;
            if (this.untypedFirstIndexByTag == null) {
                this.untypedFirstIndexByTag = new int[20];
                int i2 = 0;
                for (int i3 = 0; i3 < ConstantPool.TAGS_IN_ORDER.length; i3++) {
                    byte b3 = ConstantPool.TAGS_IN_ORDER[i3];
                    Index index2 = this.indexByTag[b3];
                    if (index2 != null) {
                        int length = index2.cpMap.length;
                        this.untypedFirstIndexByTag[b3] = i2;
                        i2 += length;
                    }
                }
                this.untypedFirstIndexByTag[19] = i2;
            }
            if (entry == null || (index = this.indexByTag[(b2 = entry.tag)]) == null) {
                return -1;
            }
            int iFindIndexOf = index.findIndexOf(entry);
            if (iFindIndexOf >= 0) {
                iFindIndexOf += this.untypedFirstIndexByTag[b2];
            }
            return iFindIndexOf;
        }

        public void initIndexByTag(byte b2, Index index) {
            if (!$assertionsDisabled && this.indexByTag[b2] != null) {
                throw new AssertionError();
            }
            Entry[] entryArr = index.cpMap;
            for (Entry entry : entryArr) {
                if (!$assertionsDisabled && entry.tag != b2) {
                    throw new AssertionError();
                }
            }
            if (b2 == 1 && !$assertionsDisabled && entryArr.length != 0 && !entryArr[0].stringValue().equals("")) {
                throw new AssertionError();
            }
            this.indexByTag[b2] = index;
            this.untypedFirstIndexByTag = null;
            this.indexByTagGroup = null;
            if (this.indexByTagAndClass != null) {
                this.indexByTagAndClass[b2] = null;
            }
        }

        public Index getIndexByTag(byte b2) {
            if (b2 >= 50) {
                return getIndexByTagGroup(b2);
            }
            Index index = this.indexByTag[b2];
            if (index == null) {
                index = new Index(ConstantPool.tagName(b2), new Entry[0]);
                this.indexByTag[b2] = index;
            }
            return index;
        }

        private Index getIndexByTagGroup(byte b2) {
            Index index;
            if (this.indexByTagGroup != null && (index = this.indexByTagGroup[b2 - 50]) != null) {
                return index;
            }
            switch (b2) {
                case 50:
                    return makeTagGroupIndex((byte) 50, ConstantPool.TAGS_IN_ORDER);
                case 51:
                    return makeTagGroupIndex((byte) 51, ConstantPool.LOADABLE_VALUE_TAGS);
                case 52:
                    return makeTagGroupIndex((byte) 52, ConstantPool.ANY_MEMBER_TAGS);
                case 53:
                    return null;
                default:
                    throw new AssertionError((Object) ("bad tag group " + ((int) b2)));
            }
        }

        /* JADX WARN: Type inference failed for: r1v19, types: [com.sun.java.util.jar.pack.ConstantPool$Index[], com.sun.java.util.jar.pack.ConstantPool$Index[][]] */
        public Index getMemberIndex(byte b2, ClassEntry classEntry) {
            if (classEntry == null) {
                throw new RuntimeException("missing class reference for " + ConstantPool.tagName(b2));
            }
            if (this.indexByTagAndClass == null) {
                this.indexByTagAndClass = new Index[19];
            }
            Index indexByTag = getIndexByTag((byte) 7);
            Index[] indexArrPartition = this.indexByTagAndClass[b2];
            if (indexArrPartition == null) {
                Index indexByTag2 = getIndexByTag(b2);
                int[] iArr = new int[indexByTag2.size()];
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    iArr[i2] = indexByTag.indexOf((Entry) ((MemberEntry) indexByTag2.get(i2)).classRef);
                }
                indexArrPartition = ConstantPool.partition(indexByTag2, iArr);
                for (int i3 = 0; i3 < indexArrPartition.length; i3++) {
                    if (!$assertionsDisabled && indexArrPartition[i3] != null && !indexArrPartition[i3].assertIsSorted()) {
                        throw new AssertionError();
                    }
                }
                this.indexByTagAndClass[b2] = indexArrPartition;
            }
            return indexArrPartition[indexByTag.indexOf((Entry) classEntry)];
        }

        public int getOverloadingIndex(MemberEntry memberEntry) {
            Index memberIndex = getMemberIndex(memberEntry.tag, memberEntry.classRef);
            Utf8Entry utf8Entry = memberEntry.descRef.nameRef;
            int i2 = 0;
            for (int i3 = 0; i3 < memberIndex.cpMap.length; i3++) {
                MemberEntry memberEntry2 = (MemberEntry) memberIndex.cpMap[i3];
                if (memberEntry2.equals(memberEntry)) {
                    return i2;
                }
                if (memberEntry2.descRef.nameRef.equals(utf8Entry)) {
                    i2++;
                }
            }
            throw new RuntimeException("should not reach here");
        }

        public MemberEntry getOverloadingForIndex(byte b2, ClassEntry classEntry, String str, int i2) {
            if (!$assertionsDisabled && !str.equals(str.intern())) {
                throw new AssertionError();
            }
            Index memberIndex = getMemberIndex(b2, classEntry);
            int i3 = 0;
            for (int i4 = 0; i4 < memberIndex.cpMap.length; i4++) {
                MemberEntry memberEntry = (MemberEntry) memberIndex.cpMap[i4];
                if (memberEntry.descRef.nameRef.stringValue().equals(str)) {
                    if (i3 == i2) {
                        return memberEntry;
                    }
                    i3++;
                }
            }
            throw new RuntimeException("should not reach here");
        }

        public boolean haveNumbers() {
            for (byte b2 : ConstantPool.NUMBER_TAGS) {
                if (getIndexByTag(b2).size() > 0) {
                    return true;
                }
            }
            return false;
        }

        public boolean haveExtraTags() {
            for (byte b2 : ConstantPool.EXTRA_TAGS) {
                if (getIndexByTag(b2).size() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void completeReferencesIn(Set<Entry> set, boolean z2) {
        completeReferencesIn(set, z2, null);
    }

    public static void completeReferencesIn(Set<Entry> set, boolean z2, List<BootstrapMethodEntry> list) {
        set.remove(null);
        ListIterator listIterator = new ArrayList(set).listIterator(set.size());
        while (listIterator.hasPrevious()) {
            Entry entry = (Entry) listIterator.previous();
            listIterator.remove();
            if (!$assertionsDisabled && entry == null) {
                throw new AssertionError();
            }
            if (z2 && entry.tag == 13) {
                SignatureEntry signatureEntry = (SignatureEntry) entry;
                Utf8Entry utf8EntryAsUtf8Entry = signatureEntry.asUtf8Entry();
                set.remove(signatureEntry);
                set.add(utf8EntryAsUtf8Entry);
                entry = utf8EntryAsUtf8Entry;
            }
            if (list != null && entry.tag == 17) {
                BootstrapMethodEntry bootstrapMethodEntry = (BootstrapMethodEntry) entry;
                set.remove(bootstrapMethodEntry);
                if (!list.contains(bootstrapMethodEntry)) {
                    list.add(bootstrapMethodEntry);
                }
            }
            int i2 = 0;
            while (true) {
                Entry ref = entry.getRef(i2);
                if (ref == null) {
                    break;
                }
                if (set.add(ref)) {
                    listIterator.add(ref);
                }
                i2++;
            }
        }
    }

    static double percent(int i2, int i3) {
        return ((int) (((10000.0d * i2) / i3) + 0.5d)) / 100.0d;
    }

    public static String tagName(int i2) {
        switch (i2) {
            case 0:
                return "**None";
            case 1:
                return "Utf8";
            case 2:
            case 14:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            default:
                return "tag#" + i2;
            case 3:
                return "Integer";
            case 4:
                return "Float";
            case 5:
                return "Long";
            case 6:
                return "Double";
            case 7:
                return "Class";
            case 8:
                return "String";
            case 9:
                return "Fieldref";
            case 10:
                return "Methodref";
            case 11:
                return "InterfaceMethodref";
            case 12:
                return "NameandType";
            case 13:
                return "*Signature";
            case 15:
                return "MethodHandle";
            case 16:
                return "MethodType";
            case 17:
                return "*BootstrapMethod";
            case 18:
                return "InvokeDynamic";
            case 50:
                return "**All";
            case 51:
                return "**LoadableValue";
            case 52:
                return "**AnyMember";
            case 53:
                return "*FieldSpecific";
        }
    }

    public static String refKindName(int i2) {
        switch (i2) {
            case 1:
                return "getField";
            case 2:
                return "getStatic";
            case 3:
                return "putField";
            case 4:
                return "putStatic";
            case 5:
                return "invokeVirtual";
            case 6:
                return "invokeStatic";
            case 7:
                return "invokeSpecial";
            case 8:
                return "newInvokeSpecial";
            case 9:
                return "invokeInterface";
            default:
                return "refKind#" + i2;
        }
    }

    private static boolean verifyTagOrder(byte[] bArr) {
        byte b2 = -1;
        for (byte b3 : bArr) {
            byte b4 = TAG_ORDER[b3];
            if (!$assertionsDisabled && b4 <= 0) {
                throw new AssertionError((Object) ("tag not found: " + ((int) b3)));
            }
            if (!$assertionsDisabled && TAGS_IN_ORDER[b4 - 1] != b3) {
                throw new AssertionError((Object) ("tag repeated: " + ((int) b3) + " => " + ((int) b4) + " => " + ((int) TAGS_IN_ORDER[b4 - 1])));
            }
            if (!$assertionsDisabled && b2 >= b4) {
                throw new AssertionError((Object) ("tags not in order: " + Arrays.toString(bArr) + " at " + ((int) b3)));
            }
            b2 = b4;
        }
        return true;
    }
}
