package org.icepdf.ri.images;

import java.net.URL;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/images/Images.class */
public class Images {
    public static final String SIZE_LARGE = "_32";
    public static final String SIZE_MEDIUM = "_24";
    public static final String SIZE_SMALL = "_16";

    public static URL get(String name) {
        return Images.class.getResource(name);
    }
}
