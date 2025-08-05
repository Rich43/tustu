package sun.misc;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:sun/misc/CompoundEnumeration.class */
public class CompoundEnumeration<E> implements Enumeration<E> {
    private Enumeration<E>[] enums;
    private int index = 0;

    public CompoundEnumeration(Enumeration<E>[] enumerationArr) {
        this.enums = enumerationArr;
    }

    private boolean next() {
        while (this.index < this.enums.length) {
            if (this.enums[this.index] != null && this.enums[this.index].hasMoreElements()) {
                return true;
            }
            this.index++;
        }
        return false;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return next();
    }

    @Override // java.util.Enumeration
    public E nextElement() {
        if (!next()) {
            throw new NoSuchElementException();
        }
        return this.enums[this.index].nextElement();
    }
}
