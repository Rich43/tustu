package com.sun.xml.internal.ws.model;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.api.TypeReference;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.Parameter;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.spi.db.RepeatedElementBridge;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.util.List;
import javax.jws.WebParam;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/ParameterImpl.class */
public class ParameterImpl implements Parameter {
    private ParameterBinding binding;
    private ParameterBinding outBinding;
    private String partName;
    private final int index;
    private final WebParam.Mode mode;
    private TypeReference typeReference;
    private TypeInfo typeInfo;
    private QName name;
    private final JavaMethodImpl parent;
    WrapperParameter wrapper;
    TypeInfo itemTypeInfo;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ParameterImpl.class.desiredAssertionStatus();
    }

    public ParameterImpl(JavaMethodImpl parent, TypeInfo type, WebParam.Mode mode, int index) {
        if (!$assertionsDisabled && type == null) {
            throw new AssertionError();
        }
        this.typeInfo = type;
        this.name = type.tagName;
        this.mode = mode;
        this.index = index;
        this.parent = parent;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public AbstractSEIModelImpl getOwner() {
        return this.parent.owner;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public JavaMethod getParent() {
        return this.parent;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public QName getName() {
        return this.name;
    }

    public XMLBridge getXMLBridge() {
        return getOwner().getXMLBridge(this.typeInfo);
    }

    public XMLBridge getInlinedRepeatedElementBridge() {
        XMLBridge xb;
        TypeInfo itemType = getItemType();
        if (itemType == null || (xb = getOwner().getXMLBridge(itemType)) == null) {
            return null;
        }
        return new RepeatedElementBridge(this.typeInfo, xb);
    }

    public TypeInfo getItemType() {
        if (this.itemTypeInfo != null) {
            return this.itemTypeInfo;
        }
        if (this.parent.getBinding().isRpcLit() || this.wrapper == null || !WrapperComposite.class.equals(this.wrapper.getTypeInfo().type) || !getBinding().isBody()) {
            return null;
        }
        this.itemTypeInfo = this.typeInfo.getItemType();
        return this.itemTypeInfo;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public Bridge getBridge() {
        return getOwner().getBridge(this.typeReference);
    }

    protected Bridge getBridge(TypeReference typeRef) {
        return getOwner().getBridge(typeRef);
    }

    public TypeReference getTypeReference() {
        return this.typeReference;
    }

    public TypeInfo getTypeInfo() {
        return this.typeInfo;
    }

    void setTypeReference(TypeReference type) {
        this.typeReference = type;
        this.name = type.tagName;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public WebParam.Mode getMode() {
        return this.mode;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public int getIndex() {
        return this.index;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public boolean isWrapperStyle() {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public boolean isReturnValue() {
        return this.index == -1;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public ParameterBinding getBinding() {
        if (this.binding == null) {
            return ParameterBinding.BODY;
        }
        return this.binding;
    }

    public void setBinding(ParameterBinding binding) {
        this.binding = binding;
    }

    public void setInBinding(ParameterBinding binding) {
        this.binding = binding;
    }

    public void setOutBinding(ParameterBinding binding) {
        this.outBinding = binding;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public ParameterBinding getInBinding() {
        return this.binding;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public ParameterBinding getOutBinding() {
        if (this.outBinding == null) {
            return this.binding;
        }
        return this.outBinding;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public boolean isIN() {
        return this.mode == WebParam.Mode.IN;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public boolean isOUT() {
        return this.mode == WebParam.Mode.OUT;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public boolean isINOUT() {
        return this.mode == WebParam.Mode.INOUT;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public boolean isResponse() {
        return this.index == -1;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public Object getHolderValue(Object obj) {
        if (obj != null && (obj instanceof Holder)) {
            return ((Holder) obj).value;
        }
        return obj;
    }

    @Override // com.sun.xml.internal.ws.api.model.Parameter
    public String getPartName() {
        if (this.partName == null) {
            return this.name.getLocalPart();
        }
        return this.partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    void fillTypes(List<TypeInfo> types) {
        TypeInfo itemType = getItemType();
        types.add(itemType != null ? itemType : getTypeInfo());
    }
}
