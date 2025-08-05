package org.omg.CosNaming;

/* loaded from: rt.jar:org/omg/CosNaming/BindingIteratorOperations.class */
public interface BindingIteratorOperations {
    boolean next_one(BindingHolder bindingHolder);

    boolean next_n(int i2, BindingListHolder bindingListHolder);

    void destroy();
}
