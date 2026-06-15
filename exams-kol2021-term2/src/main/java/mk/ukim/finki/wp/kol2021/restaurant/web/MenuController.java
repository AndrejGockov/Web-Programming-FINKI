package mk.ukim.finki.wp.kol2021.restaurant.web;

import mk.ukim.finki.wp.kol2021.restaurant.model.Menu;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuType;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuItemService;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/","/menu"})
public class MenuController {

    private final MenuService service;
    private final MenuItemService menuItemService;

    public MenuController(MenuService service, MenuItemService menuItemService) {
        this.service = service;
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String showMenus(@RequestParam(required = false)String nameSearch,
                            @RequestParam(required = false)MenuType menuType,
                            Model model) {
        List<Menu>menus = new ArrayList<>();

        if (nameSearch == null && menuType == null) {
            menus = service.listAll();
        } else {
            menus = this.service.listMenuItemsByRestaurantNameAndMenuType(nameSearch,  menuType);
        }
        model.addAttribute("menus", menus);
        model.addAttribute("menuItems", menuItemService.listAll());
        model.addAttribute("menuTypes", MenuType.values());

        return "list";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("menuItems", menuItemService.listAll());
        model.addAttribute("menuTypes", MenuType.values());
        return "form";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id,
                           Model model) {
        model.addAttribute("menu", this.service.findById(id));
        model.addAttribute("menuItems", menuItemService.listAll());
        model.addAttribute("menuTypes", MenuType.values());
        return "form";
    }

//    Create works, but doesn't pass the test
    @PostMapping
    public String create(@RequestParam(value = "name", required = false)String name,
                         @RequestParam(value = "menuType", required = false)MenuType menuType,
                         @RequestParam(value = "description", required = false)String description,
                         @RequestParam(value = "menuItemIds", required = false)List<Long> menuItemIds) {
        this.service.create(name, menuType, menuItemIds);
        return "redirect:/menu";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam(value = "name", required = false)String name,
                         @RequestParam(value = "description", required = false)String description,
                         @RequestParam(value = "menuType", required = false)MenuType menuType,
                         @RequestParam(value = "menuItemIds", required = false)List<Long> menuItemIds) {
        this.service.update(id, name, description, menuType, menuItemIds);
        return "redirect:/menu";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/menu";
    }
}
