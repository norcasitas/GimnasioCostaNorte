/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import ABMs.ABMSocios;
import Modelos.Arancel;
import Modelos.Pago;
import Modelos.Socio;
import Modelos.Socioarancel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 *
 * @author nico
 */
public class RegistrarPagoGui extends javax.swing.JDialog {

    Socio socio;
    DefaultTableModel tablaDefault;
    /**
     * Creates new form RegistrarPagoGui
     */
    public RegistrarPagoGui(java.awt.Frame parent, boolean modal, Socio socio) {
        super(parent, modal);
        initComponents();
        tablaDefault= (DefaultTableModel)tablaActividades.getModel();
        this.socio= socio;
         fecha.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Calendar cal= fecha.getCalendar();
                cal.setTime(fecha.getDate());
                cal.add(Calendar.MONTH, 1);
                fechaVence.setCalendar(cal);
            }
        });
         fecha.setDate(Calendar.getInstance().getTime());
        System.out.println(fecha.getDate());
        nombreCliente.setText(socio.getString("NOMBRE")+" "+socio.getString("APELLIDO"));
        cargarActividades();
        calcularTotal();
        System.out.println("esta todo legal");
       tablaActividades.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                calcularTotal();
            }
        });
       
    }

    private void calcularTotal(){
         BigDecimal totalB= new BigDecimal(0);
        if(tablaDefault.getRowCount()>-1){
        int rows = tablaDefault.getRowCount();
                    for(int i = 0; i< rows; i++){
                        if((boolean)tablaActividades.getValueAt(i, 2) ==true){
                            BigDecimal precio=(BigDecimal)tablaActividades.getValueAt(i, 1);
                            
                            totalB= totalB.add(BigDecimal.valueOf(precio.doubleValue()));
                        }
                    }
                                        total.setText(totalB.setScale(2, RoundingMode.CEILING).toString());

        }
    }
    
    private void cargarActividades(){
        tablaDefault.setRowCount(0);
            LazyList<Socioarancel> socaran = Socioarancel.where("id_socio = ?", socio.get("ID_DATOS_PERS"));
            Iterator<Socioarancel> iter = socaran.iterator();
            LinkedList<Arancel> tieneAran = new LinkedList();
            while(iter.hasNext()){
                Socioarancel arsoc = iter.next();
                Arancel ar = Arancel.first("id = ?", arsoc.get("id_arancel"));
                tieneAran.add(ar);
                System.out.println(ar.get("nombre")+ " gil");
            }
            Iterator<Arancel> itiene = tieneAran.iterator();
            while(itiene.hasNext()){
                Arancel arancel= itiene.next();
                String nombre = arancel.getString("nombre");
                BigDecimal precio= arancel.getBigDecimal("precio");
                Object row[] = new Object[3];
                row[0] = nombre;
                row[1] = precio;
                row[2] = true;
                tablaDefault.addRow(row);
            }
            
            /////////////////////////////////////////////////////
            
            ////////////////////////////////////////////////////
            LazyList<Arancel> listArancel = Arancel.findAll();
            LinkedList<Arancel> aranceles = new LinkedList();
            Iterator<Arancel> it = listArancel.iterator();
            while(it.hasNext()){
                Arancel a = it.next();
                aranceles.add(a);
            }
            aranceles.removeAll(tieneAran);
            Iterator<Arancel> itt = aranceles.iterator();
            while(itt.hasNext()){
                Arancel ar=itt.next();
                String nombre = ar.getString("nombre");
                 BigDecimal precio= ar.getBigDecimal("precio");
                 Object[] fil= new Object[3];
                fil[0] = nombre;
                fil[1] = precio;
                fil[2] = false;
                                tablaDefault.addRow(fil);
            }
    }
    
    
        /*va true si se quiere usar para mostrarla por pantalla es decir 12/12/2014 y false si va 
     para la base de datos, es decir 2014/12/12*/
    public String dateToMySQLDate(Date fecha, boolean paraMostrar) {
        if (paraMostrar) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(fecha);
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

        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaActividades = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        nombreCliente = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        cancelar = new javax.swing.JButton();
        realizar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        fechaVence = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Realizar cobro");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Actividades que realiza"));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        tablaActividades.setAutoCreateRowSorter(true);
        tablaActividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Actividad", "Precio", "Realiza"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.math.BigDecimal.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
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
        tablaActividades.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tablaActividadesPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(tablaActividades);
        if (tablaActividades.getColumnModel().getColumnCount() > 0) {
            tablaActividades.getColumnModel().getColumn(0).setPreferredWidth(200);
            tablaActividades.getColumnModel().getColumn(1).setPreferredWidth(30);
            tablaActividades.getColumnModel().getColumn(2).setResizable(false);
            tablaActividades.getColumnModel().getColumn(2).setPreferredWidth(1);
        }

        jPanel4.add(jScrollPane1);

        jLabel1.setText("Pago realizado por:");

        nombreCliente.setFont(new java.awt.Font("Cantarell", 1, 18)); // NOI18N
        nombreCliente.setText("Pepe");

        jLabel2.setText("TOTAL:");

        total.setFont(new java.awt.Font("Cantarell", 1, 18)); // NOI18N
        total.setForeground(new java.awt.Color(118, 209, 69));
        total.setText("X");

        cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/cross.png"))); // NOI18N
        cancelar.setText("Cancelar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        realizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/money.png"))); // NOI18N
        realizar.setText("Realizar cobro");
        realizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                realizarActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecha");

        jLabel4.setText("Vence");

        fechaVence.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(realizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelar))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fechaVence, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelar, realizar});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(fechaVence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelar)
                    .addComponent(realizar)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelar, realizar});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        int res=JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar el cobro?", null, JOptionPane.YES_NO_OPTION);
        if(res==JOptionPane.YES_OPTION){
            this.dispose();
        }
    }//GEN-LAST:event_cancelarActionPerformed

    private void realizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_realizarActionPerformed
        /*ACA VA TODA LA GILADA DEL PAGO, NO SE PUEDE AHCER EN UN CONTROLADOR*/
        BigDecimal totalB= new BigDecimal(0);
        int rows = tablaDefault.getRowCount();
                    LinkedList listaran = new LinkedList();
                    for(int i = 0; i< rows; i++){
                        if((boolean)tablaActividades.getValueAt(i, 2)==true){
                            Arancel a = Arancel.first("nombre = ?", tablaActividades.getValueAt(i, 0));
                            listaran.add(a);
                            BigDecimal precio=(BigDecimal)tablaActividades.getValueAt(i, 1);
                            System.out.println(precio);
                            totalB= totalB.add(BigDecimal.valueOf(precio.doubleValue()));
                        }
                    }
                    //Object o = clienteGui.getTablaActividades().getValueAt(1, 1).equals(true);
                    ABMSocios abmsocio= new ABMSocios();
                    Base.openTransaction();
                    Pago.createIt("ID_DATOS_PERS",socio.getString("ID_DATOS_PERS"),"FECHA",dateToMySQLDate(fecha.getDate(), false),"MONTO", totalB.setScale(2, RoundingMode.CEILING));
                    socio.setBoolean("ACTIVO",true).saveIt();
                    Base.commitTransaction();
                    if(abmsocio.modificar(socio, listaran)){
                        JOptionPane.showMessageDialog(this, "Socio dado de alta correctamente!");
                          
                         System.out.println("pago realizado!");
                         this.dispose();
                    }
    }//GEN-LAST:event_realizarActionPerformed

    private void tablaActividadesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tablaActividadesPropertyChange
                
       
    }//GEN-LAST:event_tablaActividadesPropertyChange

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelar;
    private com.toedter.calendar.JDateChooser fecha;
    private com.toedter.calendar.JDateChooser fechaVence;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombreCliente;
    private javax.swing.JButton realizar;
    private javax.swing.JTable tablaActividades;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
