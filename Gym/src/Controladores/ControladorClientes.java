/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMSocios;
import Interfaces.AbmClienteGui;
import Interfaces.BusquedaGui;
import Interfaces.DesktopPaneImage;
import Interfaces.PagosGui;
import Interfaces.TodasAsisGui;
import Modelos.Arancel;
import Modelos.Asistencia;
import Modelos.Pago;
import Modelos.Socio;
import Modelos.Socioarancel;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.edisoncor.gui.panel.PanelImage;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author nico
 */
public class ControladorClientes implements ActionListener {

    private BusquedaGui clientesGui;
    private JTable tablaClientes;
    private JList actividades;
    private AbmClienteGui altaClienteGui;
    private ControladorAbmCliente controladorAbmCliente;
    private PagosGui pagosGui;
    private TodasAsisGui asisGui;
    private ABMSocios abmSocios;
    private DefaultTableModel tablaSocDefault;
    private ActualizarDatos actualizarDAtos;
    private JDateChooser calenDesde;
    private JDateChooser calenHasta;
    private String dateDesde;
    private String dateHasta;
    private JDateChooser calenDesdeA;
    private JDateChooser calenHastaA;
    private String dateDesdeA;
    private String dateHastaA;
    private boolean verTodos = false;
    private boolean verTodas = false;
    private static JFrame foto;

