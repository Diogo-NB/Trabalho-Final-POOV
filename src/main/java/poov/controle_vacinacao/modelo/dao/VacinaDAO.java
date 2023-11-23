package poov.controle_vacinacao.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import poov.controle_vacinacao.modelo.Vacina;

public class VacinaDAO {

    private final Connection conexao;

    public VacinaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void gravar(Vacina vacina) throws SQLException {

        String sql = "INSERT INTO vacina(nome, descricao) VALUES (?, ?);";
        PreparedStatement pstmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, vacina.getNome());
        pstmt.setString(2, vacina.getDescricao());

        if (pstmt.executeUpdate() == 1) {
            System.out.println("Insercao da vacina feita com sucesso");
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                vacina.setCodigo(rs.getLong(1));
            } else {
                System.out.println("Erro ao obter o codigo gerado pelo BD para a vacina");
            }
            rs.close();
        } else {
            System.out.println("Erro ao inserir a vacina");
        }
        pstmt.close();
    }

    public List<Vacina> buscarComFiltro(Vacina vacinaFiltro) throws SQLException {
        Vacina v;
        List<Vacina> vacinas = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM vacina WHERE situacao = 'ATIVO'");
        if (vacinaFiltro.getCodigo() != null)
            sql.append(" AND codigo = " + vacinaFiltro.getCodigo());
        if (!vacinaFiltro.getNome().isEmpty())
            sql.append(" AND nome LIKE '" + vacinaFiltro.getNome() + "'");
        if (!vacinaFiltro.getDescricao().isEmpty())
            sql.append(" AND descricao LIKE '" + vacinaFiltro.getDescricao() + "'");
        sql.append(";");

        Statement stmt = conexao.createStatement();

        ResultSet rs = stmt.executeQuery(sql.toString());
        while (rs.next()) {
            v = new Vacina(rs.getLong(1), rs.getString(2), rs.getString(3));
            vacinas.add(v);
        }
        rs.close();
        stmt.close();
        return vacinas;
    }

    public Vacina buscarPeloCodigo(long codigo) throws SQLException {

        Vacina v = null;
        String sql = "SELECT * FROM vacina WHERE codigo = ? AND situacao = 'ATIVO';";
        PreparedStatement pstmt = conexao.prepareStatement(sql);
        pstmt.setLong(1, codigo);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            v = new Vacina(rs.getLong(1), rs.getString(2), rs.getString(3));
        } else {
            System.out.println("Nao foi encontrada uma vacina a partir dos parâmetros: ");
        }
        rs.close();
        pstmt.close();
        return v;
    }

    public List<Vacina> buscarTodas() throws SQLException {
        Vacina v;
        List<Vacina> vacinas = new ArrayList<>();
        String sql = "SELECT * FROM vacina WHERE situacao = 'ATIVO';";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            v = new Vacina(rs.getLong(1), rs.getString(2), rs.getString(3));
            vacinas.add(v);
        }
        rs.close();
        stmt.close();
        return vacinas;
    }

    public boolean remover(Vacina vacina) throws SQLException {
        boolean retorno = false;
        String sqlUpdate = "UPDATE vacina SET situacao = 'INATIVO' WHERE codigo = ?;";
        PreparedStatement pstmtUpd = conexao.prepareStatement(sqlUpdate);

        pstmtUpd.setLong(1, vacina.getCodigo());

        int resultado = pstmtUpd.executeUpdate();

        if (resultado == 1) {
            System.out.println("Remocao da vacina executada com sucesso");
            retorno = true;
        } else {
            System.out.println("Erro removendo a vacina com codigo: " + vacina.getCodigo());
        }

        pstmtUpd.close();

        return retorno;
    }

    public boolean atualizar(Vacina vacina) throws SQLException {
        boolean retorno = false;
        String sqlUpdate = "UPDATE vacina SET nome = ?, descricao = ?, situacao = ? WHERE codigo = ?;";
        PreparedStatement pstmtUpd = conexao.prepareStatement(sqlUpdate);
        pstmtUpd.setString(1, vacina.getNome());
        pstmtUpd.setString(2, vacina.getDescricao());
        pstmtUpd.setString(3, vacina.getSituacao().toString());
        pstmtUpd.setLong(4, vacina.getCodigo());

        int resultado = pstmtUpd.executeUpdate();
        if (resultado == 1) {
            System.out.println("Alteracao da vacina executada com sucesso");
            retorno = true;
        } else {
            System.out.println("Erro alterando a vacina com codigo: " + vacina.getCodigo());
        }
        pstmtUpd.close();

        return retorno;
    }

}
