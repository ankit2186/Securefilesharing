//package Peer2;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Peer2Peer implements Runnable {
Socket client ;
ObjectOutputStream oout ;
BufferedReader in ;
PrintWriter out ;
String input ;
int ID=2;
String root = "files2Share/";
String[] listFiles;
FileInputStream fin ;
Peer2 conn;
SignedObject sign ;
static final BigInteger Zero = new BigInteger("0");
BigInteger md;
BigInteger me;
public Peer2Peer(Socket client,Peer2 conn)
{
try {
        this.conn = conn;
        this.client = client;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
        oout = new ObjectOutputStream(client.getOutputStream());
        new ObjectInputStream(client.getInputStream());
        new Thread(this).start();
} 
catch (IOException ex)
    {
        System.out.println("Error- IOException:  "+ex.getMessage());
    }
}
@SuppressWarnings("deprecation")
    @Override
public synchronized void run()
{
try 
{
    if(input==null)
    {
        out.flush();
        out.println("Remote Peer for file transfer :"+this.client.getPort());
    }
    while((input=in.readLine())!=null){
    try {
            if(input.equals("done"))
            {
                out.flush();
                out.close();
                in.close();
                client.close();
                System.out.println(" Finish Sending!!!");
                System.out.print("cmd>");
                Thread.currentThread().stop();
                return;
            }
            if(input.contains("give"))
            {
                this.listFiles = input.split(" ");
                fin = new FileInputStream(root+this.listFiles[1]);
                byte [] from_file = new byte[(int)(new File(root+this.listFiles[1]).length())];
                fin.read(from_file);
                try {
                        int ID_other_peer;
                        byte[] data_to_encrypt;
                        ID_other_peer=Integer.parseInt(listFiles[2]);
                        System.out.println("giving "+listFiles[1]+" to Peer "+(ID_other_peer+1));
                        String filenm = "../keyBank/Peer";
                        KeyFactory keyFactory ;
                        RSAPublicKey pubKey =null;
                        RSAPublicKeySpec pubKeySpec ;
                                
                        BufferedReader keyIn = new BufferedReader(new FileReader(filenm+(ID_other_peer+1)+".txt"));
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
                        pubKeySpec = new RSAPublicKeySpec(md, me);
                        try {
                                keyFactory = KeyFactory.getInstance("RSA");
                                try {
                                        pubKey = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
                                    } catch (InvalidKeySpecException e) {
                                        System.out.println("Error- InvalidKeySpecException:  "+e.getMessage());
                                    }
                                    } catch (NoSuchAlgorithmException e) {
                                        System.out.println("Error- NoSuchAlgorithmException:  "+e.getMessage());
                                    }
                                RSAPublicKey public_key = pubKey;                                
                                try {
                                        java.security.Signature signature = java.security.Signature.getInstance("SHA512withRSA");
                                     try {
                                           sign = new SignedObject(from_file,this.conn.privateKey,signature);
                                         } 
                                     catch (InvalidKeyException e) {
                                            System.out.println("Error- InvalidKeyException:  "+e.getMessage());
                                     }
                                     catch (SignatureException e) {
                                            System.out.println("Error- SignatureException:  "+e.getMessage());
                                     }
                                     }
                                catch (NoSuchAlgorithmException e) {
                                            System.out.println("Error- NoSuchAlgorithmException:  "+e.getMessage());
                                    }
                         Crypt cryp=new Crypt();
                         cryp.publicKeyRSA=public_key;
                         cryp.dataEncrypted=from_file;
                         data_to_encrypt = cryp.selection(1);
                         keyBank key_data = new keyBank();
                         key_data.data_to_encrypt=data_to_encrypt;
                         key_data.sign=sign;
                         key_data.ID=ID;
                         out.println("data");
                         try {
                                Thread.sleep(100);
                            }
                         catch (InterruptedException ex) {
                                Logger.getLogger(Peer2Peer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    oout.writeObject(key_data);
                    out.println("done");
                    } catch (IOException e) {
                             System.out.println("Error- IOException:  "+e.getMessage());
                      }
                }
    }
    catch (FileNotFoundException e) {
        System.out.println("Error- FileNotFoundException:  "+e.getMessage());
    }
    }
    }
    catch (IOException e) {
    System.out.println("Error- IOException:  "+e.getMessage());
    }
  }//end of run()
}