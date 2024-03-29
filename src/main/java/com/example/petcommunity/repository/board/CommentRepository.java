package com.example.petcommunity.repository.board;

import com.example.petcommunity.entity.board.BoardEntity;
import com.example.petcommunity.entity.board.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByBoard(BoardEntity board);

    int countByBoard(BoardEntity board);
}
