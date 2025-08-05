package org.omg.IOP;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.IOP.CodecPackage.FormatMismatch;
import org.omg.IOP.CodecPackage.InvalidTypeForEncoding;
import org.omg.IOP.CodecPackage.TypeMismatch;

/* loaded from: rt.jar:org/omg/IOP/CodecOperations.class */
public interface CodecOperations {
    byte[] encode(Any any) throws InvalidTypeForEncoding;

    Any decode(byte[] bArr) throws FormatMismatch;

    byte[] encode_value(Any any) throws InvalidTypeForEncoding;

    Any decode_value(byte[] bArr, TypeCode typeCode) throws FormatMismatch, TypeMismatch;
}
