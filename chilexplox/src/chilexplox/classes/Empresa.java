/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilexplox.classes;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter
 */
public class Empresa {
    
    //SINGLETON!! Clase maestra
    private final static Empresa instance = new Empresa();

    /**
     * Retorna la única instancia (singleton) de Empresa
     * @return
     */
    public static Empresa getInstance()
    {
        return instance;
    }
    private DateFormat dateFormat;
    private List<Sucursal> sucursales;
    private List<Camion> camiones;
    private List<Cliente> clientes;
    private List<Encomienda> encomiendas;
    private List<Empleado> empleados;
    private List<Boss> jefes;
    private Empleado empleadoActual;
    private Boss jefeActual;
    private Cliente clienteActual;
    private Sucursal sucursalActual;
    private Encomienda encomiendaTemporal;
    private int IDEncomienda = 0;
    private int IDPedido = 0;
    private Pedido pedidotemp;
    private List<Ingreso> ingresos;
    private List<Pedido> pedidos;
    private String empleadostring;
    private String sucursalstring;
    private Boolean edittemp;
    
    public Empresa()
    {
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.sucursales = new ArrayList();
        this.camiones = new ArrayList();
        this.empleados = new ArrayList();
        this.clientes = new ArrayList();
        this.jefes = new ArrayList();
        this.encomiendas = new ArrayList();
        this.ingresos = new ArrayList();
        this.pedidos = new ArrayList();
    }
    
    /**
     * Para obtener el formato de fecha.
     * Uso:
     * dateFormat.format(date) 
     * Entrega un string con la fecha en formato "yyyy/MM/dd HH:mm:ss"
     * @return DateFormat
     */
    
    public String getEmpleadoString()
    {
    return this.empleadostring;
    }
     public String getSucursalString()
    {
    return this.sucursalstring;
    }
     public void setSucursalString(String suc)
     {
     sucursalstring=suc;
     }
     public void setEmpleadoString(String emp)
     {
     empleadostring=emp;
     }
    public List<Ingreso> ingresos()
    {
        return this.ingresos;
    }
    public DateFormat dateFormat()
    {return this.dateFormat;}
    public List<Sucursal> getsucursales()
    {return sucursales;}
    public List<Boss> getjefes()
    {return jefes;}
    public Sucursal getsucursalactual()
    {
        //System.out.println("Sucursal actual: "+sucursalActual +"->"+ sucursalActual.getEncomiendasAlmacenadas());
        return sucursalActual;
    }
    public void setsucursalactual(Sucursal sucursal)
    {
        sucursalActual=sucursal;
        //System.out.println("Seteada Sucursal actual: "+sucursal +"->"+ sucursal.getEncomiendasAlmacenadas());
    }
    public Boolean getedittemp()
    {return edittemp;}
    public void setedittemp(Boolean asd)
    {edittemp = asd;}
    public Pedido getpedidotemp()
    {return pedidotemp;}
    public void setpedidotemp(Pedido pedido)
    {pedidotemp=pedido;}
    public List<Empleado> getempleados()
    {return empleados;}
    public List<Cliente> getclientes()
    {return clientes;}
    public Empleado getempleadoactual()
    {return empleadoActual;}
    public Boss getjefeactual()
    {return jefeActual;}
    public Cliente getclienteactual()
    {return clienteActual;}
    public void setempleadoactual(Empleado empleado)
    {empleadoActual=empleado;}
    public void setjefeactual(Boss jefe)
    {jefeActual=jefe;}
    public void setclienteactual(Cliente cliente)
    {clienteActual=cliente;}
    public List<Encomienda> getEncomiendas()
    {return encomiendas;}
    public List<Pedido> getPedidos()
    {return pedidos;}
    public List<Ingreso> getIngresos()
    {return ingresos;}
    public List<Camion> getCamiones()
    {return camiones;}
    public Encomienda getencomiendatemporal()
    {return encomiendaTemporal;}
    public void setencomiendatemporal(Encomienda encotempo)
    {encomiendaTemporal=encotempo;}
    public Encomienda getencomiendabyid(String id)
    {
        Encomienda temp = null;
        for(Encomienda en: encomiendas)
        {
            if (en.getId().equals(id)) 
            {
                temp =en;
            }
        } 
        return temp;
    }
    public Sucursal getSucursalConDireccion(String direccion)
    {
        for (Sucursal s: sucursales) 
        {
            if(s.getDireccion().equals(direccion))
            {return s;}
        }
        System.out.println("La dirección "+direccion+" no fue encontrada!");
        return null;
    }
    public Camion getCamionConNombre(String nombre)
    {
        for (Camion c: camiones) 
        {
            if(c.NombreCompleto().equals(nombre))
            {return c;}
        }
        System.out.println("El camión con nombre "+nombre+" no fue encontrado!");
        return null;
    }
    
    public List<String> getDireccionSucursales()
    {
        List<String> array = new ArrayList();
        for (Sucursal s: sucursales) 
        {
            array.add(s.getDireccion());
        }
        return array;
    }
    public void AddEmpleado(Empleado e)
    {
        this.empleados.add(e);
    }
     public void AddCliente(Cliente c)
    {
        this.clientes.add(c);
    }
      public void AddJefe(Boss b)
    {
        this.jefes.add(b);
    }
    public void addIngreso(Ingreso i)
    {
        this.ingresos.add(i);
    }
    public void addSucursal(Sucursal s)
    {
        this.sucursales.add(s);
    }
    public Camion EntregarCamion()
    {
        for(Camion c: camiones)
        {
            if (c.getDisponibilidad()==true)
            {return c;}
        }
        System.out.println("No hay camiones disponibles!");
        return null;
    }
    
    public Cliente BuscarPersona(String rut)
    {
        for(Cliente c: clientes)
        {
            if (c.getRut().equals(rut))
            {
                return c;
            }
        }
        System.out.println("No existe el cliente con rut: "+rut);
        return null;
    }
    
    public void CambiarEstadoEncomienda(String id, String estado)
    {
        for(Encomienda e: encomiendas)
        {
            if(e.getId().equals(id))
            {
                e.setestado(estado);
            }
        }
    }
    
    public String VerEstadoEncomienda(String id)
    {
        for(Encomienda e: encomiendas)
        {
            if(e.getId().equals(id))
            {
                String estado=e.getEstado();
                return estado;
            }
        }
        System.out.println("La encomienda "+id+" no fue encontrada!");
        return null;   
    }
    
    public String AsignarIDEnco()
    {
        int temp = IDEncomienda;
        IDEncomienda += 1;
        return Integer.toString(temp);
    }
    
    public String AsignarIDPedido()
    {
        int temp = IDPedido;
        IDPedido += 1;
        return Integer.toString(temp);
    }
    public Firebase fbRef()
    {
        Firebase myFirebaseRef = new Firebase("https://chilexplox.firebaseio.com/");
        return myFirebaseRef;
    }
    
    
   /* public int buscarmenoridencomienda()
    {
        int largo=encomiendas.size();
        int menorid=0;
        boolean esta=true;
        for(int i=0; i<largo;i++)
        {
        for(Encomienda e: encomiendas)
        {
           
        }
        }
    }*/
    

}
