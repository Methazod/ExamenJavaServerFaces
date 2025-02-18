/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorio;

import Modelos.Tipoproyecto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge
 */
@Stateless
public class TipoproyectoFacade extends AbstractFacade<Tipoproyecto> {

    @PersistenceContext(unitName = "JorgeManzanoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoproyectoFacade() {
        super(Tipoproyecto.class);
    }
    
}
