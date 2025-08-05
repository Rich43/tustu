package com.sun.javafx.fxml;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/LoadListener.class */
public interface LoadListener {
    void readImportProcessingInstruction(String str);

    void readLanguageProcessingInstruction(String str);

    void readComment(String str);

    void beginInstanceDeclarationElement(Class<?> cls);

    void beginUnknownTypeElement(String str);

    void beginIncludeElement();

    void beginReferenceElement();

    void beginCopyElement();

    void beginRootElement();

    void beginPropertyElement(String str, Class<?> cls);

    void beginUnknownStaticPropertyElement(String str);

    void beginScriptElement();

    void beginDefineElement();

    void readInternalAttribute(String str, String str2);

    void readPropertyAttribute(String str, Class<?> cls, String str2);

    void readUnknownStaticPropertyAttribute(String str, String str2);

    void readEventHandlerAttribute(String str, String str2);

    void endElement(Object obj);
}
