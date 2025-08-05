package javafx.embed.swt;

import javafx.embed.swt.CustomTransferBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxswt.jar:javafx/embed/swt/CustomTransferBuilder.class */
public class CustomTransferBuilder<B extends CustomTransferBuilder<B>> implements Builder<CustomTransfer> {
    private String mime;
    private String name;

    protected CustomTransferBuilder() {
    }

    public static CustomTransferBuilder<?> create() {
        return new CustomTransferBuilder<>();
    }

    public B mime(String x2) {
        this.mime = x2;
        return this;
    }

    public B name(String x2) {
        this.name = x2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public CustomTransfer build() {
        CustomTransfer x2 = new CustomTransfer(this.name, this.mime);
        return x2;
    }
}
