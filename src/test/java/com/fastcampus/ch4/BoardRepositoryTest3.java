package com.fastcampus.ch4;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoardRepositoryTest3 {

    @Autowired
    BoardRepository boardRepo;
    @Autowired
    public EntityManager em;


    @BeforeEach
    public void TestData() {
        for (int i = 1; i <= 100; i++) {
            Board board = new Board();
            board.setBno((long) i);
            board.setTitle("title" + i);
            board.setContent("content" + i);
            board.setWriter("writer" + (i % 5)); // writer0~4
            board.setViewCnt((long) (Math.random() * 100)); // 0~99
            board.setInDate(new Date());
            board.setUpDate(new Date());
            boardRepo.save(board);
        }
    }

    @Test
    @DisplayName("@Query로 JPQL작성 테스트")
    public void queryAnnoTest2() {
        List<Board> list = boardRepo.findByTitleAndWriter2("title1", "writer1");
        assertTrue(list.size() == 100);
    }


    @Test
    @DisplayName("@Query로 JPQL작성 테스트")
    public void queryAnnoTest() {
        List<Board> list = boardRepo.findAllBoard();
        assertTrue(list.size()==100);
    }


    @Test
    @DisplayName("createQuery로 JPQL 작성 테스트")
    public void createQueryTest() {
        String query = "SELECT b FROM Board b";
        TypedQuery<Board> tQuery = em.createQuery(query, Board.class);
        List<Board> list = tQuery.getResultList();

        list.forEach(System.out::println);
        assertTrue(list.size() == 100);
    }
}