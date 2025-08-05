package java.nio.file.attribute;

import java.io.IOException;

/* loaded from: rt.jar:java/nio/file/attribute/DosFileAttributeView.class */
public interface DosFileAttributeView extends BasicFileAttributeView {
    @Override // java.nio.file.attribute.BasicFileAttributeView, java.nio.file.attribute.AttributeView
    String name();

    @Override // java.nio.file.attribute.BasicFileAttributeView
    DosFileAttributes readAttributes() throws IOException;

    void setReadOnly(boolean z2) throws IOException;

    void setHidden(boolean z2) throws IOException;

    void setSystem(boolean z2) throws IOException;

    void setArchive(boolean z2) throws IOException;
}
