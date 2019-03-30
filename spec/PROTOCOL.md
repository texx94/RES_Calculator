# RES - Labo 3
Authors : Loris Gilliand - Mateo Tutic

## Client-server protocol

* **What transport protocol do we use ?**

TCP

* **How does the client find the server (addresses and ports) ?**

The client has to know the server IP and port. *Server : 10.192.105.166:2205*

* **Who speaks first ?**

The client speaks first and asks for a connection to the server.

* **What is the sequence of messages exchanged by the client and the server ?**

1 - Client to Server : Are you ready ?

2 - Server to Client : Yes, I am. Welcome.

3 - Client to Server : How much is <computation> ?

4 - Server to Client : <computation> is <result>

5 - Client to Server : BYE

Steps 3 and 4 can be repeated.

* **What happens when a message is received from the other party ?**
...

* **What is the syntax of the messages? How we generate and parse them ?**

A \<computation> is like : \<op> \<number1> \<number2>

An \<op> is like : ADD, SUB, DIV or MULT

Each message ends with \0

* **Who closes the connection and when ?**

The client asks to close the connection when he's done.
