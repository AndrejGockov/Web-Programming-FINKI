package mk.ukim.finki.wp.september2021.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.repository.NewsRepository;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsCategoryService newsCategoryService;
    private final NewsRepository newsRepository;

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        News news = new News(name, description, price, type, newsCategoryService.findById(category));
        return newsRepository.save(news);
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        News news = findById(id);

        news.setName(name);
        news.setDescription(description);
        news.setPrice(price);
        news.setType(type);
        news.setCategory(newsCategoryService.findById(category));

        return newsRepository.save(news);
    }

    @Override
    public News delete(Long id) {
        News news = findById(id);
        newsRepository.delete(news);
        return news;
    }

    @Override
    public News like(Long id) {
        News news = findById(id);
        news.setLikes(news.getLikes() + 1);
        return newsRepository.save(news);
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if(price != null && type != null) {
            return listAllNews()
                    .stream()
                    .filter(news -> news.getPrice() < price
                            && news.getType() == type)
                    .toList();
        }

        if(price != null) {
            return listAllNews()
                    .stream()
                    .filter(news -> news.getPrice() < price)
                    .toList();
        }

        if(type != null) {
            return listAllNews()
                    .stream()
                    .filter(news -> news.getType() == type)
                    .toList();
        }

        return listAllNews();
    }
}
