/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Chat;

import gui.Friend;

/**
 *
 * @author grilledchops
 */
public class Room {
    public int roomIndex;
    public int tabindex;
    //Tab with no roomIndex isInitial
    public boolean isInitial;
    public String Log;
    public java.util.ArrayList<Friend> member;
    public Room(int rm_index, int tab_index, Friend t_first) {
        member = new java.util.ArrayList<Friend>();
        roomIndex = rm_index;
        tabindex = tab_index;
        member.add(t_first);
        isInitial = false;
        Log = "Welcome! \n";
    }
    
    public Room(int tab_index, Friend t_first) {
        member = new java.util.ArrayList<Friend>();
        roomIndex = -1;
        tabindex = tab_index;
        member.add(t_first);
        isInitial = true;
        Log = "NULL";
    }

    public Room() {
        System.out.println("Warning: Initial an empty room\n");
        member = new java.util.ArrayList<Friend>();
        roomIndex = -1;
        tabindex = -1;
        isInitial = true;
        Log = "NULL";
    }
}
