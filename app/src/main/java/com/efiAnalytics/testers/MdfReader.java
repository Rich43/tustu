package com.efiAnalytics.testers;

import V.a;
import al.AbstractC0570d;
import am.C0575c;
import am.C0576d;
import am.C0577e;
import am.h;
import am.i;
import am.k;
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
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/testers/MdfReader.class */
public class MdfReader {
    public static void main(String[] strArr) throws a {
        String strE;
        long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            Path path = Paths.get("C:\\Users\\p_tob\\Dropbox\\TunerStudioProjects\\support\\MLV\\MF4\\MNOA_2024-03-19_Dyno_UpperInjectorCalWork.mf4", new String[0]);
            SeekableByteChannel seekableByteChannelNewByteChannel = Files.newByteChannel(path, StandardOpenOption.READ);
            if (!a(seekableByteChannelNewByteChannel).startsWith("4")) {
                throw new a("Not a MDF 4 file");
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
                C0575c c0575cF = c0577e.f();
                int i4 = 1 + 1;
                C.c("      CGBlock 1 :" + ((Object) c0575cF));
                C.c("      Read CGBlock in " + (System.currentTimeMillis() - jCurrentTimeMillis3) + "ms");
                StringBuilder sb = new StringBuilder();
                int i5 = 0;
                for (C0576d c0576d : AbstractC0570d.a(c0575cF)) {
                    sb.append(c0576d.o().e());
                    if (i5 > 0) {
                        sb.append(", ");
                    }
                    if ((c0576d.q() instanceof k) && (strE = ((k) c0576d.q()).e()) != null && !strE.isEmpty()) {
                        sb.append("(").append(strE).append(")");
                    }
                    i5++;
                }
                C.c("      Read CGBlock in " + (System.currentTimeMillis() - jCurrentTimeMillis3) + "ms");
                C.c(GoToActionDialog.EMPTY_DESTINATION + i5 + " Channel Names: " + sb.toString());
            }
            C.c("  Read all DGBlocks in " + (System.currentTimeMillis() - jCurrentTimeMillis3) + "ms");
            System.currentTimeMillis();
        } catch (IOException e2) {
            Logger.getLogger(MdfReader.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
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
}
