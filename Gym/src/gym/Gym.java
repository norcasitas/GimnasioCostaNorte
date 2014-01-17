/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gym;

import Controladores.ControladorCliente;
import Interfaces.AplicacionGUI;
import Interfaces.LoginGUI;
import Interfaces.PanelClientes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author alan
 */
public class Gym implements ActionListener{

    /**
     * @param args the command line arguments
     */
    LoginGUI loginGUI;
    AplicacionGUI aplicacionGUI;
    PanelClientes panelClientes;
    ControladorCliente controladorCliente;
    public Gym(){
      JFrame.setDefaultLookAndFeelDecorated(true); 
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
      loginGUI = new LoginGUI();
        loginGUI.setVisible(true);
        aplicacionGUI = new AplicacionGUI();
        loginGUI.setActionListener(this);
        //aplicacionGUI.getPanelClientes().setActionListener(this);
        controladorCliente = new ControladorCliente(aplicacionGUI);
        //aplicacionGui.setVisible(true);
        aplicacionGUI.setActionListener(this);
        
    }
    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {
         Gym mainControlador = new Gym(); 
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton)e.getSource();
        if(b.equals(loginGUI.getBotConectar())){
            loginGUI.setVisible(false);
            aplicacionGUI.setVisible(true);
        } 
        if(b.equals(loginGUI.getBotSalir())){
            System.exit(0);
        }
        if(b.equals(aplicacionGUI.getBotDesconectar())){
            aplicacionGUI.dispose();
            loginGUI.setVisible(true);
            loginGUI.getTextUsuario().setText("");
            loginGUI.getTextPass().setText("");
        }
    }
}
