package br.com.dao;

import br.com.entidades.Constantes;
import br.com.entidades.Plano;
import br.com.utilitarios.ConnectionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luiz Guilherme
 */
public class PlanoDAO {

    private final Connection connection;

    public PlanoDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimaVersao(int id) throws SQLException {
        int lastId = 0;
        String sql = "select max(plano_versao) as ultimo from tb_plano where fk_plano_id_linhadist=" + id;
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }
        }
        return lastId;

    }

    public String getNomeArquivo(int id) throws SQLException {
        String lastId = "";
        String sql = "select * from tb_plano where fk_plano_id_linhadist=" + id;
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getString("plano_nome");
            }
        }
        return lastId;

    }

    public void insertPDF(Plano plano) {
        int len;
        String filename = plano.getArquivo();
        String query;
        PreparedStatement pstmt;

        try {
//            File file = new File(filename);
//            FileInputStream fis = new FileInputStream(file);
//            len = (int) file.length();
            query = ("insert into tb_plano values(?,?,?)");
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, plano.getIdLinha());
//            pstmt.setBinaryStream(2, fis, len);
            pstmt.setString(2, plano.getNome());
            pstmt.setInt(3, plano.getVersao());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(PlanoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean existePlano(int parametro) throws SQLException {

        String sql = "select * from tb_plano "
                + "where fk_plano_id_linhadist = " + parametro + "";

        PreparedStatement stmt = this.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void updatePDF(Plano plano, int id) {
        int len;
        String filename = plano.getArquivo();
        String query;
        PreparedStatement pstmt;

        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            len = (int) file.length();
            query = "update tb_plano set plano_plano = ? , plano_nome = ? , plano_versao = ? where fk_plano_id_linhadist = " + id;
            pstmt = connection.prepareStatement(query);
            pstmt.setBinaryStream(1, fis, len);
            pstmt.setString(2, plano.getNome());
            pstmt.setInt(3, plano.getVersao());

            pstmt.executeUpdate();

        } catch (FileNotFoundException | SQLException e) {
            Logger.getLogger(PlanoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean excluir(int id) throws SQLException {

        String nomearquivo = this.getNomeArquivo(id);

        File arq = new File(Constantes.CAMINHO_AQUIVOS + nomearquivo);
        if (!arq.exists()) {
            arq.delete();
        }

        String sql = "delete from tb_plano where fk_plano_id_linhadist = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Plano selecionaPlano(int id) {

        try {
            Plano plano = new Plano();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_plano "
                    + "where fk_plano_id_linhadist = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                plano.setIdLinha(rs.getInt("fk_plano_id_linhadist"));
                plano.setNome(rs.getString("plano_nome"));
                plano.setVersao(rs.getInt("plano_versao"));

            }
            return plano;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarPlano(String pesquisa, int page) throws SQLException, ParseException {
        String sql;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }
        inicio = maximo * page;

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select * from tb_plano a inner join tb_linhadistribuicao b on a.fk_plano_id_linhadist = b.id_linhadist";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select * from tb_plano a "
                    + "inner join tb_linhadistribuicao b on a.fk_plano_id_linhadist = b.id_linhadist "
                    + "where linhadist_nomenclatura like'%" + pesquisa + "%'";
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

    public int totalRegistros(String pesquisa) throws SQLException, ParseException {
        String sql;

        if ((pesquisa == null) || ("".equals(pesquisa))) {
            sql = "select count(*) as cont from tb_plano a inner join tb_linhadistribuicao b on a.fk_plano_id_linhadist = b.id_linhadist";
        } else {
            sql = "select count(*) as cont from tb_plano a "
                    + "inner join tb_linhadistribuicao b on a.fk_plano_id_linhadist = b.id_linhadist "
                    + "where linhadist_nomenclatura like'%" + pesquisa + "%'";
        }
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        int total = 0;
        if (rs.next()) {
            total = rs.getInt("cont");
        }

        return total;
    }

    public int totalPaginas(String pesquisa) throws SQLException, ParseException {
        int total = this.totalRegistros(pesquisa);
        total = (int) Math.ceil((double) total / 10);

        return total;
    }

}
