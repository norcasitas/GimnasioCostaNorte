/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.IngresoGui;
import Interfaces.LoginGUI;
import Interfaces.PrincipalGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.javalite.activejdbc.Base;

/**
 *
 * @author nico
 */
public class ControladorLogin extends Thread implements ActionListener {

    private String user;
    private char[] pass;
    private PrincipalGui app;
    private LoginGUI log;
    private IngresoGui ingreso;

    public ControladorLogin(PrincipalGui app, IngresoGui ingresoGui) {
        this.app = app;
        this.ingreso= ingresoGui;
    }

    public void run() {

        log = new LoginGUI();
        log.setActionListener(this);
        log.setLocationRelativeTo(null);
        log.setVisible(true);
        log.getTextPass().requestFocus();
        log.getTextPass().addKeyListener(new KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    log.getTextPass().setText("");
                    log.getTextPass().requestFocus();
                }
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    user = log.getTextUsuario().getText();
                    pass = log.getTextPass().getPassword();
                    //if (mu.login(user, pass)) {
                    log.dispose();
                    app.getBotDesconectar().setText("Cerrar sesión ("+ "NICO"+")");
                    ingreso.setVisible(true);
                    app.setVisible(true);
                    app.toFront();
                    abrirBase();
                    
        
                    //} else {
                    //log.getTextPass().setText("");
                    //JOptionPane.showMessageDialog(app, "INTENTE NUEVAMENTE", "¡DATOS INCORRECTOS!", JOptionPane.ERROR_MESSAGE);
                    //}

                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton b = (JButton) ae.getSource();
        if (b.equals(log.getBotConectar())) {
                    log.dispose();
                    app.getBotDesconectar().setText("Cerrar sesión  ("+ "NICO"+")");
                     ingreso.setVisible(true);
                    app.setVisible(true);
                    app.toFront();
                    abrirBase();
        }
        if (b.equals(log.getBotSalir())) {
            System.exit(0);
        }


    }
    public void abrirBase(){
                        if (!Base.hasConnection()) {
                         Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/gym", "root", "root");
                     }
    }
}
