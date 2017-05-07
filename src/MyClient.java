import com.lloseng.ocsf.client.ObservableClient;
import java.io.IOException;

/**
 * Client class for connecting to the host.
 * @author Pawat Nakpiphatkul
 */
public class MyClient extends ObservableClient {

    /**
     * @see ObservableClient#ObservableClient(java.lang.String, int) 
     */
    public MyClient(String host, int port) {
        super(host, port);
    }

    /**
     * @see ObservableClient#handleMessageFromServer(java.lang.Object) 
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        setChanged();
        notifyObservers(">> "+msg);
    }
    
    /**
     * @see ObservableClient#connectionClosed() 
     */
    @Override
    protected void connectionClosed() {
        setChanged();
        notifyObservers(">> Disconnected.");
    }
    
    /**
     * @see ObservableClient#connectionEstablished() 
     */
    @Override
    protected void connectionEstablished() {
        setChanged();
        notifyObservers(">> Connected.");
    }
    
    /**
     * Sending message to server until user sent "quit".
     * @throws IOException if host is not found.
     */
    public void runClient() throws IOException {
        openConnection();
        while(isConnected()) {
            String input = new java.util.Scanner(System.in).nextLine();
            if(input.equals("quit")) {
                closeConnection();
                break;
            }
            sendToServer(input);
        }
    }

}
