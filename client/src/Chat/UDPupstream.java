/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

/**
 *
 * @author grilledchops
 */

import java.io.*;
import java.lang.Thread.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
public class UDPupstream extends Thread{

    private int portNo;
    private String addrIP;
    private UDPdownstream listBack;
    public UDPupstream(String addrIP, int portNo) {
        this.addrIP = addrIP;
        this.portNo = portNo;

        listBack = new UDPdownstream(this.portNo);
        listBack.start();

        this.InitRecord();
        this.start();
    }
    TargetDataLine targetDL = null;                 // input Voice device
    private void InitRecord() {
        // encode PCM unsigned, 8000Hz, 1ch
        AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED,     // Encoding
                             6300.0F,                               // SampleRate
                             8,                                     // sampleSizeInBits
                             1,                                     // channels
                             1,                                     // frameSize
                             6300.0F,                               // frameRate
                             false);                                // bitEndian

        try {
            DataLine.Info infoT = new DataLine.Info(TargetDataLine.class, af);
            //get target line
            targetDL = (TargetDataLine) AudioSystem.getLine(infoT);
            targetDL.open(af);
            targetDL.start();
            recordFlag = true;
        } catch (Exception e) {
            System.out.println("" + e);
            throw new ArithmeticException();
        }
        System.out.println("Init Record OK...");
    }
    public void CloseTrans() {
        this.CloseRecord();
        listBack.CloseList();
        listBack.socket.close();
    }
    public void CloseRecord() {
        targetDL.close();
        this.recordFlag = false;
    }
    boolean recordFlag;

    public DatagramSocket socket;          // udp socket
    @Override
    public void run() {
        InetAddress addr = null;
        try {       // get addr
            addr = InetAddress.getByName(addrIP);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        System.out.println("start output");
        try {
            DatagramPacket packet = null;
            socket = new DatagramSocket();       // UDP socket
            do {                            // trans data from target data line
                byte[] audioData = new byte[120];
                int byteRead = targetDL.read(audioData, 0, audioData.length);       // audio Record data
                if (byteRead > 0) {
                    //System.out.println("audioData: "+new String (audioData));
                    packet = new DatagramPacket(audioData, audioData.length, addr, portNo);
                    socket.send(packet);
                    //socket.close();
                }
                /*packet = new DatagramPacket(audioData, audioData.length, addr, portNo);
                socket = new DatagramSocket();       // UDP socket
                socket.send(packet);*/
            } while(recordFlag);
            packet = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
