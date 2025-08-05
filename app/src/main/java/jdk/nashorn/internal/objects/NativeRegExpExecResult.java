package jdk.nashorn.internal.objects;

import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.regexp.RegExpResult;

/* loaded from: nashorn.jar:jdk/nashorn/internal/objects/NativeRegExpExecResult.class */
public final class NativeRegExpExecResult extends ScriptObject {
    public Object index;
    public Object input;
    private static PropertyMap $nasgenmap$;

    /* JADX WARN: Failed to check method for inline after forced processjdk.nashorn.internal.objects.NativeRegExpExecResult.$clinit$():void */
    static {
        $clinit$();
    }

    /*  JADX ERROR: Failed to decode insn: 0x000C: CONST
        jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
        	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    /*  JADX ERROR: Failed to decode insn: 0x001C: CONST
        jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
        	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    /*  JADX ERROR: Failed to decode insn: 0x002D: CONST
        jadx.plugins.input.java.utils.JavaClassParseException: Unsupported constant type: METHOD_HANDLE
        	at jadx.plugins.input.java.data.code.decoders.LoadConstDecoder.decode(LoadConstDecoder.java:65)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    public static void $clinit$() {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r2 = 3
            r1.<init>(r2)
            r1 = r0
            java.lang.String r2 = "index"
            r3 = 0
            // decode failed: Unsupported constant type: METHOD_HANDLE
            long r2 = r2 & r3
            jdk.nashorn.internal.runtime.AccessorProperty.create(r-1, r0, r1, r2)
            boolean r-2 = r-2.add(r-1)
            r-2 = r-3
            java.lang.String r-1 = "input"
            r0 = 0
            // decode failed: Unsupported constant type: METHOD_HANDLE
            int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
            jdk.nashorn.internal.runtime.AccessorProperty r-4 = jdk.nashorn.internal.runtime.AccessorProperty.create(r-4, r-3, r-2, r-1)
            boolean r-5 = r-5.add(r-4)
            r-5 = r-6
            java.lang.String r-4 = "length"
            r-3 = 6
            // decode failed: Unsupported constant type: METHOD_HANDLE
            if (r-3 < 0) goto LB_47d1
            long r-4 = (long) r-4
            boolean r-5 = r-5.add(r-4)
            jdk.nashorn.internal.runtime.PropertyMap r-6 = jdk.nashorn.internal.runtime.PropertyMap.newMap(r-6)
            jdk.nashorn.internal.objects.NativeRegExpExecResult.$nasgenmap$ = r-6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.objects.NativeRegExpExecResult.$clinit$():void");
    }

    public Object G$index() {
        return this.index;
    }

    public void S$index(Object obj) {
        this.index = obj;
    }

    public Object G$input() {
        return this.input;
    }

    public void S$input(Object obj) {
        this.input = obj;
    }

    NativeRegExpExecResult(RegExpResult result, Global global) {
        super(global.getArrayPrototype(), $nasgenmap$);
        setIsArray();
        setArray(ArrayData.allocate((Object[]) result.getGroups().clone()));
        this.index = Integer.valueOf(result.getIndex());
        this.input = result.getInput();
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public String getClassName() {
        return "Array";
    }

    public static Object length(Object self) {
        if (self instanceof ScriptObject) {
            return Double.valueOf(JSType.toUint32(((ScriptObject) self).getArray().length()));
        }
        return 0;
    }

    public static void length(Object self, Object length) {
        if (self instanceof ScriptObject) {
            ((ScriptObject) self).setLength(NativeArray.validLength(length));
        }
    }
}
