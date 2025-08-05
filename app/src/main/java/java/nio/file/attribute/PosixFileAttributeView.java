package java.nio.file.attribute;

import java.io.IOException;
import java.util.Set;

/* loaded from: rt.jar:java/nio/file/attribute/PosixFileAttributeView.class */
public interface PosixFileAttributeView extends BasicFileAttributeView, FileOwnerAttributeView {
    @Override // java.nio.file.attribute.BasicFileAttributeView, java.nio.file.attribute.AttributeView
    String name();

    @Override // java.nio.file.attribute.BasicFileAttributeView
    PosixFileAttributes readAttributes() throws IOException;

    void setPermissions(Set<PosixFilePermission> set) throws IOException;

    void setGroup(GroupPrincipal groupPrincipal) throws IOException;
}
