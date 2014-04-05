/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMAranceles;
import Interfaces.ActividadesGui;
import Modelos.Arancel;
import Modelos.Combo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 *
 * @author nico
 */
public class ControladorActividades implements ActionListener {

    private ActividadesGui actividadesGui;
    private JTable tablaActividades;
    private DefaultTableModel tablaActividadesDefault;
    private boolean isNuevo;
    private ABMAranceles abmAranceles;
    private JTable tablaActCombo;
    private DefaultTableModel tablaActComboDefault;
    private Arancel ar;
    
    public ControladorActividades(ActividadesGui actividadesGui) {
        this.actividadesGui = actividadesGui;
        abmAranceles = new ABMAranceles();
        actividadesGui.setActionListener(this);
        tablaActividades = this.actividadesGui.getTablaActividades();
        tablaActividadesDefault = this.actividadesGui.getTablaActividadesDefault();
        tablaActividades.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        tablaActCombo = this.actividadesGui.getTablaActCombo();
        tablaActComboDefault= this.actividadesGui.getTablaActComboDefault();
        this.actividadesGui.getCategoria().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                habilitarTablaCombo();
            }
        });
    }

    public void habilitarTablaCombo(){
        if(actividadesGui.getCategoria().getSelectedItem().equals("COMBO")){
            actividadesGui.getDias().setEnabled(false);
            tablaActCombo.setEnabled(!actividadesGui.getBotModif().isEnabled());
                
            
            if(isNuevo){
                cargarActividadesNuevoCombo();
                System.out.println("nuevo");
            }
            else{
                cargarActividadesCombo(ar.getInteger("id"));
                System.out.println("viejo");
            }
        }
        else{
            tablaActComboDefault.setRowCount(0);
            tablaActCombo.setEnabled(false);
            actividadesGui.getDias().setEnabled(true);
        }
    }
    
    private void cargarActividadesNuevoCombo(){
        tablaActComboDefault.setRowCount(0);
        LazyList<Arancel> activs=Arancel.findAll();
        Iterator<Arancel> itAr= activs.iterator();
        Arancel aran;
        while(itAr.hasNext()){
            aran= itAr.next();
            Object row[] = new Object[4];
            row[0] = aran.getInteger("id");
            row[1] = aran.getString("nombre");
            row[2] = false;
            row[3] = 0;
            tablaActComboDefault.addRow(row);
        }
    }
    private void cargarActividadesCombo(int idCombo){
        System.out.println(idCombo);
        tablaActComboDefault.setRowCount(0);
        LazyList<Combo> activsCombo=Combo.where("id_combo = ?", idCombo);
        Iterator<Combo> it= activsCombo.iterator();
        LinkedList<Integer> activsDelCombo = new LinkedList<>();
        while(it.hasNext()){
            Combo combo= it.next();
            activsDelCombo.add(combo.getInteger("id_activ"));
        }
        LinkedList<Integer> todasActivs= new LinkedList<>();
        LazyList<Arancel> activs=Arancel.where("categoria <> ?", "COMBO");
        Iterator<Arancel> itAr= activs.iterator();
        while(itAr.hasNext()){
            Arancel aran= itAr.next();
            todasActivs.add(aran.getInteger("id"));
        }
        todasActivs.removeAll(activsDelCombo);
        it= activsCombo.iterator();
        Arancel aran;
        while(it.hasNext()){
            Combo combo= it.next();
            aran= Arancel.findFirst("id = ?",combo.getInteger("id_activ") );
            Object row[] = new Object[4];
            row[0] = aran.getInteger("id");
            row[1] = aran.getString("nombre");
            row[2] = true;
            row[3] = combo.getInteger("dias");
            tablaActComboDefault.addRow(row);
        }
        Iterator<Integer> itTodas= todasActivs.iterator();
        while(itTodas.hasNext()){
            aran= Arancel.findFirst("id = ?",itTodas.next());
            Object row[] = new Object[4];
            row[0] = aran.getInteger("id");
            row[1] = aran.getString("nombre");
            row[2] = false;
            row[3] = 0;
            tablaActComboDefault.addRow(row);
        }
    }
    
    
    /*Esta la voy a usar para guando pongas guardar*/
    private void guardarActivs(Object idCombo){
        int rows= tablaActCombo.getRowCount();
        Base.openTransaction();
        Combo.delete("id_combo = ? ",  actividadesGui.getTablaActividadesDefault().getValueAt(actividadesGui.getTablaActividades().getSelectedRow(), 0) );
        Base.commitTransaction();
        for (int i=0;i<rows;i++){
            if(tablaActCombo.getValueAt(i, 2).equals(true)){
                Base.openTransaction();
                Combo.createIt("id_combo",idCombo,"id_activ",tablaActCombo.getValueAt(i, 0),"dias",tablaActCombo.getValueAt(i, 3));
                Base.commitTransaction();
            }
        }
        
    }
    public void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        actividadesGui.getBotGuardar().setEnabled(false);
        actividadesGui.getBotModif().setEnabled(true);
        actividadesGui.getBotEliminarCancelar().setEnabled(true);
        actividadesGui.getBotEliminarCancelar().setText("Eliminar");
        System.out.println("hice click en una actividad");
        int row = actividadesGui.getTablaActividades().getSelectedRow();
        ar = Arancel.first("id = ?", actividadesGui.getTablaActividades().getValueAt(row, 0));
        actividadesGui.getActividad().setText(ar.getString("nombre"));
        int diasSemana= ar.getInteger("dias");
        if(diasSemana==99){
            actividadesGui.getPaseLibre().setSelected(true);
            actividadesGui.getDias().setValue(0);
        }
        else{
           actividadesGui.getPaseLibre().setSelected(false);
           actividadesGui.getDias().setValue(diasSemana);
        }
        actividadesGui.getPrecio().setText(String.valueOf(ar.getFloat("precio")));
        actividadesGui.getDesde().setDate(ar.getDate("fecha"));
        actividadesGui.getCategoria().setSelectedItem(ar.get("categoria"));
        if(actividadesGui.getCategoria().getSelectedItem().equals("COMBO")){
            cargarActividadesCombo(ar.getInteger("id"));
            
        }
        else{
            tablaActComboDefault.setRowCount(0);
        }
        actividadesGui.bloquearCampos(true);
        /*Cargo la info de las actividades en los campos! */
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == actividadesGui.getBotEliminarCancelar()) {
            System.out.println("Boton eliminar pulsado");
            if (actividadesGui.getBotEliminarCancelar().getText().equals("Eliminar")) {
                actividadesGui.bloquearCampos(true);
                int ret = JOptionPane.showConfirmDialog(actividadesGui, "¿Desea borrar la actividad.....?", null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    Arancel ar = Arancel.first("nombre = ?", actividadesGui.getActividad().getText());
                    abmAranceles.baja(ar);
                    LazyList ListAranceles = Arancel.where("activo = ?", 1);
                    actividadesGui.getTablaActividadesDefault().setRowCount(0);
                    Iterator<Arancel> it = ListAranceles.iterator();
                    while(it.hasNext()){
                        Arancel aran = it.next();
                        Object row[] = new Object[3];
                        row[0] = aran.getInteger("id");
                        row[1] = aran.getString("nombre");
                        row[2] = aran.getFloat("precio");
                        actividadesGui.getTablaActividadesDefault().addRow(row);
                     }
                    /*Aqui va todo para borrar! */
                    actividadesGui.limpiarCampos();
                    actividadesGui.bloquearCampos(true);
                    actividadesGui.getBotGuardar().setEnabled(false);
                    actividadesGui.getBotModif().setEnabled(false);
                    actividadesGui.getBotEliminarCancelar().setEnabled(false);
                }
            } else {
                System.out.println("cancelé !");
                int ret = JOptionPane.showConfirmDialog(actividadesGui, "¿Desea cancelar los cambios?", null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    actividadesGui.limpiarCampos();
                    actividadesGui.bloquearCampos(true);
                    actividadesGui.getBotGuardar().setEnabled(false);
                    actividadesGui.getBotModif().setEnabled(false);
                    actividadesGui.getBotEliminarCancelar().setEnabled(false);
                }
            }
        }
        if (ae.getSource() == actividadesGui.getBotGuardar()) {
            if (!isNuevo) {
                /*Aca va todo para guardar uno modificado*/
                System.out.println("Se modificó uno que existia");
                Arancel a = new Arancel();
                boolean error=false;
                int vecesSemana;
                if(actividadesGui.getPaseLibre().isSelected()){
                    vecesSemana=99; //99 veces por semana, para simular muchas je je je
                }
                else{
                    vecesSemana= (Integer)actividadesGui.getDias().getValue();
                }
                /*Aca tenés la variable de la cantidad de veces, si es 99 significa pase libre*/
                
                System.out.println(vecesSemana);
                a.set("fecha", actividadesGui.getDesde().getDate());
try{
                    BigDecimal precio=BigDecimal.valueOf(Double.valueOf(actividadesGui.getPrecio().getText()));
                                    a.set("precio",precio);

                }catch(java.lang.NumberFormatException ex){
                    error=true;
                }                a.set("activo", 1);
                a.set("categoria", actividadesGui.getCategoria().getSelectedItem().toString().toUpperCase());
                a.set("nombre", actividadesGui.getActividad().getText().toUpperCase());
                a.set("dias", vecesSemana);
                a.set("id", actividadesGui.getTablaActividadesDefault().getValueAt(actividadesGui.getTablaActividades().getSelectedRow(), 0));
                if(!error){
                if(abmAranceles.modificar(a)){
                    if(actividadesGui.getCategoria().getSelectedItem().equals("COMBO"))
                        guardarActivs(actividadesGui.getTablaActividadesDefault().getValueAt(actividadesGui.getTablaActividades().getSelectedRow(), 0));
                    JOptionPane.showMessageDialog(actividadesGui, "Actividad modificado exitosamente!");
                    actividadesGui.bloquearCampos(true);
                    LazyList ListAranceles = Arancel.where("activo = ?", 1);
                    actividadesGui.getTablaActividadesDefault().setRowCount(0);
                    Iterator<Arancel> it = ListAranceles.iterator();
                    while(it.hasNext()){
                        Arancel ar = it.next();
                        Object row[] = new Object[3];
                        row[0] = ar.getInteger("id");
                        row[1] = ar.getString("nombre");
                        row[2] = ar.getFloat("precio");
                        actividadesGui.getTablaActividadesDefault().addRow(row);
                     }

                    
                }else{ 
                    JOptionPane.showMessageDialog(actividadesGui, "Ocurrió un error, revise los datos", "Error!", JOptionPane.ERROR_MESSAGE);
                }
                                }else{
                JOptionPane.showMessageDialog(actividadesGui, "Precio incorrecto", "error", JOptionPane.ERROR_MESSAGE);
  
                }
                
            }else{ 
                Arancel a = new Arancel();
                boolean error=false;
                a.set("fecha", actividadesGui.getDesde().getDate());
                try{
                    BigDecimal precio=BigDecimal.valueOf(Double.valueOf(actividadesGui.getPrecio().getText()));
                                    a.set("precio",precio);

                }catch(java.lang.NumberFormatException ex){
                    error=true;
                }
                int vecesSemana;
                if(actividadesGui.getPaseLibre().isSelected()){
                    vecesSemana=99; //99 veces por semana, para simular muchas je je je
                }
                else{
                    vecesSemana= (Integer)actividadesGui.getDias().getValue();
                }
                a.set("activo", 1);
                a.set("nombre", actividadesGui.getActividad().getText().toUpperCase());
                a.set("dias", vecesSemana);
                a.set("categoria", actividadesGui.getCategoria().getSelectedItem().toString().toUpperCase());
                System.out.println("Boton guardó uno nuevito");
                if(!error){
                if(abmAranceles.alta(a)){
                    JOptionPane.showMessageDialog(actividadesGui, "Actividad guardada exitosamente!");
                    actividadesGui.bloquearCampos(true);
                    LazyList ListAranceles = Arancel.where("activo = ?", 1);
                    actividadesGui.getTablaActividadesDefault().setRowCount(0);
                    Iterator<Arancel> it = ListAranceles.iterator();
                    while(it.hasNext()){
                        Arancel ar = it.next();
                        Object row[] = new Object[3];
                        row[0] = ar.getInteger("id");
                        row[1] = ar.getString("nombre");
                        row[2] = ar.getFloat("precio");
                        actividadesGui.getTablaActividadesDefault().addRow(row);
                     }
                }else{
                    JOptionPane.showMessageDialog(actividadesGui, "Ocurrió un error, revise los datos", "Error!", JOptionPane.ERROR_MESSAGE);
                }
                }else{
                JOptionPane.showMessageDialog(actividadesGui, "Precio incorrecto", "error", JOptionPane.ERROR_MESSAGE);
  
                }
            
            actividadesGui.limpiarCampos();
            actividadesGui.bloquearCampos(true);
            actividadesGui.getBotGuardar().setEnabled(false);
            actividadesGui.getBotModif().setEnabled(false);
            actividadesGui.getBotEliminarCancelar().setEnabled(false);
            }
                   }   
        if (ae.getSource() == actividadesGui.getBotModif()) {
            System.out.println("Boton modif pulsado");
            actividadesGui.bloquearCampos(false);
            actividadesGui.getBotEliminarCancelar().setText("Cancelar");
            actividadesGui.getBotModif().setEnabled(false);
            actividadesGui.getBotGuardar().setEnabled(true);
            if(actividadesGui.getCategoria().getSelectedItem().equals("COMBO")){
                actividadesGui.getDias().setEnabled(false);
                actividadesGui.getPaseLibre().setEnabled(false);
            }
            else{
               actividadesGui.getDias().setEnabled(true);
            }
            isNuevo = false;
            actividadesGui.getDias().setEnabled(!actividadesGui.getPaseLibre().isSelected());

        }
        if (ae.getSource() == actividadesGui.getBotNuevo()) {
            System.out.println("Boton nuevo pulsado");
            isNuevo = true;
            actividadesGui.setBotonesNuevo(true);
            actividadesGui.limpiarCampos();
            actividadesGui.bloquearCampos(false);
            actividadesGui.getCategoria().setSelectedIndex(0);
            actividadesGui.getDias().setEnabled(!actividadesGui.getPaseLibre().isSelected());
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
}
