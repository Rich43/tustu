package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/ApplyCaseFoldArg.class */
public final class ApplyCaseFoldArg {
    final ScanEnvironment env;
    final CClassNode cc;
    ConsAltNode altRoot;
    ConsAltNode tail;

    public ApplyCaseFoldArg(ScanEnvironment env, CClassNode cc) {
        this.env = env;
        this.cc = cc;
    }
}
