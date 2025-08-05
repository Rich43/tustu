package jdk.nashorn.internal.objects;

import java.util.Collections;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptObject;

/* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeMath.class */
public final class NativeMath extends ScriptObject {
    private static PropertyMap $nasgenmap$;

    /* renamed from: E, reason: collision with root package name */
    public static final double f12874E = 2.718281828459045d;
    public static final double LN10 = 2.302585092994046d;
    public static final double LN2 = 0.6931471805599453d;
    public static final double LOG2E = 1.4426950408889634d;
    public static final double LOG10E = 0.4342944819032518d;
    public static final double PI = 3.141592653589793d;
    public static final double SQRT1_2 = 0.7071067811865476d;
    public static final double SQRT2 = 1.4142135623730951d;

    /* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeMath$Constructor.class */
    final class Constructor extends ScriptObject {
        private Object abs;
        private Object acos;
        private Object asin;
        private Object atan;
        private Object atan2;
        private Object ceil;
        private Object cos;
        private Object exp;
        private Object floor;
        private Object log;
        private Object max;
        private Object min;
        private Object pow;
        private Object random;
        private Object round;
        private Object sin;
        private Object sqrt;
        private Object tan;
        private static final PropertyMap $nasgenmap$ = null;

        public double G$E() {
            return NativeMath.f12874E;
        }

        public double G$LN10() {
            return NativeMath.LN10;
        }

        public double G$LN2() {
            return NativeMath.LN2;
        }

        public double G$LOG2E() {
            return NativeMath.LOG2E;
        }

        public double G$LOG10E() {
            return NativeMath.LOG10E;
        }

        public double G$PI() {
            return NativeMath.PI;
        }

        public double G$SQRT1_2() {
            return NativeMath.SQRT1_2;
        }

        public double G$SQRT2() {
            return NativeMath.SQRT2;
        }

        public Object G$abs() {
            return this.abs;
        }

        public void S$abs(Object obj) {
            this.abs = obj;
        }

        public Object G$acos() {
            return this.acos;
        }

        public void S$acos(Object obj) {
            this.acos = obj;
        }

        public Object G$asin() {
            return this.asin;
        }

        public void S$asin(Object obj) {
            this.asin = obj;
        }

        public Object G$atan() {
            return this.atan;
        }

        public void S$atan(Object obj) {
            this.atan = obj;
        }

        public Object G$atan2() {
            return this.atan2;
        }

        public void S$atan2(Object obj) {
            this.atan2 = obj;
        }

        public Object G$ceil() {
            return this.ceil;
        }

        public void S$ceil(Object obj) {
            this.ceil = obj;
        }

        public Object G$cos() {
            return this.cos;
        }

        public void S$cos(Object obj) {
            this.cos = obj;
        }

        public Object G$exp() {
            return this.exp;
        }

        public void S$exp(Object obj) {
            this.exp = obj;
        }

        public Object G$floor() {
            return this.floor;
        }

        public void S$floor(Object obj) {
            this.floor = obj;
        }

        public Object G$log() {
            return this.log;
        }

        public void S$log(Object obj) {
            this.log = obj;
        }

        public Object G$max() {
            return this.max;
        }

        public void S$max(Object obj) {
            this.max = obj;
        }

        public Object G$min() {
            return this.min;
        }

        public void S$min(Object obj) {
            this.min = obj;
        }

        public Object G$pow() {
            return this.pow;
        }

        public void S$pow(Object obj) {
            this.pow = obj;
        }

        public Object G$random() {
            return this.random;
        }

        public void S$random(Object obj) {
            this.random = obj;
        }

        public Object G$round() {
            return this.round;
        }

        public void S$round(Object obj) {
            this.round = obj;
        }

        public Object G$sin() {
            return this.sin;
        }

        public void S$sin(Object obj) {
            this.sin = obj;
        }

        public Object G$sqrt() {
            return this.sqrt;
        }

        public void S$sqrt(Object obj) {
            this.sqrt = obj;
        }

        public Object G$tan() {
            return this.tan;
        }

        public void S$tan(Object obj) {
            this.tan = obj;
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: ArrayIndexOutOfBoundsException: null in method: jdk.nashorn.internal.objects.NativeMath.Constructor.<init>():void, file: nashorn.jar:jdk/nashorn/internal/objects/NativeMath$Constructor.class
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:168)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            Caused by: java.lang.ArrayIndexOutOfBoundsException
            */
        Constructor() {
            /*
            // Can't load method instructions: Load method exception: ArrayIndexOutOfBoundsException: null in method: jdk.nashorn.internal.objects.NativeMath.Constructor.<init>():void, file: nashorn.jar:jdk/nashorn/internal/objects/NativeMath$Constructor.class
            */
            throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.objects.NativeMath.Constructor.<init>():void");
        }

