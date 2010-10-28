/**
 *
 *
 *
 *
 **/

package project1;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ShortestPath  {
    public static void main(String[] args) {
        Frame f = new FileViewer(null);
        f.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) { System.exit(0); }
            });
        f.show();
    }


}
