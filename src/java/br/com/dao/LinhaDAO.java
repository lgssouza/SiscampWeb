package br.com.dao;

import br.com.entidades.Linha;
import br.com.utilitarios.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Luiz Guilherme
 */
public class LinhaDAO {

    private final Connection connection;

    public LinhaDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_linhadist) as ultimo from tb_linhadistribuicao";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }
        }
        return lastId;

    }

    public String getNomeLinha(int id) throws SQLException {
        String nomenclatura = null;
        String sql = "select linhadist_nomenclatura from tb_linhadistribuicao where id_linhadist=" + id;
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                nomenclatura = rs.getString("linhadist_nomenclatura");
            }

        }

        return nomenclatura;

    }

    public void inserir(Linha cadastrar) throws SQLException {
        int id = getUltimoId();
        String sql = "insert into tb_linhadistribuicao "
                + "(id_linhadist,linhadist_nomenclatura,linhadist_cabopraio,linhadist_cabocond,linhadist_largfaixa)"
                + " values (?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, id);
                stmt.setString(2, cadastrar.getNomenclatura());
                stmt.setString(3, cadastrar.getCaboRaio());
                stmt.setString(4, cadastrar.getCaboCond());
                stmt.setInt(5, cadastrar.getLargFaixa());

                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeLinha(String parametro, int id) throws SQLException {
        String sql;

        if (id == 0) {
            sql = "select linhadist_nomenclatura from tb_linhadistribuicao "
                    + "where linhadist_nomenclatura = '" + parametro + "'";
        } else {
            sql = "select linhadist_nomenclatura from tb_linhadistribuicao "
                    + "where linhadist_nomenclatura = '" + parametro + "' and id_linhadist != " + id;
        }

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void atualizar(Linha atualizar, int id) throws SQLException {

        String sql = "update tb_linhadistribuicao set "
                + "linhadist_nomenclatura = ? ,linhadist_cabopraio = ? , linhadist_cabocond = ? "
                + ",linhadist_largfaixa = ? where id_linhadist = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, atualizar.getNomenclatura());
            stmt.setString(2, atualizar.getCaboRaio());
            stmt.setString(3, atualizar.getCaboCond());
            stmt.setInt(4, atualizar.getLargFaixa());

            stmt.execute();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_linhadistribuicao where id_linhadist = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Linha selecionaLinha(int id) {

        try {
            Linha linha = new Linha();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_linhadistribuicao "
                    + "where id_linhadist = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Linha.idLinha = rs.getInt("id_linhadist");
                linha.setNomenclatura(rs.getString("linhadist_nomenclatura"));
                linha.setCaboRaio(rs.getString("linhadist_cabopraio"));
                linha.setCaboCond(rs.getString("linhadist_cabocond"));
                linha.setLargFaixa(rs.getInt("linhadist_largfaixa"));

            }
            return linha;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarLinhas(String filtro, int page) throws SQLException {
        String sql;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        inicio = maximo * page;

        if ((filtro == null) || ("".equals(filtro))) {
            sql = "select * from tb_linhadistribuicao";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select * from tb_linhadistribuicao where linhadist_nomenclatura like '%" + filtro + "%' "
                    + "order by linhadist_nomenclatura";
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

    public ResultSet listarLinhasCombo() throws SQLException {
        String sql = "select * from tb_linhadistribuicao";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int totalRegistros(String filtro) throws SQLException {
        String sql;
        if ((filtro == null) || ("".equals(filtro))) {
            sql = "select count(*) as cont from tb_linhadistribuicao";
        } else {
            sql = "select count(*) as cont from tb_linhadistribuicao where linhadist_nomenclatura like '%" + filtro + "%' "
                    + "order by linhadist_nomenclatura";
        }
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        int total = 0;
        if (rs.next()) {
            total = rs.getInt("cont");
        }

        return total;
    }

    public int totalPaginas(String filtro) throws SQLException {
        int total = this.totalRegistros(filtro);
        total = (int) Math.ceil((double) total / 10);

        return total;
    }

}
