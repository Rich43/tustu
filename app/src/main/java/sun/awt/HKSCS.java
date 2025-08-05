package sun.awt;

import java.nio.charset.Charset;
import sun.nio.cs.ext.MS950_HKSCS_XP;

/* loaded from: charsets.jar:sun/awt/HKSCS.class */
public class HKSCS extends MS950_HKSCS_XP {
    @Override // sun.nio.cs.ext.MS950_HKSCS_XP, java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof HKSCS;
    }
}
