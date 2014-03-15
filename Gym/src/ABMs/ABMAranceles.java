/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ABMs;

import Modelos.Arancel;
import Modelos.Socioarancel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;

/**
 *
 * @author alan
 */
public class ABMAranceles {
    
    public Arancel getArancel(Arancel s) {
        return Arancel.first("nombre = ?", s.get("nombre"));
    }
    public boolean findArancel(Arancel s) {
        return (Arancel.first("nombre = ?", s.get("nombre")) != null);
    }
    
    public boolean alta(Arancel s) {
        if (!findArancel(s)) {
            Base.openTransaction();
            Arancel nuevo = Arancel.create("nombre",s.get("nombre"), "precio", s.get("precio"), "fecha", s.get("fecha"), "activo", s.get("activo"),"categoria", s.get("categoria"));
            nuevo.saveIt();
            Base.commitTransaction();
            return true;
        } else {
            return false;
        }
    }

    public boolean baja(Arancel s) {
        Arancel viejo = Arancel.first("id = ?", s.getString("id"));
        if (viejo != null) {
            Base.openTransaction();
                LazyList<Socioarancel> listAran = Socioarancel.where("id_arancel = ?", viejo.get("id"));
                Iterator<Socioarancel> it = listAran.iterator();
                while(it.hasNext()){
                    Socioarancel soar = it.next();
                    soar.delete();
                    
                }
                viejo.delete();
                
                
            Base.commitTransaction();
            return true;
        }
        return false;
    }

    public boolean modificar(Arancel s) {
        Arancel viejo = Arancel.first("id = ?", s.getString("id"));
        if (viejo != null) {
            Base.openTransaction();
            viejo.set("nombre", s.get("nombre"),"fecha", s.get("fecha"), "precio", s.get("precio"), "categoria", s.get("categoria"));
            viejo.saveIt();
            Base.commitTransaction();
            return true;
        }
        return false;
    }
    
}
