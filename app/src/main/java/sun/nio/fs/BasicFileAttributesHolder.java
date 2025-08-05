package sun.nio.fs;

import java.nio.file.attribute.BasicFileAttributes;

/* loaded from: rt.jar:sun/nio/fs/BasicFileAttributesHolder.class */
public interface BasicFileAttributesHolder {
    BasicFileAttributes get();

    void invalidate();
}
