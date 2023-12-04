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

import poov.controle_vacinacao.modelo.Aplicacao;
import poov.controle_vacinacao.modelo.Pessoa;
import poov.controle_vacinacao.modelo.Vacina;

public class AplicacaoDAO {

    private final Connection conexao;

    public AplicacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public List<Aplicacao> buscarTodas() throws SQLException {
        Aplicacao a;
        Pessoa p;
        Vacina v;
        List<Aplicacao> aplicacoes = new ArrayList<>();
        String sql = "SELECT A.codigo, A.data, A.codigopessoa, P.nome, P.cpf, P.datanascimento, A.codigovacina, V.nome, V.descricao FROM aplicacao AS A INNER JOIN pessoa AS P ON A.codigopessoa = P.codigo INNER JOIN vacina AS V ON A.codigovacina = V.codigo WHERE A.situacao = 'ATIVO';";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            p = new Pessoa(rs.getLong(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate());
            v = new Vacina(rs.getLong(7), rs.getString(8), rs.getString(9));
            a = new Aplicacao(rs.getLong(1), rs.getDate(2).toLocalDate(), p, v);
            aplicacoes.add(a);
        }
        rs.close();
        stmt.close();
        return aplicacoes;
    }

    public void gravar(Aplicacao aplicacao) throws SQLException {

        String sql = "INSERT INTO aplicacao (data, codigopessoa, codigovacina) VALUES (?, ?, ?);";
        PreparedStatement pstmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pstmt.setDate(1, Date.valueOf(aplicacao.getData()));
        pstmt.setLong(2, aplicacao.getCodigoPessoa());
        pstmt.setLong(3, aplicacao.getCodigoVacina());

        if (pstmt.executeUpdate() == 1) {
            System.out.println("Insercao da aplicacao feita com sucesso");
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                aplicacao.setCodigo(rs.getLong(1));
            } else {
                System.out.println("Erro ao obter o codigo gerado pelo BD para a aplicacao");
            }
            rs.close();
        } else {
            System.out.println("Erro ao inserir a aplicacao");
        }
        pstmt.close();
    }

}