/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 2011/3/19, 下午 11:03:12
 */

package Chat;

import gui.Friend;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import testclient.NewMain;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

/**
 *
 * @author grilledchops
 */
public class MainFrame extends javax.swing.JFrame {

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
    }
    
    private void initComponents() {

        Book = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        Send = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextBox = new javax.swing.JTextPane();
        Invite = new javax.swing.JLabel();
        Leave = new javax.swing.JLabel();
        History = new javax.swing.JLabel();
        ColorSelect = new javax.swing.JLabel();
        Font = new javax.swing.JLabel();
        File = new javax.swing.JLabel();
        Call = new javax.swing.JLabel();
        Poke = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        SelfIcon = new javax.swing.JLabel();
        StatTitle = new javax.swing.JLabel();
        CLOSEWINDOW = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        RoomPage = new java.util.ArrayList<javax.swing.JTextPane>();
        rooms = new java.util.ArrayList<Room>();
        invitelist = new InviteList(this);
        TextEditor = new java.util.ArrayList<Display>();
        display = new javax.swing.text.DefaultStyledDocument();
        CallSystem = new Phone();
        Editor = new Display(TextBox);
        RM_INDEX_COUNT = -1;
        isEnterPressed = false;
        COLOR = Color.BLACK.getRGB();

        FriendIcon = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                we.equals(WindowEvent.WINDOW_CLOSING);
                //System.out.println("window closing");
                for(int i = 0; i < RoomPage.size(); i++) {
                    leaveroom();
                }
            }
        });

        this.getContentPane().setBackground(Color.white);
        
        Book.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        Book.setBackground(Color.WHITE);
        Book.setFont(new java.awt.Font("新細明體", 1, 14));
        Book.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BookMouseClicked(evt);
            }
        });

        FriendIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        FriendIcon.setMaximumSize(new java.awt.Dimension(20, 20));
        FriendIcon.setMinimumSize(new java.awt.Dimension(20, 20));
        FriendIcon.setPreferredSize(new java.awt.Dimension(20, 20));

        StatTitle.setBackground(Color.WHITE);
        StatTitle.setFont(new java.awt.Font("新細明體", 1, 18));
        StatTitle.setIcon(null);

        jScrollPane1.setAutoscrolls(true);
        CLOSEWINDOW.setIcon(new ImageIcon(getClass().getResource("/Chat/Exit.png")));
        CLOSEWINDOW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CLOSEWINDOWMouseClicked(evt);
            }
        });

        jLabel16.setIcon(null);

        jPanel1.setBackground(Color.WHITE);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(FriendIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StatTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(CLOSEWINDOW, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FriendIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
            .addComponent(StatTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(CLOSEWINDOW, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
        );

        Send.setFont(new java.awt.Font("Calibri", 1, 18));
        Send.setText("Send");
        Send.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SendMouseClicked(evt);
            }
        });

        TextBox.setFont(new java.awt.Font("新細明體", 1, 14));
        TextBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        TextBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InputPaneKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                InputPaneKeyReleased(evt);
            }

            private void InputPaneKeyPressed(KeyEvent evt) {
                if (evt.getKeyChar() == '\n') {
                    isEnterPressed = true;
                }
            }

            private void InputPaneKeyReleased(KeyEvent evt) {
                if (evt.getKeyChar() == '\n') {
                    if(TextBox.getText() != null) {
                        String CheckInput = TextBox.getText();
                        if(!CheckInput.startsWith("\n")) {
                            NewMain.NWKSER.sendmsg(packmsg(CheckInput.replace("\n", "")));
                        }
                    }
                    TextBox.setText(null);
                    for(Friend fr : rooms.get(Book.getSelectedIndex()).member) {
                        //System.out.println("room member:" + fr.name);
                    }
                }
            }
        });
        jScrollPane1.setViewportView(TextBox);
        Invite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/invite.png")));
        Invite.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        Invite.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InviteMouseClicked(evt);
            }
        });

        Leave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/ByeBye.png")));
        Leave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        Leave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LeaveMouseClicked(evt);
            }
        });

        History.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/history.png")));
        History.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        History.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HistoryMouseClicked(evt);
            }
        });

        ColorSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/Color.png")));
        ColorSelect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        ColorSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ColorSelectMouseClicked(evt);
            }
        });

        Font.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/Font.png")));
        Font.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        Font.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FontMouseClicked(evt);
            }
        });

        File.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/File.png")));
        File.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        File.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FileMouseClicked(evt);
            }
        });

        Call.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/call.png")));
        Call.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        Call.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CallMouseClicked(evt);
            }
        });

        Poke.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Chat/yell.png")));
        Poke.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));
        Poke.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PokeMouseClicked(evt);
            }
        });

        jLabel11.setText("jLabel1");

        SelfIcon.setIcon(null);
        SelfIcon.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Book, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(SelfIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Invite)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Leave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(History)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ColorSelect)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Font)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(File)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Call)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Poke)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Send, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Book, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(Send, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Invite, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Leave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(History, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ColorSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Font, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(File, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Call, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Poke, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(SelfIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }

    private void InviteMouseClicked(java.awt.event.MouseEvent evt) {
        invitelist.Invoke(rooms.get(Book.getSelectedIndex()));
    }

    private void LeaveMouseClicked(java.awt.event.MouseEvent evt) {
        leaveroom();
    }

    private void HistoryMouseClicked(java.awt.event.MouseEvent evt) {
        TextEditor.get(Book.getSelectedIndex()).NewLineStyle("No History Availible.", "System", "Error");
    }

    private void ColorSelectMouseClicked(java.awt.event.MouseEvent evt) {
        colorselect();
    }

    private void FontMouseClicked(java.awt.event.MouseEvent evt) {
        fontselect();
    }

    private void SendMouseClicked(java.awt.event.MouseEvent evt) {
        if(TextBox.getText() != null) {
            NewMain.NWKSER.sendmsg(packmsg(TextBox.getText()));
        }
        TextBox.setText(null);
        for(Friend fr : rooms.get(Book.getSelectedIndex()).member) {
            //System.out.println("room member:" + fr.name);
        }
    }

    private void FileMouseClicked(java.awt.event.MouseEvent evt) {
        if(rooms.get(getRmIndex(Book.getSelectedIndex())).member.size() == 1) {
            NewMain.NWKSER.sendfile(rooms.get(getRmIndex(Book.getSelectedIndex())).member.get(0).name);
            TextEditor.get(Book.getSelectedIndex()).NewLineStyle("FileManager File Transfer ...", "System", "Message");
        }
        else {
            TextEditor.get(Book.getSelectedIndex()).NewLineStyle("Can't Send File in This Room!!", "System", "Error");
        }
    }

    private void CallMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if(rooms.get(Book.getSelectedIndex()).member.size() == 1) {
            if(CallSystem.callFlag) {
                NewMain.NWKSER.sendmsg("/voice" + " " + rooms.get(Book.getSelectedIndex()).member.get(0).name + " " + "off:");
                CallSystem.call(null);
            }
            else {
                //System.out.println("call");
                NewMain.NWKSER.sendmsg("/voice" + " " + rooms.get(Book.getSelectedIndex()).member.get(0).name + " " + "req:");
            }
        }
    }

    private void PokeMouseClicked(java.awt.event.MouseEvent evt) {
        NewMain.NWKSER.friendlist.ding();
        if(rooms.get(Book.getSelectedIndex()).isInitial) {
            TextEditor.get(Book.getSelectedIndex()).NewLineStyle("DO NOT DING!!", "System", "Message");
        }
        else {
            NewMain.NWKSER.sendmsg("/ding" + " " + rooms.get(Book.getSelectedIndex()).roomIndex);
        }
    }

    private void BookMouseClicked(java.awt.event.MouseEvent evt) {
        if(rooms.get(Book.getSelectedIndex()).member.size() == 1) {
            setFriendIcon();
            setStatTitle();
            FriendIcon.setVisible(true);
        }
        else {
            FriendIcon.setIcon(new ImageIcon(getClass().getResource("/Chat/Room.png")));
        }
    }

    private void CLOSEWINDOWMouseClicked(java.awt.event.MouseEvent evt) {
        //System.out.println("window closing");
        for(int i = 0; i < RoomPage.size(); i++) {
            leaveroom();
        }
        this.setVisible(false);
    }

    private void setIcon() {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(NewMain.NWKSER.myImage.getImage(), 0, 0, 100,100 ,null);
        ImageIcon packedIcon = new ImageIcon(bi);
        SelfIcon.setIcon(packedIcon);
    }

    private void setFriendIcon() {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        for(int i = 0; i < NewMain.NWKSER.friendlist.friendVec.size(); i++) {
            if(NewMain.NWKSER.friendlist.friendVec.get(i).equals(rooms.get(Book.getSelectedIndex()).member.get(0))) {
                g.drawImage(NewMain.NWKSER.friendlist.friendVec.get(i).imageIcon.getImage(), 0, 0, 100,100 ,null);
                return;
            }
        }
        ImageIcon packedIcon = new ImageIcon(bi);
        FriendIcon.setIcon(packedIcon);
    }

    private void setTabTitle(int tab_index) {
        String rmTitle = String.valueOf(tab_index) + ".";
        for(int i = 0; i < rooms.get(tab_index).member.size(); i++) {
            rmTitle += " " + rooms.get(tab_index).member.get(i).name;
            //System.out.println(rooms.get(tab_index).member.get(i).name);
        }
        if(rmTitle.length() <= 12) {
            rmTitle = (rmTitle + "　　　　　　　　").substring(0, 9);
        }
        else {
            rmTitle = rmTitle.substring(0, 9) + "..";
        }
        Book.setTitleAt(tab_index, rmTitle) ;
    }

    private void setStatTitle() {
        int tab_index = Book.getSelectedIndex();
        String statTitle = String.valueOf(tab_index);
        for(int i = 0; i < rooms.get(tab_index).member.size(); i++) {
            statTitle += " [" + rooms.get(tab_index).member.get(i).name + "]";
        }
        StatTitle.setText(statTitle);
    }

    private void colorselect() {
        Color colorPrefer = javax.swing.JColorChooser.showDialog(this, "Choose color.", new Color(COLOR));
            if( colorPrefer != null ) {
                COLOR = colorPrefer.getRGB();
                Editor.DisplayColor(TextBox.getText(), colorPrefer.getRGB());
            }
    }

    private void fontselect() {
        
    }

    //OK
    private int requestIndex() {
        RM_INDEX_COUNT++;
        //System.out.println("rmc+" + RM_INDEX_COUNT);
        return RM_INDEX_COUNT;
    }

    //OK
    private void syncIndex(int tab_index, int rm_index) {
        int i = 0;
        for(Room rm : rooms) {
            if(i >= tab_index) {
                rm.tabindex--;
            }
            if(rm.roomIndex > rm_index) {
                rm.roomIndex--;
            }
        }
        if(!rooms.get(tab_index).isInitial) {
            RM_INDEX_COUNT--;
        }
        //System.out.println("rmc-" + RM_INDEX_COUNT);
    }

    //OK
    private int getTabIndex(int rm_index) {
        int returnVal = -1;
        for(Room rm : rooms) {
            if(rm.roomIndex == rm_index) {
                returnVal = rm.tabindex;
            }
        }
        return returnVal;
    }

    //OK
    private int getRmIndex(int tab_index) {
        int returnVal = -1;
        for(Room rm : rooms) {
            if(rm.tabindex == tab_index) {
                returnVal = rm.roomIndex;
            }
        }
        return returnVal;
    }

    //OK
    private boolean MutiPageInsertDetect(Friend t_friend) {
        //System.out.println(t_friend);
        for(Room rmcheck : rooms) {
            if(rmcheck.member.size() == 1 && rmcheck.member.get(0).name.equals(t_friend.name)) {
                //System.out.println("MutiPageInsertDetect");
                return true;
            }
        }
        return false;
    }
    
    //OK do MutiPageInsertDetect first
    private Room getExcessRoom(Friend t_friend) {
        int i = 0;
        for(Room rmcheck : rooms) {
            if(rmcheck.member.size() == 1 && rmcheck.member.get(0).name.equals(t_friend.name)) {
                return rmcheck;
            }
        }
        return null;
    }

    //OK
    public void speakto(Friend t_friend) {
        if(MutiPageInsertDetect(t_friend)) {
            return;
        }
        else {
            insertpage(t_friend);
        }
    }

    //OK
    //initial a page component framepage(tab), room
    public void insertpage(Friend t_friend) {
        setIcon();
        //System.out.println("insertpage");
        javax.swing.JScrollPane frame = new javax.swing.JScrollPane();
        javax.swing.JTextPane page = new javax.swing.JTextPane();
        java.awt.Dimension d_frame = new java.awt.Dimension(656,375);
        frame.setSize(d_frame);
        frame.setAutoscrolls(true);
        frame.setPreferredSize(d_frame);
        frame.setMaximumSize(d_frame);
        page.setSize(d_frame);
        page.setFont(new java.awt.Font("新細明體", 1, 14));
        page.setPreferredSize(d_frame);
        page.setMaximumSize(d_frame);
        page.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        frame.setViewportView(page);
        page.setEditable(false);
        RoomPage.add(page);
        TextEditor.add(new Display(page));
        Book.add("room " + rooms.size(), frame);
        //allow null page insert
        if(t_friend == null) {
            rooms.add(new Room());
            //System.out.println("Chat.MainFrame.insertpage(Friend) / null parameter");
        }
        else {
            rooms.add(new Room(RoomPage.size()-1, t_friend));
        }
        setTabTitle(RoomPage.size()-1);
        setStatTitle();
        this.setVisible(true);
    }

    //OK
    public void invitedby(java.util.ArrayList<String> t_members) {
        insertpage(null);
        Room rm = rooms.get(rooms.size() - 1);
        for(String t_member : t_members) {
            rm.member.add(NewMain.NWKSER.searchbyname(t_member));
        }
        rm.roomIndex = requestIndex();
        rm.isInitial = false;
        rm.tabindex = RoomPage.size() - 1;
        setTabTitle(RoomPage.size()-1);
        setFriendIcon();
        setStatTitle();
    }

    public void UnsafeInvite() {
        TextEditor.get(Book.getSelectedIndex()).NewLineStyle("Invitation denied", "System", "Message");
        TextBox.setText("Enter...");
        TextBox.selectAll();
    }

    //OK
    public void leaveroom() {
        if(!rooms.get(Book.getSelectedIndex()).isInitial) {
            NewMain.NWKSER.sendmsg("/exitroom" + " " + rooms.get(Book.getSelectedIndex()).roomIndex);
        }
        if(rooms.size() == 1)
        {
            this.setVisible(false);
        }
        removepage(Book.getSelectedIndex());
    }

    //OK
    private void removepage(int tab_index) {
        syncIndex(tab_index, rooms.get(tab_index).roomIndex);
        Book.remove(tab_index);
        RoomPage.remove(tab_index);
        TextEditor.remove(tab_index);
        rooms.remove(tab_index);
    }

    //OK
    private String packmsg(String plainmsg) {
        String packedmsg = null;
        boolean isOffline = false;
        if(rooms.get(Book.getSelectedIndex()).isInitial) {
            String temp = rooms.get(Book.getSelectedIndex()).member.get(0).name;
            packedmsg = "/speak" + " " + temp + " " + NewMain.NWKSER.user_id + " " + TextEditor.get(Book.getSelectedIndex()).DocEnCode(plainmsg, COLOR);
            for(int i = 0; i < NewMain.NWKSER.friendlist.groupVec.size(); i++) {
                if(NewMain.NWKSER.friendlist.groupVec.get(i).groupName.equals(
                        NewMain.NWKSER.searchbyname(temp).group)) {
                    if(!NewMain.NWKSER.friendlist.groupVec.get(i).itisOnline(temp)) {
                        TextEditor.get(Book.getSelectedIndex()).NewLineStyle(plainmsg, NewMain.NWKSER.user_id, "OfflineMsg");
                        isOffline = true;
                        //System.out.println("isOffline");
                        break;
                    }
                }
            }
            if(isOffline) {
                rooms.get(Book.getSelectedIndex()).isInitial = true;
            }
            else {
                rooms.get(RoomPage.size()-1).roomIndex = requestIndex();
            }
        }
        else {
            packedmsg = "/speak" + " " + getRmIndex(Book.getSelectedIndex()) + " " + NewMain.NWKSER.user_id + " " + TextEditor.get(Book.getSelectedIndex()).DocEnCode(plainmsg, COLOR);
        }
        return packedmsg;
    }

    //OK
    public String Extract(String rawMsg) {
        System.out.println("dsfjdslfkjdsl"+rawMsg);
        String plainText=new String();
        if(rawMsg.contains("/NEWLINE/")){
            String []oneLineArr=rawMsg.split("/NEWLINE/");
            for(int i=0;i<oneLineArr.length;i++){
                String oneLine=oneLineArr[i];
                System.out.println(oneLine);
                plainText+=(oneLine.split("<System>")[1])+"\n";
                System.out.println("plaintText:"+plainText);
            }
            return plainText;
        }
        else{
            plainText=rawMsg.split("<System>")[1];
            System.out.println("plaintText:"+plainText);
            return plainText;
        }
    }

    //OK
    public void rcvdmsg(String usrID, String msg, int rm_index) {
        if(getTabIndex(rm_index) == -1) {
            rcvdmsgbyname(msg, usrID);
        }
        else {
            //System.out.print("rcvdmsg " + getTabIndex(rm_index) + " " +msg + " " +usrID);
            rooms.get(getTabIndex(rm_index)).isInitial = false;
            TextEditor.get(getTabIndex(rm_index)).NewLine(msg, usrID);
            if(Book.getSelectedIndex() != getTabIndex(rm_index)) {
                NewMain.NWKSER.friendlist.dendenden();
            }
        }
    }

    //OK
    public void rcvdmsgbyname(String msg, String usrID) {
        //System.out.print("rcvdmsgbyname");
        if(MutiPageInsertDetect(NewMain.NWKSER.searchbyname(usrID))) {
            removepage(getExcessRoom(NewMain.NWKSER.searchbyname(usrID)).tabindex);
            insertpage(NewMain.NWKSER.searchbyname(usrID));
            rooms.get(rooms.size()-1).isInitial = false;
            rooms.get(rooms.size()-1).roomIndex = requestIndex();
        }
        else {
            ArrayList<String> singlemember = new ArrayList<String>();
            singlemember.add(usrID);
            invitedby(singlemember);
        }
        NewMain.NWKSER.friendlist.dendenden();
        TextEditor.get(RM_INDEX_COUNT).NewLine(msg, usrID);
    }

    //OK
    public void addmember(Friend target, int tab_index) {
        rooms.get(tab_index).member.add(target);
    }

    //For Server to renew room ONLY
    //OK
    public void delmember(Friend target, int tab_index) {
        rooms.get(tab_index).member.remove(target);
        if(rooms.get(tab_index).member.isEmpty()) {
            NewMain.NWKSER.sendmsg("/exitroom" + " " + getRmIndex(tab_index));
            rooms.get(tab_index).member.add(target);
            rooms.get(tab_index).isInitial = true;
            rooms.get(tab_index).roomIndex = -1;
            RM_INDEX_COUNT--;
        }
    }

    //OK
    public void chageofmember(String target, int rm_index) {
        boolean isNewMember = true;
        int tab_index = getTabIndex(rm_index);
        if(rm_index == -1) {
            //System.out.println("index = -1");
        }
        else {
            for(Friend t_check : rooms.get(tab_index).member) {
                if(t_check.name.equals(target)) {
                    isNewMember = false;
                }
            }
            if(isNewMember) {
                addmember(NewMain.NWKSER.searchbyname(target), tab_index);
                TextEditor.get(tab_index).NewLineStyle(target +" has Enter this Room!!", "Room", "Message");
            }
            else {
                delmember(NewMain.NWKSER.searchbyname(target), tab_index);
                TextEditor.get(tab_index).NewLineStyle(target +" has Left this Room!!", "Room", "Message");
            }
        }
        setTabTitle(tab_index);
    }

    public void popofflinemsg() {
        System.out.println("popofflinemsg()");
        for(int i = 0; i < NewMain.NWKSER.friendlist.friendVec.size(); i++){
            if(NewMain.NWKSER.friendlist.friendVec.get(i).offlinemsg.length() > 0) {
                speakto(NewMain.NWKSER.friendlist.friendVec.get(i));
                TextEditor.get(RoomPage.size()-1).NewLineStyle(Extract(NewMain.NWKSER.friendlist.friendVec.get(i).offlinemsg), NewMain.NWKSER.friendlist.friendVec.get(i).name, "OfflineMsg");
            }
        }
    }

    public void dingbyothers(int rm_index) {
        NewMain.NWKSER.friendlist.ding();
        TextEditor.get(getTabIndex(rm_index)).NewLineStyle("Ding~~~~", "Sombody", "Message");
    }
    
    /**
    * Used for Test Only
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton Send;
    private javax.swing.JLabel Invite;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel Leave;
    private javax.swing.JLabel History;
    private javax.swing.JLabel ColorSelect;
    private javax.swing.JLabel Font;
    private javax.swing.JLabel File;
    private javax.swing.JLabel Call;
    private javax.swing.JLabel Poke;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane Book;
    private javax.swing.JTextPane TextBox;
    //
    private java.util.ArrayList<javax.swing.JTextPane> RoomPage;
    private InviteList invitelist;
    private java.util.ArrayList<Room> rooms;
    private javax.swing.text.StyledDocument display;
    private java.util.ArrayList<Display> TextEditor;
    //
    private int RM_INDEX_COUNT;
    private int COLOR;
    public Phone CallSystem;
    private Display Editor;
    //
    private javax.swing.JLabel FriendIcon;
    private javax.swing.JLabel SelfIcon;
    private javax.swing.JLabel StatTitle;
    private javax.swing.JLabel CLOSEWINDOW;
    private javax.swing.JLabel jLabel16;
    //
    private boolean isEnterPressed;
}
