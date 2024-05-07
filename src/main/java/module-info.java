module pl.gornicki.whaleidentificator {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.gornicki.whaleidentificator to javafx.fxml;
    exports pl.gornicki.whaleidentificator;
}