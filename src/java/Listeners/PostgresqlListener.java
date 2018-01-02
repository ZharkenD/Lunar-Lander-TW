package Listeners;

import javax.persistence.*;
import javax.servlet.*;

public class PostgresqlListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LunarLander_TWPU");
        arg0.getServletContext().setAttribute("emf", emf);
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        EntityManagerFactory emf = (EntityManagerFactory) arg0.getServletContext().getAttribute("emf");
        emf.close();
    }
}
