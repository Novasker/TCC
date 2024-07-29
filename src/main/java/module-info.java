module com.projeto.teste {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.projeto.teste to javafx.fxml;
    exports com.projeto.teste;
}