package ay;

import bH.G;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ay.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/k.class */
public class C0934k {
    public static String a(String str) {
        return a(new URL(str));
    }

    public static String a(URL url) {
        InputStream inputStreamOpenStream = null;
        try {
            try {
                inputStreamOpenStream = url.openStream();
                String next = new Scanner(inputStreamOpenStream).useDelimiter("\\A").next();
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e2) {
                        Logger.getLogger(G.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return next;
            } catch (Throwable th) {
                if (inputStreamOpenStream != null) {
                    try {
                        inputStreamOpenStream.close();
                    } catch (IOException e3) {
                        Logger.getLogger(G.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                        throw th;
                    }
                }
                throw th;
            }
        } catch (IOException e4) {
            throw e4;
        }
    }
}
