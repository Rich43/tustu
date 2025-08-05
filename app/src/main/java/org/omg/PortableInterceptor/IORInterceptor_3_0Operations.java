package org.omg.PortableInterceptor;

/* loaded from: rt.jar:org/omg/PortableInterceptor/IORInterceptor_3_0Operations.class */
public interface IORInterceptor_3_0Operations extends IORInterceptorOperations {
    void components_established(IORInfo iORInfo);

    void adapter_manager_state_changed(int i2, short s2);

    void adapter_state_changed(ObjectReferenceTemplate[] objectReferenceTemplateArr, short s2);
}
