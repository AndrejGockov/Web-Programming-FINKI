package mk.ukim.finki.wp.kol2023.g1.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import mk.ukim.finki.wp.kol2023.g1.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final TeamService teamService;
    private final PlayerRepository playerRepository;

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player player = new Player(name, bio, pointsPerGame, position, teamService.findById(team));
        return playerRepository.save(player);
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player player = findById(id);

        player.setName(name);
        player.setBio(bio);
        player.setPointsPerGame(pointsPerGame);
        player.setPosition(position);
        player.setTeam(teamService.findById(team));

        return playerRepository.save(player);
    }

    @Override
    public Player delete(Long id) {
        Player player = findById(id);
        playerRepository.delete(player);

        return player;
    }

    @Override
    public Player vote(Long id) {
        Player player = findById(id);
        player.setVotes(player.getVotes() + 1);

        return playerRepository.save(player);
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
        if(pointsPerGame != null && position != null) {
            return listAllPlayers()
                    .stream()
                    .filter(player -> player.getPointsPerGame() < pointsPerGame
                            && player.getPosition() == position)
                    .toList();
        }

        if(pointsPerGame != null) {
            return listAllPlayers()
                    .stream()
                    .filter(player -> player.getPointsPerGame() < pointsPerGame)
                    .toList();
        }

        if(position != null) {
            return listAllPlayers()
                    .stream()
                    .filter(player -> player.getPosition() == position)
                    .toList();
        }

        return listAllPlayers();
    }
}
