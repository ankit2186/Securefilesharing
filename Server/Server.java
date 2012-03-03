//package Server;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

public class Server implements Runnable {
    private static final int MINARG = 1;
    private static ServerSocket serversocket=null;
    private Socket clientsocket = null;
public static void main(String[] args)
{
    if (args.length != MINARG)
		{
			System.out.println("Please provide: "+MINARG+" arguments.");
			System.out.println("java Server <serverPort>");
			System.exit(1);
		}
       String port;
       port = args[0];
       new Server(port);

}
public Server(String port)
{
	try 
        {
	 serversocket = new ServerSocket(Integer.parseInt(port));
	} 
        catch (IOException e) 
        {
	 System.out.println("Error- IOException: "+e.getMessage());
	}
        int localPort;
        localPort= serversocket.getLocalPort();
        System.out.println("Server running on port number : " +localPort);
	new Thread(this).start();
}

    @Override
public void run()
    {
	System.out.println("**** Waiting for Peers to connect ****");
	while(true)
        {
	try 
        {
            java.net.InetAddress inetAddress;
            int port;
            clientsocket = serversocket.accept();
            inetAddress = clientsocket.getInetAddress();
            port = clientsocket.getPort();
            System.out.println("Connected to Peer address : "+inetAddress+" and port : "+port);
            new connectToPeer(clientsocket);
	}
        catch (IOException e)
        {
	   System.out.println("Error- IOException: "+e.getMessage());
	}
	}

}
}