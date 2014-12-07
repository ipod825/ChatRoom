/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import testclient.NewMain;

/**
 *
 * @author grilledchops
 */
public class Phone {

        Phone() {
            try {
                HOST_ADDR = java.net.InetAddress.getLocalHost().getHostAddress();
                Enumeration en = java.net.NetworkInterface.getNetworkInterfaces();
                NetworkInterface ni = null;
                InetAddress address=InetAddress.getLocalHost();
                //address.getHostAddress();
                System.out.println("wtf inetaddress"+address.getHostAddress());
            } catch (SocketException ex) {
            Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    public void call(String target) {
        if (callFlag == false) {
            try {
                String TargetAddr = target;
                //String addrIP = this.jTextField1.getText();
                //String addrIP = "127.0.0.1";
                //jTextArea1.setText("");
                // collection IP and connection IP use Port 1200 UDP protocol
                HOST_ADDR = java.net.InetAddress.getLocalHost().getHostAddress();
                UTC = new UDPupstream(TargetAddr, 1200);
                System.out.println("Connection to: " +TargetAddr);
                //this.Call.setText("disconnect");
                callFlag = true;
            } catch (UnknownHostException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            UTC.CloseTrans();
            UTC = null;
            System.out.println("Close Connect");
            //this.Call.setText("connection");
            callFlag = false;
        }
    }

    public void ParsePhoneCall(String target, String Info) {
        String[] linkInfo = Info.split(":");
        if(linkInfo[0].equals("req")) {
            int reply = javax.swing.JOptionPane.showConfirmDialog(NewMain.MF, target + "calls you", "OnlinePhone", javax.swing.JOptionPane.OK_CANCEL_OPTION);
            if(reply == javax.swing.JOptionPane.OK_OPTION) {
                NewMain.NWKSER.sendmsg("/voice" + " " + target + " " + "confirm:");
                call(linkInfo[1]);
            }
            else {
                NewMain.NWKSER.sendmsg("/voice" + " " + target + " " + "deny:");
            }
        }
        else if(linkInfo[0].equals("off")) {
            System.out.println("off");
            call(null);
        }
        else if(linkInfo[0].equals("confirm")) {
            System.out.println("confirm");
            call(linkInfo[1]);
        }
        else if(linkInfo[0].equals("deny")) {
            javax.swing.JOptionPane.showMessageDialog(NewMain.MF, "call denied", "Call Reply", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            System.out.println("denied");
        }
        else {
            System.out.println("parsemsg.msgformat[0].equals() error");
        }
    }

    private UDPupstream UTC;
    public boolean callFlag = false;
    public String HOST_ADDR;
}
