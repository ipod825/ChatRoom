/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author ntuee
 */
       public class MyListCell extends JLabel{
        public MyListCell(String str,Color c,ImageIcon i){
            this.setFont(new java.awt.Font("新細明體", 1, 13));
            this.setText(str);
            this.setForeground(c);
            this.setIcon(i);
           // this.setIcon(new ImageIcon("images.jpg"));
        }

        public void changeonoff(Boolean state){
            if(state){
            this.setForeground(Color.GRAY);
            }
            else if(!state)
            {
            this.setForeground(Color.BLACK);
            }

        }



        public String getfriendname(){
            return this.getText();
        }
    }

