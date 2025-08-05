package bG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:bG/F.class */
public class F {
    public void a(File file, List list) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        try {
            bufferedWriter.append((CharSequence) "teeth=");
            for (int i2 = 0; i2 < list.size(); i2++) {
                k kVar = (k) list.get(i2);
                bufferedWriter.append((CharSequence) Double.toString(kVar.a())).append((CharSequence) CallSiteDescriptor.TOKEN_DELIMITER).append((CharSequence) Double.toString(kVar.b()));
                if (i2 < list.size() - 1) {
                    bufferedWriter.append((CharSequence) ",");
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    public List a(File file) throws IOException {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        do {
            try {
                try {
                    line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                } catch (Exception e2) {
                    throw new IOException("Invalid Wheel File Format.");
                }
            } finally {
                bufferedReader.close();
            }
        } while (!line.startsWith("teeth="));
        if (line == null) {
            throw new IOException("Invalid Wheel File.");
        }
        String strSubstring = line.trim().substring("teeth=".length());
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(strSubstring, ",");
        while (stringTokenizer.hasMoreTokens()) {
            String[] strArrSplit = stringTokenizer.nextToken().split(CallSiteDescriptor.TOKEN_DELIMITER);
            k kVar = new k();
            kVar.a(Double.parseDouble(strArrSplit[0]));
            kVar.b(Double.parseDouble(strArrSplit[1]));
            arrayList.add(kVar);
        }
        return arrayList;
    }
}
