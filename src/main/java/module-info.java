module JavaFXApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens poov.controle_vacinacao to javafx.fxml, javafx.graphics;
    opens poov.controle_vacinacao.modelo to javafx.base, javafx.graphics;
    opens poov.controle_vacinacao.controller to javafx.fxml, javafx.graphics;
    exports poov.controle_vacinacao;
}
