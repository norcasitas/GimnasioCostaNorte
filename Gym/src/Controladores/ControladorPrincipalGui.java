/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMSocios;
import Interfaces.ActividadesGui;
import Interfaces.BusquedaGui;
import Interfaces.IngresoGui;
import Interfaces.PrincipalGui;
import Interfaces.UsuarioGui;
import Modelos.Arancel;
import Modelos.Socio;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.javalite.activejdbc.LazyList;

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
    private ControladorIngreso controladorIngreso;
    private IngresoGui ingresoGui;

    public ControladorPrincipalGui() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
                                                com.jtattoo.plaf.aero.AeroLookAndFeel.setTheme("Green-Large-Font");

            UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        principalGui = new PrincipalGui();
        controladorLogin = new ControladorLogin(principalGui);
        controladorLogin.start();//inicio el thread para la pantalla login asií se carga todo mientras inicias sesion
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
        ingresoGui= new IngresoGui();
        controladorIngreso= new ControladorIngreso(ingresoGui);
        ingresoGui.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ingresoGui.setVisible(true);
        principalGui.toFront();
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
            LazyList<Socio> ListSocios= Socio.findAll();
            socios.getTablaClientesDefault().setRowCount(0);
             Iterator<Socio> it = ListSocios.iterator();
             while(it.hasNext()){
                Socio a = it.next();
                String row[] = new String[4];
                row[0] = a.getString("NOMBRE");
                row[1] = a.getString("APELLIDO");
                row[2] = a.getString("DNI");
                row[3] = a.getString("TEL");
                socios.getTablaClientesDefault().addRow(row);
             }
             socios.getLabelResult3().setText(Integer.toString(ListSocios.size()));
             LazyList lista = Arancel.where("ACTIVO = ?", 1);
             Iterator<Arancel> iter = lista.iterator();
             String d[] = new String[100];
             int i = 1;
             while(iter.hasNext()){
                 Arancel a = iter.next();
                 d[i] = a.getString("nombre");
                 i++;
             }
             socios.getActividades().setListData(d);
             ABMSocios abm = new ABMSocios();
             /*
              * ESTO SE EJECUTA UNA VEZ!
              */
           //  abm.modbase();
        }
        if (ae.getSource() == principalGui.getBotActividades()) {
            System.out.println("actividades pulsado");
            actividadesGui.setVisible(true);
            actividadesGui.toFront();
            actividadesGui.getTablaActividadesDefault().setRowCount(0);
            LazyList ListAranceles = Arancel.where("activo = ?", 1);
            Iterator<Arancel> it = ListAranceles.iterator();
            while(it.hasNext()){
                        Arancel ar = it.next();
                        Object row[] = new Object[3];
                        row[0] = ar.getInteger("id");
                        row[1] = ar.getString("nombre");
                        row[2] = ar.getFloat("precio");
                        actividadesGui.getTablaActividadesDefault().addRow(row);
             }
        }
        if(ae.getSource()==principalGui.getBotUsuario()){
            usuarioGui.setVisible(true);
            usuarioGui.toFront();
        }
        if(ae.getSource()==principalGui.getIngreso()){
            System.out.println("ingreso presionado wachin");
            ingresoGui.setVisible(true);
            ingresoGui.toFront();
            ingresoGui.setLocationRelativeTo(null);
        }


    }

    public static void main(String[] args) throws InterruptedException {
        
        ControladorPrincipalGui appl = new ControladorPrincipalGui();

    }
}
