package W;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.icepdf.core.util.PdfOps;

/* renamed from: W.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/g.class */
public class C0181g {

    /* renamed from: a, reason: collision with root package name */
    private String f2130a = "efiaLangFile!";

    public Map a(File file) {
        return a(new HashMap(), file);
    }

    public Map a(Map map, File file) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(b(file), "UTF-8"));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    String strTrim = line.trim();
                    if (strTrim.length() > 3 && strTrim.indexOf("=") != -1) {
                        C0182h c0182h = new C0182h();
                        try {
                            c0182h.a(strTrim);
                            map.put(bH.W.b(c0182h.a(), "\\n", "\n"), bH.W.b(c0182h.b(), "\\n", "\n"));
                        } catch (S e2) {
                            bH.C.b("Error parsing Content row:\n:" + strTrim + "\n" + e2.getMessage());
                        } catch (Exception e3) {
                            bH.C.b("Error parsing Content row:\n:" + strTrim + "\n" + e3.getMessage());
                        }
                    } else if (strTrim.length() != 0) {
                        bH.C.d("Invalid row:" + strTrim);
                    }
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                return map;
            } catch (UnsupportedEncodingException e4) {
                Logger.getLogger(C0181g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                return null;
            }
        } catch (Throwable th) {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            throw th;
        }
    }

    private InputStream b(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(this.f2130a);
            }
            return zipFile.getInputStream((FileHeader) zipFile.getFileHeaders().get(0));
        } catch (Exception e2) {
            bH.W.b(e2.getMessage(), "zip", "fast binary");
            bH.C.c("Reading translation file as plain text");
            return new FileInputStream(file);
        }
    }

    public void b(Map map, File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        PrintWriter printWriter = new PrintWriter(file, "UTF-8");
        try {
            for (String str : map.keySet()) {
                printWriter.println(new String((PdfOps.DOUBLE_QUOTE__TOKEN + str + "\" = \"" + ((String) map.get(str)) + PdfOps.DOUBLE_QUOTE__TOKEN).getBytes("UTF-8")));
            }
        } finally {
            printWriter.flush();
            printWriter.close();
        }
    }
}
