package com.sun.xml.internal.ws.client.sei;

import com.sun.xml.internal.ws.client.sei.ValueSetter;
import com.sun.xml.internal.ws.model.ParameterImpl;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetterFactory.class */
public abstract class ValueSetterFactory {
    public static final ValueSetterFactory SYNC = new ValueSetterFactory() { // from class: com.sun.xml.internal.ws.client.sei.ValueSetterFactory.1
        @Override // com.sun.xml.internal.ws.client.sei.ValueSetterFactory
        public ValueSetter get(ParameterImpl p2) {
            return ValueSetter.getSync(p2);
        }
    };
    public static final ValueSetterFactory NONE = new ValueSetterFactory() { // from class: com.sun.xml.internal.ws.client.sei.ValueSetterFactory.2
        @Override // com.sun.xml.internal.ws.client.sei.ValueSetterFactory
        public ValueSetter get(ParameterImpl p2) {
            throw new WebServiceException("This shouldn't happen. No response parameters.");
        }
    };
    public static final ValueSetterFactory SINGLE = new ValueSetterFactory() { // from class: com.sun.xml.internal.ws.client.sei.ValueSetterFactory.3
        @Override // com.sun.xml.internal.ws.client.sei.ValueSetterFactory
        public ValueSetter get(ParameterImpl p2) {
            return ValueSetter.SINGLE_VALUE;
        }
    };

    public abstract ValueSetter get(ParameterImpl parameterImpl);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetterFactory$AsyncBeanValueSetterFactory.class */
    public static final class AsyncBeanValueSetterFactory extends ValueSetterFactory {
        private Class asyncBean;

        public AsyncBeanValueSetterFactory(Class asyncBean) {
            this.asyncBean = asyncBean;
        }

        @Override // com.sun.xml.internal.ws.client.sei.ValueSetterFactory
        public ValueSetter get(ParameterImpl p2) {
            return new ValueSetter.AsyncBeanValueSetter(p2, this.asyncBean);
        }
    }
}
