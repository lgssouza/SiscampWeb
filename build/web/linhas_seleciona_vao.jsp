<%@page import="br.com.entidades.Linha"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%@include file="_header.jsp"%>  

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li class="current"><a href="linhas_seleciona_vao.jsp">Seleciona Linha</a></li>
</ul>

<%    Linha linha = new Linha();
    LinhaDAO daoLinha = new LinhaDAO();
%>

<section class="row">
    <%@include file="_aside.jsp"%>        
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Selecione a Linha de Transmissão</h2>
            </div>
        </div>
        <div class="row">
            <div class="large-12 columns">
                <form class="row collapse" method="post" action="vaos.jsp">
                    <label>Linhas de Transmissão                                                                    
                        <select name="linha">                                                   
                            <%
                                ResultSet rs = daoLinha.listarLinhasCombo();
                                rs.previous();
                                while (rs.next()) {
                                    out.println("<option value=" + rs.getString("id_linhadist") + ">" + rs.getString("linhadist_nomenclatura") + "</option>");
                                }

                                rs.close();
                            %>                                        
                        </select>
                    </label>         
                    <div class="small-2 columns">
                        <button class="button postfix" type="submit">Seleciona</button>
                    </div>
                </form>
            </div>
        </div>        
    </article>
</section>
<%@include file="_footer.jsp"%>  