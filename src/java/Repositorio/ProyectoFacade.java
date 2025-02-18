/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorio;

import Modelos.Cad;
import Modelos.Proyecto;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jorge
 */
@Stateless
public class ProyectoFacade extends AbstractFacade<Proyecto> {

    @PersistenceContext(unitName = "JorgeManzanoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProyectoFacade() {
        super(Proyecto.class);
    }
    
    public List<Integer> anosProyectos(){
        em = getEntityManager();
        Query q = em.createNamedQuery("Proyecto.getAnos");
        return q.getResultList();
    }
    
    public List<Proyecto> proyectosPorAnyo(Integer anyo){
        em = getEntityManager();
        Query q = em.createNamedQuery("Proyecto.findByAnyo").setParameter("anyo", anyo);
        return q.getResultList();
    }
    
    public List<Proyecto> proyectosPorPais(String pais){
        em = getEntityManager();
        Query q = em.createNamedQuery("Proyecto.findByPais").setParameter("pais", pais);
        return q.getResultList();
    }
    
    public List<Proyecto> proyectosPorAnyoYPais(Integer anyo, String pais){
        em = getEntityManager();
        Query q = em.createNamedQuery("Proyecto.porAnyoYPais").setParameter("elPais", pais).setParameter("elAnyo", anyo);
        return q.getResultList();
    }
    
    public Double getGrafica(Integer anyo){
        em = this.getEntityManager();
        Query q = em.createNamedQuery("Proyecto.grafica").setParameter("elAnyo", anyo);              
        return (Double)q.getSingleResult();
    }
    
    public String getCad(Integer codCad){
        em = this.getEntityManager();
        Query q = em.createNamedQuery("Cad.findNameByCodCad").setParameter("elCodigo", codCad);              
        return (String) q.getSingleResult();
    }
}
