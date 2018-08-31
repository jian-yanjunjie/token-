package cn.java.receive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * token文件处理
 */
@Component
public class FileOperater {
    Logger logger = LoggerFactory.getLogger(FileOperater.class);

    @Value("${token.path}")
    private String tokenPath;
    private static String Token = "";

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 初始化token，从文件中加载到缓存中
     */
    @PostConstruct
    private void getfileinfo() {

        try {
            FileSystemResource resource = new FileSystemResource(tokenPath);
            BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
            String str = null;
            while ((str = br.readLine()) != null) {
                Token += str;
            }
            br.close();
        } catch (IOException e) {
            logger.warn("token 读操作失败！" + e.getMessage());
        }
    }

    /**
     * 文件写操作
     * @param t
     * @return
     */
    private Boolean writefileinfo(String t) {

        FileSystemResource resource = new FileSystemResource(tokenPath);
        try {
            FileWriter fileWriter = (new FileWriter(resource.getFile()));
            fileWriter.write(t);//覆盖文本
            fileWriter.close();

        } catch (IOException e) {
            logger.warn("token 写操作失败！");
            return false;
        }
        return true;
    }

    /**
     * 获取token
     * @return token
     */
    public String getTokenStr() {
        Lock readLock = lock.readLock();
        readLock.lock();

        if (StringUtils.isEmpty(Token))
            logger.warn("token 丢失！");

        readLock.unlock();
        return Token;
    }

    /**
     * 更新token，更新文件和缓存
     * @param t new token
     * @return 更新成功：true
     */
    public Boolean updateTokenStr(String t) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();

        if (writefileinfo(t)) {
            Token = t;

            writeLock.unlock();

            return true;
        }
        logger.warn("token 更新失败");
        return false;
    }
}


