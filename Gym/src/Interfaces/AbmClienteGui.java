/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.edisoncor.gui.panel.PanelImage;

/**
 *
 * @author nico
 */
public class AbmClienteGui extends javax.swing.JInternalFrame {

    /**
     * Creates new form AbmClienteGui
     */
    private String namePicture="sin_imagen_disponible.jpg";
    private BufferedImage image;
    
    private DefaultTableModel tablaActivDefault ;
    public AbmClienteGui() {
        initComponents();
        tablaActivDefault= (DefaultTableModel) tablaActividades.getModel();
        setPicture(namePicture);
    }

    public void setActionListener(ActionListener lis){
        this.BotHuella.addActionListener(lis);
        this.botEliminarCancelar.addActionListener(lis);
        this.botFicha.addActionListener(lis);
        this.botGuardar.addActionListener(lis);
        this.botModif.addActionListener(lis);
        this.botNuevo.addActionListener(lis);
        this.botPago.addActionListener(lis);
        this.btnAddPhoto.addActionListener(lis);
        this.btnDeletePhoto.addActionListener(lis);
    }
    public JButton getBotHuella() {
        return BotHuella;
    }

    public JButton getBtnAddPhoto() {
        return btnAddPhoto;
    }

    public JButton getBtnDeletePhoto() {
        return btnDeletePhoto;
    }



    public JTextField getApellido() {
        return apellido;
    }

    public JButton getBotEliminarCancelar() {
        return botEliminarCancelar;
    }
    
    public JDateChooser getFechaNacimJDate() {
        return fechaNacim;
    }

    public JButton getBotFicha() {
        return botFicha;
    }

    public JButton getBotGuardar() {
        return botGuardar;
    }

    public JButton getBotModif() {
        return botModif;
    }

    public JButton getBotNuevo() {
        return botNuevo;
    }

    public JButton getBotPago() {
        return botPago;
    }

    public JTextField getDireccion() {
        return direccion;
    }

    public JTextField getDni() {
        return dni;
    }

    public JDateChooser getFechaNacim() {
        return fechaNacim;
    }

    public JLabel getLabelFechaIngreso() {
        return labelFechaIngreso;
    }

    public JLabel getLabelFechaVenci() {
        return labelFechaVenci;
    }



    public JTextField getNombre() {
        return nombre;
    }

    public JComboBox getSexo() {
        return sexo;
    }

    public JTextField getTelefono() {
        return telefono;
    }
    
    public void setBotonesNuevo(boolean si){
        this.botModif.setEnabled(!si);
        this.botPago.setEnabled(!si);
        this.botFicha.setEnabled(!si);
        this.BotHuella.setEnabled(!si);
        this.botNuevo.setEnabled(si);
        this.botGuardar.setEnabled(si);
        this.btnAddPhoto.setEnabled(si);
        this.btnDeletePhoto.setEnabled(si);
        if(si)
        this.botEliminarCancelar.setText("Cancelar");
        else
            this.botEliminarCancelar.setText("Eliminar");
    }
    
    public void bloquearCampos(boolean si){
        nombre.setEnabled(!si);
        apellido.setEnabled(!si);
        dni.setEnabled(!si);
        direccion.setEnabled(!si);
        telefono.setEnabled(!si);
        fechaNacim.setEnabled(!si);
        fechaNacim.getDateEditor().setEnabled(false);
        tablaActividades.setEnabled(!si);
        sexo.setEnabled(!si);
        btnAddPhoto.setEnabled(!si);
        btnDeletePhoto.setEnabled(!si);
    }
    
        public void limpiarCampos(){
        nombre.setText("");
        apellido.setText("");
        dni.setText("");
        direccion.setText("");
        telefono.setText("");
        labelFechaIngreso.setText("");
        labelFechaVenci.setText("");
        fechaNacim.setDate(Calendar.getInstance().getTime());
        tablaActividades.clearSelection();
            setPicture("sin_imagen_disponible.jpg");
        

    }

    public void setPicture(String nombre){
        namePicture=nombre;
        String rutaImagen= System.getProperty("user.dir");
        rutaImagen+="/user_images/"+nombre;
        pnlImageSocio.setIcon(new javax.swing.ImageIcon(rutaImagen));
        pnlImageSocio.repaint();
        if("sin_imagen_disponible.jpg".equals(nombre)){
            image=null;
        }
    }
    
       public void setPicture(BufferedImage image){
        this.image= image;
        this.namePicture= "hay_imagen";
        pnlImageSocio.setIcon(new javax.swing.ImageIcon(image));
        pnlImageSocio.repaint();
    } 
    
    public DefaultTableModel getTablaActivDefault() {
        return tablaActivDefault;
    }

    public JTable getTablaActividades() {
        return tablaActividades;
    }

    public String getNamePicture() {
        return namePicture;
    }

