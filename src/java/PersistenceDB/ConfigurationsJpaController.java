/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PersistenceDB;

import PersistenceDB.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cdore
 */
public class ConfigurationsJpaController implements Serializable {

    public ConfigurationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configurations configurations) {
        if (configurations.getScoresList() == null) {
            configurations.setScoresList(new ArrayList<Scores>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = configurations.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                configurations.setUserId(userId);
            }
            List<Scores> attachedScoresList = new ArrayList<Scores>();
            for (Scores scoresListScoresToAttach : configurations.getScoresList()) {
                scoresListScoresToAttach = em.getReference(scoresListScoresToAttach.getClass(), scoresListScoresToAttach.getId());
                attachedScoresList.add(scoresListScoresToAttach);
            }
            configurations.setScoresList(attachedScoresList);
            em.persist(configurations);
            if (userId != null) {
                userId.getConfigurationsList().add(configurations);
                userId = em.merge(userId);
            }
            for (Scores scoresListScores : configurations.getScoresList()) {
                Configurations oldConfIdOfScoresListScores = scoresListScores.getConfId();
                scoresListScores.setConfId(configurations);
                scoresListScores = em.merge(scoresListScores);
                if (oldConfIdOfScoresListScores != null) {
                    oldConfIdOfScoresListScores.getScoresList().remove(scoresListScores);
                    oldConfIdOfScoresListScores = em.merge(oldConfIdOfScoresListScores);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Configurations configurations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Configurations persistentConfigurations = em.find(Configurations.class, configurations.getId());
            Users userIdOld = persistentConfigurations.getUserId();
            Users userIdNew = configurations.getUserId();
            List<Scores> scoresListOld = persistentConfigurations.getScoresList();
            List<Scores> scoresListNew = configurations.getScoresList();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                configurations.setUserId(userIdNew);
            }
            List<Scores> attachedScoresListNew = new ArrayList<Scores>();
            for (Scores scoresListNewScoresToAttach : scoresListNew) {
                scoresListNewScoresToAttach = em.getReference(scoresListNewScoresToAttach.getClass(), scoresListNewScoresToAttach.getId());
                attachedScoresListNew.add(scoresListNewScoresToAttach);
            }
            scoresListNew = attachedScoresListNew;
            configurations.setScoresList(scoresListNew);
            configurations = em.merge(configurations);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getConfigurationsList().remove(configurations);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getConfigurationsList().add(configurations);
                userIdNew = em.merge(userIdNew);
            }
            for (Scores scoresListOldScores : scoresListOld) {
                if (!scoresListNew.contains(scoresListOldScores)) {
                    scoresListOldScores.setConfId(null);
                    scoresListOldScores = em.merge(scoresListOldScores);
                }
            }
            for (Scores scoresListNewScores : scoresListNew) {
                if (!scoresListOld.contains(scoresListNewScores)) {
                    Configurations oldConfIdOfScoresListNewScores = scoresListNewScores.getConfId();
                    scoresListNewScores.setConfId(configurations);
                    scoresListNewScores = em.merge(scoresListNewScores);
                    if (oldConfIdOfScoresListNewScores != null && !oldConfIdOfScoresListNewScores.equals(configurations)) {
                        oldConfIdOfScoresListNewScores.getScoresList().remove(scoresListNewScores);
                        oldConfIdOfScoresListNewScores = em.merge(oldConfIdOfScoresListNewScores);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configurations.getId();
                if (findConfigurations(id) == null) {
                    throw new NonexistentEntityException("The configurations with id " + id + " no longer exists.");
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
            Configurations configurations;
            try {
                configurations = em.getReference(Configurations.class, id);
                configurations.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configurations with id " + id + " no longer exists.", enfe);
            }
            Users userId = configurations.getUserId();
            if (userId != null) {
                userId.getConfigurationsList().remove(configurations);
                userId = em.merge(userId);
            }
            List<Scores> scoresList = configurations.getScoresList();
            for (Scores scoresListScores : scoresList) {
                scoresListScores.setConfId(null);
                scoresListScores = em.merge(scoresListScores);
            }
            em.remove(configurations);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Configurations> findConfigurationsEntities() {
        return findConfigurationsEntities(true, -1, -1);
    }

    public List<Configurations> findConfigurationsEntities(int maxResults, int firstResult) {
        return findConfigurationsEntities(false, maxResults, firstResult);
    }

    private List<Configurations> findConfigurationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Configurations.class));
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

    public Configurations findConfigurations(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Configurations.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigurationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Configurations> rt = cq.from(Configurations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
