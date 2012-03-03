//package Peer1;
import java.io.*;
import java.net.*;
import java.util.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;

public final class Peer1 implements Runnable
{
    private static final int MINARG = 1;
    static final BigInteger Zero = new BigInteger("0");
    BigInteger md;
    BigInteger me;
    int ID = 0;
    String root = "files2Share/";
    String getc;
    String[] getFiles;
    String[] listFiles;
    File myDirectory;
    Socket client ;
    BufferedReader in ;
    PrintWriter out ;
    keyBank keyBk;
    ObjectOutputStream oos ;
    RSAPrivateKey privateKey ;
    RSAPublicKey publicKey ;
    ArrayList<String> listOfFiles = new ArrayList<String>();

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException 
    {
        if (args.length != MINARG )
		{
			System.out.println("Please provide: "+MINARG+" arguments.");
			System.out.println("java Peer1 <serverPort>");
			System.exit(1);
		}
        String port;
        port = args[0];
        new Peer1(port);
    }

    public Peer1(String port) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException
    {
        while(true)
        {
            myConnection("127.0.0.1", Integer.parseInt(port));
            sendPeerInfo();
            withServer();
        }
    }

    public void myConnection(String host,int port) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException
    {
    try 
    {
        client = new Socket(host,port);
        new ObjectInputStream(client.getInputStream());
        oos = new ObjectOutputStream(client.getOutputStream());
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
    }
    catch (UnknownHostException e) 
    {
        System.out.println("Error- UnknownHostException : "+e.getMessage());
    } catch (IOException e) 
    {
        System.out.println("Error- IOException : "+e.getMessage());
    }
    myDirectory= new File(root);
    listFiles=myDirectory.list();
    listOfFiles.clear();
    listOfFiles.addAll(Arrays.asList(listFiles));
    System.out.println("File list Registered to server......");
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    KeyPair generating_keypairs = keyGen.generateKeyPair();
    keyGen.initialize(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4));
    privateKey = (RSAPrivateKey) generating_keypairs.getPrivate();
    publicKey = (RSAPublicKey) generating_keypairs.getPublic();
    try
    {
        int ind=0;
        String filen="../keyBank/Peer"+(ind+1);
        FileOutputStream fout = new FileOutputStream(filen + ".key");
        fout.write(publicKey.getEncoded());
        fout.close();
        FileWriter fwrite = new FileWriter(filen + ".txt");
        fwrite.write(publicKey.getModulus().toString(16).toUpperCase());
        fwrite.write("\n");
        fwrite.write(publicKey.getPublicExponent().toString(16).toUpperCase());
        fwrite.close();
    }
    catch (IOException e) {
        System.out.println("error- IOException: "+e.getMessage());
    }

    keyBk = new keyBank();
    keyBk.ID=ID;
    new Thread(this).start();
}
    @Override
