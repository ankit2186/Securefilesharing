//package Peer2;

import java.io.*;
import java.net.*;

class filePeer2Peer implements Runnable
{
ServerSocket serversocket;
Socket clientsocket;
Peer2 conn;

public filePeer2Peer(int port,Peer2 conn)
{
    this.conn=conn;
    try 
    {
        serversocket = new ServerSocket(port);
    }
    catch (IOException e) 
    {
        System.out.println("Error- IOException: "+e.getMessage());
        return;
    }
    new Thread(this).start();
}

@Override
public synchronized void run()
{
    while(true)
    {
        try {
                clientsocket=serversocket.accept();
                System.out.println("Requesting for File transfer from Peer :"+clientsocket.getPort());
                new Peer2Peer(clientsocket,this.conn);
            } 
        catch (IOException e) 
        {
                System.out.println("Error- IOException:  "+e.getMessage());
        }
    }
}
}