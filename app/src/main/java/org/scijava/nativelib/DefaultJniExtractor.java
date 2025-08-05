package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;

/* loaded from: jssc.jar:org/scijava/nativelib/DefaultJniExtractor.class */
public class DefaultJniExtractor extends BaseJniExtractor {
    private File nativeDir;

    public DefaultJniExtractor(Class<?> libraryJarClass) throws IOException {
        super(libraryJarClass);
        this.nativeDir = getTempDir();
        this.nativeDir.mkdirs();
        if (!this.nativeDir.isDirectory()) {
            throw new IOException("Unable to create native library working directory " + ((Object) this.nativeDir));
        }
        this.nativeDir.deleteOnExit();
    }

    @Override // org.scijava.nativelib.BaseJniExtractor
    public File getJniDir() {
        return this.nativeDir;
    }

    @Override // org.scijava.nativelib.BaseJniExtractor
    public File getNativeDir() {
        return this.nativeDir;
    }
}
