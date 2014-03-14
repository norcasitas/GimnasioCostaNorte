/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMSocios;
import Interfaces.AbmClienteGui;
import Interfaces.CargarHuellaGui;
import Interfaces.FichaMedicaGui;
import Interfaces.RegistrarPagoGui;
import Modelos.Arancel;
import Modelos.Socio;
import Modelos.Socioarancel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

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
    private ABMSocios abmsocio;
    private Socio s;

    public ControladorAbmCliente(AbmClienteGui clienteGui) {
        this.clienteGui = clienteGui;
        this.clienteGui.setActionListener(this);
        abmsocio = new ABMSocios();
    }
    
    private void CargarDatosSocio(Socio s){
        s.set("NOMBRE", clienteGui.getNombre().getText().toUpperCase());
        s.set("APELLIDO", clienteGui.getApellido().getText().toUpperCase());
        s.set("TEL", clienteGui.getTelefono().getText().toUpperCase());
        s.set("DNI", clienteGui.getDni().getText().toUpperCase());
        System.out.println(clienteGui.getDireccion().getText().toUpperCase());
        s.set("DIR", clienteGui.getDireccion().getText().toUpperCase());
        s.set("FECHA_NAC", dateToMySQLDate(clienteGui.getFechaNacimJDate().getCalendar().getTime(),false));
        if(clienteGui.getSexo().getSelectedIndex()==1){
            s.set("SEXO", "M");
        }else{
            s.set("SEXO", "F");
        }
        
    }

    /*va true si se quiere usar para mostrarla por pantalla es decir 12/12/2014 y false si va 
    para la base de datos, es decir 2014/12/12*/
    public String dateToMySQLDate(Date fecha, boolean paraMostrar) {
        if(paraMostrar){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
        }
        else{
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
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
                    s = new Socio();
                    CargarDatosSocio(s);
                    int rows = clienteGui.getTablaActivDefault().getRowCount();
                    LinkedList listaran = new LinkedList();
                    for(int i = 1; i< rows; i++){ //¿ANDA ARRANCANDO DESDE 1, NO SE SALTEA EL PRIMERO?
                        if(clienteGui.getTablaActividades().getValueAt(i, 1) != null){
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
                for(int i = 0; i< 12; i++){
                    System.out.println(clienteGui.getTablaActividades().getValueAt(i, 1));
                }
                //System.out.println(clienteGui.getTablaActividades().getValueAt(0, 0));
                //debe mantener abierta la ventana y que se habilite el botón de la huella, la huella
                //solo puede ser creada si el usuario existe
                s = new Socio();
               // boolean val;
                CargarDatosSocio(s);
                if(s.getString("DNI").equals("") || s.getString("APELLIDO").equals("")){
                    JOptionPane.showMessageDialog(clienteGui, "Faltan datos obligatorios", "Error!", JOptionPane.ERROR_MESSAGE);
                }else{
                     int rows = clienteGui.getTablaActivDefault().getRowCount();
                     System.out.println("La cant de rows es "+rows);
                     LinkedList listaran = new LinkedList();
                     for(int i = 0; i< rows; i++){
                        // System.out.println(clienteGui.getTablaActividades().getValueAt(i, 0));
                     //    val = (boolean) clienteGui.getTablaActividades().getValueAt(i, 1); 
                       //  System.out.println("se te pudrio");
                         if( clienteGui.getTablaActividades().getValueAt(i, 1) != null){
                             Arancel a = Arancel.first("nombre = ?", clienteGui.getTablaActividades().getValueAt(i, 0));
                             listaran.add(a);
                             System.out.println("esta GIL");
                         }else{
                             System.out.println("no esta GIL");
                         }
                    }
                    if(abmsocio.alta(s, listaran)){
                        JOptionPane.showMessageDialog(clienteGui, "Socio guardado exitosamente!");
                        clienteGui.bloquearCampos(true);
                        //clienteGui.limpiarCampos();
                        clienteGui.getBotNuevo().setEnabled(true);
                        clienteGui.getBotGuardar().setEnabled(false);
                        clienteGui.getBotHuella().setEnabled(true);
                        clienteGui.getBotFicha().setEnabled(true);
                        s= Socio.findFirst("DNI = ? ", clienteGui.getDni().getText());
                        
                    } else {
                        JOptionPane.showMessageDialog(clienteGui, "Ocurrió un error, revise los datos", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                
            }

        }
        if (ae.getSource() == clienteGui.getBotHuella()) {
            System.out.println("Boton huella pulsado");
            try {
                System.out.println(s==null);
                cargarHuellaGui= new CargarHuellaGui(null, s.getInteger("ID_DATOS_PERS"));//Aca va el ID del cliente !
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
            clienteGui.getTablaActivDefault().setRowCount(0);
            Socio socio = Socio.first("DNI = ?", clienteGui.getDni().getText());
            LazyList<Socioarancel> socaran = Socioarancel.where("id_socio = ?", socio.get("ID_DATOS_PERS"));
            Iterator<Socioarancel> iter = socaran.iterator();
            LinkedList<String> tieneAran = new LinkedList();
            while(iter.hasNext()){
                Socioarancel arsoc = iter.next();
                Arancel ar = Arancel.first("id = ?", arsoc.get("id_arancel"));
                tieneAran.add(ar.getString("nombre"));
                System.out.println(ar.get("nombre")+ " gil");
            }
            Iterator<String> itiene = tieneAran.iterator();
            while(itiene.hasNext()){
                String tiene = itiene.next();
                Object row[] = new Object[2];
                row[0] = tiene;
                row[1] = true;
                clienteGui.getTablaActivDefault().addRow(row);
            }
            
            /////////////////////////////////////////////////////
            
            ////////////////////////////////////////////////////
            LazyList<Arancel> listArancel = Arancel.findAll();
            LinkedList<String> Aranceles = new LinkedList();
            Iterator<Arancel> it = listArancel.iterator();
            while(it.hasNext()){
                Arancel a = it.next();
                Aranceles.add(a.getString("nombre"));
            }
            Aranceles.removeAll(tieneAran);
            Iterator<String> itt = Aranceles.iterator();
            while(itt.hasNext()){
                String tiene2 = itt.next();
                Object row[] = new Object[2];
                row[0] = tiene2;
                //row[1] = false;
                clienteGui.getTablaActivDefault().addRow(row);
            }
            
            
          /*  while(iter.hasNext()){
                Socioarancel sa = iter.next();
                Arancel ar = Arancel.first("id = ?", sa.get("id_arancel"));
                System.out.println("que onda1");
                int i ;
                for(i = 0; i < clienteGui.getTablaActivDefault().getRowCount(); i++){
                    System.out.println("que onda2");
                    if(clienteGui.getTablaActivDefault().getValueAt(i, 0) == ar.getString("nombre")){
                        System.out.println("que onda3");
                        clienteGui.getTablaActivDefault().setValueAt(true, i, 1);
                    }
                }
                System.out.println("que onda");
            }*/
        
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
            pagoGui= new RegistrarPagoGui(null, true, s);
            pagoGui.setLocationRelativeTo(null);
            pagoGui.setVisible(true);

        }
    }

    public void setIsNuevo(boolean isNuevo) {
        this.isNuevo = isNuevo;
    }
    
    private void diferenciaListas(LinkedList tiene, LazyList todos){
     /*   Iterator<Arancel> itiene = tiene.iterator();
        Iterator<Arancel> itodos = todos.iterator();
        while(itiene.hasNext()){
            Arancel atiene = itiene.next();
            while(itodos.hasNext()){
                Arancel atodos = itodos.next();
                System.out.println("antes if");
                if(atiene == atodos){
                    System.out.println("ENTRO al if");
                    itodos.remove();
                }
                System.out.println("despues al if");
            }
        }*/
        
        for(int i = 0; i < tiene.size(); i++){
            Arancel a = (Arancel) tiene.get(i);
            for(int j = 0; j < todos.size(); j++){
                if(tiene.get(i) == todos.get(j)){
                    todos.remove(j);
                }
            }
        }
       
    }
        public void abrirBase(){
         if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/GYM", "root", "root");
        }
        }
        
                public void cerrarBase(){
         if (Base.hasConnection()) {
            Base.close();
        }
        }

    public void setSocio(Socio s) {
        this.s = s;
    }
                
                
}
