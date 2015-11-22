/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilexplox;

import chilexplox.classes.Empresa;
import chilexplox.classes.Mensaje;
import chilexplox.classes.Sucursal;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.sun.org.apache.xpath.internal.operations.Bool;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
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
 * @author Thomas Pryce Jones
 */
public class FXMLMensajesRecibidosController implements Initializable {
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

    Firebase postRef;
    Firebase newPostRef;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        emp = Empresa.getInstance();
        //----Agregar sucursales a choicebox----
        LoadSucursales();
        //----Agregar mensajes a listview----
        LoadMensajes();
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
                if (s.getDireccion().equals(direccionDestino)) 
                {
                    try{
                        postRef = emp.fbRef().child("sucursales").child(direccionDestino).child("mensajesRecibidos");
                        Mensaje mnsj = new Mensaje(contenido,urgencia);
                        newPostRef = postRef.push(); newPostRef.setValue(mnsj);
                        EnviarContenido.setText("");
                    }
                    catch(Exception e){}
                    
                }
            }
        }
        
        
    }
    
    private void LoadSucursales()
    {
        Firebase sucursalesRef = emp.fbRef().child("sucursales");
        sucursalesRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot ds, String previousChildKey) {
                Sucursal s = ds.getValue(Sucursal.class);
                EnviarDestino.getItems().add(s.getDireccion());
            }
            @Override
            public void onChildChanged(DataSnapshot ds, String string) {
                //Hay que hacer algo por si cambia nombre?
            }
            @Override
            public void onChildRemoved(DataSnapshot ds) {
                Sucursal s = ds.getValue(Sucursal.class);
                System.out.println("Sucursal REMOVIDA:" + s.toString());
                EnviarDestino.getItems().remove(s.getDireccion());
            }
            @Override
            public void onChildMoved(DataSnapshot ds, String string) {
                // Who cares... (No se requiere hacer nada)
            }
            @Override
            public void onCancelled(FirebaseError fe) {
                System.out.println("ERROR FB-101:" + fe.getMessage());
            }
        });/**/
    }
    
    private void LoadMensajes()
    {
        Firebase mensajesRef = emp.fbRef().child("sucursales").child(emp.getsucursalactual().getDireccion()).child("mensajesRecibidos");
        mensajesRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot ds, String previousChildKey) {
                Mensaje m = ds.getValue(Mensaje.class);
                if (m.getUrgente() == true) 
                {
                    Recibidos.getItems().add(0, "URGENTE " + m.getContenido()); // Invierto el orden, se agregan al principio
                }
                else
                {
                    Recibidos.getItems().add(0, m.getContenido()); // Invierto el orden, se agregan al principio
                }
                }
            @Override
            public void onChildChanged(DataSnapshot ds, String string) {
                //No se cambian los mensajes
            }
            @Override
            public void onChildRemoved(DataSnapshot ds) {
                //no implementamos borrar mensajes
            }
            @Override
            public void onChildMoved(DataSnapshot ds, String string) {
                // Who cares... (No se requiere hacer nada)
            }
            @Override
            public void onCancelled(FirebaseError fe) {
                System.out.println("ERROR FB-101:" + fe.getMessage());
            }
        });/**/
    }
}
