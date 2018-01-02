/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PersistenceDB;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cdore
 */
@Entity
@Table(name = "scores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scores.findAll", query = "SELECT s FROM Scores s")
    , @NamedQuery(name = "Scores.findById", query = "SELECT s FROM Scores s WHERE s.id = :id")
    , @NamedQuery(name = "Scores.findBySpeed", query = "SELECT s FROM Scores s WHERE s.speed = :speed")
    , @NamedQuery(name = "Scores.findByFuel", query = "SELECT s FROM Scores s WHERE s.fuel = :fuel")
    , @NamedQuery(name = "Scores.findByInitTime", query = "SELECT s FROM Scores s WHERE s.initTime = :initTime")
    , @NamedQuery(name = "Scores.findByEndTime", query = "SELECT s FROM Scores s WHERE s.endTime = :endTime")})
public class Scores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "speed")
    private Float speed;
    @Column(name = "fuel")
    private Float fuel;
    @Column(name = "init_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date initTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @JoinColumn(name = "conf_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Configurations confId;

    public Scores() {
    }

    public Scores(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getFuel() {
        return fuel;
    }

    public void setFuel(Float fuel) {
        this.fuel = fuel;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Configurations getConfId() {
        return confId;
    }

    public void setConfId(Configurations confId) {
        this.confId = confId;
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
        if (!(object instanceof Scores)) {
            return false;
        }
        Scores other = (Scores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersistenceDB.Scores[ id=" + id + " ]";
    }
    
}
