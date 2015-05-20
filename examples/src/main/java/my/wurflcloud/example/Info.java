package my.wurflcloud.example;

/**
 *         $Id$
 */
public class Info {


    private final long t;

    private String cloudVersion;
    private String clientVersion;

    public Info(long t) {
        this.t = t;
    }

    public long getTime() {
        return t;
    }

    public void setCloudVersion(String version) {
        this.cloudVersion = version;
    }

    public String getCloudVersion() {
        return cloudVersion;
    }


    public void setClientVersion(String clientVersion) {
        this.clientVersion=clientVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }
}
