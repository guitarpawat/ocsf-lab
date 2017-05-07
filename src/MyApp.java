
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * GUI class for the program.
 * @author Pawat Nakpiphatkul
 */
public class MyApp extends JFrame implements Observer{
    
    private MyClient client;
    private JTextArea output = new JTextArea(20,20);
    
    /**
     * Initialize MyApp.
     * @param client is an object of client to connect to host.
     */
    public MyApp(MyClient client) {
        this.client = client;
    }
    
    /**
     * Initialize MyApp.
     * @param host is a host name.
     * @param port is a port number.
     */
    public MyApp(String host,int port) {
        this.client = new MyClient(host, port);
        init();
        try {
            client.addObserver(this);
            client.openConnection();
        }
        catch (IOException e) {
            output.append(">> ERROR : "+e.getMessage()+"\n");
        }
    } 
    
    private void init() {
        super.setTitle("My Client App");
        super.setLayout(new BorderLayout());
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        super.pack();
        super.setVisible(true);
    }
    
    private void initUI() {
        JPanel panel = new JPanel();
        JLabel host = new JLabel("Host ");
        JTextField hostInput = new JTextField(30);
        JLabel port = new JLabel("Port ");
        JTextField portInput = new JTextField(10);
        JButton connect = new JButton("Connect/Disconnect");
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        ActionListener connectAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(hostInput.getText().length() > 0 && portInput.getText().length() >0) {
                    try {
                        String host = hostInput.getText();
                        int port = Integer.parseInt(portInput.getText());
                        if(client != null) client.closeConnection();
                        client.setHost(host);
                        client.setPort(port);
                        client.openConnection();
                    }
                    catch(NumberFormatException e) {
                        output.append(">> ERROR : Please enter valid port number.\n");
                    }
                    catch(IOException e) {
                        output.append(">> ERROR : "+e.getMessage()+"\n");
                    }
                }
            }
        };
        hostInput.addActionListener(connectAction);
        portInput.addActionListener(connectAction);
        connect.addActionListener(connectAction);
        panel.add(host);
        panel.add(hostInput);
        panel.add(port);
        panel.add(portInput);
        panel.add(connect);
        super.add(panel,BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(output);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        super.add(scroll,BorderLayout.CENTER);
        JTextField input = new JTextField();
        ActionListener inputAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(input.getText().length() > 0) {
                    output.append("#  "+input.getText()+"\n");
                    try {
                        client.sendToServer(input.getText());
                        input.setText("");
                    }
                    catch(IOException e) {
                        output.append(">> ERROR : "+e.getMessage()+"\n");
                    }
                }
            }
        };
        input.addActionListener(inputAction);
        super.add(input,BorderLayout.SOUTH);
    }

    /**
     * Append message to the JTextArea.
     * @param o is an Observable object.
     * @param msg is an Observable object's message.
     */
    @Override
    public void update(Observable o, Object msg) {
        output.append(msg.toString()+"\n");
    }
    
}
