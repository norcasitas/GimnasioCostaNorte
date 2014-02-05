/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;


public class DesktopPaneImage extends JDesktopPane {

    private Image IMG = new ImageIcon(getClass().getResource("/Imagenes/Costa Norte logo.jpg")).getImage();

    public void paintChildren(Graphics g) {
        g.drawImage(IMG, 0, 0, getWidth(), getHeight(), this);
        super.paintChildren(g);
    }
}
