package jssc;

import java.io.File;
import java.io.IOException;
import org.scijava.nativelib.DefaultJniExtractor;
import org.scijava.nativelib.NativeLibraryUtil;

/* loaded from: jssc.jar:jssc/DefaultJniExtractorStub.class */
public class DefaultJniExtractorStub extends DefaultJniExtractor {
    private File bootPath;

    public DefaultJniExtractorStub(Class<?> libraryJarClass) throws IOException {
        super(libraryJarClass);
    }

    public DefaultJniExtractorStub(Class<?> libraryJarClass, String bootPath) throws IOException {
        this(libraryJarClass);
        if (bootPath != null) {
            File bootTest = new File(bootPath);
            if (bootTest.exists()) {
                this.bootPath = bootTest;
            } else {
                System.err.println("WARNING " + DefaultJniExtractorStub.class.getCanonicalName() + ": Boot path " + bootPath + " not found, falling back to default extraction behavior.");
            }
        }
    }

    @Override // org.scijava.nativelib.BaseJniExtractor, org.scijava.nativelib.JniExtractor
    public File extractJni(String libPath, String libName) throws IOException {
        if (this.bootPath != null) {
            return new File(this.bootPath, NativeLibraryUtil.getPlatformLibraryName(libName));
        }
        return super.extractJni(libPath, libName);
    }

    @Override // org.scijava.nativelib.BaseJniExtractor, org.scijava.nativelib.JniExtractor
    public void extractRegistered() throws IOException {
        if (this.bootPath != null) {
            return;
        }
        super.extractRegistered();
    }
}
