/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

/**
 *
 * @author Administrator
 */

import ImageRelated.*;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 *
 * @author Administrator
 */
public class Sample extends JFrame
{
  public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException
  {
      try {
          // load the sqlite-JDBC driver using the current class loader
          Class.forName("org.sqlite.JDBC");
          Connection connection = null;
          // create a database connection
          connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
          Statement statement = (Statement) connection.createStatement();
          statement.executeUpdate("drop table if exists person");
          statement.executeUpdate("create table person (a,image)");

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          File file=new File("images.jpg");
          try {
              ImageIO.write(ImageIO.read(file), "JPG", baos);
          } catch (IOException ex) {
              Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
          }
          byte[] imageData = baos.toByteArray();
          String tmp=new String(imageData);
          //System.out.println(tmp);
          PreparedStatement prep = connection.prepareStatement("insert into person values(?,?)") ;
          prep.setString(1, "p");
          prep.setBytes(2, imageData);
          prep.executeUpdate();

          ResultSet rs = statement.executeQuery("select * from person where a=\"p\"");
          rs.next();
          //Blob blob=rs.getBlob("image");
          byte[] imageData2=rs.getBytes("image");

          ImageIcon icon=new ImageIcon(imageData2);
          Sample s=new Sample();
          JLabel l=new JLabel(icon);
          l.setVisible(true);
          s.add(l);
          s.setVisible(true);

          //ImageIcon im=new ImageIcon()

      } catch (SQLException ex) {
          Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
}

