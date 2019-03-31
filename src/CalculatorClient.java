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
 * @author Mateo Tutic, inspired by 06-PresenceApplication example
 */
public class CalculatorClient {

    final static Logger LOG = Logger.getLogger(CalculatorClient.class.getName());

    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    boolean connected = false;

    /**
     * This method is used to send a calculation to the server and get the result.
     * @param computation Calculation to compute
     */
    public void compute(String computation) {
        LOG.log(Level.INFO, "Client request: {0}", computation);

        // Send the computation
        out.println(computation);
        out.flush();

        // Read the result
        String result;
        try {
            while (connected && in.ready()) {
                result = in.readLine();
                LOG.log(Level.INFO, "Server response: {0}", result);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to get the server response: {0}", e.getMessage());
            connected = false;
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
            while (connected && in.ready()) {
                message = in.readLine();
                LOG.log(Level.INFO, "Server response: {0}", message);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unable to connect to server: {0}", e.getMessage());
            cleanup();
            return;
        }
    }

    public void disconnect() {
        LOG.log(Level.INFO, "CalculatorClient has requested to be disconnected.");
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Connection to the server
        CalculatorClient c1 = new CalculatorClient();
        c1.connect("cserver", 2205);  // TODO add container IP !

        while (true) {
            // prompt the user to enter their calculation
            System.out.print("Enter your calculation : ");

            // get user input
            String computation = "";

            try {
                computation = reader.readLine();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Unable to read user calculation: {0}", e.getMessage());
            }

            if (computation.equals("BYE")) {
                c1.disconnect();
                return;
            } else {
                c1.compute(computation);
            }
        }
    }
}
