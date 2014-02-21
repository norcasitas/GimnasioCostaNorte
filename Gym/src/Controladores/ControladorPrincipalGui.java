/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.AbmClienteGui;
import Interfaces.ActividadesGui;
import Interfaces.BusquedaGui;
import Interfaces.PrincipalGui;
import Interfaces.UsuarioGui;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author nico
 */
public class ControladorPrincipalGui implements ActionListener {

    private ControladorPrincipalGui controladorPrincipal;
    private PrincipalGui principalGui;
    private ControladorLogin controladorLogin;
    private BusquedaGui socios;
    private ControladorClientes controladorClientes;
    private ActividadesGui actividadesGui;
    private ControladorActividades controladorActividades;
    private UsuarioGui usuarioGui;
    private ControladorUsuario controladorUsuario;

    public ControladorPrincipalGui() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
                                                com.jtattoo.plaf.aero.AeroLookAndFeel.setTheme("Green-Large-Font");

            UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        principalGui = new PrincipalGui();
        controladorLogin = new ControladorLogin(principalGui);
        controladorLogin.run();//inicio el thread para la pantalla login asií se carga todo mientras inicias sesion
        principalGui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        principalGui.setCursor(Cursor.WAIT_CURSOR); //cambio el cursor por si se inicia sesión antes de cargar las cosas

        socios = new BusquedaGui();
        controladorClientes = new ControladorClientes(socios, principalGui.getDesktop());
        principalGui.setActionListener(this);
        principalGui.getDesktop().add(socios);

        actividadesGui = new ActividadesGui();
        controladorActividades = new ControladorActividades(actividadesGui);
        principalGui.getDesktop().add(actividadesGui);
        usuarioGui= new UsuarioGui();
        controladorUsuario= new ControladorUsuario(usuarioGui);
        principalGui.getDesktop().add(usuarioGui);
        principalGui.setCursor(Cursor.DEFAULT_CURSOR);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == principalGui.getBotDesconectar()) {
            System.out.println("cerrar sesión pulsado");
            //Aca debe ir para que se cierre la sesión
        }
        if (ae.getSource() == principalGui.getBotSalir()) {
            int r = JOptionPane.showConfirmDialog(principalGui, "¿Desea cerrar la aplicación?", "Salir", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
        if (ae.getSource() == principalGui.getBotSocios()) {
            System.out.println("boton socios pulsado");
            socios.setVisible(true);
            socios.toFront();
        }
        if (ae.getSource() == principalGui.getBotActividades()) {
            System.out.println("actividades pulsado");
            actividadesGui.setVisible(true);
            actividadesGui.toFront();
        }
        if(ae.getSource()==principalGui.getBotUsuario()){
            usuarioGui.setVisible(true);
            usuarioGui.toFront();
        }


    }

    public static void main(String[] args) throws InterruptedException {
        ControladorPrincipalGui appl = new ControladorPrincipalGui();

    }
}
