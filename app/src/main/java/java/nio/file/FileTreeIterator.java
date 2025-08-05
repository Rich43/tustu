package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileTreeWalker;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/nio/file/FileTreeIterator.class */
class FileTreeIterator implements Iterator<FileTreeWalker.Event>, Closeable {
    private final FileTreeWalker walker;
    private FileTreeWalker.Event next;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FileTreeIterator.class.desiredAssertionStatus();
    }

    FileTreeIterator(Path path, int i2, FileVisitOption... fileVisitOptionArr) throws IOException {
        this.walker = new FileTreeWalker(Arrays.asList(fileVisitOptionArr), i2);
        this.next = this.walker.walk(path);
        if (!$assertionsDisabled && this.next.type() != FileTreeWalker.EventType.ENTRY && this.next.type() != FileTreeWalker.EventType.START_DIRECTORY) {
            throw new AssertionError();
        }
        IOException iOExceptionIoeException = this.next.ioeException();
        if (iOExceptionIoeException != null) {
            throw iOExceptionIoeException;
        }
    }

    private void fetchNextIfNeeded() {
        if (this.next == null) {
            FileTreeWalker.Event next = this.walker.next();
            while (true) {
                FileTreeWalker.Event event = next;
                if (event != null) {
                    IOException iOExceptionIoeException = event.ioeException();
                    if (iOExceptionIoeException != null) {
                        throw new UncheckedIOException(iOExceptionIoeException);
                    }
                    if (event.type() != FileTreeWalker.EventType.END_DIRECTORY) {
                        this.next = event;
                        return;
                    }
                    next = this.walker.next();
                } else {
                    return;
                }
            }
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (!this.walker.isOpen()) {
            throw new IllegalStateException();
        }
        fetchNextIfNeeded();
        return this.next != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public FileTreeWalker.Event next() {
        if (!this.walker.isOpen()) {
            throw new IllegalStateException();
        }
        fetchNextIfNeeded();
        if (this.next == null) {
            throw new NoSuchElementException();
        }
        FileTreeWalker.Event event = this.next;
        this.next = null;
        return event;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.walker.close();
    }
}