public void run() {
    new filePeer2Peer(client.getLocalPort(),this);
}
public void withServer() throws NoSuchAlgorithmException{
    String fromServer;
    String fromUser;
    ArrayList<Integer> portList = new ArrayList<Integer>();
    String listOfPort;
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    try {          
        fromServer=in.readLine();
        while(fromServer!=null)
        {
            System.out.println(fromServer);
            if(fromServer.contains("exit"))
            {
                System.exit(1);
            }
        
            if(fromServer.equals("list of ports"))
            {
                portList.clear();
                while((listOfPort=in.readLine())!=null)
                {
                    if(listOfPort.equals("end"))
                    {
                         break;
                    }
                    else
                    {
                        portList.add(Integer.parseInt(listOfPort));
                    }
                }
                System.out.println("size:"+portList.size());
                System.out.println("Port list:-");
                int k=1;
                for(int i=0;i<portList.size();i++)
                {
				
                    System.out.println(k+") "+ portList.get(i));
                    k++;
                }
                for(int i=0;i<portList.size();i++)
                {
                    if(portList.get(i)==client.getLocalPort())
                    {
                        System.out.println("File already exist in files2Share directory");
                        portList.clear();
                        break;
                    }
                }
    if(portList.size()>0)
    {
        int c;
        if(portList.size()>1)
        {
        System.out.print("Select Port option :");
        fromUser=stdIn.readLine();
        while(fromUser==null)
        {
            System.out.print("Select Port option :");
            fromUser=stdIn.readLine();
        }
        c=Integer.parseInt(fromUser);}
        else
        {
            c=1;
        }
        Socket socket_to_otherpeer = new Socket("127.0.0.1",portList.get((c-1)));
        System.out.println("Connecting to Peer at: "+client.getInetAddress()+"  "+portList.get((c-1)));
        BufferedReader temp_in = new BufferedReader(new InputStreamReader(socket_to_otherpeer.getInputStream()));
        PrintWriter temp_out = new PrintWriter(socket_to_otherpeer.getOutputStream(),true);
        ObjectInputStream temp_oin = new ObjectInputStream(socket_to_otherpeer.getInputStream());
        ObjectOutputStream temp_oout = new ObjectOutputStream(socket_to_otherpeer.getOutputStream());
        byte[] encryptData;
        byte[] decryptData = null ;
        String fromOtherPeer;
        
        fromOtherPeer=temp_in.readLine();
        while(fromOtherPeer!=null)
        {
            if(fromOtherPeer.contains("Remote"))
            {
                temp_out.println("give "+this.getFiles[1]+" "+ID);
                if(temp_in.readLine().contains("data"))
                {
                    keyBank keyBk = new keyBank();
                    try {
                            keyBk = (keyBank)temp_oin.readObject();
                            encryptData = keyBk.data_to_encrypt;
                            Crypt cryp=new Crypt();
                            cryp.privateKeyRSA=this.privateKey;
                            cryp.dataDec=encryptData;
                            decryptData = cryp.selection(0);
                            String filenm = "../keyBank/Peer";
                            KeyFactory keyFactory ;
                            RSAPublicKey pubKey =null;
                            RSAPublicKeySpec pubKeySpec ;
                            BufferedReader keyIn = new BufferedReader(new FileReader(filenm+(keyBk.ID+1)+".txt"));
                            BigInteger keyModulus = new BigInteger(keyIn.readLine(), 16);
                            BigInteger keyExponent = new BigInteger(keyIn.readLine(), 16);
                            keyIn.close();
                            if ( keyModulus == null)
                            {
                                md= Zero;
                            }
                            else
                            {
                                md=keyModulus;
                            }
                            if (keyExponent == null)
                            {
                                me= Zero;
                            }
                            else
                            {
                                me=keyExponent;
                            }
                            pubKeySpec = new RSAPublicKeySpec(md, me); //Providing Public Key specification
                            try 
                            {
                                keyFactory = KeyFactory.getInstance("RSA");
                            try {
                                pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);  //Calculate RSA public key
                            } catch (InvalidKeySpecException e)
                            {
                                System.out.println("Error- InvalidKeySpecException:  "+e.getMessage());
                            }
                            } catch (NoSuchAlgorithmException e) {
                                System.out.println("Error- NoSuchAlgorithmException: "+e.getMessage());
                            }

							/* Code to provide Digital Signature */
							
		java.security.Signature signature = java.security.Signature.getInstance("SHA512withRSA");
        SignedObject sign_obj=keyBk.sign;
        try 
        {         
			sign_obj.verify(pubKey, signature);
        }
        catch (InvalidKeyException ex) {
            System.out.println("Error- InvalidKeyException:  "+ex.getMessage());
        } 
        catch (SignatureException ex) {
            System.out.println("Error- SignatureException: "+ex.getMessage());
        }
        } catch (ClassNotFoundException e) {
            System.out.println("Error- ClassNotFoundException:  "+e.getMessage());
        }
      }
   }
    if(fromOtherPeer.equals("done"))
    {
        temp_out.println("done"); 
        break;
    }
    fromOtherPeer=temp_in.readLine();
    }
    temp_in.close();
    temp_out.close();
    temp_out.flush();
    temp_oout.flush();
    temp_oout.close();
    temp_oin.close();
    FileOutputStream fout = new FileOutputStream(new File(root+this.getFiles[1]));
    fout.write(decryptData);
    fout.close();
    
    BufferedReader fin=new BufferedReader(new FileReader(new File(root+this.getFiles[1])));
    PrintWriter fouttemp=new PrintWriter(new BufferedWriter(new FileWriter(new File(root+"temp.txt"),true)));
    String temp=fin.readLine();

    while(temp!=null)
    {
        fouttemp.println(temp.trim());
        temp=fin.readLine();
    }
    fin.close();
    fouttemp.close();
    PrintWriter foutc=new PrintWriter(new BufferedWriter(new FileWriter(new File(root+this.getFiles[1]))),true);
    BufferedReader fintemp=new BufferedReader(new FileReader(new File(root+"temp.txt")));
    String temp2=fintemp.readLine();
    while(temp2!=null)
    {
        foutc.println(temp2);
        temp2=fintemp.readLine();
    }
    fintemp.close();
    foutc.close();
    new File(root+"temp.txt").delete();
    socket_to_otherpeer.close();
    myDirectory= new File(root);
    listFiles=myDirectory.list();
    listOfFiles.clear();
    listOfFiles.addAll(Arrays.asList(listFiles));
    System.out.println("File list Registered to the server");

    out.println("update list");
    if(in.readLine().equals("send"))
    {
        sendPeerInfo();
    }
    }
 }
    System.out.print("cmd>");
    fromUser=stdIn.readLine();
    if(fromUser!=null)
    {
        if(fromUser.contains("get"))
        {
            getFiles = fromUser.split(" ");
						
        }                                 
        out.println(fromUser);
    }
    fromServer=in.readLine();
    }//while
    } catch (IOException e) {
        System.out.println("Error- IOException:  "+e.getMessage());
    }
}

public void sendPeerInfo()
{
    try 
    {
        Serialize serialized = new Serialize();
        serialized.setFiles(listOfFiles);
        serialized.setID(ID);
        serialized.setPort(client.getLocalPort());
        oos.writeObject(serialized);
    } catch (UnknownHostException e) {
        System.out.println("Error- UnknownHostException:  "+e.getMessage());
    } catch (IOException e) {
       System.out.println("Error- IOException:  "+e.getMessage());
    }
    }
}

class keyBank implements Serializable
{
    byte[] data_to_encrypt;
    SignedObject sign;
    int ID;
} 
