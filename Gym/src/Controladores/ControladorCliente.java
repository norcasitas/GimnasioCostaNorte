/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Interfaces.AplicacionGUI;
import Interfaces.PanelClientes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author alan
 */
public class ControladorCliente implements ActionListener{
    private boolean BotNuevoPres;
    private AplicacionGUI aplicacionGUI;
    private PanelClientes panelClientes;
    
    public ControladorCliente(AplicacionGUI ap){
        aplicacionGUI = ap;
        panelClientes = ap.getPanelClientes();
        panelClientes.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton)e.getSource();
        if(b.equals(panelClientes.getBotFicha())){
            
        }
        if(b.equals(panelClientes.getBotNuevo())){
            panelClientes.DesBloqBotNuevo();
            panelClientes.LimpiarTexts();
            BotNuevoPres = true;
        }
        if(b.equals(panelClientes.getBotEliminarCancelar()) && BotNuevoPres){
            panelClientes.BloquearTextsBots();
            panelClientes.getBotEliminarCancelar().setText("Eliminar");
            BotNuevoPres = false;
            panelClientes.LimpiarTexts();
            panelClientes.getBotGuardar().setEnabled(true);
            panelClientes.getBotModif().setEnabled(true);
        }
    }
    
}
