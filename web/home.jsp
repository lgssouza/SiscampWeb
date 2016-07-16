<%@include file="_header.jsp"%>    

<ul class="breadcrumbs">    	
    <li class="current"><a href="#">Home</a></li>
</ul>

<%        String msg = (String) session.getAttribute("sucesso");

    if (msg != "" && msg != null) {
        out.println("<div class=row>");
        out.println("<div data-alert class=\"alert-box success\">");
        out.println("" + msg + "");
        out.println("<a href=\"error\" class=\"close\">&times;</a>");
        out.println("</div>");
        out.println("</div>");
    }
    session.setAttribute("sucesso", null);
  
%>

<section class="row">
    <%@include file="_aside.jsp"%>

    <article class="columns large-9 medium-8">
        <div class="row">
            <div class="small-12 columns">
                <h2>Siscamp - Web</h2>
            </div>
        </div>                     
        <div>
            <h5>
                Trata-se de um projeto de desenvolvimento de software para smartphone, 
                visando propor solu��es para as Equipes de Linhas da CEMIG.
                Tal ferramenta permitir� aos t�cnicos das equipes informa��es precisas, 
                em tempo h�bil sobre todos os aspectos pertinentes ao trabalho. 
                Atrav�s da ferramenta ser� poss�vel a verifica��o da programa��o de servi�os, 
                as Ordens de Manuten��o abertas, informa��es das linhas e outras informa��es relativas �s equipes.
                A solu��o visa implementar o processo aumentando de maneira consider�vel a integridade,
                a confiabilidade e principalmente a agilidade das informa��es.
            </h5>
        </div>
    </article>
</section>
<%@include file="_footer.jsp" %>

