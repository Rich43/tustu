package java.nio.file.attribute;

import java.io.IOException;

/* loaded from: rt.jar:java/nio/file/attribute/BasicFileAttributeView.class */
public interface BasicFileAttributeView extends FileAttributeView {
    @Override // java.nio.file.attribute.AttributeView
    String name();

    BasicFileAttributes readAttributes() throws IOException;

    void setTimes(FileTime fileTime, FileTime fileTime2, FileTime fileTime3) throws IOException;
}
