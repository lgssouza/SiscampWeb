<%@page import="br.com.dao.ProgServicoDAO"%>
<%@page import="br.com.dao.PleDAO"%>
<%@page import="br.com.utilitarios.MetodosUtil"%>
<%@page import="br.com.dao.SobreavisoDAO"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%@include file="_header.jsp"%>  

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li class="current"><a href="servicos.jsp">Serviços</a></li>
</ul>

<%  String pesq = "";
    if (request.getParameter("pesquisa") != null) {
        pesq = request.getParameter("pesquisa");
    }
    String filtro = "";
    if (request.getParameter("filtro") != null) {
        filtro = request.getParameter("filtro");
    }
    String msg = (String) session.getAttribute("sucesso");
    if (msg != "" && msg != null) {
        out.println("<div class=row>");
        out.println("<div data-alert class=\"alert-box success\">");
        out.println("" + msg + "");
        out.println("<a href=\"error\" class=\"close\">&times;</a>");
        out.println("</div>");
        out.println("</div>");
    }
    session.setAttribute("sucesso", null);

    ProgServicoDAO dao = new ProgServicoDAO();
    int pagant = 1;
    int pagprox = 1;
    int totpag = dao.totalPaginas(pesq, filtro);

    int pagatual = 1;
    if (request.getParameter("pag") != null) {
        pagatual = Integer.parseInt(request.getParameter("pag"));
    }

    pagant = pagatual - 1;
    pagprox = pagatual + 1;
    if (pagant == 0) {
        pagant = 1;
    }
    if (pagprox > totpag) {
        pagprox = totpag;
    }

%>

<section class="row">
    <%@include file="_aside.jsp"%>        
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Serviços
                    <a href="servicos_form.jsp" class="button secondary small right">+ Novo</a>
                </h2>
            </div>
        </div>
        <div class="row">
            <div class="large-12 columns">
                <form class="row collapse" method="get" action="servicos.jsp">
                    <div class="small-8 columns">
                        <input type="text" name="pesquisa" placeholder="Digite sua Pesquisa aqui">
                    </div>
                    <div class="small-2 columns">
                        <button class="button postfix" type="submit">Filtrar</button>
                    </div>
                    <div class="large-2 columns">
                        <select name="filtro">
                            <option value="linha">Linha</option>
                            <option value="data">Data</option>                                
                            <option value="semana">Semana</option>                                
                        </select>
                    </div>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="small-12 columns">
                <table style="width:100%; max-width:100%;">
                    <thead>
                        <tr>
                            <th style="text-align: center; width:80px;">OM</th>
                            <th style="width:150px;">Descrição</th>                            
                            <th style="text-align: center; width:80px;">Semana</th>
                            <th style="text-align: center; width:100px;">Data</th>
                            <th style="text-align: center; width:80px;">Qtd. Pessoas</th>
                            <th>Linha</th>
                            <th style="text-align: center; width:100px;">Ple</th>
                            <th style="width:80px;">Editar</th>
                            <th style="width:80px;">Excluir</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%                            //grid
                            ResultSet rs = dao.listarProgServico(pesq, pagatual, filtro);

                            rs.previous();

                            while (rs.next()) {
                                out.println("<tr>");
                                out.println("<td style=\"text-align:center\">" + rs.getLong("id_progservico") + "</td>");
                                out.println("<td>" + rs.getString("progs_descservico") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getInt("progs_semanaexec") + "</td>");
                                out.println("<td style=\"text-align:center\">" + MetodosUtil.formataDataExibir(rs.getDate("progs_dataexec")) + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getInt("progs_qtdpessoas") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("linhadist_nomenclatura") + "</td>");
                                if (rs.getInt("fk_progs_id_ple") != 0) {
                                    out.println("<td style=\"text-align:center\" onclick=\"window.location='ples_form.jsp?id=" + rs.getInt("fk_progs_id_ple") + "'\">" + rs.getInt("fk_progs_id_ple") + "</td>");
                                } else {
                                    out.println("<td style=\"text-align:center\">N/A</td>");
                                }
                                out.println("<td style=\"text-align:center\" onclick=\"window.location='servicos_form.jsp?id=" + rs.getLong("id_progservico") + "'\"><img src=\"Foundation/img/editar.png\"></td>");
                                out.println("<td class=\"text-center\"><a href=\"ProgServicoServlet?id=" + rs.getLong("id_progservico") + "&excluir=sim\" class=\"remove\" onclick=\"return confirm('Confirma a exclusão?')\"><img src=\"Foundation/img/excluir.png\"></a></td>");
                                out.println("</tr>");
                            }

                            rs.close();
                        %>                                        
                    </tbody>
                </table>
            </div>
            <div class="pagination-centered">
                <ul class="pagination">
                    <%
                        if (totpag
                                > 1) {
                            if (!pesq.equals("")) {
                                out.println("<li class=\"arrow\"><a href=\"servicos.jsp?pag=" + pagant + "\">&laquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"servicos.jsp?pesquisa=" + pesq + "&pag=" + pagant + "\">&laquo;</a></li>");
                            }
                            for (int i = 1; i <= totpag; i++) {
                                if (!pesq.equals("")) {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"servicos.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"servicos.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    }
                                } else {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"servicos.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"servicos.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    }
                                }
                            }

                            if (!pesq.equals("")) {
                                out.println("<li class=\"arrow\"><a href=\"servicos.jsp?pag=" + pagprox + "\">&raquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"servicos.jsp?pesquisa=" + pesq + "&pag=" + pagprox + "\">&raquo;</a></li>");
                            }

                        }
                    %>                    
                </ul>
            </div>
        </div>
    </article>
</section>
<%@include file="_footer.jsp"%>  