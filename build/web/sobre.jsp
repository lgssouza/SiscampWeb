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
            <div class="small-12 columns">
                <h2>Siscamp - Web</h2>
            </div>
        </div>                     
        <div>
            <h5>
                Com o avanço da internet e dos dispositivos móveis, as empresas estão escolhendo por introduzir 
                as aplicações móveis em seus ambientes de negócios, trazendo agilidade e qualidade nas informações. 
                As Equipes de Linhas da CEMIG estão fora deste contexto, mesmo necessitando de várias informações no campo,
                em tempo real, para tomada de decisões. O objetivo deste trabalho é o desenvolvimento de um software
                para smartphones, na plataforma Android, para consultas destas informações, via internet.
                Neste contexto de aplicações diferentes, surge a necessidade de uma tecnologia que possa fazer a 
                integração entre estes componentes. A arquitetura Web Service surge como a solução para esta integração
                e comunicação entre software mobile e o banco de dados. 
            </h5>
        </div>
    </article>
</section>
<%@include file="_footer.jsp" %>

