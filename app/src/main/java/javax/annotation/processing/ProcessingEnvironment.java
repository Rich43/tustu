package javax.annotation.processing;

import java.util.Locale;
import java.util.Map;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/* loaded from: rt.jar:javax/annotation/processing/ProcessingEnvironment.class */
public interface ProcessingEnvironment {
    Map<String, String> getOptions();

    Messager getMessager();

    Filer getFiler();

    Elements getElementUtils();

    Types getTypeUtils();

    SourceVersion getSourceVersion();

    Locale getLocale();
}
