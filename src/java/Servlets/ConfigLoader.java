package Servlets;

import PersistenceDB.Configurations;
import PersistenceDB.Users;
import PersistenceDB.UsersJpaController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigLoader extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        UsersJpaController ufc = new UsersJpaController(emf);
        String username = getUserFromCookie(request);
        Users user = ufc.findUser(username);

        if (user.getConfigurationsCollection() != null) {
            Collection<Configurations> configList = user.getConfigurationsCollection();
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(configList);
            response.setContentType("application/json");
            PrintWriter pw;
            try {
                pw = response.getWriter();
                pw.println(json);
            } catch (IOException ex) {
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param request
     * @return Return the string with the username from cookie
     */
    private String getUserFromCookie(HttpServletRequest request) {
        String cookieUsername = "lunar";
        String username = null;
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookieUsername.equals(cookies[i].getName())) {
                username = cookies[i].getValue();
            }
        }
        return username;
    }

    /**
     *
     * @param request
     * @param response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        UsersJpaController ufc = new UsersJpaController(emf);
        String username = getUserFromCookie(request);
        Users user = ufc.findUser(username);
        Configurations config = new Configurations();

        config.setConfigureName(request.getParameter("nameConfig"));
        config.setDiffId(new Integer(request.getParameter("difConfig")));
        config.setSpaceshipId(new Integer(request.getParameter("nave")));
        config.setPlanetId(new Integer(request.getParameter("lugar")));
        config.setUserId(user);

        if (user.getConfigurationsCollection() != null) {
            Collection<Configurations> configList = user.getConfigurationsCollection();
            configList.add(config);
            user.setConfigurationsCollection(configList);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw;
            try {
                pw = response.getWriter();
                pw.println("{\"error\":\"Failed to save configurations\"}");
            } catch (IOException ex) {
                Logger.getLogger(ConfigLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
