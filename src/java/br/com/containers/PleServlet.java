package br.com.containers;

import br.com.dao.PleDAO;
import br.com.entidades.Ple;
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
public class PleServlet extends HttpServlet {

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
            PleDAO dao = new PleDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "ples", "ples.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "ples", "ples.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PleServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            Ple ple = new Ple();
            ple.setIdPle(Integer.parseInt(request.getParameter("numero")));
            ple.setSupervisor(request.getParameter("supervisor").toUpperCase());
            ple.setData(request.getParameter("data"));
            ple.setHrInicial(request.getParameter("hrinicial"));
            ple.setHrFinal(request.getParameter("hrfinal"));
            ple.setIdLinhaDist(Integer.parseInt(request.getParameter("linha")));

            if (ple.getSupervisor() == null || ple.getSupervisor().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Supervisor é obrigatório!", "ples", ple, "ples_form.jsp");
                return;
            }

            if (ple.getIdLinhaDist() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Linha de Transmissão é obrigatório!", "ples", ple, "ples_form.jsp");
                return;
            }

            if (ple.getData() == null || ple.getData().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Data é obrigatório!", "ples", ple, "ples_form.jsp");
                return;
            }

            if (MetodosUtil.validaData(ple.getData())) {
                ple.setData(MetodosUtil.formataDataGravar(ple.getData()));
            } else {
                MetodosUtil.validaJspErro(request, response, "Campo Data Preenchido Errado!", "ples", ple, "ples_form.jsp");
                return;
            }

            if (!MetodosUtil.validaHora(ple.getHrInicial())) {
                MetodosUtil.validaJspErro(request, response, "Campo Hora Inicial Preenchido Errado!", "ples", ple, "ples_form.jsp");
                return;
            }

            if (!MetodosUtil.validaHora(ple.getHrFinal())) {
                MetodosUtil.validaJspErro(request, response, "Campo Hora Final Preenchido Errado!", "ples", ple, "ples_form.jsp");
                return;
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            PleDAO dao = new PleDAO();

            if (id != 0) {
                dao.atualizar(ple, id);
                MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "ples", "ples.jsp");
            } else {
                dao.inserir(ple);
                MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "ples", "ples.jsp");
            }

        } catch (ClassNotFoundException | SQLException | ParseException ex) {
            Logger.getLogger(PleServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
