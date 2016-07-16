<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.entidades.Estrutura"%>
<%@page import="br.com.dao.EstruturaDAO"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%
    EstruturaDAO dao = new EstruturaDAO();
    LinhaDAO daoLinha = new LinhaDAO();
    Estrutura estrutura = new Estrutura();
    if (session.getAttribute("estruturas") != null && session.getAttribute("estruturas") != null) {
        estrutura = (Estrutura) session.getAttribute("estruturas");
        session.setAttribute("estruturas", null);
    } else if (request.getParameter("id") != null) {
        estrutura = dao.seleciona(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("estruturas", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="linhas_seleciona_est.jsp">Seleciona Linha</a></li>
    <li><a href="estruturas.jsp">Estruturas</a></li>
    <li class="current"><a href="#">Cadastro de Estrutura</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Estrutura</h2>
            </div>
        </div>

        <%        String msg = (String) session.getAttribute("erro");
            if (msg != "" && msg != null) {
                out.println("<div class=\"row\">");
                out.println("<div data-alert class=\"alert-box alert\">");
                out.println("" + msg + "");
                out.println("<a href=\"error\" class=\"close\">&times;</a>");
                out.println("</div>");
                out.println("</div>");
            }
            session.setAttribute("erro", null);
        %>

        <form name="cadEstrutura" method="post" action="EstruturaServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-6 columns">
                            <label>Número *
                                <input type="text"  name="numero" placeholder="Digite o Número" value="<%  if (estrutura.getNumero() != null) {
                                        out.print(estrutura.getNumero());
                                    } %>" maxlength="5" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                        <div class="large-6 columns">
                            <label>Modelo
                                <input type="text" name="modelo"  placeholder="Digite o Modelo" value="<% if (estrutura.getModelo() != null) {
                                        out.print(estrutura.getModelo());
                                    } %>" maxlength="45"/>
                            </label>                            
                        </div>                      
                    </div>
                    <div class="row">
                        <div class="large-6 columns">
                            <label>Tipo
                                <input type="text" name="tipo"  placeholder="Digite o Tipo" value="<% if (estrutura.getTipo() != null) {
                                        out.print(estrutura.getTipo());
                                    } %>" maxlength="45" />
                            </label>                            
                        </div>                        
                        <div class="large-6 columns">
                            <label>Altura
                                <input type="text" name="altura" placeholder="Digite a Altura" value="<% if (estrutura.getAltura() != null) {
                                        out.print(estrutura.getAltura());
                                    }%>" maxlength="45"/>
                            </label>                            
                        </div>                                            
                    </div>                    
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="linhas_seleciona_est.jsp" class="button secondary">Cancelar</a>
                            <button type="submit" class="button success">Salvar</button>
                        </div>
                    </div>                    
                </div>
            </div>
        </form>
    </article>
</section>                                        
<script src="Foundation/js/custom/select-ajax.js"></script>
<%@include file="_footer.jsp"%>  