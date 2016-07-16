package br.com.containers;

import br.com.dao.ContatoDAO;
import br.com.entidades.Contato;
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
 * @author Luiz Guilherme
 */
public class ContatoServlet extends HttpServlet {

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
            ContatoDAO dao = new ContatoDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "contatos", "contatos.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "contatos", "contatos.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ContatoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            Contato contato = new Contato();
            contato.setNome(request.getParameter("nome").toUpperCase());
            contato.setEspecialidade(request.getParameter("espec").toUpperCase());
            contato.setTel1(request.getParameter("tel1").toUpperCase());
            contato.setTel2(request.getParameter("tel2").toUpperCase());
            contato.setCidade(request.getParameter("cidade").toUpperCase());
            contato.setEnd(request.getParameter("end").toUpperCase());

            if (contato.getNome() == null || contato.getNome().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Nome é obrigatório!", "contatos", contato, "contatos_form.jsp");
                return;
            }

            if (contato.getEspecialidade() == null || contato.getEspecialidade().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Especialidade é obrigatório!", "contatos", contato, "contatos_form.jsp");
                return;
            }

            if (contato.getTel1() == null || contato.getTel1().equals("")) {
                contato.setTel1("N/A");
            }

            if (contato.getTel2() == null || contato.getTel2().equals("")) {
                contato.setTel2("N/A");
            }

            if (contato.getCidade() == null || contato.getCidade().equals("")) {
                contato.setCidade("N/A");
            }

            if (contato.getEnd() == null || contato.getEnd().equals("")) {
                contato.setEnd("N/A");
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            ContatoDAO dao = new ContatoDAO();

            if (!dao.existeContato(contato.getNome(), id)) {
                if (id != 0) {
                    dao.atualizar(contato, id);
                    MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "contatos", "contatos.jsp");
                } else {
                    dao.inserir(contato);
                    MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "contatos", "contatos.jsp");
                }
            } else {
                MetodosUtil.validaJspErro(request, response, "Contato Existente!", "contatos", contato, "contatos_form.jsp");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ContatoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
