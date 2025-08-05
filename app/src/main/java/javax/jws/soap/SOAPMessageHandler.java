package javax.jws.soap;

@Deprecated
/* loaded from: rt.jar:javax/jws/soap/SOAPMessageHandler.class */
public @interface SOAPMessageHandler {
    String name() default "";

    String className();

    InitParam[] initParams() default {};

    String[] roles() default {};

    String[] headers() default {};
}
