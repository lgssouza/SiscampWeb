<%@page import="br.com.dao.EstruturaDAO"%>
<%@page import="br.com.entidades.Vao"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.VaoDAO"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>

<%
    VaoDAO dao = new VaoDAO();
    EstruturaDAO daoEst = new EstruturaDAO();
    Vao vao = new Vao();
    if (session.getAttribute("vaos") != null && session.getAttribute("vaos") != null) {
        vao = (Vao) session.getAttribute("vaos");
        session.setAttribute("vaos", null);
    } else if (request.getParameter("id") != null) {
        vao = dao.seleciona(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("vaos", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="linhas_seleciona_vao.jsp">Seleciona Linha</a></li>
    <li><a href="vaos.jsp">Vãos</a></li>
    <li class="current"><a href="#">Cadastro de Vão</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Vão</h2>
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

        <form name="cadVao" method="post" action="VaoServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-6 columns">
                            <label>Estrutura Inicial *                                                                    
                                <select name="estinicial" data-selected="<% if (vao.getIdEstInicial() != 0) {
                                        out.print(vao.getIdEstInicial());
                                    } %>">                                                   
                                    <%
                                        ResultSet rs = daoEst.listarEstruturasComboInicial(Linha.idLinha);
                                        rs.previous();
                                        while (rs.next()) {
                                            out.print("<option ");
                                            if (rs.getInt("id_estrutura") == vao.getIdEstInicial()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs.getString("id_estrutura") + ">" + rs.getString("est_numero") + "</option>");
                                        }

                                        rs.close();
                                    %>                                        
                                </select>
                            </label>                            
                            <small class="error">Selecione a Estrutura Inicial</small>
                        </div>                        
                        <div class="large-6 columns">
                            <label>Estrutura Final *                                                                    
                                <select name="estfinal" data-selected="<% if (vao.getIdEstFinal() != 0) {
                                        out.print(vao.getIdEstFinal());
                                    } %>">                                                   
                                    <%
                                        ResultSet rs2 = daoEst.listarEstruturasComboFinal(Linha.idLinha);
                                        rs2.previous();
                                        while (rs2.next()) {
                                            out.print("<option ");
                                            if (rs2.getInt("id_estrutura") == vao.getIdEstFinal()) {
                                                out.print("selected ");
                                            }
                                            out.println("value=" + rs2.getString("id_estrutura") + ">" + rs2.getString("est_numero") + "</option>");
                                        }

                                        rs2.close();
                                    %>                                        
                                </select>
                            </label>                            
                            <small class="error">Selecione a Estrutura Final</small>
                        </div>             
                    </div>
                    <div class="row">
                        <div class="large-6 columns">
                            <label>Distância *
                                <input type="text" name="distancia" class="metros" placeholder="Digite a Distância" value="<% if (vao.getDistancia() != null) {
                                        out.print(vao.getDistancia());
                                    } %>" required/>
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                        <div class="large-6 columns">
                            <label>Sequência *
                                <%
                                    int sequencia = 0;
                                    if (request.getParameter("id") == null) {
                                        VaoDAO daoVao = new VaoDAO();
                                        sequencia = daoVao.getUltimaSequencia(Linha.idLinha);
                                    } else {
                                        sequencia = vao.getSequencia();
                                    }

                                %>
                                <input type="text" name="sequencia"  placeholder="Digite a Sequência" value="<% out.print(sequencia);%>" required/>
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                                                
                    </div>                    
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="linhas_seleciona_vao.jsp" class="button secondary">Cancelar</a>
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