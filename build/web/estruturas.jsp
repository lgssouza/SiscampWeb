<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.EstruturaDAO"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%@include file="_header.jsp"%>  

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="linhas_seleciona_est.jsp">Seleciona Linha</a></li>
    <li class="current"><a href="#">Estruturas</a></li>
</ul>

<%  String pesq = "";
    String filtro = "";

    if (request.getParameter("linha") != null) {
        Linha.idLinha = Integer.parseInt(request.getParameter("linha"));
    } else {
        if (Linha.idLinha == 0) {
            response.sendRedirect("linhas_seleciona_est.jsp");
        }

    }
    String msg = (String) session.getAttribute("sucesso");
    if (request.getParameter("pesquisa") != null) {
        pesq = request.getParameter("pesquisa");
    }

    if (request.getParameter("filtro") != null) {
        filtro = request.getParameter("filtro");
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

    EstruturaDAO dao = new EstruturaDAO();
    int pagant = 1;
    int pagprox = 1;
    int totpag = dao.totalPaginas(pesq, Linha.idLinha);

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

                <a href="estruturas_form.jsp" class="button secondary small right">+ Novo</a>
                </h2>
            </div>
        </div>        
        <div class="row">
            <div class="large-12 columns">
                <form class="row collapse" method="get" action="estruturas.jsp">
                    <div class="small-10 columns">
                        <input type="text" name="pesquisa" placeholder="Digite sua Pesquisa aqui">
                    </div>
                    <div class="small-2 columns">
                        <button class="button postfix" type="submit">Filtrar</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="small-12 columns">
                <table style="width:100%; max-width:100%;">
                    <thead>
                        <tr>                            
                            <th style="width:80px;">Número</th>
                            <th style="width:80px;">Modelo</th>
                            <th style="width:80px;">Tipo</th>
                            <th style="width:80px;">Altura</th>
                            <!--<th>Linha</th>-->
                            <th style="width:80px;">Editar</th>
                            <th style="width:80px;">Excluir</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%                            //grid
                            ResultSet rs = dao.listar(pesq, pagatual, Linha.idLinha);
                            rs.previous();
                            while (rs.next()) {
                                out.println("<tr>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("est_numero") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("est_modelo") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("est_tipo") + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("est_altura") + "</td>");
//                                out.println("<td>" + rs.getString("linhadist_nomenclatura") + "</td>");
                                out.println("<td style=\"text-align:center\" onclick=\"window.location='estruturas_form.jsp?id=" + rs.getInt("id_estrutura") + "'\"><img src=\"Foundation/img/editar.png\"></td>");
                                out.println("<td class=\"text-center\"><a href=\"EstruturaServlet?id=" + rs.getInt("id_estrutura") + "&excluir=sim\" class=\"remove\" onclick=\"return confirm('Confirma a exclusão?')\"><img src=\"Foundation/img/excluir.png\"></a></td>");
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
                                out.println("<li class=\"arrow\"><a href=\"estruturas.jsp?pag=" + pagant + "\">&laquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"estruturas.jsp?pesquisa=" + pesq + "&pag=" + pagant + "\">&laquo;</a></li>");
                            }
                            for (int i = 1; i <= totpag; i++) {
                                if (!pesq.equals("")) {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"estruturas.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"estruturas.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    }
                                } else {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"estruturas.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"estruturas.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    }
                                }
                            }

                            if (!pesq.equals("")) {
                                out.println("<li class=\"arrow\"><a href=\"estruturas.jsp?pag=" + pagprox + "\">&raquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"estruturas.jsp?pesquisa=" + pesq + "&pag=" + pagprox + "\">&raquo;</a></li>");
                            }

                        }
                    %>                    
                </ul>
            </div>
        </div>
    </article>
</section>
<%@include file="_footer.jsp"%>  