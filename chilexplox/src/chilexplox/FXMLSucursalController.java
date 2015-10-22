/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilexplox;
import chilexplox.classes.*;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Thomas Pryce Jones
 */
public class FXMLSucursalController implements Initializable {
    @FXML
    private Label LabelNombreTrabajador;
    @FXML
    private ChoiceBox<Sucursal> Sucursales;
    @FXML
    private Button IngresarPedido;
    @FXML
    private Button Mensajes;
    @FXML
    private Button ModificarPedido;
    @FXML
    private Button NotificarErrorPedido;
    @FXML
    private Label LabelCamionesDisponibles;
    @FXML
    private Button CargarCamion;
    @FXML
    private Button DescargarPedido;
    @FXML
    private ChoiceBox<Camion> Camiones;
    @FXML
    private Button EnviarCamion;
    @FXML
    private ListView ListMessagesPreview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*EnviarCamion.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            populateMessages();
            }
        });*/
    }  
   
    /**
     * prueba 1 rellenar listview con preview de mensajes
     */
    @FXML
    public void populateMessages(){
        ObservableList<String> messages = FXCollections.observableArrayList(
             "IMPORTANTE: Tienes que hacer que...",
             "El paquete de la semana pasada n...",
             "Cuando creen que vaya a llegar e...",
             "IMPORTANTE: EL TIPO DE AYER ERA ...",
             "Jesus está vivo, crean en el y y..."
        );
        ListMessagesPreview.setItems(messages);
    }
    
    @FXML
    private void IngresarPedidoAction(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLIngresoPedido.fxml"));
        Parent root2 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root2));  
        stage.show();
    }
    
}
