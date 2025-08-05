package com.sun.nio.zipfs;

import java.io.IOException;
import java.nio.file.ClosedDirectoryStreamException;
import java.nio.file.DirectoryStream;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipDirectoryStream.class */
public class ZipDirectoryStream implements DirectoryStream<Path> {
    private final ZipFileSystem zipfs;
    private final byte[] path;
    private final DirectoryStream.Filter<? super Path> filter;
    private volatile boolean isClosed;
    private volatile Iterator<Path> itr;

    ZipDirectoryStream(ZipPath zipPath, DirectoryStream.Filter<? super Path> filter) throws IOException {
        this.zipfs = zipPath.getFileSystem();
        this.path = zipPath.getResolvedPath();
        this.filter = filter;
        if (!this.zipfs.isDirectory(this.path)) {
            throw new NotDirectoryException(zipPath.toString());
        }
    }

    @Override // java.nio.file.DirectoryStream, java.lang.Iterable, java.util.List
    public synchronized Iterator<Path> iterator() {
        if (this.isClosed) {
            throw new ClosedDirectoryStreamException();
        }
        if (this.itr != null) {
            throw new IllegalStateException("Iterator has already been returned");
        }
        try {
            this.itr = this.zipfs.iteratorOf(this.path, this.filter);
            return new Iterator<Path>() { // from class: com.sun.nio.zipfs.ZipDirectoryStream.1
                private Path next;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    if (!ZipDirectoryStream.this.isClosed) {
                        return ZipDirectoryStream.this.itr.hasNext();
                    }
                    return false;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public synchronized Path next() {
                    if (!ZipDirectoryStream.this.isClosed) {
                        return (Path) ZipDirectoryStream.this.itr.next();
                    }
                    throw new NoSuchElementException();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } catch (IOException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.isClosed = true;
    }
}
