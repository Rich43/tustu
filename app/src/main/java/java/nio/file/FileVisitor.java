package java.nio.file;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: rt.jar:java/nio/file/FileVisitor.class */
public interface FileVisitor<T> {
    FileVisitResult preVisitDirectory(T t2, BasicFileAttributes basicFileAttributes) throws IOException;

    FileVisitResult visitFile(T t2, BasicFileAttributes basicFileAttributes) throws IOException;

    FileVisitResult visitFileFailed(T t2, IOException iOException) throws IOException;

    FileVisitResult postVisitDirectory(T t2, IOException iOException) throws IOException;
}
