/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMSocios;
import Interfaces.AbmClienteGui;
import Interfaces.FichaMedicaGui;
import Interfaces.RegistrarPagoGui;
import Modelos.Arancel;
import Modelos.Socio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import org.javalite.activejdbc.Model;

/**
 *
 * @author nico
 */
public class ControladorAbmCliente implements ActionListener {

    private AbmClienteGui clienteGui;
    private boolean isNuevo;
    private RegistrarPagoGui pagoGui;
    private FichaMedicaGui fichaMedicaGui;
    private ABMSocios abmsocio;

    public ControladorAbmCliente(AbmClienteGui clienteGui) {
        this.clienteGui = clienteGui;
        this.clienteGui.setActionListener(this);
        abmsocio = new ABMSocios();
    }
    
    private void CargarDatosSocio(Socio s){
        s.set("NOMBRE", clienteGui.getNombre().getText().toUpperCase());
        s.set("APELLIDO", clienteGui.getApellido().getText().toUpperCase());
        s.set("TEL", clienteGui.getTelefono().getText());
        s.set("DNI", clienteGui.getDni().getText());
        s.set("DIR", clienteGui.getDireccion().getText().toUpperCase());
       // s.set("FECHA_NAC", clienteGui.getFechaNacimJDate().getCalendar().getTime());
        if(clienteGui.getSexo().getSelectedIndex()==1){
            s.set("SEXO", "M");
        }else{
            s.set("SEXO", "F");
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clienteGui.getBotEliminarCancelar()) {
            System.out.println("Boton eliminar pulsado");
            if(clienteGui.getBotEliminarCancelar().getText().equals("Eliminar")){
                clienteGui.bloquearCampos(true);
                int ret=JOptionPane.showConfirmDialog(clienteGui, "¿Desea borrar a "+ clienteGui.getNombre()+" "+clienteGui.getApellido()+" ? "/*ATENCIÓN: Este procedimiento solo marcara a dicho socio como INACTIVO."*/,null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                /*    Socio s = new Socio();
                    s.set("DNI", clienteGui.getDni());
                    abmsocio.baja(s);
                   */ clienteGui.limpiarCampos();
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
                    Socio s = new Socio();
                    CargarDatosSocio(s);
                    int rows = clienteGui.getTablaActivDefault().getRowCount();
                    LinkedList listaran = new LinkedList();
                    for(int i = 1; i< rows; i++){
                        if(clienteGui.getTablaActividades().getValueAt(i, 1) == true){
                            Arancel a = Arancel.first("nombre = ?", clienteGui.getTablaActividades().getValueAt(i, 0));
                            listaran.add(a);
                        }
                    }
                    //Object o = clienteGui.getTablaActividades().getValueAt(1, 1).equals(true);
                    if(abmsocio.modificar(s, listaran)){
                        JOptionPane.showMessageDialog(clienteGui, "Socio modificado exitosamente!");
                        clienteGui.bloquearCampos(true);
                        //clienteGui.limpiarCampos();
                        clienteGui.getBotNuevo().setEnabled(true);
                        clienteGui.getBotGuardar().setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(clienteGui, "Ocurrió un error, revise los datos", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
            }
            else{
                System.out.println("Boton guardó uno nuevito");
                Socio s = new Socio();
                CargarDatosSocio(s);
                if(s.getString("DNI").equals("") || s.getString("APELLIDO").equals("")){
                    JOptionPane.showMessageDialog(clienteGui, "Faltan datos obligatorios", "Error!", JOptionPane.ERROR_MESSAGE);
                }else{
                     int rows = clienteGui.getTablaActivDefault().getRowCount();
                     LinkedList listaran = new LinkedList();
                     for(int i = 1; i< rows; i++){
                        if(clienteGui.getTablaActividades().getValueAt(i, 1) == true){
                            Arancel a = Arancel.first("nombre = ?", clienteGui.getTablaActividades().getValueAt(i, 0));
                            listaran.add(a);
                        }
                    }
                    if(abmsocio.alta(s, listaran)){
                        JOptionPane.showMessageDialog(clienteGui, "Socio guardado exitosamente!");
                        clienteGui.bloquearCampos(true);
                        //clienteGui.limpiarCampos();
                        clienteGui.getBotNuevo().setEnabled(true);
                        clienteGui.getBotGuardar().setEnabled(false);
                        
                    } else {
                        JOptionPane.showMessageDialog(clienteGui, "Ocurrió un error, revise los datos", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                
            }

        }
        if (ae.getSource() == clienteGui.getBotHuella()) {
            System.out.println("Boton huella pulsado");

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
            clienteGui.getBotGuardar().setEnabled(true);

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
