/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMUsuarios;
import Interfaces.UsuarioGui;
import Modelos.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author nico
 */
public class ControladorUsuario implements ActionListener{
    private UsuarioGui usuarioGui;
    private boolean isNuevo;
    private ABMUsuarios abmUsuarios;
        private JTable tablaUsuarios;
    private DefaultTableModel tablaUsuariosDefault;

    public ControladorUsuario(UsuarioGui usuarioGui) {
        this.usuarioGui = usuarioGui;
        this.usuarioGui.setActionListener(this);
        abmUsuarios = new ABMUsuarios();
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
        System.out.println("hice click en un usuario");
        int row = usuarioGui.getTablaUsuario().getSelectedRow();
        User u = User.first("USUARIO = ?", usuarioGui.getTablaUsuarioDefault().getValueAt(row, 0));
        usuarioGui.getUser().setText(u.getString("USUARIO"));
        usuarioGui.getPass().setText(u.getString("PASSWD"));
        if(u.getInteger("ADMINIS") == 1){
            usuarioGui.getAdmin().setSelected(true);
            System.out.println("PRAAAAAAAA");
        }
        if(u.getInteger("ADMINIS") == 0){
             usuarioGui.getAdmin().setSelected(false);
              System.out.println("noOOOOOOOOOOOOOO");
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == usuarioGui.getBotEliminar()) {
            System.out.println("Boton eliminar pulsado");
            if (usuarioGui.getBotEliminar().getText().equals("Eliminar")) {
                usuarioGui.bloquearCampos(true);
                int ret = JOptionPane.showConfirmDialog(usuarioGui, "¿Desea borrar el usuario?", null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    /*Aqui va todo para borrar! */
                    User u = new User();
                    u.set("USUARIO", usuarioGui.getUser().getText());
                    if(abmUsuarios.baja(u)){
                        JOptionPane.showMessageDialog(usuarioGui, "Usuario eliminado exitosamente!");
                        LazyList listUsuarios = User.findAll();
                        usuarioGui.getTablaUsuarioDefault().setRowCount(0);
                        Iterator<User> itusu = listUsuarios.iterator();
                        while(itusu.hasNext()){
                            User k = itusu.next();
                            Object row[] = new Object[1];
                            row[0] = k.get("USUARIO");
                            usuarioGui.getTablaUsuarioDefault().addRow(row);
                        }
                    }else{
                        JOptionPane.showMessageDialog(usuarioGui, "Ocurrio un error inesperado", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                    usuarioGui.limpiarCampos();
                    usuarioGui.bloquearCampos(true);
                    usuarioGui.getBotGuardar().setEnabled(false);
                    usuarioGui.getBotModif().setEnabled(false);
                    usuarioGui.getBotEliminar().setEnabled(false);
                    usuarioGui.getAdmin().setEnabled(false);
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
            if((usuarioGui.getUser().getText().length()< 4) || (usuarioGui.getPass().getText().length()< 4)){
                JOptionPane.showMessageDialog(usuarioGui, "El numero minimo de caracteres de usuario y contraseña es de 4 caracteres", "Error!", JOptionPane.ERROR_MESSAGE);
            }else{
            if (!isNuevo) {
                /*Aca va todo para guardar uno modificado*/
                User u = new User();
                u.set("USUARIO", usuarioGui.getUser().getText());
                if(usuarioGui.getAdmin().isSelected()){
                    u.set("ADMINIS",1);
                }else{
                    u.set("ADMINIS",0);
                }
                u.set("PASSWD", usuarioGui.getPass().getText());
               
                if(abmUsuarios.modificar(u)){
                    JOptionPane.showMessageDialog(usuarioGui, "Usuario modificado exitosamente!");
                    LazyList listUsuarios = User.findAll();
                        usuarioGui.getTablaUsuarioDefault().setRowCount(0);
                        Iterator<User> itusu = listUsuarios.iterator();
                        while(itusu.hasNext()){
                            User k = itusu.next();
                            Object row[] = new Object[1];
                            row[0] = k.get("USUARIO");
                            usuarioGui.getTablaUsuarioDefault().addRow(row);
                        }
                }else{
                    JOptionPane.showMessageDialog(usuarioGui, "Ocurrio un error inesperadooooo", "Error!", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Se modificó uno que existia");
                
            } else {
                /*Aca va todo para guardar uno nuevo*/
                 User user = new User();
                if(usuarioGui.getAdmin().isSelected()){
                    user.set("ADMINIS",1);
                }
                if(!usuarioGui.getAdmin().isSelected()){
                    user.set("ADMINIS",0);
                }
                user.set("USUARIO",usuarioGui.getUser().getText());
                user.set("PASSWD", usuarioGui.getPass().getText());
                System.out.println("datos :"+ user.getString("USUARIO")+ " "+ user.getString("PASSWD")+" "+user.getInteger("ADMINIS"));
                if(abmUsuarios.alta(user)){
                    JOptionPane.showMessageDialog(usuarioGui, "Usuario creado exitosamente!");
                    LazyList listUsuarios = User.findAll();
                        usuarioGui.getTablaUsuarioDefault().setRowCount(0);
                        Iterator<User> itusu = listUsuarios.iterator();
                        while(itusu.hasNext()){
                            User k = itusu.next();
                            Object row[] = new Object[1];
                            row[0] = k.get("USUARIO");
                            usuarioGui.getTablaUsuarioDefault().addRow(row);
                        }
                }else{
                    JOptionPane.showMessageDialog(usuarioGui, "Nombre de usuario ya utilizado, cambie el nombre", "Error!", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Boton guardó uno nuevito");
            }
            usuarioGui.limpiarCampos();
            usuarioGui.bloquearCampos(true);
            usuarioGui.getBotGuardar().setEnabled(false);
            usuarioGui.getBotModif().setEnabled(false);
            usuarioGui.getBotEliminar().setEnabled(false);
            usuarioGui.getAdmin().setEnabled(false);
            }

        }
        if (ae.getSource() == usuarioGui.getBotModif()) {
            System.out.println("Boton modif pulsado");
            usuarioGui.bloquearCampos(false);
            usuarioGui.getBotEliminar().setText("Cancelar");
            usuarioGui.getBotModif().setEnabled(false);
            usuarioGui.getBotGuardar().setEnabled(true);
            usuarioGui.getAdmin().setEnabled(true);
            isNuevo = false;
            usuarioGui.getUser().setEnabled(false);
            
            
        }
        if (ae.getSource() == usuarioGui.getBotNuevo()) {
            System.out.println("Boton nuevo pulsado");
            isNuevo = true;
            usuarioGui.setBotonesNuevo(true);
            usuarioGui.limpiarCampos();
            usuarioGui.bloquearCampos(false);
            usuarioGui.getAdmin().setEnabled(true);

        }

    }
    
}
