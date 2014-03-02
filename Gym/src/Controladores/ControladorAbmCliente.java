/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.AbmClienteGui;
import Interfaces.CargarHuellaGui;
import Interfaces.FichaMedicaGui;
import Interfaces.RegistrarPagoGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 *
 * @author nico
 */
public class ControladorAbmCliente implements ActionListener {

    private AbmClienteGui clienteGui;
    private boolean isNuevo;
    private RegistrarPagoGui pagoGui;
    private FichaMedicaGui fichaMedicaGui;
    private CargarHuellaGui cargarHuellaGui;

    public ControladorAbmCliente(AbmClienteGui clienteGui) {
        this.clienteGui = clienteGui;
        this.clienteGui.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clienteGui.getBotEliminarCancelar()) {
            System.out.println("Boton eliminar pulsado");
            if(clienteGui.getBotEliminarCancelar().getText().equals("Eliminar")){
                clienteGui.bloquearCampos(true);
                int ret=JOptionPane.showConfirmDialog(clienteGui, "¿Desea borrar el cliente.....",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                    /*Aqui va todo para borrar! */
                    clienteGui.limpiarCampos();
                    clienteGui.setVisible(false);
                }
            }
            else{
                System.out.println("cancelé !");
                int ret=JOptionPane.showConfirmDialog(clienteGui, "¿Desea cancelar los cambios?",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                    clienteGui.limpiarCampos();
                    clienteGui.setVisible(false);
                }
            }

        }
        if (ae.getSource() == clienteGui.getBotFicha()) {
            System.out.println("Boton ficha pulsado");
            fichaMedicaGui= new FichaMedicaGui(null, true);
            fichaMedicaGui.setLocationRelativeTo(null);
            fichaMedicaGui.setVisible(true);
            

        }
        if (ae.getSource() == clienteGui.getBotGuardar()) {
            if(!isNuevo){
                    System.out.println("Se modificó uno que existia");
            }
            else{
                System.out.println("Boton guardó uno nuevito");
                //debe mantener abierta la ventana y que se habilite el botón de la huella, la huella
                //solo puede ser creada si el usuario existe
                
            }

        }
        if (ae.getSource() == clienteGui.getBotHuella()) {
            System.out.println("Boton huella pulsado");
            try {
                cargarHuellaGui= new CargarHuellaGui(null, 1);//Aca va el ID del cliente !
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAbmCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            cargarHuellaGui.setLocationRelativeTo(null);
            cargarHuellaGui.setVisible(true);
            

        }
        if (ae.getSource() == clienteGui.getBotModif()) {
            System.out.println("Boton modif pulsado");
            clienteGui.bloquearCampos(false);
            clienteGui.getBotEliminarCancelar().setText("Cancelar");
            clienteGui.getBotModif().setEnabled(false);
            clienteGui.getBotGuardar().setEnabled(true);
            isNuevo=false;

        }
        if (ae.getSource() == clienteGui.getBotNuevo()) {
            System.out.println("Boton nuevo pulsado");
            isNuevo=true;
            clienteGui.limpiarCampos();
            clienteGui.bloquearCampos(false);

        }
        if (ae.getSource() == clienteGui.getBotPago()) {
            System.out.println("Boton pago pulsado");
            /*SE DEBERÁ MODIFICAR EL CONSTRUCTOR DE REGISTRARPAGOGUI PARA QUE TOME
             UN CLIENTE ASÍ SE HACE EL PAGO TODO DESDE ESA CLASE*/
            pagoGui= new RegistrarPagoGui(null, true);
            pagoGui.setLocationRelativeTo(null);
            pagoGui.setVisible(true);

        }
    }

    public void setIsNuevo(boolean isNuevo) {
        this.isNuevo = isNuevo;
    }
    
    
}
