package mk.ukim.finki.wp.kol2021.restaurant.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2021.restaurant.model.Menu;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuItem;
import mk.ukim.finki.wp.kol2021.restaurant.model.MenuType;
import mk.ukim.finki.wp.kol2021.restaurant.model.exceptions.InvalidMenuIdException;
import mk.ukim.finki.wp.kol2021.restaurant.repository.MenuRepository;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuItemService;
import mk.ukim.finki.wp.kol2021.restaurant.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuItemService menuItemService;
    private final MenuRepository menuRepository;

    @Override
    public List<Menu> listAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu findById(Long id) {
        return menuRepository.findById(id).orElseThrow(InvalidMenuIdException::new);
    }

    @Override
    public Menu create(String restaurantName, MenuType menuType, List<Long> menuItems) {
        List<MenuItem>items = new ArrayList<>();

        for(Long menuItemId : menuItems){
            items.add(menuItemService.findById(menuItemId));
        }

        Menu menu = new Menu(restaurantName, menuType, items);

        return menuRepository.save(menu);
    }

    @Override
    public Menu update(Long id, String restaurantName, String description,  MenuType menuType, List<Long> menuItems) {
        Menu menu = findById(id);

        List<MenuItem>items = new ArrayList<>();

        for(Long menuItemId : menuItems){
            items.add(menuItemService.findById(menuItemId));
        }

        menu.setRestaurantName(restaurantName);
        menu.setMenuType(menuType);
        menu.setMenuItems(items);

        return menuRepository.save(menu);
    }

    @Override
    public Menu delete(Long id) {
        Menu menu = findById(id);
        menuRepository.delete(menu);
        return menu;
    }

    @Override
    public List<Menu> listMenuItemsByRestaurantNameAndMenuType(String restaurantName, MenuType menuType) {
        if(restaurantName != null && menuType != null){
            return listAll()
                    .stream()
                    .filter(menu -> menu.getRestaurantName().toLowerCase().contains(restaurantName.toLowerCase())
                            && menu.getMenuType().equals(menuType))
                    .toList();
        }

        if(restaurantName != null){
            return listAll()
                    .stream()
                    .filter(menu -> menu.getRestaurantName().toLowerCase().contains(restaurantName.toLowerCase()))
                    .toList();
        }

        if(menuType != null){
            return listAll()
                    .stream()
                    .filter(menu -> menu.getMenuType().equals(menuType))
                    .toList();
        }

        return listAll();
    }
}
