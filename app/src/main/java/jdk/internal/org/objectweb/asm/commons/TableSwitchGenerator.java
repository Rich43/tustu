package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.Label;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/TableSwitchGenerator.class */
public interface TableSwitchGenerator {
    void generateCase(int i2, Label label);

    void generateDefault();
}
