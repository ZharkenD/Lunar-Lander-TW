/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PersistenceDB;

import PersistenceDB.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author cdore
 */
public class ScoresJpaController implements Serializable {

    public ScoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Scores scores) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configurations confId = scores.getConfId();
            if (confId != null) {
                confId = em.getReference(confId.getClass(), confId.getId());
                scores.setConfId(confId);
            }
            em.persist(scores);
            if (confId != null) {
                confId.getScoresCollection().add(scores);
                confId = em.merge(confId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Scores scores) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Scores persistentScores = em.find(Scores.class, scores.getId());
            Configurations confIdOld = persistentScores.getConfId();
            Configurations confIdNew = scores.getConfId();
            if (confIdNew != null) {
                confIdNew = em.getReference(confIdNew.getClass(), confIdNew.getId());
                scores.setConfId(confIdNew);
            }
            scores = em.merge(scores);
            if (confIdOld != null && !confIdOld.equals(confIdNew)) {
                confIdOld.getScoresCollection().remove(scores);
                confIdOld = em.merge(confIdOld);
            }
            if (confIdNew != null && !confIdNew.equals(confIdOld)) {
                confIdNew.getScoresCollection().add(scores);
                confIdNew = em.merge(confIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = scores.getId();
                if (findScores(id) == null) {
                    throw new NonexistentEntityException("The scores with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Scores scores;
            try {
                scores = em.getReference(Scores.class, id);
                scores.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The scores with id " + id + " no longer exists.", enfe);
            }
            Configurations confId = scores.getConfId();
            if (confId != null) {
                confId.getScoresCollection().remove(scores);
                confId = em.merge(confId);
            }
            em.remove(scores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Scores> findScoresEntities() {
        return findScoresEntities(true, -1, -1);
    }

    public List<Scores> findScoresEntities(int maxResults, int firstResult) {
        return findScoresEntities(false, maxResults, firstResult);
    }

    private List<Scores> findScoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Scores.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Scores findScores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Scores.class, id);
        } finally {
            em.close();
        }
    }

    public int getScoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Scores> rt = cq.from(Scores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
