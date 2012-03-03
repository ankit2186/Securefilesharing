Topic: Secure File Sharing
------------------------------------------------------------------------
Group Members:
1. Ankitkumar Biscuitwala : abiscui1@binghamton.edu
2. Satyajit Deshpande : sdeshpa2@binghamton.edu
3. Sushant Rai : srai1@binghamton.edu
------------------------------------------------------------------------
Programming Language Used: Java
------------------------------------------------------------------------
File structure:
securefilesharing
|--Server directory
|  |-make
|  |-connectToPeer.java
|  |-Peer.java
|  |-Server.java 
|--Peer1 directory    
   |-make
   |-Peer1.java
   |-Peer2Peer.java
   |-Crypt.java
   |-Serialize.java
   |-filePeer2Peer.java
   |----files2share directory
   |	|-------test1.txt
   | 	|-------test2.txt
   |	|-------test3.txt

Peer2 and Peer3 directories with similar internal file structure of Peer1.
keyBank directory - initially empty.
--------------------------------------------------------------------------
Execution details of project.

Open four terminals. (1 for Server and 3 for Peers)
At each terminal, do:
make clean
make

Run Server and so allow it to listen to Peer requests.

java Server <ServerPortNo>

At Peer1 
java Peer1 <ServerPortNo>

At Peer2 
java Peer2 <ServerPortNo>

At Peer3 (similar to other peers also):
java Peer3 <ServerPortNo>

You will see cmd> prompt.
cmd>get test1.txt
In the above fashion, transfer as many files you want.

When finished, do:
cmd>exit
-----------------------------------------------------------------------------
Extra Credit:
1. After each file transfer from peer to peer, auto update the server with latest set of files at peer.
2. If file requested at more than 1 peer then allow requesting peer to choose from which peer to receive file.
3. Efficient error handling.
----------------------------------------------------------------------------

