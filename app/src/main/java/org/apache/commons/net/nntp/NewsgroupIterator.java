package org.apache.commons.net.nntp;

import java.util.Iterator;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/NewsgroupIterator.class */
class NewsgroupIterator implements Iterator<NewsgroupInfo>, Iterable<NewsgroupInfo> {
    private final Iterator<String> stringIterator;

    public NewsgroupIterator(Iterable<String> iterableString) {
        this.stringIterator = iterableString.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.stringIterator.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public NewsgroupInfo next() {
        String line = this.stringIterator.next();
        return NNTPClient.__parseNewsgroupListEntry(line);
    }

    @Override // java.util.Iterator
    public void remove() {
        this.stringIterator.remove();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<NewsgroupInfo> iterator() {
        return this;
    }
}
