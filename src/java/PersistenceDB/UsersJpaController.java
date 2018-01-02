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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cdore
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getConfigurationsCollection() == null) {
            users.setConfigurationsCollection(new ArrayList<Configurations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Configurations> attachedConfigurationsCollection = new ArrayList<Configurations>();
            for (Configurations configurationsCollectionConfigurationsToAttach : users.getConfigurationsCollection()) {
                configurationsCollectionConfigurationsToAttach = em.getReference(configurationsCollectionConfigurationsToAttach.getClass(), configurationsCollectionConfigurationsToAttach.getId());
                attachedConfigurationsCollection.add(configurationsCollectionConfigurationsToAttach);
            }
            users.setConfigurationsCollection(attachedConfigurationsCollection);
            em.persist(users);
            for (Configurations configurationsCollectionConfigurations : users.getConfigurationsCollection()) {
                Users oldUserIdOfConfigurationsCollectionConfigurations = configurationsCollectionConfigurations.getUserId();
                configurationsCollectionConfigurations.setUserId(users);
                configurationsCollectionConfigurations = em.merge(configurationsCollectionConfigurations);
                if (oldUserIdOfConfigurationsCollectionConfigurations != null) {
                    oldUserIdOfConfigurationsCollectionConfigurations.getConfigurationsCollection().remove(configurationsCollectionConfigurations);
                    oldUserIdOfConfigurationsCollectionConfigurations = em.merge(oldUserIdOfConfigurationsCollectionConfigurations);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Collection<Configurations> configurationsCollectionOld = persistentUsers.getConfigurationsCollection();
            Collection<Configurations> configurationsCollectionNew = users.getConfigurationsCollection();
            Collection<Configurations> attachedConfigurationsCollectionNew = new ArrayList<Configurations>();
            for (Configurations configurationsCollectionNewConfigurationsToAttach : configurationsCollectionNew) {
                configurationsCollectionNewConfigurationsToAttach = em.getReference(configurationsCollectionNewConfigurationsToAttach.getClass(), configurationsCollectionNewConfigurationsToAttach.getId());
                attachedConfigurationsCollectionNew.add(configurationsCollectionNewConfigurationsToAttach);
            }
            configurationsCollectionNew = attachedConfigurationsCollectionNew;
            users.setConfigurationsCollection(configurationsCollectionNew);
            users = em.merge(users);
            for (Configurations configurationsCollectionOldConfigurations : configurationsCollectionOld) {
                if (!configurationsCollectionNew.contains(configurationsCollectionOldConfigurations)) {
                    configurationsCollectionOldConfigurations.setUserId(null);
                    configurationsCollectionOldConfigurations = em.merge(configurationsCollectionOldConfigurations);
                }
            }
            for (Configurations configurationsCollectionNewConfigurations : configurationsCollectionNew) {
                if (!configurationsCollectionOld.contains(configurationsCollectionNewConfigurations)) {
                    Users oldUserIdOfConfigurationsCollectionNewConfigurations = configurationsCollectionNewConfigurations.getUserId();
                    configurationsCollectionNewConfigurations.setUserId(users);
                    configurationsCollectionNewConfigurations = em.merge(configurationsCollectionNewConfigurations);
                    if (oldUserIdOfConfigurationsCollectionNewConfigurations != null && !oldUserIdOfConfigurationsCollectionNewConfigurations.equals(users)) {
                        oldUserIdOfConfigurationsCollectionNewConfigurations.getConfigurationsCollection().remove(configurationsCollectionNewConfigurations);
                        oldUserIdOfConfigurationsCollectionNewConfigurations = em.merge(oldUserIdOfConfigurationsCollectionNewConfigurations);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Collection<Configurations> configurationsCollection = users.getConfigurationsCollection();
            for (Configurations configurationsCollectionConfigurations : configurationsCollection) {
                configurationsCollectionConfigurations.setUserId(null);
                configurationsCollectionConfigurations = em.merge(configurationsCollectionConfigurations);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
