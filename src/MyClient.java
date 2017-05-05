import com.lloseng.ocsf.client.ObservableClient;
import java.io.IOException;

/**
 *
 * @author Pawat Nakpiphatkul
 */
public class MyClient extends ObservableClient {

    public MyClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        setChanged();
        notifyObservers(">> "+msg);
    }
    
    @Override
    protected void connectionClosed() {
        setChanged();
        notifyObservers(">> Disconnected.");
    }
    
    @Override
    protected void connectionEstablished() {
        setChanged();
        notifyObservers(">> Connected.");
    }
    
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
