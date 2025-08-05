package javax.imageio.spi;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.imageio.spi.ServiceRegistry;

/* compiled from: ServiceRegistry.java */
/* loaded from: rt.jar:javax/imageio/spi/FilterIterator.class */
class FilterIterator<T> implements Iterator<T> {
    private Iterator<T> iter;
    private ServiceRegistry.Filter filter;
    private T next = null;

    public FilterIterator(Iterator<T> it, ServiceRegistry.Filter filter) {
        this.iter = it;
        this.filter = filter;
        advance();
    }

    private void advance() {
        while (this.iter.hasNext()) {
            T next = this.iter.next();
            if (this.filter.filter(next)) {
                this.next = next;
                return;
            }
        }
        this.next = null;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.next != null;
    }

    @Override // java.util.Iterator
    public T next() {
        if (this.next == null) {
            throw new NoSuchElementException();
        }
        T t2 = this.next;
        advance();
        return t2;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
