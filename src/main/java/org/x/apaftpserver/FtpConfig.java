package org.x.apaftpserver;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.PropertiesUserManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FtpConfig extends CachingConfigurerSupport {

    @Value("${ftp.port}")
    private Integer ftpPort;

    @Value("${ftp.passivePort}")
    private String ftpPassivePort;

    @Value("${ftp.passiveExternalAddress}")
    private String ftpPassiveExternalAddress;

    @Bean
    public FtpServer createFtpServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();

        /**
         * 被动模式
         */
        DataConnectionConfigurationFactory dccf = new DataConnectionConfigurationFactory();
        dccf.setIdleTime(60);
        dccf.setActiveLocalPort(ftpPort);
        dccf.setPassiveIpCheck(true);
        dccf.setPassivePorts(ftpPassivePort);
        dccf.setPassiveExternalAddress(ftpPassiveExternalAddress);

        factory.setDataConnectionConfiguration(dccf.createDataConnectionConfiguration());
        serverFactory.addListener("default", factory.createListener());

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        try {
            ClassPathResource classPathResource = new ClassPathResource("user.properties");
            userManagerFactory.setUrl(classPathResource.getURL());
        } catch (Exception e) {
            throw new RuntimeException("配置文件user.properties不存在");
        }

        userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        Map<String, Ftplet> m = new HashMap<>(1);
        m.put("miaFtplet", new UploadListener());
        serverFactory.setFtplets(m);
        FtpServer server = serverFactory.createServer();
        return server;
    }
}
