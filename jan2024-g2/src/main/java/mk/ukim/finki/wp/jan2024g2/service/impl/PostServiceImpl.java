package mk.ukim.finki.wp.jan2024g2.service.impl;

import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.jan2024g2.model.Post;
import mk.ukim.finki.wp.jan2024g2.model.PostType;
import mk.ukim.finki.wp.jan2024g2.model.Tag;
import mk.ukim.finki.wp.jan2024g2.model.exceptions.InvalidPostIdException;
import mk.ukim.finki.wp.jan2024g2.repository.PostRepository;
import mk.ukim.finki.wp.jan2024g2.service.PostService;
import mk.ukim.finki.wp.jan2024g2.service.TagService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final TagService tagService;
    private final PostRepository postRepository;

    @Override
    public List<Post> listAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(InvalidPostIdException::new);
    }

    @Override
    public Post create(String title, String content, LocalDate dateCreated, PostType postType, List<Long> tags) {
        List<Tag> postTags = new ArrayList<>();

        for(Long tagId : tags){
            postTags.add(tagService.findById(tagId));
        }

        Post post = new Post(title, content, dateCreated, postType, postTags);
        return postRepository.save(post);
    }

    @Override
    public Post update(Long id, String title, String content, LocalDate dateCreated, PostType postType, List<Long> tags) {
        List<Tag> postTags = new ArrayList<>();

        for(Long tagId : tags){
            postTags.add(tagService.findById(tagId));
        }

        Post post = findById(id);

        post.setTitle(title);
        post.setContent(content);
        post.setDateCreated(dateCreated);
        post.setPostType(postType);
        post.setTags(postTags);

        return postRepository.save(post);
    }

    @Override
    public Post delete(Long id) {
        Post post = findById(id);
        postRepository.delete(post);
        return post;
    }

    @Override
    public Post like(Long id) {
        Post post = findById(id);
        post.setLikes(post.getLikes() + 1);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public List<Post> filterPosts(Long tagId, PostType postType) {
        if(tagId != null && postType != null){
            return listAll()
                    .stream()
                    .filter(post ->
                            post.getTags().stream().anyMatch(p -> p.getId().equals(tagId))
                            && post.getPostType().equals(postType))
                    .toList();
        }

        if(tagId != null){
            return listAll()
                    .stream()
                    .filter(post ->
                            post.getTags().stream().anyMatch(p -> p.getId().equals(tagId)))
                    .toList();
        }

        if(postType != null){
            return listAll()
                    .stream()
                    .filter(post -> post.getPostType().equals(postType))
                    .toList();
        }

        return listAll();
    }
}
