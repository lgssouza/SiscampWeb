package br.com.dao;

import br.com.entidades.ProgServico;
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
public class ProgServicoDAO {

    private final Connection connection;

    public ProgServicoDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_progservico) as ultimo from tb_progservico";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }

        }

        return lastId;

    }

    public void inserir(ProgServico cadastrar) throws SQLException {

        String sql = "insert into tb_progservico "
                + "(id_progservico,fk_progs_id_linhadist,progs_descservico,progs_semanaexec, progs_dataexec,progs_qtdpessoas,fk_progs_id_ple)"
                + " values (?,?,?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, cadastrar.getIdProgServico());
                stmt.setInt(2, cadastrar.getIdLinhaDist());
                stmt.setString(3, cadastrar.getDescServico());
                stmt.setInt(4, cadastrar.getSemanaExec());
                stmt.setString(5, cadastrar.getDataExec());
                stmt.setInt(6, cadastrar.getQtdPessoas());
                if (cadastrar.getIdPle() == 0) {
                    stmt.setString(7, null);
                } else {
                    stmt.setInt(7, cadastrar.getIdPle());
                }

                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeServico(String parametro, int id) throws SQLException {
        String sql;

        if (id == 0) {
            sql = "select fk_progs_id_ple from tb_progservico "
                    + "where fk_progs_id_ple = " + parametro;
        } else {
            sql = "select fk_progs_id_ple from tb_progservico "
                    + "where fk_progs_id_ple = " + parametro + " and id_progservico != " + id;
        }

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void atualizar(ProgServico atualizar, int id) throws SQLException {

        String sql = "update tb_progservico set "
                + "fk_progs_id_linhadist = ? , progs_descservico = ? , progs_semanaexec = ? "
                + ", progs_dataexec = ?, progs_qtdpessoas = ? , fk_progs_id_ple= ? where id_progservico = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, atualizar.getIdLinhaDist());
            stmt.setString(2, atualizar.getDescServico());
            stmt.setInt(3, atualizar.getSemanaExec());
            stmt.setString(4, atualizar.getDataExec());
            stmt.setInt(5, atualizar.getQtdPessoas());
            stmt.setInt(6, atualizar.getIdPle());

            stmt.execute();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_progservico where id_progservico = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public ProgServico selecionaServico(int id) {

        try {
            ProgServico progs = new ProgServico();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_progservico "
                    + "where id_progservico = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                progs.setIdProgServico(rs.getLong("id_progservico"));
                progs.setIdLinhaDist(rs.getInt("fk_progs_id_linhadist"));
                progs.setDescServico(rs.getString("progs_descservico"));
                progs.setSemanaExec(rs.getInt("progs_semanaexec"));
                progs.setDataExec(MetodosUtil.formataDataExibir(rs.getDate("progs_dataexec")));
                progs.setQtdPessoas(rs.getInt("progs_qtdpessoas"));
                progs.setIdPle(rs.getInt("fk_progs_id_ple"));

            }
            return progs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarProgServico(String pesquisa, int page, String filtro) throws SQLException, ParseException {
        String sql;
        String campo;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        switch (filtro) {
            case "data":
                if (MetodosUtil.validaData(pesquisa)) {
                    campo = "progs_dataexec = '" + MetodosUtil.formataDataGravar(pesquisa) + "'";
                } else {
                    campo = "progs_dataexec = 2015-01-01";
                }
                break;
            case "linha":
                campo = "linhadist_nomenclatura like '%" + pesquisa + "%'";
                break;
            default:
                campo = "progs_semanaexec =" + pesquisa;
                break;
        }

        inicio = maximo * page;

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select a.*,b.id_linhadist,b.linhadist_nomenclatura,c.* from tb_progservico a inner join tb_linhadistribuicao b on a.fk_progs_id_linhadist = b.id_linhadist left join tb_ple c on a.fk_progs_id_ple = c.id_ple";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select a.*,b.id_linhadist,b.linhadist_nomenclatura,c.* from tb_progservico a inner join tb_linhadistribuicao b on a.fk_progs_id_linhadist = b.id_linhadist left join tb_ple c on a.fk_progs_id_ple = c.id_ple "
                    + "where " + campo + " "
                    + "order by id_progservico";
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

        switch (filtro) {
            case "data":
                if (MetodosUtil.validaData(pesquisa)) {
                    campo = "progs_dataexec = '" + MetodosUtil.formataDataGravar(pesquisa) + "'";
                } else {
                    campo = "progs_dataexec = 2015-01-01";
                }
                break;
            case "linha":
                campo = "linhadist_nomenclatura like '%" + pesquisa + "%'";
                break;
            default:
                campo = "progs_semanaexec =" + pesquisa;
                break;
        }

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select count(*) as cont from tb_progservico a inner join tb_linhadistribuicao b on a.fk_progs_id_linhadist = b.id_linhadist left join tb_ple c on a.fk_progs_id_ple = c.id_ple";

        } else {
            sql = "select count(*) as cont from tb_progservico a inner join tb_linhadistribuicao b on a.fk_progs_id_linhadist = b.id_linhadist left join tb_ple c on a.fk_progs_id_ple = c.id_ple "
                    + "where " + campo + " "
                    + "order by id_progservico";
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
