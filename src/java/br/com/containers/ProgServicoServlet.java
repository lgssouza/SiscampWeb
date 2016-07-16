package br.com.containers;

import br.com.dao.ProgServicoDAO;
import br.com.entidades.ProgServico;
import br.com.utilitarios.MetodosUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
public class ProgServicoServlet extends HttpServlet {

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
            ProgServicoDAO dao = new ProgServicoDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "servicos", "servicos.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "servicos", "servicos.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProgServicoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            ProgServico progs = new ProgServico();
            progs.setIdProgServico(Long.parseLong(request.getParameter("om")));
            progs.setIdLinhaDist(Integer.parseInt(request.getParameter("linha")));
            progs.setDescServico(request.getParameter("descricao"));
            progs.setSemanaExec(Integer.parseInt(request.getParameter("semana")));
            progs.setDataExec(request.getParameter("data"));
            progs.setQtdPessoas(Integer.parseInt(request.getParameter("qtdpessoas")));
            if (request.getParameter("ple").equals("N/A PLE")) {
                progs.setIdPle(0);
            } else {
                progs.setIdPle(Integer.parseInt(request.getParameter("ple")));
            }

            if (progs.getDescServico() == null || progs.getDescServico().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Descrição do Serviço é obrigatório!", "servicos", progs, "servicos_form.jsp");
                return;
            }

            if (progs.getIdLinhaDist() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Linha de Transmissão é obrigatório!", "servicos", progs, "servicos_form.jsp");
                return;
            }

            if (progs.getIdProgServico() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo OM é obrigatório!", "servicos", progs, "servicos_form.jsp");
                return;
            }

            if (progs.getDataExec() == null || progs.getDataExec().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Data é obrigatório!", "servicos", progs, "servicos_form.jsp");
                return;
            }

            if (MetodosUtil.validaData(progs.getDataExec())) {
                progs.setDataExec(MetodosUtil.formataDataGravar(progs.getDataExec()));
            } else {
                MetodosUtil.validaJspErro(request, response, "Campo Data Preenchido Errado!", "servicos", progs, "servicos_form.jsp");
                return;
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            ProgServicoDAO dao = new ProgServicoDAO();
            if (id != 0) {
                dao.atualizar(progs, id);
                MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "servicos", "servicos.jsp");
            } else {
                dao.inserir(progs);
                MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "servicos", "servicos.jsp");
            }

        } catch (ClassNotFoundException | SQLException | ParseException ex) {
            Logger.getLogger(ProgServicoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
