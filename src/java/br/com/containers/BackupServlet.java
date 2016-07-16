package br.com.containers;

import br.com.utilitarios.MetodosUtil;
import br.com.utilitarios.MySQLBackup;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luiz Guilherme
 */
public class BackupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        MySQLBackup bck = new MySQLBackup();
        if (bck.fazerBackup()) {
            MetodosUtil.validaJspSucesso(request, response, "Backup Realizado com Sucesso!", "backup", "home.jsp");
        } else {
            MetodosUtil.validaJspSucesso(request, response, "Falha na realização do Backup, contate o Suporte!", "backup", "home.jsp");
        }
    }

}
