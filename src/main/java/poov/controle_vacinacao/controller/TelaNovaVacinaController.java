package poov.controle_vacinacao.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TelaNovaVacinaController {

    // Text fields/areas vacina
    @FXML
    private TextArea textAreaDescricao;
    
    @FXML
    private TextField textFieldCodigo;
    
    @FXML
    private TextField textFieldNome;

    @FXML
    private Button buttonConfirmar;

    public TelaNovaVacinaController() {
        System.out.println("TelaNovaVacinaController criado");
    }

}
