package javax.naming.spi;

import javax.naming.directory.DirContext;

/* compiled from: ContinuationDirContext.java */
/* loaded from: rt.jar:javax/naming/spi/DirContextStringPair.class */
class DirContextStringPair {
    DirContext ctx;
    String str;

    DirContextStringPair(DirContext dirContext, String str) {
        this.ctx = dirContext;
        this.str = str;
    }

    DirContext getDirContext() {
        return this.ctx;
    }

    String getString() {
        return this.str;
    }
}
