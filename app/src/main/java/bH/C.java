package bH;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bH/C.class */
public class C {

    /* renamed from: a, reason: collision with root package name */
    static HashMap f6990a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    static HashMap f6991b = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private static boolean f6992d = true;

    /* renamed from: e, reason: collision with root package name */
    private static boolean f6993e = false;

    /* renamed from: f, reason: collision with root package name */
    private static InterfaceC1010r f6994f = null;

    /* renamed from: g, reason: collision with root package name */
    private static BufferedWriter f6995g = null;

    /* renamed from: h, reason: collision with root package name */
    private static List f6996h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private static List f6997i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private static String f6998j = null;

    /* renamed from: k, reason: collision with root package name */
    private static int f6999k = 0;

    /* renamed from: l, reason: collision with root package name */
    private static long f7000l = Long.MAX_VALUE;

    /* renamed from: m, reason: collision with root package name */
    private static int f7001m = 120000;

    /* renamed from: c, reason: collision with root package name */
    static PrintWriter f7002c = null;

    public static boolean a() {
        return f6992d;
    }

    public static void a(boolean z2) {
        f6992d = z2;
    }

    public static void a(InterfaceC1009q interfaceC1009q) {
        f6997i.add(interfaceC1009q);
    }

    private static void f(String str) {
        Iterator it = f6997i.iterator();
        while (it.hasNext()) {
            ((InterfaceC1009q) it.next()).a(str);
        }
    }

    public static void a(InterfaceC1010r interfaceC1010r) {
        f6994f = interfaceC1010r;
    }

