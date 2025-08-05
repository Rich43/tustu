package javax.naming.spi;

import javax.naming.Name;
import javax.naming.directory.DirContext;

/* compiled from: ContinuationDirContext.java */
/* loaded from: rt.jar:javax/naming/spi/DirContextNamePair.class */
class DirContextNamePair {
    DirContext ctx;
    Name name;

    DirContextNamePair(DirContext dirContext, Name name) {
        this.ctx = dirContext;
        this.name = name;
    }

    DirContext getDirContext() {
        return this.ctx;
    }

    Name getName() {
        return this.name;
    }
}
