package java.nio.file.attribute;

import java.io.IOException;
import java.util.List;

/* loaded from: rt.jar:java/nio/file/attribute/AclFileAttributeView.class */
public interface AclFileAttributeView extends FileOwnerAttributeView {
    @Override // java.nio.file.attribute.FileOwnerAttributeView, java.nio.file.attribute.AttributeView
    String name();

    List<AclEntry> getAcl() throws IOException;

    void setAcl(List<AclEntry> list) throws IOException;
}
