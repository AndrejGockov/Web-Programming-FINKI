package mk.ukim.finki.wp.kol2023.g1.web;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import mk.ukim.finki.wp.kol2023.g1.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/", "/players"})
public class PlayersController {
    private final TeamService teamService;
    private final PlayerService playerService;

    public PlayersController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    /**
     * This method should use the "list.html" template to display all players.
     * The method should be mapped on paths '/' and '/players'.
     * The arguments that this method takes are optional and can be 'null'.
     * In the case when the arguments are not passed (both are 'null') all players should be displayed.
     * If one, or both of the arguments are not 'null', the players that are the result of the call
     * to the method 'listPlayersWithPointsLessThanAndPosition' from the PlayerService should be displayed.
     *
     * @param pointsPerGame
     * @param position
     * @return The view "list.html".
     */
    @RequestMapping
    public String showPlayers(@RequestParam(required = false)Double pointsPerGame,
                              @RequestParam(required = false)PlayerPosition position,
                              Model model) {
        List<Player> players = new ArrayList<>();

        if (pointsPerGame == null && position == null) {
            players = this.playerService.listAllPlayers();
        } else {
            players = this.playerService.listPlayersWithPointsLessThanAndPosition(pointsPerGame, position);
        }

        model.addAttribute("players", players);
        model.addAttribute("teams", this.teamService.listAll());
        model.addAttribute("positions", PlayerPosition.values());

        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/players/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("teams", this.teamService.listAll());
        model.addAttribute("positions", PlayerPosition.values());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the player that is updated.
     * The method should be mapped on path '/players/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        model.addAttribute("player", this.playerService.findById(id));
        model.addAttribute("teams", this.teamService.listAll());
        model.addAttribute("positions", PlayerPosition.values());
        return "form";
    }

    /**
     * This method should create a player given the arguments it takes.
     * The method should be mapped on path '/players'.
     * After the player is created, all players should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping
    public String create(@RequestParam(required = false)String name,
                         @RequestParam(required = false)String bio,
                         @RequestParam(required = false)Double pointsPerGame,
                         @RequestParam(required = false)PlayerPosition position,
                         @RequestParam(required = false)Long team) {
        this.playerService.create(name, bio, pointsPerGame, position, team);
        return "redirect:/players";
    }

    /**
     * This method should update a player given the arguments it takes.
     * The method should be mapped on path '/players/[id]'.
     * After the player is updated, all players should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam(required = false)String name,
                         @RequestParam(required = false)String bio,
                         @RequestParam(required = false)Double pointsPerGame,
                         @RequestParam(required = false)PlayerPosition position,
                         @RequestParam(required = false)Long team) {
        this.playerService.update(id, name, bio, pointsPerGame, position, team);
        return "redirect:/players";
    }

    /**
     * This method should delete the player that has the appropriate identifier.
     * The method should be mapped on path '/players/[id]/delete'.
     * After the player is deleted, all players should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.playerService.delete(id);
        return "redirect:/players";
    }

    /**
     * This method should increase the number of votes of the appropriate player by 1.
     * The method should be mapped on path '/players/[id]/vote'.
     * After the operation, all players should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/{id}/vote")
    public String vote(@PathVariable Long id) {
        this.playerService.vote(id);
        return "redirect:/players";
    }
}
