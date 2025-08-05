package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ApplyCaseFold.class */
final class ApplyCaseFold {
    static final ApplyCaseFold INSTANCE = new ApplyCaseFold();

    ApplyCaseFold() {
    }

    public static void apply(int from, int to, Object o2) {
        ApplyCaseFoldArg arg = (ApplyCaseFoldArg) o2;
        ScanEnvironment env = arg.env;
        CClassNode cc = arg.cc;
        BitSet bs2 = cc.f12882bs;
        boolean inCC = cc.isCodeInCC(from);
        if ((inCC && !cc.isNot()) || (!inCC && cc.isNot())) {
            if (to >= 256) {
                cc.addCodeRange(env, to, to);
            } else {
                bs2.set(to);
            }
        }
    }
}
