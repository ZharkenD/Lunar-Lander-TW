package PersistenceDB;

import PersistenceDB.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class ConfigurationsJpaController implements Serializable {

    public ConfigurationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Configurations configurations) {
        if (configurations.getScoresCollection() == null) {
            configurations.setScoresCollection(new ArrayList<Scores>());
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
            Collection<Scores> attachedScoresCollection = new ArrayList<Scores>();
            for (Scores scoresCollectionScoresToAttach : configurations.getScoresCollection()) {
                scoresCollectionScoresToAttach = em.getReference(scoresCollectionScoresToAttach.getClass(), scoresCollectionScoresToAttach.getId());
                attachedScoresCollection.add(scoresCollectionScoresToAttach);
            }
            configurations.setScoresCollection(attachedScoresCollection);
            em.persist(configurations);
            if (userId != null) {
                userId.getConfigurationsCollection().add(configurations);
                userId = em.merge(userId);
            }
            for (Scores scoresCollectionScores : configurations.getScoresCollection()) {
                Configurations oldConfIdOfScoresCollectionScores = scoresCollectionScores.getConfId();
                scoresCollectionScores.setConfId(configurations);
                scoresCollectionScores = em.merge(scoresCollectionScores);
                if (oldConfIdOfScoresCollectionScores != null) {
                    oldConfIdOfScoresCollectionScores.getScoresCollection().remove(scoresCollectionScores);
                    oldConfIdOfScoresCollectionScores = em.merge(oldConfIdOfScoresCollectionScores);
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
            Collection<Scores> scoresCollectionOld = persistentConfigurations.getScoresCollection();
            Collection<Scores> scoresCollectionNew = configurations.getScoresCollection();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                configurations.setUserId(userIdNew);
            }
            Collection<Scores> attachedScoresCollectionNew = new ArrayList<Scores>();
            for (Scores scoresCollectionNewScoresToAttach : scoresCollectionNew) {
                scoresCollectionNewScoresToAttach = em.getReference(scoresCollectionNewScoresToAttach.getClass(), scoresCollectionNewScoresToAttach.getId());
                attachedScoresCollectionNew.add(scoresCollectionNewScoresToAttach);
            }
            scoresCollectionNew = attachedScoresCollectionNew;
            configurations.setScoresCollection(scoresCollectionNew);
            configurations = em.merge(configurations);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getConfigurationsCollection().remove(configurations);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getConfigurationsCollection().add(configurations);
                userIdNew = em.merge(userIdNew);
            }
            for (Scores scoresCollectionOldScores : scoresCollectionOld) {
                if (!scoresCollectionNew.contains(scoresCollectionOldScores)) {
                    scoresCollectionOldScores.setConfId(null);
                    scoresCollectionOldScores = em.merge(scoresCollectionOldScores);
                }
            }
            for (Scores scoresCollectionNewScores : scoresCollectionNew) {
                if (!scoresCollectionOld.contains(scoresCollectionNewScores)) {
                    Configurations oldConfIdOfScoresCollectionNewScores = scoresCollectionNewScores.getConfId();
                    scoresCollectionNewScores.setConfId(configurations);
                    scoresCollectionNewScores = em.merge(scoresCollectionNewScores);
                    if (oldConfIdOfScoresCollectionNewScores != null && !oldConfIdOfScoresCollectionNewScores.equals(configurations)) {
                        oldConfIdOfScoresCollectionNewScores.getScoresCollection().remove(scoresCollectionNewScores);
                        oldConfIdOfScoresCollectionNewScores = em.merge(oldConfIdOfScoresCollectionNewScores);
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
                userId.getConfigurationsCollection().remove(configurations);
                userId = em.merge(userId);
            }
            Collection<Scores> scoresCollection = configurations.getScoresCollection();
            for (Scores scoresCollectionScores : scoresCollection) {
                scoresCollectionScores.setConfId(null);
                scoresCollectionScores = em.merge(scoresCollectionScores);
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
    
    
        public Users findUser(String username) {
        EntityManager em = getEntityManager();
        try {
            return (Users) em.createNamedQuery("Users.findByUsername").setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
}
