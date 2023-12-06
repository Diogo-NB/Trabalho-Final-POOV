package poov.controle_vacinacao.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class TelaNovaVacinaController {

    // Text fields/areas vacina
    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private TextField textFieldNome;

    @FXML
    private Button buttonConfirmar;

    private VacinaDAO vacinaDAO;

    public void setVacinaDAO(VacinaDAO vacinaDAO) {
        this.vacinaDAO = vacinaDAO;
    }

    public TelaNovaVacinaController() {
        System.out.println("TelaNovaVacinaController criado");
    }

    public void limparFields(){
        textAreaDescricao.setText("");
        textFieldNome.setText("");
    }

    @FXML
    private void onConfirmar(ActionEvent event) {
        Vacina vacina = new Vacina(textFieldNome.getText(), textAreaDescricao.getText());

        if (vacina.getNome().isBlank()) {
            Alert alert = new Alert(AlertType.ERROR, "O campo de nome da vacina não pode ser vazio");
            alert.setTitle("Erro na criação da vacina");
            alert.showAndWait();
            return;
        }

        String alertText = "Deseja criar a seguinte vacina?\nNome: " + vacina.getNome() + "\nDescrição: "
                + vacina.getDescricao() + "\n";

        if (vacina.getDescricao().isBlank())
            alertText += "Observação: O campo de descrição da vacina está vazio, proceder mesmo assim?";

        Alert alert = new Alert(AlertType.CONFIRMATION, alertText, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    vacinaDAO.gravar(vacina);
                    // Confirmação de sucesso para o usuário
                    (new Alert(AlertType.INFORMATION, "Vacina criada com sucesso!")).showAndWait();
                } catch (SQLException e) {
                    // Alertar sobre o erro para o usuário
                    (new Alert(AlertType.ERROR,
                            "Erro na criação da vacina, certifique-se da conexão com o banco de dados"))
                            .showAndWait();
                } finally {
                    Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    s.close();
                }
            }
        });
    }

}
