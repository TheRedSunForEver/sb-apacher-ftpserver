package org.x.apaftpserver;

import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.*;

import java.io.File;
import java.io.IOException;

@Slf4j
public class UploadListener extends DefaultFtplet {

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest ftpRequest) throws FtpException, IOException {
        String path = session.getUser().getHomeDirectory();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String userName = session.getUser().getName();
        String fileName = ftpRequest.getArgument();
        log.info("开始上传, user:{}, path:{}, fileName:{}", userName, path, fileName);
        return super.onUploadStart(session, ftpRequest);
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest ftpRequest) throws FtpException, IOException {
        String path = session.getUser().getHomeDirectory();
        String userName = session.getUser().getName();
        String fileName = ftpRequest.getArgument();
        log.info("开始完成, user:{}, path:{}, fileName:{}", userName, path, fileName);
        return super.onUploadEnd(session, ftpRequest);
    }

    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest ftpRequest) throws FtpException, IOException {
        return super.onDownloadStart(session, ftpRequest);
    }

    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest ftpRequest) throws FtpException, IOException {
        return super.onDownloadEnd(session, ftpRequest);
    }

}
