package gui;

import ImageRelated.*;
import GroupRelated.*;
import java.net.MalformedURLException;
import testclient.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;

public class Friendlist extends javax.swing.JFrame{

    public enum FriendStatus{ONLINE,BUSY,NOTAVAIL,OFFLINE};



    private JComboBox stateComboBox;
    private JMenu filemenu;
    private JMenu friedMenu;
    private JMenu groupmenu;
    private JMenu Imagemenu;
    private JMenuItem logout;
    private JMenuItem exit;
    private JMenuItem addgroup;
    private JMenuItem changegroup;
    private JMenuItem deletegroup;
    private JMenuItem addfriend;
    private JMenuItem deletefriend;
    private JMenuItem ChangeImage;
    private JMenuBar menuBar;
    private JScrollPane scrollPanel;
    private JList list;
    private DefaultListModel listModel;
    public Vector<Group> groupVec;
    public Vector<String> groupNameVec;
    public Vector<Friend> friendVec;
    public Vector<String> friendNameVec;
    public String myurl = new String();
    ChooseFriend choose = new ChooseFriend();
    Typefriend Type = new Typefriend();
    String choosed = new String();
    String who = new String();

    public Friendlist() {
        initComponents();
    }

    private void initComponents() {
        if(!NewMain._login.account.isEmpty()){
            who = NewMain._login.account;
        }
        else{
            who = NewMain._login.reg.ApplyAccount;
        }

        scrollPanel = new javax.swing.JScrollPane();
        listModel=new DefaultListModel();
        list=new JList(listModel);
        list.setCellRenderer(new ListCellRenderer(){
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                MyListCell cell=(MyListCell) value;
                return cell;
            }
        });
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        friendVec = new Vector<Friend>();
        friendNameVec = new Vector<String>();
        stateComboBox = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        filemenu = new javax.swing.JMenu();
        friedMenu = new javax.swing.JMenu();
        groupmenu = new javax.swing.JMenu();
        Imagemenu = new javax.swing.JMenu();
        logout = new javax.swing.JMenuItem();
        exit= new javax.swing.JMenuItem();
        addfriend = new javax.swing.JMenuItem();
        deletefriend = new javax.swing.JMenuItem();
        changegroup = new javax.swing.JMenuItem();
        addgroup = new javax.swing.JMenuItem();
        deletegroup = new javax.swing.JMenuItem();
        ChangeImage = new javax.swing.JMenuItem();
        groupVec=new Vector<Group>();
        groupNameVec=new Vector<String>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        scrollPanel.setViewportView(list);

        stateComboBox.setModel(
                new javax.swing.DefaultComboBoxModel(new String[] { "在線", "忙碌", "離開", "離線" }));

        logout.setText("登出");
        exit.setText("結束");
        addfriend.setText("新增好友");
        deletefriend.setText("刪除好友");
        changegroup.setText("改變群組");
        addgroup.setText("新增群組");
        deletegroup.setText("刪除群組");
        filemenu.setText("檔案");
        friedMenu.setText("聯絡人");
        groupmenu.setText("群組");
        Imagemenu.setText("圖片");
        ChangeImage.setText("改變圖片");
        
        menuBar.add(filemenu);
        menuBar.add(friedMenu);
        menuBar.add(groupmenu);
        menuBar.add(Imagemenu);
        filemenu.add(logout);
        filemenu.add(exit);

        groupmenu.add(addgroup);
        groupmenu.add(deletegroup);

        friedMenu.add(addfriend);
        friedMenu.add(deletefriend);
        friedMenu.add(changegroup);

        Imagemenu.add(ChangeImage);

        addgroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addgroupActionPerformed(evt);
            }
        });

        deletegroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletegroupActionPerformed(evt);
            }
        });



        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        addfriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addfriendActionPerformed(evt);
            }
        });

        deletefriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletefriendActionPerformed(evt);
            }
        });

        changegroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changegroupActionPerformed(evt);
            }
        });

        ChangeImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeImageActionPerformed(evt);
            }
        });
        
        setJMenuBar(menuBar);

        scrollPanel.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    public void listMouseClicked(MouseEvent evt){
        int selectedNum = list.getLeadSelectionIndex();
        if(selectedNum!=-1){
        MyListCell selectedCell=(MyListCell)listModel.getElementAt(selectedNum);
        if (groupNameVec.contains(selectedCell.getText())){
            Group selectedGroup=groupVec.get(groupNameVec.indexOf(selectedCell.getText()));
            if(selectedGroup.isFolded){
                for( int a = 0; a<=selectedGroup.onlinefriendVec.size()-1;a++)
                    listModel.insertElementAt(selectedGroup.onlinefriendVec.elementAt(a).getCell(),++selectedNum);
                for( int a = 0; a<=selectedGroup.offlineFriendVec.size()-1;a++)
                    listModel.insertElementAt(selectedGroup.offlineFriendVec.elementAt(a).getCell(),++selectedNum);

                selectedGroup.isFolded=false;
            }
            else{
                for( int a = 0; a<=selectedGroup.onlinefriendVec.size()-1;a++)
                    listModel.removeElement(selectedGroup.onlinefriendVec.elementAt(a).getCell());
                for( int a = 0; a<=selectedGroup.offlineFriendVec.size()-1;a++)
                    listModel.removeElement(selectedGroup.offlineFriendVec.elementAt(a).getCell());
                selectedGroup.isFolded=true;
            }
        }
    }
    }

    public void newuserList(String list){
        int divide=list.indexOf("/offline/");
        String onLineList=list.substring(0,divide);
        String offLineList=list.substring(divide+9);
        String[] onlineFriendData = onLineList.split("/next/");
        String[] offlineFriendData = offLineList.split("/next/");

        if(!onLineList.isEmpty())
            insertUserList(true,onlineFriendData);
        if(!offLineList.isEmpty())
            insertUserList(false,offlineFriendData);
    }

    public void insertUserList(boolean isOnline,String[] data){
        byte[] a = new byte[0];
        for(int i = 0; i<=data.length-1;i++){
     
            String friendDataArr[]=data[i].split(":", 4);
            String groupName=friendDataArr[0];
            String friendName=friendDataArr[1];
            String offLineMsg=friendDataArr[2];
            

            Friend newone = new Friend(groupName,friendName,isOnline,a);
          
            friendVec.add(newone);
            newone.offlinemsg = offLineMsg;
            friendNameVec.add(friendName);


            if(!offLineMsg.isEmpty()){   //showOffLineMsg
            }
            if (groupNameVec.contains(groupName)){
                groupVec.get(groupNameVec.indexOf(groupName)).addFriend(friendName,isOnline,a);
            }
            else{
                Group newGroup = new Group(groupName);
                groupVec.add(newGroup);
                groupNameVec.add(groupName);
                listModel.addElement(newGroup.groupCell);
                newGroup.addFriend( friendName,isOnline,a);
            }
        }
    
        list.addMouseListener(new MouseAdapter() {

        public void mouseClicked(MouseEvent evt) {
           
        JList list = (JList)evt.getSource();
        System.out.println(evt.getClickCount());
        if (evt.getClickCount() == 2) {
            String talkto = ((MyListCell)list.getSelectedValue()).getfriendname();
             System.out.println(talkto);
            NewMain.MF.speakto(NewMain.NWKSER.searchbyname(talkto));

         }

        }
        });

        NewMain.MF.popofflinemsg();

    }

     public void renewUserList(Boolean isOnline,String friendName){
           String groupName = new String();
           byte[] imagedata;
           groupName = friendVec.get(friendNameVec.indexOf(friendName)).group;
           imagedata = friendVec.get(friendNameVec.indexOf(friendName)).IMAGEURL;
           Friend thefriend = friendVec.get(friendNameVec.indexOf(friendName));
           groupVec.get(groupNameVec.indexOf(groupName)).deleteFriend( friendName,!isOnline,imagedata);
           groupVec.get(groupNameVec.indexOf(groupName)).addFriend( friendName,isOnline,imagedata);
           renewlist();
    }

        private void logoutActionPerformed(java.awt.event.ActionEvent evt) {

        NewMain.NWKSER.disconnect();
        JOptionPane.showMessageDialog(this, "已登出", "", JOptionPane.PLAIN_MESSAGE);
        this.setVisible(false);
        NewMain.MF.setVisible(false);
        NewMain._login.setVisible(true);
    }

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {

        NewMain.NWKSER.disconnect();
        System.exit(0);
    }

    private void addfriendActionPerformed(java.awt.event.ActionEvent evt) {

        Type.setVisible(true);
    }

    private void deletefriendActionPerformed(java.awt.event.ActionEvent evt) {
        for(int a = 0;a<=this.friendNameVec.size()-1;a++){
            choose.addUser(this.friendNameVec.elementAt(a));
        }
        choose.setVisible(true);
    }


    public void removefriend(String name){
        String thegroup = friendVec.elementAt(friendNameVec.indexOf(name)).group;
        boolean onoff =  groupVec.elementAt(groupNameVec.indexOf(thegroup)).itisOnline(name);
        groupVec.elementAt(groupNameVec.indexOf(thegroup)).deleteFriend(name,onoff,friendVec.elementAt(friendNameVec.indexOf(name)).IMAGEURL);
        
        friendVec.removeElementAt(friendNameVec.indexOf(name));
        friendNameVec.remove(name);
        renewlist();
    }

    public void Invited(String name){
         int confirm = JOptionPane.showConfirmDialog(null, name+"想加你為好友,是否新增好友?", "", JOptionPane.YES_NO_OPTION);
         if(confirm == JOptionPane.YES_OPTION){
            String group = JOptionPane.showInputDialog("請輸入欲新增至的群組");
            JOptionPane.showMessageDialog(this, "已新增"+name+"為好友 並新增至"+group+"群組", "", JOptionPane.PLAIN_MESSAGE);
            NewMain.NWKSER.sendmsg("/okaddedby "+name+ " "+group);
            /*
            String imageUrl = "";
            Friend one = new Friend(group,name,true,imageUrl);
            friendVec.add(one);
            friendNameVec.add(name);
            if (groupNameVec.contains(group))
                groupVec.get(groupNameVec.indexOf(group)).addFriend( name,true,imageUrl);
            else{
                Group newGroup = new Group(group);
                groupVec.add(newGroup);
                groupNameVec.add(group);
                listModel.addElement(newGroup.groupCell);
                newGroup.addFriend( name,true,imageUrl);
            }
             * 
             */
        }
        else{
             NewMain.NWKSER.sendmsg("/badaddedby "+name);
        }
    }

    public void renewlist(){
        list.removeAll();
        listModel.clear();
        for( int g = 0 ; g<= groupVec.size()-1;g++){
            groupVec.elementAt(g).isFolded = true;
            listModel.addElement(groupVec.elementAt(g).groupCell);
        }
        System.out.println("renewlist");
        
        list.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {

        JList list = (JList)evt.getSource();
        System.out.println(evt.getClickCount());
        if (evt.getClickCount() == 2) {
            String talkto = ((MyListCell)list.getSelectedValue()).getfriendname();
             System.out.println(talkto);
            NewMain.MF.speakto(NewMain.NWKSER.searchbyname(talkto));
         }
        }
        });
    }

    private void changegroupActionPerformed(java.awt.event.ActionEvent evt) {
        ChangeGroup change = new ChangeGroup();
        for(int a = 0;a<=this.groupNameVec.size()-1;a++){
            change.addGroup(groupNameVec.elementAt(a));
        }
        for(int a = 0;a<=this.friendNameVec.size()-1;a++ ){
            change.addUser(friendNameVec.elementAt(a));
        }
        change.setVisible(true);
    }

    public void change(String name,String togroup){
        Friend friendchange = friendVec.elementAt(friendNameVec.indexOf(name));
        String fromgroup = friendVec.elementAt(friendNameVec.indexOf(name)).group;
        Group fromthegroup = groupVec.elementAt(groupNameVec.indexOf(fromgroup));
        boolean onoff = fromthegroup.itisOnline(name);
        groupVec.elementAt(groupNameVec.indexOf(fromgroup)).deleteFriend(name,onoff,friendchange.IMAGEURL);
        groupVec.elementAt(groupNameVec.indexOf(togroup)).addFriend(name,onoff,friendchange.IMAGEURL);
        friendVec.elementAt(friendNameVec.indexOf(name)).group = togroup;
        NewMain.NWKSER.sendmsg("/changegroup "+name+" "+togroup);
        renewlist();
    }


    private void deletegroupActionPerformed(java.awt.event.ActionEvent evt) {
        deletegroup del = new deletegroup();
        for(int a = 0; a<=this.groupNameVec.size()-1;a++){
            del.addGroup(groupNameVec.elementAt(a));
        }
        del.setVisible(true);
    }

    public void deletegroup(String target){
        //Group delete = NewMain.NWKSER.friendlist.groupVec.elementAt(NewMain.NWKSER.friendlist.groupNameVec.indexOf(target));
        listModel.removeElement(NewMain.NWKSER.friendlist.groupVec.get(NewMain.NWKSER.friendlist.groupNameVec.indexOf(target)).groupCell);
    }
    


    private void addgroupActionPerformed(java.awt.event.ActionEvent evt) {
        String newgroupname = new String(JOptionPane.showInputDialog("請輸入欲新增的群組名"));
        if(newgroupname.contains(" "))
        {
           JOptionPane.showMessageDialog(this, "新增群組名不能包含空白 請重新輸入", "", JOptionPane.PLAIN_MESSAGE);
        }
        else {
           Group newGroup = new Group(newgroupname);
           groupVec.add(newGroup);
           groupNameVec.add(newgroupname);
           listModel.addElement(newGroup.groupCell);
        }
    }



    public void newfriend(String onoff,String group,String name,byte[] imagedata){
          if (onoff.equals("online")){
            Friend newone = new Friend(group,name,true,imagedata);
            friendVec.add(newone);
            friendNameVec.add(name);
            System.out.println(name);
            if(!groupNameVec.contains(group)){
                Group newGroup=new Group(group);
                groupNameVec.add(group);
                groupVec.add(new Group(group));
                listModel.addElement(newGroup.groupCell);
            }
            groupVec.get(groupNameVec.indexOf(group)).addFriend( name,true,imagedata);
            renewlist();
          }
          else{
              Friend newone = new Friend(group,name,false,imagedata);
            friendVec.add(newone);
            friendNameVec.add(name);
            if(!groupNameVec.contains(group)){
                Group newGroup=new Group(group);
                groupNameVec.add(group);
                groupVec.add(new Group(group));
                listModel.addElement(newGroup.groupCell);
            }
            groupVec.get(groupNameVec.indexOf(group)).addFriend( name,false,imagedata);
            renewlist();
          }
    }
    public void newimage(String name,byte[] imagedata){
        
        String hisgroup= new String(friendVec.elementAt(friendNameVec.indexOf(name)).group);
        removefriend(name);
        newfriend("online",hisgroup,name,imagedata);

    }




    private void ChangeImageActionPerformed(java.awt.event.ActionEvent evt) {
        this.changeimage();
    }

    public void changeimage(){
           ImageChooser imagechooser = new ImageChooser();
   }

   public void ding(){
       Bang bang = new Bang();
       bang.play("alarm1.wav");
   }
   
   public void dendenden(){
       Bang bang = new Bang();
       bang.play("msg.wav");
   }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Friendlist().setVisible(true);
                
            }
        });
    }
}
