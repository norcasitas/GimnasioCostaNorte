/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import BD.ConexionBD;
import Controladores.ControladorIngreso;
import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentEvent;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentListener;
import com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentVetoException;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author Nico
 */
public class CargarHuellaGui extends javax.swing.JDialog {

    private EnumMap<DPFPFingerIndex, DPFPTemplate> template = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);
    ConexionBD con = new ConexionBD();
    private DPFPTemplate templateIndividual;
    private DPFPFingerIndex fingerIndividual;
    private int idCliente;
   

    /**
     * Creates new form CargarHuellaGui
     *
     * @param owner
     * @param maxCount
     * @param templates
     * @param reasonToFail
     */
    public CargarHuellaGui(Frame owner, int id) throws SQLException {
        super(owner, true);
        initComponents();
        ControladorIngreso.Lector.stopCapture();
        Connection c = con.conectar();
        String query = "SELECT * FROM huellas where client_id="+id;
        PreparedStatement stmt = c.prepareStatement(query);
         //stmt.setInt(1,id);
        ResultSet rs = stmt.executeQuery(query);
        

        
        
        //Ejecuta la sentencia
        if(rs.next()){
        fingerIndividual = DPFPFingerIndex.valueOf(rs.getString(3));
        byte templateBuffer[] = rs.getBytes(2);
        //System.out.println("entre");
        //Crea una nueva plantilla a partir de la guardada en la base de datos
        templateIndividual = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
        CargarHuellaGui.this.template.put(fingerIndividual, templateIndividual);
        }
        con.desconectar();
        idCliente = id;
        EnumSet<DPFPFingerIndex> fingers = EnumSet.noneOf(DPFPFingerIndex.class);
        fingers.addAll(template.keySet());
        panelEnrolamiento.setEnrolledFingers(fingers);
        panelEnrolamiento.setMaxEnrollFingerCount(1);
        //System.out.println("algo anda mal");
        panelEnrolamiento.addEnrollmentListener(new DPFPEnrollmentListener() {
            public void fingerDeleted(DPFPEnrollmentEvent e) throws DPFPEnrollmentVetoException {

                CargarHuellaGui.this.template.remove(e.getFingerIndex());
                borrarHuella();
                //System.out.println("borre el dedito");

            }

            public void fingerEnrolled(DPFPEnrollmentEvent e) throws DPFPEnrollmentVetoException {

                CargarHuellaGui.this.template.put(e.getFingerIndex(), e.getTemplate());
                templateIndividual = e.getTemplate();
                fingerIndividual = e.getFingerIndex();
                //System.out.println("enrole el dedito");
                guardarHuella();
            }
        });

        getContentPane().setLayout(new BorderLayout());

        JButton closeButton = new JButton("cerrar");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(closeButton);
        add(panelEnrolamiento, BorderLayout.CENTER);
        add(bottom, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e){
                //System.out.println("Se cerró la ventana");
                if(!ControladorIngreso.Lector.isStarted())
                                ControladorIngreso.Lector.startCapture();//End Dialog

            }
        });
    }

    private void borrarHuella() {
        Connection c = con.conectar(); //establece la conexion con la BD
        try (PreparedStatement guardarStmt = c.prepareStatement(" DELETE FROM huellas WHERE client_id=" + idCliente)) {
            //Ejecuta la sentencia
            guardarStmt.execute();
            JOptionPane.showMessageDialog(null, "Huella borrada Correctamente");
            con.desconectar();
        } catch (SQLException ex) {
            //Si ocurre un error lo indica en la consola
            System.err.println("Error al borrar los datos de la huella." + ex);
        } finally {
            con.desconectar();
        }

    }

    public void guardarHuella() {
        //Obtiene los datos del template de la huella actual

        ByteArrayInputStream datosHuella = new ByteArrayInputStream(templateIndividual.serialize());
        Integer tamañoHuella = templateIndividual.serialize().length;

        //Pregunta el nombre de la persona a la cual corresponde dicha huella
        try {
            //Establece los valores para la sentencia SQL
            Connection c = con.conectar(); //establece la conexion con la BD
            try (PreparedStatement guardarStmt = c.prepareStatement("INSERT INTO huellas(huella, dedo,client_id) values(?,?,?)")) {
                guardarStmt.setBinaryStream(1, datosHuella, tamañoHuella);
                guardarStmt.setString(2, fingerIndividual.toString());
                guardarStmt.setInt(3, idCliente);
                //Ejecuta la sentencia
                guardarStmt.execute();
            }
            JOptionPane.showMessageDialog(null, "Huella Guardada Correctamente");
            con.desconectar();
        } catch (SQLException ex) {
            //Si ocurre un error lo indica en la consola
            System.err.println("Error al guardar los datos de la huella." + ex);
        } finally {
            con.desconectar();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelEnrolamiento = new com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentControl();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelEnrolamiento, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelEnrolamiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CargarHuellaGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CargarHuellaGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CargarHuellaGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CargarHuellaGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Map<DPFPFingerIndex, DPFPTemplate> template = new EnumMap<DPFPFingerIndex, DPFPTemplate>(DPFPFingerIndex.class);

                CargarHuellaGui dialog;
                try {
                    dialog = new CargarHuellaGui(null, 1);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(CargarHuellaGui.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.digitalpersona.onetouch.ui.swing.DPFPEnrollmentControl panelEnrolamiento;
    // End of variables declaration//GEN-END:variables
}
