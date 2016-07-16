<%@page import="br.com.utilitarios.MetodosUtil"%>
<%@page import="br.com.dao.SobreavisoDAO"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%@include file="_header.jsp"%>  

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li class="current"><a href="sobreavisos.jsp">Sobreavisos</a></li>
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

    SobreavisoDAO dao = new SobreavisoDAO();
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
                <h2>Sobreavisos
                    <a href="sobreavisos_form.jsp" class="button secondary small right">+ Novo</a>
                </h2>
            </div>
        </div>
        <div class="row">
            <div class="large-12 columns">
                <form class="row collapse" method="get" action="sobreavisos.jsp">
                    <div class="small-8 columns">
                        <input type="text" name="pesquisa" placeholder="Digite sua Pesquisa aqui">
                    </div>
                    <div class="small-2 columns">
                        <button class="button postfix" type="submit">Filtrar</button>
                    </div>
                    <div class="large-2 columns">
                        <select name="filtro">
                            <option value="func">Funcionário</option>
                            <option value="data">Data</option>                                
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
                            <th>Nome</th>
                            <th style="text-align: center; width:150px;">Data</th>
                            <th style="text-align: center; width:150px;">Telefone</th>                            
                            <th style="width:80px;">Editar</th>
                            <th style="width:80px;">Excluir</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%                            //grid
                            ResultSet rs = dao.listarSobreavisos(pesq, pagatual, filtro);

                            rs.previous();

                            while (rs.next()) {
                                out.println("<tr>");
                                out.println("<td>" + rs.getString("func_nome") + "</td>");
                                out.println("<td style=\"text-align:center\">" + MetodosUtil.formataDataExibir(rs.getDate("sobreaviso_data")) + "</td>");
                                out.println("<td style=\"text-align:center\">" + rs.getString("func_telpessoal") + "</td>");
                                out.println("<td style=\"text-align:center\" onclick=\"window.location='sobreavisos_form.jsp?id=" + rs.getInt("id_sobreaviso") + "'\"><img src=\"Foundation/img/editar.png\"></td>");
                                out.println("<td class=\"text-center\"><a href=\"SobreavisoServlet?id=" + rs.getInt("id_sobreaviso") + "&excluir=sim\" class=\"remove\" onclick=\"return confirm('Confirma a exclusão?')\"><img src=\"Foundation/img/excluir.png\"></a></td>");
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
                                out.println("<li class=\"arrow\"><a href=\"sobreavisos.jsp?pag=" + pagant + "\">&laquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"sobreavisos.jsp?pesquisa=" + pesq + "&pag=" + pagant + "\">&laquo;</a></li>");
                            }
                            for (int i = 1; i <= totpag; i++) {
                                if (!pesq.equals("")) {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"sobreavisos.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"sobreavisos.jsp?pesquisa=" + pesq + "&pag=" + i + "\">" + i + "</a></li>");
                                    }
                                } else {
                                    if (pagatual == i) {
                                        out.println("<li class=\"current\"><a href=\"sobreavisos.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    } else {
                                        out.println("<li><a href=\"sobreavisos.jsp?pag=" + i + "\">" + i + "</a></li>");
                                    }
                                }
                            }

                            if (!pesq.equals("")) {
                                out.println("<li class=\"arrow\"><a href=\"sobreavisos.jsp?pag=" + pagprox + "\">&raquo;</a></li>");
                            } else {
                                out.println("<li class=\"arrow\"><a href=\"sobreavisos.jsp?pesquisa=" + pesq + "&pag=" + pagprox + "\">&raquo;</a></li>");
                            }

                        }
                    %>                    
                </ul>
            </div>
        </div>
    </article>
</section>
<%@include file="_footer.jsp"%>  