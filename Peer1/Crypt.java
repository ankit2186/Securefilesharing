//package Peer1;

import java.security.*;
import javax.crypto.*;
import java.security.interfaces.*;
class Crypt
{
   
    Cipher encifer;
    Cipher decifer;
    RSAPublicKey publicKeyRSA;
    RSAPrivateKey privateKeyRSA;
    byte[] plainText=null;
    byte[] dataEncrypted;
    byte[] cipherText;
    byte[] cipherText2;
    byte[] dataDec;
    byte[] dataDecrypted;
    
    

    synchronized byte[] selection(int i)
    {
        if(i==1)
        { 
		
			/* Encryption Code */
			
            try {
                        encifer=Cipher.getInstance("RSA/ECB/NoPadding");
                try {
                        encifer.init(Cipher.ENCRYPT_MODE, publicKeyRSA);
                    } 
                catch (InvalidKeyException e) 
                {
                    System.out.println("Error- InvalidKeyException : "+e.getMessage());
                }
                try 
                {   
                    cipherText = encifer.doFinal(this.dataEncrypted);
                } 
                catch (BadPaddingException e) 
                {
                    System.out.println("Error- BadPaddingException : "+e.getMessage());
                }
                catch (IllegalBlockSizeException e) 
                {
                    System.out.println("Error- IllegalBlockSizeException : "+e.getMessage());
                }
                }
                catch (NoSuchAlgorithmException e) 
                {
                    System.out.println("Error- NoSuchAlgorithmException : "+e.getMessage());
                } 
                catch (NoSuchPaddingException e) 
                {
                    System.out.println("Error- NoSuchPaddingException : "+e.getMessage());
                }
            
                return cipherText;
           }
           else
          {
		  
			/* Decryption Code */
			
             try 
            {
                decifer = Cipher.getInstance("RSA/ECB/NoPadding");
            } 
           
            catch (NoSuchPaddingException ex) 
            {
                System.out.println("Error- NoSuchPaddingException" +ex.getMessage());
            }
              catch (NoSuchAlgorithmException ex) 
            {
                System.out.println("Error- NoSuchAlgorithmException" +ex.getMessage());
            }
            try
            {
                decifer.init(Cipher.DECRYPT_MODE, this.privateKeyRSA);
            } 
            catch (InvalidKeyException ex)
            {
                System.out.println("Error- InvalidKeyException" +ex.getMessage());
            }
             cipherText2 = dataDec;
        try {
                plainText = decifer.doFinal(this.dataDec);
            }
                catch (BadPaddingException ex) 
        {
                System.out.println("Error- BadPaddingException" +ex.getMessage());
        }
        catch (IllegalBlockSizeException ex) 
        {
                System.out.println("Error- IllegalBlockSizeException" +ex.getMessage());
        }
        
        dataDecrypted = plainText;
        
        return plainText;
        }
        
    }
}