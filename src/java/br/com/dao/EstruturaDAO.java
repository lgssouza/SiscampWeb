package br.com.dao;

import br.com.entidades.Estrutura;
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
public class EstruturaDAO {

    private final Connection connection;

    public EstruturaDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_estrutura) as ultimo from tb_estrutura";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }
            rs.close();
            stmt.close();
        }

        return lastId;

    }

    public ResultSet getIdLinha(int id) throws SQLException {
        int idLinha = 0;
        String sql = "select * from mov_est_linha a inner join tb_linhadistuibuicao b on a.fk_mov_id_linhadist = b.id_linhadist where fk_mov_id_estrutura = " + id;
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            return rs;
        }
    }

    public int getUltimoIdMov() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_movestlinha) as ultimo from mov_est_linha";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }
        }
        return lastId;

    }

    public void inserir(Estrutura cadastrar) throws SQLException {
        int id = getUltimoId();
        int id2 = getUltimoIdMov();
        String sql = "insert into tb_estrutura "
                + "(id_estrutura,est_numero,est_modelo,est_tipo,est_altura)"
                + " values (?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, id);
                stmt.setString(2, cadastrar.getNumero());
                stmt.setString(3, cadastrar.getModelo());
                stmt.setString(4, cadastrar.getTipo());
                stmt.setString(5, cadastrar.getAltura());

                stmt.execute();
                stmt.close();
            }
            sql = "insert into mov_est_linha (id_movestlinha, fk_mov_id_linhadist, fk_mov_id_estrutura) values (?,?,?)";
            try {
                try (PreparedStatement stmt2 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt2.setInt(1, id2);
                    stmt2.setInt(2, Linha.idLinha);
                    stmt2.setInt(3, id);
                    stmt2.execute();
                }

            } catch (SQLException e2) {
                throw new RuntimeException(e2);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existe(String parametro1, int parametro2, int id) throws SQLException {
        String sql;

        if (id == 0) {
            sql = "select a.est_numero from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "where a.est_numero = '" + parametro1 + "' and b.fk_mov_id_linhadist = " + parametro2;
        } else {
            sql = "select a.est_numero from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "where a.est_numero = '" + parametro1 + "' and b.fk_mov_id_linhadist = " + parametro2 + " and a.id_estrutura !=" + id;
        }

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void atualizar(Estrutura atualizar, int id) throws SQLException {

        String sql = "update tb_estrutura set "
                + "est_numero = ? ,est_modelo = ? , est_tipo = ? "
                + ",est_altura = ? where id_estrutura = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, atualizar.getNumero());
            stmt.setString(2, atualizar.getModelo());
            stmt.setString(3, atualizar.getTipo());
            stmt.setString(4, atualizar.getAltura());

            stmt.execute();
            stmt.close();
        }
        sql = "update mov_est_linha set fk_mov_id_linhadist= ? where fk_mov_id_estrutura=" + id;
        try (PreparedStatement stmt2 = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt2.setInt(1, Linha.idLinha);
            stmt2.execute();
            stmt2.close();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_estrutura where id_estrutura = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Estrutura seleciona(int id) {

        try {
            Estrutura est = new Estrutura();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "where id_estrutura = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                est.setIdEstrutura(rs.getInt("id_estrutura"));
                est.setNumero(rs.getString("est_numero"));
                est.setModelo(rs.getString("est_modelo"));
                est.setTipo(rs.getString("est_tipo"));
                est.setAltura(rs.getString("est_altura"));
            }
            return est;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listar(String pesquisa, int page, int idLinha) {
        String sql;
        String campo = null;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        inicio = maximo * page;
        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select a.id_estrutura, a.est_numero, a.est_modelo, a.est_tipo, a.est_altura, "
                    + "c.linhadist_nomenclatura, c.id_linhadist from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao c on b.fk_mov_id_linhadist = c.id_linhadist"
                    + " where c.id_linhadist = " + idLinha + " order by cast(a.est_numero as signed)";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select a.id_estrutura, a.est_numero, a.est_modelo, a.est_tipo, a.est_altura, "
                    + "c.linhadist_nomenclatura from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao c on b.fk_mov_id_linhadist = c.id_linhadist"
                    + " where c.id_linhadist = " + idLinha + " and a.est_numero = '" + pesquisa + "' "
                    + "order by cast(a.est_numero as signed)";
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

    public int totalRegistros(String pesquisa, int idLinha) throws SQLException {
        String sql;
        String campo = "";

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select count(*) as cont from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao c on b.fk_mov_id_linhadist = c.id_linhadist"
                    + " where c.id_linhadist = " + idLinha;

        } else {
            sql = "select count(*) as cont from tb_estrutura a "
                    + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao c on b.fk_mov_id_linhadist = c.id_linhadist"
                    + " where c.id_linhadist = " + idLinha + " and a.est_numero = '" + pesquisa + "' "
                    + "order by est_numero";

        }
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        int total = 0;
        if (rs.next()) {
            total = rs.getInt("cont");
        }

        return total;
    }

    public int totalPaginas(String pesquisa, int idLinha) throws SQLException {
        int total = this.totalRegistros(pesquisa, idLinha);
        total = (int) Math.ceil((double) total / 10);

        return total;
    }

    public ResultSet listarEstruturasComboInicial(int idLinha) {
        String sql = "select a.id_estrutura, a.est_numero "
                + "from tb_estrutura a "
                + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                + "inner join tb_linhadistribuicao c on b.fk_mov_id_linhadist = c.id_linhadist "
                + "where c.id_linhadist = " + idLinha + " "
                + "and a.id_estrutura not in (select fk_id_est_inicial from tb_vao) "
                + "order by cast(a.est_numero as signed)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public ResultSet listarEstruturasComboFinal(int idLinha) {
        String sql = "select a.id_estrutura, a.est_numero "
                + "from tb_estrutura a "
                + "inner join mov_est_linha b on a.id_estrutura = b.fk_mov_id_estrutura "
                + "inner join tb_linhadistribuicao c on b.fk_mov_id_linhadist = c.id_linhadist "
                + "where c.id_linhadist = " + idLinha + " "
                + "and a.id_estrutura not in (select fk_id_est_final from tb_vao) "
                + "order by cast(a.est_numero as signed)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
