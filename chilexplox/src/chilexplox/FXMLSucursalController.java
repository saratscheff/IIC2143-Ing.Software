/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilexplox;

import chilexplox.classes.Camion;
import chilexplox.classes.Empresa;
import chilexplox.classes.Encomienda;
import chilexplox.classes.Mensaje;
import chilexplox.classes.Sucursal;
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
import ciruman.EllipsisListCell;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.Iterator;
import java.util.List;
import javafx.util.Callback;

//Listener
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxResult;
import se.mbaeumer.fxmessagebox.MessageBoxType;

/**
 * FXML Controller class
 *
 * @author Thomas Pryce Jones
 */
public class FXMLSucursalController implements Initializable {
    @FXML
    private Label LabelNombreTrabajador;
    @FXML
    private Label Urgencia;
    @FXML
    private Label LabelRecibidosCamiones;
    @FXML
    private ComboBox<Camion> ChoiceBoxCamiones;
    @FXML
    private ComboBox<Sucursal> ChoiceBoxSucursales;
    @FXML
    private ChoiceBox<Sucursal> ChoiceBoxDestinoCamiones;
    @FXML
    private Button IngresarPedido;
    @FXML
    private Button PasarACola;
    @FXML
    private Button VerPedidosRecibidos;
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
    private Button EnviarCamion;
    @FXML
    private Button CargarSucursal;
    @FXML
    private Button EntregarEncomienda;
    @FXML
    private Button QuitarEncomiendaCamion;
    @FXML
    private ListView ListMessagesPreview; //TIENE CELL-FACTORY!! No convertir a ListView<String>!!
    @FXML
    private ListView<String> EncomiendasEnSucursal;
    @FXML
    private ListView<String> EncomiendasRecibidas;
    @FXML
    private Label ErrorLabelSucursal;
    @FXML
    private Label LabelSucursal;
    @FXML
    private ProgressBar ProgressBarCapacity;

