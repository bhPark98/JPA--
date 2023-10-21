package com.fastcampus.ch4;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

import static com.fastcampus.ch4.QBoard.board;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BoardRepositoryTest4 {

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
    @DisplayName("@Query로 네이티브 쿼리(SQL)작성 테스트 - 페이징과 정렬")
    public void queryAnnoTestPagingAndSort() {

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "view_cnt");
        List<Board> list = boardRepo.findAllBoardBySQL3(pageable);
        list.forEach(System.out::println);
    }

    @Test
    @DisplayName("querydsl로 쿼리 작성 테스트 3 - 동적 쿼리작성")
    public void querydslTest3() {
        String searchBy = "TC";
        String keyword = "1";
        keyword = "%" + keyword + "%";

        BooleanBuilder builder = new BooleanBuilder();

        // 동적으로 조건을 달리하게
        if(searchBy.equalsIgnoreCase("T"))
            builder.and(board.title.like(keyword));
        else if(searchBy.equalsIgnoreCase("C"))
            builder.and(board.content.like(keyword));
        else if(searchBy.equalsIgnoreCase("TC"))
            builder.and(board.title.like(keyword).or(board.content.like(keyword)));

        JPAQueryFactory qf = new JPAQueryFactory(em);
        JPAQuery query = qf.selectFrom(board)
                .where(builder)
                .orderBy(board.upDate.desc());

        List<Board> list = query.fetch();
        list.forEach(System.out::println);
    }

    @Test
    @DisplayName("querydsl로 쿼리 작성 테스트 2 - 복잡한 쿼리작성")
    public void querydslTest2() {
        JPAQueryFactory qf = new JPAQueryFactory(em);

        JPAQuery<Tuple> query = qf.select(board.writer, board.viewCnt.sum()).from(board)
                .where(board.title.notLike("title1%"))
                .where(board.writer.eq("writer1"))
                .where(board.content.contains("content"))
                .where(board.content.isNotNull())
                .groupBy(board.writer)
                .having(board.viewCnt.sum().gt(100)) // 조회수의 총합이 100이 넘는 작성자
                .orderBy(board.writer.asc())
                .orderBy(board.viewCnt.sum().desc());

        List<Tuple> list = query.fetch();
        list.forEach(System.out::println);
    }

    @Test
    @DisplayName("querydsl로 쿼리 작성 테스트 1 - 간단한 쿼리작성")
    public void querydslTest1() {
        QBoard board = QBoard.board;
        // 1. JPAQueryFactory를 생성
        JPAQueryFactory qf = new JPAQueryFactory(em);

        // 2. 쿼리 작성
        JPAQuery<Board> query = qf.selectFrom(board)
                .where(board.title.eq("title1"));
        // 3. 쿼리 실행
        List<Board> list = query.fetch();
        list.forEach(System.out::println);
    }


}