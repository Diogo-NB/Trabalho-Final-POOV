package poov.controle_vacinacao.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import poov.controle_vacinacao.modelo.Vacina;
import poov.controle_vacinacao.modelo.dao.VacinaDAO;

public class TelaEditarVacinaController implements Initializable {

    // Text fields/areas vacina
    @FXML
    private TextField textFieldCodigo;

    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private TextField textFieldNome;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonResetar;

    private VacinaDAO vacinaDAO;

    public void setVacinaDAO(VacinaDAO vacinaDAO) {
        this.vacinaDAO = vacinaDAO;
    }

    @FXML
    public void onResetar() {
        setVacina(vacina);
    }

    public void limpar() {
        vacina = null;
        textFieldCodigo.setText("");
        textAreaDescricao.setText("");
        textFieldNome.setText("");
    }

    private Vacina vacina; // Vacina a ser editada

    public void setVacina(Vacina vacina) throws IllegalArgumentException {

        if (vacina == null)
            throw new IllegalArgumentException("Vacina inválida!");

        if (vacina.getCodigo() == null)
            throw new IllegalArgumentException("Vacina inválida!");

        this.vacina = vacina;
        textFieldCodigo.setText(vacina.getCodigo().toString());
        textAreaDescricao.setText(vacina.getDescricao());
        textFieldNome.setText(vacina.getNome());
    }

    @FXML
    private void onConfirmar(ActionEvent event) {

        Vacina vacinaEditada = new Vacina(vacina.getCodigo(), textFieldNome.getText(), textAreaDescricao.getText());

        if (vacinaEditada.getNome().isBlank()) {
            Alert alert = new Alert(AlertType.ERROR, "O campo de nome da vacina não pode ser vazio");
            alert.setTitle("Erro na edição da vacina");
            alert.showAndWait();
            textFieldNome.setText(vacina.getNome());
            return;
        }

        String alertText = "Confirmar edição?\nVacina modificada: \nNome: " + vacinaEditada.getNome() + "\nDescrição: "
                + vacinaEditada.getDescricao() + "\n";

        if (vacinaEditada.getDescricao().isBlank())
            alertText += "Observação: O campo de descrição da vacina está vazio, proceder mesmo assim?";

        Alert alert = new Alert(AlertType.CONFIRMATION, alertText, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    vacinaDAO.atualizar(vacinaEditada);
                    // Confirmação de sucesso para o usuário
                    (new Alert(AlertType.INFORMATION, "Vacina editada com sucesso!")).showAndWait();
                } catch (SQLException e) {
                    // Alertar sobre o erro para o usuário
                    (new Alert(AlertType.ERROR,
                            "Erro na modificação da vacina, certifique-se da conexão com o banco de dados"))
                            .showAndWait();
                } finally {
                    Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    s.close();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textAreaDescricao.setWrapText(true);
    }
}
