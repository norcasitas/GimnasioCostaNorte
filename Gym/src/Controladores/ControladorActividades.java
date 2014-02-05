/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.ActividadesGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nico
 */
public class ControladorActividades implements ActionListener {

    private ActividadesGui actividadesGui;
    private JTable tablaActividades;
    private DefaultTableModel tablaActividadesDefault;
    private boolean isNuevo;

    public ControladorActividades(ActividadesGui actividadesGui) {
        this.actividadesGui = actividadesGui;
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
                
            } else {
                /*Aca va todo para guardar uno nuevo*/
                System.out.println("Boton guardó uno nuevito");
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
}
