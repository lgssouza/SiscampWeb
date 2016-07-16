<%@page import="br.com.utilitarios.MetodosUtil"%>
<%@page import="br.com.entidades.Funcionario"%>
<%@page import="br.com.dao.FuncionarioDAO"%>
<%@page import="br.com.entidades.Linha"%>
<%@page import="br.com.dao.LinhaDAO"%>
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

        <%            //
            String msg = (String) session.getAttribute("erro");
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

        <form name="cadSenha" method="post" action="SenhaServlet" data-abide>            
            <input name="usuario" value="<% out.print(login);%>" type="hidden">
            <div class="row">
                <div class="small-6 columns">
                    <div class="row">
                        <div class="small-6 columns">
                            <label>Senha Antiga *
                                <input type="password" name="senhaold" placeholder="Digite a Senha Antiga" maxlength="50" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                    </div>
                    <div class="row">
                        <div class="small-6 columns">
                            <label>Senha Nova *
                                <input type="password" name="senhanew1" placeholder="Digite a Senha Nova" maxlength="50" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                    </div>
                    <div class="row">
                        <div class="small-6 columns">
                            <label>Repita Senha Nova *
                                <input type="password" name="senhanew2" placeholder="Repita a Senha Nova" maxlength="50" required />
                            </label>
                            <small class="error">Obrigatório</small>
                        </div>                        
                    </div>
                </div>  
                <div class="row">
                    <div class="small-12 columns text-left">
                        <a href="home.jsp" class="button secondary">Cancelar</a>
                        <button type="submit" class="button success">Salvar</button>
                    </div>
                </div>                    
            </div>
            </div>
        </form>
    </article>
</section>                                        
<script src="Foundation/js/custom/select-ajax.js"></script>
<script>
    
    function validarSenha() {
        $('#senhanew1').val()
        $('#senhanew2').val()
        
        if (senhanew1 == senhanew2)
            alert("SENHAS IGUAIS")
        else
            alert("SENHAS DIFERENTES")
    }
</script>
<%@include file="_footer.jsp"%>  