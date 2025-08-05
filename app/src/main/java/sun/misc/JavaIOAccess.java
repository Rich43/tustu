package sun.misc;

import java.io.Console;
import java.nio.charset.Charset;

/* loaded from: rt.jar:sun/misc/JavaIOAccess.class */
public interface JavaIOAccess {
    Console console();

    Charset charset();
}
