package javafx.scene;

import java.security.AccessController;

/* loaded from: jfxrt.jar:javafx/scene/PropertyHelper.class */
class PropertyHelper {
    PropertyHelper() {
    }

    static boolean getBooleanProperty(String propName) {
        try {
            boolean answer = ((Boolean) AccessController.doPrivileged(() -> {
                String propVal = System.getProperty(propName);
                return Boolean.valueOf("true".equals(propVal.toLowerCase()));
            })).booleanValue();
            return answer;
        } catch (Exception e2) {
            return false;
        }
    }
}
