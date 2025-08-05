package javax.tools;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;

/* loaded from: rt.jar:javax/tools/JavaFileObject.class */
public interface JavaFileObject extends FileObject {
    Kind getKind();

    boolean isNameCompatible(String str, Kind kind);

    NestingKind getNestingKind();

    Modifier getAccessLevel();

    /* loaded from: rt.jar:javax/tools/JavaFileObject$Kind.class */
    public enum Kind {
        SOURCE(".java"),
        CLASS(".class"),
        HTML(".html"),
        OTHER("");

        public final String extension;

        Kind(String str) {
            str.getClass();
            this.extension = str;
        }
    }
}
