package poov.controle_vacinacao.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import poov.controle_vacinacao.modelo.Vacina;
import poov.controle_vacinacao.modelo.dao.ConexaoFactory;
import poov.controle_vacinacao.modelo.dao.VacinaDAO;

public class TelaPrincipalController implements Initializable {

    @FXML
    private TableView<Vacina> tableViewVacina;

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
            buildTable(searchVacina);
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
        System.out.println("Editar");
        System.out.println(tableViewVacina.getSelectionModel().getSelectedItems());
    }

    // Remover vacina
    @FXML
    private Button buttonRemoverVacina;

    @FXML
    void onRemoverVacina(ActionEvent event) {
        System.out.println("Remover");
    }

    // Text fields/areas
    @FXML
    private TextArea textAreaDescricaoVacina;

    @FXML
    private TextField textFieldCodigoVacina;

    @FXML
    private TextField textFieldNomeVacina;

    private Stage stageTelaSecundaria;

    private final Connection conexao = ConexaoFactory.getConexao();

    private VacinaDAO dao;

    public TelaPrincipalController() {
        System.out.println("TelaPrincipalController criado");
    }

    void buildTable(Vacina searchVacina) {
        try {
            ObservableList<Vacina> tbList = tableViewVacina.getItems();
            tbList.clear();
            if (searchVacina == null)
                tbList.addAll(dao.buscarTodas());
            else
                tbList.addAll(dao.buscarComFiltro(searchVacina));
        } catch (SQLException e) {
            System.out.println("Erro na busca de dados " + e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dao = new VacinaDAO(conexao);
        tableColumnCodigoVacina.setCellValueFactory(new PropertyValueFactory<Vacina, Long>("codigo"));
        tableColumnNomeVacina.setCellValueFactory(new PropertyValueFactory<Vacina, String>("nome"));
        tableColumnDescricaoVacina.setCellValueFactory(new PropertyValueFactory<Vacina, String>("descricao"));
        buildTable(null);

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
