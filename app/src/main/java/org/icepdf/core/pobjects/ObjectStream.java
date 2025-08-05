package org.icepdf.core.pobjects;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.SeekableByteArrayInputStream;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.io.SeekableInputConstrainedWrapper;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Parser;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/ObjectStream.class */
public class ObjectStream extends Stream {
    private static final Logger logger = Logger.getLogger(Form.class.toString());
    public static final Name N_KEY = new Name("N");
    public static final Name FIRST_KEY = new Name("First");
    private boolean init;
    private SeekableInput decodedStream;
    private int[] objectNumbers;
    private long[] objectOffset;

    public ObjectStream(Library l2, HashMap h2, SeekableInputConstrainedWrapper streamInputWrapper) {
        super(l2, h2, streamInputWrapper);
    }

    @Override // org.icepdf.core.pobjects.Dictionary
    public synchronized void init() {
        if (this.init) {
            return;
        }
        this.init = true;
        int numObjects = this.library.getInt(this.entries, N_KEY);
        long firstObjectsOffset = this.library.getLong(this.entries, FIRST_KEY);
        this.decodedStream = new SeekableByteArrayInputStream(getDecodedStreamBytes(0));
        this.objectNumbers = new int[numObjects];
        this.objectOffset = new long[numObjects];
        try {
            Parser parser = new Parser(this.decodedStream);
            for (int i2 = 0; i2 < numObjects; i2++) {
                this.objectNumbers[i2] = parser.getIntSurroundedByWhitespace();
                this.objectOffset[i2] = parser.getLongSurroundedByWhitespace() + firstObjectsOffset;
            }
        } catch (Exception e2) {
            logger.log(Level.SEVERE, "Error loading object stream instance: ", (Throwable) e2);
        }
    }

    public Object loadObject(Library library, int objectIndex) {
        init();
        if (this.objectNumbers == null || this.objectOffset == null || this.objectNumbers.length != this.objectOffset.length || objectIndex < 0) {
            return null;
        }
        try {
            if (objectIndex >= this.objectNumbers.length) {
                return null;
            }
            try {
                int objectNumber = this.objectNumbers[objectIndex];
                long position = this.objectOffset[objectIndex];
                this.decodedStream.beginThreadAccess();
                this.decodedStream.seekAbsolute(position);
                Parser parser = new Parser(this.decodedStream, 1);
                Object ob = parser.getObject(library);
                if (ob == null) {
                    Reference ref = new Reference(objectNumber, 0);
                    PObject pObject = parser.addPObject(library, ref);
                    ob = pObject.getObject();
                } else if (!(ob instanceof PObject)) {
                    Reference ref2 = new Reference(objectNumber, 0);
                    library.addObject(ob, ref2);
                }
                if (ob != null && (ob instanceof Dictionary)) {
                    ((Dictionary) ob).setPObjectReference(new Reference(objectNumber, 0));
                }
                Object obj = ob;
                this.decodedStream.endThreadAccess();
                return obj;
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error loading PDF object.", (Throwable) e2);
                this.decodedStream.endThreadAccess();
                return null;
            }
        } catch (Throwable th) {
            this.decodedStream.endThreadAccess();
            throw th;
        }
    }
}
