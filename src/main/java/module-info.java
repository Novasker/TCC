module com.projeto.teste {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.xml.dom;
    requires java.sql;


    opens com.projeto.teste to javafx.fxml;
    exports com.projeto.teste;
}