/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author grilledchops
 */
public class Display {
    
    public Display(javax.swing.JTextPane tagetPane) {
        COLOR = Color.BLACK.getRGB();
        DisplayArea = tagetPane;
        styleDoc = DisplayArea.getStyledDocument();
        Style base = StyleContext.getDefaultStyleContext(). getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = styleDoc.addStyle("regular", base);
        StyleConstants.setFontFamily(base, "SansSerif");

        Style s = styleDoc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = styleDoc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = styleDoc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = styleDoc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        //system style
        s = styleDoc.addStyle("Server", regular);
        StyleConstants.setForeground(s, Color.BLUE);

        s = styleDoc.addStyle("system", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.BLUE);

        s = styleDoc.addStyle("warn", regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.RED);

        s = styleDoc.addStyle("whisper", regular);
        StyleConstants.setForeground(s, Color.MAGENTA);

        //default style
        s = styleDoc.addStyle("black", regular);
        StyleConstants.setForeground(s, Color.BLACK);

        s = styleDoc.addStyle("Error", regular);
        StyleConstants.setForeground(s, Color.RED);

        s = styleDoc.addStyle("blue", regular);
        StyleConstants.setForeground(s, Color.BLUE);

        s = styleDoc.addStyle("green", regular);
        StyleConstants.setForeground(s, Color.GREEN);

        s = styleDoc.addStyle("Message", regular);
        StyleConstants.setForeground(s, Color.ORANGE);

        s = styleDoc.addStyle("yellow", regular);
        StyleConstants.setForeground(s, Color.YELLOW);

        s = styleDoc.addStyle("cyan", regular);
        StyleConstants.setForeground(s, Color.CYAN);

        s = styleDoc.addStyle("OfflineMsg", regular);
        StyleConstants.setForeground(s, Color.GRAY);

        s = styleDoc.addStyle("textbox", regular);
        StyleConstants.setForeground(s, Color.BLACK);

        s = styleDoc.addStyle("newline", regular);
        StyleConstants.setForeground(s, Color.BLACK);
    }

    public void NewLine(String rawMsg, String usrId) {
        try {
            String[] split = rawMsg.split("<System>");
            System.out.println(split[0]);
            setColor(Integer.parseInt(split[0]), "newline");
            if(!split[1].isEmpty()) {
                styleDoc.insertString(styleDoc.getLength(), usrId + ": \n  ", styleDoc.getStyle("blue"));
                styleDoc.insertString(styleDoc.getLength(), split[1] + "\n", styleDoc.getStyle("newline"));
            }
        }
        catch (BadLocationException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (NumberFormatException nfe) {
            System.out.println("DocInfoDec() error");
        }
        DisplayArea.setCaretPosition(DisplayArea.getDocument().getLength());
    }

    public void NewLineStyle(String rawMsg, String usrId, String style) {
        try {
            styleDoc.insertString(styleDoc.getLength(), usrId + "(" + style + ")" + ": \n  ", styleDoc.getStyle("blue"));
            styleDoc.insertString(styleDoc.getLength(), rawMsg + "\n", styleDoc.getStyle(style));
        } catch (BadLocationException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
        DisplayArea.setCaretPosition(DisplayArea.getDocument().getLength()); 
    }

    public void setColor(int code, String style) {
        Style s = styleDoc.addStyle(style, styleDoc.getStyle("regular"));
        s = styleDoc.getStyle(style);
        StyleConstants.setForeground(s, new Color(code));
    }

    public String DocEnCode(String rawmsg, int code) {
        COLOR = code;
        String EnMsg = COLOR + "<System>" + rawmsg;
        return EnMsg;
    }

    public void DisplayColor(String text, int color) {
        try {
            COLOR = color;
            setColor(color, "textbox");
            System.out.println(COLOR);
            this.DisplayArea.setText(null);
            this.DisplayArea.setForeground(new Color(color));
            System.out.println("color changed");
            if(text.isEmpty()) {
                styleDoc.insertString(styleDoc.getLength(), " ", styleDoc.getStyle("textbox"));
                this.DisplayArea.setText(null);
            }
            styleDoc.insertString(styleDoc.getLength(), text, styleDoc.getStyle("textbox"));
        } catch (BadLocationException ex) {
            Logger.getLogger(Display.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private javax.swing.text.StyledDocument styleDoc;
    private javax.swing.JTextPane DisplayArea;
    private int COLOR;
}
