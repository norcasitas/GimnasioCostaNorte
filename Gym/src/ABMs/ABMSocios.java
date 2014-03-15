/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ABMs;

import Modelos.Arancel;
import Modelos.Socio;
import Modelos.Socioarancel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 *
 * @author alan
 */
public class ABMSocios {
        
    public Socio getSocio(Socio s) {
        return Socio.first("DNI = ?", s.get("DNI"));
    }

    public boolean findSocio(Socio s) {
        return (Socio.first("DNI = ?", s.get("DNI")) != null);
    }

    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        return formateador.format(ahora);
    }
    
    public boolean alta(Socio s, LinkedList act) {
        if (!findSocio(s)) {
            Base.openTransaction();
            Socio nuevo = Socio.create("NOMBRE", s.get("NOMBRE"), "APELLIDO", s.get("APELLIDO"), "DNI", s.get("DNI"), "FECHA_NAC", s.get("FECHA_NAC"), "TEL", s.get("TEL"), "SEXO", s.get("SEXO"), "ACTIVO", 1, "DIR", s.get("DIR"));
            nuevo.set("FECHA_ING", getFechaActual());
            nuevo.saveIt();
            Iterator<Arancel> it = act.iterator();
            while(it.hasNext()){
                Arancel a = it.next();
                Socio so = getSocio(s);
                Socioarancel sa = Socioarancel.create("id_socio", so.get("ID_DATOS_PERS"), "id_arancel", a.get("id"));
                sa.saveIt();
            }
            Base.commitTransaction();
            
            return true;
        } else {
            return false;
        }
    }

    public boolean baja(Socio s) {
        Socio viejo = Socio.first("DNI = ?", s.getString("DNI"));
        if (viejo != null) {
            Base.openTransaction();
                viejo.set("ACTIVO", 0);
                viejo.saveIt();
            Base.commitTransaction();
            return true;
        }
        return false;
    }

    public boolean modificar(Socio s, LinkedList act) {
        Socio viejo = Socio.first("DNI = ?", s.getString("DNI"));
        if (viejo != null) {
            Base.openTransaction();
            viejo.set("NOMBRE", s.get("NOMBRE"), "APELLIDO", s.get("APELLIDO"), "DNI", s.get("DNI"), "FECHA_NAC", s.get("FECHA_NAC"), "TEL", s.get("TEL"), "SEXO", s.get("SEXO"));
            Socio soc = getSocio(s);
            LazyList l = Socioarancel.where("id_socio = ?", soc.get("ID_DATOS_PERS"));
            Iterator<Socioarancel> i = l.iterator();
            while(i.hasNext()){
                Socioarancel soar = i.next();
                System.out.println(soar.get("id_arancel"));
                soar.delete();
//                soar.saveIt();
            }
            Iterator<Arancel> it = act.iterator();
            while(it.hasNext()){
                Arancel a = it.next();
               // System.out.println(a.get("nombre"));
                Socio so = getSocio(s);
                Socioarancel sa = Socioarancel.create("id_socio", so.get("ID_DATOS_PERS"), "id_arancel", a.get("id"));
                sa.saveIt();
            }
            viejo.saveIt();
            Base.commitTransaction();
            return true;
        }
        return false;
    }
    
    public void modbase(){
         if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/GYM", "root", "root");
        }
         
    }
}

