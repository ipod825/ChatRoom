package server;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.net.*;

/**
 * uSocket chatroom server class
 * @author StarryDawn
 */
public class Server {
	private ServerSocket serverSocket;
	private int port;
	Vector<Client> clientVec;
        Vector<String> clientNameVec;
        Vector<Room> roomVect;
	private int id; // cumulate client ids
	private Console console;
        private Database database;

        public Database getDatabase(){return database;}

	public Server() throws ClassNotFoundException, SQLException {
            clientVec = new Vector<Client>();
            clientNameVec=new Vector<String>();
            roomVect=new Vector<Room>();
            console = new Console( this );
            console.setVisible(true);
            database=new Database(this);
            port=9987;
            
            System.out.println("port: " + port);
            try {
                serverSocket = new ServerSocket(port);
                console.addHistory( "Server created." );
                console.addHistory( "Waiting for client." );
                while(true) {
                    synchronized(this) {
                        Socket clientSocket = serverSocket.accept();
                        Client c=new Client(this,clientSocket);
                        clientVec.addElement(c);
                        clientNameVec.add(c.getName());
                    }
                    Thread thd = new Thread( clientVec.lastElement() );
                    thd.start();
                    }
		}
                catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

        public void printMsg( String msg ) {console.addHistory(msg);}

        public void clientLogin(Client c, String requester) {
            Vector<String>  friendList=database.getFriendList(requester);
            for(int i=0;i<friendList.size();i++){
                Client cli=this.getClient(friendList.get(i));
                if(cli!=null)
                    cli.renewFriendList(requester, "online");
            }
            clientVec.add(c);
            clientNameVec.add(requester);
        }

        public Room addRoom(Client c1,Client c2){
            Room room=new Room(c1,c2,this.database);
            System.out.println(room.history);
            roomVect.add(room);
            return room;
        }

        public void removeClient(Client c){
            clientVec.remove(c);
            clientNameVec.remove(c.getName());
        }


        public Client getClient(String name){
            if(clientNameVec.contains(name))
                return clientVec.get((clientNameVec.indexOf(name)));
            return null;
        }

    public boolean isClientOnline(String target) {
        return (clientNameVec.contains(target));
    }
}
