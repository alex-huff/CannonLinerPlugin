package phonis.cannonliner.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CannonLinerServer extends Thread {

    private CannonLinerHandler currentHandler;
    private Thread currentHandlerThread;
    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(25566);

            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = this.serverSocket.accept();

                if (this.currentHandler != null) {
                    this.currentHandler.close();
                }

                if (this.currentHandlerThread != null) {
                    this.currentHandlerThread.interrupt();
                }

                this.currentHandler = new CannonLinerHandler(socket);
                this.currentHandlerThread = new Thread(this.currentHandler::handle);

                this.currentHandlerThread.start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.serverSocket != null) this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
