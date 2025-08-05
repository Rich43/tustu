jadx.core.utils.exceptions.JadxRuntimeException: Failed to generate code for class: jdk.nashorn.internal.objects.NativeJava
	at jadx.core.ProcessClass.generateCode(ProcessClass.java:123)
	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
Caused by: java.lang.OutOfMemoryError: Java heap space
	at jadx.plugins.input.java.data.code.decoders.TableSwitchDecoder.read(TableSwitchDecoder.java:35)
	at jadx.plugins.input.java.data.code.decoders.TableSwitchDecoder.decode(TableSwitchDecoder.java:12)
	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
	at jadx.core.dex.instructions.InsnDecoder$$Lambda/0x0000000080623b28.accept(Unknown Source)
	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
	at jadx.core.ProcessClass.process(ProcessClass.java:69)
	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
	... 3 more

