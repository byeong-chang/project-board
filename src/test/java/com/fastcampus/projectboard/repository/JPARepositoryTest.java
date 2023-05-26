package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) //jpaconfig 를 못읽어오기 때문에 improt 필요
@DataJpaTest//slice test
class JPARepositoryTest {

    //필드 주입 Autowired
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JPARepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenselecting_thenWorksFine(){

        //Given
        //When

        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles)
                .isNotNull();
    }
    @DisplayName("insert 테스트")
    @Test
    void givenTestData_wheninserting_thenWorksFine(){

        //Given
        long previousCount = articleRepository.count();

        //When
        Article saveArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));
        //Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount+1);

        System.out.println(previousCount);
    }
    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenupdating_thenWorksFine(){

        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        //When
        Article saveArticle = articleRepository.saveAndFlush(article);

        //Then
        assertThat(saveArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whendeleting_thenWorksFine(){

        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();
        articleRepository.delete(article);
        //Given

        //When

        //Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount-1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount-1);
    }
}