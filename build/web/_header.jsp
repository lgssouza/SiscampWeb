<%-- 
    Document   : admin
    Created on : 27/04/2015, 11:58:39
    Author     : Luiz Guilherme Souza
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //verificação - usuário logado na mesma sessão
    String login = (String) session.getAttribute("uname");
    if (login == "" || login == null) {        
        request.getRequestDispatcher("LogoffServlet").forward(request, response);

    }
%>
<!DOCTYPE HTML>
<html lang="pt-BR">
    <head>
        <link href="Foundation/img/favicon.ico" rel="shortcut icon" type="image/x-icon">    
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />    
        <link rel="stylesheet" href="Foundation/css/foundation.css" />
        <link rel="stylesheet" href="Foundation/css/foundation-icons/foundation-icons.css" />
        <link rel="stylesheet" href="Foundation/css/foundation.min.css" type="text/css">
        <link rel="stylesheet" href="Foundation/css/foundation.min.css" type="text/css"/>           
        <script src="Foundation/js/vendor/jquery.js"></script>
        <script src="Foundation/js/jquery.maskedinput.min.js"></script>    
        <script src="Foundation/js/inputmask/jquery.inputmask.js"></script>
        <script src="Foundation/js/custom/mask.js"></script>    
        <script src="Foundation/js/vendor/modernizr.js"></script>

        <title>Siscamp - Consultas de Linhas de Transmissão</title>    
    </head>

    <body>
        <nav class="top-bar" data-topbar role="navigation">
            <ul class="title-area">
                <li class="name">
                    <h1>
                        <a href="#" style="padding:0 10px; float:left;">
                            <img src="Foundation/img/logo-cemig.png" style="max-width:90%; max-height:40px;">
                        </a>
                    </h1>
                </li>
                <!-- Remove the class "menu-icon" to get rid of menu icon. Take out "Menu" to just have icon alone -->
                <li class="toggle-topbar menu-icon">
                    <a href="#"><span>Menu</span></a>
                </li>
            </ul>
            <section class="top-bar-section">
                <!-- Right Nav Section -->
                <ul class="right">                
                    <li class="has-dropdown show-for-small-only">
                        <a href="#"><i class="fi-calendar"></i> Área de Consultas</a>
                        <ul class="dropdown">
                            <li>
                                <a href="home.jsp">Home</a>
                            </li>
                        </ul>
                    </li>
                    <li class="has-dropdown show-for-small-only">
                        <a href="#panel2a"><i class="fi-list"></i> Cadastros e Consultas</a>
                        <ul class="dropdown">
                            <li>
                                <a href="linhas.jsp">Linhas de Transmissão</a>
                            </li>
                            <li>
                                <a href="estruturas.jsp">Estruturas</a>
                            </li>
                            <li>
                                <a href="vaos.jsp">Vãos</a>
                            </li>
                            <li>
                                <a href="servicos.jsp">Serviços</a>
                            </li>
                            <li>
                                <a href="sobreavisos.jsp">Sobreavisos</a>
                            </li>
                            <li>
                                <a href="contatos.jsp">Contatos</a>
                            </li>               
                        </ul>
                    </li>
                    <li class="has-dropdown show-for-small-only">
                        <a href="#"><i class="fi-wrench"></i> Administrativo</a>
                        <ul class="dropdown">
                            <li>
                                <a href="#">Backup</a>
                            </li>
                        </ul>
                    </li>                
                    <li class="has-dropdown show-for-small-only">
                        <a href="#"><i class="fi-info"></i> Ajuda</a>
                        <ul class="dropdown">
                            <li>
                                <a href="#">Sobre o Sistema</a>
                            </li>
                            <li>
                                <a href="#">Suporte</a>
                            </li>
                        </ul>
                    </li>
                    <li class="has-dropdown">
                        <%
                            String nomeUsuario = (String) session.getAttribute("uname");
                            out.println("<a>" + nomeUsuario + "</a>");
                        %>
                        <ul class="dropdown">                        
                            <li>
                                <a href="trocarsenha.jsp">Alterar Senha</a>
                                <a href="LogoffServlet" onclick="return confirm('Deseja Sair?')">Sair</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </section>
        </nav>
