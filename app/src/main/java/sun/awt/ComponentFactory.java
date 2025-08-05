package sun.awt;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Window;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.WindowPeer;
import sun.awt.datatransfer.DataTransferer;

/* loaded from: rt.jar:sun/awt/ComponentFactory.class */
public interface ComponentFactory {
    CanvasPeer createCanvas(Canvas canvas) throws HeadlessException;

    PanelPeer createPanel(Panel panel) throws HeadlessException;

    WindowPeer createWindow(Window window) throws HeadlessException;

    FramePeer createFrame(Frame frame) throws HeadlessException;

    DialogPeer createDialog(Dialog dialog) throws HeadlessException;

    ButtonPeer createButton(Button button) throws HeadlessException;

    TextFieldPeer createTextField(TextField textField) throws HeadlessException;

    ChoicePeer createChoice(Choice choice) throws HeadlessException;

    LabelPeer createLabel(Label label) throws HeadlessException;

    ListPeer createList(List list) throws HeadlessException;

    CheckboxPeer createCheckbox(Checkbox checkbox) throws HeadlessException;

    ScrollbarPeer createScrollbar(Scrollbar scrollbar) throws HeadlessException;

    ScrollPanePeer createScrollPane(ScrollPane scrollPane) throws HeadlessException;

    TextAreaPeer createTextArea(TextArea textArea) throws HeadlessException;

    FileDialogPeer createFileDialog(FileDialog fileDialog) throws HeadlessException;

    MenuBarPeer createMenuBar(MenuBar menuBar) throws HeadlessException;

    MenuPeer createMenu(Menu menu) throws HeadlessException;

    PopupMenuPeer createPopupMenu(PopupMenu popupMenu) throws HeadlessException;

    MenuItemPeer createMenuItem(MenuItem menuItem) throws HeadlessException;

    CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) throws HeadlessException;

    DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws HeadlessException, InvalidDnDOperationException;

    FontPeer getFontPeer(String str, int i2);

    RobotPeer createRobot(Robot robot, GraphicsDevice graphicsDevice) throws AWTException, HeadlessException;

    DataTransferer getDataTransferer();
}
