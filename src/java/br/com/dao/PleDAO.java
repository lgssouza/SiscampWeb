package br.com.dao;

import br.com.entidades.Ple;
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
public class PleDAO {

    private final Connection connection;

    public PleDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_ple) as ultimo from tb_ple";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }
        }
        return lastId;
    }

    public void inserir(Ple cadastrar) throws SQLException {
        String sql = "insert into tb_ple "
                + "(id_ple,ple_supervisor,ple_data,ple_hrinicial,ple_hrfinal,fk_ple_id_linhadist)"
                + " values (?,?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, cadastrar.getIdPle());
                stmt.setString(2, cadastrar.getSupervisor());
                stmt.setString(3, cadastrar.getData());
                stmt.setString(4, cadastrar.getHrInicial());
                stmt.setString(5, cadastrar.getHrFinal());
                stmt.setInt(6, cadastrar.getIdLinhaDist());

                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeContato(String parametro, int id) throws SQLException {
        String sql;

        if (id == 0) {
            sql = "select contato_nome from tb_contatos "
                    + "where contato_nome = '" + parametro + "'";
        } else {
            sql = "select contato_nome from tb_contatos "
                    + "where contato_nome = '" + parametro + "' and id_contato != " + id;
        }

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void atualizar(Ple atualizar, int id) throws SQLException {

        String sql = "update tb_ple set "
                + "ple_supervisor = ? ,ple_data = ? , ple_hrinicial = ? "
                + ",ple_hrfinal = ?, fk_ple_id_linhadist = ? where id_ple = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, atualizar.getSupervisor());
            stmt.setString(2, atualizar.getData());
            stmt.setString(3, atualizar.getHrInicial());
            stmt.setString(4, atualizar.getHrFinal());
            stmt.setInt(5, atualizar.getIdLinhaDist());

            stmt.execute();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_ple where id_ple = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Ple selecionaPle(int id) {

        try {
            Ple ple = new Ple();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_ple "
                    + "where id_ple = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ple.setIdPle(rs.getInt("id_ple"));
                ple.setSupervisor(rs.getString("ple_supervisor"));
                ple.setData(rs.getString("ple_data"));
                ple.setHrInicial(MetodosUtil.formataHoraExibir(rs.getTime("ple_hrinicial")));
                ple.setHrFinal(MetodosUtil.formataHoraExibir(rs.getTime("ple_hrfinal")));
                ple.setIdLinhaDist(rs.getInt("fk_ple_id_linhadist"));

            }
            return ple;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarPle(String pesquisa, int page, String filtro) throws SQLException, ParseException {
        String sql;
        String campo;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        if (filtro.equals("data")) {
            if (MetodosUtil.validaData(pesquisa)) {
                campo = "ple_data = '" + MetodosUtil.formataDataGravar(pesquisa) + "'";
            } else {
                campo = "ple_data = 2015-01-01";
            }

        } else {
            campo = "ple_supervisor like '%" + pesquisa + "%'";
        }

        inicio = maximo * page;

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select * from tb_ple a inner join tb_linhadistribuicao b on a.fk_ple_id_linhadist = b.id_linhadist order by ple_data";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select * from tb_ple a inner join tb_linhadistribuicao b on a.fk_ple_id_linhadist = b.id_linhadist "
                    + "where " + campo + " "
                    + "order by ple_data";
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
            if (MetodosUtil.validaData(pesquisa)) {
                campo = "ple_data = '" + MetodosUtil.formataDataGravar(pesquisa) + "'";
            } else {
                campo = "ple_data = 2015-01-01";
            }

        } else {
            campo = "ple_supervisor like '%" + pesquisa + "%'";
        }

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select count(*) as cont from tb_ple a inner join tb_linhadistribuicao b on a.fk_ple_id_linhadist = b.id_linhadist order by ple_data";
        } else {
            sql = "select count(*) as cont from tb_ple a inner join tb_linhadistribuicao b on a.fk_ple_id_linhadist = b.id_linhadist "
                    + "where " + campo + " "
                    + "order by ple_data";
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

    public ResultSet listarPleCombo() throws SQLException {
        String sql = "select * from tb_ple";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
