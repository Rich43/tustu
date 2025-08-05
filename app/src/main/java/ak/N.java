package ak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/* loaded from: TunerStudioMS.jar:ak/N.class */
public class N extends C0546f {
    public N() {
        super(",", false);
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1983y;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        int i2 = 0;
        BufferedReader bufferedReader = null;
        try {
            this.f4824h = new File(str);
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.f4824h)));
            String line = bufferedReader2.readLine();
            if (line == null) {
                int i3 = 0 - 1;
                try {
                    bufferedReader2.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return i3;
            }
            while (i2 < 1000 && !line.startsWith("[Channel Information]")) {
                i2++;
                line = bufferedReader2.readLine();
            }
            int i4 = i2 + 2;
            try {
                bufferedReader2.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return i4;
        } catch (Exception e4) {
            try {
                bufferedReader.close();
            } catch (Exception e5) {
                e5.printStackTrace();
            }
            return 0;
        } catch (Throwable th) {
            try {
                bufferedReader.close();
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            throw th;
        }
    }
}
