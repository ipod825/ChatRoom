/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import java.awt.Container;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author grilledchops
 */
public class filetransfer{

    public filetransfer() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("File Exchange");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        missionList = new java.util.Vector<progressbar>();

        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(layout);

        frame.pack();
        frame.setVisible(false);
    }

    public void addmission(progressbar pgb) {
        frame.setVisible(true);
        missionList.add(pgb);
        contentPane.add(pgb);
        frame.pack();
    }

    public void delmission() {
        for(int index = 0; index < missionList.size(); index++) {
            progressbar t_checkbar = missionList.get(index);
            if(t_checkbar.status.equals("canceled")) {
                missionList.remove(index);
                contentPane.remove(index);
            }
            index++;
        }
        frame.pack();
        if(missionList.isEmpty()) {
            this.frame.setVisible(false);
        }
    }

    public void preparetransfer(String linkmsg) {
        String info[] = linkmsg.split(":");
        try {
            int fport = Integer.parseInt(info[1]);
            String user_id = info[0];
            String Address = info[2];
            String filename = info[3];
            int filesize = Integer.parseInt(info[4]);
            if(user_id.isEmpty() || Address.isEmpty() || filename.isEmpty()) {
                System.out.println("file transfer link msg failure");
                return;
            }
            testclient.NewMain.NWKSER.rcvdfile(user_id, Address, filename, filesize, fport);
        }
        catch (NumberFormatException  e) {
            System.out.println("file transfer link msg failure");
        }
    }

    public String packfilename(String filename) {
        String packedname = filename.replace(" ", "(blank)");
        return packedname;
    }

    public String unpack(String filename) {
        String unpackedname = filename.replace("(blank)", " ");
        return unpackedname;
    }

    public JFrame frame;
    private Container contentPane;
    private BoxLayout layout;
    public java.util.Vector<progressbar> missionList;
}
