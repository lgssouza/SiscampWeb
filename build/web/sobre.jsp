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
                Com o avan�o da internet e dos dispositivos m�veis, as empresas est�o escolhendo por introduzir 
                as aplica��es m�veis em seus ambientes de neg�cios, trazendo agilidade e qualidade nas informa��es. 
                As Equipes de Linhas da CEMIG est�o fora deste contexto, mesmo necessitando de v�rias informa��es no campo,
                em tempo real, para tomada de decis�es. O objetivo deste trabalho � o desenvolvimento de um software
                para smartphones, na plataforma Android, para consultas destas informa��es, via internet.
                Neste contexto de aplica��es diferentes, surge a necessidade de uma tecnologia que possa fazer a 
                integra��o entre estes componentes. A arquitetura Web Service surge como a solu��o para esta integra��o
                e comunica��o entre software mobile e o banco de dados. 
            </h5>
        </div>
    </article>
</section>
<%@include file="_footer.jsp" %>

