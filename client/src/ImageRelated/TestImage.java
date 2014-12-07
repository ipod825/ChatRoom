/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ImageRelated;

import gui.*;
import testclient.*;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



/**
 *
 * @author YTC
 */



public class TestImage {

//static String choosed = new String();
   public TestImage() {
       ImageChooser imagechoose = new ImageChooser();
    }

   public static byte[] sample(String choosed){
       String tmp = new String();
       
       // load the sqlite-JDBC driver using the current class loader
          // create a database connection
 

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          File file=new File(choosed);
          try {
              ImageIO.write(ImageIO.read(file), "JPG", baos);
          } catch (IOException ex) {
              Logger.getLogger(Sample.class.getName()).log(Level.SEVERE, null, ex);
          }
          byte[] imageData = baos.toByteArray();

          return imageData;
          
    }
          /*System.out.println(tmp);
          PreparedStatement prep = connection.prepareStatement("insert into person values(?,?)") ;
          prep.setString(1, "p");
          prep.setBytes(2, imageData);
          prep.executeUpdate();

          ResultSet rs = statement.executeQuery("select * from person where a=\"p\"");
          rs.next();

          //Blob blob=rs.getBlob("image");
          */
  public void unsample(String t){
          String input = new String(t);
          byte[] imageData2 = input.getBytes();
          ImageIcon icon=new ImageIcon(imageData2);  
    }

}

