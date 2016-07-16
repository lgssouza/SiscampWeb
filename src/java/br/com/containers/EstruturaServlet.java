package br.com.containers;

import br.com.dao.EstruturaDAO;
import br.com.dao.VaoDAO;
import br.com.entidades.Estrutura;
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
 * @author Luiz Guilherme
 */
public class EstruturaServlet extends HttpServlet {

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
            EstruturaDAO dao = new EstruturaDAO();
            VaoDAO daoVao = new VaoDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    daoVao.atualizaDistancias();
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "estruturas", "estruturas.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "estruturas", "estruturas.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EstruturaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            Estrutura estruturas = new Estrutura();
            estruturas.setNumero(request.getParameter("numero").toUpperCase());
            estruturas.setModelo(request.getParameter("modelo").toUpperCase());
            estruturas.setTipo(request.getParameter("tipo").toUpperCase());
            estruturas.setAltura(request.getParameter("altura").toUpperCase());

            if (estruturas.getNumero() == null || estruturas.getNumero().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Número é obrigatório!", "estruturas", estruturas, "estruturas_form.jsp");
                return;
            }

            if (estruturas.getModelo() == null || estruturas.getModelo().equals("")) {
                estruturas.setModelo("-");
            }

            if (estruturas.getTipo() == null || estruturas.getTipo().equals("")) {
                estruturas.setTipo("-");
            }

            if (estruturas.getAltura() == null || estruturas.getAltura().equals("")) {
                estruturas.setAltura("-");
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            EstruturaDAO dao = new EstruturaDAO();

            if (!dao.existe(estruturas.getNumero(), Linha.idLinha, id)) {
                if (id != 0) {
                    dao.atualizar(estruturas, id);
                    MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "estruturas", "estruturas.jsp");
                } else {
                    dao.inserir(estruturas);
                    MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "estruturas", "estruturas.jsp");
                }
            } else {
                MetodosUtil.validaJspErro(request, response, "Estrutura Existente!", "estruturas", estruturas, "estruturas_form.jsp");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EstruturaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
