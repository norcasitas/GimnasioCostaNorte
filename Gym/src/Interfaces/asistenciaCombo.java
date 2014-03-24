/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Modelos.Arancel;
import Modelos.Combo;
import Modelos.Socioarancel;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import Modelos.Asistencia;

/**
 *
 * @author Nico
 */
public class asistenciaCombo extends javax.swing.JDialog {

    /**
     * Creates new form asistenciaCombo
     */
    private ButtonGroup actividades = new ButtonGroup();
    public int idActiv = -1;
    public int idActivCombo = -1;

    public asistenciaCombo(java.awt.Frame parent, boolean modal, LazyList<Socioarancel> socioArancel, Object idCliente) {
        super(parent, modal);
        initComponents();
        Iterator<Socioarancel> it = socioArancel.iterator();
        Calendar calendario= Calendar.getInstance();
        calendario.set(2014, 2, 21);
        System.out.println(calendario.get(calendario.DAY_OF_WEEK));
        System.out.println(calendario.getTime());
        calendario.add(Calendar.DATE,-calendario.get(Calendar.DAY_OF_WEEK)+1   );
        System.out.println(calendario.getTime());

        boolean superoAsistencias=false;
        Arancel aran = null;
        while (it.hasNext()) {
            Socioarancel socAr = it.next();
            aran = Arancel.findFirst("id = ?", socAr.get("id_arancel"));

            if (aran != null) {
                if (aran.getString("categoria").equals("COMBO")) {
                    JScrollPane scroll = new javax.swing.JScrollPane();
                    jPanel1.add(scroll);
                    JPanel panelComboManual = new JPanel(new java.awt.GridLayout(0, 1));
                    scroll.add(panelComboManual);
                    scroll.setViewportView(panelComboManual);
                    int idCombo = aran.getInteger("id");
                    panelComboManual.setBorder(javax.swing.BorderFactory.createTitledBorder("Actividades del combo: " + aran.getString("nombre")));
                    LazyList<Combo> comboAct = Combo.where("id_combo = ?", aran.get("id"));
                    Iterator<Combo> itCombo = comboAct.iterator();
                    while (itCombo.hasNext()) {
                        Combo combo = itCombo.next();
                        System.out.println(idCliente+" "+ dateToMySQLDate(calendario.getTime(), false)+" "+aran.get("id")+" "+combo.get("id_activ"));
                        LazyList<Asistencia> asistencia = Asistencia.where("ID_DATOS_PERS = ? and FECHA> ? and ID_ACTIV =? and ID_ACTIV_COMBO =?", idCliente, dateToMySQLDate(calendario.getTime(), false),aran.get("id"),combo.get("id_activ"));
                        Iterator<Asistencia> itAsistencia= asistencia.iterator();
                        //boolean noEsDomingo=true;
                        int contador=0;
                        while(itAsistencia.hasNext()){
                        Asistencia asis= itAsistencia.next();
                        Date fechaAsis=  asis.getDate("fecha");

                            contador++;
                        
                    }
                    if(contador<combo.getInteger("dias")){
                        System.out.println("puede ir!");
                        Arancel arancel = Arancel.findFirst("id = ?", combo.get("id_activ"));
                        JRadioButton boton1 = new JRadioButton(arancel.getString("nombre"));
                        boton1.setActionCommand(idCombo + "-" + arancel.getInteger("id"));
                        panelComboManual.add(boton1);
                        actividades.add(boton1);
                        System.out.println("actividades del combo");
                    }
                    }
                } else {
                    LazyList<Asistencia> asistencia = Asistencia.where("ID_DATOS_PERS = ? and FECHA> ? and ID_ACTIV =?", idCliente, dateToMySQLDate(calendario.getTime(), false),aran.get("id"));
                    Iterator<Asistencia> itAsistencia= asistencia.iterator();
                    //boolean noEsDomingo=true;
                    int contador=0;
                    while(itAsistencia.hasNext()){
                        Asistencia asis= itAsistencia.next();
                        Date fechaAsis=  asis.getDate("fecha");

                            contador++;
                        
                    }
                    if(contador<aran.getInteger("dias")){
                        System.out.println("puede ir!");
                        int idActiv = aran.getInteger("id");
                        JRadioButton boton = new JRadioButton(aran.getString("nombre"));
                        boton.setActionCommand(idActiv + "-");
                        panelActiv.add(boton);
                        actividades.add(boton);
                        System.out.println("actividades sin combo");
                    }
                    
                }
            }
        }
    }

    public ButtonGroup getActividades() {
        return actividades;
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

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelActiv = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        panelActiv.setBorder(javax.swing.BorderFactory.createTitledBorder("Actividades individuales"));
        panelActiv.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane1.setViewportView(panelActiv);

        jPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (actividades.getSelection() != null) {
            String[] split = actividades.getSelection().getActionCommand().split("-");
            idActiv = Integer.valueOf(split[0]);
            if (split.length > 1) {
                idActivCombo = Integer.valueOf(split[1]);
                System.out.println("id activ: " + idActiv + "id combo: " + idActivCombo);
            }
            this.setVisible(false);
        }


    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelActiv;
    // End of variables declaration//GEN-END:variables
}
