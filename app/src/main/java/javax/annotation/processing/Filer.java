package javax.annotation.processing;

import java.io.IOException;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/* loaded from: rt.jar:javax/annotation/processing/Filer.class */
public interface Filer {
    JavaFileObject createSourceFile(CharSequence charSequence, Element... elementArr) throws IOException;

    JavaFileObject createClassFile(CharSequence charSequence, Element... elementArr) throws IOException;

    FileObject createResource(JavaFileManager.Location location, CharSequence charSequence, CharSequence charSequence2, Element... elementArr) throws IOException;

    FileObject getResource(JavaFileManager.Location location, CharSequence charSequence, CharSequence charSequence2) throws IOException;
}
