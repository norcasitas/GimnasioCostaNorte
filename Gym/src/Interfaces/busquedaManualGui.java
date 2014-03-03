/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

import ABMs.ABMSocios;
import Controladores.ControladorIngreso;
import Modelos.Arancel;
import Modelos.Socio;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author Nico
 */
public class busquedaManualGui extends javax.swing.JDialog {
    private DefaultTableModel tablaClientesDefault;
    private ControladorIngreso contr;

    /**
     * Creates new form busquedaManualGui
     */
    public busquedaManualGui(java.awt.Frame parent, boolean modal, ControladorIngreso contr) {
        super(parent, modal);
        initComponents();
        this.contr= contr;
        tablaClientesDefault = (DefaultTableModel) tablaClientes.getModel();//conveirto la tabla
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getClickCount()==2){
                    cargarDatos();
                }
            }
        });
        busqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaKeyReleased(evt);
            }
        });
    }
    public void cargarSocios(){
        LazyList<Socio> ListSocios= Socio.findAll();
            tablaClientesDefault.setRowCount(0);
             Iterator<Socio> it = ListSocios.iterator();
             while(it.hasNext()){
                Socio a = it.next();
                String row[] = new String[4];
                row[0] = a.getString("NOMBRE");
                row[1] = a.getString("APELLIDO");
                row[2] = a.getString("DNI");
                row[3] = a.getString("TEL");
                tablaClientesDefault.addRow(row);
             }
             LabelResult3.setText(Integer.toString(ListSocios.size()));
             LazyList lista = Arancel.where("ACTIVO = ?", 1);
             Iterator<Arancel> iter = lista.iterator();
             String d[] = new String[100];
             int i = 1;
             while(iter.hasNext()){
                 Arancel a = iter.next();
                 d[i] = a.getString("nombre");
                 i++;
             }
             ABMSocios abm = new ABMSocios();
    }
    private void cargarDatos(){
                            int row = tablaClientes.getSelectedRow();
                Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(row, 2));
                this.contr.cargarDatos(s);
    }
        public void busquedaKeyReleased(java.awt.event.KeyEvent evt) {
        System.out.println("apreté el caracter" + evt.getKeyChar());
        /*Aca va la gilada para la busqueda mientras escribis se puede hacer 
         función y que se invoque a esa así tambien se llama cuando se 
         seleccionan actividades*/
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        LabelResult3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        busqueda = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Clientes"));

        jLabel14.setText("Registros encontrados: ");

        LabelResult3.setText("x");

        tablaClientes.setAutoCreateRowSorter(true);
        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "Apellido", "DNI", "Telefono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(tablaClientes);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelResult3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(230, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(LabelResult3))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(busqueda)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelResult3;
    private javax.swing.JTextField busqueda;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tablaClientes;
    // End of variables declaration//GEN-END:variables
}
