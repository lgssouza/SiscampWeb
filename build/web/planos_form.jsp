<%@page import="br.com.dao.PlanoDAO"%>
<%@page import="br.com.entidades.Plano"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%
    PlanoDAO dao = new PlanoDAO();
    Plano plano = new Plano();
    if (session.getAttribute("planos") != null && session.getAttribute("planos") != null) {
        plano = (Plano) session.getAttribute("planos");
        session.setAttribute("planos", null);
    } else if (request.getParameter("id") != null) {
        plano = dao.selecionaPlano(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("planos", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="planos.jsp">Planos de Contigência</a></li>
    <li class="current"><a href="#">Cadastro de Plano de Contigência</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Plano de Contigência</h2>
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

        <form name="cadPlano" method="post" action="UploadServlet" enctype="multipart/form-data" >
            <input name="id" value="<%
                if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } else {
                    out.print(0);
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">                    
                    <div class="row">                                                                       
                        <div class="large-8 columns">
                            <label>Linhas de Transmissão *                                                                    
                                <select name="linha">                                                   
                                    <%
                                        LinhaDAO daoLinha = new LinhaDAO();
                                        ResultSet rs = daoLinha.listarLinhasCombo();
                                        rs.previous();
                                        while (rs.next()) {
                                            out.print("<option ");
                                            if (rs.getInt("id_linhadist") == plano.getIdLinha()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs.getString("id_linhadist") + ">" + rs.getString("linhadist_nomenclatura") + "</option>");
                                        }

                                        rs.close();
                                    %>                                        
                                </select>
                            </label> 
                        </div>
                        <div class="large-4 columns">
                            <%
                                int sequencia = 0;
                                if (request.getParameter("id") == null) {
                                    sequencia = 1;
                                } else {
                                    sequencia = dao.getUltimaVersao(Integer.parseInt(request.getParameter("id")));
                                }
                            %>
                            <label>Versão *
                                <input type="text" name="versao" pattern="[0-9]+$" placeholder="Digite a Largura da Faixa" value="<% out.println(sequencia); %>" maxlength="11"/>
                            </label>                            
                        </div>
                    </div>                    
                    <fieldset>
                        <legend>Plano de Contigência</legend>
                        <div class="small-12 columns"> 
                            <div class="row">
                                <label>Arquivo: </label>  
                                <%

                                    if (plano.getNome() != null) {
                                        out.println("<h5>" + plano.getNome() + "</h5>");
                                    }


                                %>
                                <a></a>
                                <input name="arquivo" type="file" accept="application/pdf" > 
                            </div>
                        </div>
                    </fieldset>
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="planos.jsp" class="button secondary">Cancelar</a>
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