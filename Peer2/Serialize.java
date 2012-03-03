//package Peer2;

import java.io.*;
import java.util.*;

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