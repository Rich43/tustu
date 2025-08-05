package org.icepdf.core.util.content;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/content/ContentParserFactory.class */
public class ContentParserFactory {
    private static final Logger logger = Logger.getLogger(ContentParserFactory.class.toString());
    private static ContentParserFactory contentParserFactory;
    private static final String N_CONTENT_PARSER = "org.icepdf.core.util.content.NContentParser";
    private static boolean foundPro;

    static {
        try {
            Class.forName(N_CONTENT_PARSER);
            foundPro = true;
        } catch (ClassNotFoundException e2) {
            logger.log(Level.FINE, "ICEpdf PRO was not found on the class path");
        }
    }

    private ContentParserFactory() {
    }

    public static ContentParserFactory getInstance() {
        if (contentParserFactory == null) {
            contentParserFactory = new ContentParserFactory();
        }
        return contentParserFactory;
    }

    public ContentParser getContentParser(Library library, Resources resources) {
        if (foundPro) {
            try {
                Class<?> contentParserClass = Class.forName(N_CONTENT_PARSER);
                Class[] parserArgs = {Library.class, Resources.class};
                Constructor fontClassConstructor = contentParserClass.getDeclaredConstructor(parserArgs);
                Object[] args = {library, resources};
                return (ContentParser) fontClassConstructor.newInstance(args);
            } catch (Throwable e2) {
                logger.log(Level.FINE, "Could not load font dictionary class", e2);
            }
        }
        return new OContentParser(library, resources);
    }
}
