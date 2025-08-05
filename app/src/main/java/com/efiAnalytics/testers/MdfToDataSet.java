package com.efiAnalytics.testers;

import V.a;
import W.C0189o;
import al.AbstractC0570d;
import am.C0577e;
import am.h;
import am.i;
import bH.C;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/testers/MdfToDataSet.class */
public class MdfToDataSet {
    public static void main(String[] strArr) {
        AutoCloseable autoCloseable = null;
        long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            try {
                Path path = Paths.get("C:\\Users\\p_tob\\Dropbox\\TunerStudioProjects\\support\\MLV\\MF4\\MNOA_2024-03-19_Dyno_UpperInjectorCalWork.mf4", new String[0]);
                SeekableByteChannel seekableByteChannelNewByteChannel = Files.newByteChannel(path, StandardOpenOption.READ);
                if (!a(seekableByteChannelNewByteChannel).startsWith("4")) {
                    throw new a("Invalid file format for MDF4 reader");
                }
                i iVarA = i.a(path, seekableByteChannelNewByteChannel);
                C.c("IDBlock: " + ((Object) iVarA));
                C.c("Read MDF IDBlock in " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms");
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                h hVarJ = iVarA.j();
                C.c("HDBlock: " + ((Object) hVarJ));
                C.c("Read HDBlock " + (System.currentTimeMillis() - jCurrentTimeMillis2) + "ms");
                long jCurrentTimeMillis3 = System.currentTimeMillis();
                int i2 = 1;
                for (C0577e c0577e : AbstractC0570d.a(hVarJ)) {
                    int i3 = i2;
                    i2++;
                    C.c("  DGBlock " + i3 + " :" + ((Object) c0577e));
                    C0189o.a(AbstractC0570d.a(AbstractC0570d.a(c0577e.f())), a(path.toString()) + i2 + ".msl", "\t");
                    if (i2 > 0) {
                        break;
                    }
                }
                C.c("  Read all DGBlocks in " + (System.currentTimeMillis() - jCurrentTimeMillis3) + "ms");
                try {
                    seekableByteChannelNewByteChannel.close();
                } catch (Exception e2) {
                    Logger.getLogger(MdfToDataSet.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            } catch (IOException e3) {
                Logger.getLogger(MdfReader.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                try {
                    autoCloseable.close();
                } catch (Exception e4) {
                    Logger.getLogger(MdfToDataSet.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
        } catch (Throwable th) {
            try {
                autoCloseable.close();
            } catch (Exception e5) {
                Logger.getLogger(MdfToDataSet.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            }
            throw th;
        }
    }

    private static String a(SeekableByteChannel seekableByteChannel) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(64);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(0L);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        byte[] bArr = new byte[8];
        byteBufferAllocate.get(bArr);
        String str = new String(bArr, FTP.DEFAULT_CONTROL_ENCODING);
        if (!str.equals("MDF     ")) {
            throw new IOException("Invalid or corrupt MDF file: " + str);
        }
        byte[] bArr2 = new byte[8];
        byteBufferAllocate.get(bArr2);
        return new String(bArr2, FTP.DEFAULT_CONTROL_ENCODING);
    }

    public static String a(String str) {
        int iLastIndexOf = str.lastIndexOf(".");
        if (iLastIndexOf > 0) {
            str = str.substring(0, iLastIndexOf);
        }
        return str;
    }
}
