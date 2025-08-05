package javax.tools;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.tools.JavaFileManager;

/* loaded from: rt.jar:javax/tools/StandardLocation.class */
public enum StandardLocation implements JavaFileManager.Location {
    CLASS_OUTPUT,
    SOURCE_OUTPUT,
    CLASS_PATH,
    SOURCE_PATH,
    ANNOTATION_PROCESSOR_PATH,
    PLATFORM_CLASS_PATH,
    NATIVE_HEADER_OUTPUT;

    private static final ConcurrentMap<String, JavaFileManager.Location> locations = new ConcurrentHashMap();

    public static JavaFileManager.Location locationFor(final String str) {
        if (locations.isEmpty()) {
            for (StandardLocation standardLocation : values()) {
                locations.putIfAbsent(standardLocation.getName(), standardLocation);
            }
        }
        locations.putIfAbsent(str.toString(), new JavaFileManager.Location() { // from class: javax.tools.StandardLocation.1
            @Override // javax.tools.JavaFileManager.Location
            public String getName() {
                return str;
            }

            @Override // javax.tools.JavaFileManager.Location
            public boolean isOutputLocation() {
                return str.endsWith("_OUTPUT");
            }
        });
        return locations.get(str);
    }

    @Override // javax.tools.JavaFileManager.Location
    public String getName() {
        return name();
    }

    @Override // javax.tools.JavaFileManager.Location
    public boolean isOutputLocation() {
        switch (this) {
            case CLASS_OUTPUT:
            case SOURCE_OUTPUT:
            case NATIVE_HEADER_OUTPUT:
                return true;
            default:
                return false;
        }
    }
}
