/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

/**
 *
 * @author grilledchops
 */    

import javax.sound.sampled.*;
import java.net.*;
import java.lang.Thread.*;
public class UDPdownstream extends Thread {

    private int portNo;
    AudioInputStream ais = null;                     // input stream
    AudioFormat af = null;                           // data format is AU file
    SourceDataLine sourceDL = null;                  // output Voice device

    public UDPdownstream(int portNo) {
        this.portNo = portNo;
        this.InitListener();
    }
    private void InitListener() {
        // encode PCM unsigned, 8000Hz, 1ch
        af = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED,     // Encoding
                             6300.0F,                               // SampleRate
                             8,                                     // sampleSizeInBits
                             1,                                     // channels
                             1,                                     // frameSize
                             6300.0F,                               // frameRate
                             false);                                // bitEndian

        try {
            DataLine.Info infoS = new DataLine.Info(SourceDataLine.class, af);
            //get source line
            sourceDL = (SourceDataLine) AudioSystem.getLine(infoS);
            sourceDL.open(af);
            sourceDL.start();
            this.listenerFlag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean listenerFlag;

    public void CloseList() {
        sourceDL.close();
        this.listenerFlag = false;
    }
    public DatagramSocket socket;
    @Override
    public void run() {
        DatagramPacket packet = null;

        try  {
            byte[] listData = new byte[120];
            socket = new DatagramSocket(this.portNo);       // start udp trans set port
            System.out.println("start receive");
            do {
                packet = new DatagramPacket(listData, listData.length);   // prepare receive packet
                socket.receive(packet);                                   // blocking waiting receive
System.out.println("listData: "+new String (listData));
                sourceDL.write(listData, 0, listData.length);                     // output to headphone
                //String msg = new String(buf, 0, packet.getLength());
                //System.out.println(msg);
                System.out.println(new String(listData));
            }while (listenerFlag);
            packet = null;
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


