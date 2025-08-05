package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerCharacter.class */
final class PrimitiveArrayListerCharacter<BeanT> extends Lister<BeanT, char[], Character, CharacterArrayPack> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ void endPacking(CharacterArrayPack characterArrayPack, Object obj, Accessor accessor) throws AccessorException {
        endPacking2(characterArrayPack, (CharacterArrayPack) obj, (Accessor<CharacterArrayPack, char[]>) accessor);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public /* bridge */ /* synthetic */ CharacterArrayPack startPacking(Object obj, Accessor accessor) throws AccessorException {
        return startPacking((PrimitiveArrayListerCharacter<BeanT>) obj, (Accessor<PrimitiveArrayListerCharacter<BeanT>, char[]>) accessor);
    }

    private PrimitiveArrayListerCharacter() {
    }

    static void register() {
        Lister.primitiveArrayListers.put(Character.TYPE, new PrimitiveArrayListerCharacter());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public ListIterator<Character> iterator(final char[] objects, XMLSerializer context) {
        return new ListIterator<Character>() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.PrimitiveArrayListerCharacter.1
            int idx = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public boolean hasNext() {
                return this.idx < objects.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator
            public Character next() {
                char[] cArr = objects;
                int i2 = this.idx;
                this.idx = i2 + 1;
                return Character.valueOf(cArr[i2]);
            }
        };
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public CharacterArrayPack startPacking(BeanT current, Accessor<BeanT, char[]> acc) {
        return new CharacterArrayPack();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void addToPack(CharacterArrayPack objects, Character o2) {
        objects.add(o2);
    }

    /* renamed from: endPacking, reason: avoid collision after fix types in other method */
    public void endPacking2(CharacterArrayPack pack, BeanT bean, Accessor<BeanT, char[]> acc) throws AccessorException {
        acc.set(bean, pack.build());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Lister
    public void reset(BeanT o2, Accessor<BeanT, char[]> acc) throws AccessorException {
        acc.set(o2, new char[0]);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/PrimitiveArrayListerCharacter$CharacterArrayPack.class */
    static final class CharacterArrayPack {
        char[] buf = new char[16];
        int size;

        CharacterArrayPack() {
        }

        void add(Character b2) {
            if (this.buf.length == this.size) {
                char[] nb = new char[this.buf.length * 2];
                System.arraycopy(this.buf, 0, nb, 0, this.buf.length);
                this.buf = nb;
            }
            if (b2 != null) {
                char[] cArr = this.buf;
                int i2 = this.size;
                this.size = i2 + 1;
                cArr[i2] = b2.charValue();
            }
        }

        char[] build() {
            if (this.buf.length == this.size) {
                return this.buf;
            }
            char[] r2 = new char[this.size];
            System.arraycopy(this.buf, 0, r2, 0, this.size);
            return r2;
        }
    }
}
