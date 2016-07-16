package br.com.containers;

import br.com.dao.SobreavisoDAO;
import br.com.entidades.Sobreaviso;
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
public class SobreavisoServlet extends HttpServlet {

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
            SobreavisoDAO dao = new SobreavisoDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "sobreavisos", "sobreavisos.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "sobreavisos", "sobreavisos.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SobreavisoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            response.setContentType("text/html;charset=UTF-8");
            Sobreaviso sobreaviso = new Sobreaviso();
            sobreaviso.setFuncMatricula(Integer.parseInt(request.getParameter("func").toUpperCase()));
            sobreaviso.setData(request.getParameter("data"));

            if (sobreaviso.getFuncMatricula() == 0) {
                MetodosUtil.validaJspErro(request, response, "Campo Funcionário é obrigatório!", "sobreavisos", sobreaviso, "sobreavisos_form.jsp");
                return;
            }

            if (sobreaviso.getData() == null || sobreaviso.getData().equals("")) {
                MetodosUtil.validaJspErro(request, response, "Campo Data é obrigatório!", "sobreavisos", sobreaviso, "sobreavisos_form.jsp");
                return;
            }

            if (MetodosUtil.validaData(sobreaviso.getData())) {
                sobreaviso.setData(MetodosUtil.formataDataGravar(sobreaviso.getData()));
            } else {
                MetodosUtil.validaJspErro(request, response, "Campo Data Preenchido Errado!", "sobreavisos", sobreaviso, "sobreavisos_form.jsp");
                return;
            }

            //id aqui serve para saber se estamos inserindo ou atualizando.
            int id = 0;
            if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            SobreavisoDAO dao = new SobreavisoDAO();

            if (id != 0) {
                dao.atualizar(sobreaviso, id);
                MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "sobreavisos", "sobreavisos.jsp");
            } else {
                dao.inserir(sobreaviso);
                MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "sobreavisos", "sobreavisos.jsp");
            }

        } catch (ClassNotFoundException | SQLException | ParseException ex) {
            Logger.getLogger(SobreavisoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
