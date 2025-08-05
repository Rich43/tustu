package sun.misc;

/* loaded from: rt.jar:sun/misc/RequestProcessor.class */
public class RequestProcessor implements Runnable {
    private static Queue<Request> requestQueue;
    private static Thread dispatcher;

    public static void postRequest(Request request) {
        lazyInitialize();
        requestQueue.enqueue(request);
    }

    @Override // java.lang.Runnable
    public void run() {
        lazyInitialize();
        while (true) {
            try {
                try {
                    requestQueue.dequeue().execute();
                } catch (Throwable th) {
                }
            } catch (InterruptedException e2) {
            }
        }
    }

    public static synchronized void startProcessing() {
        if (dispatcher == null) {
            dispatcher = new Thread(new RequestProcessor(), "Request Processor");
            dispatcher.setPriority(7);
            dispatcher.start();
        }
    }

    private static synchronized void lazyInitialize() {
        if (requestQueue == null) {
            requestQueue = new Queue<>();
        }
    }
}
