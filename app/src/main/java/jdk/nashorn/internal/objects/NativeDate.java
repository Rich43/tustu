package jdk.nashorn.internal.objects;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.parser.DateParser;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.PrototypeObject;
import jdk.nashorn.internal.runtime.ScriptEnvironment;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeDate.class */
public final class NativeDate extends ScriptObject {
    private static final String INVALID_DATE = "Invalid Date";
    private static final int YEAR = 0;
    private static final int MONTH = 1;
    private static final int DAY = 2;
    private static final int HOUR = 3;
    private static final int MINUTE = 4;
    private static final int SECOND = 5;
    private static final int MILLISECOND = 6;
    private static final int FORMAT_DATE_TIME = 0;
    private static final int FORMAT_DATE = 1;
    private static final int FORMAT_TIME = 2;
    private static final int FORMAT_LOCAL_DATE_TIME = 3;
    private static final int FORMAT_LOCAL_DATE = 4;
    private static final int FORMAT_LOCAL_TIME = 5;
    private static final int hoursPerDay = 24;
    private static final int minutesPerHour = 60;
    private static final int secondsPerMinute = 60;
    private static final int msPerSecond = 1000;
    private static final int msPerMinute = 60000;
    private static final double msPerHour = 3600000.0d;
    private static final double msPerDay = 8.64E7d;
    private static int[][] firstDayInMonth;
    private static String[] weekDays;
    private static String[] months;
    private static final Object TO_ISO_STRING;
    private double time;
    private final TimeZone timezone;
    private static PropertyMap $nasgenmap$;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeDate$Constructor.class */
    final class Constructor extends ScriptFunction {
        private Object parse;
        private Object UTC;
        private Object now;
        private static final PropertyMap $nasgenmap$ = null;

        public Object G$parse() {
            return this.parse;
        }

        public void S$parse(Object obj) {
            this.parse = obj;
        }

        public Object G$UTC() {
            return this.UTC;
        }

        public void S$UTC(Object obj) {
            this.UTC = obj;
        }

        public Object G$now() {
            return this.now;
        }

        public void S$now(Object obj) {
            this.now = obj;
        }

        /*  JADX ERROR: Failed to decode insn: 0x0003: CONST
            jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
            	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        /*  JADX ERROR: Failed to decode insn: 0x0011: CONST
            jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
            	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        /*  JADX ERROR: Failed to decode insn: 0x001D: CONST
            jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
            	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        /*  JADX ERROR: Failed to decode insn: 0x0027: CONST
            jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
            	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        /*  JADX ERROR: Failed to decode insn: 0x0037: CONST
            jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
            	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        Constructor() {
            /*
                r12 = this;
                r0 = r12
                java.lang.String r1 = "Date"
                // decode failed: Unsupported constant type: METHOD_HANDLE
                r14 = r1
                r1 = 1
                jdk.nashorn.internal.runtime.Specialization[] r1 = new jdk.nashorn.internal.runtime.Specialization[r1]
                r2 = r1
                r3 = 0
                jdk.nashorn.internal.runtime.Specialization r4 = new jdk.nashorn.internal.runtime.Specialization
                r5 = r4
                // decode failed: Unsupported constant type: METHOD_HANDLE
                r3.<init>(r4, r5)
                r0[r1] = r2
                r-5.<init>(r-4, r-3, r-2, r-1)
                r-5 = r12
                java.lang.String r-4 = "parse"
                // decode failed: Unsupported constant type: METHOD_HANDLE
                float r-5 = r-5 - r-4
                r-6.parse = r-5
                r-6 = r12
                java.lang.String r-5 = "UTC"
                // decode failed: Unsupported constant type: METHOD_HANDLE
                float r-6 = r-6 - r-5
                r-5 = r-6
                r-4 = 7
                r-5.setArity(r-4)
                r-7.UTC = r-6
                r-7 = r12
                java.lang.String r-6 = "now"
                // decode failed: Unsupported constant type: METHOD_HANDLE
                float r-7 = r-7 - r-6
                r-8.now = r-7
                r-8 = r12
                jdk.nashorn.internal.objects.NativeDate$Prototype r-7 = new jdk.nashorn.internal.objects.NativeDate$Prototype
                r-6 = r-7
                r-6.<init>()
                r-6 = r-7
                r-5 = r12
                jdk.nashorn.internal.runtime.PrototypeObject.setConstructor(r-6, r-5)
                r-8.setPrototype(r-7)
                r-8 = r12
                r-7 = 7
                r-8.setArity(r-7)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.objects.NativeDate.Constructor.<init>():void");
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeDate$Prototype.class */
    final class Prototype extends PrototypeObject {
        private Object toString;
        private Object toDateString;
        private Object toTimeString;
        private Object toLocaleString;
        private Object toLocaleDateString;
        private Object toLocaleTimeString;
        private Object valueOf;
        private Object getTime;
        private Object getFullYear;
        private Object getUTCFullYear;
        private Object getYear;
        private Object getMonth;
        private Object getUTCMonth;
        private Object getDate;
        private Object getUTCDate;
        private Object getDay;
        private Object getUTCDay;
        private Object getHours;
        private Object getUTCHours;
        private Object getMinutes;
        private Object getUTCMinutes;
        private Object getSeconds;
        private Object getUTCSeconds;
        private Object getMilliseconds;
        private Object getUTCMilliseconds;
        private Object getTimezoneOffset;
        private Object setTime;
        private Object setMilliseconds;
        private Object setUTCMilliseconds;
        private Object setSeconds;
        private Object setUTCSeconds;
        private Object setMinutes;
        private Object setUTCMinutes;
        private Object setHours;
        private Object setUTCHours;
        private Object setDate;
        private Object setUTCDate;
        private Object setMonth;
        private Object setUTCMonth;
        private Object setFullYear;
        private Object setUTCFullYear;
        private Object setYear;
        private Object toUTCString;
        private Object toGMTString;
        private Object toISOString;
        private Object toJSON;
        private static final PropertyMap $nasgenmap$ = null;

