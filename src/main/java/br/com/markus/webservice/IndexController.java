package br.com.markus.webservice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        StringBuilder msg = new StringBuilder("Desafio M4U Markus<br><br>");
        msg.append("URI autorizar: /m4umarkus/ws/autorizar<br>");
        msg.append("URI solicitar assincrono: /m4umarkus/ws/solicitar<br>");
        msg.append("URI consultar: /m4umarkus/ws/consultar<br>");
        model.addAttribute("message", msg.toString());

        return "result";
    }

}