package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import com.sun.javafx.scene.text.HitInfo;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.geometry.VerticalDirection;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.swing.text.AbstractDocument;
import org.icepdf.core.util.PdfOps;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextAreaSkin.class */
public class TextAreaSkin extends TextInputControlSkin<TextArea, TextAreaBehavior> {
    private final TextArea textArea;
    private final boolean USE_MULTIPLE_NODES = false;
    private double computedMinWidth;
    private double computedMinHeight;
    private double computedPrefWidth;
    private double computedPrefHeight;
    private double widthForComputedPrefHeight;
    private double characterWidth;
    private double lineHeight;
    private ContentView contentView;
    private Group paragraphNodes;
    private Text promptNode;
    private ObservableBooleanValue usePromptText;
    private ObservableIntegerValue caretPosition;
    private Group selectionHighlightGroup;
    private ScrollPane scrollPane;
    private Bounds oldViewportBounds;
    private VerticalDirection scrollDirection;
    private Path characterBoundingPath;
    private Timeline scrollSelectionTimeline;
    private EventHandler<ActionEvent> scrollSelectionHandler;
    public static final int SCROLL_RATE = 30;
    private double pressX;
    private double pressY;
    private boolean handlePressed;
    double targetCaretX;
    private static final Path tmpCaretPath = new Path();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$002(com.sun.javafx.scene.control.skin.TextAreaSkin r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.computedPrefWidth = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.TextAreaSkin.access$002(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$302(com.sun.javafx.scene.control.skin.TextAreaSkin r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.widthForComputedPrefHeight = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.TextAreaSkin.access$302(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$402(com.sun.javafx.scene.control.skin.TextAreaSkin r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.computedPrefHeight = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.TextAreaSkin.access$402(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$502(com.sun.javafx.scene.control.skin.TextAreaSkin r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.computedMinWidth = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.TextAreaSkin.access$502(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$702(com.sun.javafx.scene.control.skin.TextAreaSkin r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.computedMinHeight = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.TextAreaSkin.access$702(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double");
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected void invalidateMetrics() {
        this.computedMinWidth = Double.NEGATIVE_INFINITY;
        this.computedMinHeight = Double.NEGATIVE_INFINITY;
        this.computedPrefWidth = Double.NEGATIVE_INFINITY;
        this.computedPrefHeight = Double.NEGATIVE_INFINITY;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TextAreaSkin$ContentView.class */
    private class ContentView extends Region {
        private ContentView() {
            getStyleClass().add(AbstractDocument.ContentElementName);
            addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                ((TextAreaBehavior) TextAreaSkin.this.getBehavior()).mousePressed(event);
                event.consume();
            });
            addEventHandler(MouseEvent.MOUSE_RELEASED, event2 -> {
                ((TextAreaBehavior) TextAreaSkin.this.getBehavior()).mouseReleased(event2);
                event2.consume();
            });
            addEventHandler(MouseEvent.MOUSE_DRAGGED, event3 -> {
                ((TextAreaBehavior) TextAreaSkin.this.getBehavior()).mouseDragged(event3);
                event3.consume();
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javafx.scene.Parent
        public ObservableList<Node> getChildren() {
            return super.getChildren();
        }

        @Override // javafx.scene.Node
        public Orientation getContentBias() {
            return Orientation.HORIZONTAL;
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.TextAreaSkin.access$002(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double */
        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            if (TextAreaSkin.this.computedPrefWidth < 0.0d) {
                double prefWidth = 0.0d;
                for (Node node : TextAreaSkin.this.paragraphNodes.getChildren()) {
                    Text paragraphNode = (Text) node;
                    prefWidth = Math.max(prefWidth, Utils.computeTextWidth(paragraphNode.getFont(), paragraphNode.getText(), 0.0d));
                }
                double prefWidth2 = prefWidth + snappedLeftInset() + snappedRightInset();
                Bounds viewPortBounds = TextAreaSkin.this.scrollPane.getViewportBounds();
                TextAreaSkin.access$002(TextAreaSkin.this, Math.max(prefWidth2, viewPortBounds != null ? viewPortBounds.getWidth() : 0.0d));
            }
            return TextAreaSkin.this.computedPrefWidth;
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.TextAreaSkin.access$302(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double */
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.TextAreaSkin.access$402(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double */
        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double wrappingWidth;
            if (width != TextAreaSkin.this.widthForComputedPrefHeight) {
                TextAreaSkin.this.invalidateMetrics();
                TextAreaSkin.access$302(TextAreaSkin.this, width);
            }
            if (TextAreaSkin.this.computedPrefHeight < 0.0d) {
                if (width == -1.0d) {
                    wrappingWidth = 0.0d;
                } else {
                    wrappingWidth = Math.max(width - (snappedLeftInset() + snappedRightInset()), 0.0d);
                }
                double prefHeight = 0.0d;
                for (Node node : TextAreaSkin.this.paragraphNodes.getChildren()) {
                    Text paragraphNode = (Text) node;
                    prefHeight += Utils.computeTextHeight(paragraphNode.getFont(), paragraphNode.getText(), wrappingWidth, paragraphNode.getBoundsType());
                }
                double prefHeight2 = prefHeight + snappedTopInset() + snappedBottomInset();
                Bounds viewPortBounds = TextAreaSkin.this.scrollPane.getViewportBounds();
                TextAreaSkin.access$402(TextAreaSkin.this, Math.max(prefHeight2, viewPortBounds != null ? viewPortBounds.getHeight() : 0.0d));
            }
            return TextAreaSkin.this.computedPrefHeight;
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.TextAreaSkin.access$502(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double */
        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinWidth(double height) {
            if (TextAreaSkin.this.computedMinWidth < 0.0d) {
                double hInsets = snappedLeftInset() + snappedRightInset();
                TextAreaSkin.access$502(TextAreaSkin.this, Math.min(TextAreaSkin.this.characterWidth + hInsets, computePrefWidth(height)));
            }
            return TextAreaSkin.this.computedMinWidth;
        }

        /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.TextAreaSkin.access$702(com.sun.javafx.scene.control.skin.TextAreaSkin, double):double */
        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computeMinHeight(double width) {
            if (TextAreaSkin.this.computedMinHeight < 0.0d) {
                double vInsets = snappedTopInset() + snappedBottomInset();
                TextAreaSkin.access$702(TextAreaSkin.this, Math.min(TextAreaSkin.this.lineHeight + vInsets, computePrefHeight(width)));
            }
            return TextAreaSkin.this.computedMinHeight;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.Parent
        public void layoutChildren() {
            Text paragraphNode;
            Text paragraphNode2;
            TextArea textArea = (TextArea) TextAreaSkin.this.getSkinnable();
            double width = getWidth();
            double topPadding = snappedTopInset();
            double leftPadding = snappedLeftInset();
            double wrappingWidth = Math.max(width - (leftPadding + snappedRightInset()), 0.0d);
            double y2 = topPadding;
            List<Node> paragraphNodesChildren = TextAreaSkin.this.paragraphNodes.getChildren();
            for (int i2 = 0; i2 < paragraphNodesChildren.size(); i2++) {
                Node node = paragraphNodesChildren.get(i2);
                Text paragraphNode3 = (Text) node;
                paragraphNode3.setWrappingWidth(wrappingWidth);
                Bounds bounds = paragraphNode3.getBoundsInLocal();
                paragraphNode3.setLayoutX(leftPadding);
                paragraphNode3.setLayoutY(y2);
                y2 += bounds.getHeight();
            }
            if (TextAreaSkin.this.promptNode != null) {
                TextAreaSkin.this.promptNode.setLayoutX(leftPadding);
                TextAreaSkin.this.promptNode.setLayoutY(topPadding + TextAreaSkin.this.promptNode.getBaselineOffset());
                TextAreaSkin.this.promptNode.setWrappingWidth(wrappingWidth);
            }
            IndexRange selection = textArea.getSelection();
            Bounds oldCaretBounds = TextAreaSkin.this.caretPath.getBoundsInParent();
            TextAreaSkin.this.selectionHighlightGroup.getChildren().clear();
            int caretPos = textArea.getCaretPosition();
            int anchorPos = textArea.getAnchor();
            if (TextInputControlSkin.SHOW_HANDLES) {
                if (selection.getLength() > 0) {
                    TextAreaSkin.this.selectionHandle1.resize(TextAreaSkin.this.selectionHandle1.prefWidth(-1.0d), TextAreaSkin.this.selectionHandle1.prefHeight(-1.0d));
                    TextAreaSkin.this.selectionHandle2.resize(TextAreaSkin.this.selectionHandle2.prefWidth(-1.0d), TextAreaSkin.this.selectionHandle2.prefHeight(-1.0d));
                } else {
                    TextAreaSkin.this.caretHandle.resize(TextAreaSkin.this.caretHandle.prefWidth(-1.0d), TextAreaSkin.this.caretHandle.prefHeight(-1.0d));
                }
                if (selection.getLength() > 0) {
                    int paragraphIndex = paragraphNodesChildren.size();
                    int paragraphOffset = textArea.getLength() + 1;
                    do {
                        paragraphIndex--;
                        paragraphNode2 = (Text) paragraphNodesChildren.get(paragraphIndex);
                        paragraphOffset -= paragraphNode2.getText().length() + 1;
                    } while (anchorPos < paragraphOffset);
                    TextAreaSkin.this.updateTextNodeCaretPos(anchorPos - paragraphOffset);
                    TextAreaSkin.this.caretPath.getElements().clear();
                    TextAreaSkin.this.caretPath.getElements().addAll(paragraphNode2.getImpl_caretShape());
                    TextAreaSkin.this.caretPath.setLayoutX(paragraphNode2.getLayoutX());
                    TextAreaSkin.this.caretPath.setLayoutY(paragraphNode2.getLayoutY());
                    Bounds b2 = TextAreaSkin.this.caretPath.getBoundsInParent();
                    if (caretPos < anchorPos) {
                        TextAreaSkin.this.selectionHandle2.setLayoutX(b2.getMinX() - (TextAreaSkin.this.selectionHandle2.getWidth() / 2.0d));
                        TextAreaSkin.this.selectionHandle2.setLayoutY(b2.getMaxY() - 1.0d);
                    } else {
                        TextAreaSkin.this.selectionHandle1.setLayoutX(b2.getMinX() - (TextAreaSkin.this.selectionHandle1.getWidth() / 2.0d));
                        TextAreaSkin.this.selectionHandle1.setLayoutY((b2.getMinY() - TextAreaSkin.this.selectionHandle1.getHeight()) + 1.0d);
                    }
                }
            }
            int paragraphIndex2 = paragraphNodesChildren.size();
            int paragraphOffset2 = textArea.getLength() + 1;
            do {
                paragraphIndex2--;
                paragraphNode = (Text) paragraphNodesChildren.get(paragraphIndex2);
                paragraphOffset2 -= paragraphNode.getText().length() + 1;
            } while (caretPos < paragraphOffset2);
            TextAreaSkin.this.updateTextNodeCaretPos(caretPos - paragraphOffset2);
            TextAreaSkin.this.caretPath.getElements().clear();
            TextAreaSkin.this.caretPath.getElements().addAll(paragraphNode.getImpl_caretShape());
            TextAreaSkin.this.caretPath.setLayoutX(paragraphNode.getLayoutX());
            paragraphNode.setLayoutX((2.0d * paragraphNode.getLayoutX()) - paragraphNode.getBoundsInParent().getMinX());
            TextAreaSkin.this.caretPath.setLayoutY(paragraphNode.getLayoutY());
            if (oldCaretBounds == null || !oldCaretBounds.equals(TextAreaSkin.this.caretPath.getBoundsInParent())) {
                TextAreaSkin.this.scrollCaretToVisible();
            }
            int start = selection.getStart();
            int end = selection.getEnd();
            int max = paragraphNodesChildren.size();
            for (int i3 = 0; i3 < max; i3++) {
                Text textNode = (Text) paragraphNodesChildren.get(i3);
                int paragraphLength = textNode.getText().length() + 1;
                if (end > start && start < paragraphLength) {
                    textNode.setImpl_selectionStart(start);
                    textNode.setImpl_selectionEnd(Math.min(end, paragraphLength));
                    Path selectionHighlightPath = new Path();
                    selectionHighlightPath.setManaged(false);
                    selectionHighlightPath.setStroke(null);
                    PathElement[] selectionShape = textNode.getImpl_selectionShape();
                    if (selectionShape != null) {
                        selectionHighlightPath.getElements().addAll(selectionShape);
                    }
                    TextAreaSkin.this.selectionHighlightGroup.getChildren().add(selectionHighlightPath);
                    TextAreaSkin.this.selectionHighlightGroup.setVisible(true);
                    selectionHighlightPath.setLayoutX(textNode.getLayoutX());
                    selectionHighlightPath.setLayoutY(textNode.getLayoutY());
                    TextAreaSkin.this.updateHighlightFill();
                } else {
                    textNode.setImpl_selectionStart(-1);
                    textNode.setImpl_selectionEnd(-1);
                    TextAreaSkin.this.selectionHighlightGroup.setVisible(false);
                }
                start = Math.max(0, start - paragraphLength);
                end = Math.max(0, end - paragraphLength);
            }
            if (TextInputControlSkin.SHOW_HANDLES) {
                Bounds b3 = TextAreaSkin.this.caretPath.getBoundsInParent();
                if (selection.getLength() > 0) {
                    if (caretPos < anchorPos) {
                        TextAreaSkin.this.selectionHandle1.setLayoutX(b3.getMinX() - (TextAreaSkin.this.selectionHandle1.getWidth() / 2.0d));
                        TextAreaSkin.this.selectionHandle1.setLayoutY((b3.getMinY() - TextAreaSkin.this.selectionHandle1.getHeight()) + 1.0d);
                    } else {
                        TextAreaSkin.this.selectionHandle2.setLayoutX(b3.getMinX() - (TextAreaSkin.this.selectionHandle2.getWidth() / 2.0d));
                        TextAreaSkin.this.selectionHandle2.setLayoutY(b3.getMaxY() - 1.0d);
                    }
                } else {
                    TextAreaSkin.this.caretHandle.setLayoutX((b3.getMinX() - (TextAreaSkin.this.caretHandle.getWidth() / 2.0d)) + 1.0d);
                    TextAreaSkin.this.caretHandle.setLayoutY(b3.getMaxY());
                }
            }
            if (TextAreaSkin.this.scrollPane.getPrefViewportWidth() == 0.0d || TextAreaSkin.this.scrollPane.getPrefViewportHeight() == 0.0d) {
                TextAreaSkin.this.updatePrefViewportWidth();
                TextAreaSkin.this.updatePrefViewportHeight();
                if ((getParent() != null && TextAreaSkin.this.scrollPane.getPrefViewportWidth() > 0.0d) || TextAreaSkin.this.scrollPane.getPrefViewportHeight() > 0.0d) {
                    getParent().requestLayout();
                }
            }
            Bounds viewportBounds = TextAreaSkin.this.scrollPane.getViewportBounds();
            boolean wasFitToWidth = TextAreaSkin.this.scrollPane.isFitToWidth();
            boolean wasFitToHeight = TextAreaSkin.this.scrollPane.isFitToHeight();
            boolean setFitToWidth = textArea.isWrapText() || computePrefWidth(-1.0d) <= viewportBounds.getWidth();
            boolean setFitToHeight = computePrefHeight(width) <= viewportBounds.getHeight();
            if (wasFitToWidth != setFitToWidth || wasFitToHeight != setFitToHeight) {
                Platform.runLater(() -> {
                    TextAreaSkin.this.scrollPane.setFitToWidth(setFitToWidth);
                    TextAreaSkin.this.scrollPane.setFitToHeight(setFitToHeight);
                });
                getParent().requestLayout();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TextAreaSkin(final TextArea textArea) {
        super(textArea, new TextAreaBehavior(textArea));
        this.USE_MULTIPLE_NODES = false;
        this.computedMinWidth = Double.NEGATIVE_INFINITY;
        this.computedMinHeight = Double.NEGATIVE_INFINITY;
        this.computedPrefWidth = Double.NEGATIVE_INFINITY;
        this.computedPrefHeight = Double.NEGATIVE_INFINITY;
        this.widthForComputedPrefHeight = Double.NEGATIVE_INFINITY;
        this.contentView = new ContentView();
        this.paragraphNodes = new Group();
        this.selectionHighlightGroup = new Group();
        this.scrollDirection = null;
        this.characterBoundingPath = new Path();
        this.scrollSelectionTimeline = new Timeline();
        this.scrollSelectionHandler = event -> {
            switch (this.scrollDirection) {
            }
        };
        this.targetCaretX = -1.0d;
        ((TextAreaBehavior) getBehavior()).setTextAreaSkin(this);
        this.textArea = textArea;
        this.caretPosition = new IntegerBinding() { // from class: com.sun.javafx.scene.control.skin.TextAreaSkin.1
            {
                bind(textArea.caretPositionProperty());
            }

            @Override // javafx.beans.binding.IntegerBinding
            protected int computeValue() {
                return textArea.getCaretPosition();
            }
        };
        this.caretPosition.addListener((observable, oldValue, newValue) -> {
            this.targetCaretX = -1.0d;
            if (newValue.intValue() > oldValue.intValue()) {
                setForwardBias(true);
            }
        });
        forwardBiasProperty().addListener(observable2 -> {
            if (textArea.getWidth() > 0.0d) {
                updateTextNodeCaretPos(textArea.getCaretPosition());
            }
        });
        this.scrollPane = new ScrollPane();
        this.scrollPane.setFitToWidth(textArea.isWrapText());
        this.scrollPane.setContent(this.contentView);
        getChildren().add(this.scrollPane);
        ((TextArea) getSkinnable()).addEventFilter(ScrollEvent.ANY, event2 -> {
            if (event2.isDirect() && this.handlePressed) {
                event2.consume();
            }
        });
        this.selectionHighlightGroup.setManaged(false);
        this.selectionHighlightGroup.setVisible(false);
        this.contentView.getChildren().add(this.selectionHighlightGroup);
        this.paragraphNodes.setManaged(false);
        this.contentView.getChildren().add(this.paragraphNodes);
        this.caretPath.setManaged(false);
        this.caretPath.setStrokeWidth(1.0d);
        this.caretPath.fillProperty().bind(this.textFill);
        this.caretPath.strokeProperty().bind(this.textFill);
        this.caretPath.opacityProperty().bind(new DoubleBinding() { // from class: com.sun.javafx.scene.control.skin.TextAreaSkin.2
            {
                bind(TextAreaSkin.this.caretVisible);
            }

            @Override // javafx.beans.binding.DoubleBinding
            protected double computeValue() {
                return TextAreaSkin.this.caretVisible.get() ? 1.0d : 0.0d;
            }
        });
        this.contentView.getChildren().add(this.caretPath);
        if (SHOW_HANDLES) {
            this.contentView.getChildren().addAll(this.caretHandle, this.selectionHandle1, this.selectionHandle2);
        }
        this.scrollPane.hvalueProperty().addListener((observable3, oldValue2, newValue2) -> {
            ((TextArea) getSkinnable()).setScrollLeft(newValue2.doubleValue() * getScrollLeftMax());
        });
        this.scrollPane.vvalueProperty().addListener((observable4, oldValue3, newValue3) -> {
            ((TextArea) getSkinnable()).setScrollTop(newValue3.doubleValue() * getScrollTopMax());
        });
        this.scrollSelectionTimeline.setCycleCount(-1);
        List<KeyFrame> scrollSelectionFrames = this.scrollSelectionTimeline.getKeyFrames();
        scrollSelectionFrames.clear();
        scrollSelectionFrames.add(new KeyFrame(Duration.millis(350.0d), this.scrollSelectionHandler, new KeyValue[0]));
        for (int i2 = 0; i2 < 1; i2++) {
            CharSequence paragraph = 1 == 1 ? textArea.textProperty().getValueSafe() : textArea.getParagraphs().get(i2);
            addParagraphNode(i2, paragraph.toString());
        }
        textArea.selectionProperty().addListener((observable5, oldValue4, newValue4) -> {
            textArea.requestLayout();
            this.contentView.requestLayout();
        });
        textArea.wrapTextProperty().addListener((observable6, oldValue5, newValue5) -> {
            invalidateMetrics();
            this.scrollPane.setFitToWidth(newValue5.booleanValue());
        });
        textArea.prefColumnCountProperty().addListener((observable7, oldValue6, newValue6) -> {
            invalidateMetrics();
            updatePrefViewportWidth();
        });
        textArea.prefRowCountProperty().addListener((observable8, oldValue7, newValue7) -> {
            invalidateMetrics();
            updatePrefViewportHeight();
        });
        updateFontMetrics();
        this.fontMetrics.addListener(valueModel -> {
            updateFontMetrics();
        });
        this.contentView.paddingProperty().addListener(valueModel2 -> {
            updatePrefViewportWidth();
            updatePrefViewportHeight();
        });
        this.scrollPane.viewportBoundsProperty().addListener(valueModel3 -> {
            if (this.scrollPane.getViewportBounds() != null) {
                Bounds newViewportBounds = this.scrollPane.getViewportBounds();
                if (this.oldViewportBounds == null || this.oldViewportBounds.getWidth() != newViewportBounds.getWidth() || this.oldViewportBounds.getHeight() != newViewportBounds.getHeight()) {
                    invalidateMetrics();
                    this.oldViewportBounds = newViewportBounds;
                    this.contentView.requestLayout();
                }
            }
        });
        textArea.scrollTopProperty().addListener((observable9, oldValue8, newValue8) -> {
            double vValue = newValue8.doubleValue() < getScrollTopMax() ? newValue8.doubleValue() / getScrollTopMax() : 1.0d;
            this.scrollPane.setVvalue(vValue);
        });
        textArea.scrollLeftProperty().addListener((observable10, oldValue9, newValue9) -> {
            double hValue = newValue9.doubleValue() < getScrollLeftMax() ? newValue9.doubleValue() / getScrollLeftMax() : 1.0d;
            this.scrollPane.setHvalue(hValue);
        });
        textArea.textProperty().addListener(observable11 -> {
            invalidateMetrics();
            ((Text) this.paragraphNodes.getChildren().get(0)).setText(textArea.textProperty().getValueSafe());
            this.contentView.requestLayout();
        });
        this.usePromptText = new BooleanBinding() { // from class: com.sun.javafx.scene.control.skin.TextAreaSkin.3
            {
                bind(textArea.textProperty(), textArea.promptTextProperty());
            }

            @Override // javafx.beans.binding.BooleanBinding
            protected boolean computeValue() {
                String txt = textArea.getText();
                String promptTxt = textArea.getPromptText();
                return ((txt != null && !txt.isEmpty()) || promptTxt == null || promptTxt.isEmpty()) ? false : true;
            }
        };
        if (this.usePromptText.get()) {
            createPromptNode();
        }
        this.usePromptText.addListener(observable12 -> {
            createPromptNode();
            textArea.requestLayout();
        });
        updateHighlightFill();
        updatePrefViewportWidth();
        updatePrefViewportHeight();
        if (textArea.isFocused()) {
            setCaretAnimating(true);
        }
        if (SHOW_HANDLES) {
            this.selectionHandle1.setRotate(180.0d);
            EventHandler<MouseEvent> handlePressHandler = e2 -> {
                this.pressX = e2.getX();
                this.pressY = e2.getY();
                this.handlePressed = true;
                e2.consume();
            };
            EventHandler<MouseEvent> handleReleaseHandler = event3 -> {
                this.handlePressed = false;
            };
            this.caretHandle.setOnMousePressed(handlePressHandler);
            this.selectionHandle1.setOnMousePressed(handlePressHandler);
            this.selectionHandle2.setOnMousePressed(handlePressHandler);
            this.caretHandle.setOnMouseReleased(handleReleaseHandler);
            this.selectionHandle1.setOnMouseReleased(handleReleaseHandler);
            this.selectionHandle2.setOnMouseReleased(handleReleaseHandler);
            this.caretHandle.setOnMouseDragged(e3 -> {
                Text textNode = getTextNode();
                Point2D tp = textNode.localToScene(0.0d, 0.0d);
                Point2D p2 = new Point2D((((e3.getSceneX() - tp.getX()) + 10.0d) - this.pressX) + (this.caretHandle.getWidth() / 2.0d), ((e3.getSceneY() - tp.getY()) - this.pressY) - 6.0d);
                HitInfo hit = textNode.impl_hitTestChar(translateCaretPosition(p2));
                int pos = hit.getCharIndex();
                if (pos > 0) {
                    int oldPos = textNode.getImpl_caretPosition();
                    textNode.setImpl_caretPosition(pos);
                    PathElement element = textNode.getImpl_caretShape()[0];
                    if ((element instanceof MoveTo) && ((MoveTo) element).getY() > e3.getY() - getTextTranslateY()) {
                        hit.setCharIndex(pos - 1);
                    }
                    textNode.setImpl_caretPosition(oldPos);
                }
                positionCaret(hit, false, false);
                e3.consume();
            });
            this.selectionHandle1.setOnMouseDragged(new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.TextAreaSkin.4
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.event.EventHandler
                public void handle(MouseEvent e4) {
                    TextArea textArea2 = (TextArea) TextAreaSkin.this.getSkinnable();
                    Text textNode = TextAreaSkin.this.getTextNode();
                    Point2D tp = textNode.localToScene(0.0d, 0.0d);
                    Point2D p2 = new Point2D((((e4.getSceneX() - tp.getX()) + 10.0d) - TextAreaSkin.this.pressX) + (TextAreaSkin.this.selectionHandle1.getWidth() / 2.0d), ((e4.getSceneY() - tp.getY()) - TextAreaSkin.this.pressY) + TextAreaSkin.this.selectionHandle1.getHeight() + 5.0d);
                    HitInfo hit = textNode.impl_hitTestChar(TextAreaSkin.this.translateCaretPosition(p2));
                    int pos = hit.getCharIndex();
                    if (textArea2.getAnchor() < textArea2.getCaretPosition()) {
                        textArea2.selectRange(textArea2.getCaretPosition(), textArea2.getAnchor());
                    }
                    if (pos > 0) {
                        if (pos >= textArea2.getAnchor()) {
                            pos = textArea2.getAnchor();
                        }
                        int oldPos = textNode.getImpl_caretPosition();
                        textNode.setImpl_caretPosition(pos);
                        PathElement element = textNode.getImpl_caretShape()[0];
                        if ((element instanceof MoveTo) && ((MoveTo) element).getY() > e4.getY() - TextAreaSkin.this.getTextTranslateY()) {
                            hit.setCharIndex(pos - 1);
                        }
                        textNode.setImpl_caretPosition(oldPos);
                    }
                    TextAreaSkin.this.positionCaret(hit, true, false);
                    e4.consume();
                }
            });
            this.selectionHandle2.setOnMouseDragged(new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.TextAreaSkin.5
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.event.EventHandler
                public void handle(MouseEvent e4) {
                    TextArea textArea2 = (TextArea) TextAreaSkin.this.getSkinnable();
                    Text textNode = TextAreaSkin.this.getTextNode();
                    Point2D tp = textNode.localToScene(0.0d, 0.0d);
                    Point2D p2 = new Point2D((((e4.getSceneX() - tp.getX()) + 10.0d) - TextAreaSkin.this.pressX) + (TextAreaSkin.this.selectionHandle2.getWidth() / 2.0d), ((e4.getSceneY() - tp.getY()) - TextAreaSkin.this.pressY) - 6.0d);
                    HitInfo hit = textNode.impl_hitTestChar(TextAreaSkin.this.translateCaretPosition(p2));
                    int pos = hit.getCharIndex();
                    if (textArea2.getAnchor() > textArea2.getCaretPosition()) {
                        textArea2.selectRange(textArea2.getCaretPosition(), textArea2.getAnchor());
                    }
                    if (pos > 0) {
                        if (pos <= textArea2.getAnchor() + 1) {
                            pos = Math.min(textArea2.getAnchor() + 2, textArea2.getLength());
                        }
                        int oldPos = textNode.getImpl_caretPosition();
                        textNode.setImpl_caretPosition(pos);
                        PathElement element = textNode.getImpl_caretShape()[0];
                        if ((element instanceof MoveTo) && ((MoveTo) element).getY() > e4.getY() - TextAreaSkin.this.getTextTranslateY()) {
                            hit.setCharIndex(pos - 1);
                        }
                        textNode.setImpl_caretPosition(oldPos);
                        TextAreaSkin.this.positionCaret(hit, true, false);
                    }
                    e4.consume();
                }
            });
        }
    }

    private /* synthetic */ void lambda$new$15(ListChangeListener.Change change) {
        while (change.next()) {
            int from = change.getFrom();
            int to = change.getTo();
            List<? extends CharSequence> removed = change.getRemoved();
            if (from < to) {
                if (removed.isEmpty()) {
                    for (int i2 = from; i2 < to; i2++) {
                        addParagraphNode(i2, ((CharSequence) change.getList().get(i2)).toString());
                    }
                } else {
                    for (int i3 = from; i3 < to; i3++) {
                        Node node = this.paragraphNodes.getChildren().get(i3);
                        Text paragraphNode = (Text) node;
                        paragraphNode.setText(((CharSequence) change.getList().get(i3)).toString());
                    }
                }
            } else {
                this.paragraphNodes.getChildren().subList(from, from + removed.size()).clear();
            }
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        this.scrollPane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void createPromptNode() {
        if (this.promptNode == null && this.usePromptText.get()) {
            this.promptNode = new Text();
            this.contentView.getChildren().add(0, this.promptNode);
            this.promptNode.setManaged(false);
            this.promptNode.getStyleClass().add("text");
            this.promptNode.visibleProperty().bind(this.usePromptText);
            this.promptNode.fontProperty().bind(((TextArea) getSkinnable()).fontProperty());
            this.promptNode.textProperty().bind(((TextArea) getSkinnable()).promptTextProperty());
            this.promptNode.fillProperty().bind(this.promptTextFill);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addParagraphNode(int i2, String string) {
        TextArea textArea = (TextArea) getSkinnable();
        Text paragraphNode = new Text(string);
        paragraphNode.setTextOrigin(VPos.TOP);
        paragraphNode.setManaged(false);
        paragraphNode.getStyleClass().add("text");
        paragraphNode.boundsTypeProperty().addListener((observable, oldValue, newValue) -> {
            invalidateMetrics();
            updateFontMetrics();
        });
        this.paragraphNodes.getChildren().add(i2, paragraphNode);
        paragraphNode.fontProperty().bind(textArea.fontProperty());
        paragraphNode.fillProperty().bind(this.textFill);
        paragraphNode.impl_selectionFillProperty().bind(this.highlightTextFill);
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase, javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        Text firstParagraph = (Text) this.paragraphNodes.getChildren().get(0);
        return Utils.getAscent(((TextArea) getSkinnable()).getFont(), firstParagraph.getBoundsType()) + this.contentView.snappedTopInset() + this.textArea.snappedTopInset();
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public char getCharacter(int index) {
        int n2 = this.paragraphNodes.getChildren().size();
        int offset = index;
        String paragraph = null;
        for (int paragraphIndex = 0; paragraphIndex < n2; paragraphIndex++) {
            Text paragraphNode = (Text) this.paragraphNodes.getChildren().get(paragraphIndex);
            paragraph = paragraphNode.getText();
            int count = paragraph.length() + 1;
            if (offset < count) {
                break;
            }
            offset -= count;
        }
        if (offset == paragraph.length()) {
            return '\n';
        }
        return paragraph.charAt(offset);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public int getInsertionPoint(double x2, double y2) {
        TextArea textArea = (TextArea) getSkinnable();
        int n2 = this.paragraphNodes.getChildren().size();
        int index = -1;
        if (n2 > 0) {
            if (y2 >= this.contentView.snappedTopInset()) {
                if (y2 > this.contentView.snappedTopInset() + this.contentView.getHeight()) {
                    int lastParagraphIndex = n2 - 1;
                    Text lastParagraphView = (Text) this.paragraphNodes.getChildren().get(lastParagraphIndex);
                    index = getNextInsertionPoint(lastParagraphView, x2, -1, VerticalDirection.UP) + (textArea.getLength() - lastParagraphView.getText().length());
                } else {
                    int paragraphOffset = 0;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= n2) {
                            break;
                        }
                        Text paragraphNode = (Text) this.paragraphNodes.getChildren().get(i2);
                        Bounds bounds = paragraphNode.getBoundsInLocal();
                        double paragraphViewY = paragraphNode.getLayoutY() + bounds.getMinY();
                        if (y2 >= paragraphViewY && y2 < paragraphViewY + paragraphNode.getBoundsInLocal().getHeight()) {
                            index = getInsertionPoint(paragraphNode, x2 - paragraphNode.getLayoutX(), y2 - paragraphNode.getLayoutY()) + paragraphOffset;
                            break;
                        }
                        paragraphOffset += paragraphNode.getText().length() + 1;
                        i2++;
                    }
                }
            } else {
                index = getNextInsertionPoint((Text) this.paragraphNodes.getChildren().get(0), x2, -1, VerticalDirection.DOWN);
            }
        }
        return index;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void positionCaret(HitInfo hit, boolean select, boolean extendSelection) {
        int pos = Utils.getHitInsertionIndex(hit, ((TextArea) getSkinnable()).getText());
        boolean isNewLine = pos > 0 && pos <= ((TextArea) getSkinnable()).getLength() && ((TextArea) getSkinnable()).getText().codePointAt(pos - 1) == 10;
        if (!hit.isLeading() && isNewLine) {
            hit.setLeading(true);
            pos--;
        }
        if (select) {
            if (extendSelection) {
                ((TextArea) getSkinnable()).extendSelection(pos);
            } else {
                ((TextArea) getSkinnable()).selectPositionCaret(pos);
            }
        } else {
            ((TextArea) getSkinnable()).positionCaret(pos);
        }
        setForwardBias(hit.isLeading());
    }

    private double getScrollTopMax() {
        return Math.max(0.0d, this.contentView.getHeight() - this.scrollPane.getViewportBounds().getHeight());
    }

    private double getScrollLeftMax() {
        return Math.max(0.0d, this.contentView.getWidth() - this.scrollPane.getViewportBounds().getWidth());
    }

    private int getInsertionPoint(Text paragraphNode, double x2, double y2) {
        HitInfo hitInfo = paragraphNode.impl_hitTestChar(new Point2D(x2, y2));
        return Utils.getHitInsertionIndex(hitInfo, paragraphNode.getText());
    }

    public int getNextInsertionPoint(double x2, int from, VerticalDirection scrollDirection) {
        return 0;
    }

    private int getNextInsertionPoint(Text paragraphNode, double x2, int from, VerticalDirection scrollDirection) {
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public Rectangle2D getCharacterBounds(int index) {
        Text paragraphNode;
        TextArea textArea = (TextArea) getSkinnable();
        int paragraphIndex = this.paragraphNodes.getChildren().size();
        int paragraphOffset = textArea.getLength() + 1;
        do {
            paragraphIndex--;
            paragraphNode = (Text) this.paragraphNodes.getChildren().get(paragraphIndex);
            paragraphOffset -= paragraphNode.getText().length() + 1;
        } while (index < paragraphOffset);
        int characterIndex = index - paragraphOffset;
        boolean terminator = false;
        if (characterIndex == paragraphNode.getText().length()) {
            characterIndex--;
            terminator = true;
        }
        this.characterBoundingPath.getElements().clear();
        this.characterBoundingPath.getElements().addAll(paragraphNode.impl_getRangeShape(characterIndex, characterIndex + 1));
        this.characterBoundingPath.setLayoutX(paragraphNode.getLayoutX());
        this.characterBoundingPath.setLayoutY(paragraphNode.getLayoutY());
        Bounds bounds = this.characterBoundingPath.getBoundsInLocal();
        double x2 = (bounds.getMinX() + paragraphNode.getLayoutX()) - textArea.getScrollLeft();
        double y2 = (bounds.getMinY() + paragraphNode.getLayoutY()) - textArea.getScrollTop();
        double width = bounds.isEmpty() ? 0.0d : bounds.getWidth();
        double height = bounds.isEmpty() ? 0.0d : bounds.getHeight();
        if (terminator) {
            x2 += width;
            width = 0.0d;
        }
        return new Rectangle2D(x2, y2, width, height);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public void scrollCharacterToVisible(int index) {
        Platform.runLater(() -> {
            if (((TextArea) getSkinnable()).getLength() == 0) {
                return;
            }
            Rectangle2D characterBounds = getCharacterBounds(index);
            scrollBoundsToVisible(characterBounds);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void scrollCaretToVisible() {
        TextArea textArea = (TextArea) getSkinnable();
        Bounds bounds = this.caretPath.getLayoutBounds();
        double x2 = bounds.getMinX() - textArea.getScrollLeft();
        double y2 = bounds.getMinY() - textArea.getScrollTop();
        double w2 = bounds.getWidth();
        double h2 = bounds.getHeight();
        if (SHOW_HANDLES) {
            if (this.caretHandle.isVisible()) {
                h2 += this.caretHandle.getHeight();
            } else if (this.selectionHandle1.isVisible() && this.selectionHandle2.isVisible()) {
                x2 -= this.selectionHandle1.getWidth() / 2.0d;
                y2 -= this.selectionHandle1.getHeight();
                w2 += (this.selectionHandle1.getWidth() / 2.0d) + (this.selectionHandle2.getWidth() / 2.0d);
                h2 += this.selectionHandle1.getHeight() + this.selectionHandle2.getHeight();
            }
        }
        if (w2 > 0.0d && h2 > 0.0d) {
            scrollBoundsToVisible(new Rectangle2D(x2, y2, w2, h2));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void scrollBoundsToVisible(Rectangle2D bounds) {
        TextArea textArea = (TextArea) getSkinnable();
        Bounds viewportBounds = this.scrollPane.getViewportBounds();
        double viewportWidth = viewportBounds.getWidth();
        double viewportHeight = viewportBounds.getHeight();
        double scrollTop = textArea.getScrollTop();
        double scrollLeft = textArea.getScrollLeft();
        if (bounds.getMinY() < 0.0d) {
            double y2 = scrollTop + bounds.getMinY();
            if (y2 <= this.contentView.snappedTopInset()) {
                y2 = 0.0d;
            }
            textArea.setScrollTop(y2);
        } else if (this.contentView.snappedTopInset() + bounds.getMaxY() > viewportHeight) {
            double y3 = ((scrollTop + this.contentView.snappedTopInset()) + bounds.getMaxY()) - viewportHeight;
            if (y3 >= getScrollTopMax() - this.contentView.snappedBottomInset()) {
                y3 = getScrollTopMax();
            }
            textArea.setScrollTop(y3);
        }
        if (bounds.getMinX() < 0.0d) {
            double x2 = (scrollLeft + bounds.getMinX()) - 6.0d;
            if (x2 <= this.contentView.snappedLeftInset() + 6.0d) {
                x2 = 0.0d;
            }
            textArea.setScrollLeft(x2);
            return;
        }
        if (this.contentView.snappedLeftInset() + bounds.getMaxX() > viewportWidth) {
            double x3 = (((scrollLeft + this.contentView.snappedLeftInset()) + bounds.getMaxX()) - viewportWidth) + 6.0d;
            if (x3 >= (getScrollLeftMax() - this.contentView.snappedRightInset()) - 6.0d) {
                x3 = getScrollLeftMax();
            }
            textArea.setScrollLeft(x3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void updatePrefViewportWidth() {
        int columnCount = ((TextArea) getSkinnable()).getPrefColumnCount();
        this.scrollPane.setPrefViewportWidth((columnCount * this.characterWidth) + this.contentView.snappedLeftInset() + this.contentView.snappedRightInset());
        this.scrollPane.setMinViewportWidth(this.characterWidth + this.contentView.snappedLeftInset() + this.contentView.snappedRightInset());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void updatePrefViewportHeight() {
        int rowCount = ((TextArea) getSkinnable()).getPrefRowCount();
        this.scrollPane.setPrefViewportHeight((rowCount * this.lineHeight) + this.contentView.snappedTopInset() + this.contentView.snappedBottomInset());
        this.scrollPane.setMinViewportHeight(this.lineHeight + this.contentView.snappedTopInset() + this.contentView.snappedBottomInset());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateFontMetrics() {
        Text firstParagraph = (Text) this.paragraphNodes.getChildren().get(0);
        this.lineHeight = Utils.getLineHeight(((TextArea) getSkinnable()).getFont(), firstParagraph.getBoundsType());
        this.characterWidth = this.fontMetrics.get().computeStringWidth(PdfOps.W_TOKEN);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected void updateHighlightFill() {
        for (Node node : this.selectionHighlightGroup.getChildren()) {
            Path selectionHighlightPath = (Path) node;
            selectionHighlightPath.setFill(this.highlightFill.get());
        }
    }

    private double getTextTranslateX() {
        return this.contentView.snappedLeftInset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getTextTranslateY() {
        return this.contentView.snappedTopInset();
    }

    private double getTextLeft() {
        return 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point2D translateCaretPosition(Point2D p2) {
        return p2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Text getTextNode() {
        return (Text) this.paragraphNodes.getChildren().get(0);
    }

    public HitInfo getIndex(double x2, double y2) {
        Text textNode = getTextNode();
        Point2D p2 = new Point2D(x2 - textNode.getLayoutX(), y2 - getTextTranslateY());
        HitInfo hit = textNode.impl_hitTestChar(translateCaretPosition(p2));
        int pos = hit.getCharIndex();
        if (pos > 0) {
            int oldPos = textNode.getImpl_caretPosition();
            textNode.setImpl_caretPosition(pos);
            PathElement element = textNode.getImpl_caretShape()[0];
            if ((element instanceof MoveTo) && ((MoveTo) element).getY() > y2 - getTextTranslateY()) {
                hit.setCharIndex(pos - 1);
            }
            textNode.setImpl_caretPosition(oldPos);
        }
        return hit;
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public void nextCharacterVisually(boolean moveRight) {
        if (isRTL()) {
            moveRight = !moveRight;
        }
        Text textNode = getTextNode();
        Bounds caretBounds = this.caretPath.getLayoutBounds();
        if (this.caretPath.getElements().size() == 4) {
            caretBounds = new Path(this.caretPath.getElements().get(0), this.caretPath.getElements().get(1)).getLayoutBounds();
        }
        double hitX = moveRight ? caretBounds.getMaxX() : caretBounds.getMinX();
        double hitY = (caretBounds.getMinY() + caretBounds.getMaxY()) / 2.0d;
        HitInfo hit = textNode.impl_hitTestChar(new Point2D(hitX, hitY));
        Path charShape = new Path(textNode.impl_getRangeShape(hit.getCharIndex(), hit.getCharIndex() + 1));
        if ((moveRight && charShape.getLayoutBounds().getMaxX() > caretBounds.getMaxX()) || (!moveRight && charShape.getLayoutBounds().getMinX() < caretBounds.getMinX())) {
            hit.setLeading(!hit.isLeading());
            positionCaret(hit, false, false);
            return;
        }
        int dot = this.textArea.getCaretPosition();
        this.targetCaretX = moveRight ? 0.0d : Double.MAX_VALUE;
        downLines(moveRight ? 1 : -1, false, false);
        this.targetCaretX = -1.0d;
        if (dot == this.textArea.getCaretPosition()) {
            if (moveRight) {
                this.textArea.forward();
            } else {
                this.textArea.backward();
            }
        }
    }

    static {
    }

    protected void downLines(int nLines, boolean select, boolean extendSelection) {
        Text textNode = getTextNode();
        Bounds caretBounds = this.caretPath.getLayoutBounds();
        double targetLineMidY = ((caretBounds.getMinY() + caretBounds.getMaxY()) / 2.0d) + (nLines * this.lineHeight);
        if (targetLineMidY < 0.0d) {
            targetLineMidY = 0.0d;
        }
        double x2 = this.targetCaretX >= 0.0d ? this.targetCaretX : caretBounds.getMaxX();
        HitInfo hit = textNode.impl_hitTestChar(translateCaretPosition(new Point2D(x2, targetLineMidY)));
        int pos = hit.getCharIndex();
        int oldPos = textNode.getImpl_caretPosition();
        boolean oldBias = textNode.isImpl_caretBias();
        textNode.setImpl_caretBias(hit.isLeading());
        textNode.setImpl_caretPosition(pos);
        tmpCaretPath.getElements().clear();
        tmpCaretPath.getElements().addAll(textNode.getImpl_caretShape());
        tmpCaretPath.setLayoutX(textNode.getLayoutX());
        tmpCaretPath.setLayoutY(textNode.getLayoutY());
        Bounds tmpCaretBounds = tmpCaretPath.getLayoutBounds();
        double foundLineMidY = (tmpCaretBounds.getMinY() + tmpCaretBounds.getMaxY()) / 2.0d;
        textNode.setImpl_caretBias(oldBias);
        textNode.setImpl_caretPosition(oldPos);
        if (pos > 0) {
            if (nLines > 0 && foundLineMidY > targetLineMidY) {
                hit.setCharIndex(pos - 1);
            }
            if (pos >= this.textArea.getLength() && getCharacter(pos - 1) == '\n') {
                hit.setLeading(true);
            }
        }
        if (nLines == 0 || ((nLines > 0 && foundLineMidY > caretBounds.getMaxY()) || (nLines < 0 && foundLineMidY < caretBounds.getMinY()))) {
            positionCaret(hit, select, extendSelection);
            this.targetCaretX = x2;
        }
    }

    public void previousLine(boolean select) {
        downLines(-1, select, false);
    }

    public void nextLine(boolean select) {
        downLines(1, select, false);
    }

    public void previousPage(boolean select) {
        downLines(-((int) (this.scrollPane.getViewportBounds().getHeight() / this.lineHeight)), select, false);
    }

    public void nextPage(boolean select) {
        downLines((int) (this.scrollPane.getViewportBounds().getHeight() / this.lineHeight), select, false);
    }

    public void lineStart(boolean select, boolean extendSelection) {
        this.targetCaretX = 0.0d;
        downLines(0, select, extendSelection);
        this.targetCaretX = -1.0d;
    }

    public void lineEnd(boolean select, boolean extendSelection) {
        this.targetCaretX = Double.MAX_VALUE;
        downLines(0, select, extendSelection);
        this.targetCaretX = -1.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void paragraphStart(boolean previousIfAtStart, boolean select) {
        TextArea textArea = (TextArea) getSkinnable();
        String text = textArea.textProperty().getValueSafe();
        int pos = textArea.getCaretPosition();
        if (pos > 0) {
            if (previousIfAtStart && text.codePointAt(pos - 1) == 10) {
                pos--;
            }
            while (pos > 0 && text.codePointAt(pos - 1) != 10) {
                pos--;
            }
            if (select) {
                textArea.selectPositionCaret(pos);
            } else {
                textArea.positionCaret(pos);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void paragraphEnd(boolean goPastInitialNewline, boolean goPastTrailingNewline, boolean select) {
        TextArea textArea = (TextArea) getSkinnable();
        String text = textArea.textProperty().getValueSafe();
        int pos = textArea.getCaretPosition();
        int len = text.length();
        boolean wentPastInitialNewline = false;
        if (pos < len) {
            if (goPastInitialNewline && text.codePointAt(pos) == 10) {
                pos++;
                wentPastInitialNewline = true;
            }
            if (!goPastTrailingNewline || !wentPastInitialNewline) {
                while (pos < len && text.codePointAt(pos) != 10) {
                    pos++;
                }
                if (goPastTrailingNewline && pos < len) {
                    pos++;
                }
            }
            if (select) {
                textArea.selectPositionCaret(pos);
            } else {
                textArea.positionCaret(pos);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTextNodeCaretPos(int pos) {
        Text textNode = getTextNode();
        if (isForwardBias()) {
            textNode.setImpl_caretPosition(pos);
        } else {
            textNode.setImpl_caretPosition(pos - 1);
        }
        textNode.impl_caretBiasProperty().set(isForwardBias());
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected PathElement[] getUnderlineShape(int start, int end) {
        int pStart = 0;
        for (Node node : this.paragraphNodes.getChildren()) {
            Text p2 = (Text) node;
            int pEnd = pStart + p2.textProperty().getValueSafe().length();
            if (pEnd >= start) {
                return p2.impl_getUnderlineShape(start - pStart, end - pStart);
            }
            pStart = pEnd + 1;
        }
        return null;
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected PathElement[] getRangeShape(int start, int end) {
        int pStart = 0;
        for (Node node : this.paragraphNodes.getChildren()) {
            Text p2 = (Text) node;
            int pEnd = pStart + p2.textProperty().getValueSafe().length();
            if (pEnd >= start) {
                return p2.impl_getRangeShape(start - pStart, end - pStart);
            }
            pStart = pEnd + 1;
        }
        return null;
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected void addHighlight(List<? extends Node> nodes, int start) {
        int pStart = 0;
        Text paragraphNode = null;
        Iterator<Node> it = this.paragraphNodes.getChildren().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Text p2 = (Text) it.next();
            int pEnd = pStart + p2.textProperty().getValueSafe().length();
            if (pEnd >= start) {
                paragraphNode = p2;
                break;
            }
            pStart = pEnd + 1;
        }
        if (paragraphNode != null) {
            for (Node node : nodes) {
                node.setLayoutX(paragraphNode.getLayoutX());
                node.setLayoutY(paragraphNode.getLayoutY());
            }
        }
        this.contentView.getChildren().addAll(nodes);
    }

    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    protected void removeHighlight(List<? extends Node> nodes) {
        this.contentView.getChildren().removeAll(nodes);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void deleteChar(boolean previous) {
        boolean z2;
        if (previous) {
            z2 = !((TextArea) getSkinnable()).deletePreviousChar();
        } else {
            z2 = !((TextArea) getSkinnable()).deleteNextChar();
        }
        boolean shouldBeep = z2;
        if (shouldBeep) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.TextInputControlSkin
    public Point2D getMenuPosition() {
        this.contentView.layoutChildren();
        Point2D p2 = super.getMenuPosition();
        if (p2 != null) {
            p2 = new Point2D(Math.max(0.0d, (p2.getX() - this.contentView.snappedLeftInset()) - ((TextArea) getSkinnable()).getScrollLeft()), Math.max(0.0d, (p2.getY() - this.contentView.snappedTopInset()) - ((TextArea) getSkinnable()).getScrollTop()));
        }
        return p2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Bounds getCaretBounds() {
        return ((TextArea) getSkinnable()).sceneToLocal(this.caretPath.localToScene(this.caretPath.getBoundsInLocal()));
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case LINE_FOR_OFFSET:
            case LINE_START:
            case LINE_END:
            case BOUNDS_FOR_RANGE:
            case OFFSET_AT_POINT:
                Text text = getTextNode();
                return text.queryAccessibleAttribute(attribute, parameters);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
