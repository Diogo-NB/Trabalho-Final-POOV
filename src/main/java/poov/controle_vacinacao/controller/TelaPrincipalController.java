package poov.controle_vacinacao.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import poov.controle_vacinacao.modelo.Aplicacao;
import poov.controle_vacinacao.modelo.Pessoa;
import poov.controle_vacinacao.modelo.Vacina;
import poov.controle_vacinacao.modelo.dao.AplicacaoDAO;
import poov.controle_vacinacao.modelo.dao.ConexaoFactory;
import poov.controle_vacinacao.modelo.dao.DAOFactory;
import poov.controle_vacinacao.modelo.dao.PessoaDAO;
import poov.controle_vacinacao.modelo.dao.VacinaDAO;
import poov.controle_vacinacao.utils.TextFieldFormatters;

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

    // Pesquisar pessoa
    @FXML
    private Button buttonPesquisarPessoa;

    @FXML
    void onPesquisarPessoa(ActionEvent event) {
        Long codigo = null;
        try {
            codigo = Long.parseLong(textFieldCodigoPessoa.getText());
        } catch (NumberFormatException e) {
        } finally {
            Pessoa searchPessoa = new Pessoa(codigo, textFieldNomePessoa.getText(), textFieldCPFPessoa.getText(), null);
            buildPessoaTable(searchPessoa, datePickerDataNascimentoRange0Pessoa.getValue(),
                    datePickerDataNascimentoRange1Pessoa.getValue());
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

    // Text fields/areas pessoa
    @FXML
    private TextField textFieldCodigoPessoa;

    @FXML
    private TextField textFieldNomePessoa;

    @FXML
    private TextField textFieldCPFPessoa;

    @FXML
    private DatePicker datePickerDataNascimentoRange0Pessoa;

    @FXML
    private DatePicker datePickerDataNascimentoRange1Pessoa;

    // Aplicação
    @FXML
    private Button buttonCriarAplicacao;

    @FXML
    void onCriarAplicacao() {
        Pessoa pessoaSelecionada = tableViewPessoa.getSelectionModel().getSelectedItem();
        Vacina vacinaSelecionada = tableViewVacina.getSelectionModel().getSelectedItem();

        if (pessoaSelecionada != null && vacinaSelecionada != null) {
            System.out.println(pessoaSelecionada + "\n" + vacinaSelecionada);

            Aplicacao aplicacao = new Aplicacao(LocalDate.now(), pessoaSelecionada, vacinaSelecionada);

            Alert alert = new Alert(
                    AlertType.CONFIRMATION, "Deseja criar a seguinte aplicação?\n\nPessoa selecionada:\n"
                            + pessoaSelecionada.toString() + "\nVacina selecionada:\n" + vacinaSelecionada.toString(),
                    ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmação da criação de uma aplicação");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        aplicacaoDAO.gravar(aplicacao);
                        // Confirmação de sucesso para o usuário
                        (new Alert(AlertType.INFORMATION, "Aplicação criada com sucesso!")).showAndWait();
                    } catch (SQLException e) {
                        // Alertar sobre o erro para o usuário
                        (new Alert(AlertType.ERROR,
                                "Erro na criação na aplicação, certifique-se da conexão com o banco de dados"))
                                .showAndWait();
                    }
                }
            });
        } else {
            // Alertar de erro
            // Setando o texto para o alerta
            String alertText;
            if (pessoaSelecionada == null && vacinaSelecionada == null) {
                alertText = "Não foi selecionado uma pessoa e uma vacina para criar a aplicação";
            } else if (pessoaSelecionada == null) {
                alertText = "Não foi selecionado uma pessoa para criar a aplicação";
            } else {
                alertText = "Não foi selecionado uma vacina para criar a aplicação";
            }

            Alert alert = new Alert(AlertType.ERROR, alertText);
            alert.setTitle("Erro na criação da aplicação");
            alert.showAndWait();
        }
    }

    private Stage stageTelaNovaVacina;

    // DAO's e conexao para o banco de dados

    private final Connection conexao = ConexaoFactory.getConexao();

    private VacinaDAO vacinaDAO;

    private PessoaDAO pessoaDAO;

    private AplicacaoDAO aplicacaoDAO;

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

    void buildPessoaTable(Pessoa searchPessoa, LocalDate fromDataNascimento, LocalDate toDataNascimento) {
        try {
            ObservableList<Pessoa> tbList = tableViewPessoa.getItems();
            tbList.clear();
            if (searchPessoa == null && fromDataNascimento != null)
                tbList.addAll(pessoaDAO.buscarTodas());
            else
                tbList.addAll(pessoaDAO.buscarComFiltro(searchPessoa, fromDataNascimento, toDataNascimento));
        } catch (SQLException e) {
            DAOFactory.mostrarSQLException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialização dos DAOs
        vacinaDAO = new VacinaDAO(conexao);
        pessoaDAO = new PessoaDAO(conexao);
        aplicacaoDAO = new AplicacaoDAO(conexao);

        // Setando os formatters para os campos de texto
        textFieldCodigoPessoa.setTextFormatter(TextFieldFormatters.apenasDigitos());
        textFieldCodigoVacina.setTextFormatter(TextFieldFormatters.apenasDigitos());
        textFieldCPFPessoa.setTextFormatter(TextFieldFormatters.apenasDigitos());

        // Inicialização da tabela vacinas
        tableColumnCodigoVacina.setCellValueFactory(new PropertyValueFactory<Vacina, Long>("codigo"));
        tableColumnNomeVacina.setCellValueFactory(new PropertyValueFactory<Vacina, String>("nome"));
        tableColumnDescricaoVacina.setCellValueFactory(new PropertyValueFactory<Vacina, String>("descricao"));

        buildVacinaTable(null);

        // Inicialização da tabela pessoas
        tableColumnCodigoPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, Long>("codigo"));
        tableColumnNomePessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("nome"));
        tableColumnCPFPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("cpf"));
        tableColumnDataNascimentoPessoa
                .setCellValueFactory(new PropertyValueFactory<Pessoa, LocalDate>("dataNascimento"));

        // Alterar a formatação da data na coluna de data de nascimento
        tableColumnDataNascimentoPessoa.setCellFactory(col -> {
            TableCell<Pessoa, LocalDate> cell = new TableCell<Pessoa, LocalDate>() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    // Cleanup the cell before populating it
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        // Format the birth date in dd/MM/yyyy
                        String formattedDob = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(item);
                        this.setText(formattedDob);
                    }
                }
            };
            // cell.setStyle("-fx-alignment: CENTER");
            return cell;
        });

        tableColumnCodigoPessoa.setStyle("-fx-alignment: CENTER");
        tableColumnCodigoVacina.setStyle("-fx-alignment: CENTER");
        tableColumnDataNascimentoPessoa.setStyle("-fx-alignment: CENTER");

        buildPessoaTable(null, null, null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TelaNovaVacina.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stageTelaNovaVacina = new Stage();
            stageTelaNovaVacina.setScene(scene);
            stageTelaNovaVacina.setTitle("CRUD - Vacina");
            stageTelaNovaVacina.getIcons().add(new Image(getClass().getResourceAsStream("/images/java.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
