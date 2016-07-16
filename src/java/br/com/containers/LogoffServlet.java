package br.com.containers;

import br.com.dao.FuncionarioDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Luiz Guilherme Souza
 */
public class LogoffServlet extends HttpServlet {

    private FuncionarioDAO dao;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        session.invalidate();        
        request.getRequestDispatcher("index.jsp").forward(request, response);
        

    }
}
