package java.nio.file.attribute;

/* loaded from: rt.jar:java/nio/file/attribute/DosFileAttributes.class */
public interface DosFileAttributes extends BasicFileAttributes {
    boolean isReadOnly();

    boolean isHidden();

    boolean isArchive();

    boolean isSystem();
}
