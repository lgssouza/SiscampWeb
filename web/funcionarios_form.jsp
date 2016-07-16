<%@page import="br.com.entidades.Funcionario"%>
<%@page import="br.com.dao.FuncionarioDAO"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>
<%
    FuncionarioDAO dao = new FuncionarioDAO();
    Funcionario func = new Funcionario();
    if (session.getAttribute("funcionarios") != null && session.getAttribute("funcionarios") != null) {
        func = (Funcionario) session.getAttribute("funcionarios");
        session.setAttribute("funcionarios", null);
    } else if (request.getParameter("id") != null) {
        func = dao.selecionaFuncionario(Integer.parseInt(request.getParameter("id")));
        session.setAttribute("funcionarios", null);
    }

%>

<%@include file="_header.jsp"%>

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li><a href="funcionarios.jsp">Funcionários</a></li>
    <li class="current"><a href="#">Cadastro de Funcionário</a></li>
</ul>



<section class="row">
    <%@include file="_aside.jsp"%>
    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Cadastro de Funcionários</h2>
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
                $("input").blur(function () {
                    $("#info").html("Unmasked value: " + $(this).mask());
                }).dblclick(function () {
                    $(this).unmask();
                });
            });

        </script>
        <form name="cadFunc" method="post" action="FuncionarioServlet" data-abide>
            <input name="id" value="<%  if (request.getParameter("id") != null) {
                    out.print(request.getParameter("id"));
                } %>" type="hidden">
            <div class="row">
                <div class="small-12 columns">
                    <div class="row">
                        <div class="large-12 columns">
                            <label>Nome *
                                <input type="text" name="nome" placeholder="Digite o Nome Completo" value="<%  if (func.getNome() != null) {
                                        out.print(func.getNome());
                                    } %>" maxlength="50" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                    </div>
                    <div class="row">
                        <div class="large-4 columns">
                            <label>Matrícula *
                                <input type="text" name="matricula" placeholder="Digite a Matrícula" value="<%  if (func.getMatricula() != 0) {
                                        out.print(func.getMatricula());
                                    } %>" maxlength="100" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>        
                        <div class="large-4 columns">
                            <label>Telefone
                                <input id="phone" type="tel" name="tel" value="<% if (func.getTel() != null) {
                                        out.print(func.getTel());
                                    }%>" placeholder="(99) 99999-9999" />
                            </label>                            
                        </div>  
                        <div class="row">
                            <div class="large-4 columns">
                                <label>Usuário
                                    <input type="text" name="usuario" placeholder="Digite o Usuário" value="<% if (func.getUsuario() != null) {
                                            out.print(func.getUsuario());
                                        }%>" maxlength="45" required />
                                </label>            
                                <small class="error">Obrigatório</small>
                            </div>                                    
                        </div>
                    </div>  
                    <div class="row">
                        <div class="small-12 columns text-right">
                            <a href="funcionarios.jsp" class="button secondary">Cancelar</a>
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