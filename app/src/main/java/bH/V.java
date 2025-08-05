package bH;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/* loaded from: TunerStudioMS.jar:bH/V.class */
public class V extends Thread {

    /* renamed from: c, reason: collision with root package name */
    private static String f7024c = "http://www.efianalytics.com/activate/checkCon.txt";

    /* renamed from: a, reason: collision with root package name */
    static String f7025a = "ALIVE";

    /* renamed from: b, reason: collision with root package name */
    public static int f7026b = 10000;

    /* renamed from: d, reason: collision with root package name */
    private boolean f7027d = false;

    /* renamed from: e, reason: collision with root package name */
    private boolean f7028e = false;

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        a(false);
        try {
            URLConnection uRLConnectionOpenConnection = new URL(f7024c).openConnection();
            uRLConnectionOpenConnection.setConnectTimeout(b());
            byte[] bArr = new byte[5];
            uRLConnectionOpenConnection.getInputStream().read(bArr);
            this.f7027d = new String(bArr).equals(f7025a);
            uRLConnectionOpenConnection.getInputStream().close();
        } catch (MalformedURLException e2) {
            System.out.println("Bad URL:\n" + f7024c);
            this.f7027d = false;
        } catch (UnknownHostException e3) {
            C.c("Ukh: No Internet?");
            this.f7027d = false;
        } catch (Exception e4) {
            this.f7027d = false;
        }
        a(true);
    }

    public boolean a() {
        return this.f7027d;
    }

    protected int b() {
        return f7026b;
    }

    protected void a(int i2) {
        f7026b = i2;
    }

    protected boolean c() {
        return this.f7028e;
    }

    protected void a(boolean z2) {
        this.f7028e = z2;
    }
}
