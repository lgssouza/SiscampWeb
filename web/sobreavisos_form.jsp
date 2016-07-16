<%@page import="br.com.utilitarios.MetodosUtil"%>
<%@page import="br.com.dao.FuncionarioDAO"%>
<%@page import="br.com.entidades.Sobreaviso"%>
<%@page import="br.com.dao.SobreavisoDAO"%>
<%@page import="br.com.dao.EstruturaDAO"%>
<%@page import="br.com.entidades.Vao"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.VaoDAO"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>

<%

    SobreavisoDAO dao = new SobreavisoDAO();
    Sobreaviso sobreaviso = new Sobreaviso();
    if (session.getAttribute("sobreavisos") != null && session.getAttribute("sobreavisos") != null) {
        sobreaviso = (Sobreaviso) session.getAttribute("sobreavisos");
        session.setAttribute("sobreavisos", null);
    } else if (request.getParameter("id") != null) {
        sobreaviso = dao.selecionaSobreaviso(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("sobreavisos", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="sobreavisos.jsp">Sobreavisos</a></li>
    <li class="current"><a href="#">Cadastro de Sobreaviso</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Sobreaviso</h2>
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

        <form name="cadSobreaviso" method="post" action="SobreavisoServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-6 columns">
                            <label>Funcionário* 
                                <select name="func" data-selected="<% if (sobreaviso.getFuncMatricula() != 0) {
                                        out.print(sobreaviso.getFuncMatricula());
                                    } %>">                                                   
                                    <%
                                        FuncionarioDAO daoFunc = new FuncionarioDAO();
                                        ResultSet rs = daoFunc.listarFuncionarioCombo();
                                        rs.previous();
                                        while (rs.next()) {
                                            out.print("<option ");
                                            if (rs.getInt("matricula") == sobreaviso.getFuncMatricula()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs.getString("matricula") + ">" + rs.getString("func_nome") + "</option>");
                                        }

                                        rs.close();
                                    %>                                        
                                </select>
                            </label>                            
                            <small class="error">Selecione o Funcionário!</small>
                        </div>
                        <div class="large-6 columns">
                            <label>Data *
                                <input type="text" name="data"  class="data" placeholder="99/99/9999" value="<% if (sobreaviso.getData() != null) {
                                        out.print(MetodosUtil.formataDataExibir(sobreaviso.getData()));
                                    }%>" pattern="\d{1,2}/\d{1,2}/\d{4}" maxlength="10" required/>
                            </label>                            
                            <small class="error">Data Obrigatória no formato dd/mm/aaaa</small>
                        </div>
                    </div>                                        
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="sobreavisos.jsp" class="button secondary">Cancelar</a>
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