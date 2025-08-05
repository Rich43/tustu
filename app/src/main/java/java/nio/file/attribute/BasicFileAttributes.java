package java.nio.file.attribute;

/* loaded from: rt.jar:java/nio/file/attribute/BasicFileAttributes.class */
public interface BasicFileAttributes {
    FileTime lastModifiedTime();

    FileTime lastAccessTime();

    FileTime creationTime();

    boolean isRegularFile();

    boolean isDirectory();

    boolean isSymbolicLink();

    boolean isOther();

    long size();

    Object fileKey();
}
