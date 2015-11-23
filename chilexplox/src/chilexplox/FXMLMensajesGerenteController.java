/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilexplox;


import chilexplox.classes.Empresa;
import chilexplox.classes.Mensaje;
import chilexplox.classes.Sucursal;
import com.sun.org.apache.xpath.internal.operations.Bool;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
/**
 * FXML Controller class
 *
 * @author Felix
 */
public class FXMLMensajesGerenteController implements Initializable {
Empresa emp;
    @FXML
    private ListView<String> Recibidos;
    @FXML
    private TextArea EnviarContenido;
    @FXML
    private Button Enviar;
    @FXML
    private CheckBox EnviarUrgente;
    @FXML
    private ChoiceBox<String> EnviarDestino;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    emp = Empresa.getInstance();
    EnviarDestino.getItems().add("Enviar a todos");
        for(Sucursal s: emp.getsucursales())
        {
            EnviarDestino.getItems().add(s.getDireccion());
        }
        for(Mensaje m: emp.getsucursalactual().getMensajesRecibidos())
        {
            if (m.getUrgente() == true) 
            {
                Recibidos.getItems().add(0, "URGENTE " + m.getContenido()); // Invierto el orden, se agregan al principio
            }
            else
            {
                Recibidos.getItems().add(0, m.getContenido()); // Invierto el orden, se agregan al principio
            }
            
        }
    }    
    
    @FXML
    private void btnEnviarMensaje(MouseEvent event) throws IOException
    {
        Boolean urgencia = EnviarUrgente.isSelected();
        String contenido = "[" + emp.getsucursalactual().getDireccion() + "] --- " + EnviarContenido.getText();
        String direccionDestino = EnviarDestino.getValue();
        if (direccionDestino != null & EnviarContenido.getText() != "") 
        {
            
            for(Sucursal s: emp.getsucursales())
            {
                if(direccionDestino.equals("Enviar a todos"))
                {
                emp.getempleadoactual().EnviarMensaje(s, contenido, urgencia);
                    EnviarContenido.setText("");
                }
                if (s.getDireccion().equals(direccionDestino)) 
                {
                    emp.getempleadoactual().EnviarMensaje(s, contenido, urgencia);
                    EnviarContenido.setText("");
                }
            }
        }
        
        
    }
    
    
}
