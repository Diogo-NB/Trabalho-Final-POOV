package poov.controle_vacinacao.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import poov.controle_vacinacao.modelo.Pessoa;
import poov.controle_vacinacao.modelo.Vacina;
import poov.controle_vacinacao.modelo.dao.ConexaoFactory;
import poov.controle_vacinacao.modelo.dao.DAOFactory;
import poov.controle_vacinacao.modelo.dao.PessoaDAO;
import poov.controle_vacinacao.modelo.dao.VacinaDAO;

public class TelaPrincipalController implements Initializable {

    // Tabela pessoas
    @FXML
    private TableView<Pessoa> tableViewPessoa;

    // Colunas da tabela pessoas
    @FXML
    private TableColumn<Pessoa, Long> tableColumnCodigoPessoa;

    @FXML
    private TableColumn<Pessoa, String> tableColumnNomePessoa;

    @FXML
    private TableColumn<Pessoa, String> tableColumnCPFPessoa;

    @FXML
    private TableColumn<Pessoa, LocalDate> tableColumnDataNascimentoPessoa;

    // Tabela vacinas
    @FXML
    private TableView<Vacina> tableViewVacina;

    // Colunas da tabela vacinas
    @FXML
    private TableColumn<Vacina, Long> tableColumnCodigoVacina;

    @FXML
    private TableColumn<Vacina, String> tableColumnNomeVacina;

    @FXML
    private TableColumn<Vacina, String> tableColumnDescricaoVacina;

    // Pesquisar vacina
    @FXML
    private Button buttonPesquisarVacina;

    @FXML
    void onPesquisarVacina(ActionEvent event) {
        Long codigo = null;
        try {
            codigo = Long.parseLong(textFieldCodigoVacina.getText());
        } catch (NumberFormatException e) {
        } finally {
            Vacina searchVacina = new Vacina(codigo, textFieldNomeVacina.getText(), textAreaDescricaoVacina.getText());
            // searchVacina.setNome(textFieldNomeVacina.getText());
            // searchVacina.setDescricao(textAreaDescricaoVacina.getText());
            // System.out.println("Vacina de filtro: " + searchVacina);
            buildVacinaTable(searchVacina);
        }
    }

    // Nova vacina
    @FXML
    private Button buttonNovaVacina;

    @FXML
    void onNovaVacina(ActionEvent event) {
        System.out.println("Nova");
    }

    // Editar vacina
    @FXML
    private Button buttonEditarVacina;

    @FXML
    void onEditarVacina(ActionEvent event) {
        // System.out.println("Editar");
        // System.out.println(tableViewVacina.getSelectionModel().getSelectedItems());
        PessoaDAO dao = new PessoaDAO(conexao);
        try {
            System.out.println(dao.buscarComFiltro(new Pessoa("barbosa", "", null)));
        } catch (SQLException e) {
            DAOFactory.mostrarSQLException(e);
        }
    }

    // Remover vacina
    @FXML
    private Button buttonRemoverVacina;

    @FXML
    void onRemoverVacina(ActionEvent event) {
        System.out.println("Remover");
    }

    // Text fields/areas vacina
    @FXML
    private TextArea textAreaDescricaoVacina;

    @FXML
    private TextField textFieldCodigoVacina;

    @FXML
    private TextField textFieldNomeVacina;

    private Stage stageTelaSecundaria;

    private final Connection conexao = ConexaoFactory.getConexao();

    private VacinaDAO vacinaDAO;

    private PessoaDAO pessoaDAO;

    public TelaPrincipalController() {
        System.out.println("TelaPrincipalController criado");
    }

    void buildVacinaTable(Vacina searchVacina) {
        try {
            ObservableList<Vacina> tbList = tableViewVacina.getItems();
            tbList.clear();
            if (searchVacina == null)
                tbList.addAll(vacinaDAO.buscarTodas());
            else
                tbList.addAll(vacinaDAO.buscarComFiltro(searchVacina));
        } catch (SQLException e) {
            DAOFactory.mostrarSQLException(e);
        }
    }

    void buildPessoaTable(Pessoa searchPessoa) {
        try {
            ObservableList<Pessoa> tbList = tableViewPessoa.getItems();
            tbList.clear();
            if (searchPessoa == null)
                tbList.addAll(pessoaDAO.buscarTodas());
            else
                tbList.addAll(pessoaDAO.buscarComFiltro(searchPessoa));
        } catch (SQLException e) {
            DAOFactory.mostrarSQLException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialização dos DAOs
        vacinaDAO = new VacinaDAO(conexao);
        pessoaDAO = new PessoaDAO(conexao);

        // Inicialização da tabela vacinas  
        tableColumnCodigoVacina.setCellValueFactory(new PropertyValueFactory<Vacina, Long>("codigo"));
        tableColumnNomeVacina.setCellValueFactory(new PropertyValueFactory<Vacina, String>("nome"));
        tableColumnDescricaoVacina.setCellValueFactory(new PropertyValueFactory<Vacina, String>("descricao"));
        buildVacinaTable(null);

        // Inicialização da tabela vacinas  
        tableColumnCodigoPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, Long>("codigo"));
        tableColumnNomePessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("nome"));
        tableColumnCPFPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("cpf"));
        tableColumnDataNascimentoPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, LocalDate>("dataNascimento"));
        buildPessoaTable(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaSecundaria.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stageTelaSecundaria = new Stage();
            stageTelaSecundaria.setScene(scene);
            stageTelaSecundaria.setTitle("CRUD - Vacina");
            stageTelaSecundaria.getIcons().add(new Image(getClass().getResourceAsStream("/images/java.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
