/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.UsuarioGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nico
 */
public class ControladorUsuario implements ActionListener{
    private UsuarioGui usuarioGui;
    private boolean isNuevo;
        private JTable tablaUsuarios;
    private DefaultTableModel tablaUsuariosDefault;

    public ControladorUsuario(UsuarioGui usuarioGui) {
        this.usuarioGui = usuarioGui;
        this.usuarioGui.setActionListener(this);
        tablaUsuarios = this.usuarioGui.getTablaUsuario();
        tablaUsuariosDefault = this.usuarioGui.getTablaUsuarioDefault();
        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
    }

    public void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        usuarioGui.bloquearCampos(true);
        usuarioGui.getBotGuardar().setEnabled(false);
        usuarioGui.getBotModif().setEnabled(true);
        usuarioGui.getBotEliminar().setEnabled(true);
        usuarioGui.getBotEliminar().setText("Eliminar");
        System.out.println("hice click en una actividad");

        /*Cargo la info de las actividades en los campos! */
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == usuarioGui.getBotEliminar()) {
            System.out.println("Boton eliminar pulsado");
            if (usuarioGui.getBotEliminar().getText().equals("Eliminar")) {
                usuarioGui.bloquearCampos(true);
                int ret = JOptionPane.showConfirmDialog(usuarioGui, "¿Desea borrar la actividad.....?", null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    /*Aqui va todo para borrar! */
                    usuarioGui.limpiarCampos();
                    usuarioGui.bloquearCampos(true);
                    usuarioGui.getBotGuardar().setEnabled(false);
                    usuarioGui.getBotModif().setEnabled(false);
                    usuarioGui.getBotEliminar().setEnabled(false);
                }
            } else {
                System.out.println("cancelé !");
                int ret = JOptionPane.showConfirmDialog(usuarioGui, "¿Desea cancelar los cambios?", null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    usuarioGui.limpiarCampos();
                    usuarioGui.bloquearCampos(true);
                    usuarioGui.getBotGuardar().setEnabled(false);
                    usuarioGui.getBotModif().setEnabled(false);
                    usuarioGui.getBotEliminar().setEnabled(false);
                }
            }
        }
        if (ae.getSource() == usuarioGui.getBotGuardar()) {
            if (!isNuevo) {
                /*Aca va todo para guardar uno modificado*/
                System.out.println("Se modificó uno que existia");
                
            } else {
                /*Aca va todo para guardar uno nuevo*/
                System.out.println("Boton guardó uno nuevito");
            }
            usuarioGui.limpiarCampos();
            usuarioGui.bloquearCampos(true);
            usuarioGui.getBotGuardar().setEnabled(false);
            usuarioGui.getBotModif().setEnabled(false);
            usuarioGui.getBotEliminar().setEnabled(false);

        }
        if (ae.getSource() == usuarioGui.getBotModif()) {
            System.out.println("Boton modif pulsado");
            usuarioGui.bloquearCampos(false);
            usuarioGui.getBotEliminar().setText("Cancelar");
            usuarioGui.getBotModif().setEnabled(false);
            usuarioGui.getBotGuardar().setEnabled(true);
            isNuevo = false;

        }
        if (ae.getSource() == usuarioGui.getBotNuevo()) {
            System.out.println("Boton nuevo pulsado");
            isNuevo = true;
            usuarioGui.setBotonesNuevo(true);
            usuarioGui.limpiarCampos();
            usuarioGui.bloquearCampos(false);

        }

    }
    
}
