package br.com.dao;

import br.com.entidades.Linha;
import br.com.entidades.Sobreaviso;
import br.com.utilitarios.ConnectionUtil;
import br.com.utilitarios.MetodosUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/**
 *
 * @author Luiz Guilherme
 */
public class SobreavisoDAO {

    private final Connection connection;

    public SobreavisoDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_sobreaviso) as ultimo from tb_sobreaviso";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }
            
        }

        return lastId;

    }

    public void inserir(Sobreaviso cadastrar) throws SQLException {
        int id = getUltimoId();
        String sql = "insert into tb_sobreaviso "
                + "(id_sobreaviso,fk_func_matricula,sobreaviso_data)"
                + " values (?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, id);
                stmt.setInt(2, cadastrar.getFuncMatricula());
                stmt.setString(3, cadastrar.getData());
                
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Sobreaviso atualizar, int id) throws SQLException {

        String sql = "update tb_sobreaviso set "
                + "fk_func_matricula = ? ,sobreaviso_data = ? where id_sobreaviso = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, atualizar.getFuncMatricula());
            stmt.setString(2, atualizar.getData());

            stmt.execute();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_sobreaviso where id_sobreaviso = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Sobreaviso selecionaSobreaviso(int id) {

        try {
            Sobreaviso sobreaviso = new Sobreaviso();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_sobreaviso "
                    + "where id_sobreaviso = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Linha.idLinha = rs.getInt("id_sobreaviso");
                sobreaviso.setFuncMatricula(rs.getInt("fk_func_matricula"));
                sobreaviso.setData(rs.getString("sobreaviso_data"));
            }
            return sobreaviso;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarSobreavisos(String pesquisa, int page, String filtro) throws SQLException, ParseException {
        String sql;
        String campo;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        if (filtro.equals("data")) {
            if (MetodosUtil.validaData(pesquisa)) {
                campo = "sobreaviso_data = '" + MetodosUtil.formataDataGravar(pesquisa) + "'";
            } else {
                campo = "sobreaviso_data = 2015-01-01";
            }

        } else {
            campo = "func_nome like '%" + pesquisa + "%'";
        }

        inicio = maximo * page;

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select * from tb_sobreaviso a inner join tb_funcionario b on b.matricula = a.fk_func_matricula";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select * from tb_sobreaviso a inner join tb_funcionario b on b.matricula = a.fk_func_matricula"
                    + " where " + campo + " order by sobreaviso_data";
            sql += " limit " + inicio + "," + maximo + "";
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int totalRegistros(String pesquisa, String filtro) throws SQLException, ParseException {
        String sql;
        String campo;

        if (filtro.equals("data")) {
            if (MetodosUtil.validaData(filtro)) {
                campo = "sobreaviso_data = '" + MetodosUtil.formataDataGravar(filtro) + "'";
            } else {
                campo = "sobreaviso_data = 2015-01-01";
            }

        } else {
            campo = "func_nome like '%" + pesquisa + "%'";
        }

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select count(*) as cont from tb_sobreaviso a inner join tb_funcionario b on b.matricula = a.fk_func_matricula";

        } else {
            sql = "select count(*) as cont from tb_sobreaviso a inner join tb_funcionario b on b.matricula = a.fk_func_matricula"
                    + " where " + campo + " order by sobreaviso_data";
        }
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        int total = 0;
        if (rs.next()) {
            total = rs.getInt("cont");
        }

        return total;
    }

    public int totalPaginas(String pesquisa, String filtro) throws SQLException, ParseException {
        int total = this.totalRegistros(pesquisa, filtro);
        total = (int) Math.ceil((double) total / 10);

        return total;
    }

}