    Empresa emp;
    Camion camionActual;
    double espacioCamion = -1;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get singleton empresa
        emp = Empresa.getInstance();
        // Set welcome message
        String bienvenida = "Hola, " + emp.getempleadoactual().getnombre();
        LabelNombreTrabajador.setText(bienvenida);
        // Update sucursales
        for (Sucursal s: emp.getsucursales()) 
        {
            ChoiceBoxSucursales.getItems().add(s);
            ChoiceBoxDestinoCamiones.getItems().add(s);
        }
        // Ocultar botones deshabilitados
        QuitarEncomiendaCamion.setVisible(false);
        VerPedidosRecibidos.setVisible(false);
    }
    
    
    
    public void UpdateConSucursal(){
        // CARGAR ENCOMIENDAS
        EncomiendasEnSucursal.getItems().clear();
        Boolean boolurgencia = false;
        for(Encomienda en: emp.getsucursalactual().getEncomiendasAlmacenadas())
        {
            if (en.getPrioridad() == "Urgente") 
            {
                boolurgencia = true;
            }
            EncomiendasEnSucursal.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion()+" Tipo: "+en.getTipo());
        }
        if (boolurgencia == false) 
        {
            Urgencia.setText(null);
        }
        if (boolurgencia) 
        {
            Urgencia.setText("Hay una encomienda urgente!");
            boolurgencia = false;
        }
        
        // CARGAR ENCOMIENDAS RECIBIDAS
        EncomiendasRecibidas.getItems().clear();
        for(Encomienda en: emp.getsucursalactual().getEncomiendasRecibidas())
        {
            EncomiendasRecibidas.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion());
        }
        // CARGAR PREVIEW MENSAJES!! (Agregar un timer de sincronización?)
        ListMessagesPreview.getItems().clear();
        for(Mensaje m: emp.getsucursalactual().getMensajesRecibidos())
        {
            if (m.getUrgente() == true) 
            {
                String mensajePreview ="URGENTE " + m.getContenido();
                String[] mpArray = mensajePreview.split("\\r?\\n");
                if (mpArray.length > 1) { mensajePreview = mpArray[0]+"...";} // Solo la primera linea
                ListMessagesPreview.getItems().add(0, mensajePreview); // Añado al principio
            }
            else
            {
                String mensajePreview = m.getContenido();
                String[] mpArray = mensajePreview.split("\\r?\\n");
                if (mpArray.length > 1) { mensajePreview = mpArray[0]+"...";} // Solo la primera linea
                ListMessagesPreview.getItems().add(0, mensajePreview); // Añado al principio
            }
        }
        ListMessagesPreview.setCellFactory(new Callback<ListView<String>, EllipsisListCell>() {
            @Override
            public EllipsisListCell call(ListView<String> p) {
                EllipsisListCell cell = new EllipsisListCell();
                return cell;
            }
        });
        // CARGAR CAMIONES DISPONIBLES
        espacioCamion = -1;
        ChoiceBoxCamiones.getItems().clear();
        for (Camion c: emp.getsucursalactual().getCamionesEstacionados()) 
        {
            ChoiceBoxCamiones.getItems().add(c);
        }/**/
    }
    
    @FXML
    private void CargarVentanaInformes()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLVerInforme.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            FXMLVerInformeController controller = fxmlLoader.<FXMLVerInformeController>getController();
            controller.setEmpresa(emp);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));  
            stage.show();
        }
        catch (Exception e){
            System.out.println("ERROR 101: " + e.toString());
        }
    }
    
    @FXML
    private void EntregarEncomiendaAction(){
        String enco = EncomiendasRecibidas.getSelectionModel().getSelectedItem();
        String encomiendaID = EncomiendasRecibidas.getSelectionModel().getSelectedItem().split("#")[1]; // Obtengo el id
        Encomienda encomienda = null;
        try
        {
            encomienda = emp.getsucursalactual().getEncomienda(encomiendaID);
        } 
        catch (Exception e)
        {
            return; //Nada
        }
        emp.getsucursalactual().getEncomiendasRecibidas().remove(encomienda);
        EncomiendasRecibidas.getItems().remove(enco);
    }
    
    @FXML
    private void RefreshProgressBarAction(){
       
        Camion camionSeleccionado = ChoiceBoxCamiones.getValue();
        
        // Reviso que haya seleccion al momento de llamar al metodo
        if (camionSeleccionado == null) { return; }
        
        camionActual = camionSeleccionado;
        espacioCamion = camionSeleccionado.PorcentajeDisponible();

        //Stufffff to do
        ProgressBarCapacity.setProgress(espacioCamion);
         if (espacioCamion<0.7)
         {
              ProgressBarCapacity.setStyle("-fx-accent: green;");
         }
         else if (espacioCamion<0.85)
         {
              ProgressBarCapacity.setStyle("-fx-accent: yellow;");
         }
         else
         {
              ProgressBarCapacity.setStyle("-fx-accent: red;");
         }
    }
    
    @FXML
    private void btnCargarSucursal() throws IOException{
        Sucursal s = ChoiceBoxSucursales.getValue();
        emp.setsucursalactual(s);
        UpdateConSucursal();
        ErrorLabelSucursal.setText("");
        LabelSucursal.setText(emp.getsucursalactual().getDireccion());
    }
    
    @FXML
    private void CargarCamionAction() throws IOException{
        String encomiendaID = EncomiendasEnSucursal.getSelectionModel().getSelectedItem().split("#")[1]; // Obtengo el id
        Encomienda encomienda = null;
        try
        {
            encomienda = emp.getsucursalactual().getEncomienda(encomiendaID);
        } 
        catch (Exception e)
        {
            System.out.println("No hay encomienda seleccionada");
            return; //Nada
        }
        
        try
        {
            //OJO con la importancia de que el id sea un numero valido
            // Obtener Camion a Enviar
            Camion camion = ChoiceBoxCamiones.getValue();
            System.out.println(camion + "--AAAAAAAAAAAAAAAAAAA--"+encomienda);
            if (camion != null && encomienda != null)// aca
            {
                if(camion.getTipo()==encomienda.getTipo())
                {
                espacioCamion = camion.PorcentajeDisponible();
                if (espacioCamion>=1.0){ return; }

                //CARGAR CAMION!!

                encomienda.setestado("En Camión") ;
                camion.addencomienda(encomienda);
                emp.getsucursalactual().getEncomiendasAlmacenadas().remove(encomienda);

                //Recargar Encomiendas
                EncomiendasEnSucursal.getItems().clear();
                Boolean boolurgencia = false;

                for(Encomienda en: emp.getsucursalactual().getEncomiendasAlmacenadas())
                {
                    if (en.getPrioridad() == "Urgente") 
                    {
                        boolurgencia = true;
                    }
                    EncomiendasEnSucursal.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion()+" Tipo: "+en.getTipo());
                }
                if (boolurgencia == false) 
                {
                    Urgencia.setText(null);
                }
                if (boolurgencia) 
                {
                    Urgencia.setText("Hay una encomienda urgente!");
                    boolurgencia = false;
                }

                //Actualizar capacidad
                espacioCamion = camion.PorcentajeDisponible();
                // RECICLAR ESTE CODIGO DESPUES!!! ¡¡¡¡DRY!!!!
                ProgressBarCapacity.setProgress(espacioCamion);
                if (espacioCamion<0.7)
                {
                     ProgressBarCapacity.setStyle("-fx-accent: green;");
                }
                else if (espacioCamion<0.85)
                {
                     ProgressBarCapacity.setStyle("-fx-accent: yellow;");
                }
                else
                {
                     ProgressBarCapacity.setStyle("-fx-accent: red;");
                }
            }
            else
            {
                MessageBox mb = new MessageBox("Solo se puede cargar una encomienda \n tipo "+ encomienda.getTipo()+
                        " en un camion "+encomienda.getTipo()+"!", MessageBoxType.OK_ONLY);
                mb.showAndWait();
                
                return;}//podria hacer un messagebox que no coincide tipo
            }
        }
        catch (Exception e)
        {
            System.out.println("ERROR 101: " + e.toString());
        }
    }
    
    @FXML
    private void EnviarCamionAction() throws IOException{
        // Obtener Camion a Enviar
        Camion camion = ChoiceBoxCamiones.getValue();
        if (camion != null)
        {
            // Obtener Sucursal destino
            Sucursal destinoSucursal = ChoiceBoxDestinoCamiones.getValue();
            // Enviar Camion
            destinoSucursal.getCamionesEstacionados().add(camion);
            emp.getsucursalactual().getCamionesEstacionados().remove(camion);
            // Descargar encomiendas en destino (Inmediato por ahora)
            for (Encomienda e: camion.getEncomiendas())
            {
                e.setestado("En Destino");
                destinoSucursal.getEncomiendasRecibidas().add(e);
            }
            camion.borrarencomiendas();/**/
            }
        
        ChoiceBoxDestinoCamiones.getSelectionModel().clearSelection();
        // CARGAR CAMIONES DISPONIBLES
        ChoiceBoxCamiones.getItems().clear();
        for (Camion c: emp.getsucursalactual().getCamionesEstacionados()) 
        {
            ChoiceBoxCamiones.getItems().add(c);
        }/**/
        espacioCamion = -1;
        ProgressBarCapacity.setProgress(-1); // -1 para indeterminado
    }
    
    
    @FXML
    private void IngresarPedidoAction() throws IOException{
        if (ChoiceBoxSucursales.getValue() == null)
        {
            ErrorLabelSucursal.setText("¡Debes seleccionar una sucursal!");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLIngresoPedido.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            FXMLIngresoPedidoController controller = fxmlLoader.<FXMLIngresoPedidoController>getController();
            controller.setSucursalController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));  
            stage.show();
        } catch (Exception e){
            System.out.println("ERROR 102: " + e.toString());
        }
    }
    
    @FXML
    private void MensajesAction() throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLMensajesRecibidos.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));  
            stage.show();
        } catch (Exception e){
            ErrorLabelSucursal.setText("¡Debes seleccionar una sucursal!");
        }
    }
    
    @FXML
    private void VerCamionAction() throws IOException{
        // Obtener Camion a Revisar
        Camion camion = ChoiceBoxCamiones.getValue();
        LabelRecibidosCamiones.setText("Camion: "+camion.getNombre());
        NotificarErrorPedido.setVisible(false);
        EntregarEncomienda.setVisible(false);
        PasarACola.setVisible(false);
        QuitarEncomiendaCamion.setVisible(true);
        VerPedidosRecibidos.setVisible(true);
        EncomiendasRecibidas.getItems().clear();
        for(Encomienda en: camion.getEncomiendas())
        {
            EncomiendasRecibidas.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion());
        }
    }
    
    @FXML
    private void VerPedidosRecibidosAction() throws IOException{
        LabelRecibidosCamiones.setText("Pedidos Recibidos");
        NotificarErrorPedido.setVisible(true);
        EntregarEncomienda.setVisible(true);
        PasarACola.setVisible(true);
        QuitarEncomiendaCamion.setVisible(false);
        VerPedidosRecibidos.setVisible(false);
        EncomiendasRecibidas.getItems().clear();
        for(Encomienda en: emp.getsucursalactual().getEncomiendasRecibidas())
        {
            EncomiendasRecibidas.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion());
        }
    }
    
    @FXML
    private void NotificarErrorAction() throws IOException{
        String encomiendaID = EncomiendasRecibidas.getSelectionModel().getSelectedItem().split("#")[1]; // Obtengo el id
        Encomienda encomienda = null;
        try
        {
            encomienda = emp.getsucursalactual().getEncomienda(encomiendaID);
        } 
        catch (Exception e)
        {
            return; //Nada
        }
        String mensaje = "Se ha detectado error en la encomienda ID #"+encomienda.getId()+"#";
        emp.getempleadoactual().EnviarMensaje(emp.getsucursalcondir(encomienda.getOrigen()), mensaje, true);
    }
    
    @FXML
    private void QuitarEncomiendaCamionAction() throws IOException{
        String encomiendaID = EncomiendasRecibidas.getSelectionModel().getSelectedItem().split("#")[1]; // Obtengo el id
        Encomienda encomienda = null;
        Camion camion = ChoiceBoxCamiones.getValue();
        for(Encomienda en: camion.getEncomiendas())
        {
            if (en.getId() == encomiendaID) 
            {
                encomienda = en;
            }
        }
        if (camion != null && encomienda != null)
        {
            espacioCamion = camion.PorcentajeDisponible();
            if (espacioCamion>=1.0){ return; }
            
            //CARGAR CAMION!!
            encomienda.setestado("En cola");
            camion.borrarencomienda(encomienda);
            emp.getsucursalactual().getEncomiendasAlmacenadas().add(encomienda);
            //Recargar Encomiendas
            EncomiendasEnSucursal.getItems().clear();
            Boolean boolurgencia = false;
            for(Encomienda en: emp.getsucursalactual().getEncomiendasAlmacenadas())
            {
                if ("Urgente".equals(en.getPrioridad())) 
                {
                    boolurgencia = true;
                }
                EncomiendasEnSucursal.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion()+" Tipo: "+en.getTipo());
            }
            if (boolurgencia == false) 
            {
                Urgencia.setText(null);
            }
            if (boolurgencia) 
            {
                Urgencia.setText("Hay una encomienda urgente!");
                boolurgencia = false;
            }
            
            EncomiendasRecibidas.getItems().clear();
            for(Encomienda en: camion.getEncomiendas())
            {
                EncomiendasRecibidas.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion());
            }
            //Actualizar capacidad
            espacioCamion = camion.PorcentajeDisponible();
            // RECICLAR ESTE CODIGO DESPUES!!! ¡¡¡¡DRY!!!!
            ProgressBarCapacity.setProgress(espacioCamion);
            if (espacioCamion<0.7)
            {
                 ProgressBarCapacity.setStyle("-fx-accent: green;");
            }
            else if (espacioCamion<0.85)
            {
                 ProgressBarCapacity.setStyle("-fx-accent: yellow;");
            }
            else
            {
                 ProgressBarCapacity.setStyle("-fx-accent: red;");
            }
        }
    }
    
    @FXML
    private void PasarAColaAction() throws IOException{
        String encomiendaID = EncomiendasRecibidas.getSelectionModel().getSelectedItem().split("#")[1]; // Obtengo el id
        Encomienda encomienda = null;
        try
        {
            encomienda = emp.getsucursalactual().getEncomienda(encomiendaID);
            
        } 
        catch (Exception e)
        {
            return; //Nada
        }
        EncomiendasEnSucursal.getItems().add("["+encomienda.getPrioridad()+"]" + "(" + encomienda.getEstado() +")" + "// " + "ID: #" + encomienda.getId() + "# Destino: " + emp.getsucursalcondir(encomienda.getDestino()).getDireccion()+" Tipo: "+encomienda.getTipo());
        emp.getsucursalactual().getEncomiendasAlmacenadas().add(encomienda);
        emp.getsucursalactual().getEncomiendasRecibidas().remove(encomienda);
        //Recargar Encomiendas
        EncomiendasEnSucursal.getItems().clear();
        Boolean boolurgencia = false;
        for(Encomienda en: emp.getsucursalactual().getEncomiendasAlmacenadas())
        {
            if (en.getPrioridad() == "Urgente") 
            {
                boolurgencia = true;
            }
            EncomiendasEnSucursal.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion()+" Tipo: "+encomienda.getTipo());
        }
        if (boolurgencia == false) 
        {
            Urgencia.setText(null);
        }
        if (boolurgencia) 
        {
            Urgencia.setText("Hay una encomienda urgente!");
            boolurgencia = false;
        }
        
        EncomiendasRecibidas.getItems().clear();
        for(Encomienda en: emp.getsucursalactual().getEncomiendasRecibidas())
        {
            EncomiendasRecibidas.getItems().add("["+en.getPrioridad()+"]" + "(" + en.getEstado() +")" + "// " + "ID: #" + en.getId() + "# Destino: " + emp.getsucursalcondir(en.getDestino()).getDireccion());
        }   
    }
    
    @FXML
    private void ModificarPedidoAction() throws IOException{
        try
        {
            String encomiendaID = EncomiendasEnSucursal.getSelectionModel().getSelectedItem().split("#")[1]; // Obtengo el id
            Encomienda encomienda = null;
            encomienda = emp.getsucursalactual().getEncomienda(encomiendaID);
            if (emp.getsucursalactual() == emp.getsucursalcondir(encomienda.getOrigen())) 
            {
                emp.setencomiendatemporal(encomienda);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLModificarPedido.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                FXMLModificarPedidoController controller = fxmlLoader.<FXMLModificarPedidoController>getController();
                controller.setSucursalController(this);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.show();
            }
            if(emp.getsucursalactual() != emp.getsucursalcondir(encomienda.getOrigen()))
            {
                MessageBox mb = new MessageBox("Solo se puede editar desde la sucursal de origen!", MessageBoxType.OK_CANCEL);
                mb.showAndWait();
                if (mb.getMessageBoxResult() == MessageBoxResult.OK)
                {
                        System.out.println("OK");
                        
                }
                else
                {
                        System.out.println("Cancel");
                }
            }
            
        } 
        catch (Exception e)
        {
            //Nada
        }
    }
}
