/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.filechooser.FileNameExtensionFilter;
import ImageRelated.ImageChooser;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;
import testclient.*;
import static java.lang.System.*;


/**
 *
 * @author grilledchops
 */
public class nwkservice {

    public int port;
    public int fport;
    public String serverIP;
    public String user_id;

    private int Default_port = 9987;
    private int Default_fport = 9988;
    private String Default_ip = "140.112.18.204";
    private String Default_id;
    private String Default_passwd;
    private String user_passwd;
    private Socket socket;
    private DataOutputStream ostream;
    private DataInputStream istream;
    private Thread recvmsg_thread;
    private boolean Login = false;
    private File file;
    Timer confirm_timeout;
    public Friendlist friendlist;
    public filetransfer filemanager;
    public ImageIcon myImage;

    public nwkservice()
    {
        this.port = Default_port;
        this.fport = Default_fport;
        this.serverIP = Default_ip;
        this.filemanager = new filetransfer();
        this.myImage = new ImageIcon("image.jpg");
    }

    public nwkservice(int t_port, int t_fport, String t_ip) {
        this.port = t_port;
        this.fport = t_fport;
        this.serverIP = t_ip;
        this.filemanager = new filetransfer();
        this.myImage = new ImageIcon("image.jpg");
    }

    private nwkservice(Object object, Object object0) {
        this();
        throw new UnsupportedOperationException("msg: format not support, use default settings.");
    }

    public boolean loginas(String t_id, String t_passwd, boolean t_newb) {
        this.user_id = t_id;
        this.user_passwd = t_passwd;
        return(connectserver(t_newb));
    }

