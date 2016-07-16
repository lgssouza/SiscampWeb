package br.com.containers;

import br.com.dao.VaoDAO;
import br.com.entidades.Vao;
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
public class VaoServlet extends HttpServlet {

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
            VaoDAO dao = new VaoDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    dao.atualizaDistancias();
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "estruturas", "estruturas.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "estruturas", "estruturas.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VaoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            Vao vao = new Vao();
            vao.setIdEstInicial(Integer.parseInt(request.getParameter("estinicial")));
            vao.setIdEstFinal(Integer.parseInt(request.getParameter("estfinal")));

            if (request.getParameter("distancia").contains(",")) {
                vao.setDistancia(Float.parseFloat(request.getParameter("distancia").replace(",", ".")));
            } else {
                vao.setDistancia(Float.parseFloat(request.getParameter("distancia")));
            }
            vao.setSequencia(Integer.parseInt(request.getParameter("sequencia")));

            if (vao.getIdEstInicial() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Estrutura Inicial é obrigatório!", "vaos", vao, "vaos_form.jsp");
                return;
            }

            if (vao.getIdEstFinal() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Estrutura Final é obrigatório!", "vaos", vao, "vaos_form.jsp");
                return;
            }

            if (vao.getDistancia() == null) {
                MetodosUtil.validaJspErro(request, response, "Campo Distância é obrigatório!", "vaos", vao, "vaos_form.jsp");
                return;
            }

            if (vao.getSequencia() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Sequência é obrigatório!", "vaos", vao, "vaos_form.jsp");
                return;
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            VaoDAO dao = new VaoDAO();

            if (!dao.existe(vao.getIdEstInicial(), vao.getIdEstFinal(), id)) {
                if (id != 0) {
                    dao.atualizar(vao, id);
                    dao.atualizaDistancias();
                    MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "vaos", "vaos.jsp");
                } else {
                    dao.inserir(vao);
                    dao.atualizaDistancias();
                    MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "vaos", "vaos.jsp");
                }
            } else {
                MetodosUtil.validaJspErro(request, response, "Vão Existente!", "vaos", vao, "vaos_form.jsp");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(VaoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
