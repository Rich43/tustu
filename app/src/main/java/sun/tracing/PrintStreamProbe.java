package sun.tracing;

/* compiled from: PrintStreamProviderFactory.java */
/* loaded from: rt.jar:sun/tracing/PrintStreamProbe.class */
class PrintStreamProbe extends ProbeSkeleton {
    private PrintStreamProvider provider;
    private String name;

    PrintStreamProbe(PrintStreamProvider printStreamProvider, String str, Class<?>[] clsArr) {
        super(clsArr);
        this.provider = printStreamProvider;
        this.name = str;
    }

    @Override // sun.tracing.ProbeSkeleton, com.sun.tracing.Probe
    public boolean isEnabled() {
        return true;
    }

    @Override // sun.tracing.ProbeSkeleton
    public void uncheckedTrigger(Object[] objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.provider.getName());
        stringBuffer.append(".");
        stringBuffer.append(this.name);
        stringBuffer.append("(");
        boolean z2 = true;
        for (Object obj : objArr) {
            if (!z2) {
                stringBuffer.append(",");
            } else {
                z2 = false;
            }
            stringBuffer.append(obj.toString());
        }
        stringBuffer.append(")");
        this.provider.getStream().println(stringBuffer.toString());
    }
}
