/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author mingo
 */
public class Room {
    private Vector<Client> clientVec;
    public String history;
    public Database dataBase;

    public Room(Client c1,Client c2,Database db){
        clientVec=new Vector<Client>();
        history=new String();
        clientVec.add(c1);
        clientVec.add(c2);
        this.dataBase=db;
    }

    public Room addClient(Client newMember) {
        String roomMembers=new String();
        String newMemberName=newMember.getName();
        for(Iterator<Client> it =clientVec.iterator();it.hasNext();){
            Client oldMember= it.next();
            oldMember.renewRoomClinetList(this,newMemberName);
            roomMembers+=(oldMember.getName()+" ");
        }
        clientVec.add(newMember);
        newMember.send("/invitedby "+ roomMembers);
        return this;
    }

    public void broadcast(String speaker,String msg){
        for(int i=0;i<clientVec.size();i++)
            clientVec.get(i).receiveMessage(speaker,this,msg);
        history+=(speaker+":"+msg+"/NEWLINE/");
    }

    void removeClient(Client leaver) {
        clientVec.remove(leaver);
        String leaverName=leaver.getName();
        for(int i=0;i<clientVec.size();i++)
            this.dataBase.updateFriendData(leaverName, clientVec.get(i).getName(), "history",history);
        for(int i=0;i<clientVec.size();i++)
            clientVec.get(i).renewRoomClinetList(this,leaver.getName());
    }

    void ding() {
        for(int i=0;i<clientVec.size();i++)
            clientVec.get(i).ding(this);
    }
}
