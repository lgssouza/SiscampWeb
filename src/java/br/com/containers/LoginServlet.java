package br.com.containers;

import br.com.dao.FuncionarioDAO;
import br.com.entidades.LoginSessao;
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
 * @author Luiz Guilherme Souza
 */
public class LoginServlet extends HttpServlet {

    private FuncionarioDAO dao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        LoginSessao usuario = new LoginSessao();
        usuario.setUsuario(request.getParameter("nomeusuario"));
        usuario.setSenha(request.getParameter("senhausuario"));

        try {
            try {
                dao = new FuncionarioDAO();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            LoginSessao logado;
            logado = dao.validarLogin(usuario);
            if (logado != null) {
                HttpSession session = request.getSession();

                session.setAttribute("uname", logado.getUsuario());
                session.setAttribute("uid", logado.getMatricula());
                if (logado.getSenha().equals(String.valueOf(logado.getMatricula()))) {
                    session.setAttribute("primeiroacesso", "sim");
                } else {
                    session.setAttribute("primeiroacesso", "nao");
                }
                response.sendRedirect("home.jsp");
            } else {
                MetodosUtil.validaJspErro(request, response, "Usuário/Senha Incorretos!", "pessoa", "", "index.jsp");
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
