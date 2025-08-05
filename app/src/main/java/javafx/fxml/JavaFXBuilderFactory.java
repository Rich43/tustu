package javafx.fxml;

import com.sun.javafx.fxml.builder.JavaFXFontBuilder;
import com.sun.javafx.fxml.builder.JavaFXImageBuilder;
import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import com.sun.javafx.fxml.builder.ProxyBuilder;
import com.sun.javafx.fxml.builder.TriangleMeshBuilder;
import com.sun.javafx.fxml.builder.URLBuilder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import sun.reflect.misc.ConstructorUtil;

/* loaded from: jfxrt.jar:javafx/fxml/JavaFXBuilderFactory.class */
public final class JavaFXBuilderFactory implements BuilderFactory {
    private final JavaFXBuilder NO_BUILDER;
    private final Map<Class<?>, JavaFXBuilder> builders;
    private final ClassLoader classLoader;
    private final boolean alwaysUseBuilders;
    private final boolean webSupported;

    public JavaFXBuilderFactory() {
        this(FXMLLoader.getDefaultClassLoader(), false);
    }

    public JavaFXBuilderFactory(boolean alwaysUseBuilders) {
        this(FXMLLoader.getDefaultClassLoader(), alwaysUseBuilders);
    }

    public JavaFXBuilderFactory(ClassLoader classLoader) {
        this(classLoader, false);
    }

    public JavaFXBuilderFactory(ClassLoader classLoader, boolean alwaysUseBuilders) {
        this.NO_BUILDER = new JavaFXBuilder();
        this.builders = new HashMap();
        if (classLoader == null) {
            throw new NullPointerException();
        }
        this.classLoader = classLoader;
        this.alwaysUseBuilders = alwaysUseBuilders;
        this.webSupported = Platform.isSupported(ConditionalFeature.WEB);
    }

    @Override // javafx.util.BuilderFactory
    public Builder<?> getBuilder(Class<?> type) throws Exception {
        Builder<?> builder;
        boolean hasDefaultConstructor;
        if (type == Scene.class) {
            builder = new JavaFXSceneBuilder();
        } else if (type == Font.class) {
            builder = new JavaFXFontBuilder();
        } else if (type == Image.class) {
            builder = new JavaFXImageBuilder();
        } else if (type == URL.class) {
            builder = new URLBuilder(this.classLoader);
        } else if (type == TriangleMesh.class) {
            builder = new TriangleMeshBuilder();
        } else if (scanForConstructorAnnotations(type)) {
            builder = new ProxyBuilder<>(type);
        } else {
            Builder<?> objectBuilder = null;
            JavaFXBuilder typeBuilder = this.builders.get(type);
            if (typeBuilder != this.NO_BUILDER) {
                if (typeBuilder == null) {
                    try {
                        ConstructorUtil.getConstructor(type, new Class[0]);
                    } catch (Exception e2) {
                        hasDefaultConstructor = false;
                    }
                    if (this.alwaysUseBuilders) {
                        throw new Exception();
                    }
                    hasDefaultConstructor = true;
                    if (!hasDefaultConstructor || (this.webSupported && type.getName().equals("javafx.scene.web.WebView"))) {
                        try {
                            typeBuilder = createTypeBuilder(type);
                        } catch (ClassNotFoundException e3) {
                        }
                    }
                    this.builders.put(type, typeBuilder == null ? this.NO_BUILDER : typeBuilder);
                }
                if (typeBuilder != null) {
                    objectBuilder = typeBuilder.createBuilder();
                }
            }
            builder = objectBuilder;
        }
        return builder;
    }

    JavaFXBuilder createTypeBuilder(Class<?> type) throws ClassNotFoundException {
        JavaFXBuilder typeBuilder = null;
        Class<?> builderClass = this.classLoader.loadClass(type.getName() + "Builder");
        try {
            typeBuilder = new JavaFXBuilder(builderClass);
        } catch (Exception ex) {
            Logger.getLogger(JavaFXBuilderFactory.class.getName()).log(Level.WARNING, "Failed to instantiate JavaFXBuilder for " + ((Object) builderClass), (Throwable) ex);
        }
        if (!this.alwaysUseBuilders) {
            Logger.getLogger(JavaFXBuilderFactory.class.getName()).log(Level.FINER, "class {0} requires a builder.", type);
        }
        return typeBuilder;
    }

    private boolean scanForConstructorAnnotations(Class<?> type) {
        Constructor[] constructors = ConstructorUtil.getConstructors(type);
        for (Constructor constructor : constructors) {
            Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
            for (int i2 = 0; i2 < constructor.getParameterTypes().length; i2++) {
                for (Annotation annotation : paramAnnotations[i2]) {
                    if (annotation instanceof NamedArg) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
