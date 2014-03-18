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
import Modelos.Ficha;
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
    private boolean fichaNueva;

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
        if(clienteGui.getFechaNacimJDate().getCalendar()!=null)
             s.set("FECHA_NAC", dateToMySQLDate(clienteGui.getFechaNacimJDate().getCalendar().getTime(),false));
        if(clienteGui.getSexo().getSelectedIndex()==1){
            s.set("SEXO", "M");
        }else{
            s.set("SEXO", "F");
        }
        
    }
    
    public void CargarFicha(Ficha ficha){
        if(ficha.getString("FACTOR") == "RH+" || ficha.getString("FACTOR") == "+"){
            fichaMedicaGui.getSigno().setSelectedItem("+");
        }
        if(ficha.getString("FACTOR") == "RH-" || ficha.getString("FACTOR") == "-"){
            fichaMedicaGui.getSigno().setSelectedItem("-");
        }
        if(ficha.get("FACTOR") == null){
            fichaMedicaGui.getSigno().setSelectedItem("NE");
        }
        if(ficha.get("GRUPO_SANG") != null){
            fichaMedicaGui.getLetraSangui().setSelectedItem(ficha.get("GRUPO_SANG"));
        }else{
            fichaMedicaGui.getLetraSangui().setSelectedItem("NE");
        }
        fichaMedicaGui.getTelEmergencia().setText(ficha.getString("TEL_EMERG"));
        fichaMedicaGui.getTextoAlergias().setText(ficha.getString("ALERGICO"));
        fichaMedicaGui.getTextoMedicamentos().setText(ficha.getString("MEDICAM"));
        fichaMedicaGui.getObservaciones().setText(ficha.getString("OBSERV"));
        if(ficha.getInteger("ARTROSIS") != null){
            if(ficha.getInteger("ARTROSIS") != 0){
              fichaMedicaGui.getArtrosis().setSelected(true);
            }       
        }
        if(ficha.getInteger("ASMA") != null){
            if(ficha.getInteger("ASMA") != 0){
              fichaMedicaGui.getAsma().setSelected(true);
            }       
        }
        if(ficha.getInteger("CARDIACO") != null){
            if(ficha.getInteger("CARDIACO") != 0){
              fichaMedicaGui.getCardiaco().setSelected(true);
            }       
        }
        if(ficha.getInteger("DIABETES") != null){
            if(ficha.getInteger("DIABETES") != 0){
              fichaMedicaGui.getDiabetes().setSelected(true);
            }       
        }
        if(ficha.getInteger("EMBARAZO") != null){
            if(ficha.getInteger("EMBARAZO") != 0){
              fichaMedicaGui.getEmbarazo().setSelected(true);
            }       
        }
        if(ficha.getInteger("ENDOCRINOLOGIA") != null){
            if(ficha.getInteger("ENDOCRINOLOGIA") != 0){
              fichaMedicaGui.getEndocrinologia().setSelected(true);
            }       
        }
        if(ficha.getInteger("HUESOS") != null){
            if(ficha.getInteger("HUESOS") != 0){
              fichaMedicaGui.getHuesoLigam().setSelected(true);
            }       
        }
        if(ficha.getInteger("PULMONARES") != null){
            if(ficha.getInteger("PULMONARES") != 0){
              fichaMedicaGui.getEnfPulmonar().setSelected(true);
            }       
        }
        if(ficha.getInteger("EPILEPTICO") != null){
            if(ficha.getInteger("EPILEPTICO") != 0){
              fichaMedicaGui.getEpileptico().setSelected(true);
            }       
        }
        if(ficha.getInteger("HIPERTENSION") != null){
            if(ficha.getInteger("HIPERTENSION") != 0){
              fichaMedicaGui.getHipertension().setSelected(true);
            }       
        }
        if(ficha.getInteger("DEPORTIVA") != null){
            if(ficha.getInteger("DEPORTIVA") != 0){
              fichaMedicaGui.getLesionDeportiva().setSelected(true);
            }       
        }
        if(ficha.getInteger("OBESIDAD") != null){
            if(ficha.getInteger("OBESIDAD") != 0){
              fichaMedicaGui.getObesidad().setSelected(true);
            }       
        }
        if(ficha.getInteger("REUMA") != null){
            if(ficha.getInteger("REUMA") != 0){
              fichaMedicaGui.getReuma().setSelected(true);
            }       
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
                //int ret=JOptionPane.showConfirmDialog(null, "¿Desea borrar a "+ clienteGui.getNombre()+" "+clienteGui.getApellido()+" ? ",null,JOptionPane.YES_NO_OPTION);
               // if(ret== JOptionPane.YES_OPTION){
                /*    Socio s = new Socio();
                    s.set("DNI", clienteGui.getDni());
                    abmsocio.baja(s);
                   */ clienteGui.limpiarCampos();
                    clienteGui.setVisible(false);
                //}
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
            fichaMedicaGui.setActionListener(this);
            fichaMedicaGui.setLocationRelativeTo(null);
            Socio socio = Socio.first("DNI = ?", clienteGui.getDni().getText());
            Ficha f = Ficha.first("ID_DATOS_PERS = ?", socio.get("ID_DATOS_PERS"));
            //System.out.println(f.get("ID_DATOS_PERS"));
            if(f == null){
                //int ret=JOptionPane.showConfirmDialog(clienteGui, "Socio sin ficha, ¿Desea crear ficha?",null,JOptionPane.YES_NO_OPTION);
                //if(ret == JOptionPane.YES_OPTION){
                fichaNueva = true;
                JOptionPane.showMessageDialog(fichaMedicaGui, "Socio sin ficha, debe cargar ficha");
                fichaMedicaGui.setVisible(true);
                System.out.println("el valor es "+fichaNueva);
            }else{
                    CargarFicha(f); 
                    fichaMedicaGui.setVisible(true);
                    fichaNueva = false;
                    System.out.println("el valor es "+fichaNueva);
                }
    
        }
        if (ae.getSource() == clienteGui.getBotGuardar()) {
            if(!isNuevo){
                    System.out.println("Se modificó uno que existia");
                    s = new Socio();
                    CargarDatosSocio(s);
                    int rows = clienteGui.getTablaActivDefault().getRowCount();
                    LinkedList listaran = new LinkedList();
                    for(int i = 0; i< rows; i++){ //ahora si :P abajo en el alta estaba bien, aca en el modificar no lo habia cambiado :P
                      // boolean val =  clienteGui.getTablaActividades().getValueAt(i, 1).equals(false);
                        System.out.println(clienteGui.getTablaActividades().getValueAt(i, 1) != null );
                       // System.out.println( clienteGui.getTablaActividades().getValueAt(i, 1).equals(false));
                        if(clienteGui.getTablaActividades().getValueAt(i, 1) != null ){//|| clienteGui.getTablaActividades().getValueAt(i, 1).equals(false)){
                                                    if(clienteGui.getTablaActividades().getValueAt(i, 1).equals(true)){

                            Arancel a = Arancel.first("nombre = ?", clienteGui.getTablaActividades().getValueAt(i, 0));
                            listaran.add(a);
                            System.out.println(a.get("nombre"));
                                                    }
                        }

                    }
                    //Object o = clienteGui.getTablaActividades().getValueAt(1, 1).equals(true);
                    if(abmsocio.modificar(s, listaran)){
                        JOptionPane.showMessageDialog(clienteGui, "Socio modificado exitosamente!");
                        clienteGui.bloquearCampos(true);
                        //clienteGui.limpiarCampos();
                        clienteGui.getBotNuevo().setEnabled(true);
                        clienteGui.getBotGuardar().setEnabled(false);
                        clienteGui.getBotModif().setEnabled(true);
                        clienteGui.getBotPago().setEnabled(true);
                        clienteGui.getBotHuella().setEnabled(true);
                        clienteGui.getBotFicha().setEnabled(true);
                        clienteGui.getBotEliminarCancelar().setText("Eliminar");
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
                        clienteGui.getBotModif().setEnabled(true);
                        clienteGui.getBotPago().setEnabled(true);
                        clienteGui.getBotEliminarCancelar().setText("Eliminar");
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
            clienteGui.getBotPago().setEnabled(false);
            clienteGui.getBotHuella().setEnabled(false);
            clienteGui.getBotFicha().setEnabled(false);
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
               // row[1] = false;
                clienteGui.getTablaActivDefault().addRow(row);
            }
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
        /*
            ESTO PERTENECE AL CONTROLADOR DE LA FICHA MEDICA
        */
        if(ae.getSource() == fichaMedicaGui.getAceptar()){
            if(fichaNueva){
               if(altaFicha()){
                   JOptionPane.showMessageDialog(fichaMedicaGui, "Ficha creada exitosamente!");
               }else{
                   JOptionPane.showMessageDialog(fichaMedicaGui, "Ocurrio un error", "Error", JOptionPane.ERROR_MESSAGE);
               }
            }
            if(!fichaNueva){
                int ret=JOptionPane.showConfirmDialog(null, "Desea modificar la ficha?",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                  if(modFicha()){
                     JOptionPane.showMessageDialog(fichaMedicaGui, "Ficha modificada exitosamente!");
                 }else{
                   JOptionPane.showMessageDialog(fichaMedicaGui, "Ocurrio un error", "Error", JOptionPane.ERROR_MESSAGE);
               }
                
            }
        }
    }
    }
    
    private boolean altaFicha(){
        Socio s = Socio.first(" DNI = ?", clienteGui.getDni().getText());
        Ficha nueva = Ficha.create("ID_DATOS_PERS", s.get("ID_DATOS_PERS"), "TEL_EMERG", fichaMedicaGui.getTelEmergencia().getText(), "ALERGICO", fichaMedicaGui.getTextoAlergias().getText(), "MEDICAM", fichaMedicaGui.getTextoMedicamentos().getText(), "OBSERV", fichaMedicaGui.getObservaciones().getText(), "GRUPO_SANG", fichaMedicaGui.getLetraSangui().getSelectedItem(), "FACTOR", fichaMedicaGui.getSigno().getSelectedItem());
        if(fichaMedicaGui.getArtrosis().isSelected()){
            nueva.set("ARTROSIS", 1);
        }else{
            nueva.set("ARTROSIS", 0);
        }
        if(fichaMedicaGui.getAsma().isSelected()){
            nueva.set("ASMA", 1);
        }else{
            nueva.set("ASMA", 0);
        }
        if(fichaMedicaGui.getCardiaco().isSelected()){
            nueva.set("CARDIACO", 1);
        }else{
            nueva.set("CARDIACO", 0);
        }
        if(fichaMedicaGui.getDiabetes().isSelected()){
            nueva.set("DIABETES", 1);
        }else{
            nueva.set("DIABETES", 0);
        }
        if(fichaMedicaGui.getEmbarazo().isSelected()){
            nueva.set("EMBARAZO", 1);
        }else{
            nueva.set("EMBARAZO", 0);
        }
        if(fichaMedicaGui.getEndocrinologia().isSelected()){
            nueva.set("ENDOCRINOLOGIA", 1);
        }else{
            nueva.set("ENDOCRINOLOGIA", 0);
        }
        if(fichaMedicaGui.getHuesoLigam().isSelected()){
            nueva.set("HUESOS", 1);
        }else{
            nueva.set("HUESOS", 0);
        }
        if(fichaMedicaGui.getEnfPulmonar().isSelected()){
            nueva.set("PULMONARES", 1);
        }else{
            nueva.set("PULMONARES", 0);
        }
        if(fichaMedicaGui.getEpileptico().isSelected()){
            nueva.set("EPILEPTICO", 1);
        }else{
            nueva.set("EPILEPTICO", 0);
        }
        if(fichaMedicaGui.getHipertension().isSelected()){
            nueva.set("HIPERTENSION", 1);
        }else{
            nueva.set("HIPERTENSION", 0);
        }
        if(fichaMedicaGui.getLesionDeportiva().isSelected()){
            nueva.set("DEPORTIVA", 1);
        }else{
            nueva.set("DEPORTIVA", 0);
        }
        if(fichaMedicaGui.getObesidad().isSelected()){
            nueva.set("OBESIDAD", 1);
        }else{
            nueva.set("OBESIDAD", 0);
        }
        if(fichaMedicaGui.getReuma().isSelected()){
            nueva.set("REUMA", 1);
        }else{
            nueva.set("REUMA", 0);
        }
        nueva.saveIt();
        return true;
    }
    
    private boolean modFicha(){
        Socio s = Socio.first(" DNI = ?", clienteGui.getDni().getText());
        Ficha nueva = Ficha.first("ID_DATOS_PERS = ?", s.get("ID_DATOS_PERS"));
        nueva.set("TEL_EMERG", fichaMedicaGui.getTelEmergencia().getText(), "ALERGICO", fichaMedicaGui.getTextoAlergias().getText(), "MEDICAM", fichaMedicaGui.getTextoMedicamentos().getText(), "OBSERV", fichaMedicaGui.getObservaciones().getText(), "GRUPO_SANG", fichaMedicaGui.getLetraSangui().getSelectedItem(), "FACTOR", fichaMedicaGui.getSigno().getSelectedItem());
        if(fichaMedicaGui.getArtrosis().isSelected()){
            nueva.set("ARTROSIS", 1);
        }else{
            nueva.set("ARTROSIS", 0);
        }
        if(fichaMedicaGui.getAsma().isSelected()){
            nueva.set("ASMA", 1);
        }else{
            nueva.set("ASMA", 0);
        }
        if(fichaMedicaGui.getCardiaco().isSelected()){
            nueva.set("CARDIACO", 1);
        }else{
            nueva.set("CARDIACO", 0);
        }
        if(fichaMedicaGui.getDiabetes().isSelected()){
            nueva.set("DIABETES", 1);
        }else{
            nueva.set("DIABETES", 0);
        }
        if(fichaMedicaGui.getEmbarazo().isSelected()){
            nueva.set("EMBARAZO", 1);
        }else{
            nueva.set("EMBARAZO", 0);
        }
        if(fichaMedicaGui.getEndocrinologia().isSelected()){
            nueva.set("ENDOCRINOLOGIA", 1);
        }else{
            nueva.set("ENDOCRINOLOGIA", 0);
        }
        if(fichaMedicaGui.getHuesoLigam().isSelected()){
            nueva.set("HUESOS", 1);
        }else{
            nueva.set("HUESOS", 0);
        }
        if(fichaMedicaGui.getEnfPulmonar().isSelected()){
            nueva.set("PULMONARES", 1);
        }else{
            nueva.set("PULMONARES", 0);
        }
        if(fichaMedicaGui.getEpileptico().isSelected()){
            nueva.set("EPILEPTICO", 1);
        }else{
            nueva.set("EPILEPTICO", 0);
        }
        if(fichaMedicaGui.getHipertension().isSelected()){
            nueva.set("HIPERTENSION", 1);
        }else{
            nueva.set("HIPERTENSION", 0);
        }
        if(fichaMedicaGui.getLesionDeportiva().isSelected()){
            nueva.set("DEPORTIVA", 1);
        }else{
            nueva.set("DEPORTIVA", 0);
        }
        if(fichaMedicaGui.getObesidad().isSelected()){
            nueva.set("OBESIDAD", 1);
        }else{
            nueva.set("OBESIDAD", 0);
        }
        if(fichaMedicaGui.getReuma().isSelected()){
            nueva.set("REUMA", 1);
        }else{
            nueva.set("REUMA", 0);
        }
        nueva.saveIt();
        return true;
    }

    public void setIsNuevo(boolean isNuevo) {
        this.isNuevo = isNuevo;
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
