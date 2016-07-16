package br.com.containers;

import br.com.dao.FuncionarioDAO;
import br.com.utilitarios.MetodosUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Luiz Guilherme
 */
public class SenhaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            String usuario = request.getParameter("usuario");
            String senhaold = request.getParameter("senhaold");
            String senhanew1 = request.getParameter("senhanew1");
            String senhanew2 = request.getParameter("senhanew2");

            if (!senhanew1.equals(senhanew2)) {
                HttpSession session = request.getSession();
                session.setAttribute("erro", "Senha Nova Não Confere!");
                response.sendRedirect("trocarsenha.jsp");
                return;
            }

            FuncionarioDAO dao = new FuncionarioDAO();
            if (!dao.verificaSenhaAntiga(senhaold, usuario)) {
                HttpSession session = request.getSession();
                session.setAttribute("erro", "Senha Antiga Não Confere!");
                response.sendRedirect("trocarsenha.jsp");
                return;
            }

            dao.atualizarSenha(usuario, senhanew1);
            MetodosUtil.validaJspSucesso(request, response, "Senha Alterada com Sucesso!", "trocasenha", "home.jsp");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SenhaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
