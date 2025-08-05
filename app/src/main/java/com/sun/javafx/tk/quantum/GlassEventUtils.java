package com.sun.javafx.tk.quantum;

import com.sun.glass.events.ViewEvent;
import com.sun.glass.events.WindowEvent;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassEventUtils.class */
class GlassEventUtils {
    private GlassEventUtils() {
    }

    public static String getMouseEventString(int type) {
        switch (type) {
            case 211:
                return "BUTTON_NONE";
            case 212:
                return "BUTTON_LEFT";
            case 213:
                return "BUTTON_RIGHT";
            case 214:
                return "BUTTON_OTHER";
            case 215:
            case 216:
            case 217:
            case 218:
            case 219:
            case 220:
            default:
                return "UNKNOWN";
            case 221:
                return "DOWN";
            case 222:
                return "UP";
            case 223:
                return "DRAG";
            case 224:
                return "MOVE";
            case 225:
                return "ENTER";
            case 226:
                return "EXIT";
            case 227:
                return "CLICK";
            case 228:
                return "WHEEL";
        }
    }

    public static String getViewEventString(int type) {
        switch (type) {
            case 411:
                return "ADD";
            case 412:
                return "REMOVE";
            case 413:
            case 414:
            case 415:
            case 416:
            case 417:
            case 418:
            case 419:
            case NNTPReply.NO_CURRENT_ARTICLE_SELECTED /* 420 */:
            case 424:
            case FTPReply.CANNOT_OPEN_DATA_CONNECTION /* 425 */:
            case FTPReply.TRANSFER_ABORTED /* 426 */:
            case 427:
            case 428:
            case 429:
            case NNTPReply.NO_SUCH_ARTICLE_FOUND /* 430 */:
            default:
                return "UNKNOWN";
            case 421:
                return "REPAINT";
            case 422:
                return "RESIZE";
            case 423:
                return "MOVE";
            case 431:
                return "FULLSCREEN_ENTER";
            case ViewEvent.FULLSCREEN_EXIT /* 432 */:
                return "FULLSCREEN_EXIT";
        }
    }

    public static String getWindowEventString(int type) {
        switch (type) {
            case WindowEvent.RESIZE /* 511 */:
                return "RESIZE";
            case 512:
                return "MOVE";
            case 513:
            case 514:
            case 515:
            case 516:
            case 517:
            case 518:
            case 519:
            case 520:
            case 523:
            case 524:
            case 525:
            case 526:
            case 527:
            case 528:
            case 529:
            case FTPReply.NOT_LOGGED_IN /* 530 */:
            case FTPReply.REQUEST_DENIED /* 534 */:
            case FTPReply.FAILED_SECURITY_CHECK /* 535 */:
            case FTPReply.REQUESTED_PROT_LEVEL_NOT_SUPPORTED /* 536 */:
            case 537:
            case 538:
            case 539:
            case 540:
            default:
                return "UNKNOWN";
            case 521:
                return "CLOSE";
            case 522:
                return "DESTROY";
            case WindowEvent.MINIMIZE /* 531 */:
                return "MINIMIZE";
            case 532:
                return "MAXIMIZE";
            case 533:
                return "RESTORE";
            case 541:
                return "FOCUS_LOST";
            case WindowEvent.FOCUS_GAINED /* 542 */:
                return "FOCUS_GAINED";
            case WindowEvent.FOCUS_GAINED_FORWARD /* 543 */:
                return "FOCUS_GAINED_FORWARD";
            case 544:
                return "FOCUS_GAINED_BACKWARD";
            case WindowEvent.FOCUS_DISABLED /* 545 */:
                return "FOCUS_DISABLED";
            case WindowEvent.FOCUS_UNGRAB /* 546 */:
                return "FOCUS_UNGRAB";
        }
    }
}
