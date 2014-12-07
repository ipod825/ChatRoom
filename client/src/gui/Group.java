/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import testclient.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author Administrator
 */
public class Group {
   public String groupName;
        public Boolean isFolded;
        public MyListCell groupCell;
        public Vector<Friend> onlinefriendVec;
        public Vector<Friend> offlineFriendVec;

        public Group(String name) {
            this.groupName=name;
            isFolded=true;
            groupCell=new MyListCell(groupName,Color.blue,new TriangleIcon(this,Color.gray));
            onlinefriendVec=new Vector<Friend>();
            offlineFriendVec=new Vector<Friend>();
        }

        public void addFriend(String friendName,boolean isOnline,byte[] imagedata) {
            if(isOnline){
                onlinefriendVec.add(new Friend(this.groupName,friendName,isOnline,imagedata));
            }
            else{
                offlineFriendVec.add(new Friend(this.groupName,friendName,isOnline,imagedata));
            }
        }

        public void deleteFriend(String friendName,boolean isOnline,byte[] imagedata){
             if(isOnline){
                
                for(int a = 0; a <= onlinefriendVec.size()-1;a++){
                    if(onlinefriendVec.elementAt(a).name.equals(friendName)){
                        onlinefriendVec.remove(onlinefriendVec.elementAt(a));
                    }
                }
            }
            else{
                for(int a = 0; a <= offlineFriendVec.size()-1;a++){
                    if(offlineFriendVec.elementAt(a).name.equals(friendName)){
                        offlineFriendVec.remove(offlineFriendVec.elementAt(a));
                    }
                 }
            }
        }


       public boolean itisOnline(String name){
               boolean a = false;
               for(int on = 0;on<= onlinefriendVec.size()-1;on++){
                    if(onlinefriendVec.elementAt(on).name.equals(name)){
                        a = true;
                    }
                }
                for(int off = 0;off<= offlineFriendVec.size()-1;off++){
                    if(offlineFriendVec.elementAt(off).name.equals(name)){
                        a = false;
                   }
                }
            return a;
        }

        public  class TriangleIcon extends ImageIcon implements Icon {
        private Group group;
        private Color color;
        private int width;
        private int height;
        private Polygon foldedPoly;
        private Polygon unFoldedPoly;
        private static final int DEFAULT_WIDTH = 10;
        private static final int DEFAULT_HEIGHT = 10;

        public int getIconHeight() {return height;}
        public int getIconWidth() {return width;}

        public TriangleIcon(Group g,Color color) {this(g,color, DEFAULT_WIDTH, DEFAULT_HEIGHT);}
        public TriangleIcon(Group g, Color color, int width, int height) {
            this.group=g;
            this.color = color;
            this.width = width;
            this.height = height;
            int halfWidth = width / 2;
            int halfHeight = height/2;
            foldedPoly = new Polygon();
            foldedPoly.addPoint(0,0);
            foldedPoly.addPoint(0,height);
            foldedPoly.addPoint(width,halfHeight);
            unFoldedPoly=new Polygon();
            unFoldedPoly.addPoint(0,0);
            unFoldedPoly.addPoint(width,0);
            unFoldedPoly.addPoint(halfWidth,height);
        }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.translate(x, y);
            if (this.group.isFolded) {
                g.fillPolygon(foldedPoly);
            } else {
                g.fillPolygon(unFoldedPoly);
            }
            g.translate(-x, -y);
        }
    }

}
