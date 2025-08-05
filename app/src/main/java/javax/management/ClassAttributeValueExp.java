package javax.management;

/* loaded from: rt.jar:javax/management/ClassAttributeValueExp.class */
class ClassAttributeValueExp extends AttributeValueExp {
    private static final long oldSerialVersionUID = -2212731951078526753L;
    private static final long newSerialVersionUID = -1081892073854801359L;
    private static final long serialVersionUID;
    private String attr;

    /* JADX WARN: Removed duplicated region for block: B:8:0x0025  */
    static {
        /*
            r0 = 0
            r4 = r0
            com.sun.jmx.mbeanserver.GetPropertyAction r0 = new com.sun.jmx.mbeanserver.GetPropertyAction     // Catch: java.lang.Exception -> L2a
            r1 = r0
            java.lang.String r2 = "jmx.serial.form"
            r1.<init>(r2)     // Catch: java.lang.Exception -> L2a
            r5 = r0
            r0 = r5
            java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch: java.lang.Exception -> L2a
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Exception -> L2a
            r6 = r0
            r0 = r6
            if (r0 == 0) goto L25
            r0 = r6
            java.lang.String r1 = "1.0"
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> L2a
            if (r0 == 0) goto L25
            r0 = 1
            goto L26
        L25:
            r0 = 0
        L26:
            r4 = r0
            goto L2b
        L2a:
            r5 = move-exception
        L2b:
            r0 = r4
            if (r0 == 0) goto L38
            r0 = -2212731951078526753(0xe14acc0398ceb0df, double:-4.7092608401687987E160)
            javax.management.ClassAttributeValueExp.serialVersionUID = r0
            goto L3e
        L38:
            r0 = -1081892073854801359(0xf0fc58e41492ea31, double:-1.802632716247347E236)
            javax.management.ClassAttributeValueExp.serialVersionUID = r0
        L3e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.ClassAttributeValueExp.m4039clinit():void");
    }

    public ClassAttributeValueExp() {
        super("Class");
        this.attr = "Class";
    }

    @Override // javax.management.AttributeValueExp, javax.management.ValueExp
    public ValueExp apply(ObjectName objectName) throws InvalidApplicationException, BadBinaryOpValueExpException, BadAttributeValueExpException, BadStringOperationException {
        Object value = getValue(objectName);
        if (value instanceof String) {
            return new StringValueExp((String) value);
        }
        throw new BadAttributeValueExpException(value);
    }

    @Override // javax.management.AttributeValueExp
    public String toString() {
        return this.attr;
    }

    protected Object getValue(ObjectName objectName) {
        try {
            return QueryEval.getMBeanServer().getObjectInstance(objectName).getClassName();
        } catch (Exception e2) {
            return null;
        }
    }
}