        public Object G$toString() {
            return this.toString;
        }

        public void S$toString(Object obj) {
            this.toString = obj;
        }

        public Object G$toDateString() {
            return this.toDateString;
        }

        public void S$toDateString(Object obj) {
            this.toDateString = obj;
        }

        public Object G$toTimeString() {
            return this.toTimeString;
        }

        public void S$toTimeString(Object obj) {
            this.toTimeString = obj;
        }

        public Object G$toLocaleString() {
            return this.toLocaleString;
        }

        public void S$toLocaleString(Object obj) {
            this.toLocaleString = obj;
        }

        public Object G$toLocaleDateString() {
            return this.toLocaleDateString;
        }

        public void S$toLocaleDateString(Object obj) {
            this.toLocaleDateString = obj;
        }

        public Object G$toLocaleTimeString() {
            return this.toLocaleTimeString;
        }

        public void S$toLocaleTimeString(Object obj) {
            this.toLocaleTimeString = obj;
        }

        public Object G$valueOf() {
            return this.valueOf;
        }

        public void S$valueOf(Object obj) {
            this.valueOf = obj;
        }

        public Object G$getTime() {
            return this.getTime;
        }

        public void S$getTime(Object obj) {
            this.getTime = obj;
        }

        public Object G$getFullYear() {
            return this.getFullYear;
        }

        public void S$getFullYear(Object obj) {
            this.getFullYear = obj;
        }

        public Object G$getUTCFullYear() {
            return this.getUTCFullYear;
        }

        public void S$getUTCFullYear(Object obj) {
            this.getUTCFullYear = obj;
        }

        public Object G$getYear() {
            return this.getYear;
        }

        public void S$getYear(Object obj) {
            this.getYear = obj;
        }

        public Object G$getMonth() {
            return this.getMonth;
        }

        public void S$getMonth(Object obj) {
            this.getMonth = obj;
        }

        public Object G$getUTCMonth() {
            return this.getUTCMonth;
        }

        public void S$getUTCMonth(Object obj) {
            this.getUTCMonth = obj;
        }

        public Object G$getDate() {
            return this.getDate;
        }

        public void S$getDate(Object obj) {
            this.getDate = obj;
        }

        public Object G$getUTCDate() {
            return this.getUTCDate;
        }

        public void S$getUTCDate(Object obj) {
            this.getUTCDate = obj;
        }

        public Object G$getDay() {
            return this.getDay;
        }

        public void S$getDay(Object obj) {
            this.getDay = obj;
        }

        public Object G$getUTCDay() {
            return this.getUTCDay;
        }

        public void S$getUTCDay(Object obj) {
            this.getUTCDay = obj;
        }

        public Object G$getHours() {
            return this.getHours;
        }

        public void S$getHours(Object obj) {
            this.getHours = obj;
        }

        public Object G$getUTCHours() {
            return this.getUTCHours;
        }

        public void S$getUTCHours(Object obj) {
            this.getUTCHours = obj;
        }

        public Object G$getMinutes() {
            return this.getMinutes;
        }

        public void S$getMinutes(Object obj) {
            this.getMinutes = obj;
        }

        public Object G$getUTCMinutes() {
            return this.getUTCMinutes;
        }

        public void S$getUTCMinutes(Object obj) {
            this.getUTCMinutes = obj;
        }

        public Object G$getSeconds() {
            return this.getSeconds;
        }

        public void S$getSeconds(Object obj) {
            this.getSeconds = obj;
        }

