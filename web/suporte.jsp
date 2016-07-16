<%@include file="_header.jsp"%>    

<ul class="breadcrumbs">
    <li><a href="home.jsp">Home</a></li>
    <li class="current"><a href="sobre.jsp">Sobre</a></li>
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

        </div>                     
        <div>
            
            <img src="Foundation/img/contatos.jpg" style="max-width:90%; max-height:90%;">
            
            <h3>
                TCC - Sistemas de Informação
            </h3>
            <h5>
                Luiz Guilherme Santos de Souza
                <br>
                Tel: (35) 99907-4729
                <br>
                e-mail: lguilhermesouza93@gmail.com
                <br>                
                <br>
                Sandro Luis Rodrigues
                <br>
                Tel: (35) 99110-5432
                <br>
                e-mail: sandroluis0308@yahoo.com.br 
                <br>
                <br>
                Orientador: Thiago Giovanella
                <br>
                Faculdade: Faculdade Cenecista de Varginha - FACECA
            </h5>


        </div>
    </article>
</section>
<%@include file="_footer.jsp" %>

