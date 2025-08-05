package com.sun.xml.internal.ws.client.sei;

import com.sun.xml.internal.ws.api.message.MessageContextFactory;
import com.sun.xml.internal.ws.client.sei.ValueSetterFactory;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import java.util.Iterator;
import java.util.List;
import javax.jws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/StubAsyncHandler.class */
public class StubAsyncHandler extends StubHandler {
    private final Class asyncBeanClass;

    public StubAsyncHandler(JavaMethodImpl jm, JavaMethodImpl sync, MessageContextFactory mcf) {
        super(sync, mcf);
        List<ParameterImpl> rp = sync.getResponseParameters();
        int size = 0;
        for (ParameterImpl param : rp) {
            if (param.isWrapperStyle()) {
                size += ((WrapperParameter) param).getWrapperChildren().size();
                if (sync.getBinding().getStyle() == SOAPBinding.Style.DOCUMENT) {
                    size += 2;
                }
            } else {
                size++;
            }
        }
        Class tempWrap = null;
        if (size > 1) {
            List<ParameterImpl> rp2 = jm.getResponseParameters();
            Iterator<ParameterImpl> it = rp2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ParameterImpl param2 = it.next();
                if (param2.isWrapperStyle()) {
                    WrapperParameter wrapParam = (WrapperParameter) param2;
                    if (sync.getBinding().getStyle() == SOAPBinding.Style.DOCUMENT) {
                        tempWrap = (Class) wrapParam.getTypeInfo().type;
                        break;
                    }
                    Iterator<ParameterImpl> it2 = wrapParam.getWrapperChildren().iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        ParameterImpl p2 = it2.next();
                        if (p2.getIndex() == -1) {
                            tempWrap = (Class) p2.getTypeInfo().type;
                            break;
                        }
                    }
                    if (tempWrap != null) {
                        break;
                    }
                } else if (param2.getIndex() == -1) {
                    tempWrap = (Class) param2.getTypeInfo().type;
                    break;
                }
            }
        }
        this.asyncBeanClass = tempWrap;
        switch (size) {
            case 0:
                this.responseBuilder = buildResponseBuilder(sync, ValueSetterFactory.NONE);
                break;
            case 1:
                this.responseBuilder = buildResponseBuilder(sync, ValueSetterFactory.SINGLE);
                break;
            default:
                this.responseBuilder = buildResponseBuilder(sync, new ValueSetterFactory.AsyncBeanValueSetterFactory(this.asyncBeanClass));
                break;
        }
    }

    @Override // com.sun.xml.internal.ws.client.sei.StubHandler
    protected void initArgs(Object[] args) throws Exception {
        if (this.asyncBeanClass != null) {
            args[0] = this.asyncBeanClass.newInstance();
        }
    }

    @Override // com.sun.xml.internal.ws.client.sei.StubHandler
    ValueGetterFactory getValueGetterFactory() {
        return ValueGetterFactory.ASYNC;
    }
}
