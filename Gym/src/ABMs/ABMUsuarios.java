/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ABMs;

import Modelos.User;
import org.javalite.activejdbc.Base;

/**
 *
 * @author A
 */
public class ABMUsuarios {
    
     public User getUsuario(User u) {
        return User.first("USUARIO = ?", u.get("USUARIO"));
    }

    public boolean findUsuario(User u) {
        return (User.first("USUARIO = ?", u.get("USUARIO")) != null);
    }

    public boolean alta(User u) {
        if (!findUsuario(u)) {
            Base.openTransaction();
            User nuevo = User.create("USUARIO", u.get("USUARIO"), "PASSWD", u.get("PASSWD"), "ADMINIS", u.get("ADMINIS"));
            nuevo.saveIt();
            Base.commitTransaction();
            return true;
        } else {
            return false;
        }
    }

    public boolean baja(User u) {
        User viejo = User.first("USUARIO = ?", u.getString("USUARIO"));
        if (viejo != null) {
            Base.openTransaction();
            viejo.delete();
            Base.commitTransaction();
            return true;
        }
        return false;
    }

    public boolean modificar(User u) {
        User viejo = User.first("USUARIO = ?", u.get("USUARIO"));
        if (viejo != null) {
            Base.openTransaction();
            //System.out.println("entre si esta");
            viejo.set("PASSWD", u.get("PASSWD"), "ADMINIS", u.get("ADMINIS"));
            viejo.saveIt();
            Base.commitTransaction();
            return true;
        }
        return false;
    }
}
