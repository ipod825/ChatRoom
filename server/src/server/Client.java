/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mingo
 */
public class Client implements Runnable{
    private Socket socket;
    private Server server;
    private String name;
    
    private DataInputStream in;
    private DataOutputStream out;
    private Vector<Room> roomVec;
    private boolean isRunnable;

    public String getName(){return name;}
    public Room getRoom(String roomId){return roomVec.get(new Integer(roomId));}

    public Client(Server ser, Socket soc){
        server=ser;
        socket=soc;
        isRunnable=true;
        roomVec=new Vector<Room>();
        try{
            in = new DataInputStream( socket.getInputStream() );
            out = new DataOutputStream( socket.getOutputStream() );
        }
        catch(IOException e){}
    }
    
    public boolean login(){
        try {
            String request = in.readUTF();
            String[] s = request.split(" ", 3);
            if (s[0].equals("/register")) {
                if (server.getDatabase().register(s[1], s[2])) {
                    name = s[1];
                    server.clientLogin(this, name);
                    send("/okregister");
                    return true;
                } else {
                    send("/badregister");
                    return false;
                }
            }
            else if (s[0].equals("/login")) {
                if (server.getDatabase().checkIdentity(s[1], s[2])) {
                    name = s[1];
                    Client cli=server.getClient(name);
                    if(cli!=null){
                        server.removeClient(cli);
                        cli.send("/kick");
                        cli.isRunnable=false;
                    }
                    String startingList="/oklogin " +server.getDatabase().getStartingList(this.name);
                    send(startingList);
                    send("/yourimageurl "+server.getDatabase().selectAccountData(this.name, "imageurl"));
                    String newFriends=server.getDatabase().getNewFriends(this.name);
                    if (!newFriends.equals(""))
                        send("/addedby "+newFriends);
                    server.clientLogin(this, name);
                    Thread thd = new Thread(new Runnable(){
                        public void run(){
                            Vector<String> friendList = server.getDatabase().getFriendList(name);     
                            for (Iterator<String> it = friendList.iterator(); it.hasNext();){
                                String friendName=it.next();
                                if(server.isClientOnline(friendName)){
                                    server.getClient(friendName).sendImage(name);
                                    sendImage(friendName);
                                }
                            }
                        }
                    });
                    thd.start();
                    return true;
                }
                else {
                    send("/badlogin");
                    return false;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void send(String reply){
        System.out.println("Reply to "+this.name+":"+reply);
    	try {out.writeUTF(reply);}
        catch(SocketException e){
            System.out.println("Damn it SocketException at send()************************");
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void sendImage(String friendName){
        try {
            byte[] image = server.getDatabase().getImage(friendName);
            if(image!=null){
                out.writeUTF("/renewfriendimage "+friendName+" "+image.length);
                out.write(image, 0, image.length);
            }
            else
                out.writeUTF("/renewfriendimage "+friendName+" "+"-1");
        }
        catch(SocketException e){
            System.out.println("Damn it SocketException at sendImage*********************");
        }
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        while (!login()){}
        while(isRunnable){
            try {
                String str=in.readUTF();
                parseRequest(str);
            } catch (IOException e) {}
        }
    }

    public void speak(String target,String speaker,String msg){
        Integer i=null;
        try{
        i=Integer.parseInt(target);
        }
        catch (NumberFormatException e){
            if(server.isClientOnline(target)){
                Client friend=server.getClient(target);
                Room room=server.addRoom(this,friend);
                this.roomVec.add(room);
                friend.roomVec.add(room);
                this.send("/message "+roomVec.indexOf(room)+" "+speaker+" "+msg);
                friend.send("/message "+friend.roomVec.indexOf(room)+" "+speaker+" "+msg);
            }
            else
                server.getDatabase().updateFriendData(target,this.name,"offlinemsg",msg);
            return;
        }
            roomVec.get(i).broadcast(this.name,msg);
    }

    public void receiveMessage(String speaker,Room r,String msg){
        Integer i=roomVec.indexOf(r);
        send("/message " + i.toString()+" "+speaker+" "+msg);

    }

    public void inviteFriend(String friendName, int roomId){
        Client friend=server.getClient(friendName);
        Room r=roomVec.get(roomId).addClient(friend);
        friend.roomVec.add(r);
    }
    
    public void exitRoom(int roomId){
        roomVec.get(roomId).removeClient(this);
        roomVec.remove(roomId);
    }


    public void logout(){
        for(int i=0;i<roomVec.size();i++)
            roomVec.get(i).removeClient(this);
        Vector<String> friendList = server.getDatabase().getFriendList(name);     
        for (int i=0;i<friendList.size();i++){
           Client friend=server.getClient(friendList.get(i));
           if(friend!=null)
                friend.renewFriendList(this.name, "offline");
        }
        server.removeClient(this);
        isRunnable=false;
    }


    public void addFriend(String friendName, String groupName) {
        Database db=server.getDatabase();
        if(db.hasFriend(this.name,friendName))
            send("/badaddfriend "+"hasadded");
        else{
            if(!db.hasaccount(friendName))
                send("/badaddfriend "+ "notexist");
            else{
                String isOnline="online";
                if(!server.isClientOnline(friendName))
                    isOnline="offline";
                if(server.getDatabase().hasFriend(friendName,this.name)){
                    send("/newfriend "+isOnline+" "+groupName+" "+friendName);
                    sendImage(friendName);
                }
                else if(server.isClientOnline(friendName))
                    server.getClient(friendName).send("/addedby " + this.name);
                db.addFriend(this.name, friendName, groupName);
            }
        }
    }


    public void deleteFriendData(String column,String columnValue) {
        server.getDatabase().deleteFriendData(this.name,column,columnValue);
    }


    public void renewFriendList(String friendName,String isOnline){
        if(server.isClientOnline(friendName))
            send("/renewfriendlist "+ friendName + " "+isOnline);
    }

    public void renewRoomClinetList(Room r,String newMember){
        Integer i=roomVec.indexOf(r);
        send("/renewroom "+ newMember+" "+ i.toString());
    }

    public void agreeAddedBy(String friendName,String groupName){
        Database db=server.getDatabase();
        db.aggreeAddedby(this.name,friendName,groupName);
        String isOnline="online";
        if(!server.isClientOnline(friendName))
            isOnline="offline";
        this.send("/newfriend "+isOnline+" "+groupName+" "+friendName);
        if(server.isClientOnline(friendName))
           this.sendImage(friendName);
        if(server.isClientOnline(friendName)){
            Client friend=server.getClient(friendName);
            friend.send("/newfriend online "+db.getFriendData(friendName,this.name,"groupname")+" "+this.name);
            friend.sendImage(this.name);
        }
    }


    public void changeFriendGroup(String friendName, String groupName) {
        server.getDatabase().updateFriendData(this.name,friendName,"groupname",groupName);
    }

    private void changeImage(String imageLength,String imageUrl) {
        try {
            int length = Integer.parseInt(imageLength);
            byte[] imageData = new byte[length];
            in.readFully(imageData, 0, length);
            server.getDatabase().changeImage(this.name, imageData, imageUrl);
            Vector<String> friendList = server.getDatabase().getFriendList(this.name);
            System.out.println(friendList.size());
            for (int i=0;i<friendList.size();i++) {
                Client friend = server.getClient(friendList.get(i));
                if (friend != null)
                    friend.sendImage(this.name);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void receveFile(String target, String fileInfo) {
        server.getClient(target).send("/file "+target+" "+fileInfo);
    }

    public void voiceInvite(String friendName, String voiceInfo) {
        String ip=socket.getInetAddress().toString().substring(1);
        server.getClient(friendName).send("/voice "+this.name+" "+voiceInfo+ip);
    }

    public void ding(String target){
        int roomId=Integer.parseInt(target);
        this.roomVec.get(roomId).ding();
    }

    public void ding(Room room){
        send("/ding "+roomVec.indexOf(room));
    }

    public void parseRequest(String request) throws IOException{
        System.out.println("Receive: "+request+"from"+this.name);
        String[] s=request.split(" ",4);
        String op=s[0];
        if(op.equals("/speak"))
            speak(s[1],s[2],s[3]);
        else{
            s=request.split(" ",3);
            if(op.equals("/invite"))
                inviteFriend(s[1], new Integer(s[2]));
            else if(op.equals("/addfriend"))
                addFriend(s[1],s[2]);
            else if(op.equals("/changegroup"))
                changeFriendGroup(s[1],s[2]);
            else if(op.equals("/okaddedby"))
                agreeAddedBy(s[1],s[2]);
            else if(op.equals("/changeimage"))
                changeImage(s[1],s[2]);
            else if(op.equals("/file"))
                receveFile(s[1],s[2]);
            else if(op.equals("/voice"))
                voiceInvite(s[1],s[2]);
            else if(op.equals("/ding"))
                ding(s[1]);
            else{
                s=request.split(" ",2);
                if(op.equals("/removefriend"))
                    deleteFriendData("name",s[1]);
                else if(op.equals("/removegroup"))
                    deleteFriendData("groupname",s[1]);
                else if(op.equals("badaddedby"))
                    deleteFriendData("name",s[1]);
                else if(op.equals("/exitroom"))
                    exitRoom(new Integer(s[1]));
                else if(op.equals("/history"))
                    getHistory(s[1]);
                else if(op.equals("/logout"))
                    logout();
                else
                System.out.println("op not supported");
                }
        }
    }

    private void getHistory(String target) {
        server.getDatabase().getFriendData(name, target,"history");
    }

}