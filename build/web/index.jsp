<%-- 
    Document   : index
    Created on : 25/03/2015, 20:19:59
    Author     : Luiz Guilherme Souza
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <link href="Foudation/img/favicon.ico" rel="shortcut icon" type="image/x-icon">    
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    
    <link rel="stylesheet" href="Foundation/css/foundation.css" />    
    <link rel="stylesheet" href="Foundation/css/foundation-icons/foundation-icons.css" />
    <script src="Foundation/js/vendor/modernizr.js"></script>    
    
    <title>Login | Siscamp WEB - Consultas de Linhas de Transmissão</title>
    
</head>

<body>

	<div class="row">
    	<div class="large-5 medium-7 small-12 small-centered columns panel" style="margin-bottom:0; background-color:#bbb ">
        
        	<div class="row">
                    <div class="large-12 columns text-center">
                            <img src="Foundation/img/logo-cemig.png" style="max-width:250px;">
                    </div>
                </div>
            <div class="row" style="margin:20px 0">
                <div class="large-12 columns">
                    <h2 class="text-center">
                    	Siscamp WEB
                    </h2>
                </div>
            </div>
            <%              
                String msg =(String)session.getAttribute("erro");
                if (msg!="" && msg!=null){
                    out.println("<div class=\"row\">");            
                    out.println("<div data-alert class=\"alert-box alert\">");
                    out.println(""+msg+"");                
                    out.println("<a href=\"error\" class=\"close\">&times;</a>");
                    out.println("</div>");
                    out.println("</div>");
                }                    
                session.setAttribute("erro",null);
            %>
            <form method="post" action="LoginServlet" data-abide>
                
                <div class="row">
                    <div class="large-12 columns">
                        <label>
                            Usuário:
                            <input type="text" name="nomeusuario" placeholder="Informe seu usuário" required />
                        </label>
                        <small class="error">Informe o usuário</small>
                    </div>
                </div>
                <div class="row">
                    <div class="large-12 columns">
                        <label>
                            Senha:
                            <input type="password" name="senhausuario" placeholder="digite sua senha" required />
                        </label>
                        <small class="error">Informe a senha</small>
                    </div>
                </div>
                <div class="row">
                    <div class="large-12 columns">
                    	<button class="button right">Entrar</button>
                    </div>
                </div>
                <div> 
                    <script>
                        function alertar{
                            alert("Senha/Usuário Errados!");
                        }
                    </script>
                </div>
            </form>
        </div>
    </div>
	
    
    <script src="Foundation/js/vendor/jquery.js"></script>
    <script src="Foundation/js/foundation.min.js"></script>
    <script>
      $(document).foundation();
    </script>
</body>
</html>

