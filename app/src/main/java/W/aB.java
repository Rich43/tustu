package W;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:W/aB.class */
public class aB {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f2032a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    String f2033b = "";

    public ArrayList a(File file, int i2) throws V.a {
        if (file == null || !file.exists()) {
            throw new V.a("File Not Found!");
        }
        this.f2033b = "";
        ArrayList arrayList = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String strA = "";
            try {
                bufferedReader.mark(1000);
                strA = a(bufferedReader);
                if (strA == null || strA.isEmpty()) {
                    bufferedReader.reset();
                }
            } catch (IOException e2) {
                bH.C.c("Failed to get FirmwareIdentifier from file.");
            }
            try {
                this.f2033b = b(bufferedReader);
            } catch (IOException e3) {
                bH.C.c("Failed to get FirmwareIdentifier from file.");
            }
            long length = file.length();
            try {
                ArrayList arrayListC = c(bufferedReader);
                try {
                    C0188n c0188nA = a(arrayListC, bufferedReader);
                    c0188nA.c(strA);
                    c0188nA.d(this.f2033b);
                    int i3 = 0 + 1;
                    arrayList.add(c0188nA);
                    a(arrayList, length);
                    while (c0188nA != null) {
                        c0188nA = a(arrayListC, bufferedReader);
                        i3++;
                        if (i3 > i2) {
                            break;
                        }
                        c0188nA.c(strA);
                        arrayList.add(c0188nA);
                        if (!a(length - r0.available())) {
                            break;
                        }
                    }
                } catch (aD e4) {
                } catch (IOException e5) {
                    throw new V.a("Error Reading Trigger Log File\n" + file.getAbsolutePath());
                } catch (Exception e6) {
                    throw new V.a("Invalid File Format!\nError Reading Trigger Log File, could not understand header rows.\n" + file.getAbsolutePath());
                }
                a(arrayList);
                return arrayList;
            } catch (IOException e7) {
                throw new V.a("Error Reading Trigger Log File, could not read header rows.\n" + file.getAbsolutePath());
            } catch (Exception e8) {
                throw new V.a("Invalid File Format!\nError Opening Trigger Log File, could not parse header rows in:\n" + file.getAbsolutePath());
            }
        } catch (FileNotFoundException e9) {
            throw new V.a("File Not Found!\n" + file.getAbsolutePath());
        }
    }

    public void b(File file, int i2) {
        new aE(this, file, i2).start();
    }

    private void a(ArrayList arrayList, long j2) {
        Iterator it = this.f2032a.iterator();
        while (it.hasNext()) {
            try {
                ((Y) it.next()).a(arrayList, j2);
            } catch (Exception e2) {
                bH.C.a("Exception cause by a LogLoadListener, this was caught and handled but should be corrected in the implementation.");
                e2.printStackTrace();
            }
        }
    }

    private boolean a(long j2) {
        boolean z2 = true;
        Iterator it = this.f2032a.iterator();
        while (it.hasNext()) {
            try {
                if (!((Y) it.next()).a(j2)) {
                    z2 = false;
                }
            } catch (Exception e2) {
                bH.C.a("Exception cause by a LogLoadListener, this was caught and handled but should be corrected in the implementation.");
                e2.printStackTrace();
            }
        }
        return z2;
    }

    private void a(ArrayList arrayList) {
        Iterator it = this.f2032a.iterator();
        while (it.hasNext()) {
            try {
                ((Y) it.next()).a(arrayList);
            } catch (Exception e2) {
                bH.C.a("Exception cause by a LogLoadListener, this was caught and handled but should be corrected in the implementation.");
                e2.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(V.a aVar) {
        Iterator it = this.f2032a.iterator();
        while (it.hasNext()) {
            try {
                ((Y) it.next()).a(aVar);
            } catch (Exception e2) {
                bH.C.a("Exception cause by a LogLoadListener, this was caught and handled but should be corrected in the implementation.");
                e2.printStackTrace();
            }
        }
    }

    public void a(Y y2) {
        this.f2032a.add(y2);
    }

    private String a(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        if (line.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            return line.startsWith("#Firmware:") ? line.substring(10).trim() : "";
        }
        bufferedReader.reset();
        return "";
    }

    private String b(BufferedReader bufferedReader) throws IOException {
        bufferedReader.mark(2000);
        String line = bufferedReader.readLine();
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (!line.startsWith("Header:") && !line.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                bufferedReader.reset();
                return sb.toString();
            }
            if (line.startsWith("Header:")) {
                line = line.substring(8);
            }
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(line);
            bufferedReader.mark(1000);
            line = bufferedReader.readLine();
        }
    }

    private ArrayList c(BufferedReader bufferedReader) throws V.a, IOException {
        String line;
        ArrayList arrayList = new ArrayList();
        String line2 = bufferedReader.readLine();
        while (true) {
            line = line2;
            if (line.length() != 0 && !line.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX) && !line.startsWith("Header:")) {
                try {
                    break;
                } catch (Exception e2) {
                    throw new V.a("Invalid Header Row:\n" + line);
                }
            }
            if (line.startsWith("Header:")) {
                this.f2033b = bH.W.b(line, "Header:", "");
            }
            line2 = bufferedReader.readLine();
        }
        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
        while (stringTokenizer.hasMoreTokens()) {
            aC aCVar = new aC(this);
            aCVar.a(stringTokenizer.nextToken());
            arrayList.add(aCVar);
        }
        int i2 = 0;
        line = bufferedReader.readLine();
        StringTokenizer stringTokenizer2 = new StringTokenizer(line, ",");
        while (stringTokenizer2.hasMoreTokens()) {
            ((aC) arrayList.get(i2)).b(bH.W.b(stringTokenizer2.nextToken(), PdfOps.DOUBLE_QUOTE__TOKEN, ""));
            i2++;
        }
        return arrayList;
    }

    private C0188n a(ArrayList arrayList, BufferedReader bufferedReader) throws aD, IOException {
        C0188n c0188n = new C0188n();
        if (this.f2033b != null && !this.f2033b.equals("")) {
            c0188n.d(this.f2033b);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0184j c0184j = new C0184j();
            aC aCVar = (aC) it.next();
            c0184j.a(aCVar.a());
            c0184j.e(aCVar.b());
            c0188n.a(c0184j);
        }
        String line = bufferedReader.readLine();
        if (line == null) {
            throw new aD(this);
        }
        while (line != null && !line.startsWith("MARK") && line.length() > 0) {
            int i2 = 0;
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            while (stringTokenizer.hasMoreTokens()) {
                ((C0184j) c0188n.get(i2)).b(stringTokenizer.nextToken());
                i2++;
            }
            line = bufferedReader.readLine();
            if (line == null) {
                throw new aD(this);
            }
        }
        return c0188n;
    }
}
