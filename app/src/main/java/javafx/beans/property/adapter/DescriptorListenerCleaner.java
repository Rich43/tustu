package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.ref.WeakReference;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/DescriptorListenerCleaner.class */
class DescriptorListenerCleaner implements Runnable {
    private final ReadOnlyPropertyDescriptor pd;
    private final WeakReference<ReadOnlyPropertyDescriptor.ReadOnlyListener<?>> lRef;

    DescriptorListenerCleaner(ReadOnlyPropertyDescriptor pd, ReadOnlyPropertyDescriptor.ReadOnlyListener<?> l2) {
        this.pd = pd;
        this.lRef = new WeakReference<>(l2);
    }

    @Override // java.lang.Runnable
    public void run() throws IllegalArgumentException {
        ReadOnlyPropertyDescriptor.ReadOnlyListener<?> l2 = this.lRef.get();
        if (l2 != null) {
            this.pd.removeListener(l2);
        }
    }
}
