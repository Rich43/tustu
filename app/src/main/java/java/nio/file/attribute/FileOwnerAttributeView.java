package java.nio.file.attribute;

import java.io.IOException;

/* loaded from: rt.jar:java/nio/file/attribute/FileOwnerAttributeView.class */
public interface FileOwnerAttributeView extends FileAttributeView {
    @Override // java.nio.file.attribute.AttributeView
    String name();

    UserPrincipal getOwner() throws IOException;

    void setOwner(UserPrincipal userPrincipal) throws IOException;
}
