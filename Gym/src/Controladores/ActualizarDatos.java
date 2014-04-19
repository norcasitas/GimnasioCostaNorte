/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controladores;

import Interfaces.BusquedaGui;
import Modelos.Arancel;
import Modelos.Socio;
import java.util.Iterator;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author Nico
 */
public class ActualizarDatos {

    private BusquedaGui clientesGui;
    
    public ActualizarDatos(BusquedaGui clientesGui) {
    this.clientesGui= clientesGui;
    }
    
    public void cargarSocios(){
        LazyList<Socio> ListSocios= Socio.where("ACTIVO = ?", 1);
            clientesGui.getTablaClientesDefault().setRowCount(0);
             Iterator<Socio> it = ListSocios.iterator();
             while(it.hasNext()){
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
             LazyList lista = Arancel.where("ACTIVO = ?", 1);
             Iterator<Arancel> iter = lista.iterator();
             String d[] = new String[lista.size()+2];
             int i = 0;
             while(iter.hasNext()){
                 Arancel a = iter.next();
                 d[i] = a.getString("nombre");
                 i++;
             }
             d[i] = "INACTIVOS";
             d[i+1] = "MOROSOS";
             clientesGui.getActividades().setListData(d);
             clientesGui.getLabelResult3().setText(Integer.toString(clientesGui.getTablaClientes().getRowCount()));
    }
    
}
