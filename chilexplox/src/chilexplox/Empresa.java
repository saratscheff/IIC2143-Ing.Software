/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chilexplox;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter
 */
public class Empresa {
    public List<Sucursal> sucursales;
    public List<Camion> camiones;
    public List<Cliente> clientes;
    
    public Empresa()
    {
        this.sucursales = new ArrayList();
        this.camiones = new ArrayList();
        this.clientes = new ArrayList();
    }
    public Camion EntregarCamion()
    {
        for(Camion c: camiones)
        {
            if (c.disponibilidad==true)
            {return c;}
        }
        return null;
    }
    
    public Cliente buscarpersona(int rut)
    {
    for(Cliente c: clientes)
    {
    if (c.rut==rut)
    {
    return c;
    }
    }
    return null;
    }
}