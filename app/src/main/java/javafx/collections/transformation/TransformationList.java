package javafx.collections.transformation;

import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;

/* loaded from: jfxrt.jar:javafx/collections/transformation/TransformationList.class */
public abstract class TransformationList<E, F> extends ObservableListBase<E> implements ObservableList<E> {
    private ObservableList<? extends F> source;
    private ListChangeListener<F> sourceListener;

    protected abstract void sourceChanged(ListChangeListener.Change<? extends F> change);

    public abstract int getSourceIndex(int i2);

    protected TransformationList(ObservableList<? extends F> source) {
        if (source == null) {
            throw new NullPointerException();
        }
        this.source = source;
        source.addListener(new WeakListChangeListener(getListener()));
    }

    public final ObservableList<? extends F> getSource() {
        return this.source;
    }

    public final boolean isInTransformationChain(ObservableList<?> list) {
        if (this.source == list) {
            return true;
        }
        List<?> currentSource = this.source;
        while (currentSource instanceof TransformationList) {
            currentSource = ((TransformationList) currentSource).source;
            if (currentSource == list) {
                return true;
            }
        }
        return false;
    }

    private ListChangeListener<F> getListener() {
        if (this.sourceListener == null) {
            this.sourceListener = c2 -> {
                sourceChanged(c2);
            };
        }
        return this.sourceListener;
    }

    public final int getSourceIndexFor(ObservableList<?> list, int index) {
        if (!isInTransformationChain(list)) {
            throw new IllegalArgumentException("Provided list is not in the transformation chain of thistransformation list");
        }
        List<?> currentSource = this.source;
        int idx = getSourceIndex(index);
        while (currentSource != list && (currentSource instanceof TransformationList)) {
            TransformationList tSource = (TransformationList) currentSource;
            idx = tSource.getSourceIndex(idx);
            currentSource = tSource.source;
        }
        return idx;
    }
}
