<%@page import="br.com.utilitarios.MetodosUtil"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.VaoDAO"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%@include file="_header.jsp"%>  

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="linhas_seleciona_vao.jsp">Seleciona Linha</a></li>
    <li class="current"><a href="#">Vãos</a></li>
</ul>

<%  String pesq = "";
    String filtro = "";

    if (request.getParameter("linha") != null) {
        Linha.idLinha = Integer.parseInt(request.getParameter("linha"));
    } else {
        if (Linha.idLinha == 0) {
            response.sendRedirect("linhas_seleciona_vao");
        }
    }

    String msg = (String) session.getAttribute("sucesso");
    if (request.getParameter("pesquisa") != null) {
        pesq = request.getParameter("pesquisa");
    }

    if (request.getParameter("filtro") != null) {
        filtro = request.getParameter("filtro");
    } else {
        filtro = "inicial";
    }
    if (msg != "" && msg != null) {
        out.println("<div class=row>");
        out.println("<div data-alert class=\"alert-box success\">");
        out.println("" + msg + "");
        out.println("<a href=\"error\" class=\"close\">&times;</a>");
        out.println("</div>");
        out.println("</div>");
    }
    session.setAttribute("sucesso", null);

    VaoDAO dao = new VaoDAO();
    int pagant = 1;
    int pagprox = 1;
    int totpag = dao.totalPaginas(pesq, filtro, Linha.idLinha);

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
                <%                    LinhaDAO daoLinha = new LinhaDAO();
                    String nomenclatura = daoLinha.getNomeLinha(Linha.idLinha);
                    out.println("<h4>Estruturas - " + nomenclatura);
                %>
                <a href="vaos_form.jsp" class="button secondary small right">+ Novo</a>
                </h2>
            </div>
        </div>        
        <div class="row">
            <div class="large-12 columns">
                <form class="row collapse" method="get" action="vaos.jsp">
                    <div class="small-8 columns">
                        <input type="text" name="pesquisa" placeholder="Digite sua Pesquisa aqui">
                    </div>
                    <div class="small-2 columns">
                        <button class="button postfix" type="submit">Filtrar</button>
                    </div>
                    <div class="small-2 columns">
                        <select name="filtro">                       
                            <option value="inicial">Est. Inicial</option>
                            <option value="final">Est. Final</option>                        
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
                            <th style="width:80px;">Sequência</th>
                            <th style="width:80px;">Est. Inicial</th>
                            <th style="width:80px;">Est. Final</th>
                            <th style="width:80px;">Distância</th>
                            <th style="width:80px;">Progressiva</th>
                            <th style="width:80px;">Regressiva</th>                            
                            <th style="width:80px;">Editar</th>
                            <th style="width:80px;">Excluir</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%                            //grid
                            ResultSet rs = dao.listar(pesq, pagatual, filtro, Linha.idLinha);
                            rs.previous();
                            while (rs.next()) {
                                out.println("<tr>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("vao_sequencia") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("est1") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("est2") + "</td>");
                                out.println("<td style=\"text-align:center\">" + MetodosUtil.formataDistancia(rs.getDouble("vao_distancia")) + "</td>");
                                out.println("<td style=\"text-align:center\">" + MetodosUtil.formataDistancia(rs.getDouble("vao_progressiva")) + "</td>");
                                out.println("<td style=\"text-align:center\">" + MetodosUtil.formataDistancia(rs.getDouble("vao_regressiva")) + "</td>");
//                                out.println("<td>" + rs.getString("linhadist_nomenclatura") + "</td>");
                                out.println("<td style=\"text-align:center\" onclick=\"window.location='vaos_form.jsp?id=" + rs.getInt("id_vao") + "'\"><img src=\"Foundation/img/editar.png\"></td>");
                                out.println("<td class=\"text-center\"><a href=\"VaoServlet?id=" + rs.getInt("id_vao") + "&excluir=sim\" class=\"remove\" onclick=\"return confirm('Confirma a exclusão?')\"><img src=\"Foundation/img/excluir.png\"></a></td>");
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
                        if (totpag > 1) {
                            if (!pesq.equals("")) {
                                out.println("<li class=\"arrow\"><a href=\"vaos.jsp?pag=" + pagant + "\">&laquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"vaos.jsp?pesquisa=" + pesq + "&pag=" + pagant + "\">&laquo;</a></li>");
                            }
                            for (int i = 1; i <= totpag; i++) {
                                if (!pesq.equals("")) {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"vaos.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"vaos.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    }
                                } else {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"vaos.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"vaos.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    }
                                }
                            }

                            if (!pesq.equals("")) {
                                out.println("<li class=\"arrow\"><a href=\"vaos.jsp?pag=" + pagprox + "\">&raquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"vaos.jsp?pesquisa=" + pesq + "&pag=" + pagprox + "\">&raquo;</a></li>");
                            }

                        }
                    %>                    
                </ul>
            </div>
        </div>
    </article>
</section>
<%@include file="_footer.jsp"%>  