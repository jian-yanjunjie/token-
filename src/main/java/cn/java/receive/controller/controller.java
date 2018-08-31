package cn.java.receive.controller;

import cn.java.receive.domain.RequestToken;
import cn.java.receive.domain.ResponseToken;
import cn.java.receive.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/token")
public class controller {

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/g")
    @ResponseBody
    public ResponseToken getToken() {
        return new ResponseToken(0,"success",tokenService.getToken());
    }

    @RequestMapping(value = "/w", method = RequestMethod.POST, consumes = {"application/*"})
    @ResponseBody()
    public ResponseToken updateToken(@RequestBody RequestToken token) {
        //空判断
        if (StringUtils.isEmpty(token.access_token))
            return new ResponseToken(1,"no token");
        //与原来token是否相同
        if (tokenService.getToken().equals(token))
            return new ResponseToken(1,"no token");

        if (tokenService.wriToken(token.access_token))
            return new ResponseToken(0,"success");

        return new ResponseToken(1,"no token");
    }

    @RequestMapping(value = "/s", method = RequestMethod.POST, consumes = {"application/*"})
    @ResponseBody
    public Boolean synchronousToken(@RequestBody RequestToken token) {
        return tokenService.syncToken(token.access_token);
    }

}
