package java.beans;

/* loaded from: rt.jar:java/beans/PropertyEditorManager.class */
public class PropertyEditorManager {
    public static void registerEditor(Class<?> cls, Class<?> cls2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        ThreadGroupContext.getContext().getPropertyEditorFinder().register(cls, cls2);
    }

    public static PropertyEditor findEditor(Class<?> cls) {
        return ThreadGroupContext.getContext().getPropertyEditorFinder().find(cls);
    }

    public static String[] getEditorSearchPath() {
        return ThreadGroupContext.getContext().getPropertyEditorFinder().getPackages();
    }

    public static void setEditorSearchPath(String[] strArr) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        ThreadGroupContext.getContext().getPropertyEditorFinder().setPackages(strArr);
    }
}
