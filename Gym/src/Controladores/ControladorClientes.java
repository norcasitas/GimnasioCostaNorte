/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.AbmClienteGui;
import Interfaces.BusquedaGui;
import Interfaces.DesktopPaneImage;
import Interfaces.PagosGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nico
 */
public class ControladorClientes implements ActionListener {

    private BusquedaGui clientesGui;
    private JTable tablaClientes;
    private JList actividades;
    private AbmClienteGui altaClienteGui;
    private ControladorAbmCliente controladorAbmCliente;
    private PagosGui pagosGui;

    public ControladorClientes(BusquedaGui clientes, DesktopPaneImage desktop) {
        this.clientesGui = clientes;
        clientes.setActionListener(this);
        altaClienteGui = new AbmClienteGui();
        controladorAbmCliente = new ControladorAbmCliente(altaClienteGui);
        desktop.add(altaClienteGui);
        pagosGui= new PagosGui();
        desktop.add(pagosGui);
        pagosGui.setActionListener(this);
        tablaClientes = this.clientesGui.getTablaClientes();
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        clientesGui.getBusqueda().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaKeyReleased(evt);
            }
        });
        actividades = clientesGui.getActividades();
        actividades.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                if (lse.getValueIsAdjusting()) {
                    System.out.println("opcion siendo seleccionada,no hago nada");
                } else {
                    System.out.println("opcion dejo de ser seleccionada ");
                    List actividadesSelecc = actividades.getSelectedValuesList();
                    System.out.println(actividadesSelecc.toString());
                    /*aca iria la incovación de la función para cuando se está buscando que se terminaron de
                     elegir las actividades*/
                }
            }
        });

    }

    public void busquedaKeyReleased(java.awt.event.KeyEvent evt) {
        System.out.println("apreté el caracter" + evt.getKeyChar());
        /*Aca va la gilada para la busqueda mientras escribis se puede hacer 
         función y que se invoque a esa así tambien se llama cuando se 
         seleccionan actividades*/
    }

    public void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        clientesGui.getBotEliminarSocio().setEnabled(true);
        clientesGui.getBotRegistrosPago().setEnabled(true);
        if (evt.getClickCount() == 2) {
            System.out.println("Hice doble click sobre un socio");
            altaClienteGui.setBotonesNuevo(false);
            altaClienteGui.bloquearCampos(true);
            altaClienteGui.limpiarCampos();
            altaClienteGui.setTitle("Información del socio");
            altaClienteGui.setVisible(true);
            altaClienteGui.toFront();
            /*Aca va el código para que se abra la ventana de clientes con TODOS
             los datos del cliente cargado*/
        }

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clientesGui.getBotAltaSocio()) {
            System.out.println("Alta socio pulsado");
            altaClienteGui.setBotonesNuevo(true);
            altaClienteGui.bloquearCampos(false);
            altaClienteGui.limpiarCampos();
            controladorAbmCliente.setIsNuevo(true);
            altaClienteGui.setTitle("Alta de socio");
            altaClienteGui.setVisible(true);
            altaClienteGui.toFront();
            /*Se debe abrir la ventana de clientes para permitir el alta de giles*/

        }
        if (ae.getSource() == clientesGui.getBotEliminarSocio()) {
            System.out.println("eliminar socio");
            if(tablaClientes.getSelectedRow()>=0){
                /*elimino el socio SELECCIONADO, pregunto y fue*/
                                int ret=JOptionPane.showConfirmDialog(clientesGui, "¿Desea eliminar el socio.......?",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                    System.out.println("elimino el de la fila "+tablaClientes.getSelectedRow());

                }
            }
        }
        if (ae.getSource() == clientesGui.getBotRegistrosPago()) {
            System.out.println("ver registros de pago pulsado");
            if(tablaClientes.getSelectedRow()>=0){
            /*abro la ventana de pagos y veo los pagos realizados por el cliente
             seleccionado*/
                pagosGui.setVisible(true);
                pagosGui.toFront();
                System.out.println("pagos del gil de la fila"+tablaClientes.getSelectedRow());
            }

        }
        if(ae.getSource()==pagosGui.getBotBorrarPago()){
             int ret=JOptionPane.showConfirmDialog(pagosGui, "¿Desea eliminar el pago seleccionado?",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                    System.out.println("elimino el de la fila "+tablaClientes.getSelectedRow());

                }
        }
        if(ae.getSource()==pagosGui.getBotVerTodos()){
            System.out.println("quiero ver todos los pago guacho");
        }
    }
}
