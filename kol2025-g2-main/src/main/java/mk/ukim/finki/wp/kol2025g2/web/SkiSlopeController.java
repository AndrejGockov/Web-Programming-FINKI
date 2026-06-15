package mk.ukim.finki.wp.kol2025g2.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2025g2.model.SlopeDifficulty;
import mk.ukim.finki.wp.kol2025g2.service.SkiResortService;
import mk.ukim.finki.wp.kol2025g2.service.SkiSlopeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping({"/", "/ski-slopes"})
@AllArgsConstructor
public class SkiSlopeController {
    private final SkiSlopeService skiSlopeService;
    private final SkiResortService skiResortService;
    /**
     * This method should use the "list.html" template to display all ski slopes.
     * The method should be mapped on paths '/' and '/ski-slopes'.
     * The arguments that this method takes are optional and can be 'null'.
     * The filtered ski slopes that are the result of the call
     * findPage method from the SkiSlopeService should be displayed.
     * If you want to return a paginated result, you should also pass the page number and the page size as arguments.
     *
     * @param name       Filters ski slopes whose names contain the specified text.
     * @param length     Filters ski slopes longer than the specified length in meters.
     * @param difficulty Filters ski slopes matching the specified difficulty level.
     * @param skiResort  Filters ski slopes belonging to the specified ski center.
     * @param pageNum    The page number
     * @param pageSize   The number of items per page
     * @return The view "list.html".
     */
    @GetMapping
    public String listAll(@RequestParam(required = false)String name,
                          @RequestParam(required = false)Integer length,
                          @RequestParam(required = false)SlopeDifficulty difficulty,
                          @RequestParam(required = false)Long skiResort,
                          @RequestParam(defaultValue = "1")Integer pageNum,
                          @RequestParam(defaultValue = "10")Integer pageSize,
                          Model model) {
        model.addAttribute("skiSlopes", skiSlopeService.findPage(name, length, difficulty, skiResort, pageNum - 1, pageSize));
        model.addAttribute("skiResorts", skiResortService.listAll());
        model.addAttribute("slopeDifficulties", SlopeDifficulty.values());
        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/ski-slopes/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("skiResorts", skiResortService.listAll());
        model.addAttribute("slopeDifficulties", SlopeDifficulty.values());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case, all 'input' elements should be filled with the appropriate value for the ski slope that is updated.
     * The method should be mapped on path '/ski-slopes/edit/[id]'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("skiSlope", skiSlopeService.findById(id));
        model.addAttribute("skiResorts", skiResortService.listAll());
        model.addAttribute("slopeDifficulties", SlopeDifficulty.values());
        return "form";
    }

    /**
     * This method should create a ski slope given the arguments it takes.
     * The method should be mapped on path '/ski-slopes'.
     * After the ski slope is created, all ski slopes should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping
    public String create(@RequestParam(required = false)String name,
                         @RequestParam(required = false)Integer length,
                         @RequestParam(required = false)SlopeDifficulty difficulty,
                         @RequestParam(required = false)Long skiResort) {
        skiSlopeService.create(name, length, difficulty, skiResort);
        return "redirect:/ski-slopes";
    }

    /**
     * This method should update a ski slope given the arguments it takes.
     * The method should be mapped on path '/ski-slopes/[id]'.
     * After the ski slope is updated, all ski slopes should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam(required = false)String name,
                         @RequestParam(required = false)Integer length,
                         @RequestParam(required = false)SlopeDifficulty difficulty,
                         @RequestParam(required = false)Long skiResort) {
        skiSlopeService.update(id, name, length, difficulty, skiResort);
        return "redirect:/ski-slopes";
    }

    /**
     * This method should delete the ski slope that has the appropriate identifier.
     * The method should be mapped on path '/ski-slopes/delete/[id]'.
     * After the ski slope is deleted, all ski slopes should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        skiSlopeService.delete(id);
        return "redirect:/ski-slopes";
    }

    /**
     * This method should close a ski slope.
     * The method should be mapped on path '/ski-slopes/close/[id]'.
     * After the selected ski slope is closed, all ski slopes should be displayed.
     *
     * @param id The ID of the ski slope to close.
     * @return The view "list.html".
     */
    @PostMapping("/close/{id}")
    public String close(@PathVariable Long id) {
        skiSlopeService.close(id);
        return "redirect:/ski-slopes";
    }

}