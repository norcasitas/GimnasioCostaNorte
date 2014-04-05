/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ABMs;

import Modelos.Usuario;
import org.javalite.activejdbc.Base;

/**
 *
 * @author A
 */
public class ABMUsuarios {
    
     public Usuario getUsuario(Usuario u) {
        return Usuario.first("USUARIO = ?", u.get("USUARIO"));
    }

    public boolean findUsuario(Usuario u) {
        return (Usuario.first("USUARIO = ?", u.get("USUARIO")) != null);
    }

    public boolean alta(Usuario u) {
        if (!findUsuario(u)) {
            Base.openTransaction();
            Usuario nuevo = Usuario.create("USUARIO", u.get("USUARIO"), "PASSWD", u.get("PASSWD"), "ADMINIS", u.get("ADMINIS"));
            nuevo.saveIt();
            Base.commitTransaction();
            return true;
        } else {
            return false;
        }
    }

    public boolean baja(Usuario u) {
        Usuario viejo = Usuario.first("USUARIO = ?", u.getString("USUARIO"));
        if (viejo != null) {
            Base.openTransaction();
            viejo.delete();
            Base.commitTransaction();
            return true;
        }
        return false;
    }

    public boolean modificar(Usuario u) {
        Usuario viejo = Usuario.first("USUARIO = ?", u.get("USUARIO"));
        if (viejo != null) {
            Base.openTransaction();
            viejo.set("PASSWD", u.get("PASSWD"), "ADMINIS", u.get("ADMINIS"));
            viejo.saveIt();
            Base.commitTransaction();
            return true;
        }
        return false;
    }
}
