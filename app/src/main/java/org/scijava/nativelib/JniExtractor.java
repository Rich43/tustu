package org.scijava.nativelib;

import java.io.File;
import java.io.IOException;

/* loaded from: jssc.jar:org/scijava/nativelib/JniExtractor.class */
public interface JniExtractor {
    File extractJni(String str, String str2) throws IOException;

    void extractRegistered() throws IOException;
}
