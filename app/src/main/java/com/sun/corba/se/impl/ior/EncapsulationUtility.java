package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.spi.ior.Identifiable;
import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import com.sun.corba.se.spi.ior.WriteContents;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import java.util.List;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.EncapsInputStreamFactory;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/EncapsulationUtility.class */
public class EncapsulationUtility {
    private EncapsulationUtility() {
    }

    public static void readIdentifiableSequence(List list, IdentifiableFactoryFinder identifiableFactoryFinder, InputStream inputStream) {
        int i2 = inputStream.read_long();
        for (int i3 = 0; i3 < i2; i3++) {
            list.add(identifiableFactoryFinder.create(inputStream.read_long(), inputStream));
        }
    }

    public static void writeIdentifiableSequence(List list, OutputStream outputStream) {
        outputStream.write_long(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Identifiable identifiable = (Identifiable) it.next();
            outputStream.write_long(identifiable.getId());
            identifiable.write(outputStream);
        }
    }

    public static void writeOutputStream(OutputStream outputStream, OutputStream outputStream2) {
        byte[] byteArray = ((CDROutputStream) outputStream).toByteArray();
        outputStream2.write_long(byteArray.length);
        outputStream2.write_octet_array(byteArray, 0, byteArray.length);
    }

    public static InputStream getEncapsulationStream(InputStream inputStream) {
        byte[] octets = readOctets(inputStream);
        EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(inputStream.orb(), octets, octets.length);
        encapsInputStreamNewEncapsInputStream.consumeEndian();
        return encapsInputStreamNewEncapsInputStream;
    }

    public static byte[] readOctets(InputStream inputStream) {
        int i2 = inputStream.read_ulong();
        byte[] bArr = new byte[i2];
        inputStream.read_octet_array(bArr, 0, i2);
        return bArr;
    }

    public static void writeEncapsulation(WriteContents writeContents, OutputStream outputStream) {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB) outputStream.orb());
        encapsOutputStreamNewEncapsOutputStream.putEndian();
        writeContents.writeContents(encapsOutputStreamNewEncapsOutputStream);
        writeOutputStream(encapsOutputStreamNewEncapsOutputStream, outputStream);
    }
}
