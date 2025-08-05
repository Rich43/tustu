package V;

import java.io.PrintStream;

/* loaded from: TunerStudioMS.jar:V/g.class */
public class g extends Exception {

    /* renamed from: a, reason: collision with root package name */
    private Exception f1908a;

    /* renamed from: b, reason: collision with root package name */
    private int f1909b;

    public g(String str) {
        super(str);
        this.f1908a = null;
        this.f1909b = 0;
    }

    public g(String str, int i2) {
        super(str);
        this.f1908a = null;
        this.f1909b = 0;
        this.f1909b = i2;
    }

    public g(String str, Exception exc) {
        super(str);
        this.f1908a = null;
        this.f1909b = 0;
        a(exc);
    }

    public void a(Exception exc) {
        this.f1908a = exc;
    }

    public int a() {
        return this.f1909b;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return (this.f1908a == null || this.f1908a.getMessage() == null) ? super.getMessage() : super.getMessage() + "\nRoot Problem:" + this.f1908a.getMessage();
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.out);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (this.f1908a != null) {
            printStream.println("Nested Exception - Root cause:");
            this.f1908a.printStackTrace(printStream);
        }
    }
}
