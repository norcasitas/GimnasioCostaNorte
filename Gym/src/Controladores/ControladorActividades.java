/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMAranceles;
import Interfaces.ActividadesGui;
import Modelos.Arancel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.javalite.activejdbc.LazyList;

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
    }

    public void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        actividadesGui.bloquearCampos(true);
        actividadesGui.getBotGuardar().setEnabled(false);
        actividadesGui.getBotModif().setEnabled(true);
        actividadesGui.getBotEliminarCancelar().setEnabled(true);
        actividadesGui.getBotEliminarCancelar().setText("Eliminar");
        System.out.println("hice click en una actividad");
        int row = actividadesGui.getTablaActividades().getSelectedRow();
        Arancel ar = Arancel.first("id = ?", actividadesGui.getTablaActividadesDefault().getValueAt(row, 0));
        actividadesGui.getActividad().setText(ar.getString("nombre"));
        actividadesGui.getPrecio().setText(String.valueOf(ar.getFloat("precio")));
        actividadesGui.getDesde().setDate(ar.getDate("fecha"));
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
                a.set("fecha", actividadesGui.getDesde().getDate());
                a.set("precio",actividadesGui.getPrecio().getText());
                a.set("activo", 1);
                a.set("nombre", actividadesGui.getActividad().getText().toUpperCase());
                a.set("id", actividadesGui.getTablaActividadesDefault().getValueAt(actividadesGui.getTablaActividades().getSelectedRow(), 0));
                if(abmAranceles.modificar(a)){
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
                
            } else {
                Arancel a = new Arancel();
                a.set("fecha", actividadesGui.getDesde().getDate());
                a.set("precio",actividadesGui.getPrecio().getText());
                a.set("activo", 1);
                a.set("nombre", actividadesGui.getActividad().getText().toUpperCase());
                System.out.println("Boton guardó uno nuevito");
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
            }
            actividadesGui.limpiarCampos();
            actividadesGui.bloquearCampos(true);
            actividadesGui.getBotGuardar().setEnabled(false);
            actividadesGui.getBotModif().setEnabled(false);
            actividadesGui.getBotEliminarCancelar().setEnabled(false);

        }
        if (ae.getSource() == actividadesGui.getBotModif()) {
            System.out.println("Boton modif pulsado");
            actividadesGui.bloquearCampos(false);
            actividadesGui.getBotEliminarCancelar().setText("Cancelar");
            actividadesGui.getBotModif().setEnabled(false);
            actividadesGui.getBotGuardar().setEnabled(true);
            isNuevo = false;

        }
        if (ae.getSource() == actividadesGui.getBotNuevo()) {
            System.out.println("Boton nuevo pulsado");
            isNuevo = true;
            actividadesGui.setBotonesNuevo(true);
            actividadesGui.limpiarCampos();
            actividadesGui.bloquearCampos(false);

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
