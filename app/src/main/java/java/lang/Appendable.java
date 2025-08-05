package java.lang;

import java.io.IOException;

/* loaded from: rt.jar:java/lang/Appendable.class */
public interface Appendable {
    Appendable append(CharSequence charSequence) throws IOException;

    Appendable append(CharSequence charSequence, int i2, int i3) throws IOException;

    Appendable append(char c2) throws IOException;
}
