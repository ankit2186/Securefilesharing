//package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class connectToPeer implements Runnable 
{
	private Socket clientsocket = null;
	private ObjectOutputStream oout =null;
	private ObjectInputStream oin= null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String input = null;
	private int index;
    static Hashtable<Integer, Peer> database=new Hashtable<Integer, Peer>();
    static Peer[] peerList= new Peer[10];
    public connectToPeer(Socket clientsocket)
    {
        try 
        {
            this.clientsocket = clientsocket;
            in = new BufferedReader(new InputStreamReader(this.clientsocket.getInputStream()));
            out = new PrintWriter(clientsocket.getOutputStream(), true);
            oout = new ObjectOutputStream(clientsocket.getOutputStream());
            oin = new ObjectInputStream(clientsocket.getInputStream());
            new Thread(this).start();
        } catch (IOException ex)
        {
            System.out.println("Error- IOException: "+ex.getMessage());
        }
    }
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Override
    public synchronized void run(){

                    if(input==null)
                    {
                    readPeerInfo();
                    out.println("Connected to Server");
                    }

	try {   
            input=in.readLine();
	    while(input!=null)
            {
                    if(input.contains("get"))
                    {
					    String[] getFileCommand = input.split(" ");
						if(getFileCommand.length == 1)
						{
							out.println("ERROR: Please provide file name.");					
						}
                         else
						{						 
                            port(getFileCommand[1]);
						}
                    }
                    else if(input.contains("update list"))
                    {
                            out.println("send");
							readPeerInfo();
                    }
                    else if(input.contains("exit"))
                    {
                            database.remove(index);
                            System.out.println("Peer "+(index+1)+" is now disconnected");
                            out.println("exited from server ");
                            clientsocket.close();
                            Thread.currentThread().stop();
                    }
                    else
                    {
                        out.println("Invalid Command!!!");
                    }
                    input=in.readLine();			
            }
	} 
        catch (IOException e)
        {
		System.out.println("Error- IOException: "+e.getMessage());
	}
}

    private synchronized void readPeerInfo()
    {
     try 
     {
                ArrayList<String> fileList = new ArrayList<String>();
                fileList.clear();
                try {
                        Serialize readpeer = (Serialize)oin.readObject();
                        fileList = readpeer.getFiles();
						index=readpeer.getID();
                        Peer peer = new Peer(clientsocket.getPort());
                        peer.fileList=fileList;
                        database.put(index, peer);
                       
                        System.out.println("Files list at Peer "+(index+1)+":-");
                        for(int i=0;i<fileList.size();i++)
                        {
                          System.out.println(fileList.get(i));
                        }


                } catch (ClassNotFoundException e)
                {
                    System.out.println("Error- ClassNotFoundException:  "+e.getMessage());
                }
                oout.flush();
        } 
        catch (IOException e)
        {
                System.out.println("Error- IOException: " +e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void  port(String FileName)
    {
        ArrayList<Integer> portList = new ArrayList<Integer>();
        Peer peer;
    
        for (Enumeration e = database.keys(); e.hasMoreElements();)
        {
        int ind = (Integer)e.nextElement();
    Peer val = database.get(ind);
    if(val.fileList.contains(FileName))
    {
        portList.add(val.PeerName);
    }

    }
    if( portList.isEmpty())
    {
    out.println("Requested file does not exist");
    }
    else
    {
    out.println("list of ports");
    for(int i=0;i<portList.size();i++)
    {
    out.println(portList.get(i));
    }
    out.println("end");
    }
    portList.clear();

}

}
class Serialize implements Serializable
{
    int ID;
    ArrayList<String> listFiles = new ArrayList<String>();
    int portNumber;

@SuppressWarnings("unchecked")
public void setFiles(ArrayList<String> listFiles)
{
	this.listFiles=(ArrayList<String>)listFiles.clone();
}
public ArrayList<String> getFiles()
{
	return this.listFiles;
}
public void setID(int ID)
{
	this.ID=ID;
}
public int getID()
{
        return this.ID;
}
public void setPort(int port)
{
	this.portNumber=port;
}
public int getPort()
{
        return this.portNumber;
}
}