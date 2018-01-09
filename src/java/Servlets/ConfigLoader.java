package Servlets;

import PersistenceDB.Configurations;
import PersistenceDB.ConfigurationsJpaController;
import PersistenceDB.Users;
import PersistenceDB.UsersJpaController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
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
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            UsersJpaController ufc = new UsersJpaController(emf);
            Users user = ufc.findUser(getUserFromCookie(request));
            
            if (user.getConfigurationsList() != null) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(gson.toJson(user.getConfigurationsList()));
            }
        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Server error");

            Gson gson = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));

        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            UsersJpaController ufc = new UsersJpaController(emf);
            Users user = ufc.findUser(getUserFromCookie(request));
            ConfigurationsJpaController cjc = new ConfigurationsJpaController(emf);
//If dont exist configuration
            Configurations config = new Configurations();

            config.setConfigureName(request.getParameter("nameConfig"));
            config.setDiffId(new Integer(request.getParameter("difConfig")));
            config.setSpaceshipId(new Integer(request.getParameter("shipConfig")));
            config.setPlanetId(new Integer(request.getParameter("landConfig")));
            config.setUserId(user);

            cjc.create(config);

            Map<String, String> emess = new HashMap<>();
            emess.put("mess", "The configuration has been saved successfully");
            Gson gson = new GsonBuilder().create();
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));

        } catch (Exception e) {
            Map<String, String> emess = new HashMap<>();
            emess.put("error", "Server error");

            Gson gson = new GsonBuilder().create();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(gson.toJson(emess));
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

}
