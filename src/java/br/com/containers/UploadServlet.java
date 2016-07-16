/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.containers;

import br.com.dao.PlanoDAO;
import br.com.entidades.Constantes;
import br.com.entidades.Plano;
import br.com.utilitarios.MetodosUtil;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
            PlanoDAO dao = new PlanoDAO();
            if (sim.equals("sim")) {
                if (dao.excluir(id)) {
                    MetodosUtil.validaJspSucesso(request, response, "Excluído com Sucesso!", "planos", "planos.jsp");
                } else {
                    MetodosUtil.validaJspSucesso(request, response, "Não foi possível excluir!", "planos", "planos.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Plano plano = new Plano();
        int id = 0;
        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            return;
        }
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> fields = upload.parseRequest(request);

            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) {
                return;
            }

            while (it.hasNext()) {
                FileItem fileItem = it.next();
                boolean isFormField = fileItem.isFormField();

                if (isFormField) {
                    switch (fileItem.getFieldName()) {
                        case "id":
                            if (!"0".equals(fileItem.getString())) {
                                id = Integer.parseInt(fileItem.getString());
                            }
                            break;
                        case "linha":
                            plano.setIdLinha(Integer.parseInt(fileItem.getString()));
                            break;
                        case "versao":
                            plano.setVersao(Integer.parseInt(fileItem.getString()));
                            break;
                    }
                } else {
                    File diretorio = new File(Constantes.CAMINHO_AQUIVOS);
                    if (!diretorio.exists()) {
                        diretorio.mkdirs();
                    }

                    File saveTo = new File(Constantes.CAMINHO_AQUIVOS + fileItem.getName());
                    fileItem.write(saveTo);
//                    plano.setArquivo(Constantes.CAMINHO_AQUIVOS + fileItem.getName());
                    plano.setNome(fileItem.getName());
                }
            }
            PlanoDAO dao = new PlanoDAO();
            if (id == 0) {
                if (!dao.existePlano(plano.getIdLinha())) {
                    dao.insertPDF(plano);
                    MetodosUtil.validaJspSucesso(request, response, "Cadastrado com Sucesso!", "planos", "planos.jsp");
                } else {
                    MetodosUtil.validaJspErro(request, response, "Já existe um Plano para Esta Linha!", "planos", plano, "planos_form.jsp");
                }
            } else {
                dao.updatePDF(plano, id);
                MetodosUtil.validaJspSucesso(request, response, "Atualizado com Sucesso!", "planos", "planos.jsp");
            }

        } catch (FileUploadException e) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