        public Object G$getUTCSeconds() {
            return this.getUTCSeconds;
        }

        public void S$getUTCSeconds(Object obj) {
            this.getUTCSeconds = obj;
        }

        public Object G$getMilliseconds() {
            return this.getMilliseconds;
        }

        public void S$getMilliseconds(Object obj) {
            this.getMilliseconds = obj;
        }

        public Object G$getUTCMilliseconds() {
            return this.getUTCMilliseconds;
        }

        public void S$getUTCMilliseconds(Object obj) {
            this.getUTCMilliseconds = obj;
        }

        public Object G$getTimezoneOffset() {
            return this.getTimezoneOffset;
        }

        public void S$getTimezoneOffset(Object obj) {
            this.getTimezoneOffset = obj;
        }

        public Object G$setTime() {
            return this.setTime;
        }

        public void S$setTime(Object obj) {
            this.setTime = obj;
        }

        public Object G$setMilliseconds() {
            return this.setMilliseconds;
        }

        public void S$setMilliseconds(Object obj) {
            this.setMilliseconds = obj;
        }

        public Object G$setUTCMilliseconds() {
            return this.setUTCMilliseconds;
        }

        public void S$setUTCMilliseconds(Object obj) {
            this.setUTCMilliseconds = obj;
        }

        public Object G$setSeconds() {
            return this.setSeconds;
        }

        public void S$setSeconds(Object obj) {
            this.setSeconds = obj;
        }

        public Object G$setUTCSeconds() {
            return this.setUTCSeconds;
        }

        public void S$setUTCSeconds(Object obj) {
            this.setUTCSeconds = obj;
        }

        public Object G$setMinutes() {
            return this.setMinutes;
        }

        public void S$setMinutes(Object obj) {
            this.setMinutes = obj;
        }

        public Object G$setUTCMinutes() {
            return this.setUTCMinutes;
        }

        public void S$setUTCMinutes(Object obj) {
            this.setUTCMinutes = obj;
        }

        public Object G$setHours() {
            return this.setHours;
        }

        public void S$setHours(Object obj) {
            this.setHours = obj;
        }

        public Object G$setUTCHours() {
            return this.setUTCHours;
        }

        public void S$setUTCHours(Object obj) {
            this.setUTCHours = obj;
        }

        public Object G$setDate() {
            return this.setDate;
        }

        public void S$setDate(Object obj) {
            this.setDate = obj;
        }

        public Object G$setUTCDate() {
            return this.setUTCDate;
        }

        public void S$setUTCDate(Object obj) {
            this.setUTCDate = obj;
        }

        public Object G$setMonth() {
            return this.setMonth;
        }

        public void S$setMonth(Object obj) {
            this.setMonth = obj;
        }

        public Object G$setUTCMonth() {
            return this.setUTCMonth;
        }

        public void S$setUTCMonth(Object obj) {
            this.setUTCMonth = obj;
        }

        public Object G$setFullYear() {
            return this.setFullYear;
        }

        public void S$setFullYear(Object obj) {
            this.setFullYear = obj;
        }

        public Object G$setUTCFullYear() {
            return this.setUTCFullYear;
        }

        public void S$setUTCFullYear(Object obj) {
            this.setUTCFullYear = obj;
        }

        public Object G$setYear() {
            return this.setYear;
        }

        public void S$setYear(Object obj) {
            this.setYear = obj;
        }

        public Object G$toUTCString() {
            return this.toUTCString;
        }

        public void S$toUTCString(Object obj) {
            this.toUTCString = obj;
        }

        public Object G$toGMTString() {
            return this.toGMTString;
        }

        public void S$toGMTString(Object obj) {
            this.toGMTString = obj;
        }

        public Object G$toISOString() {
            return this.toISOString;
        }

        public void S$toISOString(Object obj) {
            this.toISOString = obj;
        }

        public Object G$toJSON() {
            return this.toJSON;
        }

        public void S$toJSON(Object obj) {
            this.toJSON = obj;
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: ArrayIndexOutOfBoundsException: null in method: jdk.nashorn.internal.objects.NativeDate.Prototype.<init>():void, file: nashorn.jar:jdk/nashorn/internal/objects/NativeDate$Prototype.class
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:168)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            Caused by: java.lang.ArrayIndexOutOfBoundsException
            */
        Prototype() {
            /*
            // Can't load method instructions: Load method exception: ArrayIndexOutOfBoundsException: null in method: jdk.nashorn.internal.objects.NativeDate.Prototype.<init>():void, file: nashorn.jar:jdk/nashorn/internal/objects/NativeDate$Prototype.class
            */
            throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.objects.NativeDate.Prototype.<init>():void");
        }

