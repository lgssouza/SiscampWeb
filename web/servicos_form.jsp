<%@page import="br.com.entidades.ProgServico"%>
<%@page import="br.com.dao.ProgServicoDAO"%>
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

    ProgServicoDAO dao = new ProgServicoDAO();
    ProgServico progs = new ProgServico();
    if (session.getAttribute("servicos") != null && session.getAttribute("servicos") != null) {
        progs = (ProgServico) session.getAttribute("servicos");
        session.setAttribute("servicos", null);
    } else if (request.getParameter("id") != null) {
        progs = dao.selecionaServico(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("servicos", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="servicos.jsp">Serviços</a></li>
    <li class="current"><a href="#">Cadastro de Serviço</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Serviço</h2>
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

        <form name="cadServico" method="post" action="ProgServicoServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">                    
                    <div class="row">        
                        <div class="large-2 columns">
                            <label>OM * 
                                <input type="text" name="om" placeholder="Digite Nº da OM" value="<% if (progs.getIdProgServico() != 0) {
                                        out.print(progs.getIdProgServico());
                                    }%>" maxlength="11" required/>
                            </label>                  
                            <small class="error">Obrigatório</small>
                        </div>
                        <div class="large-4 columns">
                            <label>Linhas de Transmissão *                                                                    
                                <select name="linha">                                                   
                                    <%
                                        LinhaDAO daoLinha = new LinhaDAO();
                                        ResultSet rs = daoLinha.listarLinhasCombo();
                                        rs.previous();
                                        while (rs.next()) {
                                            out.print("<option ");
                                            if (rs.getInt("id_linhadist") == progs.getIdLinhaDist()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs.getString("id_linhadist") + ">" + rs.getString("linhadist_nomenclatura") + "</option>");
                                        }

                                        rs.close();
                                    %>                                        
                                </select>
                            </label> 
                        </div>
                        <div class="large-2 columns">
                            <label>Data *
                                <input type="text" name="data"  class="data" placeholder="99/99/9999" value="<% if (progs.getDataExec() != null) {
                                        out.print(progs.getDataExec());
                                    }%>" pattern="\d{1,2}/\d{1,2}/\d{4}" maxlength="10" required/>
                            </label>                            
                            <small class="error">Data Obrigatória no formato dd/mm/aaaa</small>
                        </div>
                        <div class="large-2 columns">
                            <label>Semana
                                <input type="text" name="semana" placeholder="Digite Nº da Semana" value="<% if (progs.getSemanaExec() != 0) {
                                        out.print(progs.getSemanaExec());
                                    }%>" maxlength="3"/>
                            </label>                                                        
                        </div>
                        <div class="large-2 columns">
                            <label>Qtd Pessoas
                                <input type="text" name="qtdpessoas" placeholder="Qtd de Pessoas" value="<% if (progs.getQtdPessoas() != 0) {
                                        out.print(progs.getQtdPessoas());
                                    }%>" maxlength="3"/>
                            </label>                                                        
                        </div>
                    </div>
                    <div class="row">    
                        <div class="large-6 columns">
                            <label>Ple
                                <select name="ple">                                                   
                                    <%
                                        PleDAO daoPle = new PleDAO();
                                        ResultSet rs2 = daoPle.listarPleCombo();
                                        rs2.previous();
                                        out.print("<option>N/A PLE</option>");
                                        while (rs2.next()) {
                                            out.print("<option ");
                                            if (rs2.getInt("id_ple") == progs.getIdPle()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs2.getString("id_ple") + ">" + rs2.getString("id_ple") + "</option>");
                                        }

                                        rs2.close();
                                    %>                                        
                                </select>
                            </label> 
                        </div>
                    </div>

                    <div class="row">
                        <div class="large-12 columns">
                            <label>Descrição do Serviço *      
                                <textarea type="text" name="descricao" placeholder="Digite a Descrição do Serviço" value="<% if (progs.getDescServico() != null) {
                                        out.print(progs.getDescServico());
                                    }%>" maxlength="300" required/></textarea>
                            </label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="servicos.jsp" class="button secondary">Cancelar</a>
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