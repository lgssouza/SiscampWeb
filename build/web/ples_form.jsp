<%@page import="br.com.utilitarios.MetodosUtil"%>
<%@page import="br.com.dao.PleDAO"%>
<%@page import="br.com.entidades.Ple"%>
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

    PleDAO dao = new PleDAO();
    Ple ple = new Ple();
    if (session.getAttribute("ples") != null && session.getAttribute("ples") != null) {
        ple = (Ple) session.getAttribute("ples");
        session.setAttribute("ples", null);
    } else if (request.getParameter("id") != null) {
        ple = dao.selecionaPle(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("ples", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="ples.jsp">Ple</a></li>
    <li class="current"><a href="#">Cadastro de Ple</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Ple</h2>
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

        <form name="cadPle" method="post" action="PleServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-3 columns">
                            <label>Número *                                 
                                <input type="text" name="numero" placeholder="Digite o Número da PLE" value="<% if (ple.getIdPle() != 0) {
                                        out.print(ple.getIdPle());
                                    }%>" maxlength="11" required/>
                            </label>                            
                            <small class="error">Selecione o Supervisor!</small>
                        </div>
                        <div class="large-6 columns">
                            <label>Supervisor *                                 
                                <input type="text" name="supervisor" placeholder="Digite o Supervisor" value="<% if (ple.getSupervisor() != null) {
                                        out.print(ple.getSupervisor());
                                    }%>" maxlength="50" required/>
                            </label>                            
                            <small class="error">Selecione o Supervisor!</small>
                        </div>
                        <div class="large-3 columns">
                            <label>Data *
                                <input type="text" name="data"  class="data" placeholder="99/99/9999" value="<% if (ple.getData() != null) {
                                        out.print(MetodosUtil.formataDataExibir(ple.getData()));
                                    }%>" pattern="\d{1,2}/\d{1,2}/\d{4}" maxlength="10" required/>
                            </label>                            
                            <small class="error">Data Obrigatória no formato dd/mm/aaaa</small>
                        </div>
                    </div>
                    <div class="row">
                        <div class="large-2 columns">
                            <label>Hora Inicial *  
                                <input type="text" name="hrinicial" class="hora" placeholder="Digite a Hora Inicial" value="<% if (ple.getHrInicial() != null) {
                                        out.print(ple.getHrInicial());
                                    }%>" maxlength="5" required/>
                            </label>                            
                            <small class="error">Hora Inicial Obrigatória no formato HH:mm</small>
                        </div>
                        <div class="large-2 columns">
                            <label>Hora Final *  
                                <input type="text" name="hrfinal" class="hora" placeholder="Digite a Hora Inicial" value="<% if (ple.getHrFinal() != null) {
                                        out.print(ple.getHrFinal());
                                    }%>"  maxlength="5" required/>
                            </label>                            
                            <small class="error">Hora Final Obrigatória no formato HH:mm</small>
                        </div>
                        <div class="large-8 columns">
                            <label>Linhas de Transmissão                                                                    
                                <select name="linha">                                                   
                                    <%
                                        LinhaDAO daoLinha = new LinhaDAO();
                                        ResultSet rs = daoLinha.listarLinhasCombo();
                                        rs.previous();
                                        while (rs.next()) {
                                            out.print("<option ");
                                            if (rs.getInt("id_linhadist") == ple.getIdLinhaDist()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs.getString("id_linhadist") + ">" + rs.getString("linhadist_nomenclatura") + "</option>");
                                        }

                                        rs.close();
                                    %>                                        
                                </select>
                            </label> 
                        </div>
                    </div>
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="ples.jsp" class="button secondary">Cancelar</a>
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