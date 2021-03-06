/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 *
 * @author nico
 */
public class PrincipalGui extends javax.swing.JFrame {
    /**
     * Creates new form PrincipalGui
     */
    private DesktopPaneImage desktop;
    
    public PrincipalGui() {
        initComponents();
        desktop= new DesktopPaneImage();
        this.add(desktop);
        
    }
    

    
    public void setActionListener(ActionListener lis){
        this.botDesconectar.addActionListener(lis);
        this.botSocios.addActionListener(lis);
        this.botSalir.addActionListener(lis);
        this.botActividades.addActionListener(lis);
        this.botUsuario.addActionListener(lis);
        this.ingreso.addActionListener(lis);
        this.declaracion.addActionListener(lis);
        this.impresionAranceles.addActionListener(lis);
        this.depurar.addActionListener(lis);
    }

    public JMenuItem getDepurar() {
        return depurar;
    }

    public JMenuItem getBotDesconectar() {
        return botDesconectar;
    }

    public JMenuItem getImpresionAranceles() {
        return impresionAranceles;
    }

    public JButton getBotUsuario() {
        return botUsuario;
    }

    public JMenuItem getBotSalir() {
        return botSalir;
    }

    public JButton getBotSocios() {
        return botSocios;
    }

    public JMenuItem getDeclaracion() {
        return declaracion;
    }

    public DesktopPaneImage getDesktop() {
        return desktop;
    }

    public JButton getBotActividades() {
        return botActividades;
    }

    public JButton getIngreso() {
        return ingreso;
    }


    

    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBotones = new javax.swing.JPanel();
        botSocios = new javax.swing.JButton();
        botActividades = new javax.swing.JButton();
        botUsuario = new javax.swing.JButton();
        ingreso = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        botDesconectar = new javax.swing.JMenuItem();
        botSalir = new javax.swing.JMenuItem();
        depurar = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        declaracion = new javax.swing.JMenuItem();
        impresionAranceles = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        tecPro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gimnasio Costa Norte");
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/icono.png")).getImage());

        botSocios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/clients.png"))); // NOI18N
        botSocios.setToolTipText("Gestión de socios");

        botActividades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/actividad.png"))); // NOI18N
        botActividades.setToolTipText("Gestión de actividades");

        botUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/usuario.png"))); // NOI18N
        botUsuario.setToolTipText("Gestión de usuarios");

        ingreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ingreso.png"))); // NOI18N
        ingreso.setToolTipText("Ingreso de clientes");

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(botSocios, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(botActividades, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(botUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(ingreso, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelBotonesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {botActividades, botSocios, botUsuario});

        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addComponent(botSocios, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(botActividades, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(botUsuario)
                .addGap(0, 0, 0)
                .addComponent(ingreso)
                .addGap(0, 0, 0))
        );

        panelBotonesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {botActividades, botSocios, botUsuario});

        getContentPane().add(panelBotones, java.awt.BorderLayout.LINE_START);

        jMenu1.setText("Archivo");
        jMenu1.setToolTipText("");

        botDesconectar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cerrar.png"))); // NOI18N
        botDesconectar.setText("Cerrar sesión");
        jMenu1.add(botDesconectar);

        botSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/apagar.png"))); // NOI18N
        botSalir.setText("Salir");
        jMenu1.add(botSalir);

        depurar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/1397349580_virus_definitions.png"))); // NOI18N
        depurar.setText("Depurar datos");
        jMenu1.add(depurar);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Documentos");

        declaracion.setText("Declaración jurada");
        jMenu3.add(declaracion);

        impresionAranceles.setText("Aranceles para imprimir");
        jMenu3.add(impresionAranceles);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Acerca de");

        tecPro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/acerca.png"))); // NOI18N
        tecPro.setText("Tec-Pro Software");
        tecPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tecProActionPerformed(evt);
            }
        });
        jMenu2.add(tecPro);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tecProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tecProActionPerformed
        AcercaDe acercaDe= new AcercaDe(this, true);
        acercaDe.setLocationRelativeTo(this);
        acercaDe.setVisible(true);
    }//GEN-LAST:event_tecProActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botActividades;
    private javax.swing.JMenuItem botDesconectar;
    private javax.swing.JMenuItem botSalir;
    private javax.swing.JButton botSocios;
    private javax.swing.JButton botUsuario;
    private javax.swing.JMenuItem declaracion;
    private javax.swing.JMenuItem depurar;
    private javax.swing.JMenuItem impresionAranceles;
    private javax.swing.JButton ingreso;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JMenuItem tecPro;
    // End of variables declaration//GEN-END:variables
}
