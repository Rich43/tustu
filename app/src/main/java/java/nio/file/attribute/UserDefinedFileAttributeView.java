package java.nio.file.attribute;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/* loaded from: rt.jar:java/nio/file/attribute/UserDefinedFileAttributeView.class */
public interface UserDefinedFileAttributeView extends FileAttributeView {
    @Override // java.nio.file.attribute.AttributeView
    String name();

    List<String> list() throws IOException;

    int size(String str) throws IOException;

    int read(String str, ByteBuffer byteBuffer) throws IOException;

    int write(String str, ByteBuffer byteBuffer) throws IOException;

    void delete(String str) throws IOException;
}
