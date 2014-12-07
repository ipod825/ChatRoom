/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import testclient.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
/**
 *
 * @author Administrator
 */
public class Friend {
        public String name;
        public String group;
        public byte[] IMAGEURL;
        //private String StateMessage;
        public ImageIcon imageIcon;
        public ImageIcon imageIcon2;
        public String offlinemsg;
        private MyListCell cell;
        public MyListCell getCell(){return cell;}
        public Friend(String grou, String nam, boolean isOnline,byte[] imagedata){
            this.group = grou;
            this.name = nam;
            this.IMAGEURL = imagedata;
            imageIcon = new ImageIcon(imagedata);

            imageIcon2  = new ImageIcon(imagedata);
            Image img = imageIcon2.getImage();
            BufferedImage bi = new BufferedImage(20,20, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, 20, 20, null);
            imageIcon2 = new ImageIcon(bi);

            if(isOnline)
                cell=new MyListCell(nam,Color.black,imageIcon2);
            else
                cell=new MyListCell(nam,Color.gray,imageIcon2);
        }

        public ImageIcon setimage(byte[] imagedata){

            imageIcon  = new ImageIcon(imagedata);
            
            Image img = imageIcon.getImage();
            BufferedImage bi = new BufferedImage(20,20, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, 20, 20, null);
            imageIcon = new ImageIcon(bi);
            System.out.println("setimage in ");
            return imageIcon;
        }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
