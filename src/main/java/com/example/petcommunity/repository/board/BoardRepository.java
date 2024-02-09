package com.example.petcommunity.repository.board;

import com.example.petcommunity.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
}