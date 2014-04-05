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
import Modelos.Arancel;
import Modelos.Pago;
import Modelos.Socio;
import Modelos.Socioarancel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
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
    private ABMSocios abmSocios;
    private DefaultTableModel tablaSocDefault;
    private ActualizarDatos actualizarDAtos;
    
    
    public ControladorClientes(BusquedaGui clientes, DesktopPaneImage desktop, ActualizarDatos actualizarDatos) {
        
        this.clientesGui = clientes;
        clientes.setActionListener(this);
        altaClienteGui = new AbmClienteGui();
        abmSocios = new ABMSocios();
        this.actualizarDAtos=actualizarDatos;
        controladorAbmCliente = new ControladorAbmCliente(altaClienteGui,actualizarDatos );
        desktop.add(altaClienteGui);
        pagosGui= new PagosGui();
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
                    System.out.println("opcion siendo seleccionada,no hago nada");
                } else {
                    System.out.println("opcion dejo de ser seleccionada ");
                    List actividadesSelecc = actividades.getSelectedValuesList();
                    System.out.println(actividadesSelecc.toString());
                    /*aca iria la incovación de la función para cuando se está buscando que se terminaron de
                     elegir las actividades*/
                }
            }
        });

    }
    public void cargarSocios(String filtro){
        LazyList<Socio> ListSocios= Socio.where("NOMBRE like ? or APELLIDO like ? or DNI like ? ", filtro + "%",filtro + "%",filtro + "%");;
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
             clientesGui.getLabelResult3().setText(Integer.toString(ListSocios.size()));
           /*  LazyList lista = Arancel.where("ACTIVO = ?", 1);
             Iterator<Arancel> iter = lista.iterator();
             String d[] = new String[100];
             int i = 1;
             while(iter.hasNext()){
                 Arancel a = iter.next();
                 d[i] = a.getString("nombre");
                 i++;
             }*/
             
    }
    
    public void busquedaKeyReleased(java.awt.event.KeyEvent evt) {
        System.out.println("apreté el caracter" + evt.getKeyChar());
        cargarSocios(clientesGui.getBusqueda().getText());
        /*Aca va la gilada para la busqueda mientras escribis se puede hacer 
         función y que se invoque a esa así tambien se llama cuando se 
         seleccionan actividades*/
    }
    
    

    public void tablaMouseClicked(java.awt.event.MouseEvent evt) {
        clientesGui.getBotEliminarSocio().setEnabled(true);
        clientesGui.getBotRegistrosPago().setEnabled(true);
        if (evt.getClickCount() == 2) {
            System.out.println("Hice doble click sobre un socio");
            altaClienteGui.setBotonesNuevo(false);
            altaClienteGui.bloquearCampos(true);
            altaClienteGui.limpiarCampos();
             int row = tablaClientes.getSelectedRow();
            Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(row, 2));
            altaClienteGui.setTitle(s.getString("APELLIDO")+" "+ s.getString("NOMBRE"));
            altaClienteGui.setVisible(true);
            altaClienteGui.toFront();
            controladorAbmCliente.setSocio(s);
            altaClienteGui.getNombre().setText(s.getString("NOMBRE"));
            altaClienteGui.getApellido().setText(s.getString("APELLIDO"));
            altaClienteGui.getTelefono().setText(s.getString("TEL"));
            altaClienteGui.getDni().setText(s.getString("DNI"));
            altaClienteGui.getDireccion().setText(s.getString("DIR"));
            if(s.getString("SEXO").equals("M")){
                altaClienteGui.getSexo().setSelectedIndex(1);
            }else{
                altaClienteGui.getSexo().setSelectedIndex(0);
            }
            altaClienteGui.getFechaNacimJDate().setDate(s.getDate("FECHA_NAC")); ;
            altaClienteGui.getLabelFechaIngreso().setText(dateToMySQLDate(s.getDate("FECHA_ING"),true));
            altaClienteGui.getLabelFechaVenci().setText(dateToMySQLDate(s.getDate("FECHA_PROX_PAGO"), true)); 
             altaClienteGui.getTablaActivDefault().setRowCount(0);
            LazyList<Socioarancel> ListSocAran = Socioarancel.where("id_socio = ?", s.get("ID_DATOS_PERS"));
            Iterator<Socioarancel> ite = ListSocAran.iterator();
                while(ite.hasNext()){
                    Socioarancel sa = ite.next();
                    Arancel a = Arancel.first("id = ?", sa.get("id_arancel"));
                    Object row1[] = new Object[2];
                    row1[0] = a.getString("nombre");
                    row1[1] = true;
                    altaClienteGui.getTablaActivDefault().addRow(row1);
            /*Se debe abrir la ventana de clientes para permitir el alta de giles*/
                }
            
        }

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clientesGui.getBotAltaSocio()) {
            System.out.println("Alta socio pulsado");
            altaClienteGui.setBotonesNuevo(true);
            altaClienteGui.bloquearCampos(false);
            altaClienteGui.limpiarCampos();
            controladorAbmCliente.setIsNuevo(true);
            altaClienteGui.setTitle("Alta de socio");
            altaClienteGui.setVisible(true);
            altaClienteGui.toFront();
            altaClienteGui.getTablaActivDefault().setRowCount(0);
            LazyList<Arancel> ListAran = Arancel.findAll();
            Iterator<Arancel> ite = ListAran.iterator();
                while(ite.hasNext()){
                    Arancel a = ite.next();
                    Object row[] = new Object[2];
                    row[0] = a.getString("nombre");
                    altaClienteGui.getTablaActivDefault().addRow(row);
            /*Se debe abrir la ventana de clientes para permitir el alta de giles*/
                }
        }
        if (ae.getSource() == clientesGui.getBotEliminarSocio()) {
            System.out.println("eliminar socio");
            if(tablaClientes.getSelectedRow()>=0){
                /*elimino el socio SELECCIONADO, pregunto y fue*/
                int ret=JOptionPane.showConfirmDialog(clientesGui, "¿Desea eliminar a "+tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0)+" "+tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1)+" ?",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                    System.out.println("elimino el de la fila "+tablaClientes.getSelectedRow());
                    Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
                    if(abmSocios.baja(s)){
                        JOptionPane.showMessageDialog(clientesGui, "Socio dado de baja exitosamente!");
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
                    }else{
                        JOptionPane.showMessageDialog(clientesGui, "Ocurrio un error inesperado");
                    }
                }
            }
        }
        if (ae.getSource() == clientesGui.getBotRegistrosPago()) {
            System.out.println("ver registros de pago pulsado");
            if(tablaClientes.getSelectedRow()>=0){
                Socio s = Socio.first("DNI = ?", tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2));
                LazyList ListPagos = Pago.where("ID_DATOS_PERS = ?", s.getString("ID_DATOS_PERS"));
                pagosGui.getTablaPagosDefault().setRowCount(0);
                Iterator<Pago> it = ListPagos.iterator();
                
                while(it.hasNext()){
                    Pago p = it.next();
                    Object row[] = new Object[6];
                    row[0] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 0);
                    row[1] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 1);
                    row[2] = tablaClientes.getValueAt(tablaClientes.getSelectedRow(), 2);
                    row[3] = dateToMySQLDate(p.getDate("FECHA"),true);
                    row[4] = p.getFloat("MONTO");
                    pagosGui.getTablaPagosDefault().addRow(row);
                }
                pagosGui.setVisible(true);
                pagosGui.toFront();
                System.out.println("pagos del gil de la fila"+tablaClientes.getSelectedRow());
            }

        }
        if(ae.getSource()==pagosGui.getBotBorrarPago()){
             int ret=JOptionPane.showConfirmDialog(pagosGui, "¿Desea eliminar el pago seleccionado?",null,JOptionPane.YES_NO_OPTION);
                if(ret== JOptionPane.YES_OPTION){
                    System.out.println("elimino el de la fila "+tablaClientes.getSelectedRow());

                }
        }
        if(ae.getSource()==pagosGui.getBotVerTodos()){
            System.out.println("quiero ver todos los pago guacho");
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
        if(fecha!=null){
        if(paraMostrar){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
        }
        else{
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
        }
        }
        else{
            return "";
        }
    }
}