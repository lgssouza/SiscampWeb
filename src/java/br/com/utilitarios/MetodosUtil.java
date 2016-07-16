/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.utilitarios;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Luiz Guilherme Souza
 */
public class MetodosUtil {

    public static String onlyNumbers(String str) {
        if (str != null) {
            return str.replaceAll("[^0123456789]", "");
        } else {
            return "";
        }
    }

    public static void validaJspErro(HttpServletRequest request, HttpServletResponse response, String Msg,
            String Pagina, Object cadPessoa, String Redirecionar) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute("erro", Msg);
        session.setAttribute(Pagina, cadPessoa);
        response.sendRedirect(Redirecionar);

    }

    public static void validaJspSucesso(HttpServletRequest request, HttpServletResponse response, String Msg,
            String Pagina, String Redirecionar) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute("sucesso", Msg);
        response.sendRedirect(Redirecionar);

    }

    public static void validaJspSucessoId(HttpServletRequest request, HttpServletResponse response, String Msg,
            String Pagina, String Redirecionar, int id) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute("sucesso", Msg);
        response.sendRedirect(Redirecionar + "?id=" + id);

    }

    public static String getDateTimeSystem() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String formataDataExibir(Date Convert) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(Convert);
    }

    public static String formataDataExibir(String data) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse(data);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(d);
    }

    public static String formataHoraExibir(Time Convert) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(Convert);
    }

    public static String formataHoraExibir(String Convert) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(Convert);
    }

    public static String formataDataGravar(String data) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date d = formatter.parse(data);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(d);
    }

    public static String formataDistancia(Double prestacao) {
        DecimalFormat df = new DecimalFormat("##0.00");
        String dx = df.format(prestacao);
        return dx;
    }

    public static String firstName(String name) {
        int cont = name.length();
        for (int i = 0; i < cont; i++) {
            if (name.substring(i, i + 1).equals(" ")) {
                int posicao = i + 1;
                name = name.substring(0, posicao);
                return name.trim();
            }
        }
        return name.trim();
    }

    public static Map< String, String> getFieldValues(List lista) {

        HashMap< String, String> mapa = new HashMap< String, String>();

        for (int i = 0; i < lista.size(); i++) {

            FileItem item = (FileItem) lista.get(i);

            if (item.isFormField()) {
                mapa.put(item.getFieldName(), item.getString());
            }

        }

        return mapa;

    }

    public static boolean validaData(String data) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        try {
            df.parse(data);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    public static boolean validaHora(String hora) {

        DateFormat df = new SimpleDateFormat("HH:mm");
        df.setLenient(false);
        try {
            df.parse(hora);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

}
