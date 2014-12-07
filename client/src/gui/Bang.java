/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.applet.*;
import javax.swing.*;
import java.awt.event.*;
import sun.audio.*; 
import java.io.*;

/**
 *
 * @author YTC
 */


/*
<APPLET code="soundApplet.class" width = 300 height = 300>
</APPLET>
*/

public class Bang extends JApplet
{

    public void play(String Filename)
    {
    try{
    InputStream in = new FileInputStream(Filename);
     AudioStream as = new AudioStream(in);
     AudioPlayer.player.start(as);

  } catch(FileNotFoundException e)
    {
      System.out.print("FileNotFoundException ");
    } catch(IOException e)
    {
        System.out.print("ERROR!");
    }
  }

}

