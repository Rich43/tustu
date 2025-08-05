package java.nio.file;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/* loaded from: rt.jar:java/nio/file/SimpleFileVisitor.class */
public class SimpleFileVisitor<T> implements FileVisitor<T> {
    protected SimpleFileVisitor() {
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult preVisitDirectory(T t2, BasicFileAttributes basicFileAttributes) throws IOException {
        Objects.requireNonNull(t2);
        Objects.requireNonNull(basicFileAttributes);
        return FileVisitResult.CONTINUE;
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult visitFile(T t2, BasicFileAttributes basicFileAttributes) throws IOException {
        Objects.requireNonNull(t2);
        Objects.requireNonNull(basicFileAttributes);
        return FileVisitResult.CONTINUE;
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult visitFileFailed(T t2, IOException iOException) throws IOException {
        Objects.requireNonNull(t2);
        throw iOException;
    }

    @Override // java.nio.file.FileVisitor
    public FileVisitResult postVisitDirectory(T t2, IOException iOException) throws IOException {
        Objects.requireNonNull(t2);
        if (iOException != null) {
            throw iOException;
        }
        return FileVisitResult.CONTINUE;
    }
}
