package D;

/* loaded from: TunerStudioMS.jar:D/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private int f242a = 0;

    /* renamed from: b, reason: collision with root package name */
    private a f243b = null;

    public int a() {
        return this.f242a;
    }

    public void a(int i2) {
        this.f242a = i2;
    }

    public a b() {
        return this.f243b;
    }

    public void a(a aVar) {
        this.f243b = aVar;
    }

    public static String b(int i2) {
        switch (i2) {
            case 0:
                return "Successful";
            case 1:
                return "Activation processing error 1! Please contact EFI Analytics. support@efianalytics.com";
            case 2:
                return "Activation processing error 2! Please contact EFI Analytics. support@efianalytics.com";
            case 4:
                return "Activation processing error 3! Please contact EFI Analytics. support@efianalytics.com";
            case 8:
                return "This device is currently disabled! Please contact EFI Analytics. support@efianalytics.com";
            case 32768:
                return "Cannot access internet. Please connect to the Internet to activate this device.";
            case 65536:
                return "Activation processing error 4! Please contact EFI Analytics. support@efianalytics.com";
            default:
                return "Activation processing error 4! Please contact EFI Analytics. support@efianalytics.com";
        }
    }
}
