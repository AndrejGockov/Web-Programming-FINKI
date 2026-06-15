package mk.ukim.finki.wp.june2025g1.web;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.june2025g1.model.Industry;
import mk.ukim.finki.wp.june2025g1.service.FounderService;
import mk.ukim.finki.wp.june2025g1.service.StartupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping({"/", "/startups"})
public class StartupController {
    private final FounderService founderService;
    private final StartupService startupService;

    /**
     * This method should use the "list.html" template to display all startups.
     * The method should be mapped on paths '/' and '/startups'.
     * The arguments that this method takes are optional and can be 'null'.
     * The filtered startups that are the result of the call
     * findPage method from the StartupService should be displayed.
     * If you want to return a paginated result, you should also pass the page number and the page size as arguments.
     *
     * @param name        Filters startups whose names contain the specified text.
     * @param valuation   Filters startups with a valuation greater than the specified value.
     * @param yearFounded Filters startups founded after the specified year.
     * @param industry    Filters startups by the specified industry.
     * @param founderId   Filters startups by the specified founder ID.
     * @param pageNum     The page number.
     * @param pageSize    The number of items per page.
     * @return The view "list.html"
     */
    @GetMapping
    public String listAll(@RequestParam(required = false) String name,
                          @RequestParam(required = false) Double valuation,
                          @RequestParam(required = false) Integer yearFounded,
                          @RequestParam(required = false) Industry industry,
                          @RequestParam(required = false) Long founderId,
                          @RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "10") int pageSize,
                          Model model) {
        model.addAttribute("startups",
                startupService.findPage(name, valuation, yearFounded, industry, founderId, pageNum - 1, pageSize));
        model.addAttribute("founders", founderService.listAll());
        model.addAttribute("industries", Industry.values());
        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/startups/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("founders", founderService.listAll());
        model.addAttribute("industries", Industry.values());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case, all 'input' elements should be filled with the appropriate value for the startup that is updated.
     * The method should be mapped on path '/startups/edit/[id]'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("startup", startupService.findById(id));
        model.addAttribute("founders", founderService.listAll());
        model.addAttribute("industries", Industry.values());
        return "form";
    }

    /**
     * This method should create a new startup given the arguments it takes.
     * The method should be mapped on path '/startups'.
     * After the startup is created, all startups should be displayed.
     *
     * @param name        The name of the startup
     * @param valuation   The valuation of the startup
     * @param yearFounded The year the startup was founded
     * @param industry    The industry of the startup
     * @param founderId   The id of the founder of the startup
     * @return Redirects to the list of startups
     */
    @PostMapping
    public String create(@RequestParam(required = false)String name,
                         @RequestParam(required = false)Double valuation,
                         @RequestParam(required = false)Integer yearFounded,
                         @RequestParam(required = false)Industry industry,
                         @RequestParam(required = false)Long founderId) {
        startupService.create(name, valuation, yearFounded, industry, founderId);
        return "redirect:/startups";
    }

    /**
     * This method should update an existing startup given the arguments it takes.
     * The method should be mapped on path '/startups/[id]'.
     * After the startup is updated, all startups should be displayed.
     *
     * @param id          The id of the startup that is being updated
     * @param name        The name of the startup
     * @param valuation   The valuation of the startup
     * @param yearFounded The year the startup was founded
     * @param industry    The industry of the startup
     * @param founderId   The id of the founder of the startup
     * @return Redirects to the list of startups
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam(required = false)String name,
                         @RequestParam(required = false)Double valuation,
                         @RequestParam(required = false)Integer yearFounded,
                         @RequestParam(required = false)Industry industry,
                         @RequestParam(required = false)Long founderId) {
        startupService.update(id, name, valuation, yearFounded, industry, founderId);
        return "redirect:/startups";
    }

    /**
     * This method should delete the startup that has the appropriate identifier.
     * The method should be mapped on path '/startups/delete/[id]'.
     * After the startup is deleted, all startups should be displayed.
     *
     * @param id The ID of the startup to delete
     * @return Redirects to the list of startups
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        startupService.delete(id);
        return "redirect:/startups";
    }

    /**
     * This method should deactivate the startup that has the appropriate identifier.
     * The method should be mapped on path '/startups/deactivate/[id]'.
     * After the selected startups is closed, all startups should be displayed.
     *
     * @param id The ID of the startup to deactivate
     * @return Redirects to the list of startups
     */
    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Long id) {
        startupService.deactivate(id);
        return "redirect:/startups";
    }
}

