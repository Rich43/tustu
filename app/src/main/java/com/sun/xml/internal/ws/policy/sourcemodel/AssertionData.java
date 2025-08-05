package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/AssertionData.class */
public final class AssertionData implements Cloneable, Serializable {
    private static final long serialVersionUID = 4416256070795526315L;
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) AssertionData.class);
    private final QName name;
    private final String value;
    private final Map<QName, String> attributes = new HashMap();
    private ModelNode.Type type;
    private boolean optional;
    private boolean ignorable;

    public static AssertionData createAssertionData(QName name) throws IllegalArgumentException {
        return new AssertionData(name, null, null, ModelNode.Type.ASSERTION, false, false);
    }

    public static AssertionData createAssertionParameterData(QName name) throws IllegalArgumentException {
        return new AssertionData(name, null, null, ModelNode.Type.ASSERTION_PARAMETER_NODE, false, false);
    }

    public static AssertionData createAssertionData(QName name, String value, Map<QName, String> attributes, boolean optional, boolean ignorable) throws IllegalArgumentException {
        return new AssertionData(name, value, attributes, ModelNode.Type.ASSERTION, optional, ignorable);
    }

    public static AssertionData createAssertionParameterData(QName name, String value, Map<QName, String> attributes) throws IllegalArgumentException {
        return new AssertionData(name, value, attributes, ModelNode.Type.ASSERTION_PARAMETER_NODE, false, false);
    }

    AssertionData(QName name, String value, Map<QName, String> attributes, ModelNode.Type type, boolean optional, boolean ignorable) throws IllegalArgumentException {
        this.name = name;
        this.value = value;
        this.optional = optional;
        this.ignorable = ignorable;
        if (attributes != null && !attributes.isEmpty()) {
            this.attributes.putAll(attributes);
        }
        setModelNodeType(type);
    }

    private void setModelNodeType(ModelNode.Type type) throws IllegalArgumentException {
        if (type == ModelNode.Type.ASSERTION || type == ModelNode.Type.ASSERTION_PARAMETER_NODE) {
            this.type = type;
            return;
        }
        throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0074_CANNOT_CREATE_ASSERTION_BAD_TYPE(type, ModelNode.Type.ASSERTION, ModelNode.Type.ASSERTION_PARAMETER_NODE))));
    }

    AssertionData(AssertionData data) {
        this.name = data.name;
        this.value = data.value;
        if (!data.attributes.isEmpty()) {
            this.attributes.putAll(data.attributes);
        }
        this.type = data.type;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public AssertionData m2538clone() throws CloneNotSupportedException {
        return (AssertionData) super.clone();
    }

    public boolean containsAttribute(QName name) {
        boolean zContainsKey;
        synchronized (this.attributes) {
            zContainsKey = this.attributes.containsKey(name);
        }
        return zContainsKey;
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0078  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            if (r0 != r1) goto L7
            r0 = 1
            return r0
        L7:
            r0 = r4
            boolean r0 = r0 instanceof com.sun.xml.internal.ws.policy.sourcemodel.AssertionData
            if (r0 != 0) goto L10
            r0 = 0
            return r0
        L10:
            r0 = 1
            r5 = r0
            r0 = r4
            com.sun.xml.internal.ws.policy.sourcemodel.AssertionData r0 = (com.sun.xml.internal.ws.policy.sourcemodel.AssertionData) r0
            r6 = r0
            r0 = r5
            if (r0 == 0) goto L2d
            r0 = r3
            javax.xml.namespace.QName r0 = r0.name
            r1 = r6
            javax.xml.namespace.QName r1 = r1.name
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L2d
            r0 = 1
            goto L2e
        L2d:
            r0 = 0
        L2e:
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L56
            r0 = r3
            java.lang.String r0 = r0.value
            if (r0 != 0) goto L44
            r0 = r6
            java.lang.String r0 = r0.value
            if (r0 != 0) goto L56
            goto L52
        L44:
            r0 = r3
            java.lang.String r0 = r0.value
            r1 = r6
            java.lang.String r1 = r1.value
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L56
        L52:
            r0 = 1
            goto L57
        L56:
            r0 = 0
        L57:
            r5 = r0
            r0 = r3
            java.util.Map<javax.xml.namespace.QName, java.lang.String> r0 = r0.attributes
            r1 = r0
            r7 = r1
            monitor-enter(r0)
            r0 = r5
            if (r0 == 0) goto L78
            r0 = r3
            java.util.Map<javax.xml.namespace.QName, java.lang.String> r0 = r0.attributes     // Catch: java.lang.Throwable -> L80
            r1 = r6
            java.util.Map<javax.xml.namespace.QName, java.lang.String> r1 = r1.attributes     // Catch: java.lang.Throwable -> L80
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L80
            if (r0 == 0) goto L78
            r0 = 1
            goto L79
        L78:
            r0 = 0
        L79:
            r5 = r0
            r0 = r7
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L80
            goto L88
        L80:
            r8 = move-exception
            r0 = r7
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L80
            r0 = r8
            throw r0
        L88:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.policy.sourcemodel.AssertionData.equals(java.lang.Object):boolean");
    }

    public String getAttributeValue(QName name) {
        String str;
        synchronized (this.attributes) {
            str = this.attributes.get(name);
        }
        return str;
    }

    public Map<QName, String> getAttributes() {
        HashMap map;
        synchronized (this.attributes) {
            map = new HashMap(this.attributes);
        }
        return map;
    }

    public Set<Map.Entry<QName, String>> getAttributesSet() {
        HashSet hashSet;
        synchronized (this.attributes) {
            hashSet = new HashSet(this.attributes.entrySet());
        }
        return hashSet;
    }

    public QName getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public int hashCode() {
        int result;
        int result2 = (37 * 17) + this.name.hashCode();
        int result3 = (37 * result2) + (this.value == null ? 0 : this.value.hashCode());
        synchronized (this.attributes) {
            result = (37 * result3) + this.attributes.hashCode();
        }
        return result;
    }

    public boolean isPrivateAttributeSet() {
        return PolicyConstants.VISIBILITY_VALUE_PRIVATE.equals(getAttributeValue(PolicyConstants.VISIBILITY_ATTRIBUTE));
    }

    public String removeAttribute(QName name) {
        String strRemove;
        synchronized (this.attributes) {
            strRemove = this.attributes.remove(name);
        }
        return strRemove;
    }

    public void setAttribute(QName name, String value) {
        synchronized (this.attributes) {
            this.attributes.put(name, value);
        }
    }

    public void setOptionalAttribute(boolean value) {
        this.optional = value;
    }

    public boolean isOptionalAttributeSet() {
        return this.optional;
    }

    public void setIgnorableAttribute(boolean value) {
        this.ignorable = value;
    }

    public boolean isIgnorableAttributeSet() {
        return this.ignorable;
    }

    public String toString() {
        return toString(0, new StringBuffer()).toString();
    }

    public StringBuffer toString(int indentLevel, StringBuffer buffer) {
        String indent = PolicyUtils.Text.createIndent(indentLevel);
        String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
        String innerDoubleIndent = PolicyUtils.Text.createIndent(indentLevel + 2);
        buffer.append(indent);
        if (this.type == ModelNode.Type.ASSERTION) {
            buffer.append("assertion data {");
        } else {
            buffer.append("assertion parameter data {");
        }
        buffer.append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("namespace = '").append(this.name.getNamespaceURI()).append('\'').append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("prefix = '").append(this.name.getPrefix()).append('\'').append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("local name = '").append(this.name.getLocalPart()).append('\'').append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("value = '").append(this.value).append('\'').append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("optional = '").append(this.optional).append('\'').append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("ignorable = '").append(this.ignorable).append('\'').append(PolicyUtils.Text.NEW_LINE);
        synchronized (this.attributes) {
            if (this.attributes.isEmpty()) {
                buffer.append(innerIndent).append("no attributes");
            } else {
                buffer.append(innerIndent).append("attributes {").append(PolicyUtils.Text.NEW_LINE);
                for (Map.Entry<QName, String> entry : this.attributes.entrySet()) {
                    QName aName = entry.getKey();
                    buffer.append(innerDoubleIndent).append("name = '").append(aName.getNamespaceURI()).append(':').append(aName.getLocalPart());
                    buffer.append("', value = '").append(entry.getValue()).append('\'').append(PolicyUtils.Text.NEW_LINE);
                }
                buffer.append(innerIndent).append('}');
            }
        }
        buffer.append(PolicyUtils.Text.NEW_LINE).append(indent).append('}');
        return buffer;
    }

    public ModelNode.Type getNodeType() {
        return this.type;
    }
}
