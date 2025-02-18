/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repositorio;

import Modelos.Mision;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jorge
 */
@Stateless
public class MisionFacade extends AbstractFacade<Mision> {

    @PersistenceContext(unitName = "JorgeManzanoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MisionFacade() {
        super(Mision.class);
    }
    
}
