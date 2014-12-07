/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mingo
 */
public class Database {
    private Connection conn;
    private Server server;
   // Statement statement;
    public Database(Server s) throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");
        conn =  DriverManager.getConnection("jdbc:sqlite:account.db");
        this.server=s;
        Statement statement = conn.createStatement();
        statement.executeUpdate("create table if not exists account_tb(account primary key,passwd,image,imageurl)");
    }
    public synchronized boolean register(String account,String passwd){
        try {
            String tb = account + "_tb";
            Statement statement = conn.createStatement();
            //ResultSet rs = statement.executeQuery("select * from account_tb where account=" + "\"" + account + "\"");
            //if(rs.getString("account")!=null)
             //   return false;
            statement.executeUpdate("insert into account_tb values(\"" + account + "\",\"" + passwd + "\",NULL,NULL)");
            statement.executeUpdate("create table " + tb+"(name primary key, groupname ,history,offlinemsg)");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean checkIdentity(String account,String passwd){
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from account_tb where account=" + "\"" + account + "\"");
            if(!rs.next())
                return false;
            if (account.equals(rs.getString("account")) && passwd.equals(rs.getString("passwd")))
                return true;
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean hasaccount(String account){
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from account_tb where account=" + "\"" + account + "\"");
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public synchronized void addFriend(String requester,String target, String groupName){
        try {
            Statement statement = conn.createStatement();
            String requestertb = requester + "_tb";
            String targettb= target + "_tb";
            if(hasFriend(target,requester))
                statement.executeUpdate(
                "insert into " + requestertb + " values(\"" + target + "\",\"" + groupName +"\",NULL,NULL)");
            else{
            statement.executeUpdate(
                "insert into " + requestertb + " values(\"" + target + "\",\"" + groupName +"\",NULL,\"/UNACCEPTED/\")");
                statement.executeUpdate(
                "insert into " + targettb + " values(\"" + requester + "\",NULL,NULL,\"/NEWFRIEND/\")");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean hasFriend(String requester,String friendName){
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            ResultSet rs = statement.executeQuery(
                    "select * from " + tb + " where name=\"" + friendName + "\" and offlinemsg!=\"/UNACCEPTED/\" and offlinemsg!=\"/NEWFRIEND/\"");
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public synchronized void deleteFriendData(String requester,String column,String columnValue) {
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            statement.executeUpdate(
                    "delete from " + tb + " where " + column +"=\"" + columnValue + "\"");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }




    public synchronized void updateFriendData(String requester,String target,String column,String newData) {
        String tb = requester + "_tb";
        
        try {
            Statement statement = conn.createStatement();
            if(column.equals("offlinemsg") && !newData.equals("")){
                ResultSet rs = statement.executeQuery("select * from " + tb +" where name=\"" + target+"\"");
                String oldoffline=rs.getString("offlinemsg");
                if(!oldoffline.equals("")){
                    newData=oldoffline+"/NEWLINE/"+newData;
                    String str="update " + tb + " set "+ column + "=\""  + newData + "\" where name=\""+target+"\"";
                    System.out.println(str);
                    statement.executeUpdate(str);
               }


            }
            if(newData==null)
                newData="NULL";
            else
                newData="\""+newData+"\"";
            statement.executeUpdate(
                    "update " + tb + " set "+ column + "=" + newData + " where name=\"" + target+"\"");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String  getStartingList(String requester){
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            ResultSet imageRs;
            String onlinelist = "";
            String offlinelist = "";
            String oneFriendData;
            Vector<String> friendList=this.getFriendList(requester);
            ResultSet rs = statement.executeQuery(
                    "select * from " + tb + " where offlinemsg!=\"/NEWFRIEND/\" and offlinemsg!=\"/UNACCEPTED/\"");

            while (rs.next()) {
                System.out.println("requester's friend"+rs.getString("name"));
                String off=rs.getString("offlinemsg");
                oneFriendData=rs.getString("groupname")+":"+rs.getString("name")+":"+off;
                if(server.isClientOnline(rs.getString("name")))
                    onlinelist+=(oneFriendData+"/next/");
                else
                    offlinelist+=(oneFriendData+"/next/");
            }
            if(!onlinelist.equals(""))
                onlinelist=onlinelist.substring(0, onlinelist.lastIndexOf("/next/"));
            if(!offlinelist.equals(""))
                offlinelist=offlinelist.substring(0, offlinelist.lastIndexOf("/next/"));
            statement.executeUpdate(
                    "update "+tb+" set offlinemsg=\"\" where offlinemsg!=\"/NEWFRIEND/\" and offlinemsg!=\"/UNACCEPTED/\"" );

            return (onlinelist+"/offline/"+offlinelist);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Vector<String> getFriendList(String requester) {
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            ResultSet rs = statement.executeQuery("select * from " + tb);
            Vector<String> friendList = new Vector<String>();
            while (rs.next()){
                System.out.println("getFriendList: "+rs.getString("name"));
                String friendName=rs.getString("name");
                if(hasFriend(friendName,requester))
                    friendList.add(rs.getString("name"));
            }
            return friendList;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getNewFriends(String requester) {
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            String str="select * from " + tb + " where offlinemsg=\"/NEWFRIEND/\"";
            ResultSet rs = statement.executeQuery(str);
            String newFriends="";
            while (rs.next())
                newFriends += (rs.getString("name")+" ");
            statement.executeUpdate("update " +tb+" set offlinemsg=NULL where offlinemsg=\"/NEWFRIEND/\"");
            if(!newFriends.equals(""))
                newFriends=newFriends.substring(0, newFriends.length()-1);
            return newFriends;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String selectAccountData(String account,String column) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "select * from account_tb where account=\"" +account+ "\"");
            if(rs.next())
                return rs.getString(column);
            else
                return  new String();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    public byte[] getImage(String account) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from account_tb where account=\"" + account+"\"");
            if(rs.getBytes("image")==null)
                return null;
            else
                return rs.getBytes("image");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    void changeImage(String account,byte[] image,String imageUrl) {
        try {
             PreparedStatement prep = conn.prepareStatement("update account_tb set image=? where account=?") ;
             prep.setBytes(1, image);
             prep.setString(2, account);
             prep.executeUpdate();

             prep = conn.prepareStatement("update account_tb set imageurl=? where account=?") ;
             prep.setString(1, imageUrl);
             prep.setString(2, account);
             prep.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFriendData(String requester, String friendName,String column) {
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            ResultSet rs = statement.executeQuery("select * from " + tb + " where name=\"" + friendName + "\"");
            return rs.getString(column);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    void addFriend(String name, String friendName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void aggreeAddedby(String requester, String target, String groupName) {
        updateFriendData(requester,target,"offlinemsg","");
        updateFriendData(requester, target, "groupname", groupName);
        updateFriendData(target,requester,"offlinemsg","");
    }

    public boolean isUnacceptedFriend(String requester, String target) {
        try {
            Statement statement = conn.createStatement();
            String tb = requester + "_tb";
            ResultSet rs = statement.executeQuery("select offlinemsg from " + tb + " where name=\"" + target + "\"");
            if(rs.next()){
                updateFriendData(requester, target, "offlinemsg","");
                return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
