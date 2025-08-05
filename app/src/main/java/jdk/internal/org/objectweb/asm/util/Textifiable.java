package jdk.internal.org.objectweb.asm.util;

import java.util.Map;
import jdk.internal.org.objectweb.asm.Label;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/Textifiable.class */
public interface Textifiable {
    void textify(StringBuffer stringBuffer, Map<Label, String> map);
}
