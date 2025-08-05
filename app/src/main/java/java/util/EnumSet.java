package java.util;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.Enum;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/EnumSet.class */
public abstract class EnumSet<E extends Enum<E>> extends AbstractSet<E> implements Cloneable, Serializable {
    final Class<E> elementType;
    final Enum<?>[] universe;
    private static Enum<?>[] ZERO_LENGTH_ENUM_ARRAY = new Enum[0];

    abstract void addAll();

    abstract void addRange(E e2, E e3);

    abstract void complement();

    EnumSet(Class<E> cls, Enum<?>[] enumArr) {
        this.elementType = cls;
        this.universe = enumArr;
    }

    public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> cls) {
        Enum[] universe = getUniverse(cls);
        if (universe == null) {
            throw new ClassCastException(((Object) cls) + " not an enum");
        }
        if (universe.length <= 64) {
            return new RegularEnumSet(cls, universe);
        }
        return new JumboEnumSet(cls, universe);
    }

    public static <E extends Enum<E>> EnumSet<E> allOf(Class<E> cls) {
        EnumSet<E> enumSetNoneOf = noneOf(cls);
        enumSetNoneOf.addAll();
        return enumSetNoneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> copyOf(EnumSet<E> enumSet) {
        return enumSet.mo3394clone();
    }

    public static <E extends Enum<E>> EnumSet<E> copyOf(Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return ((EnumSet) collection).mo3394clone();
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Collection is empty");
        }
        Iterator<E> it = collection.iterator();
        EnumSet<E> enumSetOf = of(it.next());
        while (it.hasNext()) {
            enumSetOf.add(it.next());
        }
        return enumSetOf;
    }

    public static <E extends Enum<E>> EnumSet<E> complementOf(EnumSet<E> enumSet) {
        EnumSet<E> enumSetCopyOf = copyOf((EnumSet) enumSet);
        enumSetCopyOf.complement();
        return enumSetCopyOf;
    }

    public static <E extends Enum<E>> EnumSet<E> of(E e2) {
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.add(e2);
        return enumSetNoneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> of(E e2, E e3) {
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.add(e2);
        enumSetNoneOf.add(e3);
        return enumSetNoneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> of(E e2, E e3, E e4) {
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.add(e2);
        enumSetNoneOf.add(e3);
        enumSetNoneOf.add(e4);
        return enumSetNoneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> of(E e2, E e3, E e4, E e5) {
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.add(e2);
        enumSetNoneOf.add(e3);
        enumSetNoneOf.add(e4);
        enumSetNoneOf.add(e5);
        return enumSetNoneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> of(E e2, E e3, E e4, E e5, E e6) {
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.add(e2);
        enumSetNoneOf.add(e3);
        enumSetNoneOf.add(e4);
        enumSetNoneOf.add(e5);
        enumSetNoneOf.add(e6);
        return enumSetNoneOf;
    }

    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> of(E e2, E... eArr) {
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.add(e2);
        for (E e3 : eArr) {
            enumSetNoneOf.add(e3);
        }
        return enumSetNoneOf;
    }

    public static <E extends Enum<E>> EnumSet<E> range(E e2, E e3) {
        if (e2.compareTo(e3) > 0) {
            throw new IllegalArgumentException(((Object) e2) + " > " + ((Object) e3));
        }
        EnumSet<E> enumSetNoneOf = noneOf(e2.getDeclaringClass());
        enumSetNoneOf.addRange(e2, e3);
        return enumSetNoneOf;
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public EnumSet<E> mo3394clone() {
        try {
            return (EnumSet) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new AssertionError(e2);
        }
    }

    final void typeCheck(E e2) {
        Class<?> cls = e2.getClass();
        if (cls != this.elementType && cls.getSuperclass() != this.elementType) {
            throw new ClassCastException(((Object) cls) + " != " + ((Object) this.elementType));
        }
    }

    private static <E extends Enum<E>> E[] getUniverse(Class<E> cls) {
        return (E[]) SharedSecrets.getJavaLangAccess().getEnumConstantsShared(cls);
    }

    /* loaded from: rt.jar:java/util/EnumSet$SerializationProxy.class */
    private static class SerializationProxy<E extends Enum<E>> implements Serializable {
        private final Class<E> elementType;
        private final Enum<?>[] elements;
        private static final long serialVersionUID = 362491234563181265L;

        SerializationProxy(EnumSet<E> enumSet) {
            this.elementType = enumSet.elementType;
            this.elements = (Enum[]) enumSet.toArray(EnumSet.ZERO_LENGTH_ENUM_ARRAY);
        }

        private Object readResolve() {
            EnumSet enumSetNoneOf = EnumSet.noneOf(this.elementType);
            for (Enum<?> r0 : this.elements) {
                enumSetNoneOf.add(r0);
            }
            return enumSetNoneOf;
        }
    }

    Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
