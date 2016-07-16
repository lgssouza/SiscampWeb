package br.com.dao;

import br.com.entidades.Contato;
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
public class ContatoDAO {

    private final Connection connection;

    public ContatoDAO() throws ClassNotFoundException, SQLException {
        this.connection = new ConnectionUtil().getConnection();
    }

    public int getUltimoId() throws SQLException {
        int lastId = 0;
        String sql = "select max(id_contato) as ultimo from tb_contatos";
        try (PreparedStatement stmt = (PreparedStatement) connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt("ultimo") + 1;
            }

        }

        return lastId;

    }

    public void inserir(Contato cadastrar) throws SQLException {
        int id = getUltimoId();
        String sql = "insert into tb_contatos "
                + "(id_contato,contato_nome,contato_espec,contato_tel1,contato_tel2,contato_cidade,contato_end)"
                + " values (?,?,?,?,?,?,?)";

        try {
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, id);
                stmt.setString(2, cadastrar.getNome());
                stmt.setString(3, cadastrar.getEspecialidade());
                stmt.setString(4, cadastrar.getTel1());
                stmt.setString(5, cadastrar.getTel2());
                stmt.setString(6, cadastrar.getCidade());
                stmt.setString(7, cadastrar.getEnd());
                
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

    public void atualizar(Contato atualizar, int id) throws SQLException {

        String sql = "update tb_contatos set "
                + "contato_nome = ? ,contato_espec = ? , contato_tel1 = ? "
                + ",contato_tel2 = ?, contato_cidade = ?, contato_end = ?  where id_contato = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, atualizar.getNome());
            stmt.setString(2, atualizar.getEspecialidade());
            stmt.setString(3, atualizar.getTel1());
            stmt.setString(4, atualizar.getTel2());
            stmt.setString(5, atualizar.getCidade());
            stmt.setString(6, atualizar.getEnd());

            stmt.execute();
        }

    }

    public boolean excluir(int id) throws SQLException {

        String sql = "delete from tb_contatos where id_contato = " + id;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.execute();
        }
        return true;
    }

    public Contato selecionaContato(int id) {

        try {
            Contato contato = new Contato();
            PreparedStatement stmt = connection.prepareStatement("select * from tb_contatos "
                    + "where id_contato = " + id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                contato.setIdContato(rs.getInt("id_contato"));
                contato.setNome(rs.getString("contato_nome"));
                contato.setEspecialidade(rs.getString("contato_espec"));
                contato.setTel1(rs.getString("contato_tel1"));
                contato.setTel2(rs.getString("contato_tel2"));
                contato.setCidade(rs.getString("contato_cidade"));
                contato.setEnd(rs.getString("contato_end"));

            }
            return contato;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet listarContatos(String filtro, int page) throws SQLException {
        String sql;
        int maximo = 10;
        int inicio = 0;
        if (page > 0) {
            page--;
        }

        inicio = maximo * page;

        if ((filtro == null) || ("".equals(filtro))) {
            sql = "select * from tb_contatos";
            sql += " limit " + inicio + "," + maximo + "";
        } else {
            sql = "select * from tb_contatos where contato_nome like '%" + filtro + "%' "
                    + "order by contato_nome";
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

    public int totalRegistros(String filtro) throws SQLException {
        String sql;
        if ((filtro == null) || ("".equals(filtro))) {
            sql = "select count(*) as cont from tb_contatos";
        } else {
            sql = "select count(*) as cont from tb_contatos where contato_nome like '%" + filtro + "%' "
                    + "order by contato_nome";
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
