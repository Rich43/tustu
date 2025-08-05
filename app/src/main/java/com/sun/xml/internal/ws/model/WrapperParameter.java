package com.sun.xml.internal.ws.model;

import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.spi.db.TypeInfo;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebParam;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/WrapperParameter.class */
public class WrapperParameter extends ParameterImpl {
    protected final List<ParameterImpl> wrapperChildren;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WrapperParameter.class.desiredAssertionStatus();
    }

    public WrapperParameter(JavaMethodImpl parent, TypeInfo typeRef, WebParam.Mode mode, int index) {
        super(parent, typeRef, mode, index);
        this.wrapperChildren = new ArrayList();
        typeRef.properties().put(WrapperParameter.class.getName(), this);
    }

    @Override // com.sun.xml.internal.ws.model.ParameterImpl, com.sun.xml.internal.ws.api.model.Parameter
    public boolean isWrapperStyle() {
        return true;
    }

    public List<ParameterImpl> getWrapperChildren() {
        return this.wrapperChildren;
    }

    public void addWrapperChild(ParameterImpl wrapperChild) {
        this.wrapperChildren.add(wrapperChild);
        wrapperChild.wrapper = this;
        if (!$assertionsDisabled && wrapperChild.getBinding() != ParameterBinding.BODY) {
            throw new AssertionError();
        }
    }

    public void clear() {
        this.wrapperChildren.clear();
    }

    @Override // com.sun.xml.internal.ws.model.ParameterImpl
    void fillTypes(List<TypeInfo> types) {
        super.fillTypes(types);
        if (WrapperComposite.class.equals(getTypeInfo().type)) {
            for (ParameterImpl p2 : this.wrapperChildren) {
                p2.fillTypes(types);
            }
        }
    }
}
