package org.apache.commons.net.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.io.Util;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/nntp/ReplyIterator.class */
class ReplyIterator implements Iterator<String>, Iterable<String> {
    private final BufferedReader reader;
    private String line;
    private Exception savedException;

    ReplyIterator(BufferedReader _reader, boolean addDotReader) throws IOException {
        this.reader = addDotReader ? new DotTerminatedMessageReader(_reader) : _reader;
        this.line = this.reader.readLine();
        if (this.line == null) {
            Util.closeQuietly(this.reader);
        }
    }

    ReplyIterator(BufferedReader _reader) throws IOException {
        this(_reader, true);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.savedException != null) {
            throw new NoSuchElementException(this.savedException.toString());
        }
        return this.line != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public String next() throws NoSuchElementException {
        if (this.savedException != null) {
            throw new NoSuchElementException(this.savedException.toString());
        }
        String prev = this.line;
        if (prev == null) {
            throw new NoSuchElementException();
        }
        try {
            this.line = this.reader.readLine();
            if (this.line == null) {
                Util.closeQuietly(this.reader);
            }
        } catch (IOException ex) {
            this.savedException = ex;
            Util.closeQuietly(this.reader);
        }
        return prev;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<String> iterator() {
        return this;
    }
}
