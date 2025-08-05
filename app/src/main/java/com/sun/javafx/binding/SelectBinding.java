package com.sun.javafx.binding;

import com.sun.javafx.property.JavaBeanAccessHelper;
import com.sun.javafx.property.PropertyReference;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding.class */
public class SelectBinding {
    private SelectBinding() {
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsObject.class */
    public static class AsObject<T> extends ObjectBinding<T> {
        private final SelectBindingHelper helper;

        public AsObject(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsObject(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.ObjectBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.ObjectBinding
        protected T computeValue() {
            ObservableValue<?> observableValue = this.helper.getObservableValue();
            if (observableValue == null) {
                return null;
            }
            try {
                return (T) observableValue.getValue2();
            } catch (ClassCastException e2) {
                Logging.getLogger().warning("Value of select-binding has wrong type, returning null.", e2);
                return null;
            }
        }

        @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsBoolean.class */
    public static class AsBoolean extends BooleanBinding {
        private static final boolean DEFAULT_VALUE = false;
        private final SelectBindingHelper helper;

        public AsBoolean(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsBoolean(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.BooleanBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.BooleanBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.BooleanBinding
        protected boolean computeValue() {
            ObservableValue<?> observable = this.helper.getObservableValue();
            if (observable == null) {
                return false;
            }
            if (observable instanceof ObservableBooleanValue) {
                return ((ObservableBooleanValue) observable).get();
            }
            try {
                return ((Boolean) observable.getValue2()).booleanValue();
            } catch (ClassCastException ex) {
                Logging.getLogger().warning("Value of select-binding has wrong type, returning default value.", ex);
                return false;
            } catch (NullPointerException ex2) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", ex2);
                return false;
            }
        }

        @Override // javafx.beans.binding.BooleanBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsDouble.class */
    public static class AsDouble extends DoubleBinding {
        private static final double DEFAULT_VALUE = 0.0d;
        private final SelectBindingHelper helper;

        public AsDouble(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsDouble(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.DoubleBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.DoubleBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.DoubleBinding
        protected double computeValue() {
            ObservableValue<?> observable = this.helper.getObservableValue();
            if (observable == null) {
                return 0.0d;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue) observable).doubleValue();
            }
            try {
                return ((Number) observable.getValue2()).doubleValue();
            } catch (ClassCastException ex) {
                Logging.getLogger().warning("Exception while evaluating select-binding", ex);
                return 0.0d;
            } catch (NullPointerException ex2) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", ex2);
                return 0.0d;
            }
        }

        @Override // javafx.beans.binding.DoubleBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsFloat.class */
    public static class AsFloat extends FloatBinding {
        private static final float DEFAULT_VALUE = 0.0f;
        private final SelectBindingHelper helper;

        public AsFloat(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsFloat(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.FloatBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.FloatBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.FloatBinding
        protected float computeValue() {
            ObservableValue<?> observable = this.helper.getObservableValue();
            if (observable == null) {
                return 0.0f;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue) observable).floatValue();
            }
            try {
                return ((Number) observable.getValue2()).floatValue();
            } catch (ClassCastException ex) {
                Logging.getLogger().warning("Exception while evaluating select-binding", ex);
                return 0.0f;
            } catch (NullPointerException ex2) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", ex2);
                return 0.0f;
            }
        }

        @Override // javafx.beans.binding.FloatBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsInteger.class */
    public static class AsInteger extends IntegerBinding {
        private static final int DEFAULT_VALUE = 0;
        private final SelectBindingHelper helper;

        public AsInteger(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsInteger(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.IntegerBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.IntegerBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.IntegerBinding
        protected int computeValue() {
            ObservableValue<?> observable = this.helper.getObservableValue();
            if (observable == null) {
                return 0;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue) observable).intValue();
            }
            try {
                return ((Number) observable.getValue2()).intValue();
            } catch (ClassCastException ex) {
                Logging.getLogger().warning("Exception while evaluating select-binding", ex);
                return 0;
            } catch (NullPointerException ex2) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", ex2);
                return 0;
            }
        }

        @Override // javafx.beans.binding.IntegerBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsLong.class */
    public static class AsLong extends LongBinding {
        private static final long DEFAULT_VALUE = 0;
        private final SelectBindingHelper helper;

        public AsLong(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsLong(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.LongBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.LongBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.LongBinding
        protected long computeValue() {
            ObservableValue<?> observable = this.helper.getObservableValue();
            if (observable == null) {
                return 0L;
            }
            if (observable instanceof ObservableNumberValue) {
                return ((ObservableNumberValue) observable).longValue();
            }
            try {
                return ((Number) observable.getValue2()).longValue();
            } catch (ClassCastException ex) {
                Logging.getLogger().warning("Exception while evaluating select-binding", ex);
                return 0L;
            } catch (NullPointerException ex2) {
                Logging.getLogger().fine("Value of select binding is null, returning default value", ex2);
                return 0L;
            }
        }

        @Override // javafx.beans.binding.LongBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$AsString.class */
    public static class AsString extends StringBinding {
        private static final String DEFAULT_VALUE = null;
        private final SelectBindingHelper helper;

        public AsString(ObservableValue<?> root, String... steps) {
            this.helper = new SelectBindingHelper((Binding) this, (ObservableValue) root, steps);
        }

        public AsString(Object root, String... steps) {
            this.helper = new SelectBindingHelper(this, root, steps);
        }

        @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
        public void dispose() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.StringBinding
        protected void onInvalidating() {
            this.helper.unregisterListener();
        }

        @Override // javafx.beans.binding.StringBinding
        protected String computeValue() {
            ObservableValue<?> observable = this.helper.getObservableValue();
            if (observable == null) {
                return DEFAULT_VALUE;
            }
            try {
                return observable.getValue2().toString();
            } catch (RuntimeException ex) {
                Logging.getLogger().warning("Exception while evaluating select-binding", ex);
                return DEFAULT_VALUE;
            }
        }

        @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
        public ObservableList<ObservableValue<?>> getDependencies() {
            return this.helper.getDependencies();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SelectBinding$SelectBindingHelper.class */
    private static class SelectBindingHelper implements InvalidationListener {
        private final Binding<?> binding;
        private final String[] propertyNames;
        private final ObservableValue<?>[] properties;
        private final PropertyReference<?>[] propRefs;
        private final WeakInvalidationListener observer;
        private ObservableList<ObservableValue<?>> dependencies;

        private SelectBindingHelper(Binding<?> binding, ObservableValue<?> firstProperty, String... steps) {
            if (firstProperty == null) {
                throw new NullPointerException("Must specify the root");
            }
            steps = steps == null ? new String[0] : steps;
            this.binding = binding;
            int n2 = steps.length;
            for (String str : steps) {
                if (str == null) {
                    throw new NullPointerException("all steps must be specified");
                }
            }
            this.observer = new WeakInvalidationListener(this);
            this.propertyNames = new String[n2];
            System.arraycopy(steps, 0, this.propertyNames, 0, n2);
            this.propRefs = new PropertyReference[n2];
            this.properties = new ObservableValue[n2 + 1];
            this.properties[0] = firstProperty;
            this.properties[0].addListener(this.observer);
        }

        private static ObservableValue<?> checkAndCreateFirstStep(Object root, String[] steps) {
            if (root == null || steps == null || steps[0] == null) {
                throw new NullPointerException("Must specify the root and the first property");
            }
            try {
                return JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(root, steps[0]);
            } catch (NoSuchMethodException e2) {
                throw new IllegalArgumentException("The first property '" + steps[0] + "' doesn't exist");
            }
        }

        private SelectBindingHelper(Binding<?> binding, Object root, String... steps) {
            this(binding, checkAndCreateFirstStep(root, steps), (String[]) Arrays.copyOfRange(steps, 1, steps.length));
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            this.binding.invalidate();
        }

        public ObservableValue<?> getObservableValue() {
            int n2 = this.properties.length;
            for (int i2 = 0; i2 < n2 - 1; i2++) {
                Object obj = this.properties[i2].getValue2();
                try {
                    if (this.propRefs[i2] == null || !obj.getClass().equals(this.propRefs[i2].getContainingClass())) {
                        this.propRefs[i2] = new PropertyReference<>(obj.getClass(), this.propertyNames[i2]);
                    }
                    if (this.propRefs[i2].hasProperty()) {
                        this.properties[i2 + 1] = this.propRefs[i2].getProperty(obj);
                    } else {
                        this.properties[i2 + 1] = JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(obj, this.propRefs[i2].getName());
                    }
                    this.properties[i2 + 1].addListener(this.observer);
                } catch (NoSuchMethodException ex) {
                    Logging.getLogger().warning("Exception while evaluating select-binding " + stepsToString(), ex);
                    updateDependencies();
                    return null;
                } catch (RuntimeException ex2) {
                    PlatformLogger logger = Logging.getLogger();
                    if (logger.isLoggable(PlatformLogger.Level.WARNING)) {
                        Logging.getLogger().warning("Exception while evaluating select-binding " + stepsToString());
                        if (ex2 instanceof IllegalStateException) {
                            logger.warning("Property '" + this.propertyNames[i2] + "' does not exist in " + ((Object) obj.getClass()), ex2);
                        } else if (ex2 instanceof NullPointerException) {
                            logger.fine("Property '" + this.propertyNames[i2] + "' in " + ((Object) this.properties[i2]) + " is null", ex2);
                        } else {
                            Logging.getLogger().warning("", ex2);
                        }
                    }
                    updateDependencies();
                    return null;
                }
            }
            updateDependencies();
            ObservableValue<?> result = this.properties[n2 - 1];
            if (result == null) {
                Logging.getLogger().fine("Property '" + this.propertyNames[n2 - 1] + "' in " + ((Object) this.properties[n2 - 1]) + " is null", new NullPointerException());
            }
            return result;
        }

        private String stepsToString() {
            return Arrays.toString(this.propertyNames);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unregisterListener() {
            int n2 = this.properties.length;
            for (int i2 = 1; i2 < n2 && this.properties[i2] != null; i2++) {
                this.properties[i2].removeListener(this.observer);
                this.properties[i2] = null;
            }
            updateDependencies();
        }

        private void updateDependencies() {
            if (this.dependencies != null) {
                this.dependencies.clear();
                int n2 = this.properties.length;
                for (int i2 = 0; i2 < n2 && this.properties[i2] != null; i2++) {
                    this.dependencies.add(this.properties[i2]);
                }
            }
        }

        public ObservableList<ObservableValue<?>> getDependencies() {
            if (this.dependencies == null) {
                this.dependencies = FXCollections.observableArrayList();
                updateDependencies();
            }
            return FXCollections.unmodifiableObservableList(this.dependencies);
        }
    }
}