        @Override // jdk.nashorn.internal.runtime.ScriptObject
        public String getClassName() {
            return "Date";
        }
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [int[], int[][]] */
    static {
        $assertionsDisabled = !NativeDate.class.desiredAssertionStatus();
        firstDayInMonth = new int[]{new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, FTPReply.SECURITY_MECHANISM_IS_OK}, new int[]{0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335}};
        weekDays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        TO_ISO_STRING = new Object();
        $clinit$();
    }

    private static InvokeByName getTO_ISO_STRING() {
        return Global.instance().getInvokeByName(TO_ISO_STRING, new Callable<InvokeByName>() { // from class: jdk.nashorn.internal.objects.NativeDate.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public InvokeByName call() {
                return new InvokeByName("toISOString", ScriptObject.class, Object.class, Object.class);
            }
        });
    }

    private NativeDate(double time, ScriptObject proto, PropertyMap map) {
        super(proto, map);
        ScriptEnvironment env = Global.getEnv();
        this.time = time;
        this.timezone = env._timezone;
    }

    NativeDate(double time, ScriptObject proto) {
        this(time, proto, $nasgenmap$);
    }

    NativeDate(double time, Global global) {
        this(time, global.getDatePrototype(), $nasgenmap$);
    }

    private NativeDate(double time) {
        this(time, Global.instance());
    }

    private NativeDate() {
        this(System.currentTimeMillis());
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public String getClassName() {
        return "Date";
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public Object getDefaultValue(Class<?> hint) {
        return super.getDefaultValue(hint == null ? String.class : hint);
    }

    public static Object construct(boolean isNew, Object self) {
        NativeDate result = new NativeDate();
        return isNew ? result : toStringImpl(result, 0);
    }

    public static Object construct(boolean isNew, Object self, Object... args) {
        NativeDate result;
        double num;
        if (!isNew) {
            return toStringImpl(new NativeDate(), 0);
        }
        switch (args.length) {
            case 0:
                result = new NativeDate();
                break;
            case 1:
                Object arg = JSType.toPrimitive(args[0]);
                if (JSType.isString(arg)) {
                    num = parseDateString(arg.toString());
                } else {
                    num = timeClip(JSType.toNumber(args[0]));
                }
                result = new NativeDate(num);
                break;
            default:
                result = new NativeDate(0.0d);
                double[] d2 = convertCtorArgs(args);
                if (d2 == null) {
                    result.setTime(Double.NaN);
                    break;
                } else {
                    double time = timeClip(utc(makeDate(d2), result.getTimeZone()));
                    result.setTime(time);
                    break;
                }
        }
        return result;
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public String safeToString() {
        String str = isValidDate() ? toISOStringImpl(this) : INVALID_DATE;
        return "[Date " + str + "]";
    }

    public String toString() {
        return isValidDate() ? toString(this).toString() : INVALID_DATE;
    }

    public static double parse(Object self, Object string) {
        return parseDateString(JSType.toString(string));
    }

    public static double UTC(Object self, Object... args) {
        NativeDate nd = new NativeDate(0.0d);
        double[] d2 = convertCtorArgs(args);
        double time = d2 == null ? Double.NaN : timeClip(makeDate(d2));
        nd.setTime(time);
        return time;
    }

    public static double now(Object self) {
        return System.currentTimeMillis();
    }

    public static String toString(Object self) {
        return toStringImpl(self, 0);
    }

    public static String toDateString(Object self) {
        return toStringImpl(self, 1);
    }

    public static String toTimeString(Object self) {
        return toStringImpl(self, 2);
    }

    public static String toLocaleString(Object self) {
        return toStringImpl(self, 3);
    }

    public static String toLocaleDateString(Object self) {
        return toStringImpl(self, 4);
    }

    public static String toLocaleTimeString(Object self) {
        return toStringImpl(self, 5);
    }

    public static double valueOf(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd != null) {
            return nd.getTime();
        }
        return Double.NaN;
    }

    public static double getTime(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd != null) {
            return nd.getTime();
        }
        return Double.NaN;
    }

    public static Object getFullYear(Object self) {
        return Double.valueOf(getField(self, 0));
    }

    public static double getUTCFullYear(Object self) {
        return getUTCField(self, 0);
    }

    public static double getYear(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd == null || !nd.isValidDate()) {
            return Double.NaN;
        }
        return yearFromTime(nd.getLocalTime()) - 1900;
    }

    public static double getMonth(Object self) {
        return getField(self, 1);
    }

    public static double getUTCMonth(Object self) {
        return getUTCField(self, 1);
    }

    public static double getDate(Object self) {
        return getField(self, 2);
    }

    public static double getUTCDate(Object self) {
        return getUTCField(self, 2);
    }

