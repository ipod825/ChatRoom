/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import gui.Friend;
import gui.Group;
import testclient.*;
import javax.swing.*;
import java.util.*;
import java.util.ArrayList;
/**
 *
 * @author grilledchops
 */
public class InviteList extends javax.swing.JFrame {

    /** Creates new form ChooseFriend */
    public InviteList(MainFrame parent) {
        initComponents();
    }

    private void initComponents() {

        Cancel = new javax.swing.JButton();
        UserBox = new javax.swing.JComboBox();
        Invite = new javax.swing.JButton();
        Onlines = new java.util.Vector<String>();
        rmindex = -1;

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        Invite.setText("Invite");
        Invite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InviteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 151, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(UserBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Invite, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Cancel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UserBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Invite)
                    .addComponent(Cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }  

    private void getonlinefriend() {
        for(Group t_group : NewMain.NWKSER.friendlist.groupVec) {
            for(Friend t_friend : t_group.onlinefriendVec) {
                Onlines.add(t_friend.name);
            }
        }
    }

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void InviteActionPerformed(java.awt.event.ActionEvent evt) {
        target = UserBox.getSelectedItem().toString();
        if(rmindex == -1) {
            NewMain.MF.UnsafeInvite();
        }
        else {
            NewMain.NWKSER.sendmsg("/invite" + " " + NewMain.NWKSER.searchbyname(target).name + " " + rmindex);
        }
        this.setVisible(false);
    }

    private void setAvailible() {
        for(String t_onlines : Onlines) {
            UserBox.addItem(t_onlines);
        }
    }

    public void Invoke(Room rm) {
        UserBox.removeAllItems();
        Onlines.removeAllElements();
        getonlinefriend();
        rmindex = rm.roomIndex;
        //int t_index = 0;
        for(Friend t_member : rm.member) {
            for(int i = 0; i < Onlines.size(); i++) {
                String check = Onlines.get(i);
                if(check.equals(t_member.name)) {
                    Onlines.remove(check);
                }
            }
        }
        setAvailible();
        this.setVisible(true);
    }

    public void loadtest() {

    }

    public String target;
    private int rmindex;
    private Vector<String> Onlines;
    private javax.swing.JButton Cancel;
    private javax.swing.JButton Invite;
    private javax.swing.JComboBox UserBox;
}