    public BufferedImage getImage() {
        return image;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        botFicha = new javax.swing.JButton();
        apellido = new javax.swing.JTextField();
        dni = new javax.swing.JTextField();
        telefono = new javax.swing.JTextField();
        direccion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        sexo = new javax.swing.JComboBox();
        BotHuella = new javax.swing.JButton();
        fechaNacim = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        botNuevo = new javax.swing.JButton();
        botModif = new javax.swing.JButton();
        botEliminarCancelar = new javax.swing.JButton();
        botGuardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaActividades = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        labelFechaIngreso = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        labelFechaVenci = new javax.swing.JLabel();
        botPago = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAddPhoto = new javax.swing.JButton();
        btnDeletePhoto = new javax.swing.JButton();
        pnlImageSocio = new org.edisoncor.gui.panel.PanelImage();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(595, 424));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(595, 424));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Cliente"));

        jLabel2.setText("Nombre");

        jLabel3.setText("Apellido");

        jLabel4.setText("DNI");

        jLabel5.setText("Telefono");

        jLabel6.setText("Direccion");

        jLabel7.setText("Fecha de nac.");

        botFicha.setText("Ver ficha medica");

        dni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dniActionPerformed(evt);
            }
        });

        jLabel11.setText("Sexo");

        sexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Femenino", "Masculino" }));

        BotHuella.setText("Cargar huella");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(10, 10, 10))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombre)
                            .addComponent(dni)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel11)
                                .addGap(27, 27, 27)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(telefono, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                            .addComponent(sexo, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 176, Short.MAX_VALUE)
                        .addComponent(botFicha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BotHuella))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(4, 4, 4)
                        .addComponent(fechaNacim, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(direccion))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(10, 10, 10)
                        .addComponent(apellido)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fechaNacim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botFicha)
                            .addComponent(BotHuella)))))
        );

        jPanel5.setLayout(new org.jdesktop.swingx.HorizontalLayout());

        botNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar.png"))); // NOI18N
        botNuevo.setText("Nuevo");
        jPanel5.add(botNuevo);

        botModif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/modificar.png"))); // NOI18N
        botModif.setText("Modificar");
        jPanel5.add(botModif);

        botEliminarCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/borrar.png"))); // NOI18N
        botEliminarCancelar.setText("Eliminar");
        jPanel5.add(botEliminarCancelar);

        botGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/guardar.png"))); // NOI18N
        botGuardar.setText("Guardar");
        jPanel5.add(botGuardar);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Gimnasio"));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Actividades"));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        tablaActividades.setAutoCreateRowSorter(true);
        tablaActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null,  new Boolean(false)},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Actividad", "Realiza"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaActividades.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tablaActividades.setShowHorizontalLines(false);
        tablaActividades.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tablaActividades);
        if (tablaActividades.getColumnModel().getColumnCount() > 0) {
            tablaActividades.getColumnModel().getColumn(1).setMaxWidth(60);
        }

        jPanel4.add(jScrollPane1);

        jLabel1.setText("Fecha de ingreso:");

        labelFechaIngreso.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel8.setText("Próximo vencimiento:");

        labelFechaVenci.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        botPago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/pagar.png"))); // NOI18N
        botPago.setText("Registrar Pago");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 200));

        btnAddPhoto.setText("Agregar");

        btnDeletePhoto.setText("Eliminar");

        pnlImageSocio.setIcon(new javax.swing.ImageIcon("C:\\Users\\NicoOrcasitas\\Downloads\\sin_imagen_disponible.jpg")); // NOI18N
        pnlImageSocio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlImageSocioMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlImageSocioLayout = new javax.swing.GroupLayout(pnlImageSocio);
        pnlImageSocio.setLayout(pnlImageSocioLayout);
        pnlImageSocioLayout.setHorizontalGroup(
            pnlImageSocioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlImageSocioLayout.setVerticalGroup(
            pnlImageSocioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnAddPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btnDeletePhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
            .addComponent(pnlImageSocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(pnlImageSocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDeletePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelFechaIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelFechaVenci, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botPago)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(labelFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labelFechaVenci, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botPago))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dniActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dniActionPerformed

    private void pnlImageSocioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlImageSocioMouseClicked
        if(evt.getClickCount()==2){
            JFrame k= new JFrame("VER  IMAGEN DE SOCIO");
            k.setResizable(true);
            k.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            k.setLayout(new BorderLayout());
            PanelImage p= new PanelImage();
            p.setIcon(this.pnlImageSocio.getIcon());
            k.add(p);
            k.setSize(p.getIcon().getIconWidth(),p.getIcon().getIconHeight());
            k.setLocationRelativeTo(null);
            k.setVisible(true);
            k.toFront();
        }
        
    }//GEN-LAST:event_pnlImageSocioMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotHuella;
    private javax.swing.JTextField apellido;
    private javax.swing.JButton botEliminarCancelar;
    private javax.swing.JButton botFicha;
    private javax.swing.JButton botGuardar;
    private javax.swing.JButton botModif;
    private javax.swing.JButton botNuevo;
    private javax.swing.JButton botPago;
    private javax.swing.JButton btnAddPhoto;
    private javax.swing.JButton btnDeletePhoto;
    private javax.swing.JTextField direccion;
    private javax.swing.JTextField dni;
    private com.toedter.calendar.JDateChooser fechaNacim;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelFechaIngreso;
    private javax.swing.JLabel labelFechaVenci;
    private javax.swing.JTextField nombre;
    private org.edisoncor.gui.panel.PanelImage pnlImageSocio;
    private javax.swing.JComboBox sexo;
    private javax.swing.JTable tablaActividades;
    private javax.swing.JTextField telefono;
    // End of variables declaration//GEN-END:variables



}