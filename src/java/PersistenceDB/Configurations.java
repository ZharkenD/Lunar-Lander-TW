/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PersistenceDB;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cdore
 */
@Entity
@Table(name = "configurations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configurations.findAll", query = "SELECT c FROM Configurations c")
    , @NamedQuery(name = "Configurations.findById", query = "SELECT c FROM Configurations c WHERE c.id = :id")
    , @NamedQuery(name = "Configurations.findByConfigureName", query = "SELECT c FROM Configurations c WHERE c.configureName = :configureName")
    , @NamedQuery(name = "Configurations.findByDiffId", query = "SELECT c FROM Configurations c WHERE c.diffId = :diffId")
    , @NamedQuery(name = "Configurations.findBySpaceshipId", query = "SELECT c FROM Configurations c WHERE c.spaceshipId = :spaceshipId")
    , @NamedQuery(name = "Configurations.findByPlanetId", query = "SELECT c FROM Configurations c WHERE c.planetId = :planetId")})
public class Configurations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "configure_name")
    private String configureName;
    @Column(name = "diff_id")
    private Integer diffId;
    @Column(name = "spaceship_id")
    private Integer spaceshipId;
    @Column(name = "planet_id")
    private Integer planetId;
    @OneToMany(mappedBy = "confId", fetch = FetchType.LAZY)
    private Collection<Scores> scoresCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userId;

    public Configurations() {
    }

    public Configurations(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigureName() {
        return configureName;
    }

    public void setConfigureName(String configureName) {
        this.configureName = configureName;
    }

    public Integer getDiffId() {
        return diffId;
    }

    public void setDiffId(Integer diffId) {
        this.diffId = diffId;
    }

    public Integer getSpaceshipId() {
        return spaceshipId;
    }

    public void setSpaceshipId(Integer spaceshipId) {
        this.spaceshipId = spaceshipId;
    }

    public Integer getPlanetId() {
        return planetId;
    }

    public void setPlanetId(Integer planetId) {
        this.planetId = planetId;
    }

    @XmlTransient
    public Collection<Scores> getScoresCollection() {
        return scoresCollection;
    }

    public void setScoresCollection(Collection<Scores> scoresCollection) {
        this.scoresCollection = scoresCollection;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configurations)) {
            return false;
        }
        Configurations other = (Configurations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersistenceDB.Configurations[ id=" + id + " ]";
    }
    
}
