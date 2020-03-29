/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.backend;

import banco.backend.db.BancoDAO;
import banco.backend.estructuras.Cliente;
import banco.backend.estructuras.Usuario;

/**
 *
 * @author jonathan
 */
public class Controlador {

    private Controlador() {

    }

    public Usuario login(Usuario credenciales) {
        Usuario  res = bd.recuperarUsuario(credenciales);
        
        if(res!=null && !res.equals(credenciales)){
                res=null;
        }

        return res;
    }
    public Cliente   recuperarDatosPersonales(Usuario cliente){
        Cliente resultado = bd.recuperarCliente(cliente.getCedula());

        return resultado;
    }
    
    
    private static Controlador instancia;

    public static Controlador getInstancia() {
        return instancia == null ? instancia = new Controlador() : instancia;
    }
    BancoDAO bd = new BancoDAO();
}
