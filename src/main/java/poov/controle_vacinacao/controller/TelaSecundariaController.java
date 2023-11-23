package poov.controle_vacinacao.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import poov.controle_vacinacao.modelo.Vacina;

public class TelaSecundariaController {
    
    @FXML
    private Button buttonPesquisar;

    @FXML
    private Button buttonNova;

    @FXML
    private Button buttonEditar;

    @FXML
    private Button buttonRemover;

    @FXML
    private TextArea textAreaDescricao;

    @FXML
    private TextField textFieldCodigo;

    @FXML
    private TextField textFieldNome;

    private boolean valido = false;

    private Vacina vacina;

    public TelaSecundariaController() {
        System.out.println("TelaSecundariaController criado");
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
        textFieldCodigo.setText(String.valueOf(vacina.getCodigo()));
        textFieldNome.setText(vacina.getNome());
        textAreaDescricao.setText(vacina.getDescricao());
    }

    public Vacina getVacina() {
        return vacina;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public void limpar() {
        valido = false;
        vacina = new Vacina();
        textFieldCodigo.setText("");
        textFieldNome.setText("");
        textAreaDescricao.setText("");
    }

    private boolean validarCampos() {
        return !textFieldCodigo.getText().isEmpty() &&
               !textFieldNome.getText().isEmpty() &&
               !textAreaDescricao.getText().isEmpty();
    }

    @FXML
    void buttonOkClicado(ActionEvent event) {
        if (validarCampos()) {
            vacina = new Vacina();
            vacina.setCodigo(Long.parseLong(textFieldCodigo.getText()));
            vacina.setNome(textFieldNome.getText());
            vacina.setDescricao(textAreaDescricao.getText());
            valido = true;
            ((Button)event.getSource()).getScene().getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Um dos campos está vazio");
            alert.showAndWait();
        }
    }

    @FXML
    void buttonCancelarClicado(ActionEvent event) {
        valido = false;
        ((Button)event.getSource()).getScene().getWindow().hide();
    }
}
