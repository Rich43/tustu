package com.sun.glass.events;

import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: jfxrt.jar:com/sun/glass/events/ViewEvent.class */
public class ViewEvent {
    public static final int ADD = 411;
    public static final int REMOVE = 412;
    public static final int REPAINT = 421;
    public static final int RESIZE = 422;
    public static final int MOVE = 423;
    public static final int FULLSCREEN_ENTER = 431;
    public static final int FULLSCREEN_EXIT = 432;

    public static String getTypeString(int type) {
        String string = "UNKNOWN";
        switch (type) {
            case 411:
                string = "ADD";
                break;
            case 412:
                string = "REMOVE";
                break;
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
                System.err.println("Unknown view event type: " + type);
                break;
            case 421:
                string = "REPAINT";
                break;
            case 422:
                string = "RESIZE";
                break;
            case 423:
                string = "MOVE";
                break;
            case 431:
                string = "FULLSCREEN_ENTER";
                break;
            case FULLSCREEN_EXIT /* 432 */:
                string = "FULLSCREEN_EXIT";
                break;
        }
        return string;
    }
}
