package poov.controle_vacinacao.utils;

import javafx.scene.control.TextFormatter;

public class TextFieldFormatters {

    /// Retorna um novo formatter de String que aceita apenas digitos
    public static TextFormatter<String> apenasDigitos() {
        return new TextFormatter<>(change -> {
            if (!change.isContentChange())
                return change;
            
            String text = change.getControlNewText();

            if (text.length() == 0) {  // Permite campo vazio
                return change;
            } else {  // verifica se o texto, com a mudança, é um long válido
                try {
                    Long.parseLong(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return change;
        });
    }
    
}
