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
import javax.persistence.NoResultException;

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
        if (users.getConfigurationsList() == null) {
            users.setConfigurationsList(new ArrayList<Configurations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Configurations> attachedConfigurationsList = new ArrayList<Configurations>();
            for (Configurations configurationsListConfigurationsToAttach : users.getConfigurationsList()) {
                configurationsListConfigurationsToAttach = em.getReference(configurationsListConfigurationsToAttach.getClass(), configurationsListConfigurationsToAttach.getId());
                attachedConfigurationsList.add(configurationsListConfigurationsToAttach);
            }
            users.setConfigurationsList(attachedConfigurationsList);
            em.persist(users);
            for (Configurations configurationsListConfigurations : users.getConfigurationsList()) {
                Users oldUserIdOfConfigurationsListConfigurations = configurationsListConfigurations.getUserId();
                configurationsListConfigurations.setUserId(users);
                configurationsListConfigurations = em.merge(configurationsListConfigurations);
                if (oldUserIdOfConfigurationsListConfigurations != null) {
                    oldUserIdOfConfigurationsListConfigurations.getConfigurationsList().remove(configurationsListConfigurations);
                    oldUserIdOfConfigurationsListConfigurations = em.merge(oldUserIdOfConfigurationsListConfigurations);
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
            List<Configurations> configurationsListOld = persistentUsers.getConfigurationsList();
            List<Configurations> configurationsListNew = users.getConfigurationsList();
            List<Configurations> attachedConfigurationsListNew = new ArrayList<Configurations>();
            for (Configurations configurationsListNewConfigurationsToAttach : configurationsListNew) {
                configurationsListNewConfigurationsToAttach = em.getReference(configurationsListNewConfigurationsToAttach.getClass(), configurationsListNewConfigurationsToAttach.getId());
                attachedConfigurationsListNew.add(configurationsListNewConfigurationsToAttach);
            }
            configurationsListNew = attachedConfigurationsListNew;
            users.setConfigurationsList(configurationsListNew);
            users = em.merge(users);
            for (Configurations configurationsListOldConfigurations : configurationsListOld) {
                if (!configurationsListNew.contains(configurationsListOldConfigurations)) {
                    configurationsListOldConfigurations.setUserId(null);
                    configurationsListOldConfigurations = em.merge(configurationsListOldConfigurations);
                }
            }
            for (Configurations configurationsListNewConfigurations : configurationsListNew) {
                if (!configurationsListOld.contains(configurationsListNewConfigurations)) {
                    Users oldUserIdOfConfigurationsListNewConfigurations = configurationsListNewConfigurations.getUserId();
                    configurationsListNewConfigurations.setUserId(users);
                    configurationsListNewConfigurations = em.merge(configurationsListNewConfigurations);
                    if (oldUserIdOfConfigurationsListNewConfigurations != null && !oldUserIdOfConfigurationsListNewConfigurations.equals(users)) {
                        oldUserIdOfConfigurationsListNewConfigurations.getConfigurationsList().remove(configurationsListNewConfigurations);
                        oldUserIdOfConfigurationsListNewConfigurations = em.merge(oldUserIdOfConfigurationsListNewConfigurations);
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
            List<Configurations> configurationsList = users.getConfigurationsList();
            for (Configurations configurationsListConfigurations : configurationsList) {
                configurationsListConfigurations.setUserId(null);
                configurationsListConfigurations = em.merge(configurationsListConfigurations);
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

    /**
     * Check if the username exits.
     *
     * @param username
     * @return If you find an username, return true does not return false.
     */
    public Boolean checkUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            List<Users> list = em.createNamedQuery("Users.findByUsername").setParameter("username", username).getResultList();
            return !list.isEmpty();
        } finally {
            em.close();
        }
    }

    /**
     * Check if the email exists.
     *
     * @param email
     * @return If you find an email, return true does not return false
     */
    public Boolean checkEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            List<Users> list = em.createNamedQuery("Users.findByEmail").setParameter("email", email).getResultList();
            return !list.isEmpty();

        } finally {
            em.close();
        }
    }

    /**
     * Check if the username exists
     *
     * @param username
     * @return If you find an username, return it does not return null
     */
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
