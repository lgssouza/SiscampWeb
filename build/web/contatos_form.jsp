<%@page import="br.com.entidades.Contato"%>
<%@page import="br.com.dao.ContatoDAO"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%
    ContatoDAO dao = new ContatoDAO();
    Contato contato = new Contato();
    if (session.getAttribute("contatos") != null && session.getAttribute("contatos") != null) {
        contato = (Contato) session.getAttribute("contatos");
        session.setAttribute("contatos", null);
    } else if (request.getParameter("id") != null) {
        contato = dao.selecionaContato(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("contatos", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="contatos.jsp">Contatos</a></li>
    <li class="current"><a href="#">Cadastro de Contatos</a></li>
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
        <script type="text/javascript">
            $(function () {
                $("#phone").mask("(99) 9?99999999");
                $("#phone2").mask("(99) 9?99999999");
                $("input").blur(function () {
                    $("#info").html("Unmasked value: " + $(this).mask());
                }).dblclick(function () {
                    $(this).unmask();
                });
            });

        </script>
        <form name="cadContatos" method="post" action="ContatoServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-12 columns">
                            <label>Nome *
                                <input type="text" name="nome" placeholder="Digite o Nome Completo" value="<%  if (contato.getNome() != null) {
                                        out.print(contato.getNome());
                                    } %>" maxlength="50" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                    </div>
                    <div class="row">
                        <div class="large-6 columns">
                            <label>Especialidade *
                                <input type="text" name="espec" placeholder="Digite a Especialidade" value="<%  if (contato.getEspecialidade() != null) {
                                        out.print(contato.getEspecialidade());
                                    } %>" maxlength="100" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>        
                        <div class="large-3 columns">
                            <label>Telefone
                                <input id="phone" type="tel" name="tel1" value="<% if (contato.getTel1() != null) {
                                        out.print(contato.getTel1());
                                    } %>" placeholder="(99) 99999-9999" />
                            </label>                            
                        </div>                        
                        <div class="large-3 columns">
                            <label>Telefone Alternativo
                                <input id="phone2" type="tel" name="tel2" value="<% if (contato.getTel2() != null) {
                                        out.print(contato.getTel2());
                                    } %>" placeholder="(99) 99999-9999" />
                            </label>
                            <small class="error">Informe um telefone válido</small>
                        </div>            

                    </div>  
                    <div class="row">
                        <div class="large-4 columns">
                            <label>Cidade
                                <input type="text" name="cidade" placeholder="Digite a Cidade" value="<% if (contato.getCidade() != null) {
                                        out.print(contato.getCidade());
                                    }%>" maxlength="60" />
                            </label>                            
                        </div>        
                        <div class="large-8 columns">
                            <label>Endereço
                                <input type="text" name="end" placeholder="Digite o Endereço" value="<% if (contato.getEnd() != null) {
                                        out.print(contato.getEnd());
                                    }%>" maxlength="100" />
                            </label>                            
                        </div>   
                    </div>
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="contatos.jsp" class="button secondary">Cancelar</a>
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