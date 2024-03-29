package chilexplox.classes;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
* Representa un ingreso  único con fecha
*/
public class Ingreso {
    private int valor;
    private Date fecha;
    private String id;
    private String sucursal;
    private String empleado;
    
    public Ingreso() {} // Constructor vacío para Firebase
    
    public Ingreso(int valor, Date fecha, String sucursal, String empleado)
    {
        this.valor = valor;
        this.fecha = fecha;
        this.sucursal=sucursal;
        this.empleado=empleado;
    }
    public int getValor()
    {return this.valor;}
    public Date getFecha()
    {return this.fecha;}
    public String getId()
    {return this.id;}
    public String getSucursal()
    {return this.sucursal;}
    public String getEmpleado()
    {return this.empleado;}
    
    public void setId(String id)
    {this.id = id;}
    
    /**
     * Retorna "fecha->valor" del ingreso
     * @return String
     */
    @Override
    public String toString() {
        String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(this.fecha);
        return s +" ->"+ this.valor;
    }
}
