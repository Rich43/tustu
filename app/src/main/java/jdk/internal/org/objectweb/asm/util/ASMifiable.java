package jdk.internal.org.objectweb.asm.util;

import java.util.Map;
import jdk.internal.org.objectweb.asm.Label;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/ASMifiable.class */
public interface ASMifiable {
    void asmify(StringBuffer stringBuffer, String str, Map<Label, String> map);
}
