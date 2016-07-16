package br.com.containers;

import br.com.dao.LinhaDAO;
import br.com.entidades.Linha;
import br.com.utilitarios.MetodosUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luiz Guilherme Souza
 */
public class LinhaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = 0;
            String sim = null;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }
            if (request.getParameter("excluir") != null && !"".equals(request.getParameter("excluir"))) {
                sim = request.getParameter("excluir");
            }
            LinhaDAO dao = new LinhaDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "linhas", "linhas.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "linhas", "linhas.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LinhaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            Linha linhas = new Linha();
            linhas.setNomenclatura(request.getParameter("nomenclatura").toUpperCase());
            linhas.setCaboRaio(request.getParameter("cabopraio").toUpperCase());
            linhas.setCaboCond(request.getParameter("cabocond").toUpperCase());
            linhas.setLargFaixa(Integer.parseInt(request.getParameter("largfaixa").toUpperCase()));

            if (linhas.getNomenclatura() == null || linhas.getNomenclatura().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Nomenclatura é obrigatório!", "linhas", linhas, "linhas_form.jsp");
                return;
            }

            if (linhas.getCaboRaio() == null || linhas.getCaboRaio().equals("")) {
                linhas.setCaboRaio("-");
            }

            if (linhas.getCaboCond() == null || linhas.getCaboCond().equals("")) {
                linhas.setCaboCond("-");
            }

            if (linhas.getLargFaixa() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Largura da Faixa é obrigatório!", "linhas", linhas, "linhas_form.jsp");
                return;
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            LinhaDAO dao = dao = new LinhaDAO();

            if (!dao.existeLinha(linhas.getNomenclatura(), id)) {
                if (id != 0) {
                    dao.atualizar(linhas, id);
                    MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "linhas", "linhas.jsp");
                } else {
                    dao.inserir(linhas);
                    MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "linhas", "linhas.jsp");
                }
            } else {
                MetodosUtil.validaJspErro(request, response, "Linha Existente!", "linhas", linhas, "linhas_form.jsp");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LinhaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
