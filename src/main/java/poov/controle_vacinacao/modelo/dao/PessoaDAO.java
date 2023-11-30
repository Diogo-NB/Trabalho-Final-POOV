package poov.controle_vacinacao.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import poov.controle_vacinacao.modelo.Pessoa;

public class PessoaDAO {

    private final Connection conexao;

    public PessoaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public List<Pessoa> buscarTodas() throws SQLException {
        Pessoa p;
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM pessoa WHERE situacao = 'ATIVO';";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            p = new Pessoa(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
            pessoas.add(p);
        }
        rs.close();
        stmt.close();
        return pessoas;
    }

    public List<Pessoa> buscarComFiltro(Pessoa pessoaFiltro, LocalDate fromDataNasc, LocalDate toDataNasc)
            throws SQLException {
        Pessoa p;
        List<Pessoa> pessoas = new ArrayList<>();
        // Construindo a string do sql "dinamicamente"
        StringBuilder sql = new StringBuilder("SELECT * FROM pessoa WHERE situacao = 'ATIVO'");

        Long codigo = null;
        String cpf = "";
        String nome = "";

        if (pessoaFiltro != null) {
            codigo = pessoaFiltro.getCodigo();
            cpf = pessoaFiltro.getCpf().trim();
            nome = pessoaFiltro.getNome().trim();
            if (codigo != null)
                sql.append(" AND codigo = ?");

            if (!nome.isEmpty())
                sql.append(" AND nome ILIKE ?");

            if (!cpf.isEmpty())
                sql.append(" AND cpf ILIKE ?");
        }

        if (fromDataNasc != null)
            sql.append(" AND datanascimento >= ?");

        if (toDataNasc != null)
            sql.append(" AND datanascimento <= ?");

        // sql.append("AND '[2014-02-01, 2030-03-01]'::daterange @> dataNascimento");
        sql.append(";");

        PreparedStatement pstmt = conexao.prepareStatement(sql.toString());
        int i = 1;

        if (codigo != null) {
            pstmt.setLong(i, codigo);
            i++;
        }

        if (!nome.isEmpty()) {
            pstmt.setString(i, '%' + nome + '%');
            i++;
        }

        if (!cpf.isEmpty()) {
            pstmt.setString(i, '%' + cpf + '%');
            i++;
        }

        if (fromDataNasc != null) {
            pstmt.setDate(i, Date.valueOf(fromDataNasc));
            i++;
        }

        if (toDataNasc != null) {
            pstmt.setDate(i, Date.valueOf(toDataNasc));
            i++;
        }

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            p = new Pessoa(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
            pessoas.add(p);
        }
        rs.close();
        pstmt.close();
        return pessoas;
    }

}