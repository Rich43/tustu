package org.omg.IOP;

import org.omg.IOP.CodecFactoryPackage.UnknownEncoding;

/* loaded from: rt.jar:org/omg/IOP/CodecFactoryOperations.class */
public interface CodecFactoryOperations {
    Codec create_codec(Encoding encoding) throws UnknownEncoding;
}
