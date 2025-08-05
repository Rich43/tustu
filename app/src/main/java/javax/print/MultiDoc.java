package javax.print;

import java.io.IOException;

/* loaded from: rt.jar:javax/print/MultiDoc.class */
public interface MultiDoc {
    Doc getDoc() throws IOException;

    MultiDoc next() throws IOException;
}
