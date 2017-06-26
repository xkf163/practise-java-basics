package ff.project.controller;

import ff.project.entity.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by F on 2017/6/15.
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController {


    @RequestMapping(value = "/all")
    public List<Role> getAll(){
        return null;
    }
}