    // <editor-fold defaultstate="collapsed" desc="nwkservice methods">
    private boolean connectserver(boolean t_newb) {
        try {
            Login = true;
            socket = new Socket(InetAddress.getByName(serverIP), port);
            ostream = new DataOutputStream(socket.getOutputStream());
            istream = new DataInputStream(socket.getInputStream());
            //listens to channel
            recvmsg_thread = new Thread(new Runnable() {

                public void run() {
                    while (Login) {
                        try {
                            String str = istream.readUTF();
                            parsemsg(str);
                        } catch (IOException ex) {
                            Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            //login
            if (socket.isConnected()) {
                recvmsg_thread.start();
                if (t_newb) {
                    sendmsg("/register " + this.user_id + " " + this.user_passwd);
                } else {
                    sendmsg("/login " + this.user_id + " " + this.user_passwd);
                }
                //confirm_timeout = new Timer();
                //confirm_timeout.schedule(new timeout(), 10000);
                //confirm_timeout = new Timer();
                //confirm_timeout.schedule(new timeout(), 10000);
            } else {
                System.out.println("socket failed");
                connectserver(t_newb);
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean sendfile(final String targetid) {
        Thread filetransfer_thread = new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("File transmiting");
                    JFileChooser fc = new JFileChooser();
                    fc.setName("Send File");
                    int returnVal = fc.showDialog(null, "select");
                    System.out.println("return val " + returnVal);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        System.out.println("Approve");
                        File file = fc.getSelectedFile();
                        String filename = filemanager.packfilename(fc.getName(file));
                        long filesize = file.length();
                        sendmsg("/file" + " " + targetid + " "  + user_id + ":" + fport + ":" + InetAddress.getLocalHost().getHostAddress() +
                                ":" + filename + ":" + filesize);
                        ServerSocket fileserver = new ServerSocket(fport);
                        Socket fsocket = fileserver.accept();
                        DataInputStream dataistream = new DataInputStream(fsocket.getInputStream() );
                        DataOutputStream dataostream = new DataOutputStream(fsocket.getOutputStream() );
                        BufferedInputStream buffistream = new BufferedInputStream(new FileInputStream(file));
                        String ack = dataistream.readUTF();
                        while(!ack.equals("Deny" + filename) && !ack.equals("Confirm" + filename)) {
                            System.out.println("sendfile rcvd reply: " + ack);
                            ack = dataistream.readUTF();
                        }
                        if(ack.equals("Confirm" + filename)) {
                            progressbar SEND = new progressbar(Thread.currentThread());
                            SEND.setmission(filename, filesize, targetid, true);
                            filemanager.addmission(SEND);
                            byte [] buff = new byte [2048];
                            try {
                                int loopcount = 0;
                                while (buffistream.read(buff) != -1) {
                                    dataostream.write(buff);
                                    loopcount++;
                                    SEND.setprogress(loopcount);
                                }
                            }
                            catch (IOException ex) {
                                Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println(ex.getMessage());
                                parsemsg("/nwkserviceFailed flag_4");
                            }
                            SEND.missioncomplete();
                            buffistream.close();
                            dataostream.flush();
                            dataostream.close();
                            fileserver.close();
                            fsocket.close();
                            System.out.println("file trans. done");
                        }
                        else {
                            javax.swing.JOptionPane.showMessageDialog(NewMain.MF, "Denied!!");
                        }
                    }
                    else {
                        //abort
                        System.out.println("Abort");
                    }
                }
                catch (IOException e) {
                    System.out.println(e.toString());
                    parsemsg("/nwkserviceFailed flag_5");
                }
            }
        });
        filetransfer_thread.start();
        return true;
    }
    
    public boolean rcvdfile(final String id, final String ipaddr, final String t_filename, final int filesize, int port){
        Thread filetransfer_thread = new Thread(new Runnable() {
            public void run() {
                try {
                    if(javax.swing.JOptionPane.showConfirmDialog(NewMain.MF, "File[" + t_filename + "] transfer request from " + id + ", Accept?",
                            "File Manager", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                        JFileChooser fc = new JFileChooser();
                        fc.setName("Save File");
                        int returnVal = fc.showDialog(null, "Save");
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            String filename = fc.getName(file);
                            progressbar RECVD = new progressbar(Thread.currentThread());
                            RECVD.setmission(filename, filesize, id, false);
                            filemanager.addmission(RECVD);
                            Socket rsocket = new Socket(ipaddr, fport);
                            DataInputStream dataistream = new DataInputStream(rsocket.getInputStream());
                            DataOutputStream dataostream = new DataOutputStream(rsocket.getOutputStream());
                            BufferedOutputStream buffostream = new BufferedOutputStream(new FileOutputStream(file));
                            dataostream.writeUTF("Confirm" + t_filename);
                            byte[] buffer = new byte[2048];
                            int loopcount = 0;
                            while(dataistream.read(buffer) != -1) {
                                RECVD.setprogress(loopcount);
                                loopcount++;
                                System.out.println(loopcount);
                                buffostream.write(buffer);
                            }
                            System.out.println("done transfer");
                            RECVD.missioncomplete();
                            buffostream.flush();
                            buffostream.close();
                            dataostream.close();
                            rsocket.close();
                        }
                        else {
                            Socket rsocket = new Socket(ipaddr, fport);
                            DataOutputStream dataostream = new DataOutputStream(rsocket.getOutputStream());
                            dataostream.writeUTF("Deny" + t_filename);
                            rsocket.close();
                        }
                    }
                    else {
                        //abort
                        Socket rsocket = new Socket(ipaddr, fport);
                        DataOutputStream dataostream = new DataOutputStream(rsocket.getOutputStream());
                        dataostream.writeUTF("Deny" + t_filename);
                        rsocket.close();
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        filetransfer_thread.start();
    return true;
    }
    
    // </editor-fold>

    public Friend searchbyname(String name) {
        int index = 0;
        for(String check : friendlist.friendNameVec) {
            System.out.println(check + "\n");
            if(check.equals(name)) {
                return friendlist.friendVec.get(index);
            }
            index++;
        }
        System.out.println("nwkservice.searchbyname() Can't find friend");
        return null;
    }

    private void parsemsg(String msg) {
        System.out.println("socket received " + msg);
        String[] msgformat = msg.split(" ");
        if(msgformat[0].equals("/friendimage")) {
            int l=new Integer(msgformat[1]);
            System.out.println(l);
            byte[]imageData = new byte[l];
            try {
                istream.read(imageData, 0, l);
            } catch (IOException ex) {
                Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            //ImageIcon imageIcon =new ImageIcon(imageData);

        }
        else if(msgformat[0].equals("/badlogin")) {
            NewMain._login.LoginFailed();
            /*
             * @ grilledchops
             * for nwkservice to cancel timer
             */
  //        confirm_timeout.cancel();
        }
        else if(msgformat[0].equals("/badregister")) {
            NewMain._login.reg.RegisterFailed();
            /*
             * @ grilledchops
             * for nwkservice to cancel timer
             */
 ///        confirm_timeout.cancel();
        }
        else if(msgformat[0].equals("/ok")) {
            /*
             * @ grilledchops
             * for nwkservice to cancel timer
             */
//          confirm_timeout.cancel();

        }
        else if(msgformat[0].equals("/bad")) {

        }
        else if(msgformat[0].equals("/invitedby")) {
            java.util.ArrayList<String> members = new java.util.ArrayList<String>();
            members.addAll(Arrays.asList(msgformat));
            members.remove("/invitedby");
            NewMain.MF.invitedby(members);
        }

        else if(msgformat[0].startsWith("/renewfriendlist")) {
            String[] nameonoff = msg.split(" ");
            if(nameonoff[2].equals("online")){
                 friendlist.renewUserList(true,nameonoff[1]);
            }
            else if(nameonoff[2].equals("offline")){
                 friendlist.renewUserList(false,nameonoff[1]);
            }
        }
         else if(msgformat[0].equals("/newfriend")) {
            String[] ogn = msg.split(" ");
            byte[] a = new byte[0];
                
            NewMain.NWKSER.friendlist.newfriend(ogn[1],ogn[2],ogn[3],a);

        }
         else if(msgformat[0].equals("/renewfriendimage")) {
            try {
                int length = Integer.parseInt(msgformat[2]);
                if(length!=-1){

                byte[] imageData = new byte[length];
                //istream.read(imageData, 0, length);
                //istream.read(imageData);
                istream.readFully(imageData, 0, length);
                //System.out.println(new String(imageData));
                NewMain.NWKSER.friendlist.newimage(msgformat[1], imageData);
                }
                else{
                      ByteArrayOutputStream baos = new ByteArrayOutputStream();
                      File file=new File("images.jpg");
                      try {
                      ImageIO.write(ImageIO.read(file), "JPG", baos);
                      } catch (IOException ex) {
                        Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
                         }
                       byte[] imageData = baos.toByteArray();
                        NewMain.NWKSER.friendlist.newimage(msgformat[1],imageData);

                }
              }
                catch (IOException ex) {
                Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
              }

           }
         

      
        else if(msgformat[0].equals("/renewroom")) {
                NewMain.MF.chageofmember(msgformat[1], Integer.parseInt(msgformat[2]));
        }
        else if(msgformat[0].equals("/message")) {
            try {
                if(msgformat[1].isEmpty() || msgformat[2].isEmpty()) {
                    System.out.println("parsemsg() message null");
                }
                Integer roomid = Integer.parseInt(msgformat[1]);
                int i = msgformat[0].length()+msgformat[1].length()+msgformat[2].length()+3;
                String substring = msg.substring(i, msg.length());
                System.out.println(msgformat[2] + " " + substring + " " + roomid);
                NewMain.MF.rcvdmsg(msgformat[2], substring, roomid);
            }
            catch (NumberFormatException e) {
                NewMain.MF.rcvdmsgbyname(msgformat[2], msgformat[1]);
            }
        }
        else if(msgformat[0].equals("/nwkserviceFailed")) {
            /*
             * @ grilledchops
             * service failed, possibly cause : bad port or sever ip
             */
        }
        else if (msgformat[0].equals("/okregister")){
                 NewMain._login.reg.RegisterSuccess();
                 NewMain._login.setVisible(false);
                 friendlist = new Friendlist();
                 friendlist.setTitle(NewMain._login.reg.ApplyAccount);
                 friendlist.setVisible(true);
                 this.myImage = new ImageIcon("images.jpg");

                 
 //              confirm_timeout.cancel();
        }
        else if (msgformat[0].equals("/oklogin")){
                 NewMain._login.LoginSuccess();
                 friendlist = new Friendlist();
                 friendlist.setTitle(NewMain._login.account);
                 friendlist.setVisible(true);
                 //String namelist = msgformat[0];
                 String msgin[] = msg.split(" ", 2);
                 out.println(msgin[1]);
                 friendlist.newuserList(msgin[1]);
                 this.myImage = new ImageIcon("images.jpg");
        
   //            confirm_timeout.cancel();
        }
        else if(msgformat[0].equals("/badaddfriend")){
                 friendlist.Type.addFailed(msgformat[1]);
 //              confirm_timeout.cancel();
        }
        else if(msgformat[0].equals("/addedby")){
            String name[] = msg.split(" ");      
            for(int a = 1 ; a<=name.length-1 ; a++)
                friendlist.Invited(name[a]);
        }
        else if(msgformat[0].equals("/file")) {
            if(!msgformat[1].equals(user_id)) {
                System.out.println("wrong file receiver");
                return;
            }
            int length = user_id.length();
            String fileInfo = msg.substring(7 + length);
            filemanager.unpack(fileInfo);
            NewMain.NWKSER.filemanager.preparetransfer(fileInfo);
        }
        else if(msgformat[0].equals("/voice")) {
            NewMain.MF.CallSystem.ParsePhoneCall(msgformat[1], msgformat[2]);
        }
        else if(msgformat[0].equals("/yourimageurl")){
            String ur[] = msg.split(" ", 2);
            if(!ur[1].equals("null")){
                  this.myImage = new ImageIcon(ur[1]);
                  this.friendlist.myurl = ur[1];
            }
            else{
                  this.myImage = new ImageIcon("images.jpg");
                  this.friendlist.myurl = "images.jpg";
            }
        }
        else if(msgformat[0].equals("/kick")){
             disconnect();
             JOptionPane.showMessageDialog(NewMain.NWKSER.friendlist,"你被踢了", "", JOptionPane.PLAIN_MESSAGE);
             System.exit(0);
        }
        else if(msgformat[0].equals("/ding")){
            NewMain.MF.dingbyothers(Integer.parseInt(msgformat[1]));
        }

        else if(msgformat[0].equals("/loginTimeout")) {
            /*
             * @ grilledchops
             * NO REPLY from server
             */
        }
            /*
             * @ grilledchops
             * NO REPLY from server
             */

        else {
            System.out.println(msg + "not supported");
            /*
             * @ grilledchops
             * msg format not supported
             */
        }

    }

    public void sendmsg(String msg) {
        try {
            System.out.println("socket send: " + msg);
            ostream.writeUTF(msg);
        } catch (IOException ex) {
            Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sendByte(byte[] msg){
        try {
            System.out.println("socket send: " + msg);
            ostream.write(msg);
        } catch (IOException ex) {
            Logger.getLogger(nwkservice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void reconnect() {
        //useless
    }

    public void disconnect() {
        sendmsg("/logout");
        Login = false;
    }
/*
    public class timeout extends TimerTask {
        @Override
        public void run() {
            parsemsg("/loginTimeout");
            System.out.println("No Reply from Server");
            disconnect();
        }
    }
 *
 */
}


