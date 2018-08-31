package cn.java.receive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Service
public class TokenService {
    Logger logger = LoggerFactory.getLogger(FileOperater.class);

    @Value("${ips}")
    private String ips;

    @Autowired
    private FileOperater fileOperater;

    public String getToken() {

        return fileOperater.getTokenStr();
    }

    public boolean wriToken(String t) {
        //向其他服务器同步
        req(t,getReqIp());
        return fileOperater.updateTokenStr(t);
    }

    public boolean syncToken(String st){

        return fileOperater.updateTokenStr(st);
    }

    private Boolean req(String token,ArrayList<String> ips) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> params= new HashMap<String, String>();
        params.put("access_token", token);

        HttpEntity requestEntity = new HttpEntity(params, headers);
        //  执行HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        for (String ip : ips){
            String url = buildUrl(ip);
            Boolean r = restTemplate.postForObject(url,requestEntity,boolean.class);
            if (r)
                logger.info("向ip[{}]同步成功！",ip);

        }
        return true;
    }

    private String getLocalHost(){
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get server host Exception e:", e);
        }
        return host;
    }

    private String buildUrl(String ip){
        return "http://"+ip+":8080/token/s";
    }

    private ArrayList getReqIp(){
        String[] ipArray = ips.split(",");
        ArrayList<String> ipList = new ArrayList();
        ipList.addAll(Arrays.asList(ipArray));
        ipList.remove(getLocalHost());
        return ipList;
    }
}
