package br.com.containers;

import br.com.dao.FuncionarioDAO;
import br.com.entidades.Funcionario;
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
public class FuncionarioServlet extends HttpServlet {

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
            FuncionarioDAO dao = new FuncionarioDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "funcionarios", "funcionarios.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "funcionarios", "funcionarios.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            Funcionario func = new Funcionario();
            func.setNome(request.getParameter("nome").toUpperCase());
            func.setMatricula(Integer.parseInt(request.getParameter("matricula")));
            func.setTel(request.getParameter("tel").toUpperCase());
            func.setUsuario(request.getParameter("usuario"));
            func.setSenha(String.valueOf(func.getMatricula()));

            if (func.getNome() == null || func.getNome().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Nome é obrigatório!", "contatos", func, "contatos_form.jsp");
                return;
            }

            if (func.getMatricula() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Matrícula é obrigatório!", "contatos", func, "contatos_form.jsp");
                return;
            }

            if (func.getTel() == null || func.getTel().equals("")) {
                func.setTel("N/A");
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            FuncionarioDAO dao = new FuncionarioDAO();

            if (!dao.existeFuncionarioNome(func.getNome(), id)) {
                if (!dao.existeFuncionarioUsuario(func.getUsuario(), id)) {
                    if (id != 0) {
                        dao.atualizar(func, id);
                        MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "funcionarios", "funcionarios.jsp");
                    } else {
                        dao.inserir(func);
                        MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "funcionarios", "funcionarios.jsp");
                    }
                } else {                    
                        MetodosUtil.validaJspErro(request, response, "Usuário Existente!", "funcionarios", func, "funcionarios_form.jsp");                   
                }
            } else {
                MetodosUtil.validaJspErro(request, response, "Nome Existente!", "funcionarios", func, "funcionarios_form.jsp");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FuncionarioServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
