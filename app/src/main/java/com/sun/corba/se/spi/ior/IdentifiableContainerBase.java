package com.sun.corba.se.spi.ior;

import com.sun.corba.se.impl.ior.FreezableList;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IdentifiableContainerBase.class */
public class IdentifiableContainerBase extends FreezableList {
    public IdentifiableContainerBase() {
        super(new ArrayList());
    }

    public Iterator iteratorById(final int i2) {
        return new Iterator() { // from class: com.sun.corba.se.spi.ior.IdentifiableContainerBase.1
            Iterator iter;
            Object current = advance();

            {
                this.iter = IdentifiableContainerBase.this.iterator();
            }

            private Object advance() {
                while (this.iter.hasNext()) {
                    Identifiable identifiable = (Identifiable) this.iter.next();
                    if (identifiable.getId() == i2) {
                        return identifiable;
                    }
                }
                return null;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.current != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                Object obj = this.current;
                this.current = advance();
                return obj;
            }

            @Override // java.util.Iterator
            public void remove() {
                this.iter.remove();
            }
        };
    }
}
