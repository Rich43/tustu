package com.sun.org.glassfish.gmbal;

import com.sun.org.glassfish.external.amx.AMX;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/AMXMetadata.class */
public @interface AMXMetadata {
    @DescriptorKey(AMX.DESC_IS_SINGLETON)
    boolean isSingleton() default false;

    @DescriptorKey(AMX.DESC_GROUP)
    String group() default "other";

    @DescriptorKey(AMX.DESC_SUB_TYPES)
    String[] subTypes() default {};

    @DescriptorKey(AMX.DESC_GENERIC_INTERFACE_NAME)
    String genericInterfaceName() default "com.sun.org.glassfish.admin.amx.core.AMXProxy";

    @DescriptorKey("immutableInfo")
    boolean immutableInfo() default true;

    @DescriptorKey(AMX.DESC_STD_INTERFACE_NAME)
    String interfaceClassName() default "";

    @DescriptorKey("type")
    String type() default "";
}
