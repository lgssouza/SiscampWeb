/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.dao;

import br.com.entidades.Funcionario;
import br.com.entidades.LoginSessao;
import br.com.utilitarios.Criptografia;
import br.com.utilitarios.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Luiz Guilherme Souza
 */
public class FuncionarioDAO {

    private final Connection connection;
    private ResultSet rs;
    Criptografia pass = new Criptografia();

    public FuncionarioDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public LoginSessao validarLogin(LoginSessao logar) throws SQLException {
        LoginSessao user = null;

        String senhasegura = pass.Criptografar(logar.getSenha());
        String usuario = logar.getUsuario();

        String sql = "select * from tb_funcionario where func_login = ? and func_senha = ?";

        PreparedStatement st = connection.prepareStatement(sql);

        st.setString(1, usuario);
        st.setString(2, senhasegura);

        rs = st.executeQuery();
        if (rs.next()) {
            user = new LoginSessao();

            user.setMatricula(rs.getInt("matricula"));
            user.setUsuario(rs.getString("func_nome"));
            user.setSenha(logar.getSenha());
        }

        return user;
    }

    public void inserir(Funcionario cadastrar) throws SQLException {

        String sql = "insert into tb_funcionario "
                + "(matricula,func_nome,func_telpessoal,func_login,func_senha)"
                + " values (?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, cadastrar.getMatricula());
                stmt.setString(2, cadastrar.getNome());
                stmt.setString(3, cadastrar.getTel());
                stmt.setString(4, cadastrar.getUsuario());
                String senhasegura = pass.Criptografar(cadastrar.getSenha());
                stmt.setString(5, senhasegura);

                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeFuncionarioNome(String parametro, int id) throws SQLException {
        String sql;

        if (id == 0) {
            sql = "select func_nome from tb_funcionario "
                    + "where func_nome = '" + parametro + "'";
        } else {
            sql = "select func_nome from tb_funcionario "
                    + "where func_nome = '" + parametro + "' and matricula != " + id;
        }

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rsFunc = stmt.executeQuery();
        return rsFunc.next();
    }

    public boolean existeFuncionarioUsuario(String parametro, int id) throws SQLException {
        String sql;

        if (parametro == null || parametro.equals("")) {
            return false;
        } else {

            if (id == 0) {
                sql = "select func_login from tb_funcionario "
                        + "where func_login = '" + parametro + "'";
            } else {
                sql = "select func_login from tb_funcionario "
                        + "where func_login = '" + parametro + "' and matricula != " + id;
            }

            PreparedStatement stmt = this.connection.prepareStatement(sql);
            ResultSet rsFunc = stmt.executeQuery();
            return rsFunc.next();
        }
    }

    public void atualizar(Funcionario atualizar, int id) throws SQLException {

        String sql = "update tb_funcionario set "
                + "func_nome = ? ,func_telpessoal = ? , func_login = ? "
                + "where matricula = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, atualizar.getNome());
            stmt.setString(2, atualizar.getTel());
            stmt.setString(3, atualizar.getUsuario());

            stmt.execute();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_funcionario where matricula = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Funcionario selecionaFuncionario(int id) {

        try {
            Funcionario func = new Funcionario();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_funcionario "
                    + "where matricula = " + id);
            ResultSet rsFunc = stmt.executeQuery();

            if (rsFunc.next()) {
                func.setMatricula(rsFunc.getInt("matricula"));
                func.setNome(rsFunc.getString("func_nome"));
                func.setTel(rsFunc.getString("func_telpessoal"));
                func.setUsuario(rsFunc.getString("func_login"));
            }
            return func;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarFuncionarios(String filtro, int page) throws SQLException {
        String sql;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        inicio = maximo * page;

        if ((filtro == null) || ("".equals(filtro))) {
            sql = "select * from tb_funcionario";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select * from tb_funcionario where func_nome like '%" + filtro + "%' "
                    + "order by func_nome";
            sql += " limit " + inicio + "," + maximo + "";
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rsFunc = stmt.executeQuery(sql);
            return rsFunc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int totalRegistros(String filtro) throws SQLException {
        String sql;
        if ((filtro == null) || ("".equals(filtro))) {
            sql = "select count(*) as cont from tb_funcionario";
        } else {
            sql = "select count(*) as cont from tb_funcionario where func_nome like '%" + filtro + "%' "
                    + "order by func_nome";
        }
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rsFunc = stmt.executeQuery();
        int total = 0;
        if (rsFunc.next()) {
            total = rsFunc.getInt("cont");
        }

        return total;
    }

    public int totalPaginas(String filtro) throws SQLException {
        int total = this.totalRegistros(filtro);
        total = (int) Math.ceil((double) total / 10);

        return total;
    }

    public boolean verificaSenhaAntiga(String senha, String usuario) throws SQLException {
        LoginSessao user = null;

        String senhasegura = pass.Criptografar(senha);

        String sql = "select * from tb_funcionario where func_login = ? and func_senha = ?";

        PreparedStatement st = connection.prepareStatement(sql);

        st.setString(1, usuario);
        st.setString(2, senhasegura);

        rs = st.executeQuery();
        return rs.next();

    }

    public void atualizarSenha(String usuario, String senha) throws SQLException {
        String senhasegura = pass.Criptografar(senha);
        String sql = "update tb_funcionario set "
                + "func_senha = ? "
                + "where func_login = '" + usuario + "'";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, senhasegura);
            stmt.execute();
        }

    }

    public ResultSet listarFuncionarioCombo() throws SQLException {
        String sql;

        sql = "select * from tb_funcionario";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rsFunc = stmt.executeQuery(sql);
            return rsFunc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
