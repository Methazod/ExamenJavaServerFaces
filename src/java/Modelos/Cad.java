/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jorge
 */
@Entity
@Table(name = "cads")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cad.findAll", query = "SELECT c FROM Cad c")
    , @NamedQuery(name = "Cad.findByCodCad", query = "SELECT c FROM Cad c WHERE c.codCad = :codCad")
    , @NamedQuery(name = "Cad.findNameByCodCad", query = "SELECT c.nomCad FROM Cad c WHERE c.codCad = :elCodigo")
    , @NamedQuery(name = "Cad.findByNomCad", query = "SELECT c FROM Cad c WHERE c.nomCad = :nomCad")})
public class Cad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cad")
    private Integer codCad;
    @Size(max = 100)
    @Column(name = "nom_cad")
    private String nomCad;
    @OneToMany(mappedBy = "codCad")
    private List<Cadsub> cadsubList;

    public Cad() {
    }

    public Cad(Integer codCad) {
        this.codCad = codCad;
    }

    public Integer getCodCad() {
        return codCad;
    }

    public void setCodCad(Integer codCad) {
        this.codCad = codCad;
    }

    public String getNomCad() {
        return nomCad;
    }

    public void setNomCad(String nomCad) {
        this.nomCad = nomCad;
    }

    @XmlTransient
    public List<Cadsub> getCadsubList() {
        return cadsubList;
    }

    public void setCadsubList(List<Cadsub> cadsubList) {
        this.cadsubList = cadsubList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCad != null ? codCad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cad)) {
            return false;
        }
        Cad other = (Cad) object;
        if ((this.codCad == null && other.codCad != null) || (this.codCad != null && !this.codCad.equals(other.codCad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelos.Cad[ codCad=" + codCad + " ]";
    }
    
}
