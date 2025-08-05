package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/ModelNode.class */
public final class ModelNode implements Iterable<ModelNode>, Cloneable {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) ModelNode.class);
    private LinkedList<ModelNode> children;
    private Collection<ModelNode> unmodifiableViewOnContent;
    private final Type type;
    private ModelNode parentNode;
    private PolicySourceModel parentModel;
    private PolicyReferenceData referenceData;
    private PolicySourceModel referencedModel;
    private AssertionData nodeData;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/ModelNode$Type.class */
    public enum Type {
        POLICY(XmlToken.Policy),
        ALL(XmlToken.All),
        EXACTLY_ONE(XmlToken.ExactlyOne),
        POLICY_REFERENCE(XmlToken.PolicyReference),
        ASSERTION(XmlToken.UNKNOWN),
        ASSERTION_PARAMETER_NODE(XmlToken.UNKNOWN);

        private XmlToken token;

        Type(XmlToken token) {
            this.token = token;
        }

        public XmlToken getXmlToken() {
            return this.token;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isChildTypeSupported(Type childType) {
            switch (this) {
                case ASSERTION_PARAMETER_NODE:
                    switch (childType) {
                        case ASSERTION_PARAMETER_NODE:
                            return true;
                        default:
                            return false;
                    }
                case POLICY:
                case ALL:
                case EXACTLY_ONE:
                    switch (childType) {
                        case ASSERTION_PARAMETER_NODE:
                            return false;
                        default:
                            return true;
                    }
                case POLICY_REFERENCE:
                    return false;
                case ASSERTION:
                    switch (childType) {
                        case ASSERTION_PARAMETER_NODE:
                        case POLICY:
                        case POLICY_REFERENCE:
                            return true;
                        default:
                            return false;
                    }
                default:
                    throw ((IllegalStateException) ModelNode.LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0060_POLICY_ELEMENT_TYPE_UNKNOWN(this))));
            }
        }
    }

    static ModelNode createRootPolicyNode(PolicySourceModel model) throws IllegalArgumentException {
        if (model == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0039_POLICY_SRC_MODEL_INPUT_PARAMETER_MUST_NOT_BE_NULL())));
        }
        return new ModelNode(Type.POLICY, model);
    }

    private ModelNode(Type type, PolicySourceModel parentModel) {
        this.type = type;
        this.parentModel = parentModel;
        this.children = new LinkedList<>();
        this.unmodifiableViewOnContent = Collections.unmodifiableCollection(this.children);
    }

    private ModelNode(Type type, PolicySourceModel parentModel, AssertionData data) {
        this(type, parentModel);
        this.nodeData = data;
    }

    private ModelNode(PolicySourceModel parentModel, PolicyReferenceData data) {
        this(Type.POLICY_REFERENCE, parentModel);
        this.referenceData = data;
    }

    private void checkCreateChildOperationSupportForType(Type type) throws UnsupportedOperationException {
        if (!this.type.isChildTypeSupported(type)) {
            throw ((UnsupportedOperationException) LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0073_CREATE_CHILD_NODE_OPERATION_NOT_SUPPORTED(type, this.type))));
        }
    }

    public ModelNode createChildPolicyNode() throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.POLICY);
        ModelNode node = new ModelNode(Type.POLICY, this.parentModel);
        addChild(node);
        return node;
    }

    public ModelNode createChildAllNode() throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.ALL);
        ModelNode node = new ModelNode(Type.ALL, this.parentModel);
        addChild(node);
        return node;
    }

    public ModelNode createChildExactlyOneNode() throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.EXACTLY_ONE);
        ModelNode node = new ModelNode(Type.EXACTLY_ONE, this.parentModel);
        addChild(node);
        return node;
    }

    public ModelNode createChildAssertionNode() throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.ASSERTION);
        ModelNode node = new ModelNode(Type.ASSERTION, this.parentModel);
        addChild(node);
        return node;
    }

    public ModelNode createChildAssertionNode(AssertionData nodeData) throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.ASSERTION);
        ModelNode node = new ModelNode(Type.ASSERTION, this.parentModel, nodeData);
        addChild(node);
        return node;
    }

    public ModelNode createChildAssertionParameterNode() throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.ASSERTION_PARAMETER_NODE);
        ModelNode node = new ModelNode(Type.ASSERTION_PARAMETER_NODE, this.parentModel);
        addChild(node);
        return node;
    }

    ModelNode createChildAssertionParameterNode(AssertionData nodeData) throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.ASSERTION_PARAMETER_NODE);
        ModelNode node = new ModelNode(Type.ASSERTION_PARAMETER_NODE, this.parentModel, nodeData);
        addChild(node);
        return node;
    }

    ModelNode createChildPolicyReferenceNode(PolicyReferenceData referenceData) throws UnsupportedOperationException {
        checkCreateChildOperationSupportForType(Type.POLICY_REFERENCE);
        ModelNode node = new ModelNode(this.parentModel, referenceData);
        this.parentModel.addNewPolicyReference(node);
        addChild(node);
        return node;
    }

    Collection<ModelNode> getChildren() {
        return this.unmodifiableViewOnContent;
    }

    void setParentModel(PolicySourceModel model) throws IllegalAccessException {
        if (this.parentNode != null) {
            throw ((IllegalAccessException) LOGGER.logSevereException(new IllegalAccessException(LocalizationMessages.WSP_0049_PARENT_MODEL_CAN_NOT_BE_CHANGED())));
        }
        updateParentModelReference(model);
    }

    private void updateParentModelReference(PolicySourceModel model) {
        this.parentModel = model;
        Iterator<ModelNode> it = this.children.iterator();
        while (it.hasNext()) {
            ModelNode child = it.next();
            child.updateParentModelReference(model);
        }
    }

    public PolicySourceModel getParentModel() {
        return this.parentModel;
    }

    public Type getType() {
        return this.type;
    }

    public ModelNode getParentNode() {
        return this.parentNode;
    }

    public AssertionData getNodeData() {
        return this.nodeData;
    }

    PolicyReferenceData getPolicyReferenceData() {
        return this.referenceData;
    }

    public AssertionData setOrReplaceNodeData(AssertionData newData) {
        if (!isDomainSpecific()) {
            throw ((UnsupportedOperationException) LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0051_OPERATION_NOT_SUPPORTED_FOR_THIS_BUT_ASSERTION_RELATED_NODE_TYPE(this.type))));
        }
        AssertionData oldData = this.nodeData;
        this.nodeData = newData;
        return oldData;
    }

    boolean isDomainSpecific() {
        return this.type == Type.ASSERTION || this.type == Type.ASSERTION_PARAMETER_NODE;
    }

    private boolean addChild(ModelNode child) {
        this.children.add(child);
        child.parentNode = this;
        return true;
    }

    void setReferencedModel(PolicySourceModel model) {
        if (this.type != Type.POLICY_REFERENCE) {
            throw ((UnsupportedOperationException) LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0050_OPERATION_NOT_SUPPORTED_FOR_THIS_BUT_POLICY_REFERENCE_NODE_TYPE(this.type))));
        }
        this.referencedModel = model;
    }

    PolicySourceModel getReferencedModel() {
        return this.referencedModel;
    }

    public int childrenSize() {
        return this.children.size();
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<ModelNode> iterator() {
        return this.children.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ModelNode)) {
            return false;
        }
        ModelNode that = (ModelNode) obj;
        boolean result = 1 != 0 && this.type.equals(that.type);
        boolean result2 = result && (this.nodeData != null ? this.nodeData.equals(that.nodeData) : that.nodeData == null);
        boolean result3 = result2 && (this.children != null ? this.children.equals(that.children) : that.children == null);
        return result3;
    }

    public int hashCode() {
        int result = (37 * 17) + this.type.hashCode();
        return (37 * ((37 * ((37 * result) + (this.parentNode == null ? 0 : this.parentNode.hashCode()))) + (this.nodeData == null ? 0 : this.nodeData.hashCode()))) + this.children.hashCode();
    }

    public String toString() {
        return toString(0, new StringBuffer()).toString();
    }

    public StringBuffer toString(int indentLevel, StringBuffer buffer) {
        String indent = PolicyUtils.Text.createIndent(indentLevel);
        String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
        buffer.append(indent).append((Object) this.type).append(" {").append(PolicyUtils.Text.NEW_LINE);
        if (this.type == Type.ASSERTION) {
            if (this.nodeData == null) {
                buffer.append(innerIndent).append("no assertion data set");
            } else {
                this.nodeData.toString(indentLevel + 1, buffer);
            }
            buffer.append(PolicyUtils.Text.NEW_LINE);
        } else if (this.type == Type.POLICY_REFERENCE) {
            if (this.referenceData == null) {
                buffer.append(innerIndent).append("no policy reference data set");
            } else {
                this.referenceData.toString(indentLevel + 1, buffer);
            }
            buffer.append(PolicyUtils.Text.NEW_LINE);
        } else if (this.type == Type.ASSERTION_PARAMETER_NODE) {
            if (this.nodeData == null) {
                buffer.append(innerIndent).append("no parameter data set");
            } else {
                this.nodeData.toString(indentLevel + 1, buffer);
            }
            buffer.append(PolicyUtils.Text.NEW_LINE);
        }
        if (this.children.size() > 0) {
            Iterator<ModelNode> it = this.children.iterator();
            while (it.hasNext()) {
                ModelNode child = it.next();
                child.toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
            }
        } else {
            buffer.append(innerIndent).append("no child nodes").append(PolicyUtils.Text.NEW_LINE);
        }
        buffer.append(indent).append('}');
        return buffer;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ModelNode m2541clone() throws CloneNotSupportedException {
        ModelNode clone = (ModelNode) super.clone();
        if (this.nodeData != null) {
            clone.nodeData = this.nodeData.m2538clone();
        }
        if (this.referencedModel != null) {
            clone.referencedModel = this.referencedModel.m2553clone();
        }
        clone.children = new LinkedList<>();
        clone.unmodifiableViewOnContent = Collections.unmodifiableCollection(clone.children);
        Iterator<ModelNode> it = this.children.iterator();
        while (it.hasNext()) {
            ModelNode thisChild = it.next();
            clone.addChild(thisChild.m2541clone());
        }
        return clone;
    }

    PolicyReferenceData getReferenceData() {
        return this.referenceData;
    }
}
