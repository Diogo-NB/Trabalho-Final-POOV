package poov.controle_vacinacao.modelo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

}