    public static void b() {
        for (Object obj : f6996h) {
            if (obj instanceof OutputStream) {
                try {
                    ((OutputStream) obj).flush();
                } catch (Exception e2) {
                    Logger.getLogger(C.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
    }

    private static ArrayList a(Thread thread) {
        return (ArrayList) f6991b.get(thread);
    }

    private static ArrayList b(Thread thread) {
        return (ArrayList) f6990a.get(thread);
    }

    private static void c(Thread thread) {
        f6991b.remove(thread);
    }

    private static void d(Thread thread) {
        f6990a.remove(thread);
    }

    public static void a(String str, Exception exc) {
        String str2 = "Error: " + str;
        g(str2);
        f(str2);
        ArrayList arrayListB = b(Thread.currentThread());
        StringWriter stringWriter = new StringWriter();
        exc.printStackTrace(new PrintWriter(stringWriter));
        String string = stringWriter.toString();
        if (arrayListB == null) {
            b("errorStack is null! the message should be: " + str2 + "\n" + string);
        } else {
            arrayListB.add("Error: " + str2);
            arrayListB.add(string);
        }
    }

    public static void a(String str) {
        String str2 = "Error: " + str;
        g(str2);
        f(str2);
        ArrayList arrayListB = b(Thread.currentThread());
        if (arrayListB != null) {
            arrayListB.add("Error: " + str2);
        }
    }

    public static void b(String str) {
        g("Warning: " + str);
        ArrayList arrayListA = a(Thread.currentThread());
        if (arrayListA != null) {
            arrayListA.add("Warning: " + str);
        }
    }

    public static void c(String str) {
        if (a()) {
            g("Debug: " + str);
        }
    }

    public static void d(String str) {
        g("Info: " + str);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00ff A[Catch: all -> 0x01c5, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000c, B:8:0x0016, B:36:0x01ab, B:38:0x01b2, B:39:0x01b8, B:41:0x01c1, B:10:0x0025, B:12:0x003c, B:14:0x0042, B:18:0x00ae, B:19:0x00b7, B:21:0x00c0, B:17:0x0088, B:16:0x007c, B:22:0x00f9, B:24:0x00ff, B:26:0x0105, B:31:0x0172, B:32:0x017b, B:34:0x0184, B:35:0x019a, B:29:0x0131, B:28:0x0125, B:30:0x0151), top: B:49:0x0006, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0151 A[Catch: all -> 0x01c5, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000c, B:8:0x0016, B:36:0x01ab, B:38:0x01b2, B:39:0x01b8, B:41:0x01c1, B:10:0x0025, B:12:0x003c, B:14:0x0042, B:18:0x00ae, B:19:0x00b7, B:21:0x00c0, B:17:0x0088, B:16:0x007c, B:22:0x00f9, B:24:0x00ff, B:26:0x0105, B:31:0x0172, B:32:0x017b, B:34:0x0184, B:35:0x019a, B:29:0x0131, B:28:0x0125, B:30:0x0151), top: B:49:0x0006, inners: #1, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0184 A[Catch: all -> 0x01c5, LOOP:1: B:32:0x017b->B:34:0x0184, LOOP_END, TryCatch #0 {, blocks: (B:4:0x0006, B:6:0x000c, B:8:0x0016, B:36:0x01ab, B:38:0x01b2, B:39:0x01b8, B:41:0x01c1, B:10:0x0025, B:12:0x003c, B:14:0x0042, B:18:0x00ae, B:19:0x00b7, B:21:0x00c0, B:17:0x0088, B:16:0x007c, B:22:0x00f9, B:24:0x00ff, B:26:0x0105, B:31:0x0172, B:32:0x017b, B:34:0x0184, B:35:0x019a, B:29:0x0131, B:28:0x0125, B:30:0x0151), top: B:49:0x0006, inners: #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void g(java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 461
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bH.C.g(java.lang.String):void");
    }

    public static void a(String str, Exception exc, Object obj) {
        if (f6994f != null) {
            f6994f.a(str, exc, obj);
            return;
        }
        if (exc != null) {
            System.out.println("\t" + exc.getMessage());
            exc.printStackTrace();
            if (exc.getMessage() != null && str.indexOf(exc.getMessage()) == -1) {
                str = str + "\nReported Error:\n" + exc.getMessage();
            }
        }
        g("Error Reported: " + str);
    }

    public static void c() {
        if (b(Thread.currentThread()) == null) {
            f6990a.put(Thread.currentThread(), new ArrayList());
        } else {
            b(Thread.currentThread()).clear();
        }
        ArrayList arrayList = (ArrayList) f6991b.get(Thread.currentThread());
        if (arrayList != null) {
            arrayList.clear();
        } else {
            f6991b.put(Thread.currentThread(), new ArrayList());
        }
    }

    public static int d() {
        ArrayList arrayListB = b(Thread.currentThread());
        if (arrayListB != null && !arrayListB.isEmpty()) {
            return arrayListB.size();
        }
        d(Thread.currentThread());
        return 0;
    }

    public static int e() {
        ArrayList arrayListA = a(Thread.currentThread());
        if (arrayListA != null && !arrayListA.isEmpty()) {
            return arrayListA.size();
        }
        c(Thread.currentThread());
        return 0;
    }

    public static String f() {
        ArrayList arrayListB = b(Thread.currentThread());
        ArrayList arrayListA = a(Thread.currentThread());
        if (arrayListB == null && arrayListA == null) {
            return "No errors or warnings captured.";
        }
        String str = "";
        if (arrayListB != null) {
            str = arrayListB.size() + " Errors:\n";
            Iterator it = arrayListB.iterator();
            while (it.hasNext()) {
                str = str + it.next().toString() + "\n";
            }
            d(Thread.currentThread());
        }
        if (arrayListA != null) {
            str = str + "\n" + arrayListA.size() + " Warnings:\n";
            Iterator it2 = arrayListA.iterator();
            while (it2.hasNext()) {
                str = str + it2.next().toString() + "\n";
            }
            c(Thread.currentThread());
        }
        return str;
    }

    public static void g() {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Thread thread : allStackTraces.keySet()) {
            c("Printing Stack for Thread: " + thread.toString());
            for (StackTraceElement stackTraceElement : allStackTraces.get(thread)) {
                System.out.println("\t" + stackTraceElement.toString());
            }
        }
    }

    public static void a(Exception exc) {
        if (h() != null) {
            exc.printStackTrace(h());
        } else {
            exc.printStackTrace();
        }
    }

    private static PrintWriter h() {
        if (f7002c == null) {
            if (f6995g != null) {
                f7002c = new PrintWriter(f6995g);
            } else {
                f7002c = new PrintWriter(System.out);
            }
        }
        return f7002c;
    }

    public static void e(String str) {
        g(str);
    }
}
