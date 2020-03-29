/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.backend.dao;

import banco.backend.estructuras.Cliente;
import banco.backend.estructuras.Cuenta;
import banco.backend.estructuras.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author jonguz
 */
public class BancoDAO {

    //<editor-fold desc="Metodos de generales" defaultstate="collapsed">
    private BancoDAO() {
        try {

            this.cfg.load(getClass().getResourceAsStream(UBICACION_CREDENCIALES));
            this.baseDatos = cfg.getProperty("base_datos");
            this.usuario = cfg.getProperty("usuario");
            this.clave = cfg.getProperty("clave");
        } catch (IOException ex) {
            System.err.printf("Excepción: '%s'%n", ex.getMessage());
        }
    }

    private Connection obtenerConexion() {
        try {
            return GestorBD.obtenerInstancia().obtenerConexion(baseDatos, usuario, clave);
        } catch (SQLException e) {
            String error = e.getLocalizedMessage();
            System.err.printf("No se pudo conectar con la base de datos: %s \n", error);
            return null;
        }

    }

    public static BancoDAO obtenerInstancia() {
        return instancia == null ? instancia = new BancoDAO() : instancia;
    }
    //</editor-fold>

    //<editor-fold desc="Metodos de estructuras" defaultstate="collapsed">
    //<editor-fold desc="Usuario" defaultstate="collapsed">
    public boolean agregarUsuario(Usuario usuario) {
        String comando = usuario.esAdministrativo() ? CMD_AGREGAR_ADMIN : CMD_AGREGAR_USUARIO;
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(comando)) {

            stm.clearParameters();
            stm.setInt(1, usuario.getCedula());
            stm.setString(2, usuario.getPass());

            return stm.executeUpdate() == 1;
        } catch (Exception ex) {
            System.err.printf("No se pudo insertar un usuario nuevo: %s \n", ex.getMessage());
        }
        return false;
    }

    public Usuario recuperarUsuario(Usuario credenciales) {
        Usuario resultado = null;
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm
                = cnx.prepareStatement(
                        credenciales.esAdministrativo()
                        ? CMD_RECUPERAR_ADMIN
                        : CMD_RECUPERAR_USUARIO)) {
            stm.clearParameters();

            stm.setInt(1, credenciales.getCedula());

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    resultado = new Usuario(rs.getInt(1), rs.getString(2), credenciales.esAdministrativo());
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return resultado;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        String comando = usuario.esAdministrativo() ? CMD_ACTUALIZAR_ADMIN : CMD_ACTUALIZAR_USUARIO;
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(comando)) {

            stm.clearParameters();
            stm.setString(1, usuario.getPass());
            stm.setInt(2, usuario.getCedula());
            stm.setString(3, usuario.getPass());

            return stm.executeUpdate() == 1;
        } catch (Exception ex) {
            System.err.printf("No se pudo insertar un usuario nuevo: %s \n", ex.getMessage());
        }
        return false;
    }

    //</editor-fold>
    //<editor-fold desc="Cliente" defaultstate="collapsed">
    public boolean agregarCliente(Cliente cliente) {
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(CMD_AGREGAR_CLIENTE)) {

            stm.clearParameters();
            stm.setInt(1, cliente.getCedula());
            stm.setString(2, cliente.getNombre());
            stm.setString(3, cliente.getApellidos());
            stm.setString(4, cliente.getNumero());

            return stm.executeUpdate() == 1;
        } catch (Exception ex) {
            System.err.printf("No se pudo insertar un cliente nuevo: %s \n", ex.getMessage());
        }
        return false;
    }

    public Cliente recuperarCliente(int cedula) {
        Cliente resultado = null;
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(CMD_RECUPERAR_CLIENTE)) {
            stm.clearParameters();

            stm.setInt(1, cedula);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    resultado
                            = new Cliente(
                                    rs.getInt(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getString(4)
                            );
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } catch (Exception ex) {
            String error = ex.getLocalizedMessage();
            System.err.println(error);
        }
        return resultado;
    }

    public boolean actualizarCliente(Cliente cliente) {
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(CMD_ACTUALIZAR_CLIENTE)) {

            stm.clearParameters();

            stm.setString(1, cliente.getNombre());
            stm.setString(2, cliente.getApellidos());
            stm.setString(3, cliente.getNumero());
            stm.setInt(4, cliente.getCedula());

            return stm.executeUpdate() == 1;
        } catch (Exception ex) {
            System.err.printf("No se pudo insertar un cliente nuevo: %s \n", ex.getMessage());
        }
        return false;
    }

    //</editor-fold>
    //<editor-fold desc="Cuenta" defaultstate="collapsed">
    public Cuenta recuperarCuenta(int numeroCuenta) {
        Cuenta resultado = null;
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(CMD_RECUPERAR_CUENTA)) {
            stm.clearParameters();

            stm.setInt(1, numeroCuenta);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    resultado
                            = new Cuenta(
                                    rs.getInt(1),
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getBigDecimal(4),
                                    rs.getInt(4)
                            );
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } catch (Exception ex) {
            String error = ex.getLocalizedMessage();
            System.err.println(error);
        }
        return resultado;
    }

    public Cuenta[] recuperarCuentas(int cedula) {
        ArrayList<Cuenta> resultado = new ArrayList<>();
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(CMD_RECUPERAR_CUENTAS)) {
            stm.clearParameters();

            stm.setInt(1, cedula);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Cuenta nuevo
                            = new Cuenta(
                                    rs.getInt(1),
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getBigDecimal(4),
                                    rs.getInt(4)
                            );
                    resultado.add(nuevo);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } catch (Exception ex) {
            String error = ex.getLocalizedMessage();
            System.err.println(error);
        }
        return resultado.toArray(new Cuenta[0]);
    }

    //</editor-fold>
    //<editor-fold desc="Cuenta Vinculada" defaultstate="collapsed">
    public Cuenta[] recuperarCuentasVinculadas(int cedula) {
        ArrayList<Cuenta> resultado = new ArrayList<>();
        try (Connection cnx = obtenerConexion();
                PreparedStatement stm = cnx.prepareStatement(CMD_RECUPERAR_CUENTA_VINCULADA)) {
            stm.clearParameters();

            stm.setInt(1, cedula);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Cuenta nuevo
                            = new Cuenta(
                                    rs.getInt(1),
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getBigDecimal(4),
                                    rs.getInt(4)
                            );
                    resultado.add(nuevo);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } catch (Exception ex) {
            String error = ex.getLocalizedMessage();
            System.err.println(error);
        }
        return resultado.toArray(new Cuenta[0]);
    }
    //</editor-fold>
    private static BancoDAO instancia = null;
    private static final String UBICACION_CREDENCIALES = "/configuraciones/credenciales-bd.properties";
    private Properties cfg = new Properties();
    private String baseDatos;
    private String usuario;
    private String clave;

    //<editor-fold desc="SENTENCIAS SQL" defaultstate="collapsed">
    //<editor-fold desc="Usuario" defaultstate="collapsed">
    private static final String CMD_AGREGAR_USUARIO
            = "INSERT INTO usuario "
            + "(cedula, pass) "
            + "VALUES (?, ?);";
    private static final String CMD_RECUPERAR_USUARIO
            = "select * from usuario "
            + "where cedula = ?;";
    private static final String CMD_ACTUALIZAR_USUARIO
            = "UPDATE usuario "
            + "SET pass = ? "
            + "WHERE cedula=?, pass = ?;";
    //</editor-fold>
    //<editor-fold desc="Administrador" defaultstate="collapsed">
    private static final String CMD_AGREGAR_ADMIN
            = "INSERT INTO administrador "
            + "(cedula, pass) "
            + "VALUES (?, ?);";
    private static final String CMD_RECUPERAR_ADMIN
            = "select * from administrador "
            + "where cedula = ?;";
    private static final String CMD_ACTUALIZAR_ADMIN
            = "UPDATE administrador "
            + "SET pass = ? "
            + "WHERE cedula=?, pass = ?;";
    //</editor-fold>
    //<editor-fold desc="Cliente" defaultstate="collapsed">
    private static final String CMD_AGREGAR_CLIENTE
            = "insert into cliente "
            + "(cedula, nombre, apellidos, telefono) "
            + "values (?, ?, ?, ?);";
    private static final String CMD_RECUPERAR_CLIENTE
            = "select * from cliente "
            + "where cedula = ?;";
    private static final String CMD_ACTUALIZAR_CLIENTE
            = "UPDATE cliente "
            + "SET nombre = ?, apellidos=?, numero=? "
            + "WHERE cedula=?;";
    //</editor-fold>
    //<editor-fold desc="Cuenta" defaultstate="collapsed">
    private static final String CMD_AGREGAR_CUENTA
            = "insert into cuenta "
            + "(cedula, moneda, saldo, limite_transferencia) "
            + "values (?, ?, ?, ?, ?);";
    private static final String CMD_RECUPERAR_CUENTA
            = "select * from cuenta "
            + "where id_cuenta = ?;";
    private static final String CMD_RECUPERAR_CUENTAS
            = "select * from cuenta "
            + "where cedula= ?;";
    private static final String CMD_ACTUALIZAR_CUENTA_SALDO
            = "UPDATE cliente "
            + "SET saldo = ? "
            + "WHERE cedula=?;";
    private static final String CMD_ACTUALIZAR_CUENTA_LIMITE
            = "UPDATE cliente "
            + "SET saldo = ? "
            + "WHERE cedula=?;";
    //</editor-fold>
    //<editor-fold desc="Cuenta Vinculada" defaultstate="collapsed">
    private static final String CMD_AGREGAR_CUENTA_VINCULADA
            = "insert into cuenta "
            + "(id_cuenta, cedula) "
            + "values (?, ?, ?, ?, ?);";
    private static final String CMD_RECUPERAR_CUENTA_VINCULADA
            = "select * from cuenta_vinculada "
            + "where cedula= ?";
    private static final String CMD_REMOVER_CUENTA_VINCULADA
            = "delete from cuenta_vinculada "
            + "where id_cuenta= ? cedula= ? ";

    //</editor-fold>
    //<editor-fold desc="Moneda" defaultstate="collapsed">
    private static final String CMD_RECUPERAR_MONEDA
            = "select * from moneda";
    private static final String CMD_RECUPERAR_MONEDAS
            = "select * from moneda "
            + "where codigo= ?";
    //</editor-fold>
    //<editor-fold desc="Movimiento" defaultstate="collapsed">
    private static final String CMD_AGREGAR_MOVIMIENTO
            = "insert into movimiento "
            + "(id_cuenta, deposito, monto, descripcion)"
            + "values (?, ?, ?, ?);";
    private static final String CMD_RECUPERAR_MOVIMIENTO
            = "select * from movimiento "
            + "where id_transaccion = ?";
    //</editor-fold>
}
