/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge
 */
@Entity
@Table(name = "subsectorespropios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subsectorespropio.findAll", query = "SELECT s FROM Subsectorespropio s")
    , @NamedQuery(name = "Subsectorespropio.findByCodSubsector", query = "SELECT s FROM Subsectorespropio s WHERE s.codSubsector = :codSubsector")
    , @NamedQuery(name = "Subsectorespropio.findByNomSubsector", query = "SELECT s FROM Subsectorespropio s WHERE s.nomSubsector = :nomSubsector")
    , @NamedQuery(name = "Subsectorespropio.findByCodSector", query = "SELECT s FROM Subsectorespropio s WHERE s.codSector = :codSector")})
public class Subsectorespropio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cod_subsector")
    private String codSubsector;
    @Size(max = 100)
    @Column(name = "nom_subsector")
    private String nomSubsector;
    @Size(max = 3)
    @Column(name = "cod_sector")
    private String codSector;

    public Subsectorespropio() {
    }

    public Subsectorespropio(String codSubsector) {
        this.codSubsector = codSubsector;
    }

    public String getCodSubsector() {
        return codSubsector;
    }

    public void setCodSubsector(String codSubsector) {
        this.codSubsector = codSubsector;
    }

    public String getNomSubsector() {
        return nomSubsector;
    }

    public void setNomSubsector(String nomSubsector) {
        this.nomSubsector = nomSubsector;
    }

    public String getCodSector() {
        return codSector;
    }

    public void setCodSector(String codSector) {
        this.codSector = codSector;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSubsector != null ? codSubsector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subsectorespropio)) {
            return false;
        }
        Subsectorespropio other = (Subsectorespropio) object;
        if ((this.codSubsector == null && other.codSubsector != null) || (this.codSubsector != null && !this.codSubsector.equals(other.codSubsector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelos.Subsectorespropio[ codSubsector=" + codSubsector + " ]";
    }
    
}
