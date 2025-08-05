package java.nio.file.attribute;

import java.util.Set;

/* loaded from: rt.jar:java/nio/file/attribute/PosixFileAttributes.class */
public interface PosixFileAttributes extends BasicFileAttributes {
    UserPrincipal owner();

    GroupPrincipal group();

    Set<PosixFilePermission> permissions();
}
