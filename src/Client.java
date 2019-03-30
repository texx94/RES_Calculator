import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a simple client for our computation protocol.
 * When the client connects to a server, he can ask the server to compute
 * a calculation.
 *
 * @author Mateo Tutic
 */
public class Client {

    final static Logger LOG = Logger.getLogger(Client.class.getName());

    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    boolean connected = false;

    /**
     * This inner class implements the Runnable interface, so that the run()
     * method can execute on its own thread. This method reads data sent from the
     * server, line by line, until the connection is closed or lost.
     */
//    class NotificationListener implements Runnable {
//
//        @Override
//        public void run() {
//            String notification;
//            try {
//                while ((connected && (notification = in.readLine()) != null)) {
//                    LOG.log(Level.INFO, "Server notification for {1}", new Object[]{notification});
//                }
//            } catch (IOException e) {
//                LOG.log(Level.SEVERE, "Connection problem in client used by {1}", new Object[]{e.getMessage()});
//                connected = false;
//            } finally {
//                cleanup();
//            }
//        }
//    }

    /**
     * This method is used to send a calculation to the server and get the result.
     * @param computation Calculation to compute
     */
    public void compute(String computation) {
        LOG.log(Level.INFO, computation);

        // Send the computation
        out.println(computation);
        out.flush();

        // Read the result
        String result;
        try {
            while ((connected && (result = in.readLine()) != null)) {
                LOG.log(Level.INFO, "The result is {0}", computation);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to get the result: {1}", e.getMessage());
            connected = false;
        } finally {
            cleanup();
        }
    }

    /**
     * This method is used to connect to the server and to inform the server that
     * the user "behind" the client has a name (in other words, the HELLO command
     * is issued after successful connection).
     *
     * @param serverAddress the IP address used by the Presence Server
     * @param serverPort the port used by the Presence Server
     */
    public void connect(String serverAddress, int serverPort) {
        try {
            clientSocket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            connected = true;

            // Read welcome message from the server
            String message;
            while ((connected && (message = in.readLine()) != null)) {
                LOG.log(Level.INFO, "Server response {0}", message);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to connect to server: {0}", e.getMessage());
            cleanup();
            return;
        }
    }

    public void disconnect() {
        LOG.log(Level.INFO, "Client has requested to be disconnected.");
        connected = false;
        out.println("BYE");
        cleanup();
    }

    private void cleanup() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (out != null) {
            out.close();
        }

        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void main(String[] args) {
        Client c1 = new Client();
        c1.connect("10.192.105.166", 2205);

        // TODO Make calculation

        c1.disconnect();
    }
}