    public static double getDay(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd == null || !nd.isValidDate()) {
            return Double.NaN;
        }
        return weekDay(nd.getLocalTime());
    }

    public static double getUTCDay(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd == null || !nd.isValidDate()) {
            return Double.NaN;
        }
        return weekDay(nd.getTime());
    }

    public static double getHours(Object self) {
        return getField(self, 3);
    }

    public static double getUTCHours(Object self) {
        return getUTCField(self, 3);
    }

    public static double getMinutes(Object self) {
        return getField(self, 4);
    }

    public static double getUTCMinutes(Object self) {
        return getUTCField(self, 4);
    }

    public static double getSeconds(Object self) {
        return getField(self, 5);
    }

    public static double getUTCSeconds(Object self) {
        return getUTCField(self, 5);
    }

    public static double getMilliseconds(Object self) {
        return getField(self, 6);
    }

    public static double getUTCMilliseconds(Object self) {
        return getUTCField(self, 6);
    }

    public static double getTimezoneOffset(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            long msec = (long) nd.getTime();
            return (-nd.getTimeZone().getOffset(msec)) / msPerMinute;
        }
        return Double.NaN;
    }

    public static double setTime(Object self, Object time) {
        NativeDate nd = getNativeDate(self);
        double num = timeClip(JSType.toNumber(time));
        nd.setTime(num);
        return num;
    }

    public static double setMilliseconds(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 6, args, true);
        return nd.getTime();
    }

    public static double setUTCMilliseconds(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 6, args, false);
        return nd.getTime();
    }

    public static double setSeconds(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 5, args, true);
        return nd.getTime();
    }

    public static double setUTCSeconds(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 5, args, false);
        return nd.getTime();
    }

    public static double setMinutes(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 4, args, true);
        return nd.getTime();
    }

    public static double setUTCMinutes(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 4, args, false);
        return nd.getTime();
    }

    public static double setHours(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 3, args, true);
        return nd.getTime();
    }

    public static double setUTCHours(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 3, args, false);
        return nd.getTime();
    }

    public static double setDate(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 2, args, true);
        return nd.getTime();
    }

    public static double setUTCDate(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 2, args, false);
        return nd.getTime();
    }

    public static double setMonth(Object self, Object... args) {
        NativeDate nd = getNativeDate(self);
        setFields(nd, 1, args, true);
        return nd.getTime();
    }

    public static double setUTCMonth(Object self, Object... args) {
        NativeDate nd = ensureNativeDate(self);
        setFields(nd, 1, args, false);
        return nd.getTime();
    }

    public static double setFullYear(Object self, Object... args) {
        NativeDate nd = ensureNativeDate(self);
        if (nd.isValidDate()) {
            setFields(nd, 0, args, true);
        } else {
            double[] d2 = convertArgs(args, 0.0d, 0, 0, 3);
            if (d2 != null) {
                nd.setTime(timeClip(utc(makeDate(makeDay(d2[0], d2[1], d2[2]), 0.0d), nd.getTimeZone())));
            } else {
                nd.setTime(Double.NaN);
            }
        }
        return nd.getTime();
    }

    public static double setUTCFullYear(Object self, Object... args) {
        NativeDate nd = ensureNativeDate(self);
        if (nd.isValidDate()) {
            setFields(nd, 0, args, false);
        } else {
            double[] d2 = convertArgs(args, 0.0d, 0, 0, 3);
            nd.setTime(timeClip(makeDate(makeDay(d2[0], d2[1], d2[2]), 0.0d)));
        }
        return nd.getTime();
    }

    public static double setYear(Object self, Object year) {
        NativeDate nd = getNativeDate(self);
        if (Double.isNaN(nd.getTime())) {
            nd.setTime(utc(0.0d, nd.getTimeZone()));
        }
        double yearNum = JSType.toNumber(year);
        if (Double.isNaN(yearNum)) {
            nd.setTime(Double.NaN);
            return nd.getTime();
        }
        int yearInt = (int) yearNum;
        if (0 <= yearInt && yearInt <= 99) {
            yearInt += 1900;
        }
        setFields(nd, 0, new Object[]{Integer.valueOf(yearInt)}, true);
        return nd.getTime();
    }

    public static String toUTCString(Object self) {
        return toGMTStringImpl(self);
    }

    public static String toGMTString(Object self) {
        return toGMTStringImpl(self);
    }

    public static String toISOString(Object self) {
        return toISOStringImpl(self);
    }

    public static Object toJSON(Object self, Object key) {
        Object selfObj = Global.toObject(self);
        if (!(selfObj instanceof ScriptObject)) {
            return null;
        }
        ScriptObject sobj = (ScriptObject) selfObj;
        Object value = sobj.getDefaultValue(Number.class);
        if (value instanceof Number) {
            double num = ((Number) value).doubleValue();
            if (Double.isInfinite(num) || Double.isNaN(num)) {
                return null;
            }
        }
        try {
            try {
                InvokeByName toIsoString = getTO_ISO_STRING();
                Object func = (Object) toIsoString.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(func)) {
                    return (Object) toIsoString.getInvoker().invokeExact(func, sobj, key);
                }
                throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(func));
            } catch (Error | RuntimeException e2) {
                throw e2;
            }
        } catch (Throwable t2) {
            throw new RuntimeException(t2);
        }
    }

    private static double parseDateString(String str) {
        double d2;
        DateParser parser = new DateParser(str);
        if (parser.parse()) {
            Integer[] fields = parser.getDateFields();
            double d3 = makeDate(fields);
            if (fields[7] != null) {
                d2 = d3 - (fields[7].intValue() * msPerMinute);
            } else {
                d2 = utc(d3, Global.getEnv()._timezone);
            }
            return timeClip(d2);
        }
        return Double.NaN;
    }

    private static void zeroPad(StringBuilder sb, int n2, int length) {
        int l2 = 1;
        int i2 = 10;
        while (true) {
            int d2 = i2;
            if (l2 < length) {
                if (n2 < d2) {
                    sb.append('0');
                }
                l2++;
                i2 = d2 * 10;
            } else {
                sb.append(n2);
                return;
            }
        }
    }

    private static String toStringImpl(Object self, int format) {
        NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            StringBuilder sb = new StringBuilder(40);
            double t2 = nd.getLocalTime();
            switch (format) {
                case 0:
                case 1:
                case 3:
                    sb.append(weekDays[weekDay(t2)]).append(' ').append(months[monthFromTime(t2)]).append(' ');
                    zeroPad(sb, dayFromTime(t2), 2);
                    sb.append(' ');
                    zeroPad(sb, yearFromTime(t2), 4);
                    if (format != 1) {
                        sb.append(' ');
                        break;
                    }
                    return sb.toString();
                case 2:
                    break;
                case 4:
                    zeroPad(sb, yearFromTime(t2), 4);
                    sb.append('-');
                    zeroPad(sb, monthFromTime(t2) + 1, 2);
                    sb.append('-');
                    zeroPad(sb, dayFromTime(t2), 2);
                    return sb.toString();
                case 5:
                    zeroPad(sb, hourFromTime(t2), 2);
                    sb.append(':');
                    zeroPad(sb, minFromTime(t2), 2);
                    sb.append(':');
                    zeroPad(sb, secFromTime(t2), 2);
                    return sb.toString();
                default:
                    throw new IllegalArgumentException("format: " + format);
            }
            TimeZone tz = nd.getTimeZone();
            double utcTime = nd.getTime();
            int offset = tz.getOffset((long) utcTime) / msPerMinute;
            boolean inDaylightTime = offset != tz.getRawOffset() / msPerMinute;
            int offset2 = ((offset / 60) * 100) + (offset % 60);
            zeroPad(sb, hourFromTime(t2), 2);
            sb.append(':');
            zeroPad(sb, minFromTime(t2), 2);
            sb.append(':');
            zeroPad(sb, secFromTime(t2), 2);
            sb.append(" GMT").append(offset2 < 0 ? '-' : '+');
            zeroPad(sb, Math.abs(offset2), 4);
            sb.append(" (").append(tz.getDisplayName(inDaylightTime, 0, Locale.US)).append(')');
            return sb.toString();
        }
        return INVALID_DATE;
    }

    private static String toGMTStringImpl(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            StringBuilder sb = new StringBuilder(29);
            double t2 = nd.getTime();
            sb.append(weekDays[weekDay(t2)]).append(", ");
            zeroPad(sb, dayFromTime(t2), 2);
            sb.append(' ').append(months[monthFromTime(t2)]).append(' ');
            zeroPad(sb, yearFromTime(t2), 4);
            sb.append(' ');
            zeroPad(sb, hourFromTime(t2), 2);
            sb.append(':');
            zeroPad(sb, minFromTime(t2), 2);
            sb.append(':');
            zeroPad(sb, secFromTime(t2), 2);
            sb.append(" GMT");
            return sb.toString();
        }
        throw ECMAErrors.rangeError("invalid.date", new String[0]);
    }

    private static String toISOStringImpl(Object self) {
        NativeDate nd = getNativeDate(self);
        if (nd != null && nd.isValidDate()) {
            StringBuilder sb = new StringBuilder(24);
            double t2 = nd.getTime();
            zeroPad(sb, yearFromTime(t2), 4);
            sb.append('-');
            zeroPad(sb, monthFromTime(t2) + 1, 2);
            sb.append('-');
            zeroPad(sb, dayFromTime(t2), 2);
            sb.append('T');
            zeroPad(sb, hourFromTime(t2), 2);
            sb.append(':');
            zeroPad(sb, minFromTime(t2), 2);
            sb.append(':');
            zeroPad(sb, secFromTime(t2), 2);
            sb.append('.');
            zeroPad(sb, msFromTime(t2), 3);
            sb.append(Constants.HASIDCALL_INDEX_SIG);
            return sb.toString();
        }
        throw ECMAErrors.rangeError("invalid.date", new String[0]);
    }

    private static double day(double t2) {
        return Math.floor(t2 / msPerDay);
    }

    private static double timeWithinDay(double t2) {
        double val = t2 % msPerDay;
        return val < 0.0d ? val + msPerDay : val;
    }

    private static boolean isLeapYear(int y2) {
        return y2 % 4 == 0 && (y2 % 100 != 0 || y2 % 400 == 0);
    }

    private static int daysInYear(int y2) {
        return isLeapYear(y2) ? 366 : 365;
    }

    private static double dayFromYear(double y2) {
        return (((365.0d * (y2 - 1970.0d)) + Math.floor((y2 - 1969.0d) / 4.0d)) - Math.floor((y2 - 1901.0d) / 100.0d)) + Math.floor((y2 - 1601.0d) / 400.0d);
    }

    private static double timeFromYear(int y2) {
        return dayFromYear(y2) * msPerDay;
    }

    private static int yearFromTime(double t2) {
        int y2 = ((int) Math.floor(t2 / 3.1556952E10d)) + 1970;
        double t22 = timeFromYear(y2);
        if (t22 > t2) {
            y2--;
        } else if (t22 + (msPerDay * daysInYear(y2)) <= t2) {
            y2++;
        }
        return y2;
    }

    private static int dayWithinYear(double t2, int year) {
        return (int) (day(t2) - dayFromYear(year));
    }

    private static int monthFromTime(double t2) {
        int year = yearFromTime(t2);
        int day = dayWithinYear(t2, year);
        int[] firstDay = firstDayInMonth[isLeapYear(year) ? (char) 1 : (char) 0];
        int month = 0;
        while (month < 11 && firstDay[month + 1] <= day) {
            month++;
        }
        return month;
    }

    private static int dayFromTime(double t2) {
        int year = yearFromTime(t2);
        int day = dayWithinYear(t2, year);
        int[] firstDay = firstDayInMonth[isLeapYear(year) ? (char) 1 : (char) 0];
        int month = 0;
        while (month < 11 && firstDay[month + 1] <= day) {
            month++;
        }
        return (1 + day) - firstDay[month];
    }

    private static int dayFromMonth(int month, int year) {
        if (!$assertionsDisabled && (month < 0 || month > 11)) {
            throw new AssertionError();
        }
        int[] firstDay = firstDayInMonth[isLeapYear(year) ? (char) 1 : (char) 0];
        return firstDay[month];
    }

    private static int weekDay(double time) {
        int day = ((int) (day(time) + 4.0d)) % 7;
        return day < 0 ? day + 7 : day;
    }

    private static double localTime(double time, TimeZone tz) {
        return time + tz.getOffset((long) time);
    }

    private static double utc(double time, TimeZone tz) {
        return time - tz.getOffset((long) (time - tz.getRawOffset()));
    }

    private static int hourFromTime(double t2) {
        int h2 = (int) (Math.floor(t2 / msPerHour) % 24.0d);
        return h2 < 0 ? h2 + 24 : h2;
    }

    private static int minFromTime(double t2) {
        int m2 = (int) (Math.floor(t2 / 60000.0d) % 60.0d);
        return m2 < 0 ? m2 + 60 : m2;
    }

    private static int secFromTime(double t2) {
        int s2 = (int) (Math.floor(t2 / 1000.0d) % 60.0d);
        return s2 < 0 ? s2 + 60 : s2;
    }

    private static int msFromTime(double t2) {
        int m2 = (int) (t2 % 1000.0d);
        return m2 < 0 ? m2 + 1000 : m2;
    }

    private static int valueFromTime(int unit, double t2) {
        switch (unit) {
            case 0:
                return yearFromTime(t2);
            case 1:
                return monthFromTime(t2);
            case 2:
                return dayFromTime(t2);
            case 3:
                return hourFromTime(t2);
            case 4:
                return minFromTime(t2);
            case 5:
                return secFromTime(t2);
            case 6:
                return msFromTime(t2);
            default:
                throw new IllegalArgumentException(Integer.toString(unit));
        }
    }

    private static double makeTime(double hour, double min, double sec, double ms) {
        return (hour * msPerHour) + (min * 60000.0d) + (sec * 1000.0d) + ms;
    }

    private static double makeDay(double year, double month, double date) {
        double y2 = year + Math.floor(month / 12.0d);
        int m2 = (int) (month % 12.0d);
        if (m2 < 0) {
            m2 += 12;
        }
        double d2 = dayFromYear(y2);
        return ((d2 + dayFromMonth(m2, (int) y2)) + date) - 1.0d;
    }

    private static double makeDate(double day, double time) {
        return (day * msPerDay) + time;
    }

    private static double makeDate(Integer[] d2) {
        double time = makeDay(d2[0].intValue(), d2[1].intValue(), d2[2].intValue()) * msPerDay;
        return time + makeTime(d2[3].intValue(), d2[4].intValue(), d2[5].intValue(), d2[6].intValue());
    }

    private static double makeDate(double[] d2) {
        double time = makeDay(d2[0], d2[1], d2[2]) * msPerDay;
        return time + makeTime(d2[3], d2[4], d2[5], d2[6]);
    }

    private static double[] convertCtorArgs(Object[] args) {
        double[] d2 = new double[7];
        boolean nullReturn = false;
        int i2 = 0;
        while (i2 < d2.length) {
            if (i2 < args.length) {
                double darg = JSType.toNumber(args[i2]);
                if (Double.isNaN(darg) || Double.isInfinite(darg)) {
                    nullReturn = true;
                }
                d2[i2] = (long) darg;
            } else {
                d2[i2] = i2 == 2 ? 1.0d : 0.0d;
            }
            i2++;
        }
        if (0.0d <= d2[0] && d2[0] <= 99.0d) {
            d2[0] = d2[0] + 1900.0d;
        }
        if (nullReturn) {
            return null;
        }
        return d2;
    }

    private static double[] convertArgs(Object[] args, double time, int fieldId, int start, int length) {
        double[] d2 = new double[length];
        boolean nullReturn = false;
        for (int i2 = start; i2 < start + length; i2++) {
            if (fieldId <= i2 && i2 < fieldId + args.length) {
                double darg = JSType.toNumber(args[i2 - fieldId]);
                if (Double.isNaN(darg) || Double.isInfinite(darg)) {
                    nullReturn = true;
                }
                d2[i2 - start] = (long) darg;
            } else {
                if (i2 == fieldId) {
                    nullReturn = true;
                }
                if (!nullReturn && !Double.isNaN(time)) {
                    d2[i2 - start] = valueFromTime(i2, time);
                }
            }
        }
        if (nullReturn) {
            return null;
        }
        return d2;
    }

    private static double timeClip(double time) {
        if (Double.isInfinite(time) || Double.isNaN(time) || Math.abs(time) > 8.64E15d) {
            return Double.NaN;
        }
        return (long) time;
    }

    private static NativeDate ensureNativeDate(Object self) {
        return getNativeDate(self);
    }

    private static NativeDate getNativeDate(Object self) {
        if (self instanceof NativeDate) {
            return (NativeDate) self;
        }
        if (self != null && self == Global.instance().getDatePrototype()) {
            return Global.instance().getDefaultDate();
        }
        throw ECMAErrors.typeError("not.a.date", ScriptRuntime.safeToString(self));
    }

    private static double getField(Object self, int field) {
        NativeDate nd = getNativeDate(self);
        if (nd == null || !nd.isValidDate()) {
            return Double.NaN;
        }
        return valueFromTime(field, nd.getLocalTime());
    }

    private static double getUTCField(Object self, int field) {
        NativeDate nd = getNativeDate(self);
        if (nd == null || !nd.isValidDate()) {
            return Double.NaN;
        }
        return valueFromTime(field, nd.getTime());
    }

    private static void setFields(NativeDate nd, int fieldId, Object[] args, boolean local) {
        int start;
        int length;
        double newTime;
        double newTime2;
        if (fieldId < 3) {
            start = 0;
            length = 3;
        } else {
            start = 3;
            length = 4;
        }
        double time = local ? nd.getLocalTime() : nd.getTime();
        double[] d2 = convertArgs(args, time, fieldId, start, length);
        if (!nd.isValidDate()) {
            return;
        }
        if (d2 == null) {
            newTime2 = Double.NaN;
        } else {
            if (start == 0) {
                newTime = makeDate(makeDay(d2[0], d2[1], d2[2]), timeWithinDay(time));
            } else {
                newTime = makeDate(day(time), makeTime(d2[0], d2[1], d2[2], d2[3]));
            }
            if (local) {
                newTime = utc(newTime, nd.getTimeZone());
            }
            newTime2 = timeClip(newTime);
        }
        nd.setTime(newTime2);
    }

    private boolean isValidDate() {
        return !Double.isNaN(this.time);
    }

    private double getLocalTime() {
        return localTime(this.time, this.timezone);
    }

    private double getTime() {
        return this.time;
    }

    private void setTime(double time) {
        this.time = time;
    }

    private TimeZone getTimeZone() {
        return this.timezone;
    }
}
