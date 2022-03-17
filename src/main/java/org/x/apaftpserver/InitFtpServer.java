package org.x.apaftpserver;

import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitFtpServer implements CommandLineRunner {
    @Autowired
    private FtpServer ftpServer;

    @Override
    public void run(String... args) throws Exception {
        try {
            ftpServer.start();
            log.info("--ftp server is start");
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }
}
