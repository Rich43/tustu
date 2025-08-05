package org.icepdf.core.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.io.SeekableInput;
import org.icepdf.core.pobjects.CrossReference;
import org.icepdf.core.pobjects.ObjectStream;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.PTrailer;
import org.icepdf.core.pobjects.Reference;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/LazyObjectLoader.class */
public class LazyObjectLoader {
    private static final Logger logger = Logger.getLogger(LazyObjectLoader.class.toString());
    private Library library;
    private SeekableInput seekableInput;
    private CrossReference crossReference;
    private final Object leastRectlyUsedLock = new Object();
    private final Object streamLock = new Object();
    protected SoftLRUCache<Reference, ObjectStream> leastRecentlyUsed = new SoftLRUCache<>(256);

    public LazyObjectLoader(Library lib, SeekableInput seekableInput, CrossReference xref) {
        this.library = lib;
        this.seekableInput = seekableInput;
        this.crossReference = xref;
    }

    public Object loadObject(Reference reference) {
        Object ob;
        ObjectStream objectStream;
        Object objLoadObject;
        if (reference == null || this.library == null || this.crossReference == null) {
            return null;
        }
        int objNum = reference.getObjectNumber();
        CrossReference.Entry entry = this.crossReference.getEntryForObject(Integer.valueOf(objNum));
        if (entry == null) {
            return null;
        }
        try {
            if (entry instanceof CrossReference.UsedEntry) {
                try {
                    if (this.seekableInput == null) {
                        if (this.seekableInput == null) {
                            return null;
                        }
                        this.seekableInput.endThreadAccess();
                        return null;
                    }
                    synchronized (this.streamLock) {
                        CrossReference.UsedEntry usedEntry = (CrossReference.UsedEntry) entry;
                        long position = usedEntry.getFilePositionOfObject();
                        this.seekableInput.beginThreadAccess();
                        long savedPosition = this.seekableInput.getAbsolutePosition();
                        this.seekableInput.seekAbsolute(position);
                        Parser parser = new Parser(this.seekableInput);
                        ob = parser.getObject(this.library);
                        this.seekableInput.seekAbsolute(savedPosition);
                    }
                    if (this.seekableInput != null) {
                        this.seekableInput.endThreadAccess();
                    }
                    return ob;
                } catch (Exception e2) {
                    logger.log(Level.SEVERE, "Error loading object instance: " + reference.toString(), (Throwable) e2);
                    if (this.seekableInput == null) {
                        return null;
                    }
                    this.seekableInput.endThreadAccess();
                    return null;
                }
            }
            if (!(entry instanceof CrossReference.CompressedEntry)) {
                return null;
            }
            try {
                CrossReference.CompressedEntry compressedEntry = (CrossReference.CompressedEntry) entry;
                int objectStreamsObjectNumber = compressedEntry.getObjectNumberOfContainingObjectStream();
                int objectIndex = compressedEntry.getIndexWithinObjectStream();
                Reference objectStreamRef = new Reference(objectStreamsObjectNumber, 0);
                synchronized (this.leastRectlyUsedLock) {
                    objectStream = this.leastRecentlyUsed.get(objectStreamRef);
                }
                if (objectStream == null) {
                    synchronized (this.streamLock) {
                        objectStream = (ObjectStream) this.library.getObject(objectStreamRef);
                    }
                    if (objectStream != null) {
                        synchronized (this.leastRectlyUsedLock) {
                            this.leastRecentlyUsed.put(objectStreamRef, objectStream);
                        }
                    }
                }
                if (objectStream == null) {
                    return null;
                }
                synchronized (this.streamLock) {
                    objLoadObject = objectStream.loadObject(this.library, objectIndex);
                }
                return objLoadObject;
            } catch (Exception e3) {
                logger.log(Level.SEVERE, "Error loading object instance: " + reference.toString(), (Throwable) e3);
                return null;
            }
        } catch (Throwable th) {
            if (this.seekableInput != null) {
                this.seekableInput.endThreadAccess();
            }
            throw th;
        }
    }

    public boolean haveEntry(Reference reference) {
        if (reference == null || this.crossReference == null) {
            return false;
        }
        int objNum = reference.getObjectNumber();
        CrossReference.Entry entry = this.crossReference.getEntryForObject(Integer.valueOf(objNum));
        return entry != null;
    }

    public PTrailer loadTrailer(long position) {
        PTrailer trailer = null;
        try {
            try {
                if (this.seekableInput != null) {
                    this.seekableInput.beginThreadAccess();
                    long savedPosition = this.seekableInput.getAbsolutePosition();
                    this.seekableInput.seekAbsolute(position);
                    Parser parser = new Parser(this.seekableInput);
                    Object obj = parser.getObject(this.library);
                    if (obj instanceof PObject) {
                        obj = ((PObject) obj).getObject();
                    }
                    trailer = (PTrailer) obj;
                    if (trailer != null) {
                        trailer.setPosition(position);
                    }
                    this.seekableInput.seekAbsolute(savedPosition);
                }
                if (this.seekableInput != null) {
                    this.seekableInput.endThreadAccess();
                }
            } catch (Exception e2) {
                logger.log(Level.FINE, "Error loading PTrailer instance: " + position, (Throwable) e2);
                if (this.seekableInput != null) {
                    this.seekableInput.endThreadAccess();
                }
            }
            return trailer;
        } catch (Throwable th) {
            if (this.seekableInput != null) {
                this.seekableInput.endThreadAccess();
            }
            throw th;
        }
    }

    public Library getLibrary() {
        return this.library;
    }
}
