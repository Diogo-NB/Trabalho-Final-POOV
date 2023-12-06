package poov.controle_vacinacao.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import poov.controle_vacinacao.modelo.Vacina;

public class TelaNovaVacinaController {

    // Text fields/areas vacina
    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private TextField textFieldNome;

    @FXML
    private Button buttonConfirmar;

    public TelaNovaVacinaController() {
        System.out.println("TelaNovaVacinaController criado");
    }

    @FXML
    private void onConfirmar() {
        Vacina vacina = new Vacina(textFieldNome.getText(), textAreaDescricao.getText());
        System.out.println("Nova:\n" + vacina);
    }

}
