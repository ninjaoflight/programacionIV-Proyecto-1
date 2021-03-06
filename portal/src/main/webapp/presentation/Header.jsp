<%@page import="banco.backend.estructuras.Cliente"%>
<%@page import="banco.backend.estructuras.Usuario"%>
<% Usuario usuario = (Usuario) session.getAttribute("usuario");%>
<header>
    <div class="logo">
        <img src="/portal/images/logo.png" alt="logo">
        <div class="overlay">
            <div>
                <a href="/portal">Inicio</a>
            </div>
        </div>
    </div>

    <nav>
        <ul>
            <%if (usuario != null) {%>
            <%if (usuario.esAdministrativo()) {%>
            <li><a href="/portal/admin/AbrirCuenta/show">Abrir Cuenta</a></li>
            <li><a href="/portal/admin/Movimiento">Movimientos y Transferencias</a></li>
            <li><a href="/portal/admin/intereses">Acreditar Intereses</a></li>
            <%}else{%>
            <li><a href="/portal/cliente/cuentas">Ver Cuentas</a></li>
            <li><a href="/portal/cliente/cuentas/vincular">Vincular Cuentas</a></li>
            <li><a href="/portal/cliente/cuentas/transferencia">Transferencia remota</a></li>
            <%}}%>
        </ul>
    </nav>
    <%if (usuario == null) {%>
            <a href="/portal/login/show"><button>Iniciar Sesion</button></a>
        <%} else {%>
            <a href="/portal/logout"><button>Cerrar Sesion</button></a>
     <%}%>
</header>