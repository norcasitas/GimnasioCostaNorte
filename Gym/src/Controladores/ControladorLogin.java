/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.IngresoGui;
import Interfaces.LoginGUI;
import Interfaces.PrincipalGui;
import Modelos.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;

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
    //private String usuario;
    static boolean esAdmin;
    public ControladorLogin(PrincipalGui app, IngresoGui ingresoGui) {
        this.app = app;
        this.ingreso = ingresoGui;
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
                    abrirBase();
                    user = log.getTextUsuario().getText();
                    pass = log.getTextPass().getPassword();
                    //User u = User.first("USUARIO = ? and PASSWD = ?", usuario, log.getTextPass().getText());
                    if (login(user, pass) == false) {
                        JOptionPane.showMessageDialog(app, "Usuario inexistente o contraseña incorrecta.", "¡DATOS INCORRECTOS!", JOptionPane.ERROR_MESSAGE);
                        log.getTextUsuario().setText("");
                        log.getTextPass().setText("");
                    } else {
                        User u = User.first("USUARIO = ?", user);
                        if (!u.getBoolean("ADMINIS")) {
                            app.getBotUsuario().setEnabled(false);
                            app.getDepurar().setEnabled(false);
                            esAdmin=false;
                        } else {
                            app.getBotUsuario().setEnabled(true);
                            app.getDepurar().setEnabled(true);
                            esAdmin=true;
                        }
                        log.dispose();
                        app.getBotDesconectar().setText("Cerrar sesión  (" + user + ")");
                        ingreso.setVisible(true);
                        app.setVisible(true);
                        app.toFront();

                    }

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
            abrirBase();
            user = log.getTextUsuario().getText();
            pass = log.getTextPass().getPassword();
            //User u = User.first("USUARIO = ? and PASSWD = ?", usuario, pass.toString());
            if (login(user, pass) == false) {
                JOptionPane.showMessageDialog(app, "Usuario inexistente o contraseña incorrecta.", "¡DATOS INCORRECTOS!", JOptionPane.ERROR_MESSAGE);
                log.getTextUsuario().setText("");
                log.getTextPass().setText("");
            } else {
                                        User u = User.first("USUARIO = ?", user);
                        if (!u.getBoolean("ADMINIS")) {
                            app.getBotUsuario().setEnabled(false);
                            app.getDepurar().setEnabled(false);
                            esAdmin=false;
                        } else {
                            app.getBotUsuario().setEnabled(true);
                            app.getDepurar().setEnabled(true);
                            esAdmin=true;
                        }
                log.dispose();
                app.getBotDesconectar().setText("Cerrar sesión  (" + user + ")");
                ingreso.setVisible(true);
                app.setVisible(true);
                app.toFront();

            }

        }
        if (b.equals(log.getBotSalir())) {
            System.exit(0);
        }

    }

    public void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/gym", "root", "root");
        }
    }

    public LoginGUI getLog() {
        return log;
    }

    public boolean login(String user, char[] pass) {
        User u = User.first("USUARIO = ?", user);
        if (u != null) {
            char[] correct = u.getString("PASSWD").toCharArray();
            if (user.equals(u.getString("USUARIO")) && Arrays.equals(pass, correct)) {
                return true;
            }
        }
        return false;
    }

}
