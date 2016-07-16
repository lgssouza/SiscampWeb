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
                visando propor soluções para as Equipes de Linhas da CEMIG.
                Tal ferramenta permitirá aos técnicos das equipes informações precisas, 
                em tempo hábil sobre todos os aspectos pertinentes ao trabalho. 
                Através da ferramenta será possível a verificação da programação de serviços, 
                as Ordens de Manutenção abertas, informações das linhas e outras informações relativas às equipes.
                A solução visa implementar o processo aumentando de maneira considerável a integridade,
                a confiabilidade e principalmente a agilidade das informações.
            </h5>
        </div>
    </article>
</section>
<%@include file="_footer.jsp" %>