    public ControladorClientes(BusquedaGui clientes, DesktopPaneImage desktop, ActualizarDatos actualizarDatos) {

        this.clientesGui = clientes;
        clientes.setActionListener(this);
        altaClienteGui = new AbmClienteGui();
        abmSocios = new ABMSocios();
        this.actualizarDAtos = actualizarDatos;
        controladorAbmCliente = new ControladorAbmCliente(altaClienteGui, actualizarDatos);
        desktop.add(altaClienteGui);
        pagosGui = new PagosGui();

        // pagosGui.getCActiv().addItem("TODOS");
        //  pagosGui.getCActiv().addActionListener(new ActionListener() { 
        //public void actionPerformed(ActionEvent e){
        // }}); 
        asisGui = new TodasAsisGui();
        asisGui.setActionListener(this);
        desktop.add(asisGui);
        desktop.add(pagosGui);
        pagosGui.setActionListener(this);
        tablaClientes = this.clientesGui.getTablaClientes();
        tablaSocDefault = this.clientesGui.getTablaClientesDefault();
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        clientesGui.getBusqueda().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                busquedaKeyReleased(evt);
            }
        });
        actividades = clientesGui.getActividades();
        actividades.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent lse) {
                if (lse.getValueIsAdjusting()) {
                } else {
                    List actividadesSelecc = actividades.getSelectedValuesList();
                    if (!actividadesSelecc.isEmpty()) {
                        boolean inactivo = false;
                        boolean morosos = false;
                        int i = 0;
                        while (i < actividadesSelecc.size()) {
                            if (actividadesSelecc.get(i) == "INACTIVOS") {
                                inactivo = true;
                            }
                            if (actividadesSelecc.get(i) == "MOROSOS") {
                                morosos = true;
                            }
                            i++;
                        }
                        cargarSociosActiv(actividadesSelecc, inactivo, morosos, false);
                    } else {
                        cargarSociosActiv(actividadesSelecc, false, false, true);
                    }

                }
            }
        });
        dateDesde = "0-0-0";
        dateHasta = "9999-0-0";
        calenDesde = pagosGui.getDesde();
        calenHasta = pagosGui.getHasta();
        ///////////1
        calenDesdeA = asisGui.getDesde();
        calenHastaA = asisGui.getHasta();
        //////////2
        calenHasta.setCalendar(Calendar.getInstance());
        calenDesde.setCalendar(Calendar.getInstance());
        calenDesde.setDate(new Date(110, 1, 1));
        /////////1
        calenHastaA.setCalendar(Calendar.getInstance());
        calenDesdeA.setCalendar(Calendar.getInstance());
        calenDesdeA.setDate(new Date(110, 1, 1));
        //////2
        calenDesde.getJCalendar().addPropertyChangeListener("calendar", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                calenDesdePropertyChange(evt);
            }
        });

        //////////////////1
        calenDesdeA.getJCalendar().addPropertyChangeListener("calendar", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                calenDesdeAPropertyChange(evt);
            }
        });
        /////////////////2
        calenDesde.getDateEditor().setEnabled(false);
        calenHasta.getDateEditor().setEnabled(false);
        /////////////1
        calenDesdeA.getDateEditor().setEnabled(false);
        calenHastaA.getDateEditor().setEnabled(false);
        /////////////2
        calenHasta.getJCalendar().addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                calenHastaPropertyChange(e);
            }
        });

        /////////1
        calenHastaA.getJCalendar().addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                calenHastaAPropertyChange(e);
            }
        });
        ////////////2

    }

    public void calenDesdePropertyChange(PropertyChangeEvent e) {
        dateHasta = dateToMySQLDate(pagosGui.getHasta().getCalendar().getTime(), false);
        dateDesde = dateToMySQLDate(pagosGui.getDesde().getCalendar().getTime(), false);
        Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
        String idCli = "nuul";
        if (s != null) {
            idCli = s.getString("ID_DATOS_PERS");
        }
        actualizarPagos(idCli, dateDesde, dateHasta, verTodos);
    }

    ////////////1
    public void calenDesdeAPropertyChange(PropertyChangeEvent e) {
        dateHastaA = dateToMySQLDate(asisGui.getHasta().getCalendar().getTime(), false);
        dateDesdeA = dateToMySQLDate(asisGui.getDesde().getCalendar().getTime(), false);
        Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
        String idCli = "nuul";
        if (s != null) {
            idCli = s.getString("ID_DATOS_PERS");
        }
        actualizarAsis(idCli, dateDesdeA, dateHastaA, verTodas);
    }
    ////////////////2

    public void calenHastaPropertyChange(PropertyChangeEvent e) {
        final Calendar c = (Calendar) e.getNewValue();
        dateHasta = dateToMySQLDate(pagosGui.getHasta().getCalendar().getTime(), false);
        dateDesde = dateToMySQLDate(pagosGui.getDesde().getCalendar().getTime(), false);
        Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
        String idCli = "nuul";
        if (s != null) {
            idCli = s.getString("ID_DATOS_PERS");
        }
        actualizarPagos(idCli, dateDesde, dateHasta, verTodos);
    }

    ///////////1
    public void calenHastaAPropertyChange(PropertyChangeEvent e) {
        final Calendar c = (Calendar) e.getNewValue();
        dateHastaA = dateToMySQLDate(asisGui.getHasta().getCalendar().getTime(), false);
        dateDesdeA = dateToMySQLDate(asisGui.getDesde().getCalendar().getTime(), false);
        Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
        String idCli = "nuul";
        if (s != null) {
            idCli = s.getString("ID_DATOS_PERS");
        }
        actualizarAsis(idCli, dateDesdeA, dateHastaA, verTodas);
    }
    ////////////2

    public void cargarSociosActiv(List lista, boolean inactivo, boolean morosos, boolean soloNombre) {
        tablaSocDefault.setRowCount(0);

        if (soloNombre) {
            LazyList<Socio> ListSocios = Socio.where("ACTIVO = ? and (NOMBRE like ? or APELLIDO like ? or DNI like ? )", 1, clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%");
            clientesGui.getTablaClientesDefault().setRowCount(0);
            Iterator<Socio> it = ListSocios.iterator();
            while (it.hasNext()) {
                Socio a = it.next();
                Object row[] = new Object[5];
                row[0] = a.getString("NOMBRE");
                row[1] = a.getString("APELLIDO");
                row[2] = a.getString("DNI");
                row[3] = a.getString("TEL");
                row[4] = a.getBoolean("ACTIVO");
                clientesGui.getTablaClientesDefault().addRow(row);
            }
            clientesGui.getLabelResult3().setText(Integer.toString(ListSocios.size()));
            /*LazyList lista3 = Arancel.where("ACTIVO = ?", 1);
             Iterator<Arancel> iter = lista3.iterator();
             String d[] = new String[100];
             int i = 1;
             while (iter.hasNext()) {
             Arancel a = iter.next();
             d[i] = a.getString("nombre");
             i++;
             }*/
            //clientesGui.getActividades().setListData(d);
            //ABMSocios abm = new ABMSocios();
        } else {
            if ((lista.size() == 2) && morosos && inactivo) {
                LazyList lSocios = Socio.where("ACTIVO = ? and (NOMBRE like ? or APELLIDO like ? or DNI like ? )", 0, clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%");
                Iterator<Socio> is = lSocios.iterator();
                while (is.hasNext()) {
                    Socio s = is.next();
                    LazyList asistencias = Asistencia.where("ID_DATOS_PERS = ?", s.getInteger("ID_DATOS_PERS")).orderBy("ID_ASISTENCIA desc");
                    if (!asistencias.isEmpty()) {
                        if (s.getDate("FECHA_PROX_PAGO").before(asistencias.get(0).getDate("FECHA"))) {
                            Object row[] = new Object[5];
                            row[0] = s.getString("NOMBRE");
                            row[1] = s.getString("APELLIDO");
                            row[2] = s.getString("DNI");
                            row[3] = s.getString("TEL");
                            row[4] = s.getBoolean("ACTIVO");
                            tablaSocDefault.addRow(row);
                        }
                    }

                }
            }
            if ((lista.size() == 1) && morosos) {
                LazyList lSocios = Socio.where("ACTIVO = ? and (NOMBRE like ? or APELLIDO like ? or DNI like ? )", 1, clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%");
                Iterator<Socio> is = lSocios.iterator();
                while (is.hasNext()) {
                    Socio s = is.next();
                    LazyList asistencias = Asistencia.where("ID_DATOS_PERS = ?", s.getInteger("ID_DATOS_PERS")).orderBy("ID_ASISTENCIA desc");
                    if (!asistencias.isEmpty()) {
                        if (s.getDate("FECHA_PROX_PAGO").before(asistencias.get(0).getDate("FECHA"))) {
                            Object row[] = new Object[5];
                            row[0] = s.getString("NOMBRE");
                            row[1] = s.getString("APELLIDO");
                            row[2] = s.getString("DNI");
                            row[3] = s.getString("TEL");
                            row[4] = s.getBoolean("ACTIVO");
                            tablaSocDefault.addRow(row);
                        }
                    }

                }
            }
            if ((lista.size() == 1) && inactivo) {
                LazyList socioslist = Socio.where("ACTIVO = ? and (NOMBRE like ? or APELLIDO like ? or DNI like ? )", 0, clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%");
                Iterator<Socio> itera = socioslist.iterator();
                while (itera.hasNext()) {
                    Socio soc = itera.next();
                    Object row[] = new Object[5];
                    row[0] = soc.getString("NOMBRE");
                    row[1] = soc.getString("APELLIDO");
                    row[2] = soc.getString("DNI");
                    row[3] = soc.getString("TEL");
                    row[4] = soc.getBoolean("ACTIVO");

                    tablaSocDefault.addRow(row);
                }
            }
            if ((!inactivo) && (!morosos)) {
                int i = 0;
                LinkedList<String> h = new LinkedList();
                while (i < lista.size()) {
                    Arancel a = Arancel.first("nombre = ?", lista.get(i));
                    int arancel_id = a.getInteger("id");
                    LazyList socioArancel = Socioarancel.where("id_arancel = ?", arancel_id);
                    clientesGui.getLabelResult3().setText(Integer.toString(socioArancel.size()));
                    Iterator<Socioarancel> it = socioArancel.iterator();
                    while (it.hasNext()) {
                        Socioarancel sa = it.next();
                        Socio so = Socio.first("ID_DATOS_PERS = ? and (NOMBRE like ? or APELLIDO like ? or DNI like ? )", sa.getInteger("id_socio"), clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%");
                        // codigo agregado el 09-4-2014
                        if (so != null) {
                            if (so.getInteger("ACTIVO") == 1) {
                                if (!h.contains(so.getString("ID_DATOS_PERS"))) {
                                    h.add(so.getString("ID_DATOS_PERS"));
                                }
                            }
                        }

                    }
                    i++;
                }
                Iterator<String> aux = h.iterator();
                String cli;
                while (aux.hasNext()) {
                    cli = aux.next();
                    Socio so = Socio.first("ID_DATOS_PERS = ?", cli);
                    Object row[] = new Object[5];
                    row[0] = so.getString("NOMBRE");
                    row[1] = so.getString("APELLIDO");
                    row[2] = so.getString("DNI");
                    row[3] = so.getString("TEL");
                    row[4] = so.getBoolean("ACTIVO");

                    tablaSocDefault.addRow(row);
                }
            }
            if ((inactivo) && (lista.size() > 1) && (!morosos)) {
                int i = 0;
                LinkedList<String> h = new LinkedList();
                while (i < lista.size()) {
                    if (lista.get(i) != "INACTIVOS") {
                        Arancel a = Arancel.first("nombre = ?", lista.get(i));
                        LazyList socioArancel = Socioarancel.where("id_arancel = ?", a.getInteger("id"));
                        clientesGui.getLabelResult3().setText(Integer.toString(socioArancel.size()));
                        Iterator<Socioarancel> it = socioArancel.iterator();
                        while (it.hasNext()) {
                            Socioarancel sa = it.next();
                            Socio so = Socio.first("ID_DATOS_PERS = ? and (NOMBRE like ? or APELLIDO like ? or DNI like ? )", sa.getInteger("id_socio"), clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%", clientesGui.getBusqueda().getText() + "%");
                            if (so != null) {
                                if (so.getInteger("ACTIVO") == 0) {
                                    if (!h.contains(so.getString("ID_DATOS_PERS"))) {
                                        h.add(so.getString("ID_DATOS_PERS"));
                                    }
                                }
                            }

                        }
                    }
                    i++;

                }
                Iterator<String> aux = h.iterator();
                String cli;
                while (aux.hasNext()) {
                    cli = aux.next();
                    Socio so = Socio.first("ID_DATOS_PERS = ?", cli);
                    Object row[] = new Object[5];
                    row[0] = so.getString("NOMBRE");
                    row[1] = so.getString("APELLIDO");
                    row[2] = so.getString("DNI");
                    row[3] = so.getString("TEL");
                    row[4] = so.getBoolean("ACTIVO");

                    tablaSocDefault.addRow(row);
                }
            }
        }
        clientesGui.getLabelResult3().setText(Integer.toString(clientesGui.getTablaClientes().getRowCount()));

    }

    public void cargarSocios(String filtro) {

        List actividadesSelecc = actividades.getSelectedValuesList();
        if (!actividadesSelecc.isEmpty()) {
            boolean inactivo = false;
            boolean morosos = false;
            int i = 0;
            while (i < actividadesSelecc.size()) {
                if (actividadesSelecc.get(i) == "INACTIVOS") {
                    inactivo = true;
                }
                if (actividadesSelecc.get(i) == "MOROSOS") {
                    morosos = true;
                }
                i++;
            }
            cargarSociosActiv(actividadesSelecc, inactivo, morosos, false);
        }

        /*LazyList<Socio> ListSocios= Socio.where("NOMBRE like ? or APELLIDO like ? or DNI like ? ", filtro + "%",filtro + "%",filtro + "%");
         tablaSocDefault.setRowCount(0);
         Iterator<Socio> it = ListSocios.iterator();
         while(it.hasNext()){
         Socio a = it.next();
         String row[] = new String[4];
         row[0] = a.getString("NOMBRE");
         row[1] = a.getString("APELLIDO");
         row[2] = a.getString("DNI");
         row[3] = a.getString("TEL");
         tablaSocDefault.addRow(row);
         }
         clientesGui.getLabelResult3().setText(Integer.toString(ListSocios.size()));*/
    }

    public void busquedaKeyReleased(java.awt.event.KeyEvent evt) {
        List actividadesSelecc = actividades.getSelectedValuesList();
        if (!actividadesSelecc.isEmpty()) {
            boolean inactivo = false;
            boolean morosos = false;
            int i = 0;
            while (i < actividadesSelecc.size()) {
                if (actividadesSelecc.get(i) == "INACTIVOS") {
                    inactivo = true;
                }
                if (actividadesSelecc.get(i) == "MOROSOS") {
                    morosos = true;
                }
                i++;
            }
            cargarSociosActiv(actividadesSelecc, inactivo, morosos, false);
        } else {
            cargarSociosActiv(actividadesSelecc, false, false, true);
        }

        clientesGui.getLabelResult3().setText(Integer.toString(clientesGui.getTablaClientes().getRowCount()));

    }

    public void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        clientesGui.getBotEliminarSocio().setEnabled(true);
        clientesGui.getBotRegistrosPago().setEnabled(true);
        if (evt.getClickCount() == 2) {
            altaClienteGui.setBotonesNuevo(false);
            altaClienteGui.bloquearCampos(true);
            altaClienteGui.limpiarCampos();
            int row = tablaClientes.getSelectedRow();
            Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(row, 2));
            altaClienteGui.setPicture(s.getInteger("ID_DATOS_PERS"));
            altaClienteGui.setTitle(s.getString("APELLIDO") + " " + s.getString("NOMBRE"));
            altaClienteGui.setVisible(true);
            try {
                altaClienteGui.setMaximum(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            altaClienteGui.toFront();
            controladorAbmCliente.setSocio(s);
            altaClienteGui.getNombre().setText(s.getString("NOMBRE"));
            altaClienteGui.getApellido().setText(s.getString("APELLIDO"));
            altaClienteGui.getTelefono().setText(s.getString("TEL"));
            altaClienteGui.getDni().setText(s.getString("DNI"));
            altaClienteGui.getDireccion().setText(s.getString("DIR"));
            if (s.getString("SEXO").equals("M")) {
                altaClienteGui.getSexo().setSelectedIndex(1);
            } else {
                altaClienteGui.getSexo().setSelectedIndex(0);
            }
            altaClienteGui.getFechaNacimJDate().setDate(s.getDate("FECHA_NAC"));;
            altaClienteGui.getLabelFechaIngreso().setText(dateToMySQLDate(s.getDate("FECHA_ING"), true));
            altaClienteGui.getLabelFechaVenci().setText(dateToMySQLDate(s.getDate("FECHA_PROX_PAGO"), true));
            altaClienteGui.getTablaActivDefault().setRowCount(0);
            LazyList<Socioarancel> ListSocAran = Socioarancel.where("id_socio = ?", s.get("ID_DATOS_PERS"));
            Iterator<Socioarancel> ite = ListSocAran.iterator();
            while (ite.hasNext()) {
                Socioarancel sa = ite.next();
                Arancel a = Arancel.first("id = ?", sa.get("id_arancel"));
                Object row1[] = new Object[2];
                row1[0] = a.getString("nombre");
                row1[1] = true;
                altaClienteGui.getTablaActivDefault().addRow(row1);
                /*Se debe abrir la ventana de clientes para permitir el alta de giles*/
            }

        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            Point p = evt.getPoint();
            int rowNumber = tablaClientes.rowAtPoint(p);
            ListSelectionModel modelo = tablaClientes.getSelectionModel();
            modelo.setSelectionInterval(rowNumber, rowNumber);
            if(foto!=null){
                foto.dispose();
            }
            foto = new JFrame("VER  IMAGEN DE SOCIO "+(String)tablaClientes.getValueAt(rowNumber, 0)+" "+(String)tablaClientes.getValueAt(rowNumber, 1));
            foto.setResizable(true);
            foto.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            foto.setLayout(new BorderLayout());
            PanelImage panel = new PanelImage();
            String rutaImagen = System.getProperty("user.dir");
            rutaImagen += "/user_images/" +Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2)).getString("ID_DATOS_PERS") + ".jpg";
            File file= new File(rutaImagen);
            if(file.exists()){
                panel.setIcon(new javax.swing.ImageIcon(rutaImagen));
            }
            else{
                rutaImagen = System.getProperty("user.dir");
            rutaImagen += "/user_images/sin_imagen_disponible.jpg";
                panel.setIcon(new javax.swing.ImageIcon(rutaImagen));
            }
            panel.repaint();
            
            foto.add(panel);
            foto.setSize(panel.getIcon().getIconWidth(), panel.getIcon().getIconHeight());
            foto.setLocationRelativeTo(null);
            foto.setVisible(true);
            foto.toFront();
        }
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clientesGui.getBotAltaSocio()) {
            altaClienteGui.setBotonesNuevo(true);
            altaClienteGui.bloquearCampos(false);
            altaClienteGui.limpiarCampos();
            controladorAbmCliente.setIsNuevo(true);
            altaClienteGui.setTitle("Alta de socio");
            altaClienteGui.setVisible(true);
            try {
                altaClienteGui.setMaximum(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(ControladorClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            altaClienteGui.toFront();
            altaClienteGui.getTablaActivDefault().setRowCount(0);
            LazyList<Arancel> ListAran = Arancel.findAll();
            Iterator<Arancel> ite = ListAran.iterator();
            while (ite.hasNext()) {
                Arancel a = ite.next();
                Object row[] = new Object[2];
                row[0] = a.getString("nombre");
                altaClienteGui.getTablaActivDefault().addRow(row);
                /*Se debe abrir la ventana de clientes para permitir el alta de giles*/
            }
        }
        if (ae.getSource() == clientesGui.getBotEliminarSocio()) {
            if (tablaClientes.getSelectedRow() >= 0) {
                /*elimino el socio SELECCIONADO, pregunto y fue*/
                int ret = JOptionPane.showConfirmDialog(clientesGui, "¿Desea eliminar a " + tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0) + " " + tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1) + " ?", null, JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
                    if (abmSocios.baja(s)) {
                        JOptionPane.showMessageDialog(clientesGui, "Socio dado de baja exitosamente!");
                        LazyList<Socio> ListSocios = Socio.where("ACTIVO = ?", 1);
                        clientesGui.getTablaClientesDefault().setRowCount(0);
                        Iterator<Socio> it = ListSocios.iterator();
                        while (it.hasNext()) {
                            Socio a = it.next();
                            Object row[] = new Object[5];
                            row[0] = a.getString("NOMBRE");
                            row[1] = a.getString("APELLIDO");
                            row[2] = a.getString("DNI");
                            row[3] = a.getString("TEL");
                            row[4] = a.getBoolean("ACTIVO");

                            clientesGui.getTablaClientesDefault().addRow(row);
                        }
                    } else {
                        JOptionPane.showMessageDialog(clientesGui, "Ocurrio un error inesperado");
                    }
                }
            }
        }
        if (ae.getSource() == clientesGui.getBotRegistrosPago()) {
            verTodos = false;
            if (tablaClientes.getSelectedRow() >= 0) {
                Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
                LazyList ListPagos = Pago.where("ID_DATOS_PERS = ?", s.getString("ID_DATOS_PERS"));
                pagosGui.getTablaPagosDefault().setRowCount(0);
                Iterator<Pago> it = ListPagos.iterator();

                while (it.hasNext()) {
                    Pago p = it.next();
                    Object row[] = new Object[6];
                    row[0] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0);
                    row[1] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1);
                    row[2] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2);
                    row[3] = dateToMySQLDate(p.getDate("FECHA"), true);
                    row[4] = p.getFloat("MONTO");
                    row[5] = p.getInteger("ID_PAGOS");
                    pagosGui.getTablaPagosDefault().addRow(row);
                }
                pagosGui.setVisible(true);

                pagosGui.toFront();
            }

        }
        if (ae.getSource() == clientesGui.getAsistencias()) {
            verTodas = false;
            if (tablaClientes.getSelectedRow() >= 0) {
                Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
                LazyList ListAsis = Asistencia.where("ID_DATOS_PERS = ?", s.getString("ID_DATOS_PERS"));
                ListAsis.orderBy("ID_ASISTENCIA");
                asisGui.getTablaAsisDefault().setRowCount(0);
                Iterator<Asistencia> it = ListAsis.iterator();

                while (it.hasNext()) {
                    Asistencia p = it.next();
                    Object row[] = new Object[7];
                    row[0] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0);
                    row[1] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1);
                    row[2] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2);
                    row[3] = dateToMySQLDate(p.getDate("FECHA"), true);
                    //row[4] = p.getFloat("MONTO");
                    Arancel ar = Arancel.findFirst("id = ?", p.get("ID_ACTIV"));
                    String nombreActiv = ar.getString("nombre");
                    String nombreActivCombo = "";
                    if (p.get("ID_ACTIV_COMBO") != null) {
                        ar = Arancel.findFirst("id = ?", p.get("ID_ACTIV_COMBO"));
                        nombreActivCombo = ar.getString("nombre");
                    }
                    row[4] = nombreActiv;
                    row[5] = nombreActivCombo;
                    row[6] = p.getInteger("ID_ASISTENCIA");
                    asisGui.getTablaAsisDefault().addRow(row);
                }
                asisGui.setVisible(true);
                asisGui.toFront();
            }

        }
        if (ae.getSource() == asisGui.getBotBorrarAsis()) {
            int ret = JOptionPane.showConfirmDialog(asisGui, "¿Desea eliminar la asistencia seleccionada?", null, JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_OPTION) {

                Asistencia.delete("ID_ASISTENCIA = ?", asisGui.getTablaAsis().getValueAt(asisGui.getTablaAsis().getSelectedRow(), 6));
                dateHasta = dateToMySQLDate(asisGui.getHasta().getCalendar().getTime(), false);
                dateDesde = dateToMySQLDate(asisGui.getDesde().getCalendar().getTime(), false);
                actualizarAsis(dateDesde, dateDesde, dateHasta, verTodas);
            }
        }
        if (ae.getSource() == asisGui.getBotVerTodos()) {
            verTodas = true;
            dateHastaA = dateToMySQLDate(asisGui.getHasta().getCalendar().getTime(), false);
            dateDesdeA = dateToMySQLDate(asisGui.getDesde().getCalendar().getTime(), false);
            actualizarAsis("no me importa", dateDesdeA, dateHastaA, verTodas);

        }
        if (ae.getSource() == pagosGui.getBotBorrarPago()) {
            int ret = JOptionPane.showConfirmDialog(pagosGui, "¿Desea eliminar el pago seleccionado?", null, JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_OPTION) {

                Pago.delete("ID_PAGOS = ?", pagosGui.getTablaPagos().getValueAt(pagosGui.getTablaPagos().getSelectedRow(), 5));
                dateHasta = dateToMySQLDate(pagosGui.getHasta().getCalendar().getTime(), false);
                dateDesde = dateToMySQLDate(pagosGui.getDesde().getCalendar().getTime(), false);
                actualizarPagos(dateDesde, dateDesde, dateHasta, verTodos);
            }
        }
        if (ae.getSource() == pagosGui.getBotVerTodos()) {
            verTodos = true;
            dateHasta = dateToMySQLDate(pagosGui.getHasta().getCalendar().getTime(), false);
            dateDesde = dateToMySQLDate(pagosGui.getDesde().getCalendar().getTime(), false);

            actualizarPagos("no me importa", dateDesde, dateHasta, verTodos);

        }

    }

    /*public void cargarSocios(){
     LazyList<Socio> ListSocios= Socio.where("ACTIVO = ?", 1);
     clientesGui.getTablaClientesDefault().setRowCount(0);
     Iterator<Socio> it = ListSocios.iterator();
     while(it.hasNext()){
     Socio a = it.next();
     String row[] = new String[4];
     row[0] = a.getString("NOMBRE");
     row[1] = a.getString("APELLIDO");
     row[2] = a.getString("DNI");
     row[3] = a.getString("TEL");
     clientesGui.getTablaClientesDefault().addRow(row);
     }
     clientesGui.getLabelResult3().setText(Integer.toString(ListSocios.size()));
     LazyList lista = Arancel.where("ACTIVO = ?", 1);
     Iterator<Arancel> iter = lista.iterator();
     String d[] = new String[100];
     int i = 1;
     while(iter.hasNext()){
     Arancel a = iter.next();
     d[i] = a.getString("nombre");
     i++;
     }
     clientesGui.getActividades().setListData(d);
     //ABMSocios abm = new ABMSocios();
     }*/
    /*va true si se quiere usar para mostrarla por pantalla es decir 12/12/2014 y false si va 
     para la base de datos, es decir 2014/12/12*/
    public String dateToMySQLDate(Date fecha, boolean paraMostrar) {
        if (fecha != null) {
            if (paraMostrar) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(fecha);
            } else {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(fecha);
            }
        } else {
            return "";
        }

    }

    private void actualizarPagos(String idcliente, String desde, String hasta, boolean todos) {
        LazyList<Pago> ListPagos;
        if (!todos) {
            ListPagos = Pago.where("ID_DATOS_PERS = ? and FECHA between ? and ?", idcliente, desde, hasta);
        } else {
            ListPagos = Pago.where("FECHA between ? and ?", desde, hasta);
        }
        pagosGui.getTablaPagosDefault().setRowCount(0);
        if (ListPagos != null) {
            Iterator<Pago> it = ListPagos.iterator();
            Socio s;
            while (it.hasNext()) {
                Pago p = it.next();
                s = Socio.findFirst("ID_DATOS_PERS =?", p.getString("ID_DATOS_PERS"));
                Object row[] = new Object[6];
                row[0] = s.getString("NOMBRE");
                row[1] = s.getString("APELLIDO");
                row[2] = s.getString("DNI");
                row[3] = dateToMySQLDate(p.getDate("FECHA"), true);
                row[4] = p.getFloat("MONTO");
                row[5] = p.getInteger("ID_PAGOS");
                pagosGui.getTablaPagosDefault().addRow(row);
            }
        }
    }

    private void actualizarAsis(String idcliente, String desde, String hasta, boolean todos) {
        LazyList<Asistencia> ListAsis;
        if (!todos) {
            ListAsis = Asistencia.where("ID_DATOS_PERS = ? and FECHA between ? and ?", idcliente, desde, hasta);
        } else {
            ListAsis = Asistencia.where("FECHA between ? and ?", desde, hasta);
        }
        asisGui.getTablaAsisDefault().setRowCount(0);
        if (ListAsis != null) {
            Iterator<Asistencia> it = ListAsis.iterator();
            Socio s;
            while (it.hasNext()) {
                Asistencia p = it.next();
                s = Socio.findFirst("ID_DATOS_PERS =?", p.getString("ID_DATOS_PERS"));
                Object row[] = new Object[7];
                row[0] = s.getString("NOMBRE");
                row[1] = s.getString("APELLIDO");
                row[2] = s.getString("DNI");
                row[3] = dateToMySQLDate(p.getDate("FECHA"), true);
                // row[4] = p.getFloat("MONTO");
                Arancel ar = Arancel.findFirst("id = ?", p.get("ID_ACTIV"));
                if (ar != null) {
                    String nombreActiv = ar.getString("nombre");
                    String nombreActivCombo = "";
                    if (p.get("ID_ACTIV_COMBO") != null) {
                        ar = Arancel.findFirst("id = ?", p.get("ID_ACTIV_COMBO"));
                        nombreActivCombo = ar.getString("nombre");
                    }
                    row[4] = nombreActiv;
                    row[5] = nombreActivCombo;
                }
                row[6] = p.getInteger("ID_ASISTENCIA");
                asisGui.getTablaAsisDefault().addRow(row);
            }
        }
    }
}
