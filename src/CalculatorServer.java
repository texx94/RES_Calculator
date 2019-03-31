import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculatorServer {
    static final Logger LOG = Logger.getLogger(CalculatorServer.class.getName());

    int port;

    public CalculatorServer(int port) {
        this.port = port;
    }

    public void serveClients() {
        ServerSocket serverSocket;
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }

        while(true) {
            try {
                LOG.log(Level.INFO, "\"Waiting (blocking) for a new client on port {0}", port);
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream());

                String line;
                boolean carryOn = true;

                out.println("Welcome to the Calculator Server!\n" +
                            "You can send me a request according to <OP> <numb1> <numb2>.\n" +
                            "OP can be ADD, SUB, MULT, DIV or BYE to close.");
                out.flush();

                LOG.info("Reading request until client sends BYE or closes the connection...");
                while(carryOn && ((line = in.readLine()) != null)) {
                    if(line.equalsIgnoreCase("bye"))
                        carryOn = false;

                    String[] tokens = line.split(" ");

                    if(tokens.length != 3) {
                        out.println("invalid number of parameter.\nYou must have <OP> <numb1> <numb2>");
                    } else {
                        int leftOperand = Integer.parseInt(tokens[1]);
                        int rightOperand = Integer.parseInt(tokens[2]);
                        switch (tokens[0].toLowerCase()) {
                            case "add":
                                out.println(leftOperand + rightOperand);
                                break;
                            case "sub":
                                out.println(leftOperand - rightOperand);
                                break;
                            case "mult":
                                out.println(leftOperand * rightOperand);
                                break;
                            case "div":
                                out.println(leftOperand / rightOperand);
                                break;
                            default:
                                out.println("Format error! please respect <OP> <numb1> <numb2>");
                        }
                        out.flush();
                    }
                }

                LOG.info("Cleaning up resources...");
                clientSocket.close();
                in.close();
                out.close();

            } catch (IOException ex) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

        CalculatorServer cServe = new CalculatorServer(2205);
        cServe.serveClients();
    }
}
