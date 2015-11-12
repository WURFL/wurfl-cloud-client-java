package my.wurflcloud.example;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            loader = new CloudClientLoader("nnnnnn:XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"); // Place your API Key here
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
        String useragent = req.getHeader("User-Agent");
        if (useragent == null) useragent = req.getHeader("user-agent");
        logger.fine("user agent: " + useragent);
        useragent = useragent.trim();
        long start = System.currentTimeMillis();
        AbstractDevice device = manager.getDeviceFromRequest(req, resp, capabilities);
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
