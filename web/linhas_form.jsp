<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%
    LinhaDAO dao = new LinhaDAO();
    Linha linha = new Linha();
    if (session.getAttribute("linhas") != null && session.getAttribute("linhas") != null) {
        linha = (Linha) session.getAttribute("linhas");
        session.setAttribute("linhas", null);
    } else if (request.getParameter("id") != null) {
        linha = dao.selecionaLinha(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("linhas", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="linhas.jsp">Linhas de Transmissão</a></li>
    <li class="current"><a href="#">Cadastro de Linha de Transmissão</a></li>
</ul>

<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Linha de Transmissão</h2>
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

        <form name="cadLinhas" method="post" action="LinhaServlet"  data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-12 columns">
                            <label>Nomenclatura *
                                <input type="text" name="nomenclatura" placeholder="Digite o Nome Completo" value="<%  if (linha.getNomenclatura() != null) {
                                        out.print(linha.getNomenclatura());
                                    } %>" maxlength="50" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                    </div>
                    <div class="row">
                        <div class="large-4 columns">
                            <label>Cabo para Raio
                                <input type="text" name="cabopraio"  placeholder="Digite o Cabo para Raio" value="<% if (linha.getCaboRaio() != null) {
                                        out.print(linha.getCaboRaio());
                                    } %>" maxlength="10"/>
                            </label>                            
                        </div>                        
                        <div class="large-4 columns">
                            <label>Cabo Condutor
                                <input type="text" name="cabocond"  placeholder="Digite o Cabo Condutor" value="<% if (linha.getCaboCond() != null) {
                                        out.print(linha.getCaboCond());
                                    } %>" maxlength="15"/>
                            </label>                            
                        </div>                        
                        <div class="large-4 columns">
                            <label>Largura da Faixa *
                                <input type="text" name="largfaixa" pattern="[0-9]+$" placeholder="Digite a Largura da Faixa" value="<% if (linha.getLargFaixa() != 0) {
                                        out.print(linha.getLargFaixa());
                                    }%>" maxlength="11" required/>
                            </label>
                            <small class="error">Somente valores númericos</small>
                        </div>                                            
                    </div>                                        
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="linhas.jsp" class="button secondary">Cancelar</a>
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