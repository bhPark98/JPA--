package com.fastcampus.ch4;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {

    @Query("SELECT b FROM Board b") // JPQL은 명칭을 대소문자 구분에 주의
    List<Board> findAllBoard(); // 메서드 이름은 아무거나 해도 괜찮음

    @Query("SELECT b FROM Board b WHERE b.title=?1 AND b.writer=?2")
    List<Board> findByTitleAndWriter2(String title, String writer);

    // SELECT COUNT(*) FROM BOARD WHERE WRITER = :writer
    int countAllByWriter(String writer);
    // SELECT * FROM BOARD WHERE WRITER = :writer
    List<Board> findByWriter(String writer);
    // SELECT * FROM BOARD WHERE TITLE = :title AND WRITER = :writer
    List<Board> findByTitleAndWriter(String title, String writer);
    // DELETE FROM BOARD WHERE WRITER = :writer
    @Transactional // delete의 경우, 여러 건을 delete 할 수 있기 때문에 Tx 처리 필수
    int deleteByWriter(String writer);

}
