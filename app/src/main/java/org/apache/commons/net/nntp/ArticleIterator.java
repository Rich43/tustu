package org.apache.commons.net.nntp;

import java.util.Iterator;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/ArticleIterator.class */
class ArticleIterator implements Iterator<Article>, Iterable<Article> {
    private final Iterator<String> stringIterator;

    public ArticleIterator(Iterable<String> iterableString) {
        this.stringIterator = iterableString.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.stringIterator.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Article next() {
        String line = this.stringIterator.next();
        return NNTPClient.__parseArticleEntry(line);
    }

    @Override // java.util.Iterator
    public void remove() {
        this.stringIterator.remove();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Article> iterator() {
        return this;
    }
}
