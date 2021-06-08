package my.wurflcloud.example;


import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.scientiamobile.wurflcloud.CloudClientLoader;
import com.scientiamobile.wurflcloud.ICloudClientManager;
import com.scientiamobile.wurflcloud.device.AbstractDevice;

public class MyCloudClientServlet extends HttpServlet {
    private static final long serialVersionUID = -6370538869254374459L;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private ICloudClientManager manager;
    private String[] capabilities;

    public void init() throws ServletException {
        super.init();
        CloudClientLoader loader;
        try {
            loader = new CloudClientLoader("XXXXXX:YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"); // Place your API Key here
            manager = loader.getClientManager();
            capabilities = loader.getSearchedCapabilities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useragent = req.getParameter("user_agent");
        logger.fine("User-Agent from text area: " + useragent);
        logger.fine("User-Agent from request: " + req.getHeader("User-Agent"));
        useragent = useragent.trim();
        long start = System.currentTimeMillis();
        AbstractDevice device = manager.getDeviceFromUserAgent(useragent, capabilities);
        // If empty UA is submitted use request instead
        if (useragent == "") {
        	device = manager.getDeviceFromRequest(req, resp, capabilities);
        }
        long time = System.currentTimeMillis() - start;
        logger.info("device: " + device);
        Info i = new Info(time);
        String apiVersion = manager.getAPIVersion();
        logger.info("apiVersion: " + apiVersion);
        i.setCloudVersion(apiVersion);
        i.setClientVersion(manager.getClientVersion());
        req.setAttribute("device", device);
        req.setAttribute("info", i);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/response.jsp");
        rd.forward(req, resp);
    }

}
