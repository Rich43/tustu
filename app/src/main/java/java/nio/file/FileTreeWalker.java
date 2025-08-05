package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import sun.nio.fs.BasicFileAttributesHolder;

/* loaded from: rt.jar:java/nio/file/FileTreeWalker.class */
class FileTreeWalker implements Closeable {
    private final boolean followLinks;
    private final LinkOption[] linkOptions;
    private final int maxDepth;
    private final ArrayDeque<DirectoryNode> stack = new ArrayDeque<>();
    private boolean closed;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:java/nio/file/FileTreeWalker$EventType.class */
    enum EventType {
        START_DIRECTORY,
        END_DIRECTORY,
        ENTRY
    }

    static {
        $assertionsDisabled = !FileTreeWalker.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:java/nio/file/FileTreeWalker$DirectoryNode.class */
    private static class DirectoryNode {
        private final Path dir;
        private final Object key;
        private final DirectoryStream<Path> stream;
        private final Iterator<Path> iterator;
        private boolean skipped;

        DirectoryNode(Path path, Object obj, DirectoryStream<Path> directoryStream) {
            this.dir = path;
            this.key = obj;
            this.stream = directoryStream;
            this.iterator = directoryStream.iterator();
        }

        Path directory() {
            return this.dir;
        }

        Object key() {
            return this.key;
        }

        DirectoryStream<Path> stream() {
            return this.stream;
        }

        Iterator<Path> iterator() {
            return this.iterator;
        }

        void skip() {
            this.skipped = true;
        }

        boolean skipped() {
            return this.skipped;
        }
    }

    /* loaded from: rt.jar:java/nio/file/FileTreeWalker$Event.class */
    static class Event {
        private final EventType type;
        private final Path file;
        private final BasicFileAttributes attrs;
        private final IOException ioe;

        private Event(EventType eventType, Path path, BasicFileAttributes basicFileAttributes, IOException iOException) {
            this.type = eventType;
            this.file = path;
            this.attrs = basicFileAttributes;
            this.ioe = iOException;
        }

        Event(EventType eventType, Path path, BasicFileAttributes basicFileAttributes) {
            this(eventType, path, basicFileAttributes, null);
        }

        Event(EventType eventType, Path path, IOException iOException) {
            this(eventType, path, null, iOException);
        }

        EventType type() {
            return this.type;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Path file() {
            return this.file;
        }

        BasicFileAttributes attributes() {
            return this.attrs;
        }

        IOException ioeException() {
            return this.ioe;
        }
    }

    FileTreeWalker(Collection<FileVisitOption> collection, int i2) {
        boolean z2 = false;
        Iterator<FileVisitOption> it = collection.iterator();
        while (it.hasNext()) {
            switch (it.next()) {
                case FOLLOW_LINKS:
                    z2 = true;
                default:
                    throw new AssertionError((Object) "Should not get here");
            }
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("'maxDepth' is negative");
        }
        this.followLinks = z2;
        this.linkOptions = z2 ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        this.maxDepth = i2;
    }

    private BasicFileAttributes getAttributes(Path path, boolean z2) throws IOException {
        BasicFileAttributes attributes;
        BasicFileAttributes basicFileAttributes;
        if (z2 && (path instanceof BasicFileAttributesHolder) && System.getSecurityManager() == null && (basicFileAttributes = ((BasicFileAttributesHolder) path).get()) != null && (!this.followLinks || !basicFileAttributes.isSymbolicLink())) {
            return basicFileAttributes;
        }
        try {
            attributes = Files.readAttributes(path, (Class<BasicFileAttributes>) BasicFileAttributes.class, this.linkOptions);
        } catch (IOException e2) {
            if (!this.followLinks) {
                throw e2;
            }
            attributes = Files.readAttributes(path, (Class<BasicFileAttributes>) BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        }
        return attributes;
    }

    private boolean wouldLoop(Path path, Object obj) {
        Iterator<DirectoryNode> it = this.stack.iterator();
        while (it.hasNext()) {
            DirectoryNode next = it.next();
            Object objKey = next.key();
            if (obj != null && objKey != null) {
                if (obj.equals(objKey)) {
                    return true;
                }
            } else {
                try {
                    if (Files.isSameFile(path, next.directory())) {
                        return true;
                    }
                } catch (IOException | SecurityException e2) {
                }
            }
        }
        return false;
    }

    private Event visit(Path path, boolean z2, boolean z3) {
        try {
            BasicFileAttributes attributes = getAttributes(path, z3);
            if (this.stack.size() >= this.maxDepth || !attributes.isDirectory()) {
                return new Event(EventType.ENTRY, path, attributes);
            }
            if (this.followLinks && wouldLoop(path, attributes.fileKey())) {
                return new Event(EventType.ENTRY, path, new FileSystemLoopException(path.toString()));
            }
            try {
                this.stack.push(new DirectoryNode(path, attributes.fileKey(), Files.newDirectoryStream(path)));
                return new Event(EventType.START_DIRECTORY, path, attributes);
            } catch (IOException e2) {
                return new Event(EventType.ENTRY, path, e2);
            } catch (SecurityException e3) {
                if (z2) {
                    return null;
                }
                throw e3;
            }
        } catch (IOException e4) {
            return new Event(EventType.ENTRY, path, e4);
        } catch (SecurityException e5) {
            if (z2) {
                return null;
            }
            throw e5;
        }
    }

    Event walk(Path path) {
        if (this.closed) {
            throw new IllegalStateException("Closed");
        }
        Event eventVisit = visit(path, false, false);
        if ($assertionsDisabled || eventVisit != null) {
            return eventVisit;
        }
        throw new AssertionError();
    }

    Event next() {
        Event eventVisit;
        DirectoryNode directoryNodePeek = this.stack.peek();
        if (directoryNodePeek == null) {
            return null;
        }
        do {
            Path next = null;
            IOException cause = null;
            if (!directoryNodePeek.skipped()) {
                Iterator<Path> it = directoryNodePeek.iterator();
                try {
                    if (it.hasNext()) {
                        next = it.next();
                    }
                } catch (DirectoryIteratorException e2) {
                    cause = e2.getCause();
                }
            }
            if (next == null) {
                try {
                    directoryNodePeek.stream().close();
                } catch (IOException e3) {
                    if (cause != null) {
                        cause = e3;
                    } else {
                        cause.addSuppressed(e3);
                    }
                }
                this.stack.pop();
                return new Event(EventType.END_DIRECTORY, directoryNodePeek.directory(), cause);
            }
            eventVisit = visit(next, true, true);
        } while (eventVisit == null);
        return eventVisit;
    }

    void pop() {
        if (!this.stack.isEmpty()) {
            try {
                this.stack.pop().stream().close();
            } catch (IOException e2) {
            }
        }
    }

    void skipRemainingSiblings() {
        if (!this.stack.isEmpty()) {
            this.stack.peek().skip();
        }
    }

    boolean isOpen() {
        return !this.closed;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (!this.closed) {
            while (!this.stack.isEmpty()) {
                pop();
            }
            this.closed = true;
        }
    }
}
