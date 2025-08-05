package javax.management;

import java.io.Serializable;

/* loaded from: rt.jar:javax/management/QueryEval.class */
public abstract class QueryEval implements Serializable {
    private static final long serialVersionUID = 2675899265640874796L;
    private static ThreadLocal<MBeanServer> server = new InheritableThreadLocal();

    public void setMBeanServer(MBeanServer mBeanServer) {
        server.set(mBeanServer);
    }

    public static MBeanServer getMBeanServer() {
        return server.get();
    }
}