        @Override // jdk.nashorn.internal.runtime.ScriptObject
        public String getClassName() {
            return "Math";
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeMath$Prototype.class */
    final class Prototype extends PrototypeObject {
        Prototype() {
        }

        @Override // jdk.nashorn.internal.runtime.ScriptObject
        public String getClassName() {
            return "Math";
        }
    }

    static {
        $clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    private NativeMath() {
        throw new UnsupportedOperationException();
    }

    public static double abs(Object self, Object x2) {
        return Math.abs(JSType.toNumber(x2));
    }

    public static int abs(Object self, int x2) {
        return Math.abs(x2);
    }

    public static long abs(Object self, long x2) {
        return Math.abs(x2);
    }

    public static double abs(Object self, double x2) {
        return Math.abs(x2);
    }

    public static double acos(Object self, Object x2) {
        return Math.acos(JSType.toNumber(x2));
    }

    public static double acos(Object self, double x2) {
        return Math.acos(x2);
    }

    public static double asin(Object self, Object x2) {
        return Math.asin(JSType.toNumber(x2));
    }

    public static double asin(Object self, double x2) {
        return Math.asin(x2);
    }

    public static double atan(Object self, Object x2) {
        return Math.atan(JSType.toNumber(x2));
    }

    public static double atan(Object self, double x2) {
        return Math.atan(x2);
    }

    public static double atan2(Object self, Object y2, Object x2) {
        return Math.atan2(JSType.toNumber(y2), JSType.toNumber(x2));
    }

    public static double atan2(Object self, double y2, double x2) {
        return Math.atan2(y2, x2);
    }

    public static double ceil(Object self, Object x2) {
        return Math.ceil(JSType.toNumber(x2));
    }

    public static int ceil(Object self, int x2) {
        return x2;
    }

    public static long ceil(Object self, long x2) {
        return x2;
    }

    public static double ceil(Object self, double x2) {
        return Math.ceil(x2);
    }

    public static double cos(Object self, Object x2) {
        return Math.cos(JSType.toNumber(x2));
    }

    public static double cos(Object self, double x2) {
        return Math.cos(x2);
    }

    public static double exp(Object self, Object x2) {
        return Math.exp(JSType.toNumber(x2));
    }

    public static double floor(Object self, Object x2) {
        return Math.floor(JSType.toNumber(x2));
    }

    public static int floor(Object self, int x2) {
        return x2;
    }

    public static long floor(Object self, long x2) {
        return x2;
    }

    public static double floor(Object self, double x2) {
        return Math.floor(x2);
    }

    public static double log(Object self, Object x2) {
        return Math.log(JSType.toNumber(x2));
    }

    public static double log(Object self, double x2) {
        return Math.log(x2);
    }

    public static double max(Object self, Object... args) {
        switch (args.length) {
            case 0:
                return Double.NEGATIVE_INFINITY;
            case 1:
                return JSType.toNumber(args[0]);
            default:
                double res = JSType.toNumber(args[0]);
                for (int i2 = 1; i2 < args.length; i2++) {
                    res = Math.max(res, JSType.toNumber(args[i2]));
                }
                return res;
        }
    }

    public static double max(Object self) {
        return Double.NEGATIVE_INFINITY;
    }

    public static int max(Object self, int x2, int y2) {
        return Math.max(x2, y2);
    }

    public static long max(Object self, long x2, long y2) {
        return Math.max(x2, y2);
    }

    public static double max(Object self, double x2, double y2) {
        return Math.max(x2, y2);
    }

    public static double max(Object self, Object x2, Object y2) {
        return Math.max(JSType.toNumber(x2), JSType.toNumber(y2));
    }

    public static double min(Object self, Object... args) {
        switch (args.length) {
            case 0:
                return Double.POSITIVE_INFINITY;
            case 1:
                return JSType.toNumber(args[0]);
            default:
                double res = JSType.toNumber(args[0]);
                for (int i2 = 1; i2 < args.length; i2++) {
                    res = Math.min(res, JSType.toNumber(args[i2]));
                }
                return res;
        }
    }

    public static double min(Object self) {
        return Double.POSITIVE_INFINITY;
    }

    public static int min(Object self, int x2, int y2) {
        return Math.min(x2, y2);
    }

    public static long min(Object self, long x2, long y2) {
        return Math.min(x2, y2);
    }

    public static double min(Object self, double x2, double y2) {
        return Math.min(x2, y2);
    }

    public static double min(Object self, Object x2, Object y2) {
        return Math.min(JSType.toNumber(x2), JSType.toNumber(y2));
    }

    public static double pow(Object self, Object x2, Object y2) {
        return Math.pow(JSType.toNumber(x2), JSType.toNumber(y2));
    }

    public static double pow(Object self, double x2, double y2) {
        return Math.pow(x2, y2);
    }

    public static double random(Object self) {
        return Math.random();
    }

    public static double round(Object self, Object x2) {
        double d2 = JSType.toNumber(x2);
        if (Math.getExponent(d2) >= 52) {
            return d2;
        }
        return Math.copySign(Math.floor(d2 + 0.5d), d2);
    }

    public static double sin(Object self, Object x2) {
        return Math.sin(JSType.toNumber(x2));
    }

    public static double sin(Object self, double x2) {
        return Math.sin(x2);
    }

    public static double sqrt(Object self, Object x2) {
        return Math.sqrt(JSType.toNumber(x2));
    }

    public static double sqrt(Object self, double x2) {
        return Math.sqrt(x2);
    }

    public static double tan(Object self, Object x2) {
        return Math.tan(JSType.toNumber(x2));
    }

    public static double tan(Object self, double x2) {
        return Math.tan(x2);
    }
}
