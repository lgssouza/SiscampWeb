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
                <h2>Backup</h2>
            </div>
        </div>

        <%            String msg = (String) session.getAttribute("erro");
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

        <form name="backup" method="post" action="BackupServlet" data-abide>                        
            <div class="row">
                <div class="small-12 columns">
                    <div class="small-12 columns">
                        <h3>Selecione o caminho que deseja salvar o arquivo de backup.</h2>
                            <input type="file" multiple webkitdirectory id="pasta"/>
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