package br.com.dao;

import br.com.entidades.Linha;
import br.com.entidades.Vao;
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
public class VaoDAO {

    private final Connection connection;

    public VaoDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_vao) as ultimo from tb_vao";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }

        }

        return lastId;

    }

    public int getUltimaSequencia(int idLinha) throws SQLException {
        int lastId = 0;
        String sql = "select max(vao_sequencia) as ultimo "
                + "from tb_vao a "
                + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist "
                + "where e.id_linhadist = " + idLinha + " "
                + "order by a.vao_sequencia";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }

        }

        return lastId;

    }

    public void inserir(Vao cadastrar) throws SQLException {
        int id = getUltimoId();

        String sql = "insert into tb_vao "
                + "(id_vao,fk_id_est_inicial,fk_id_est_final,vao_distancia,vao_progressiva,vao_regressiva,vao_sequencia)"
                + " values (?,?,?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, id);
                stmt.setInt(2, cadastrar.getIdEstInicial());
                stmt.setInt(3, cadastrar.getIdEstFinal());
                stmt.setFloat(4, cadastrar.getDistancia());
                stmt.setFloat(5, 0);
                stmt.setFloat(6, 0);
                stmt.setInt(7, cadastrar.getSequencia());

                stmt.execute();
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existe(int parametro1, int parametro2, int id) throws SQLException {
        String sql;

        if (id == 0) {
            sql = "select * from tb_vao "
                    + "where fk_id_est_inicial = " + parametro1 + " and fk_id_est_final = " + parametro2;
        } else {
            sql = "select * from tb_vao "
                    + "where fk_id_est_inicial = " + parametro1 + " and fk_id_est_final = " + parametro2 + " and id_vao !=" + id;
        }

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void atualizar(Vao atualizar, int id) throws SQLException {

        String sql = "update tb_vao set "
                + "fk_id_est_inicial = ? ,fk_id_est_final = ? , vao_distancia = ?, "
                + "vao_sequencia = ?  where id_vao = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, atualizar.getIdEstInicial());
            stmt.setInt(2, atualizar.getIdEstFinal());
            stmt.setFloat(3, atualizar.getDistancia());
            stmt.setInt(4, atualizar.getSequencia());

            stmt.execute();
            stmt.close();
        }
    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_vao where id_vao = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Vao seleciona(int id) {

        try {
            Vao vao = new Vao();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_vao "
                    + "where id_vao = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                vao.setIdVao(rs.getInt("id_vao"));
                vao.setIdEstInicial(rs.getInt("fk_id_est_inicial"));
                vao.setIdEstFinal(rs.getInt("fk_id_est_final"));
                vao.setDistancia(rs.getFloat("vao_distancia"));
                vao.setProgressiva(rs.getFloat("vao_progressiva"));
                vao.setRegressiva(rs.getFloat("vao_regressiva"));
                vao.setSequencia(rs.getInt("vao_sequencia"));
            }
            return vao;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listar(String pesquisa, int page, String filtro, int idLinha) {
        String sql;
        String campo = null;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        switch (filtro) {
            case "inicial":
                campo = "b.est_numero = " + pesquisa + "";
                break;
            case "final":
                campo = "c.est_numero = " + pesquisa + "";
                break;
        }
        inicio = maximo * page;
        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select a.*, b.est_numero as est1, c.est_numero as est2, e.linhadist_nomenclatura "
                    + "from tb_vao a "
                    + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                    + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                    + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist "
                    + "where e.id_linhadist = " + idLinha + " "
                    + "order by a.vao_sequencia";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select a.*, b.est_numero as est1, c.est_numero as est2, e.linhadist_nomenclatura "
                    + "from tb_vao a "
                    + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                    + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                    + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist"
                    + "where e.id_linhadist = " + idLinha + " and " + campo + " "
                    + "order by a.vao_sequencia";
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

    public int totalRegistros(String pesquisa, String filtro, int idLinha) throws SQLException {
        String sql;
        String campo = null;
        switch (filtro) {
            case "inicial":
                campo = "b.est_numero = " + pesquisa + "";
                break;
            case "final":
                campo = "c.est_numero = " + pesquisa + "";
                break;
        }

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select count(*) as cont "
                    + "from tb_vao a "
                    + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                    + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                    + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist "
                    + "where e.id_linhadist = " + idLinha + " "
                    + "order by a.vao_sequencia";
        } else {
            sql = "select count(*) as cont "
                    + "from tb_vao a "
                    + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                    + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                    + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                    + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist"
                    + "where e.id_linhadist = " + idLinha + " and " + campo + " "
                    + "order by a.vao_sequencia";
        }
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        int total = 0;
        if (rs.next()) {
            total = rs.getInt("cont");
        }
        return total;
    }

    public int totalPaginas(String pesquisa, String filtro, int idLinha) throws SQLException {
        int total = this.totalRegistros(pesquisa, filtro, idLinha);
        total = (int) Math.ceil((double) total / 10);

        return total;
    }

    public void atualizaDistancias() throws SQLException {

        Float total = this.getTotalDistancia();
        String sqlTodosVaos = "select * "
                + "from tb_vao a "
                + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist "
                + "where e.id_linhadist = " + Linha.idLinha + " "
                + "order by a.vao_sequencia";

        PreparedStatement stmt = connection.prepareStatement(sqlTodosVaos);
        ResultSet rs = stmt.executeQuery();
        rs.previous();
        while (rs.next()) {
            Float progDist = this.getProgDistancia(rs.getInt("vao_sequencia"));
            Float reg = total - progDist;
            this.atualizarDistancia(progDist, reg, rs.getInt("id_vao"));
        }

    }

    public Float getTotalDistancia() throws SQLException {
        Float totalDistancia = null;
        String sqlTotalDistancia = "select sum(vao_distancia) as total "
                + "from tb_vao a "
                + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist "
                + "where e.id_linhadist = " + Linha.idLinha + " order by a.vao_sequencia ";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sqlTotalDistancia); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                totalDistancia = rs.getFloat("total");
            }

        }

        return totalDistancia;

    }

    public Float getProgDistancia(int seq) throws SQLException {
        Float progDistancia = null;
        String sqlProgressiva = "select sum(vao_distancia) as total from tb_vao a "
                + "inner join tb_estrutura b on a.fk_id_est_inicial = b.id_estrutura "
                + "inner join tb_estrutura c on a.fk_id_est_final = c.id_estrutura "
                + "inner join mov_est_linha d on a.fk_id_est_inicial = d.fk_mov_id_estrutura "
                + "inner join tb_linhadistribuicao e on d.fk_mov_id_linhadist = e.id_linhadist "
                + "where e.id_linhadist = " + Linha.idLinha + " and a.vao_sequencia <=" + seq + " order by a.vao_sequencia";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sqlProgressiva); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                progDistancia = rs.getFloat("total");
            }

        }

        return progDistancia;

    }

    public void atualizarDistancia(Float prog, Float reg, int id) throws SQLException {

        String sql = "update tb_vao set "
                + "vao_progressiva = ? , vao_regressiva = ?  where id_vao = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setFloat(1, prog);
            stmt.setFloat(2, reg);
            stmt.execute();
            stmt.close();
        }
    }

}
